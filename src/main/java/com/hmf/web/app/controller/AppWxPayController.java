package com.hmf.web.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.hmf.web.config.SignatureAnnotation;
import com.hmf.web.manager.bo.OpenBo;
import com.hmf.web.manager.service.OrderService;
import com.hmf.web.utils.ApiResult;
import com.hmf.web.utils.enums.HttpStateEnum;
import com.hmf.web.entity.PeOrders;
import com.hmf.web.utils.PayCommonUtil;
import com.hmf.web.utils.StringUtil;
import com.hmf.web.utils.WxPayUtil;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

/**
 * @ClassName: AppWxPayController
 * @Version: 1.0
 */
@RestController
@RequestMapping("/app/wxpay")
public class AppWxPayController {
    private static Logger logger   = LoggerFactory.getLogger(AppWxPayController.class);
    private static String wxnotify = "/app/wxpay/notify_url";

    @Value("${project.domainName.url}")
    private  String projectUrl;

    @Autowired
    private OrderService orderService;

    @GetMapping("/query_pay")
    @SignatureAnnotation
    public ApiResult QueryPay(String trade_no, HttpServletRequest request, HttpServletResponse res) {
        logger.info("查询微信支付,流水号:"+trade_no);//包装数据
        ApiResult apiResult = checkTrade_no(trade_no);
        if(apiResult != null){
            return apiResult;
        }
        //SUCCESS—支付成功 REFUND—转入退款 NOTPAY—未支付 CLOSED—已关闭 REVOKED—已撤销（付款码支付） USERPAYING--用户支付中（付款码支付） PAYERROR--支付失败(其他原因，如银行返回失败)
        SortedMap<String, Object> stringObjectSortedMap = PayCommonUtil.WxPublicQueryPay(trade_no, request);
        if (stringObjectSortedMap.get("trade_state").equals("NOTPAY")){
            apiResult = new ApiResult(HttpStateEnum.WEPAY_ERROR_2.getIndex(),stringObjectSortedMap.get("trade_state_desc").toString());
        }else if (stringObjectSortedMap.get("trade_state").equals("SUCCESS")){
            apiResult = new ApiResult(HttpStateEnum.OK.getIndex(),stringObjectSortedMap.get("trade_state_desc").toString());
        }else if(stringObjectSortedMap.get("trade_state").equals("REFUND")){
            apiResult = new ApiResult(HttpStateEnum.WEPAY_ERROR_1.getIndex(),stringObjectSortedMap.get("trade_state_desc").toString());
        }else if(stringObjectSortedMap.get("trade_state").equals("CLOSED")){
            apiResult = new ApiResult(HttpStateEnum.WEPAY_ERROR_3.getIndex(),stringObjectSortedMap.get("trade_state_desc").toString());
        }else if(stringObjectSortedMap.get("trade_state").equals("REVOKED")){
            apiResult = new ApiResult(HttpStateEnum.WEPAY_ERROR_4.getIndex(),stringObjectSortedMap.get("trade_state_desc").toString());
        }else if(stringObjectSortedMap.get("trade_state").equals("USERPAYING")){
            apiResult = new ApiResult(HttpStateEnum.WEPAY_ERROR_5.getIndex(),stringObjectSortedMap.get("trade_state_desc").toString());
        }else if(stringObjectSortedMap.get("trade_state").equals("PAYERROR")){
            apiResult = new ApiResult(HttpStateEnum.WEPAY_ERROR_6.getIndex(),stringObjectSortedMap.get("trade_state_desc").toString());
        }
        return apiResult;
    }

