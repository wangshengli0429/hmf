package com.pc.manage.dao;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.rest.service.dao.impl.AppraiseDaoImpl;
import com.util.CommUtils;
import com.util.CurrentPage;
import com.util.SmsUtils2;
import com.util.XingeUtils;
/**
 * 用户管理
 */
public class ManageUsersDaoImpl {
	private static Logger logger = Logger.getLogger(ManageUsersDaoImpl.class);
	private JdbcTemplate jt = CommUtils.getJdbcTemplate();
	
	//（年级等级，公共代码CODE_TYPE=014） - ageScale
	public List<Map<String, Object>> findAgescale() {
		String sql = "select * from t_base_code where CODE_TYPE='014'";
		List<Map<String, Object>> forList = jt.queryForList(sql);
		return forList;
	}
	
	//（（年级,年级表grade） - ageDetail
	public List<Map<String, Object>> findAgedetail() {
		String sql = "select * from grade";
		List<Map<String, Object>> forList = jt.queryForList(sql);
		return forList;
	}
	
	//（地区） - area
	public List<Map<String, Object>> findArea(int id) {
		String sql = "select f_id id,f_name name,f_parent pid from area where f_parent="+id;
		List<Map<String, Object>> forList = jt.queryForList(sql);
		return forList;
	}
	
