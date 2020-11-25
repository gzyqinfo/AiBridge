package com.yunqiao.db.model;

import java.sql.Timestamp;

public class Device {

    private String deviceName;
    private String deviceToken;
    private int deviceStatus;
    private String deviceRemark;
    private String rtspAddress;
    private Timestamp updateTime;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public int getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getDeviceRemark() {
        return deviceRemark;
    }

    public void setDeviceRemark(String deviceRemark) {
        this.deviceRemark = deviceRemark;
    }

    public String getRtspAddress() {
        return rtspAddress;
    }

    public void setRtspAddress(String rtspAddress) {
        this.rtspAddress = rtspAddress;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceName='" + deviceName + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                ", deviceStatus=" + deviceStatus +
                ", deviceRemark='" + deviceRemark + '\'' +
                ", rtspAddress='" + rtspAddress + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
