package com.hmf.web.utils.enums;

import com.hmf.web.config.BaseCodeEnum;
import lombok.Getter;

public enum PayTypeEnum implements BaseCodeEnum {
    ORDER_ALIPAY(1,"订单支付"),
    REPAIR_ALIPAY(2,"报修支付"),
    OFFLINE_PAYMENT(5,"线下支付"),
    ;
    @Getter
    private int code;
    @Getter
    private String message;

    PayTypeEnum(int code, String message) { this.code = code; this.message = message; }

    public static PayTypeEnum get(int code) {
        String value = String.valueOf(code);
        return get(value);
    }
    public static PayTypeEnum get(String code) {
        for (PayTypeEnum e : values()) {
            if(String.valueOf(e.getCode()).equals(code)) {
                return e;
            }
        }
        return null;
    }
}
