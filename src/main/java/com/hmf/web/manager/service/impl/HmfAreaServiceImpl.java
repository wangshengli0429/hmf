package com.hmf.web.manager.service.impl;

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

@Service
@Transactional
public class HmfAreaServiceImpl implements HmfAreaService {
    private static Logger logger = LoggerFactory.getLogger(HmfAreaServiceImpl.class);
    @Autowired
    private THmfAreaDao hmfAreaDao;

    @Override
    public ApiResult deleteByPrimaryKey(Integer id) {
        ApiResult apiResult = new ApiResult();
        logger.info("衡美肤删除操作id:{0}",id);
        if(id == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }

        boolean result = hmfAreaDao.deleteByPrimaryKey(id) > 0;
        if(result){
            apiResult.setStatus(HttpCodeEnum.DEL_CODE_200.getCode());
            apiResult.setMessage(HttpCodeEnum.DEL_CODE_200.getMessage());
            return apiResult;
        }
        apiResult.setStatus(HttpCodeEnum.DEL_CODE_500.getCode());
        apiResult.setMessage(HttpCodeEnum.DEL_CODE_500.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult insert(THmfArea record) {
        ApiResult apiResult = new ApiResult();
        logger.info("衡美肤新增操作THmfArea:{0}",record);
        if(record == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }
        boolean result = hmfAreaDao.insert(record) > 0;
        if(result){
            apiResult.setStatus(HttpCodeEnum.ADD_CODE_200.getCode());
            apiResult.setMessage(HttpCodeEnum.ADD_CODE_200.getMessage());
            return apiResult;
        }
        apiResult.setStatus(HttpCodeEnum.ADD_CODE_500.getCode());
        apiResult.setMessage(HttpCodeEnum.ADD_CODE_500.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult insertSelective(THmfArea record) {
        ApiResult apiResult = new ApiResult();
        logger.info("衡美肤新增操作THmfArea:{0}",record);
        if(record == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }
        boolean result = hmfAreaDao.insert(record) > 0;
        if(result){
            apiResult.setStatus(HttpCodeEnum.ADD_CODE_200.getCode());
            apiResult.setMessage(HttpCodeEnum.ADD_CODE_200.getMessage());
            return apiResult;
        }
        apiResult.setStatus(HttpCodeEnum.ADD_CODE_500.getCode());
        apiResult.setMessage(HttpCodeEnum.ADD_CODE_500.getMessage());
        return apiResult;
    }

    @Override
    public ApiResult selectByPrimaryKey(Integer id) {
        ApiResult apiResult = new ApiResult();
        logger.info("衡美肤删除操作id:{0}",id);
        if(id == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }

        THmfArea hmfArea = hmfAreaDao.selectByPrimaryKey(id);
        apiResult.setStatus(HttpCodeEnum.QUERY_CODE_200.getCode());
        apiResult.setMessage(HttpCodeEnum.QUERY_CODE_200.getMessage());
        apiResult.setData(hmfArea);
        return apiResult;
    }

    @Override
    public ApiResult updateByPrimaryKeySelective(THmfArea record) {
        return null;
    }

    @Override
    public ApiResult updateByPrimaryKey(THmfArea record) {
        return null;
    }
}
