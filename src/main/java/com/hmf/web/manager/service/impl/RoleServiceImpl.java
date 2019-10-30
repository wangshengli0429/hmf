package com.hmf.web.manager.service.impl;

import com.hmf.web.entity.TRole;
import com.hmf.web.manager.dao.TRoleDao;
import com.hmf.web.manager.service.RoleService;
import com.hmf.web.utils.ApiResult;
import com.hmf.web.utils.enums.HttpCodeEnum;
import org.apache.commons.httpclient.util.DateUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private static Logger logger = getLogger(RoleServiceImpl.class);
    @Autowired
    private TRoleDao roleDao;

    @Override
    public ApiResult addRole(TRole role) {
        ApiResult apiResult = new ApiResult();
        logger.info("add role:{0}",role);
        if(role == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }
        try {
            role.setCreateTime(DateUtil.formatDate(new Date()));
            boolean result = roleDao.insert(role) > 0;
            if(result){
                apiResult.setStatus(HttpCodeEnum.ADD_CODE_200.getCode());
                apiResult.setMessage(HttpCodeEnum.ADD_CODE_200.getMessage());
                return apiResult;
            }
            apiResult.setStatus(HttpCodeEnum.ADD_FAIL_CODE_200.getCode());
            apiResult.setMessage(HttpCodeEnum.ADD_FAIL_CODE_200.getMessage());
            return apiResult;
        }catch (Exception e){
            logger.info("新增角色异常：{0}",e);
            apiResult.setStatus(HttpCodeEnum.ADD_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.ADD_CODE_500.getMessage());
            return apiResult;
        }

    }

    @Override
    public ApiResult delRole(Integer id) {
        ApiResult apiResult = new ApiResult();
        logger.info("衡美肤区域删除操作id:{0}",id);
        if(id == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }
        try {
            boolean result = roleDao.deleteByPrimaryKey(id) > 0;
            if(result){
                apiResult.setStatus(HttpCodeEnum.DEL_CODE_200.getCode());
                apiResult.setMessage(HttpCodeEnum.DEL_CODE_200.getMessage());
                return apiResult;
            }
            apiResult.setStatus(HttpCodeEnum.DEL_FAIL_CODE_200.getCode());
            apiResult.setMessage(HttpCodeEnum.DEL_FAIL_CODE_200.getMessage());
            return apiResult;
        }catch (Exception e){
            logger.info("衡美肤角色删除操作异常:{0}",e);
            apiResult.setStatus(HttpCodeEnum.DEL_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.DEL_CODE_500.getMessage());
            return apiResult;
        }
    }

    @Override
    public ApiResult getRoleById(Integer id) {
        ApiResult apiResult = new ApiResult();
        logger.info("衡美肤角色删除操作id:{0}",id);
        if(id == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }
        try {
            TRole role = roleDao.selectByPrimaryKey(id);
            apiResult.setStatus(HttpCodeEnum.QUERY_CODE_200.getCode());
            apiResult.setMessage(HttpCodeEnum.QUERY_CODE_200.getMessage());
            apiResult.setData(role);
            return apiResult;
        }catch (Exception e){
            logger.info("查询角色异常：{0}",e);
            apiResult.setStatus(HttpCodeEnum.QUERY_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.QUERY_CODE_500.getMessage());
            return apiResult;
        }
    }

    @Override
    public ApiResult updateRole(TRole record) {
        ApiResult apiResult = new ApiResult();
        logger.info("衡美肤区域修改操作id:{0}",record);
        if(record == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }
        try {
            boolean result = roleDao.updateByPrimaryKey(record) > 0;
            if (result) {
                apiResult.setStatus(HttpCodeEnum.EDIT_CODE_200.getCode());
                apiResult.setMessage(HttpCodeEnum.EDIT_CODE_200.getMessage());
                return apiResult;
            }
            apiResult.setStatus(HttpCodeEnum.EDIT_FAIL_CODE_200.getCode());
            apiResult.setMessage(HttpCodeEnum.EDIT_FAIL_CODE_200.getMessage());
            return apiResult;
        }catch (Exception e){
            logger.info("衡美肤区域修改异常:{0}",e);
            apiResult.setStatus(HttpCodeEnum.EDIT_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.EDIT_CODE_500.getMessage());
            return apiResult;
        }

    }
}
