package com.hmf.web.manager.dao;

import com.hmf.web.entity.TFunction;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TFunctionDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TFunction record);

    int insertSelective(TFunction record);

    TFunction selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TFunction record);

    int updateByPrimaryKey(TFunction record);
}