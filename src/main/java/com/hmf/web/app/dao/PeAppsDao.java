package com.hmf.web.app.dao;

import com.hmf.web.entity.PeApps;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PeAppsDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PeApps record);

    int insertSelective(PeApps record);

    PeApps selectByPrimaryKey(Integer id);

    PeApps selectByAppId(Integer appId);

    int updateByPrimaryKeySelective(PeApps record);

    int updateByPrimaryKey(PeApps record);
}