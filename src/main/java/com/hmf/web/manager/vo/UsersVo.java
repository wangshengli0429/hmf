package com.hmf.web.manager.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: UsersVo
 * @Version: 1.0
 */
@Data
public class UsersVo implements Serializable{
    private Integer id;

    private String realname;

    private String username;

    private Byte status;

    private String lastLoginTime;

    private List<RolesbyUserVo> rolesVos;
}
