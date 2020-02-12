package com.rest.service.dao.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.entity.BaseSqlResultBean;
import com.entity.Composition;
import com.entity.Teacher;
import com.rest.service.dao.UploadDao;
import com.rest.service.impl.UploadServiceImpl;
import com.util.ComPicUtils;
import com.util.CommUtils;

public class UploadDaoImpl implements UploadDao {

	private JdbcTemplate jt;

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

	// 上传作文
	public BaseSqlResultBean insertComposi(final Composition comp) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String ctime = dateFormat.format(now);
		final String sql1 = "insert into composition(UDID,CONTENT,DRAFT,NEW_TITLE,OLD_TITLE,PROPO,CREATE_TIME,IMAGE1,IMAGE2,IMAGE3,AUTHOR,GEADE,STATE,TYPE,CSTATE,TID,OPEN) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		// int i = jt.update(sql1, new Object[] { comp.getUdid(), comp.getContent(),
		// comp.getDraft(), comp.getNewtitle(), comp.getOldtitle(), comp.getPropo(),
		// ctime, comp.getImage1(), comp.getImage2(), comp.getImage3(),
		// comp.getAuthor(), comp.getGrade(), comp.getState(), comp.getType(),
		// comp.getCstate() }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
		// Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
		// Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
		// Types.VARCHAR, Types.VARCHAR });
		BaseSqlResultBean resultBean = new BaseSqlResultBean();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int i = jt.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, comp.getUdid());
				ps.setString(2, comp.getContent());
				ps.setString(3, comp.getDraft());
				ps.setString(4, comp.getNewtitle());
				ps.setString(5, comp.getOldtitle());
				ps.setString(6, comp.getPropo());
				ps.setString(7, ctime);
				ps.setString(8, comp.getImage1());
				ps.setString(9, comp.getImage2());
				ps.setString(10, comp.getImage3());
				ps.setString(11, comp.getAuthor());
				ps.setString(12, comp.getGrade());
				ps.setString(13, comp.getState());
				ps.setString(14, comp.getType());
				ps.setInt(15, comp.getCstate());
				ps.setString(16, comp.getTid());
				ps.setBoolean(17, comp.isOpen());
				return ps;
			}
		}, keyHolder);
		int autoID = keyHolder.getKey().intValue();
		resultBean.setSqlCode(i);
		resultBean.setSqlID(autoID);
		return resultBean;
	}

	// 上传师资认证
	public int insertAuthent(Teacher tea) {
		String sql = "update teacher set CERT_URL=?,CARD1=?,CARD2=? where UDID=?";
		int i = jt.update(sql, new Object[] { tea.getCerturl(), tea.getCard1(), tea.getCard2(), tea.getUdid() }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR });
		// 判断 师资认证 资料是否上传完整
		String sql1 = "select * from teacher where UDID='" + tea.getUdid() + "'";
		List<Map<String, Object>> forList = jt.queryForList(sql1);
		if (forList.size() > 0) {
			if (forList.get(0).get("CERT_URL") != null && forList.get(0).get("CARD1") != null && forList.get(0).get("CARD2") != null) {
				Date now = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = dateFormat.format(now);// 上传资料时间
				String sql2 = "update teacher set AUSTATE = 0 , UPLOAD_TIME=? where UDID=?";
				jt.update(sql2, new Object[] { time, tea.getUdid() }, new int[] { Types.TIMESTAMP, Types.VARCHAR });
			}
		}
		return i;
	}

	@Override
	public List<Map<String, Object>> getTeacherAuthInfo(String udid) {
		List<Map<String, Object>> list = new ArrayList<>();
		String sql = "select CERT_URL, CARD1, CARD2, AUSTATE from teacher where UDID='" + udid + "'";
		List<Map<String, Object>> forList = jt.queryForList(sql);
		Map<String, Object> map = new HashMap<>();
		if (forList != null && forList.size() > 0) {
			if (forList.get(0).get("AUSTATE") != null) {
				map.put("auState", forList.get(0).get("AUSTATE"));
			} else {
				map.put("auState", "");
			}
			if (forList.get(0).get("CARD1") != null) {
				map.put("card1", CommUtils.getServerHost() + forList.get(0).get("CARD1"));
			} else {
				map.put("card1", "");
			}
			if (forList.get(0).get("CARD2") != null) {
				map.put("card2", CommUtils.getServerHost() + forList.get(0).get("CARD2"));
			} else {
				map.put("card2", "");
			}
			if (forList.get(0).get("CERT_URL") != null) {
				map.put("certUrl", CommUtils.getServerHost() + forList.get(0).get("CERT_URL"));
			} else {
				map.put("certUrl", "");
			}
			list.add(map);
		}
		return list;
	}

	@Override
	public int updateComposi(Composition comp) {
		String sql = "UPDATE composition SET CONTENT = ? , DRAFT = ? , IMAGE1 = ? , IMAGE2 = ? , IMAGE3 = ? , NEW_TITLE = ? , OLD_TITLE = ? , PROPO = ? WHERE ID = ?";
		int i = jt.update(sql, new Object[]{comp.getContent(), comp.getDraft(), comp.getImage1(), comp.getImage2(), comp.getImage3(), comp.getNewtitle(), comp.getOldtitle(), comp.getPropo(), comp.getId()});
		return i;
	}
	@Override
	public int updateComposi2(Composition comp) {
		String sql = "UPDATE composition SET CONTENT = ? , DRAFT = ? , NEW_TITLE = ? , PROPO = ? WHERE ID = ?";
		int i = jt.update(sql, new Object[]{comp.getContent(), comp.getDraft(), comp.getNewtitle(), comp.getPropo(), comp.getId()});
		return i;
	}

	//查询是否已暂存批改
	@Override
	public int selectCom_composition(String udid, String cid) {
		int i = 0;
		String sql = "SELECT * FROM com_composition c,teacher t WHERE c.TEACHER_ID=t.ID AND t.UDID=? AND c.COMP_ID=?";
		List<Map<String, Object>> list = jt.queryForList(sql, new Object[]{udid, cid});
		if(list.size()>0){
			i=1;
		}
		return i;
	}
	/* (non Javadoc) 
	 * @Title: insertCom_compositionBySpeech
	 * @Description: TODO
	 * @param cid
	 * @param voice 
	 * @see com.rest.service.dao.UploadDao#insertCom_compositionBySpeech(java.lang.String, java.lang.String) 
	 */
	@Override
	public int insertCom_compositionBySpeech(String cid, String voice, String score, String suggest, String recordTime) {
		String sql1 = "SELECT c.NEW_TITLE title,c.TID,s.ID SID,c.CONTENT,UNIX_TIMESTAMP(o.GMT_PAYMENT) time,t.AVG_TIME avg_time,c.GEADE grade,c.IMAGE1 image1,c.IMAGE2 image2,c.IMAGE3 image3 from composition c,student s,s_order o,teacher t WHERE c.UDID = s.UDID AND o.COMP_ID = c.ID AND c.TID = t.ID AND c.ID = ?";
		List<Map<String, Object>> list = jt.queryForList(sql1, new Object[]{cid});
		Object tid = list.get(0).get("TID");
		Object sid = list.get(0).get("SID");
		Object content = list.get(0).get("CONTENT"); 
		Object title = list.get(0).get("title"); 
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String ctime = df.format(d);
		//根绝年级分数计算几类文
		String grading = null;
		try {
			String grade = list.get(0).get("grade").toString(); 
			grading = CommUtils.getCompositionLeveByScoreAndGrade(score, grade);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//图片
		Object image1 = null;
		Object image2 = null;
		Object image3 = null;
		if (list.get(0).get("image1") != null && !list.get(0).get("image1").toString().equals("")) {
			image1 = list.get(0).get("image1"); 
		}
		if (list.get(0).get("image2") != null && !list.get(0).get("image2").toString().equals("")) {
			image2 = list.get(0).get("image2"); 
		}
		if (list.get(0).get("image3") != null && !list.get(0).get("image3").toString().equals("")) {
			image3 = list.get(0).get("image3"); 
		}
		//状态值
		String state = "3";
		String sql2 = "INSERT INTO com_composition SET COMP_ID = ?, TEACHER_ID = ?, STU_ID = ?, COM_TIME = ?, STATE = ?, VOICE = ?, SUGGEST = ?, SCORE = ?, CONTENT = ?, RECORDTIME = ?, GRADING = ?, COM_IMAGE1 = ?, COM_IMAGE2 = ?, COM_IMAGE3 = ?";
		int i = jt.update(sql2, new Object[]{cid, tid, sid, ctime, state, voice, suggest, score, content, recordTime, grading, image1, image2, image3});
		if (i > 0) {
			String sql3 = "UPDATE composition SET STATE = 3 WHERE ID = ?";
			i = jt.update(sql3, new Object[]{cid});
			if (i > 0) {
				String sql4 = "UPDATE s_order SET STATE = 3 WHERE COMP_ID = ?";
				i = jt.update(sql4, new Object[]{cid});
			}
			if (i > 0) {
				String avg_time = "";
				if (list.get(0).get("avg_time") != null) {
					avg_time = list.get(0).get("avg_time").toString(); 
				}
				long time = (System.currentTimeMillis() /1000 - (long)list.get(0).get("time")); 
				long lavg_time = CommUtils.stringTimeToLong(avg_time);
				long l = CommUtils.getAvgBetween2Long(lavg_time, time);
				String str = CommUtils.longTimeToString(l);
				String sql5 = "UPDATE teacher SET AVG_TIME = ? WHERE ID = ?";
				jt.update(sql5, new Object[]{str, tid});
			}
			if (i > 0) {
				String a = "您的作文《" + title + "》老师已经给出了详细的点评，请到已点评中查看哦！";
				String sql6 = "insert into information (INF_CONTENT,CREATE_TIME,STU_ID,DELETE_STATE,STATE,ICON,TITLE)" + " values ('" + a + "','" + ctime + "'," + sid + ",0,1,'1','作文已评点') ";
				jt.update(sql6);
			}
		}
		return i;
	}

	@Override
	public void insertInformation(String udid, String tid) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = dateFormat.format(now);
		
		String sql1 = "SELECT NICKNAME FROM student WHERE UDID = ?";
		String sname = jt.queryForObject(sql1, new Object[]{udid}, String.class);
		String content = "您已收到" + sname + "同学的一篇作文，请在48小时内为其点评";
		String sql2 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,2,7)";
		jt.update(sql2, new Object[] { "被邀请点评", content, ctime, tid});
	}

	@Override
	public String findGradeByUdid(String udid) {
		String sql = "SELECT GRADE FROM student WHERE UDID = ?";
		String string = "";
		try {
			string = jt.queryForObject(sql, new Object[]{udid}, String.class);
		} catch (Exception e) {
			logger.info("--------年级查询异常-------udid=" + udid);
		}
		return string;
	}
	private static Logger logger = Logger.getLogger(UploadDaoImpl.class);

	@Override
	public int insertComPic(String data, String comPicUrl, String imageIndex, String compId, int oldwidth, int oldheight, int newwidth, int newheight) {
		Object sid = null;
		Object tid = null;
		String sql2 = "SELECT s.ID sid,c.TID tid FROM composition c, student s WHERE c.ID = ? AND s.UDID = c.UDID";
		List<Map<String,Object>> list2 = jt.queryForList(sql2, new Object[]{compId});
		if (list2 != null && list2.size() > 0) {
			sid = list2.get(0).get("sid");
			tid = list2.get(0).get("tid");
		}
		
		String sql = "UPDATE com_composition SET COM_IMAGE"+imageIndex+" = ? WHERE COMP_ID = ?";
		int i = jt.update(sql, new Object[]{comPicUrl, compId});
		if (i <= 0) {
			String sql3 = "insert into com_composition set COMP_ID = ?, TEACHER_ID = ?, STU_ID = ?,  STATE = 2, COM_TIME = null, COM_IMAGE"+imageIndex+" = ?";
			jt.update(sql3, new Object[]{compId, tid, sid, comPicUrl});
		}
		try {
			String sql3 = "INSERT INTO comment (STU_ID,TEA_ID,COMP_ID,COM_IMAGE_ID,CONTENT,COORDS_X1,COORDS_Y1,COORDS_X2,COORDS_Y2) VALUES (?,?,?,?,?,?,?,?,?)";
			List<Map<String,String>> list = ComPicUtils.readJsonFromFile(data);
			for (Map<String, String> map : list) {
				int x1 = Integer.parseInt(map.get("cords_x1"));
				int y1 = Integer.parseInt(map.get("cords_y1"));
				int x2 = Integer.parseInt(map.get("cords_x2"));
				int y2 = Integer.parseInt(map.get("cords_y2"));
				x1 = ComPicUtils.turnPixel(oldwidth, newwidth, x1);
				x2 = ComPicUtils.turnPixel(oldwidth, newwidth, x2);
				y1 = ComPicUtils.turnPixel(oldheight, newheight, y1);
				y2 = ComPicUtils.turnPixel(oldheight, newheight, y2);
				jt.update(sql3, new Object[]{sid, tid, compId, imageIndex, map.get("content"), x1, y1, x2, y2});
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return 0;
	}

	//修改点评内容
	@Override
	public int updateCom_compositionBySpeech(String cid, String voice, String score, String suggest,
			String recordTime) {
		String sql1 = "SELECT c.NEW_TITLE title,c.TID,s.ID SID,c.CONTENT,UNIX_TIMESTAMP(o.GMT_PAYMENT) time,t.AVG_TIME avg_time,c.GEADE grade,c.IMAGE1 image1,c.IMAGE2 image2,c.IMAGE3 image3 from composition c,student s,s_order o,teacher t WHERE c.UDID = s.UDID AND o.COMP_ID = c.ID AND c.TID = t.ID AND c.ID = ?";
		List<Map<String, Object>> list = jt.queryForList(sql1, new Object[]{cid});
		Object tid = list.get(0).get("TID");
		Object sid = list.get(0).get("SID");
		Object content = list.get(0).get("CONTENT"); 
		Object title = list.get(0).get("title"); 
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String ctime = df.format(d);
		//根绝年级分数计算几类文
		String grading = null;
		try {
			String grade = list.get(0).get("grade").toString(); 
			grading = CommUtils.getCompositionLeveByScoreAndGrade(score, grade);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//图片
		Object image1 = null;
		Object image2 = null;
		Object image3 = null;
		if (list.get(0).get("image1") != null && !list.get(0).get("image1").toString().equals("")) {
			image1 = list.get(0).get("image1"); 
		}
		if (list.get(0).get("image2") != null && !list.get(0).get("image2").toString().equals("")) {
			image2 = list.get(0).get("image2"); 
		}
		if (list.get(0).get("image3") != null && !list.get(0).get("image3").toString().equals("")) {
			image3 = list.get(0).get("image3"); 
		}
		//状态值
		String state = "3";
		String sql2 = "UPDATE com_composition SET TEACHER_ID = ?, STU_ID = ?, COM_TIME = ?, STATE = ?, VOICE = ?, SUGGEST = ?, SCORE = ?, CONTENT = ?, RECORDTIME = ?, GRADING = ?, COM_IMAGE1 = ?, COM_IMAGE2 = ?, COM_IMAGE3 = ? WHERE COMP_ID = ? ";
		int i = jt.update(sql2, new Object[]{tid, sid, ctime, state, voice, suggest, score, content, recordTime, grading, image1, image2, image3, cid});
		if (i > 0) {
			String sql3 = "UPDATE composition SET STATE = 3 WHERE ID = ?";
			i = jt.update(sql3, new Object[]{cid});
			if (i > 0) {
				String sql4 = "UPDATE s_order SET STATE = 3 WHERE COMP_ID = ?";
				i = jt.update(sql4, new Object[]{cid});
			}
			if (i > 0) {
				String avg_time = "";
				if (list.get(0).get("avg_time") != null) {
					avg_time = list.get(0).get("avg_time").toString(); 
				}
				long time = (System.currentTimeMillis() /1000 - (long)list.get(0).get("time")); 
				long lavg_time = CommUtils.stringTimeToLong(avg_time);
				long l = CommUtils.getAvgBetween2Long(lavg_time, time);
				String str = CommUtils.longTimeToString(l);
				String sql5 = "UPDATE teacher SET AVG_TIME = ? WHERE ID = ?";
				jt.update(sql5, new Object[]{str, tid});
			}
			if (i > 0) {
				String a = "您的作文《" + title + "》老师已经给出了详细的点评，请到已点评中查看哦！";
				String sql6 = "insert into information (INF_CONTENT,CREATE_TIME,STU_ID,DELETE_STATE,STATE,ICON,TITLE)" + " values ('" + a + "','" + ctime + "'," + sid + ",0,1,'1','作文已评点') ";
				jt.update(sql6);
			}
		}
		return i;
	}
}
