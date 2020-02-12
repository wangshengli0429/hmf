/**   
 * Copyright © 2017 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.util.wxPayUtils 
 * @author: think   
 * @date: 2017-12-21 下午1:45:42 
 */
package com.util.wxPayUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.util.WxConstant;

/**
 * @ClassName: WXConfigImpl
 * @Description: 客户端微信支付使用
 * @author: think
 * @date: 2017-12-21 下午1:45:42
 */
public class WXPayConfigImpl extends WXPayConfig {

	private byte[] certData;
	private static WXPayConfigImpl INSTANCE;

	private WXPayConfigImpl() throws Exception {
		String certPath = WXPayConstants.certAdress;
		File file = new File(certPath);
		InputStream certStream = new FileInputStream(file);
		this.certData = new byte[(int) file.length()];
		certStream.read(this.certData);
		certStream.close();
	}

	public static WXPayConfigImpl getInstance() throws Exception {
		if (INSTANCE == null) {
			synchronized (WXPayConfigImpl.class) {
				if (INSTANCE == null) {
					INSTANCE = new WXPayConfigImpl();
				}
			}
		}
		return INSTANCE;
	}

	@Override
	String getAppID() {
		return WxConstant.appId;
	}

	@Override
	String getMchID() {
		return WxConstant.mch_id;
	}

	@Override
	String getKey() {
		return WxConstant.appKey;
	}

	@Override
	InputStream getCertStream() {
		ByteArrayInputStream certBis;
		certBis = new ByteArrayInputStream(this.certData);
		return certBis;
	}

	@Override
	IWXPayDomain getWXPayDomain() {
		return IWXPayDomainSimpleImpl.instance();
	}

	@Override
	public int getReportWorkerNum() {
		return 1;
	}

	@Override
	public int getReportBatchSize() {
		return 2;
	}

}
