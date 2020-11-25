package com.yunqiao.server;


import com.yunqiao.cache.DeviceCache;
import com.yunqiao.cache.EventCache;
import com.yunqiao.cache.SmsRecordCache;
import com.yunqiao.db.DBAccessException;
import com.yunqiao.rest.RestApplication;
import com.yunqiao.util.PropertyUtil;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Timer;

public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> logger.info("Application stopped.")));

        try {
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            String hostName = InetAddress.getLocalHost().getHostName();
            String canonicalHostName = InetAddress.getLocalHost().getCanonicalHostName();
            logger.info("current IP address : {}/{}/{}", ipAddress, hostName, canonicalHostName);
        } catch (UnknownHostException e) {
            logger.error("Cannot get IP address.");
            e.printStackTrace();
        }

        String restHostName = "http://localhost:";
        int restPort = Integer.valueOf(PropertyUtil.readValue("app.port"));
        String restServiceRootPath = "/";
        String restUri = restHostName+String.valueOf(restPort)+restServiceRootPath;
        JettyHttpContainerFactory.createServer(URI.create(restUri), new RestApplication());
        logger.info("RESTful Server started at : " + restUri);
        logger.info("AiBridge started...");

        //Cache init

        try {
            DeviceCache.getInstance();
            EventCache.getInstance();
            SmsRecordCache.getInstance();

        } catch (DBAccessException e) {
            e.printStackTrace();
            logger.error("Error while init system cache , {}", e.getMessage());
        }

        Timer timer = new Timer();

        timer.schedule(new BridgeTask(true), 0);
        timer.schedule(new BridgeTask(false), 1000L*30, 1000L * 30);

        logger.info("BridgeTask started.");
    }

}
