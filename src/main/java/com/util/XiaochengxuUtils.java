package com.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

public class XiaochengxuUtils {
	
	private static Logger logger = Logger.getLogger(XiaochengxuUtils.class);
	private static boolean initialized = false; 
	
	public static String grant_type = "authorization_code";
	public static String loginUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + WxConstant.appId2 + "&secret=" + WxConstant.appSecret + "&grant_type=" + grant_type;
	
	public static String httpsRequest(String code) {
		try {
			//创建SSLContext对象，并使用我们指定的信任管理器初始化
			//TrustManager[] tm = { new TrustManager() };
			//SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			//sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			//SSLSocketFactory ssf = sslContext.getSocketFactory();
			
			//appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
			String requestUrl = loginUrl + "&js_code=" + code;
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			//conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod("POST");
			//conn.setRequestProperty("content-type", "text/xml"); 
			//conn.setRequestProperty("contentType", "utf-8");
			//conn.setRequestProperty("Host", "api.mch.weixin.qq.com");
			conn.setConnectTimeout(10*1000);
			conn.setReadTimeout(10*1000);
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
			String result = buffer.toString();
			Map map = (Map)JSON.parse(result);
			if (map.get("session_key") == null) {
				return "{\"status\": \"fail\"}";
			}else {
				map.put("status", "success");
				Gson gson = new Gson();  
				return gson.toJson(map); 
			}
		} catch (ConnectException ce) {
			logger.error("小程序连接超时：{}", ce);
		} catch (Exception e) {
			logger.error("小程序https请求异常：{}", e);
		}
		return "{\"status\": \"fail\"}";
	}
	private static byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte){    
        initialize();    
        try {    
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");    
            Key sKeySpec = new SecretKeySpec(keyByte, "AES");    
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化     
            byte[] result = cipher.doFinal(content);    
            return result;    
        } catch (Exception e) {    
            e.printStackTrace();  
            logger.info("生成iv异常");
        }    
        return null;    
    }      
        
	private static void initialize(){      
        if (initialized) return;      
        Security.addProvider(new BouncyCastleProvider());      
        initialized = true;      
    }    
    //生成iv      
	private static AlgorithmParameters generateIV(byte[] iv) throws Exception{      
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");      
        params.init(new IvParameterSpec(iv));      
        return params;      
    }  
    public static String getDecryptJSON(String encryptedData, String session_key, String iv){
    	Map<String, String> map = new HashMap<>();
        try {    
            byte[] resultByte  = decrypt(Base64.decodeBase64(encryptedData),    
                    Base64.decodeBase64(session_key), Base64.decodeBase64(iv));    
            if(null != resultByte && resultByte.length > 0){    
                String userInfo = new String(resultByte, "UTF-8");                   
                map.put("status", "success");    
                map.put("userInfo", userInfo);    
            }else{    
                map.put("status", "fail");    
            }    
        } catch (UnsupportedEncodingException e){  
        	logger.info("resultByte转化异常");
        	e.printStackTrace();    
        }                  
        Gson gson = new Gson();    
        String decodeJSON = gson.toJson(map);    
        System.out.println(decodeJSON);
        return decodeJSON;
    }
}
