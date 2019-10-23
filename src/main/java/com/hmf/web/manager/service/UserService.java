package com.hmf.web.manager.service;

import com.hmf.web.entity.PeUsers;
import com.hmf.web.manager.bo.UserInfo;
import com.hmf.web.utils.ApiResult;
import com.hmf.web.manager.bo.LoginBo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: UserService
 * @Version: 1.0
 */
public interface UserService {



    ApiResult login(LoginBo loginBo, HttpServletRequest request);

    UserInfo findByUsername(String userName);
}
