package com.hmf.web.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hmf.web.entity.THmfArea;
import com.hmf.web.manager.dao.THmfAreaDao;
import com.hmf.web.manager.service.HmfAreaService;
import com.hmf.web.utils.ApiResult;
import com.hmf.web.utils.enums.HttpCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

import static org.slf4j.LoggerFactory.*;

@Service
@Transactional
public class HmfAreaServiceImpl implements HmfAreaService {
    private static Logger logger = getLogger(HmfAreaServiceImpl.class);

    @Autowired
    private THmfAreaDao hmfAreaDao;
    @Override
    public ApiResult deleteByPrimaryKey(Integer id) {
        ApiResult apiResult = new ApiResult();
        logger.info("衡美肤区域删除操作id:{0}",id);
        if(id == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }
        try {
            boolean result = hmfAreaDao.deleteByPrimaryKey(id) > 0;
            if(result){
                apiResult.setStatus(HttpCodeEnum.DEL_CODE_200.getCode());
                apiResult.setMessage(HttpCodeEnum.DEL_CODE_200.getMessage());
                return apiResult;
            }
            apiResult.setStatus(HttpCodeEnum.DEL_FAIL_CODE_200.getCode());
            apiResult.setMessage(HttpCodeEnum.DEL_FAIL_CODE_200.getMessage());
            return apiResult;
        }catch (Exception e){
            logger.info("衡美肤区域删除操作异常:{0}",e);
            apiResult.setStatus(HttpCodeEnum.DEL_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.DEL_CODE_500.getMessage());
            return apiResult;
        }

    }

    @Override
    public ApiResult selectHmfAreaList(String areaName) {
        ApiResult apiResult = new ApiResult();
        try {
            List<THmfArea> areaList = hmfAreaDao.selectHmfAreaList(areaName);
            PageInfo<THmfArea> pageInfo = new PageInfo<>(areaList);
            apiResult.setStatus(HttpCodeEnum.QUERY_CODE_200.getCode());
            apiResult.setMessage(HttpCodeEnum.QUERY_CODE_200.getMessage());
            apiResult.setData(pageInfo);
        }catch (Exception e){
            logger.info("查询区域异常：{0}",e);
            apiResult.setStatus(HttpCodeEnum.EDIT_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.EDIT_CODE_500.getMessage());
            return apiResult;
        }
        apiResult.setStatus(HttpCodeEnum.QUERY_FAIL_CODE_200.getCode());
        apiResult.setMessage(HttpCodeEnum.QUERY_FAIL_CODE_200.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult insert(THmfArea record) {
        ApiResult apiResult = new ApiResult();
        logger.info("衡美肤区域新增操作THmfArea:{0}",record);
        if(record == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }
        try{
            boolean result = hmfAreaDao.insert(record) > 0;
            if(result){
                apiResult.setStatus(HttpCodeEnum.ADD_CODE_200.getCode());
                apiResult.setMessage(HttpCodeEnum.ADD_CODE_200.getMessage());
                return apiResult;
            }
        }catch (Exception e){
            logger.info("新增区域异常：{0}",e);
            apiResult.setStatus(HttpCodeEnum.ADD_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.ADD_CODE_500.getMessage());
            return apiResult;
        }
        apiResult.setStatus(HttpCodeEnum.ADD_FAIL_CODE_200.getCode());
        apiResult.setMessage(HttpCodeEnum.ADD_FAIL_CODE_200.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult insertSelective(THmfArea record) {
        ApiResult apiResult = new ApiResult();
        logger.info("衡美肤区域新增操作THmfArea:{0}",record);
        if(record == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }
        try{
            boolean result = hmfAreaDao.insert(record) > 0;
            if(result){
                apiResult.setStatus(HttpCodeEnum.ADD_CODE_200.getCode());
                apiResult.setMessage(HttpCodeEnum.ADD_CODE_200.getMessage());
                return apiResult;
            }
        }catch (Exception e){
            logger.info("新增衡美肤区域异常：{0}",e);
            apiResult.setStatus(HttpCodeEnum.ADD_FAIL_CODE_200.getCode());
            apiResult.setMessage(HttpCodeEnum.ADD_FAIL_CODE_200.getMessage());
            return apiResult;
        }
        apiResult.setStatus(HttpCodeEnum.ADD_CODE_500.getCode());
        apiResult.setMessage(HttpCodeEnum.ADD_CODE_500.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult selectByPrimaryKey(Integer id) {
        ApiResult apiResult = new ApiResult();
        logger.info("衡美肤区域删除操作id:{0}",id);
        if(id == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }
        try {
            THmfArea hmfArea = hmfAreaDao.selectByPrimaryKey(id);
            apiResult.setStatus(HttpCodeEnum.QUERY_CODE_200.getCode());
            apiResult.setMessage(HttpCodeEnum.QUERY_CODE_200.getMessage());
            apiResult.setData(hmfArea);
            return apiResult;
        }catch (Exception e){
            logger.info("查询区域异常：{0}",e);
            apiResult.setStatus(HttpCodeEnum.QUERY_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.QUERY_CODE_500.getMessage());
            return apiResult;
        }
    }

    @Override
    public ApiResult updateByPrimaryKeySelective(THmfArea record) {
        ApiResult apiResult = new ApiResult();
        logger.info("衡美肤区域修改操作id:{0}",record);
        if(record == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }
        try {
            boolean result = hmfAreaDao.updateByPrimaryKeySelective(record) > 0;
            if (result) {
                apiResult.setStatus(HttpCodeEnum.EDIT_CODE_200.getCode());
                apiResult.setMessage(HttpCodeEnum.EDIT_CODE_200.getMessage());
                return apiResult;
            }
        }catch (Exception e){
            logger.info("衡美肤区域修改异常:{0}",e);
            apiResult.setStatus(HttpCodeEnum.EDIT_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.EDIT_CODE_500.getMessage());
            return apiResult;
        }
        apiResult.setStatus(HttpCodeEnum.EDIT_FAIL_CODE_200.getCode());
        apiResult.setMessage(HttpCodeEnum.EDIT_FAIL_CODE_200.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult updateByPrimaryKey(THmfArea record) {
        ApiResult apiResult = new ApiResult();
        logger.info("衡美肤区域修改操作id:{0}",record);
        if(record == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }
        try {
            boolean result = hmfAreaDao.updateByPrimaryKeySelective(record) > 0;
            if (result) {
                apiResult.setStatus(HttpCodeEnum.EDIT_CODE_200.getCode());
                apiResult.setMessage(HttpCodeEnum.EDIT_CODE_200.getMessage());
                return apiResult;
            }
        }catch (Exception e){
            logger.info("衡美肤区域修改异常:{0}",e);
            apiResult.setStatus(HttpCodeEnum.EDIT_FAIL_CODE_200.getCode());
            apiResult.setMessage(HttpCodeEnum.EDIT_FAIL_CODE_200.getMessage());
            return apiResult;
        }
        apiResult.setStatus(HttpCodeEnum.EDIT_CODE_500.getCode());
        apiResult.setMessage(HttpCodeEnum.EDIT_CODE_500.getMessage());
        return apiResult;
    }
}
