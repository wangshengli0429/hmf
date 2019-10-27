package com.hmf.web.manager.service;

import com.hmf.web.entity.THmfArea;
import com.hmf.web.utils.ApiResult;

public interface HmfAreaService {

    ApiResult deleteByPrimaryKey(Integer id);

    ApiResult insert(THmfArea record);

    ApiResult insertSelective(THmfArea record);

    ApiResult selectByPrimaryKey(Integer id);

    ApiResult selectHmfAreaList(String areaName);

    ApiResult updateByPrimaryKeySelective(THmfArea record);

    ApiResult updateByPrimaryKey(THmfArea record);
}
