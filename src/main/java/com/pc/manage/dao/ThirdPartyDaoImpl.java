package com.pc.manage.dao;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.util.CommUtils;
import com.util.CurrentPage;
/**
 * 第三方合作
 */
public class ThirdPartyDaoImpl {
	private static Logger logger = Logger.getLogger(ManageUsersDaoImpl.class);
	
	private JdbcTemplate jt = CommUtils.getJdbcTemplate();

	//待点评
	public CurrentPage findDaidplist(int currentPage, int numPerPage, String name, String nj, String startDate, String endDate) {
		String sql = "select a.*, t.NAME name, t.PHONE tphone from(select c.TACHER_STATE tea_state,c.ID id, c.NEW_TITLE newTitle, s.NICKNAME author, s.id stu_id, s.PHONE sphone, c.GEADE geade, c.DRAFT draft, c.OLD_TITLE oldTitle, c.CREATE_TIME createTime ,o.TEACHER_ID teacherId from s_order o , composition c , student s where o.COMP_ID=c.ID and o.STU_ID=s.ID and o.STATE=2 and c.PLATFORM_ID='10001')a LEFT JOIN teacher t on a.teacherId=t.ID where 1=1";
		if(name!=null&&name!=""){
			sql += " and (a.newTitle like '%"+ name +"%' or a.author like '%"+ name +"%' or t.NAME like '%"+ name +"%' or t.PHONE like '%"+ name +"%')";
		}
		if(nj!=null&&nj!=""){
			if(nj.equals("1")){//小学
				sql += " and a.geade like '02700%'";
			}else if(nj.equals("2")){//初中
				sql += " and a.geade like '02701%'";
			}else if(nj.equals("3")){//高中
				sql += " and a.geade like '02702%'";
			}else {
				sql += " and a.geade='"+ nj +"'";
			}
		}
		if(startDate!=null&&startDate!=""){
			sql +=" and a.createTime>='"+startDate+"'";
		}
		if(endDate!=null&&endDate!=""){
			sql +=" and a.createTime<='"+endDate+"'";
		}
		sql += " ORDER BY a.createTime DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//已点评
	public CurrentPage findYidplist(int currentPage, int numPerPage, String grade, String grading, String name, String startDate, String endDate) {
		String sql = "select cc.ID id, c.ID comp_id, c.NEW_TITLE newTitle, s.NICKNAME author, s.PHONE sphone, c.GEADE geade,t.NAME name, t.PHONE tphone, c.DRAFT draft, c.OLD_TITLE oldTitle, c.CREATE_TIME createTime, c.OPEN open, cc.COM_TIME comTime, cc.GRADING grading from s_order o , composition c , com_composition cc , teacher t , student s where o.COMP_ID=c.ID and cc.TEACHER_ID=t.ID and o.STATE=3 and o.STU_ID=s.ID and cc.COMP_ID=c.ID and c.PLATFORM_ID='10001'";
		if(grade!=null&&grade!=""){
			if(grade.equals("1")){//小学
				sql += " and c.GEADE like '02700%'";
			}else if(grade.equals("2")){//初中
				sql += " and c.GEADE like '02701%'";
			}else if(grade.equals("3")){//高中
				sql += " and c.GEADE like '02702%'";
			}else {
				sql += " and c.GEADE='"+ grade +"'";
			}
		}
		if(grading!=null&&grading!=""){
			sql += " and cc.GRADING ='"+grading+"'";
		}
		if(name!=null&&name!=""){
			sql += " and (c.NEW_TITLE like '%"+ name +"%' or s.NICKNAME like '%"+ name +"%' or t.NAME like '%"+ name +"%' or t.PHONE like '%"+ name +"%')";
		}
		if(startDate!=null&&startDate!=""){
			sql +=" and cc.COM_TIME>='"+startDate+"'";
		}
		if(endDate!=null&&endDate!=""){
			sql +=" and cc.COM_TIME<='"+endDate+"'";
		}
		sql += " ORDER BY comTime DESC  ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//退款
	public CurrentPage findRefund(int currentPage, int numPerPage, String state, String name, String startDate, String endDate) {
		String sql = "select b.*,t.`NAME` tname from (select r.ID id, o.OUT_TRADE_NO outtradeno, s.NICKNAME sname , r.MONEY money, c.NEW_TITLE title ,c.OLD_TITLE oldtitle, r.COM_ID comid, c.DRAFT draft, r.STATE state, r.CREATE_TIME ctime, r.DISPOSE_TIME dtime , m.USERNAME userName ,r.TEACHER_ID tid ,o.PLATFORM_ID platformid from refund r , s_order o, student s , composition c , manage_users m where r.ORDER_ID=o.ID and r.STU_ID=s.ID and r.COM_ID = c.ID and r.ACCOUNT_ID=m.ID and o.PLATFORM_ID='10001')b LEFT JOIN teacher t on b.tid=t.ID where 1=1 ";
		if(state!=null&&state!=""){
			int statei = Integer.parseInt(state);
			sql +=" and b.state="+statei;
		}
		if(name!=null&&name!=""){
			sql +=" and (b.outtradeno like '%"+name+"%' or b.sname like '%"+name+"%' or b.title like '%"+name+"%')";
		}
		if(startDate!=null&&startDate!=""){
			sql +=" and b.ctime>='"+startDate+"'";
		}
		if(endDate!=null&&endDate!=""){
			sql +=" and b.ctime<='"+endDate+"'";
		}
		sql += " ORDER BY b.ctime DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}
	
	

}
