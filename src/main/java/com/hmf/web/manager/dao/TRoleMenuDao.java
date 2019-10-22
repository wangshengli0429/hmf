package com.hmf.web.manager.dao;

import com.hmf.web.entity.TRoleMenu;

public interface TRoleMenuDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TRoleMenu record);

    int insertSelective(TRoleMenu record);

    TRoleMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TRoleMenu record);

    int updateByPrimaryKey(TRoleMenu record);
}