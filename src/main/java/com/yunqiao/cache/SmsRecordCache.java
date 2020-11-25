package com.yunqiao.cache;

import com.yunqiao.db.DBAccessException;
import com.yunqiao.db.accesser.SmsRecordAccessor;
import com.yunqiao.db.model.SmsRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmsRecordCache {
    private Logger logger = LoggerFactory.getLogger(SmsRecordCache.class);

    private static SmsRecordCache instance;

    private Map<String, List<SmsRecord>> keyMap;

    private SmsRecordCache() throws DBAccessException {
        keyMap = new HashMap<>();
        reload();
    }

    public static synchronized SmsRecordCache getInstance() throws DBAccessException {
        if (instance == null) {
            instance = new SmsRecordCache();
        }
        return instance;
    }

    public void reload() throws DBAccessException {
        keyMap.clear();

        List<SmsRecord> list = SmsRecordAccessor.getInstance().getAllSmsRecords();

        list.forEach(record->{
            addIntoCache(record);
        });

        logger.info("SmsRecordCache reloaded. {} record(s) are refreshed.", keyMap.size());
    }

    private void addIntoCache(SmsRecord record) {
        if (keyMap.containsKey(record.getPhoneNumber())) {
            keyMap.get(record.getPhoneNumber()).add(record);
        } else {
            List<SmsRecord> phoneList = new ArrayList<>();
            phoneList.add(record);
            keyMap.put(record.getPhoneNumber(), phoneList);
        }
    }

    public boolean containsKey(String key) {
        return keyMap.containsKey(key);
    }

    public List<SmsRecord> getByKey(String key) {
        return keyMap.get(key);
    }

    public void addSmsRecord(SmsRecord record) throws DBAccessException {
        logger.info("add into SmsRecord cache: {}", record);

        addIntoCache(record);
        SmsRecordAccessor.getInstance().addSmsRecord(record);

    }


}
