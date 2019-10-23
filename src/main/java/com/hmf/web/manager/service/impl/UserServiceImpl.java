package com.hmf.web.manager.service.impl;

import com.hmf.web.entity.TUser;
import com.hmf.web.manager.bo.LoginBo;
import com.hmf.web.manager.bo.SysRole;
import com.hmf.web.manager.bo.UserInfo;
import com.hmf.web.manager.dao.TRoleDao;
import com.hmf.web.manager.dao.TUserDao;
import com.hmf.web.manager.dao.TUserRoleDao;
import com.hmf.web.manager.service.UserService;
import com.hmf.web.utils.ApiResult;
import com.hmf.web.utils.StringUtil;
import com.hmf.web.utils.enums.HttpStateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
    private TUserDao userDao;

    @Autowired
    private TRoleDao roleDao;

    @Autowired
    private TUserRoleDao userRoleDao;


    @Override
    public ApiResult login(LoginBo loginBo, HttpServletRequest request) {
        try {
            ApiResult apiResult = checkLoginBo(loginBo);
            if(apiResult != null){
                return apiResult;
            }
            //查询用户信息
//            userDao.selectByPrimaryKey()
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResult(HttpStateEnum.SELECT_EXCEP.getIndex(), HttpStateEnum.SELECT_EXCEP.getName());
        }

    }

    @Override
    public UserInfo findByUsername(String userName) {
        TUser user = userDao.findByUsername(userName);
        UserInfo userInfo = null;
        if(user != null) {
            userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUserName());
            userInfo.setPassword(user.getUserPwd());
            userInfo.setSalt(user.getSalt());
            List<SysRole> roleList = userRoleDao.selectUserRoleByUserId(user.getId());
            userInfo.setRoleList(roleList);
        }

        return userInfo;
    }

    //实体参数防空校验
    private ApiResult checkLoginBo(LoginBo loginBo){
        if(null == loginBo){
            return new ApiResult(HttpStateEnum.PARAM_ERROR.getIndex(), HttpStateEnum.PARAM_ERROR.getName());
        }
        boolean result = StringUtil.isEmpty(loginBo.getUsername())?true:false || StringUtil.isEmpty(loginBo.getPassword())?true:false;
        if(result){
            return new ApiResult(HttpStateEnum.PARAM_ERROR.getIndex(), HttpStateEnum.PARAM_ERROR.getName());
        }
        return null;
    }
}
