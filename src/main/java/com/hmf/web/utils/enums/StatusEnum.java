package com.hmf.web.utils.enums;

import com.hmf.web.config.BaseCodeEnum;
import lombok.Getter;

public enum StatusEnum implements BaseCodeEnum {
    ENABLE(0,"启用"),
    DISABLE(1,"禁用");
    @Getter
    private int code;
    @Getter
    private String message;

    StatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
