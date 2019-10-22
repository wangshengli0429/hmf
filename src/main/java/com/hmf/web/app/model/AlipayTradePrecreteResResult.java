package com.hmf.web.app.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AlipayTradePrecreteResResult implements Serializable {

    private Alipay_trade_precreate_response alipay_trade_precreate_response;
    private String sign;

    @Data
    public static class Alipay_trade_precreate_response {
        private String code;
        private String msg;
        private String out_trade_no;
        private String qr_code;
    }
}
