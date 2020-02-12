package com.rest.service;

import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

/*
 *上传作文、师资认证接口
 * 
 */
public interface UploadService {
	
	//上传作文
	public String insertComposi(String json) throws ParseException;
	
	//上传师资认证
	public String insertAuthent(String json) throws ParseException;
	
	//修改作文
	public String sModifyCompositionUploadlist(String json);
	
	//师资认证多文件上传 
    public String tUploadFileList(List<Attachment>attachments,@Context HttpServletRequest request);  
    
    //作文多文件上传 
    public String sUploadFileList(List<Attachment>attachments,@Context HttpServletRequest request);  
  
    // 修改作文多文件上传 
    public String sModifyCompositionUploadlist2(List<Attachment>attachments,@Context HttpServletRequest request);  
    
    // 老师端未批改作文语音点评 
	public String tSpeechCommentUploadlist(List<Attachment> attachments, HttpServletRequest request) ;
    
	//作文多文件上传新流程
    public String sUploadlist2(List<Attachment>attachments,@Context HttpServletRequest request);

    //老师批改图片
	String teaComPic(List<Attachment>attachments,@Context HttpServletRequest request);  
}
