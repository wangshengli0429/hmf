package com.util;

import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTemplateE extends JdbcTemplate {
	/**
	 * 分页查询（无过滤参数）
	 * @param sql        SQL语句
	 * @param pageindex  查询页的索引（从0开始）
	 * @param pagesize   每页的记录数
	 * @return
	 */
	public List<Map<String,Object>> queryPageForList(String sql,int pageindex,int pagesize) {
		return queryForList(generatePageSql(sql,pageindex,pagesize));
	}
	
	/**
	 * 分页查询（带过滤参数）
	 * @param sql       SQL语句
	 * @param args      过滤参数
	 * @param pageindex 查询页的索引（从0开始）
	 * @param pagesize  每页的记录数
	 * @return
	 */
	public List<Map<String,Object>> queryPageForList(String sql,Object[] args,int pageindex,int pagesize) {
		return queryForList(generatePageSql(sql,pageindex,pagesize),args);
	}
	
	/**
	 * 生成分页查询SQL
	 * @param sql       SQL语句
	 * @param pageindex 查询页的索引（从0开始）
	 * @param pagesize  每页的记录数
	 */
	private String generatePageSql(String sql,int pageindex,int pagesize) {
		StringBuffer strbSql = new StringBuffer();
		
		String type = getDatabaseType();
		if ("oracle".equals(type)) {
			
		} else {
			//默认为mysql数据库
			//strbSql.append("select * from(");
			strbSql.append(sql);
			//strbSql.append(") vt limit ");
			strbSql.append(" limit ");
			strbSql.append(pageindex*pagesize);
			strbSql.append(",");
			strbSql.append(pagesize);
		}
		
		return strbSql.toString();
	}
	
	/**
	 * 获取数据库类型
	 * @return
	 */
	private String getDatabaseType() {
		try {
			DatabaseMetaData md = this.getDataSource().getConnection().getMetaData();
			String databaseType = md.getDatabaseProductName();
			return databaseType == null ? "" : databaseType.toLowerCase();
		} catch (Exception e) {
			return null;
		}
	}
}
