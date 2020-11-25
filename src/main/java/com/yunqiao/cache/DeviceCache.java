package com.yunqiao.cache;

import com.yunqiao.db.model.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DeviceCache {
    private Logger logger = LoggerFactory.getLogger(DeviceCache.class);

    private static DeviceCache instance;

    private Map<String, Device> keyMap;

    private DeviceCache() {
        keyMap = new HashMap<>();
        reload();
    }

    public static synchronized DeviceCache getInstance()  {
        if (instance == null) {
            instance = new DeviceCache();
        }
        return instance;
    }

    public void reload() {
        int oldNum = keyMap.size();
        keyMap.clear();

        logger.info("DebitLogCache reloaded. {} record(s) are clear.", oldNum);
    }

    public void addDevice(Device device) {
        logger.info("add into Device cache: {}", device);

        keyMap.put(device.getDeviceToken(), device);
    }

    public Map<String, Device> getDeviceMap() {
        return keyMap;
    }

}
