package com.pc.manage.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pc.manage.dao.ManageFankuiDaoImpl;
import com.util.CurrentPage;
@WebServlet(name = "ManageFankuiServlet", urlPatterns = "/servlet/ManageFankuiServlet")  //标记为servlet，以便启动器扫描。
public class ManageFankuiServlet extends HttpServlet {
	
	public ManageFankuiServlet() {
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
		// 获取登录用户id
		String suserId = "";

		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if ("adminId".equals(cookie.getName())) {
					suserId = cookie.getValue();
					cookie.setMaxAge(60*60*3);
					response.addCookie(cookie);
				}
				if ("adminUserName".equals(cookie.getName())) {
					cookie.setMaxAge(60*60*3);
					response.addCookie(cookie);
				}
				if ("adminPw".equals(cookie.getName())) {
					cookie.setMaxAge(60*60*3);
					response.addCookie(cookie);
				}
			}
		}

		if (suserId == null || suserId.equals("")) {
			response.sendRedirect(path + "/adminPC/admin_login.jsp");
			return;
		}

		
		int userId = Integer.parseInt(suserId);
		ManageFankuiDaoImpl fankui = new ManageFankuiDaoImpl();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		int currentPage = 1;
		int numPerPage = 10;
		
		/**
		 * 用户反馈		条件+分页
		 */
		if(flag.equals("1")){
			//条件
			String sname = request.getParameter("name");

			String name = "";
			if (sname != null && !sname.equals("")) {
				name = sname.replaceAll(" ", "");
			}
			
			String scurrentPage = request.getParameter("currentPage");//当前页
			if(scurrentPage!=null&&scurrentPage!=""){
				currentPage = Integer.parseInt(scurrentPage);
			}
			String snumPerPage = request.getParameter("numPerPage");//每页记录数
			if(snumPerPage!=null&&snumPerPage!=""){
				numPerPage = Integer.parseInt(snumPerPage);
			}
			CurrentPage page = fankui.findFankui(currentPage,numPerPage,name);
			List<Map<String, Object>> fklist = page.getResultList();
			if(fklist.size()>0){
				for(int i=0;i<fklist.size();i++){
					if(fklist.get(i).get("time")!=null){
						String times = fklist.get(i).get("time").toString();
						try {
							//转换时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							fklist.get(i).put("time", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("page", page);
			request.setAttribute("fklist", fklist);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/yonghufankui/fankui.jsp").forward(request, response);
		}
		
	}
	
	public void init() throws ServletException {
		// Put your code here
	}
}
