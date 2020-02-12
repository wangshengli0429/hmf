package com.pc.teacher.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.entity.Teacher;
import com.pc.teacher.dao.LoginDaoImpl;

public class LoginServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String phone="123456487";
		String pass="1212312";
		LoginDaoImpl login=new LoginDaoImpl();
		Teacher t=login.checkLogin(phone, pass);
		if(t!=null){
			
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(t.getPhone());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
		}
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		HttpSession httpsession = request.getSession();
		httpsession.invalidate();
		String path = request.getContextPath();
		String lname=request.getParameter("loginName");
		String pwd=request.getParameter("pwd");
		
		LoginDaoImpl login=new LoginDaoImpl();
		int i =login.checkLoginName(lname);
		if(i==0){
			request.setAttribute("result", "用户未注册");
			request.getRequestDispatcher("/WEB-INF/views/teacherPC/teacher_login.jsp").forward(request, response);
		}else{
			Teacher t=login.checkLogin(lname, pwd);
			if(t!=null){
				//资料审核状态(审核中0，已通过1，未通过2, 禁用3 ,未上传4 )
				if(t.getAustate().equals("0")||t.getAustate().equals("1")||t.getAustate().equals("2")||t.getAustate().equals("4")){
					request.getSession().setAttribute("tuserName", t.getName());
					
					String encode = URLEncoder.encode(t.getName(), "utf-8");
					
					Cookie nameCookie = new Cookie("teacherName", encode);
					//Cookie nameCookie = new Cookie("teacherName", t.getName());
					Cookie pwCookie = new Cookie("teacherPw", t.getPassword());
					Cookie idCookie = new Cookie("teacherId", t.getId() + "");
					
					nameCookie.setMaxAge(60*60*3);
					pwCookie.setMaxAge(60*60*3);
					idCookie.setMaxAge(60*60*3);
					
					response.addCookie(idCookie);
					response.addCookie(pwCookie);
					response.addCookie(nameCookie);
					
					//验证预点评作文是否提交
					List<Map<String,Object>> expected = login.testExpected(t.getId());
					if(expected!=null){
						int state = Integer.parseInt(expected.get(0).get("STATE").toString());
						String cause = "";
						if(t.getCause()!=null){
							cause=t.getCause();
						}
						//状态(审核中0，审核通过1，审核不通过2)
						if(state==0){
							if(!t.getAustate().equals("0")){
								request.getSession().setAttribute("m", cause);
							}
							request.getRequestDispatcher("/WEB-INF/views/teacherPC/yuPiGai/shenhe_ing.jsp").forward(request, response);
						}else if(state==2){
							if(!t.getAustate().equals("0")){
								request.getSession().setAttribute("m", cause);
							}
							request.getRequestDispatcher("/WEB-INF/views/teacherPC/yuPiGai/shenhe_not.jsp").forward(request, response);
						}else if(state==1){//&&t.getCompleteInfo().equals("1")
							if(t.getAustate().equals("1")){
								request.getRequestDispatcher("/WEB-INF/views/teacherPC/zuoWenGuanLi/zwGuanLi_index.jsp").forward(request, response);
							}else if(t.getAustate().equals("0")){//师资认证审核中
								request.getRequestDispatcher("/WEB-INF/views/teacherPC/yuPiGai/szrz_ing.jsp").forward(request, response);
							}else if(t.getAustate().equals("2")){//师资认证未通过
								request.getRequestDispatcher("/WEB-INF/views/teacherPC/yuPiGai/szrz_not.jsp").forward(request, response);
							}else{
								request.getSession().setAttribute("m", cause);
								request.getRequestDispatcher("/WEB-INF/views/teacherPC/yuPiGai/shenhe_ing.jsp").forward(request, response);
							}
						}
					}else{
						response.sendRedirect(path+"/WEB-INF/views/servlet/TeacherFindServlet?do=6");
					}
				}else if(t.getAustate().equals("3")){//禁用3
					request.setAttribute("result", "您的账号已被禁用，请联系一堂作文课平台，电话为010-88333186");
					request.getRequestDispatcher("/WEB-INF/views/teacherPC/teacher_login.jsp").forward(request, response);
				}else{
					request.setAttribute("result", "您目前无法登陆");
					request.getRequestDispatcher("/WEB-INF/views/teacherPC/teacher_login.jsp").forward(request, response);
				}
			}else{
				request.setAttribute("result", "密码错误，请重新输入");
				request.getRequestDispatcher("/WEB-INF/views/teacherPC/teacher_login.jsp").forward(request, response);
			}
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
