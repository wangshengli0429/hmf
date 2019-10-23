package com.hmf.web.manager.dao;

import com.hmf.web.entity.TUserRole;
import com.hmf.web.manager.bo.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TUserRoleDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TUserRole record);

    int insertSelective(TUserRole record);

    TUserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TUserRole record);

    int updateByPrimaryKey(TUserRole record);

    List<SysRole> selectUserRoleByUserId(Integer userId);
}