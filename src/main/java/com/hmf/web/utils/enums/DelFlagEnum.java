package com.hmf.web.utils.enums;

import com.hmf.web.config.BaseCodeEnum;
import lombok.Getter;

/**
 * 删除标识 1：是 0：否
 */
public enum DelFlagEnum implements BaseCodeEnum {
    YES(1,"是"),
    NO(0,"否");
    @Getter
    private int code;
    @Getter
    private String message;

    DelFlagEnum(int code,String message) { this.code = code; this.message = message; }
}
