package com.util.zfbPayUtils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import com.util.Base64;

public class SignUtils1 {

	private static final String ALGORITHM = "RSA";

	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

	private static final String DEFAULT_CHARSET = "UTF-8";

	public static final String APPID = "2017081808260629";

	public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCytMQmDqIezgefoddmH/kXYlt5RTpw29m99l6kUCwPjjMHaiKPJCSPvz8l9zs88F/6SPK2ajZ06XpQHn3Z6iOMvYXQ+aA5BuJnEVhoD5IjCsVa3g8mu/HT94D5fTn2akZElJS3Ntoax+ns+uyH7lOcDm0FqXF6YvgP9wZKCwSyzx88mYd0wFHq13gtTMOZnt+zTeO145vCydU1R1Oy0fta0iT+yuKAHoWx98qfhro3YPskDPP47guGkMno0MEvQyDAPXceBGXQSr3TN7dUHvWlo5ox3SpFaKOEkk5XudMoyXZY6rlQb0nnKEELS022YBnEVSzsex1fcnGuGMv1fYPfAgMBAAECggEAcFxD8AHHYUL9fBsQCcAUp+VJymgCpK82qr/bJcjayzUHVKQg8osxCcifdcmKz3f7gKyZHqSNdNjP9MwXKQqUK4uGNayeVvzDITbAtT0FD09YYxe3wwzsse+ugi+AYDTV1YzuLHwqiigkJsJmMdlhbH+NgL/W/7J2ceNJawiefaW6TprJafa+yU7TvOhxS6PQcsGboJwPsn5Zj9mwzAF4JaHkItWDXjphWTj6Vs3O29LYl8sMl4/vLEvLtuUCi/xNdDHeS6KFyAUUNRmExmJ9XZmrr/6pIZlMI6iRVHL4NtYtyhqmVKeCto1Hik4Pz9VNatul5csOKBfvc3nP488+MQKBgQDuyTSXQTthIvnNbt1pSwT3+FURQVyA+kYhanLpz/9oQN+sME5JilZoBNZlyxvMvns6ObJ6NNSc8+w7aF+0b6SHaLSqRAQM1PlpSizP2cfxmhkQVjYSh3DBIo0QGgwmn7mDCMv9a/35rHkwHH77Z340CYtlig8N5fcVCiDd0xne0wKBgQC/lsmSP5IoD5Zp0qy+ymA53vfJ1KPUhtiXleCfrWd6t0bqi0FTw8iXOUlhpnSPNAaERpB5U8uoLOkFOM4ISBg1qZiD58BMZDUV/08bnYh7lsxfYLOSwG0FGxq+zMrB9Tw0qy7Sdg58Y30j8jrhvCG+XXRf4nJ/Qi0MLWwsbNKXRQKBgQDKd555Fd2UaoSnNPbV/b8zxsPSgro9vnhSgyD2sd+ddUxygu8OmhbT0i48V7C1tVllMGDuD/erMYQ/hyuHIha+ngB6UnugqcDlDvjaMI3BhOj1mjP1GBR6HqK/YWamJU62FXJp6fSKjDyhl7fv0XwIAYPJz/sdzLzpgRIHJKTN8wKBgF6da8xiwp4wHHsERLUwIldSCpLNBV9WfK5XoQeaTiknS8lWnhts66nq65di6dsyuSkn4/9W55OVIjqm8/DZEcOmjqBNmgTf0+uaGworQRSvwxwzGFuX1f8YhJvHbTZeEq57/3KGRN8fDtTqsjN6gjuN0YPsw9ie6LpD0FEcKN/1AoGAXqiBHJgr8tx9pc17VIR6SlaelImD8rBgCJ9gh0EovRHB2cias8BknnXPsxpvRugMSDm+wDheoXqEpieKREJm1cjM6Qkp8jDBobHzpINsMAV/ekYFuE2wlpA51btyQDdlRtRmwISHxHY7zYuLBQC2jCOGMl8HFEaJ4Qe9NcClzkk=";

	public static final String RSA2_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsrTEJg6iHs4Hn6HXZh/5F2JbeUU6cNvZvfZepFAsD44zB2oijyQkj78/Jfc7PPBf+kjytmo2dOl6UB592eojjL2F0PmgOQbiZxFYaA+SIwrFWt4PJrvx0/eA+X059mpGRJSUtzbaGsfp7Prsh+5TnA5tBalxemL4D/cGSgsEss8fPJmHdMBR6td4LUzDmZ7fs03jteObwsnVNUdTstH7WtIk/srigB6FsffKn4a6N2D7JAzz+O4LhpDJ6NDBL0MgwD13HgRl0Eq90ze3VB71paOaMd0qRWijhJJOV7nTKMl2WOq5UG9J5yhBC0tNtmAZxFUs7HsdX3JxrhjL9X2D3wIDAQAB";

	public static final String ALIPAY_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh6DoivqM3E6iJ3RGMt+4thYBDpkQriyLrpoIsWdynVyED8P2R6YkPhD4CJpPIZRRqzXhZbMYmujAy2LcEhcTcF4OFq0m4LpQqixMEFWrXmyxY9kyYmaycJNqMrUqdJbKFqkJe0tZh+oOdSQCdmSH+Y8dXJKXOarLdmoFh6N9xIB6Mm8r1y1qWw4Ti+x6aIDJ3HSWJzeOqe4EcIgks8+zg2NO1aBjVoXd70g4fUaDQT693pnuSrha/J5aJzTlwoKB7Bb7mIURCFJQsKL+HpV2Zr3syducVr28tteGP177tVe1iuJa+XFJwV1Hgu4H3iS+rkI+80N/QQai3fmXr6Ix3wIDAQAB";

	private static String getAlgorithms(boolean rsa2) {
		return rsa2 ? SIGN_SHA256RSA_ALGORITHMS : SIGN_ALGORITHMS;
	}

	public static String sign(String content, boolean rsa2) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(RSA2_PRIVATE));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature.getInstance(getAlgorithms(rsa2));

			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
