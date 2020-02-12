package com.pc.manage.servlet;

import java.io.File;
import java.io.IOException;
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
import com.pc.manage.dao.ManageFindFwDaoImpl;
import com.util.CommUtils;
import com.util.CurrentPage;
/**
 * 管理PC端-资源管理
 */
@WebServlet(name = "ManageFindFwServlet", urlPatterns = "/servlet/ManageFindFwServlet")  //标记为servlet，以便启动器扫描。
public class ManageFindFwServlet extends HttpServlet{

	public ManageFindFwServlet() {
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
		
		ManageFindFwDaoImpl findfw = new ManageFindFwDaoImpl();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		int currentPage = 1;
		int numPerPage = 10;
		
		/**
		 * 范文集合   - 条件+分页
		 */
		if(flag.equals("1")){
			//（题材类型，公共代码CODE_TYPE=033） - type
			//List<Map<String, Object>> type =  findfw.findType();
			String dir = request.getSession().getServletContext().getRealPath("/jslib/fenlei.json");
			List<Map<String, String>> type =  new ArrayList<Map<String, String>>();
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
	                type.add(map);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
			//（年级,年级表grade） - ageDetail
			List<Map<String, Object>> ageDetail =  findfw.findAgedetail();
			//条件
			String nj = request.getParameter("ageDetail");//年级
			String tc = request.getParameter("type");//题材
			String sname = request.getParameter("name");//范文标题
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
			//范文集合
			CurrentPage page = findfw.findFw(currentPage,numPerPage,nj,tc,name);
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
					if(fwlist.get(i).get("type")!=null){
						String string = fwlist.get(i).get("type").toString();
						
						String string2 = "";
						for(int j=0;j<type.size();j++){
							if(type.get(j).get("CODE").equals(string)){
								string2 = type.get(j).get("CODE_NAME");
							}
						}
						if(string2==""){
							if(fwlist.get(i).get("style")!=null){
								String style = fwlist.get(i).get("style").toString();
								for(int j=0;j<type.size();j++){
									if(type.get(j).get("CODE").equals(style)){
										string2 = type.get(j).get("CODE_NAME");
									}
								}
							}
						}
						fwlist.get(i).put("type1", string2);
					}else {
						if(fwlist.get(i).get("style")!=null){
							String string = fwlist.get(i).get("style").toString();
							String string2 = "";
							for(int j=0;j<type.size();j++){
								if(type.get(j).get("CODE").equals(string)){
									string2 = type.get(j).get("CODE_NAME");
								}
							}
							fwlist.get(i).put("type1", string2);
						}
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("type", type);
			request.setAttribute("ageDetail", ageDetail);
			request.setAttribute("page", page);
			request.setAttribute("fwlist", fwlist);
			request.setAttribute("nj", nj);
			request.setAttribute("tc", tc);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/ziYuanGuanLi/fanWen/fanWen.jsp").forward(request, response);
		}
		
		//跳转 添加范文 页面  -
		if(flag.equals("2")){
			//（题材类型，公共代码CODE_TYPE=033） - type
			//List<Map<String, Object>> type =  findfw.findType();
			String dir = request.getSession().getServletContext().getRealPath("/jslib/fenlei.json");
			List<Map<String, String>> type =  new ArrayList<Map<String, String>>();
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
	                type.add(map);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			//（年级,年级表grade） - ageDetail
			List<Map<String, Object>> ageDetail =  findfw.findAgedetail();
			request.setAttribute("type", type);
			request.setAttribute("ageDetail", ageDetail);
			request.getRequestDispatcher("/WEB-INF/views/ziYuanGuanLi/fanWen/fanWen_add.jsp").forward(request, response);
		}
		
		//添加范文-提交  
		if(flag.equals("3")){
			String name = request.getParameter("name");//范文标题
			String author = request.getParameter("author");//作者
			String nj = request.getParameter("ageDetail");//年级
			Object[] parseAge = CommUtils.parseAge(nj);
			String ageScale = parseAge[0].toString();
			String ageDetail = parseAge[1].toString();
			String type = request.getParameter("type");//题材
			String content = request.getParameter("content");//内容
			int i = findfw.addFenwen(name,author,ageScale,ageDetail,type,content);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageFindFwServlet?do=1");
			}
		}
		
		//删除范文
		if(flag.equals("4")){
			String id = request.getParameter("id");
			int i = findfw.deleteFw(id);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageFindFwServlet?do=1");
			}
		}
		
