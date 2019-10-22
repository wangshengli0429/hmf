package com.hmf.web.config;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("token");
        if (authHeader == null || authHeader.equals("")) {
//            response.sendRedirect("/test/index/hello");
            throw new ServletException("invalid Authorization header 没有token");
        }
        return true;
//        //取得token
//        String token = authHeader.substring(7);
//        try {
//            JwtUtil.checkToken(token);
//            return true;
//        } catch (Exception e) {
//            throw new ServletException(e.getMessage());
//        }
    }

}
