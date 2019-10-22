package com.hmf.web.manager.dao;

import com.hmf.web.entity.THmfShop;

public interface THmfShopDao {
    int deleteByPrimaryKey(Integer id);

    int insert(THmfShop record);

    int insertSelective(THmfShop record);

    THmfShop selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(THmfShop record);

    int updateByPrimaryKey(THmfShop record);
}