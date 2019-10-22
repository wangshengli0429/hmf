package com.hmf.web.manager.service.impl;

import com.hmf.web.manager.vo.GaCodeVo;
import com.hmf.web.entity.PeUsers;
import com.hmf.web.utils.enums.HttpStateEnum;
import com.hmf.web.manager.bo.LoginBo;
import com.hmf.web.manager.dao.PeUsersDao;
import com.hmf.web.manager.service.UserService;
import com.hmf.web.utils.*;
import com.hmf.web.utils.enums.HttpCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: UserServiceImpl
 * @Version: 1.0
 */

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PeUsersDao          peUsersDao;

    /**
     * @Description 用户查看二维码
      @Param
     **/
    @Override
    public void QRCode(HttpServletRequest request, HttpServletResponse response, int width, int height, String format, String userName,String realmname) {
        //查询用户信息
        GaCodeVo gaCodeVo = peUsersDao.selectGacodeByName(userName);
        if(null!=gaCodeVo&&null!=gaCodeVo.getId()){
            crtQRCode(gaCodeVo.getGacode(),width,height,format,request,response,userName,realmname);
        }
    }


    @Override
    public ApiResult login(LoginBo loginBo, HttpServletRequest request) {
        try {
            ApiResult apiResult = checkLoginBo(loginBo);
            if(apiResult != null){
                return apiResult;
            }
            //查询用户信息
            GaCodeVo gaCodeVo = peUsersDao.selectGacodeByName(loginBo.getUsername());
            if(null!=gaCodeVo&&null!=gaCodeVo.getId()){

                boolean syslogin = gaCodeVo.getPermissions().containsKey("syslogin");
                logger.info(loginBo.getUsername()+"是否有后端登录权限："+syslogin);
                if(syslogin){
                    if(gaCodeVo.getPassword().equals(MD5Util.digest(gaCodeVo.getSalt()+loginBo.getPassword()+gaCodeVo.getSalt()))){
                        //校验gacode
                        long t = System.currentTimeMillis();
                        GoogleAuthenticator ga = new GoogleAuthenticator();
                        ga.setWindowSize(5); //should give 5 * 30 seconds of grace...
                        boolean resultcheck = ga.check_code(gaCodeVo.getGacode(), loginBo.getGacode(), t);
                        if(resultcheck){
                            //清空加密信息
                            gaCodeVo.setSalt("");
                            gaCodeVo.setPassword("");
                            request.getSession().setAttribute("currentUser",gaCodeVo);
                            //更新登录ip和登录时间
                            PeUsers peUsers = new PeUsers();
                            peUsers.setId(gaCodeVo.getId());
                            peUsers.setLastIp(loginBo.getLastIp());
                            peUsers.setLastLoginTime(new Date());
                            int updateIPAndTime = peUsersDao.updateByPrimaryKeySelective(peUsers);;
                            //生成token并返回
                            String token = JavaWebTokenUtil.createJWT(gaCodeVo.getId() + "", "allits.com.cn", gaCodeVo.getUsername(), 1000 * 60 * 60 * 24 * 5 + 60000);
                            gaCodeVo.setToken(token);
                            ApiResult result = new ApiResult(HttpCodeEnum.CODE_0000.getCode(), HttpCodeEnum.CODE_0000.getMessage());
                            result.setData(gaCodeVo);;
                            return result;
                        }else {
                            ApiResult result = new ApiResult(HttpStateEnum.CRM_LOGIN_ERROR_1.getIndex(), HttpStateEnum.CRM_LOGIN_ERROR_1.getName());
                            return result;
                        }
                    }else{
                        //密码不对
                        return new ApiResult(HttpStateEnum.LOGIN_ERROR_9.getIndex(), HttpStateEnum.LOGIN_ERROR_9.getName());
                    }
                }else {
                    return new ApiResult(HttpStateEnum.CRM_LOGIN_ERROR_2.getIndex(), HttpStateEnum.CRM_LOGIN_ERROR_2.getName());
                }
            }else {
                return new ApiResult(HttpStateEnum.LOGIN_ERROR_3.getIndex(), HttpStateEnum.LOGIN_ERROR_3.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResult(HttpStateEnum.SELECT_EXCEP.getIndex(), HttpStateEnum.SELECT_EXCEP.getName());
        }
    }

    /**
     * @Description 新增用户
    * @Param
     **/
    @Override
    public ApiResult crt_user(PeUsers peUsers) {

        ApiResult result = new ApiResult();
        try {
            if (null!=peUsers&&!"".equals(peUsers)){
                if (!StringUtil.isEmpty(peUsers.getUsername())){
                    //查询用户名
                    PeUsers nameParam = new PeUsers();
                    nameParam.setUsername(peUsers.getUsername().trim());
                    List<PeUsers> nameList = peUsersDao.selectByCondition(nameParam);
                    if (null!=nameList&&0!=nameList.size()){
                        result.setStatus(HttpStateEnum.REGIST_ERROR_8.getIndex());
                        result.setMessage(HttpStateEnum.REGIST_ERROR_8.getName());
                        return result;
                    }
                    //查询手机号
                    if (null!=peUsers.getPhone()){
                        PeUsers phoneParam = new PeUsers();
                        phoneParam.setPhone(peUsers.getPhone().trim());
                        List<PeUsers> phoneList = peUsersDao.selectByCondition(phoneParam);
                        if(null!=phoneList&&0!=phoneList.size()){
                            result.setStatus(HttpStateEnum.REGIST_ERROR_6.getIndex());
                            result.setMessage(HttpStateEnum.REGIST_ERROR_6.getName());
                            return result;
                        }
                    }
                    if(StringUtil.isEmpty(peUsers.getPassword())){
                        //如果没有填入密码则给初始密码
                        peUsers.setPassword("1234567809");
                    }
                    //选择MD5参数加密
                    String salt = MD5Util.digest(OrderUtil.get5UUId());
                    peUsers.setSalt(salt);
                    //生成谷歌身份秘钥
                    String secret = GoogleAuthenticator.generateSecretKey();
                    peUsers.setGacode(secret);
                    peUsers.setPassword(MD5Util.digest(salt+peUsers.getPassword().trim()+salt));
                    byte status=1;
                    peUsers.setRealname(peUsers.getUsername().trim());
                    peUsers.setStatus(status);//填入默认值1-启用 0-禁用
                    int i = peUsersDao.insertSelective(peUsers);
                    if (i>0){

                        result.setStatus(HttpStateEnum.OK.getIndex());
                        result.setMessage(HttpStateEnum.OK.getName());
                    }else{
                        result.setStatus(HttpStateEnum.INSERET_ERROR.getIndex());
                        result.setMessage(HttpStateEnum.INSERET_ERROR.getName());
                    }
                }else {
                    result.setStatus(HttpStateEnum.PARAM_NULL.getIndex());
                    result.setMessage(HttpStateEnum.PARAM_NULL.getName());
                }
            }else{
                result.setStatus(HttpStateEnum.PARAM_NULL.getIndex());
                result.setMessage(HttpStateEnum.PARAM_NULL.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("新增用户插入异常！");
            result.setStatus(HttpStateEnum.INSERET_EXCEP.getIndex());
            result.setMessage(HttpStateEnum.INSERET_EXCEP.getName());
        }
        return result;
    }
    //生成二维码字节流返回
    private void crtQRCode(String secret, int width, int height, String format, HttpServletRequest request, HttpServletResponse response, String userName, String realm){
        try {
            String qrcode = GoogleAuthenticator.getQRBarcode(userName,realm,secret);
            logger.info("二维码地址:"+qrcode);
            QRCodeUtil.generateQRCode(qrcode, width, height, format, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //实体参数防空校验
    private ApiResult checkLoginBo(LoginBo loginBo){
        if(null == loginBo){
            return new ApiResult(HttpStateEnum.PARAM_ERROR.getIndex(), HttpStateEnum.PARAM_ERROR.getName());
        }
        boolean result = StringUtil.isEmpty(loginBo.getUsername())?true:false
                || StringUtil.isEmpty(loginBo.getPassword())?true:false
                ||null==loginBo.getGacode()?true:false;
        if(result){
            return new ApiResult(HttpStateEnum.PARAM_ERROR.getIndex(), HttpStateEnum.PARAM_ERROR.getName());
        }
        return null;
    }
}