		//修改范文 -回显
		if(flag.equals("5")){
			//（题材类型，公共代码CODE_TYPE=033） - type
			//List<Map<String, Object>> type =  findfw.findType();
			String dir = request.getSession().getServletContext().getRealPath("/jslib/fenlei.json");
			List<Map<String, String>> type =  new ArrayList<Map<String, String>>();
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
	                type.add(map);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			//（年级,年级表grade） - ageDetail
			List<Map<String, Object>> ageDetail =  findfw.findAgedetail();
			String id = request.getParameter("id");
			List<Map<String, Object>> fanwen = findfw.toFindFw(id);
			if(fanwen.size()>0){
				if(fanwen.get(0).get("type")!=null){
					String string = fanwen.get(0).get("type").toString();
					String string2 = "";
					for(int j=0;j<type.size();j++){
						if(type.get(j).get("CODE").equals(string)){
							string2 = type.get(j).get("CODE_NAME");
						}
					}
					if(string2==""){
						if(fanwen.get(0).get("style")!=null){
							String style = fanwen.get(0).get("style").toString();
							for(int j=0;j<type.size();j++){
								if(type.get(j).get("CODE").equals(style)){
									string2 = type.get(j).get("CODE_NAME");
								}
							}
						}
					}
					fanwen.get(0).put("type1", string2);
				}else {
					if(fanwen.get(0).get("style")!=null){
						String string = fanwen.get(0).get("style").toString();
						String string2 = "";
						for(int j=0;j<type.size();j++){
							if(type.get(j).get("CODE").equals(string)){
								string2 = type.get(j).get("CODE_NAME");
							}
						}
						fanwen.get(0).put("type1", string2);
					}
				}
			}
			request.setAttribute("fanwen", fanwen);
			request.setAttribute("type", type);
			request.setAttribute("ageDetail", ageDetail);
			request.getRequestDispatcher("/WEB-INF/views/ziYuanGuanLi/fanWen/fanWen_update.jsp").forward(request, response);
		}
		
