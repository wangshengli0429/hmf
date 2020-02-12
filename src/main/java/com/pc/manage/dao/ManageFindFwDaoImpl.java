package com.pc.manage.dao;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.util.CommUtils;
import com.util.CurrentPage;
/**
 * 资源管理
 */
public class ManageFindFwDaoImpl {
	private JdbcTemplate jt = CommUtils.getJdbcTemplate();
	
	//（题材类型，公共代码CODE_TYPE=033） - type
	public List<Map<String, Object>> findType() {
        String sql = "select * from t_base_code where CODE_TYPE='033'";
        List<Map<String, Object>> list = jt.queryForList(sql);
        return list;
	}
	//（（年级,年级表grade） - ageDetail
	public List<Map<String, Object>> findAgedetail() {
		String sql = "select * from grade";
		List<Map<String, Object>> forList = jt.queryForList(sql);
		return forList;
	}
	//（年级等级，公共代码CODE_TYPE=014） - ageScale
	public List<Map<String, Object>> findAgescale() {
		String sql = "select * from t_base_code where CODE_TYPE='014'";
		List<Map<String, Object>> forList = jt.queryForList(sql);
		return forList;
	}
	//（素材分类，公共代码CODE_TYPE=024） - type
	public List<Map<String, Object>> findCtype() {
		 String sql = "select * from t_base_code where CODE_TYPE='024'";
		 List<Map<String, Object>> forList = jt.queryForList(sql);
		return forList;
	}
	//（技法分类，公共代码CODE_TYPE=032、033） - type
	public List<Map<String, Object>> findType2() {
		String sql = "select * from t_base_code where CODE_TYPE='033' or CODE_TYPE='032'";
        List<Map<String, Object>> list = jt.queryForList(sql);
        return list;
	}

	//范文集合
	public CurrentPage findFw(int currentPage, int numPerPage, String nj, String tc, String name) {
		String sql = "select res.ID id, res.NAME name, res.AUTHOR author, res.PUBLISH_TIME time,res.AGE_DETAIL ageDetail, res.TYPE type, res.STYLE style from t_res_composion res where STATUS='004002' and CAUSE='008004' ";
		if(nj!=null&&nj!=""){
			sql += " and res.AGE_DETAIL='"+ nj +"'";
		}
		if(tc!=null&&tc!=""){
			String str = tc.substring(0,3);
			if(str.equals("033")){
				sql += " and res.TYPE='"+ tc +"'";
			}else if(str.equals("013")){
				sql += " and res.STYLE='"+ tc +"'";
			}
		}
		if(name!=null&&name!=""){
			//sql += " and (res.NAME like '%"+ name +"%' or res.KEY_WORD like '%"+ name +"%')";
			sql += " and (res.NAME like '%"+ name +"%' )";
		}
		sql += " ORDER BY res.PUBLISH_TIME DESC";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		List<Map<String,Object>> resultList = page.getResultList();
		Map<String,Object> map = new HashMap<String,Object>();
		for(int i=0;i<resultList.size();i++){
			int id = Integer.parseInt(resultList.get(i).get("id").toString());
			//查询阅读人数
			String sql1="select READ_NUM rr from read_no where TYPE=013 and COMP_ID="+id;
			List<Map<String,Object>> forList = jt.queryForList(sql1);
			String yuedi = "0";
			if(forList.size()>0){
				yuedi =forList.get(0).get("rr").toString();
			}
			resultList.get(i).put("yuedi", yuedi);
			//查询收藏人数
			String sql2="select count(ID) cc from collection where DIST=013 and COMP_ID="+id;
			String shoucang = jt.queryForList(sql2).get(0).get("cc").toString();
			resultList.get(i).put("shoucang", shoucang);
		}
		page.setResultList(resultList);
		return page;
	}
	
