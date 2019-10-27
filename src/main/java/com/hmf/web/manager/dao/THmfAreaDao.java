package com.hmf.web.manager.dao;

import com.hmf.web.entity.THmfArea;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface THmfAreaDao {
    int deleteByPrimaryKey(Integer id);

    int insert(THmfArea record);

    int insertSelective(THmfArea record);

    THmfArea selectByPrimaryKey(Integer id);

    List<THmfArea> selectHmfAreaList(String areaName);


    int updateByPrimaryKeySelective(THmfArea record);

    int updateByPrimaryKey(THmfArea record);
}