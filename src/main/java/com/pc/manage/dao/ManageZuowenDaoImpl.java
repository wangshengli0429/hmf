package com.pc.manage.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSON;
import com.util.BackRefundUtils;
import com.util.CommUtils;
import com.util.CurrentPage;
/**
 * 作文管理
 */
public class ManageZuowenDaoImpl {
	private static Logger logger = Logger.getLogger(ManageZuowenDaoImpl.class);
	private JdbcTemplate jt = CommUtils.getJdbcTemplate();

	//年级	：一年级到高三
	public List<Map<String, Object>> selectGeade() {
		String sql = "select CODE code, GRADE grade from grade";
		List<Map<String,Object>> queryForList = jt.queryForList(sql);
		return queryForList;
	}
	//素材年级	：小学-高中
	public List<Map<String, Object>> selectGeadeSucai() {
		String sql = "select CODE code, CODE_NAME grade from t_base_code where CODE_TYPE='014'";
		List<Map<String,Object>> queryForList = jt.queryForList(sql);
		return queryForList;
	}
	//体裁 ：CODE_TYPE=013
	public List<Map<String, Object>> selectStyless() {
		String sql = "select CODE code, CODE_NAME code_name,CODE_TYPE code_type from t_base_code where CODE_TYPE in ('013','033') ";
		List<Map<String,Object>> queryForList = jt.queryForList(sql);
		return queryForList;
	}
	public List<Map<String, Object>> selectStyle() {
		String sql = "select CODE code, CODE_NAME code_name,CODE_TYPE code_type from t_base_code where CODE_TYPE='013'";
		List<Map<String,Object>> queryForList = jt.queryForList(sql);
		return queryForList;
	}
	//体裁 ：CODE_TYPE=013
	public List<Map<String, Object>> selectStyle2() {
		String sql = "select CODE , CODE_NAME from t_base_code where CODE_TYPE='013'";
		List<Map<String,Object>> queryForList = jt.queryForList(sql);
		return queryForList;
	}
	//根据CODE查询年级等级
	public String findCodename(String string) {
		String sql = "select CODE_NAME grade from t_base_code where CODE='"+string+"'";
		List<Map<String,Object>> queryForList = jt.queryForList(sql);
		String string2 = queryForList.get(0).get("grade").toString();
		return string2;
	}
	
