package com.hmf.web.app.co;

import lombok.Data;

import java.io.Serializable;

/**
 * 支付宝支付-检查订单支付状态请求参数
 */
@Data
public class OrderCo implements Serializable {
    private Integer id;
    private String orderNum;
}
