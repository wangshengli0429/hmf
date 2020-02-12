package com.hmf.web.manager.controller;

import com.hmf.web.manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/manager")
public class PlatformController {

    private static Logger logger = LoggerFactory.getLogger(PlatformController.class);

    @Autowired
    private UserService userService;


    @RequestMapping("/test")//测试跳转JSP
    @ResponseBody
    public String test(){
        return "/test";
    }
    //打开所有html页面
//    @RequestMapping("/{view}")
//    public String html(@PathVariable("view")String view){
//        return view;
//    }//其中view就是你要打开的视图哦

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "/login";
    }



    @RequestMapping("/403")
    public String unauthorizedRole(){
        System.out.println("------没有权限-------");
        return "403";
    }

}
