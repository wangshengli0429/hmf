package com.pc.teacher.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.pc.teacher.dao.TeacherFindDaoImpl;
import com.util.CommUtils;
import com.util.CurrentPage;
import com.util.TimeCycle;

public class TeacherFindServlet extends HttpServlet {
	
	private static Logger logger = Logger.getLogger(TeacherFindServlet.class);
	
	public TeacherFindServlet() {
		super();
	}
	
	public void destroy() {
		logger.info("-------this："+this);
		logger.info("-------this.hashCode："+this.hashCode());
		String name = Thread.currentThread().getName();
	    long threadl = Thread.currentThread().getId();
	    logger.info("------------servlet销毁：当前线程名字:"+name+",id："+threadl+"，毫秒值："+System.currentTimeMillis()+"------------------------");
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
				if ("teacherId".equals(cookie.getName())) {
					suserId = cookie.getValue();
					cookie.setMaxAge(60*60*3);
					response.addCookie(cookie);
				}
				if ("teacherPw".equals(cookie.getName())) {
					cookie.setMaxAge(60*60*3);
					response.addCookie(cookie);
				}
				if ("teacherName".equals(cookie.getName())) {
					cookie.setMaxAge(60*60*3);
					response.addCookie(cookie);
				}
			}
		}

		if (suserId == null || suserId.equals("")) {
			response.sendRedirect(path + "/teacherPC/teacher_login.jsp");
			return;
		}
		
				
		int userId = Integer.parseInt(suserId.toString());
		TeacherFindDaoImpl tfind = new TeacherFindDaoImpl();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		int currentPage = 1;
		int numPerPage = 10;
		
		//作文管理 
		if (flag.equals("1")) {
			//（年级,年级表grade） - ageDetail
			List<Map<String, Object>> ageDetail = tfind.findAgedetail();
			request.setAttribute("ageDetail", ageDetail);
			String geade = request.getParameter("geade");
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
			/*未点评List*/
			CurrentPage page = tfind.weidianpingList(geade, name, currentPage, numPerPage, userId);
			List<Map<String,Object>> wlist = page.getResultList();
			if(wlist.size()>0){
				for(int i=0;i<wlist.size();i++){
					TimeCycle t = new TimeCycle();
					if(wlist.get(i).get("createTime")!=null){
						String time = wlist.get(i).get("createTime").toString();
						try {
							//距提交时长
							SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date begin = dfs.parse(time);
							Date end = new Date();
							long between = (end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒
							long day = between/(24*3600);//天
							long hour = between%(24*3600)/3600;//时
							long minute = between%3600/60;//分
							wlist.get(i).put("day", day);
							wlist.get(i).put("hour", hour);
							wlist.get(i).put("minute", minute);
							//String time2 = t.getTime(time);
							//wlist.get(i).put("distanceTime", time2);
							//转换提交时间
							Date parse = df.parse(time);
							String format = df.format(parse);
							wlist.get(i).put("createTime1", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(wlist.get(i).get("geade")!=null){
						Object[] parseAge = CommUtils.parseAge(wlist.get(i).get("geade").toString());
						String grade = parseAge[1].toString();
						wlist.get(i).put("geade", grade);
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("page", page);
			request.setAttribute("wlist", wlist);
			request.setAttribute("geade", geade);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/teacherPC/zuoWenGuanLi/zwgl_center.jsp").forward(request, response);
		}
		
		//已点评-条件查询
		if(flag.equals("2")){
			//（年级,年级表grade） - ageDetail
			List<Map<String, Object>> ageDetail = tfind.findAgedetail();
			request.setAttribute("ageDetail", ageDetail);
			String geade = request.getParameter("geade");
			String sname = request.getParameter("name");
			
			String name = "";
			if (sname != null && !sname.equals("")) {
				name = sname.replaceAll(" ", "");
			}
			
			String startTime = request.getParameter("startDate");
			String endTime = request.getParameter("endDate");
			String scurrentPage = request.getParameter("currentPage");//当前页
			if(scurrentPage!=null&&scurrentPage!=""){
				currentPage = Integer.parseInt(scurrentPage);
			}
			String snumPerPage = request.getParameter("numPerPage");//每页记录数
			if(snumPerPage!=null&&snumPerPage!=""){
				numPerPage = Integer.parseInt(snumPerPage);
			}
			CurrentPage page = tfind.yidianpingList(geade, startTime, endTime, name, currentPage, numPerPage, userId);
			List<Map<String,Object>> resultList = page.getResultList();
			if(resultList.size()>0){
				for(int i=0;i<resultList.size();i++){
					String time = resultList.get(i).get("createTime").toString();
					String time2 = resultList.get(i).get("comtime").toString();
					try {
						//转换提交时间
						Date parse = df.parse(time);
						String format = df.format(parse);
						resultList.get(i).put("createTime1", format);
						//转换点评时间
						Date parse2 = df.parse(time2);
						String format2 = df.format(parse2);
						resultList.get(i).put("comtime1", format2);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(resultList.get(i).get("geade")!=null){
						Object[] parseAge = CommUtils.parseAge(resultList.get(i).get("geade").toString());
						String grade = parseAge[1].toString();
						resultList.get(i).put("geade", grade);
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("page", page);
			request.setAttribute("ylist", resultList);
			request.setAttribute("geade", geade);
			request.setAttribute("name", name);
			request.setAttribute("startDate", startTime);
			request.setAttribute("endDate", endTime);
			request.getRequestDispatcher("/teacherPC/zuoWenGuanLi/zwgl_center_ydp.jsp").forward(request, response);
		}
		
		//批改 未点评 - 查看、回显
		if(flag.equals("3")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			//点评作文年级
			List<Map<String, Object>> gradeList = tfind.grade(id);
			//String grade = gradeList.get(0).get("grade").toString();
			String grade = "";
			if(gradeList.size()>0){
				if(gradeList.get(0).get("grade")!=null){
					Object[] parseAge = CommUtils.parseAge(gradeList.get(0).get("grade").toString());
					grade = parseAge[1].toString();
				}
			}
			//评分标准信息
			List<Map<String, Object>> pingfen = tfind.pingfen(grade);
			//未点评作文
			List<Map<String, Object>> wdp = new ArrayList<Map<String, Object>>();
			tfind.updateCompositionTeacherState(id);
			
			wdp = tfind.getTemporary(id,userId);//暂存数据
			if(wdp.size()==0){
				wdp = tfind.toupdateWdp(id,userId);//未点评数据
				if(wdp.size()>0){
					if(wdp.get(0).get("content")!=null&&!wdp.get(0).get("content").equals("")){
						String temp = CommUtils.teaComReplace(wdp.get(0).get("content").toString());
						String textareaText = "<p style=\"text-indent: 2em;\">"+temp;
						String replaceAll = textareaText.replaceAll("\\n","<p style=\"text-indent: 2em;\">");
						wdp.get(0).put("content", replaceAll);
					}
				}
			}
			String host = CommUtils.getServerHost();
			if(wdp.size()>0){
				if(wdp.get(0).get("geade")!=null){
					Object[] parseAge = CommUtils.parseAge(wdp.get(0).get("geade").toString());
					String grade1 = parseAge[1].toString();
					wdp.get(0).put("geade1", grade1);
				}
				if(wdp.get(0).get("img1")!=null&&!wdp.get(0).get("img1").equals("")){
					wdp.get(0).put("img1", host+wdp.get(0).get("img1").toString());
				}
				if(wdp.get(0).get("img2")!=null&&!wdp.get(0).get("img2").equals("")){
					wdp.get(0).put("img2", host+wdp.get(0).get("img2").toString());
				}
				if(wdp.get(0).get("img3")!=null&&!wdp.get(0).get("img3").equals("")){
					wdp.get(0).put("img3", host+wdp.get(0).get("img3").toString());
				}
				if(wdp.get(0).get("voice") != null && !wdp.get(0).get("voice").equals("")){
					wdp.get(0).put("voice", host+wdp.get(0).get("voice").toString());
				}else{
					wdp.get(0).put("voice", "");
				}
			}
			request.setAttribute("grade", grade);
			request.setAttribute("pingfen", pingfen);
			request.setAttribute("wdp", wdp);
			request.setAttribute("compId", id);
			request.getRequestDispatcher("/teacherPC/zuoWenGuanLi/weidianping_update_fontImg.jsp").forward(request, response);
			/*String img = wdp.get(0).get("img1").toString();
			String content = wdp.get(0).get("content").toString();
			if(!"".equals(img)){
				if(!"".equals(content)){
					request.getRequestDispatcher("/teacherPC/zuoWenGuanLi/weidianping_update_fontImg.jsp").forward(request, response);
				}else{
					request.getRequestDispatcher("/teacherPC/zuoWenGuanLi/weidianping_update_img.jsp").forward(request, response);
				}
			}else if(!"".equals(content)){
				request.getRequestDispatcher("/teacherPC/zuoWenGuanLi/weidianping_update_font.jsp").forward(request, response);
			}*/
		}
		
		//查看已点评详情
		if(flag.equals("4")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> ydp = tfind.findYdp(id);
			String host = CommUtils.getServerHost();
			if(ydp.size()>0){
				if(ydp.get(0).get("geade")!=null&&!ydp.get(0).get("geade").equals("")){
					Object[] parseAge = CommUtils.parseAge(ydp.get(0).get("geade").toString());
					String grade1 = parseAge[1].toString();
					ydp.get(0).put("geade", grade1);
				}
				if(ydp.get(0).get("img1")!=null&&!ydp.get(0).get("img1").equals("")){
					ydp.get(0).put("img1", host+ydp.get(0).get("img1").toString());
				}
				if(ydp.get(0).get("img2")!=null&&!ydp.get(0).get("img2").equals("")){
					ydp.get(0).put("img2", host+ydp.get(0).get("img2").toString());
				}
				if(ydp.get(0).get("img3")!=null&&!ydp.get(0).get("img3").equals("")){
					ydp.get(0).put("img3", host+ydp.get(0).get("img3").toString());
				}
				if(ydp.get(0).get("voice")!=null&&!ydp.get(0).get("voice").equals("")){
					String pathsss = "F:/server/cedu-files"+ydp.get(0).get("voice").toString();
					String base64 = CommUtils.fileToBase64(pathsss);
					ydp.get(0).put("voice", base64);
				}else {
					ydp.get(0).put("voice", "");
				}
				
				if(ydp.get(0).get("scoring")!=null&&!ydp.get(0).get("scoring").equals("")){
					String str = ydp.get(0).get("scoring").toString();
					String scoring = str.replaceAll("\\n","<br>");
					ydp.get(0).put("scoring", scoring);
				}
				if(ydp.get(0).get("points")!=null&&!ydp.get(0).get("points").equals("")){
					String str = ydp.get(0).get("points").toString();
					String points = str.replaceAll("\\n","<br>");
					ydp.get(0).put("points", points);
				}
				if(ydp.get(0).get("suggest")!=null&&!ydp.get(0).get("suggest").equals("")){
					String str = ydp.get(0).get("suggest").toString();
					String suggest = str.replaceAll("\\n","<br>");
					ydp.get(0).put("suggest", suggest);
				}
			}
			request.setAttribute("ydp", ydp);
			request.getRequestDispatcher("/teacherPC/zuoWenGuanLi/yidianping_select.jsp").forward(request, response);
		}
		
		//批改 未点评 - 提交
		if(flag.equals("5")){
			String sid = "";
			int id = 0;
			String stid = "";
			Map<String, Object> student = null;
			int stuid = 0;
			String title = "";
			String content = "";
			String score = "";
			String grade = "";
			String geade = "";
			String pcontent = "";
			String planguage = "";
			String pstructure = "";
			String pwriting = "";
			String scoring = "";
			String points = "";
			String suggest = "";
			String categoryCa = "";
			String languageCa = "";
			String structureCa = "";
			String writingCa = "";
			try{
				sid = request.getParameter("id");
				id = Integer.parseInt(sid);
				stid = request.getParameter("udid");
				student = tfind.findstuid(stid);//查询学生 id
				stuid = Integer.parseInt(student.get("ID").toString());
				title = request.getParameter("title");//作文标题
				content = request.getParameter("content");//内容
				score = request.getParameter("score");//分数
				grade = request.getParameter("grade");//点评作文年级
				geade = request.getParameter("geade");//点评作文年级CODE
				pcontent = request.getParameter("pcontent");//评分标准-内容
				planguage = request.getParameter("planguage");//评分标准-语言（高中表达express）
				pstructure = request.getParameter("pstructure");//评分标准-结构（高中特征feature）
				pwriting = request.getParameter("pwriting");//评分标准-书写
				scoring = request.getParameter("scoring");//得分点
				points = request.getParameter("points");//失分点
				suggest = request.getParameter("suggest");//建议
			}catch(Exception e){
				logger.error("教师端PC 提交   request获取参数异常 Exception"+e.getMessage());
				e.printStackTrace();
			}
			logger.info("---教师端PC 提交 开始----");
			logger.info("grade:"+geade+",score:"+score + ",pcontent:"+pcontent+",planguage:"+planguage+",pstructure:"+
			pstructure+",pwriting:"+pwriting+",scoring:"+scoring+",points:"+points+",suggest:"+suggest);
			//判断点评内容是否完整
			boolean b = CommUtils.hasCompleteByCom_composition(geade, score, pcontent, planguage, pstructure, pwriting, scoring, points, suggest);
			if (!b) {
				int i = tfind.temporary(id,userId,stuid,content,score,geade,pcontent,planguage,pstructure,pwriting,scoring,points,suggest,categoryCa,languageCa,structureCa,writingCa);
				PrintWriter writer = response.getWriter();
		        writer.write("点评不完善，提交点评失败!");
		        writer.flush();
		        return;
			}
			try {
				List<Map<String, Object>> pfbz = tfind.findPfbz(grade,pcontent,planguage,pstructure,pwriting);//评分标准
				for(int i=0;i<pfbz.size();i++){
					if(pfbz.get(i).containsKey("content")){
						if(pfbz.get(i).get("content").toString().equals(pcontent)){
							categoryCa = pfbz.get(i).get("category").toString();
						}
					}
					if(pfbz.get(i).containsKey("language")){
						if(pfbz.get(i).get("language").toString().equals(planguage)){
							languageCa = pfbz.get(i).get("category").toString();
						}
					}
					if(pfbz.get(i).containsKey("structure")){
						if(pfbz.get(i).get("structure").toString().equals(pstructure)){
							structureCa = pfbz.get(i).get("category").toString();
						}
					}
					if(pfbz.get(i).containsKey("writing")){
						if(pfbz.get(i).get("writing").toString().equals(pwriting)){
							writingCa = pfbz.get(i).get("category").toString();
						}
					}
				}
			} catch (Exception e) {
				logger.error("教师端PC 提交  查询相应年纪异常 Exception"+e.getMessage());
				e.printStackTrace();
			}
			int i = tfind.addComment(id,userId,stuid,content,score,geade,pcontent,planguage,pstructure,pwriting,scoring,points,suggest,categoryCa,languageCa,structureCa,writingCa);
			logger.info("---教师端PC 提交 结束I:----"+i);
			if(i == 1){
				//添加消息
				tfind.addInformation(id,title,stuid);
				//response.sendRedirect(path+"/servlet/TeacherFindServlet?do=1");
				PrintWriter writer = response.getWriter();
		        writer.write("批改成功!");
		        writer.flush();
			}else {
				// 失败
				logger.error("教师端PC 提交失败i"+i+"---id:"+id+"-----userId:"+userId+"-------stuid:"+stuid+"-----content:"+content+"----score:"+score+"----geade:"+geade+"----pcontent:"+pcontent+"----planguage:"+planguage+"----pstructure:"+pstructure+"----pwriting:"+pwriting+"----scoring:"+scoring+"----points:"+points+"----suggest:"+suggest+"----categoryCa:"+categoryCa+"----languageCa:"+languageCa+"---structureCa:"+structureCa+"---writingCa:"+writingCa);
				PrintWriter writer = response.getWriter();
		        writer.write("提交失败，已为您暂存！");
		        writer.flush();
			}
		}
		
		//分配预点评作文(查询)
		if(flag.equals("6")){
			//作文表
			List<Map<String,Object>> listMap = tfind.selectYdp(userId);
			int a = listMap.size();
	        Random random = new Random();
	        int s = random.nextInt(a);
	        Map<String, Object> map = listMap.get(s);
	        //String grade = map.get("grade").toString();027013
	        Object[] parseAge = CommUtils.parseAge(map.get("grade").toString());
			String grade = parseAge[1].toString();
	        //评分标准信息
			List<Map<String, Object>> pingfen = tfind.pingfen(grade);
			map.put("grade", grade);
			request.getSession().setAttribute("list", map);
	        request.getSession().setAttribute("pingfen", pingfen);
	        request.getRequestDispatcher("/teacherPC/yuPiGai/yupigai_index.jsp").forward(request, response);
	        
	        //范文表
			/*int max=10010;
	        int min=10000;
	        Random random = new Random();
	        int id = random.nextInt(max)%(max-min+1) + min;
	        List<Map<String,Object>> listMap = tfind.yudianping(id);
	        String grade = listMap.get(0).get("grade").toString();
	        //评分标准信息
			List<Map<String, Object>> pingfen = tfind.pingfen(grade);
			request.getSession().setAttribute("pingfen", pingfen);
			request.getSession().setAttribute("list", listMap);
			request.getRequestDispatcher("/teacherPC/yuPiGai/yupigai_index.jsp").forward(request, response);*/
		}
		
		//预点评作文  -提交
		if(flag.equals("7")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			String content = request.getParameter("content");//内容
			String score = request.getParameter("score");//分数
			String grade = request.getParameter("grade");//点评作文年级
			String pcontent = request.getParameter("pcontent");//评分标准-内容
			String planguage = request.getParameter("planguage");//评分标准-语言（高中表达express）
			String pstructure = request.getParameter("pstructure");//评分标准-结构（高中特征feature）
			String pwriting = request.getParameter("pwriting");//评分标准-书写
			String scoring = request.getParameter("scoring");//得分点
			String points = request.getParameter("points");//失分点
			String suggest = request.getParameter("suggest");//建议
			String contentCa = "";
			String languageCa = "";
			String structureCa = "";
			String writingCa = "";
			List<Map<String, Object>> pfbz = tfind.findPfbz(grade,pcontent,planguage,pstructure,pwriting);//评分标准
			for(int i=0;i<pfbz.size();i++){
				if(pfbz.get(i).containsKey("content")){
					if(pfbz.get(i).get("content").toString().equals(pcontent)){
						contentCa = pfbz.get(i).get("category").toString();
					}
				}
				if(pfbz.get(i).containsKey("language")){
					if(pfbz.get(i).get("language").toString().equals(planguage)){
						languageCa = pfbz.get(i).get("category").toString();
					}
				}
				if(pfbz.get(i).containsKey("structure")){
					if(pfbz.get(i).get("structure").toString().equals(pstructure)){
						structureCa = pfbz.get(i).get("category").toString();
					}
				}
				if(pfbz.get(i).containsKey("writing")){
					if(pfbz.get(i).get("writing").toString().equals(pwriting)){
						writingCa = pfbz.get(i).get("category").toString();
					}
				}
			}
			System.out.println("--------------老师登录ID："+userId);
			int i = tfind.addExpected(userId,id,content,score,pcontent,planguage,pstructure,pwriting,scoring,points,suggest,contentCa,languageCa,structureCa,writingCa);
			response.sendRedirect(path+"/teacherPC/yuPiGai/yuPiGai_tiJiao.jsp");
		}
		
		//预点评作文  未通过  -重新点评
		if(flag.equals("8")){
			List<Map<String, Object>> ydp = tfind.tofindYdp(userId);
			Object[] parseAge = CommUtils.parseAge(ydp.get(0).get("grade").toString());
			String grade = parseAge[1].toString();
			ydp.get(0).put("grade", grade);
	        //评分标准信息
			List<Map<String, Object>> pingfen = tfind.pingfen(grade);
			request.setAttribute("ydp", ydp);
			request.setAttribute("pingfen", pingfen);
			request.getRequestDispatcher("/teacherPC/yuPiGai/yuPiGai_not.jsp").forward(request, response);
		}
		
		//预点评作文  未通过   -提交
		if(flag.equals("9")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			String content = request.getParameter("content");//内容
			String score = request.getParameter("score");//分数
			String grade = request.getParameter("grade");//点评作文年级
			String pcontent = request.getParameter("pcontent");//评分标准-内容
			String planguage = request.getParameter("planguage");//评分标准-语言（高中表达express）
			String pstructure = request.getParameter("pstructure");//评分标准-结构（高中特征feature）
			String pwriting = request.getParameter("pwriting");//评分标准-书写
			String scoring = request.getParameter("scoring");//得分点
			String points = request.getParameter("points");//失分点
			String suggest = request.getParameter("suggest");//建议
			String contentCa = "";
			String languageCa = "";
			String structureCa = "";
			String writingCa = "";
			List<Map<String, Object>> pfbz = tfind.findPfbz(grade,pcontent,planguage,pstructure,pwriting);//评分标准
			for(int i=0;i<pfbz.size();i++){
				if(pfbz.get(i).containsKey("content")){
					if(pfbz.get(i).get("content").toString().equals(pcontent)){
						contentCa = pfbz.get(i).get("category").toString();
					}
				}
				if(pfbz.get(i).containsKey("language")){
					if(pfbz.get(i).get("language").toString().equals(planguage)){
						languageCa = pfbz.get(i).get("category").toString();
					}
				}
				if(pfbz.get(i).containsKey("structure")){
					if(pfbz.get(i).get("structure").toString().equals(pstructure)){
						structureCa = pfbz.get(i).get("category").toString();
					}
				}
				if(pfbz.get(i).containsKey("writing")){
					if(pfbz.get(i).get("writing").toString().equals(pwriting)){
						writingCa = pfbz.get(i).get("category").toString();
					}
				}
			}
			int i = tfind.updateExpected(id,content,score,pcontent,planguage,pstructure,pwriting,scoring,points,suggest,contentCa,languageCa,structureCa,writingCa);
			if(i>0){
				response.sendRedirect(path+"/teacherPC/yuPiGai/yuPiGai_tiJiao.jsp");
			}
		}
		//批改 未点评   -暂存
		if(flag.equals("10")){
			String sid = "";
			int id = 0;
			String stid = "";
			Map<String, Object> student = null;
			int stuid = 0;
			String title = "";
			String content = "";
			String score = "";
			String grade = "";
			String geade = "";
			String pcontent = "";
			String planguage = "";
			String pstructure = "";
			String pwriting = "";
			String scoring = "";
			String points = "";
			String suggest = "";
			String categoryCa = "";
			String languageCa = "";
			String structureCa = "";
			String writingCa = "";
			try{
				sid = request.getParameter("id");
				id = Integer.parseInt(sid);
				stid = request.getParameter("udid");
				student = tfind.findstuid(stid);//查询学生 id
				stuid = Integer.parseInt(student.get("ID").toString());
				title = request.getParameter("title");//作文标题
				content = request.getParameter("content");//内容
				score = request.getParameter("score");//分数
				grade = request.getParameter("grade");//点评作文年级
				geade = request.getParameter("geade");//点评作文年级CODE
				pcontent = request.getParameter("pcontent");//评分标准-内容
				planguage = request.getParameter("planguage");//评分标准-语言（高中表达express）
				pstructure = request.getParameter("pstructure");//评分标准-结构（高中特征feature）
				pwriting = request.getParameter("pwriting");//评分标准-书写
				scoring = request.getParameter("scoring");//得分点
				points = request.getParameter("points");//失分点
				suggest = request.getParameter("suggest");//建议
			}catch(Exception e){
				logger.error("教师端PC request获取参数异常 Exception"+e.getMessage());
				e.printStackTrace();
			}	
			try {
				List<Map<String, Object>> pfbz = tfind.findPfbz(grade,pcontent,planguage,pstructure,pwriting);//评分标准
				for(int i=0;i<pfbz.size();i++){
					if(pfbz.get(i).containsKey("content")){
						if(pfbz.get(i).get("content").toString().equals(pcontent)){
							categoryCa = pfbz.get(i).get("category").toString();
						}
					}
					if(pfbz.get(i).containsKey("language")){
						if(pfbz.get(i).get("language").toString().equals(planguage)){
							languageCa = pfbz.get(i).get("category").toString();
						}
					}
					if(pfbz.get(i).containsKey("structure")){
						if(pfbz.get(i).get("structure").toString().equals(pstructure)){
							structureCa = pfbz.get(i).get("category").toString();
						}
					}
					if(pfbz.get(i).containsKey("writing")){
						if(pfbz.get(i).get("writing").toString().equals(pwriting)){
							writingCa = pfbz.get(i).get("category").toString();
						}
					}
				}
			} catch (Exception e) {
				logger.error("教师端PC 查询相应年纪异常 Exception"+e.getMessage());
				e.printStackTrace();
			}	
			logger.info("---教师端PC 暂存开始----");	
			int i = tfind.temporary(id,userId,stuid,content,score,geade,pcontent,planguage,pstructure,pwriting,scoring,points,suggest,categoryCa,languageCa,structureCa,writingCa);
			logger.info("---教师端PC 暂存结束----");	
			if(i>0){
				PrintWriter writer = response.getWriter();
		        writer.write("暂存成功!");
		        writer.flush();
				//response.sendRedirect(path+"/servlet/TeacherFindServlet?do=1");
			}else{
				// 暂存失败
				logger.error("教师端PC 暂存失败i"+i+"---id:"+id+"-----userId:"+userId+"-------stuid:"+stuid+"-----content:"+content+"----score:"+score+"----geade:"+geade+"----pcontent:"+pcontent+"----planguage:"+planguage+"----pstructure:"+pstructure+"----pwriting:"+pwriting+"----scoring:"+scoring+"----points:"+points+"----suggest:"+suggest+"----categoryCa:"+categoryCa+"----languageCa:"+languageCa+"---structureCa:"+structureCa+"---writingCa:"+writingCa);
			}
			
		}
	}
	
	public void init() throws ServletException {
		logger.info("-------this："+this);
		logger.info("-------this.hashCode："+this.hashCode());
		String name = Thread.currentThread().getName();
	    long threadl = Thread.currentThread().getId();
	    logger.info("------------servlet初始化：当前线程名字:"+name+",id："+threadl+"，毫秒值："+System.currentTimeMillis()+"------------------------");
	}
	
}
