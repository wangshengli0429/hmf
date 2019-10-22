package com.hmf.web.app.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: RolesUserVo
 * @Version: 1.0
 */
@Data
public class RolesUserVo implements Serializable{
    private static final long serialVersionUID = 1L;
    private Integer roleid;
    private String rolename;
}
