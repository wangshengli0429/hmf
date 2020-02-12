package com.pc.teacher.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {
	public LogoutServlet() {
		super();
	}
	
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String path = request.getContextPath();
		
		Cookie killMyCookie = new Cookie("teacherPw", null);
		killMyCookie.setMaxAge(0);
		killMyCookie.setPath("/");
		response.addCookie(killMyCookie);
		Cookie killMyCookie1 = new Cookie("teacherId", null);
		killMyCookie1.setMaxAge(0);
		killMyCookie1.setPath("/");
		response.addCookie(killMyCookie1);
		
		HttpSession session=request.getSession(false);
        if(session==null){
            response.sendRedirect(path+"/teacherPC/teacher_login.jsp");
            return;
        }
        session.removeAttribute("tuserName");
        
		response.setHeader("Pragma", "No-cache");  
		response.setHeader("Cache-Control", "no-cache");  
		response.setDateHeader("Expires", 0);  
		
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
		
        response.sendRedirect(path+"/teacherPC/teacher_login.jsp");
		
	}
	public void init() throws ServletException {
		// Put your code here
	}
}
