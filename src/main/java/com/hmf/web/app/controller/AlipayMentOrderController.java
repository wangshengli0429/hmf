//package com.hmf.web.app.controller;
//
//import com.hmf.web.app.service.AlipayMentOrderService;
//import com.hmf.web.config.SignatureAnnotation;
//import com.hmf.web.utils.ApiResult;
//import com.hmf.web.app.co.OrderCo;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
///**
// * 支付宝支付功能
// */
//@RestController
//@RequestMapping("/app/alipay")
//public class AlipayMentOrderController {
//
//    private static Logger logger = LoggerFactory.getLogger(AlipayMentOrderController.class);
//
//    @Autowired
//    private AlipayMentOrderService alipayMentOrderService;
//
////    /**
////     * 手机网站支付
////     * 获取支付宝加签后台的订单信息字符串
////     *
////     * @param orders
////     * @return
////     */
////    @PostMapping(value = "/order_qr_code")
////    @SignatureAnnotation
////    public ApiResult getAliPayOrderStr(HttpServletRequest request, HttpServletResponse response, PeOrders orders) throws IOException {
////        if(orders!=null) {
////            logger.info("method getAliPayOrderStr params orders:" + orders.toString());
////        }
////        return alipayMentOrderService.getAliPayOrderStr(request, response, orders);
////    }
//
//
//    /**
//     * 支付宝支付成功后.异步请求该接口
//     * 支付宝异步回调返回值要求success！ 否则会认为异步回调失败，失败会25小时以内完成8次通知（通知的间隔频率一般是：4m,10m,10m,1h,2h,6h,15h）
//     * @param request
//     * @return
//     * @throws IOException
//     */
//    @RequestMapping(value = "/notify_url")
//    @SignatureAnnotation
//    public String notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        logger.info("==================支付宝异步返回支付结果开始");
//        //1.从支付宝回调的request域中取值
//        //获取支付宝返回的参数集合
//        Map<String, String[]> aliParams = request.getParameterMap();
//        //用以存放转化后的参数集合
//        Map<String, String> conversionParams = new HashMap<String, String>();
//        for (Iterator<String> iter = aliParams.keySet().iterator(); iter.hasNext(); ) {
//            String key = iter.next();
//            String[] values = aliParams.get(key);
//            String valueStr = "";
//            for (int i = 0; i < values.length; i++) {
//                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
//            }
//            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
////             valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
//            conversionParams.put(key, valueStr);
//        }
//        logger.info("==================返回参数集合：" + conversionParams);
//        return alipayMentOrderService.notify(conversionParams);
//    }
//
//    /**
//     * 向支付宝发起订单查询请求
//     * @param orderCo
//     * @return
//     */
//    @RequestMapping("/check_order")
//    @SignatureAnnotation
//    public ApiResult checkAlipay(HttpServletRequest request, HttpServletResponse response, OrderCo orderCo){
//        logger.info("=======检查支付状态请求参数OrderCo："+orderCo.toString());
//        return alipayMentOrderService.checkAlipay(orderCo);
//    }
//
//}