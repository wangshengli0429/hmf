package com.hmf.web.entity;

public class THmfShop {
    private Integer id;

    private String shopName;

    private String shopCode;

    private String shopDesc;

    private String shopAddress;

    private Integer largeArea;

    private String respectiveRegion;

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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    public String getShopDesc() {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc == null ? null : shopDesc.trim();
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress == null ? null : shopAddress.trim();
    }

    public Integer getLargeArea() {
        return largeArea;
    }

    public void setLargeArea(Integer largeArea) {
        this.largeArea = largeArea;
    }

    public String getRespectiveRegion() {
        return respectiveRegion;
    }

    public void setRespectiveRegion(String respectiveRegion) {
        this.respectiveRegion = respectiveRegion == null ? null : respectiveRegion.trim();
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