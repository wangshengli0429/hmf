package com.pc.manage.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;
import com.pc.manage.dao.ManageZuowenDaoImpl;
import com.util.CommUtils;
import com.util.Constant;
import com.util.CurrentPage;
import com.util.HttpUtils;

/**
 * 管理PC端-作文管理
 */
@WebServlet(name = "ManageZuowenServlet", urlPatterns = "/servlet/ManageZuowenServlet")  //标记为servlet，以便启动器扫描。
public class ManageZuowenServlet extends HttpServlet {
 
	public ManageZuowenServlet() {
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
		ManageZuowenDaoImpl zuowen = new ManageZuowenDaoImpl();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		int currentPage = 1;
		int numPerPage = 10;
		
		/**
		 * 待点评作文集合   - 条件+分页
		 */
		if(flag.equals("1")){
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
			//老师用户集合
			CurrentPage page1 = zuowen.findTeacherlist(currentPage,numPerPage,name);
			List<Map<String, Object>> tlist = page1.getResultList();
			if(tlist.size()>0){
				request.setAttribute("page1", page1);
				request.setAttribute("tlist", tlist);
				
			}else{
				request.setAttribute("m1", "无搜索结果");
			}
			//待点评作文
			CurrentPage page = zuowen.findDaidplist(currentPage,numPerPage,name);
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
						String ageDetail = parseAge[1].toString();
						list.get(i).put("geade", ageDetail);
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("page", page);
			request.setAttribute("list", list);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/daiDianPing/daiDianPing.jsp").forward(request, response);
		}
		
		//查看待点评作文
		if(flag.equals("2")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> list = zuowen.selectDaidp(id);
			String host = CommUtils.getServerHost();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					if(list.get(0).get("img1")!=null&&list.get(0).get("img1")!=""){
						list.get(0).put("img1", host+list.get(0).get("img1").toString());
					}
					if(list.get(0).get("img2")!=null&&list.get(0).get("img2")!=""){
						list.get(0).put("img2", host+list.get(0).get("img2").toString());
					}
					if(list.get(0).get("img3")!=null&&list.get(0).get("img3")!=""){
						list.get(0).put("img3", host+list.get(0).get("img3").toString());
					}
					if(list.get(0).get("content")!=null&&list.get(0).get("content")!=""){
						String textareaText = "<p style=\"text-indent: 2em;\">"+list.get(0).get("content").toString();
						String replaceAll = textareaText.replaceAll("\\n","<p style=\"text-indent: 2em;\">");
						list.get(0).put("content", replaceAll);
					}
					if(list.get(i).get("createTime")!=null){
						String times = list.get(i).get("createTime").toString();
						try {
							//转换提交时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							list.get(i).put("times", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(list.get(i).get("geade")!=null){
						Object[] parseAge = CommUtils.parseAge(list.get(i).get("geade").toString());
						String ageDetail = parseAge[1].toString();
						list.get(i).put("geade", ageDetail);
					}
				}
			}
			request.setAttribute("list", list);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/daiDianPing/daiDianPing_select.jsp").forward(request, response);
		}
		
		/**
		 * 已点评作文集合   - 条件+分页
		 */
		if(flag.equals("3")){
			String grade = request.getParameter("grade");//年级
			String grading = request.getParameter("grading");//作文等级
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
			//已点评作文
			CurrentPage page = zuowen.findYidplist(currentPage,numPerPage,grade,grading,name);
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
						String ageDetail = parseAge[1].toString();
						list.get(i).put("geade", ageDetail);
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("page", page);
			request.setAttribute("list", list);
			request.setAttribute("grade", grade);
			request.setAttribute("grading", grading);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/yiDianPing/yiDianPing.jsp").forward(request, response);
		}
		
