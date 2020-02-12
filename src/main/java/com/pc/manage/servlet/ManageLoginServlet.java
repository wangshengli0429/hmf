package com.pc.manage.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.entity.ManageUsers;
import com.pc.manage.dao.ManageLoginDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 管理PC端-登录
 */
@WebServlet(name = "ManageLoginServlet", urlPatterns = "/servlet/ManageLoginServlet")  //标记为servlet，以便启动器扫描。
public class ManageLoginServlet extends HttpServlet{
	private static Logger logger = LoggerFactory.getLogger(ManageLoginServlet.class);

	public ManageLoginServlet() {
		// TODO Auto-generated constructor stub
	}
	
	public void destroy() {
		logger.info("-------------- ManageLoginServlet destroy ---------- ");
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("-------------- ManageLoginServlet doGet ---------- ");
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("-------------- ManageLoginServlet doPost ---------- ");

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		HttpSession httpsession = request.getSession();
		httpsession.invalidate();
		String path = request.getContextPath();
		String uname=request.getParameter("username");
		String pw=request.getParameter("pwd");
		String flag=request.getParameter("do");
		if (flag.equals("1")) {
			ManageLoginDaoImpl login = new ManageLoginDaoImpl();
			int i = login.loginName(uname);
			if(i==1){
				ManageUsers m = login.checkLogin(uname, pw);
				if(m!=null){
					request.getSession().setAttribute("userName", m.getUsername());
					
					String encode = URLEncoder.encode(m.getUsername(), "utf-8");
					
					Cookie userNameCookie = new Cookie("adminUserName", encode);
					Cookie pwCookie = new Cookie("adminPw", m.getPassword());
					Cookie idCookie = new Cookie("adminId", m.getId() + "");
					
					userNameCookie.setMaxAge(60*60*3);
					pwCookie.setMaxAge(60*60*3);
					idCookie.setMaxAge(60*60*3);
					
					response.addCookie(idCookie);
					response.addCookie(pwCookie);
					response.addCookie(userNameCookie);
					Set<Map<String,Object>> list = login.getJurisdiction(m.getId());
					request.getSession().setAttribute("list", list);
					request.getRequestDispatcher("/WEB-INF/views/admin_index.jsp").forward(request, response);
				}else{
					request.setAttribute("result", "密码错误，请重新输入！");
					request.getRequestDispatcher("/WEB-INF/views/adminPC/admin_login.jsp").forward(request, response);
				}
			}else{
				request.setAttribute("result", "账户不存在！");
				request.getRequestDispatcher("/WEB-INF/views/adminPC/admin_login.jsp").forward(request, response);
			}
		}
		if (flag.equals("2")) {
			//request.getSession().removeAttribute("userName");
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
			response.sendRedirect(path + "/adminPC/admin_login.jsp");
		}
	}
	
	public void init() throws ServletException {
		// Put your code here
		logger.info("-------------- ManageLoginServlet init ---------- ");
	}

}
