package com.util;

import com.pc.teacher.servlet.TeacherFindServlet;
import com.rest.service.TomcatListener;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类
 * 
 * @author yy
 *
 */
@Component
public class CommUtils {
	private static Logger logger = Logger.getLogger(TeacherFindServlet.class);
	// 下载或查看文件时的路径，Nginx服务的地址
	private static String downloadUrl;
	public static String imgRootPath = "F:/server/cedu-files/files/pingdianapp_img";
	public static String voiceRootPath = "F:/server/cedu-files/files/pingdianapp_voice";
	public static String defaultHead = "http://www.pingdianedu.com:8101/files/app/defaul_square_head.png";

	public static void setDownloadUrl(String downloadUrl) {
		CommUtils.downloadUrl = downloadUrl;
	}

	public static String getServerHost() {
		//小程序文件服务器地址
		//return "https://api.pingdianedu.com:443/download";
		//正式和测试服务器文件服务器地址
		//return "http://123.56.186.12:8101";
		return TomcatListener.serverHost;
	}

	/**
	 * 获得sql形式的当前时间
	 * 
	 * @return
	 */
	public static String getTimeForSql() {
		Date date = new Date();// 获得系统时间.
		String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return nowTime;
	}

	/**
	 * 将object转至String
	 * 
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	/**
	 * 将String类型的纯数字参数转换成int类型
	 * 
	 * @param param
	 * @return
	 */
	public static int parseHttpInt(String param) {
		int res = 0;
		try {
			res = Integer.parseInt(param.trim());
		} catch (Exception e) {
			logger.error("CommonUtils转换String类型数字错误：" + e.getMessage());
		}
		return res;
	}

	/**
	 * 解析文章图片名中的顺序信息
	 * 
	 * @param fileName
	 * @return
	 */
	public static int getPicOrder(String fileName) {
		return Integer.parseInt(fileName.substring(0, fileName.lastIndexOf('.')));
	}

	/**
	 * 处理传入的图片名称
	 * 
	 * @param fileName
	 * @return
	 */
	public static String processPicName(String fileName) {
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmssSSS");
		String formatStr = formatter.format(new Date());
		StringBuffer name = new StringBuffer("pd" + formatStr + "_" + CommUtils.getPicOrder(fileName));
		fileName = fileName.substring(fileName.indexOf('.'));
		name.append(fileName);
		return name.toString();
	}

	public static String processSoundName(String fileName) {
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmssSSS");
		String formatStr = formatter.format(new Date());
		StringBuffer name = new StringBuffer("pd" + formatStr + "_" + fileName);
		// fileName = fileName.substring(fileName.indexOf('.'));
		// name.append(fileName);
		return name.toString();
	}

