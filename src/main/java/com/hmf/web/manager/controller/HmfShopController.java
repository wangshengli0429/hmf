package com.hmf.web.manager.controller;

import com.hmf.web.entity.THmfArea;
import com.hmf.web.entity.THmfShop;
import com.hmf.web.manager.service.HmfShopService;
import com.hmf.web.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class HmfShopController {

    @Autowired
    private HmfShopService shopService;

    @RequestMapping("/shop/add")
    private ApiResult add(THmfShop shop){
        return shopService.insert(shop);
    }

    @RequestMapping("/shop/queryById")
    private ApiResult list(Integer id){
        return shopService.selectByPrimaryKey(id);
    }

    @RequestMapping("/shop/edit")
    private ApiResult edit(THmfShop shop){
        return shopService.updateByPrimaryKey(shop);
    }

    @RequestMapping("/shop/del")
    private ApiResult del(Integer id){
        return shopService.deleteByPrimaryKey(id);
    }

}
