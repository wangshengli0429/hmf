package com.hmf.web.manager.controller;

import com.hmf.web.entity.TRole;
import com.hmf.web.manager.service.RoleService;
import com.hmf.web.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping("/add/role")
    private ApiResult add(TRole role){
        return roleService.addRole(role);
    }


    @RequestMapping("/role/edit")
    private ApiResult edit(TRole role){
        return roleService.updateRole(role);
    }

    @RequestMapping("/role/del")
    private ApiResult del(Integer id){
        return roleService.delRole(id);
    }

}
