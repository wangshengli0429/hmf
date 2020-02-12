package com.rest.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.entity.Student;
import com.entity.Teacher;
import com.rest.service.dao.RegistDao;

public class RegistDaoImpl implements RegistDao {

	public int insertStudent(final Student student, final String token) {
		// 学生注册
		List<Map<String, Object>> boollog = null;
		final int idResult;
		try {
			String sql5 = "select * from student where phone = ?";
			boollog = jt.queryForList(sql5, new Object[] { student.getPhone() });
			// if(boollog.size() !=0){
			// String sql7 = "select * from student where phone = ?";
			// List<Map<String, Object>> u = jt.queryForList(sql7, new Object[] {
			// student.getPhone()});
			// }
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				boollog = null;
		}
		if (boollog.size() == 0) {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			final String ctime = dateFormat.format(now);
			final String sql1 = "insert into login(NAME,LOGIN_TIME,LOGINSTATE,token,LOGINTYPE,CREATED_TIME,PHONE)values(?,?,?,?,?,?,?)";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jt.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql1, PreparedStatement.RETURN_GENERATED_KEYS);
					ps.setObject(1, student.getNickname());
					ps.setObject(2, ctime);
					ps.setObject(3, "2");
					ps.setObject(4, token);
					ps.setObject(5, "2");
					ps.setObject(6, ctime);
					ps.setObject(7, student.getPhone());
					return ps;
				}
			}, keyHolder);
			idResult = keyHolder.getKey().intValue();
			if (idResult > 0) {
				// 插入学生表
				final String sql3 = "insert into student(nickname,LOGIN_ID,phone,udid,password,grade,CREATE_TIME,name,OPENID_MSG,OPENID_QQ,MSG_NAME,QQ_NAME,HEAD,UNIONID_QQ,UNIONID_MSG,USER_TYPE)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				// int rest=jt.update(sql3, new Object[] {
				// student.getNickname(),idResult,student.getPhone(),student.getUdid(),student.getPassword(),student.getGrade(),student.getCreatedTime()
				// }, new int[] {
				// Types.VARCHAR,Types.INTEGER,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP
				// });
				jt.update(new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(sql3, PreparedStatement.RETURN_GENERATED_KEYS);
						ps.setObject(1, student.getNickname());
						ps.setObject(2, idResult);
						ps.setObject(3, student.getPhone());
						ps.setObject(4, student.getUdid());
						ps.setObject(5, student.getPassword());
						ps.setObject(6, student.getGrade());
						ps.setObject(7, student.getCreatedTime());
						ps.setObject(8, student.getName());
						ps.setObject(9, student.getMsgOpenid());
						ps.setObject(10, student.getQqOpenid());
						ps.setObject(11, student.getMsgName());
						ps.setObject(12, student.getQqName());
						ps.setObject(13, student.getHeadurl());
						ps.setObject(14, student.getQqUnionid());
						ps.setObject(15, student.getMsgUnionid());
						ps.setObject(16, student.getUserType());
						return ps;
					}
				}, keyHolder);
				int sid = keyHolder.getKey().intValue();
				if (sid > 0) {
					// 插入消息表
					String sql2 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,?,?)";
					jt.update(sql2, new Object[] { "注册成功", "您已成功注册一堂作文课学生端，可点击首页左上角？对我们的产品做深入了解。", ctime, sid, 1, 2 }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
					return sid;
				}
			}
		}
		return 0;
	}

	public int insertTeacher(final Teacher teacher, final String token, final String deviceType) {
		// 老师注册
		List<Map<String, Object>> boollog = null;
		final int idResult;
		try {
			String sql5 = "select * from teacher where phone = ?";
			boollog = jt.queryForList(sql5, new Object[] { teacher.getPhone() });
			// if(boollog.size() !=0){
			// String sql7 = "select * from student where phone = ?";
			// List<Map<String, Object>> u = jt.queryForList(sql7, new Object[] {
			// student.getPhone()});
			// }
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				boollog = null;
		}
		if (boollog == null || (boollog != null && boollog.size() == 0)) {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			final String ctime = dateFormat.format(now);
			final String sql1 = "insert into login(name,LOGIN_TIME,loginstate,token,logintype,CREATED_TIME,PHONE)values(?,?,?,?,?,?,?)";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jt.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql1, PreparedStatement.RETURN_GENERATED_KEYS);
					ps.setObject(1, teacher.getName());
					ps.setObject(2, ctime);
					ps.setObject(3, "2");
					ps.setObject(4, token);
					ps.setObject(5, "1");// 老师1，学生2
					ps.setObject(6, ctime);
					ps.setObject(7, teacher.getPhone());
					return ps;
				}
			}, keyHolder);
			idResult = keyHolder.getKey().intValue();
			if (idResult > 0) {
				final String sql3 = "insert into teacher(name,LOGIN_ID,phone,udid,password,CREATE_TIME,LASTLOGINDEVICETYPE,OPENID_MSG,OPENID_QQ,MSG_NAME,QQ_NAME,HAND_URL,UNIONID_QQ,UNIONID_MSG)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				// int rest=jt.update(sql3, new Object[] {
				// teacher.getName(),idResult,teacher.getPhone(),teacher.getUdid(),teacher.getPassword(),teacher.getCreatedTime()
				// }, new int[] {
				// Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP
				// });
				jt.update(new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(sql3, PreparedStatement.RETURN_GENERATED_KEYS);
						ps.setObject(1, teacher.getName());
						ps.setObject(2, idResult);
						ps.setObject(3, teacher.getPhone());
						ps.setObject(4, token);
						ps.setObject(5, teacher.getPassword());
						ps.setObject(6, teacher.getCreatedTime());
						ps.setObject(7, deviceType);
						ps.setObject(8, teacher.getMsgOpenid());
						ps.setObject(9, teacher.getQqOpenid());
						ps.setObject(10, teacher.getMsgName());
						ps.setObject(11, teacher.getQqName());
						ps.setObject(12, teacher.getHeadurl());
						ps.setObject(13, teacher.getQqUnionid());
						ps.setObject(14, teacher.getMsgUnionid());
						return ps;
					}
				}, keyHolder);
				int tid = keyHolder.getKey().intValue();
				if (tid > 0) {
					// 插入消息表
					String sql2 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,?,?)";
					jt.update(sql2, new Object[] { "注册成功", "您已成功注册一堂作文课老师端，可以上传资料成为平台作文点评老师。", ctime, tid, 2, 2 }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
					return tid;
				}
			}
		}
		return 0;
	}
	
	//老师注册2 -年级
	public int insertTeacher2(final Teacher teacher, final String token, final String deviceType) {
		// 老师注册
		List<Map<String, Object>> boollog = null;
		final int idResult;
		try {
			String sql5 = "select * from teacher where phone = ?";
			boollog = jt.queryForList(sql5, new Object[] { teacher.getPhone() });
			// if(boollog.size() !=0){
			// String sql7 = "select * from student where phone = ?";
			// List<Map<String, Object>> u = jt.queryForList(sql7, new Object[] {
			// student.getPhone()});
			// }
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				boollog = null;
		}
		if (boollog == null || (boollog != null && boollog.size() == 0)) {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			final String ctime = dateFormat.format(now);
			final String sql1 = "insert into login(name,LOGIN_TIME,loginstate,token,logintype,CREATED_TIME,PHONE)values(?,?,?,?,?,?,?)";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jt.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql1, PreparedStatement.RETURN_GENERATED_KEYS);
					ps.setObject(1, teacher.getName());
					ps.setObject(2, ctime);
					ps.setObject(3, "2");
					ps.setObject(4, token);
					ps.setObject(5, "1");// 老师1，学生2
					ps.setObject(6, ctime);
					ps.setObject(7, teacher.getPhone());
					return ps;
				}
			}, keyHolder);
			idResult = keyHolder.getKey().intValue();
			if (idResult > 0) {
				final String sql3 = "insert into teacher(name,LOGIN_ID,phone,udid,password,CREATE_TIME,LASTLOGINDEVICETYPE,OPENID_MSG,OPENID_QQ,MSG_NAME,QQ_NAME,HAND_URL,UNIONID_QQ,UNIONID_MSG,GRADE)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				// int rest=jt.update(sql3, new Object[] {
				// teacher.getName(),idResult,teacher.getPhone(),teacher.getUdid(),teacher.getPassword(),teacher.getCreatedTime()
				// }, new int[] {
				// Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP
				// });
				jt.update(new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(sql3, PreparedStatement.RETURN_GENERATED_KEYS);
						ps.setObject(1, teacher.getName());
						ps.setObject(2, idResult);
						ps.setObject(3, teacher.getPhone());
						ps.setObject(4, token);
						ps.setObject(5, teacher.getPassword());
						ps.setObject(6, teacher.getCreatedTime());
						ps.setObject(7, deviceType);
						ps.setObject(8, teacher.getMsgOpenid());
						ps.setObject(9, teacher.getQqOpenid());
						ps.setObject(10, teacher.getMsgName());
						ps.setObject(11, teacher.getQqName());
						ps.setObject(12, teacher.getHeadurl());
						ps.setObject(13, teacher.getQqUnionid());
						ps.setObject(14, teacher.getMsgUnionid());
						ps.setObject(15, teacher.getGrade());
						return ps;
					}
				}, keyHolder);
				int tid = keyHolder.getKey().intValue();
				if (tid > 0) {
					// 插入消息表
					String sql2 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,?,?)";
					jt.update(sql2, new Object[] { "注册成功", "您已成功注册一堂作文课老师端，可以上传资料成为平台作文点评老师。", ctime, tid, 2, 2 }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
					return tid;
				}
			}
		}
		return 0;
	}

	private JdbcTemplate jt;

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}
}
