package com.hmf.web.manager.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRoleBo implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer RoleId;
}
