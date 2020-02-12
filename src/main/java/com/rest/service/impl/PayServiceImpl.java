package com.rest.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.rest.service.PayService;
import com.rest.service.dao.OrderDao;
import com.util.CommUtils;
import com.util.Constant;
import com.util.SecurityUtil;
import com.util.WxConstant;
import com.util.wxPayUtils.WXPayConstants;
import com.util.wxPayUtils.WXPayUtil;
import org.springframework.stereotype.Component;

@Component
public class PayServiceImpl implements PayService {

	private static Logger logger = Logger.getLogger(PayServiceImpl.class);
	
	@POST
	@Path("/pay")
	@Produces(MediaType.APPLICATION_JSON)
	public String pay(String json) throws ParseException {
		// 支付
		JSONObject jsonObject;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			jsonObject = new JSONObject(json);
			JSONObject str1 = jsonObject.getJSONObject("BM");
			String money = str1.getString("money");
			String out_trade_no = str1.getString("out_trade_no");
			String token = str1.getString("token");
			String pay = str1.getString("pay");
			String price = str1.getString("price");
			String rid = str1.getString("rid");
			String sign = str1.getString("sign");
			String tid = str1.getString("tid");
			String trade_no = str1.getString("trade_no");
			String udid = str1.getString("udid");
			map.put("money", money);
			map.put("out_trade_no", out_trade_no);
			map.put("udid", udid);
			map.put("pay", pay);
			map.put("price", price);
			map.put("rid", rid);
			map.put("sign", sign);
			map.put("tid", tid);
			map.put("trade_no", trade_no);
			if (!SecurityUtil.isUserLogin(token)) {
				return Constant.PLEASE_LOGIN;
			}
			int rest = orderDao.FindOrder(map);
			
			if (rest <= 0) {
				return "{\"BM\":{},\"EC\":10087,\"EM\":\"支付异常\"}";
			} else {
				return "{\"BM\":{\"rest\":\"success\"},\"EC\":0,\"EM\":\"\"}";
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "{\"BM\":{},\"EC\":10007,\"EM\":\"参数错误\"}";
		}
	}
	
	@POST
	@Path("/notify2") // 支付宝回调函数
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String notify2(String json) throws ParseException, JSONException, UnsupportedEncodingException {
		logger.info("支付宝回调notify2  json:--------" + json);
		String[] split = json.split("&");
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < split.length; i++) {
			String[] split1 = split[i].split("=");
			if ("sign".equals(split1[0])) {
				continue;
			} else {
				map.put(split1[0], URLDecoder.decode(split1[1], "utf-8"));
			}
		}
		map.get("gmt_payment");// 交易付款时间
		map.get("buyer_pay_amount");// 付款金额
		map.get("subject");// 订单标题
		map.get("fund_bill_list");// 支付金额信息
		map.get("total_amount");// 订单金额
		map.get("sign");// 签名
		map.get("sign_type");// 签名类型
		map.get("app_id");// 开发者的应用Id
		map.get("out_trade_no");// 商户订单号
		map.get("trade_no");// 支付宝交易号
		com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(map.get("body").toString());
		// {"from":"安卓-邀请点评作文付款","tid":12,"tprice":36.00,"udid":"","rid":""}
		map.put("tid", jsonObject.getIntValue("tid"));
		map.put("udid", jsonObject.getString("udid"));
		map.put("rid", jsonObject.getString("rid"));
		map.put("buyType", "");
		map.put("compId", "");
		map.put("dpCardId", "");
		try {
			if (jsonObject.getString("buyType") != null && !jsonObject.getString("buyType").equals("")) {
				map.put("buyType", jsonObject.getString("buyType"));
			}
		} catch (Exception e) {
			logger.info("--------------------支付宝获取购买类型异常------------------------json"+json);
		}
		try {
			if (jsonObject.getString("compId") != null && !jsonObject.getString("compId").equals("")) {
				map.put("compId", jsonObject.getString("compId"));
			}
		} catch (Exception e) {
			logger.info("--------------------支付宝获取作文id异常------------------------json"+json);
		}
		try {
			if (jsonObject.getString("dpCardId") != null && !jsonObject.getString("dpCardId").equals("")) {
				map.put("dpCardId", jsonObject.getString("dpCardId"));
			}
		} catch (Exception e) {
			logger.info("--------------------支付宝获取月卡类型异常------------------------json"+json);
		}
		
		map.put("payment", "支付宝");

