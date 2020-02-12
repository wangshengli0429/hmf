package com.rest.service.dao.impl;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.entity.Student;
import com.entity.Teacher;
import com.rest.service.dao.UpdateDao;

public class UpdateDaoImpl implements UpdateDao {

	private JdbcTemplate jt;

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

	/* 修改密码(学生) */
	// 查询旧密码是否一致
	public int selectStudentPw(String udid, String oldpw) {
		try {
			String sql1 = "select PASSWORD from student where UDID = '" + udid + "'";
			List<Map<String, Object>> queryForList = jt.queryForList(sql1);
			if (queryForList.get(0).get("PASSWORD") == null || "".equals(queryForList.get(0).get("PASSWORD").toString())) {
				return 1;//没有设置过密码
			}else if (queryForList.get(0).get("PASSWORD").toString().equals(oldpw)){
				return 2;//旧密码输入正确
			}else {
				return 3;//旧密码输入错误
			}
			
		} catch (Exception e) {
			return 0;
		}
	}

	// 修改学生密码
	public int updateStudentPw(final Student student, final String udid) {
		String sql2 = "update student set PASSWORD = '" + student.getPassword() + "' where UDID = '" + student.getUdid() + "'";
		int rest = jt.update(sql2);
		return rest;
	}

	/* 忘记密码(学生) */
	// 验证手机号是否存在
	public int selectSphone(String phone) {
		try {
			String sql1 = "select * from student where PHONE = '" + phone + "'";
			List<Map<String, Object>> queryForList = jt.queryForList(sql1);
			if (queryForList.get(0).get("PHONE").equals(phone)) {
				return 1;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return 0;
		}
		return 0;
	}

	// 修改密码
	public int updateForgotSpw(String password, String phone) {
		String sql2 = "update student set PASSWORD = '" + password + "' where PHONE = '" + phone + "'";
		int rest = jt.update(sql2);
		return rest;
	}

	/* 修改密码(老师) */
	// 查询旧密码是否一致
	public int selectTeacherPw(String udid, String oldpw) {
		try {
			String sql1 = "select PASSWORD from teacher where UDID = '" + udid + "'";
			List<Map<String, Object>> queryForList = jt.queryForList(sql1);
			if (queryForList.get(0).get("PASSWORD") == null || "".equals(queryForList.get(0).get("PASSWORD").toString())) {
				return 1;//没有设置过密码
			}else if (queryForList.get(0).get("PASSWORD").toString().equals(oldpw)) {
				return 2;//密码输入正确
			}else {
				return 3;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	// 修改老师密码
	public int updateTeacherPw(Teacher teacher, String token) {
		String sql2 = "update teacher set PASSWORD = '" + teacher.getPassword().toString() + "' where UDID = '" + token + "'";
		int rest = jt.update(sql2);
		return rest;
	}

	/* 忘记密码(老师) */
	// 验证手机号是否存在
	public int selectTphone(String phone) {
		try {
			String sql1 = "select * from teacher where PHONE = '" + phone + "'";
			List<Map<String, Object>> queryForList = jt.queryForList(sql1);
			if (queryForList.get(0).get("PHONE").equals(phone)) {
				return 1;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return 0;
		}
		return 0;
	}

	// 修改密码
	public int updateForgotTpw(String password, String phone) {
		String sql2 = "update teacher set PASSWORD = '" + password + "' where PHONE = '" + phone + "'";
		int rest = jt.update(sql2);
		return rest;
	}

	// 修改学生手机号
	public int findStudent(String pw, String phone, String udid) {
		try {
			String sql = "select * from student where UDID ='" + udid + "'";
			List<Map<String, Object>> userPhone = jt.queryForList(sql);
			// 判断密码
			if (userPhone.get(0).get("PASSWORD").equals(pw)) {
				//判断新手机号是否注册过
				String sql2 = "select * from student where PHONE ='" + phone + "'";
				List<Map<String,Object>> forList = jt.queryForList(sql2);
				if(forList.size()==0){
					// 修改手机号
					String sql1 = "update student set PHONE='" + phone + "' where UDID='" + udid + "'";
					int rest = jt.update(sql1);
					if (rest > 0) {
						return rest;
					}
				}else{
					return -2;
				}
			} else {
				return -4;
			}
			// 用户不存在返回空
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return 0;
		}
		return 0;
	}

	// 修改老师手机号
	public int findTeacher(String pw, String phone, String udid) {
		try {
			String sql = "select * from teacher where UDID = '" + udid + "'";
			List<Map<String, Object>> userPhone = jt.queryForList(sql);
			// 判断密码是否一致
			if (userPhone.get(0).get("PASSWORD").equals(pw)) {
				//判断新手机号是否注册过
				String sql2 = "select * from teacher where PHONE ='" + phone + "'";
				List<Map<String,Object>> forList = jt.queryForList(sql2);
				if(forList.size()==0){
					// 修改手机号
					String sql1 = "update teacher set PHONE = '" + phone + "' where UDID = '" + udid + "'";
					int rest = jt.update(sql1);
					if (rest > 0) {
						return rest;
					}
				}else{
					return -3;
				}
			} else {
				return -2;
			}
			// 用户不存在返回空
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return 0;
		}
		return 0;
	}

	// 回显基本信息（学生）
	public List<Map<String, Object>> getStudent(String udid) {
		try {
			String sql1 = "select * from student where UDID='" + udid + "'";
			List<Map<String, Object>> queryForList = jt.queryForList(sql1);
			if (queryForList.size() > 0) {
				return queryForList;
			}
			// 不存在返回空
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	// 修改基本信息（学生）
	public int updateStudent(Student student) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = dateFormat.format(now);
		Calendar date = Calendar.getInstance();
		String year = String.valueOf(date.get(Calendar.YEAR));
		String may = String.valueOf(date.get(Calendar.MONTH) + 1);
		String lastmay = "";
		String lastyear = "";
		
		if (may.equals("12")) {
			lastmay = "1";
			lastyear = String.valueOf(date.get(Calendar.YEAR) + 1);
		} else {
			lastmay = String.valueOf(date.get(Calendar.MONTH) + 2);
			lastyear = String.valueOf(date.get(Calendar.YEAR));
		}

		String da = String.valueOf(date.get(Calendar.DATE));

		String sql2 = "update student set";
		if (!TextUtils.isEmpty(student.getExtension())) {
			sql2 += " EXTENSION = '" + student.getExtension() + "'";
		}
		if (!TextUtils.isEmpty(student.getArea())) {
			sql2 += " ,AREA = '" + student.getArea() + "'";
		}
		if (!TextUtils.isEmpty(student.getGrade())) {
			sql2 += ", GRADE = '" + student.getGrade() + "'";
		}
		if (!TextUtils.isEmpty(student.getIntroduce())) {
			sql2 += ", INTRODUCE = '" + student.getIntroduce() + "'";
		}
		if (!TextUtils.isEmpty(student.getName())) {
			sql2 += ", NAME = '" + student.getName() + "'";
		}
		if (!TextUtils.isEmpty(student.getNickname())) {
			sql2 += ", NICKNAME = '" + student.getNickname() + "'";
		}
		if (!TextUtils.isEmpty(student.getSchool())) {
			sql2 += ", SCHOOL = '" + student.getSchool() + "'";
		}
		if (!TextUtils.isEmpty(student.getSex())) {
			sql2 += ", SEX = '" + student.getSex() + "'";
		}
		if (!TextUtils.isEmpty(student.getHeadurl())) {
			sql2 += ", HEAD = '" + student.getHeadurl() + "'";
		}
		int rest = 0;
		sql2 += " where UDID = '" + student.getUdid() + "'";
		rest = jt.update(sql2);

		String stuInfoSql = "select * from student where udid = ? ";
		List<Map<String, Object>> infoList = jt.queryForList(stuInfoSql, new Object[] { student.getUdid() });
		if (infoList != null && infoList.size() > 0) {// 有该用户
			if (infoList.get(0).get("COMPLETE_INFO") != null && (int) infoList.get(0).get("COMPLETE_INFO") == 1) { // 已经完善信息 只更新要完善的信息

			} else {// 没有完善信息
				if (isFullFillInfo(infoList)) {
					// 更新完善信息的字段，同时增加红包和消息 0、未完善；1、已完善
					String completeSql = "update student set COMPLETE_INFO = 1 where UDID = '" + student.getUdid() + "'";
					jt.update(completeSql);
					// 插入消息表
					String sql4 = "select ID from student where udid = ?";
					String id = jt.queryForObject(sql4, new Object[] { student.getUdid() }, String.class);
					String sql3 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,?,?)";
					jt.update(sql3, new Object[] { "红包到账", "请在"+lastyear+"年"+lastmay+"月"+da+"日之前使用，快到“我的——红包”查看吧", ctime, id, 1, 9 }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
					// 插入红包表
					String sql5 = "insert into red_packet(PACKET_NAME,PACKET_PRICE,UDID,FAILURE,REDUSE,START_TIME,END_TIME)values(?,?,?,?,?,?,?)";
					jt.update(sql5, new Object[] { "新用户35元红包", 35, student.getUdid(), 0, 0, year + "-" + may + "-" + da, lastyear + "-" + lastmay + "-" + da }, new int[] { Types.VARCHAR, Types.DECIMAL, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.TIMESTAMP, Types.TIMESTAMP });
				}
			}
		}
		return rest;
	}

	private boolean isFullFillInfo(List<Map<String, Object>> infoList) {
		Map<String, Object> infoMap = infoList.get(0);
		if (!(infoMap.get("NAME") != null && !TextUtils.isEmpty(infoMap.get("NAME").toString()))) {
			return false;
		}
		if (!(infoMap.get("AREA") != null && !TextUtils.isEmpty(infoMap.get("AREA").toString()))) {
			return false;
		}
		if (!(infoMap.get("GRADE") != null && !TextUtils.isEmpty(infoMap.get("GRADE").toString()))) {
			return false;
		}
		if (!(infoMap.get("SCHOOL") != null && !TextUtils.isEmpty(infoMap.get("SCHOOL").toString()))) {
			return false;
		}
		if (!(infoMap.get("SEX") != null && !TextUtils.isEmpty(infoMap.get("SEX").toString()))) {
			return false;
		}
		if (!(infoMap.get("NICKNAME") != null && !TextUtils.isEmpty(infoMap.get("NICKNAME").toString()))) {
			return false;
		}
		if (!(infoMap.get("HEAD") != null && !TextUtils.isEmpty(infoMap.get("HEAD").toString()))) {
			return false;
		}
		return true;
	}

	// 回显基本信息（老师）
	public List<Map<String, Object>> getTeacher(String udid) {
		if (udid == null || "".equals(udid)) {
			return null;
		}else {
			try {
				//String sql1 = "select * from teacher where UDID='" + udid + "'";
				String sql1 = "SELECT t.*,e.STATE estate FROM teacher t LEFT JOIN expected_com e ON e.TEACHER_ID=t.ID WHERE UDID='" + udid + "'";
				List<Map<String, Object>> queryForList = jt.queryForList(sql1);
				if (queryForList.size() > 0) {
					return queryForList;
				}
				// 不存在返回空
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	public int findPassword(String password, String udid) {
		try {
			String sql1 = "select PASSWORD from student where UDID='" + udid + "'";
			String pw = jt.queryForObject(sql1, String.class);
			if (password.equals(pw)) {
				return 1;
			}
			// 不存在返回空
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return 0;
		}
		return 0;
	}

	public int findTpassword(String password, String udid) {

		try {
			String sql1 = "select PASSWORD from teacher where UDID='" + udid + "'";
			String pw = jt.queryForObject(sql1, String.class);
			if (password.equals(pw)) {
				return 1;
			}
			// 不存在返回空
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return 0;
		}
		return 0;
	}

	public int updateTstate(String state, String udid) {
		try {
			String sql1 = "update teacher set STATE = '" + state + "' where UDID = '" + udid + "'";
			int s = jt.update(sql1);
			if (s > 0) {
				return 1;
			}
			// 不存在返回空
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return 0;
		}
		return 0;
	}

	// 修改基本信息（老师）
	public int updateTeacher(Teacher teacher) {
		String sql2 = "update teacher set";
		if (!TextUtils.isEmpty(teacher.getExtension())) {
			sql2 += " EXTENSION = '" + teacher.getExtension() + "'";
		}
		if (!TextUtils.isEmpty(teacher.getGrange())) {
			sql2 += ", GRANGE = '" + teacher.getGrange() + "'";
		}
		if (!TextUtils.isEmpty(teacher.getAcv())) {
			sql2 += ", ACV = '" + teacher.getAcv() + "'";
		}
		if (!TextUtils.isEmpty(teacher.getCity())) {
			sql2 += ", CITY = '" + teacher.getCity() + "'";
		}
		if (!TextUtils.isEmpty(teacher.getEdutime())) {
			sql2 += ", EDU_TIME = '" + teacher.getEdutime() + "'";
		}
		if (!TextUtils.isEmpty(teacher.getExper())) {
			sql2 += ", EXPER = '" + teacher.getExper() + "'";
		}
		if (!TextUtils.isEmpty(teacher.getFeatures())) {
			sql2 += ", FEATURES = '" + teacher.getFeatures() + "'";
		}
		if (!TextUtils.isEmpty(teacher.getHonor())) {
			sql2 += ", HONOR = '" + teacher.getHonor() + "'";
		}
		if (!TextUtils.isEmpty(teacher.getName())) {
			sql2 += ", NAME = '" + teacher.getName() + "'";
		}
		if (!TextUtils.isEmpty(teacher.getSchool())) {
			sql2 += ", SCHOOL = '" + teacher.getSchool() + "'";
		}
		if (!TextUtils.isEmpty(teacher.getSex())) {
			sql2 += ", SEX = '" + teacher.getSex() + "'";
		}
		if (!TextUtils.isEmpty(teacher.getHeadurl())) {
			sql2 += ", HAND_URL = '" + teacher.getHeadurl() + "'";
		}
		if (!TextUtils.isEmpty(teacher.getGrade())) {
			sql2 += ", GRADE = '" + teacher.getGrade() + "'";
		}
		sql2 += " where UDID = '" + teacher.getUdid() + "'";
		// String sql2 = "update teacher set ACV = '" + teacher.getAcv()+ "', CITY =
		// '"+teacher.getCity()+ "', EDU_TIME = '"
		// + teacher.getEdutime() + "', EXPER = '" + teacher.getExper() + "', FEATURES =
		// '" + teacher.getFeatures()
		// + "', HONOR = '" + teacher.getHonor() + "', NAME = '" + teacher.getName() +
		// "', SCHOOL = '" + teacher.getSchool()
		// + "', SEX = '" + teacher.getSex() + "', HAND_URL = '" + teacher.getHeadurl()
		// + "', GRADE = '" + teacher.getGrade()
		// + "' where UDID = '" + teacher.getUdid() + "'";
		int rest = jt.update(sql2);
		// 判断 个人信息 是否完整
		String sql1 = "select * from teacher where UDID='" + teacher.getUdid() + "'";
		List<Map<String, Object>> forList = jt.queryForList(sql1);
		if (forList.size() > 0) {
			//完善所有的个人资料
			if (forList.get(0).get("ACV")!=null&&!forList.get(0).get("ACV").equals("") && forList.get(0).get("CITY")!=null&&!forList.get(0).get("CITY").equals("") && forList.get(0).get("EDU_TIME")!=null&&!forList.get(0).get("EDU_TIME").equals("") && forList.get(0).get("EXPER")!=null&&!forList.get(0).get("EXPER").equals("") && forList.get(0).get("FEATURES")!=null&&!forList.get(0).get("FEATURES").equals("") && forList.get(0).get("GRADE")!=null&&!forList.get(0).get("GRADE").equals("") && forList.get(0).get("HONOR")!=null&&!forList.get(0).get("HONOR").equals("") && forList.get(0).get("NAME")!=null&&!forList.get(0).get("NAME").equals("") && forList.get(0).get("SCHOOL")!=null&&!forList.get(0).get("SCHOOL").equals("") && forList.get(0).get("SEX")!=null&&!forList.get(0).get("SEX").equals("") && forList.get(0).get("HAND_URL")!=null&&!forList.get(0).get("HAND_URL").equals("")) {
			//只完善年级、姓名
			//if (forList.get(0).get("GRADE")!=null&&!forList.get(0).get("GRADE").equals("") && forList.get(0).get("NAME")!=null&&!forList.get(0).get("NAME").equals("") ) {
				Date now = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = dateFormat.format(now);// 完善个人信息 时间
				String sql3 = "update teacher set PERFECT_TIME=? where UDID=?";
				jt.update(sql3, new Object[] { time, teacher.getUdid() }, new int[] { Types.TIMESTAMP, Types.VARCHAR });
			}
		}
		return rest;
	}

	// 修改师资认证 -回显
	public List<Map<String, Object>> getRenzheng(String udid) {
		try {
			String sql1 = "select * from teacher where UDID='" + udid + "'";
			List<Map<String, Object>> queryForList = jt.queryForList(sql1);
			if (queryForList.size() > 0) {
				return queryForList;
			}
			// 不存在返回空
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getStudentCard(String udid) {
		String sql = "SELECT c.REMAINING_COUNT count, c.END_TIME time FROM stu_dpcard_use c,student s WHERE s.UDID = ? AND s.ID = c.STU_ID AND STATE = 0";
		try {
			List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{udid});
			return list;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public List<Map<String, Object>> getDpayment(String udid) {
		String sql = "SELECT COUNT(c.ID) number FROM composition c,student s WHERE s.UDID = ? AND s.UDID = c.UDID AND c.STATE = 0";
		try {
			List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{udid});
			return list;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public List<Map<String, Object>> getDcomment(String udid) {
		String sql = "SELECT COUNT(c.ID) number FROM composition c,student s WHERE s.UDID = ? AND s.UDID = c.UDID AND c.STATE = 2";
		try {
			List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{udid});
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Map<String, String> selectCompositionCount(String udid) {
		String sql = "SELECT count(c.STATE) count,c.STATE FROM composition c,teacher t WHERE t.UDID = ? AND t.ID = c.TID AND c.STATE in (2,3) GROUP BY c.STATE";
		Map<String, String> returnMap = new HashMap<>();
		try {
			List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{udid});
			for (Map<String, Object> map : list) {
				String state = map.get("state").toString();
				if ("2".equals(state)) {
					returnMap.put("unRemark", map.get("count").toString());
				}else {
					returnMap.put("remark", map.get("count").toString());
				}
			}
			return returnMap;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
