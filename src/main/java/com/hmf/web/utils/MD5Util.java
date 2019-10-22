package com.hmf.web.utils;

import java.security.MessageDigest;

public class MD5Util {
	public static String digest16(String inStr) {
		return digest(inStr, 16);
	}
	public static String digest(String inStr) {
		return digest(inStr, 32);
	}
	private static String digest(String inStr, int rang) {
		MessageDigest md5 = null;
		if ( StringUtil.isEmpty(inStr) ) {
			return "";
		}

		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuilder hexValue = new StringBuilder();
		// StringBuffer

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		if ( rang == 32 ) {
			return hexValue.toString();
		} else {
			return hexValue.toString().substring(8, 24);
			// JS : "".substring
		}
	}
	private static String byteArrayToHexString(byte b[]) {
		StringBuilder resultSb = new StringBuilder();
		for (byte aB : b) resultSb.append(byteToHexString(aB));
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = origin;
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception ignored) {
		}
		return resultString;
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };


//	/**
//	 * 加密解密算法 执行一次加密，两次解密
//	 */
//	public static String convertMD5(String inStr) {
//
//		char[] a = inStr.toCharArray();
//		for (int i = 0; i < a.length; i++) {
//			a[i] = (char) (a[i] ^ 't');
//		}
//		String s = new String(a);
//		return s;
//	}
//
//	public static void main(String args[]) {
//
//		// 21232f297a57a5a743894a0e4a801fc3
////    	String ss = "0123456789aabc";
////	    String s = new String("admin");
////	    System.out.println(digest(s));
////
////	    s = new String("admin1");
////	    System.out.println(digest(s));
//		String s = convertMD5("ddc6f44d47c06448f304da1d48e31cb");
//		System.out.println("解密之后的字符串"+s);
//	}
}
