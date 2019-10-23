package com.hmf.web.manager.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserInfo implements Serializable {
    private Integer id;
    private String salt;
    private String username;
    private String password;

    private List<SysRole> roleList;
}
