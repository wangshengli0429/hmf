package com.rest.service.dao.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.rest.service.dao.ITMDao;
import com.util.CommUtils;
import com.util.CurrentPage;

public class ITMDaoImpl implements ITMDao {

	private static Logger logger = Logger.getLogger(ITMDaoImpl.class);

	private JdbcTemplate jt;

	public JdbcTemplate getJt() {
		return jt;
	}

	public void setJt(JdbcTemplate jt) {
		this.jt = jt;
	}

	public Map<String, Object> findModel(String dist, String sign, String udid, String codeType) {
		// 范文
		try {
			String readNum = getReadNum(sign, dist);
			Map<String, Object> list = new HashMap<String, Object>();
			String sql5 = "select AUTHOR,NAME,CONTENT,AGE_DETAIL,TYPE,STYLE,C_TYPE,E_TYPE from t_res_composion where id = ?";
			List<Map<String, Object>> flist = jt.queryForList(sql5, new Object[] { sign });
			if (flist.size() > 0) {
				if (flist.get(0).get("AUTHOR") == (null) || flist.get(0).get("AUTHOR").equals("")) {
					list.put("author", "佚名");
				} else {
					list.put("author", flist.get(0).get("AUTHOR").toString());
				}
				if (flist.get(0).get("CONTENT") == (null)) {
					list.put("content", "");
				} else {
					list.put("content", CommUtils.deleteTagForContent(flist.get(0).get("CONTENT").toString()));
				}
				
				list.put("grade", "无");
				if (flist.get(0).get("AGE_DETAIL") != null && !flist.get(0).get("AGE_DETAIL").equals("")) {
					list.put("grade", flist.get(0).get("AGE_DETAIL").toString());
				}
				if (flist.get(0).get("AGE_SCALE") != null && "无".equals(list.get("grade")) && !flist.get(0).get("AGE_SCALE").equals("")) {
					String sql = "select code_name from t_base_code where code = ?";
					List<Map<String,Object>> list2 = jt.queryForList(sql, new Object[]{flist.get(0).get("AGE_SCALE")});
					if (list2 != null && list2.size() > 0) {
						String code_name = (String) list2.get(0).get("code_name");
						list.put("grade", code_name);
					}
				}
				
				if (flist.get(0).get("NAME") == (null)) {
					list.put("title", "");
				} else {
					list.put("title", flist.get(0).get("NAME").toString());
				}
				list.put("readNo", readNum);
				list.put("type", "无");
				if (codeType.equals("013")) {
					if (flist.get(0).get("STYLE") != null && !flist.get(0).get("STYLE").toString().equals("")) {
						String sql7 = "select CODE_NAME from t_base_code where CODE_TYPE = '013' and CODE=?";
						String code = flist.get(0).get("STYLE").toString();
						List<Map<String, Object>> list2 = jt.queryForList(sql7, new Object[] { code });
						String cname = null;
						if (list2 != null && list2.size() > 0) {
							cname = (String) list2.get(0).get("CODE_NAME");
						}
						if (cname != null && !TextUtils.isEmpty(cname)) {
							list.put("type", cname);
						} 
					}
				}else if (codeType.equals("022")) {
					if ( flist.get(0).get("C_TYPE") != null && !flist.get(0).get("C_TYPE").toString().equals("")) {
						String sql7 = "select CODE_NAME from t_base_code where CODE_TYPE = '022' and CODE=?";
						String code = flist.get(0).get("C_TYPE").toString();
						List<Map<String, Object>> list2 = jt.queryForList(sql7, new Object[] { code });
						String cname = null;
						if (list2 != null && list2.size() > 0) {
							cname = (String) list2.get(0).get("CODE_NAME");
						}
						if (cname != null && !TextUtils.isEmpty(cname)) {
							list.put("type", cname);
						}
					}
				}else if (codeType.equals("029")) {
					if ( flist.get(0).get("E_TYPE") != null && !flist.get(0).get("E_TYPE").toString().equals("")) {
						String sql7 = "select CODE_NAME from t_base_code where CODE_TYPE = '029' and CODE=?";
						String code = flist.get(0).get("E_TYPE").toString();
						List<Map<String, Object>> list2 = jt.queryForList(sql7, new Object[] { code });
						String cname = null;
						if (list2 != null && list2.size() > 0) {
							cname = (String) list2.get(0).get("CODE_NAME");
						}
						if (cname != null && !TextUtils.isEmpty(cname)) {
							list.put("type", cname);
						}
					}
				}
				
				if (list.get("type") == null || "无".equals(list.get("type"))) {
					String code = "";
					if (flist.get(0).get("TYPE") != null && !flist.get(0).get("TYPE").toString().equals("")) {
						code = flist.get(0).get("TYPE").toString();
					}
					if ("".equals(code) && flist.get(0).get("STYLE") != null && !flist.get(0).get("STYLE").toString().equals("")) {
						code = flist.get(0).get("STYLE").toString();
					}
					if ("".equals(code) && flist.get(0).get("C_TYPE") != null && !flist.get(0).get("C_TYPE").toString().equals("")) {
						code = flist.get(0).get("C_TYPE").toString();
					}
					if ("".equals(code) && flist.get(0).get("E_TYPE") != null && !flist.get(0).get("E_TYPE").toString().equals("")) {
						code = flist.get(0).get("E_TYPE").toString();
					}
						
					String sql7 = "select CODE_NAME from t_base_code where CODE=?";
					List<Map<String, Object>> list2 = jt.queryForList(sql7, new Object[] { code });
					String cname = null;
					if (list2 != null && list2.size() > 0) {
						cname = (String) list2.get(0).get("CODE_NAME");
					}
					if (cname != null && !TextUtils.isEmpty(cname)) {
						list.put("type", cname);
					} 
				}
				
				// 是否收藏
				String sql6 = "select count(id) from collection where udid = '" + udid + "' AND COMP_ID= '" + sign + "'";
				int bool = jt.queryForObject(sql6, Integer.class);
				if (bool > 0) {
					list.put("collect", "1");
				} else {
					list.put("collect", "0");
				}
				return list;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	private String getReadNum(String sign, String dist) {
		String readNumSql = "select READ_NUM from read_no where COMP_ID = ? and TYPE = ? ";
		List<Map<String, Object>> readList = jt.queryForList(readNumSql, new Object[] { sign, dist });
		String readNumStr = "0";// 阅读人数
		if (readList != null && readList.size() > 0) {// 有该作文
			int number = (int) readList.get(0).get("READ_NUM");
			number += 1;
			String sql1 = "update read_no set READ_NUM = " + number + " where COMP_ID = ? and TYPE = ? ";
			jt.update(sql1, new Object[] { sign, dist });
			readNumStr = number + "";
		} else { // 没有该作文
			String sql1 = "insert into read_no(COMP_ID,TYPE,READ_NUM) values(?,?,?)";
			jt.update(sql1, new Object[] { sign, dist, 1 }, new int[] { Types.INTEGER, Types.VARCHAR, Types.INTEGER });
			readNumStr = "1";
		}
		return readNumStr;
	}

	public Map<String, Object> findMaterial(String dist, String sign, String udid) {
		// 素材
		try {
			String readNum = getReadNum(sign, dist);
			Map<String, Object> list = new HashMap<String, Object>();
			String sql5 = "select AUTHOR,NAME,CONTENT,AGE_DETAIL,C_TYPE,AGE_SCALE,STYLE from t_res_composion_sc where id = ?";
			List<Map<String, Object>> flist = jt.queryForList(sql5, new Object[] { sign });
			if (flist.size() > 0) {
				if (flist.get(0).get("AUTHOR") == (null) || flist.get(0).get("AUTHOR").equals("")) {
					list.put("author", "佚名");
				} else {
					list.put("author", flist.get(0).get("AUTHOR").toString());
				}
				if (flist.get(0).get("CONTENT") == (null)) {
					list.put("content", "");
				} else {
					list.put("content", CommUtils.deleteTagForContent(flist.get(0).get("CONTENT").toString()));
				}
				
				list.put("grade", "无");
				if (flist.get(0).get("AGE_DETAIL") != null && !flist.get(0).get("AGE_DETAIL").equals("")) {
					list.put("grade", flist.get(0).get("AGE_DETAIL").toString());
				}
				if (flist.get(0).get("AGE_SCALE") != null && "无".equals(list.get("grade")) && !flist.get(0).get("AGE_SCALE").equals("")) {
					String sql = "select code_name from t_base_code where code = ?";
					List<Map<String,Object>> list2 = jt.queryForList(sql, new Object[]{flist.get(0).get("AGE_SCALE")});
					if (list2 != null && list2.size() > 0) {
						String code_name = (String) list2.get(0).get("code_name");
						list.put("grade", code_name);
					}
				}
				if (flist.get(0).get("NAME") == null) {
					list.put("title", "");
				} else {
					list.put("title", flist.get(0).get("NAME").toString());
				}	
				list.put("readNo", readNum);
				// 作文类型
				String code = "";
				if (flist.get(0).get("C_TYPE") != null && !flist.get(0).get("C_TYPE").toString().equals("")) {
					code = flist.get(0).get("C_TYPE").toString();
				}
				if ("".equals(code) && flist.get(0).get("STYLE") != null && !flist.get(0).get("STYLE").toString().equals("")) {
					code = flist.get(0).get("STYLE").toString();
				}
				String sql7 = "select CODE_NAME from t_base_code where CODE=?";
				List<Map<String,Object>> list2 = jt.queryForList(sql7, new Object[]{code});
				String cname = "";
				if (list2 != null && list2.size() > 0) {
					cname = (String) list2.get(0).get("CODE_NAME");
				}
				if (cname != null && !TextUtils.isEmpty(cname)) {
					list.put("type", cname);
				} 
				// 是否收藏
				String sql6 = "select count(id) from collection where udid = '" + udid + "' AND COMP_ID= '" + sign + "'";
				int bool = jt.queryForObject(sql6, Integer.class);
				if (bool > 0) {
					list.put("collect", "1");
				} else {
					list.put("collect", "0");
				}
				return list;
			}
		} catch (Exception e) {
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)
				return null;
		}
		return null;
	}

	public Map<String, Object> findTechnical(String dist, String sign, String udid, String codeType) {
		// 技法
		try {
			String readNum = getReadNum(sign, dist);
			Map<String, Object> list = new HashMap<String, Object>();
			String sql5 = "select AUTHOR,NAME,CONTENT,AGE_DETAIL,TYPE,STYLE,C_TYPE from t_res_composion_jf where id = ?";
			List<Map<String, Object>> flist = jt.queryForList(sql5, new Object[] { sign });
			if (flist.size() > 0) {
				if (flist.get(0).get("AUTHOR") == null || flist.get(0).get("AUTHOR").equals("") ) {
					list.put("author", "佚名");
				} else {
					list.put("author", flist.get(0).get("AUTHOR").toString());
				}
				if (flist.get(0).get("CONTENT") == null) {
					list.put("content", "");
				} else {
					list.put("content", CommUtils.deleteTagForContent(flist.get(0).get("CONTENT").toString()));
				}
				
				list.put("grade", "无");
				if (flist.get(0).get("AGE_DETAIL") != null && !flist.get(0).get("AGE_DETAIL").equals("")) {
					list.put("grade", flist.get(0).get("AGE_DETAIL").toString());
				}
				if (flist.get(0).get("AGE_SCALE") != null && "无".equals(list.get("grade")) && !flist.get(0).get("AGE_SCALE").equals("")) {
					String sql = "select code_name from t_base_code where code = ?";
					List<Map<String,Object>> list2 = jt.queryForList(sql, new Object[]{flist.get(0).get("AGE_SCALE")});
					if (list2 != null && list2.size() > 0) {
						String code_name = (String) list2.get(0).get("code_name");
						list.put("grade", code_name);
					}
				}
				
				if (flist.get(0).get("NAME") == null) {
					list.put("title", "");
				} else {
					list.put("title", flist.get(0).get("NAME").toString());
				}
				list.put("readNo", readNum);
				
				list.put("type", "无");
				if (codeType.equals("033")) {
					// 技法类型
					if (flist.get(0).get("STYLE") != null && !flist.get(0).get("STYLE").toString().equals("")) {
						String sql7 = "select CODE_NAME from t_base_code where CODE_TYPE = '033' and CODE=?";
						List<Map<String,Object>> list2 = jt.queryForList(sql7, new Object[]{flist.get(0).get("STYLE").toString()});
						if (list2 != null && list2.size() > 0) {
							String cname = (String) list2.get(0).get("CODE_NAME");
							if (!TextUtils.isEmpty(cname)) {
								list.put("type", cname);
							}
						}
					}
				}
				
				if (list.get("type") == null || "无".equals(list.get("type"))){
					
					String code = "";
					if (flist.get(0).get("TYPE") != null && !flist.get(0).get("TYPE").toString().equals("") ) {
						code = flist.get(0).get("TYPE").toString();
					}
					if ("".equals(code) && flist.get(0).get("STYLE") != null && !flist.get(0).get("STYLE").toString().equals("")) {
						code = flist.get(0).get("STYLE").toString();
					}
					if ("".equals(code) && flist.get(0).get("C_TYPE") != null && !flist.get(0).get("C_TYPE").toString().equals("")) {
						code = flist.get(0).get("C_TYPE").toString();
					}
					
					String sql7 = "select CODE_NAME from t_base_code where CODE=?";
					List<Map<String,Object>> list2 = jt.queryForList(sql7, new Object[]{code});
					if (list2 != null && list2.size() > 0) {
						String cname = (String) list2.get(0).get("CODE_NAME");
						if (!TextUtils.isEmpty(cname)) {
							list.put("type", cname);
						}
					} 
				}
				
				// 是否收藏
				String sql6 = "select count(id) from collection where udid = '" + udid + "' AND COMP_ID= '" + sign + "'";
				int bool = jt.queryForObject(sql6, Integer.class);
				if (bool > 0) {
					list.put("collect", "1");
				} else {
					list.put("collect", "0");
				}
				return list;
			}
		} catch (Exception e) {
			System.out.println(e);
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)

				return null;
		}
		return null;
	}

	@Override
	public Map<String, Object> findRecomm(String dist, String sign, String udid) {
		// 推荐
		try {
			// READ_NUM
			String readNum = getReadNum(sign, dist);
			Map<String, Object> list = new HashMap<String, Object>();
			String sql5 = "select AUTHOR,NAME,CONTENT,AGE_DETAIL,TYPE,STYLE,C_TYPE,E_TYPE from recommend_com where id = ?";
			List<Map<String, Object>> flist = jt.queryForList(sql5, new Object[] { sign });
			if (flist != null && flist.size() > 0) {
				if (flist.get(0).get("AUTHOR") == null || flist.get(0).get("AUTHOR").equals("")) {
					list.put("author", "佚名");
				} else {
					list.put("author", flist.get(0).get("AUTHOR").toString());
					// logger.info("-----作者AUTHOR" + flist.get(0).get("AUTHOR").toString());
				}
				if (flist.get(0).get("CONTENT") == null) {
					list.put("content", "");
				} else {
					list.put("content", CommUtils.deleteTagForContent(flist.get(0).get("CONTENT").toString()));
					// logger.info("-----作文内容CONTENT" + flist.get(0).get("CONTENT").toString());
				}
				
				list.put("grade", "无");
				if (flist.get(0).get("AGE_DETAIL") != null && !flist.get(0).get("AGE_DETAIL").equals("")) {
					list.put("grade", flist.get(0).get("AGE_DETAIL").toString());
				}
				if (flist.get(0).get("AGE_SCALE") != null && "无".equals(list.get("grade")) && !flist.get(0).get("AGE_SCALE").equals("")) {
					String sql = "select code_name from t_base_code where code = ?";
					List<Map<String,Object>> list2 = jt.queryForList(sql, new Object[]{flist.get(0).get("AGE_SCALE")});
					if (list2 != null && list2.size() > 0) {
						String code_name = (String) list2.get(0).get("code_name");
						list.put("grade", code_name);
					}
				}
				
				
				if (flist.get(0).get("NAME") == null) {
					list.put("title", "");
				} else {
					list.put("title", flist.get(0).get("NAME").toString());
					// logger.info("-----题目" + flist.get(0).get("NAME").toString());
				}
				list.put("readNo", readNum);
				// logger.info("-----阅读次数" + readNum);
				// 作文类型
				
				
				list.put("type", "无");
				String code = "";
				if (flist.get(0).get("TYPE") != null && !flist.get(0).get("TYPE").toString().equals("") ) {
					code = flist.get(0).get("TYPE").toString();
				}
				if ("".equals(code) && flist.get(0).get("STYLE") != null && !flist.get(0).get("STYLE").toString().equals("")) {
					code = flist.get(0).get("STYLE").toString();
				}
				if ("".equals(code) && flist.get(0).get("C_TYPE") != null && !flist.get(0).get("C_TYPE").toString().equals("")) {
					code = flist.get(0).get("C_TYPE").toString();
				}
				if ("".equals(code) && flist.get(0).get("E_TYPE") != null && !flist.get(0).get("E_TYPE").toString().equals("")) {
					code = flist.get(0).get("E_TYPE").toString();
				}
				String sql7 = "select CODE_NAME from t_base_code where CODE=?";
				// String sql7 = "select CODE_NAME from t_base_code ";
				List<Map<String, Object>> list2 = jt.queryForList(sql7, new Object[] {code});
				if (list2 != null && list2.size() > 0) {
					String cname = (String) list2.get(0).get("CODE_NAME");
					if (!TextUtils.isEmpty(cname)) {
						list.put("type", cname);
						// logger.info("-----作文类型" + cname);
				}
				}
				
				// 是否收藏
				String sql6 = "select count(id) from collection where udid = '" + udid + "' AND COMP_ID= '" + sign + "'";
				int bool = jt.queryForObject(sql6, Integer.class);
				if (bool > 0) {
					list.put("collect", "1");
				} else {
					list.put("collect", "0");
				}
				/*
				 * list.put("collect", "0"); String sql6 =
				 * "select count(id) c from collection where udid = ? AND COMP_ID= ?";
				 * List<Map<String, Object>> list3 = jt.queryForList(sql6, new Object[]{udid
				 * ,sign}); if (list3 != null && list3.size() > 0) { Long l = (Long)
				 * list3.get(0).get("c"); //System.out.println(object); if (l != 0) {
				 * list.put("collect", "1"); //logger.info("-----是否收藏" + 1); } }
				 */
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if ((e instanceof IncorrectResultSizeDataAccessException) && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0) {
				logger.info("dao捕获查询异常");
				return null;
			} else {
				logger.info("dao捕获其他异常");
				return null;
			}
		}
		return null;
	}

	// 范文list
	public List<Map<String, Object>> findModelList(String class1, String subject, String numPerPage, String currentPage) {
		CurrentPage cp = new CurrentPage();
		String sql = "";
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Integer count = 0;// 阅读人数
		if (class1.equals("")) {
			if (subject.equals("")) {
				sql = "SELECT ID,CONTENT,NAME,PUBLISH_TIME,AUTHOR,AGE_DETAIL,AGE_SCALE,TYPE,E_TYPE,STYLE,C_TYPE FROM t_res_composion";// 查询全部
			} else {
				sql = "SELECT ID,CONTENT,NAME,PUBLISH_TIME,AUTHOR,AGE_DETAIL,AGE_SCALE,TYPE,E_TYPE,STYLE,C_TYPE FROM t_res_composion WHERE TYPE='" + subject + "'";// 查询条件subject
			}
		} else if (subject.equals("")) {
			sql = "SELECT ID,CONTENT,NAME,PUBLISH_TIME,AUTHOR,AGE_DETAIL,TYPE,AGE_SCALE,E_TYPE,STYLE,C_TYPE FROM t_res_composion WHERE AGE_DETAIL='" + class1 + "'";// 查询条件class1
		} else {
			sql = "SELECT ID,CONTENT,NAME,PUBLISH_TIME,AUTHOR,AGE_DETAIL,AGE_SCALE,TYPE,E_TYPE,STYLE,C_TYPE FROM t_res_composion WHERE TYPE='" + subject + "' AND AGE_DETAIL='" + class1 + "'";// 查询条件class1\subject
		}
		cp.Page(sql, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
		List<Map<String, Object>> re = cp.getResultList();
		if (re != null && re.size() > 0) {
			for (int i = 0; i < re.size(); i++) {
				map.put("content", re.get(i).get("CONTENT").toString());
				// 024 素材类型 026 技法类型 013 范文类型015推荐作文
				count = jt.queryForObject("SELECT READ_NUM FROM read_no WHERE TYPE = '013' AND COMP_ID=?", new Object[] { re.get(i).get("ID") }, Integer.class);
				map.put("number", count);
				map.put("sign", re.get(i).get("ID").toString());
				map.put("title", re.get(i).get("NAME").toString());
				if (re.get(i).get("PUBLISH_TIME") == (null)) {
					map.put("time", "");
				} else {
					map.put("time", CommUtils.ObjectTime2String(re.get(i).get("PUBLISH_TIME")));
				}
				if (re.get(i).get("AUTHOR") == (null)||re.get(i).get("AUTHOR").toString().equals("")) {
					map.put("author", "佚名");
				} else {
					map.put("author", re.get(i).get("AUTHOR").toString());
				}
				map.put("grade", "无");
				if (re.get(i).get("AGE_DETAIL") != null && !re.get(i).get("AGE_DETAIL").toString().equals("")) {
					map.put("grade", re.get(i).get("AGE_DETAIL").toString());
				}
				if (re.get(i).get("AGE_SCALE") != null && "无".equals(map.get("grade")) && !re.get(i).get("AGE_SCALE").toString().equals("")) {
					String sql1 = "select code_name from t_base_code where code = ?";
					List<Map<String,Object>> list2 = jt.queryForList(sql1, new Object[]{re.get(i).get("AGE_SCALE")});
					if (list2 != null && list2.size() > 0) {
						String code_name = (String) list2.get(0).get("code_name");
						map.put("grade", code_name);
					}
				}
				map.put("type", "无");
				String code = "";
				if(re.get(i).get("STYLE") != (null)||!re.get(i).get("STYLE").toString().equals("")){
					code = re.get(i).get("STYLE").toString();
				} else if(re.get(i).get("C_TYPE") != (null)||!re.get(i).get("C_TYPE").toString().equals("")){
					code = re.get(i).get("C_TYPE").toString();
				} else if(re.get(i).get("E_TYPE") != (null)||!re.get(i).get("E_TYPE").toString().equals("")){
					code = re.get(i).get("E_TYPE").toString();
				} else if (re.get(i).get("TYPE") != (null)||!re.get(i).get("TYPE").toString().equals("")) {
					code = re.get(i).get("TYPE").toString();
				}
				if(code!=""&&!"".equals(code)) {
					String sql7 = "select CODE_NAME from t_base_code where CODE=?";
					List<Map<String, Object>> list2 = jt.queryForList(sql7, new Object[] { code });
					String cname = null;
					if (list2 != null && list2.size() > 0) {
						cname = (String) list2.get(0).get("CODE_NAME");
					}
					if (cname != null && !TextUtils.isEmpty(cname)) {
						map.put("type", cname);
					} 
				}
				list.add(map);
			}
		}
		return list;
	}

	// 素材list
	public List<Map<String, Object>> findMaterialList(String class1, String grade, String numPerPage, String currentPage) {
		CurrentPage cp = new CurrentPage();
		String sql = "";
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Integer count = 0;// 阅读人数
		if (class1.equals("")) {
			if (grade.equals("")) {
				sql = "SELECT ID,CONTENT,NAME,AUTHOR,CREATE_TIME,AGE_DETAIL,AGE_SCALE,C_TYPE,STYLE FROM t_res_composion_sc";// 查询全部
			} else {
				sql = "SELECT ID,CONTENT,NAME,AUTHOR,CREATE_TIME,AGE_DETAIL,AGE_SCALE,C_TYPE,STYLE FROM t_res_composion_sc WHERE C_TYPE='" + class1 + "'";// 查询条件grade
			}
		} else if (grade.equals("")) {
			sql = "SELECT ID,CONTENT,NAME,AUTHOR,CREATE_TIME,AGE_DETAIL,AGE_SCALE,C_TYPE,STYLE FROM t_res_composion_sc WHERE AGE_DETAIL='" + grade + "'";// 查询条件class1
		} else {
			sql = "SELECT ID,CONTENT,NAME,AUTHOR,CREATE_TIME,AGE_DETAIL,AGE_SCALE,C_TYPE,STYLE FROM t_res_composion_sc WHERE C_TYPE='" + class1 + "' AND AGE_DETAIL='" + grade + "'";// 查询条件class1\grade
		}
		cp.Page(sql, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
		List<Map<String, Object>> re = cp.getResultList();
		if (re != null && re.size() > 0) {
			for (int i = 0; i < re.size(); i++) {
				map.put("content", re.get(i).get("CONTENT").toString());
				// 024 素材类型 026 技法类型 013 范文类型015推荐作文
				count = jt.queryForObject("SELECT READ_NUM FROM read_no WHERE TYPE = '024' AND COMP_ID=?", new Object[] { re.get(i).get("ID") }, Integer.class);
				map.put("number", count);
				map.put("sign", re.get(i).get("ID").toString());
				map.put("title", re.get(i).get("NAME").toString());
				if (re.get(i).get("AUTHOR") == (null) || re.get(i).get("AUTHOR").toString().equals("")) {
					map.put("author", "佚名");
				} else {
					map.put("author", re.get(i).get("AUTHOR").toString());
				}
				map.put("grade", "无");
				if (re.get(i).get("AGE_DETAIL") != null && !re.get(0).get("AGE_DETAIL").toString().equals("")) {
					map.put("grade", re.get(0).get("AGE_DETAIL").toString());
				}
				if (re.get(i).get("AGE_SCALE") != null && "无".equals(map.get("grade")) && !re.get(i).get("AGE_SCALE").toString().equals("")) {
					String sql1 = "select code_name from t_base_code where code = ?";
					List<Map<String,Object>> list2 = jt.queryForList(sql1, new Object[]{re.get(0).get("AGE_SCALE")});
					if (list2 != null && list2.size() > 0) {
						String code_name = (String) list2.get(0).get("code_name");
						map.put("grade", code_name);
					}
				}
				map.put("type", "无");
				String code = "";
				if (re.get(i).get("C_TYPE") != null && !re.get(i).get("C_TYPE").toString().equals("")) {
					code = re.get(i).get("C_TYPE").toString();
				}
				if ("".equals(code) && re.get(i).get("STYLE") != null && !re.get(i).get("STYLE").toString().equals("")) {
					code = re.get(i).get("STYLE").toString();
				}
				if(code!=""&&!"".equals(code)) {
					String sql7 = "select CODE_NAME from t_base_code where CODE=?";
					List<Map<String,Object>> list2 = jt.queryForList(sql7, new Object[]{code});
					String cname = "";
					if (list2 != null && list2.size() > 0) {
						cname = (String) list2.get(0).get("CODE_NAME");
					}
					if (cname != null && !TextUtils.isEmpty(cname)) {
						map.put("type", cname);
					} 
				}
				if (re.get(i).get("CREATE_TIME") == (null)) {
					map.put("time", "");
				} else {
					map.put("time", CommUtils.ObjectTime2String(re.get(i).get("CREATE_TIME")));
				}
				list.add(map);
			}
		}
		return list;
	}

	// 技法list
	public List<Map<String, Object>> findTechniqueList(String type, String grade, String numPerPage, String currentPage) {
		CurrentPage cp = new CurrentPage();
		String sql = "";
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Integer count = 0;// 阅读人数
		if (type.equals("")) {
			if (grade.equals("")) {
				sql = "SELECT ID,CONTENT,NAME,CREATE_TIME,AUTHOR,AGE_DETAIL,AGE_SCALE,C_TYPE,STYLE FROM t_res_composion_jf";// 查询全部
			} else {
				sql = "SELECT ID,CONTENT,NAME,CREATE_TIME,AUTHOR,AGE_DETAIL,AGE_SCALE,C_TYPE,STYLE FROM t_res_composion_jf WHERE C_TYPE='" + type + "'";
			}
		} else if (grade.equals("")) {
			sql = "SELECT ID,CONTENT,NAME,CREATE_TIME,AUTHOR,AGE_DETAIL,AGE_SCALE,C_TYPE,STYLE FROM t_res_composion_jf WHERE AGE_DETAIL='" + grade + "'";
		} else {
			sql = "SELECT ID,CONTENT,NAME,CREATE_TIME,AUTHOR,AGE_DETAIL,AGE_SCALE,C_TYPE,STYLE FROM t_res_composion_jf WHERE (C_TYPE='" + type + "') AND AGE_DETAIL='" + grade + "'";// 查询条件class1\grade
		}
		cp.Page(sql, Integer.parseInt(currentPage), Integer.parseInt(numPerPage), jt);
		List<Map<String, Object>> re = cp.getResultList();
		if (re != null && re.size() > 0) {
			for (int i = 0; i < re.size(); i++) {
				map.put("content", re.get(i).get("CONTENT").toString());
				// 024 素材类型 026 技法类型 013 范文类型015推荐作文
				count = jt.queryForObject("SELECT READ_NUM FROM read_no WHERE TYPE = '026' AND COMP_ID=?", new Object[] { re.get(i).get("ID") }, Integer.class);
				map.put("number", count);
				map.put("sign", re.get(i).get("ID").toString());
				map.put("title", re.get(i).get("NAME").toString());
				if (re.get(i).get("AUTHOR") == (null) || re.get(i).get("AUTHOR").toString().equals("")) {
					map.put("author", "佚名");
				} else {
					map.put("author", re.get(i).get("AUTHOR").toString());
				}
				map.put("grade", "无");
				if (re.get(i).get("AGE_DETAIL") != null && !re.get(0).get("AGE_DETAIL").toString().equals("")) {
					map.put("grade", re.get(0).get("AGE_DETAIL").toString());
				}
				if (re.get(i).get("AGE_SCALE") != null && "无".equals(map.get("grade")) && !re.get(i).get("AGE_SCALE").toString().equals("")) {
					String sql1 = "select code_name from t_base_code where code = ?";
					List<Map<String,Object>> list2 = jt.queryForList(sql1, new Object[]{re.get(0).get("AGE_SCALE")});
					if (list2 != null && list2.size() > 0) {
						String code_name = (String) list2.get(0).get("code_name");
						map.put("grade", code_name);
					}
				}
				map.put("type", "无");
				String code = "";
				if (re.get(i).get("C_TYPE") != null && !re.get(i).get("C_TYPE").toString().equals("")) {
					code = re.get(i).get("C_TYPE").toString();
				}
				if ("".equals(code) && re.get(i).get("STYLE") != null && !re.get(i).get("STYLE").toString().equals("")) {
					code = re.get(i).get("STYLE").toString();
				}
				if(code!=""&&!"".equals(code)) {
					String sql7 = "select CODE_NAME from t_base_code where CODE=?";
					List<Map<String,Object>> list2 = jt.queryForList(sql7, new Object[]{code});
					String cname = "";
					if (list2 != null && list2.size() > 0) {
						cname = (String) list2.get(0).get("CODE_NAME");
					}
					if (cname != null && !TextUtils.isEmpty(cname)) {
						map.put("type", cname);
					} 
				}
				if (re.get(i).get("CREATE_TIME") == (null)) {
					map.put("time", "");
				} else {
					map.put("time", CommUtils.ObjectTime2String(re.get(i).get("CREATE_TIME")));
				}
				list.add(map);
			}
		}
		return list;
	}

}
