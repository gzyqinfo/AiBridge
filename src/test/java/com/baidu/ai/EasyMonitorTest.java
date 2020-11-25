package com.baidu.ai;

import com.alibaba.fastjson.JSONObject;
import com.yunqiao.baidu.AuthService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.yunqiao.sms.SmsManager;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EasyMonitorTest {
    private static Logger logger = LoggerFactory.getLogger(EasyMonitorTest.class);

    private String ACCESS_KEY = "t2oxrrYBaOs0RVShSeK2qIUs";
    private String SECRET_KEY = "kM3P7Bjp1DDVUFGn7KgYGd8mFb07xGVf";

    private static String ACCESS_TOKEN = null;

    @Test
    public void testListDevice(){
        String url = "https://aip.baidubce.com/rpc/2.0/cvsaas/v1/device/list";
        if (ACCESS_TOKEN == null) { //TODO : cached access_token
            ACCESS_TOKEN = AuthService.getAuth(ACCESS_KEY, SECRET_KEY);
            logger.info("got token: {}", ACCESS_TOKEN);
        }

        url += "?access_token=" + ACCESS_TOKEN;

        JSONObject json = new JSONObject();

        json.put("pageNo", 1);

        Client restClient;
        ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        restClient = Client.create(config);

        WebResource webResource = restClient.resource(url);
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,json);
        logger.info(response.getEntity(String.class));

    }

    @Test
    public void testListEvent() {
        String url = "https://aip.baidubce.com/rpc/2.0/cvsaas/v1/event/list";
        if (ACCESS_TOKEN == null) { //TODO : cached access_token
            ACCESS_TOKEN = AuthService.getAuth(ACCESS_KEY, SECRET_KEY);
            logger.info("got token: {}", ACCESS_TOKEN);
        }

        url += "?access_token=" + ACCESS_TOKEN;


        JSONObject json = new JSONObject();

        json.put("deviceToken", "PYLU-CMKM-BD98-GYQH");
        json.put("skillId", 18);

        long currentTime = System.currentTimeMillis();
//        json.put("startTime", currentTime-1576039837000l);
        json.put("endTime", currentTime);

        Client restClient;
        ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        restClient = Client.create(config);

        WebResource webResource = restClient.resource(url);
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,json);
        logger.info(response.getEntity(String.class));

    }

    @Test
    public void testEventDetail() {
        String url = "https://aip.baidubce.com/rpc/2.0/cvsaas/v1/event/detail";
        if (ACCESS_TOKEN == null) { //TODO : cached access_token
            ACCESS_TOKEN = AuthService.getAuth(ACCESS_KEY, SECRET_KEY);
            logger.info("got token: {}", ACCESS_TOKEN);
        }

        url += "?access_token=" + ACCESS_TOKEN;

        JSONObject json = new JSONObject();

        json.put("eventId", "14416841");

        long currentTime = System.currentTimeMillis();
//        json.put("startTime", currentTime-1576039837000l);
//        json.put("endTime", currentTime);

        Client restClient;
        ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        restClient = Client.create(config);

        WebResource webResource = restClient.resource(url);
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,json);
        logger.info(response.getEntity(String.class));

        System.out.println(System.currentTimeMillis());
    }

    @Test
    public void testSendingSMS() throws InterruptedException {

//        SmsManager.getInstance().sendSms("13602499238", 19,"天网2号", System.currentTimeMillis(), "白烟");
        Thread.sleep(20000);
    }
}

