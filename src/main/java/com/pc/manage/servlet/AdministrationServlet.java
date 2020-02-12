package com.pc.manage.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.pc.manage.dao.AdministrationDaoImpl;
import com.pc.teacher.servlet.TeacherFindServlet;
import com.util.CurrentPage;
/**
 * 管理PC端-系统管理
 */
@WebServlet(name = "AdministrationServlet", urlPatterns = "/servlet/AdministrationServlet")  //标记为servlet，以便启动器扫描。
public class AdministrationServlet extends HttpServlet{
	private static Logger logger = Logger.getLogger(TeacherFindServlet.class);
	public AdministrationServlet() {
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
		int userId = Integer.parseInt(suserId.toString());
		AdministrationDaoImpl admin = new AdministrationDaoImpl();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		int currentPage = 1;
		int numPerPage = 10;
		
		/**
		 * 权限管理 (用户列表)  - 条件+分页
		 */
		if(flag.equals("1")){
			String scurrentPage = request.getParameter("currentPage");//当前页
			if(scurrentPage!=null&&scurrentPage!=""){
				currentPage = Integer.parseInt(scurrentPage);
			}
			String snumPerPage = request.getParameter("numPerPage");//每页记录数
			if(snumPerPage!=null&&snumPerPage!=""){
				numPerPage = Integer.parseInt(snumPerPage);
			}
			CurrentPage page = admin.findAdministrators(currentPage,numPerPage);
			List<Map<String, Object>> list = page.getResultList();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					if(list.get(i).get("CREATE_TIME")!=null){
						String times = list.get(i).get("CREATE_TIME").toString();
						try {
							//转换提交时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							list.get(i).put("CREATE_TIME", format);
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
			request.setAttribute("list", list);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/administration/jurisdiction/jurisdiction.jsp").forward(request, response);
		
			
		}
		//权限列表（所有菜单）回显
		if(flag.equals("2")){
			String id = request.getParameter("id");
			List<Map<String, Object>> list = admin.getJurisdiction();
			String permissions = admin.getJurisdiction(id);
			request.setAttribute("list", list);
			request.setAttribute("id", id);
			request.setAttribute("permissions", permissions);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/administration/jurisdiction/jurisdiction_set.jsp").forward(request, response);
		}
		//所有权限列表
		if(flag.equals("3")){
			List<Map<String, Object>> list = admin.getJurisdiction();
			request.setAttribute("list", list);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/administration/jurisdiction/jurisdiction_add.jsp").forward(request, response);
		}

		//设置权限
		if(flag.equals("6")){
			List<String> list = new  ArrayList<>();
			String id = "";
			Map map = request.getParameterMap();  
		    Set keSet = map.entrySet();  
		    for(Iterator itr = keSet.iterator(); itr.hasNext();){  
		        Map.Entry me = (Map.Entry)itr.next();  
		        Object ok = me.getKey();
		        Object ov = me.getValue();  
		        if ("id".equals((String)ok)) {
					id = ov.toString();
				}
		        String[] value = new String[1];  
		        if(ov instanceof String[]){  
		            value = (String[])ov;  
		        }else{  
		            value[0] = ov.toString();  
		        }  
		        for(int k = 0; k < value.length; k++){  
		            if (value[k].length() == 6) {
						list.add(value[k]);
					}  
		        } 
		        if ("id".equals((String)ok)) {
					id = value[0];
				}
		    }
		    Set<String> set = new HashSet<>();
		    for (String string : list) {
				if (string.startsWith("001")) {
					set.add("001000");
				}
				if (string.startsWith("002")) {
					set.add("002000");
				}
				if (string.startsWith("003")) {
					set.add("003000");
				}
				if (string.startsWith("004")) {
					set.add("004000");
				}
				if (string.startsWith("005")) {
					set.add("005000");
				}
				if (string.startsWith("006")) {
					set.add("006000");
				}
				if (string.startsWith("007")) {
					set.add("007000");
				}
				if (string.startsWith("008")) {
					set.add("008000");
				}
				if (string.startsWith("009")) {
					set.add("009000");
				}
			}
		    set.addAll(list);
		    admin.updatePermission(id, set);
			response.sendRedirect(path + "/servlet/AdministrationServlet?do=1");
		}
		//修改信息--回显
		if(flag.equals("7")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> list = admin.selectInformation(id);
			request.setAttribute("list", list);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/administration/jurisdiction/information_update.jsp").forward(request, response);
		}
		//修改信息--提交修改
		if(flag.equals("8")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			String name = request.getParameter("name");//姓名
			String userName = request.getParameter("userName");//账号
			String pwd = request.getParameter("pwd");//密码
			String role = request.getParameter("role");//角色
			int i = admin.updateInformation(id,name,userName,pwd,role);
			PrintWriter writer = response.getWriter();
			if(i>0){
		        writer.write("修改成功!");
			}else{
				writer.write("修改失败!");
			}
			writer.flush();
		}
		//删除管理员
		if(flag.equals("9")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			int i = admin.deleteAdmin(id);
			PrintWriter writer = response.getWriter();
			if(i>0){
		        writer.write("删除成功!");
			}else{
				writer.write("删除失败!");
			}
			writer.flush();
		}
		//添加管理员
		if(flag.equals("10")){
			String name = request.getParameter("name");
			String userName = request.getParameter("userName");
			String pw = request.getParameter("pw");
			String role = request.getParameter("role");
			String jurisdiction = request.getParameter("jurisdiction");
			int jur = 2;
			if(jurisdiction!=null&&!"".equals(jurisdiction)){
				jur = 1;
			}
			String[] sts = jurisdiction.split(",");
			
			Set<String> set = new HashSet<>();
		    for (String string : sts) {
				if (string.startsWith("001")) {
					set.add("001000");
				}
				if (string.startsWith("002")) {
					set.add("002000");
				}
				if (string.startsWith("003")) {
					set.add("003000");
				}
				if (string.startsWith("004")) {
					set.add("004000");
				}
				if (string.startsWith("005")) {
					set.add("005000");
				}
				if (string.startsWith("006")) {
					set.add("006000");
				}
				if (string.startsWith("007")) {
					set.add("007000");
				}
				if (string.startsWith("008")) {
					set.add("008000");
				}
				if (string.length() == 6) {
					set.add(string);
				}
			}
			
			int i = admin.addAdmin(name,userName,pw,role,jur);
			PrintWriter writer = response.getWriter();
			if(i == -1){
		        writer.write("账号已存在!");
			}else if(i > 0){
				if(jur == 1){
					//设置权限
					admin.addJurisdiction(i,set);
				}
				writer.write("添加成功!");
			}else if(i == 0){
				writer.write("添加失败!");
			}
			writer.flush();
			//response.sendRedirect(path + "/adminPC/administration/jurisdiction/jurisdiction_add.jsp");
		}
	}
	
	public void init() throws ServletException {
		// Put your code here
	}
}
