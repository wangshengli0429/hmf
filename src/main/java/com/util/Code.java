package com.util;

import com.hmf.web.manager.service.impl.ExternalFacadeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加解密
 * @author lcc
 *
 */
public class Code {
	Logger logger = LoggerFactory.getLogger(ExternalFacadeImpl.class);
	private static char[][] CODE_INT = {
			{'a','k','u','E','O'}   //0
			,{'b','l','v','F','P'}  //1
			,{'c','m','w','G','Q'}  //2
			,{'d','n','x','H','R'}  //3
			,{'e','o','y','I','S'}  //4
			,{'f','p','z','J','T'}  //5
			,{'g','q','A','K','U'}  //6
			,{'h','r','B','L','V'}  //7
			,{'i','s','C','M','W'}  //8
			,{'j','t','D','N','X'}  //9
	};
	
	private static char[] WORD = {
			'a', 'b', 'c', 'd', 'e', 'f', 'g'
			, 'h', 'i', 'j', 'k','l','m','n'
			, 'o','p','q','r','s','t','u','v','w','x','y','z'
	};
	
	private static char[] CODE_TABLE = {'0','1','2','3','4','5','6','7','8','9'
			                           ,'a','b','c','d','e','f','g','h','i','j'
			                           ,'k','l','m','n','o','p','q','r','s','t'
			                           ,'u','v','w','x','y','*'};
	
	public static int getDecodeInt(String code) {
		if (code == null || code.length() < 1) {
			return -1;
		}
		try {
			if (code.length() == 32) {
				//加密的数据
				return Code.decodeInt(code);
			} else {
				//未加密的数据
				return Integer.valueOf(code);
			}
		} catch (Exception e) {
//			logger.error("201512171041 getDecodeInt :"+e.getMessage());
			return -1;
		}
	}
	
	/**
	 * 针对int类型加密,与decodeInt配对
	 * @param n 需大于等于0，且小于等于20位 
	 * @return 返回32位加密后的字符串
	 */
	public static String encodeInt(int n) {
		if (n < 0) {
			return "";
		}
		final String number = String.valueOf(n);
		final int length = number.length();
		if (20 < length) {
//			logger.error("2015112314051 无效的整数:"+n);
			return "";
		}
		
		StringBuffer code = new StringBuffer();
		for (int i=length-1; i>-1; i--) {
			int x = Integer.valueOf(String.valueOf(number.charAt(i)));
			code.append(CODE_INT[x][((int)(Math.random()*10000000))%5]);
		}
		
		int preLength = ((int)(Math.random()*10000000))%9+2;
		StringBuffer pre = new StringBuffer();
		pre.append(WORD[preLength]);
		pre.append(WORD[length]);
		for (int i=2; i<preLength; i++) {
			pre.append(WORD[((int)(Math.random()*10000000))%26]);
		}
		
		int lastLength = 32-length-preLength;
		StringBuffer last = new StringBuffer();
		for (int i=0; i<lastLength; i++) {
			last.append(WORD[((int)(Math.random()*10000000))%26]);
		}
		
		return pre.toString()+code.toString()+last.toString();
	}
	
	//针对int类型解密,与encodeInt配对
	public static int decodeInt(String code) {
		if (code == null || code.length() != 32) {
			return -1;
		}
		
		int length = 0;
		int preLength = 0;
		char first = code.charAt(0);
		char second = code.charAt(1);
		
		for (int i=WORD.length-1; i>-1; i--) {
			if (WORD[i] == first) {
				preLength = i;
			} 
			if (WORD[i] == second) {
				length = i;
			}
		}
		if (preLength < 1 || length < 1) {
//			logger.error("2015112317191 无效的编码:"+code);
			return -1;
		}
		
		int number = 0;
		String n_code = code.substring(preLength, preLength+length);
		for (int i=0; i<length; i++) {
			char c = n_code.charAt(i);
			int n = -1;
			for (int x=0; x<CODE_INT.length; x++) {
				for (int y=CODE_INT[x].length-1; y>-1; y--) {
					if (CODE_INT[x][y] == c) {
						n = x;
						break;
					}
				}
				if (n > -1) {
					break;
				}
			}
			if (n < 0) {
//				logger.error("2015112317192 无效的编码:"+code);
				return -1;
			}
			number += n*(int)Math.pow(10,i);
		}
		
		return number;
	}
	
	//加密数据表名
	public static String encodeTableName(String table) {
		if (table == null || "".equals(table)) {
			return "";
		}		
		
		StringBuffer strb = new StringBuffer();
		table = table.toUpperCase();
		for (int i=0,count=table.length(); i<count; i++) {
			char c = table.charAt(i);
			if (c == '_') {
				strb.append('z');
			} else if (65 <= c && c < 91){
				strb.append(CODE_TABLE[c-65]);
			} else if (48 <= c && c < 58) {
				strb.append(CODE_TABLE[c-48+26]);
			} else {
				strb.append(c);
			}
		}
		return strb.toString();
	}
	
	//解密数据表名
	public static String decodeTableName(String code) {
		if (code == null || "".equals(code)) {
			return "";
		}
		
		StringBuffer strb = new StringBuffer();
		for (int i=0,count=code.length(); i<count; i++) {
			char c = code.charAt(i);
			if (c == 'z') {
				strb.append('_');
				continue;
			}
			
			int j=35;
			for (; j>-1; j--) {
				if (CODE_TABLE[j] == c) {
					if (j < 26) {
						strb.append((char)(j+65));
					} else {
						strb.append((char)(j-26+48));
					}
					break;
				}
			}
			if (j < 0) {
				strb.append(c);
			}
		}
		return strb.toString();
	}
	
	//将ID的前端补0（共7位），用于前台显示。
	public static String getIdForShow(int id) {
		return String.format("%07d", id);
	}
	public static String getIdForShow(String id) {
		return String.format("%07d", Code.getDecodeInt(id));
	}
	
	//getIdForShow的反向工程
	public static int getIdForDatabase(String idForShow) {
		if (idForShow == null) {
			return -1;
		}
		return Integer.valueOf(idForShow);
	}
}
