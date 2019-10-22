package com.hmf.web.manager.bo;

import lombok.Data;

import java.io.Serializable;


/**
 * @ClassName: OpenBo
 * @Version: 1.0
 */
@Data
public class OpenBo implements Serializable{
    private String code;
    private String echostr;
    private String orderNum;
}
