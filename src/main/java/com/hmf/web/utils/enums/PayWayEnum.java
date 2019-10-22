package com.hmf.web.utils.enums;

import com.hmf.web.config.BaseCodeEnum;
import lombok.Getter;

public enum PayWayEnum implements BaseCodeEnum {
    WECHAT(1,"微信支付"),
    ALIPAY(2,"支付宝"),
    BALANCE_PAY(3,"余额支付"),
    ZERO_PAY(4,"无需支付"),
    OFFLINE_PAYMENT(5,"线下支付"),
     ;
    @Getter
    private int code;
    @Getter
    private String message;

    PayWayEnum(int code, String message) { this.code = code; this.message = message; }



    public static PayWayEnum get(int code) {
        String value = String.valueOf(code);
        return get(value);
    }
    public static PayWayEnum get(String code) {
        for (PayWayEnum e : values()) {
            if(String.valueOf(e.getCode()).equals(code)) {
                return e;
            }
        }
        return null;
    }
}