    /**
     * @return a
     * @Description crm微信预约下单
    * @Param
     **/
    @RequestMapping(value = "/crtpay",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @SignatureAnnotation
    public ApiResult crtpay(String OrderNum, HttpServletRequest request, HttpServletResponse res) {
        logger.info("微信支付预约下单,订单号"+OrderNum);//包装数据
        ApiResult apiResult = checkParam(OrderNum);
        if(apiResult != null){
            return apiResult;
        }
        apiResult = orderService.ToPay(OrderNum, wxnotify,projectUrl, request, res);
        return apiResult;
    }
    /**
     * 支付回调地址
     * @param request
     * @return  produces = MediaType.APPLICATION_JSON_VALUE
     * @throws IOException
     */
    @RequestMapping(value = "/notify_url")
    public String wxpaySucc(HttpServletRequest request,HttpServletResponse response) throws IOException {
        System.out.println("微信支付回调");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String resultxml = new String(outSteam.toByteArray(), "utf-8");
//        String resultxml="<xml><appid><![CDATA[wx7e311fb531ed55b4]]></appid>\n" +
//                "<attach><![CDATA[{\"order_num\":\"20190121142520000001404728834\"}]]></attach>\n" +
//                "<bank_type><![CDATA[CFT]]></bank_type>\n" +
//                "<cash_fee><![CDATA[1]]></cash_fee>\n" +
//                "<fee_type><![CDATA[CNY]]></fee_type>\n" +
//                "<is_subscribe><![CDATA[Y]]></is_subscribe>\n" +
//                "<mch_id><![CDATA[1515651421]]></mch_id>\n" +
//                "<nonce_str><![CDATA[EAEVs9uoJqzAS281VP0GJmg7l7Q2PWtq]]></nonce_str>\n" +
//                "<openid><![CDATA[oYbLR0uQ2pUCloqC3cadIBEaExt4]]></openid>\n" +
//                "<out_trade_no><![CDATA[a0edf511b9b54e94a3a1f51d5284d22d]]></out_trade_no>\n" +
//                "<result_code><![CDATA[SUCCESS]]></result_code>\n" +
//                "<return_code><![CDATA[SUCCESS]]></return_code>\n" +
//                "<sign><![CDATA[0F0C755761F6879511F0730F25905A68]]></sign>\n" +
//                "<time_end><![CDATA[20190122204009]]></time_end>\n" +
//                "<total_fee>1</total_fee>\n" +
//                "<trade_type><![CDATA[JSAPI]]></trade_type>\n" +
//                "<transaction_id><![CDATA[4200000267201901228762344845]]></transaction_id>\n" +
//                "</xml>";
        logger.info("微信的xml"+resultxml);
        Map<String, String> params = null;
        try {
            params = PayCommonUtil.doXMLParse(resultxml);
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        outSteam.close();
        inStream.close();
        if (!PayCommonUtil.isTenpaySign(params)) {
            // 支付失败
            return "fail";
        } else {
            logger.info("===============付款成功==============");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
            // ------------------------------
            //商户支付订单号
            String out_trade_no = params.get("out_trade_no");
            //商户包装数据以获得产品订单
            String attachJSON = params.get("attach");
            JSONObject jsonObject = JSONObject.parseObject(attachJSON);
            String order_num = jsonObject.getString("order_num");
            //微信支付订单号
            String transaction_id = params.get("transaction_id");
            //订单支付金额
            String total_fee = params.get("total_fee");
            //单位转换分转化为元
            BigDecimal pay_fee = new BigDecimal(total_fee);
            pay_fee=pay_fee.divide(new BigDecimal(100));
            //支付完成时间
            String time_end = params.get("time_end");

            Date endDate = null;
            try {
                DateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
                endDate= format1.parse(time_end);
            } catch (ParseException e) {
                e.printStackTrace();
                logger.info("时间格式转换异常");
            }

            PeOrders record = new PeOrders();
            record.setOrderNum(order_num);
            //查询相关的父订单子订单
            //TODO 查询父订单信息
//            PeOrders parentOrders = orderService.selectByOrderNum(record);
            PeOrders parentOrders = null;
            if(null!=parentOrders&&null!=parentOrders.getId()&&!"".equals(parentOrders.getId())){
                ApiResult result= new ApiResult();
                try {
                    //TODO 更新订单
//                    result = orderService.UpdatePayInfos(parentOrders,transaction_id,params,endDate,pay_fee, PayWayEnum.WECHAT);
                } catch (Exception e) {
                    e.printStackTrace();
                    result= new ApiResult(HttpStateEnum.UPDATE_EXCEP.getIndex(), HttpStateEnum.UPDATE_EXCEP.getName());
                }
                //ApiResult result = new ApiResult();
                result.setStatus("200");
                if(!"200".equals(result.getStatus())){

                    return WxPayUtil.setXML("FAIL","ERROR");
                }else{
                    String resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    //response.getWriter().write(returnResutStr);
                    //Response.Write(returnResutStr);
//                    response.reset();
//                    response.getWriter().write(returnResutStr);
//                   return returnResutStr;
//                    BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//                    out.write(resXml.getBytes());
//                    out.flush();
//                    out.close();
                    return resXml;
                }
            }else{
                logger.info("未查询到相应的订单:"+order_num);
                return WxPayUtil.setXML("FAIL","ERROR");
            }
//            return "success";
        }
    }
    @GetMapping("/authorize")
    public void authorize(HttpServletRequest request, HttpServletResponse response){
        try {
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7e311fb531ed55b4&redirect_uri=http://smb.allits.com.cn/apis/wechat_order/getCode?orderNum=1111&response_type=code&scope=snsapi_base";
            response.sendRedirect(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/getCode")
    public ApiResult getCode(String code,HttpServletRequest request, HttpServletResponse response){
        ApiResult result = new ApiResult();
        try {
            if(StringUtil.isNotEmpty(code)){
                result.setStatus(HttpStateEnum.OK.getIndex());
                result.setMessage(HttpStateEnum.OK.getName());
                result.setData(code);
            }else {
                result.setStatus(HttpStateEnum.WECHAT_ERROR_1.getIndex());
                result.setMessage(HttpStateEnum.WECHAT_ERROR_1.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    //微信公众号获取openid
    @RequestMapping("/open_id")
    public ApiResult getOpenId(OpenBo openBo, HttpServletResponse res)  {
        logger.info("获取公众号openID:"+openBo);//包装数据
        ApiResult result = orderService.getOpenId(openBo,res);
        return result;
    }
    //实体参数防空校验
    private ApiResult checkParam(String OrderNum){
        boolean result = (StringUtil.isEmpty(OrderNum));
        if(result){
            return new ApiResult(HttpStateEnum.PARAM_ERROR.getIndex(), HttpStateEnum.PARAM_ERROR.getName());
        }
        return null;
    }
    //实体参数防空校验
    private ApiResult checkTrade_no(String trade_no){
        boolean result = (StringUtil.isEmpty(trade_no))?true:false||trade_no.equalsIgnoreCase("undefined");
        if(result){
            return new ApiResult(HttpStateEnum.PARAM_ERROR.getIndex(), HttpStateEnum.PARAM_ERROR.getName());
        }
        return null;
    }

}