	//待点评作文集合   - 条件+分页
	public CurrentPage findDaidplist(int currentPage, int numPerPage, String name) {
		//String sql = "select c.TACHER_STATE tea_state,c.ID id, c.NEW_TITLE newTitle, s.NICKNAME author, s.id stu_id, s.PHONE sphone, c.GEADE geade, t.NAME name, t.PHONE tphone, c.DRAFT draft, c.OLD_TITLE oldTitle, c.CREATE_TIME createTime from s_order o , composition c , teacher t , student s where o.COMP_ID=c.ID and o.TEACHER_ID=t.ID and o.STU_ID=s.ID and o.STATE=2";
		//if(name!=null&&name!=""){
		//	sql += " and (c.NEW_TITLE like '%"+ name +"%' or s.NICKNAME like '%"+ name +"%' or t.NAME like '%"+ name +"%' or t.PHONE like '%"+ name +"%')";
		//}
		//sql += " ORDER BY c.CREATE_TIME DESC ";
		String sql = "select a.*, t.NAME name, t.PHONE tphone from(select c.TACHER_STATE tea_state,c.ID id, c.NEW_TITLE newTitle, s.NICKNAME author, s.id stu_id, s.PHONE sphone, c.GEADE geade, c.DRAFT draft, c.OLD_TITLE oldTitle, c.CREATE_TIME createTime ,o.TEACHER_ID teacherId from s_order o , composition c , student s where o.COMP_ID=c.ID and o.STU_ID=s.ID and o.STATE=2)a LEFT JOIN teacher t on a.teacherId=t.ID where 1=1";
		if(name!=null&&name!=""){
			sql += " and (a.newTitle like '%"+ name +"%' or a.author like '%"+ name +"%' or t.NAME like '%"+ name +"%' or t.PHONE like '%"+ name +"%')";
		}
		sql += " ORDER BY a.createTime DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//查看待点评作文
	public List<Map<String, Object>> selectDaidp(int id) {
		String sql = "select c.ID id, c.NEW_TITLE newTitle, c.PROPO propo, s.NICKNAME author, c.DRAFT draft, c.GEADE geade, c.CREATE_TIME createTime, c.CONTENT content, c.IMAGE1 img1, c.IMAGE2 img2, c.IMAGE3 img3 from s_order o , composition c , student s where o.COMP_ID=c.ID and o.STU_ID=s.ID and c.ID="+id;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}

	//已点评作文集合   - 条件+分页
	public CurrentPage findYidplist(int currentPage, int numPerPage, String grade, String grading, String name) {
		String sql = "select cc.ID id, c.ID comp_id, c.NEW_TITLE newTitle, s.NICKNAME author, s.PHONE sphone, c.GEADE geade,t.NAME name, t.PHONE tphone, c.DRAFT draft, c.OLD_TITLE oldTitle, c.CREATE_TIME createTime, c.OPEN open, cc.COM_TIME comTime, cc.GRADING grading from s_order o , composition c , com_composition cc , teacher t , student s where o.COMP_ID=c.ID and cc.TEACHER_ID=t.ID and o.STATE=3 and o.STU_ID=s.ID and cc.COMP_ID=c.ID";
		if(grade!=null&&grade!=""){
			String nj = "";
			if(grade.equals("1")){//小学
				nj = "027001,027002,027003,027004,027005,027006";
			}
			if(grade.equals("2")){//初中
				nj = "027011,027012,027013";
			}
			if(grade.equals("3")){//高中
				nj = "027021,027022,027023";
			}
			sql += " and c.GEADE in ("+nj+")";
		}
		if(grading!=null&&grading!=""){
			sql += " and cc.GRADING ='"+grading+"'";
		}
		if(name!=null&&name!=""){
			sql += " and (c.NEW_TITLE like '%"+ name +"%' or s.NICKNAME like '%"+ name +"%' or t.NAME like '%"+ name +"%' or t.PHONE like '%"+ name +"%')";
		}
		sql += " ORDER BY comTime DESC  ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//查看已点评作文
	public List<Map<String, Object>> selectYidp(int id) {
		String sql = "select cc.ID id, c.NEW_TITLE newTitle, c.PROPO propo,cc.SCORE score, s.NICKNAME author, c.DRAFT draft, c.GEADE geade, c.CREATE_TIME createTime, cc.CONTENT content, c.IMAGE1 img1, c.IMAGE2 img2, c.IMAGE3 img3, t.NAME name, cc.COM_TIME comTime, cc.VOICE voice ,cc.DP_CONTENT dpcontent ,cc.DP_LANGUAGE dplanguage ,cc.DP_STRUCTURE dpstructure ,cc.DP_WRITING dpwriting ,cc.SCORING scoring,cc.POINTS points,cc.SUGGEST suggest from s_order o , composition c , com_composition cc , teacher t , student s where o.COMP_ID=c.ID and cc.TEACHER_ID=t.ID and o.STATE=3 and o.STU_ID=s.ID and cc.COMP_ID=c.ID and cc.ID="+id;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}
	//评分标准信息
	public List<Map<String, Object>> pingfen(String geade) {
		String sql1 = "";
		if(geade.equals("低年级")||geade.equals("小学")||geade.equals("一年级")||geade.equals("二年级")||geade.equals("三年级")||geade.equals("四年级")||geade.equals("五年级")||geade.equals("六年级")){
			sql1 = "select * from pschool";
		}
		if(geade.equals("初中")||geade.equals("初一")||geade.equals("初二")||geade.equals("初三")||geade.equals("七年级")||geade.equals("八年级")||geade.equals("九年级")){
			sql1 = "select * from jschool";
		}
		if(geade.equals("高中")||geade.equals("高一")||geade.equals("高二")||geade.equals("高三")){
			sql1 = "select * from hschool";
		}
		List<Map<String, Object>> pingfen = jt.queryForList(sql1);
		return pingfen;
	}

	//预点评-已点评作文集合	条件+分页
	public CurrentPage findYudpYi(int currentPage, int numPerPage, String name) {
		String sql = "select e.ID id ,p.NEW_TITLE newTitle, t.NAME name ,t.PHONE tphone ,p.GEADE geade ,p.CREATE_TIME createTime ,e.COM_TIME comTime from expected_com e, pre_composition p, teacher t where e.COMP_ID=p.ID and e.TEACHER_ID=t.ID and e.STATE is not null ";
		if(name!=null&&name!=""){
			sql += " and (p.NEW_TITLE like '%"+ name +"%' or t.NAME like '%"+ name +"%' or t.PHONE like '%"+ name +"%')";
		}
		sql += " ORDER BY e.COM_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}
	
	//预点评-查看已点评作文
	public List<Map<String, Object>> selectYudpyi(int id) {
		String sql = "select e.ID id, p.NEW_TITLE newTitle, p.PROPO propo,e.SCORE score, p.AUTHOR author, p.DRAFT draft, p.GEADE geade, p.CREATE_TIME createTime,"
				+ " e.CONTENT content, p.IMAGE1 img1, p.IMAGE2 img2, p.IMAGE3 img3, t.NAME name, e.COM_TIME comTime,"
				+ " e.DP_CONTENT dpcontent ,e.DP_LANGUAGE dplanguage ,e.DP_STRUCTURE dpstructure ,e.DP_WRITING dpwriting,"
				+ " e.SCORING scoring, e.POINTS points, e.SUGGEST suggest from expected_com e ,pre_composition p ,teacher t"
				+ " where e.COMP_ID=p.ID and e.TEACHER_ID = t.ID and e.STATE is not null and e.ID="+id;
		List<Map<String,Object>> queryForList = jt.queryForList(sql);
		return queryForList;
	}
	
	//预点评-未点评作文集合	条件+分页
	public CurrentPage findYudpWei(int currentPage, int numPerPage, String name) {
		String sql = "select e.ID id ,p.NEW_TITLE newTitle, t.NAME name ,t.PHONE tphone ,p.GEADE geade ,p.CREATE_TIME createTime ,e.STATE from expected_com e, pre_composition p, teacher t where e.COMP_ID=p.ID and e.TEACHER_ID=t.ID and e.STATE is null ";
		if(name!=null&&name!=""){
			sql += " and (p.NEW_TITLE like '%"+ name +"%' or t.NAME like '%"+ name +"%' or t.PHONE like '%"+ name +"%')";
		}
		sql += " ORDER BY p.CREATE_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//预点评-未点评-删除
	public int deleteYudpwei(int id) {
		String sql = "delete from expected_com where ID ="+id;
		int i = jt.update(sql);
		return i;
		
	}

	//预点评-未点评    -修改    回显 
	public List<Map<String, Object>> getYudpwei(int id) {
		String sql = "select e.ID id, e.COMP_ID comId, p.NEW_TITLE newTitle, p.PROPO propo,e.SCORE score, p.AUTHOR author, p.DRAFT draft, p.GEADE geade, p.CREATE_TIME createTime,"
				+ " e.CONTENT content, p.IMAGE1 img1, p.IMAGE2 img2, p.IMAGE3 img3, t.NAME name, e.COM_TIME comTime,"
				+ " e.DP_CONTENT dpcontent ,e.DP_LANGUAGE dplanguage ,e.DP_STRUCTURE dpstructure ,e.DP_WRITING dpwriting,"
				+ " e.SCORING scoring, e.POINTS points, e.SUGGEST suggest from expected_com e ,pre_composition p ,teacher t"
				+ " where e.COMP_ID=p.ID and e.TEACHER_ID = t.ID and e.STATE is null and e.ID="+id;
		List<Map<String,Object>> queryForList = jt.queryForList(sql);
		return queryForList;
	}

	//预点评-未点评    -修改    提交 
	public int updateYudpwei(int id, int comId, String title, String oldTitle, String content, String grade) {
		String sql = "update pre_composition set NEW_TITLE='"+title+"', OLD_TITLE='"+oldTitle+"', CONTENT='"+content+"', GEADE='"+grade+"' where ID="+comId;
		int i = jt.update(sql);
		String sql1 = "update expected_com set CONTENT='"+content+"' where ID="+id;
		int i1 = jt.update(sql1);
		if(i>0&&i1>0){
			return i;
		}
		return 0;
	}

	//查看待点评作文
	public List<Map<String, Object>> selectYudpWei(int id) {
		String sql = "select e.ID id, p.NEW_TITLE newTitle,p.GEADE geade, e.CONTENT content, p.IMAGE1 img1, p.IMAGE2 img2, p.IMAGE3 img3"
				+ " from expected_com e ,pre_composition p ,teacher t where e.COMP_ID=p.ID and e.TEACHER_ID = t.ID and e.STATE is null and e.ID="+id;
		List<Map<String,Object>> queryForList = jt.queryForList(sql);
		return queryForList;
	}

	//上传预点评作文	-提交
	public int insertYudp(String title, String content, String grade) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		String sql = "insert into pre_composition (NEW_TITLE,CONTENT,GEADE,CREATE_TIME) values (?,?,?,?)";
		int i = jt.update(sql, new Object[] {title,content,grade,time},
				new int[] {Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP});
		return i;
	}

	//作文推荐 	条件+分页
	public CurrentPage findYudpTuijian(int currentPage, int numPerPage, String name) {
		String sql = "select r.ID id ,r.NAME name ,r.AUTHOR author ,r.AGE_SCALE ageScale ,r.AGE_DETAIL ageDetail ,r.PUBLISH_TIME publishTime , b.CODE_NAME style ,r.DISPLAY display ,r.COM_ID comId from recommend_com r LEFT JOIN t_base_code b on b.`CODE`=r.TYPE or b.`CODE`=r.STYLE where 1=1";
		if(name!=null&&name!=""){
			sql += " and (r.NAME like '%"+ name +"%' or r.AUTHOR like '%"+ name +"%')";
		}
		sql += " ORDER BY r.PUBLISH_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		List<Map<String,Object>> resultList = page.getResultList();
		for(int i=0;i<resultList.size();i++){
			int id = Integer.parseInt(resultList.get(i).get("id").toString());
			//查询阅读人数
			String sql1="select READ_NUM rr from read_no where TYPE=015 and COMP_ID="+id;
			List<Map<String,Object>> forList = jt.queryForList(sql1);
			String yuedi = "0";
			if(forList.size()>0){
				yuedi = forList.get(0).get("rr").toString();
			}
			resultList.get(i).put("yuedi", yuedi);
			//查询收藏人数
			String sql2="select count(ID) cc from collection where DIST=015 and COMP_ID="+id;
			String shoucang = jt.queryForList(sql2).get(0).get("cc").toString();
			resultList.get(i).put("shoucang", shoucang);
		}
		return page;
	}
	
	//修改推荐作文显示状态
	public int updateType(int id, int display) {
		String sql = "";
		if(display==1){
			sql = "update recommend_com set DISPLAY=2 where ID="+id;
		}else {
			sql = "update recommend_com set DISPLAY=1 where ID="+id;
		}
		int i = jt.update(sql);
		return i;
	}

	//修改推荐作文 	回显
	public List<Map<String, Object>> selectTuijian(int id) {
		String sql = "select ID id, NAME name, CONTENT content, AUTHOR author, AGE_SCALE ageScale, AGE_DETAIL ageDetail ,STYLE style from recommend_com where ID="+id;
		List<Map<String,Object>> queryForList = jt.queryForList(sql);
		return queryForList;
	}

	//修改推荐作文 	提交修改
	public int updateTuijian(int id, String title, String content, String author, String nj, String style) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		String sql = "";
		String sql1 = "";
		String str =nj.substring(0, 3);
		String ageScale = "";
		String ageDetail = "";
		if(str.equals("027")){
			Object[] parseAge = CommUtils.parseAge(nj);
			ageScale = parseAge[0].toString();
			ageDetail = parseAge[1].toString();
			//sql = "update recommend_com set NAME='"+ title +"', AGE_SCALE='"+ ageScale  +"', AGE_DETAIL='"+ ageDetail +"' ,PUBLISH_TIME='"+ time +"'";
			sql = "update recommend_com set NAME=?, AGE_SCALE=?, AGE_DETAIL=? ,PUBLISH_TIME=?";
		}else {
			//sql1 = "update recommend_com set NAME='"+ title +"', AGE_SCALE='"+ nj +"', AGE_DETAIL=null, PUBLISH_TIME='"+ time +"'";
			sql1 = "update recommend_com set NAME=?, AGE_SCALE=?, AGE_DETAIL=null, PUBLISH_TIME=?";
		}
		if(content!=null&&content!=""){
			//sql += ", CONTENT='"+ content +"'";
			sql += ", CONTENT=?";
			sql1 += ", CONTENT=?";
		}
		if(author!=null&&author!=""){
			//sql += ", AUTHOR='"+ author +"'";
			sql += ", AUTHOR=?";
			sql1 += ", AUTHOR=?";
		}
		if(style!=null&&!style.equals("")){
			//sql += ", STYLE='"+ style +"'";
			sql += ", STYLE=?";
			sql1 += ", STYLE=?";
		}
		//sql += " where ID="+id;
		sql += " where ID=?";
		sql1 += " where ID=?";
		int i = 0;
		if(str.equals("027")){
			i = jt.update(sql, new Object[] {title,ageScale,ageDetail,time,content,author,style,id},
					new int[] {Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.INTEGER});
		}else{
			i = jt.update(sql1, new Object[] {title,nj,time,content,author,style,id},
					new int[] {Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.INTEGER});
		}
		//int i = jt.update(sql);
		return i;
	}

	//删除推荐作文
	public int daleteTuijian(int id) {
		String sql = "delete from recommend_com where ID="+id;
		int i = jt.update(sql);
		return i;
	}
	
	//添加推荐作文	提交
	public int insertTuijian(String title, String content, String author, String nj, String style) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		int i = 0;
		String str =style.substring(0, 3);
		if(str.equals("033")){
			String sql = "insert into recommend_com ( NAME, CONTENT, AUTHOR, AGE_DETAIL, CREATE_TIME, PUBLISH_TIME, TYPE, DISPLAY) values (?,?,?,?,?,?,?,1)";
			i = jt.update(sql, new Object[] {title,content,author,nj,time,time,style},
					new int[] {Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.TIMESTAMP,Types.VARCHAR});
		}else {
			String sql = "insert into recommend_com ( NAME, CONTENT, AUTHOR, AGE_DETAIL, CREATE_TIME, PUBLISH_TIME, STYLE, DISPLAY) values (?,?,?,?,?,?,?,1)";
			i = jt.update(sql, new Object[] {title,content,author,nj,time,time,style},
					new int[] {Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.TIMESTAMP,Types.VARCHAR});
		}
		
		return i;
	}

	//评价管理		条件+分页
	public CurrentPage findYudpPingjia(int currentPage, int numPerPage, String satisfaction, String name) {
		String sql = "select s.NICKNAME sname, a.COMP_ID comId, c.NEW_TITLE title, a.ID app_id, t.NAME tname, a.SATISFACTION satisfaction ,a.ATTIT attit ,a.PROFES profes ,a.AVERAGE average ,c.GEADE grade ,a.APPR_TIME apprTime ,a.MESSAGE_TIME messageTime from appraise a, student s,composition c ,teacher t where a.UDID=s.UDID and a.COMP_ID=c.ID and a.TEACHER_ID=t.ID";
		if(satisfaction!=null&&satisfaction!=""){
			int satis = Integer.parseInt(satisfaction);
			sql += " and a.SATISFACTION="+satis;
		}
		if(name!=null&&name!=""){
			sql += " and (s.NICKNAME like '%"+ name +"%' or c.NEW_TITLE like '%"+ name +"%' or t.NAME like '%"+ name +"%')";
		}
		sql += " ORDER BY a.APPR_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}
	
	//查看评价作文
	public List<Map<String, Object>> selectPingjiaZw(int id) {
		//String sql = "select c.NEW_TITLE title, s.NICKNAME author, c.DRAFT draft, c.GEADE geade, cc.SCORE score, cc.CONTENT content, c.IMAGE1 img1 ,c.IMAGE2 img2,c.IMAGE3 img3 from com_composition cc ,composition c , student s where cc.COMP_ID=c.ID and cc.STU_ID=s.ID and c.ID="+id;
		String sql = "select cc.ID cid, c.ID id, c.NEW_TITLE newTitle, c.PROPO propo,cc.SCORE score, s.NICKNAME author, c.DRAFT draft, c.GEADE geade, c.CREATE_TIME createTime, cc.CONTENT content, c.IMAGE1 img1, c.IMAGE2 img2, c.IMAGE3 img3, t.NAME name, cc.COM_TIME comTime, cc.VOICE voice,cc.DP_CONTENT dpcontent ,cc.DP_LANGUAGE dplanguage ,cc.DP_STRUCTURE dpstructure ,cc.DP_WRITING dpwriting ,cc.SCORING scoring,cc.POINTS points,cc.SUGGEST suggest, a.SATISFACTION satisfaction, a.ATTIT attit, a.PROFES profes, a.AVERAGE average, a.STU_MESSAGE stumessage, a.APPR_TIME apprtime, a.TEACHER_MESSAGE teachermessage, a.MESSAGE_TIME messagetime from appraise a, student s,composition c ,teacher t , com_composition cc where a.UDID=s.UDID and cc.STU_ID=s.ID and a.COMP_ID=c.ID and a.TEACHER_ID=t.ID and cc.COMP_ID=c.ID and c.ID="+id;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}
	//查询留言list
	public List<List<Map<String, String>>> selectMessage(int id) {
		String sql3 = "SELECT * FROM appraise_message WHERE COMP_ID = ? ORDER BY TIME";
		List<Map<String,Object>> alist2 = jt.queryForList(sql3, new Object[]{id});
		List<List<Map<String, String>>> list = CommUtils.arrangeAppraiseMessage(alist2);
		return list;
	}

	//其他作文
	public CurrentPage findZw(int currentPage, int numPerPage, String nj, String type, String name) {
		//范文
		String sql = "select ID id, NAME name ,CONTENT content ,AUTHOR author ,AGE_DETAIL ageDetail from t_res_composion where STATUS='004002' and CAUSE='008004' ";
		if(nj!=null&&!nj.equals("")){
			sql += " and AGE_DETAIL = '"+ nj +"'";
		}
		if(name!=null&&!name.equals("")){
			sql += " and NAME like '%"+ name +"%'";
		}
		if(type!=null){
			if(type.equals("2")){//技法
				sql = "select a.*,b.CODE_NAME ageDetail from (select ID id, NAME name ,CONTENT content ,AUTHOR author ,AGE_SCALE ageScale ,STATUS from t_res_composion_jf)a left join t_base_code b on a.ageScale=b.CODE where a.STATUS='004002' ";
				if(nj!=null&&!nj.equals("")){
					sql += " and b.CODE_NAME = '"+ nj +"'";
				}
				if(name!=null&&!name.equals("")){
					sql += " and a.name like '%"+ name +"%'";
				}
			}
			if(type.equals("3")){//素材
				String detail = "AGE_DETAIL";
				sql = "select a.*,b.CODE_NAME ageDetail from (select res.ID id, res.NAME name, res.CONTENT content, res.AUTHOR author, res.AGE_SCALE ageScale, res.STATUS from t_res_composion_sc res )a left join t_base_code b on a.ageScale=b.CODE where a.STATUS='004002' ";
				if(nj!=null&&!nj.equals("")){
					sql += " and b.CODE_NAME = '"+ nj +"'";
				}
				if(name!=null&&!name.equals("")){
					sql += " and a.name like '%"+ name +"%'";
				}
				
			}
		}
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}
	
	//其他作文	--	获取值到添加页面（范文）
	public List<Map<String, Object>> selectZuowen(int id) {
		String sql = "select NAME title, CONTENT content, AUTHOR author, AGE_DETAIL age_detail, TYPE type, STYLE style from t_res_composion where ID="+id;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}
	
	//其他作文	--	查看作文（范文）
	public List<Map<String, Object>> selectFanwen(int id) {
		String sql = "select r.ID id, r.NAME name ,r.CONTENT content ,r.AUTHOR author ,r.AGE_DETAIL ageDetail ,t.CODE_NAME type from t_res_composion r LEFT JOIN t_base_code t on r.TYPE=t.`CODE` where ID="+id;
		List<Map<String, Object>> forList = jt.queryForList(sql);
		return forList;
	}
	
	//其他作文	--	查看作文（技法）
	public List<Map<String, Object>> selectJifa(int id) {
		String sql = "select r.ID id, r.NAME name ,r.CONTENT content ,r.AUTHOR author ,r.AGE_DETAIL ageDetail ,t.CODE_NAME type from t_res_composion_jf r LEFT JOIN t_base_code t on r.TYPE=t.`CODE` where ID="+id;
		List<Map<String, Object>> forList = jt.queryForList(sql);
		return forList;
	}
	
	//其他作文	--	查看作文（素材）
	public List<Map<String, Object>> selectSucai(int id) {
		String sql = "select a.* , t1.CODE_NAME ageDetail from (select r.ID id, r.NAME name ,r.CONTENT content ,r.AUTHOR author ,r.AGE_SCALE ageScale ,t.CODE_NAME type from t_res_composion_sc r LEFT JOIN t_base_code t on r.C_TYPE=t.`CODE`)a LEFT JOIN t_base_code t1 on a.ageScale=t1.`CODE` where ID="+id;
		List<Map<String, Object>> forList = jt.queryForList(sql);
		return forList;
	}
	
	//其他作文	--	获取值到添加页面（技法）
	public List<Map<String, Object>> findJifa(int id) {
		String sql = "select r.ID id, r.NAME title, r.CONTENT content, r.AUTHOR author, t.CODE_NAME age_detail, r.TYPE type, r.STYLE style from t_res_composion_jf r LEFT JOIN t_base_code t on r.AGE_SCALE=t.`CODE` where ID="+id;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}
	
	//其他作文	--	获取值到添加页面（素材）
	public List<Map<String, Object>> findSucai(int id) {
		String sql = "select r.ID id, r.NAME title ,r.CONTENT content ,r.AUTHOR author ,t.CODE_NAME age_detail ,r.C_TYPE style from t_res_composion_sc r LEFT JOIN t_base_code t on r.AGE_SCALE=t.`CODE` where r.ID="+id;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}
	
	public List<Map<String, String>> findGenre(String dir) {
		List<Map<String, String>> stylelist =  new ArrayList<Map<String, String>>();
        try {
            File file = new File(dir);
            if (!file.exists()) {
                file.createNewFile();
            }
           String str= FileUtils.readFileToString(file, "UTF-8");
           JSONObject object = JSONObject.fromObject(str);
           String string = object.get("fanwen").toString();
           Object parse = JSON.parse(string);
           JSONArray jsonObject = JSONArray.fromObject(parse);
           Iterator it = jsonObject.iterator();
           while (it.hasNext()) {
        	   Map<String, String> map = new HashMap<String, String>();
                JSONObject ob = (JSONObject) it.next();
                map.put("CODE", ob.getString("CODE"));
                map.put("CODE_NAME", ob.getString("CODE_NAME"));
                stylelist.add(map);
            }
           String string1 = object.get("jifa").toString();
           Object parse1 = JSON.parse(string1);
           JSONArray jsonObject1 = JSONArray.fromObject(parse1);
           Iterator it1 = jsonObject1.iterator();
           while (it1.hasNext()) {
        	   Map<String, String> map = new HashMap<String, String>();
                JSONObject ob = (JSONObject) it1.next();
                map.put("CODE", ob.getString("CODE"));
                map.put("CODE_NAME", ob.getString("CODE_NAME"));
                stylelist.add(map);
            }
           String string2 = object.get("sucai").toString();
           Object parse2 = JSON.parse(string2);
           JSONArray jsonObject2 = JSONArray.fromObject(parse2);
           Iterator it2 = jsonObject2.iterator();
           while (it2.hasNext()) {
        	   Map<String, String> map = new HashMap<String, String>();
                JSONObject ob = (JSONObject) it2.next();
                map.put("CODE", ob.getString("CODE"));
                map.put("CODE_NAME", ob.getString("CODE_NAME"));
                stylelist.add(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return stylelist;
	}
	
	//查询年级等级014
	public String selectAgeScale(String ageScale) {
		String sql = "select CODE_NAME from t_base_code where CODE='"+ageScale+"'";
		List<Map<String,Object>> forList = jt.queryForList(sql);
		if(forList.size()>0){
			String string = forList.get(0).get("CODE_NAME").toString();
			return string;
		}
		return null;
	}
	
	//已点评作文	--	删除
	public int deleteYidp(int id) {
		String sql = "select cc.COMP_ID,cc.TEACHER_ID,cc.STU_ID,c.NEW_TITLE from com_composition cc,composition c where cc.COMP_ID = C.ID AND c.ID = "+id;
		Map<String, Object> forMap = jt.queryForMap(sql);
		if(forMap.size()>0){
			int comId = Integer.parseInt(forMap.get("COMP_ID").toString());
			String stuId = forMap.get("STU_ID").toString();
			String teaId = forMap.get("TEACHER_ID").toString();
			String title = forMap.get("NEW_TITLE").toString();
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String ctime = dateFormat.format(now);
			
			try {
				String sql1 = "UPDATE composition SET STATE = 2,TACHER_STATE=0 WHERE ID = ?";
				jt.update(sql1, new Object[]{comId});
				String sql2 = "DELETE from com_composition where COMP_ID = ?";
				jt.update(sql2, new Object[]{comId});
				String sql3 = "DELETE from appraise where COMP_ID = ?";
				jt.update(sql3, new Object[]{comId});
				String sql7 = "DELETE from appraise_message where COMP_ID = ?";
				jt.update(sql7, new Object[]{comId});
				String sql4 = "UPDATE s_order SET STATE = 2 WHERE COMP_ID = ?";
				jt.update(sql4, new Object[]{comId});
				String sql5 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,?,?)";
				jt.update(sql5, new Object[] { "系统通知", "您对作文《" + title + "》的点评内容已被管理员删除，详情请联系管理员！", ctime, teaId, 2, "2" }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
				String sql6 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,?,?)";
				jt.update(sql6, new Object[] { "系统通知", "您的作文《" + title + "》，点评内容已被管理员删除，恢复为待点评状态，请耐心等待点评结果！", ctime, stuId, 1, "2" }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
				return 0;
			} catch (Exception e) {
				return 1;
			}
		}
		return 1;
	}
	//待点评作文	--	删除
	public int deleteDaidp(String id, String adminId) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = dateFormat.format(now);
		String sql1 = "DELETE from composition where ID = ?";
		String sql2 = "update s_order set STATE=6, UPDATE_TIME='"+ctime+"' WHERE COMP_ID = ?";
		String sql3 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,?,?)";
		String sql6 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,?,?)";
		String sql4 = "SELECT c.*,s.ID sid FROM composition c,student s WHERE c.UDID = s.UDID AND c.ID ="+id;
		String sql5 = "SELECT SIGN,OUT_TRADE_NO,TRADE_NO,BUYER_PAY_AMOUNT,PAYMENT,TOTAL_AMOUNT,ID,APP_ID from s_order WHERE COMP_ID = ?";
		String sql7 = "insert into refund ( MONEY, CREATE_TIME, TEACHER_ID, COMP_TITLE, PAYMENT, ORDER_PRICE, STATE, STU_ID, COM_ID, DISPOSE_TIME, ACCOUNT_ID, OUT_REQUEST_NO, ORDER_ID)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sql8 = "INSERT INTO composition_del (OLD_TITLE,NEW_TITLE,DRAFT,PROPO,CREATE_TIME,UDID,GEADE,IMAGE1,IMAGE2,IMAGE3,CONTENT,TID,OLD_ID,DELETE_TIME) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sql9 = "UPDATE stu_dpcard_use SET USE_COUNT = USE_COUNT - 1,REMAINING_COUNT = REMAINING_COUNT + 1,STATE = 0 WHERE STU_ID = ?";
		//发送信息查询所需参数
		List<Map<String,Object>> list = jt.queryForList(sql4);
		Object title = null;
		Object sid = null;
		Object tid = null;
		if (list == null || list.size() == 0) {
			return 5;
		}
		if (list != null && list.size() > 0) {
			title = list.get(0).get("NEW_TITLE");
			sid = list.get(0).get("sid");
			tid = list.get(0).get("TID");
		}
		
		//退款查询所需参数
		String json = "";
		Object out_trade_no = null;// 评点订单 有
		Object trade_no = null;// 支付宝订单 有
		Object refund_amount = null;// 退款金额 有 对应用户付款金额
		Object refund_reason = null;// 退款原因
		Object out_request_no = null;// out_request_no 唯一退款标志
		String payment = "";
		Object total_amount = null;
		Object order_id = null;
		String appId = "";
		String sign = "";
		try {
			List<Map<String,Object>> list2 = jt.queryForList(sql5, new Object[]{id});
			if (list2 != null && list2.size() > 0) {
				out_trade_no = list2.get(0).get("OUT_TRADE_NO");
				trade_no = list2.get(0).get("TRADE_NO");
				refund_amount = list2.get(0).get("BUYER_PAY_AMOUNT");
				refund_reason = "系统退款";
				out_request_no = CommUtils.getOutTradeNo();
				payment = list2.get(0).get("PAYMENT").toString();
				total_amount = list2.get(0).get("TOTAL_AMOUNT");
				order_id = list2.get(0).get("ID");
				if (!"评点卡".equals(payment)) {
					appId = list2.get(0).get("APP_ID").toString();
				}
				if ("微信".equals(payment)) {
					sign = list2.get(0).get("SIGN").toString();
				}
			}
		} catch (Exception e) {
			logger.info("------------zuoWenDaoImpl获取退款信息失败---------------");
			return 2;
		}
		if ("支付宝".equals(payment)) {
			//先退款
			json = "{\"out_trade_no\":\""+out_trade_no+"\",\"trade_no\":\""+trade_no+"\",\"refund_amount\":\""+refund_amount+"\",\"refund_reason\":\""+refund_reason+"\",\"out_request_no\":\""+out_request_no+"\",\"terminal_id\":\""+adminId+"\"}";
			try {
				logger.info("------------zuoWenDaoImpl系统删除待点评作文支付宝---------------");
				String result = BackRefundUtils.zfbBackRefund(json, appId);
				if ("fail".equals(result)) {
					return 4;
				}
			} catch (Exception e) {
				logger.info("------------zuoWenDaoImpl支付宝退款失败---------------");
				return 3;
			}
			jt.update(sql3, new Object[] { "系统通知", "您的作文《" + title + "》，已被管理员删除，相应金额已退还到您的账号，详情请联系管理员！", ctime, sid, 1, "2" }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
		}else if ("微信".equals(payment)){
			Map<String, Object> wxMap = new HashMap<>();
			wxMap.put("sign", sign);
			wxMap.put("trade_no", trade_no);
			wxMap.put("total_fee", total_amount);
			wxMap.put("refund_fee", refund_amount);
			wxMap.put("out_request_no", out_request_no);
			wxMap.put("appId", appId);
			JSONArray js = JSONArray.fromObject(wxMap);
			json = js.toString();
			try {
				logger.info("------------zuoWenDaoImpl系统删除待点评作文微信---------------");
				String result = BackRefundUtils.wxBackRefund(json);
				if ("fail".equals(result)) {
					return 4;
				}
			} catch (Exception e) {
				logger.info("------------zuoWenDaoImpl微信退款失败---------------");
				return 3;
			}
			jt.update(sql3, new Object[] { "系统通知", "您的作文《" + title + "》，已被管理员删除，相应金额已退还到您的账号，详情请联系管理员！", ctime, sid, 1, "2" }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
		}else {
			jt.update(sql9, new Object[]{sid});
			jt.update(sql3, new Object[] { "系统通知", "您的作文《" + title + "》，已被管理员删除，评点卡使用次数已为您还原，详情请联系管理员！", ctime, sid, 1, "2" }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
		}
		
		//退款成功继续执行
		//将删除作文添加到删除作文表中
		jt.update(sql8, new Object[]{list.get(0).get("OLD_TITLE"), list.get(0).get("NEW_TITLE"), list.get(0).get("DRAFT"), list.get(0).get("PROPO"), list.get(0).get("CREATE_TIME"), list.get(0).get("UDID"), 
				list.get(0).get("GEADE"), list.get(0).get("IMAGE1"), list.get(0).get("IMAGE2"), list.get(0).get("IMAGE3"), list.get(0).get("CONTENT"), list.get(0).get("TID"), list.get(0).get("ID"), ctime});
				
		int i = jt.update(sql1, new Object[]{id});
		if (i > 0) {
			i = jt.update(sql2, new Object[]{id});
		}
		if (i > 0) {
			i = jt.update(sql7, new Object[]{refund_amount, ctime, tid, title, payment, total_amount, 2, sid, id, ctime, adminId, out_request_no, order_id});
		}
		if (i > 0) {
			jt.update(sql6, new Object[] { "系统通知", "《" + title + "》这篇作文的点评邀请已被管理员删除，无需对其进行点评，请您谅解！", ctime, tid, 2, "2" }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
		}
		return i;
	}
	// 评价删除
	public int deletePingJia(String id, String cid, String userType, String index) {
		int returnInt = 0;
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = dateFormat.format(now);
		String sql1 = "DELETE FROM appraise_message WHERE ID = ? OR FATHER_ID = ?";
		String sql2 = "update s_order set HAVE_COMMENT = false WHERE COMP_ID = ?";
		String sql3 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,?,?)";
		String sql4 = "SELECT c.NEW_TITLE title,t.ID tid,s.ID sid FROM composition c,student s,teacher t WHERE c.ID = ? AND c.TID = t.ID AND c.UDID = s.UDID";
		String sql5 = "SELECT ID,USER_TYPE FROM appraise_message WHERE COMP_ID = ? order by time";
		String sql6 = "DELETE FROM appraise where COMP_ID = ?";
		String sql7 = "UPDATE appraise SET TEACHER_MESSAGE = NULL, MESSAGE_TIME = NULL WHERE COMP_ID = ?";
		//List<Map<String,Object>> list2 = jt.queryForList(sql5, new Object[]{cid});
		
		//拆分index
		String[] sts = index.split("-");
		//查询删除的是否是集合第一个元素的第一个信息，也就是判断是否是首次留言，如果删除首次留言，则把appraise表中的数据也删除
		if (Objects.equals(sts[0], "0") && Objects.equals(sts[1], "0")) {
			//如果评论集合只有一个，说明学生只进行一次留言，appraise表中的数据删除，订单状态修改
			if (Integer.parseInt(sts[2]) == 1) {
				jt.update(sql6, new Object[]{cid});
				jt.update(sql2, new Object[]{cid});
				returnInt = 1;
			}
			//如果不是集合第一个元素，说明学生多次留言，订单表不变，把appraise维护
			else {
				String sql8 = "SELECT MESSAGE,ID,TIME FROM appraise_message WHERE COMP_ID = ? AND FATHER_ID IS NULL ORDER BY TIME LIMIT 1,1";
				String sql9 = "SELECT MESSAGE,TIME FROM appraise_message WHERE COMP_ID = ? AND USER_TYPE = 'tea' AND FATHER_ID = ? ORDER BY TIME LIMIT 0,1";
				String sql10 = "UPDATE appraise SET APPR_TIME = ?,STU_MESSAGE = ?,TEACHER_MESSAGE = ?,MESSAGE_TIME = ? WHERE COMP_ID = ?";
				List<Map<String,Object>> list1 = jt.queryForList(sql8, new Object[]{cid});
				Object smessage = null;
				Object tmessage = null;
				Object stime = null;
				Object ttime = null;
				if (list1 != null && list1.size() > 0) {
					smessage = list1.get(0).get("MESSAGE");
					stime = list1.get(0).get("TIME");
					List<Map<String,Object>> list3 = jt.queryForList(sql9, new Object[]{cid, list1.get(0).get("ID")});
					if (list3 != null && list3.size() > 0) {
						tmessage = list3.get(0).get("MESSAGE");
						ttime = list3.get(0).get("TIME");
					}
				}
				jt.update(sql10, new Object[]{stime,smessage,tmessage,ttime,cid});
			}
		}
		//如果删除的是集合第一个元素，但不是第一次留言，再判断是否是删除的是老师第一次回复，如果是则把apprise的老师回复置空
		else if (Objects.equals(sts[0], "0") && !Objects.equals(sts[1], "0")) {
			String sql11 = "SELECT ID FROM appraise_message WHERE COMP_ID = ? AND USER_TYPE = 'tea' ORDER BY TIME LIMIT 0,1";
			String str = jt.queryForObject(sql11, new Object[]{cid}, String.class);
			if (Objects.equals(str, id)) {
				jt.update(sql7, new Object[]{cid});
			}
		}
		
		//查询用户信息
		List<Map<String,Object>> list = jt.queryForList(sql4, new Object[]{cid});
		String sid = null;
		String title = null;
		String tid = null;
		if (list != null && list.size() > 0) {
			sid = list.get(0).get("sid").toString();
			title = list.get(0).get("title").toString();
			tid = list.get(0).get("tid").toString();
		}
		
		//再把appraise_message消息删除
		int count = jt.update(sql1, new Object[]{id, id});
		//如果删除的是多条，则给老师和学生都发送消息
		if (count > 1) {
			jt.update(sql3, new Object[] { "系统通知", "您的作文《" + title + "》的留言已被管理员删除，详情请联系管理员！", ctime, sid, 1, "2" });
			jt.update(sql3, new Object[] { "系统通知", "您点评的作文《" + title + "》的留言已被管理员删除，详情请联系管理员！", ctime, tid, 2, "2" });
		}else {
			//如果只是删除一条数据，再判断是删除的老师还是学生的留言，发送相应的消息
			if (Objects.equals(userType, "tea")) {
				jt.update(sql3, new Object[] { "系统通知", "您点评的作文《" + title + "》的留言已被管理员删除，详情请联系管理员！", ctime, tid, 2, "2" });
			}else {
				jt.update(sql3, new Object[] { "系统通知", "您的作文《" + title + "》的留言已被管理员删除，详情请联系管理员！", ctime, sid, 1, "2" });
			}
		}
		return returnInt;
	}
	
	//老师用户集合
	public CurrentPage findTeacherlist(int currentPage, int numPerPage, String name) {
		String sql = "select t.ID id,t.PHONE userName,t.NAME name,t.SEX sex,t.SCHOOL school,t.GRADE grade,t.CITY city,t.EDU_TIME eduTime,t.EXPER exper,t.STATE state,t.CREATE_TIME createTime,t.AUSTATE austste from teacher t , expected_com e where e.TEACHER_ID=t.ID and e.STATE=1 and (t.AUSTATE='1' or t.AUSTATE=3) ";
		if(name!=null&&name!=""){
			sql += " and (t.PHONE like '%"+ name +"%' or t.NAME like '%"+ name +"%')";
		}
		sql += " ORDER BY t.CREATE_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}
	//修改点评老师
	public int updateTeacher(String comid, String newTid) {
		String start = "SELECT TACHER_STATE FROM composition WHERE ID = ?";
		Integer integer = jt.queryForObject(start, new Object[]{comid}, Integer.class);
		if (integer == 1) {
			return -1;
		}
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = dateFormat.format(now);
		
		String sql = "SELECT s.NICKNAME sname,s.ID sid,c.NEW_TITLE title,t.ID tid FROM student s,composition c,teacher t WHERE c.UDID = s.UDID AND t.ID = c.TID AND c.ID = ?";
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{comid});
		Object sname = list.get(0).get("sname");
		Object sid = list.get(0).get("sid");
		Object oldTid = list.get(0).get("tid");
		Object title = list.get(0).get("title");
		
		String sq6 = "SELECT NAME FROM teacher WHERE ID = ?";
		String newTname = jt.queryForObject(sq6, new Object[]{newTid}, String.class);
		
		String sql1 = "update composition set TID="+newTid+" where ID="+comid;
		String sql2 = "update s_order set TEACHER_ID="+newTid+" where COMP_ID="+comid;
		int i = jt.update(sql1);
		int i1 =0;
		if(i>0){
			i1 = jt.update(sql2);
		}
		
		String content1 = "您的这篇《"+title+"》，点评老师已成功更换为"+newTname+"，请耐心等待点评结果。";
		String content2 = sname+"同学已经把《"+title+"》这篇作文给了另外一个老师批改，请等待下次批改机会。";
		String content3 = "您已收到" + sname + "同学的一篇作文，请在48小时内为其点评";
		String sql3 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,2,11)";
		String sql4 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,1,11)";
		String sql5 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,2,7)";
		jt.update(sql3, new Object[] { "点评老师已更换", content2, ctime, oldTid});
		jt.update(sql4, new Object[] { "点评老师已更换", content1, ctime, sid});
		jt.update(sql5, new Object[] { "被邀请点评", content3, ctime, newTid});
		return i1;
	}
	
	//指派点评老师
	public int assignTeacher(String comid, String tid) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = dateFormat.format(now);
		
		String sql = "SELECT s.NICKNAME sname,s.ID sid,c.NEW_TITLE title FROM student s,composition c WHERE c.UDID = s.UDID AND c.ID = ?";
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{comid});
		Object sname = list.get(0).get("sname");
		Object sid = list.get(0).get("sid");
		Object title = list.get(0).get("title");
		
		String sq6 = "SELECT NAME FROM teacher WHERE ID = ?";
		String newTname = jt.queryForObject(sq6, new Object[]{tid}, String.class);
		
		String sql1 = "update composition set TID="+tid+" where (TID is null or TID='') and ID="+comid;
		String sql2 = "update s_order set TEACHER_ID="+tid+" where (TEACHER_ID is null or TEACHER_ID='') and COMP_ID="+comid;
		int i = jt.update(sql1);
		int i1 =0;
		if(i>0){
			i1 = jt.update(sql2);
		}
		if(i1>0){
			String content1 = "您的这篇《"+title+"》，已成功指派给"+newTname+"老师点评，请耐心等待点评结果。";
			String content3 = "您已收到" + sname + "同学的一篇作文，请在48小时内为其点评";
			String sql4 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,1,11)";
			String sql5 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,2,7)";
			jt.update(sql4, new Object[] { "点评老师已指派", content1, ctime, sid});
			jt.update(sql5, new Object[] { "被邀请点评", content3, ctime, tid});
		}
		return i1;
	}
	public List<Map<String, Object>> recommendYidp(int id) {
		String sql = "select c.ID id, c.NEW_TITLE title, s.NICKNAME author, c.GEADE geade, c.CREATE_TIME createTime, cc.CONTENT content, c.IMAGE1 img1, c.IMAGE2 img2, c.IMAGE3 img3, t.NAME name, cc.COM_TIME comTime, cc.VOICE voice from s_order o , composition c , com_composition cc , teacher t , student s where o.COMP_ID=c.ID and cc.TEACHER_ID=t.ID and o.STATE=3 and o.STU_ID=s.ID and cc.COMP_ID=c.ID and c.ID="+id;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}
	//添加推荐作文（已点评）		提交
	public int insertTuijian2(int id, String title, String content, String author, String nj, String style) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		int i = 0;
		String str =style.substring(0, 3);
		if(str.equals("033")){
			String sql = "insert into recommend_com (COM_ID, NAME, CONTENT, AUTHOR, AGE_DETAIL, CREATE_TIME, PUBLISH_TIME, TYPE, DISPLAY) values (?,?,?,?,?,?,?,?,1)";
			i = jt.update(sql, new Object[] {id,title,content,author,nj,time,time,style},
					new int[] {Types.INTEGER,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.TIMESTAMP,Types.VARCHAR});
		}else {
			String sql = "insert into recommend_com (COM_ID, NAME, CONTENT, AUTHOR, AGE_DETAIL, CREATE_TIME, PUBLISH_TIME, STYLE, DISPLAY) values (?,?,?,?,?,?,?,?,1)";
			i = jt.update(sql, new Object[] {id,title,content,author,nj,time,time,style},
					new int[] {Types.INTEGER,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.TIMESTAMP,Types.VARCHAR});
		}
		
		return i;
	}
	//查看推荐作文-已点评作文
	public List<Map<String, Object>> selectTuijianYdp(int id) {
		String sql = "select cc.ID id, c.NEW_TITLE newTitle, c.PROPO propo,cc.SCORE score, s.NICKNAME author, c.DRAFT draft, c.GEADE geade, c.CREATE_TIME createTime, r.CONTENT content, c.IMAGE1 img1, c.IMAGE2 img2, c.IMAGE3 img3, t.NAME name, cc.COM_TIME comTime, cc.VOICE voice ,cc.DP_CONTENT dpcontent ,cc.DP_LANGUAGE dplanguage ,cc.DP_STRUCTURE dpstructure ,cc.DP_WRITING dpwriting ,cc.SCORING scoring,cc.POINTS points,cc.SUGGEST suggest from s_order o , composition c , com_composition cc , teacher t , student s , recommend_com r where o.COMP_ID=c.ID and cc.TEACHER_ID=t.ID and o.STATE=3 and o.STU_ID=s.ID and cc.COMP_ID=c.ID and r.COM_ID=c.ID and c.ID="+id;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}
	
	//查询待点评作文订单
	public List<Map<String, Object>> selectSorder(String id) {
		String sql = "select * from s_order where COMP_ID="+id;
		List<Map<String,Object>> list = jt.queryForList(sql);
		return list;
	}
	
	//悦作业上传的作文管理端删除生成退款信息
	public void insetRefund(String id, String adminId) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = dateFormat.format(now);
		String sql = "SELECT s.SIGN sign,s.OUT_TRADE_NO out_trade_no,s.TRADE_NO trade_no,s.BUYER_PAY_AMOUNT refund_amount,s.PAYMENT payment,s.TOTAL_AMOUNT total_amount,s.ID order_id,s.APP_ID appId,c.NEW_TITLE title, c.TID tid,stu.ID sid from s_order s, composition c, student stu WHERE s.COMP_ID=c.ID and c.UDID=stu.UDID and s.COMP_ID = ?";
		Object title = null;
		Object sid = null;
		Object tid = null;
		Object refund_amount = null;// 退款金额 有 对应用户付款金额
		Object out_request_no = null;// out_request_no 唯一退款标志
		Object payment = null;
		Object total_amount = null;
		Object order_id = null;
		List<Map<String,Object>> list = jt.queryForList(sql, new Object[]{id});
		if (list != null && list.size() > 0) {
			title = list.get(0).get("title");
			sid = list.get(0).get("sid");
			tid = list.get(0).get("tid");
			refund_amount = list.get(0).get("refund_amount");
			out_request_no = list.get(0).get("out_trade_no"); 
			payment = list.get(0).get("payment");
			total_amount = list.get(0).get("total_amount");
			order_id = list.get(0).get("order_id");
		}
		
		String sql2 = "insert into refund ( MONEY, CREATE_TIME, TEACHER_ID, COMP_TITLE, PAYMENT, ORDER_PRICE, STATE, STU_ID, COM_ID, DISPOSE_TIME, ACCOUNT_ID, OUT_REQUEST_NO, ORDER_ID)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int i = jt.update(sql2, new Object[]{refund_amount, ctime, tid, title, payment, total_amount, 2, sid, id, ctime, adminId, out_request_no, order_id});
		String sql3 = "update s_order set BACK_TIME=?, UPDATE_TIME=? WHERE COMP_ID = ?";
		int a = jt.update(sql3, new Object[]{ ctime, ctime, id});
		
	}
}
