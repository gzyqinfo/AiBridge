package com.yunqiao.server;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.yunqiao.baidu.AuthService;
import com.yunqiao.baidu.SKILL;
import com.yunqiao.cache.EventCache;
import com.yunqiao.db.DBAccessException;
import com.yunqiao.db.model.Device;
import com.yunqiao.db.model.Event;
import com.yunqiao.json.object.AiDevice;
import com.yunqiao.sms.SmsManager;
import com.yunqiao.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.*;

public class BridgeTask extends TimerTask {
    private Logger logger = LoggerFactory.getLogger(BridgeTask.class);

    private static String ACCESS_KEY = "t2oxrrYBaOs0RVShSeK2qIUs";
    private static String SECRET_KEY = "kM3P7Bjp1DDVUFGn7KgYGd8mFb07xGVf";

    private static String ACCESS_TOKEN = null;

    private boolean isFirstTimeInit;

    private static String NOTICE_PHONE_NUMBERS = PropertyUtil.readValue("notice.phone.numbers");

    private static Client restClient;
    private static WebResource webResource;

    static {
        ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        restClient = Client.create(config);
    }


    public BridgeTask(boolean isFirstTimeInit) {
        this.isFirstTimeInit = isFirstTimeInit;
    }

    @Override
    public void run() {
        logger.info("Start to execute BridgeTask");

        List<Device> deviceList = getDeviceList();
        logger.info("list size : {}",deviceList.size());

        deviceList.forEach(device -> {
//            getEventList(device.getDeviceToken(), SKILL.id("电子围栏"));
//            getEventList(device.getDeviceToken(), SKILL.STRANGER.id());
            getEventList(device.getDeviceToken(), SKILL.SMOKEFIRE.id());
//            getEventList(device.getDeviceToken(), SKILL.HELMET.id());
        });
        logger.info("finish executing BridgeTask");
    }

    private List<Device> getDeviceList(){
        List<Device> deviceList = new ArrayList<>();
        String url = "https://aip.baidubce.com/rpc/2.0/cvsaas/v1/device/list";
        setAccessToken();

        url += "?access_token=" + ACCESS_TOKEN;

        JSONObject json = new JSONObject();
        json.put("pageNo", 1);

        WebResource webResource = restClient.resource(url);
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,json);
        AiDevice aiDevice = JSONObject.parseObject(response.getEntity(String.class), AiDevice.class);

        if (aiDevice.getError_code() == 0 && aiDevice.getResult().getData() != null) {

            aiDevice.getResult().getData().forEach(data->{
                Device device = new Device();
                device.setDeviceName(data.getDeviceName());
                device.setDeviceToken(data.getDeviceToken());
                device.setDeviceStatus(data.getDeviceStatus());
                device.setRtspAddress(data.getRtspAddress());
                device.setDeviceRemark(data.getRemark());
                device.setUpdateTime(new Timestamp(data.getUpdateTime()));
                deviceList.add(device);
            });
        }

