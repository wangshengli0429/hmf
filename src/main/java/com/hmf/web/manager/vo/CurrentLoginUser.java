package com.hmf.web.manager.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 当前登录用户实体信息 在controller方法@CurrentUser CurrentLoginUser user既可以获取crm当前登录用户信息
 */
@Data
public class CurrentLoginUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private     Integer            id;
    private     String             gaurl;
    private     String             realname;
    private     String             username;
    private     String             salt;
    private     String             phone;
    private     String             weChat;
    private     String             email;
    private     String             icon;
    private Map<String,String> permissions;
}
