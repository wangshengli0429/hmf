package com.hmf.web.utils;

import com.hmf.web.config.BaseCodeEnum;
import lombok.Getter;


/**
 * 服务单处理报修工单标识
 */
public enum SalveFlagEnum implements BaseCodeEnum {
    CLOSE(10,"关单"),
    SEND(20,"寄修");
    @Getter
    private int code;
    @Getter
    private String message;

    SalveFlagEnum(int code, String message) { this.code = code; this.message = message; }

    public static SalveFlagEnum get(int code) {
        String value = String.valueOf(code);
        return get(value);
    }

    public static SalveFlagEnum get(String code) {
        for (SalveFlagEnum e : values()) {
            if(String.valueOf(e.getCode()).equals(code)) {
                return e;
            }
        }
        return null;
    }
}
