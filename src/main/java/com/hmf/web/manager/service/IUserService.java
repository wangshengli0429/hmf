package com.hmf.web.manager.service;

import com.hmf.web.entity.TUser;

public interface IUserService {
    boolean insertSelective(TUser record);
}
