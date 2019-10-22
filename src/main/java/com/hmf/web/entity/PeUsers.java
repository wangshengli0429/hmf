package com.hmf.web.entity;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Data
public class PeUsers {
    private Integer id;

    private String realname;

    private String username;

    private String password;

    private String salt;

    private String gacode;

    private String weChat;

    private String phone;

    private String email;

    private String icon;

    private Byte status;

    private Date creatTime;

    private Date updateTime;

    private Date deleteTime;

    private BigInteger lastIp;

    private Date lastLoginTime;

    private List<Integer> roleids;

}