		int haveRecord = orderDao.findOrderByOutTradeNo((String) map.get("out_trade_no"));
		logger.info("------查询订单结果---------" + haveRecord);
		if (haveRecord == 0) { // 支付返回的notify通知
			logger.info("------开始付款---------");
			int rest = orderDao.InsterOrder(map);
			if (rest > 0) {
				System.out.println("生成订单成功");
			} else {
				logger.error("------生成订单失败--json" + json);
				System.out.println("生成订单失败");
				return "fail";
			}
		} else { // 退款返回的notify通知
			logger.info("------开始退款---------");
		}
		return "success";
	}
	
	@POST
	@Path("/notify") // 支付宝回调函数
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String notify(String json) throws ParseException, JSONException, UnsupportedEncodingException {
		logger.info("支付宝回调notify  json:--------" + json);
		String[] split = json.split("&");
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < split.length; i++) {
			String[] split1 = split[i].split("=");
			if ("sign".equals(split1[0])) {
				continue;
			} else {
				map.put(split1[0], URLDecoder.decode(split1[1], "utf-8"));
			}
		}
		map.get("gmt_payment");// 交易付款时间
		map.get("buyer_pay_amount");// 付款金额
		map.get("subject");// 订单标题
		map.get("fund_bill_list");// 支付金额信息
		map.get("total_amount");// 订单金额
		map.get("sign");// 签名
		map.get("sign_type");// 签名类型
		map.get("app_id");// 开发者的应用Id
		map.get("out_trade_no");// 商户订单号
		map.get("trade_no");// 支付宝交易号
		com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(map.get("body").toString());
		// {"from":"安卓-邀请点评作文付款","tid":12,"tprice":36.00,"udid":"","rid":""}
		map.put("tid", jsonObject.getIntValue("tid"));
		map.put("udid", jsonObject.getString("udid"));
		map.put("rid", jsonObject.getString("rid"));
		map.put("buyType", "");
		map.put("compId", "");
		map.put("cardType", "1");
		map.put("usage_count", "10");
		try {
			if (jsonObject.getString("buyType") != null && !jsonObject.getString("buyType").equals("")) {
				map.put("buyType", jsonObject.getString("buyType"));
			}
		} catch (Exception e) {
			logger.info("--------------------支付宝获取购买类型异常------------------------json"+json);
		}
		try {
			if (jsonObject.getString("compId") != null && !jsonObject.getString("compId").equals("")) {
				map.put("compId", jsonObject.getString("compId"));
			}
		} catch (Exception e) {
			logger.info("--------------------支付宝获取作文id异常------------------------json"+json);
		}
		try {
			if (jsonObject.getString("cardType") != null && !jsonObject.getString("cardType").equals("")) {
				map.put("cardType", jsonObject.getString("cardType"));
			}
		} catch (Exception e) {
			logger.info("--------------------支付宝获取月卡类型异常------------------------json"+json);
		}
		try {
			if (jsonObject.getString("usage_count") != null && !jsonObject.getString("usage_count").equals("")) {
				map.put("usage_count", jsonObject.getString("usage_count"));
			}
		} catch (Exception e) {
			logger.info("--------------------支付宝获取月卡使用期限异常------------------------json"+json);
		}
		
		map.put("payment", "支付宝");
		
		int haveRecord = orderDao.findOrderByOutTradeNo((String) map.get("out_trade_no"));
		logger.info("------查询订单结果---------" + haveRecord);
		if (haveRecord == 0) { // 支付返回的notify通知
			logger.info("------开始付款---------");
			int rest = orderDao.InsterOrder(map);
			if (rest > 0) {
				System.out.println("生成订单成功");
			} else {
				logger.error("------生成订单失败--json" + json);
				System.out.println("生成订单失败");
				return "fail";
			}
		} else { // 退款返回的notify通知
			logger.info("------开始退款---------");
		}
		return "success";
	}
	
	@POST
	@Path("/wxNotify") // 微信回调函数
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String wxNotify(String json) throws Exception  {
		logger.info("微信回调notify  json:--------" + json);
		Map<String, String> wxhdmap = null;
		Map<String, String> returnMap = new HashMap<>();
		try {
			wxhdmap = WXPayUtil.xmlToMap(json);
		} catch (Exception e) {
			logger.info("---------微信回调参数无法解析成map----------");
		}
		//创建集合添加所需参数
		Map<String, String> map = new HashMap<>();
		if (wxhdmap == null) {
			logger.info("------------微信回调解析json失败-----------");
			returnMap.put("return_code", "FAIL");
			returnMap.put("return_msg", "回调解析json失败");
			String xml = WXPayUtil.mapToXml(returnMap);
			return xml;
		}
		map.put("gmt_payment", wxhdmap.get("time_end"));// 交易付款时间
		map.put("buyer_pay_amount", CommUtils.wxPayDoubleDivide100(wxhdmap.get("cash_fee")));// 付款金额
		map.put("subject", "邀请点评");// 订单标题
		map.put("fund_bill_list", "[{\"amount\":\""+CommUtils.wxPayDoubleDivide100(wxhdmap.get("total_fee"))+"\",\"fundChannel\":\"WXPAYACCOUNT\"}]");// 支付金额信息
		map.put("total_amount", CommUtils.wxPayDoubleDivide100(wxhdmap.get("total_fee")));// 订单金额
		map.put("sign", wxhdmap.get("sign"));// 签名
		map.put("sign_type", WXPayConstants.HMACSHA256);// 签名类型
		map.put("app_id", wxhdmap.get("appid"));// 开发者的应用Id
		map.put("out_trade_no", wxhdmap.get("out_trade_no"));// 商户订单号
		map.put("trade_no", wxhdmap.get("transaction_id"));// 交易号
		
		String oldAttach = wxhdmap.get("attach");
		String attach = oldAttach.substring(1, oldAttach.length() - 1);
		net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(attach);
		//签名验证,并校验返回的订单金额是否与商户侧的订单金额一致
		try {
			boolean b = WXPayUtil.isSignatureValid(wxhdmap, WxConstant.appKey, WXPayConstants.SignType.HMACSHA256);
			if (!b) {
				logger.info("------------微信回调签名验证失败-----------");
				returnMap.put("return_code", "FAIL");
				returnMap.put("return_msg", "回调签名验证失败");
				String xml = WXPayUtil.mapToXml(returnMap);
				return xml;
			}
		} catch (Exception e) {
		}
		double d1 = Double.parseDouble(wxhdmap.get("total_fee"));
		double d2 = Double.parseDouble(jsonObject.getString("p"));
		if (d1 != d2) {
			logger.info("------------微信回调金额校验失败-----------");
			returnMap.put("return_code", "FAIL");
			returnMap.put("return_msg", "回调金额校验失败");
			String xml = WXPayUtil.mapToXml(returnMap);
			return xml;
		}
		
		map.put("tid", jsonObject.getString("t"));
		map.put("udid", jsonObject.getString("u"));
		map.put("rid", jsonObject.getString("r"));
		map.put("buyType", jsonObject.getString("b") );
		map.put("compId", jsonObject.getString("c"));
		map.put("dpCardId", jsonObject.getString("d"));
		map.put("payment", "微信");

		int haveRecord = orderDao.findOrderByOutTradeNo((String) map.get("out_trade_no"));
		logger.info("------查询订单结果---------" + haveRecord);
		if (haveRecord == 0) { // 支付返回的notify通知
			logger.info("------开始付款---------");
			int rest = orderDao.InsterOrder2(map);
			if (rest > 0) {
				System.out.println("生成订单成功");
			} else {
				logger.error("------生成订单失败--json" + json);
				returnMap.put("return_code", "FAIL");
				returnMap.put("return_msg", "生成订单失败");
				String xml = WXPayUtil.mapToXml(returnMap);
				return xml;
			}
		} else { // 退款返回的notify通知
			logger.info("------开始退款---------");
		}
		returnMap.put("return_code", "SUCCESS");
		String xml = WXPayUtil.mapToXml(returnMap);
		return xml;
	}
	
	@POST
	@Path("/dpCardPay") // 评点卡支付
	@Produces(MediaType.APPLICATION_JSON)
	public String dpCardPay(String json) {
		JSONObject jsonObject = new JSONObject(json);
		JSONObject str1 = jsonObject.getJSONObject("BM");
		String token = str1.getString("token");
		String udid = str1.getString("udid");
		String cid = str1.getString("cid");//作文id
		//tid为null老流程，tid为空未选择老师，tid有值选择老师
		String tid = null;
		try {
			tid = str1.getString("tid");
		} catch (Exception e) {
		}
		if (!SecurityUtil.isUserLogin(token)) {
			return Constant.PLEASE_LOGIN;
		}
		int c = orderDao.findCompositionHasPay(cid);
		if (c == 1) {
			return "{\"BM\":{},\"EC\":10012,\"EM\":\"作文已经付款，无法进行支付！\"}";
		}
		int i = orderDao.InsterOrderByCard(cid, udid, tid);
		if (i <= 0) {
			logger.error("------生成订单失败--json" + json);
			return "{\"BM\":{},\"EC\":12181,\"EM\":\"评点卡支付失败\"}";
		}else if (i == 3){
			return "{\"BM\":{},\"EC\":12182,\"EM\":\"查询作文信息失败\"}";
		}else if (i == 4) {
			return "{\"BM\":{},\"EC\":12183,\"EM\":\"更新作文状态失败\"}";
		}else if (i == 2) {
			return "{\"BM\":{},\"EC\":12184,\"EM\":\"评点卡次数已使用完\"}";
		}
		return "{\"BM\":{\"token\":\""+token+"\",\"udid\":\""+udid+"\"},\"EC\":0,\"EM\":\"\"}";
	}

	public OrderDao orderDao;

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

}
