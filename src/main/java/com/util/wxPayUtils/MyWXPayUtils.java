/**   
 * Copyright © 2018 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.util.wxPayUtils 
 * @author: think   
 * @date: 2018-1-25 下午4:51:30 
 */
package com.util.wxPayUtils;

import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.util.CommUtils;
import com.util.WxConstant;
import com.util.wxPayUtils.WXPayConstants.SignType;

/**
 * @ClassName: MyUtils 
 * @Description: TODO
 * @author: think
 * @date: 2018-1-25 下午4:51:30  
 */
public class MyWXPayUtils {
	/**
	 * 封装map
	 * @throws Exception 
	 */
	//客户端使用
	public static Map<String, String> getWxSign(String price, String attach) throws Exception{
		SignType signType = WXPayConstants.SignType.HMACSHA256;
        String key = WxConstant.appKey;
        
        SortedMap<String, String> parameters = new TreeMap<>();
        parameters.put("appid", WxConstant.appId);  //公众账号ID                  
        parameters.put("mch_id", WxConstant.mch_id); //商户号          
        //parameters.put("device_info","WEB"); //终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"     
        parameters.put("nonce_str", WXPayUtil.generateNonceStr()); //随机字符串,推荐随机数生成算法
        parameters.put("body", "邀请点评");//支付订单描述，取资源名称
        //parameters.put("detail", "");//商品详情，取资源名称
        parameters.put("attach", attach);//附加数据。在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        parameters.put("out_trade_no", CommUtils.getOutTradeNo());//商户订单号，取订单记录id
        parameters.put("fee_type", "CNY");//货币类型 /符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
        parameters.put("total_fee", price); //总金额,取资源所需话费金额  订单总金额，单位为分，详见支付金额
        parameters.put("spbill_create_ip", "123.56.186.12"); //APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP     
        String notify_url = URLEncoder.encode(WXPayConstants.NOTIFYURL, "UTF-8");
        parameters.put("notify_url", notify_url);//支付成功后回调的action，与JSAPI相同
        parameters.put("trade_type", "APP");
        //parameters.put("product_id", "");//商品号要唯一
        parameters.put("sign_type", WXPayConstants.HMACSHA256);
        
        String sign = WXPayUtil.generateSignature(parameters, key, signType);
        parameters.put("sign", sign);
        //boolean b = WXPayUtil.isSignatureValid(parameters, WXPayConstants.appKey, WXPayConstants.SignType.HMACSHA256);
        //String xml1 = WXPayUtil.mapToXml(parameters);
        //String xml2 = getRequestXml(parameters);
        //方法1
        /*String string1 = httpsRequest(WXPayConstants.UNIFIED_ORDER_URL, xml1);
        //String string2 = httpsRequest2(WXPayConstants.UNIFIED_ORDER_URL, xml1);
        //String string2 = httpsRequest(WXPayConstants.UNIFIED_ORDER_URL, xml2);
        Map<String, String> newMap = WXPayUtil.xmlToMap(string1);*/
        //方法2
		WXPay wxPay = null;
		WXPayConfig config = null;
        try {
        	config = WXPayConfigImpl.getInstance();
			wxPay = new WXPay(config, WXPayConstants.NOTIFYURL);
		} catch (Exception e) {
			logger.info("-----------统一下单wxPay初始化异常----------------");
		}
        if (wxPay == null) {
        	return null;
		}
        
        Map<String, String> map = null;
        try {
			map = wxPay.unifiedOrder(parameters);
		} catch (Exception e) {
			logger.info("---------------统一下单wxPay异常--------------------map:" + map);
		}
        /*Map<Object, Object> result = new HashMap<>();
        result.put("old", newMap);
        result.put("new", map);*/
        
        if (map == null) {
			logger.info("--------获取签名失败map2为空----------");
			return null;
		}
		if (Objects.equals(map.get("return_code"), "FAIL")) {
			logger.info("--------获取签名失败return_msg：" + map.get("return_msg"));
			return null;
		}else {
			if (Objects.equals(map.get("result_code"), "FAIL")) {
				logger.info("--------获取签名失败err_code：" + map.get("err_code") + ",err_code_des:" + map.get("err_code_des"));
				return null;
			}
		}
		SortedMap<String, String> parameters2 = new TreeMap<>();
		parameters2.put("appid", WxConstant.appId);  //公众账号ID 
		parameters2.put("partnerid", WxConstant.mch_id); //商户号    
		parameters2.put("prepayid", map.get("prepay_id"));
		parameters2.put("package", "Sign=WXPay");
		parameters2.put("noncestr", map.get("nonce_str")); //随机字符串,推荐随机数生成算法
		parameters2.put("timestamp", getTimeStampByS());
		String sign2 = WXPayUtil.generateSignature(parameters2, key, signType);
		parameters2.put("sign", sign2);
        boolean b = WXPayUtil.isSignatureValid(parameters2, WxConstant.appKey, WXPayConstants.SignType.HMACSHA256);
        if (b) {
        	return parameters2;
		}else {
			return null;
		}
	}
	
