package com.hmf.web.utils.enums;

import com.hmf.web.config.BaseCodeEnum;
import lombok.Getter;

/**
 * @ClassName: isMainOrderEnum
 * @Description: ${description}
 * @Version: 1.0
 */
public enum isMainOrderEnum implements BaseCodeEnum {
    YES(1,"是"),
    NO(0,"否");
    @Getter
    private int code;
    @Getter
    private String message;

    isMainOrderEnum(int code, String message) { this.code = code; this.message = message; }
}
