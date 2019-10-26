package com.hmf.web.manager.service.impl;

import com.hmf.web.entity.TRole;
import com.hmf.web.manager.dao.TRoleDao;
import com.hmf.web.manager.service.RoleService;
import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private TRoleDao roleDao;

    @Override
    public Boolean addRole(TRole role) {

        role.setCreateTime(DateUtil.formatDate(new Date()));
       boolean result = roleDao.insert(role) > 0;
        return result;
    }
}
