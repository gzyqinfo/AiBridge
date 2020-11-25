package com.yunqiao.sms;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.yunqiao.baidu.SKILL;
import com.yunqiao.cache.SmsRecordCache;
import com.yunqiao.db.DBAccessException;
import com.yunqiao.db.model.SmsRecord;
import com.yunqiao.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SmsManager {
    private Logger logger = LoggerFactory.getLogger(SmsManager.class);
    private static SmsManager instance;


    // Access Key (在阿里云访问控制台寻找)
    private static final String accessKeyId = "LTAI4GJbcjWNw45ADa9L8HTF";
    private static final String accessKeySecret = "uABTchYXv0aRHwCdU6f1DbXU1Q9jhG";

    private SmsManager () {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
    }

    public static synchronized SmsManager getInstance() {
        if (instance == null) {
            instance = new SmsManager();
        }
        return instance;
    }


    public void sendSms(String numbers, int skillId, String deviceName, long timeStamp) {
        sendSms(numbers, skillId, deviceName, timeStamp, null);
    }

    public void sendSms(String numbers, int skillId, String deviceName, long timeStamp, String smokeOrFire) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
        String moment = dateFormat.format(new Date(timeStamp));

        Runnable runnable = () -> {
            logger.info("Start to send message to {}", numbers);
            String checkedNumbers = limitCheck(numbers, skillId);
            if (!checkedNumbers.equals("")) {

                DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
                IAcsClient client = new DefaultAcsClient(profile);

                CommonRequest request = new CommonRequest();
                request.setSysMethod(MethodType.POST);
                request.setSysDomain("dysmsapi.aliyuncs.com");
                request.setSysVersion("2017-05-25");
                request.setSysAction("SendSms");


                request.putQueryParameter("RegionId", "cn-hangzhou");
                request.putQueryParameter("PhoneNumbers", checkedNumbers);
                request.putQueryParameter("SignName", "广州云桥");

                if (skillId == SKILL.SMOKEFIRE.id()) {
                    request.putQueryParameter("TemplateCode", "SMS_205810916");
                    request.putQueryParameter("TemplateParam", "{\"device\":\""+deviceName+"\", \"time\":\""+moment+"\", \"smokeOrFire\":\""+smokeOrFire+"\"}");
                } else if (skillId == SKILL.HELMET.id()) {
                    request.putQueryParameter("TemplateCode", "SMS_205810926");
                    request.putQueryParameter("TemplateParam", "{\"device\":\""+deviceName+"\", \"time\":\""+moment+"\"}");
                } else {
                    request.putQueryParameter("TemplateCode", "SMS_205461959");
                    request.putQueryParameter("TemplateParam", "{\"device\":\""+deviceName+"\", \"time\":\""+moment+"\"}");
                }
                try {
                    CommonResponse response = client.getCommonResponse(request);
                    logger.info(response.getData());

                } catch (ServerException e) {
                    e.printStackTrace();
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }
        };

        logger.info("Submit the task specified by the runnable to the executor service.");
        executorService.submit(runnable);
    }

    private String limitCheck(String numbers, int skillId) {
        String checkedNumbers = "";
        String[] numberList = numbers.split(",");
        for (int i=0; i<numberList.length; i++) {
            try {
                if (SmsRecordCache.getInstance().containsKey(numberList[i])) {
                    if (minuteCheck(numberList[i]) && hourCheck(numberList[i]) && dayCheck(numberList[i])) {
                        checkedNumbers = appendCheckedNumber(checkedNumbers, numberList, i);
                        SmsRecordCache.getInstance().addSmsRecord(new SmsRecord(numberList[i], skillId, new Timestamp(System.currentTimeMillis())));
                    }
                } else {
                    checkedNumbers = appendCheckedNumber(checkedNumbers, numberList, i);
                    SmsRecordCache.getInstance().addSmsRecord(new SmsRecord(numberList[i], skillId, new Timestamp(System.currentTimeMillis())));
                }
            } catch (DBAccessException e) {
                e.printStackTrace();
            }
        }

        return checkedNumbers;
    }

    private String appendCheckedNumber(String checkedNumbers, String[] numberList, int i) {
        if (i == 0) {
            checkedNumbers = checkedNumbers + numberList[i];
        } else {
            checkedNumbers = checkedNumbers + "," + numberList[i];
        }
        return checkedNumbers;
    }

    private boolean minuteCheck(String phoneNumber) {
        int minuteCheckLimit  = Integer.valueOf(PropertyUtil.readValue("sms.sending.check.minute"));
        try {
            if (SmsRecordCache.getInstance().containsKey(phoneNumber)) {
                List<SmsRecord> phoneList = SmsRecordCache.getInstance().getByKey(phoneNumber);
                int index = phoneList.size() - minuteCheckLimit;
                if (index >= 0) {
                    long diffTime = System.currentTimeMillis() - phoneList.get(index).getSendTime().getTime();
                    if (diffTime < 1000l * 60 * 1) {
                        logger.warn("Phone {} failed in minute check, won't be sent SMS again", phoneNumber);
                        return false;
                    }
                }
            }
        } catch (DBAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean hourCheck(String phoneNumber) {
        int minuteCheckLimit  = Integer.valueOf(PropertyUtil.readValue("sms.sending.check.hour"));
        try {
            if (SmsRecordCache.getInstance().containsKey(phoneNumber)) {
                List<SmsRecord> phoneList = SmsRecordCache.getInstance().getByKey(phoneNumber);
                int index = phoneList.size() - minuteCheckLimit;
                if (index >= 0) {
                    long diffTime = System.currentTimeMillis() - phoneList.get(index).getSendTime().getTime();
                    if (diffTime < 1000l * 60 * 60) {
                        logger.warn("Phone {} failed in hour check, won't be sent SMS again", phoneNumber);
                        return false;
                    }
                }
            }
        } catch (DBAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean dayCheck(String phoneNumber) {
        int minuteCheckLimit  = Integer.valueOf(PropertyUtil.readValue("sms.sending.check.day"));
        try {
            if (SmsRecordCache.getInstance().containsKey(phoneNumber)) {
                List<SmsRecord> phoneList = SmsRecordCache.getInstance().getByKey(phoneNumber);
                int index = phoneList.size() - minuteCheckLimit;
                if (index >= 0) {
                    long diffTime = System.currentTimeMillis() - phoneList.get(index).getSendTime().getTime();
                    if (diffTime < 1000l * 60 * 60 * 24) {
                        logger.warn("Phone {} failed in day check, won't be sent SMS again", phoneNumber);
                        return false;
                    }
                }
            }
        } catch (DBAccessException e) {
            e.printStackTrace();
        }
        return true;
    }
}
