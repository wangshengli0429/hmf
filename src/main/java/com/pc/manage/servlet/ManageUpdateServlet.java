package com.pc.manage.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pc.manage.dao.ManageUpdateDaoImpl;
/**
 * 管理PC端-修改密码
 */
@WebServlet(name = "ManageUpdateServlet", urlPatterns = "/servlet/ManageUpdateServlet")  //标记为servlet，以便启动器扫描。
public class ManageUpdateServlet extends HttpServlet {

	public ManageUpdateServlet() {
		// TODO Auto-generated constructor stub
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
		String flag = request.getParameter("do");
		String path = request.getContextPath();
		
		String sId = "";
		String sPw = "";
		
		if (flag.equals("1")) {
			Cookie[] cookies = request.getCookies();
			if (cookies != null && cookies.length > 0) {
				for (Cookie cookie : cookies) {
					if ("adminId".equals(cookie.getName())) {
						sId = cookie.getValue();
					}
					if ("adminPw".equals(cookie.getName())) {
						sPw = cookie.getValue();
					}
					
				}
			}

			if (sId == null || sId.equals("") || sPw == null || sPw.equals("")) {
				request.getRequestDispatcher("/WEB-INF/views/adminPC/admin_login.jsp")
						.forward(request, response);
			}
			
			request.setAttribute("id", sId);
			request.setAttribute("pw", sPw);
			
			request.getRequestDispatcher("/WEB-INF/views/adminPC/update_password.jsp").forward(request, response);
		}
		
		if (flag.equals("2")) {
			String sid=request.getParameter("id");
			String oldPw=request.getParameter("oldPw");
			String newPw=request.getParameter("newPw");
			int id = Integer.parseInt(sid);
			ManageUpdateDaoImpl login = new ManageUpdateDaoImpl();
			int i = login.updatePw(oldPw, newPw ,id);
			if(i!=0){
				HttpSession session = request.getSession();
				session.removeAttribute("id");
				session.removeAttribute("userName");
				session.removeAttribute("pw");
				Cookie[] cookies = request.getCookies();
				for (Cookie cookie : cookies) {
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
				response.sendRedirect(path + "/adminPC/admin_login.jsp");
			}
		}
	}
	
	public void init() throws ServletException {
		// Put your code here
	}

}
