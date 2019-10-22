package com.hmf.web.entity;

import com.hmf.web.utils.enums.IsVipEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户实体
 */
@Data
public class LoginResultEntity implements Serializable {
    //客户Id
    private  Integer id;
    //公司名称
    private  Integer companyId;
    //客户名称
    private String userName;

    private String password;

    private String salt;
    //公司名称
    private String companyName;
    //联系人姓名
    private String contactName;
    //手机
    private String phone;
    // 是否会员
    private IsVipEnum isVip;
    //VIP 到期时间
    private Date vipExpire;
    //公司会员号
    private String memberNum;

    //最后一次登录时间
    private String lastLoginTime;
    //失败次数
    private Integer failNum;
    //查询数量
    private int num;

}
