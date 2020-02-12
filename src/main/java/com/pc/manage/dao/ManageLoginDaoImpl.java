package com.pc.manage.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.entity.ManageUsers;
import com.util.CommUtils;
import org.springframework.stereotype.Component;

/**
 * 登录
 */
@Component
public class ManageLoginDaoImpl {
	private static Logger logger = LoggerFactory.getLogger(ManageLoginDaoImpl.class);
	@Autowired
	private JdbcTemplate jt ;

	@Autowired
	public void setJt(JdbcTemplate jt) {
		if(jt == null) {
			jt = CommUtils.getJdbcTemplate();
		}
		logger.info("set mySecretKey={}", jt);
	}
	//验证用户名
	public int loginName(String uname) {
		String sql = "select ID from manage_users where USERNAME='"+uname+"'";
		try {
			List<Map<String,Object>> forList = jt.queryForList(sql);
			if(forList.size()>0){
				return 1;
			}
		}catch (Exception e){
			System.out.println("打印SQLException:"+e.getMessage());
			System.out.println("打印SQLException:"+e);
		}

		return 0;
	}
	
	public ManageUsers checkLogin(String uname, String pw) {
		// 定义sql语句
        String sql = "select ID,USERNAME,PASSWORD,DISABLE from manage_users where USERNAME=? and PASSWORD= ?";
        // 执行查询
        List<ManageUsers> users = jt.query(sql, new Object[] { uname, pw },
                new RowMapper<ManageUsers>() {
        	
                    public ManageUsers mapRow(ResultSet rs, int num)
                            throws SQLException {
                    	ManageUsers u = new ManageUsers();
                        u.setId(rs.getInt("ID"));
                        u.setUsername(rs.getString("USERNAME"));
                        u.setPassword(rs.getString("PASSWORD"));
                        u.setDisable(rs.getInt("DISABLE"));
                        return u;
                    }
					
                });
        // 定义返回值
        ManageUsers m= null;
        // 判断集合对象是否为null 并且长度大于0
        if (users != null && users.size() > 0) {
            m = users.get(0); // 取第一个值
        }
        return m;
	}

	public Set<Map<String,Object>> getJurisdiction(Integer id) {
		String sql= "SELECT * from user_permission u,permission p WHERE u.USER_ID = ? AND u.PERMISSION = p.ID ";
		List<Map<String,Object>> forList = jt.queryForList(sql, new Object[]{id});
		Set<Map<String,Object>> set = new TreeSet<>(new Comparator<Map<String,Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				int i1 = Integer.parseInt(o1.get("ID").toString());
				int i2 = Integer.parseInt(o2.get("ID").toString());
				return i1 - i2;
			}
		});
		for (Map<String, Object> map : forList) {
			set.add(map);
		}
		return set;
	}
	
}
