package com.hmf.web.manager.controller;

import com.hmf.web.manager.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping("/add/role")
    public String addRole(){

        return null;
    }

}
