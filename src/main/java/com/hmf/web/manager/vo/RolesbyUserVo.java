package com.hmf.web.manager.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: RolesbyUserVo
 * @Description: ${description}
 * @Version: 1.0
 */
@Data
public class RolesbyUserVo implements Serializable{
    private Integer id;
    private Integer userId;
    private Integer prid;
    private String rolename;
}
