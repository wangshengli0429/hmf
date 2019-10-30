package com.hmf.web.manager.bo;

import lombok.Data;

import java.util.List;

@Data
public class Menu {
    private Integer id;

    private String menuName;

    private Integer parentId;

    private Integer menuLevel;

    private Integer showIndex;

    private List<Menu> childrenList;
}
