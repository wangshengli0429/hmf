//package com.hmf.web.app.service;
//
//import com.hmf.web.utils.ApiResult;
//import com.hmf.web.app.co.OrderCo;
//import com.hmf.web.entity.PeOrders;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Map;
//
//public interface AlipayMentOrderService {
//
//    /**
//     * 手机网站调用
//     * 获取支付宝加签后台的订单信息字符串
//     *
//     * @param orders
//     * @return
//     */
//    public ApiResult getAliPayOrderStr(HttpServletRequest httpRequest, HttpServletResponse httpResponse, PeOrders orders) throws IOException;
//
//
//    /**
//     * 支付宝异步请求逻辑处理
//     * @param conversionParams
//     * @return
//     */
//    public String notify(Map<String, String> conversionParams);
//
//
//    /**
//     * 向支付宝发起订单查询请求
//     * @param orderCo
//     * @return
//     */
//    public ApiResult checkAlipay(OrderCo orderCo);
//}