	//小程序自己使用它
	public static Map<String, String> getWxSign2(String price, String attach, String openid) throws Exception{
		SignType signType = WXPayConstants.SignType.HMACSHA256;
		String key = WxConstant.appKey;
		
		SortedMap<String, String> parameters = new TreeMap<>();
		parameters.put("appid", WxConstant.appId2);  //公众账号ID                  
		parameters.put("mch_id", WxConstant.mch_id); //商户号          
		parameters.put("nonce_str", WXPayUtil.generateNonceStr()); //随机字符串,推荐随机数生成算法
		parameters.put("body", "邀请点评");//支付订单描述，取资源名称
		//parameters.put("detail", "");//商品详情，取资源名称
		parameters.put("attach", attach);//附加数据。在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
		parameters.put("out_trade_no", CommUtils.getOutTradeNo());//商户订单号，取订单记录id
		parameters.put("fee_type", "CNY");//货币类型 /符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
		parameters.put("total_fee", price); //总金额,取资源所需话费金额  订单总金额，单位为分，详见支付金额
		parameters.put("spbill_create_ip", "123.56.186.12"); //APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP     
		String notify_url = URLEncoder.encode(WXPayConstants.NOTIFYURL, "UTF-8");
		parameters.put("notify_url", notify_url);//支付成功后回调的action，与JSAPI相同
		parameters.put("trade_type", "JSAPI");
		//parameters.put("product_id", "");//商品号要唯一
		parameters.put("sign_type", WXPayConstants.HMACSHA256);
		parameters.put("openid", openid);
		
		String sign = WXPayUtil.generateSignature(parameters, key, signType);
		parameters.put("sign", sign);
		String xml2 = WXPayUtil.mapToXml(parameters);
		System.out.println(xml2);
		
		WXPay wxPay = null;
		WXPayConfig config = null;
		try {
			config = WXPayConfigImpl2.getInstance();
			wxPay = new WXPay(config, WXPayConstants.NOTIFYURL);
		} catch (Exception e) {
			logger.info("-----------统一下单wxPay初始化异常----------------");
		}
		if (wxPay == null) {
			return null;
		}
		
		logger.info("----付款openId:"+openid+",appId:"+parameters.get("appid"));
		
		Map<String, String> map = null;
		try {
			map = wxPay.unifiedOrder(parameters);
		} catch (Exception e) {
			logger.info("---------------统一下单wxPay异常--------------------map:" + map);
		}
		
		if (map == null) {
			logger.info("--------获取签名失败map为空----------");
			return null;
		}
		if (Objects.equals(map.get("return_code"), "FAIL")) {
			logger.info("--------获取签名失败return_msg：" + map.get("return_msg"));
			return null;
		}else {
			if (Objects.equals(map.get("result_code"), "FAIL")) {
				logger.info("--------获取签名失败err_code：" + map.get("err_code") + ",err_code_des:" + map.get("err_code_des"));
				return null;
			}
		}
		SortedMap<String, String> parameters2 = new TreeMap<>();
		parameters2.put("appId", WxConstant.appId2);  //公众账号ID 
		//parameters2.put("partnerid", WXPayConstants.mch_id); //商户号    
		//parameters2.put("prepayid", map.get("prepay_id"));
		parameters2.put("package", "prepay_id="+map.get("prepay_id"));
		parameters2.put("nonceStr", map.get("nonce_str")); //随机字符串,推荐随机数生成算法
		parameters2.put("timeStamp", getTimeStampByS());
		parameters2.put("signType", WXPayConstants.HMACSHA256);
		String sign2 = WXPayUtil.generateSignature(parameters2, key, signType);
		parameters2.put("sign", sign2);
		return parameters2;
	}
	/**
	 * 时间戳
	 */
	public static String getTimeStampByS() {
        long time = System.currentTimeMillis();
        return String.valueOf(time / 1000);
    }
	 /**
	  * 字符串转为map
     */
    /*public static Map<String, String> stringToMap(String str){
   	 Map<String, String> mapObj = new HashMap<>();
   	 String[] sts = str.split("&");
   	 for (String string : sts) {
			String[] sts2 = string.split("=");
			mapObj.put(sts2[0], sts2[1]);
		}
   	 return mapObj;
    }
    
    private static String httpsRequest2(String requestUrl, String reqBody) throws Exception {
    	String UTF8 = "UTF-8";
        URL httpUrl = new URL(requestUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
        httpURLConnection.setRequestProperty("Host", "api.mch.weixin.qq.com");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setConnectTimeout(10*1000);
        httpURLConnection.setReadTimeout(10*1000);
        httpURLConnection.connect();
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(reqBody.getBytes(UTF8));

        //获取内容
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF8));
        final StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
        }
        String resp = stringBuffer.toString();
        if (stringBuffer!=null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (inputStream!=null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream!=null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(resp);
		return resp;
    }
    private static String httpsRequest(String requestUrl, String outputStr) {
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			//TrustManager[] tm = { new TrustManager() };
			//SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			//sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			//SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			//conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod("POST");
			conn.setRequestProperty("content-type", "text/xml"); 
			conn.setRequestProperty("contentType", "utf-8");
			conn.setRequestProperty("Host", "api.mch.weixin.qq.com");
			conn.setConnectTimeout(10*1000);
			conn.setReadTimeout(10*1000);
			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			return buffer.toString();
		} catch (ConnectException ce) {
			logger.error("连接超时：{}", ce);
		} catch (Exception e) {
			logger.error("https请求异常：{}", e);
		}
		return null;
	}
    
	public static String getRequestXml(SortedMap<String,String> parameters){
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {
				sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
			}else {
				sb.append("<"+k+">"+v+"</"+k+">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}*/
	
    private static Logger logger = Logger.getLogger(MyWXPayUtils.class);

}
