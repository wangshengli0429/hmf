package com.hmf.web.config;

import com.hmf.web.manager.vo.CurrentLoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SysCurrentUserInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(SysCurrentUserInterceptor.class);
    /**
     * 进入拦截器后首先进入的方法
     * 返回false则不再继续执行
     * 返回true则继续执行
     */
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)throws Exception
    {
        logger.info("-----------拦截器判断crm用户是否登录");
        HttpSession session=request.getSession();
        CurrentLoginUser userInfo = (CurrentLoginUser)session.getAttribute("currentUser");
        if(userInfo==null)
        {
            logger.info("-----------拦截器判断crm用户没有登录,跳到登录页");
            //TODO 后台登录页
//            response.sendRedirect(request.getContextPath()+"/login.html");
            response.sendRedirect("www.baidu.com");
            return false;
        }
        logger.info("-----------拦截器判断当前用户已经登录");
        return  true;
    }
//    /**
//     * 生成视图时执行，可以用来处理异常，并记录在日志中
//     */
//    public void afterCompletion(HttpServletRequest request,
//                                HttpServletResponse response,
//                                Object arg2, Exception exception){
//        //-----------------//
//    }
//
//    /** -
//     * 生成视图之前执行，可以修改ModelAndView
//     */
//    public void postHandle(HttpServletRequest request,
//                           HttpServletResponse response,
//                           Object arg2, ModelAndView arg3)
//            throws Exception{
//        //----------------------------//
//    }
}
