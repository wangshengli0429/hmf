package com.pc.manage.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import org.json.JSONException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.pc.manage.dao.ManageJiaoyiDaoImpl;
import com.util.BackRefundUtils;
import com.util.CommUtils;
import com.util.Constant;
import com.util.CurrentPage;
import com.util.ExportExcel;
import com.util.HttpUtils;
/**
 * 管理PC端-交易管理
 */
@WebServlet(name = "ManageJiaoyiServlet", urlPatterns = "/servlet/ManageJiaoyiServlet")  //标记为servlet，以便启动器扫描。
public class ManageJiaoyiServlet extends HttpServlet {

	public ManageJiaoyiServlet() {
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
		ManageJiaoyiDaoImpl jiaoyi = new ManageJiaoyiDaoImpl();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		int currentPage = 1;
		int numPerPage = 10;
		
		/**
		 * 作文订单管理		条件+分页
		 */
		if(flag.equals("1")){
			//条件
			String state = request.getParameter("state");//状态
			String payment = request.getParameter("payment");//支付方式
			String startDate = request.getParameter("startDate");//时间
			String endDate = request.getParameter("endDate");//时间
			String sname = request.getParameter("name");//编号、用户
			
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
			//订单集合
			CurrentPage page = jiaoyi.findDingdan(currentPage,numPerPage,state,payment,startDate,endDate,name);
			List<Map<String, Object>> ddlist = page.getResultList();
			if(ddlist.size()>0){
				for(int i=0;i<ddlist.size();i++){
					if(ddlist.get(i).get("gmtpayment")!=null){
						String times = ddlist.get(i).get("gmtpayment").toString();
						try {
							//转换支付时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							ddlist.get(i).put("gmtpayment", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(ddlist.get(i).get("payment")!=null){
						String payment1 = ddlist.get(i).get("payment").toString();
						if(payment1.equals("01")){
							ddlist.get(i).put("payment", "微信");
		                }else if(payment1.equals("02")){
		                	ddlist.get(i).put("payment", "支付宝");
		                }
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("page", page);
			request.setAttribute("ddlist", ddlist);
			request.setAttribute("state", state);
			request.setAttribute("payment", payment);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/jiaoYiGuanLi/dingDan/dingDan.jsp").forward(request, response);
		}
		
		/**
		 * 退款/售后管理
		 */
		//待处理		条件+分页
		if(flag.equals("2")){
			//条件
			String sname = request.getParameter("name");//编号、用户
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
			//待处理集合
			CurrentPage page = jiaoyi.findTuikuan(currentPage,numPerPage,name);
			List<Map<String, Object>> tklist = page.getResultList();
			if(tklist.size()>0){
				for(int i=0;i<tklist.size();i++){
					if(tklist.get(i).get("time")!=null){
						String times = tklist.get(i).get("time").toString();
						try {
							//转换申请时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							tklist.get(i).put("time", format);
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
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/jiaoYiGuanLi/tuiKuan/tuikuan_dai.jsp").forward(request, response);
		}
		
		//待处理	查看退款作文
		if(flag.equals("3")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> tkzw = jiaoyi.selectTkzw(id);
			String host = CommUtils.getServerHost();
			if(tkzw.size()>0){
				for(int i=0;i<tkzw.size();i++){
					if(tkzw.get(i).get("time")!=null&&!tkzw.get(i).get("time").equals("")){
						String times = tkzw.get(i).get("time").toString();
						try {
							//转换申请时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							tkzw.get(i).put("time", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(tkzw.get(i).get("img1")!=null&&!tkzw.get(i).get("img1").equals("")){
						tkzw.get(i).put("img1", host+tkzw.get(i).get("img1").toString());
					}
					if(tkzw.get(i).get("img2")!=null&&!tkzw.get(i).get("img2").equals("")){
						tkzw.get(i).put("img2", host+tkzw.get(i).get("img2").toString());
					}
					if(tkzw.get(i).get("img3")!=null&&!tkzw.get(i).get("img3").equals("")){
						tkzw.get(i).put("img3", host+tkzw.get(i).get("img3").toString());
					}
					if(tkzw.get(i).get("geade")!=null&&!tkzw.get(i).get("geade").equals("")){
						Object[] parseAge = CommUtils.parseAge(tkzw.get(i).get("geade").toString());
						String ageDetail = parseAge[1].toString();
						tkzw.get(i).put("geade", ageDetail);
					}
				}
			}
			request.setAttribute("tkzw", tkzw);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/jiaoYiGuanLi/tuiKuan/weitkzuowen_select.jsp").forward(request, response);
		}
		
		//待处理	处理-查看
		if(flag.equals("4")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> tkcl = jiaoyi.selectChuli(id);
			if(tkcl.size()>0){
				for(int i=0;i<tkcl.size();i++){
					if(tkcl.get(i).get("time")!=null){
						String times = tkcl.get(i).get("time").toString();
						try {
							//转换申请时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							tkcl.get(i).put("time", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(tkcl.get(i).get("payment")!=null){
						String payment1 = tkcl.get(i).get("payment").toString();
						if(payment1.equals("01")){
							tkcl.get(i).put("payment", "微信");
		                }else if(payment1.equals("02")){
		                	tkcl.get(i).put("payment", "支付宝");
		                }
					}
				}
			}
			request.setAttribute("tkcl", tkcl);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/jiaoYiGuanLi/tuiKuan/daiChuLi_chuli.jsp").forward(request, response);
		}
		
		//待处理	处理-提交
		if(flag.equals("5")){
			String tkid = request.getParameter("id");//退款id
			int id = Integer.parseInt(tkid);
			String stuid = request.getParameter("sid");//学生id
			int sid = Integer.parseInt(stuid);
			String sstate = request.getParameter("state");
			int state = Integer.parseInt(sstate);
			String ddbh = request.getParameter("ddbh");//订单编号
			String title = request.getParameter("title");//作文标题
			String backRefund = "";
			List<Map<String, Object>> refund = jiaoyi.findOrder(id);
			if(state==2){//同意退款	--阿里退款  backRefund
				//jiaoyi.tuikuan(ddbh);
				//查询退款订单
				if(refund.size()>0){
					Object out_trade_no = refund.get(0).get("out_trade_no");// 评点订单 有
					Object trade_no = refund.get(0).get("trade_no");// 支付宝订单  有
					Object refund_amount = refund.get(0).get("refund_amount");// 退款金额  有 对应用户付款金额
					
					// 支付类型
					Object payment = refund.get(0).get("PAYMENT");
					Object sign_type = refund.get(0).get("SIGN_TYPE");
					//判断支付账号
					Object appId = refund.get(0).get("APP_ID");
					Object platId = refund.get(0).get("PLATFORM_ID");
					
					String refund_reason = "正常退款";// 退款原因 
					Object out_request_no = refund.get(0).get("out_request_no");// out_request_no 唯一退款标志
					String operator_id = suserId.toString();// 管理员的ID
					String store_id = suserId.toString();// 管理员的ID
					String terminal_id = suserId.toString();// 管理员的ID
					
					//判断支付类型
					if(platId!=null&&platId!=""&&Constant.YUE_ZUO_YE.equals(platId.toString())){
						String jsonData = "{\"bm\":{\"outRefundNo\":\""+out_request_no+"\"}}";
						JSONObject tempJson = JSONObject.fromObject(jsonData);
						JSONObject resultJson = HttpUtils.doPost(Constant.YUE_ZUO_YE_BACK, tempJson);
						int r = jiaoyi.updateRefunds(id,userId);
						
					}else {
						if ("支付宝".equals(payment)) {//支付宝退款
							String json = "{\"out_trade_no\":\""+out_trade_no+"\",\"trade_no\":\""+trade_no+"\",\"refund_amount\":\""+refund_amount+"\",\"refund_reason\":\""+refund_reason+"\",\"out_request_no\":\""+out_request_no+"\",\"operator_id\":\""+operator_id+"\",\"store_id\":\""+store_id+"\",\"terminal_id\":\""+terminal_id+"\"}";
							try {
								backRefund = BackRefundUtils.zfbBackRefund(json, appId.toString());
								System.out.println("--------------------退款结果："+backRefund);
								if ("fail".equals(backRefund)) {
									request.setAttribute("result", "fail");
									request.getRequestDispatcher("/WEB-INF/views/adminPC/jiaoYiGuanLi/tuiKuan/tuikuan_dai.jsp").forward(request, response);
								}
							} catch (ParseException | JSONException e) {
								request.setAttribute("result", "fail");
								request.getRequestDispatcher("/WEB-INF/views/adminPC/jiaoYiGuanLi/tuiKuan/tuikuan_dai.jsp").forward(request, response);
							}
						}else if ("微信".equals(payment)) {//微信退款
							String s_order_id = refund.get(0).get("s_order_id").toString();//原订单id
							Map<String, Object> wxMap = jiaoyi.findS_orderByWx(s_order_id);
							if (wxMap == null) {
								request.setAttribute("result", "fail");
								request.getRequestDispatcher("/WEB-INF/views/adminPC/jiaoYiGuanLi/tuiKuan/tuikuan_dai.jsp").forward(request, response);
							}
							wxMap.put("out_request_no", out_request_no);
							wxMap.put("appId", appId);
							JSONArray js = JSONArray.fromObject(wxMap);
							String json = js.toString();
							try {
								backRefund = BackRefundUtils.wxBackRefund(json);
								System.out.println("--------------------退款结果："+backRefund);
								if ("fail".equals(backRefund)) {
									request.setAttribute("result", backRefund);
									request.getRequestDispatcher("/WEB-INF/views/adminPC/jiaoYiGuanLi/tuiKuan/tuikuan_dai.jsp").forward(request, response);
								}
							} catch (ParseException | JSONException e) {
								request.setAttribute("result", "fail");
								request.getRequestDispatcher("/WEB-INF/views/adminPC/jiaoYiGuanLi/tuiKuan/tuikuan_dai.jsp").forward(request, response);
							}
						}else {
							String sql9 = "UPDATE stu_dpcard_use SET USE_COUNT = USE_COUNT - 1,REMAINING_COUNT = REMAINING_COUNT + 1,STATE = 0 WHERE STU_ID = ?";
							JdbcTemplate jt = CommUtils.getJdbcTemplate();
							int i = jt.update(sql9, new Object[]{sid});
							if (i > 0) {
								backRefund = "success";
							}
						}
						int i = jiaoyi.updateChuli(id,sid,title,state,userId,ddbh,backRefund);
					}
				}
			}else if(state==3){
				Object platId = refund.get(0).get("PLATFORM_ID");
				Object out_request_no = refund.get(0).get("out_request_no");// out_request_no 唯一退款标志
				int j = jiaoyi.updateChuli(id,sid,title,state,userId,ddbh,backRefund);
				if(j>0){
					if(platId!=null&&platId!=""&&Constant.YUE_ZUO_YE.equals(platId.toString())){
						String jsonData = "{\"bm\":{\"outRefundNo\":\""+out_request_no+"\"}}";
						JSONObject tempJson = JSONObject.fromObject(jsonData);
						JSONObject resultJson = HttpUtils.doPost(Constant.YUE_ZUO_YE_BACK_REFUSE, tempJson);
					}
				}
			}
			response.sendRedirect(path+"/servlet/ManageJiaoyiServlet?do=2");
		}
		
		//已处理		条件+分页
		if(flag.equals("6")){
			//条件
			String state = request.getParameter("state");//状态
			String sname = request.getParameter("name");//编号、用户
			
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
			//已处理集合
			CurrentPage page = jiaoyi.findYichuli(currentPage,numPerPage,state,name);
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
			request.getRequestDispatcher("/WEB-INF/views/adminPC/jiaoYiGuanLi/tuiKuan/tuikuan_yi.jsp").forward(request, response);
		}
		
		//已处理	查看退款作文
		if(flag.equals("7")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> tkzw = jiaoyi.selectTkzw(id);
			String host = CommUtils.getServerHost();
			if(tkzw.size()>0){
				for(int i=0;i<tkzw.size();i++){
					if(tkzw.get(i).get("time")!=null&&!tkzw.get(i).get("time").equals("")){
						String times = tkzw.get(i).get("time").toString();
						try {
							//转换申请时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							tkzw.get(i).put("time", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(tkzw.get(i).get("img1")!=null&&!tkzw.get(i).get("img1").equals("")){
						tkzw.get(i).put("img1", host+tkzw.get(i).get("img1").toString());
					}
					if(tkzw.get(i).get("img2")!=null&&!tkzw.get(i).get("img2").equals("")){
						tkzw.get(i).put("img2", host+tkzw.get(i).get("img2").toString());
					}
					if(tkzw.get(i).get("img3")!=null&&!tkzw.get(i).get("img3").equals("")){
						tkzw.get(i).put("img3", host+tkzw.get(i).get("img3").toString());
					}
					if(tkzw.get(i).get("geade")!=null&&!tkzw.get(i).get("geade").equals("")){
						Object[] parseAge = CommUtils.parseAge(tkzw.get(i).get("geade").toString());
						String ageDetail = parseAge[1].toString();
						tkzw.get(i).put("geade", ageDetail);
					}
				}
			}
			request.setAttribute("tkzw", tkzw);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/jiaoYiGuanLi/tuiKuan/yitkzuowen_select.jsp").forward(request, response);
		}
		
		//已处理	查看
		if(flag.equals("8")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			String states = request.getParameter("state");
			int state = Integer.parseInt(states);
			List<Map<String, Object>> tklist = jiaoyi.selectYitk(id,state);
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
					if(tklist.get(i).get("payment")!=null){
						String payment1 = tklist.get(i).get("payment").toString();
						if(payment1.equals("01")){
							tklist.get(i).put("payment", "微信");
		                }else if(payment1.equals("02")){
		                	tklist.get(i).put("payment", "支付宝");
		                }
					}
				}
			}
			request.setAttribute("tklist", tklist);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/jiaoYiGuanLi/tuiKuan/yiChuLi_select.jsp").forward(request, response);
		}
		
		/**
		 * 结算管理
		 */
		//生成账单
		if(flag.equals("9")){
			String clearingTime = request.getParameter("clearingTime");//时间
			int i = jiaoyi.order(clearingTime,userId);
			response.sendRedirect(path+"/servlet/ManageJiaoyiServlet?do=10");
			
		}
		
		//查询账单
		if(flag.equals("10")){
			String clearingTime = request.getParameter("clearingTime");//时间
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
			CurrentPage page = jiaoyi.findZhangdan(clearingTime,name,currentPage, numPerPage);
			List<Map<String, Object>> zdlist = page.getResultList();
			if(zdlist.size()>0){
				for(int i=0;i<zdlist.size();i++){
					if(zdlist.get(i).get("cleartime")!=null){
						String times = zdlist.get(i).get("cleartime").toString();
						try {
							//转换时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							zdlist.get(i).put("cleartime", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(zdlist.get(i).get("billTime")!=null){
						String times = zdlist.get(i).get("billTime").toString();
						try {
							//转换时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							zdlist.get(i).put("billTime", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("clearingTime", clearingTime);
			request.setAttribute("name", name);
			request.setAttribute("page", page);
			request.setAttribute("zdlist", zdlist);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/jiaoYiGuanLi/jieSuan/jieSuan.jsp").forward(request, response);
		}
		
		//查看未结算
		if(flag.equals("11")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			//查询未结算基本信息
			List<Map<String, Object>> wjsInfor = jiaoyi.findWeiinfor(id);
			if(wjsInfor.size()>0){
				if(wjsInfor.get(0).get("billTime")!=null){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
					String times = wjsInfor.get(0).get("billTime").toString();//生成账单时间
					try {
						Date parse = sdf.parse(times);
						String format = sdf.format(parse);
						wjsInfor.get(0).put("time", format);//所属年月
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			String scurrentPage = request.getParameter("currentPage");//当前页
			if(scurrentPage!=null&&scurrentPage!=""){
				currentPage = Integer.parseInt(scurrentPage);
			}
			String snumPerPage = request.getParameter("numPerPage");//每页记录数
			if(snumPerPage!=null&&snumPerPage!=""){
				numPerPage = Integer.parseInt(snumPerPage);
			}
			CurrentPage page = jiaoyi.findWeijiesuan(id,currentPage, numPerPage);
			List<Map<String, Object>> wjslist = page.getResultList();
			if(wjslist.size()>0){
				/*if(wjslist.get(0).get("tname")!=null){
					String tname = wjslist.get(0).get("tname").toString();
					request.setAttribute("tername", tname);
				}
				if(wjslist.get(0).get("phone")!=null){
					String phone = wjslist.get(0).get("phone").toString();
					request.setAttribute("terphone", phone);
				}
				if(wjslist.get(0).get("billTime")!=null){
					String times = wjslist.get(0).get("billTime").toString();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
					try {
						Date parse = sdf.parse(times);
						String format2 = sdf.format(parse);
						request.setAttribute("time", format2);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}*/
				for(int i=0;i<wjslist.size();i++){
					if(wjslist.get(i).get("comTime")!=null){
						String times = wjslist.get(i).get("comTime").toString();
						
						try {
							//转换时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							wjslist.get(i).put("comTime", format);
							
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					wjslist.get(i).put("total", "0");
				}
			}
			request.setAttribute("id", id);
			request.setAttribute("wjsInfor", wjsInfor);
			request.setAttribute("wjslist", wjslist);
			request.setAttribute("page", page);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/jiaoYiGuanLi/jieSuan/weiJieSuan_select.jsp").forward(request, response);
		}
		
		//查看已结算
		if(flag.equals("12")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			//查询已结算基本信息
			List<Map<String, Object>> yjsInfor = jiaoyi.findYiinfor(id);
			if(yjsInfor.size()>0){
				if(yjsInfor.get(0).get("billTime")!=null){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
					String times = yjsInfor.get(0).get("billTime").toString();//生成账单时间
					String clearTime = yjsInfor.get(0).get("clearTime").toString();//结算时间
					try {
						Date parse = sdf.parse(times);
						String format = sdf.format(parse);
						yjsInfor.get(0).put("time", format);//所属年月
						Date parse2 = sdf.parse(clearTime);
						String format2 = sdf.format(parse2);
						yjsInfor.get(0).put("clearTime", format2);//结算时间
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			String scurrentPage = request.getParameter("currentPage");//当前页
			if(scurrentPage!=null&&scurrentPage!=""){
				currentPage = Integer.parseInt(scurrentPage);
			}
			String snumPerPage = request.getParameter("numPerPage");//每页记录数
			if(snumPerPage!=null&&snumPerPage!=""){
				numPerPage = Integer.parseInt(snumPerPage);
			}
			//已结算订单
			CurrentPage page = jiaoyi.findYijiesuan(id,currentPage, numPerPage);
			List<Map<String, Object>> yjslist = page.getResultList();
			if(yjslist.size()>0){
				for(int i=0;i<yjslist.size();i++){
					if(yjslist.get(i).get("comTime")!=null){
						String times = yjslist.get(i).get("comTime").toString();
						
						try {
							//转换时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							yjslist.get(i).put("comTime", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					yjslist.get(i).put("total", "0");
				}
			}
			request.setAttribute("id", id);
			request.setAttribute("yjsInfor", yjsInfor);
			request.setAttribute("yjslist", yjslist);
			request.setAttribute("page", page);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/jiaoYiGuanLi/jieSuan/yiJieSuan_select.jsp").forward(request, response);
		}
		
		//未结算	结算
		if(flag.equals("13")){
			String sid = request.getParameter("id");//账单id
			int id = Integer.parseInt(sid);
			//String snum = request.getParameter("num");
			//int num = Integer.parseInt(snum);
			//String sprice = request.getParameter("price");
			//double price = Double.valueOf(sprice);
			int i = jiaoyi.settlement(id);
			response.sendRedirect(path+"/servlet/ManageJiaoyiServlet?do=10");
		}
		
		//导出账单
		if(flag.equals("14")){
			String ids = request.getParameter("ids");
			String title ="结算账单";
			String[] rowsName = new String[]{"老师姓名", "老师账号", "点评篇数（篇）", "应结金额（元）", "操作人员账号","结算状态","生成账单时间","结算时间"};
			
			try {
				List<Map<String, Object>> zdList = jiaoyi.exportByidsZd(ids);
				List<Object[]>  dataList = new ArrayList<Object[]>();
				for (int i=0;i<zdList.size();i++) {
					Object[] obj = null;
					obj = new Object[rowsName.length];
					obj[0] = zdList.get(i).get("tname");
					obj[1] = zdList.get(i).get("tphone");
					obj[2] = zdList.get(i).get("num");
					obj[3] = zdList.get(i).get("price");
					obj[4] = zdList.get(i).get("userName");
					if(zdList.get(i).get("state")!=null){
						obj[5] = "已结算";
					}else{
						obj[5] = "未结算 ";
					}
					if(zdList.get(i).get("billTime")!=null){
						String times = zdList.get(i).get("billTime").toString();
						
						try {
							//转换时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							obj[6] = format;
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						obj[6] = "";
					}
					if(zdList.get(i).get("cleartime")!=null){
						String times = zdList.get(i).get("cleartime").toString();
						
						try {
							//转换时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							obj[7] = format;
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						obj[7] = "";
					}
					dataList.add(obj);
				}
				ExportExcel exportExcel = new ExportExcel(title, rowsName, dataList, response);
				exportExcel.export();
				//response.sendRedirect(path+"/servlet/ManageJiaoyiServlet?do=10");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//导出	老师未结算订单
		if(flag.equals("15")){
			String zdids = request.getParameter("zdid");
			int id = Integer.parseInt(zdids);
			String ids = request.getParameter("ids");
			String title ="老师已点评订单";
			String[] rowsName = new String[]{"账单编号", "所属作文", "作者昵称", "收取佣金（元）", "支付金额（元）","点评时间"};
			
			try {
				List<Map<String, Object>> ddList = jiaoyi.exportDingdanwjs(id,ids);
				List<Object[]>  dataList = new ArrayList<Object[]>();
				for (int i=0;i<ddList.size();i++) {
					Object[] obj = null;
					obj = new Object[rowsName.length];
					obj[0] = ddList.get(i).get("outtradeNo");
					obj[1] = ddList.get(i).get("title");
					obj[2] = ddList.get(i).get("author");
					//obj[3] = ddList.get(i).get("total");
					obj[3] = 0;
					obj[4] = ddList.get(i).get("buyerpay");
					if(ddList.get(i).get("comTime")!=null){
						String times = ddList.get(i).get("comTime").toString();
						
						try {
							//转换时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							obj[5] = format;
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						obj[5] = "";
					}
					dataList.add(obj);
				}
				ExportExcel exportExcel = new ExportExcel(title, rowsName, dataList, response);
				exportExcel.export();
				//response.sendRedirect(path+"/servlet/ManageJiaoyiServlet?do=10");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//导出	老师已结算订单
		if(flag.equals("16")){
			String zdids = request.getParameter("zdid");
			int id = Integer.parseInt(zdids);
			String ids = request.getParameter("ids");
			String title ="老师已点评订单";
			String[] rowsName = new String[]{"账单编号", "所属作文", "作者昵称", "收取佣金（元）", "支付金额（元）","点评时间"};
			
			try {
				List<Map<String, Object>> ddList = jiaoyi.exportDingdanyi(id,ids);
				List<Object[]>  dataList = new ArrayList<Object[]>();
				for (int i=0;i<ddList.size();i++) {
					Object[] obj = null;
					obj = new Object[rowsName.length];
					obj[0] = ddList.get(i).get("outtradeNo");
					obj[1] = ddList.get(i).get("title");
					obj[2] = ddList.get(i).get("author");
					//obj[3] = ddList.get(i).get("total");
					obj[3] = 0;
					obj[4] = ddList.get(i).get("buyerpay");
					if(ddList.get(i).get("comTime")!=null){
						String times = ddList.get(i).get("comTime").toString();
						
						try {
							//转换时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							obj[5] = format;
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						obj[5] = "";
					}
					dataList.add(obj);
				}
				ExportExcel exportExcel = new ExportExcel(title, rowsName, dataList, response);
				exportExcel.export();
				//response.sendRedirect(path+"/servlet/ManageJiaoyiServlet?do=10");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 收益订单管理		条件+分页
		 */
		if(flag.equals("18")){
			//条件
			String startDate = request.getParameter("startDate");//开始时间
			String endDate = request.getParameter("endDate");//结束时间
			String sname = request.getParameter("name");//编号、用户
			
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
			//收益订单集合
			CurrentPage page = jiaoyi.findProfitOrder(currentPage,numPerPage,startDate,endDate,name);
			List<Map<String, Object>> list = page.getResultList();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					if(list.get(i).get("gmtpayment")!=null){
						String times = list.get(i).get("gmtpayment").toString();
						try {
							//转换支付时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							list.get(i).put("gmtpayment", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(list.get(i).get("payment")!=null){
						String payment1 = list.get(i).get("payment").toString();
						if(payment1.equals("01")){
							list.get(i).put("payment", "微信");
		                }else if(payment1.equals("02")){
		                	list.get(i).put("payment", "支付宝");
		                }
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("page", page);
			request.setAttribute("list", list);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/jiaoYiGuanLi/profitOrder/profitOrder.jsp").forward(request, response);
		}
		
		
	}
	
	public void init() throws ServletException {
		// Put your code here
	}

}