		//查看已点评作文
		if(flag.equals("4")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> list = zuowen.selectYidp(id);
			String host = CommUtils.getServerHost();
			List<Map<String, Object>> pingfen = null;
			if(list.size()>0){
				if(list.get(0).get("content")!=null&&list.get(0).get("content")!=""){
					String textareaText = "<p style=\"text-indent: 2em;\">"+list.get(0).get("content").toString();
					String replaceAll = textareaText.replaceAll("\\n","<p style=\"text-indent: 2em;\">");
					list.get(0).put("content", replaceAll);
				}
				if(list.get(0).get("voice")!=null&&!list.get(0).get("voice").equals("")){
					String pathsss = "F:/server/cedu-files"+list.get(0).get("voice").toString();
					String base64 = CommUtils.fileToBase64(pathsss);
					list.get(0).put("voice", base64);
				}else {
					list.get(0).put("voice", "");
				}
				
				if(list.get(0).get("img1")!=null&&!list.get(0).get("img1").equals("")){
					list.get(0).put("img1", host+list.get(0).get("img1").toString());
				}
				if(list.get(0).get("img2")!=null&&!list.get(0).get("img2").equals("")){
					list.get(0).put("img2", host+list.get(0).get("img2").toString());
				}
				if(list.get(0).get("img3")!=null&&!list.get(0).get("img3").equals("")){
					list.get(0).put("img3", host+list.get(0).get("img3").toString());
				}
				if(list.get(0).get("geade")!=null&&!list.get(0).get("geade").equals("")){
					Object[] parseAge = CommUtils.parseAge(list.get(0).get("geade").toString());
					String ageDetail = parseAge[1].toString();
					list.get(0).put("geade", ageDetail);
					//评分标准信息
					pingfen = zuowen.pingfen(ageDetail);
				}
				if(list.get(0).get("createTime")!=null&&!list.get(0).get("createTime").equals("")){
					String times = list.get(0).get("createTime").toString();
					try {
						//转换提交时间
						Date parse = df.parse(times);
						String format = df.format(parse);
						list.get(0).put("createTimes", format);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(list.get(0).get("comTime")!=null&&!list.get(0).get("comTime").equals("")){
					String times = list.get(0).get("comTime").toString();
					try {
						//转换提交时间
						Date parse = df.parse(times);
						String format = df.format(parse);
						list.get(0).put("comTimes", format);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(list.get(0).get("scoring")!=null&&!list.get(0).get("scoring").equals("")){
					String str = list.get(0).get("scoring").toString();
					String scoring = str.replaceAll("\\n","<br>");
					list.get(0).put("scoring", scoring);
				}
				if(list.get(0).get("points")!=null&&!list.get(0).get("points").equals("")){
					String str = list.get(0).get("points").toString();
					String points = str.replaceAll("\\n","<br>");
					list.get(0).put("points", points);
				}
				if(list.get(0).get("suggest")!=null&&!list.get(0).get("suggest").equals("")){
					String str = list.get(0).get("suggest").toString();
					String suggest = str.replaceAll("\\n","<br>");
					list.get(0).put("suggest", suggest);
				}
			}
			request.setAttribute("list", list);
			request.setAttribute("pingfen", pingfen);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/yiDianPing/yiDianPing_select.jsp").forward(request, response);
		}
		
		/**
		 * 预点评-已点评作文集合	条件+分页
		 */
		if(flag.equals("5")){
			String name = request.getParameter("name");
			
			String scurrentPage = request.getParameter("currentPage");//当前页
			if(scurrentPage!=null&&scurrentPage!=""){
				currentPage = Integer.parseInt(scurrentPage);
			}
			String snumPerPage = request.getParameter("numPerPage");//每页记录数
			if(snumPerPage!=null&&snumPerPage!=""){
				numPerPage = Integer.parseInt(snumPerPage);
			}
			//已点评作文
			CurrentPage page = zuowen.findYudpYi(currentPage,numPerPage,name);
			List<Map<String, Object>> list = page.getResultList();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					if(list.get(i).get("createTime")!=null){
						String times = list.get(i).get("createTime").toString();
						try {
							//转换上传时间
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
							//转换点评时间
							Date parse1 = df.parse(times);
							String format1 = df.format(parse1);
							list.get(i).put("comtimes", format1);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			request.setAttribute("page", page);
			request.setAttribute("list", list);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/yuDianPing/yuDianPing_yidp.jsp").forward(request, response);
		}
		
		//预点评-查看已点评作文
		if(flag.equals("6")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> yidplist = zuowen.selectYudpyi(id);
			String host = CommUtils.getServerHost();
			List<Map<String, Object>> pingfen = null;
			if(yidplist.size()>0){
				if(yidplist.get(0).get("img1")!=null&&yidplist.get(0).get("img1")!=""){
					yidplist.get(0).put("img1", host+yidplist.get(0).get("img1").toString());
				}
				if(yidplist.get(0).get("img2")!=null&&yidplist.get(0).get("img2")!=""){
					yidplist.get(0).put("img2", host+yidplist.get(0).get("img2").toString());
				}
				if(yidplist.get(0).get("img3")!=null&&yidplist.get(0).get("img3")!=""){
					yidplist.get(0).put("img3", host+yidplist.get(0).get("img3").toString());
				}
				if(yidplist.get(0).get("geade")!=null){
					String geade = yidplist.get(0).get("geade").toString();
					//评分标准信息
					pingfen = zuowen.pingfen(geade);
				}
				for(int i=0;i<yidplist.size();i++){
					if(yidplist.get(i).get("createTime")!=null){
						String times = yidplist.get(i).get("createTime").toString();
						try {
							//转换提交时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							yidplist.get(i).put("createTimes", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(yidplist.get(i).get("comTime")!=null){
						String times = yidplist.get(i).get("comTime").toString();
						try {
							//转换提交时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							yidplist.get(i).put("comTimes", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			request.setAttribute("yidplist", yidplist);
			request.setAttribute("pingfen", pingfen);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/yuDianPing/yudp_findyi.jsp").forward(request, response);
		}
		
		
		/**
		 * 预点评-未点评作文集合	条件+分页
		 */
		if(flag.equals("7")){
			String name = request.getParameter("name");
			
			String scurrentPage = request.getParameter("currentPage");//当前页
			if(scurrentPage!=null&&scurrentPage!=""){
				currentPage = Integer.parseInt(scurrentPage);
			}
			String snumPerPage = request.getParameter("numPerPage");//每页记录数
			if(snumPerPage!=null&&snumPerPage!=""){
				numPerPage = Integer.parseInt(snumPerPage);
			}
			//未点评作文
			CurrentPage page = zuowen.findYudpWei(currentPage,numPerPage,name);
			List<Map<String, Object>> list = page.getResultList();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					if(list.get(i).get("createTime")!=null){
						String times = list.get(i).get("createTime").toString();
						try {
							//转换上传时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							list.get(i).put("createtimes", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			request.setAttribute("page", page);
			request.setAttribute("list", list);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/yuDianPing/yuDianPing_weidp.jsp").forward(request, response);
		}
		
		//预点评-未点评-删除
		if(flag.equals("8")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			int i = zuowen.deleteYudpwei(id);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageZuowenServlet?do=7");
			}
		}
		
		//预点评-未点评    -修改    回显 
		if(flag.equals("9")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> wdp = zuowen.getYudpwei(id);
			request.setAttribute("wdp", wdp);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/yuDianPing/yuDianPing_update.jsp").forward(request, response);
		}
		
		//预点评-未点评    -修改    提交 
		if(flag.equals("10")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			String scomId = request.getParameter("comId");
			int comId = Integer.parseInt(scomId);
			String title = request.getParameter("newTitle");
			String oldTitle = request.getParameter("oldTitle");
			String content = request.getParameter("content");
			String grade = request.getParameter("ydpGrade");
			int i = zuowen.updateYudpwei(id,comId,title,oldTitle,content,grade);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageZuowenServlet?do=7");
			}
		}
		
		//查看待点评作文
		if(flag.equals("11")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> weidplist = zuowen.selectYudpWei(id);
			String host = CommUtils.getServerHost();
			if(weidplist.size()>0){
				if(weidplist.get(0).get("img1")!=null&&weidplist.get(0).get("img1")!=""){
					weidplist.get(0).put("img1", host+weidplist.get(0).get("img1").toString());
				}
				if(weidplist.get(0).get("img2")!=null&&weidplist.get(0).get("img2")!=""){
					weidplist.get(0).put("img2", host+weidplist.get(0).get("img2").toString());
				}
				if(weidplist.get(0).get("img3")!=null&&weidplist.get(0).get("img3")!=""){
					weidplist.get(0).put("img3", host+weidplist.get(0).get("img3").toString());
				}
			}
			request.setAttribute("weidplist", weidplist);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/yuDianPing/yudp_findwei.jsp").forward(request, response);
		}
		
		//上传预点评作文	-跳转页面
		if(flag.equals("12")){
			List<Map<String, Object>> geadelist = zuowen.selectGeade();//年级	：一年级到高三
			request.setAttribute("geadelist", geadelist);
			
			request.getRequestDispatcher("/WEB-INF/views/WEB-INF/views/zuoWenGuanLi/yuDianPing/yuDianPing_add.jsp").forward(request, response);
		}
		
		//上传预点评作文	-提交
		if(flag.equals("13")){
			String title = request.getParameter("ydpTitle");
			String content = request.getParameter("content");
			String grade = request.getParameter("ydpGrade");
			int i = zuowen.insertYudp(title,content,grade);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageZuowenServlet?do=7");
			}
		}
		
		/**
		 * 作文推荐 	条件+分页
		 */
		if(flag.equals("14")){
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
			//作文  推荐
			CurrentPage page = zuowen.findYudpTuijian(currentPage,numPerPage,name);
			List<Map<String, Object>> list = page.getResultList();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					if(list.get(i).get("publishTime")!=null&&list.get(i).get("publishTime")!=""){
						String times = list.get(i).get("publishTime").toString();
						try {
							//转换上传时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							list.get(i).put("publishTimes", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(list.get(i).get("ageDetail")==null||list.get(i).get("ageDetail")==""){
						if(list.get(i).get("ageScale")!=null&&list.get(i).get("ageScale")!=""){
							String string = list.get(i).get("ageScale").toString();
							String ageScale = zuowen.findCodename(string);
							list.get(i).put("ageDetail", ageScale);
						}
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("page", page);
			request.setAttribute("list", list);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/tuiJian/tuiJian.jsp").forward(request, response);
		}
		
		//修改推荐作文显示状态
		if(flag.equals("15")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			String sdisplay = request.getParameter("display");
			int display = Integer.parseInt(sdisplay);
			int i = zuowen.updateType(id,display);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageZuowenServlet?do=14");
			}
			
		}
		
		//修改推荐作文 	回显
		if(flag.equals("16")){
			List<Map<String, Object>> geadelist = zuowen.selectGeade();//年级	：一年级到高三
			List<Map<String, Object>> geadelist2 = zuowen.selectGeadeSucai();//年级	：小学到高中
			if(geadelist2.size()>0){
				for(int i=0;i<geadelist2.size();i++){
					geadelist.add(geadelist2.get(i));
				}
			}
			request.setAttribute("geadelist", geadelist);
			List<Map<String, Object>> stylelist2 = zuowen.selectStyle2();//体裁 ：CODE_TYPE=013
			
			//11.2日改 by wb
			
			/*String dir = request.getSession().getServletContext().getRealPath("/jslib/fenlei.json");
			List<Map<String, String>> stylelist = zuowen.findGenre(dir);
			for(int j=0;j<stylelist2.size();j++){
				Map<String, String> arrayList =  new HashMap<String, String>();
				arrayList.put("CODE", stylelist2.get(j).get("CODE").toString());
				arrayList.put("CODE_NAME", stylelist2.get(j).get("CODE_NAME").toString());
				stylelist.add(arrayList);
			}
			request.setAttribute("stylelist", stylelist);*/
			
			List<Map<String, String>> stylelist = new ArrayList<>();
			for(int j=0;j<stylelist2.size();j++){
				Map<String, String> arrayList =  new HashMap<String, String>();
				arrayList.put("CODE", stylelist2.get(j).get("CODE").toString());
				arrayList.put("CODE_NAME", stylelist2.get(j).get("CODE_NAME").toString());
				stylelist.add(arrayList);
			}
			request.setAttribute("stylelist", stylelist);
			
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> tuijian = zuowen.selectTuijian(id);
			if(tuijian.size()>0){
				if(tuijian.get(0).get("ageDetail")==null||tuijian.get(0).get("ageDetail")==""){
					if(tuijian.get(0).get("ageScale")!=null||tuijian.get(0).get("ageScale")!=""){
						String string = tuijian.get(0).get("ageScale").toString();
						String str = zuowen.findCodename(string);
						tuijian.get(0).put("ageDetail", str);
					}
				}
				if(tuijian.get(0).get("name")!=null||tuijian.get(0).get("name")!=""){
					String replaceAll = tuijian.get(0).get("name").toString().replaceAll("\"","&quot;");
					tuijian.get(0).put("name", replaceAll);
				}
				if(tuijian.get(0).get("author")!=null||tuijian.get(0).get("author")!=""){
					String replaceAll = tuijian.get(0).get("author").toString().replaceAll("\"","&quot;");
					tuijian.get(0).put("author", replaceAll);
				}
			}
			request.setAttribute("tuijian", tuijian);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/tuiJian/tuiJian_update.jsp").forward(request, response);
		}
		
		//修改推荐作文 	提交修改
		if(flag.equals("17")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			String title = request.getParameter("zwTitle");
			String content = request.getParameter("content");
			String author = request.getParameter("zwName");
			String nj = request.getParameter("zwGrade");
			String style = request.getParameter("zwStyle");
			int i = zuowen.updateTuijian(id,title,content,author,nj,style);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageZuowenServlet?do=14");
			}
		}
		
		//删除推荐作文	
		if(flag.equals("18")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			int i = zuowen.daleteTuijian(id);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageZuowenServlet?do=14");
			}
		}
		
		//查看推荐作文
		if(flag.equals("19")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> tuijian = zuowen.selectTuijian(id);
			if(tuijian.size()>0){
				if(tuijian.get(0).get("ageDetail")==null||tuijian.get(0).get("ageDetail").equals("")){
					if(tuijian.get(0).get("ageScale")!=null&&!tuijian.get(0).get("ageScale").equals("")){
						String ageScale = zuowen.selectAgeScale(tuijian.get(0).get("ageScale").toString());
						tuijian.get(0).put("ageDetail", ageScale);
					}
				}
			}
			request.setAttribute("tuijian", tuijian);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/tuiJian/tuiJian_select.jsp").forward(request, response);
		}
		
		//添加推荐作文	跳转页面
		if(flag.equals("20")){
			List<Map<String, Object>> geadelist = zuowen.selectGeade();//年级	：一年级到高三
			List<Map<String, Object>> geadelist2 = zuowen.selectGeadeSucai();//年级	：小学到高中
			request.setAttribute("geadelist", geadelist);
			request.setAttribute("geadelist2", geadelist2);
			List<Map<String, Object>> stylelist = zuowen.selectStyle2();//体裁 ：CODE_TYPE=013
			request.setAttribute("zwStyle", stylelist);
			
			//条件
			String type = request.getParameter("type");//类型：范文、技法、素材
			String nj = request.getParameter("ageDetail");//年级
			String name = request.getParameter("name");//范文标题
			
			String scurrentPage = request.getParameter("currentPage");//当前页
			if(scurrentPage!=null&&scurrentPage!=""){
				currentPage = Integer.parseInt(scurrentPage);
			}
			String snumPerPage = request.getParameter("numPerPage");//每页记录数
			if(snumPerPage!=null&&snumPerPage!=""){
				numPerPage = Integer.parseInt(snumPerPage);
			}
			if(type==null||type.equals("")){
				type="1";
			}
			//作文集合
			CurrentPage page = zuowen.findZw(currentPage,numPerPage,nj,type,name);
			List<Map<String, Object>> fwlist = page.getResultList();
			if(fwlist.size()>0){
				for(int i=0;i<fwlist.size();i++){
					if(fwlist.get(i).get("time")!=null){
						String times = fwlist.get(i).get("time").toString();
						try {
							//转换提交时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							fwlist.get(i).put("times", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			request.setAttribute("type", type);
			request.setAttribute("nj", nj);
			request.setAttribute("name", name);
			request.setAttribute("page", page);
			request.setAttribute("fwlist", fwlist);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/tuiJian/tuiJian_add.jsp").forward(request, response);
		}
		
		//添加推荐作文	提交
		if(flag.equals("21")){
			String title = request.getParameter("zwTitle");
			String content = request.getParameter("content");
			String author = request.getParameter("zwName");
			String nj = request.getParameter("zwGrade");
			String style = request.getParameter("zwStyle");
			
			int i = zuowen.insertTuijian(title,content,author,nj,style);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageZuowenServlet?do=14");
			}
		}
		
		/**
		 * 评价管理		条件+分页
		 */
		if(flag.equals("22")){
			String sname = request.getParameter("name");
			
			String name = "";
			if (sname != null && !sname.equals("")) {
				name = sname.replaceAll(" ", "");
			}
			
			String satisfaction = request.getParameter("satisfaction");
			
			String scurrentPage = request.getParameter("currentPage");//当前页
			if(scurrentPage!=null&&scurrentPage!=""){
				currentPage = Integer.parseInt(scurrentPage);
			}
			String snumPerPage = request.getParameter("numPerPage");//每页记录数
			if(snumPerPage!=null&&snumPerPage!=""){
				numPerPage = Integer.parseInt(snumPerPage);
			}
			//评价
			CurrentPage page = zuowen.findYudpPingjia(currentPage,numPerPage,satisfaction,name);
			List<Map<String, Object>> list = page.getResultList();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					if(list.get(i).get("apprTime")!=null&&!list.get(i).get("apprTime").equals("")){
						String times = list.get(i).get("apprTime").toString();
						try {
							//转换上传时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							list.get(i).put("apprTimes", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(list.get(i).get("messageTime")!=null&&!list.get(i).get("messageTime").equals("")){
						String times = list.get(i).get("messageTime").toString();
						try {
							//转换上传时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							list.get(i).put("messageTimes", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(list.get(i).get("grade")!=null&&!list.get(i).get("grade").equals("")){
						Object[] parseAge = CommUtils.parseAge(list.get(i).get("grade").toString());
						String ageDetail = parseAge[1].toString();
						list.get(i).put("grade", ageDetail);
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("page", page);
			request.setAttribute("list", list);
			request.setAttribute("name", name);
			request.setAttribute("satisfaction", satisfaction);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/pingJia/pingJia.jsp").forward(request, response);
		}

		//查看评价作文
		if(flag.equals("23")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> pingjia = zuowen.selectPingjiaZw(id);
			List<List<Map<String, String>>> message = zuowen.selectMessage(id);
			List<Map<String, Object>> pingfen = null;
			String host = CommUtils.getServerHost();
			if(pingjia.size()>0){
				if(pingjia.get(0).get("voice")!=null&&!pingjia.get(0).get("voice").equals("")){
					String pathsss = "F:/server/cedu-files"+pingjia.get(0).get("voice").toString();
					String base64 = CommUtils.fileToBase64(pathsss);
					pingjia.get(0).put("voice", base64);
				}else {
					pingjia.get(0).put("voice", "");
				}
				if(pingjia.get(0).get("img1")!=null&&pingjia.get(0).get("img1")!=""){
					pingjia.get(0).put("img1", host+pingjia.get(0).get("img1").toString());
				}
				if(pingjia.get(0).get("img2")!=null&&pingjia.get(0).get("img2")!=""){
					pingjia.get(0).put("img2", host+pingjia.get(0).get("img2").toString());
				}
				if(pingjia.get(0).get("img3")!=null&&pingjia.get(0).get("img3")!=""){
					pingjia.get(0).put("img3", host+pingjia.get(0).get("img3").toString());
				}
				if(pingjia.get(0).get("geade")!=null){
					Object[] parseAge = CommUtils.parseAge(pingjia.get(0).get("geade").toString());
					String ageDetail = parseAge[1].toString();
					pingjia.get(0).put("geade", ageDetail);
					//评分标准信息
					pingfen = zuowen.pingfen(ageDetail);
				}
				if(pingjia.get(0).get("createTime")!=null){
					String times = pingjia.get(0).get("createTime").toString();
					try {
						//转换提交时间
						Date parse = df.parse(times);
						String format = df.format(parse);
						pingjia.get(0).put("createTimes", format);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(pingjia.get(0).get("comTime")!=null){
					String times = pingjia.get(0).get("comTime").toString();
					try {
						//转换提交时间
						Date parse = df.parse(times);
						String format = df.format(parse);
						pingjia.get(0).put("comTimes", format);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(pingjia.get(0).get("apprtime")!=null){
					String times = pingjia.get(0).get("apprtime").toString();
					try {
						//转换提交时间
						Date parse = df.parse(times);
						String format = df.format(parse);
						pingjia.get(0).put("apprtime", format);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(pingjia.get(0).get("messagetime")!=null){
					String times = pingjia.get(0).get("messagetime").toString();
					try {
						//转换提交时间
						Date parse = df.parse(times);
						String format = df.format(parse);
						pingjia.get(0).put("messagetime", format);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(pingjia.get(0).get("scoring")!=null&&!pingjia.get(0).get("scoring").equals("")){
					String str = pingjia.get(0).get("scoring").toString();
					String scoring = str.replaceAll("\\n","<br>");
					pingjia.get(0).put("scoring", scoring);
				}
				if(pingjia.get(0).get("points")!=null&&!pingjia.get(0).get("points").equals("")){
					String str = pingjia.get(0).get("points").toString();
					String points = str.replaceAll("\\n","<br>");
					pingjia.get(0).put("points", points);
				}
				if(pingjia.get(0).get("suggest")!=null&&!pingjia.get(0).get("suggest").equals("")){
					String str = pingjia.get(0).get("suggest").toString();
					String suggest = str.replaceAll("\\n","<br>");
					pingjia.get(0).put("suggest", suggest);
				}
			}
			request.setAttribute("pingjia", pingjia);
			request.setAttribute("message", message);
			request.setAttribute("pingfen", pingfen);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/pingJia/pingJia_select.jsp").forward(request, response);
		}
		
		//其他作文
		if(flag.equals("24")){
			List<Map<String, Object>> geadelist = zuowen.selectGeade();//年级	：一年级到高三
			List<Map<String, Object>> stylelist = zuowen.selectStyle();//体裁 ：CODE_TYPE=013
			request.setAttribute("stylelist", stylelist);
			//条件
			String type = request.getParameter("type");//类型：范文、技法、素材
			String nj = request.getParameter("ageDetail");//年级
		    //String nj = new String(request.getParameter("ageDetail").getBytes("iso-8859-1"), "utf-8");  //年级
			String name = request.getParameter("name");//范文标题
			//String name = new String(request.getParameter("name").getBytes("iso-8859-1"), "utf-8");  //范文标题
			
			String scurrentPage = request.getParameter("currentPage");//当前页
			if(scurrentPage!=null&&scurrentPage!=""){
				currentPage = Integer.parseInt(scurrentPage);
			}
			String snumPerPage = request.getParameter("numPerPage");//每页记录数
			if(snumPerPage!=null&&snumPerPage!=""){
				numPerPage = Integer.parseInt(snumPerPage);
			}
			if(type==null||type.equals("")){
				type="1";
			}
			//作文集合
			if(type.equals("1")){//范文
				CurrentPage page = zuowen.findZw(currentPage,numPerPage,nj,type,name);
				List<Map<String, Object>> fwlist = page.getResultList();
				if(fwlist.size()>0){
					for(int i=0;i<fwlist.size();i++){
						if(fwlist.get(i).get("time")!=null){
							String times = fwlist.get(i).get("time").toString();
							try {
								//转换提交时间
								Date parse = df.parse(times);
								String format = df.format(parse);
								fwlist.get(i).put("times", format);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				request.setAttribute("geadelist", geadelist);
				request.setAttribute("type", type);
				request.setAttribute("nj", nj);
				request.setAttribute("name", name);
				request.setAttribute("page", page);
				request.setAttribute("fwlist", fwlist);
				request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/tuiJian/tuiJian_qita_fw.jsp").forward(request, response);
			}
			if(type.equals("2")){//技法
				List<Map<String, Object>> geadeJifa = zuowen.selectGeadeSucai();//素材年级	：小学-高中
				CurrentPage page = zuowen.findZw(currentPage,numPerPage,nj,type,name);
				List<Map<String, Object>> fwlist = page.getResultList();
				if(fwlist.size()>0){
					for(int i=0;i<fwlist.size();i++){
						if(fwlist.get(i).get("time")!=null){
							String times = fwlist.get(i).get("time").toString();
							try {
								//转换提交时间
								Date parse = df.parse(times);
								String format = df.format(parse);
								fwlist.get(i).put("times", format);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				request.setAttribute("geadelist", geadeJifa);
				request.setAttribute("type", type);
				request.setAttribute("nj", nj);
				request.setAttribute("name", name);
				request.setAttribute("page", page);
				request.setAttribute("fwlist", fwlist);
				request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/tuiJian/tuiJian_qita_jf.jsp").forward(request, response);
			}
			if(type.equals("3")){//素材
				List<Map<String, Object>> geadeSucai = zuowen.selectGeadeSucai();//素材年级	：小学-高中
				CurrentPage page = zuowen.findZw(currentPage,numPerPage,nj,type,name);
				List<Map<String, Object>> fwlist = page.getResultList();
				if(fwlist.size()>0){
					for(int i=0;i<fwlist.size();i++){
						if(fwlist.get(i).get("time")!=null){
							String times = fwlist.get(i).get("time").toString();
							try {
								//转换提交时间
								Date parse = df.parse(times);
								String format = df.format(parse);
								fwlist.get(i).put("times", format);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				request.setAttribute("geadelist", geadeSucai);
				request.setAttribute("type", type);
				request.setAttribute("nj", nj);
				request.setAttribute("name", name);
				request.setAttribute("page", page);
				request.setAttribute("fwlist", fwlist);
				request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/tuiJian/tuiJian_qita_sc.jsp").forward(request, response);
			}
		}
		
		//其他作文	--	获取值到添加页面（范文）
		if(flag.equals("25")){
			List<Map<String, Object>> geadelist = zuowen.selectGeade();//年级	：一年级到高三
			//List<Map<String, Object>> stylelist = zuowen.selectStyle();//体裁 ：CODE_TYPE=013
			String dir = request.getSession().getServletContext().getRealPath("/jslib/fenlei.json");
			//List<Map<String, String>> stylelist = zuowen.findGenre(dir);
			List<Map<String, String>> stylelist =  new ArrayList<Map<String, String>>();
	        try {
	            File file = new File(dir);
	            if (!file.exists()) {
	                file.createNewFile();
	            }
	           String str= FileUtils.readFileToString(file, "UTF-8");
	           JSONObject object = JSONObject.fromObject(str);
	           String string = object.get("fanwen").toString();
	           Object parse = JSON.parse(string);
	           JSONArray jsonObject = JSONArray.fromObject(parse);
	           Iterator it = jsonObject.iterator();
	           while (it.hasNext()) {
	        	   Map<String, String> map = new HashMap<String, String>();
	                JSONObject ob = (JSONObject) it.next();
	                map.put("CODE", ob.getString("CODE"));
	                map.put("CODE_NAME", ob.getString("CODE_NAME"));
	                stylelist.add(map);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> list = zuowen.selectZuowen(id);
			if(list.size()>0){
				if(list.get(0).get("type")!=null){
					String string = list.get(0).get("type").toString();
					String string2 = "";
					for(int j=0;j<stylelist.size();j++){
						if(stylelist.get(j).get("CODE").equals(string)){
							string2 = stylelist.get(j).get("CODE");
						}
					}
					if(string2==""){
						if(list.get(0).get("style")!=null){
							String style = list.get(0).get("style").toString();
							for(int j=0;j<stylelist.size();j++){
								if(stylelist.get(j).get("CODE").equals(style)){
									string2 = stylelist.get(j).get("CODE");
								}
							}
						}
					}
					list.get(0).put("style", string2);
				}else {
					if(list.get(0).get("style")!=null){
						String string = list.get(0).get("style").toString();
						String string2 = "";
						for(int j=0;j<stylelist.size();j++){
							if(stylelist.get(j).get("CODE").equals(string)){
								string2 = stylelist.get(j).get("CODE");
							}
						}
						list.get(0).put("style", string2);
					}
				}
				if (list.get(0).get("content")!=null && !list.get(0).get("content").equals("")) {
					String yuanxing = list.get(0).get("content").toString();
					String newStr = CommUtils.deleteXmlForComposition(yuanxing);
					list.get(0).put("content", newStr);
				}
				
			}
			request.setAttribute("geadelist", geadelist);
			request.setAttribute("stylelist", stylelist);
			request.setAttribute("list", list);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/tuiJian/tuiJian_add2.jsp").forward(request, response);
		}
		
		//其他作文	--	查看作文（范文）
		if(flag.equals("26")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> list = zuowen.selectFanwen(id);
			JSONArray jsonmap = JSONArray.fromObject(list);
			String string = jsonmap.get(0).toString();
			PrintWriter out =null;
			try {
				out = response.getWriter();
				out.print(string);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if (out!=null) {
					out.flush();
					out.close();
				}
			}
		}
		
		//其他作文	--	查看作文（技法）
		if(flag.equals("27")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> list = zuowen.selectJifa(id);
			JSONArray jsonmap = JSONArray.fromObject(list);
			String string = jsonmap.get(0).toString();
			PrintWriter out =null;
			try {
				out = response.getWriter();
				out.print(string);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if (out!=null) {
					out.flush();
					out.close();
				}
			}
		}
		
		//其他作文	--	查看作文（素材）
		if(flag.equals("28")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> list = zuowen.selectSucai(id);
			JSONArray jsonmap = JSONArray.fromObject(list);
			String string = jsonmap.get(0).toString();
			PrintWriter out =null;
			try {
				out = response.getWriter();
				out.print(string);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if (out!=null) {
					out.flush();
					out.close();
				}
			}
		}
		
		//其他作文	--	获取值到添加页面（技法）
		if(flag.equals("29")){
			List<Map<String, Object>> geadelist = zuowen.selectGeadeSucai();//年级	：小学-高中
			//List<Map<String, Object>> stylelist = zuowen.selectStyle();//体裁 ：CODE_TYPE=013
			String dir = request.getSession().getServletContext().getRealPath("/jslib/fenlei.json");
			List<Map<String, String>> stylelist =  new ArrayList<Map<String, String>>();
	        try {
	            File file = new File(dir);
	            if (!file.exists()) {
	                file.createNewFile();
	            }
	           String str= FileUtils.readFileToString(file, "UTF-8");
	           JSONObject object = JSONObject.fromObject(str);
	           String string = object.get("jifa").toString();
	           Object parse = JSON.parse(string);
	           JSONArray jsonObject = JSONArray.fromObject(parse);
	           Iterator it = jsonObject.iterator();
	           while (it.hasNext()) {
	        	   Map<String, String> map = new HashMap<String, String>();
	                JSONObject ob = (JSONObject) it.next();
	                map.put("CODE", ob.getString("CODE"));
	                map.put("CODE_NAME", ob.getString("CODE_NAME"));
	                stylelist.add(map);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> list = zuowen.findJifa(id);
			if(list.size()>0){
				if(list.get(0).get("type")!=null){
					String string = list.get(0).get("type").toString();
					String string2 = "";
					for(int j=0;j<stylelist.size();j++){
						if(stylelist.get(j).get("CODE").equals(string)){
							string2 = stylelist.get(j).get("CODE");
						}
					}
					if(string2==""){
						if(list.get(0).get("style")!=null){
							String style = list.get(0).get("style").toString();
							for(int j=0;j<stylelist.size();j++){
								if(stylelist.get(j).get("CODE").equals(style)){
									string2 = stylelist.get(j).get("CODE");
								}
							}
						}
					}
					list.get(0).put("style", string2);
				}else {
					if(list.get(0).get("style")!=null){
						String string = list.get(0).get("style").toString();
						String string2 = "";
						for(int j=0;j<stylelist.size();j++){
							if(stylelist.get(j).get("CODE").equals(string)){
								string2 = stylelist.get(j).get("CODE_NAME");
							}
						}
						list.get(0).put("style", string2);
					}
				}
				if (list.get(0).get("content")!=null && !list.get(0).get("content").equals("")) {
					String yuanxing = list.get(0).get("content").toString();
					String newStr = CommUtils.deleteXmlForComposition(yuanxing);
					list.get(0).put("content", newStr);
				}
			}
			request.setAttribute("geadelist", geadelist);
			request.setAttribute("stylelist", stylelist);
			request.setAttribute("list", list);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/tuiJian/tuiJian_add2.jsp").forward(request, response);
		}
		
		//其他作文	--	获取值到添加页面（素材）
		if(flag.equals("30")){
			List<Map<String, Object>> geadelist = zuowen.selectGeadeSucai();//素材年级	：小学-高中
			//List<Map<String, Object>> stylelist = zuowen.selectStyle();//体裁 ：CODE_TYPE=013
			String dir = request.getSession().getServletContext().getRealPath("/jslib/fenlei.json");
			List<Map<String, String>> stylelist =  new ArrayList<Map<String, String>>();
	        try {
	            File file = new File(dir);
	            if (!file.exists()) {
	                file.createNewFile();
	            }
	           String str= FileUtils.readFileToString(file, "UTF-8");
	           JSONObject object = JSONObject.fromObject(str);
	           String string = object.get("sucai").toString();
	           Object parse = JSON.parse(string);
	           JSONArray jsonObject = JSONArray.fromObject(parse);
	           Iterator it = jsonObject.iterator();
	           while (it.hasNext()) {
	        	   Map<String, String> map = new HashMap<String, String>();
	                JSONObject ob = (JSONObject) it.next();
	                map.put("CODE", ob.getString("CODE"));
	                map.put("CODE_NAME", ob.getString("CODE_NAME"));
	                stylelist.add(map);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> list = zuowen.findSucai(id);
			if(list.size()>0){
				if (list.get(0).get("content")!=null && !list.get(0).get("content").equals("")) {
					String yuanxing = list.get(0).get("content").toString();
					String newStr = CommUtils.deleteXmlForComposition(yuanxing);
					list.get(0).put("content", newStr);
				}
			}
			request.setAttribute("geadelist", geadelist);
			request.setAttribute("stylelist", stylelist);
			request.setAttribute("list", list);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/tuiJian/tuiJian_add2.jsp").forward(request, response);
		}
		
		//已点评作文	--	删除
		if(flag.equals("31")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			int i = zuowen.deleteYidp(id);
			String str = "";
			if (i == 0) {
				str = "删除成功！作文状态恢复为待点评状态。";
			}else {
				str = "删除失败！";
			}
			PrintWriter out = response.getWriter();
			out.print(str);
			out.flush();
			out.close();
			//response.sendRedirect(path+"/servlet/ManageZuowenServlet?do=3");
		}
		//待点评评作文	--	删除
		if(flag.equals("32")){
			String id = request.getParameter("id");
			Cookie[] cs = request.getCookies();
			String adminId = "";
			for (Cookie cookie : cs) {
				if ("adminId".equals(cookie.getName())) {
					adminId = cookie.getValue();
				}
			}
			int i = 0;
			List<Map<String,Object>> list = zuowen.selectSorder(id);
			if(list.size()>0){
				Object platId = list.get(0).get("PLATFORM_ID");//
				Object out_request_no = list.get(0).get("OUT_TRADE_NO");// out_request_no 唯一退款标志
				if(platId!=null&&platId!=""&&Constant.YUE_ZUO_YE.equals(platId.toString())){
					zuowen.insetRefund(id, adminId);
					String jsonData = "{\"bm\":{\"outRefundNo\":\""+out_request_no+"\"}}";
					JSONObject tempJson = JSONObject.fromObject(jsonData);
					JSONObject resultJson = HttpUtils.doPost(Constant.YUE_ZUO_YE_BACK, tempJson);
					i = 1;
				}else{
					i = zuowen.deleteDaidp(id, adminId);
				}
			}
			//int i = zuowen.deleteDaidp(id, adminId);
			String str = "";
			if (i == 0) {
				str = "修改数据库失败";
			}else if (i == 1) {
				str = "删除成功";
			}else if (i == 2) {
				str = "获取退款信息失败";
			}else if (i == 3){
				str = "退款异常";
			}else if (i == 4){
				str = "退款失败";
			}else if (i == 5){
				str = "获取原作文异常";
			}
			PrintWriter out = response.getWriter();
			out.print(str);
			out.flush();
			out.close();
			//response.sendRedirect(path+"/servlet/ManageZuowenServlet?do=1");
		}
		//评价	--	删除
		if(flag.equals("33")){
			String id = request.getParameter("id");
			String cid = request.getParameter("cid");
			String userType = request.getParameter("userType");
			String index = request.getParameter("index");
			System.out.println(index);
			int i = zuowen.deletePingJia(id, cid, userType, index);
			String result = null;
			if (i > 0) {
				result = "no";
			}else {
				result = "yes";
			}
			PrintWriter out = response.getWriter();
			out.print(result);
			out.flush();
			out.close();
			//response.sendRedirect(path+"/servlet/ManageZuowenServlet?do=22&id="+cid);
		}
		
		/**
		 * 老师用户集合   - 条件+分页
		 */
		if(flag.equals("34")){
			//条件
			String sname = request.getParameter("name");//用户名
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
			//老师用户集合
			CurrentPage page = zuowen.findTeacherlist(currentPage,numPerPage,name);
			List<Map<String, Object>> list = page.getResultList();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					if(list.get(i).get("EDU_TIME")!=null){
						String times = list.get(i).get("EDU_TIME").toString();
						try {
							//转换提交时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							list.get(i).put("times", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(list.get(i).get("createTime")!=null){
						String times = list.get(i).get("createTime").toString();
						try {
							//转换提交时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							list.get(i).put("createTime", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(list.get(i).get("grade")!=null){
						String grade = list.get(i).get("grade").toString();
						Object[] parseAge = CommUtils.parseAge(grade);
						String gradename = parseAge[1].toString();
						list.get(i).put("grade", gradename);
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("page", page);
			request.setAttribute("list", list);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/daiDianPing/teacher.jsp").forward(request, response);
//			PrintWriter out = response.getWriter();
//			out.print(list);
//			out.flush();
//			out.close();
		}
		
		/**
		 * 修改点评老师
		 */
		if(flag.equals("35")){
			//条件
			String comid = request.getParameter("id");//作文id
			String newTid = request.getParameter("tid");//老师信息
			int i = zuowen.updateTeacher(comid,newTid);
			if(i>0){
				PrintWriter writer = response.getWriter();
		        writer.write("修改老师成功!");
		        writer.flush();
				//response.sendRedirect(path+"/servlet/ManageZuowenServlet?do=1");
			}
		}
		
		/**
		 * 指派点评老师
		 */
		if(flag.equals("36")){
			//条件
			String comid = request.getParameter("id");//作文id
			String tid = request.getParameter("tid");//老师信息
			int i = zuowen.assignTeacher(comid,tid);
			PrintWriter writer = response.getWriter();
			if(i>0){
				writer.write("指派老师成功!");
				writer.flush();
			}else {
				writer.write("指派老师成失败!");
				writer.flush();
			}
		}
		
		/* 已点评-推荐  */
		if(flag.equals("37")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			
			List<Map<String, Object>> geadelist = zuowen.selectGeade();//年级	：一年级到高三
			request.setAttribute("geadelist", geadelist);
			List<Map<String, Object>> stylelist = zuowen.selectStyless();//体裁 ：CODE_TYPE=013，033
			//List<Map<String, Object>> stylelist = zuowen.selectStyle2();//体裁 ：CODE_TYPE=013
			request.setAttribute("zwStyle", stylelist);
			
			List<Map<String, Object>> list = zuowen.recommendYidp(id);
			String host = CommUtils.getServerHost();
			if(list.size()>0){
				if (list.get(0).get("content")!=null && !list.get(0).get("content").equals("")) {
					String yuanxing = list.get(0).get("content").toString();
					String newStr = CommUtils.deleteXmlForComposition(yuanxing);
					list.get(0).put("content", newStr);
				}
				if(list.get(0).get("voice")!=null&&!list.get(0).get("voice").equals("")){
					String pathsss = "F:/server/cedu-files"+list.get(0).get("voice").toString();
					String base64 = CommUtils.fileToBase64(pathsss);
					list.get(0).put("voice", base64);
				}else {
					list.get(0).put("voice", "");
				}
				
				if(list.get(0).get("img1")!=null&&!list.get(0).get("img1").equals("")){
					list.get(0).put("img1", host+list.get(0).get("img1").toString());
				}
				if(list.get(0).get("img2")!=null&&!list.get(0).get("img2").equals("")){
					list.get(0).put("img2", host+list.get(0).get("img2").toString());
				}
				if(list.get(0).get("img3")!=null&&!list.get(0).get("img3").equals("")){
					list.get(0).put("img3", host+list.get(0).get("img3").toString());
				}
				if(list.get(0).get("geade")!=null&&!list.get(0).get("geade").equals("")){
					Object[] parseAge = CommUtils.parseAge(list.get(0).get("geade").toString());
					String ageDetail = parseAge[1].toString();
					list.get(0).put("age_detail", ageDetail);
				}
			}
			request.setAttribute("geadelist", geadelist);
			request.setAttribute("stylelist", stylelist);
			request.setAttribute("list", list);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/yiDianPing/tuiJian_add3.jsp").forward(request, response);
		}
		
		//已点评-添加推荐作文	提交
		if(flag.equals("38")){
			String sid = request.getParameter("zwId");
			int id = Integer.parseInt(sid);
			String title = request.getParameter("zwTitle");
			String content = request.getParameter("content");
			String author = request.getParameter("zwName");
			String nj = request.getParameter("zwGrade");
			String style = request.getParameter("zwStyle");
			
			int i = zuowen.insertTuijian2(id,title,content,author,nj,style);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageZuowenServlet?do=3");
			}
		}
		
		//查看已点评作文
		if(flag.equals("39")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> list = zuowen.selectTuijianYdp(id);
			String host = CommUtils.getServerHost();
			List<Map<String, Object>> pingfen = null;
			if(list.size()>0){
				if(list.get(0).get("content")!=null&&list.get(0).get("content")!=""){
					String textareaText = "<p style=\"text-indent: 2em;\">"+list.get(0).get("content").toString();
					String replaceAll = textareaText.replaceAll("\\n","<p style=\"text-indent: 2em;\">");
					list.get(0).put("content", replaceAll);
				}
				if(list.get(0).get("voice")!=null&&!list.get(0).get("voice").equals("")){
					String pathsss = "F:/server/cedu-files"+list.get(0).get("voice").toString();
					String base64 = CommUtils.fileToBase64(pathsss);
					list.get(0).put("voice", base64);
				}else {
					list.get(0).put("voice", "");
				}
				
				if(list.get(0).get("img1")!=null&&!list.get(0).get("img1").equals("")){
					list.get(0).put("img1", host+list.get(0).get("img1").toString());
				}
				if(list.get(0).get("img2")!=null&&!list.get(0).get("img2").equals("")){
					list.get(0).put("img2", host+list.get(0).get("img2").toString());
				}
				if(list.get(0).get("img3")!=null&&!list.get(0).get("img3").equals("")){
					list.get(0).put("img3", host+list.get(0).get("img3").toString());
				}
				if(list.get(0).get("geade")!=null&&!list.get(0).get("geade").equals("")){
					Object[] parseAge = CommUtils.parseAge(list.get(0).get("geade").toString());
					String ageDetail = parseAge[1].toString();
					list.get(0).put("geade", ageDetail);
					//评分标准信息
					pingfen = zuowen.pingfen(ageDetail);
				}
				if(list.get(0).get("createTime")!=null&&!list.get(0).get("createTime").equals("")){
					String times = list.get(0).get("createTime").toString();
					try {
						//转换提交时间
						Date parse = df.parse(times);
						String format = df.format(parse);
						list.get(0).put("createTimes", format);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(list.get(0).get("comTime")!=null&&!list.get(0).get("comTime").equals("")){
					String times = list.get(0).get("comTime").toString();
					try {
						//转换提交时间
						Date parse = df.parse(times);
						String format = df.format(parse);
						list.get(0).put("comTimes", format);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(list.get(0).get("scoring")!=null&&!list.get(0).get("scoring").equals("")){
					String str = list.get(0).get("scoring").toString();
					String scoring = str.replaceAll("\\n","<br>");
					list.get(0).put("scoring", scoring);
				}
				if(list.get(0).get("points")!=null&&!list.get(0).get("points").equals("")){
					String str = list.get(0).get("points").toString();
					String points = str.replaceAll("\\n","<br>");
					list.get(0).put("points", points);
				}
				if(list.get(0).get("suggest")!=null&&!list.get(0).get("suggest").equals("")){
					String str = list.get(0).get("suggest").toString();
					String suggest = str.replaceAll("\\n","<br>");
					list.get(0).put("suggest", suggest);
				}
			}
			request.setAttribute("list", list);
			request.setAttribute("pingfen", pingfen);
			request.getRequestDispatcher("/WEB-INF/views/zuoWenGuanLi/tuiJian/tuijianYdp_select.jsp").forward(request, response);
		}
	}
	
	public void init() throws ServletException {
		// Put your code here
	}
}
