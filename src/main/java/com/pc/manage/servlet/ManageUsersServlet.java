package com.pc.manage.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

import com.pc.manage.dao.ManageUsersDaoImpl;
import com.util.CommUtils;
import com.util.CurrentPage;
/**
 * 管理PC端-用户管理
 */
@WebServlet(name = "ManageUsersServlet", urlPatterns = "/servlet/ManageUsersServlet")  //标记为servlet，以便启动器扫描。
public class ManageUsersServlet extends HttpServlet{

	public ManageUsersServlet() {
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
		ManageUsersDaoImpl users = new ManageUsersDaoImpl();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		int currentPage = 1;
		int numPerPage = 10;
		
		/**
		 * 省市     -二级联动
		 */
		if(flag.equals("0")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> districts =  users.findArea(id);
			/*request.setAttribute("districts", districts);*/
			if(districts.size()>0){
				String str = "<option value=\"-1\">请选择</option>";
				if(districts.size()>0){
					for(int i=0;i<districts.size();i++){
						str+="<option value=\""+districts.get(i).get("id").toString()+"\">"+districts.get(i).get("name").toString()+"</option>";
					}
				}
				PrintWriter out = response.getWriter();
				out.print(str);
				out.flush();
				out.close();
			}
		}
		/**
		 * 老师用户集合   - 条件+分页
		 */
		if(flag.equals("1")){
			//（（年级,年级表grade） - ageDetail
			List<Map<String, Object>> ageDetail =  users.findAgedetail();
			request.setAttribute("ageDetail", ageDetail);
			//（地区） - area
			int id = 0;
			List<Map<String, Object>> districts =  users.findArea(id);
			request.setAttribute("districts", districts);
			//条件
			String nj = request.getParameter("ageDetail");//年级
			String state = request.getParameter("state");//状态
			String sheng = request.getParameter("shengshi1");//省
			String shi = request.getParameter("shengshi2");//市
			String xian = request.getParameter("shengshi3");//县
			String userState = request.getParameter("userState");//用户状态
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
			CurrentPage page = users.findTeacherlist(currentPage,numPerPage,nj,state,sheng,shi,xian,userState,name);
			List<Map<String, Object>> list = page.getResultList();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					if(list.get(i).get("AVG_TIME")!=null && !"".equals(list.get(i).get("AVG_TIME").toString())){
						String avg_time = list.get(i).get("AVG_TIME").toString();
						String[] sts = avg_time.split(":");
						String str1 = "";
						String str2 = "";
						if (sts[0].length() == 1) {
							str1 = "0" + sts[0];
						}else {
							str1 = sts[0];
						}
						if (sts[1].length() == 1) {
							str2 = "0" + sts[1];
						}else {
							str2 = sts[1];
						}
						list.get(i).put("avg_time", str1 + ":" + str2);
					}else {
						list.get(i).put("avg_time", "");
					}
					
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
						int grange = Integer.parseInt(grade);
						if (27000 < grange && grange < 27007) {
							list.get(i).put("grade", "小学");// 027001-027006 小学
						} else if (27010 < grange && grange < 27014) {
							list.get(i).put("grade", "初中");// 027011-027013 初中
						} else {
							list.get(i).put("grade", "高中");// 027021-027023 高中
						}
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("page", page);
			request.setAttribute("list", list);
			request.setAttribute("nj", nj);
			request.setAttribute("state", state);
			request.setAttribute("sheng", sheng);
			request.setAttribute("shi", shi);
			request.setAttribute("xian", xian);
			request.setAttribute("userState", userState);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/yongHuGuanLi/laoShiGuanLi/teacher.jsp").forward(request, response);
		}
		
		//修改老师	-回显
		if(flag.equals("2")){
			String ids = request.getParameter("id");
			int id = Integer.parseInt(ids);
			//（年级,年级表grade） - ageDetail
			List<Map<String, Object>> ageDetail =  users.findAgedetail();
			request.setAttribute("ageDetail", ageDetail);
			//（地区） - area
			int areaid = 0;
			List<Map<String, Object>> districts =  users.findArea(areaid);
			request.setAttribute("districts", districts);
			List<Map<String, Object>> teacher =  users.findTeacher(id);
			String sheng = "";
			String shi = "";
			String xian = "";
			if(teacher.size()>0){
				if(teacher.get(0).get("city")!=null){
					String city = teacher.get(0).get("city").toString().replaceAll(" ","");
					int i = city.indexOf("省");
					int z = city.indexOf("自治区");
					if(i>-1){
						sheng = city.substring(0, i+1);
						int i6 = city.indexOf("行政区划");
						if(i6>-1){
							shi = city.substring(i+1, i6+4);
							xian = city.substring(i6+4);
						}else{
							String s = city.substring(i+1);
							int i5 = s.indexOf("州市");
							if(i5>-1){
								shi = s.substring(0, i5+2);
								xian = s.substring(i5+2);
							}else{
								int i4 = s.indexOf("州");
								if(i4>-1){
									shi = s.substring(0, i4+1);
									xian = s.substring(i4+1);
								}else{
									int i1 = s.indexOf("市");
									if(i1>-1){
										shi = s.substring(0, i1+1);
									}
									xian = s.substring(i1+1);
									/*int i2 = city.indexOf("区");
									if(i2>-1){
										xian = city.substring(i1+1, i2+1);
									}else{
										int i3 = city.indexOf("县");
										if(i3>-1){
											xian = city.substring(i1+1, i3+1);
										}
									}*/
								}
							}
						}
					}else if(z>-1){
						sheng = city.substring(0, z+3);//1、省级
						String s = city.substring(z+3);//2、市级
						int s1 = s.indexOf("行政区划");
						if(s1>-1){
							shi = s.substring(0,s1+4);
							xian = s.substring(s1+4);//3、县级
						}else{
							int s2 = s.indexOf("市");
							int s3 = s.indexOf("区");
							int s4 = s.indexOf("州");
							int s5 = s.indexOf("盟");
							int[] a = {s1,s2,s3,s4,s5};
							int count = 0;
							java.util.Arrays.sort(a);
							while(count<a.length){
								if(a[count]>0){
									break;
								}
								count++;	
							}
							shi = s.substring(0,a[count]+1);
							xian = s.substring(a[count]+1);//3、县级
							if(xian.length()>0){
								if(xian.substring(0,1).equals("市")){
									shi = s.substring(0,a[count]+2);
									xian = s.substring(a[count]+2);//3、县级
								}
							}
						}
						/*if(!x.equals("")){
							int i2 = x.indexOf("区");
							if(i2>-1){
								xian = x.substring(0, i2+1);
							}else{
								int i3 = x.indexOf("县");
								if(i3>-1){
									xian = x.substring(0, i3+1);
								}else{
									int i4 = x.indexOf("市");
									if(i4>-1){
										xian = x.substring(0, i4+1);
									}else{
										int i5 = x.indexOf("旗");
										if(i5>-1){
											xian = x.substring(0, i5+1);
										}
									}
								}
							}
						}*/
					}else{
						int i1 = city.indexOf("市");
						if(i1>-1){
							sheng = city.substring(0, i1+1);
						}
						int i2 = city.indexOf("区");
						if(i2>-1){
							shi = "市辖区";
							xian = city.substring(i1+1, i2+1);
						}else{
							int i3 = city.indexOf("县");
							if(i3>-1){
								shi = "县";
								xian = city.substring(i1+1, i3+1);
							}
						}
						
					}
					teacher.get(0).put("sheng", sheng);
					teacher.get(0).put("shi", shi);
					teacher.get(0).put("xian", xian);
					
					/*String[] split = teacher.get(0).get("city").toString().split(" ");
					if(split.length==1){
						sheng = split[0].trim();
						teacher.get(0).put("sheng", sheng);
						
					}
					if(split.length==2){
						sheng = split[0].trim();
						shi = split[1].trim();
						teacher.get(0).put("sheng", sheng);
						teacher.get(0).put("shi", shi);
					}
					if(split.length==3){
						sheng = split[0].trim();
						shi = split[1].trim();
						xian = split[2].trim();
						teacher.get(0).put("sheng", sheng);
						teacher.get(0).put("shi", shi);
						teacher.get(0).put("xian", xian);
					}*/
				}
			}
			//（地区） - 市
			List<Map<String, Object>> shiList =  null;
			if(sheng!=""){
				int pid =  users.findShilist(sheng);
				shiList =  users.findArea(pid);
				request.setAttribute("shiList", shiList);
			}
			//（地区） - 县
			if(shi!=""){
				int pid = 0;
				for(int i=0;i<shiList.size();i++){
					if(shiList.get(i).get("name").toString().equals(shi)){
						pid = Integer.parseInt(shiList.get(i).get("id").toString());
					}
				}
				List<Map<String, Object>> xianList =  users.findArea(pid);
				request.setAttribute("xianList", xianList);
			}
			request.setAttribute("teacher", teacher);
			request.getRequestDispatcher("/WEB-INF/views/yongHuGuanLi/laoShiGuanLi/teacher_update.jsp").forward(request, response);
		}
		
		//修改老师	-提交
		if(flag.equals("3")){
			String ids = request.getParameter("id");
			int id = Integer.parseInt(ids);
			String name = request.getParameter("name");//姓名
			String sex = request.getParameter("sex");//性别
			String school = request.getParameter("school");//学校
			String nj = request.getParameter("fwGrade");//年级
			String sheng = request.getParameter("shengshi1");//省
			String shi = request.getParameter("shengshi2");//市
			String xian = request.getParameter("shengshi3");//县
			if(shi.equals("市辖区")||shi.equals("县")||shi.equals("请选择")){
				shi = "";
			}
			if(xian.equals("市辖区")||xian.equals("县")||xian.equals("请选择")){
				xian = "";
			}
			String eduTime = request.getParameter("eduTime");//教龄
			String exper = request.getParameter("exper");//中高考阅卷经验
			String state = request.getParameter("state");//状态
			String acv = request.getParameter("acv");//教学成就
			String honor = request.getParameter("honor");//所获荣誉
			String features = request.getParameter("features");//教学特色
			int i =  users.updateTeacher(id,name,sex,school,nj,sheng,shi,xian,eduTime,exper,state,acv,honor,features);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageUsersServlet?do=1");
			}
		}
		
