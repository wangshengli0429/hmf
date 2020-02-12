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

import org.apache.log4j.Logger;

import com.pc.manage.dao.ManageFindFwDaoImpl;
import com.pc.manage.dao.ThirdPartyDaoImpl;
import com.pc.teacher.servlet.TeacherFindServlet;
import com.util.CommUtils;
import com.util.CurrentPage;
/**
 * 管理PC端-第三方合作
 */
@WebServlet(name = "ThirdPartyServlet", urlPatterns = "/servlet/ThirdPartyServlet")  //标记为servlet，以便启动器扫描。
public class ThirdPartyServlet extends HttpServlet{
	private static Logger logger = Logger.getLogger(TeacherFindServlet.class);
	public ThirdPartyServlet() {
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
		ThirdPartyDaoImpl third = new ThirdPartyDaoImpl();
		ManageFindFwDaoImpl findfw = new ManageFindFwDaoImpl();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		int currentPage = 1;
		int numPerPage = 10;
		
		/**
		 * 第三方合作（待点评）  - 条件+分页
		 */
		if(flag.equals("1")){
			String sname = request.getParameter("name");
			String nj = request.getParameter("ageDetail");//年级
			String startDate = request.getParameter("startDate");//时间
			String endDate = request.getParameter("endDate");//时间
			
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
			//（年级,年级表grade） - ageDetail
			List<Map<String, Object>> ageDetail =  findfw.findAgedetail();
			//待点评作文
			CurrentPage page = third.findDaidplist(currentPage,numPerPage,name,nj,startDate,endDate);
			List<Map<String, Object>> list = page.getResultList();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					if(list.get(i).get("createTime")!=null){
						String times = list.get(i).get("createTime").toString();
						try {
							//转换提交时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							list.get(i).put("times", format);
							//距离上传时间
							SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date begin = dfs.parse(times);
							Date end = new Date();
							long between = (end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒
							long day = between/(24*3600);//天
							long hour = between%(24*3600)/3600;//时
							long minute = between%3600/60;//分
							list.get(i).put("day", day);
							list.get(i).put("hour", hour);
							list.get(i).put("minute", minute);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(list.get(i).get("geade")!=null){
						Object[] parseAge = CommUtils.parseAge(list.get(i).get("geade").toString());
						String ageDetail1 = parseAge[1].toString();
						list.get(i).put("geade", ageDetail1);
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("ageDetail", ageDetail);
			request.setAttribute("page", page);
			request.setAttribute("list", list);
			request.setAttribute("name", name);
			request.setAttribute("nj", nj);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/thirdParty/daiDianPing.jsp").forward(request, response);
		}
		
		/**
		 * 第三方合作（已点评）  - 条件+分页
		 */
		if(flag.equals("2")){
			String grade = request.getParameter("grade");//年级
			String grading = request.getParameter("grading");//作文等级
			String startDate = request.getParameter("startDate");//时间
			String endDate = request.getParameter("endDate");//时间
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
			//（年级,年级表grade） - ageDetail
			List<Map<String, Object>> ageDetail =  findfw.findAgedetail();
			//已点评作文
			CurrentPage page = third.findYidplist(currentPage,numPerPage,grade,grading,name,startDate,endDate);
			List<Map<String, Object>> list = page.getResultList();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					if(list.get(i).get("createTime")!=null){
						String times = list.get(i).get("createTime").toString();
						try {
							//转换提交时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							list.get(i).put("createtimes", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(list.get(i).get("comTime")!=null){
						String times = list.get(i).get("comTime").toString();
						try {
							//转换提交时间
							Date parse1 = df.parse(times);
							String format1 = df.format(parse1);
							list.get(i).put("comtimes", format1);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(list.get(i).get("geade")!=null){
						Object[] parseAge = CommUtils.parseAge(list.get(i).get("geade").toString());
						String ageDetail1 = parseAge[1].toString();
						list.get(i).put("geade", ageDetail1);
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("ageDetail", ageDetail);
			request.setAttribute("page", page);
			request.setAttribute("list", list);
			request.setAttribute("grade", grade);
			request.setAttribute("grading", grading);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/thirdParty/yiDianPing.jsp").forward(request, response);
		}
		
		/**
		 * 第三方合作（退款）  - 条件+分页
		 */
		if(flag.equals("3")){
			//条件
			String state = request.getParameter("state");//状态
			String sname = request.getParameter("name");//编号、用户、作文
			String startDate = request.getParameter("startDate");//时间
			String endDate = request.getParameter("endDate");//时间
			
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

			CurrentPage page = third.findRefund(currentPage,numPerPage,state,name,startDate,endDate);
			List<Map<String, Object>> tklist = page.getResultList();
			if(tklist.size()>0){
				for(int i=0;i<tklist.size();i++){
					if(tklist.get(i).get("ctime")!=null){
						String times = tklist.get(i).get("ctime").toString();
						try {
							//转换申请时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							tklist.get(i).put("ctime", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(tklist.get(i).get("dtime")!=null){
						String times = tklist.get(i).get("dtime").toString();
						try {
							//转换申请时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							tklist.get(i).put("dtime", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(tklist.get(i).get("title")==null||tklist.get(i).get("title").equals("")){
						if(tklist.get(i).get("oldtitle")!=null&&tklist.get(i).get("oldtitle")!=""){
							String oldtitle = tklist.get(i).get("oldtitle").toString();
							tklist.get(i).put("title", oldtitle);
						}
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("page", page);
			request.setAttribute("tklist", tklist);
			request.setAttribute("state", state);
			request.setAttribute("name", name);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/thirdParty/tuikuan.jsp").forward(request, response);
		}
		
	}
}
