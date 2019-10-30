package com.hmf.web.manager.service.impl;

import com.hmf.web.entity.TMenu;
import com.hmf.web.manager.bo.Menu;
import com.hmf.web.manager.dao.TMenuDao;
import com.hmf.web.manager.service.MenuService;
import com.hmf.web.utils.ApiResult;
import com.hmf.web.utils.enums.HttpCodeEnum;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {
    private static Logger logger = getLogger(MenuServiceImpl.class);

    @Autowired
    private TMenuDao menuDao;

    @Override
    public ApiResult addMenu() {
        return null;
    }

    @Override
    public ApiResult delMenuById(Integer id) {
        return null;
    }

    @Override
    public ApiResult getMenuById(Integer id) {
        return null;
    }

    @Override
    public ApiResult queryMenuList(String menuName) {
        ApiResult apiResult = new ApiResult();
        try {
            List<Menu> menuList = menuDao.getMenuList();
            apiResult.setStatus(HttpCodeEnum.QUERY_CODE_200.getCode());
            apiResult.setMessage(HttpCodeEnum.QUERY_CODE_200.getMessage());
            apiResult.setData(menuList);
            return apiResult;
        }catch (Exception e){
            logger.info("查询菜单异常：{0}",e);
            apiResult.setStatus(HttpCodeEnum.EDIT_CODE_500.getCode());
            apiResult.setMessage(HttpCodeEnum.EDIT_CODE_500.getMessage());
            return apiResult;
        }

    }

    @Override
    public ApiResult updateMenu(TMenu menu) {
        return null;
    }
}
