package com.hmf.web.utils.enums;


import com.hmf.web.config.BaseCodeEnum;
import lombok.Getter;

/**
 * 是否会员 1:是
 */
public enum IsVipEnum implements BaseCodeEnum {
    NO(0,"否"),
    YES(1,"是");
    @Getter
    private int code;
    @Getter
    private String message;

    IsVipEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
