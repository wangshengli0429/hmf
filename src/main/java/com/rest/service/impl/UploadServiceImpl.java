package com.rest.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.entity.BaseSqlResultBean;
import com.entity.Composition;
import com.entity.Teacher;
import com.rest.service.UploadService;
import com.rest.service.dao.OrderDao;
import com.rest.service.dao.UploadDao;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.util.CommUtils;
import com.util.Constant;
import com.util.FileUpload;
import com.util.SecurityUtil;
import com.util.picMerge;
import org.springframework.stereotype.Component;

@Component
public class UploadServiceImpl implements UploadService {

	private static Logger logger = Logger.getLogger(UploadServiceImpl.class);
	
	private UploadDao uploadDao;
	private OrderDao orderDao;

	public UploadDao getUploadDao() {
		return uploadDao;
	}

	public void setUploadDao(UploadDao uploadDao) {
		this.uploadDao = uploadDao;
	}

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	// 获取文件类型
	private String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos);
	}

	// 上传作文
	@POST
	@Path("/insertComposi")
	@Produces(MediaType.APPLICATION_JSON)
	public String insertComposi(String json) throws ParseException {
		// json = "{\"BM\":{\"token\": \"1\",\"udid\": \"1\",\"content\":
		// \"content\",\"draft\": \"draft\",\"grade\": \"grade\",\"tid\":
		// \"1\",\"out_trade_no\":\"out_trade_no\",\"image1\":\"E:/wuhuan/b.jpg\",\"newtitle\":
		// \"newtitle\",\"oldtitle\":\"oldtitle\",\"propo\": \"propo\"}}";
		logger.info("----insertComposi json:" + json);
		String headPath = "F:/server/cedu-files/";
		Composition comp = new Composition();
		FileUpload fu = new FileUpload();
		JSONObject jsonObject;
		String image1 = "";
		String image2 = "";
		String image3 = "";
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			if (SecurityUtil.isUserLogin(token)) {
				String content = str1.getString("content");// 正文
				String draft = str1.getString("draft");// 第几稿
				String grade = str1.getString("grade");
				logger.info("----insertComposi方法，grade=" + grade);
				//年级没有上传的情况
				if ("".equals(grade)) {
					grade = uploadDao.findGradeByUdid(udid);
				}
				int tid = str1.getInt("tid");
				String outTradeNo = str1.getString("out_trade_no");// 订单ID
				Calendar a = Calendar.getInstance();
				int year = a.get(Calendar.YEAR);
				int month = a.get(Calendar.MONTH) + 1;
				String url = "composition/" + year + "/" + month + "/";
				if (str1.has("image1")
						&& !TextUtils.isEmpty(str1.getString("image1"))) {
					String img1 = str1.getString("image1");// 作文图片
					int start = img1.lastIndexOf("/");
					String filename = img1.substring(start + 1);
					try {
						image1 = fu.uploadContent(new File(headPath + img1),
								url, filename);
						logger.info("----insertComposi image1:" + image1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					image1 = "";
				}
				if (str1.has("image2")
						&& !TextUtils.isEmpty(str1.getString("image2"))) {
					String img2 = str1.getString("image2");
					int start = img2.lastIndexOf("/");
					String filename = img2.substring(start + 1);
					try {
						image2 = fu.uploadContent(new File(headPath + img2),
								url, filename);
						logger.info("----insertComposi image2:" + image2);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					image2 = "";
				}
				if (str1.has("image3")
						&& !TextUtils.isEmpty(str1.getString("image3"))) {
					String img3 = str1.getString("image3");
					int start = img3.lastIndexOf("/");
					String filename = img3.substring(start + 1);
					try {
						image3 = fu.uploadContent(new File(headPath + img3),
								url, filename);
						logger.info("----uploadContent image3:" + image3);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					image3 = "";
				}
				String newtitle = str1.getString("newtitle");// 新标题
				String oldtitle = str1.getString("oldtitle");// 原标题
				String propo = str1.getString("propo");// 命题要求

				comp.setUdid(udid);
				comp.setContent(content);
				comp.setDraft(draft);
				comp.setNewtitle(newtitle);
				comp.setOldtitle(oldtitle);
				comp.setPropo(propo);
				Date now = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String ctime = dateFormat.format(now);
				comp.setCreateTime(dateFormat.parse(ctime));// 创建时间
				comp.setCstate(1);// 状态
				comp.setType("4");// 题材类型
				comp.setState("2");
				comp.setGrade(grade);
				comp.setImage1(image1);
				comp.setImage2(image2);
				comp.setImage3(image3);
				logger.info("----insertComposi image1:" + image1 + "image2"
						+ image2 + "image3" + image3);
				comp.setTid(tid + "");
				BaseSqlResultBean rest = uploadDao.insertComposi(comp);
				if (rest.getSqlCode() > 0) { // 插入作文成功
					// 修改订单表的关联的作文
					Map<String, Object> orderMap = new HashMap<>();
					orderMap.put("compId", rest.getSqlID());
					orderMap.put("outTradeNo", outTradeNo);
					orderMap.put("update_time", ctime);
					orderMap.put("title", newtitle);
					orderMap.put("draft", draft);
					orderMap.put("tid", tid);
					orderMap.put("udid", udid);
					// orderMap.put("stuId", value);
					int resultCode = orderDao
							.AddCompToOrder(orderMap, tid + "");
					if (resultCode > 0) {
						return "{\"BM\":{\"token\":\"" + token
								+ "\",\"udid\":\"" + udid
								+ "\"},\"EC\":0,\"EM\":\"\"}";
					} else {
						return "{\"BM\":{},\"EC\":20010,\"EM\":\"修改订单表失败\"}";
					}
				} else {
					return "{\"BM\":{},\"EC\":20009,\"EM\":\"作文上传失败\"}";
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}
	
	// 上传师资认证
	@POST
	@Path("/insertAuthent")
	@Produces(MediaType.APPLICATION_JSON)
	public String insertAuthent(String json) throws ParseException {
		logger.info("----insertAuthent方法----------");
		Teacher tea = new Teacher();
		String folder = "F:/server/cedu-files/";
		JSONObject jsonObject;
		String image1 = "";
		String image2 = "";
		String image3 = "";
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			if (SecurityUtil.isUserLogin(token)) {
				String url = "teacher/";
				if (str1.has("image1")) {
					String img1 = str1.getString("image1");// 教师资格证
					int start = img1.lastIndexOf("/");
					String filename = img1.substring(start + 1);
					FileUpload fu = new FileUpload();
					try {
						image1 = fu.uploadContent(new File(folder + img1), url,
								filename);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					image1 = "";
				}
				if (str1.has("image2")) {
					String img2 = str1.getString("image2");// 身份证1
					int start = img2.lastIndexOf("/");
					String filename = img2.substring(start + 1);
					FileUpload fu = new FileUpload();
					try {
						image2 = fu.uploadContent(new File(folder + img2), url,
								filename);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					image2 = "";
				}
				if (str1.has("image3")) {
					String img3 = str1.getString("image3");// 身份证2
					int start = img3.lastIndexOf("/");
					String filename = img3.substring(start + 1);
					FileUpload fu = new FileUpload();
					try {
						image3 = fu.uploadContent(new File(folder + img3), url,
								filename);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					image3 = "";
				}
				tea.setUdid(udid);
				tea.setCerturl(image1);
				tea.setCard1(image2);
				tea.setCard2(image3);
				int rest = uploadDao.insertAuthent(tea);
				if (rest > 0) {
					// SecurityUtil.sessionList.add(token);
					return "{\"BM\":{\"token\":\"" + token + "\",\"udid\":\""
							+ udid + "\"},\"EC\":0,\"EM\":\"\"}";
				} else {
					return "{\"BM\":{},\"EC\":20009,\"EM\":\"师资认证上传失败\"}";
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

	// 获取教师师资信息
	@POST
	@Path("/getAuthent")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAuthent(String json) throws ParseException {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String token = str1.getString("token");
			String udid = str1.getString("udid");
			JSONObject result = new JSONObject();
			if (SecurityUtil.isUserLogin(token)) {
				// auState 审核的状态 string (0审核中，1已通过，2未通过 , 3禁用 4未上传)
				// card1 身份证正面
				// card2 身份证反面
				// certUrl 资格证书照
				List<Map<String, Object>> authInfo = uploadDao
						.getTeacherAuthInfo(udid);
				if (authInfo != null && authInfo.size() > 0) {
					result.put("BM", authInfo.get(0));
				} else {
					result.put("BM", "");
				}
				result.put("EC", 0);
				result.put("EM", "");
				return result.toString();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":20003,\"EM\":\"参数错误\"}";
		}
		return Constant.PLEASE_LOGIN;
	}

	/**
	 * 师资认证多文件上传
	 * 
	 * @param attchments
	 * @param request
	 * @return
	 */
	@POST
	@Path("/tUploadlist")
	@Consumes("multipart/form-data")
	@Override
	public String tUploadFileList(List<Attachment> attachments,
			@Context HttpServletRequest request) {
		logger.info("----tUploadlist方法----------");
		if (attachments.size() < 5) {
			return "{\"BM\":{},\"EC\":11241,\"EM\":\"上传资料不完整\"}";
		}
		Calendar a = Calendar.getInstance();
		int year = a.get(Calendar.YEAR);
		int month = a.get(Calendar.MONTH) + 1;
		String path = "teacher/" + year + "/" + month;
		String udid = "";
		String token = "";
		String image1 = "";
		String image2 = "";
		String image3 = "";
		Teacher tea = new Teacher();
		for (Attachment attach : attachments) {
			DataHandler dh = attach.getDataHandler();

			if (attach.getContentType().toString().contains(("text/plain"))) {
				try {
					String type = dh.getName();
					if ("udid".equals(type)) {
						udid = writeToString(dh.getInputStream());
					}
					if ("token".equals(dh.getName())) {
						token = writeToString(dh.getInputStream());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					String image = dh.getName();
					String result = writeToFile(dh.getInputStream(), path, 1, "222.jpg");
					if (result.equals("catch")) {
						return "{\"BM\":{},\"EC\":11242,\"EM\":\"上传图片失败，请重新上传\"}";
					}
					if (result.equals("isNull")) {
						return "{\"BM\":{},\"EC\":11241,\"EM\":\"上传资料不完整\"}";
					}
					if (result.equals("tooBig")) {
						return "{\"BM\":{},\"EC\":11244,\"EM\":\"上传图片过大，请重新上传\"}";
					}
					if (StringUtils.isBlank(image)) {
						image = "";
					}
					if (image.contains(("2"))) {
						image2 = result;
					}
					if (image.contains(("3"))) {
						image3 = result;
					}
					if (image.contains(("1"))) {
						image1 = result;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (!SecurityUtil.isUserLogin(token)) {
			return Constant.PLEASE_LOGIN;
		}
		if (image1.equals("") || image2.equals("") || image3.equals("")) {
			return "{\"BM\":{},\"EC\":11242,\"EM\":\"上传图片失败，请重新上传\"}";
		}
		tea.setUdid(udid);
		tea.setCerturl(image1);
		tea.setCard1(image2);
		tea.setCard2(image3);
		int rest = uploadDao.insertAuthent(tea);
		if (rest > 0) {
			return "{\"BM\":{\"token\":\"" + token + "\",\"udid\":\"" + udid
					+ "\"},\"EC\":0,\"EM\":\"\"}";
		} else {
			return "{\"BM\":{},\"EC\":11243,\"EM\":\"师资认证上传失败\"}";
		}
	}

	/**
	 * 作文多文件上传
	 * 
	 * @param attchments
	 * @param request
	 * @return
	 */
	@POST
	@Path("/sUploadlist")
	@Consumes("multipart/form-data")
	@Override
	public String sUploadFileList(List<Attachment> attachments,
			@Context HttpServletRequest request) {
		logger.info("----sUploadlist方法----------");
		Calendar a = Calendar.getInstance();
		int year = a.get(Calendar.YEAR);
		int month = a.get(Calendar.MONTH) + 1;
		String path = "composition/" + year + "/" + month;

		String content = "";// 正文
		String draft = "";// 第几稿
		String grade = "";// 年级
		String newtitle = "";// 新标题
		String oldtitle = "";// 原标题
		String out_trade_no = "";// 订单ID
		String propo = "";// 命题要求
		String tid = "";// 教师id
		String udid = "";
		String token = "";
		String image1 = "";
		String image2 = "";
		String image3 = "";

		Composition comp = new Composition();
		
		int count = 0;//作文图片个数计数器

		for (Attachment attach : attachments) {
			DataHandler dh = attach.getDataHandler();

			if (attach.getContentType().toString().contains(("text/plain"))) {
				try {
					String type = dh.getName();
					if ("udid".equals(type)) {
						udid = writeToString(dh.getInputStream());
					}
					if ("token".equals(dh.getName())) {
						token = writeToString(dh.getInputStream());
					}
					if ("content".equals(dh.getName())) {
						content = writeToString(dh.getInputStream());
					}
					if ("draft".equals(dh.getName())) {
						draft = writeToString(dh.getInputStream());
					}
					if ("grade".equals(dh.getName())) {
						grade = writeToString(dh.getInputStream());
					}
					if ("newtitle".equals(dh.getName())) {
						newtitle = writeToString(dh.getInputStream());
					}
					if ("oldtitle".equals(dh.getName())) {
						oldtitle = writeToString(dh.getInputStream());
					}
					if ("out_trade_no".equals(dh.getName())) {
						out_trade_no = writeToString(dh.getInputStream());
					}
					if ("propo".equals(dh.getName())) {
						propo = writeToString(dh.getInputStream());
					}
					if ("tid".equals(dh.getName())) {
						tid = writeToString(dh.getInputStream());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					String image = dh.getName();
					String result = writeToFile(dh.getInputStream(), path, 1, "22.jpg");
					if (result.equals("catch")) {
						return "{\"BM\":{},\"EC\":11274,\"EM\":\"上传图片失败，请重新上传\"}";
					}
					if (result.equals("isNull")) {
						count = count - 1;
					}
					if (result.equals("tooBig")) {
						return "{\"BM\":{},\"EC\":11275,\"EM\":\"上传图片过大，请重新上传\"}";
					}
					if (StringUtils.isBlank(image)) {
						image = "";
					}
					if (image.contains(("2"))) {
						image2 = result;
					}
					if (image.contains(("3"))) {
						image3 = result;
					}
					if (image.contains(("1"))) {
						image1 = result;
					}
					count +=1;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (!SecurityUtil.isUserLogin(token)) {
			return Constant.PLEASE_LOGIN;
		}

		if ("".equals(grade)) {
			return "{\"BM\":{},\"EC\":12201,\"EM\":\"上传失败！请您修改基本信息，选择年级后再上传作文\"}";
		}
		
		if (count == 1 && image1.equals("")) {
			return "{\"BM\":{},\"EC\":11274,\"EM\":\"上传图片失败，请重新上传\"}";
		}
		if (count == 2 && (image1.equals("") || image2.equals(""))) {
			return "{\"BM\":{},\"EC\":11274,\"EM\":\"上传图片失败，请重新上传\"}";
		}
		if (count == 3 && (image1.equals("") || image2.equals("") || image3.equals(""))) {
			return "{\"BM\":{},\"EC\":11274,\"EM\":\"上传图片失败，请重新上传\"}";
		}

		try {
			logger.info("----sUploadlist方法，grade=" + grade);
			//年级没有上传的情况
			if ("".equals(grade)) {
				grade = uploadDao.findGradeByUdid(udid);
			}
			comp.setUdid(udid);
			comp.setContent(content);
			comp.setDraft(draft);
			comp.setNewtitle(newtitle);
			comp.setOldtitle(oldtitle);
			comp.setPropo(propo);
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String ctime = dateFormat.format(now);
			comp.setCreateTime(dateFormat.parse(ctime));// 创建时间
			comp.setCstate(1);// 状态
			comp.setType("4");// 题材类型
			comp.setState("2");
			comp.setGrade(grade);
			comp.setImage1(image1);
			comp.setImage2(image2);
			comp.setImage3(image3);
			logger.info("----老流程insertComposi image1:" + image1 + "image2"
					+ image2 + "image3" + image3);
			comp.setTid(tid + "");
			BaseSqlResultBean rest = uploadDao.insertComposi(comp);
			if (rest.getSqlCode() > 0) { // 插入作文成功
				// 修改订单表的关联的作文
				Map<String, Object> orderMap = new HashMap<>();
				orderMap.put("compId", rest.getSqlID());
				orderMap.put("outTradeNo", out_trade_no);
				orderMap.put("update_time", ctime);
				orderMap.put("title", newtitle);
				orderMap.put("draft", draft);
				orderMap.put("tid", tid);
				orderMap.put("udid", udid);
				// orderMap.put("stuId", value);
				int resultCode = orderDao.AddCompToOrder(orderMap, tid + "");
				if (resultCode > 0) {
					return "{\"BM\":{\"token\":\"" + token + "\",\"udid\":\""
							+ udid + "\"},\"EC\":0,\"EM\":\"\"}";
				} else {
					return "{\"BM\":{},\"EC\":11273,\"EM\":\"修改订单表失败\"}";
				}
			} else {
				return "{\"BM\":{},\"EC\":11272,\"EM\":\"作文上传失败\"}";
			}
		} catch (Exception e) {
			return "{\"BM\":{},\"EC\":11271,\"EM\":\"参数错误\"}";
		}

	}

	private String writeToFile(InputStream file, String path, int i, String geshi) {
		OutputStream os = null;
		InputStream is = null;
		try {
			// 设置输出文件夹
			String rootPath = "";
			String uploadPath = "";
			if (i == 1) {
				rootPath = CommUtils.imgRootPath + "/" + path + "/";
				uploadPath = "/files/pingdianapp_img/";
			}else {
				rootPath = CommUtils.voiceRootPath + "/" + path + "/";
				uploadPath = "/files/pingdianapp_voice/";
			}
			File rootFolder = new File(rootPath);
			if (!rootFolder.exists()) {
				rootFolder.mkdirs();
			}
			File file2 = new File(rootPath);
			if (!file2.exists()) {
				file2.mkdir();
			}

			// logger.info("------" + rootPath+"------" );

			// System.out.println("------" + rootPath);
			is = file;
			if (is.available() > 5242880) {
				return "tooBig";
				//return "{\"BM\":{},\"EC\":20444,\"EM\":\"文件大小超出5m\"}";
			}
			if (is.available() == 0) {
				return "isNull";
			}

			// logger.info("图片大小:------" + is.available()+"------" );

			String imgName = new Date().getTime() + getExtention(geshi);
			os = new FileOutputStream(new File(rootPath + imgName));
			byte[] b = new byte[5242880];
			int bytesRead = 0;
			while ((bytesRead = is.read(b)) != -1) {

				// logger.info("图片上传过程++++++++++++++:------" +
				// bytesRead+"------" );

				os.write(b, 0, bytesRead);
			}
			String rest = uploadPath + path + "/" + imgName;

			// logger.info("图片上传完成++++++++++++++:------" + rest+"------" );

			return rest;
		} catch (Exception e) {
			return "catch";
			// logger.info("-----------错误信息二进制文件上传------"+ e.getMessage() );
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// logger.info("-----------流未正常关闭------"+ e.getMessage() );
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// logger.info("-----------流未正常关闭------"+ e.getMessage() );
				}
			}
		}
		
	}

	private String writeToString(InputStream ins) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int i = -1;
		while ((i = ins.read(b)) != -1) {
			out.write(b, 0, i);
		}
		ins.close();
		return new String(out.toByteArray(), "UTF-8");
	}

	 /** 
     * 修改作文多文件上传 
     * @param attchments 
     * @param request 
     * @return 
     */ 
	@Override
    @POST  
    @Path("/sModifyCompositionUploadlist2")  
    @Consumes("multipart/form-data")  
	public String sModifyCompositionUploadlist2(List<Attachment> attachments, HttpServletRequest request) {
    	Calendar a = Calendar.getInstance();
		int year = a.get(Calendar.YEAR);
		int month = a.get(Calendar.MONTH) + 1;
		String path = "composition/" + year + "/" + month;
		
		String cid = "";//作文id
		String content = "";// 正文
		String draft = "";// 第几稿
		String newtitle = "";// 新标题
		String oldtitle = "";// 原标题
		String propo = "";// 命题要求
		String udid = "";
		String token = "";
		String image1 = "";
		String image2 = "";
		String image3 = "";

		Composition comp = new Composition();
		
		int count = 0;//作文图片个数计数器

		for (Attachment attach : attachments) {
			DataHandler dh = attach.getDataHandler();

			if (attach.getContentType().toString().contains(("text/plain"))) {
				try {
					String type = dh.getName();
					if ("udid".equals(type)) {
						udid = writeToString(dh.getInputStream());
					}
					if ("token".equals(dh.getName())) {
						token = writeToString(dh.getInputStream());
					}
					if ("content".equals(dh.getName())) {
						content = writeToString(dh.getInputStream());
					}
					if ("draft".equals(dh.getName())) {
						draft = writeToString(dh.getInputStream());
					}
					if ("newtitle".equals(dh.getName())) {
						newtitle = writeToString(dh.getInputStream());
					}
					if ("oldtitle".equals(dh.getName())) {
						oldtitle = writeToString(dh.getInputStream());
					}
					if ("propo".equals(dh.getName())) {
						propo = writeToString(dh.getInputStream());
					}
					if ("cid".equals(dh.getName())) {
						cid = writeToString(dh.getInputStream());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					String image = dh.getName();
					String result = writeToFile(dh.getInputStream(), path, 1, "222.jpg");
					if (result.equals("catch")) {
						return "{\"BM\":{},\"EC\":11274,\"EM\":\"上传图片失败，请重新上传\"}";
					}
					if (result.equals("isNull")) {
						count = count - 1;
					}
					if (result.equals("tooBig")) {
						return "{\"BM\":{},\"EC\":11275,\"EM\":\"上传图片过大，请重新上传\"}";
					}
					if (StringUtils.isBlank(image)) {
						image = "";
					}
					if (image.contains(("2"))) {
						image2 = result;
					}
					if (image.contains(("3"))) {
						image3 = result;
					}
					if (image.contains(("1"))) {
						image1 = result;
					}
					count +=1;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (!SecurityUtil.isUserLogin(token)) {
			return Constant.PLEASE_LOGIN;
		}

		if (count == 1 && image1.equals("")) {
			return "{\"BM\":{},\"EC\":11274,\"EM\":\"上传图片失败，请重新上传\"}";
		}
		if (count == 2 && (image1.equals("") || image2.equals(""))) {
			return "{\"BM\":{},\"EC\":11274,\"EM\":\"上传图片失败，请重新上传\"}";
		}
		if (count == 3 && (image1.equals("") || image2.equals("") || image3.equals(""))) {
			return "{\"BM\":{},\"EC\":11274,\"EM\":\"上传图片失败，请重新上传\"}";
		}

		try {
			comp.setUdid(udid);
			comp.setContent(content);
			comp.setDraft(draft);
			comp.setNewtitle(newtitle);
			comp.setOldtitle(oldtitle);
			comp.setPropo(propo);
			comp.setImage1(image1);
			comp.setImage2(image2);
			comp.setImage3(image3);
			comp.setId(Integer.parseInt(cid));
			logger.info("----insertComposi修改上传图片 image1:" + image1 + "image2"
					+ image2 + "image3" + image3);
			int i = uploadDao.updateComposi(comp);
			if (i > 0) { 
				return "{\"BM\":{\"token\":\"" + token + "\",\"udid\":\""
						+ udid + "\"},\"EC\":0,\"EM\":\"\"}";
			} else {
				return "{\"BM\":{},\"EC\":11272,\"EM\":\"修改作文失败\"}";
			}
		} catch (Exception e) {
			return "{\"BM\":{},\"EC\":11271,\"EM\":\"参数错误\"}";
		}
	}
    
    /** 
     * 老师端未批改作文语音点评 
     * @param attchments 
     * @param request 
     * @return 
     */  
    @POST  
    @Path("/tSpeechCommentUploadlist")  
    @Consumes("multipart/form-data")  
	public String tSpeechCommentUploadlist(List<Attachment> attachments, HttpServletRequest request) {
    	logger.info("----tSpeechCommentUploadlist方法----------");
    	Calendar a = Calendar.getInstance();
		int year = a.get(Calendar.YEAR);
		int month = a.get(Calendar.MONTH) + 1;
		String path = "voice/" + year + "/" + month;
		
		String udid = "";
		String token = "";
		String cid = "";//作文id
		String voice = "";
		String score = "";
		String suggest = "";
		String recordTime = "";
		
		for (Attachment attach : attachments) {
			DataHandler dh = attach.getDataHandler();

			if (attach.getContentType().toString().contains(("text/plain"))) {
				try {
					String type = dh.getName();
					if ("udid".equals(type)) {
						udid = writeToString(dh.getInputStream());
					}
					if ("token".equals(dh.getName())) {
						token = writeToString(dh.getInputStream());
					}
					if ("cid".equals(type)) {
						cid = writeToString(dh.getInputStream());
					}
					if ("score".equals(type)) {
						score = writeToString(dh.getInputStream());
					}
					if ("suggest".equals(type)) {
						suggest = writeToString(dh.getInputStream());
					}
					if ("recordTime".equals(type)) {
						recordTime = writeToString(dh.getInputStream());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					String image = dh.getName();
					String result = writeToFile(dh.getInputStream(), path, 2, "222.amr");
					if (result.equals("catch")) {
						return "{\"BM\":{},\"EC\":12181,\"EM\":\"语音上传失败，请重新上传\"}";
					}
					if (result.equals("isNull")) {
						return "{\"BM\":{},\"EC\":12182,\"EM\":\"您没有录制语音，请重新上传\"}";
					}
					if (result.equals("tooBig")) {
						return "{\"BM\":{},\"EC\":12183,\"EM\":\"上传语音过长，请重新上传\"}";
					}
					if (image.contains(("amr"))) {
						voice = result;
					}
				} catch (IOException e) {
					logger.info("----------文件解析失败-----------");
				}
			}
		}

		if (!SecurityUtil.isUserLogin(token)) {
			return Constant.PLEASE_LOGIN;
		}

		try {
			int j = uploadDao.selectCom_composition(udid,cid);
			if(j == 0){
				int i = uploadDao.insertCom_compositionBySpeech(cid, voice, score, suggest, recordTime);
				if (i > 0) { 
					return "{\"BM\":{\"token\":\"" + token + "\",\"udid\":\""
							+ udid + "\"},\"EC\":0,\"EM\":\"\"}";
				} else {
					return "{\"BM\":{},\"EC\":12184,\"EM\":\"作文语音点评失败\"}";
				}
			} else {
				int i = uploadDao.updateCom_compositionBySpeech(cid, voice, score, suggest, recordTime);
				if (i > 0) { 
					return "{\"BM\":{\"token\":\"" + token + "\",\"udid\":\""
							+ udid + "\"},\"EC\":0,\"EM\":\"\"}";
				} else {
					return "{\"BM\":{},\"EC\":12184,\"EM\":\"作文语音点评失败\"}";
				}
				//return "{\"BM\":{},\"EC\":12189,\"EM\":\"作文已点评\"}";
			}
		} catch (Exception e) {
			return "{\"BM\":{},\"EC\":12185,\"EM\":\"参数错误\"}";
		}
	}

    /** 
     * 作文多文件上传新流程
     * @param attchments 
     * @param request 
     * @return 
     */  
	@Override
	@POST  
	@Path("/sUploadlist2")  
    @Consumes("multipart/form-data")  
	public String sUploadlist2(List<Attachment> attachments,
			@Context HttpServletRequest request) {
		logger.info("----sUploadlist2方法----------");
		Calendar a = Calendar.getInstance();
		int year = a.get(Calendar.YEAR);
		int month = a.get(Calendar.MONTH) + 1;
		String path = "composition/" + year + "/" + month;

		String content = "";// 正文
		String draft = "";// 第几稿
		String grade = "";// 年级
		String newtitle = "";// 新标题
		String oldtitle = "";// 原标题
		String propo = "";// 命题要求
		String tid = "";// 教师id
		String udid = "";
		String token = "";
		String image1 = "";
		String image2 = "";
		String image3 = "";
		boolean open = true;//true为公开

		Composition comp = new Composition();
		
		int count = 0;//作文图片个数计数器

		for (Attachment attach : attachments) {
			DataHandler dh = attach.getDataHandler();

			if (attach.getContentType().toString().contains(("text/plain"))) {
				try {
					String type = dh.getName();
					if ("udid".equals(type)) {
						udid = writeToString(dh.getInputStream());
					}
					if ("token".equals(dh.getName())) {
						token = writeToString(dh.getInputStream());
					}
					if ("content".equals(dh.getName())) {
						content = writeToString(dh.getInputStream());
					}
					if ("draft".equals(dh.getName())) {
						draft = writeToString(dh.getInputStream());
					}
					if ("grade".equals(dh.getName())) {
						grade = writeToString(dh.getInputStream());
					}
					if ("newtitle".equals(dh.getName())) {
						newtitle = writeToString(dh.getInputStream());
					}
					if ("oldtitle".equals(dh.getName())) {
						oldtitle = writeToString(dh.getInputStream());
					}
					if ("propo".equals(dh.getName())) {
						propo = writeToString(dh.getInputStream());
					}
					if ("tid".equals(dh.getName())) {
						tid = writeToString(dh.getInputStream());
					}
					if ("open".equals(dh.getName())) {
						open = Boolean.valueOf(writeToString(dh.getInputStream()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					String image = dh.getName();
					String result = writeToFile(dh.getInputStream(), path, 1, "22.jpg");
					if (result.equals("catch")) {
						return "{\"BM\":{},\"EC\":11274,\"EM\":\"上传图片失败，请重新上传\"}";
					}
					if (result.equals("isNull")) {
						count = count - 1;
					}
					if (result.equals("tooBig")) {
						return "{\"BM\":{},\"EC\":11275,\"EM\":\"上传图片过大，请重新上传\"}";
					}
					if (StringUtils.isBlank(image)) {
						image = "";
					}
					if (image.contains(("2"))) {
						image2 = result;
					}
					if (image.contains(("3"))) {
						image3 = result;
					}
					if (image.contains(("1"))) {
						image1 = result;
					}
					count +=1;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (!SecurityUtil.isUserLogin(token)) {
			return Constant.PLEASE_LOGIN;
		}

		if (count == 1 && image1.equals("")) {
			return "{\"BM\":{},\"EC\":11274,\"EM\":\"上传图片失败，请重新上传\"}";
		}
		if (count == 2 && (image1.equals("") || image2.equals(""))) {
			return "{\"BM\":{},\"EC\":11274,\"EM\":\"上传图片失败，请重新上传\"}";
		}
		if (count == 3 && (image1.equals("") || image2.equals("") || image3.equals(""))) {
			return "{\"BM\":{},\"EC\":11274,\"EM\":\"上传图片失败，请重新上传\"}";
		}

		try {
			logger.info("----sUploadlist2方法，年级参数grade=" + grade);
			//年级没有上传的情况
			if ("".equals(grade)) {
				grade = uploadDao.findGradeByUdid(udid);
			}
			comp.setUdid(udid);
			comp.setContent(content);
			comp.setDraft(draft);
			comp.setNewtitle(newtitle);
			comp.setOldtitle(oldtitle);
			comp.setPropo(propo);
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String ctime = dateFormat.format(now);
			comp.setCreateTime(dateFormat.parse(ctime));// 创建时间
			comp.setCstate(1);// 状态
			comp.setType("4");// 题材类型
			comp.setState("0");//代付款
			comp.setGrade(grade);
			comp.setImage1(image1);
			comp.setImage2(image2);
			comp.setImage3(image3);
			comp.setOpen(open);
			System.out.println("----新流程insertComposi image1:" + image1 + ",image2:"
					+ image2 + ",image3:" + image3);
			logger.info("----新流程insertComposi image1:" + image1 + ",image2:"
					+ image2 + ",image3:" + image3);
			if (!"".equals(tid)) {
				comp.setTid(tid + "");
			}
			BaseSqlResultBean rest = uploadDao.insertComposi(comp);
			if (rest.getSqlCode() > 0) { // 插入作文成功
				logger.info("---------------上传作文成功，等待付款------------");
				/*uploadDao.insertInformation(udid, tid);*/
				String id = rest.getSqlID() + "";
				return "{\"BM\":{\"token\":\"" + token + "\",\"udid\":\""
						+ udid + "\",\"cid\":\"" + id + "\"},\"EC\":0,\"EM\":\"\"}";
			} else {
				return "{\"BM\":{},\"EC\":11272,\"EM\":\"作文上传失败\"}";
			}
		} catch (Exception e) {
			return "{\"BM\":{},\"EC\":11271,\"EM\":\"参数错误\"}";
		}
	}

	//修改上传作文，不包括图片
	@POST
	@Path("/sModifyCompositionUploadlist")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String sModifyCompositionUploadlist(String json) {
		JSONObject jsonObject = new JSONObject(json);
		JSONObject str1 = jsonObject.getJSONObject("BM");
		String token = str1.getString("token");
		String udid = str1.getString("udid");
		if (!SecurityUtil.isUserLogin(token)) {
			return Constant.PLEASE_LOGIN;
		}
		String content = str1.getString("content");
		String draft = str1.getString("draft");
		String title = str1.getString("newtitle");
		String propo = str1.getString("propo");
		String cid = str1.getString("cid");
		Composition comp = new Composition();
		comp.setContent(content);
		comp.setId(Integer.parseInt(cid));
		comp.setDraft(draft);
		comp.setNewtitle(title);
		comp.setPropo(propo);
		int i = uploadDao.updateComposi2(comp);
		if (i > 0) {
			return "{\"BM\":{\"token\":\"" + token + "\",\"udid\":\""
					+ udid + "\"},\"EC\":0,\"EM\":\"\"}";
		}
		return "{\"BM\":{},\"EC\":11272,\"EM\":\"作文修改失败\"}";
	}
	
	@POST
	@Path("/teaComPic")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String teaComPic(List<Attachment> attachments,
			@Context HttpServletRequest request)  {
		String url = null;
		String data = null;
		String imageIndex = null;
		String compId = null;
		int newwidth = 0;
		int newheight = 0;
		int oldwidth = 0;
		int oldheight = 0;
		InputStream in = null;
		for (Attachment attach : attachments) {
			DataHandler dh = attach.getDataHandler();
			if (attach.getContentType().toString().contains(("text/plain"))) {
				try {
					String type = dh.getName();
					if ("url".equals(type)) {
						url = writeToString(dh.getInputStream());
					}
					if ("data".equals(type)) {
						data = writeToString(dh.getInputStream());
					}
					if ("imageIndex".equals(type)) {
						imageIndex = writeToString(dh.getInputStream());
					}
					if ("compId".equals(type)) {
						compId = writeToString(dh.getInputStream());
					}
					if ("newwidth".equals(type)) {
						newwidth = Integer.parseInt(writeToString(dh.getInputStream()));
					}
					if ("newheight".equals(type)) {
						newheight = Integer.parseInt(writeToString(dh.getInputStream()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					in = dh.getInputStream();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String comPicUrl = null;
		//保存教师批改图片
		try {
			List<String> list = picMerge.merger2pic(in, "F:\\server\\cedu-files\\" + url);
			if (list != null && list.size() == 3) {
				comPicUrl = list.get(0);
				oldwidth = Integer.parseInt(list.get(1));
				oldheight = Integer.parseInt(list.get(2));
			}else {
				logger.info("--------教师批改图片上传失败 ，url="+url+",data="+data);
			}
		} catch (Exception e) {
			logger.info("--------教师批改图片上传失败 ，url="+url+",data="+data);
		}
		//更新数据库
		int i = uploadDao.insertComPic(data, comPicUrl, imageIndex, compId, oldwidth, oldheight, newwidth, newheight);
		return null;
	}
}