package com.yunqiao.cache;

import com.yunqiao.db.DBAccessException;
import com.yunqiao.db.accesser.EventAccessor;
import com.yunqiao.db.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventCache {
    private Logger logger = LoggerFactory.getLogger(EventCache.class);

    private static EventCache instance;

    private Map<String, Event> keyMap;

    private EventCache() throws DBAccessException {
        keyMap = new HashMap<>();
        reload();
    }

    public static synchronized EventCache getInstance() throws DBAccessException {
        if (instance == null) {
            instance = new EventCache();
        }
        return instance;
    }

    public void reload() throws DBAccessException {
        keyMap.clear();

        List<Event> list = EventAccessor.getInstance().getAllEvents();

        list.forEach(event->{
            keyMap.put(String.valueOf(event.getEventId()), event);
        });

        logger.info("EventCache reloaded. {} record(s) are refreshed.", keyMap.size());
    }

    public Event getByKey(String key) {
        return keyMap.get(key);
    }


    public Map<String, Event> getEventMap() {
        return keyMap;
    }

    public void addEvent(Event event) throws DBAccessException {
        logger.info("add into event cache: {}", event);

        EventAccessor.getInstance().addEvent(event);
        keyMap.put(String.valueOf(event.getEventId()), event);
    }


}
