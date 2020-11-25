package com.yunqiao.db.accesser;

import com.yunqiao.db.ConnectionPool;
import com.yunqiao.db.DBAccessException;
import com.yunqiao.db.SqlHelper;
import com.yunqiao.db.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventAccessor {
    private Logger logger = LoggerFactory.getLogger(EventAccessor.class);

    private static EventAccessor instance;

    private EventAccessor() {
    }

    public static synchronized EventAccessor getInstance() {
        if (instance == null) {
            instance = new EventAccessor();
        }
        return instance;
    }

    public List<Event> getAllEvents() throws DBAccessException {
        logger.info("Get all Events request");
        Connection connection = ConnectionPool.getConnection();
        try {
            ResultSet rs = SqlHelper.executeQuery (connection, "select * from event");
            List<Event> list = new ArrayList<>();
            while(rs.next()){
                Event event = new Event();
                event.setEventId(rs.getInt("event_id"));
                event.setSkillId(rs.getInt("skill_id"));
                event.setTitle(rs.getString("title"));
                event.setSkillName(rs.getString("skill_name"));
                event.setDeviceToken(rs.getString("device_token"));
                event.setDeviceName(rs.getString("device_name"));
                event.setRequestTime(rs.getTimestamp("request_time"));
                event.setCreateTime(rs.getTimestamp("create_time"));
                event.setAlarm(rs.getInt("alarm"));
                event.setDetail(rs.getString("detail"));
                list.add(event);
            }
            connection.close();
            logger.info("returned {} row(s) data", list.size());
            return list;
        } catch (SQLException e) {
            throw new DBAccessException(connection, e);
        }
    }

    public void addEvent(Event event) throws DBAccessException {
        logger.info("Received add event data request. message type: {}", event.toString());

        String sql = "insert into event(event_id, skill_id, title, skill_name,device_token, device_name, request_time, create_time, alarm, detail) " +
                "values (?,?,?,?,?,?,?,?,?,?)";
        Connection connection = ConnectionPool.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, event.getEventId());
            preparedStatement.setInt(2, event.getSkillId());
            preparedStatement.setString(3, event.getTitle());
            preparedStatement.setString(4, event.getSkillName());
            preparedStatement.setString(5, event.getDeviceToken());
            preparedStatement.setString(6, event.getDeviceName());
            preparedStatement.setTimestamp(7, event.getRequestTime());
            preparedStatement.setTimestamp(8, event.getCreateTime());
            preparedStatement.setInt(9, event.getAlarm());
            preparedStatement.setString(10, event.getDetail());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new DBAccessException(connection, e);
        }
        logger.info("inserted Event record");
    }

}
