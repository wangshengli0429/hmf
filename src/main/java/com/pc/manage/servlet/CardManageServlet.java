package com.pc.manage.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

import com.pc.manage.dao.CardManageDaoImpl;
import com.util.CommUtils;
import com.util.CurrentPage;
@WebServlet(name = "CardManageServlet", urlPatterns = "/servlet/CardManageServlet")  //标记为servlet，以便启动器扫描。
public class CardManageServlet extends HttpServlet{
	
	public CardManageServlet() {
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
		CardManageDaoImpl card = new CardManageDaoImpl();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		int currentPage = 1;
		int numPerPage = 10;
		
		/**
		 * 学生用户集合   - 条件+分页 - （评点卡赠送）
		 */
		if(flag.equals("1")){
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
			//学生用户集合
			CurrentPage page = card.findStudentlist(currentPage,numPerPage,name);
			List<Map<String, Object>> list = page.getResultList();
			if(list.size()==0){
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("page", page);
			request.setAttribute("list", list);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/commentCard/cardManage/student.jsp").forward(request, response);
		}
		
		/**
		 * 评点卡管理   - 条件+分页
		 */
		if(flag.equals("2")){
			//条件
			String sname = request.getParameter("name");//名称
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
			//评点卡集合
			CurrentPage page = card.findCardlist(currentPage,numPerPage,name);
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
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/commentCard/cardManage/cardManage.jsp").forward(request, response);
		}
		
		/**
		 * 删除评点卡
		 */
		if(flag.equals("3")){
			String id = request.getParameter("id");
			int i = card.deleteCard(id);
			PrintWriter out = response.getWriter();
			if(i>0){
				out.print("删除成功！");
			}else{
				out.print("删除失败！");
			}
			out.flush();
			out.close();
		}
		
		/**
		 * 创建评点卡
		 */
		if(flag.equals("4")){
			String name = request.getParameter("name");
			String count = request.getParameter("count");
			String validitytime = request.getParameter("validitytime");
			String price = request.getParameter("price");
			String old_price = request.getParameter("old_price");
			int i = card.addCard(name,count,validitytime,price,userId,old_price);
			PrintWriter out = response.getWriter();
			if(i>0){
				out.print("创建成功！");
			}else{
				out.print("创建失败！");
			}
			out.flush();
			out.close();
			//response.sendRedirect(path + "/servlet/CardManageServlet?do=2");
		}
		
		/**
		 * 修改评点卡（回显）
		 */
		if(flag.equals("5")){
			String id = request.getParameter("id");
			List<Map<String, Object>> list = card.getCardByid(id);
			request.setAttribute("list", list);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/commentCard/cardManage/card_update.jsp").forward(request, response);
		}
		
		/**
		 * 修改评点卡（保存）
		 */
		if(flag.equals("6")){
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String count = request.getParameter("count");
			String validitytime = request.getParameter("validitytime");
			String price = request.getParameter("price");
			String old_price = request.getParameter("old_price");
			int i = card.updateCard(id,name,count,validitytime,price,old_price,userId);
			PrintWriter out = response.getWriter();
			if(i>0){
				out.print("修改成功！");
			}else{
				out.print("修改失败！");
			}
			out.flush();
			out.close();
		}
		
		/**
		 * 赠送评点卡
		 */
		if (flag.equals("7")) {
			String sids = request.getParameter("sids");
			String cid = request.getParameter("cid");
			String result = card.giveCardToStu(sids, cid, suserId);
			PrintWriter out = response.getWriter();
			out.print(result);
			out.flush();
		}

		
		/**
		 * 次卡使用情况   - 条件+分页
		 */
		if(flag.equals("10")){
			//条件
			String sname = request.getParameter("name");//名称
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
			//次卡使用情况集合
			CurrentPage page = card.findFrequencylist(currentPage,numPerPage,name);
			List<Map<String, Object>> list = page.getResultList();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					if(list.get(i).get("END_TIME")!=null){
						String times = list.get(i).get("END_TIME").toString();
						try {
							//转换提交时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							list.get(i).put("END_TIME", format);
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
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/commentCard/frequency/frequency.jsp").forward(request, response);
		}
		
		/**
		 * 赠送评点卡情况   - 条件+分页
		 */
		if(flag.equals("11")){
			//条件
			String sname = request.getParameter("name");//名称
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
			//赠送评点卡集合
			CurrentPage page = card.findGiveCardlist(currentPage,numPerPage,name);
			List<Map<String, Object>> list = page.getResultList();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					if(list.get(i).get("START_TIME")!=null){
						String times = list.get(i).get("START_TIME").toString();
						try {
							//转换提交时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							list.get(i).put("START_TIME", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(list.get(i).get("START_TIME")!=null && list.get(i).get("END_TIME")!=null){
						String time = CommUtils.getDayBetween2Time(list.get(i).get("START_TIME").toString(), list.get(i).get("END_TIME").toString());
						list.get(i).put("time", time);
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("page", page);
			request.setAttribute("list", list);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/commentCard/giveCard/giveCard.jsp").forward(request, response);
		}
	}
	
	public void init() throws ServletException {
		// Put your code here
	}
}
