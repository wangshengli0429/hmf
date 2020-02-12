package com.hmf.web.manager.service.impl;

import com.hmf.web.entity.TUser;
import com.hmf.web.manager.dao.TUserDao;
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

}
