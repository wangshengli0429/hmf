package com.hmf.web.manager.controller;

import com.hmf.web.manager.service.MenuService;
import com.hmf.web.manager.service.RoleMenuService;
import com.hmf.web.manager.service.UserRoleService;
import com.hmf.web.manager.service.UserService;
import com.hmf.web.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/manager")
public class PlatformController {

    private static Logger logger = LoggerFactory.getLogger(PlatformController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private MenuService menuService;


    @RequestMapping("/toLogin")
    public String toLogin(){
        return "/login";
    }

    @RequestMapping("/login")
    public String login(String username, String password, Model model) throws Exception{
        System.out.println("PlatformController.login()");
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.
        if(StringUtil.isEmpty(username)){
            model.addAttribute("msg","账号不能为空");
            return "redirect:/manager/toLogin";
        }
        if(StringUtil.isEmpty(password)){
            model.addAttribute("msg","密码不能为空");
            return "redirect:/manager/toLogin";
        }

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);

            //TODO 操作去首页
            return "success";
        }catch (UnknownAccountException ue){
            System.out.println("UnknownAccountException -- > 账号不存在：");
            model.addAttribute("msg","账号不存在");
            return "redirect:/manager/toLogin";
        }catch (IncorrectCredentialsException ie){
            System.out.println("IncorrectCredentialsException -- > 密码不正确：");
            model.addAttribute("msg","密码不正确");
            return "/manager/login";
        }catch (Exception e){
            logger.info("登陆异常:{0}",e);
            model.addAttribute("msg","系统异常");
            return "/manager/login";
        }
    }

    @RequestMapping("/403")
    public String unauthorizedRole(){
        System.out.println("------没有权限-------");
        return "403";
    }

}
