package com.hmf.web.manager.service.impl;

import com.hmf.web.entity.THmfShop;
import com.hmf.web.manager.dao.THmfShopDao;
import com.hmf.web.manager.service.HmfShopService;
import com.hmf.web.utils.ApiResult;
import com.hmf.web.utils.enums.HttpCodeEnum;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@Transactional
public class HmfShopServiceImpl implements HmfShopService {
    private static Logger logger = getLogger(HmfShopServiceImpl.class);

    @Autowired
    private THmfShopDao shopDao;


    @Override
    public ApiResult deleteByPrimaryKey(Integer id) {
        logger.info("删除门店操作id：{0}",id);
        ApiResult apiResult = new ApiResult();
        if(id == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }
        try{
            boolean result = shopDao.deleteByPrimaryKey(id) > 0;
            if(result){
                apiResult.setStatus(HttpCodeEnum.DEL_CODE_200.getCode());
                apiResult.setMessage(HttpCodeEnum.DEL_CODE_200.getMessage());
                return apiResult;
            }
            apiResult.setStatus(HttpCodeEnum.DEL_FAIL_CODE_200.getCode());
            apiResult.setMessage(HttpCodeEnum.DEL_FAIL_CODE_200.getMessage());
            return apiResult;
        }catch (Exception e){
            logger.info("衡美肤门店删除操作异常:{0}",e);
            apiResult.setStatus(HttpCodeEnum.DEL_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.DEL_CODE_500.getMessage());
            return apiResult;
        }
    }

    @Override
    public ApiResult insert(THmfShop record) {
        logger.info("新增门店操作：{0}",record);
        ApiResult apiResult = new ApiResult();
        if(record == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }
        try {
            boolean result = shopDao.insert(record) > 0;
            if (result) {
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
    public ApiResult selectByPrimaryKey(Integer id) {
        logger.info("查询门店操作id：{0}",id);
        ApiResult apiResult = new ApiResult();
        if(id == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }
        try {
            THmfShop shop = shopDao.selectByPrimaryKey(id);
            apiResult.setStatus(HttpCodeEnum.QUERY_CODE_200.getCode());
            apiResult.setMessage(HttpCodeEnum.QUERY_CODE_200.getMessage());
            apiResult.setData(shop);
            return apiResult;
        }catch (Exception e){
            logger.info("查询门店异常：{0}",e);
            apiResult.setStatus(HttpCodeEnum.QUERY_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.QUERY_CODE_500.getMessage());
            return apiResult;
        }
    }

    @Override
    public ApiResult updateByPrimaryKey(THmfShop record) {
        logger.info("修改门店操作record：{0}",record);
        ApiResult apiResult = new ApiResult();
        if(record == null){
            apiResult.setStatus(HttpCodeEnum.PARAM_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.PARAM_CODE_500.getMessage());
            return apiResult;
        }
        try {
            boolean result = shopDao.updateByPrimaryKey(record) > 0;
            if(result) {
                apiResult.setStatus(HttpCodeEnum.EDIT_CODE_200.getCode());
                apiResult.setMessage(HttpCodeEnum.EDIT_CODE_200.getMessage());
                return apiResult;
            }
        }catch (Exception e){
            logger.info("衡美肤门店修改异常:{0}",e);
            apiResult.setStatus(HttpCodeEnum.EDIT_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.EDIT_CODE_500.getMessage());
            return apiResult;
        }
        apiResult.setStatus(HttpCodeEnum.EDIT_FAIL_CODE_200.getCode());
        apiResult.setMessage(HttpCodeEnum.EDIT_FAIL_CODE_200.getMessage());
        return apiResult;
    }
}
