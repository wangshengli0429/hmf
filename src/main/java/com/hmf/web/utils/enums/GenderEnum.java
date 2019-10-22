package com.hmf.web.utils.enums;

import com.hmf.web.config.BaseCodeEnum;
import lombok.Getter;

public enum GenderEnum implements BaseCodeEnum {
    MEN(0,"男"),
    WOMEN(1,"女"),
    QT(2,"未知"),;
    @Getter
    private int code;
    @Getter
    private String message;

    GenderEnum(int code,String message) { this.code = code; this.message = message; }

    public static GenderEnum get(int code) {
        String value = String.valueOf(code);
        return get(value);
    }

    public static GenderEnum get(String code) {
        for (GenderEnum e : values()) {
            if(String.valueOf(e.getCode()).equals(code)) {
                return e;
            }
        }
        return null;
    }
}
