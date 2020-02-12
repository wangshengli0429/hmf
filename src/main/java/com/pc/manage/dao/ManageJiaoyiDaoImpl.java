package com.pc.manage.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.util.CommUtils;
import com.util.CurrentPage;
/**
 * 交易管理
 */
public class ManageJiaoyiDaoImpl {
	private JdbcTemplate jt = CommUtils.getJdbcTemplate();
	
	//订单管理		条件+分页
	public CurrentPage findDingdan(int currentPage, int numPerPage, String state, String payment, String startDate,
			String endDate, String name) {
		String sql = "select c.*,com.NEW_TITLE title from (select b.*,t.NAME tname ,t.PHONE phone from (select a.*,s.NICKNAME sname from (select ID, OUT_TRADE_NO outtradeNo, STU_ID , TOTAL_AMOUNT total, PAYMENT payment, STATE state, COMP_ID , TEACHER_ID , GMT_PAYMENT gmtpayment from s_order)a left join student s on a.STU_ID=s.ID)b left join teacher t on b.TEACHER_ID=t.ID)c left join composition com  on c.COMP_ID=com.ID where 1=1";
		if(state!=null&&state!=""){
			if(state.equals("0")){
				sql +=" and c.gmtpayment is not null";
			}else{
				int parseInt = Integer.parseInt(state);
				sql +=" and c.state = "+parseInt;
			}
		}
		if(payment!=null&&payment!=""){
			sql +=" and (c.payment = '"+payment+"'";
			if(payment.equals("微信")){
				sql +=" or c.payment = '01'";
            }else if(payment.equals("支付宝")){
            	sql +=" or c.payment = '02'";
            }else {
            	
            }
			sql +=")";
		}
		if(startDate!=null&&startDate!=""){
			sql +=" and c.gmtpayment>='"+startDate+"'";
		}
		if(endDate!=null&&endDate!=""){
			sql +=" and c.gmtpayment<='"+endDate+"'";
		}
		if(name!=null&&name!=""){
			sql +=" and (c.outtradeNo like '%"+name+"%' or c.sname like '%"+name+"%')";
		}
		sql += " ORDER BY c.gmtpayment DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//待处理	条件+分页
	public CurrentPage findTuikuan(int currentPage, int numPerPage, String name) {
//		String sql = "select r.ID id, o.OUT_TRADE_NO outtradeno, s.NICKNAME sname , r.MONEY money, c.NEW_TITLE title ,c.OLD_TITLE oldtitle, r.COM_ID comid, c.DRAFT draft, t.`NAME` tname, o.BACK_TIME time from refund r , s_order o, student s , teacher t , composition c where r.ORDER_ID=o.ID and r.STU_ID=s.ID and r.TEACHER_ID=t.ID and r.COM_ID = c.ID and o.STATE=5 and r.STATE=1";
//		if(name!=null&&name!=""){
//			sql +=" and (o.OUT_TRADE_NO like '%"+name+"%' or s.NICKNAME like '%"+name+"%')";
//		}
//		sql += " ORDER BY o.BACK_TIME DESC ";
		String sql = "select a.*, t.`NAME` tname from(select r.ID id, o.OUT_TRADE_NO outtradeno, s.NICKNAME sname , r.MONEY money, c.NEW_TITLE title ,c.OLD_TITLE oldtitle, r.COM_ID comid, c.DRAFT draft, o.BACK_TIME time, r.TEACHER_ID from refund r , s_order o, student s , composition c where r.ORDER_ID=o.ID and r.STU_ID=s.ID and r.COM_ID = c.ID and o.STATE=5 and r.STATE=1)a LEFT JOIN  teacher t on a.TEACHER_ID=t.ID where 1=1";
		if(name!=null&&name!=""){
			sql +=" and (a.outtradeno like '%"+name+"%' or a.sname like '%"+name+"%')";
		}
		sql += " ORDER BY a.time  DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//待处理	查看退款作文
	public List<Map<String, Object>> selectTkzw(int id) {
		String sql = "select c.NEW_TITLE title, c.PROPO propo, s.NICKNAME author, c.DRAFT draft, c.GEADE geade, c.CREATE_TIME time, c.CONTENT content, c.IMAGE1 img1, c.IMAGE2 img2, c.IMAGE3 img3 from s_order o , composition c , student s where o.COMP_ID=c.ID and o.STU_ID=s.ID and c.ID="+id;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}

	//待处理	处理
	public List<Map<String, Object>> selectChuli(int id) {
		//String sql = "select r.ID id , r.STU_ID sid ,s.OUT_TRADE_NO ddbh , r.CREATE_TIME time , r.MONEY money , t.`NAME` tname ,c.NEW_TITLE title ,s.PAYMENT payment , s.TOTAL_AMOUNT orderPrice , s.BUYER_PAY_AMOUNT buyer from refund r , s_order s , teacher t , composition c where r.ORDER_ID=s.ID and r.TEACHER_ID=t.ID and r.COM_ID=c.ID and r.ID="+id;
		String sql = "select a.*, t.`NAME` tname from (select r.ID id , r.STU_ID sid ,s.OUT_TRADE_NO ddbh , r.CREATE_TIME time , r.MONEY money , r.TEACHER_ID tid, c.NEW_TITLE title ,s.PAYMENT payment , s.TOTAL_AMOUNT orderPrice , s.BUYER_PAY_AMOUNT buyer from refund r , s_order s , composition c where r.ORDER_ID=s.ID and r.COM_ID=c.ID and r.ID="+id+")a LEFT JOIN teacher t on a.tid=t.ID";
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}
	
	//查询退款订单
	public List<Map<String, Object>> findOrder(int id) {
		String sql = "select s.APP_ID , s.PLATFORM_ID , s.OUT_TRADE_NO out_trade_no , s.SIGN_TYPE, s.TRADE_NO trade_no , r.MONEY refund_amount , r.OUT_REQUEST_NO out_request_no, s.id s_order_id,s.PAYMENT from refund r , s_order s where r.ORDER_ID=s.ID and r.TEACHER_ID=s.TEACHER_ID and r.ID="+id;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}
		
	//待处理	处理-提交
	public int updateChuli(int id, int sid, String title, int state, int userId, String ddbh, String backRefund) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
//		if (!"success".equals(backRefund)) {
//			return 0;
//		}
		int i = 0;
		if(state==2&&"success".equals(backRefund)){//state==2同意退款
			String sql = "update refund set DISPOSE_TIME='"+time+"', ACCOUNT_ID="+userId+", STATE="+state+" where ID="+id;
			i = jt.update(sql);
			String sql2 = "update s_order set STATE=6, UPDATE_TIME='"+time+"' where OUT_TRADE_NO='"+ddbh+"'";
			i = jt.update(sql2);
			String information = "insert into information set INF_CONTENT='您的《"+title+"》的退款金额已成功返还到您的支付账户，请到相应账户中查看！',CREATE_TIME='"+time+"',STU_ID="+sid+",STATE=1,ICON=6,TITLE='申请退款成功'";
			i = jt.update(information);
		}else if(state==3){//state==3拒绝退款
			String sql = "update refund set DISPOSE_TIME='"+time+"', ACCOUNT_ID="+userId+", STATE="+state+" where ID="+id;
			i = jt.update(sql);
			String sql2 = "update s_order set STATE=2, UPDATE_TIME='"+time+"' where OUT_TRADE_NO='"+ddbh+"'";
			i = jt.update(sql2);
			String information = "insert into information set INF_CONTENT='平台已拒绝您的退款要求，如有疑问，请及时联系我们哦。',CREATE_TIME='"+time+"',STU_ID="+sid+",STATE=1,ICON=6,TITLE='申请退款被拒绝'";
			i = jt.update(information);
			//将数据添加到拒绝退款表
			String sql3 = "insert into refusing_refund (REFUND_ID,MONEY,CREATE_TIME,TEACHER_ID,COMP_TITLE,PAYMENT,ORDER_PRICE,STATE,STU_ID,ORDER_ID,COM_ID,DISPOSE_TIME,ACCOUNT_ID,OUT_REQUEST_NO) select * from refund where STATE=3 and ID="+id;
			i = jt.update(sql3);
		}
		return i;
	}

	//已处理		条件+分页
	public CurrentPage findYichuli(int currentPage, int numPerPage, String state, String name) {
		//String sql = "select a.* from (select r.ID id, o.OUT_TRADE_NO outtradeno, s.NICKNAME sname , r.MONEY money, c.NEW_TITLE title ,c.OLD_TITLE oldtitle, r.COM_ID comid, c.DRAFT draft, t.`NAME` tname, r.STATE state, r.CREATE_TIME ctime, r.DISPOSE_TIME dtime , m.USERNAME userName from refund r , s_order o, student s , teacher t , composition c , manage_users m where r.ORDER_ID=o.ID and r.STU_ID=s.ID and r.TEACHER_ID=t.ID and r.COM_ID = c.ID and r.STATE!=1 and r.STATE!=3 and r.ACCOUNT_ID=m.ID UNION ALL select r.ID id, o.OUT_TRADE_NO outtradeno, s.NICKNAME sname , r.MONEY money, c.NEW_TITLE title ,c.OLD_TITLE oldtitle, r.COM_ID comid, c.DRAFT draft, t.`NAME` tname, r.STATE state, r.CREATE_TIME ctime, r.DISPOSE_TIME dtime , m.USERNAME userName from refusing_refund r , s_order o, student s , teacher t , composition c , manage_users m where r.ORDER_ID=o.ID and r.STU_ID=s.ID and r.TEACHER_ID=t.ID and r.COM_ID = c.ID and r.STATE=3 and r.ACCOUNT_ID=m.ID )a where 1=1 ";
		String sql = "select a.* from (select b.*,t.`NAME` tname from (select r.ID id, o.OUT_TRADE_NO outtradeno, s.NICKNAME sname , r.MONEY money, c.NEW_TITLE title ,c.OLD_TITLE oldtitle, r.COM_ID comid, c.DRAFT draft, r.STATE state, r.CREATE_TIME ctime, r.DISPOSE_TIME dtime , m.USERNAME userName ,r.TEACHER_ID tid from refund r , s_order o, student s , composition c , manage_users m where r.ORDER_ID=o.ID and r.STU_ID=s.ID and r.COM_ID = c.ID and r.STATE!=1 and r.STATE!=3 and r.ACCOUNT_ID=m.ID)b LEFT JOIN teacher t on b.tid=t.ID UNION ALL select b.*, t.`NAME` tname from (select r.ID id, o.OUT_TRADE_NO outtradeno, s.NICKNAME sname , r.MONEY money, c.NEW_TITLE title ,c.OLD_TITLE oldtitle, r.COM_ID comid, c.DRAFT draft, r.STATE state, r.CREATE_TIME ctime, r.DISPOSE_TIME dtime , m.USERNAME userName ,r.TEACHER_ID tid from refusing_refund r , s_order o, student s , composition c , manage_users m where r.ORDER_ID=o.ID and r.STU_ID=s.ID and r.COM_ID = c.ID and r.STATE=3 and r.ACCOUNT_ID=m.ID)b LEFT JOIN teacher t on b.tid=t.ID )a where 1=1 ";
		if(state!=null&&state!=""){
			int statei = Integer.parseInt(state);
			sql +=" and a.STATE="+statei;
		}
		if(name!=null&&name!=""){
			sql +=" and (outtradeno like '%"+name+"%' or sname like '%"+name+"%' or userName like '%"+name+"%')";
		}
		sql += " ORDER BY ctime DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//已处理	查看
	public List<Map<String, Object>> selectYitk(int id, int state) {
		//String sql = "select r.ID id , s.OUT_TRADE_NO ddbh , r.CREATE_TIME ctime , r.MONEY money , t.`NAME` tname ,r.COMP_TITLE title ,r.STATE state ,r.PAYMENT payment ,r.DISPOSE_TIME dtime ,s.BUYER_PAY_AMOUNT buyer from refund r , s_order s , teacher t , composition c where r.ORDER_ID=s.ID and r.TEACHER_ID=t.ID and r.COM_ID=c.ID and r.ID="+id;
		String sql = "select a.* , t.`NAME` tname from (select r.ID id , s.OUT_TRADE_NO ddbh , r.CREATE_TIME ctime , r.MONEY money ,r.COMP_TITLE title ,r.STATE state ,r.PAYMENT payment ,r.DISPOSE_TIME dtime ,s.BUYER_PAY_AMOUNT buyer , r.TEACHER_ID tid from refund r , s_order s , composition c where r.ORDER_ID=s.ID and r.COM_ID=c.ID and r.ID="+id+" )a LEFT JOIN teacher t on a.tid=t.ID";
		if(state==3){
			//sql = "select r.ID id , s.OUT_TRADE_NO ddbh , r.CREATE_TIME ctime , r.MONEY money , t.`NAME` tname ,r.COMP_TITLE title ,r.STATE state ,r.PAYMENT payment ,r.DISPOSE_TIME dtime ,s.BUYER_PAY_AMOUNT buyer from refusing_refund r , s_order s , teacher t , composition c where r.ORDER_ID=s.ID and r.TEACHER_ID=t.ID and r.COM_ID=c.ID and r.ID="+id;
			sql = "select a.* , t.`NAME` tname from (select r.ID id , s.OUT_TRADE_NO ddbh , r.CREATE_TIME ctime , r.MONEY money ,r.COMP_TITLE title ,r.STATE state ,r.PAYMENT payment ,r.DISPOSE_TIME dtime ,s.BUYER_PAY_AMOUNT buyer ,r.TEACHER_ID tid from refusing_refund r , s_order s , composition c where r.ORDER_ID=s.ID and r.COM_ID=c.ID and r.ID="+id+")a LEFT JOIN  teacher t on a.tid=t.ID ";
		}
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}
	
	/**
	 * 结算管理
	 * @param userId 
	 * @return 
	 */
	//生成账单
	public int order(String clearingTime, int userId) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		
		//查询老师id
		String sql1 = "select TEACHER_ID from s_order where STATE=3 and CLEARING_TIME is null and BILL_TIME is null and UPDATE_TIME <='"+clearingTime+"' group by TEACHER_ID";
		List<Map<String,Object>> forList = jt.queryForList(sql1);
		int i = 0;
		if(forList.size()>0){
			for(int j=0;j<forList.size();j++){
				int tid = Integer.parseInt(forList.get(j).get("TEACHER_ID").toString());
				//每个老师点评数量、总金额
				String sql2 = "select count(ID) num , sum(TOTAL_AMOUNT) price from s_order where STATE=3 and CLEARING_TIME is null and BILL_TIME is null and TEACHER_ID= "+tid+" and UPDATE_TIME <='"+clearingTime+"'";
				List<Map<String,Object>> numlist = jt.queryForList(sql2);
				int num = Integer.parseInt(numlist.get(0).get("num").toString());
				//String price = numlist.get(0).get("price").toString();
				double price = Double.valueOf(numlist.get(0).get("price").toString());
				//上次结算时间
				String sql3 = "select max(BILL_TIME) maxtime from teacher_clearing where TEACHE_ID = "+tid;
				List<Map<String,Object>> timelist = jt.queryForList(sql3);
				if(timelist.get(0).get("maxtime")!=null){
					String maxtime = timelist.get(0).get("maxtime").toString();
					//添加到结算表
					String sql4 = "insert into teacher_clearing set TEACHE_ID="+tid+",NUM="+num+",TOTAL_PRICE="+price+",USER_ID="+userId+",LAST_TIME='"+maxtime+"',BILL_TIME='"+time+"'";
					int update = jt.update(sql4);
				}else {
					String sql4 = "insert into teacher_clearing set TEACHE_ID="+tid+",NUM="+num+",TOTAL_PRICE="+price+",USER_ID="+userId+",BILL_TIME='"+time+"'";
					int update = jt.update(sql4);
				}
				
				String sql = "update s_order set BILL_TIME='"+time+"' where STATE=3 and CLEARING_TIME is null and BILL_TIME is null and TEACHER_ID= "+tid+" and UPDATE_TIME <='"+clearingTime+"'";
				i = jt.update(sql);
			}
		}
		return i;
	}

	//查询账单
	public CurrentPage findZhangdan(String clearingTime, String name, int currentPage, int numPerPage) {
		String sql = "select b.* , t.`NAME` tname , t.PHONE tphone from (select a.*, m.USERNAME userName from (select tc.ID id , tc.TEACHE_ID , tc.NUM num , tc.TOTAL_PRICE price, tc.USER_ID , tc.STATE state , tc.BILL_TIME billTime , tc.CLEAR_TIME cleartime from teacher_clearing tc)a left join manage_users m on a.USER_ID=m.ID )b left join teacher t on b.TEACHE_ID = t.ID where 1=1";
		if(name!=null&&name!=""){
			sql += " and (t.`NAME` like '%"+name+"%' or t.PHONE like '%"+name+"%') ";
		}
		sql+=" ORDER BY b.billTime DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}
	
	//查询未结算基本信息
	public List<Map<String, Object>> findWeiinfor(int id) {
		String sql1 = "select t.`NAME` tname , t.PHONE phone , o.BILL_TIME billTime , tc.CLEAR_TIME clearTime , m.USERNAME userName from teacher_clearing tc , s_order o ,  teacher t , manage_users m where tc.TEACHE_ID=o.TEACHER_ID and tc.TEACHE_ID=t.ID and tc.USER_ID=m.ID and o.STATE=3 and o.BILL_TIME=tc.BILL_TIME and tc.ID="+id+" group by t.ID";
		List<Map<String,Object>> queryForList = jt.queryForList(sql1);
		return queryForList;
	}
		
	//查看未结算
	public CurrentPage findWeijiesuan(int id, int currentPage, int numPerPage) {
		String sql = "select o.ID id, o.OUT_TRADE_NO outtradeNo, tc.STATE state, c.NEW_TITLE title, s.NICKNAME author, o.TOTAL_AMOUNT total, o.BUYER_PAY_AMOUNT buyerpay, cc.COM_TIME comTime from teacher_clearing tc , s_order o , composition c , com_composition cc , student s where tc.TEACHE_ID=o.TEACHER_ID and o.STATE=3 and o.BILL_TIME=tc.BILL_TIME and o.COMP_ID=c.ID and o.TEACHER_ID=c.TID and c.TID=cc.TEACHER_ID and c.ID=cc.COMP_ID and o.STU_ID=s.ID and tc.ID="+id;
		sql += " ORDER BY cc.COM_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//查询已结算基本信息
	public List<Map<String, Object>> findYiinfor(int id) {
		String sql1 = "select t.`NAME` tname , t.PHONE phone , o.BILL_TIME billTime , tc.CLEAR_TIME clearTime , m.USERNAME userName from teacher_clearing tc , s_order o ,  teacher t , manage_users m where tc.TEACHE_ID=o.TEACHER_ID and tc.TEACHE_ID=t.ID and tc.USER_ID=m.ID and o.STATE=4 and o.BILL_TIME=tc.BILL_TIME and tc.ID="+id+" group by t.ID";
		List<Map<String,Object>> queryForList = jt.queryForList(sql1);
		return queryForList;
	}
	//查看已结算订单
	public CurrentPage findYijiesuan(int id, int currentPage, int numPerPage) {
		String sql = "select o.ID id, o.OUT_TRADE_NO outtradeNo, tc.STATE state, c.NEW_TITLE title, s.NICKNAME author, o.TOTAL_AMOUNT total, o.BUYER_PAY_AMOUNT buyerpay, cc.COM_TIME comTime from teacher_clearing tc , s_order o , composition c , com_composition cc , student s where tc.TEACHE_ID=o.TEACHER_ID and o.STATE=4 and o.BILL_TIME=tc.BILL_TIME and o.COMP_ID=c.ID and o.TEACHER_ID=c.TID and c.TID=cc.TEACHER_ID and c.ID=cc.COMP_ID and o.STU_ID=s.ID and tc.ID="+id;
		sql += " ORDER BY cc.COM_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}
	
	//未结算	结算
	public int settlement(int id) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		String sql = "select TEACHE_ID,BILL_TIME from teacher_clearing where ID ="+id;
		Map<String, Object> forMap = jt.queryForMap(sql);
		int tid = Integer.parseInt(forMap.get("TEACHE_ID").toString());
		String billtime = forMap.get("BILL_TIME").toString();
		String sql1 = "update teacher_clearing set STATE=1,CLEAR_TIME='"+time+"' where ID ="+id;
		String sql2 = "update s_order set STATE=4,CLEARING_TIME='"+time+"' where TEACHER_ID="+tid+" and BILL_TIME='"+billtime+"'";
		jt.update(sql1);
		int i = jt.update(sql2);
		if(i>0){
			String information = "insert into information set INF_CONTENT='您上月的收益已经结算成功，可到我的账单中进行核对，如有疑问请及时联系客服！',CREATE_TIME='"+time+"',TEACHER_ID="+tid+",STATE=2,ICON=8,TITLE='收益结算 '";
			jt.update(information);
		}
		return i;
	}
	
	//导出账单
	public List<Map<String, Object>> exportByidsZd(String ids) {
	    String sql = "select b.* , t.`NAME` tname , t.PHONE tphone from (select a.*, m.USERNAME userName from (select tc.ID id , tc.TEACHE_ID , tc.NUM num , tc.TOTAL_PRICE price, tc.USER_ID , tc.STATE state , tc.CLEAR_TIME cleartime , tc.BILL_TIME billTime from teacher_clearing tc)a left join manage_users m on a.USER_ID=m.ID )b left join teacher t on b.TEACHE_ID = t.ID where 1=1 and b.id in ("+ids+")";
	    List<Map<String,Object>> forList = jt.queryForList(sql);
	    return forList;
	}

	//导出	老师未结算订单
	public List<Map<String, Object>> exportDingdanwjs(int id, String ids) {
		String sql = "select o.ID id, o.OUT_TRADE_NO outtradeNo, tc.STATE state, c.NEW_TITLE title, s.NICKNAME author, o.TOTAL_AMOUNT total, o.BUYER_PAY_AMOUNT buyerpay, cc.COM_TIME comTime from teacher_clearing tc , s_order o , composition c , com_composition cc , student s where tc.TEACHE_ID=o.TEACHER_ID and o.STATE=3 and o.BILL_TIME=tc.BILL_TIME and o.COMP_ID=c.ID and o.TEACHER_ID=c.TID and c.TID=cc.TEACHER_ID and c.ID=cc.COMP_ID and o.STU_ID=s.ID and tc.ID="+id+" and o.ID in("+ids+")";
		List<Map<String,Object>> forList = jt.queryForList(sql);
	    return forList;
	}

	//导出	老师已结算订单
	public List<Map<String, Object>> exportDingdanyi(int id, String ids) {
		String sql = "select o.ID id, o.OUT_TRADE_NO outtradeNo, tc.STATE state, c.NEW_TITLE title, s.NICKNAME author, o.TOTAL_AMOUNT total, o.BUYER_PAY_AMOUNT buyerpay, cc.COM_TIME comTime from teacher_clearing tc , s_order o , composition c , com_composition cc , student s where tc.TEACHE_ID=o.TEACHER_ID and o.STATE=4 and o.BILL_TIME=tc.BILL_TIME and o.COMP_ID=c.ID and o.TEACHER_ID=c.TID and c.TID=cc.TEACHER_ID and c.ID=cc.COMP_ID and o.STU_ID=s.ID and tc.ID="+id+" and o.ID in("+ids+")";
		List<Map<String,Object>> forList = jt.queryForList(sql);
	    return forList;
	}


	/**  
	 * 月卡集合
	*/ 
	public CurrentPage findYueKa(String failure, String name, String type,
			int currentPage, int numPerPage) {
		String sql = "SELECT c.END_TIME,c.failure,c.card_price,c.card_name,c.usage_count,c.type,s.OUT_TRADE_NO,s.GMT_PAYMENT," +
				"ss.NICKNAME FROM card c,c_order s,student ss where c.ID = s.CARD_ID AND c.UDID = ss.UDID ";
		if (failure != null && !"".equals(failure)) {
			sql += " AND C.FAILURE = " + failure;
		}
		if (name != null && !"".equals(name)) {
			sql += " AND (ss.NICKNAME LIKE '%"+name+"%' OR s.OUT_TRADE_NO = '"+name+"')";
		}
		if (type != null && !"".equals(type)) {
			sql += " AND c.TYPE = " + type;
		}
		sql += " ORDER BY start_time DESC";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//收益订单管理		条件+分页
	public CurrentPage findProfitOrder(int currentPage, int numPerPage, String startDate, String endDate, String name) {
		String sql = "select a.* from (select o.ID id,o.OUT_TRADE_NO outtradeno,s.NICKNAME name,s.PHONE phone,o.TOTAL_AMOUNT total,o.PAYMENT payment,o.GMT_PAYMENT gmtpayment from s_order o,student s where o.STU_ID=s.ID and ( o.PAYMENT='02' or o.PAYMENT='支付宝' or o.PAYMENT='01' or o.PAYMENT='微信') and o.STATE!=6 UNION ALL select o.ID id,o.OUT_TRADE_NO outtradeno,s.NICKNAME name,s.PHONE phone,o.TOTAL_AMOUNT total,o.PAYMENT payment,o.GMT_PAYMENT gmtpayment from c_order o,student s where o.STU_ID=s.ID)a where 1=1 ";
		if(startDate!=null&&startDate!=""){
			sql +=" and a.gmtpayment>='"+startDate+"'";
		}
		if(endDate!=null&&endDate!=""){
			sql +=" and a.gmtpayment<='"+endDate+"'";
		}
		if (name != null && !"".equals(name)) {
			sql += " AND (a.name LIKE '%"+name+"%' OR a.outtradeno LIKE '%"+name+"%' OR a.phone LIKE '%"+name+"%')";
		}
		sql += " ORDER BY a.gmtpayment DESC";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//微信退款是查询所需要的参数 
	public Map<String,Object> findS_orderByWx(String s_order_id) {
		String sql = "SELECT SIGN sign,TRADE_NO trade_no,TOTAL_AMOUNT total_fee,BUYER_PAY_AMOUNT refund_fee FROM s_order WHERE id = ?";
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{s_order_id});
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	//第三方退款处理
	public int updateRefunds(int id, int userId) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		String sql = "update refund set DISPOSE_TIME='"+time+"', ACCOUNT_ID="+userId+" where ID="+id;
		int i = jt.update(sql);
		return i;
	}
	
	
}
