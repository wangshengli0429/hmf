package com.pc.manage.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import com.util.CommUtils;
import com.util.CurrentPage;

public class ManageFankuiDaoImpl {
	private JdbcTemplate jt = CommUtils.getJdbcTemplate();
	
	//用户反馈		条件+分页
	public CurrentPage findFankui(int currentPage, int numPerPage, String name) {
		String sql = "select a.* from((select f.STATE state , f.CREATE_TIME time , f.FEEDBACK content , t.NAME name , t.PHONE phone from feedback f , teacher t where f.STATE='2' and f.UDID=t.UDID) UNION ALL (select f.STATE state , f.CREATE_TIME time , f.FEEDBACK content , t.NICKNAME name , t.PHONE phone from feedback f , student t where f.STATE='1' and f.UDID=t.UDID))a where 1=1";
		if(name!=null&&name!=""){
			sql +=" and a.`name` like '%"+name+"%' or a.PHONE like '%"+name+"%'";
		}
		sql += " ORDER BY a.time DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}
	
	
}
