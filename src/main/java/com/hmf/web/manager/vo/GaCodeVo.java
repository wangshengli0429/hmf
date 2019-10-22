package com.hmf.web.manager.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName: GaCodeVo
 * @Version: 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GaCodeVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer           id;
    private String            gacode;
    private String            gaurl;
    private String            realname;
    private String            username;
    private String            password;
    private String            salt;
    private String            phone;
    private String            weChat;
    private String            email;
    private String            icon;
    private String            token;
    private Boolean           isSeller;
    private Boolean           isEnger;
    private Map<String,String> permissions;
}