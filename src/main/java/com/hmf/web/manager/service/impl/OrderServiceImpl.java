//package com.hmf.web.manager.service.impl;
//
//
//import com.hmf.web.entity.PayInfos;
//import com.hmf.web.entity.PeOrders;
//import com.hmf.web.utils.enums.HttpStateEnum;
//import com.hmf.web.utils.enums.IsPaidEnum;
//import com.hmf.web.app.vo.CrtPayVo;
//import com.hmf.web.manager.bo.OpenBo;
//import com.hmf.web.manager.service.OrderService;
//import com.hmf.web.utils.ApiResult;
//import com.hmf.web.utils.PayCommonUtil;
//import com.hmf.web.utils.enums.HttpCodeEnum;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.PrintWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.*;
//
//@Service
//@Transactional
//public class OrderServiceImpl implements OrderService {
//    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
//
//    /**
//     * @Description 获取openid
//    * @Param
//     **/
//    @Override
//    public ApiResult getOpenId(OpenBo openBo, HttpServletResponse res){
//        ApiResult result = new ApiResult();
//        if(openBo.getEchostr()==null){
//            //TODO
////            String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx7e311fb531ed55b4&secret=a713f9edc87f6de2eac34d74de0af996&code="+openBo.getCode()+"&grant_type=authorization_code";
//            String url = null;
//            System.out.println(openBo.getCode());
//            String openId="";
//            try {
//                URL getUrl=new URL(url);
//                HttpURLConnection http=(HttpURLConnection)getUrl.openConnection();
//                http.setRequestMethod("GET");
//                http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//                http.setDoOutput(true);
//                http.setDoInput(true);
//
//
//                http.connect();
//                InputStream is = http.getInputStream();
//                int size = is.available();
//                byte[] b = new byte[size];
//                is.read(b);
//
//
//                String message = new String(b, "UTF-8");
//
//                com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(message);
//                openId = json.getString("openid");
//
//                result.setStatus(HttpStateEnum.OK.getIndex());
//                result.setMessage(HttpStateEnum.OK.getName());
//                result.setData(openId);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                logger.info("获取openid出现异常！");
//                result.setStatus(HttpStateEnum.INTERNAL_SERVER_ERROR.getIndex());
//                result.setMessage(HttpStateEnum.INTERNAL_SERVER_ERROR.getName());
//            }
//        }else{
//            try {
//                PrintWriter out = res.getWriter();
//                out.print(openBo.getEchostr());
//            } catch (IOException e) {
//                result= new ApiResult(HttpStateEnum.INTERNAL_SERVER_ERROR.getIndex(), HttpStateEnum.INTERNAL_SERVER_ERROR.getName());
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }
//
//    /**
//     * @Description 微信下单
//    * @Param
//     **/
//    @Override
//    public ApiResult ToPay(String OrderNum, String wxnotify, String projectUrl, HttpServletRequest request, HttpServletResponse res) {
//        logger.info("微信预支付下单请求路径："+projectUrl);
//        PeOrders record = new PeOrders();
//        record.setOrderNum(OrderNum);
//        //查询相关的父订单子订单
//        //查询父订单信息
//        //TODO 根据订单号获取产品信息 业务根据情况再定
//        PeOrders parentOrders = null;
//        if(null!=parentOrders&&null!=parentOrders.getId()&&!"".equals(parentOrders.getId())){
//            if (parentOrders.getIsPaid().equals(IsPaidEnum.YES)){
//                logger.info("订单已支付:"+OrderNum);
//                return new ApiResult(HttpStateEnum.ORDER_ERROR_7.getIndex(), HttpStateEnum.ORDER_ERROR_7.getName());
//            }
//            // 订单号
//            String tradeNo = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase().substring(0,32);
//            parentOrders.getOrderType();
//            // 回调地址
//            String notifyUrl = projectUrl + wxnotify;
//            //商品描述
//            String description="包月订单";
//            if (parentOrders.getOrderType()!=1) {
//                description="单次订单";
//            }
//            // 自定义参数
//            //Long userId = 100L; //对应用户id自己修改
//            com.alibaba.fastjson.JSONObject jsAtt = new com.alibaba.fastjson.JSONObject();
//            jsAtt.put("order_num", OrderNum);
//            String attach = jsAtt.toJSONString();
//            //更新流水号
//            PayInfos payInfos =new PayInfos();
//            payInfos.setOrderId(parentOrders.getId());
//            payInfos.setPreNum(tradeNo);
////            int updatepayinfos = payInfosDao.updateByOrderId(payInfos);
//            //TODO
//            int updatepayinfos = 0;
//            if(updatepayinfos>0){
//                logger.info("流水号更新成功");
//            }else {
//                return new ApiResult(HttpStateEnum.ORDER_ERROR_8.getIndex(), HttpStateEnum.ORDER_ERROR_8.getName());
//            }
//            //返回预支付参数
//            SortedMap<String, Object> map = PayCommonUtil.WxPublicPay(tradeNo, parentOrders.getTotalPrice(), description, attach, notifyUrl, request);
//            if(map.get("resultCode").equals("SUCCESS")){
//                CrtPayVo crtPayVo = new CrtPayVo();
//                crtPayVo.setTradeState(tradeNo);
//                crtPayVo.setCodeUrl(String.valueOf(map.get("code_url")));
//                ApiResult result = new ApiResult(HttpCodeEnum.CODE_0000.getCode(), HttpCodeEnum.CODE_0000.getMessage());
//                result.setData(crtPayVo);
//                return result;
//            }else {
//                ApiResult result = new ApiResult(HttpStateEnum.WEPAY_ERROR_7.getIndex(),map.get("errCodeDes").toString());
//                return result;
//            }
//        }else{
//            logger.info("未查询到相应的订单:"+OrderNum);
//            return new ApiResult(HttpStateEnum.ORDER_ERROR_6.getIndex(), HttpStateEnum.ORDER_ERROR_6.getName());
//        }
//
//    }
//
//    //实体参数防空校验
//    private ApiResult checkPeOrders(PeOrders record){
//        if(null == record){
//            return new ApiResult(HttpStateEnum.PARAM_ERROR.getIndex(), HttpStateEnum.PARAM_ERROR.getName());
//        }
//        boolean result = (null!=record.getId())?true:false;
//        if(!result){
//            return new ApiResult(HttpStateEnum.PARAM_ERROR.getIndex(), HttpStateEnum.PARAM_ERROR.getName());
//        }
//        return null;
//    }
//    //主键参数防空校验
//    private ApiResult checkId(Integer id){
//        if(null == id){
//            return new ApiResult(HttpStateEnum.PARAM_ERROR.getIndex(), HttpStateEnum.PARAM_ERROR.getName());
//        }
//        return null;
//    }
//
////    @Override
////    public boolean queryOrderExists(Map<String, Object> map) {
////        PeOrders orders = peOrdersDao.queryOrderExists(map);
////        if(orders!=null){
////            return true;
////        }
////        return false;
////    }
//}
