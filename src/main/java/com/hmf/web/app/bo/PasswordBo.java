package com.hmf.web.app.bo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @ClassName: PasswordBo
 * @Version: 1.0
 */
@Data
public class PasswordBo implements Serializable {
    private static final long serialVersionUID = 1L;
    //请求参数
    //当前密码
    private String     currentpwd;
    //新密码
    private String     newpassword;

    private Integer userId;
}
