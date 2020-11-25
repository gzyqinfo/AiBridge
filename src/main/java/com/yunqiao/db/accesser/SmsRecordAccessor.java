package com.yunqiao.db.accesser;

import com.yunqiao.db.ConnectionPool;
import com.yunqiao.db.DBAccessException;
import com.yunqiao.db.SqlHelper;
import com.yunqiao.db.model.SmsRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SmsRecordAccessor {
    private Logger logger = LoggerFactory.getLogger(SmsRecordAccessor.class);

    private static SmsRecordAccessor instance;

    private SmsRecordAccessor() {
    }

    public static synchronized SmsRecordAccessor getInstance() {
        if (instance == null) {
            instance = new SmsRecordAccessor();
        }
        return instance;
    }

    public List<SmsRecord> getAllSmsRecords() throws DBAccessException {
        logger.info("Get all SmsRecords request");
        Connection connection = ConnectionPool.getConnection();
        try {
            ResultSet rs = SqlHelper.executeQuery (connection, "select * from sms_record ");
            List<SmsRecord> list = new ArrayList<>();
            while(rs.next()){
                SmsRecord record = new SmsRecord(rs.getString("phone_number"),
                                         rs.getInt("skill_id"),
                                         rs.getTimestamp("send_time")  );

                list.add(record);
            }
            connection.close();
            logger.info("returned {} row(s) data", list.size());
            return list;
        } catch (SQLException e) {
            throw new DBAccessException(connection, e);
        }
    }

    public void addSmsRecord(SmsRecord record) throws DBAccessException {
        logger.info("Received add sms_record data request. message type: {}", record.toString());

        String sql = "insert into sms_record(phone_number, skill_id, send_time) " +
                "values (?,?,?)";
        Connection connection = ConnectionPool.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, record.getPhoneNumber());
            preparedStatement.setInt(2, record.getSkillId());
            preparedStatement.setTimestamp(3, record.getSendTime());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new DBAccessException(connection, e);
        }
        logger.info("inserted SmsRecord record");
    }

}
