/**   
 * Copyright © 2017 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.util 
 * @author: think   
 * @date: 2017-12-21 上午10:06:32 
 */
package com.util;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONException;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.util.wxPayUtils.WXPay;
import com.util.wxPayUtils.WXPayConfig;
import com.util.wxPayUtils.WXPayConfigImpl;
import com.util.wxPayUtils.WXPayConfigImpl2;
import com.util.wxPayUtils.WXPayConstants;
import com.util.wxPayUtils.WXPayUtil;
import com.util.zfbPayUtils.SignUtils1;
import com.util.zfbPayUtils.SignUtils2;

/**
 * @ClassName: BackRefundUtils 
 * @Description: TODO
 * @author: think
 * @date: 2017-12-21 上午10:06:32  
 */
public class BackRefundUtils {
	/**
	 * 微信申请退款
	 */
	public static String wxBackRefund(String json) throws ParseException, JSONException {
		com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(json.substring(1, json.length() - 1));
		logger.info("退款时传参  json:--------" + json);
		Map<String, String> map = new HashMap<>();
		//固定参数
		String appId = jsonObject.getString("appId");//appid
		String mch_id = WxConstant.mch_id;//商户号
		String nonce_str = WXPayUtil.generateUUID();//随机字符串
		String sign_type = WXPayConstants.SignType.HMACSHA256.toString();//签名类型
		//原订单查询参数
		String sign = jsonObject.getString("sign");//签名
		String transaction_id = jsonObject.getString("trade_no");//微信订单号
		//String out_trade_no = jsonObject.getString("out_trade_no");//商户订单号
		String out_refund_no = jsonObject.getString("out_request_no");//商户退款单号
		String total_fee = jsonObject.getString("total_fee");//订单金额
		String refund_fee = jsonObject.getString("refund_fee");//退款金额
		
		//String refund_fee_type = "CNY";//货币种类
		//String refund_desc = jsonObject.getString("refund_reason");//退款原因
		//String refund_account = "REFUND_SOURCE_UNSETTLED_FUNDS";
		
		map.put("appid", appId);
		map.put("mch_id", mch_id);
		map.put("nonce_str", nonce_str);
		map.put("sign", sign);
		map.put("sign_type", sign_type);
		map.put("transaction_id", transaction_id);
		//map.put("out_trade_no", out_trade_no);
		map.put("out_refund_no", out_refund_no);
		//map.put("refund_amount", refund_amount);
		map.put("total_fee", CommUtils.wxPayDoubleMultiply100(total_fee));
		map.put("refund_fee", CommUtils.wxPayDoubleMultiply100(refund_fee));
		//map.put("refund_fee_type", refund_fee_type);
		//map.put("refund_desc", refund_desc);
		//map.put("refund_account", refund_account);
		
		WXPay wxPay = null;
		WXPayConfig config = null;
        try {
        	if ("wx2b7732e1f86eb946".equals(appId)) {
        		config = WXPayConfigImpl.getInstance();//微信退款
			}else {
				config = WXPayConfigImpl2.getInstance();//小程序退款
			}
			wxPay = new WXPay(config);
		} catch (Exception e) {
			logger.info("-----------wxPay初始化异常----------------");
		}
        if (wxPay == null) {
        	return "fail";
		}
        
        Map<String, String> map2 = null;
        try {
			map2 = wxPay.refund(map);
		} catch (Exception e) {
			logger.info("---------------wxPay执行退款异常--------------------map:" + map);
		}
        if (map2 == null) {
        	return "fail";
		}
        String return_code = map2.get("return_code");
        String return_msg = map2.get("return_msg");
        if (!"SUCCESS".equals(return_code)) {
			logger.info("----------微信退款失败return_code：" + return_code + ",return_msg：" + return_msg);
			return "fail";
		}
        String result_code = map2.get("result_code");
        if (!"SUCCESS".equals(result_code)) {
        	String err_code = map2.get("err_code");
			String err_code_des = map2.get("err_code_des");
			logger.info("----------微信退款失败,err_code：" + err_code + ",err_code_des：" + err_code_des);
			return "fail";
        }
		return "success";
	}
	/**
	 * 支付宝申请退款
	 * @param appId 
	 */
	public static String zfbBackRefund(String json, String appId) throws ParseException, JSONException {
		// TODO
		// out_trade_no String 特殊可选 64 订单支付时传入的商户订单号,不能和 trade_no同时为空。 20150320010101001
		// trade_no String 特殊可选 64 支付宝交易号，和商户订单号不能同时为空 2014112611001004680073956707
		// refund_amount Price 必须 9 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数 200.12
		// refund_reason String 可选 256 退款的原因说明 正常退款
		// out_request_no String 可选 64 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。 HZ01RF001
		// operator_id String 可选 30 商户的操作员编号 OP001
		// store_id String 可选 32 商户的门店编号 NJ_S_001
		// terminal_id String 可选 32 商户的终端编号 NJ_T_001
		logger.info("退款时传参  json:--------" + json);
		com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(json);
		String out_trade_no = jsonObject.getString("out_trade_no");// 评点订单 有
		String trade_no = jsonObject.getString("trade_no");// 支付宝订单 有
		String refund_amount = jsonObject.getString("refund_amount");// 退款金额 有 对应用户付款金额
		String refund_reason = jsonObject.getString("refund_reason");// 退款原因
		String out_request_no = jsonObject.getString("out_request_no");// out_request_no 唯一退款标志
		String terminal_id = jsonObject.getString("terminal_id");// 管理员的ID
		
		AlipayClient alipayClient = null;
		if ("2017081808260629".equals(appId)) {
			alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", SignUtils1.APPID, SignUtils1.RSA2_PRIVATE, "json", "utf-8", SignUtils1.ALIPAY_PUBLIC, "RSA2");
		}else {
			alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", SignUtils2.APPID, SignUtils2.RSA2_PRIVATE, "json", "utf-8", SignUtils2.ALIPAY_PUBLIC, "RSA2");
		}
		
		AlipayTradeRefundModel model = new AlipayTradeRefundModel();
		
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		// 外部订单号
		model.setOutTradeNo(out_trade_no);
		// 支付宝订单
		model.setTradeNo(trade_no);
		// 退款金额
		model.setRefundAmount(refund_amount);
		// 退款原因
		model.setRefundReason(refund_reason);
		// 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
		model.setOutRequestNo(out_request_no);
		// 管理员ID
		request.setTerminalInfo(terminal_id);
		request.setBizModel(model);
		// "\",\"out_request_no\":\"" + out_request_no + "\",\"operator_id\":\"" +
		// operator_id + "\",\"store_id\":\"" + store_id + "\",\"terminal_id\":\"" +
		// terminal_id + "\" }");
		AlipayTradeRefundResponse response;
		try {
			response = alipayClient.execute(request);
			if (response.isSuccess()) {
				System.out.println("调用成功");
			} else {
				System.out.println("调用失败");
			}
			String responseStr = response.getBody();
			logger.info("退款时传参  json:--------" + json + "退款返回结果------responseStr：" + responseStr);
			return "success";
		} catch (AlipayApiException e) {
			logger.info("exception:" + e.toString() + "--code--" + e.getErrCode() + "--ErrMsg--" + e.getErrMsg());
			e.printStackTrace();
		}
		return "fail";
	}
	
	private static Logger logger = Logger.getLogger(BackRefundUtils.class);
	
}