        return deviceList;
    }

    private List<Event> getEventList(String deviceToken, int skillId){
        List<Event> eventList = new ArrayList<>();
        String url = "https://aip.baidubce.com/rpc/2.0/cvsaas/v1/event/list";
        setAccessToken();
        url += "?access_token=" + ACCESS_TOKEN;

        JSONObject json = new JSONObject();

        json.put("deviceToken", deviceToken);
        json.put("skillId", skillId);

        long currentTime = System.currentTimeMillis();
//        json.put("startTime", currentTime-1000l*60);
        json.put("endTime", currentTime);

        WebResource webResource = restClient.resource(url);
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,json);

        JSONObject result = JSONObject.parseObject(response.getEntity(String.class));

        if (result.getIntValue("error_code") == 0) {
            JSONObject resultObject = result.getJSONObject("result");
            JSONArray dataArray = resultObject.getJSONArray("data");
            String eventDetailUrl = "https://aip.baidubce.com/rpc/2.0/cvsaas/v1/event/detail";
            eventDetailUrl += "?access_token=" + ACCESS_TOKEN;

            for (int i=0; i<dataArray.size(); i++) {
                JSONObject data = (JSONObject) dataArray.get(i);

                Event event = new Event();
                event.setDetail(result.toString());
                event.setCreateTime(new Timestamp(data.getLong("createTime")));
                event.setRequestTime(new Timestamp(data.getLong("requestTime")));
                event.setDeviceName(data.getString("deviceName"));
                event.setDeviceToken(data.getString("deviceToken"));
                event.setSkillName(data.getString("skillName"));
                event.setTitle(data.getString("title"));
                event.setSkillId(data.getIntValue("skillId"));
                event.setEventId(data.getIntValue("eventId"));

                try {
                    if (!EventCache.getInstance().getEventMap().containsKey(String.valueOf(event.getEventId()))) {
                        JSONObject detailRequest = new JSONObject();
                        detailRequest.put("eventId", data.getString("eventId"));

                        webResource = restClient.resource(eventDetailUrl);
                        JSONObject detailResponse = JSONObject.parseObject(webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,detailRequest).getEntity(String.class));

                        event.setDetail(detailResponse.toString());
                        if (detailResponse.getIntValue("error_code") == 0) {
                            JSONObject detailResult = detailResponse.getJSONObject("result").getJSONObject("skillOutput").getJSONObject("result");
                            String smokeOrFire = null;
                            if (skillId == SKILL.TRESPASSER.id()) {
                                logger.info("noInface: {}", detailResult.getString("notInFaceSetUserNum"));
                                event.setAlarm(detailResult.getIntValue("notInFaceSetUserNum"));
                            } else if (skillId == SKILL.STRANGER.id())  {
                                logger.info("strangerNum: {}", detailResult.getString("strangerNum"));
                                event.setAlarm(detailResult.getIntValue("strangerNum"));
                            } else if (skillId == SKILL.HELMET.id())  {
                                logger.info("person_no_helmet_num: {}", detailResult.getString("person_no_helmet_num"));
                                event.setAlarm(detailResult.getIntValue("person_no_helmet_num"));
                            } else if (skillId == SKILL.SMOKEFIRE.id())  {
                                logger.info("smokeNum: {}", detailResult.getJSONObject("data").getString("smokeNum"));
                                logger.info("fireNum: {}", detailResult.getJSONObject("data").getString("fireNum"));
                                event.setAlarm(0);
                                if (detailResult.getJSONObject("data").getIntValue("smokeNum") > 0) {
                                    event.setAlarm(1);      // only smoke the alarm value is 1
                                    smokeOrFire = "白烟";
                                }
                                if (detailResult.getJSONObject("data").getIntValue("fireNum") > 0) {
                                    event.setAlarm(2);      // in case fire the alarm value is 2
                                    smokeOrFire = "火焰";
                                }
                            }
                            if (event.getAlarm() > 0 && !isFirstTimeInit) {
                                logger.warn("SEND OUT SMS...............................");
                                SmsManager.getInstance().sendSms(NOTICE_PHONE_NUMBERS, skillId, event.getDeviceName(), event.getCreateTime().getTime(), smokeOrFire);
                            }
                        } else {
                            logger.error("get detail ERROR: code == {}", detailResponse.getIntValue("error_code"));
                        }
                        EventCache.getInstance().addEvent(event);
                    }
                } catch (DBAccessException e) {
                    logger.error("failed to add event : {}", event);
                    logger.error(e.getMessage());
                }
            }

        } else {
            logger.error("get event ERROR: code == {}", result.getIntValue("error_code"));
        }

        return eventList;
    }

    private void setAccessToken() {
        if (ACCESS_TOKEN == null) { //TODO : cached access_token
            ACCESS_TOKEN = AuthService.getAuth(ACCESS_KEY, SECRET_KEY);
            logger.info("got token: {}", ACCESS_TOKEN);
        }
    }
}
