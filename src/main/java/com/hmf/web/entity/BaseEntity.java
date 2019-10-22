package com.hmf.web.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {
    //创建时间
    private Date createTime;
    // 修改时间
    private Date updateTime;

}
