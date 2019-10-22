package com.hmf.web.entity;

public class THmfArea {
    private Integer id;

    private String areaName;

    private String areaCode;

    private String areaDesc;

    private Integer createAtId;

    private String createAtName;

    private String createTime;

    private String updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode == null ? null : areaCode.trim();
    }

    public String getAreaDesc() {
        return areaDesc;
    }

    public void setAreaDesc(String areaDesc) {
        this.areaDesc = areaDesc == null ? null : areaDesc.trim();
    }

    public Integer getCreateAtId() {
        return createAtId;
    }

    public void setCreateAtId(Integer createAtId) {
        this.createAtId = createAtId;
    }

    public String getCreateAtName() {
        return createAtName;
    }

    public void setCreateAtName(String createAtName) {
        this.createAtName = createAtName == null ? null : createAtName.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }
}