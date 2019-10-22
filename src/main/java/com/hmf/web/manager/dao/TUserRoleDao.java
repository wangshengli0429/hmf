package com.hmf.web.manager.dao;

import com.hmf.web.entity.TUserRole;

public interface TUserRoleDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TUserRole record);

    int insertSelective(TUserRole record);

    TUserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TUserRole record);

    int updateByPrimaryKey(TUserRole record);
}