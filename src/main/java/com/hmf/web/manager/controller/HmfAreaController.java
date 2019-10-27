package com.hmf.web.manager.controller;

import com.hmf.web.entity.THmfArea;
import com.hmf.web.manager.service.HmfAreaService;
import com.hmf.web.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class HmfAreaController {

    @Autowired
    private HmfAreaService hmfAreaService;

    @RequestMapping("/area/add")
    private ApiResult add(THmfArea area){
        return hmfAreaService.insert(area);
    }

    @RequestMapping("/area/list")
    private ApiResult list(String areaName){
        return hmfAreaService.selectHmfAreaList(areaName);
    }

    @RequestMapping("/area/edit")
    private ApiResult edit(THmfArea area){
        return hmfAreaService.updateByPrimaryKey(area);
    }

    @RequestMapping("/area/del")
    private ApiResult del(Integer id){
        return hmfAreaService.deleteByPrimaryKey(id);
    }



}

