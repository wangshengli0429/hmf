package com.rest.service;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

public interface OtherService {
	
    //原标题
    public String oldTitle(String json) throws ParseException;
    
	//年级
    public String grade(String json) throws ParseException;
    
    //类型编号(文体033，体裁013，技法026)
    public String typeNumber(String json) throws ParseException;
    
	//作文稿数
    public String draftNumber(String json) throws ParseException;
    
    //搜索作文
    public String search(String json) throws ParseException;

    //支付签名
    public String autograph(String json) throws ParseException;
    public String autograph2(String json) throws ParseException;
    
    //微信支付签名
    public String wxAutograph(String json) throws Exception;
    String wxAutograph2(String json) throws Exception;
    
    //意见反馈
    public String feedback(String json) throws ParseException;
    
    //获取评点卡基本类型
    public String dpCard_base(String json) throws ParseException;
    
    /*//定时任务
    public void myScheduled() throws ParseException;*/
    
    //我的评点卡
    public String dpCardStuList(String json) throws ParseException;
    
    //分享作文
    String shareComposition(String json)throws ParseException;
    
    //展示作文点评内容
    String showComposition(String json)throws ParseException;
    
    //作文列表首页
    String compositionHome(String json)throws ParseException;

	void temp() throws ParseException;

	//图片修改建立热区
	String remakePic(String json) throws ParseException;
    
	//获得图片路径
	String findCompositionImg(String json) throws ParseException;
	
	//查看图片是否已经被点评
	String findCompositionImgHasCom(String json) throws ParseException;
	
	//h5投稿检查用户登录
	void h5TouGaoLogin(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ParseException;

	//h5投稿文字或图片投稿
	String wordAndPicContribute(List<Attachment> attachments) throws ParseException;

	//h5投稿获取验证码
	void h5getVerificationCode(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ParseException;

	//h5投稿用户注册
	void h5register(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ParseException;

	//h5投稿验证手机号是否注册
	void h5validatePhone(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ParseException;
}
