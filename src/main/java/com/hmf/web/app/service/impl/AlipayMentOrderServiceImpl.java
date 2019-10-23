//package com.hmf.web.app.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.AlipayClient;
//import com.alipay.api.DefaultAlipayClient;
//import com.alipay.api.domain.AlipayTradeWapPayModel;
//import com.alipay.api.internal.util.AlipaySignature;
//import com.alipay.api.request.AlipayTradePrecreateRequest;
//import com.alipay.api.request.AlipayTradeQueryRequest;
//import com.alipay.api.response.AlipayTradePrecreateResponse;
//import com.alipay.api.response.AlipayTradeQueryResponse;
//import com.hmf.web.manager.service.OrderService;
//import com.hmf.web.utils.ApiResult;
//import com.hmf.web.utils.enums.HttpStateEnum;
//import com.hmf.web.utils.enums.IsPaidEnum;
//import com.hmf.web.app.co.OrderCo;
//import com.hmf.web.app.model.AlipayConfig;
//import com.hmf.web.app.model.AlipayTradePrecreteResResult;
//import com.hmf.web.app.service.AlipayMentOrderService;
//import com.hmf.web.entity.PayInfos;
//import com.hmf.web.entity.PeOrders;
//import com.hmf.web.utils.StringUtil;
//import com.hmf.web.utils.enums.HttpCodeEnum;
//import net.sf.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.math.BigDecimal;
//import java.net.URLDecoder;
//import java.net.URLEncoder;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//@Service
//@Transactional
//public class AlipayMentOrderServiceImpl implements AlipayMentOrderService {
//
//    private static Logger logger = LoggerFactory.getLogger(AlipayMentOrderServiceImpl.class);
//    @Autowired
//    private OrderService orderService;
//    @Value("${project.domainName.url}")
//    private  String projectUrl;
//
//
//    /**
//     * 手机网站 加签后请求支付宝
//     * 获取支付宝加签后台的订单信息字符串
//     *
//     * @param orders
//     * @return
//     */
//    @Override
//    public ApiResult getAliPayOrderStr(HttpServletRequest httpRequest, HttpServletResponse httpResponse, PeOrders orders) throws IOException {
//        ApiResult checkParamsResult = checkOrderPrams(orders);
//        if(checkParamsResult!=null){
//            return checkParamsResult;
//        }
//        String tradeNo = null;
//        try {
//            // 订单号
//            tradeNo = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase().substring(0, 32);
//            PayInfos payInfos = new PayInfos();
//            payInfos.setOrderId(orders.getId());
//            payInfos.setPreNum(tradeNo);
//            //TODO
//            boolean editResult = false;//payInfosDao.updateByOrderId(payInfos) > 0;
//            if (editResult) {
//                logger.info("流水号更新成功");
//            } else {
//                logger.info("流水号更新失败！订单主键："+orders.getId());
//                return new ApiResult(HttpStateEnum.ORDER_ERROR_8.getIndex(), HttpStateEnum.ORDER_ERROR_8.getName());
//            }
//
//        }catch (Exception e){
//            logger.info("流水号更新失败！订单主键：["+orders.getId()+"],异常信息"+e.getMessage());
//            e.printStackTrace();
//            return new ApiResult(HttpStateEnum.ORDER_ERROR_8.getIndex(), HttpStateEnum.ORDER_ERROR_8.getName());
//        }
//        //最终返回加签之后的，app需要传给支付宝app的订单信息字符串
//        logger.info("==================支付宝下单,商户订单号为："+orders.getOrderNum()+"，商户支付流水号："+tradeNo);
//        try{
//            //实例化客户端（参数：网关地址、商户appid、商户私钥、格式、编码、支付宝公钥、加密类型），为了取得预付订单信息
//            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
//                    AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET,
//                    AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
//
//            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.precreate
//            AlipayTradePrecreateRequest ali_request = new AlipayTradePrecreateRequest();
//            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式
//            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
//
//            //业务参数传入,可以传很多，参考API
//            Map<String,Object> map = new HashMap<>();
//            map.put("orderId",  orders.getId());
//            map.put("orderNum",orders.getOrderNum());
//            model.setPassbackParams(URLEncoder.encode(map.toString(),AlipayConfig.CHARSET)); //公用参数（附加数据）
//            model.setBody(orders.getCustomerNote());                       //对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
//            model.setSubject(orders.getProductName());                 //商品名称
//            model.setOutTradeNo(tradeNo);           //商户订单号(自动生成)
//            model.setTotalAmount(orders.getTotalPrice().toString());         //支付金额
//            String notifyUrl = projectUrl + AlipayConfig.notify_url;
//            String returnUrl = AlipayConfig.return_url+"?id="+orders.getId();
//            ali_request.setNotifyUrl(notifyUrl);        //异步回调地址（后台）
//            ali_request.setReturnUrl(returnUrl);	    //同步回调地址（APP）
//            logger.info("====================异步通知的地址为：" + notifyUrl);
//            logger.info("====================异步通知--H5--的地址为：" + returnUrl);
//            ali_request.setBizModel(model);
//
//            AlipayTradePrecreateResponse alipayTradeAppPayResponse = alipayClient.execute(ali_request);
//            if(alipayTradeAppPayResponse.isSuccess()) {
//                String orderString =  alipayTradeAppPayResponse.getBody();//就是orderString 可以直接给APP请求，无需再做处理。
//                AlipayTradePrecreteResResult resResult = JSON.parseObject(orderString,AlipayTradePrecreteResResult.class);
//                logger.info("==================支付宝加签字符串:" + orderString);
//                ApiResult apiResult = new ApiResult(HttpCodeEnum.CODE_0000.getCode(), HttpCodeEnum.CODE_0000.getMessage());
//                apiResult.setData(resResult);
//                return apiResult;
//            }else{
//                logger.info("加签异常！订单主键：["+orders.getId()+"]");
//                return  new ApiResult(HttpCodeEnum.ALIPAY_CODE_6100.getCode(), HttpCodeEnum.ALIPAY_CODE_6100.getMessage());
//            }
//        } catch (AlipayApiException e) {
//            logger.info("与支付宝交互出错，加签异常，请检查代码！");
//            e.printStackTrace();
//        }
//
//        return  new ApiResult(HttpCodeEnum.ALIPAY_CODE_6100.getCode(), HttpCodeEnum.ALIPAY_CODE_6100.getMessage());
//    }
//
//
//    /**
//     * 加签校验必填参数
//     * @param orders
//     * @return
//     */
//    private ApiResult checkOrderPrams(PeOrders orders){
//        if(orders == null){
//            return new ApiResult(HttpCodeEnum.ALIPAY_CODE_6170.getCode(), HttpCodeEnum.ALIPAY_CODE_6170.getMessage());
//        }
//        if(orders.getTotalPrice()==null){
//            return new ApiResult(HttpCodeEnum.ALIPAY_CODE_6150.getCode(), HttpCodeEnum.ALIPAY_CODE_6150.getMessage());
//        }
//        int totalAmt = orders.getTotalPrice().compareTo(BigDecimal.ZERO);
//        if(totalAmt == -1 || totalAmt == 0){
//            return new ApiResult(HttpCodeEnum.ALIPAY_CODE_6150.getCode(), HttpCodeEnum.ALIPAY_CODE_6150.getMessage());
//        }
//        if(StringUtil.isEmpty(orders.getProductName())){
//            return new ApiResult(HttpCodeEnum.ALIPAY_CODE_6130.getCode(), HttpCodeEnum.ALIPAY_CODE_6130.getMessage());
//        }
//        if(orders.getId() == null){
//            return new ApiResult(HttpCodeEnum.ALIPAY_CODE_6120.getCode(), HttpCodeEnum.ALIPAY_CODE_6120.getMessage());
//        }
//        if(orders.getCompaniesId() == null){
//            return new ApiResult(HttpCodeEnum.ALIPAY_CODE_6160.getCode(), HttpCodeEnum.ALIPAY_CODE_6160.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * 支付宝异步请求逻辑处理
//     * @return
//     */
//    public String notify(Map<String, String> conversionParams) {
//        logger.info("==================支付宝异步请求逻辑处理");
//        //签名验证(对支付宝返回的数据验证，确定是支付宝返回的)
//        boolean signVerified = false;
//
//        try {
//            //调用SDK验证签名
//            signVerified = AlipaySignature.rsaCheckV1(conversionParams, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);
//        } catch (AlipayApiException e) {
//            logger.info("==================验签失败 ！" + e);
//            e.printStackTrace();
//            return "fail";
//        }
//        //对验签进行处理
//        if (signVerified) {
//            //验签通过
//            //获取需要保存的数据
//            String appId = conversionParams.get("app_id");//支付宝分配给开发者的应用Id
//            String notifyTime = conversionParams.get("notify_time");//通知时间:yyyy-MM-dd HH:mm:ss
//            String gmtCreate = conversionParams.get("gmt_create");//交易创建时间:yyyy-MM-dd HH:mm:ss
//            String gmtPayment = conversionParams.get("gmt_payment");//交易付款时间
//            String gmtRefund = conversionParams.get("gmt_refund");//交易退款时间
//            String gmtClose = conversionParams.get("gmt_close");//交易结束时间
//            String tradeNo = conversionParams.get("trade_no");//支付宝的交易号
//            String outTradeNo = conversionParams.get("out_trade_no");//获取商户之前传给支付宝的订单号（商户系统的唯一订单号）
//            String outBizNo = conversionParams.get("out_biz_no");//商户业务号(商户业务ID，主要是退款通知中返回退款申请的流水号)
//            String buyerLogonId = conversionParams.get("buyer_logon_id");//买家支付宝账号
//            String sellerId = conversionParams.get("seller_id");//卖家支付宝用户号
//            String sellerEmail = conversionParams.get("seller_email");//卖家支付宝账号
//            String totalAmount = conversionParams.get("total_amount");//订单金额:本次交易支付的订单金额，单位为人民币（元）
//            String receiptAmount = conversionParams.get("receipt_amount");//实收金额:商家在交易中实际收到的款项，单位为元
//            String invoiceAmount = conversionParams.get("invoice_amount");//开票金额:用户在交易中支付的可开发票的金额
//            String buyerPayAmount = conversionParams.get("buyer_pay_amount");//付款金额:用户在交易中支付的金额
//            String tradeStatus = conversionParams.get("trade_status");// 获取交易状态
//            String passbackParams = conversionParams.get("passback_params");
//            String orderNum = null;
//            Integer orderId = null;
//            try {
//                String params = URLDecoder.decode(passbackParams, AlipayConfig.CHARSET);
//                Map<String,Object> map = getStringToMap(params);
//                orderId = Integer.valueOf(map.get("orderId").toString());
//                orderNum = String.valueOf(map.get("orderNum"));
//            }catch (UnsupportedEncodingException e){
//                logger.info("==========支付回调时，URLDecoder解码异常"+e.getMessage());
//                e.printStackTrace();
//                return "fail";
//            }
//            //校验查询商户订单号数据库是否存在
//            //TODO
//            PeOrders peOrders = null;//peOrdersDao.selectByPrimaryKey(orderId);
//            //支付宝官方建议校验的值（out_trade_no、sellerId、app_id）
//            if (peOrders != null && AlipayConfig.APPID.equals(appId)) {
//                //修改数据库支付宝订单表(因为要保存每次支付宝返回的信息到数据库里，以便以后查证)
//
//                int returnResult = 5;    //更新交易表中状态
//                logger.info("========订单号：" + orderNum + "支付状态:" + tradeStatus);
//                switch (tradeStatus) // 判断交易结果
//                {
//                    case "TRADE_FINISHED": // 交易结束并不可退款
//                        returnResult = 3;
//                        break;
//                    case "TRADE_SUCCESS": // 交易支付成功
//                        returnResult = 2;
//                        break;
//                    case "TRADE_CLOSED": // 未付款交易超时关闭或支付完成后全额退款
//                        returnResult = 1;
//                        break;
//                    case "WAIT_BUYER_PAY": // 交易创建并等待买家付款
//                        returnResult = 0;
//                        break;
//                    default:
//                        returnResult = 5;
//                        break;
//                }
//                //只处理支付成功的订单: 修改交易表状态,支付成功
//                if (returnResult == 2 || returnResult == 3) {
//                    logger.info("订单号：" + outTradeNo + "支付完成");
//                    if (peOrders.getIsPaid().equals(IsPaidEnum.YES)) {
//                        return "success";
//                    } else if (peOrders.getIsPaid().equals(IsPaidEnum.NO)) {
//                        BigDecimal totalAmt = new BigDecimal(receiptAmount);
//                        ApiResult apiResult =null;
////                                ApiResult apiResult = orderService.UpdatePayInfos(peOrders,tradeNo,conversionParams,dateFormat(gmtPayment),totalAmt, PayWayEnum.ALIPAY);
//                        if(apiResult.getStatus().equals(HttpStateEnum.OK.getIndex())){
//                            return "success";
//                        }else {
//                            logger.info("======支付成功同步相关表状态数据异常!订单Id"+outTradeNo);
//                            return "fail";
//                        }
//
//                    }else{
//                        //支付成功，订单支付状态异常
//                        logger.info("=======支付成功！该订单支付状态异常，请联系后台！订单号："+outTradeNo);
//                        return "fail";
//                    }
//                } else {
//                    logger.info("=====支付失败！请联系后台！订单号：" + outTradeNo );
//                    return "fail";
//                }
//            } else {
//                logger.info("==================支付宝官方建议校验的值（out_trade_no、total_amount、sellerId、app_id）,不一致！返回fail");
//                logger.info("=====支付失败！请联系后台！订单号：" + outTradeNo );
//                return "fail";
//            }
//        } else {  //验签不通过
//            logger.info("==================验签不通过 ！支付失败！订单号："+ conversionParams.get("out_trade_no"));
//            return "fail";
//        }
//    }
//
//    /**
//     * 向支付宝发起订单查询请求
//     * @param orderCo
//     * @return
//     */
//    @Override
//    public ApiResult checkAlipay(OrderCo orderCo) {
//        logger.info("==================向支付宝发起查询，查询商户信息orderCo"+orderCo.toString());
//        if(orderCo==null || orderCo.getId() == null){
//            return new ApiResult(HttpCodeEnum.ALIPAY_CODE_6120.getCode(), HttpCodeEnum.ALIPAY_CODE_6120.getMessage());
//        }
//        //TODO
//        PayInfos payInfos =  null;//payInfosDao.selectByOrderId(orderCo.getId());
//        String outTradeNo = null;//商户流水号
//        if(payInfos == null || StringUtil.isEmpty(payInfos.getPreNum())){
//            return new ApiResult(HttpCodeEnum.ALIPAY_CODE_6310.getCode(), HttpCodeEnum.ALIPAY_CODE_6310.getMessage());
//        }else{
//            outTradeNo =  payInfos.getPreNum();
//        }
//        logger.info("==================订单流水号信息："+payInfos.toString());
//
//        try {
//            //实例化客户端（参数：网关地址、商户appid、商户私钥、格式、编码、支付宝公钥、加密类型）
//            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
//                    AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET,
//                    AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
//
//            AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
//            alipayTradeQueryRequest.setBizContent("{" + "\"out_trade_no\":\""+outTradeNo+"\"" +"}");
//            AlipayTradeQueryResponse alipayTradeQueryResponse = alipayClient.execute(alipayTradeQueryRequest);
//            if(alipayTradeQueryResponse.isSuccess()){
//                logger.info("==========[订单Id:"+orderCo.getId()+"]调用支付宝查询该笔订单支付结果："+alipayTradeQueryResponse.getBody());
//
//                //校验查询商户订单号数据库是否存在
//                //TODO
//                PeOrders record = null;
////                PeOrders record = peOrdersDao.selectByPrimaryKey(orderCo.getId());
//                logger.info("==================订单信息："+record.toString());
//                if(record == null){
//                    return new ApiResult(HttpCodeEnum.ALIPAY_CODE_6310.getCode(), HttpCodeEnum.ALIPAY_CODE_6310.getMessage()+"订单Id："+orderCo.getId());
//                }
//                String aliOrderNum = alipayTradeQueryResponse.getTradeNo();//支付宝订单号
//                int result = 5;
//                String resultMessage = null;
//                switch (alipayTradeQueryResponse.getTradeStatus()) // 判断交易结果
//                {
//                    case "TRADE_FINISHED": // 交易结束并不可退款
//                        result = 3;
//                        resultMessage = "交易结束并不可退款";
//                        break;
//                    case "TRADE_SUCCESS": // 交易支付成功
//                        result = 2;
//                        resultMessage = "交易支付成功";
//                        break;
//                    case "TRADE_CLOSED": // 未付款交易超时关闭或支付完成后全额退款
//                        result = 1;
//                        resultMessage = "未付款交易超时关闭或支付完成后全额退款";
//                        break;
//                    case "WAIT_BUYER_PAY": // 交易创建并等待买家付款
//                        result = 0;
//                        resultMessage = "交易创建并等待买家付款";
//                        break;
//                    default:
//                        result = 5;
//                        break;
//                }
//                if(result == 0) {
//                    //交易并创建等待买家付款
//                    logger.info("=============订单号:"+outTradeNo+",交易并创建等待买家付款");
//                    return new ApiResult(HttpCodeEnum.ALIPAY_CODE_6200.getCode(), HttpCodeEnum.ALIPAY_CODE_6200.getMessage());
//                }else if(result == 3 || result == 2){
//                    if(record.getIsPaid().equals(IsPaidEnum.YES)){
//                        logger.info("=========支付成功，订单号:"+outTradeNo);
//                        // 如果已支付 查支付表
//                        return new ApiResult(HttpCodeEnum.CODE_0000.getCode(), HttpCodeEnum.CODE_0000.getMessage());
//                    }else if(record.getIsPaid().equals(IsPaidEnum.NO)){
//                        logger.info("=========支付成功去修改表记录，订单号:"+outTradeNo);
//                        // 更新表记录
//                        BigDecimal totalAmount = new BigDecimal(alipayTradeQueryResponse.getTotalAmount());
//                        JSONObject jasonObject = JSONObject.fromObject(alipayTradeQueryResponse.getBody());
//                        Map<String,String> map = (Map<String,String>)jasonObject;
//                        //TODO
//                        ApiResult apiResult = null;//orderService.UpdatePayInfos(record,aliOrderNum,map,alipayTradeQueryResponse.getSendPayDate(),totalAmount, PayWayEnum.ALIPAY);
//                        if(apiResult.getStatus().equals(HttpStateEnum.OK.getIndex())){
//                            return new ApiResult(HttpCodeEnum.CODE_0000.getCode(), HttpCodeEnum.CODE_0000.getMessage());
//                        }else {
//                            logger.info("======支付成功同步相关表状态数据异常!订单Id"+outTradeNo);
//                            return apiResult;
//                        }
//                    }else{
//                        //支付成功，订单支付状态异常
//                        return new ApiResult(HttpCodeEnum.ALIPAY_CODE_6350.getCode(),"订单号："+outTradeNo+ HttpCodeEnum.ALIPAY_CODE_6350.getMessage());
//                    }
//                }else{
//                    logger.info("==================订单号："+outTradeNo+",该笔订单未能支付成功，支付状态："+ resultMessage);
//                    return new ApiResult(HttpCodeEnum.ALIPAY_CODE_6500.getCode(),"订单号："+outTradeNo+ HttpCodeEnum.ALIPAY_CODE_6500.getMessage());
//                }
//
//            } else {
//                logger.info("==================订单号："+outTradeNo+",调用支付宝查询接口失败！"+ alipayTradeQueryResponse.getBody());
//                return new ApiResult(HttpCodeEnum.ALIPAY_CODE_6310.getCode(), HttpCodeEnum.ALIPAY_CODE_6310.getMessage());
//            }
//        } catch (AlipayApiException e) {
//            logger.info("订单号："+outTradeNo+",支付异常:"+e.getMessage());
//            e.printStackTrace();
//            return new ApiResult(HttpCodeEnum.ALIPAY_CODE_6310.getCode(), HttpCodeEnum.ALIPAY_CODE_6310.getMessage());
//        }
//    }
//
//    private Date dateFormat(String date){
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//        try{
//            Date dateTime = simpleDateFormat.parse(date);
//            return dateTime;
//        }catch (Exception e){
//            logger.info("日期格式转换异常："+e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     *
//     * String转map
//     * @param str
//     * @return
//     */
//    public static Map<String,Object> getStringToMap(String str){
//        String start ="{";
//        String end = "}";
//        String result = null;
//        if(str.contains(start)||str.contains(end)){
//            String a = str.replace(start,"");
//            result =  a.replace(end,"");
//        }
//        //根据逗号截取字符串数组
//        String[] str1 = result.split(",");
//        //创建Map对象
//        Map<String,Object> map = new HashMap<>();
//        //循环加入map集合
//        for (int i = 0; i < str1.length; i++) {
//            //根据":"截取字符串数组
//            String[] str2 = str1[i].split("=");
//            //str2[0]为KEY,str2[1]为值
//            map.put(str2[0],str2[1]);
//        }
//        return map;
//    }
//
//
////    /**
////     * json 字符串清除字段为null or ""
////     * @return
////     */
////    private JsonConfig JsonConfigCleanField(){
////        JsonConfig jsonConfig = new JsonConfig();
////        PropertyFilter filter = new PropertyFilter() {
////            public boolean apply(Object object, String fieldName, Object fieldValue) {
////                if(fieldValue instanceof List){
////                    @SuppressWarnings("unchecked")
////                    List<Object> list = (List<Object>) fieldValue;
////                    if (list.size()==0) {
////                        return true;
////                    }
////                }
////                //过滤条件：值为null时过滤
////                return null == fieldValue;
////            }
////        };
////        jsonConfig.setJsonPropertyFilter(filter);
////        return jsonConfig;
////    }
//}
