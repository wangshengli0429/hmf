package com.rest.service.dao.impl;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.util.CommUtils;
import com.util.CurrentPage;

public class ComStateDaoImpl implements com.rest.service.dao.ComStateDao {

	private JdbcTemplate jt;

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

	// 作文状态(学生) //状态(待上传1，待点评2，已点评3)
	public List<Map<String, Object>> findComStateS(String state, String udid) {
		try {
			String sql = "select c.ID cid, c.DRAFT draft, t.NAME name,t.ID id, c.CREATE_TIME time, c.NEW_TITLE title, t.HAND_URL url" + " from com_composition cc, composition c,teacher t,student s" + " where cc.COMP_ID=c.ID and cc.TEACHER_ID=t.udid and s.ID=cc.STU_ID and" + " cc.STATE='" + state + "' and s.UDID='" + udid + "'";
			List<Map<String, Object>> queryList = jt.queryForList(sql);
			if (queryList.size() > 0) {
				return queryList;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	/**
	 * 查询订单各个状态的数据
	 * 
	 * @param state
	 * @param udid
	 * @return
	 */
	public List<Map<String, Object>> findOrderStateList(String state, String udid, String currentPage, String numPerPage) {
		// `ID` int(11) NOT NULL AUTO_INCREMENT,
		// `OUT_TRADE_NO` varchar(255) DEFAULT NULL COMMENT '订单编号',
		// `STU_ID` int(11) DEFAULT NULL COMMENT '学生id',
		// `FUND_BILL_LIST` varchar(200) DEFAULT NULL COMMENT '支付金额信息',
		// `PAYMENT` varchar(255) DEFAULT NULL COMMENT '付款方式',
		// `STATE` int(11) DEFAULT NULL COMMENT '订单状态(正常1，取消2，完成3)',
		// `COMP_ID` int(11) DEFAULT NULL COMMENT '作文id',
		// `TEACHER_ID` int(11) DEFAULT NULL COMMENT '老师id',
		// `GMT_PAYMENT` timestamp NULL DEFAULT NULL COMMENT '付款时间',
		// `R_PACK` int(11) DEFAULT NULL COMMENT '使用红包1,未使用红包2',
		// `REDID` int(11) DEFAULT NULL COMMENT '红包id',
		// `TOTAL_AMOUNT` decimal(10,2) DEFAULT NULL COMMENT '订单金额',
		// `BUYER_PAY_AMOUNT` varchar(255) DEFAULT NULL COMMENT '付款金额',
		// `SUBJECT` varchar(255) DEFAULT NULL COMMENT '订单标题',
		// `SIGN` text COMMENT '签名',
		// `SIGN_TYPE` varchar(255) DEFAULT NULL,
		// `APP_ID` varchar(200) DEFAULT NULL COMMENT '开发者ID',
		// `TRADE_NO` varchar(255) DEFAULT NULL COMMENT '支付宝交易号',
		// PRIMARY KEY (`ID`)
		// ) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COMMENT='订单表';
		//
		try {
			int num = Integer.parseInt(numPerPage) * (Integer.parseInt(currentPage) - 1);
			int count = Integer.parseInt(numPerPage);
			String sql = "";
			if ("3".equals(state)) {
				sql = "select d.*,ap.ID  pid from (select b.*,c.ID cid, c.DRAFT draft, c.CREATE_TIME time, c.NEW_TITLE title from (select a.*, t.NAME name, t.ID id, t.HAND_URL url from (select o.OUT_TRADE_NO out_trade_num, o.HAVE_COMMENT have_comment,o.BACK_TIME back_time, o.UPDATE_TIME update_time, o.DRAFT order_draft,o.COMP_TITLE order_title,o.GMT_PAYMENT pay_time,o.ID orderId, o.TEACHER_ID tid, o.STU_ID sid, o.COMP_ID comid from student s, s_order o where s.UDID= '" + udid + "'  and o.STATE in (3,4) and o.STU_ID=s.ID )a left join teacher t on a.tid=t.ID)b left join composition c on b.comid=c.ID )d left join appraise ap on d.cid = ap.COMP_ID order by update_time desc limit " + num + "," + count;
			} else if("1".equals(state)){
				sql = "select d.*,ap.ID  pid from (select b.*,c.ID cid, c.DRAFT draft, c.CREATE_TIME time, c.NEW_TITLE title from (select a.*, t.NAME name, t.ID id, t.HAND_URL url from (select o.OUT_TRADE_NO out_trade_num, o.HAVE_COMMENT have_comment,o.BACK_TIME back_time, o.UPDATE_TIME update_time, o.DRAFT order_draft,o.COMP_TITLE order_title,o.GMT_PAYMENT pay_time,o.ID orderId, o.TEACHER_ID tid, o.STU_ID sid, o.COMP_ID comid from student s, s_order o where s.UDID= '" + udid + "'  and o.STATE=" + state + " and o.STU_ID=s.ID )a left join teacher t on a.tid=t.ID)b left join composition c on b.comid=c.ID )d left join appraise ap on d.cid = ap.COMP_ID order by pay_time desc limit " + num + "," + count;
			}else if("2".equals(state)){
				sql = "select d.*,ap.ID  pid from (select b.*,c.ID cid, c.DRAFT draft, c.CREATE_TIME time, c.NEW_TITLE title, c.TACHER_STATE teacher_state from (select a.*, t.NAME name, t.ID id, t.HAND_URL url from (select o.OUT_TRADE_NO out_trade_num, o.HAVE_COMMENT have_comment,o.BACK_TIME back_time, o.UPDATE_TIME update_time, o.DRAFT order_draft,o.COMP_TITLE order_title,o.GMT_PAYMENT pay_time,o.ID orderId, o.TEACHER_ID tid, o.STU_ID sid, o.COMP_ID comid, o.PAYMENT from student s, s_order o where s.UDID= '" + udid + "'  and o.STATE=" + state + " and o.STU_ID=s.ID )a left join teacher t on a.tid=t.ID)b left join composition c on b.comid=c.ID )d left join appraise ap on d.cid = ap.COMP_ID order by time desc limit " + num + "," + count;
			}else if ("0".equals(state)) {
				sql = "select IF(IMAGE1 = '',0, 1) comType,t.HAND_URL url,t.ID tid,t.PRICE price,t.name name,c.id cid,c.DRAFT draft,c.CREATE_TIME time,c.NEW_TITLE title,c.state state from composition c,teacher t where c.TID = t.id and c.udid = '" + udid + "' and c.STATE = 0 ORDER BY time DESC limit " + num + "," + count;
			}
			List<Map<String, Object>> queryList = jt.queryForList(sql);
			if (queryList.size() > 0) {
				return queryList;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> findOrderStateListSaleAfter(String udid, Integer currentPage, Integer numPerPage) {
		try {
			String sql = "select d.*,ap.ID  pid from (select b.*,c.ID cid, c.DRAFT draft, c.CREATE_TIME time, c.NEW_TITLE title from (select a.*, t.NAME name, t.ID id, t.HAND_URL url from (select o.OUT_TRADE_NO out_trade_num, o.PAYMENT payway ,o.HAVE_COMMENT have_comment, o.STATE state,o.BUYER_PAY_AMOUNT money, o.BACK_TIME back_time, o.UPDATE_TIME update_time, o.DRAFT order_draft,o.COMP_TITLE order_title,o.ID orderId, o.TEACHER_ID tid, o.STU_ID sid, o.COMP_ID comid from student s, s_order o where s.UDID= ?  and o.STATE in (5,6,7) and o.STU_ID=s.ID )a left join teacher t on a.tid=t.ID)b left join composition c on b.comid=c.ID )d left join appraise ap on d.cid=ap.COMP_ID order by d.back_time desc limit ?,?";
			List<Map<String, Object>> queryList = jt.queryForList(sql, new Object[]{udid, currentPage, numPerPage});
			if (queryList.size() > 0) {
				return queryList;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	// 作文状态(老师) 订单状态(2、待评点，3、已评点)
	public List<Map<String, Object>> findComStateT(String state, String udid, String numPerPage, String currentPage) {
		int num = Integer.parseInt(numPerPage) * (Integer.parseInt(currentPage) - 1);
		int count = Integer.parseInt(numPerPage);
		
		try {
			String sql = "";
			if(state.equals("3")){
				sql = "SELECT so.GMT_PAYMENT time,c.ID cid,c.NEW_TITLE title,c.CONTENT content,c.GEADE grade,s.NICKNAME sname,"
						+ "s.HEAD surl,c.IMAGE1 image1,c.IMAGE2 image2,c.IMAGE3 image3,so.TOTEACHER cstate "
						+ "FROM s_order so,student s,teacher t,composition c WHERE t.UDID = ? AND so.TEACHER_ID = t.ID "
						+ "AND c.TID = t.ID AND c.UDID = s.UDID AND so.STATE in(3,4) AND so.COMP_ID = c.ID "
						+ "ORDER BY so.GMT_PAYMENT DESC LIMIT ?,?";
			}else {
				sql = "SELECT so.GMT_PAYMENT time,c.ID cid,c.NEW_TITLE title,c.CONTENT content,c.GEADE grade,s.NICKNAME sname,"
						+ "s.HEAD surl,c.IMAGE1 image1,c.IMAGE2 image2,c.IMAGE3 image3,so.TOTEACHER cstate "
						+ "FROM s_order so,student s,teacher t,composition c WHERE t.UDID = ? AND so.TEACHER_ID = t.ID "
						+ "AND c.TID = t.ID AND c.UDID = s.UDID AND so.STATE = 2 AND so.COMP_ID = c.ID "
						+ "ORDER BY so.GMT_PAYMENT DESC LIMIT ?,?";
			}
			
			/*if ("3".equals(state)) {
				sql = "SELECT rr.* from (SELECT	d.*, ap.ID pid FROM	(SELECT	b.*, c.ID cid,c.DRAFT draft,c.GEADE grade," +
						"c.CONTENT content,c.CREATE_TIME time,c.IMAGE1 image1,c.IMAGE2 image2,c.IMAGE3 image3,c.NEW_TITLE title " +
						"FROM(SELECT a.* FROM (SELECT t. NAME NAME,t.HAND_URL url,o.OUT_TRADE_NO out_trade_num,o.HAVE_COMMENT " +
						"have_comment,o.BACK_TIME back_time,o.UPDATE_TIME update_time,o.DRAFT order_draft,o.COMP_TITLE order_title," +
						"o.ID orderId,o.TEACHER_ID tid,o.STU_ID sid,o.COMP_ID comid FROM	student s,s_order o,teacher t WHERE	" +
						"o.TEACHER_ID = t.ID AND t.UDID = '"+udid+"' AND o.STATE = 3 AND o.STU_ID = s.ID) a) b " +
								"LEFT JOIN composition c ON b.comid = c.ID) d LEFT JOIN appraise ap ON d.cid = ap.COMP_ID)rr LEFT JOIN com_composition cc ON cc.COMP_ID = rr.cid ORDER BY cc.COM_TIME desc";
			}else {
				sql = "select d.*,ap.ID  pid from (select b.*,c.ID cid, c.DRAFT draft,c.GEADE grade,c.CONTENT content, c.CREATE_TIME time,c.IMAGE1 image1, c.IMAGE2 image2, c.IMAGE3 image3, c.NEW_TITLE title from (select a.* from (select t.NAME name, t.HAND_URL url,o.OUT_TRADE_NO out_trade_num, o.HAVE_COMMENT have_comment,o.BACK_TIME back_time, o.UPDATE_TIME update_time, o.DRAFT order_draft,o.COMP_TITLE order_title,o.ID orderId, o.TEACHER_ID tid, o.STU_ID sid, o.COMP_ID comid, s.NICKNAME sname, s.HEAD surl from student s, s_order o,teacher t where o.TEACHER_ID=t.ID  and t.UDID= '" + udid + "'  and o.STATE= " + state + " and o.STU_ID=s.ID )a )b left join composition c on b.comid=c.ID )d left join appraise ap on d.cid=ap.COMP_ID ORDER BY d.update_time DESC";
			}*/
			List<Map<String, Object>> queryList = jt.queryForList(sql, new Object[]{udid, num, count});
			for (Map<String, Object> map : queryList) {
				map.put("surl", CommUtils.judgeUrl(map.get("surl")));
				map.put("image1", CommUtils.judgeUrl(map.get("image1")));
				map.put("image2", CommUtils.judgeUrl(map.get("image2")));
				map.put("image3", CommUtils.judgeUrl(map.get("image3")));
				map.put("time", CommUtils.ObjectTime2String(map.get("time")));
			}
			return queryList;
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	// 批改作文详情list(学生)
	public List<Map<String, Object>> findComPart(String cid) {
		List<Map<String, Object>> c = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql = "SELECT * FROM composition WHERE ID=" + cid;
			List<Map<String, Object>> clist = jt.queryForList(sql);
			String tid = "";
			if(clist.size()>0){
				if(clist.get(0).get("TID")!=null){
					tid = clist.get(0).get("TID").toString();
				}
			}
			String sql1 = "SELECT * FROM com_composition WHERE 1=1 ";
			if(!tid.equals("")){
				sql1 += " AND TEACHER_ID = "+tid;
			}
			sql1 += " AND COMP_ID = ? ";
			List<Map<String, Object>> cclist = jt.queryForList(sql1, new Object[] { cid });
			if (clist.size() != 0 && cclist.size() != 0) {
				map.put("ask", TextUtils.isEmpty((String) clist.get(0).get("PROPO")) ? "" : clist.get(0).get("PROPO").toString());
				
				
				if (cclist.get(0).get("CONTENT") != null && !cclist.get(0).get("CONTENT").equals("")) {
					String str = cclist.get(0).get("CONTENT").toString();
					map.put("content", CommUtils.deleteKongForGecomposition(str));
				}
				if (clist.get(0).get("CONTENT") != null && !clist.get(0).get("CONTENT").equals("")) {
					String str = clist.get(0).get("CONTENT").toString();
					map.put("word_count", str.length());
				}
				
				map.put("draft", TextUtils.isEmpty((String) clist.get(0).get("DRAFT")) ? "" : clist.get(0).get("DRAFT").toString());
				map.put("grade", TextUtils.isEmpty((String) clist.get(0).get("GEADE")) ? "" : clist.get(0).get("GEADE").toString());
				map.put("image1", TextUtils.isEmpty((String) clist.get(0).get("IMAGE1")) ? "" : CommUtils.getServerHost() + clist.get(0).get("IMAGE1").toString());
				map.put("image2", TextUtils.isEmpty((String) clist.get(0).get("IMAGE2")) ? "" : CommUtils.getServerHost() + clist.get(0).get("IMAGE2").toString());
				map.put("image3", TextUtils.isEmpty((String) clist.get(0).get("IMAGE3")) ? "" : CommUtils.getServerHost() + clist.get(0).get("IMAGE3").toString());
				map.put("score", TextUtils.isEmpty((String) cclist.get(0).get("SCORE")) ? "" : cclist.get(0).get("SCORE").toString());
				map.put("stime", CommUtils.ObjectTime2String(clist.get(0).get("CREATE_TIME")));
				map.put("title", TextUtils.isEmpty((String) clist.get(0).get("NEW_TITLE")) ? "" : clist.get(0).get("NEW_TITLE").toString());
				map.put("atime", CommUtils.ObjectTime2String(cclist.get(0).get("COM_TIME")));
				map.put("points", TextUtils.isEmpty((String) cclist.get(0).get("POINTS")) ? "" : cclist.get(0).get("POINTS").toString());
				map.put("scoring", TextUtils.isEmpty((String) cclist.get(0).get("SCORING")) ? "" : cclist.get(0).get("SCORING").toString());
				map.put("suggest", TextUtils.isEmpty((String) cclist.get(0).get("SUGGEST")) ? "" : cclist.get(0).get("SUGGEST").toString());
				map.put("type", TextUtils.isEmpty((String) cclist.get(0).get("TYPE")) ? "" : cclist.get(0).get("TYPE").toString());
				map.put("content_level", TextUtils.isEmpty((String) cclist.get(0).get("DP_CONTENT")) ? "" : cclist.get(0).get("DP_CONTENT").toString());
				map.put("language_level", TextUtils.isEmpty((String) cclist.get(0).get("DP_LANGUAGE")) ? "" : cclist.get(0).get("DP_LANGUAGE").toString());
				map.put("writing_level", TextUtils.isEmpty((String) cclist.get(0).get("DP_WRITING")) ? "" : cclist.get(0).get("DP_WRITING").toString());
				map.put("structure_level", TextUtils.isEmpty((String) cclist.get(0).get("DP_STRUCTURE")) ? "" : cclist.get(0).get("DP_STRUCTURE").toString());
				map.put("content_ca", CommUtils.transferCompLevelString(cclist.get(0).get("DP_CONTENT_CA")));
				map.put("language_ca", CommUtils.transferCompLevelString(cclist.get(0).get("DP_LANGUAGE_CA")));
				map.put("writing_ca", CommUtils.transferCompLevelString(cclist.get(0).get("DP_WRITING_CA")));
				map.put("structure_ca", CommUtils.transferCompLevelString(cclist.get(0).get("DP_STRUCTURE_CA")));
				map.put("stage", CommUtils.getGradeStage((String) map.get("grade")));
				map.put("score_stage", CommUtils.getScoreStage((String) map.get("score"), (String) map.get("grade")));
				
				//语音点评获取
				if (cclist.get(0).get("VOICE") != null && !"".equals(cclist.get(0).get("VOICE").toString())) {
					map.put("voice", CommUtils.getServerHost() + cclist.get(0).get("VOICE").toString());
				}else {
					map.put("voice", "");
				}
				//语音点评时长获取
				if (cclist.get(0).get("RECORDTIME") != null && !"".equals(cclist.get(0).get("RECORDTIME").toString())) {
					map.put("recordTime", cclist.get(0).get("RECORDTIME").toString());
				}else {
					map.put("recordTime", "");
				}
				
				/*// 是否收藏
				String collectSql = "select count(id) from collection where udid = '" + udid + "' AND COMP_ID= '" + cid + "'";
				int bool = jt.queryForObject(collectSql, Integer.class);
				map.put("collect", bool > 0 ? "1" : "0");*/
				// 学生头像、学生昵称
				String stuUdid = (String) clist.get(0).get("UDID");
				String stuInfoSql = "select  NICKNAME,HEAD from student where udid = '" + stuUdid + "'";
				List<Map<String, Object>> stuList = jt.queryForList(stuInfoSql);
				if (stuList != null && stuList.size() > 0) {
					map.put("stu_nick", TextUtils.isEmpty((String)stuList.get(0).get("NICKNAME")) ? "" : stuList.get(0).get("NICKNAME").toString());
					map.put("stu_head", CommUtils.judgeUrl(stuList.get(0).get("HEAD")));
				}
				// 满意程度、综合评分、负责态度、专业水平、学生评价时间、评价内容、老师回复内容、
				c.add(map);
				return c;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return c;
		}
		return c;
	}
	
	// 批改作文详情list(学生)
	public List<Map<String, Object>> recommendCom(String cid) {
		List<Map<String, Object>> c = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql = "SELECT * FROM composition WHERE ID=" + cid;
			List<Map<String, Object>> clist = jt.queryForList(sql);
			String sql1 = "SELECT * FROM com_composition WHERE COMP_ID = ?";
			List<Map<String, Object>> cclist = jt.queryForList(sql1, new Object[] { cid });
			String sql2 = "SELECT * FROM recommend_com WHERE COM_ID = ?";
			List<Map<String, Object>> rlist = jt.queryForList(sql2, new Object[] { cid });
			if (clist.size() != 0 && cclist.size() != 0) {
				map.put("ask", TextUtils.isEmpty((String) clist.get(0).get("PROPO")) ? "" : clist.get(0).get("PROPO").toString());
				
				if(rlist.size() != 0 && rlist.size() != 0){
					if (rlist.get(0).get("CONTENT") != null && !rlist.get(0).get("CONTENT").equals("")) {
						String str = rlist.get(0).get("CONTENT").toString();
						map.put("content", CommUtils.deleteKongForGecomposition(str));
					}
				}else {
					if (cclist.get(0).get("CONTENT") != null && !cclist.get(0).get("CONTENT").equals("")) {
						String str = cclist.get(0).get("CONTENT").toString();
						map.put("content", CommUtils.deleteKongForGecomposition(str));
					}
				}
				if (clist.get(0).get("CONTENT") != null && !clist.get(0).get("CONTENT").equals("")) {
					String str = clist.get(0).get("CONTENT").toString();
					map.put("word_count", str.length());
				}
				
				map.put("draft", TextUtils.isEmpty((String) clist.get(0).get("DRAFT")) ? "" : clist.get(0).get("DRAFT").toString());
				map.put("grade", TextUtils.isEmpty((String) clist.get(0).get("GEADE")) ? "" : clist.get(0).get("GEADE").toString());
				map.put("image1", TextUtils.isEmpty((String) clist.get(0).get("IMAGE1")) ? "" : CommUtils.getServerHost() + clist.get(0).get("IMAGE1").toString());
				map.put("image2", TextUtils.isEmpty((String) clist.get(0).get("IMAGE2")) ? "" : CommUtils.getServerHost() + clist.get(0).get("IMAGE2").toString());
				map.put("image3", TextUtils.isEmpty((String) clist.get(0).get("IMAGE3")) ? "" : CommUtils.getServerHost() + clist.get(0).get("IMAGE3").toString());
				map.put("score", TextUtils.isEmpty((String) cclist.get(0).get("SCORE")) ? "" : cclist.get(0).get("SCORE").toString());
				map.put("stime", CommUtils.ObjectTime2String(clist.get(0).get("CREATE_TIME")));
				map.put("title", TextUtils.isEmpty((String) clist.get(0).get("NEW_TITLE")) ? "" : clist.get(0).get("NEW_TITLE").toString());
				map.put("atime", CommUtils.ObjectTime2String(cclist.get(0).get("COM_TIME")));
				map.put("points", TextUtils.isEmpty((String) cclist.get(0).get("POINTS")) ? "" : cclist.get(0).get("POINTS").toString());
				map.put("scoring", TextUtils.isEmpty((String) cclist.get(0).get("SCORING")) ? "" : cclist.get(0).get("SCORING").toString());
				map.put("suggest", TextUtils.isEmpty((String) cclist.get(0).get("SUGGEST")) ? "" : cclist.get(0).get("SUGGEST").toString());
				map.put("type", TextUtils.isEmpty((String) cclist.get(0).get("TYPE")) ? "" : cclist.get(0).get("TYPE").toString());
				map.put("content_level", TextUtils.isEmpty((String) cclist.get(0).get("DP_CONTENT")) ? "" : cclist.get(0).get("DP_CONTENT").toString());
				map.put("language_level", TextUtils.isEmpty((String) cclist.get(0).get("DP_LANGUAGE")) ? "" : cclist.get(0).get("DP_LANGUAGE").toString());
				map.put("writing_level", TextUtils.isEmpty((String) cclist.get(0).get("DP_WRITING")) ? "" : cclist.get(0).get("DP_WRITING").toString());
				map.put("structure_level", TextUtils.isEmpty((String) cclist.get(0).get("DP_STRUCTURE")) ? "" : cclist.get(0).get("DP_STRUCTURE").toString());
				map.put("content_ca", CommUtils.transferCompLevelString(cclist.get(0).get("DP_CONTENT_CA")));
				map.put("language_ca", CommUtils.transferCompLevelString(cclist.get(0).get("DP_LANGUAGE_CA")));
				map.put("writing_ca", CommUtils.transferCompLevelString(cclist.get(0).get("DP_WRITING_CA")));
				map.put("structure_ca", CommUtils.transferCompLevelString(cclist.get(0).get("DP_STRUCTURE_CA")));
				map.put("stage", CommUtils.getGradeStage((String) map.get("grade")));
				map.put("score_stage", CommUtils.getScoreStage((String) map.get("score"), (String) map.get("grade")));
				
				//语音点评获取
				if (cclist.get(0).get("VOICE") != null && !"".equals(cclist.get(0).get("VOICE").toString())) {
					map.put("voice", CommUtils.getServerHost() + cclist.get(0).get("VOICE").toString());
				}else {
					map.put("voice", "");
				}
				//语音点评时长获取
				if (cclist.get(0).get("RECORDTIME") != null && !"".equals(cclist.get(0).get("RECORDTIME").toString())) {
					map.put("recordTime", cclist.get(0).get("RECORDTIME").toString());
				}else {
					map.put("recordTime", "");
				}
				
				/*// 是否收藏
				String collectSql = "select count(id) from collection where udid = '" + udid + "' AND COMP_ID= '" + cid + "'";
				int bool = jt.queryForObject(collectSql, Integer.class);
				map.put("collect", bool > 0 ? "1" : "0");*/
				// 学生头像、学生昵称
				String stuUdid = (String) clist.get(0).get("UDID");
				String stuInfoSql = "select  NICKNAME,HEAD from student where udid = '" + stuUdid + "'";
				List<Map<String, Object>> stuList = jt.queryForList(stuInfoSql);
				if (stuList != null && stuList.size() > 0) {
					map.put("stu_nick", TextUtils.isEmpty((String)stuList.get(0).get("NICKNAME")) ? "" : stuList.get(0).get("NICKNAME").toString());
					map.put("stu_head", CommUtils.judgeUrl(stuList.get(0).get("HEAD")));
				}
				// 满意程度、综合评分、负责态度、专业水平、学生评价时间、评价内容、老师回复内容、
				c.add(map);
				return c;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return c;
		}
		return c;
	}

	// 批改作文详情list(学生)
	public List<Map<String, Object>> findUnCommentComPart(String cid, String udid) {
		List<Map<String, Object>> c = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql = "SELECT * FROM composition WHERE ID=" + cid;
			List<Map<String, Object>> clist = jt.queryForList(sql);
			if (clist != null && clist.size() != 0) {
				if (clist.get(0).get("PROPO") != null && !TextUtils.isEmpty((String) clist.get(0).get("PROPO"))) {
					map.put("ask", clist.get(0).get("PROPO").toString());
				} else {
					map.put("ask", "");
				}
				if (clist.get(0).get("CONTENT") != null && !TextUtils.isEmpty((String) clist.get(0).get("CONTENT"))) {
					map.put("content", clist.get(0).get("CONTENT").toString());
				} else {
					map.put("content", "");
				}
				if (clist.get(0).get("DRAFT") != null && !TextUtils.isEmpty((String) clist.get(0).get("DRAFT"))) {
					map.put("draft", clist.get(0).get("DRAFT").toString());
				} else {
					map.put("draft", "");
				}
				if (clist.get(0).get("GEADE") != null && !TextUtils.isEmpty((String) clist.get(0).get("GEADE"))) {
					map.put("grade", clist.get(0).get("GEADE").toString());
				} else {
					map.put("grade", "");
				}
				if (clist.get(0).get("IMAGE1") != null && !TextUtils.isEmpty((String) clist.get(0).get("IMAGE1"))) {
					map.put("image1", CommUtils.getServerHost() + clist.get(0).get("IMAGE1").toString());
				} else {
					map.put("image1", "");
				}
				if (clist.get(0).get("IMAGE2") != null && !TextUtils.isEmpty((String) clist.get(0).get("IMAGE2"))) {
					map.put("image2", CommUtils.getServerHost() + clist.get(0).get("IMAGE2").toString());
				} else {
					map.put("image2", "");
				}
				if (clist.get(0).get("IMAGE3") != null && !TextUtils.isEmpty((String) clist.get(0).get("IMAGE3"))) {
					map.put("image3", CommUtils.getServerHost() + clist.get(0).get("IMAGE3").toString());
				} else {
					map.put("image3", "");
				}
				map.put("stime", CommUtils.ObjectTime2String(clist.get(0).get("CREATE_TIME")));
				if (clist.get(0).get("NEW_TITLE") != null && !TextUtils.isEmpty((String) clist.get(0).get("NEW_TITLE"))) {
					map.put("title", clist.get(0).get("NEW_TITLE").toString());
				} else {
					map.put("title", "");
				}
				map.put("state", "2");
				// 是否收藏
				String collectSql = "select count(id) from collection where udid = '" + udid + "' AND COMP_ID= '" + cid + "'";
				int bool = jt.queryForObject(collectSql, Integer.class);
				map.put("collect", bool > 0 ? "1" : "0");
				// 学生头像、学生昵称
				String stuUdid = (String) clist.get(0).get("UDID");
				String stuInfoSql = "select  NICKNAME ,HEAD from student where udid = '" + stuUdid + "'";
				List<Map<String, Object>> stuList = jt.queryForList(stuInfoSql);
				if (stuList != null && stuList.size() > 0) {
					if (stuList.get(0).get("NICKNAME") != null) {
						map.put("stu_nick", stuList.get(0).get("NICKNAME").toString());
					}else {
						map.put("stu_head", "");
					}
					if (stuList.get(0).get("HEAD") != null) {
						map.put("stu_head", stuList.get(0).get("HEAD").toString());
					}else {
						map.put("stu_head", "");
					}
				}
				c.add(map);
				//判断是否是老师，是否指定老师，修改作文状态为不可更换老师
				String sql3 = "SELECT t.ID,s.TOTEACHER state FROM teacher t,composition c,s_order s WHERE t.UDID =? AND c.ID = ? AND c.ID = s.COMP_ID";
				List<Map<String,Object>> list = jt.queryForList(sql3, new Object[]{udid, cid});
				boolean b1 = list.size() > 0;
				boolean b2 = list.get(0).get("ID") != null && !"".equals(list.get(0).get("ID").toString());
				boolean b3 = list.get(0).get("state") != null && "0".equals(list.get(0).get("state").toString());
				if (b1 && b2 && b3) {
					String sql2 = "UPDATE composition SET TACHER_STATE = 1 WHERE ID = ?";
					jt.update(sql2, new Object[]{cid});
				}
				return c;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return c;
		}
		return c;
	}

	// 作文评分标准list(学生)
	public List<Map<String, Object>> findComStandard(String grade) {
		List<Map<String, Object>> c = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql1 = "SELECT * FROM standard WHERE GRADE= ?";
			List<Map<String, Object>> s = jt.queryForList(sql1, new Object[] { grade });
			if (s.size() != 0) {
				map.put("content", s.get(0).get("CONTENT"));
				map.put("language", s.get(0).get("LANGUAGE"));
				map.put("structure", s.get(0).get("STRUCTURE"));
				c.add(map);
				return c;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return c;
		}
		return c;
	}

	// 老师list(学生)
	public List<Map<String, Object>> findTeacher(String cid) {
		List<Map<String, Object>> c = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql = "SELECT TEACHER_ID FROM com_composition WHERE COMP_ID= ?";
			String teacherid = (String) jt.queryForObject(sql, new Object[] { cid }, String.class);
			String sql1 = "SELECT NAME,HAND_URL FROM teacher WHERE  ID= ?";
			List<Map<String, Object>> tlist = jt.queryForList(sql1, new Object[] { teacherid });
			if (tlist.size() != 0) {
				map.put("name", tlist.get(0).get("NAME"));
				if (tlist.get(0).get("HAND_URL") == (null)) {
					map.put("url", "");
				} else {
					map.put("url", tlist.get(0).get("HAND_URL"));
				}
				c.add(map);
				return c;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return c;
		}
		return c;
	}

	/**
	 * 根据评点作文找到老师
	 * 
	 * @param cid
	 * @return
	 */
	public List<Map<String, Object>> findTeacherByCompId(String cid) {
		List<Map<String, Object>> c = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql = "SELECT TEACHER_ID,COM_TIME FROM com_composition WHERE COMP_ID= ?";
			List<Map<String, Object>> idAndTime = jt.queryForList(sql, new Object[] { cid });
			String time = null;
			Object teacherid = "";
			if (idAndTime != null && idAndTime.size() > 0) {
				teacherid = idAndTime.get(0).get("TEACHER_ID");
				time = CommUtils.ObjectTime2String(idAndTime.get(0).get("COM_TIME"));
			}
			String sql1 = "SELECT NAME,HAND_URL FROM teacher WHERE  ID= ?";
			List<Map<String, Object>> tlist = jt.queryForList(sql1, new Object[] { teacherid });
			if (tlist.size() != 0) {
				map.put("name", tlist.get(0).get("NAME"));
				map.put("tid", teacherid);
				map.put("time", time == null ? "" : time.toString());
				map.put("url", CommUtils.judgeUrl(tlist.get(0).get("HAND_URL")));
				c.add(map);
				return c;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return c;
		}
		return c;
	}

	@Override
	public List<Map<String, Object>> findOrderState(String udid, int cid, int orderId, String out_trade_no) {
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sql = "SELECT o.ID, o.STATE, o.TEACHER_ID, o.COMP_TITLE, o.BACK_TIME FROM s_order o, student s WHERE o.COMP_ID= ? and o.STU_ID = s.ID and s.UDID = ? and o.ID = ? and o.OUT_TRADE_NO = ?";
			List<Map<String, Object>> tlist = jt.queryForList(sql, new Object[] { cid, udid, orderId, out_trade_no });
			if (tlist.size() != 0) {
				map.put("state", tlist.get(0).get("STATE"));
				map.put("backTime", CommUtils.ObjectTime2String(tlist.get(0).get("BACK_TIME")));
				map.put("id", tlist.get(0).get("ID"));
				map.put("tid", tlist.get(0).get("TEACHER_ID"));
				map.put("title", tlist.get(0).get("COMP_TITLE"));
				list.add(map);
				return list;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return list;
		}
		return list;
	}

	@Override
	public int alterOrderState(int state, String udid, int cid, int orderId, String out_trade_no) {
		int rest = 0;
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String ctime = dateFormat.format(now);
		try {
			String sql = "update s_order set STATE = ? , BACK_TIME = ?  WHERE OUT_TRADE_NO = ?";
			int status = jt.update(sql, new Object[] { state, ctime, out_trade_no });
			if (status > 0) {
				return status;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return rest;
		}
		return rest;
	}

	@Override
	public List<Map<String, Object>> findRefundOrderInfo(String stuUdid, int orderId) {
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			String sql = "SELECT r.ID from refund r, student s where r.ORDER_ID = ? and r.STU_ID = s.ID and s.UDID = ?";
			List<Map<String, Object>> tlist = jt.queryForList(sql, new Object[] { orderId, stuUdid });
			if (tlist != null && tlist.size() > 0) {
				list = tlist;
				return list;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return list;
		}
		return list;
	}

	@Override
	public int updateRefundOrderInfo(int state, String stuUdid, int orderID, int teaId, String title) {
		int rest = 0;
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String ctime = dateFormat.format(now);
		try {
			String sql = "SELECT ID from student where UDID = ?";
			List<Map<String, Object>> slist = jt.queryForList(sql, new Object[] { stuUdid });
			if (slist != null && slist.size() > 0) {
				String sql2 = "update refund set STATE = ?,CREATE_TIME = ?  where ORDER_ID = ? and STU_ID = ?";
				rest = jt.update(sql2, new Object[] { state, ctime, orderID, slist.get(0).get("ID") });
				addRefundMessage(state, teaId, title);
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return rest;
		}
		return rest;
	}

	/**
	 * 
	 * @param state
	 *            1、退款中 2、已完成退款 3、拒绝退款 4、 取消退款
	 * @param teaId
	 * @param title
	 */
	private void addRefundMessage(int state, int teaId, String title) {
		// 1、4
		// 12、作文被申请退款 state 2 icon 6 content
		// 48小时未完成点评学生申请退款提示：《作文题目》48小时内未完成点评，学生已经申请退款，请到退款作文中查看记录。
		// 13、作文被取消退款 state 2 icon 6 content 学生取消了作文《作文题目》的退款申请，该作文已重新进入待点评，请您尽快点评！
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = dateFormat.format(now);// 时间
		if (state == 1) { // 作文被申请退款
			String sql3 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,?,?)";
			jt.update(sql3, new Object[] { "作文被申请退款", "48小时未完成点评学生申请退款提示：《" + title + "》48小时内未完成点评，学生已经申请退款，请到退款作文中查看记录。", ctime, teaId, 2, 6 }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
		} else if (state == 4) {// 作文被取消退款
			String sql3 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,?,?)";
			jt.update(sql3, new Object[] { "作文被取消退款", "学生取消了作文《" + title + "》的退款申请，该作文已重新进入待点评，请您尽快点评！", ctime, teaId, 2, 7 }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
		}
	}

	@Override
	public int addRefundOrderInfo(String udid, int orderID) {
		int rest = 0;
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = dateFormat.format(now);
		try {
			String sql = "SELECT o.BUYER_PAY_AMOUNT, o.TEACHER_ID,o.COMP_TITLE,o.PAYMENT,o.TOTAL_AMOUNT,o.COMP_ID from s_order o, student s where s.UDID = ? and o.ID = ? and s.ID = o.STU_ID";
			List<Map<String, Object>> slist = jt.queryForList(sql, new Object[] { udid, orderID });
			String sql3 = "insert into refund ( MONEY, CREATE_TIME, TEACHER_ID, COMP_TITLE, PAYMENT, ORDER_PRICE, STATE, STU_ID, ORDER_ID, COM_ID,OUT_REQUEST_NO)values(?,?,?,?,?,?,?,?,?,?,?)";

			String stuSql = "SELECT ID from student where UDID = ?";
			List<Map<String, Object>> stuList = jt.queryForList(stuSql, new Object[] { udid });
			if (stuList != null && stuList.size() > 0 && slist != null && slist.size() > 0) {
				Map<String, Object> map = slist.get(0);
				rest = jt.update(sql3, new Object[] { map.get("BUYER_PAY_AMOUNT"), dateFormat.parse(ctime), map.get("TEACHER_ID"), map.get("COMP_TITLE"), map.get("PAYMENT"), map.get("TOTAL_AMOUNT"), 1, stuList.get(0).get("ID"), orderID, map.get("COMP_ID"), CommUtils.getOutTradeNo() }, new int[] { Types.DECIMAL, Types.TIMESTAMP, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.DECIMAL, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.VARCHAR });
				addRefundMessage(1, (int) map.get("TEACHER_ID"), (String) map.get("COMP_TITLE"));
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return rest;
		}
		return rest;
	}

	/* (non Javadoc) 
	 * @Title: updateCompositionShow
	 * @Description: TODO
	 * @param cid 
	 * @see com.rest.service.dao.ComStateDao#updateCompositionShow(java.lang.String) 
	 */
	@Override
	public Map<String, String> updateCompositionShow(String cid) {
		String sql = "SELECT CONTENT,DRAFT,PROPO,NEW_TITLE,OLD_TITLE,IMAGE1,IMAGE2,IMAGE3 from composition WHERE ID = ?";
		Map<String, String> map = new HashMap<>();
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{cid});
		if (list != null && list.size() > 0) {
			if (list.get(0).get("CONTENT") != null) {
				String str = list.get(0).get("CONTENT").toString();
				//map.put("content", CommUtils.deleteKongGe(str));
				map.put("content", str);
			}
			if (list.get(0).get("DRAFT") != null) {
				String str = list.get(0).get("DRAFT").toString();
				map.put("draft", str);
			}
			if (list.get(0).get("PROPO") != null) {
				String str = list.get(0).get("PROPO").toString();
				map.put("propo", str);
			}
			if (list.get(0).get("NEW_TITLE") != null) {
				String str = list.get(0).get("NEW_TITLE").toString();
				map.put("newtitle", str);
			}
			if (list.get(0).get("OLD_TITLE") != null) {
				String str = list.get(0).get("OLD_TITLE").toString();
				map.put("oldtitle", str);
			}
			if (list.get(0).get("IMAGE1") != null && !"".equals(list.get(0).get("IMAGE1").toString())) {
				String str = list.get(0).get("IMAGE1").toString();
				map.put("image1", str);
			}
			if (list.get(0).get("IMAGE2") != null && !"".equals(list.get(0).get("IMAGE2").toString())) {
				String str = list.get(0).get("IMAGE2").toString();
				map.put("image2", str);
			}
			if (list.get(0).get("IMAGE3") != null && !"".equals(list.get(0).get("IMAGE3").toString())) {
				String str = list.get(0).get("IMAGE3").toString();
				map.put("image3", str);
			}
		}
		return map;
	}

	@Override
	public int deleteComposition(String udid, String cid) {
		String sql = "DELETE FROM composition WHERE id = ? AND TACHER_STATE = 0 AND UDID = ?"; 
		int i = jt.update(sql, new Object[]{cid, udid});
		return i;
	}

	@Override
	public int changeTeacher(String cid, String newTid, String oldTid, String udid, String oldTname, String title) {
		String start = "SELECT TACHER_STATE FROM composition WHERE ID = ?";
		Integer integer = jt.queryForObject(start, new Object[]{cid}, Integer.class);
		if (integer == 1) {
			return 3;
		}
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = dateFormat.format(now);
		String sql = "SELECT NICKNAME,ID FROM student WHERE UDID = ?";
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{udid});
		Object sname = list.get(0).get("NICKNAME");
		Object sid = list.get(0).get("ID");
		String sq6 = "SELECT NAME FROM teacher WHERE ID = ?";
		String newTname = jt.queryForObject(sq6, new Object[]{newTid}, String.class);
		String content1 = "您的这篇《"+title+"》，点评老师已成功更换为"+newTname+"，请耐心等待点评结果。";
		String content4 = "您的这篇《"+title+"》，已成功选择"+newTname+"老师，请耐心等待点评结果。";
		String content2 = "学生"+sname+"已经把《"+title+"》这篇作文给了另外一个老师批改，请等待下次批改机会。";
		String content3 = "您已收到" + sname + "同学的一篇作文，请在48小时内为其点评！";
		
		String sql1 = "UPDATE composition SET TID = ? WHERE ID = ?";
		String sql2 = "UPDATE s_order SET TEACHER_ID = ?,TOTEACHER = 0  WHERE COMP_ID = ?";
		String sql3 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,2,11)";
		String sql4 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,1,11)";
		String sql5 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,2,7)";
		int i = jt.update(sql1, new Object[]{newTid, cid});
		if (i <= 0) {
			return 0;
		}
		i = jt.update(sql2, new Object[]{newTid, cid});
		if (i <= 0) {
			return 0;
		}
		if (oldTid == null || oldTid.equals("")) {
			jt.update(sql4, new Object[] { "点评老师已选择", content4, ctime, sid});
		}else {
			jt.update(sql3, new Object[] { "点评老师已更换", content2, ctime, oldTid});
			jt.update(sql4, new Object[] { "点评老师已更换", content1, ctime, sid});
		}
		jt.update(sql5, new Object[] { "被邀请点评", content3, ctime, newTid});
		return i;
	}

	@Override
	public List<Map<String, Object>> findCompositionDraft(String udid,
			String numPerPage, String currentPage) {
		List<Map<String,Object>> list = new ArrayList<>();
		int num = Integer.parseInt(numPerPage) * (Integer.parseInt(currentPage) - 1);
		int count = Integer.parseInt(numPerPage);
		String sql = "SELECT IF (IMAGE1 = '', 0, 1) comType, c.id cid,c.DRAFT draft,c.CREATE_TIME time,c.NEW_TITLE title,c.TACHER_STATE teacher_state,c.TID tid FROM composition c	WHERE c.udid = ? AND c.STATE = 0 ORDER BY time DESC limit ?,?";
		try {
			list = jt.queryForList(sql, new Object[]{udid, num, count});
			String tname = "";
			String price = "";
			String tid = "";
			String turl = "";
			for (Map<String, Object> map : list) {
				map.put("time", CommUtils.ObjectTime2String(map.get("time")));
				if (map.get("TID") != null && !map.get("TID").toString().equals("")) {
					tid = map.get("TID").toString();
					String sql2 = "SELECT NAME,PRICE,HAND_URL FROM teacher WHERE ID = ?";
					List<Map<String,Object>> list2 = jt.queryForList(sql2, new Object[]{tid});
					tname = list2.get(0).get("NAME").toString();
					price = list2.get(0).get("PRICE").toString();
					turl = CommUtils.judgeUrl(list2.get(0).get("HAND_URL"));
					map.put("hasTeacher", "0");
				}else {
					map.put("hasTeacher", "1");
				}
				map.put("tname", tname);
				map.put("price", price);
				map.put("tid", tid);
				map.put("turl", turl);
			}
			return list;
		} catch (Exception e) {
			return null;
		}
		
	}

}
