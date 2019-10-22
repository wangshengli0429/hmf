package com.hmf.web.manager.dao;

import com.hmf.web.entity.THmfArea;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface THmfAreaDao {
    int deleteByPrimaryKey(Integer id);

    int insert(THmfArea record);

    int insertSelective(THmfArea record);

    THmfArea selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(THmfArea record);

    int updateByPrimaryKey(THmfArea record);
}