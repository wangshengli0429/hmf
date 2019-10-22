package com.hmf.web.app.service;

import com.hmf.web.app.bo.PasswordBo;
import com.hmf.web.app.bo.SignBo;
import com.hmf.web.app.bo.TokenBo;
import com.hmf.web.entity.PeApps;
import com.hmf.web.utils.ApiResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AppSignService {
    ApiResult signIn(SignBo signBo, HttpServletRequest request);


    PeApps selectByAppId(Integer appid);
    ApiResult checkToken(TokenBo tokenBo, HttpServletRequest request, HttpServletResponse response);

//    ApiResult uplocation(PeUserOnlie peUserOnlie, HttpServletRequest request, HttpServletResponse response);

    ApiResult modifypassword(PasswordBo passwordBo, HttpServletRequest request, HttpServletResponse response);
}
