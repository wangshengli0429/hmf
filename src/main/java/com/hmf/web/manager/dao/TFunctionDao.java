package com.hmf.web.manager.dao;

import com.hmf.web.entity.TFunction;

public interface TFunctionDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TFunction record);

    int insertSelective(TFunction record);

    TFunction selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TFunction record);

    int updateByPrimaryKey(TFunction record);
}