package com.hmf.web.utils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;

import java.util.List;

public class PageUtil<T> {
    /**
     * @return a
     * @Description 获取分页
    * @Param
     **/
    public JSONObject getPageJson(Integer pageNum, Integer pageSize, List<T> list) {
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<T> appsPageInfo = new PageInfo<>(list);
        JSONObject json = JSONObject.fromObject(appsPageInfo);//将java对象转换为json对象
        return json;
    }
}
