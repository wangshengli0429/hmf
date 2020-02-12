package com.hmf.web.manager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/test")
public class TestController {
        Logger log = LoggerFactory.getLogger(TestController.class);

        @ResponseBody
        @RequestMapping(value = "/test", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST, RequestMethod.GET})
        public String test(){
            log.info("进入了test方法！");

            return "进入了test方法";
        }

        @RequestMapping(value = "/tHtml", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST, RequestMethod.GET})
        public String testHtml(HttpServletRequest request, HttpServletResponse response){
            log.info("进入了testHtml方法！");
            return "views/NewFile";
        }


        @RequestMapping(value = "/tJsp", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST, RequestMethod.GET})
        public void testJsp( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            log.info("进入了testJsp方法！");
            request.setAttribute("msg","这是一个JSP页面！");
            request.getRequestDispatcher("/WEB-INF/views/test.jsp").forward(request, response);
        }

        @RequestMapping(value = "/entry", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST, RequestMethod.GET})
        public void testEntry( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            log.info("entry进入了后台登陆方法！");
            //TODO 暂时定义

            request.getRequestDispatcher("/WEB-INF/views/adminPC/admin_login.jsp").forward(request, response);
        }

}
