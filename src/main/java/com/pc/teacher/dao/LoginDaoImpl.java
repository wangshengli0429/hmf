package com.pc.teacher.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.entity.Teacher;
import com.util.CommUtils;

public class LoginDaoImpl {
	private JdbcTemplate jt = CommUtils.getJdbcTemplate();
	
	//验证用户名
	public int checkLoginName(String lname) {
		String sql = "select ID,PHONE,PASSWORD,AUSTATE,NAME,UPLOAD_TIME,PERFECT_TIME from Teacher where PHONE='"+lname+"'";
		List<Map<String,Object>> forList = jt.queryForList(sql);
		if(forList.size()>0){
			return 1;
		}
		return 0;
	}
	
	public Teacher checkLogin(String phone, String pass) {
		 // 定义sql语句
        String sql = "select ID,NAME,PHONE,PASSWORD,AUSTATE,CAUSE,UPLOAD_TIME,PERFECT_TIME,COMPLETE_INFO from Teacher where PHONE=? and PASSWORD= ?";
        // 执行查询
        List<Teacher> users = jt.query(sql, new Object[] { phone, pass },
                new RowMapper<Teacher>() {
                    public Teacher mapRow(ResultSet rs, int num)
                            throws SQLException {
                    	Teacher u = new Teacher();
                        u.setId(rs.getInt("ID"));
                        u.setName(rs.getString("NAME"));//用户昵称
                        u.setPhone(rs.getString("PHONE"));
                        u.setPassword(rs.getString("PASSWORD"));
                        u.setAustate(rs.getString("AUSTATE"));//资料审核状态(审核中0，已通过1，未通过2)
                        u.setCause(rs.getString("CAUSE"));//认证失败原因
                        u.setUploadTime(rs.getString("UPLOAD_TIME"));//上传资料时间
                        u.setPerfectTime(rs.getString("PERFECT_TIME"));//完善个人信息时间
                        u.setCompleteInfo(rs.getString("COMPLETE_INFO"));//完善信息 0未完善信息 1已经完善信息
                        return u;
                    }

					
                });
        // 定义返回值
        Teacher t= null;
        // 判断集合对象是否为null 并且长度大于0
        if (users != null && users.size() > 0) {
            t = users.get(0); // 取第一个值
        }
        return t;
	}
	
	
	//验证预点评作文是否提交
	public List<Map<String, Object>> testExpected(Integer id) {
		String sql = "select * from expected_com where TEACHER_ID ="+id;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		if(forList.size()>0){
			return forList;
		}
		return null;
	}

}
