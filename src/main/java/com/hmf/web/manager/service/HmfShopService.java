package com.hmf.web.manager.service;

import com.hmf.web.entity.THmfShop;
import com.hmf.web.utils.ApiResult;

public interface HmfShopService {

    ApiResult deleteByPrimaryKey(Integer id);

    ApiResult insert(THmfShop record);

    ApiResult selectByPrimaryKey(Integer id);

    ApiResult updateByPrimaryKey(THmfShop record);
}
