package com.hmf.web.entity;

import com.hmf.web.utils.enums.IsPaidEnum;
import com.hmf.web.utils.enums.PayWayEnum;
import com.hmf.web.utils.enums.PayTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayInfos extends BaseEntity implements Serializable {
    //主键
    private Integer id;
    // 客户Id
    private Integer customerId;
    //客户公司id
    private Integer companiesId;
    //工单Id
    private Integer caseId;
    //钱包充值记录主键
    private Integer rechargeAmtRecordId;
    //预支付/支付下单号
    private String preNum;
    //支付编码
    private String payNum;
    //支付金额
    private BigDecimal amount;
    // 支付方式 微信 or 支付宝
    private PayWayEnum payWay;
    // 报修方式: ORDER_ALIPAY:订单支付,REPAIR_ALIPAY：报修支付;
    private PayTypeEnum payType;
    //订单号
    private Integer orderId;
    //是否支付
    private IsPaidEnum isPaid;
    //
    private String infoResult;
    //支付时间
    private Date paidTime;

    private Integer creator;
}
