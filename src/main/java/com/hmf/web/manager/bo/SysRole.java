package com.hmf.web.manager.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysRole implements Serializable {
    private Integer id;
    private String roleName;
    private String roleCode;
}
