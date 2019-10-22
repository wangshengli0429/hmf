package com.hmf.web.manager.dao;

import com.hmf.web.entity.TMenu;

public interface TMenuDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TMenu record);

    int insertSelective(TMenu record);

    TMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TMenu record);

    int updateByPrimaryKey(TMenu record);
}