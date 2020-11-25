package com.yunqiao.json.object;

import com.alibaba.fastjson.JSONObject;

public class AiDeviceData {
    private String rtspAddress;
    private String deviceName;
    private String deviceToken;
    private int deviceStatus;
    private long updateTime;
    private String remark;

    public String getRtspAddress() {
        return rtspAddress;
    }

    public void setRtspAddress(String rtspAddress) {
        this.rtspAddress = rtspAddress;
    }

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

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
