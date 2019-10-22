package com.hmf.web.app.controller;

import com.hmf.web.app.bo.PasswordBo;
import com.hmf.web.app.bo.SignBo;
import com.hmf.web.app.bo.TokenBo;
import com.hmf.web.app.service.AppSignService;
import com.hmf.web.config.SignatureAnnotation;
import com.hmf.web.utils.ApiResult;
import com.hmf.web.utils.enums.HttpStateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/app/sign")
public class AppSignController {
    private static Logger logger = LoggerFactory.getLogger(AppSignController.class);
    @Autowired
    private AppSignService appSignService;
    //app登录
    @PostMapping("/in")
    @SignatureAnnotation
    public ApiResult login(HttpServletRequest request, HttpServletResponse response, SignBo signBo){
        logger.info("method login param signBo:" + signBo.toString());
        try {
            return appSignService.signIn(signBo,request);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResult(HttpStateEnum.SELECT_EXCEP.getIndex(), HttpStateEnum.SELECT_EXCEP.getName());
        }
    }
    //检查token
    @PostMapping("/checkToken")
    @SignatureAnnotation
    public ApiResult checkToken(HttpServletRequest request, HttpServletResponse response, TokenBo tokenBo){
        logger.info("method checkToken param checkToken:" + tokenBo.toString());
        return appSignService.checkToken(tokenBo,request,response);
    }
//    //上报定位坐标信息
//    @PostMapping("/uplocation")
//    @SignatureAnnotation
//    public ApiResult uplocation(HttpServletRequest request, HttpServletResponse response, PeUserOnlie peUserOnlie){
//        logger.info("method uplocation param peUserOnlie:" + peUserOnlie.toString());
//        try {
//            return appSignService.uplocation(peUserOnlie,request,response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            ApiResult apiResult=new ApiResult(INSERET_EXCEP.getIndex(),INSERET_EXCEP.getName());
//            return apiResult;
//        }
//    }
    //修改密码
    @PostMapping("/modifypassword")
    @SignatureAnnotation
    public ApiResult modifypassword(HttpServletRequest request, HttpServletResponse response, PasswordBo passwordBo){
        logger.info("method modifypassword param passwordBo:" + passwordBo.toString());
            try {
            return appSignService.modifypassword(passwordBo,request,response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("更新用户异常！");
            return new ApiResult(HttpStateEnum.UPDATE_EXCEP.getIndex(), HttpStateEnum.UPDATE_EXCEP.getName());
        }
    }
}
