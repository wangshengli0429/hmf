package com.hmf.web.manager.service;

import com.hmf.web.entity.TRole;
import com.hmf.web.utils.ApiResult;

public interface RoleService {

    //角色新增
    ApiResult addRole(TRole role);

    ApiResult delRole(Integer id);

    ApiResult getRoleById(Integer id);

    ApiResult updateRole(TRole record);

}
