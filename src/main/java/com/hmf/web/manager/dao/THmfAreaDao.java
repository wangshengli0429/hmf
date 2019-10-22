package com.hmf.web.manager.dao;

import com.hmf.web.entity.THmfArea;

public interface THmfAreaDao {
    int deleteByPrimaryKey(Integer id);

    int insert(THmfArea record);

    int insertSelective(THmfArea record);

    THmfArea selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(THmfArea record);

    int updateByPrimaryKey(THmfArea record);
}