	//添加范文-提交  
	public int addFenwen(String name, String author, String ageScale, String ageDetail, String type, String content) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		String sql = "";
		String str = type.substring(0,3);
		if(str.equals("033")){
			sql = "insert into t_res_composion (NAME,CONTENT,CREATE_TIME,PUBLISH_TIME,TYPE,AGE_SCALE,AGE_DETAIL,AUTHOR,STATUS,CAUSE) values (?,?,?,?,?,?,?,?,'004002','008004') ";
		}else if(str.equals("013")){
			sql = "insert into t_res_composion (NAME,CONTENT,CREATE_TIME,PUBLISH_TIME,STYLE,AGE_SCALE,AGE_DETAIL,AUTHOR,STATUS,CAUSE) values (?,?,?,?,?,?,?,?,'004002','008004') ";
		}
		int i = jt.update(sql, new Object[] {name,content,time,time,type,ageScale,ageDetail,author},
				new int[] {Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.TIMESTAMP,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR});
		return i;
	}
	
	//删除范文
	public int deleteFw(String id) {
		int intId = Integer.parseInt(id);
		String sql = "delete from t_res_composion where ID = "+intId;
		int i = jt.update(sql);
		return i;
	}
	
	//修改范文 -回显
	public List<Map<String, Object>> toFindFw(String id) {
		int intId = Integer.parseInt(id);
		String sql = "select ID id, NAME name, AUTHOR author, CONTENT content, TYPE type, STYLE style, AGE_DETAIL ageDetail from t_res_composion where ID = "+intId;
		List<Map<String, Object>> list = jt.queryForList(sql);
		if (list != null && list.size() > 0 && list.get(0).get("content") != null && !list.get(0).get("content").equals("")) {
			String content = CommUtils.deleteXmlForComposition((String)(list.get(0).get("content")));
			list.get(0).put("content", content);
		}
		return list;
	}
	
	//修改范文 -提交
	public int updateFenwen(int id, String name, String author, String ageScale, String ageDetail, String type, String content) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		String sql ="";
		String str = type.substring(0,3);
		int i = 0;
		if(str.equals("033")){
			//sql ="update t_res_composion set NAME='"+name+"', AUTHOR='"+author+"', CONTENT='"+content+"', TYPE='"+type+"' , STYLE=null , AGE_SCALE='"+ageScale+"' , AGE_DETAIL='"+ageDetail+"' ,CREATE_TIME='"+time+"' where ID="+id;
			sql ="update t_res_composion set NAME=?, AUTHOR=?, CONTENT=?, TYPE=? , STYLE=null , AGE_SCALE=? , AGE_DETAIL=? ,PUBLISH_TIME=? where ID=?";
			i = jt.update(sql, new Object[] {name,author,content,type,ageScale,ageDetail,time,id},new int[] {Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.INTEGER});
		}else if(str.equals("013")){
			//sql ="update t_res_composion set NAME='"+name+"', AUTHOR='"+author+"', CONTENT='"+content+"', TYPE=null , STYLE='"+type+"' , AGE_SCALE='"+ageScale+"' , AGE_DETAIL='"+ageDetail+"' ,CREATE_TIME='"+time+"' where ID="+id;
			sql ="update t_res_composion set NAME=?, AUTHOR=?, CONTENT=?, TYPE=null , STYLE=? , AGE_SCALE=? , AGE_DETAIL=? ,PUBLISH_TIME=? where ID=?";
			i = jt.update(sql, new Object[] {name,author,content,type,ageScale,ageDetail,time,id},new int[] {Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.INTEGER});
		}
		
		//int i = jt.update(sql);
		return i;
	}
	
	//查看范文
	public List<Map<String, Object>> findFindFw(String id) {
		int intId = Integer.parseInt(id);
		String sql = "select res.ID id, res.NAME name, res.AUTHOR author, res.PUBLISH_TIME time,res.AGE_DETAIL ageDetail, res.TYPE type, res.STYLE style , res.CONTENT content from t_res_composion res where ID = "+intId;
		List<Map<String, Object>> forList = jt.queryForList(sql);
		Map<String,Object> map = new HashMap<String,Object>();
		//查询阅读人数
		String sql1="select READ_NUM rr from read_no where TYPE=013 and COMP_ID="+intId;
		List<Map<String,Object>> forList2 = jt.queryForList(sql1);
		String yuedu = "0";
		if(forList2.size()>0){
			yuedu =forList2.get(0).get("rr").toString();
		}
		forList.get(0).put("yuedu", yuedu);
		//查询收藏人数
		String sql2="select count(ID) cc from collection where DIST=013 and COMP_ID="+intId;
		String shoucang = jt.queryForList(sql2).get(0).get("cc").toString();
		forList.get(0).put("shoucang", shoucang);
		return forList;
	}
	
	//素材集合
	public CurrentPage findSc(int currentPage, int numPerPage, String nj, String fl, String name) {
		String sql = "select c.*,b2.CODE_NAME type1 from (select a.*,b.CODE_NAME ageDetail1 from (select res.ID id, res.NAME name,res.KEY_WORD keyword, res.CREATE_TIME time,res.AGE_SCALE ageDetail, res.C_TYPE type, res.STATUS from t_res_composion_sc res )a left join t_base_code b on a.ageDetail=b.CODE)c left join t_base_code b2 on c.type=b2.CODE where c.STATUS='004002' ";
		if(nj!=null&&nj!=""){
			sql += " and c.ageDetail1 = '"+ nj +"'";
		}
		if(fl!=null&&fl!=""){
			sql += " and c.type = '"+ fl +"'";
		}
		if(name!=null&&name!=""){
			sql += " and (c.name like '%"+ name +"%' or c.keyword like '%"+ name +"%')";
		}
		sql += " ORDER BY c.time DESC";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		List<Map<String,Object>> resultList = page.getResultList();
		if(resultList.size()>0){
			for(int i=0;i<resultList.size();i++){
				int id = Integer.parseInt(resultList.get(i).get("id").toString());
				//查询阅读人数
				String sql1="select READ_NUM rr from read_no where TYPE=024 and COMP_ID="+id;
				List<Map<String,Object>> forList = jt.queryForList(sql1);
				String yuedi = "0";
				if(forList.size()>0){
					yuedi =forList.get(0).get("rr").toString();
				}
				resultList.get(i).put("yuedi", yuedi);
				//查询收藏人数
				String sql2="select count(ID) cc from collection where DIST=024 and COMP_ID="+id;
				String shoucang = jt.queryForList(sql2).get(0).get("cc").toString();
				resultList.get(i).put("shoucang", shoucang);
			}
			page.setResultList(resultList);
		}
		return page;
	}
	
	//添加素材	-提交
	public int addSucai(String name, String ageDetail, String type, String content) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		String sql = "insert into t_res_composion_sc (NAME,CONTENT,CREATE_TIME,C_TYPE,AGE_SCALE,STATUS) values (?,?,?,?,?,'004002') ";
		int i = jt.update(sql, new Object[] {name,content,time,type,ageDetail},
				new int[] {Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.VARCHAR,Types.VARCHAR});
		return i;
	}
	
	//删除  素材
	public int deleteSc(String id) {
		int intId = Integer.parseInt(id);
		String sql = "delete from t_res_composion_sc where ID = "+intId;
		int i = jt.update(sql);
		return i;
	}
	
	//修改素材素材 -回显
	public List<Map<String, Object>> toFindSc(String id) {
		int intId = Integer.parseInt(id);
		String sql = "select ID id, NAME name, CONTENT content, C_TYPE type , AGE_SCALE ageDetail from t_res_composion_sc where ID = "+intId;
		List<Map<String, Object>> list = jt.queryForList(sql);
		if (list != null && list.size() > 0 && list.get(0).get("content") != null && !list.get(0).get("content").equals("")) {
			String content = CommUtils.deleteXmlForComposition((String)(list.get(0).get("content")));
			list.get(0).put("content", content);
		}
		return list;
	}
	
	//修改素材 -提交
	public int updateSucai(int id, String name, String ageDetail, String type, String content) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		String sql = "update t_res_composion_sc set NAME=?,CONTENT=?,CREATE_TIME=?,C_TYPE=?,AGE_SCALE=? where ID=? ";
		int i = jt.update(sql, new Object[] {name,content,time,type,ageDetail,id},
				new int[] {Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.VARCHAR,Types.VARCHAR,Types.INTEGER});
		return i;
	}
	
	//查看素材
	public List<Map<String, Object>> findFindSc(String id) {
		int intId = Integer.parseInt(id);
		String sql = "select c.*,b2.CODE_NAME type1 from (select a.*,b.CODE_NAME ageDetail1 from (select res.ID id, res.NAME name, res.CREATE_TIME time,res.AGE_SCALE ageDetail, res.C_TYPE type, res.CONTENT content from t_res_composion_sc res )a left join t_base_code b on a.ageDetail=b.CODE)c left join t_base_code b2 on c.type=b2.CODE where ID ="+intId;
		List<Map<String, Object>> forList = jt.queryForList(sql);
		Map<String,Object> map = new HashMap<String,Object>();
		//查询阅读人数
		String sql1="select READ_NUM rr from read_no where TYPE=024 and COMP_ID="+intId;
		List<Map<String,Object>> forList2 = jt.queryForList(sql1);
		String yuedu = "0";
		if(forList2.size()>0){
			yuedu =forList2.get(0).get("rr").toString();
		}
		forList.get(0).put("yuedu", yuedu);
		//查询收藏人数
		String sql2="select count(ID) cc from collection where DIST=024 and COMP_ID="+intId;
		String shoucang = jt.queryForList(sql2).get(0).get("cc").toString();
		forList.get(0).put("shoucang", shoucang);
		return forList;
	}
	
	//技法集合   - 条件+分页
	public CurrentPage findJf(int currentPage, int numPerPage, String nj, String fl, String name) {
		String sql = "select a.*,b.CODE_NAME ageDetail from (select res.ID id, res.NAME name, res.KEY_WORD keyword, res.CREATE_TIME time,res.AGE_SCALE ageDetail1, res.TYPE type, res.STYLE style, res.STATUS from t_res_composion_jf res )a left join t_base_code b on a.ageDetail1=b.CODE where a.STATUS='004002'";
		if(nj!=null&&nj!=""){
			sql += " and a.ageDetail1 = '"+ nj +"'";
		}
		if(fl!=null&&fl!=""){
			String str = fl.substring(0,3);
			if(str.equals("032")){
				sql += " and a.type='"+ fl +"'";
			}else if(str.equals("033")){
				sql += " and a.style='"+ fl +"'";
			}
		}
		if(name!=null&&name!=""){
			sql += " and (a.name like '%"+ name +"%' or a.keyword like '%"+ name +"%')";
		}
		sql += " ORDER BY a.time DESC";
		System.out.println(sql);
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		List<Map<String,Object>> resultList = page.getResultList();
		if(resultList.size()>0){
			for(int i=0;i<resultList.size();i++){
				int id = Integer.parseInt(resultList.get(i).get("id").toString());
				//查询阅读人数
				String sql1="select READ_NUM rr from read_no where TYPE=026 and COMP_ID="+id;
				List<Map<String,Object>> forList = jt.queryForList(sql1);
				String yuedi = "0";
				if(forList.size()>0){
					yuedi =forList.get(0).get("rr").toString();
				}
				resultList.get(i).put("yuedi", yuedi);
				//查询收藏人数
				String sql2="select count(ID) cc from collection where DIST=026 and COMP_ID="+id;
				String shoucang = jt.queryForList(sql2).get(0).get("cc").toString();
				resultList.get(i).put("shoucang", shoucang);
			}
			page.setResultList(resultList);
		}
		return page;
	}
	
	//添加技法  -提交
	public int addJifa(String name, String nj, String type, String content) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		String style = null;
		String type1 = null;
		String substring = type.substring(0, 3);
		if(substring.equals("033")){
			style = type;
		}else{
			type1 = type;
		}
		String sql = "insert into t_res_composion_jf (NAME,CONTENT,CREATE_TIME,STYLE,TYPE,AGE_SCALE,STATUS) values (?,?,?,?,?,?,'004002') ";
		int i = jt.update(sql, new Object[] {name,content,time,style,type1,nj},
				new int[] {Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR});
		return i;
	}
	
	//删除  技法
	public int deleteJf(String id) {
		int intId = Integer.parseInt(id);
		String sql = "delete from t_res_composion_jf where ID = "+intId;
		int i = jt.update(sql);
		return i;
	}
	//修改技法 -回显
	public List<Map<String, Object>> toFindJf(String id) {
		int intId = Integer.parseInt(id);
		String sql = "select res.ID id, res.NAME name, res.CONTENT content, res.CREATE_TIME time,res.AGE_SCALE ageDetail, res.TYPE type, res.STYLE style from t_res_composion_jf res where id="+intId;
		List<Map<String, Object>> list = jt.queryForList(sql);
		if (list != null && list.size() > 0 && list.get(0).get("content") != null && !list.get(0).get("content").equals("")) {
			String content = CommUtils.deleteXmlForComposition((String)(list.get(0).get("content")));
			list.get(0).put("content", content);
		}
		return list;
	}
	
	//修改技法 -提交
	public int updateJifa(int id, String name, String ageScale, String type, String content) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		String stype = null;
		String type1 = null;
		String substring = type.substring(0, 3);
		if(substring.equals("033")){
			stype = type;
		}else{
			type1 = type;
		}
		String sql = "update t_res_composion_jf set NAME=?,CONTENT=?,CREATE_TIME=?,STYLE=?,TYPE=?,AGE_SCALE=? where ID=? ";
		int i = jt.update(sql, new Object[] {name,content,time,stype,type1,ageScale,id},
				new int[] {Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.INTEGER});
		return i;
	}
	
	//查看技法
	public List<Map<String, Object>> findFindJf(String id) {
		int intId = Integer.parseInt(id);
		String sql = "select a.*,b.CODE_NAME ageDetail1 from (select res.ID id, res.NAME name, res.CREATE_TIME time,res.AGE_SCALE ageDetail, res.TYPE type, res.STYLE style, res.CONTENT content from t_res_composion_jf res)a left join t_base_code b on a.ageDetail=b.CODE where a.id ="+intId;
		List<Map<String, Object>> forList = jt.queryForList(sql);
		Map<String,Object> map = new HashMap<String,Object>();
		//查询阅读人数
		String sql1="select READ_NUM rr from read_no where TYPE=026 and COMP_ID="+intId;
		List<Map<String,Object>> forList2 = jt.queryForList(sql1);
		String yuedu = "0";
		if(forList2.size()>0){
			yuedu =forList2.get(0).get("rr").toString();
		}
		forList.get(0).put("yuedu", yuedu);
		//查询收藏人数
		String sql2="select count(ID) cc from collection where DIST=026 and COMP_ID="+intId;
		String shoucang = jt.queryForList(sql2).get(0).get("cc").toString();
		forList.get(0).put("shoucang", shoucang);
		return forList;
	}
	
	
}
