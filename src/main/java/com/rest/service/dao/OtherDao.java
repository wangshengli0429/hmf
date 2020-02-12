package com.rest.service.dao;

import java.util.List;
import java.util.Map;

public interface OtherDao {
	
	
	//原标题
    public List findOldTitle(String udid, String currentPage, String numPerPage);
    
    public Map<String,Object> findGrade();
    
    public List typeNumber();
    
    //作文稿数
    public List findDraftN();
    
    //搜索作文
    public List search(String cond,String numPerPage,String currentPage);
    
    // 意见反馈
    public int feedback(String state,String feedback,String token);

	//判断学生是否完善资料，没有完善不允许上传
	public String findStudentComplete_info(String udid);

	//获取评点卡
	public List<Map<String,Object>> findDpCard_base();

	//定时任务
	public void checkDpCard(int i);

	//定时任务，红包过期
	public void checkRed();

	//我的评点卡
	public List<Map<String, Object>> findDpCard_stu_list(String udid, String currentPage, String numPerPage);

	//老师排序
	public void sortTeacher();
	
	//分享作文
	public Map<String, Object> getCompostionByID(String id, String condition);

	//签名生成前检查作文是否付款
	public int findCompositionHasPay(String compId);

	//获取点评作文图片详情
	public Map<String, Object> getCompositionImageByIdAndIndex(String id, String index);

	//获取所有点评过的作文集合
	public Map<String, Object> getAllCom_composition(String condition);

	//获取opendid
	public String getOpenidByUdid(String udid);
	
	//临时复制数据库appraise
	public void tempInsertAppraise();
	//临时复制作文图片
	void tempInsertCom_composition();

	//插入热区
	public int insertComPic(List<Map<String, String>> list, String sid, String tid, String cid, String image_id);

	//获取图片路径
	public Map<String, Object> findCompositionImg(String id, String imgIndex);

	//查看图片是否已经被点评
	public boolean findCompositionImgHasCom(String id, String imgIndex);

	//h5投稿检查用户登录
	public Map<String, String> h5TouGaoLogin(String phone, String password);

	//h5投稿验证手机号是否注册
	public int validatePhone(String phone);

	//h5投稿上传作文
	public int touGaoinsertComposition(Map<String, String> map, List<String> picList);

}
