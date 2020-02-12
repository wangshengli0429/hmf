package com.pc.manage.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.util.CommUtils;
import com.util.CurrentPage;

public class ManageLunboDaoImpl {
	private JdbcTemplate jt = CommUtils.getJdbcTemplate();
	
	//轮播图集合   - 条件+分页
	public CurrentPage findLunbo(int currentPage, int numPerPage, String state, String name) {
		String sql = "select ID id, NAME name, POSITION position, USER_NO userNo, STATE state, CREATE_TIME createTime from slideshow where STATE!=4 ";
		if(state!=null&&state!=""){
			int stateint = Integer.parseInt(state);
			sql += " and STATE = "+ stateint;
		}
		if(name!=null&&name!=""){
			sql += " and NAME like '%"+ name +"%'";
		}
		sql += " ORDER BY STATE,CREATE_TIME DESC ";
		CurrentPage page = new CurrentPage();
		page.Page(sql, currentPage, numPerPage, jt);
		return page;
	}

	//轮播	删除待发布
	public int deleteDfb(int id) {
		//String sql = "delete from slideshow where ID = "+id;
		String sql = "update slideshow set STATE=4 where ID = "+id;
		int i = jt.update(sql);
		return i;
	}

	//轮播	查看
	public List<Map<String, Object>> getlunboByid(int id) {
		String sql = "select * from slideshow where ID ="+id;
		List<Map<String,Object>> queryForList = jt.queryForList(sql);
		return queryForList;
	}

	//轮播	修改-提交
	public int updateLunbo(String id, String state, String lbname, String oldPosition, String position, String image, String type, String contentUrl,
			String content, String userName) {
		int intid = Integer.parseInt(id);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		int a = Integer.parseInt(oldPosition);//旧位置
		int newposition = Integer.parseInt(position);//新位置
		String sql1 = "";
		if(state.equals("1")){
			if(!oldPosition.equals(position)){
				if(a<5){
					int b = a-1;
					for(int i=5;i>b;i--){
						sql1 = "update slideshow set POSITION="+(i+1)+" where STATE=1 and POSITION="+i;
						if(i==5){
							sql1 = "update slideshow set STATE=3 where STATE=1 and POSITION=5";
						}
						jt.update(sql1);
					}
				}else if(a==5){
					sql1 = "update slideshow set STATE=3 where STATE=1 and POSITION=5";
					jt.update(sql1);
				}
			}
		}
		String sql ="update slideshow set NAME='"+lbname+"' ,POSITION="+newposition+" ,TYPE='"+type+"' ,URL='"+contentUrl+"' ,CONTENT='"+content+"' ,CREATE_TIME='"+time+"' ,USER_NO='"+userName+"' where ID="+intid;
		if(image.length()>0){
			sql = "update slideshow set NAME='"+lbname+"' ,POSITION="+newposition+" ,IMAGE='"+image+"' ,TYPE='"+type+"' ,URL='"+contentUrl+"' ,CONTENT='"+content+"' ,CREATE_TIME='"+time+"' ,USER_NO='"+userName+"' where ID="+intid;
		}
		int i = jt.update(sql);
		return i;
	}
	
	//轮播	添加-暂存
	public int temporary(String lbname, String position, String image, String type, String contentUrl, String content, String userName) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		String sql = "insert into slideshow set STATE=2 ,CREATE_TIME='"+time+"' ,USER_NO='"+userName+"'";
		if(lbname!=null&&!lbname.equals("")){
			sql += " ,NAME='"+lbname+"'";
		}
		if(position!=null&&!position.equals("")){
			int a = Integer.parseInt(position);
			sql += " ,POSITION="+a;
		}
		if(image!=null&&!image.equals("")){
			sql += " ,IMAGE='"+image+"'";
		}
		if(type!=null&&!type.equals("")){
			sql += " ,TYPE='"+type+"'";
		}
		if(contentUrl!=null&&!contentUrl.equals("")){
			sql += " ,URL='"+contentUrl+"'";
		}
		if(content!=null&&!content.equals("")){
			sql += " ,CONTENT='"+content+"'";
		}
		//String sql = "insert into slideshow set NAME='"+lbname+"',POSITION="+a+",IMAGE='"+image+"',TYPE='"+type+"',URL='"+contentUrl+"',CONTENT='"+content+"',CREATE_TIME='"+time+"',USER_NO="+userId+",STATE=2";
		int i = jt.update(sql);
		return i;
	}
	//轮播	添加-发布
	public int addLunbo(String lbname, String position, String image, String type, String contentUrl, String content, String userName) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String time = df.format(d);
		int a = Integer.parseInt(position);
		String sql = "insert into slideshow set NAME='"+lbname+"',POSITION="+a+",IMAGE='"+image+"',TYPE='"+type+"',URL='"+contentUrl+"',CONTENT='"+content+"',CREATE_TIME='"+time+"',USER_NO='"+userName+"',STATE=2";
		int i = jt.update(sql);
		int b = 0;
		if(i>0){
			String sql1 = "select * from slideshow where POSITION="+a+" and CREATE_TIME='"+time+"'";
			List<Map<String,Object>> forList = jt.queryForList(sql1);
			System.out.println(forList);
			int id = Integer.parseInt(forList.get(0).get("ID").toString());
			b = updateFabu(id,a);
		}
		return b;
	}

	//轮播	发布
	public int updateFabu(int id, int position) {
		String sql3 = "select * from slideshow where STATE=1 and POSITION="+position;
		List<Map<String,Object>> forList = jt.queryForList(sql3);
		if(forList.size()>0){
			String sql2 = "";
			int a = position-1;
			if(position<5){
				for(int i=5;i>a;i--){
					sql2 = "update slideshow set POSITION="+(i+1)+" where STATE=1 and POSITION="+i;
					if(i==5){
						sql2 = "update slideshow set STATE=3 where STATE=1 and POSITION=5";
					}
					jt.update(sql2);
				}
			}else if(position==5){
				sql2 = "update slideshow set STATE=3 where STATE=1 and POSITION=5";
				jt.update(sql2);
			}
		}
		String sql = "update slideshow set STATE=1 where ID="+id;
		int i = jt.update(sql);
		return i;
	}

	//轮播	下架
	public int updateXiajia(int id) {
		String sql = "update slideshow set STATE=3 where ID="+id;
		int i = jt.update(sql);
		return i;
	}
	
}
