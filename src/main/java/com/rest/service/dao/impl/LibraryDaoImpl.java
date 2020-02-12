package com.rest.service.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pc.teacher.servlet.TeacherFindServlet;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.rest.service.dao.LibraryDao;
import com.rest.service.impl.FileServ;
import com.util.CommUtils;
import com.util.CommonFunc;

public class LibraryDaoImpl implements LibraryDao {

	private JdbcTemplate jt;

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

	private static Logger logger = Logger.getLogger(TeacherFindServlet.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	/**
	 * 按照标题搜索
	 */
	public List<Map<String, Object>> getDataByTitle(String cond, String tableFrom, Map<String, String> param, String pageIndex, String pageNum, List<Integer> ikanalyzerId ) {
		List<Map<String, Object>> list = null;
		List<Map<String, Object>> listnew = new ArrayList<Map<String, Object>>();
		StringBuffer sql = new StringBuffer();
		StringBuffer sql1 = new StringBuffer();
		/*sql.append("select * from (");
		if (ikanalyzerId != null && ikanalyzerId.size() > 0) {
			sql.append("select * from ");
			sql.append(judgeTable(tableFrom));
			sql.append(" where id != ");
			for (int i = 0; i < ikanalyzerId.size(); i++) {
				if (i != 0) {
					sql.append(" and id != ");
					sql.append(ikanalyzerId.get(i));
				}else {
					sql.append(ikanalyzerId.get(i));
				}
			}
			sql.append(") ");
			sql.append(judgeTable(tableFrom));
		}else {
			sql.append(judgeTable(tableFrom));
			sql.append(")");
		}*/
		sql.append("select * from ");
		sql.append(judgeTable(tableFrom));
		sql.append(" where (");

		sql1.append("select count(*) as al from ");
		sql1.append(judgeTable(tableFrom));
		if (!"t_res_composion".equals(judgeTable(tableFrom))) {
			sql.append(" STATUS='004002' ");
//			sql1.append(" where STATUS='004002'");
		} else {
			sql.append(" STATUS='004002' and CAUSE='008004' ");
//			sql1.append(" where STATUS='004002' and CAUSE='008004'");
		}
		
		if (ikanalyzerId != null && ikanalyzerId.size() > 0) {
			for (int i = 0; i < ikanalyzerId.size(); i++) {
				sql.append(" and id != ");
				sql.append(ikanalyzerId.get(i));
			}
		}
		
		Object[] pa = new Object[param.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : param.entrySet()) {
			if ("AGE_SCALE".equalsIgnoreCase(entry.getKey()) && "014001".equals(entry.getValue())) {
				sql.append(" and ");
				sql.append("AGE_DETAIL IN('一年级','二年级') or ").append(entry.getKey()).append("=").append("?");
				sql1.append(" and (");
				sql1.append("AGE_DETAIL IN('一年级','二年级') or ").append(entry.getKey()).append("=").append("?");
				sql1.append(")");
			} else {
				sql.append(" and ").append(entry.getKey()).append("=").append("?");
				sql1.append(" and ").append(entry.getKey()).append("=").append("?");
			}

			pa[i] = entry.getValue();
			i++;
		}
		
		sql.append(" ) ");
		
		if (!TextUtils.isEmpty(cond)) {
			sql.append(" and (NAME  like '%" + cond + "%')");
		}
		
		sql.append(" order by CREATE_TIME desc ");

		// 总条数
		
//		int count = 0;

		try {
//			count = jt.queryForObject(sql1.toString(), pa, Integer.class);
//			// List<Map<String,Object>> r = dao.queryForList(sql1.toString(), pa);
//			if (count <= 0) {
//				return list;
//			}

			int pagei = Integer.parseInt(pageIndex);
			int pagesize = Integer.parseInt(pageNum);
			System.out.println(generatePageSql(sql.toString(), pagei, pagesize));
			list = jt.queryForList(generatePageSql(sql.toString(), pagei, pagesize), pa);
			String url = FileServ.getDownloadUrl();
			for (int j = 0; j < list.size(); j++) {
				Map<String, Object> mapnew = new HashMap<String, Object>();
				// cid 作文唯一标识 string
				// content 作文内容 string
				// dist 024 素材类型 026 技法类型 013 范文类型 string
				// number 阅读人数 string
				// title 作文标题 string
				int objid = Integer.valueOf(list.get(j).get("ID").toString());
				mapnew.put("cid", objid);
				String brief = CommonFunc.getArticleBrief(CommonFunc.toString(list.get(j).get("CONTENT")));// 20160512 截取文章简介
				mapnew.put("content", brief);
				switch (tableFrom) {
				case "composion":
					list.get(j).put("dist", "013");
					mapnew.put("dist", "013");
					break;
				case "jf":
					list.get(j).put("dist", "026");
					mapnew.put("dist", "026");
					break;
				case "sc":
					list.get(j).put("dist", "024");
					mapnew.put("dist", "024");
					break;
				}
				// 查询阅读人数
				String readNoSql = "select READ_NUM from read_no where TYPE='" + list.get(j).get("dist") + "' and COMP_ID = " + objid;
				int readNum = 0;
				List<Map<String, Object>> readNoList = jt.queryForList(readNoSql);
				if (readNoList != null && readNoList.size() > 0 && readNoList.get(0).get("READ_NUM") != null) {
					readNum = (int) readNoList.get(0).get("READ_NUM");
				}
				mapnew.put("number", readNum + "");
				mapnew.put("title", list.get(j).get("NAME"));
				//作者
				if (list.get(j).get("AUTHOR") == (null) || list.get(j).get("AUTHOR").equals("")) {
					mapnew.put("author", "佚名");
				} else {
					mapnew.put("author", list.get(j).get("AUTHOR").toString());
				}
				//时间
				mapnew.put("time", CommUtils.ObjectTime2String(list.get(j).get("CREATE_TIME")));
				//年级
				mapnew.put("grade", "无");
				if (list.get(j).get("AGE_DETAIL") != null && !list.get(j).get("AGE_DETAIL").equals("")) {
					mapnew.put("grade", list.get(j).get("AGE_DETAIL").toString());
				}
				if (list.get(j).get("AGE_SCALE") != null && "无".equals(mapnew.get("grade")) && !list.get(j).get("AGE_SCALE").equals("")) {
					String sql2 = "select code_name from t_base_code where code = ?";
					List<Map<String,Object>> list2 = jt.queryForList(sql2, new Object[]{list.get(j).get("AGE_SCALE")});
					if (list2 != null && list2.size() > 0) {
						String code_name = (String) list2.get(0).get("code_name");
						mapnew.put("grade", code_name);
					}
				}
				//体裁类型
				mapnew.put("codename", "无");
				String code2 = "";
				if (list.get(j).get("dist") == "013" && list.get(j).get("dist").equals("013")) {//范文类型
					if (list.get(j).get("TYPE") != null && !list.get(j).get("TYPE").toString().equals("")) {
						code2 = list.get(j).get("TYPE").toString();
					}
					if ("".equals(code2) && list.get(j).get("STYLE") != null && !list.get(j).get("STYLE").toString().equals("")) {
						code2 = list.get(j).get("STYLE").toString();
					}
				}
				if (list.get(j).get("dist") == "026" && list.get(j).get("dist").equals("026")) {//技法类型
					if (list.get(j).get("TYPE") != null && !list.get(j).get("TYPE").toString().equals("")) {
						code2 = list.get(j).get("TYPE").toString();
					}
					if ("".equals(code2) && list.get(j).get("STYLE") != null && !list.get(j).get("STYLE").toString().equals("")) {
						code2 = list.get(j).get("STYLE").toString();
					}
				}
				if (list.get(j).get("dist") == "024" && list.get(j).get("dist").equals("024")) {//素材类型
					if ("".equals(code2) && list.get(j).get("C_TYPE") != null && !list.get(j).get("C_TYPE").toString().equals("")) {
						code2 = list.get(j).get("C_TYPE").toString();
					}
				}
				if(!code2.equals("")){
					String sql7 = "select CODE_NAME from t_base_code where CODE=?";
					List<Map<String, Object>> list2 = jt.queryForList(sql7, new Object[] { code2 });
					String cname = null;
					if (list2 != null && list2.size() > 0) {
						cname = (String) list2.get(0).get("CODE_NAME");
					}
					if (cname != null && !TextUtils.isEmpty(cname)) {
						mapnew.put("codename", cname);
					} 
				}
				listnew.add(mapnew);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("lib查询失败" + e.getMessage());
		}
		List list1 = new ArrayList<Object>();
		//list1.add(0, 0);
		//list1.add(1, list);
		return listnew;
	}

	/**
	 * 生成分页查询SQL
	 * 
	 * @param sql
	 *            SQL语句
	 * @param pageindex
	 *            查询页的索引（从0开始）
	 * @param pagesize
	 *            每页的记录数
	 */
	private String generatePageSql(String sql, int pageindex, int pagesize) {
		StringBuffer strbSql = new StringBuffer();
		// 默认为mysql数据库
		// strbSql.append("select * from(");
		strbSql.append(sql);
		// strbSql.append(") vt limit ");
		strbSql.append(" limit ");
		strbSql.append(pageindex);
		strbSql.append(",");
		strbSql.append(pagesize);
		return strbSql.toString();
	}

	public String judgeTable(String mark) {
		if ("composion".equals(mark)) {
			return "t_res_composion";
		} else if ("jf".equals(mark)) {
			return "t_res_composion_jf";
		} else if ("sc".equals(mark)) {
			return "t_res_composion_sc";
		} else if ("zt".equals(mark)) {
			return "t_res_composion_zt";
		} else if ("pi".equals(mark)) {
			return "t_res_image";
		} else if ("do".equals(mark)) {
			return "t_res_doc";
		} else if ("me".equals(mark)) {
			return "t_res_media";
		} else {
			return "";
		}
	};

	public String getTy(String mark) {
		if ("t_res_composion".equals(mark)) {
			return "a";
		} else if ("t_res_composion_jf".equals(mark)) {
			return "g";
		} else if ("t_res_composion_sc".equals(mark)) {
			return "h";
		} else if ("t_res_composion_zt".equals(mark)) {
			return "i";
		} else if ("t_res_image".equals(mark)) {
			return "e";
		} else if ("t_res_doc".equals(mark)) {
			return "d";
		} else if ("t_res_media".equals(mark)) {
			return "b";
		} else {
			return "";
		}
	}

	/* (non Javadoc) 
	 * @Title: getDataByContent
	 * @Description: TODO
	 * @param cond
	 * @param tableFrom
	 * @param param
	 * @param pageIndex
	 * @param pageNum
	 * @param ikanalyzerId
	 * @return 
	 * @see com.rest.service.dao.LibraryDao#getDataByContent(java.lang.String, java.lang.String, java.util.Map, java.lang.String, java.lang.String, java.util.List) 
	 */
	@Override
	public List<Map<String, Object>> getDataByContent(String cond,
			String tableFrom, Map<String, String> param, String pageIndex,
			String pageNum, List<Integer> ikanalyzerId) {
		List<Map<String, Object>> list = null;
		List<Map<String, Object>> listnew = new ArrayList<Map<String, Object>>();
		StringBuffer sql = new StringBuffer();
		StringBuffer sql1 = new StringBuffer();
		/*sql.append("select * from (");
		if (ikanalyzerId != null && ikanalyzerId.size() > 0) {
			sql.append("select * from ");
			sql.append(judgeTable(tableFrom));
			sql.append(" where id != ");
			for (int i = 0; i < ikanalyzerId.size(); i++) {
				if (i != 0) {
					sql.append(" and id != ");
					sql.append(ikanalyzerId.get(i));
				}else {
					sql.append(ikanalyzerId.get(i));
				}
			}
			sql.append(") ");
			sql.append(judgeTable(tableFrom));
		}else {
			sql.append(judgeTable(tableFrom));
			sql.append(")");
		}*/
		sql.append("select * from ");
		sql.append(judgeTable(tableFrom));
		sql.append(" where (");

		sql1.append("select count(*) as al from ");
		sql1.append(judgeTable(tableFrom));
		if (!"t_res_composion".equals(judgeTable(tableFrom))) {
			sql.append(" STATUS='004002' ");
//			sql1.append(" where STATUS='004002'");
		} else {
			sql.append(" STATUS='004002' and CAUSE='008004' ");
//			sql1.append(" where STATUS='004002' and CAUSE='008004'");
		}
		
		if (ikanalyzerId != null && ikanalyzerId.size() > 0) {
			for (int i = 0; i < ikanalyzerId.size(); i++) {
				sql.append(" and id != ");
				sql.append(ikanalyzerId.get(i));
			}
		}
		
		Object[] pa = new Object[param.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : param.entrySet()) {
			if ("AGE_SCALE".equalsIgnoreCase(entry.getKey()) && "014001".equals(entry.getValue())) {
				sql.append(" and ");
				sql.append("AGE_DETAIL IN('一年级','二年级') or ").append(entry.getKey()).append("=").append("?");
				sql1.append(" and (");
				sql1.append("AGE_DETAIL IN('一年级','二年级') or ").append(entry.getKey()).append("=").append("?");
				sql1.append(")");
			} else {
				sql.append(" and ").append(entry.getKey()).append("=").append("?");
				sql1.append(" and ").append(entry.getKey()).append("=").append("?");
			}

			pa[i] = entry.getValue();
			i++;
		}
		
		sql.append(" ) ");
		
		if (!TextUtils.isEmpty(cond)) {
			sql.append(" and (KEY_WORD like '%" + cond + "%'");
			sql.append(" or MATCH CONTENT AGAINST ('+" + cond + "*' IN BOOLEAN MODE))");
		}
		
		sql.append(" order by CREATE_TIME desc ");

		// 总条数
		
//		int count = 0;

		try {
//			count = jt.queryForObject(sql1.toString(), pa, Integer.class);
//			// List<Map<String,Object>> r = dao.queryForList(sql1.toString(), pa);
//			if (count <= 0) {
//				return list;
//			}

			int pagei = Integer.parseInt(pageIndex);
			int pagesize = Integer.parseInt(pageNum);
			System.out.println(generatePageSql(sql.toString(), pagei, pagesize));
			list = jt.queryForList(generatePageSql(sql.toString(), pagei, pagesize), pa);
			String url = FileServ.getDownloadUrl();
			for (int j = 0; j < list.size(); j++) {
				Map<String, Object> mapnew = new HashMap<String, Object>();
				// cid 作文唯一标识 string
				// content 作文内容 string
				// dist 024 素材类型 026 技法类型 013 范文类型 string
				// number 阅读人数 string
				// title 作文标题 string
				int objid = Integer.valueOf(list.get(j).get("ID").toString());
				mapnew.put("cid", objid);
				String brief = CommonFunc.getArticleBrief(CommonFunc.toString(list.get(j).get("CONTENT")));// 20160512 截取文章简介
				mapnew.put("content", brief);
				switch (tableFrom) {
				case "composion":
					list.get(j).put("dist", "013");
					mapnew.put("dist", "013");
					break;
				case "jf":
					list.get(j).put("dist", "026");
					mapnew.put("dist", "026");
					break;
				case "sc":
					list.get(j).put("dist", "024");
					mapnew.put("dist", "024");
					break;
				}
				// 查询阅读人数
				String readNoSql = "select READ_NUM from read_no where TYPE='" + list.get(j).get("dist") + "' and COMP_ID = " + objid;
				int readNum = 0;
				List<Map<String, Object>> readNoList = jt.queryForList(readNoSql);
				if (readNoList != null && readNoList.size() > 0 && readNoList.get(0).get("READ_NUM") != null) {
					readNum = (int) readNoList.get(0).get("READ_NUM");
				}
				mapnew.put("number", readNum + "");
				mapnew.put("title", list.get(j).get("NAME"));
				//作者
				if (list.get(j).get("AUTHOR") == (null) || list.get(j).get("AUTHOR").equals("")) {
					mapnew.put("author", "佚名");
				} else {
					mapnew.put("author", list.get(j).get("AUTHOR").toString());
				}
				//时间
				mapnew.put("time", CommUtils.ObjectTime2String(list.get(j).get("CREATE_TIME")));
				//年级
				mapnew.put("grade", "无");
				if (list.get(j).get("AGE_DETAIL") != null && !list.get(j).get("AGE_DETAIL").equals("")) {
					mapnew.put("grade", list.get(j).get("AGE_DETAIL").toString());
				}
				if (list.get(j).get("AGE_SCALE") != null && "无".equals(mapnew.get("grade")) && !list.get(j).get("AGE_SCALE").equals("")) {
					String sql2 = "select code_name from t_base_code where code = ?";
					List<Map<String,Object>> list2 = jt.queryForList(sql2, new Object[]{list.get(j).get("AGE_SCALE")});
					if (list2 != null && list2.size() > 0) {
						String code_name = (String) list2.get(0).get("code_name");
						mapnew.put("grade", code_name);
					}
				}
				//体裁类型
				mapnew.put("codename", "无");
				String code2 = "";
				if (list.get(j).get("dist") == "013" && list.get(j).get("dist").equals("013")) {//范文类型
					if (list.get(j).get("TYPE") != null && !list.get(j).get("TYPE").toString().equals("")) {
						code2 = list.get(j).get("TYPE").toString();
					}
					if ("".equals(code2) && list.get(j).get("STYLE") != null && !list.get(j).get("STYLE").toString().equals("")) {
						code2 = list.get(j).get("STYLE").toString();
					}
				}
				if (list.get(j).get("dist") == "026" && list.get(j).get("dist").equals("026")) {//技法类型
					if (list.get(j).get("TYPE") != null && !list.get(j).get("TYPE").toString().equals("")) {
						code2 = list.get(j).get("TYPE").toString();
					}
					if ("".equals(code2) && list.get(j).get("STYLE") != null && !list.get(j).get("STYLE").toString().equals("")) {
						code2 = list.get(j).get("STYLE").toString();
					}
				}
				if (list.get(j).get("dist") == "024" && list.get(j).get("dist").equals("024")) {//素材类型
					if ("".equals(code2) && list.get(j).get("C_TYPE") != null && !list.get(j).get("C_TYPE").toString().equals("")) {
						code2 = list.get(j).get("C_TYPE").toString();
					}
				}
				if(!code2.equals("")){
					String sql7 = "select CODE_NAME from t_base_code where CODE=?";
					List<Map<String, Object>> list2 = jt.queryForList(sql7, new Object[] { code2 });
					String cname = null;
					if (list2 != null && list2.size() > 0) {
						cname = (String) list2.get(0).get("CODE_NAME");
					}
					if (cname != null && !TextUtils.isEmpty(cname)) {
						mapnew.put("codename", cname);
					} 
				}
				listnew.add(mapnew);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("lib查询失败" + e.getMessage());
		}
		List list1 = new ArrayList<Object>();
		//list1.add(0, 0);
		//list1.add(1, list);
		return listnew;
	}
}
