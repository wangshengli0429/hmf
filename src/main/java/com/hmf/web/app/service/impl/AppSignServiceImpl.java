package com.hmf.web.app.service.impl;

import com.hmf.web.app.bo.PasswordBo;
import com.hmf.web.app.bo.SignBo;
import com.hmf.web.app.bo.TokenBo;
import com.hmf.web.app.dao.PeAppsDao;
import com.hmf.web.manager.dao.PeUsersDao;
import com.hmf.web.manager.vo.GaCodeVo;
import com.hmf.web.entity.PeApps;
import com.hmf.web.entity.PeUsers;
import com.hmf.web.utils.ApiResult;
import com.hmf.web.utils.JavaWebTokenUtil;
import com.hmf.web.utils.MD5Util;
import com.hmf.web.utils.enums.HttpCodeEnum;
import com.hmf.web.utils.enums.HttpStateEnum;
import com.hmf.web.app.service.AppSignService;
import com.hmf.web.utils.StringUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@Transactional
public class AppSignServiceImpl implements AppSignService {
    private static Logger logger = LoggerFactory.getLogger(AppSignServiceImpl.class);
    @Autowired
    private PeUsersDao peUsersDao;
    @Autowired
    private PeAppsDao peAppsDao;
//    @Autowired
//    private PeUserOnlieDao      peUserOnlieDao;

    /**
     * @return a
     * @Description app登录
     * @Param
     **/
    @Override
    public ApiResult signIn(SignBo signBo, HttpServletRequest request) {
//            checkParamSign(signBo);
        ApiResult apiResult = checkSignBo(signBo);
        if (apiResult != null) {
            return apiResult;
        }
        //查询用户信息
        GaCodeVo gaCodeVo = peUsersDao.selectGacodeByName(signBo.getUsername());
        if (null != gaCodeVo && null != gaCodeVo.getId()) {
//            List<RolesbyUserVo> rolesbyUserVos = peRolesDao.selectByUserid(gaCodeVo.getId());
//            Set<Integer> rolesSet = new HashSet<>();
//            for (RolesbyUserVo rolesbyUserVo : rolesbyUserVos) {
//                rolesSet.add(rolesbyUserVo.getId());
//            }
            //15销售8-工程师
//            boolean containsSeller = rolesSet.contains(15);
//            gaCodeVo.setIsSeller(containsSeller);
//            boolean containsEnger = rolesSet.contains(8);
//            gaCodeVo.setIsEnger(containsEnger);
//            if(containsSeller==false&&containsEnger==false){
//                return new ApiResult(CRM_LOGIN_ERROR_3.getIndex(),CRM_LOGIN_ERROR_3.getName());
//            }
            if (gaCodeVo.getPassword().equals(MD5Util.digest(gaCodeVo.getSalt() + signBo.getPassword() + gaCodeVo.getSalt()))) {
                //清空加密信息
                gaCodeVo.setSalt("");
                gaCodeVo.setPassword("");
                request.getSession().setAttribute("currentUser", gaCodeVo);
                gaCodeVo.setGacode("");
                //生成token并返回
                String token = JavaWebTokenUtil.createJWT(gaCodeVo.getId() + "", "allits.com.cn", gaCodeVo.getUsername(), 1000 * 60 * 60 * 24 * 5 + 60000);
                gaCodeVo.setToken(token);
                ApiResult result = new ApiResult(HttpCodeEnum.CODE_0000.getCode(), HttpCodeEnum.CODE_0000.getMessage());
                result.setData(gaCodeVo);
                return result;
            } else {
                //密码不对
                return new ApiResult(HttpStateEnum.LOGIN_ERROR_9.getIndex(), HttpStateEnum.LOGIN_ERROR_9.getName());
            }
        } else {
            return new ApiResult(HttpStateEnum.LOGIN_ERROR_3.getIndex(), HttpStateEnum.LOGIN_ERROR_3.getName());
        }
    }

    @Override
    public PeApps selectByAppId(Integer appid) {
        return peAppsDao.selectByAppId(appid);
    }