	/**
	 * 转换字符串方法
	 * 
	 * @param target
	 * @return
	 */
	public static String forceToUTF8(String target) {
		// 定义临时Stirng参数
		String tempTarget = "";
		// 当传入参数不为空时，执行if中的代码
		if (StringUtils.isNotEmpty(target)) {
			try {
				// 转换格式
				tempTarget = new String(target.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tempTarget;
	}

	/**
	 * url转码
	 * 
	 * @param param
	 * @return
	 */
	public static String URLEncode(String param) {
		if (CommUtils.isEmptyStr(param)) {
			return param;
		}
		String result = "";
		try {
			result = URLEncoder.encode(param, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("url参数转换失败：" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * url解码 同时支持正常文本与编码后的文本
	 * 
	 * @param param
	 * @return
	 */
	public static String URLDecode(String param) {
		if (CommUtils.isEmptyStr(param)) {
			return param;
		}
		String result = "";
		try {
			result = URLDecoder.decode(CommUtils.forceToUTF8(param), "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("url参数解码失败：" + e.getMessage());
			e.printStackTrace();
		}
		return result.trim();
	}

	/**
	 * 获取图片全路径
	 * 
	 * @param path
	 * @return
	 */
	public static String generalImgUrl(String path) {
		return CommUtils.getDownloadUrl() + "/" + path;
	}

	/**
	 * 获取文件服务器地址
	 * 
	 * @return
	 */
	public static String getDownloadUrl() {
		return CommUtils.downloadUrl;
	}

	/**
	 * 返回长度为【strLength】的随机数，在前面补0
	 * 
	 * @param strLength
	 * @return
	 */
	public static String getRandomNum(int strLength) {
		Random rm = new Random();
		// 获得随机数
		double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
		// 将获得的获得随机数转化为字符串
		String fixLenthString = String.valueOf(pross);
		// 返回固定的长度的随机数
		return fixLenthString.substring(1, strLength + 1);
	}

	/**
	 * 将list中的null值全部转换为"",防止加入jsonarry中出错
	 * 
	 * @param l
	 * @return
	 */
	public static List<Map<String, Object>> processNull(List<Map<String, Object>> l) {
		if (l == null) {
			return null;
		}
		for (Map<String, Object> map : l) {
			for (String key : map.keySet()) {
				if (map.get(key) == null) {
					map.put(key, "");
				}
			}
		}
		return l;
	}

	// /**
	// * 设置学生头像全路径，头像没有则设置默认头像
	// * @return
	// */
	// public static String setStudentPortraitPic(String picUrl,String sex){
	// if(picUrl==null||"".equals(picUrl)){
	// if("女".equals(sex)){
	// picUrl =
	// HttpSessionHelper.getProjectUrl()+"/resources/images/base/girlStudent.png";
	// }else {
	// picUrl =
	// HttpSessionHelper.getProjectUrl()+"/resources/images/base/boyStudent.png";
	// }
	// }else{
	// picUrl = CommUtils.generalImgUrl(picUrl);
	// }
	// return picUrl;
	// }
	// /**
	// * 设置老师的默认头像，头像没有则设置默认头像
	// * @param picUrl
	// * @param sex
	// * @return
	// */
	// public static String setTeacherPortraitPic(String picUrl,String sex){
	// if(picUrl==null||"".equals(picUrl)){
	// if("女".equals(sex)){
	// picUrl =
	// HttpSessionHelper.getProjectUrl()+"/resources/images/base/womanTeacher.png";
	// }else {
	// picUrl =
	// HttpSessionHelper.getProjectUrl()+"/resources/images/base/manTeacher.png";
	// }
	// }else{
	// picUrl = CommUtils.generalImgUrl(picUrl);
	// }
	// return picUrl;
	// }
	/**
	 * 处理页数 默认15条 返回一个int数组，第一个为pageNow，第二个为pageSize
	 * 
	 * @param request
	 * @return
	 */
	public static int[] processPageNum(HttpServletRequest request) {
		int pageSize = 10;
		return CommUtils.processPageNum(request, pageSize);
	}

	/**
	 * 处理页数 可由程序内部设置每页条数 返回一个int数组，第一个为pageNow，第二个为pageSize
	 * 
	 * @param request
	 * @return
	 */
	public static int[] processPageNum(HttpServletRequest request, int pageSize) {
		int pageNow = 0;
		if (!CommUtils.isEmptyStr(request.getParameter("pageNow"))) {
			pageNow = Integer.parseInt(request.getParameter("pageNow").trim()) - 1;
		}
		if (!CommUtils.isEmptyStr(request.getParameter("pageSize"))) {
			pageSize = Integer.parseInt(request.getParameter("pageSize").trim());
		}
		pageNow = pageNow * pageSize;
		int[] i = new int[] { pageNow, pageSize };
		return i;
	}

	/**
	 * 判断请求中是否有空的关键参数 用于验证http请求中是否包含所必须的参数，如果有一个为空，那么就返回true，否则返回false
	 * 
	 * @param request
	 * @param params
	 *            要验证的参数数组
	 * @return
	 */
	public static boolean hasNullHttpParam(HttpServletRequest request, String[] params) {
		boolean res = false;
		for (String param : params) {
			String temp_param = request.getParameter(param);
			if (CommUtils.isEmptyStr(temp_param)) {
				logger.error("CommUtils.hasNullHttpParam-Http参数：" + param + "为空");
				res = true;
			}
		}
		return res;
	}

	/**
	 * 解析数组参数
	 * 
	 * @param params
	 * @return
	 */
	public static String[] parseByComma(String params) {
		/*
		 * 当形式为["123315","123314","123313","123312","123311"]这样的字符串时会做处理 否则直接截取
		 */
		if ("[]".equals(params)) {
			return null;
		}
		if (params.startsWith("[") && params.endsWith("]")) {
			params = params.replace("[", "");
			params = params.replace("]", "");
			params = params.replace("\"", "");
			return params.split(",");
		} else {
			return params.split(",");
		}
	}

	/**
	 * MD5加盐加密
	 * 
	 * @param psd
	 * @param salt
	 * @return
	 */
	public static String saltEncoder(String psd, String salt) {
		try {
			StringBuffer stingBuffer = new StringBuffer();
			// 1.指定加密算法
			MessageDigest digest = MessageDigest.getInstance("MD5");
			// 2.将需要加密的字符串转化成byte类型的数据，然后进行哈希过程
			byte[] bs = digest.digest((psd + salt).getBytes());
			// 3.遍历bs,让其生成32位字符串，固定写法

			// 4.拼接字符串
			for (byte b : bs) {
				int i = b & 0xff;
				String hexString = Integer.toHexString(i);
				if (hexString.length() < 2) {
					hexString = "0" + hexString;
				}
				stingBuffer.append(hexString);
			}
			return stingBuffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	/**
	 * 解析年龄
	 * 
	 * @param ageCode
	 * @return
	 */
	public static Object[] parseAge(String ageCode) {
		String age_sclae = "";
		String age_detail = "";
		if (ageCode.equals("014001")) {
			age_sclae = "014001";
		} else {
			switch (ageCode) {
			case "027001":
				age_detail = "一年级";
				age_sclae = "014002";
				break;
			case "027002":
				age_detail = "二年级";
				age_sclae = "014002";
				break;
			case "027003":
				age_detail = "三年级";
				age_sclae = "014002";
				break;
			case "027004":
				age_detail = "四年级";
				age_sclae = "014002";
				break;
			case "027005":
				age_detail = "五年级";
				age_sclae = "014002";
				break;
			case "027006":
				age_detail = "六年级";
				age_sclae = "014002";
				break;
			case "027007":
				age_detail = "小考";
				age_sclae = "014002";
				break;
			case "027011":
				age_detail = "七年级";
				age_sclae = "014003";
				break;
			case "027012":
				age_detail = "八年级";
				age_sclae = "014003";
				break;
			case "027013":
				age_detail = "九年级";
				age_sclae = "014003";
				break;
			case "027014":
				age_detail = "中考";
				age_sclae = "014003";
				break;
			case "027021":
				age_detail = "高一";
				age_sclae = "014004";
				break;
			case "027022":
				age_detail = "高二";
				age_sclae = "014004";
				break;
			case "027023":
				age_detail = "高三";
				age_sclae = "014004";
				break;
			case "027024":
				age_detail = "高考";
				age_sclae = "014004";
				break;
			}

		}
		return new Object[] { age_sclae, age_detail };
	}

	/**
	 * 获取模糊搜索文章摘要，让关键字开头
	 * 
	 * @param htmlStr
	 * @param briefLen
	 *            如果小于1， 则默认读取文章全部内容
	 * @return
	 */
	public static String getSimilarArticleBrief(String htmlStr, int briefLen, String key) {
		if (htmlStr == null || "".equals(htmlStr.trim())) {
			return "";
		}

		// 截取文章正文:对于编辑录入的文章，文章标题、作者与正文是混合在一起的，所以需要剔除正文之外的内容
		Document xmlDoc = null;
		Element rootElt = null;
		try {
			// 替换"&nbsp;"，负责解析XML时抛异常（Nested exception: 引用了实体 "nbsp", 但未声明它。）
			htmlStr = htmlStr.replace("&nbsp;", " ");

			xmlDoc = DocumentHelper.parseText(htmlStr);
			// 获取根节点
			rootElt = xmlDoc.getRootElement();

			// 删除标题
			delElementsByAttribute(rootElt, "class", "eps-title");

			// 删除作者
			delElementsByAttribute(rootElt, "class", "eps-author");

			htmlStr = xmlDoc.asXML();
		} catch (Exception e) {
			rootElt = null;
			xmlDoc = null;
			// Logger logger = LogManager.getLogger();
			// logger.error("2016061917180 文章内容提取失败："+e.getMessage());
			// logger.error("2016061917180 文章内容全文：\r\n"+htmlStr);
		}

		htmlStr = delHTMLTag(htmlStr);
		htmlStr = htmlStr.trim();
		String finallString = "...";
		int start_index = htmlStr.indexOf(key);
		int end_index = htmlStr.length();
		if ("".equals(htmlStr)) {
			return "";
		} else if (briefLen > 0 && end_index - start_index > briefLen) {
			htmlStr = htmlStr.substring(start_index, start_index + briefLen);
			finallString += htmlStr;
			finallString += "...";
		} else if (briefLen > 0 && end_index - start_index <= briefLen) {
			htmlStr = htmlStr.substring(start_index, end_index);
			finallString += htmlStr;
		}

		return finallString;
	}

	/**
	 * 为文章删除h5标签和标题
	 * 
	 * @param htmlStr
	 * @return
	 */
	public static String deleteTagForContent(String htmlStr) {
		
		if (htmlStr == null || "".equals(htmlStr.trim())) {
			return "";
		}
		
		int i1 = htmlStr.indexOf("class=\"eps-title\"");
		if (i1 < 0) {
			return htmlStr;
		}
		if (i1 > 0) {
			int i3 = htmlStr.indexOf("</span>", i1);
			int i4 = htmlStr.lastIndexOf("<span", i1);
			StringBuffer sb = new StringBuffer();
			sb.append(htmlStr.substring(0, i4));
			sb.append(htmlStr.substring(i3 + 7, htmlStr.length()));
			htmlStr = sb.toString();
		}
		
		int i2 = htmlStr.indexOf("class=\"eps-author\"");
		if (i2 < 0) {
			return htmlStr;
		}
		if (i2 > 0) {
			int i5 = htmlStr.indexOf("</span>", i2);
			int i6 = htmlStr.lastIndexOf("<span", i2);
			StringBuffer sb = new StringBuffer();
			sb.append(htmlStr.substring(0, i6));
			sb.append(htmlStr.substring(i5 + 7, htmlStr.length()));
			htmlStr = sb.toString();
		}
		return htmlStr;
		/*// 截取文章正文:对于编辑录入的文章，文章标题、作者与正文是混合在一起的，所以需要剔除正文之外的内容
		Document xmlDoc = null;
		Element rootElt = null;
		try {
			// 替换"&nbsp;"，负责解析XML时抛异常（Nested exception: 引用了实体 "nbsp", 但未声明它。）
			htmlStr = htmlStr.replace("&nbsp;", " ");

			xmlDoc = DocumentHelper.parseText(htmlStr);
			// 获取根节点
			rootElt = xmlDoc.getRootElement();

			// 删除标题
			delElementsByAttribute(rootElt, "class", "eps-title");

			// 删除作者
			delElementsByAttribute(rootElt, "class", "eps-author");

			htmlStr = xmlDoc.asXML();
		} catch (Exception e) {
			rootElt = null;
			xmlDoc = null;
			// Logger logger = LogManager.getLogger();
			// logger.error("2016061917180 文章内容提取失败："+e.getMessage());
			// logger.error("2016061917180 文章内容全文：\r\n"+htmlStr);
		}
		// htmlStr = CommUtils.delHTMLTag(htmlStr); //暂时不删除html 标签
		return htmlStr;*/
	}

	/**
	 * 处理文字文章正文
	 * 
	 * @param htmlStr
	 * @return
	 */
	public static String processComp(String htmlStr) {
		if (htmlStr == null || "".equals(htmlStr.trim())) {
			return "";
		}
		// 处理批改表中的作文标题
		if (htmlStr.startsWith("<h3")) {
			int startIndex = htmlStr.indexOf("/h3>");
			htmlStr = htmlStr.substring(startIndex + 4, htmlStr.length());
		}
		// 截取文章正文:对于编辑录入的文章，文章标题、作者与正文是混合在一起的，所以需要剔除正文之外的内容
		Document xmlDoc = null;
		Element rootElt = null;
		try {
			xmlDoc = DocumentHelper.parseText(htmlStr);
			// 获取根节点
			rootElt = xmlDoc.getRootElement();

			// 删除标题
			delElementsByAttribute(rootElt, "class", "eps-title");

			// 删除作者
			delElementsByAttribute(rootElt, "class", "eps-author");

			htmlStr = xmlDoc.asXML();
		} catch (Exception e) {
			rootElt = null;
			xmlDoc = null;
			// Logger logger = LogManager.getLogger();
			// logger.error("2016061917180 文章内容提取失败："+e.getMessage());
			// logger.error("2016061917180 文章内容全文：\r\n"+htmlStr);
		}
		return htmlStr;
	}

	// ------------------------------------------------------------------------

	public static String trimRight(String src, char c) {
		if (src == null || src.length() < 1) {
			return "";
		}

		int index = src.length() - 1;
		while (src.charAt(index) == c) {
			index--;
		}
		index++;
		if (index < src.length()) {
			src = src.substring(0, index);
		}
		return src;
	}

	public static boolean isEmptyStr(Object obj) {
		return obj == null || "".equals(obj);
	}

	// 通过用户注册类型，判断是否为机构类用户
	public static boolean isOrgUser(String registerType) {
		if (registerType == null) {
			return true;
		}

		return !registerType.equals("003001") // 系统内注册
				&& !registerType.equals("003002") // QQ注册
				&& !registerType.equals("003003") // 新浪微博注册
				&& !registerType.equals("003020"); // 微信注册

	}

	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
	private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
	private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
	private static final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符

	/**
	 * @param htmlStr
	 * @return 删除Html标签
	 */
	public static String delHTMLTag(String htmlStr) {
		if (htmlStr == null || "".equals(htmlStr.trim())) {
			return "";
		}
		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll(""); // 过滤空格回车标签

		htmlStr = htmlStr.replace("&nbsp;", " ");
		return htmlStr.trim(); // 返回文本字符串
	}

	// 取第一句话
	public static String getTextFromHtml(String htmlStr) {
		htmlStr = delHTMLTag(htmlStr);
		htmlStr = htmlStr.replaceAll(" ", "");
		htmlStr = htmlStr.substring(0, htmlStr.indexOf("。") + 1);
		return htmlStr;
	}

	// 获取文章摘要，提取前100字,文章的内容可能存储的是HTML字符串
	public static String getArticleBrief(String htmlStr) {
		return getArticleBrief(htmlStr, 100);
	}

	// 获取文章摘要，文章的内容可能存储的是HTML字符串
	public static String getArticleContent(String htmlStr) {
		return getArticleBrief(htmlStr, -1);
	}

	/**
	 * 获取文章摘要
	 * 
	 * @param htmlStr
	 * @param briefLen
	 *            如果小于1， 则默认读取文章全部内容
	 * @return
	 */
	public static String getArticleBrief(String htmlStr, int briefLen) {
		if (htmlStr == null || "".equals(htmlStr.trim())) {
			return "";
		}

		// 截取文章正文:对于编辑录入的文章，文章标题、作者与正文是混合在一起的，所以需要剔除正文之外的内容
		Document xmlDoc = null;
		Element rootElt = null;
		try {
			// 替换"&nbsp;"，负责解析XML时抛异常（Nested exception: 引用了实体 "nbsp", 但未声明它。）
			htmlStr = htmlStr.replace("&nbsp;", " ");

			xmlDoc = DocumentHelper.parseText(htmlStr);
			// 获取根节点
			rootElt = xmlDoc.getRootElement();

			// 删除标题
			delElementsByAttribute(rootElt, "class", "eps-title");

			// 删除作者
			delElementsByAttribute(rootElt, "class", "eps-author");

			htmlStr = xmlDoc.asXML();
		} catch (Exception e) {
			rootElt = null;
			xmlDoc = null;
			// Logger logger = LogManager.getLogger();
			// logger.error("2016061917180 文章内容提取失败："+e.getMessage());
			// logger.error("2016061917180 文章内容全文：\r\n"+htmlStr);
		}

		htmlStr = delHTMLTag(htmlStr);
		htmlStr = htmlStr.trim();
		if ("".equals(htmlStr)) {

			if (rootElt != null) {
				List<Element> elements = new ArrayList<Element>();
				getElementsByTagName(rootElt, "img", elements);
				if (elements.size() > 0) {
					// 如果仅有图片，则返回图片的缩略图,进取第一张图片,且限定其大小
					htmlStr = "<img src='" + elements.get(0).attributeValue("src") + "' style='max-width:300px;max-height:100px;'></img>";
				}
			}
		} else if (briefLen > 0 && htmlStr.length() > briefLen) {
			htmlStr = htmlStr.substring(0, briefLen);
			htmlStr += "...";
		}

		return htmlStr;
	}

	// 删除由属性值attrValue指定的属性attrName的所属节点
	private static void delElementsByAttribute(Element parentElt, String attrName, String attrValue) {
		if (attrValue.equalsIgnoreCase(parentElt.attributeValue(attrName))) {
			parentElt.detach();
			return;
		}

		@SuppressWarnings("unchecked")
		List<Element> eles = parentElt.elements();
		for (int i = eles.size() - 1; i > -1; i--) {
			delElementsByAttribute(eles.get(i), attrName, attrValue);
		}
	}

	private static void getElementsByTagName(Element parentElt, String tagName, List<Element> elements) {
		if (parentElt == null || "".equals(tagName)) {
			return;
		}
		if (tagName.equalsIgnoreCase(parentElt.getName())) {
			elements.add(parentElt);
			return;
		}

		@SuppressWarnings("unchecked")
		List<Element> eles = parentElt.elements();
		for (int i = eles.size() - 1; i > -1; i--) {
			getElementsByTagName(eles.get(i), tagName, elements);
		}
	}

	// 如果用户别名为空，为其设置别名-zwm--
	public static void forUserSetAlias(JdbcTemplate dao, Map<String, Object> m, String userId, String alias_name) {
		int rows = 0;
		String account_alias = "pd" + userId + CommUtils.getRandomNum(3);// 设置别名
		Object[] params = new Object[] { account_alias, userId };
		rows = dao.update("update T_BASE_USER set ACCOUNT_ALIAS = ?  WHERE ID =?", params);
		m.put(alias_name, account_alias);
	}

	public static String[] gradeCode = { "027001", "027002", "027003", "027004", "027005", "027006", "027011", "027012", "027013", "027021", "027022", "027023" };

	public static String transferCompLevelString(Object level) {
		if (level != null && !TextUtils.isEmpty(level.toString())) {
			return levelMap.get(level.toString());
		}
		return "";
	}

	public static final Map<String, String> levelMap = new HashMap<>();

	static {
		levelMap.put("一类文", "一等");
		levelMap.put("二类文", "二等");
		levelMap.put("三类文", "三等");
		levelMap.put("四类文", "四等");
		levelMap.put("五类文", "五等");
	}

	/**
	 * 要求外部订单号必须唯一。 退款的唯一退款单号
	 *
	 * @return
	 */
	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	public static String getGradeStage(String grade) {
		for (int i = 0; i < gradeCode.length; i++) {
			if (gradeCode[i].equals(grade)) {
				if (i <= 5) {
					return "小学";
				}
				if (i <= 8) {
					return "初中";
				}
				if (i <= 11) {
					return "高中";
				}
			}
		}
		return "高中";
	}

	// 小学： 一类文分值为27-30分；二类文分值为21-26分；三类文分值为18-20分；四类文分值为12-17分；五类文分值为11分以下。
	// 初中： 一类文分值为46-50分；二类文分值为40-45分；三类文分值为35-39分；四类文分值为30-34分；五类文分值为29分以下。
	// 高中：高中作文评分标准分两部分组成，第一部分基础等级，第二部分发展等级。基础等级分为内容和表达两个角度。
	// 每个角度的作文等级为：20-16分为一类文，15-11分为二类文，10-6分为三类文，5-0为四类文

	private final static String STAGE_ONE = "一类文";
	private final static String STAGE_TWO = "二类文";
	private final static String STAGE_THREE = "三类文";
	private final static String STAGE_FOUR = "四类文";
	private final static String STAGE_FIVE = "五类文";

	public static String getScoreStage(String strValue, String gradeCode) {

		int score = 0;

		if (strValue != null && !TextUtils.isEmpty(strValue)) {
			try {
				score = Integer.parseInt(strValue);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if ("小学".equals(getGradeStage(gradeCode))) {
					if (score <= 11) {
						return STAGE_FIVE;
					} else if (11 < score && score <= 17) {
						return STAGE_FOUR;
					} else if (17 < score && score <= 20) {
						return STAGE_THREE;
					} else if (20 < score && score <= 26) {
						return STAGE_TWO;
					} else {
						return STAGE_ONE;
					}
				}

				if ("初中".equals(getGradeStage(gradeCode))) {
					if (score <= 29) {
						return STAGE_FIVE;
					} else if (29 < score && score <= 34) {
						return STAGE_FOUR;
					} else if (34 < score && score <= 39) {
						return STAGE_THREE;
					} else if (39 < score && score <= 45) {
						return STAGE_TWO;
					} else {
						return STAGE_ONE;
					}
				}

				if ("高中".equals(getGradeStage(gradeCode))) {
					if (score <= 29) {
						return STAGE_FIVE;
					} else if (29 < score && score <= 35) {
						return STAGE_FOUR;
					} else if (35 < score && score <= 44) {
						return STAGE_THREE;
					} else if (44 < score && score <= 51) {
						return STAGE_TWO;
					} else {
						return STAGE_ONE;
					}
				}
			}
		}
		return STAGE_FOUR;
	}
	
	//上传作文去除xml标签
	public static String deleteXmlForComposition(String htmlStr){
		if (htmlStr == null || "".equals(htmlStr.trim())) {
			return "";
		}
		htmlStr = htmlStr.replaceAll("<root>", "");
		htmlStr = htmlStr.replaceAll("</root>", "");
		
		int i1 = htmlStr.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		if (i1 < 0) {
			return htmlStr;
		}
		String newHtmlStr = htmlStr.substring(38, htmlStr.length());
		
		return newHtmlStr;
		
	}
	//上传作文去除空格
	public static String deleteKongForGecomposition(String htmlStr){
		if (htmlStr == null || "".equals(htmlStr.trim())) {
			return "";
		}
		htmlStr = htmlStr.replaceAll("&nbsp;", "");
		
		return htmlStr;
		
	}
	//读取远程文件，转为base64字符串
	public static String fileToBase64(String filePath) {
        String base64 = null;
        InputStream in = null;
        try {
        	in = new FileInputStream(filePath);
        	/*URL url = new URL(filePath);
        	HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
    		urlCon.connect();  
    		in = urlCon.getInputStream();*/
    		/*InputStream in = url.openStream();*/
    		byte[] bytes = new byte[in.available()];
    		in.read(bytes);
    		base64 = Base64.encode(bytes);
        } catch (Exception e) {
            logger.info("--------------音频转码base64字符串失败----------------");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return base64;
    }
	
	//long时间值转为字符串
	public static String longTimeToString(long time){
		long time1 = time / 3600;
		long l1 = time - time1 * 3600;
		long time2 = l1 / 60;
		long time3 = l1 - time2 * 60;
		String t1 = (time1 + "").length() == 2 ? (time1 + "") : "0" + (time1 + "");
		String t2 = (time2 + "").length() == 2 ? (time2 + "") : "0" + (time2 + "");
		String t3 = (time3 + "").length() == 2 ? (time3 + "") : "0" + (time3 + "");
		return t1 + ":" + t2 + ":" + t3;
	}
	
	//字符串转为long时间值
	public static long stringTimeToLong(String str){
		if ("".equals(str)) {
			return 0;
		}
		String[] sts = str.split(":");
		long base = 0;
		long result = 0;
		for (int i = 0; i < sts.length; i++) {
			if (i == 0) {
				base = 3600;
			}
			if (i == 1) {
				base = 60;
			}
			if (i == 2) {
				base = 1;
			}
			result += Long.parseLong(sts[i]) * base;
		}
		return result;
	}
	
	//计算平均时长
	public static long getAvgBetween2Long(long l1, long l2){
		if (l1 == 0) {
			return l2;
		}
		return (l1 + l2)/2;
	}

	//处理信息推送 
	public static String handleTuiSongJson(JSONObject jo1, JSONObject jo2, int i, int j) {
		StringBuffer sb = new StringBuffer();
		if (jo1 != null) {
			String ret_code = jo1.get("ret_code").toString();
			if ("-1".equals(ret_code)) {
				sb.append("，安卓成功0个，失败"+i+"个");
			}else {
				String temp = jo1.get("result").toString();
				String result = temp.substring(1, temp.length() - 1);
				String[] sts = result.split(",");
				int scount = 0;
				int fcount = 0;
				for (String string : sts) {
					if ("0".equals(string)) {
						scount += 1;
					}else {
						fcount += 1;
					}
				}
				sb.append("，安卓成功"+scount+"个，失败"+fcount+"个");
			}
		}
		if (jo2 != null) {
			
			String ret_code = jo2.get("ret_code").toString();
			if ("-1".equals(ret_code)) {
				sb.append("，IOS成功0个，失败"+j+"个");
			}else {
				String temp = jo2.get("result").toString();
				String result = temp.substring(1, temp.length() - 1);
				String[] sts = result.split(",");
				int scount = 0;
				int fcount = 0;
				for (String string : sts) {
					if ("0".equals(string)) {
						scount += 1;
					}else {
						fcount += 1;
					}
				}
				sb.append("，IOS成功"+scount+"个，失败"+fcount+"个");
			}
		}
		return sb.toString();
	}

	//打印群推送结果信息 
	public static String getTuiSongResult(List<String> list, JSONObject jo) {
		if (!"0".equals(jo.get("ret_code").toString())) {
			return "获取详细信息失败";
		}
		try {
			StringBuffer sb = new StringBuffer();
			String result = jo.get("result").toString();
			String[] sts = result.split(",");
			for (int i = 0; i < list.size(); i++) {
				sb.append("{udid=" + list.get(i));
				sb.append(",result=" + sts[i] + "},");
			}
			return sb.toString();
		} catch (Exception e) {
			return "出现异常";
		}
	}

	//计算两个字符串的时间值之间的天数
	public static String getDayBetween2Time(String start, String end) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date startDate = df.parse(start);
			Date endDate = df.parse(end);
			long l = (endDate.getTime() - startDate.getTime()) / (1000 * 24 * 60 * 60);
			return l + "";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	//微信支付使用
	public static String wxPayDoubleMultiply100(String str){
		BigDecimal b1 = new BigDecimal(str);
		BigDecimal b2 = new BigDecimal(100);
		int i = b1.multiply(b2).intValue();
		return i + "";
	}
	//微信支付使用
	public static String wxPayDoubleDivide100(String str){
		BigDecimal b1 = new BigDecimal(str);
		BigDecimal b2 = new BigDecimal(100);
		double d = b1.divide(b2).doubleValue();
		return d + "";
	}
	//根据年级分数判断几类文
	public static String getCompositionLeveByScoreAndGrade(String score, String grade){
		int igrade = 0;
		int iscore = 0;
		try {
			igrade = Integer.parseInt(grade);
			iscore = Integer.parseInt(score);
		} catch (Exception e) {
			return "无";
		}
		if (igrade < 27007) {//小学
			if (iscore <= 11) {
				return "五类文";
			}else if (iscore <= 17 && iscore >= 12) {
				return "四类文";
			}else if (iscore <= 20 && iscore >= 18) {
				return "三类文";
			}else if (iscore <= 26 && iscore >= 21) {
				return "二类文";
			}else {
				return "一类文";
			}
		}else if (igrade < 27014 && igrade > 27006) {//初中
			if (iscore <= 29) {
				return "五类文";
			}else if (iscore <= 34 && iscore >= 30) {
				return "四类文";
			}else if (iscore <= 39 && iscore >= 35) {
				return "三类文";
			}else if (iscore <= 45 && iscore >= 40) {
				return "二类文";
			}else {
				return "一类文";
			}
		}else {
			if (iscore <= 29) {//高中
				return "五类文";
			}else if (iscore <= 35 && iscore >= 30) {
				return "四类文";
			}else if (iscore <= 44 && iscore >= 36) {
				return "三类文";
			}else if (iscore <= 51 && iscore >= 45) {
				return "二类文";
			}else {
				return "一类文";
			}
		}
	}
	//判断老师点评内容是否完整
	public static boolean hasCompleteByCom_composition(String geade, String score, String pcontent, String planguage,
			String pstructure, String pwriting, String scoring, String points, String suggest) {
		int igrade = Integer.parseInt(geade);
		if (igrade < 27007) {//小学
			if (score==null||"".equals(score)||pcontent==null||"".equals(pcontent)||planguage==null||"".equals(planguage)||
					pwriting==null||"".equals(pwriting)||scoring==null||"".equals(scoring)||
					points==null||"".equals(points)||suggest==null||"".equals(suggest))	{
				return false;
			}
		}else if (igrade < 27014 && igrade > 27006) {//初中
			if (score==null||"".equals(score)||pcontent==null||"".equals(pcontent)||planguage==null||"".equals(planguage)||
			pwriting==null||"".equals(pwriting)||scoring==null||"".equals(scoring)|| pstructure==null||"".equals(pstructure)||
			points==null||"".equals(points)||suggest==null||"".equals(suggest))	{
				return false;
			}
		}else {//高中
			if (score==null||"".equals(score)||pcontent==null||"".equals(pcontent)||planguage==null||"".equals(planguage)||
			scoring==null||"".equals(scoring)|| pstructure==null||"".equals(pstructure)||
			points==null||"".equals(points)||suggest==null||"".equals(suggest))	{
				return false;
			}
		}
		return true;
	}
	
	//时间转化
	public static String ObjectTime2String(Object o){
		if (o == null || "".equals(o.toString())) {
			return "";
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			java.sql.Timestamp cr = (java.sql.Timestamp)o;
			return sdf.format(cr);
		}
	}
	
	//用户头像
	public static String userHead(Object o){ 
		if (o == null || o.toString().equals("")) {
			return CommUtils.defaultHead;
		}else {
			if (o.toString().startsWith("http")) {
				return o.toString();
			}else {
				return CommUtils.getServerHost() + o.toString();
			}
		}
	}
	//判断url类型
	public static String judgeUrl(Object o){
		if (o == null || o.toString().equals("")) {
			return "";
		}else {
			if (o.toString().startsWith("http")) {
				return o.toString();
			}else {
				return CommUtils.getServerHost() + o.toString();
			}
		}
	}
	public static String judgeUrl(Object o, String str){
		if (o == null || o.toString().equals("")) {
			return str;
		}else {
			if (o.toString().startsWith("http")) {
				return o.toString();
			}else {
				return CommUtils.getServerHost() + o.toString();
			}
		}
	}
	//判断查询内容是否为空
	public static String judgeSqlInformation(Object o){
		if (o == null || o.toString().equals("")) {
			return "";
		}else {
			return o.toString();
		}
	}
	public static String judgeSqlInformation(Object o, String str){
		if (o == null || o.toString().equals("")) {
			return str;
		}else {
			return o.toString();
		}
	}
	/**
	 * 分享作文使用
	 */
	//更改所有作文正文内容的字体大小
	public static String shareConvertComposition(Object o){
		if (o == null || o.toString().equals("")) {
			return "";
		} 
		String str = shareReplaceAllNeed(o.toString());
		return shareSetFontWithNoTag(str, 0);
	}
	private static String shareReplaceAllNeed(String str){
		StringBuffer sb = new StringBuffer(str);
		int i = sb.indexOf("px");
		int j = 0;
		if (i != -1) {
			j = sb.indexOf(":", i - 10);
			if (j < i) {
				sb.replace(j + 1, i + 2, "1rem");
			}
			return shareReplaceAllNeed(sb.toString());
		}else {
			return sb.toString();
		}
	}
	//点评正文标签没有字体的设置字体
	public static String shareSetFontWithNoTag(String str, int start){
		StringBuffer sb = new StringBuffer(str);
		int i = sb.indexOf("style=\"",start);
		int j = 0;
		if (i != -1) {
			j = sb.indexOf("\"",i + 8);
			if (j > i) {
				String temp = sb.substring(i, j);
				if (!temp.contains("font-size:")) {
					StringBuffer sb1 = new StringBuffer();
					sb1.append(temp.substring(0, 6));
					sb1.append("\"font-size: 1rem; ");
					sb1.append(temp.substring(7, temp.length()));
					sb.replace(i, j, sb1.toString());
				}
			}
			return shareSetFontWithNoTag(sb.toString(),i + 8);
		}else {
			return sb.toString();
		}
	}
	/**
	 * 
	 */
	
	//处理留言集合
	public static List<List<Map<String, String>>> arrangeAppraiseMessage(List<Map<String,Object>> messageList){
		List<List<Map<String, String>>> returnList = new ArrayList<>();
		for (Map<String, Object> m1 : messageList) {
			if (m1.get("FATHER_ID") == null) {
				String fatherId = m1.get("ID").toString();
				List<Map<String, String>> list = new ArrayList<>();
				Map<String, String> map1 = new HashMap<>();
				map1.put("message", CommUtils.judgeSqlInformation(m1.get("MESSAGE")));
				map1.put("time", CommUtils.ObjectTime2String(m1.get("TIME")));
				map1.put("userType", "stu");
				map1.put("aid", CommUtils.judgeSqlInformation(m1.get("ID")));
				list.add(map1);
				for (Map<String, Object> m2 : messageList) {
					if (m2.get("FATHER_ID") != null && m2.get("FATHER_ID").toString().equals(fatherId)) {
						Map<String, String> map2 = new HashMap<>();
						map2.put("message", CommUtils.judgeSqlInformation(m2.get("MESSAGE")));
						map2.put("time", CommUtils.ObjectTime2String(m2.get("TIME")));
						map2.put("userType", CommUtils.judgeSqlInformation(m2.get("USER_TYPE")));
						map2.put("aid", CommUtils.judgeSqlInformation(m2.get("ID")));
						list.add(map2);
					}
				}
				returnList.add(list);
			}
		}
		return returnList;
	}
	@Autowired
	private static  JdbcTemplate jdbcTemplate;

	//后台获取jdbc
	public static JdbcTemplate getJdbcTemplate(){
		// 创建jdbc对象
		//ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		//JdbcTemplate jt = (JdbcTemplate) ctx.getBean("jdbcTemplate");
//		return TomcatListener.jt;
		// springboot 已经兼容了 @Autowired JdbcTemplate jt直接使用即可
		return jdbcTemplate;
	}
	//读取本地图片转为base64字符串
	public static String image2Base64(String path){
		InputStream in = null;  
        byte[] data = null;  
        // 读取图片字节数组  
        try {  
            in = new FileInputStream("F:\\server\\cedu-files" + path);  
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        // 对字节数组Base64编码  
        BASE64Encoder encoder = new BASE64Encoder();  
        // 返回Base64编码过的字节数组字符串  
        return encoder.encode(data); 
	}
	
	// 手机号正则验证
	public static boolean isChinaPhoneLegal(String str) {
		String regExp = "^((1[3-9]))\\d{9}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		return m.matches();
	}
	
	//事件判断 ***天前。。
	public static String formatDate(Date date) {
		long delta = new Date().getTime() - date.getTime();
		if (delta < 1L * 60000L) {
			long seconds = delta / 1000L;;
			return (seconds <= 0 ? 1 : seconds) + "秒前";
		}
		if (delta < 45L * 60000L) {
			long minutes = delta / 1000L / 60L;
			return (minutes <= 0 ? 1 : minutes) + "分钟前";
		}
		if (delta < 24L * 3600000L) {
			long hours = delta / 1000L / 60L / 60L;
			return (hours <= 0 ? 1 : hours) + "小时前";
		}
		if (delta < 48L * 3600000L) {
			return "1天前";
		}
		if (delta < 30L * 86400000L) {
			long days = delta / 1000L / 60L / 60L / 24L;
			return (days <= 0 ? 1 : days) + "天前";
		}
		if (delta < 12L * 4L * 604800000L) {
			long months = delta / 1000L / 60L / 60L / 24L / 30L;
			return (months <= 0 ? 1 : months) + "月前";
		} else {
			long years = delta / 1000L / 60L / 60L / 24L / 30L / 365L;
			return (years <= 0 ? 1 : years) + "年前";
		}
	}
	
	//教师端批改作文，作文正文内容回显问题，<替换为《
	public static String teaComReplace(String str){
		return str.replace("<", "《").replace(">", "》");
	}
	
	//
	public static Map<String, Object> authentication(List<Map<String, Object>> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		String cause1 = "1、未完善个人信息";
		String cause2 = "2、未提交师资认证";
		String cause3 = "3、师资审核认证通过，去试点评";
		String cause4 = "4、试点评未通过";
		String perfectTime = CommUtils.judgeSqlInformation(list.get(0).get("PERFECT_TIME"));//完善个人信息时间
		String uploadTime = CommUtils.judgeSqlInformation(list.get(0).get("UPLOAD_TIME"));//上传师资认证
		String austate = CommUtils.judgeSqlInformation(list.get(0).get("AUSTATE"));//师资认证审核状态
		String cause = CommUtils.judgeSqlInformation(list.get(0).get("CAUSE"));//不通过原因
		map.put("austate", austate);
		if(perfectTime.equals("")){//未完善个人信息时间
			map.put("austate", "4");
			map.put("cause", cause1);
		}else if(uploadTime.equals("")){//未上传师资认证
			map.put("austate", "4");
			map.put("cause", cause2);
		}else if(austate.equals("2")){//师资认证未通过
			map.put("cause", cause);
		}else if(austate.equals("1")){//师资认证审核通过
			String expectedState = CommUtils.judgeSqlInformation(list.get(0).get("estate"));
			if(expectedState.equals("")){//未提交预点评
				map.put("austate", "4");
				map.put("cause", cause3);
			}else if(expectedState.equals("0")){//预点评审核中
				map.put("austate", "0");
				map.put("cause", "");
			}else if(expectedState.equals("2")){//预点评未通过
				map.put("austate", "2");
				map.put("cause", cause4);
			}else if(expectedState.equals("1")){//预点评已通过
				map.put("austate", "1");
				map.put("cause", "");
			}
		}
		return map;
	}
	
	//老师登录审核状态判断
	public static Map<String, Object> authentication2(List<Map<String, Object>> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		String cause1 = "未提交师资认证";
		String cause2 = "未提交试点评";
		String cause4 = "试点评未通过";
		String cause5 = "师资审核认证通过，未试点评";
//		String perfectTime = CommUtils.judgeSqlInformation(list.get(0).get("PERFECT_TIME"));//完善个人信息时间
//		String uploadTime = CommUtils.judgeSqlInformation(list.get(0).get("UPLOAD_TIME"));//上传师资认证
		String austate = CommUtils.judgeSqlInformation(list.get(0).get("AUSTATE"));//师资认证审核状态
		String cause = CommUtils.judgeSqlInformation(list.get(0).get("CAUSE"));//不通过原因
		String expectedState = CommUtils.judgeSqlInformation(list.get(0).get("estate"));//预点评作文状态
		map.put("austate", austate);
		map.put("reason", "");
		if(austate.equals("4")){//未上传师资认证
			map.put("cause", cause1);
		}else if(austate.equals("0")){//师资认证审核中
			if(expectedState.equals("")){//未提交预点评
				map.put("cause", cause2);
			}else if(expectedState.equals("0")){//预点评审核中
				map.put("cause", "");
			}else if(expectedState.equals("1")){//预点评已通过
				map.put("cause", "");
			}else if(expectedState.equals("2")){//预点评未通过
				map.put("austate", "2");
				map.put("cause", cause4);
				map.put("reason", "2");
			}
		}else if(austate.equals("1")){//师资认证审核通过
			if(expectedState.equals("")){//未提交预点评
				map.put("austate", "5");
				map.put("cause", cause5);
			}else if(expectedState.equals("0")){//预点评审核中
				map.put("austate", "0");
				map.put("cause", "");
			}else if(expectedState.equals("1")){//预点评已通过
				map.put("cause", "");
			}else if(expectedState.equals("2")){//预点评未通过
				map.put("austate", "2");
				map.put("cause", cause4);
				map.put("reason", "2");
			}
		}else if(austate.equals("2")){//师资认证未通过
			map.put("cause", cause);
			map.put("reason", "1");
		}
		return map;
	}
}
