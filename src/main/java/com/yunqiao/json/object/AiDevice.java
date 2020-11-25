package com.yunqiao.json.object;

import com.alibaba.fastjson.JSONObject;

public class AiDevice {
    private long logId;
    private int error_code;
    private String error_msg;
    private String trace_id;
    private AiDeviceResult result;

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public AiDeviceResult getResult() {
        return result;
    }

    public void setResult(AiDeviceResult result) {
        this.result = result;
    }

    public String getTrace_id() {
        return trace_id;
    }

    public void setTrace_id(String trace_id) {
        this.trace_id = trace_id;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
