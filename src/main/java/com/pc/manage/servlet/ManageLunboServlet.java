package com.pc.manage.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.pc.manage.dao.ManageLunboDaoImpl;
import com.util.CommUtils;
import com.util.CurrentPage;
@WebServlet(name = "ManageLunboServlet", urlPatterns = "/servlet/ManageLunboServlet")  //标记为servlet，以便启动器扫描。
public class ManageLunboServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
     
	public ManageLunboServlet() {
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
		//获取登录用户name
		String userName = request.getSession().getAttribute("userName").toString();
		
		ManageLunboDaoImpl lunbo = new ManageLunboDaoImpl();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		int currentPage = 1;
		int numPerPage = 10;
		
		// 文件需要上传到的路径
        //String imgPath = request.getSession().getServletContext().getRealPath("/Upload");
        String imgPath = CommUtils.imgRootPath + "/slideshow/";
        // 文件需要添加到数据库的路径
        String url = "/files/pingdianapp_img" + "/slideshow/";
        //查看图片路径
        String host = CommUtils.getServerHost();
		
		/**
		 * 轮播图集合   - 条件+分页
		 */
		if(flag.equals("1")){
			String state = request.getParameter("state");
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
			//待点评作文
			CurrentPage page = lunbo.findLunbo(currentPage,numPerPage,state,name);
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
			request.setAttribute("state", state);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/huodongGuanli/lunBoGuanLi/lunBoTu.jsp").forward(request, response);
		}
		
		//轮播	删除待发布
		if(flag.equals("2")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			int i = lunbo.deleteDfb(id);
			if(i>0){
				response.sendRedirect(path+"/servlet/ManageLunboServlet?do=1");
			}
		}
		
		//轮播	查看
		if(flag.equals("3")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> lunboByid  = lunbo.getlunboByid(id);
			if(lunboByid.size()>0){
				if(lunboByid.get(0).get("IMAGE")!=null){
					String string = lunboByid.get(0).get("IMAGE").toString();
					//String img = CommUtils.generalImgUrl(string);
					
					/*String path1 = request.getSession().getServletContext().getRealPath("/Upload");
					String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                    System.out.println(uuid);
                    int start = string.lastIndexOf("\\");
                    String filename = uuid + string.substring(start + 1);
                    FileOutputStream fos = new FileOutputStream(path1+"/"+filename);
            		FileInputStream fis = new FileInputStream(new File(string));
            		byte[] buffer = new byte[1024];
            		int len = 0;
            		while ((len = fis.read(buffer)) > 0) {
            			fos.write(buffer, 0, len);
            		}
            		fos.close();
            		fis.close();
            		String img = "/Upload/"+filename;*/
            		request.setAttribute("img", host+string);
				}
			}
			request.setAttribute("lunbo", lunboByid);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/huodongGuanli/lunBoGuanLi/lunBo_select.jsp").forward(request, response);
		}
		
		//轮播	修改-回显
		if(flag.equals("4")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			List<Map<String, Object>> lunboByid  = lunbo.getlunboByid(id);
			if(lunboByid.size()>0){
				if(lunboByid.get(0).get("IMAGE")!=null){
					String string = lunboByid.get(0).get("IMAGE").toString();
					//String img = CommUtils.generalImgUrl(string);
            		request.setAttribute("img", host+string);
				}
			}
			request.setAttribute("lunbo", lunboByid);
			request.getRequestDispatcher("/WEB-INF/views/adminPC/huodongGuanli/lunBoGuanLi/lunBo_update.jsp").forward(request, response);
		}
		
		//轮播	修改-提交
		if(flag.equals("5")){
			String id = "";
			String state = "";
			String lbname = "";
			String oldPosition = "";
			String position = "";
			String image = "";
			String type = "";
			String contentUrl = "";
			String content = "";
			String storage = "";//0发布，1暂存
	        
			// 获得磁盘文件条目工厂
	        DiskFileItemFactory factory = new DiskFileItemFactory();
	        
	        // 如果目录不存在则创建
	        File uploadDir = new File(imgPath);
	        if (!uploadDir.exists()) {
	            uploadDir.mkdir();
	        }
	        factory.setRepository(new File(imgPath));
	        // 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
	        factory.setSizeThreshold(1024 * 1024 );
	        // 高水平的API文件上传处理
	        ServletFileUpload upload = new ServletFileUpload(factory);
	        try {
	        	List<FileItem> list = upload.parseRequest(request);
	        	Map<String,String> param = new HashMap<String,String>();
	            for (FileItem item : list) {
	                String name = item.getFieldName();// 获取表单的属性名字
	                // 如果获取的 表单信息是普通的 文本 信息
	                if (item.isFormField()) {
	                    // 获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的
	                    String value = item.getString("utf-8");
	                    //request.setAttribute(name, value);
	                    param.put(name, value);
	                    if(name.equals("id")){
	                    	id=value;
	                    }
	                    if(name.equals("state")){
	                    	state=value;
	                    }
	                    if(name.equals("name")){
	                    	lbname=value;
	                    }
	                    if(name.equals("oldPosition")){
	                    	oldPosition=value;
	                    }
	                    if(name.equals("position")){
	                    	position=value;
	                    }
	                    if(name.equals("type")){
	                    	type=value;
	                    }
	                    if(name.equals("contentUrl")){
	                    	contentUrl=value;
	                    }
	                    if(name.equals("content")){
	                    	content=value;
	                    }
	                    if(name.equals("storage")){
	                    	storage=value;
	                    }
	                } else { // 对传入的非 简单的字符串进行处理 ，比如说二进制的 图片，电影这些
	                	// 获取路径名
	                    String value = item.getName();
	                    if(value!=null&&!value.equals("")){
		                    // 索引到最后一个点
		                    int start = value.lastIndexOf(".");
		                    // 截取 上传文件的 字符串名字，加1是 去掉反斜杠，
		                    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		                    System.out.println(uuid);
		                    String filename = uuid + value.substring(start);
		                    OutputStream out = new FileOutputStream(new File(imgPath, filename));
		                    InputStream in = item.getInputStream();
		                    int length = 0;
		                    byte[] buf = new byte[1024];
		                    System.out.println("获取上传文件的总共的容量：" + item.getSize());
		                    // in.read(buf) 每次读到的数据存放在 buf 数组中
		                    while ((length = in.read(buf)) != -1) {
		                        // 在 buf 数组中 取出数据 写到 （输出流）磁盘上
		                        out.write(buf, 0, length);
		                    }
		                    out.flush(); 
		                    in.close();
		                    out.close();
		                    image = url+filename;
	                    }
	                }
	            }
	            int i= 0;
	            if(storage.equals("0")){//0--发布
	            	state="1";
	            }else if(storage.equals("1")){//1--暂存
	            	state="2";
	            }
	            i = lunbo.updateLunbo(id,state,lbname,oldPosition,position,image,type,contentUrl,content,userName);
				if(i>0){
					response.sendRedirect(path+"/servlet/ManageLunboServlet?do=1");
				}
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		
		//轮播	添加-暂存、 发布
		if(flag.equals("6")){
			//http://www.iteye.com/problems/80277
			String lbname = "";
			String position = "";
			String image = "";
			String type = "";
			String contentUrl = "";
			String content = "";
			String storage = "";//0发布，1暂存
	        
			// 获得磁盘文件条目工厂
	        DiskFileItemFactory factory = new DiskFileItemFactory();
	        
	        // 如果目录不存在则创建
	        File uploadDir = new File(imgPath);
	        if (!uploadDir.exists()) {
	            uploadDir.mkdir();
	        }
	        factory.setRepository(new File(imgPath));
	        // 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
	        factory.setSizeThreshold(1024 * 1024 );
	        // 高水平的API文件上传处理
	        ServletFileUpload upload = new ServletFileUpload(factory);
	        try {
	        	List<FileItem> list = upload.parseRequest(request);
	        	Map<String,String> param = new HashMap<String,String>();
	            for (FileItem item : list) {
	                String name = item.getFieldName();// 获取表单的属性名字
	                // 如果获取的 表单信息是普通的 文本 信息
	                if (item.isFormField()) {
	                    // 获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的
	                    String value = item.getString("utf-8");
	                    //request.setAttribute(name, value);
	                    param.put(name, value);
	                    if(name.equals("name")){
	                    	lbname=value;
	                    }
	                    if(name.equals("position")){
	                    	position=value;
	                    }
	                    if(name.equals("type")){
	                    	type=value;
	                    }
	                    if(name.equals("contentUrl")){
	                    	contentUrl=value;
	                    }
	                    if(name.equals("content")){
	                    	content=value;
	                    }
	                    if(name.equals("storage")){
	                    	storage=value;
	                    }
	                } else { // 对传入的非 简单的字符串进行处理 ，比如说二进制的 图片，电影这些
	                	// 获取路径名
	                    String value = item.getName();
	                    if(value!=null&&!value.equals("")){
	                    	String uuid = UUID.randomUUID().toString().replaceAll("-", "");
	                    	// 索引到最后一个点
	                    	int start = value.lastIndexOf(".");
	                    	// 截取 上传文件的 字符串名字，加1是 去掉反斜杠，
	                    	String filename = uuid + value.substring(start);
	                    	OutputStream out = new FileOutputStream(new File(imgPath, filename));
	                    	InputStream in = item.getInputStream();
	                    	int length = 0;
	                    	byte[] buf = new byte[1024];
	                    	// in.read(buf) 每次读到的数据存放在 buf 数组中
	                    	while ((length = in.read(buf)) != -1) {
	                    		// 在 buf 数组中 取出数据 写到 （输出流）磁盘上
	                    		out.write(buf, 0, length);
	                    	}
	                    	out.flush(); 
	                    	in.close();
	                    	out.close();
	                    	image = url+filename;
	                    }
	                }
	            }
	            int i= 0;
	            if(storage.equals("0")){//0--发布
	            	i = lunbo.addLunbo(lbname,position,image,type,contentUrl,content,userName);
	            }else if(storage.equals("1")){//1--暂存
	            	i = lunbo.temporary(lbname,position,image,type,contentUrl,content,userName);
	            }
				if(i>0){
					response.sendRedirect(path+"/servlet/ManageLunboServlet?do=1");
				}
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("e-------------------------------"+e.getMessage());
	        }
		}
		
		//轮播	发布
		if(flag.equals("7")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			String p = request.getParameter("position");//位置
			int position = Integer.parseInt(p);
			int i = lunbo.updateFabu(id,position);
			response.sendRedirect(path+"/servlet/ManageLunboServlet?do=1");
		}
		
		//轮播	下架
		if(flag.equals("8")){
			String sid = request.getParameter("id");
			int id = Integer.parseInt(sid);
			int i = lunbo.updateXiajia(id);
			response.sendRedirect(path+"/servlet/ManageLunboServlet?do=1");
		}
		
	}
	
	public void init() throws ServletException {
		// Put your code here
	}
}
