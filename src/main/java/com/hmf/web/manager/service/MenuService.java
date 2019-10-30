package com.hmf.web.manager.service;

import com.hmf.web.entity.TMenu;
import com.hmf.web.utils.ApiResult;

public interface MenuService {

    ApiResult addMenu();
    ApiResult delMenuById(Integer id);
    ApiResult getMenuById(Integer id);
    ApiResult queryMenuList(String menuName);
    ApiResult updateMenu(TMenu menu);

}
