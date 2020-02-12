package com.pc.manage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.util.CommUtils;
import com.util.CurrentPage;
/**
 * 系统管理
 */
public class AdministrationDaoImpl {
	private static Logger logger = Logger.getLogger(ManageUsersDaoImpl.class);
	
	private JdbcTemplate jt = CommUtils.getJdbcTemplate();
	
	//权限管理 (用户列表)  - 条件+分页
	public CurrentPage findAdministrators(int currentPage, int numPerPage) {
		String sql = "SELECT * FROM manage_users";
		sql += " ORDER BY CREATE_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}
	
	//修改权限
	public void updatePermission(String id, Set<String> set) {
		String sql1 = "DELETE FROM user_permission WHERE user_id = ?";
		jt.update(sql1, new Object[]{id});
		String sql3 = "";
		if(set.size()>0){
			String sql2 = "INSERT INTO user_permission (USER_ID, PERMISSION) VALUES (?, ?)";
			for (String string : set) {
				jt.update(sql2, new Object[]{id, string});
			}
			sql3 = "UPDATE manage_users SET JURISDICTION = 1  where ID = ?";
			
		}else{
			sql3 = "UPDATE manage_users SET JURISDICTION = 2  where ID = ?";
		}
		jt.update(sql3, new Object[]{id});
		
	}

	//修改信息--回显
	public List<Map<String, Object>> selectInformation(int id) {
		String sql= "SELECT ID id,USERNAME userName,`NAME` name,ROLE role,`PASSWORD` pw FROM manage_users WHERE ID="+id;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}

	//修改信息--提交修改
	public int updateInformation(int id, String name, String userName, String pwd, String role) {
		String sql = "update manage_users set USERNAME='"+userName+"',NAME='"+name+"',ROLE='"+role+"'";
		if(pwd!=null&&!"".equals(pwd)){
			sql += ",PASSWORD='"+pwd+"'";
		}
		sql += " where ID="+id;
		int i = jt.update(sql);
		return i;
	}
	
	//删除管理员
	public int deleteAdmin(int id) {
		String sql = "delete from manage_users where ID="+id;
		String sql1 = "delete from user_permission where USER_ID="+id;
		int i = jt.update(sql);
		jt.update(sql1);
		return i;
	}
	
	//添加管理员--1
	public int addAdmin1(String name, String userName, String pw, String role, int jur) {
		int i = 0;
		String sql = "select * from manage_users where USERNAME='"+userName+"'";
		List<Map<String,Object>> forList = jt.queryForList(sql);
		if(forList.size()>0){
			i = -1;
		}else{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = new Date();
			String time = df.format(d);
			String sql1 = "insert into manage_users set USERNAME=?,NAME=?,ROLE=?,PASSWORD=?,CREATE_TIME=?,JURISDICTION=?";
			i = jt.update(sql1, new Object[] {userName,name,role,pw,time,jur},
					new int[] {Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.INTEGER});
		}
		return i;
	}
	//添加管理员
	public int addAdmin(final String name, final String userName, final String pw, final String role, final int jur) {
		KeyHolder keyHolder = new GeneratedKeyHolder();  
		int i1 = 0;
		String sql = "select * from manage_users where USERNAME='"+userName+"'";
		List<Map<String,Object>> forList = jt.queryForList(sql);
		if(forList.size()>0){
			i1 = -1;
		}else{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = new Date();
			final String time = df.format(d);
			final String sql1 = "insert into manage_users set USERNAME=?,NAME=?,ROLE=?,PASSWORD=?,CREATE_TIME=?,JURISDICTION=?";
			int i = jt.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection connection)
						throws SQLException {
	
					PreparedStatement ps = connection.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS); 
					ps.setString(1, userName);
					ps.setString(2, name);
					ps.setString(3, role);
					ps.setString(4, pw);
					ps.setString(5, time);
					ps.setInt(6, jur);
					return ps;
				}
			}, keyHolder);
			Number key = keyHolder.getKey();
			i1 = key.intValue();
		}
		return i1;
	}
	
	//设置权限--1
	public void addJurisdiction1(String userName, String jurisdiction) {
		String sql1 = "select * from manage_users where USERNAME='"+userName+"'";
		List<Map<String,Object>> forList = jt.queryForList(sql1);
		if(forList.size()>0){
			String sid = forList.get(0).get("ID").toString();
			int userId = Integer.parseInt(sid);
			String delsql="delete from user_permission where USER_ID="+userId;
			jt.update(delsql);
			String[] split = jurisdiction.split(",");
			String sql= "";
			for(int i=0;i<split.length;i++){
				String jur = split[i].trim();
				sql = "insert into user_permission set USER_ID="+userId+",PERMISSION='"+jur+"'";
				jt.update(sql);
			}
		}
	}
	//设置权限
	public void addJurisdiction(int userId, Set<String> set) {
		for (String string : set) {
			String sql = "insert into user_permission set USER_ID="+userId+",PERMISSION='"+string+"'";
			jt.update(sql);
		}
	}
	
	//权限列表（所有菜单）
	public List<Map<String, Object>> getJurisdiction() {
		String sql= "SELECT * FROM permission";
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}
	
	//权限列表（指定用户）
	public String getJurisdiction(String id) {
		String sql= "SELECT permission FROM user_permission where user_id = ?";
		List<Map<String,Object>> forList = jt.queryForList(sql, new Object[]{id});
		StringBuffer sb = new StringBuffer();
		for (Map<String, Object> map : forList) {
			sb.append(map.get("permission").toString()).append(",");
		}
		return sb.toString();
	}


}
