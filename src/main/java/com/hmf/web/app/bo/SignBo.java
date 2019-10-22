package com.hmf.web.app.bo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @ClassName: loginBo
 * @Description: ${description}
 * @Version: 1.0
 */
@Data
public class SignBo implements Serializable{
    private static final long serialVersionUID = 1L;
    //公共参数
    private String appid;//系统分配
    private String timestamp;//时间戳，10位。系统允许时间戳有5分钟的误差。
    private String signature;//签名
    private String nonce;//随机字符
    //请求参数
    private String username;
    private String password;
    private BigInteger lastIp;
}
