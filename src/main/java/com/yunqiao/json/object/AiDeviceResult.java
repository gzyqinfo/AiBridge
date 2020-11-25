package com.yunqiao.json.object;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class AiDeviceResult {

    private int pageNo;
    private int pageSize;
    private int totalCount;
    private int totalPage;
    private List<AiDeviceData> data;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<AiDeviceData> getData() {
        return data;
    }

    public void setData(List<AiDeviceData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