	//（地区） - 市
	public int findShilist(String sheng) {
		String sql = "select f_id id from area where f_name='"+sheng+"'";
		List<Map<String, Object>> forList = jt.queryForList(sql);
		int pid = 0;
		if(forList.size()>0){
			pid = Integer.parseInt(forList.get(0).get("id").toString());
		}
		return pid;
	}
	
	
	//老师用户集合   - 条件+分页
	public CurrentPage findTeacherlist(int currentPage, int numPerPage, String nj, String state, String sheng,
			String shi,String xian,String userState, String name) {
		String sql = "select t.AVG_TIME,t.ID id,t.PHONE userName,t.NAME name,t.SEX sex,t.SCHOOL school,t.GRADE grade,t.CITY city,t.EDU_TIME eduTime,t.EXPER exper,t.STATE state,t.CREATE_TIME createTime,t.AUSTATE austste from teacher t , expected_com e where e.TEACHER_ID=t.ID and e.STATE=1 and (t.AUSTATE='1' or t.AUSTATE=3) ";
		if(nj!=null&&nj!=""){
			sql += " and t.GRADE = '"+ nj +"'";
		}
		if(sheng!=null&&sheng!=""&&!sheng.equals("请选择")){
			sql += " and t.CITY like '%"+ sheng +"%'";
		}
		if(shi!=null&&shi!=""&&!shi.equals("请选择")&&!shi.equals("市辖区")&&!shi.equals("县")){
			sql += " and t.CITY like '%"+ shi +"%'";
		}
		if(xian!=null&&xian!=""&&!xian.equals("请选择")){
			sql += " and t.CITY like '%"+ xian +"%'";
		}
		if(state!=null&&state!=""){
			sql += " and t.STATE = '"+ state +"'";
		}
		if(userState!=null&&userState!=""){
			sql += " and t.AUSTATE = '"+ userState +"'";
		}
		if(name!=null&&name!=""){
			sql += " and (t.PHONE like '%"+ name +"%' or t.NAME like '%"+ name +"%')";
		}
		sql += " ORDER BY t.CREATE_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//修改老师	-回显
	public List<Map<String, Object>> findTeacher(int id) {
		String sql = "select ID id,NAME name,SEX sex,SCHOOL school,GRADE grade,CITY city,EDU_TIME eduTime,EXPER exper,STATE state,ACV acv,HONOR honor,FEATURES features from teacher where ID="+id;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}
	
	//修改老师	-提交
	public int updateTeacher(int id, String name, String sex, String school, String nj, String sheng, String shi,
			String xian, String eduTime, String exper, String state, String acv, String honor, String features) {
		String sql = "update teacher set NAME='"+name+"', SEX='"+sex+"', SCHOOL='"+school+"', GRADE='"+nj+"', "
				+ "CITY='"+sheng+shi+xian+"', EDU_TIME='"+eduTime+"', EXPER='"+exper+"', "
				+ "STATE='"+state+"', ACV='"+acv+"', HONOR='"+honor+"', FEATURES='"+features+"'  "
				+ "where ID="+id;
		int i = jt.update(sql);
		return i;
	}
	
	//修改用户状态
	public int updateAustste(int id, String austste) {
		String sql = "";
		if(austste.equals("1")){
			sql = "update teacher set AUSTATE='3' "
					+ "where ID="+id;
			String sql2 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,?,?)";
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String ctime = dateFormat.format(now);
			jt.update(sql2, new Object[] { "系统通知", "您的账号已被停用，请您联系管理员了解详情！", ctime, id, 2, 2 }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.VARCHAR });
		}
		if(austste.equals("3")){
			sql = "update teacher set AUSTATE='1' "
					+ "where ID="+id;
		}
		int i = jt.update(sql);
		return i;
	}

	//师资认证	-待审核
	public CurrentPage findDaish(int currentPage, int numPerPage, String name) {
		//String sql = "select t.ID id, t.PHONE userName, t.NAME name, t.CREATE_TIME createTime, t.UPLOAD_TIME uploadTime, t.PERFECT_TIME perfectTime from teacher t left join expected_com e on e.TEACHER_ID=t.ID where (t.AUSTATE = 0 or t.AUSTATE = 4 or e.STATE = 0) ";
		//String sql = "select b.* from (select a.* from (select t.ID id, t.PHONE userName, t.NAME name, t.CREATE_TIME createTime, t.PERFECT_TIME perfectTime, t.UPLOAD_TIME uploadTime,e.COM_TIME comTime,t.AUSTATE austate,e.STATE estate from teacher t left join expected_com e on e.TEACHER_ID=t.ID where (t.AUSTATE != e.STATE or e.STATE is null) and t.UPLOAD_TIME is not null and t.PERFECT_TIME is not null ORDER BY t.UPLOAD_TIME DESC)a UNION ALL select a.* from (select t.ID id, t.PHONE userName, t.NAME name, t.CREATE_TIME createTime, t.PERFECT_TIME perfectTime, t.UPLOAD_TIME uploadTime,e.COM_TIME comTime,t.AUSTATE austate,e.STATE estate from teacher t left join expected_com e on e.TEACHER_ID=t.ID where (t.AUSTATE != e.STATE or e.STATE is null) and (t.UPLOAD_TIME is null or t.PERFECT_TIME is null) ORDER BY t.CREATE_TIME DESC)a)b where 1=1";
		String sql = "select b.* from (select a.* from (select t.ID id, t.PHONE userName, t.NAME name, t.CREATE_TIME createTime, t.PERFECT_TIME perfectTime, t.UPLOAD_TIME uploadTime,e.COM_TIME comTime,t.AUSTATE austate,e.STATE estate from teacher t left join expected_com e on e.TEACHER_ID=t.ID where (t.AUSTATE = 0 and e.STATE =0) and t.UPLOAD_TIME is not null ORDER BY t.UPLOAD_TIME DESC)a UNION ALL select a.* from (select t.ID id, t.PHONE userName, t.NAME name, t.CREATE_TIME createTime, t.PERFECT_TIME perfectTime, t.UPLOAD_TIME uploadTime,e.COM_TIME comTime,t.AUSTATE austate,e.STATE estate from teacher t left join expected_com e on e.TEACHER_ID=t.ID where (t.AUSTATE != e.STATE or e.STATE is null) and t.UPLOAD_TIME is not null and t.PERFECT_TIME is not null ORDER BY t.UPLOAD_TIME DESC)a UNION ALL select a.* from (select t.ID id, t.PHONE userName, t.NAME name, t.CREATE_TIME createTime, t.PERFECT_TIME perfectTime, t.UPLOAD_TIME uploadTime,e.COM_TIME comTime,t.AUSTATE austate,e.STATE estate from teacher t left join expected_com e on e.TEACHER_ID=t.ID where (t.AUSTATE != e.STATE or e.STATE is null) and (t.UPLOAD_TIME is null or t.PERFECT_TIME is null) ORDER BY t.CREATE_TIME DESC)a)b where austate != 3 ";
		if(name!=null&&name!=""){
			sql += " and (b.userName like '%"+ name +"%' or b.name like '%"+ name +"%')";
			//sql += " and (t.PHONE like '%"+ name +"%' or t.NAME like '%"+ name +"%')";
		}
		//sql += " ORDER BY t.AUSTATE ,t.CREATE_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		/*List<Map<String,Object>> list = page.getResultList();
		for(int i=0;i<list.size();i++){
			int id = Integer.parseInt(list.get(i).get("id").toString());
			String sql1 = "select COM_TIME from expected_com where TEACHER_ID ="+id;
			jt.queryForList(sql1);
			List<Map<String,Object>> forList = jt.queryForList(sql1);
			String comTime = null;
			if(forList.size()>0){
				if(forList.get(0).get("COM_TIME")!=null){
					comTime = forList.get(0).get("COM_TIME").toString();
				}
			}
			list.get(i).put("comTime", comTime);
		}
		page.setResultList(list);*/
		return page;
	}

	//师资认证	-待审核-审核
	public List<Map<String, Object>> findShenhe(int id) {
		String sql = "select t.ID id,t.PHONE userName,t.NAME name,t.SEX sex,t.SCHOOL school,t.GRADE grade,t.CITY city,t.EDU_TIME eduTime,t.EXPER exper, t.ACV acv,t.HONOR honor,t.FEATURES features,t.CERT_URL certUrl, t.CARD1 card1,t.CARD2 card2,t.AUSTATE austste from teacher t where t.ID="+id;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}
	//师资认证	-待审核-审核-查看预点评作文
	public List<Map<String, Object>> findYdp(int id) {
		String sql = "select c.NEW_TITLE newTitle ,c.GEADE geade ,c.AUTHOR author ,e.CONTENT content , e.SCORE score ,e.SCORING scoring ,e.POINTS points ,e.SUGGEST suggest ,e.DP_CONTENT dpcontent, e.DP_CONTENT_CA categoryCa ,e.DP_LANGUAGE dplanguage , e.DP_LANGUAGE_CA languageCa ,e.DP_STRUCTURE dpstructure ,e.DP_STRUCTURE_CA structureCa ,e.DP_WRITING dpwriting ,e.DP_WRITING_CA writingCa ,e.STATE state from expected_com e left join pre_composition c on e.COMP_ID=c.ID where TEACHER_ID="+id;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}

	//师资认证	-待审核-提交审核
	public void updateState(int id, String ziLiao, String cause, String title, String zuoWen, String reason) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		if(cause==null||cause==""){
			cause = "";
		}
		if(ziLiao!=null&&ziLiao!=""){
			if(zuoWen!=null&&zuoWen!=""){
				if(ziLiao.equals("2")&&zuoWen.equals("2")){
					cause = cause+"-"+reason;
				}
			}
		}
		if(ziLiao!=null&&ziLiao!=""){
			String sql1 = "select * from teacher where AUSTATE='"+ziLiao+"' and ID="+id;
			List<Map<String,Object>> forList = jt.queryForList(sql1);
			if(forList.size()==0){
				if(ziLiao.equals("1")&&(title==null||title=="")){
					String information = "insert into information set INF_CONTENT='您提交的资质审核已通过，还未试点评，请在电脑端登录进行试点评！',CREATE_TIME='"+time+"',TEACHER_ID="+id+",STATE=2,ICON=3,TITLE='资质审核通过'";
					jt.update(information);
				}
				String sql = "update teacher set AUSTATE = '"+ziLiao+"',CAUSE = '"+cause+"' where ID="+id;
				jt.update(sql);
				if(ziLiao.equals("2")){//不通过
					String information = "insert into information set INF_CONTENT='您提交的资质审核未通过，理由："+cause+"，请重新上传！',CREATE_TIME='"+time+"',TEACHER_ID="+id+",STATE=2,ICON=4,TITLE='资质审核未通过'";
					jt.update(information);
				}
			}
		}
		if(zuoWen!=null&&zuoWen!=""){
			int a = Integer.parseInt(zuoWen);
			//String sql1 = "select * from expected_com where STATE="+a+" and TEACHER_ID="+id;
			String sql1 = "select * from expected_com where TEACHER_ID="+id;
			List<Map<String,Object>> forList = jt.queryForList(sql1);
			if(forList.size()==0){
				String sql2 = "insert into expected_com set STATE = "+a+", TEACHER_ID="+id+", COM_TIME='"+time+"'";
				jt.update(sql2);
			}else if(forList.size()>0){
				String sql2 = "update expected_com set STATE = "+a+" where TEACHER_ID="+id;
				jt.update(sql2);
				if(zuoWen.equals("2")){//不通过
					String information = "insert into information set INF_CONTENT='您的预点评作文审核不通过，理由：" + reason + "，请再次到电脑端点评作文，并提交审核。',CREATE_TIME='"+time+"',TEACHER_ID="+id+",STATE=2,ICON=4,TITLE='资质审核未通过'";
					jt.update(information);
				}
			}
		}
		if((ziLiao!=null&&ziLiao!="")&&(zuoWen!=null&&zuoWen!="")){
			if(ziLiao.equals("1")&&zuoWen.equals("1")){//通过
				String information = "insert into information set INF_CONTENT='您提交的个人资料和预点评作文审核通过，恭喜您可以为学生作文做点评服务了。',CREATE_TIME='"+time+"',TEACHER_ID="+id+",STATE=2,ICON=3,TITLE='资质审核通过 '";
				jt.update(information);
			}
		}
	}

	//师资认证	-已审核
	public CurrentPage findYish(int currentPage, int numPerPage, String name, String austate) {
		String sql = "select t.ID id, t.PHONE userName, t.NAME name, t.CREATE_TIME createTime, t.UPLOAD_TIME uploadTime, e.COM_TIME comTime, t.PERFECT_TIME perfectTime ,e.STATE state, t.AUSTATE austate from teacher t left join expected_com e on e.TEACHER_ID=t.ID where e.STATE != 0 and t.AUSTATE != '0' and t.AUSTATE != '4' ";
		if(name!=null&&name!=""){
			sql += " and (t.PHONE like '%"+ name +"%' or t.NAME like '%"+ name +"%')";
		}
		if (austate!=null&&!austate.equals("")) {
			if(austate.equals("1")){//已通过
				sql += " and e.STATE=1 and t.AUSTATE='1' ";
			}else if(austate.equals("2")){//未通过
				sql += " and (e.STATE=2 or t.AUSTATE='2') ";
			}else if(austate.equals("3")){//已禁用
				sql += " and t.AUSTATE='3' ";
			}
		}
		sql += "  ORDER BY t.CREATE_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	/**
	 * 学生用户集合   - 条件+分页
	 */
	public CurrentPage findStudentlist(int currentPage, int numPerPage, String nj, String sheng, String shi,
			String xian, String name) {
		String sql = "select s.ID id,s.NICKNAME userName,s.NAME name,s.PHONE phone,s.SEX sex,s.SCHOOL school,s.GRADE grade,s.AREA city,s.CREATE_TIME createTime from student s where 1=1 ";
		if(nj!=null&&nj!=""){
			sql += " and s.GRADE = '"+ nj +"'";
		}
		if(sheng!=null&&sheng!=""&&!sheng.equals("请选择")){
			sql += " and s.AREA like '%"+ sheng +"%'";
		}
		if(shi!=null&&shi!=""&&!shi.equals("请选择")&&!shi.equals("市辖区")&&!shi.equals("县")){
			sql += " and s.AREA like '%"+ shi +"%'";
		}
		if(xian!=null&&xian!=""&&!xian.equals("请选择")){
			sql += " and s.AREA like '%"+ xian +"%'";
		}
		if(name!=null&&name!=""){
			sql += " and (s.PHONE like '%"+ name +"%' or s.NAME like '%"+ name +"%' or s.NICKNAME like '%"+ name +"%')";
		}
		sql += " ORDER BY s.CREATE_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//修改学生	-回显
	public List<Map<String, Object>> findStudent(int id) {
		String sql = "select s.ID id,s.NICKNAME userName,s.NAME name,s.PHONE phone,s.SEX sex,s.SCHOOL school,s.GRADE grade,s.AREA city,s.CREATE_TIME createTime from student s where s.ID="+id;
		List<Map<String,Object>> forList = jt.queryForList(sql);
		return forList;
	}

	//修改学生	-提交
	public int updateStudent(int id, String userName, String name, String sex, String school, String nj, String sheng,
			String shi, String xian) {
		String sql = "update student set NICKNAME='"+userName+"', NAME='"+name+"', SEX='"+sex+"', SCHOOL='"+school+"', GRADE='"+nj+"', "
				+ "AREA='"+sheng+shi+xian+"' where ID="+id;
		int i = jt.update(sql);
		return i;
	}

	/**  
	* @Title: findUserList  
	* @Description: 查找用户
	* @param @param userType
	* @param @param name
	* @param @param currentPage
	* @param @param numPerPage    设定文件  
	* @return void    返回类型  
	* @throws  
	*/ 
	public CurrentPage findUserList(String userType, String name, int currentPage,	int numPerPage) {
		String sql = "";
		if (userType.equals("student")) {
			sql = "SELECT ID,UDID,NICKNAME,PHONE,CREATE_TIME,SCHOOL from student where 1 = 1 ";
			if (name != null && !name.equals("")) {
				sql += " and (NICKNAME like '%"+ name +"%' or PHONE like '%"+ name +"%')";
			}
		}else {
			sql = "SELECT ID,LASTLOGINDEVICETYPE,UDID,NAME,PHONE,CREATE_TIME,SCHOOL from teacher where 1 = 1 ";
			if (name != null && !name.equals("")) {
				sql += " and (NAME like '%"+ name +"%' or PHONE like '%"+ name +"%')";
			}
		}
		sql += " ORDER BY CREATE_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	/**  
	* @Title: sendInformationApp  
	* @Description: app推送消息  
	* @param @param ids
	* @param @param information    设定文件  
	* @return void    返回类型  
	* @throws  
	*/ 
	public String sendInformationApp(String ids, String information, String userType) {
		List<String> oisIdList = new ArrayList<>();
		List<String> androidIdList = new ArrayList<>();
		JSONObject jo1 = null;
		JSONObject jo2 = null;
		int count = 0;
		if (userType.equals("teacher")) {
			String[] sts = ids.split(",");
			for (String string : sts) {
				if (!string.equals("on")) {
					String[] sts2 = string.split("%");
					if (sts2.length == 3) {
						if (sts2[2].equals("IOS")) {
							oisIdList.add(sts2[1]);
						}
						if (sts2[2].equals("ANDROID")) {
							androidIdList.add(sts2[1]);
						}
					}else {
						count += 1;
					}
				}
			}
		}
		try {
			if (androidIdList.size() > 0) {
				jo1 = XingeUtils.pushAccountAndroidList(androidIdList, information);
				String str = CommUtils.getTuiSongResult(androidIdList, jo1);
				logger.info("------安卓群推送------" + jo1);
				logger.info("------安卓群推送结果------" + str);
			}
			if (oisIdList.size() > 0) {
				jo2 = XingeUtils.pushAccountIosList(oisIdList, information);
				String str = CommUtils.getTuiSongResult(oisIdList, jo2);
				logger.info("------IOS群推送------" + jo2);
				logger.info("------安卓群推送结果------" + str);
			}
		} catch (Exception e) {
			logger.info("------群推送失败------");
		}
		StringBuffer sb = new StringBuffer();
		String result = CommUtils.handleTuiSongJson(jo1, jo2, androidIdList.size(), oisIdList.size());
		sb.append("推送总数" + (androidIdList.size() + oisIdList.size() + count) + "个");
		sb.append(result);
		if (count != 0) {
			sb.append("，没有平台信息" + count + "个！");
		}else {
			sb.append("！");
		}
		return sb.toString();
	}

	/**  
	* @Title: sendInformationXiaoxiliebiao  
	* @Description: TODO(这里用一句话描述这个方法的作用)  
	* @param @param ids
	* @param @param information    设定文件  
	* @return void    返回类型  
	* @throws  
	*/ 
	public String sendInformationXiaoxiliebiao(String ids, String information, String userType) {
		List<String> idList = new ArrayList<>();
		int scount = 0;
		int fcount = 0;
		String[] sts1 = ids.split(",");
		for (String string : sts1) {
			if (!string.equals("on")) {
				String[] sts2 = string.split("%");
				idList.add(sts2[0]);
			}
		}
		try {
			String sql2 = "";
			if (userType.equals("teacher")) {
				sql2 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,TEACHER_ID,STATE,ICON)values(?,?,?,?,2,2)";
			}else {
				sql2 = "insert into information(TITLE,INF_CONTENT,CREATE_TIME,STU_ID,STATE,ICON)values(?,?,?,?,1,2)";
			}
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String ctime = dateFormat.format(now);
			int i = 0;
			for (String string : idList) {
				i = jt.update(sql2, new Object[] { "系统通知", information, ctime, string }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER });
				if (i == 1) {
					scount += 1;
				}
				if (i <= 0) {
					fcount += 1;
				}
			}
		} catch (Exception e) {
			logger.info("--------群发送信息失败-----------");
		}
		return "发送成功"+scount+"个，失败"+fcount+"个";
	}


	//删除学生
	public void deleteStudent(int id) {
		String sql = "delete from student where ID="+id;
		jt.update(sql);
	}
	
	public CurrentPage findTeacherDsh(int currentPage, int numPerPage, String userState) {
		String sql = "select b.* from (select a.* from (select t.ID id, t.PHONE userName, t.NAME name, t.CREATE_TIME createTime, t.UPLOAD_TIME uploadTime, t.PERFECT_TIME perfectTime,e.COM_TIME comTime from teacher t left join expected_com e on e.TEACHER_ID=t.ID where (t.AUSTATE = 0 or t.AUSTATE = 4 or e.STATE = 0) and t.UPLOAD_TIME is not null and t.PERFECT_TIME is not null and e.COM_TIME is not null )a UNION ALL select a.* from (select t.ID id, t.PHONE userName, t.NAME name, t.CREATE_TIME createTime, t.UPLOAD_TIME uploadTime, t.PERFECT_TIME perfectTime,e.COM_TIME comTime from teacher t left join expected_com e on e.TEACHER_ID=t.ID where (t.AUSTATE = 0 or t.AUSTATE = 4 or e.STATE = 0) and (t.UPLOAD_TIME is null or t.PERFECT_TIME is null or e.COM_TIME is null) )a)b where 1=1";
		if(userState!=null&&userState!=""&&userState.equals("1")){
			sql += " and (b.uploadTime is null or b.perfectTime is null)";
		}else if(userState!=null&&userState!=""&&userState.equals("2")){
			sql += " and b.comTime is null ";
		}
		sql +=" ORDER BY b.createTime DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}
	
	public CurrentPage findStudent(int currentPage, int numPerPage, String grade) {
		String sql = "select s.ID id,s.NICKNAME userName,s.NAME name,s.PHONE phone,s.SEX sex,s.SCHOOL school,s.GRADE grade,s.AREA city,s.CREATE_TIME createTime from student s where 1=1 ";
		if(grade!=null&&grade!=""){
			sql += " and s.GRADE = '"+ grade +"'";
		}
		sql += " ORDER BY s.CREATE_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}
	
	public String sendInformationApp2(String ids, String information) {
		String result = "";
		List<String> teachers = new ArrayList<String>();
		String[] sts = ids.split(",");
		for (String string : sts) {
			teachers.add(string);
		}
		if(information!=null&&information!=""){
			if(information.equals("message1")){
				result = SmsUtils2.sendMultiMessage1(teachers);
			}else if(information.equals("message2")){
				result = SmsUtils2.sendMultiMessage2(teachers);
			}
		}
		return result;
	}

}
