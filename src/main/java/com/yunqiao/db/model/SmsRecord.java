package com.yunqiao.db.model;

import java.sql.Timestamp;

public class SmsRecord {

    private String phoneNumber;
    private int skillId;
    private Timestamp sendTime;

    public SmsRecord(String phoneNumber, int skillId, Timestamp sendTime) {
        this.phoneNumber = phoneNumber;
        this.skillId = skillId;
        this.sendTime = sendTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "SmsRecord{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", skillId=" + skillId +
                ", sendTime=" + sendTime +
                '}';
    }
}
