//package com.hmf.web.app.service.impl;
//
//import com.hmf.web.utils.ApiResult;
//import com.hmf.web.utils.enums.OrderTypeEnum;
//import com.hmf.web.utils.enums.PayWayEnum;
//import com.hmf.web.app.service.AlipayMentOrderService;
//import com.hmf.web.app.service.PayQrCodeService;
//import com.hmf.web.entity.PeOrders;
//import com.hmf.web.utils.StringUtil;
//import com.hmf.web.utils.enums.HttpCodeEnum;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Service
//@Transactional
//public class PayQrCodeServiceImpl implements PayQrCodeService {
//    private static Logger logger = LoggerFactory.getLogger(PayQrCodeServiceImpl.class);
//    @Value("${project.domainName.url}")
//    private  String projectUrl;
//    @Value("${xcloud.devSwitch}")
//    private String devSwitch;
//    @Value("${xcloud.uploadPath}")
//    private String uploadPath;
//
////    @Autowired
////    private PeOrdersDao peOrdersDao;
//
//    @Autowired
//    private AlipayMentOrderService alipayMentOrderService;
//
//    @Override
//    public ApiResult pay(HttpServletRequest request, HttpServletResponse response, Integer id, String payWay) {
//        if(id == null){
//            return new ApiResult(HttpCodeEnum.CODE_6000.getCode(), HttpCodeEnum.CODE_6000.getMessage());
//        }
//        try {
////            PeOrders orders = peOrdersDao.selectByPrimaryKey(id);
//            PeOrders orders =null;//TODO
//            if(StringUtil.isEmpty(orders.getProductName())){
//                OrderTypeEnum orderType = OrderTypeEnum.get(orders.getOrderType());
//                if(orderType!=null){
//                    orders.setProductName(orderType.getMessage());
//                }
//            }
//            if (payWay.equals(PayWayEnum.ALIPAY.name())) {
//                //支付宝支付
//               return alipayMentOrderService.getAliPayOrderStr(request,response,orders);
//            }
//
//        }catch (Exception e){
//
//        }
//        ApiResult apiResult = new ApiResult(HttpCodeEnum.CODE_0000.getCode(), HttpCodeEnum.CODE_0000.getMessage());
//        return apiResult;
//    }
//
////    private  String fileUrl(String payWay){
////        File directory  = null;// 参数为空
////        String url = null;
////        String basePath = "pay_logo";
////        String pay_logo = null;
////        logger.info("开发环境开关状态:"+devSwitch);
////        try {
////            if (payWay.equals(PayWayEnum.WECHAT.name())) {
////                //微信支付logo
////                pay_logo = "wechat.jpg";
////            } else if (payWay.equals(PayWayEnum.ALIPAY.name())) {
////                //支付宝支付logo
////                pay_logo = "alipay.jpg";
////            }
////            if(StringUtil.isNotEmpty(pay_logo)) {
////                if (devSwitch.equals("open")) {
////                    directory = new File("F:");
////                    url = directory.getCanonicalPath() + directory.separator + uploadPath + directory.separator + basePath + directory.separator + pay_logo;
////                } else {
////                    directory = new File("");
////                    url = directory.getCanonicalPath() + directory.separator + uploadPath + directory.separator + basePath + directory.separator + pay_logo;
////                }
////            }
////        }catch (Exception e){
////            logger.error("获取支付二维码logo路径异常："+e.getMessage());
////            e.printStackTrace();
////            return null;
////        }
////        return url;
////    }
//}
