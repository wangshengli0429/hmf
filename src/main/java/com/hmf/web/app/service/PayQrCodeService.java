package com.hmf.web.app.service;

import com.hmf.web.utils.ApiResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PayQrCodeService {
    /**
     * 微信 or 支付宝 生成二维码
     * @param id
     * @param payWay
     * @return
     */
    ApiResult pay(HttpServletRequest request, HttpServletResponse response, Integer id, String payWay);
}