    @Override
    public ApiResult checkToken(TokenBo tokenBo, HttpServletRequest request, HttpServletResponse response) {
        ApiResult apiResult = checkTokenBo(tokenBo);
        if (apiResult != null) {
            return apiResult;
        }
        String exception = JavaWebTokenUtil.parseJWT(tokenBo.getToken()).toString();
        if (exception != null) {
            if (ExpiredJwtException.class.getName().equals(exception)) {
                logger.error("token已过期！");
                return new ApiResult(HttpStateEnum.TOKEN_ERROR_1.getIndex(), HttpStateEnum.TOKEN_ERROR_1.getName());
            } else if (SignatureException.class.getName().equals(exception)) {
                logger.error("token sign解析失败");
                return new ApiResult(HttpStateEnum.TOKEN_ERROR_2.getIndex(), HttpStateEnum.TOKEN_ERROR_2.getName());
            } else if (MalformedJwtException.class.getName().equals(exception)) {
                logger.error("token的head解析失败");
                return new ApiResult(HttpStateEnum.TOKEN_ERROR_3.getIndex(), HttpStateEnum.TOKEN_ERROR_3.getName());
            } else if ("success".equals(exception)) {
                logger.error("用户授权认证通过!");
                return new ApiResult(HttpCodeEnum.CODE_0000.getCode(), HttpCodeEnum.CODE_0000.getMessage());
            } else {
                logger.error("token的有效期小与2天！");
                PeUsers peUsers = peUsersDao.selectByPrimaryKey(tokenBo.getUserId());
                if(null==peUsers){
                    return new ApiResult(HttpStateEnum.TOKEN_ERROR_6.getIndex(), HttpStateEnum.TOKEN_ERROR_6.getName());
                }
                String userId = tokenBo.getUserId().toString();
                String userName = peUsers.getUsername();
                String newToken = JavaWebTokenUtil.createJWT(userId, "allits.com.cn", userName, 1000 * 60 * 60 * 24 * 2 + 60000);
                logger.info("已生成新的token："+newToken);
                JSONObject json = new JSONObject();
                json.put("token",newToken);
                ApiResult result = new ApiResult(HttpStateEnum.TOKEN_STATE_4.getIndex(), HttpStateEnum.TOKEN_STATE_4.getName());
                result.setData(json);
                return result;
            }
        }
        return new ApiResult(HttpStateEnum.TOKEN_ERROR_5.getIndex(), HttpStateEnum.TOKEN_ERROR_5.getName());
    }

//    /**
//     * @return a
//     * @Description 上报定位坐标信息
//    * @Param
//     **/
//    @Override
//    public ApiResult uplocation(PeUserOnlie peUserOnlie, HttpServletRequest request, HttpServletResponse response) {
//        ApiResult apiResult = checkPeUserOnlie(peUserOnlie);
//        if(apiResult != null){
//            return apiResult;
//        }
//        int insertSelective = peUserOnlieDao.insertSelective(peUserOnlie);
//        if(insertSelective>0){
//            apiResult=new ApiResult(CODE_0000.getCode(),CODE_0000.getMessage());
//            return apiResult;
//        }
//        apiResult=new ApiResult(INSERET_ERROR.getIndex(),INSERET_ERROR.getName());
//        return apiResult;
//    }

    /**
     * @return a
     * @Description 修改密码
    * @Param
     **/
    @Override
    public ApiResult modifypassword(PasswordBo passwordBo, HttpServletRequest request, HttpServletResponse response) {
        ApiResult result = new ApiResult();
        ApiResult apiResult = checkPasswordBo(passwordBo);
        if(apiResult != null){
            return apiResult;
        }
        //更新密码数据装配
        PeUsers modifypassword = new PeUsers();
        modifypassword.setId(passwordBo.getUserId());
        PeUsers userResult = peUsersDao.selectByPrimaryKey(passwordBo.getUserId());
        if(null!=userResult){
            //获取该用户的盐
            String salt = userResult.getSalt();
            if(MD5Util.digest(salt+passwordBo.getCurrentpwd()+salt).equals(userResult.getPassword())){
                if (!StringUtil.isEmpty(passwordBo.getNewpassword())) {
                    //选择MD5参数加密
                    modifypassword.setPassword(MD5Util.digest(salt+passwordBo.getNewpassword()+salt));
                }
                int i = peUsersDao.updateByPrimaryKeySelective(modifypassword);
                if(i>0){
                    result.setStatus(HttpCodeEnum.CODE_0000.getCode());
                    result.setMessage(HttpCodeEnum.CODE_0000.getMessage());
                }
            }else {
                result.setStatus(HttpStateEnum.LOGIN_ERROR_9.getIndex());
                result.setMessage(HttpStateEnum.LOGIN_ERROR_9.getName());
            }
        }else{
            result.setStatus(HttpStateEnum.LOGIN_ERROR_3.getIndex());
            result.setMessage(HttpStateEnum.LOGIN_ERROR_3.getName());
        }
        return result;
    }
    //实体参数防空校验
    private ApiResult checkPasswordBo(PasswordBo passwordBo){
        if(null == passwordBo){
            return new ApiResult(HttpStateEnum.PARAM_ERROR.getIndex(), HttpStateEnum.PARAM_ERROR.getName());
        }
        boolean result = StringUtil.isEmpty(passwordBo.getCurrentpwd())
                || null==passwordBo.getUserId();
        if(result){
            return new ApiResult(HttpStateEnum.PARAM_ERROR.getIndex(), HttpStateEnum.PARAM_ERROR.getName());
        }
        return null;
    }
    //实体参数防空校验
    private ApiResult checkTokenBo(TokenBo tokenBo){
        if(null == tokenBo){
            return new ApiResult(HttpStateEnum.PARAM_ERROR.getIndex(), HttpStateEnum.PARAM_ERROR.getName());
        }
        boolean result = StringUtil.isEmpty(tokenBo.getToken())
                || null==tokenBo.getUserId();
        if(result){
            return new ApiResult(HttpStateEnum.PARAM_ERROR.getIndex(), HttpStateEnum.PARAM_ERROR.getName());
        }
        return null;
    }
    //实体参数防空校验
    private ApiResult checkSignBo(SignBo signBo){
        if(null == signBo){
            return new ApiResult(HttpStateEnum.PARAM_ERROR.getIndex(), HttpStateEnum.PARAM_ERROR.getName());
        }
        boolean result = StringUtil.isEmpty(signBo.getUsername())
                || StringUtil.isEmpty(signBo.getPassword());
        if(result){
            return new ApiResult(HttpStateEnum.PARAM_ERROR.getIndex(), HttpStateEnum.PARAM_ERROR.getName());
        }
        return null;
    }
}
