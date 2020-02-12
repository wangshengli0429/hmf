package com.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rest.service.TomcatListener;

//哈希token
public class SecurityUtil {
	//存储token
	public static  List<String> sessionList = TomcatListener.sessionList;
	//存储手机号、验证码
	public static  Map<String,String> phoneMap=new HashMap<String,String>();

	public  static String makeAuthenticationInfo(  String[] args ) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			buff.append(args[i]);
		}
		return hash( buff.toString() );
	}
	
	public static String hash(String src ){
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update( src.getBytes() );
			byte[] digest = md.digest();
			return bytes2String( digest);
		} catch ( Exception e) {
			throw new RuntimeException(e);
		}
				
	}
	
	
	public static String bytes2String( byte[] data ){
		
		BigInteger bi = new BigInteger(data);
		return bi.toString(16).toUpperCase();
	}
	
	//测试哈希
	public static void main(String[] args) {
		System.out.println(  hash("poiiui"+"123456") );
		System.out.println(  hash("poiiui123456") );
	}
	
	
	public static boolean isUserLogin(String token) {
		for (int i = 0; i < sessionList.size(); i++) {
			if (sessionList.get(i).equals(token)) {
				return true;
			}
		}
		return false;
	}
	
	
}
