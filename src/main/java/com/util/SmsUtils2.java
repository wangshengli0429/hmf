package com.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.submail.utils.RequestEncoder;

import net.sf.json.JSONObject;
/**
 * 赛邮。云通信
 * 
 * @author think
 *
 */
public class SmsUtils2 {
	
	private static Logger logger = Logger.getLogger(SmsUtils2.class);
	
	public static final String appid = "20716";
	public static final String appkey = "622f6165de18314802aceab8203b8b0e";
	public static final String time = "30分钟";
	
	/**
	 * 时间戳接口配置
	 */
	public static final String TIMESTAMP = "https://api.mysubmail.com/service/timestamp";
	/**
	 * 备用接口 http://api.mysubmail.com/service/timestamp
	 * https://api.submail.cn/service/timestamp
	 * http://api.submail.cn/service/timestamp
	 */

	public static final String TYPE_MD5 = "md5";
	public static final String TYPE_SHA1 = "sha1";
	/**
	 * API 请求接口配置
	 */
	private static final String URL = "https://api.mysubmail.com/message/send";
	private static final String multi_URL="https://api.mysubmail.com/message/multixsend";

	/**
	 * 备用接口
	 * @param context 
	 * 
	 * @param args
	 *            http://api.mysubmail.com/message/send
	 *            https://api.submail.cn/message/send
	 *            http://api.submail.cn/message/send
	 */
	public static String sendMessage(String phone, ServletContext context) {
		TreeMap<String, Object> requestData = new TreeMap<String, Object>();
		/**
		 * --------------------------------参数配置------------------------------------
		 * 请仔细阅读参数配置说明
		 * 
		 * 配置参数包括 appid, appkey ,to , signtype, 	content
		 * 用户参数设置，其中 appid, appkey, content, to 为必须参数
		 * 请访问 submail 官网创建并获取 appid 和 appkey，链接：https://www.mysubmail.com/chs/sms/apps
		 * 请访问 submail 官网创建获取短信模板内容，链接：https://www.mysubmail.com/chs/sms/templates
		 * content 参数
		 *   |正文中必须提交有效的短信签名，且您的短信签名必须放在短信的最前端，e.g. 【SUBMAIL】您的短信验证码：4438，请在10分钟内输入。
         *   |content 参数将会与您账户中的短信模板进行匹配，如 API 返回 420 错误，请在您的账户中添加短信模板，并提交审核
         *   |请将 短信正文控制在 350 个字符以内
		 * signtype 为可选参数: normal | md5 | sha1
		 * 当 signtype 参数为空时，默认为 normal
		 * --------------------------------------------------------------------------
		 */
		String code = getCode();
		
		//存储code到ServletContext中
		context.setAttribute("validateCode" + phone, code);
		
		//storageCode(phone, code);
		String content = "【一堂作文课】您的验证码是："+code+"，请在"+time+"内输入！";
		String signtype = "";

		/**
		 * 签名验证方式 详细说明可参考 SUBMAIL 官网，开发文档 → 开始 → API 授权与验证机制
		 */
		requestData.put("appid", appid);
		requestData.put("content", content);
		requestData.put("to", phone);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		@SuppressWarnings("deprecation")
		ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
		for (Map.Entry<String, Object> entry : requestData.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof String) {
				builder.addTextBody(key, String.valueOf(value), contentType);
			}
		}
		if (signtype.equals(TYPE_MD5) || signtype.equals(TYPE_SHA1)) {
			String timestamp = getTimestamp();
			requestData.put("timestamp", timestamp);
			requestData.put("sign_type", signtype);
			String signStr = appid + appkey + RequestEncoder.formatRequest(requestData) + appid + appkey;
			System.out.println(signStr);
			builder.addTextBody("timestamp", timestamp);
			builder.addTextBody("sign_type", signtype);
			builder.addTextBody("signature", RequestEncoder.encode(signtype, signStr), contentType);
		} else {
			builder.addTextBody("signature", appkey, contentType);
		}
		/**
		 * http post 请求接口 成功返回 status: success,其中 fee 参数为短信费用 ，credits 参数为剩余短信余额
		 * 详细的 API 错误日志请访问 SUBMAIL 官网 → 开发文档 → DEBUG → API 错误代码
		 */
		HttpPost httpPost = new HttpPost(URL);
		httpPost.addHeader("charset", "UTF-8");
		httpPost.setEntity(builder.build());
		try {
			CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
			HttpResponse response = closeableHttpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				String jsonStr = EntityUtils.toString(httpEntity, "UTF-8");
				logger.info("------------发送验证吗" + jsonStr);
				Map map = (Map) JSON.parse(jsonStr);
				Object object = map.get("status");
				return object + "";
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//h5投稿使用
	public static String sendMessage(String phone) {
		TreeMap<String, Object> requestData = new TreeMap<String, Object>();
		/**
		 * --------------------------------参数配置------------------------------------
		 * 请仔细阅读参数配置说明
		 * 
		 * 配置参数包括 appid, appkey ,to , signtype, 	content
		 * 用户参数设置，其中 appid, appkey, content, to 为必须参数
		 * 请访问 submail 官网创建并获取 appid 和 appkey，链接：https://www.mysubmail.com/chs/sms/apps
		 * 请访问 submail 官网创建获取短信模板内容，链接：https://www.mysubmail.com/chs/sms/templates
		 * content 参数
		 *   |正文中必须提交有效的短信签名，且您的短信签名必须放在短信的最前端，e.g. 【SUBMAIL】您的短信验证码：4438，请在10分钟内输入。
         *   |content 参数将会与您账户中的短信模板进行匹配，如 API 返回 420 错误，请在您的账户中添加短信模板，并提交审核
         *   |请将 短信正文控制在 350 个字符以内
		 * signtype 为可选参数: normal | md5 | sha1
		 * 当 signtype 参数为空时，默认为 normal
		 * --------------------------------------------------------------------------
		 */
		String code = getCode();
		
		//storageCode(phone, code);
		String content = "【一堂作文课】您的验证码是："+code+"，请在"+time+"内输入！";
		String signtype = "";

		/**
		 * 签名验证方式 详细说明可参考 SUBMAIL 官网，开发文档 → 开始 → API 授权与验证机制
		 */
		requestData.put("appid", appid);
		requestData.put("content", content);
		requestData.put("to", phone);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		@SuppressWarnings("deprecation")
		ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
		for (Map.Entry<String, Object> entry : requestData.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof String) {
				builder.addTextBody(key, String.valueOf(value), contentType);
			}
		}
		if (signtype.equals(TYPE_MD5) || signtype.equals(TYPE_SHA1)) {
			String timestamp = getTimestamp();
			requestData.put("timestamp", timestamp);
			requestData.put("sign_type", signtype);
			String signStr = appid + appkey + RequestEncoder.formatRequest(requestData) + appid + appkey;
			System.out.println(signStr);
			builder.addTextBody("timestamp", timestamp);
			builder.addTextBody("sign_type", signtype);
			builder.addTextBody("signature", RequestEncoder.encode(signtype, signStr), contentType);
		} else {
			builder.addTextBody("signature", appkey, contentType);
		}
		/**
		 * http post 请求接口 成功返回 status: success,其中 fee 参数为短信费用 ，credits 参数为剩余短信余额
		 * 详细的 API 错误日志请访问 SUBMAIL 官网 → 开发文档 → DEBUG → API 错误代码
		 */
		HttpPost httpPost = new HttpPost(URL);
		httpPost.addHeader("charset", "UTF-8");
		httpPost.setEntity(builder.build());
		try {
			CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
			HttpResponse response = closeableHttpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				String jsonStr = EntityUtils.toString(httpEntity, "UTF-8");
				logger.info("------------发送验证吗" + jsonStr);
				Map map = (Map) JSON.parse(jsonStr);
				Object object = map.get("status");
				if (object != null && "success".equals(object.toString())) {
					return code;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//后台群发
	//下面是消息内容
	//【一堂作文课】您已注册一堂作文课教师端，请打开APP①点击我的-头像-基本信息，将所有信息补全，包括头像②点击我的-师资认证，上传证件信息
	public static String sendMultiMessage1(List<String> teachers) {
		TreeMap<String, Object> requestData = new TreeMap<String, Object>();
		JSONObject toJSON = new JSONObject();
		JSONObject varsJSON = new JSONObject();
		ArrayList<String> multi = new ArrayList<String>();
		/**
		 * --------------------------------参数配置------------------------------------
		 * 请仔细阅读参数配置说明
		 * 
		 * 配置参数包括 appid, appkey ,project, signtype, 	multi
		 * appid, appkey, multi, project 为必须参数
		 * 请访问 submail 官网创建并获取 appid 和 appkey，链接：https://www.mysubmail.com/chs/sms/apps
		 * 请访问 submail 官网创建获取项目标识 project_id，链接：https://www.mysubmail.com/chs/sms/templates
		 * 如何获取项目标识 project 参数，链接：https://www.mysubmail.com/chs/documents/developer/MmSw12
		 * multi参数
		 * 收件人 to 联系人参数和 vars 文本变量的整合模式，请将 to 和 vars 整合成 json 字符串格式提交（数据模型请参考DEMO中的multi参数示例）
		 *   请注意：multi 参数要求严格的 JSON 格式，以下是将参数转换为 JSON 格式的注意事项
		 *   |json 字符串必须以双引号包含
		 *   |json 字符串必须是 utf8 编码
		 *   |不能有多余的逗号 如：[1,2,]
		 *   |json 字符串首尾必须被大括号{}包含 
		 * signtype 为可选参数: normal | md5 | sha1
		 * 当 signtype 参数为空时，默认为 normal
		 *  --------------------------------------------------------------------------
		 */
		
		//String content = "【一堂作文课】"+teaName+"老师您好，您已注册一堂作文课，请打开一堂作文课教师端APP完成下面操作： 1、点击我的--头像--基本信息，将所有信息填写完整，包括头像； 2、点击我的--师资认证，上传相关证件信息。";
		
		String project = "UGbTQ2";
		String signtype = "";
		//示例：添加一组数据
		
		for (String teaPhone : teachers) {
			toJSON.put("to", teaPhone);
			multi.add(toJSON.toString());	    
			varsJSON.clear();
			toJSON.clear();
		}
		
		/**
		 *  ---------------------------------------------------------------------------
		 */
		
		/**
		 *  签名验证方式
		 *  详细说明可参考 SUBMAIL 官网，开发文档 → 开始 → API 授权与验证机制
		 */
		requestData.put("appid", appid);
		requestData.put("project", project);
		if(!multi.isEmpty()){
			requestData.put("multi",multi.toString());
		}
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		@SuppressWarnings("deprecation")
		ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8);
		for(Map.Entry<String, Object> entry: requestData.entrySet()){
			String key = entry.getKey();
			Object value = entry.getValue();
			if(value instanceof String){
				builder.addTextBody(key, String.valueOf(value),contentType);
			}
		}	
		if(signtype.equals(TYPE_MD5) || signtype.equals(TYPE_SHA1)){
			String timestamp = getTimestamp();
			requestData.put("timestamp", timestamp);
			requestData.put("sign_type", signtype);
			String signStr = appid + appkey + RequestEncoder.formatRequest(requestData) + appid + appkey;
			
			builder.addTextBody("timestamp", timestamp);
			builder.addTextBody("sign_type", signtype);
			builder.addTextBody("signature", RequestEncoder.encode(signtype, signStr), contentType);
		}else{
			builder.addTextBody("signature", appkey, contentType);
		}
		/**
		 * http post 请求接口
		 * 成功返回 status: success,其中 fee 参数为短信费用 ，credits 参数为剩余短信余额
		 * 详细的 API 错误日志请访问 SUBMAIL 官网 → 开发文档 → DEBUG → API 错误代码
		 */
		HttpPost httpPost = new HttpPost(multi_URL);
		httpPost.addHeader("charset", "UTF-8");
		httpPost.setEntity(builder.build());
		try{
			CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
			HttpResponse response = closeableHttpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				String jsonStr = EntityUtils.toString(httpEntity, "UTF-8");
				logger.info("------------老师注册，发送提示短信" + jsonStr);
				Map map = (Map) JSON.parse(jsonStr.substring(1, jsonStr.length() - 1));
				Object object = map.get("status");
				if (object != null && "success".equals(object.toString())) {
					return "sucsess";
				}
			}
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return "fail";
	}
	
	//后台群发
	//下面是消息内容
	//【一堂作文课】您已完善信息，请用电脑进行预点评，https://mobile.pingdianedu.com:8443/pingdian
	public static String sendMultiMessage2(List<String> teachers) {
		TreeMap<String, Object> requestData = new TreeMap<String, Object>();
		JSONObject toJSON = new JSONObject();
		JSONObject varsJSON = new JSONObject();
		ArrayList<String> multi = new ArrayList<String>();
		/**
		 * --------------------------------参数配置------------------------------------
		 * 请仔细阅读参数配置说明
		 * 
		 * 配置参数包括 appid, appkey ,project, signtype, 	multi
		 * appid, appkey, multi, project 为必须参数
		 * 请访问 submail 官网创建并获取 appid 和 appkey，链接：https://www.mysubmail.com/chs/sms/apps
		 * 请访问 submail 官网创建获取项目标识 project_id，链接：https://www.mysubmail.com/chs/sms/templates
		 * 如何获取项目标识 project 参数，链接：https://www.mysubmail.com/chs/documents/developer/MmSw12
		 * multi参数
		 * 收件人 to 联系人参数和 vars 文本变量的整合模式，请将 to 和 vars 整合成 json 字符串格式提交（数据模型请参考DEMO中的multi参数示例）
		 *   请注意：multi 参数要求严格的 JSON 格式，以下是将参数转换为 JSON 格式的注意事项
		 *   |json 字符串必须以双引号包含
		 *   |json 字符串必须是 utf8 编码
		 *   |不能有多余的逗号 如：[1,2,]
		 *   |json 字符串首尾必须被大括号{}包含 
		 * signtype 为可选参数: normal | md5 | sha1
		 * 当 signtype 参数为空时，默认为 normal
		 *  --------------------------------------------------------------------------
		 */
		
		
		String project = "TZnEh1";
		String signtype = "";
		//示例：添加一组数据
		
		for (String teaPhone : teachers) {
			toJSON.put("to", teaPhone);
			multi.add(toJSON.toString());	    
			varsJSON.clear();
			toJSON.clear();
		}
		
		/**
		 *  ---------------------------------------------------------------------------
		 */
		
		/**
		 *  签名验证方式
		 *  详细说明可参考 SUBMAIL 官网，开发文档 → 开始 → API 授权与验证机制
		 */
		requestData.put("appid", appid);
		requestData.put("project", project);
		if(!multi.isEmpty()){
			requestData.put("multi",multi.toString());
		}
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		@SuppressWarnings("deprecation")
		ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8);
		for(Map.Entry<String, Object> entry: requestData.entrySet()){
			String key = entry.getKey();
			Object value = entry.getValue();
			if(value instanceof String){
				builder.addTextBody(key, String.valueOf(value),contentType);
			}
		}	
		if(signtype.equals(TYPE_MD5) || signtype.equals(TYPE_SHA1)){
			String timestamp = getTimestamp();
			requestData.put("timestamp", timestamp);
			requestData.put("sign_type", signtype);
			String signStr = appid + appkey + RequestEncoder.formatRequest(requestData) + appid + appkey;
			
			builder.addTextBody("timestamp", timestamp);
			builder.addTextBody("sign_type", signtype);
			builder.addTextBody("signature", RequestEncoder.encode(signtype, signStr), contentType);
		}else{
			builder.addTextBody("signature", appkey, contentType);
		}
		/**
		 * http post 请求接口
		 * 成功返回 status: success,其中 fee 参数为短信费用 ，credits 参数为剩余短信余额
		 * 详细的 API 错误日志请访问 SUBMAIL 官网 → 开发文档 → DEBUG → API 错误代码
		 */
		HttpPost httpPost = new HttpPost(multi_URL);
		httpPost.addHeader("charset", "UTF-8");
		httpPost.setEntity(builder.build());
		try{
			CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
			HttpResponse response = closeableHttpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				String jsonStr = EntityUtils.toString(httpEntity, "UTF-8");
				logger.info("------------老师注册，发送提示短信" + jsonStr);
				Map map = (Map) JSON.parse(jsonStr.substring(1, jsonStr.length() - 1));
				Object object = map.get("status");
				if (object != null && "success".equals(object.toString())) {
					return "sucsess";
				}
			}
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return "fail";
	}
	

	// 获取时间戳
	private static String getTimestamp() {
		CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
		HttpGet httpget = new HttpGet(TIMESTAMP);
		try {
			HttpResponse response = closeableHttpClient.execute(httpget);
			HttpEntity httpEntity = response.getEntity();
			String jsonStr = EntityUtils.toString(httpEntity, "UTF-8");
			if (jsonStr != null) {
				JSONObject json = JSONObject.fromObject(jsonStr);
				return json.getString("timestamp");
			}
			closeableHttpClient.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	//随机数字
	private static String getCode(){
		int x;//定义两变量
        Random ne=new Random();//实例化一个random的对象ne
        x=ne.nextInt(9999-1000+1)+1000;//为变量赋随机值1000-9999
        return x + "";
	}
	
}
