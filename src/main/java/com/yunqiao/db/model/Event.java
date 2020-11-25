package com.yunqiao.db.model;

import java.sql.Timestamp;

public class Event {

    private int eventId;
    private int skillId;
    private String title;
    private String skillName;
    private String deviceToken;
    private String deviceName;
    private Timestamp requestTime;
    private Timestamp createTime;
    private int alarm;
    private String detail;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Timestamp getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Timestamp requestTime) {
        this.requestTime = requestTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", skillId=" + skillId +
                ", title='" + title + '\'' +
                ", skillName='" + skillName + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", requestTime=" + requestTime +
                ", createTime=" + createTime +
                ", alarm=" + alarm +
                ", detail='" + detail + '\'' +
                '}';
    }
}
