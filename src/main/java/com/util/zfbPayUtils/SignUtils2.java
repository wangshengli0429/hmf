/**   
 * Copyright © 2018 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.util.zfbPayUtils 
 * @author: think   
 * @date: 2018-1-10 下午4:20:23 
 */
package com.util.zfbPayUtils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import com.util.Base64;

public class SignUtils2 {

	private static final String ALGORITHM = "RSA";

	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

	private static final String DEFAULT_CHARSET = "UTF-8";

	public static final String APPID = "2018010301544970";

	public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCCRx1xIMnju4urDAu06ZStOlDVw354VSjbca5h9mC3RsGPcFErMddjtxw+xXz72rxLERFOdGALWYAon2U6phyhanMTGVthh56k638PbgzPuLxE55JBEQ1USW+Ai8kgC8LTdV+NqJTKgaVJdcjD7XfmxLWqcMmgEBU6bA+zakE28rWpANCav5sYhc3p1jtr/G5obiISZQcmQSE2g2vA8fTVCXTYwz+6iBeUbRTaZJ2ZcI/1THD0/sFjVkCPNR+Mda3n9FW6EGe15QVppbl3uvR0T5qEp9PTPHFOVtvLM2i7toy5eNGSxLOBfF4fZyPxtxVGidg900SwJmG0cHry42sdAgMBAAECggEBAIGv+YccdN3XXPoxS5YU0ISBgObLbaKsY7fQOyAZ+713GkiILsUpIJmLNvrl7b1pyfeQTgIjp1ilCAdoZYU0x1uIdHWW0HkOYMccB6h8jRRbek+9lsJfDz3QvMbiYJ0eGBwYw75pC3VsCfZO4kToqGt5dr7s+En2uKsn15lrNPBUxHHtf7ayOJdFL8mHnsO+J8tUgwjzQwgFhalq3W2Jr7pMZ8I3PaxjNMWuO4dZZNgJ6tV5wnhkX+i0UEagLI4rOEqKDMHc2+Y9x67YBUWIVP+Jh1efDcZ2bNsTV+vHEAOzqZSnsl4Jll6CQWqsbSI9ux2y2HXdkcAo7Dp60EGpoKECgYEA7ZRDYwYbbgmhpSaVhYdEkW0WGMS43OexXaJkjTJerhhG3IEnUieg/DgyqJ6CpY+6OiSQGHq3GLBf05hv2CNtSLTlW3zvt1dVZB2DHp6UVyTJS4EdbI00LcJrputAAcPiOfuGG5XUOH9ZPFz2GyxZS5swqhcdxIqraHPMTtHL1PMCgYEAjGEDrSn25WqQYNVeP8DBXG03ZTJ2aBrN3bNifeZpuW1ir2Oi1aui0z/2vMNpwNh2oUiaWTj9UnInRHT+xrXheb0ML7cMCrvx7QcTDL9LHPtGXjPz+/RttYY0ZoI4Coox4aLaGoNwChd7L6WRztu8yih76CUNjlgq6EmfhAI6A68CgYBowFZ18ttlSRncl4aVuW6iMFbt1M8tl3q8rj1nhC361PlpgAi2UILKsnyjnEA+f4C84iImqPTSDfIyeaPWyMgGprv5+rw3OmqMGfcWKRV+1tlqUSOIGTpIctDEaDVwkc3Ngvk+cHGB96SA2PwqPwOgMmzikg6t+fZZt7T9i8MzwQKBgD9iIn3Sf9cwWYZPcOHKT8RC0AtSUKJKz/81WthL7Bq8aB2XGFPvznIh5G9wsFRRqKbEGGUG1WKfhlljlhH015zXHiSBOt+x4tdKJXDRAsJQNoKcZKKzx8F5mjlavKENqt4dYOstw63a0lU+JerHzqNcyi2UZV2LM7x0WaEKPFWjAoGBAJBEaNA3eBMZrHO6ZEkirO+PHnKKEVsaw1x5GrL2rmVIyAD7eppY+GiuDkPXCRwuIN9N/C1BcGWYddPo9pS9hCLLmM1eCQWu27oHXLcM64HLQAN+XkGytg9AGixRhsBgB5TnTzdmo3EP8Y5NzEIvERYPPXbjN18ErettLvyOztVe";
			
	public static final String RSA2_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgkcdcSDJ47uLqwwLtOmUrTpQ1cN+eFUo23GuYfZgt0bBj3BRKzHXY7ccPsV8+9q8SxERTnRgC1mAKJ9lOqYcoWpzExlbYYeepOt/D24Mz7i8ROeSQRENVElvgIvJIAvC03VfjaiUyoGlSXXIw+135sS1qnDJoBAVOmwPs2pBNvK1qQDQmr+bGIXN6dY7a/xuaG4iEmUHJkEhNoNrwPH01Ql02MM/uogXlG0U2mSdmXCP9Uxw9P7BY1ZAjzUfjHWt5/RVuhBnteUFaaW5d7r0dE+ahKfT0zxxTlbbyzNou7aMuXjRksSzgXxeH2cj8bcVRonYPdNEsCZhtHB68uNrHQIDAQAB";
	
	public static final String ALIPAY_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkBaDZjFadr8YlZ0yv20Yo7AYrk6rJ9aRje/6l+Wd2qxlcpYhy7z4lEoYDd65SyCC6/u3PRwqZixOGbfJkN8dC39PBWUPAB3W8MhrSdk97nFvtNdd6djC3zvx7rsY2ReeZGthySNisDQkD4+6dIT6yNflzvKl4+BQYpfFsLHpU/ZCoVxR4Udyq/cOrHeq4UiCgurjyJMHirIxyG6zRDCfvfjyedt8pJRxZzuCe4y8kBJPozaBQYHKP9RST5JzyP1PnsWZRf6G4DoqDAiQxfWfJxcCkmvzAHL+YyO6+LLuL/u6nPZwNfskQqUpZpKTkdWqCLmg3jmKMMbWya61tOZuPwIDAQAB";
	
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