		//修改范文 -提交
		if(flag.equals("6")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			String name = request.getParameter("fwTitle");//范文标题
			String author = request.getParameter("fwName");//作者
			String nj = request.getParameter("fwGrade");//年级
			Object[] parseAge = CommUtils.parseAge(nj);
			String ageScale = parseAge[0].toString();
			String ageDetail = parseAge[1].toString();
			String type = request.getParameter("fwGenre");//题材
			String content = request.getParameter("content");//内容
			int i = findfw.updateFenwen(id,name,author,ageScale,ageDetail,type,content);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageFindFwServlet?do=1");
			}
		}
		
		//查看范文
		if(flag.equals("7")){
			String id = request.getParameter("id");
			List<Map<String, Object>> fanwen = findfw.findFindFw(id);
			if(fanwen.size()>0){
				String dir = request.getSession().getServletContext().getRealPath("/jslib/fenlei.json");
				List<Map<String, String>> type =  new ArrayList<Map<String, String>>();
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
		                type.add(map);
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				if(fanwen.get(0).get("time")!=null){
					String times = fanwen.get(0).get("time").toString();
					try {
						//转换提交时间
						Date parse = df.parse(times);
						String format = df.format(parse);
						fanwen.get(0).put("times", format);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(fanwen.get(0).get("type")!=null){
					String string = fanwen.get(0).get("type").toString();
					String string2 = "";
					for(int j=0;j<type.size();j++){
						if(type.get(j).get("CODE").equals(string)){
							string2 = type.get(j).get("CODE_NAME");
						}
					}
					if(string2==""){
						if(fanwen.get(0).get("style")!=null){
							String style = fanwen.get(0).get("style").toString();
							for(int j=0;j<type.size();j++){
								if(type.get(j).get("CODE").equals(style)){
									string2 = type.get(j).get("CODE_NAME");
								}
							}
						}
					}
					fanwen.get(0).put("type1", string2);
				}else {
					if(fanwen.get(0).get("style")!=null){
						String string = fanwen.get(0).get("style").toString();
						String string2 = "";
						for(int j=0;j<type.size();j++){
							if(type.get(j).get("CODE").equals(string)){
								string2 = type.get(j).get("CODE_NAME");
							}
						}
						fanwen.get(0).put("type1", string2);
					}
				}
			}
			request.setAttribute("fanwen", fanwen);
			request.getRequestDispatcher("/WEB-INF/views/ziYuanGuanLi/fanWen/fanWen_select.jsp").forward(request, response);
		}
		
		/**
		 * 素材集合   - 条件+分页
		 */
		if(flag.equals("8")){
			//（素材分类，公共代码CODE_TYPE=024） - type
			//List<Map<String, Object>> type =  findfw.findCtype();
			String dir = request.getSession().getServletContext().getRealPath("/jslib/fenlei.json");
			List<Map<String, String>> type =  new ArrayList<Map<String, String>>();
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
	                type.add(map);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			
			//（年级等级，公共代码CODE_TYPE=014） - ageScale
			List<Map<String, Object>> ageDetail1 =  findfw.findAgescale();
			List<Map<String, Object>> ageDetail = new ArrayList<>();
			for (int i = 0;i < ageDetail1.size();i++) {
				String str = (String)ageDetail1.get(i).get("CODE_NAME");
				if (!str.equals("低年级")) {
					ageDetail.add(ageDetail1.get(i));
				}
			}
			
			//条件
			String nj = request.getParameter("ageDetail");//年级
			String fl = request.getParameter("type");//分类
			String sname = request.getParameter("name");//素材标题
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
			//素材集合
			CurrentPage page = findfw.findSc(currentPage,numPerPage,nj,fl,name);
			List<Map<String, Object>> sclist = page.getResultList();
			if(sclist.size()>0){
				for(int i=0;i<sclist.size();i++){
					if(sclist.get(i).get("time")!=null){
						String times = sclist.get(i).get("time").toString();
						try {
							//转换提交时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							sclist.get(i).put("times", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("type", type);
			request.setAttribute("ageDetail", ageDetail);
			request.setAttribute("page", page);
			request.setAttribute("sclist", sclist);
			request.setAttribute("nj", nj);
			request.setAttribute("fl", fl);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/ziYuanGuanLi/suCai/suCai.jsp").forward(request, response);
		}
		
		//跳转 添加素材 页面
		if(flag.equals("9")){
			//（素材分类，公共代码CODE_TYPE=024） - type
			//List<Map<String, Object>> type =  findfw.findCtype();
			String dir = request.getSession().getServletContext().getRealPath("/jslib/fenlei.json");
			List<Map<String, String>> type =  new ArrayList<Map<String, String>>();
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
	                type.add(map);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			//（年级等级，公共代码CODE_TYPE=014） - ageScale
			List<Map<String, Object>> ageDetail =  findfw.findAgescale();
			request.setAttribute("type", type);
			request.setAttribute("ageDetail", ageDetail);
			request.getRequestDispatcher("/WEB-INF/views/ziYuanGuanLi/suCai/suCai_add.jsp").forward(request, response);
		}
		
		//添加素材	-提交
		if(flag.equals("10")){
			String name = request.getParameter("scTitle");//素材标题
			String ageDetail = request.getParameter("scGrade");//年级
			String type = request.getParameter("scType");//分类
			String content = request.getParameter("content");//内容
			int i = findfw.addSucai(name,ageDetail,type,content);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageFindFwServlet?do=8");
			}
		}
		
		//删除  素材
		if(flag.equals("11")){
			String id = request.getParameter("id");
			int i = findfw.deleteSc(id);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageFindFwServlet?do=1");
			}
		}
		
		//修改素材 -回显
		if(flag.equals("12")){
			String id = request.getParameter("id");
			//（素材分类，公共代码CODE_TYPE=024） - type
			//List<Map<String, Object>> type =  findfw.findCtype();
			String dir = request.getSession().getServletContext().getRealPath("/jslib/fenlei.json");
			List<Map<String, String>> type =  new ArrayList<Map<String, String>>();
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
	                type.add(map);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			//（年级等级，公共代码CODE_TYPE=014） - ageScale
			List<Map<String, Object>> ageDetail =  findfw.findAgescale();
			List<Map<String, Object>> sucai = findfw.toFindSc(id);
			request.setAttribute("sucai", sucai);
			request.setAttribute("type", type);
			request.setAttribute("ageDetail", ageDetail);
			request.getRequestDispatcher("/WEB-INF/views/ziYuanGuanLi/suCai/suCai_update.jsp").forward(request, response);
		}

		//修改素材 -提交
		if(flag.equals("13")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			String name = request.getParameter("scTitle");//素材标题
			String ageDetail = request.getParameter("scGrade");//年级
			String type = request.getParameter("scType");//分类
			String content = request.getParameter("content");//内容
			int i = findfw.updateSucai(id,name,ageDetail,type,content);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageFindFwServlet?do=8");
			}
		}

		//查看素材
		if(flag.equals("14")){
			String id = request.getParameter("id");
			List<Map<String, Object>> sucai = findfw.findFindSc(id);
			if(sucai.size()>0){
				if(sucai.get(0).get("time")!=null){
					String times = sucai.get(0).get("time").toString();
					try {
						//转换提交时间
						Date parse = df.parse(times);
						String format = df.format(parse);
						sucai.get(0).put("times", format);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			request.setAttribute("sucai", sucai);
			request.getRequestDispatcher("/WEB-INF/views/ziYuanGuanLi/suCai/suCai_select.jsp").forward(request, response);
		}
		
		/**
		 * 技法集合   - 条件+分页
		 */
		if(flag.equals("15")){
			//（年级等级，公共代码CODE_TYPE=014） - ageScale
			List<Map<String, Object>> ageDetail1 =  findfw.findAgescale();
			List<Map<String, Object>> ageDetail = new ArrayList<>();
			for (int i = 0;i < ageDetail1.size();i++) {
				String str = (String)ageDetail1.get(i).get("CODE_NAME");
				if (!str.equals("低年级")) {
					ageDetail.add(ageDetail1.get(i));
				}
			}
			request.setAttribute("ageDetail", ageDetail);
			//（技法分类，公共代码CODE_TYPE=032、033） - type
			//List<Map<String, Object>> type =  findfw.findType2();
			String dir = request.getSession().getServletContext().getRealPath("/jslib/fenlei.json");
			List<Map<String, String>> type =  new ArrayList<Map<String, String>>();
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
	                type.add(map);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			request.setAttribute("type", type);
			//条件
			String nj = request.getParameter("ageDetail");//年级
			String fl = request.getParameter("type");//分类
			String sname = request.getParameter("name");//素材标题
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
			//技法集合
			CurrentPage page = findfw.findJf(currentPage,numPerPage,nj,fl,name);
			List<Map<String, Object>> jflist = page.getResultList();
			if(jflist.size()>0){
				for(int i=0;i<jflist.size();i++){
					if(jflist.get(i).get("time")!=null){
						String times = jflist.get(i).get("time").toString();
						try {
							//转换提交时间
							Date parse = df.parse(times);
							String format = df.format(parse);
							jflist.get(i).put("times", format);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(jflist.get(i).get("type")!=null){
						String string = jflist.get(i).get("type").toString();
						String string2 = "";
						for(int j=0;j<type.size();j++){
							if(type.get(j).get("CODE").equals(string)){
								string2 = type.get(j).get("CODE_NAME");
							}
						}
						if(string2==""){
							if(jflist.get(i).get("style")!=null){
								String style = jflist.get(i).get("style").toString();
								for(int j=0;j<type.size();j++){
									if(type.get(j).get("CODE").equals(style)){
										string2 = type.get(j).get("CODE_NAME");
									}
								}
							}
						}
						jflist.get(i).put("type1", string2);
					}else {
						if(jflist.get(i).get("style")!=null){
							String string = jflist.get(i).get("style").toString();
							String string2 = "";
							for(int j=0;j<type.size();j++){
								if(type.get(j).get("CODE").equals(string)){
									string2 = type.get(j).get("CODE_NAME");
								}
							}
							jflist.get(i).put("type1", string2);
						}
					}
					
				}
			}else{
				request.setAttribute("m", "无搜索结果");
			}
			request.setAttribute("page", page);
			request.setAttribute("jflist", jflist);
			request.setAttribute("nj", nj);
			request.setAttribute("fl", fl);
			request.setAttribute("name", name);
			request.getRequestDispatcher("/WEB-INF/views/ziYuanGuanLi/jiFa/jiFa.jsp").forward(request, response);
		}

		//跳转  添加技法  页面
		if(flag.equals("16")){
			//（年级等级，公共代码CODE_TYPE=014） - ageScale
			List<Map<String, Object>> ageDetail =  findfw.findAgescale();
			request.setAttribute("ageDetail", ageDetail);
			//（技法分类，公共代码CODE_TYPE=032、033） - type
			//List<Map<String, Object>> type =  findfw.findType2();
			String dir = request.getSession().getServletContext().getRealPath("/jslib/fenlei.json");
			List<Map<String, String>> type =  new ArrayList<Map<String, String>>();
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
	                type.add(map);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			request.setAttribute("type", type);
			request.getRequestDispatcher("/WEB-INF/views/ziYuanGuanLi/jiFa/jiFa_add.jsp").forward(request, response);
		}

		//添加技法  -提交
		if(flag.equals("17")){
			String name = request.getParameter("jfTitle");//技法标题
			String nj = request.getParameter("jfGrade");//年级
			String type = request.getParameter("jfType");//分类
			String content = request.getParameter("content");//内容
			int i = findfw.addJifa(name,nj,type,content);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageFindFwServlet?do=15");
			}
		}

		//删除  技法
		if(flag.equals("18")){
			String id = request.getParameter("id");
			int i = findfw.deleteJf(id);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageFindFwServlet?do=15");
			}
		}

		//修改技法 -回显
		if(flag.equals("19")){
			String id = request.getParameter("id");
			//（年级等级，公共代码CODE_TYPE=014） - ageScale
			List<Map<String, Object>> ageDetail =  findfw.findAgescale();
			request.setAttribute("ageDetail", ageDetail);
			//（技法分类，公共代码CODE_TYPE=032、033） - type
			//List<Map<String, Object>> type =  findfw.findType2();
			String dir = request.getSession().getServletContext().getRealPath("/jslib/fenlei.json");
			List<Map<String, String>> type =  new ArrayList<Map<String, String>>();
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
	                type.add(map);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			request.setAttribute("type", type);
			List<Map<String, Object>> jifa = findfw.toFindJf(id);
			if(jifa.size()>0){
				if(jifa.get(0).get("type")!=null){
					String string = jifa.get(0).get("type").toString();
					String string2 = "";
					for(int j=0;j<type.size();j++){
						if(type.get(j).get("CODE").equals(string)){
							string2 = type.get(j).get("CODE_NAME");
						}
					}
					if(string2==""){
						if(jifa.get(0).get("style")!=null){
							String style = jifa.get(0).get("style").toString();
							for(int j=0;j<type.size();j++){
								if(type.get(j).get("CODE").equals(style)){
									string2 = type.get(j).get("CODE_NAME");
								}
							}
						}
					}
					jifa.get(0).put("type1", string2);
				}else {
					if(jifa.get(0).get("style")!=null){
						String string = jifa.get(0).get("style").toString();
						String string2 = "";
						for(int j=0;j<type.size();j++){
							if(type.get(j).get("CODE").equals(string)){
								string2 = type.get(j).get("CODE_NAME");
							}
						}
						jifa.get(0).put("type1", string2);
					}
				}
			
			}
			request.setAttribute("jifa", jifa);
			request.getRequestDispatcher("/WEB-INF/views/ziYuanGuanLi/jiFa/jiFa_update.jsp").forward(request, response);
		}

		//修改技法 -提交
		if(flag.equals("20")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			String name = request.getParameter("jfTitle");//技法标题
			String ageScale = request.getParameter("jfGrade");//年级
			String type = request.getParameter("jfType");//分类
			String content = request.getParameter("content");//内容
			int i = findfw.updateJifa(id,name,ageScale,type,content);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageFindFwServlet?do=15");
			}
		}
		
		//查看技法
		if(flag.equals("21")){
			String id = request.getParameter("id");
			List<Map<String, Object>> jifa = findfw.findFindJf(id);
			if(jifa.size()>0){
				String dir = request.getSession().getServletContext().getRealPath("/jslib/fenlei.json");
				List<Map<String, String>> type =  new ArrayList<Map<String, String>>();
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
		                type.add(map);
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				if(jifa.get(0).get("time")!=null){
					String times = jifa.get(0).get("time").toString();
					try {
						//转换提交时间
						Date parse = df.parse(times);
						String format = df.format(parse);
						jifa.get(0).put("times", format);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(jifa.get(0).get("type")!=null){
					String string = jifa.get(0).get("type").toString();
					String string2 = "";
					for(int j=0;j<type.size();j++){
						if(type.get(j).get("CODE").equals(string)){
							string2 = type.get(j).get("CODE_NAME");
						}
					}
					if(string2==""){
						if(jifa.get(0).get("style")!=null){
							String style = jifa.get(0).get("style").toString();
							for(int j=0;j<type.size();j++){
								if(type.get(j).get("CODE").equals(style)){
									string2 = type.get(j).get("CODE_NAME");
								}
							}
						}
					}
					jifa.get(0).put("type1", string2);
				}else {
					if(jifa.get(0).get("style")!=null){
						String string = jifa.get(0).get("style").toString();
						String string2 = "";
						for(int j=0;j<type.size();j++){
							if(type.get(j).get("CODE").equals(string)){
								string2 = type.get(j).get("CODE_NAME");
							}
						}
						jifa.get(0).put("type1", string2);
					}
				}
			}
			request.setAttribute("jifa", jifa);
			request.getRequestDispatcher("/WEB-INF/views/ziYuanGuanLi/jiFa/jiFa_select.jsp").forward(request, response);
		}
		
	}
	
	public void init() throws ServletException {
		// Put your code here
	}

}