		//修改用户状态
		if(flag.equals("4")){
			String ids = request.getParameter("id");
			int id = Integer.parseInt(ids);
			String austste = request.getParameter("austste");//状态
			int i =  users.updateAustste(id,austste);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageUsersServlet?do=1");
			}
		}
		
		/**
		 * 师资认证	-待审核
		 */
		if(flag.equals("5")){
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
			//待审核集合
			CurrentPage page = users.findDaish(currentPage,numPerPage,name);
			List<Map<String, Object>> list = page.getResultList();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					try {
						//转换注册时间
						if(list.get(i).get("createTime")!=null){
							String times = list.get(i).get("createTime").toString();
							Date parse = df.parse(times);
							String format = df.format(parse);
							list.get(i).put("createTime", format);
						}
						//上传资料时间
						if(list.get(i).get("uploadTime")!=null){
							String times2 = list.get(i).get("uploadTime").toString();
							Date parse2 = df.parse(times2);
							String format2 = df.format(parse2);
							list.get(i).put("uploadTime", format2);
						}
						//提交预批改作文时间
						if(list.get(i).get("comTime")!=null){
							String times3 = list.get(i).get("comTime").toString();
							Date parse3 = df.parse(times3);
							String format3 = df.format(parse3);
							list.get(i).put("comTime", format3);
						}
						//完善个人信息时间
						if(list.get(i).get("perfectTime")!=null){
							String times4 = list.get(i).get("perfectTime").toString();
							Date parse4 = df.parse(times4);
							String format4 = df.format(parse4);
							list.get(i).put("perfectTime", format4);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("list", list);
			request.setAttribute("page", page);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/yongHuGuanLi/renZheng/renZheng_daish.jsp").forward(request, response);
		}
		
		//师资认证	-待审核-审核
		if(flag.equals("6")){
			String ids = request.getParameter("id");
			int id = Integer.parseInt(ids);
			//个人资料
			List<Map<String, Object>> list = users.findShenhe(id);
			String host = CommUtils.getServerHost();
			if(list.size()>0){
				if(list.get(0).get("certUrl")!=null&&!list.get(0).get("certUrl").equals("")){
					list.get(0).put("certUrl", host+list.get(0).get("certUrl").toString());
				}
				if(list.get(0).get("card1")!=null&&!list.get(0).get("card1").equals("")){
					list.get(0).put("card1", host+list.get(0).get("card1").toString());
				}
				if(list.get(0).get("card2")!=null&&!list.get(0).get("card2").equals("")){
					list.get(0).put("card2", host+list.get(0).get("card2").toString());
				}
				if(list.get(0).get("grade")!=null&&!list.get(0).get("grade").equals("")){
					Object[] parseAge = CommUtils.parseAge(list.get(0).get("grade").toString());
					String grade = parseAge[1].toString();
					list.get(0).put("grade", grade);
				}
			}
			
			//预点评作文
			List<Map<String, Object>> ydp = users.findYdp(id);
			if(ydp.size()>0){
				if(ydp.get(0).get("newTitle")!=null&&!ydp.get(0).get("newTitle").equals("")){
					list.get(0).put("title", ydp.get(0).get("newTitle").toString());
					ydp.get(0).put("title", ydp.get(0).get("newTitle").toString());
				}else {
					if(ydp.get(0).get("oldTitle")!=null&&!ydp.get(0).get("oldTitle").equals("")){
						list.get(0).put("title", ydp.get(0).get("oldTitle").toString());
						ydp.get(0).put("title", ydp.get(0).get("oldTitle").toString());
					}else{
						list.get(0).put("title", "");
						ydp.get(0).put("title", "");
					}
				}
				if(ydp.get(0).get("geade")!=null&&!ydp.get(0).get("geade").equals("")){
					Object[] parseAge = CommUtils.parseAge(ydp.get(0).get("geade").toString());
					String grade = parseAge[1].toString();
					ydp.get(0).put("geade", grade);
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
			request.setAttribute("list", list);
			request.setAttribute("ydp", ydp);
			request.getRequestDispatcher("/WEB-INF/views/yongHuGuanLi/renZheng/renZheng_sheHe.jsp").forward(request, response);
		}
		
		//师资认证	-待审核-提交审核
		if(flag.equals("7")){
			String ids = request.getParameter("id");
			int id = Integer.parseInt(ids);
			String ziLiao = request.getParameter("ziLiao");//师资认证资料
			String title = request.getParameter("title");//预点评标题
			String zuoWen = request.getParameter("zuoWen");//预点评作文
			String cause = request.getParameter("cause");//审核失败原因
			String reason = request.getParameter("reason");//预点评作文未通过原因
			String ziLiao2 = request.getParameter("ziLiao2");//师资认证资料
			String zuoWen2 = request.getParameter("zuoWen2");//预点评作文
			if(ziLiao==null||ziLiao==""){
				ziLiao=ziLiao2;
			}
			if(zuoWen==null||zuoWen==""){
				zuoWen=zuoWen2;
			}
			users.updateState(id,ziLiao,cause,title,zuoWen,reason);
			response.sendRedirect(path+"/servlet/ManageUsersServlet?do=5");
		}
		
		/**
		 * 师资认证	-已审核
		 */
		if(flag.equals("8")){
			String sname = request.getParameter("name");//用户名
			
			String name = "";
			if (sname != null && !sname.equals("")) {
				name = sname.replaceAll(" ", "");
			}
			
			String austate = request.getParameter("austate");//状态
			
			String scurrentPage = request.getParameter("currentPage");//当前页
			if(scurrentPage!=null&&scurrentPage!=""){
				currentPage = Integer.parseInt(scurrentPage);
			}
			String snumPerPage = request.getParameter("numPerPage");//每页记录数
			if(snumPerPage!=null&&snumPerPage!=""){
				numPerPage = Integer.parseInt(snumPerPage);
			}
			//已审核集合
			CurrentPage page = users.findYish(currentPage,numPerPage,name,austate);
			List<Map<String, Object>> list = page.getResultList();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					try {
						//转换注册时间
						if(list.get(i).get("createTime")!=null){
							String times = list.get(i).get("createTime").toString();
							Date parse = df.parse(times);
							String format = df.format(parse);
							list.get(i).put("createTime", format);
						}
						//上传资料时间
						if(list.get(i).get("uploadTime")!=null){
							String times2 = list.get(i).get("uploadTime").toString();
							Date parse2 = df.parse(times2);
							String format2 = df.format(parse2);
							list.get(i).put("uploadTime", format2);
						}
						//提交预批改作文时间
						if(list.get(i).get("comTime")!=null){
							String times3 = list.get(i).get("comTime").toString();
							Date parse3 = df.parse(times3);
							String format3 = df.format(parse3);
							list.get(i).put("comTime", format3);
						}
						//完善个人信息时间
						if(list.get(i).get("perfectTime")!=null){
							String times4 = list.get(i).get("perfectTime").toString();
							Date parse4 = df.parse(times4);
							String format4 = df.format(parse4);
							list.get(i).put("perfectTime", format4);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("list", list);
			request.setAttribute("page", page);
			request.setAttribute("name", name);
			request.setAttribute("austate", austate);
			request.getRequestDispatcher("/WEB-INF/views/yongHuGuanLi/renZheng/renZheng_yish.jsp").forward(request, response);
		}
		
		//师资认证	-已审核-查看
		if(flag.equals("9")){
			String ids = request.getParameter("id");
			int id = Integer.parseInt(ids);
			//个人资料
			List<Map<String, Object>> list = users.findShenhe(id);
			String host = CommUtils.getServerHost();
			if(list.size()>0){
				if(list.get(0).get("grade")!=null){
					Object[] parseAge = CommUtils.parseAge(list.get(0).get("grade").toString());
					String grade = parseAge[1].toString();
					list.get(0).put("grade", grade);
				}
				if(list.get(0).get("certUrl")!=null){
					list.get(0).put("certUrl", host+list.get(0).get("certUrl").toString());
				}
				if(list.get(0).get("card1")!=null){
					list.get(0).put("card1", host+list.get(0).get("card1").toString());
				}
				if(list.get(0).get("card2")!=null){
					list.get(0).put("card2", host+list.get(0).get("card2").toString());
				}
				
			}
			//预点评作文
			List<Map<String, Object>> ydp = users.findYdp(id);
			if(ydp.size()>0){
				if(ydp.get(0).get("newTitle")!=null){
					list.get(0).put("title", ydp.get(0).get("newTitle").toString());
					ydp.get(0).put("title", ydp.get(0).get("newTitle").toString());
				}else {
					if(ydp.get(0).get("oldTitle")!=null){
						list.get(0).put("title", ydp.get(0).get("oldTitle").toString());
						ydp.get(0).put("title", ydp.get(0).get("oldTitle").toString());
					}else{
						list.get(0).put("title", "");
						ydp.get(0).put("title", "");
					}
				}
				if(ydp.get(0).get("geade")!=null){
					Object[] parseAge = CommUtils.parseAge(ydp.get(0).get("geade").toString());
					String grade = parseAge[1].toString();
					ydp.get(0).put("geade", grade);
				}
			}
			request.setAttribute("list", list);
			request.setAttribute("ydp", ydp);
			request.getRequestDispatcher("/WEB-INF/views/yongHuGuanLi/renZheng/ziLiao_find.jsp").forward(request, response);
		}
		
		/**
		 * 学生用户集合   - 条件+分页
		 */
		if(flag.equals("10")){
			//（（年级,年级表grade） - ageDetail
			List<Map<String, Object>> ageDetail =  users.findAgedetail();
			request.setAttribute("ageDetail", ageDetail);
			//（地区） - area
			int id = 0;
			List<Map<String, Object>> districts =  users.findArea(id);
			request.setAttribute("districts", districts);
			//条件
			String nj = request.getParameter("ageDetail");//年级
			String sheng = request.getParameter("shengshi1");//省
			String shi = request.getParameter("shengshi2");//市
			String xian = request.getParameter("shengshi3");//县
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
			CurrentPage page = users.findStudentlist(currentPage,numPerPage,nj,sheng,shi,xian,name);
			List<Map<String, Object>> list = page.getResultList();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
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
			request.setAttribute("nj", nj);
			request.setAttribute("sheng", sheng);
			request.setAttribute("shi", shi);
			request.setAttribute("xian", xian);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/yongHuGuanLi/xueShengGuanLi/student.jsp").forward(request, response);
		}
		
		//修改学生	-回显
		if(flag.equals("11")){
			String ids = request.getParameter("id");
			int id = Integer.parseInt(ids);
			//（年级,年级表grade） - ageDetail
			List<Map<String, Object>> ageDetail =  users.findAgedetail();
			request.setAttribute("ageDetail", ageDetail);
			//（地区） - area
			int areaid = 0;
			List<Map<String, Object>> districts =  users.findArea(areaid);
			request.setAttribute("districts", districts);
			List<Map<String, Object>> student =  users.findStudent(id);
			String sheng = "";
			String shi = "";
			String xian = "";
			if(student.size()>0){
				if(student.get(0).get("createTime")!=null&&!student.get(0).get("createTime").equals("")){
					String times = student.get(0).get("createTime").toString();
					try {
						//转换提交时间
						Date parse = df.parse(times);
						String format = df.format(parse);
						student.get(0).put("createTime", format);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(student.get(0).get("city")!=null){
					String city = student.get(0).get("city").toString().replaceAll(" ","");
					int i = city.indexOf("省");
					int z = city.indexOf("自治区");
					if(i>-1){
						sheng = city.substring(0, i+1);
						int i6 = city.indexOf("行政区划");
						if(i6>-1){
							shi = city.substring(i+1, i6+4);
							xian = city.substring(i6+4);
						}else{
							String s = city.substring(i+1);
							int i5 = s.indexOf("州市");
							if(i5>-1){
								shi = s.substring(0, i5+2);
								xian = s.substring(i5+2);
							}else{
								int i4 = s.indexOf("州");
								if(i4>-1){
									shi = s.substring(0, i4+1);
									xian = s.substring(i4+1);
								}else{
									int i1 = s.indexOf("市");
									if(i1>-1){
										shi = s.substring(0, i1+1);
									}
									xian = s.substring(i1+1);
								}
							}
						}
					}else if(z>-1){
						sheng = city.substring(0, z+3);//1、省级
						String s = city.substring(z+3);//2、市级
						int s1 = s.indexOf("行政区划");
						if(s1>-1){
							shi = s.substring(0,s1+4);
							xian = s.substring(s1+4);//3、县级
						}else{
							int s2 = s.indexOf("市");
							int s3 = s.indexOf("区");
							int s4 = s.indexOf("州");
							int s5 = s.indexOf("盟");
							int[] a = {s1,s2,s3,s4,s5};
							int count = 0;
							java.util.Arrays.sort(a);
							while(count<a.length){
								if(a[count]>0){
									break;
								}
								count++;	
							}
							shi = s.substring(0,a[count]+1);
							xian = s.substring(a[count]+1);//3、县级
							if(xian.length()>0){
								if(xian.substring(0,1).equals("市")){
									shi = s.substring(0,a[count]+2);
									xian = s.substring(a[count]+2);//3、县级
								}
							}
						}
					}else{
						int i1 = city.indexOf("市");
						if(i1>-1){
							sheng = city.substring(0, i1+1);
						}
						int i2 = city.indexOf("区");
						if(i2>-1){
							shi = "市辖区";
							xian = city.substring(i1+1, i2+1);
						}else{
							int i3 = city.indexOf("县");
							if(i3>-1){
								shi = "县";
								xian = city.substring(i1+1, i3+1);
							}
						}
						
					}
					student.get(0).put("sheng", sheng);
					student.get(0).put("shi", shi);
					student.get(0).put("xian", xian);
				}
			}
			//（地区） - 市
			List<Map<String, Object>> shiList =  null;
			if(sheng!=""){
				int pid =  users.findShilist(sheng);
				shiList =  users.findArea(pid);
				request.setAttribute("shiList", shiList);
			}
			//（地区） - 县
			if(shi!=""){
				int pid = 0;
				for(int i=0;i<shiList.size();i++){
					if(shiList.get(i).get("name").toString().equals(shi)){
						pid = Integer.parseInt(shiList.get(i).get("id").toString());
					}
				}
				List<Map<String, Object>> xianList =  users.findArea(pid);
				request.setAttribute("xianList", xianList);
			}
			request.setAttribute("student", student);
			request.getRequestDispatcher("/WEB-INF/views/yongHuGuanLi/xueShengGuanLi/student_update.jsp").forward(request, response);
		}
		
		//修改学生	-提交
		if(flag.equals("12")){
			String ids = request.getParameter("id");
			int id = Integer.parseInt(ids);
			String userName = request.getParameter("userName");//姓名
			String name = request.getParameter("name");//姓名
			String sex = request.getParameter("sex");//性别
			String school = request.getParameter("school");//学校
			String nj = request.getParameter("fwGrade");//年级
			String sheng = request.getParameter("shengshi1");//省
			String shi = request.getParameter("shengshi2");//市
			String xian = request.getParameter("shengshi3");//县
			if(shi.equals("市辖区")||shi.equals("县")||shi.equals("请选择")){
				shi = "";
			}
			if(xian.equals("市辖区")||xian.equals("县")||xian.equals("请选择")){
				xian = "";
			}
			int i =  users.updateStudent(id,userName,name,sex,school,nj,sheng,shi,xian);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageUsersServlet?do=10");
			}
		}
		
		//删除学生
		if(flag.equals("13")){
			String ids = request.getParameter("id");
			int id = Integer.parseInt(ids);
			users.deleteStudent(id);
			response.sendRedirect(path+"/servlet/ManageUsersServlet?do=10");
		}
		
		//师资认证	-待审核-查看
		if(flag.equals("14")){
			String ids = request.getParameter("id");
			int id = Integer.parseInt(ids);
			//个人资料
			List<Map<String, Object>> list = users.findShenhe(id);
			String host = CommUtils.getServerHost();
			if(list.size()>0){
				if(list.get(0).get("certUrl")!=null&&!list.get(0).get("certUrl").equals("")){
					list.get(0).put("certUrl", host+list.get(0).get("certUrl").toString());
				}
				if(list.get(0).get("card1")!=null&&!list.get(0).get("card1").equals("")){
					list.get(0).put("card1", host+list.get(0).get("card1").toString());
				}
				if(list.get(0).get("card2")!=null&&!list.get(0).get("card2").equals("")){
					list.get(0).put("card2", host+list.get(0).get("card2").toString());
				}
				if(list.get(0).get("grade")!=null&&!list.get(0).get("grade").equals("")){
					Object[] parseAge = CommUtils.parseAge(list.get(0).get("grade").toString());
					String grade = parseAge[1].toString();
					list.get(0).put("grade", grade);
				}
			}
			
			//预点评作文
			List<Map<String, Object>> ydp = users.findYdp(id);
			if(ydp.size()>0){
				if(ydp.get(0).get("newTitle")!=null&&!ydp.get(0).get("newTitle").equals("")){
					list.get(0).put("title", ydp.get(0).get("newTitle").toString());
					ydp.get(0).put("title", ydp.get(0).get("newTitle").toString());
				}else {
					if(ydp.get(0).get("oldTitle")!=null&&!ydp.get(0).get("oldTitle").equals("")){
						list.get(0).put("title", ydp.get(0).get("oldTitle").toString());
						ydp.get(0).put("title", ydp.get(0).get("oldTitle").toString());
					}else{
						list.get(0).put("title", "");
						ydp.get(0).put("title", "");
					}
				}
				if(ydp.get(0).get("geade")!=null&&!ydp.get(0).get("geade").equals("")){
					Object[] parseAge = CommUtils.parseAge(ydp.get(0).get("geade").toString());
					String grade = parseAge[1].toString();
					ydp.get(0).put("geade", grade);
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
			request.setAttribute("list", list);
			request.setAttribute("ydp", ydp);
			request.getRequestDispatcher("/WEB-INF/views/yongHuGuanLi/renZheng/renZheng_find.jsp").forward(request, response);
		}
		
		//信息发送列表查询
		if(flag.equals("17")){
			String xinResult = "";
			try {
				xinResult = request.getParameter("xinResult");
			} catch (Exception e) {
			}
			String userType = request.getParameter("userType");
			String sname = request.getParameter("name");
			String scurrentPage = request.getParameter("currentPage");
			String snumPerPage = request.getParameter("numPerPage");
			String name = "";
			if (sname != null && !sname.equals("")) {
				name = sname.replaceAll(" ", "");
			}
			if(scurrentPage!=null&&scurrentPage!=""){
				currentPage = Integer.parseInt(scurrentPage);
			}
			if(snumPerPage!=null&&snumPerPage!=""){
				numPerPage = Integer.parseInt(snumPerPage);
			}
			if (userType==null||userType.equals("")) {
				userType = "student";
			}
			CurrentPage page = users.findUserList(userType, name, currentPage, numPerPage);
			List<Map<String,Object>> list = page.getResultList();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					if(list.get(i).get("CREATE_TIME")!=null){
						String times = list.get(i).get("CREATE_TIME").toString();
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
					if(list.get(i).get("NICKNAME")!=null){
						list.get(i).put("name", list.get(i).get("NICKNAME").toString());
					}
					if(list.get(i).get("LASTLOGINDEVICETYPE")==null||list.get(i).get("LASTLOGINDEVICETYPE").equals("")){
						list.get(i).put("idAndType", list.get(i).get("ID").toString() + "%" + list.get(i).get("UDID").toString());
					}else {
						list.get(i).put("idAndType", list.get(i).get("ID").toString() + "%" + list.get(i).get("UDID").toString() + "%" + list.get(i).get("LASTLOGINDEVICETYPE").toString() );
					}
					list.get(i).put("userType", userType);
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("page", page);
			request.setAttribute("list", list);
			request.setAttribute("userType", userType);
			request.setAttribute("name", name);
			request.setAttribute("xinResult", xinResult);
			
			request.getRequestDispatcher("/WEB-INF/views/yongHuGuanLi/xinxifasong/xinxifasong.jsp").forward(request, response);
		}
		//app信息发送
		if(flag.equals("18")){
			String ids = request.getParameter("ids");
			String information = request.getParameter("inf");
			String userType = request.getParameter("userType");
			String result = users.sendInformationApp(ids, information, userType);
			//response.sendRedirect(path+"/servlet/ManageUsersServlet?do=17&xinResult="+result);
			request.getRequestDispatcher("/WEB-INF/views/servlet/ManageUsersServlet?do=17&xinResult="+result).forward(request, response);
		}
		//消息列表信息发送
		if(flag.equals("19")){
			String ids = request.getParameter("ids");
			String information = request.getParameter("inf");
			String userType = request.getParameter("userType");
			String result = users.sendInformationXiaoxiliebiao(ids, information, userType);
			//response.sendRedirect(path+"/servlet/ManageUsersServlet?do=17&xinResult="+result);
			request.getRequestDispatcher("/WEB-INF/views/servlet/ManageUsersServlet?do=17&xinResult="+result).forward(request, response);
		}
		//短信发送-用户列表
		if(flag.equals("20")){
			String userType = request.getParameter("userType");//用户类型
			String userState = request.getParameter("userState");//完成状态
			String grade = request.getParameter("ageDetail");//年级
			String template = request.getParameter("template");//模板
			
			String scurrentPage = request.getParameter("currentPage");//当前页
			if(scurrentPage!=null&&scurrentPage!=""){
				currentPage = Integer.parseInt(scurrentPage);
			}
			String snumPerPage = request.getParameter("numPerPage");//每页记录数
			if(snumPerPage!=null&&snumPerPage!=""){
				numPerPage = Integer.parseInt(snumPerPage);
			}
			CurrentPage page = new CurrentPage();
			List<Map<String, Object>> list  = new ArrayList<Map<String, Object>>();
			//（（年级,年级表grade） - ageDetail
			List<Map<String, Object>> ageDetail =  users.findAgedetail();
			request.setAttribute("ageDetail", ageDetail);
			if(userType!=null&&userType.equals("student")){
				//学生用户集合
				page = users.findStudent(currentPage,numPerPage,grade);
				list = page.getResultList();
				if(list.size()>0){
					for(int i=0;i<list.size();i++){
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
							String grade1 = list.get(i).get("grade").toString();
							Object[] parseAge = CommUtils.parseAge(grade);
							String gradename = parseAge[1].toString();
							list.get(i).put("grade", gradename);
						}
					}
				}else{
					request.setAttribute("m", "无搜索结果");
				}
				
				
			}else {
				userType = "teacher";
				//待审核集合
				page = users.findTeacherDsh(currentPage,numPerPage,userState);
				list = page.getResultList();
				if(list.size()>0){
					for(int i=0;i<list.size();i++){
						try {
							//转换注册时间
							if(list.get(i).get("createTime")!=null){
								String times = list.get(i).get("createTime").toString();
								Date parse = df.parse(times);
								String format = df.format(parse);
								list.get(i).put("createTime", format);
							}
							//上传资料时间
							if(list.get(i).get("uploadTime")!=null){
								String times2 = list.get(i).get("uploadTime").toString();
								Date parse2 = df.parse(times2);
								String format2 = df.format(parse2);
								list.get(i).put("uploadTime", format2);
							}
							//提交预批改作文时间
							if(list.get(i).get("comTime")!=null){
								String times3 = list.get(i).get("comTime").toString();
								Date parse3 = df.parse(times3);
								String format3 = df.format(parse3);
								list.get(i).put("comTime", format3);
							}
							//完善个人信息时间
							if(list.get(i).get("perfectTime")!=null){
								String times4 = list.get(i).get("perfectTime").toString();
								Date parse4 = df.parse(times4);
								String format4 = df.format(parse4);
								list.get(i).put("perfectTime", format4);
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else{
					request.setAttribute("m", "无搜索结果");
				}
			}
			request.setAttribute("list", list);
			request.setAttribute("page", page);
			request.setAttribute("userType", userType);
			request.setAttribute("userState", userState);
			request.setAttribute("grade", grade);
			request.setAttribute("template", template);
			request.getRequestDispatcher("/WEB-INF/views/yongHuGuanLi/smsSending/smsSending.jsp").forward(request, response);
		}
		//发送短信
		if(flag.equals("21")){
			String ids = request.getParameter("ids");
			String information = request.getParameter("inf");
			//String userType = request.getParameter("userType");
			String result = users.sendInformationApp2(ids, information);
			if(result.equals("sucsess")){
				result="发送成功！";
			}else if(result.equals("fail")){
				result="发送失败！";
			}
			request.getRequestDispatcher("/WEB-INF/views/servlet/ManageUsersServlet?do=20&xinResult="+result).forward(request, response);
		
		}
	}
	
	public void init() throws ServletException {
		// Put your code here
	}
}
