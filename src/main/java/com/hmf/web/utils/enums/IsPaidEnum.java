package com.hmf.web.utils.enums;

import com.hmf.web.config.BaseCodeEnum;
import lombok.Getter;

public enum IsPaidEnum  implements BaseCodeEnum {
    YES(1,"是"),
    NO(0,"否");
    @Getter
    private int code;
    @Getter
    private String message;

    IsPaidEnum(int code,String message) { this.code = code; this.message = message; }
}
