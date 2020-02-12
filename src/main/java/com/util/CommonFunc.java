package com.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 常用的公共函数
 * @author lcc
 *
 */
public class CommonFunc {
	//转Object至String 类型
	public static String toString(Object obj) {
		return obj == null ? "" : obj.toString();
	}
	
	public static String trimRight(String src, char c) {
		if (src == null || src.length() < 1) {
			return "";
		}
		
		int index = src.length()-1;
		while (src.charAt(index) == c) {
			index--;
		}
		index++;
		if (index < src.length()) {
			src = src.substring(0,index);
		}
		return src;
	}
	
	public static boolean isEmptyStr(Object obj) {
		return obj == null || "".equals(obj);
	}
	
	//通过用户注册类型，判断是否为机构类用户
	public static boolean isOrgUser(String registerType) {
		if (registerType == null) {
			return true;
		}

		return !registerType.equals("003001")    //系统内注册
			 && !registerType.equals("003002")   //QQ注册
			 && !registerType.equals("003003")   //新浪微博注册
			 && !registerType.equals("003020");  //微信注册
		
	}
	
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式  
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式  
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式  
    private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符  
      
    /** 
     * @param htmlStr 
     * @return 
     *  删除Html标签 
     */  
    public static String delHTMLTag(String htmlStr) {
    	if (htmlStr == null || "".equals(htmlStr.trim())) {
    		return "";
    	}
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);  
        Matcher m_script = p_script.matcher(htmlStr);  
        htmlStr = m_script.replaceAll(""); // 过滤script标签  
  
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);  
        Matcher m_style = p_style.matcher(htmlStr);  
        htmlStr = m_style.replaceAll(""); // 过滤style标签  
  
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);  
        Matcher m_html = p_html.matcher(htmlStr);  
        htmlStr = m_html.replaceAll(""); // 过滤html标签  
  
        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);  
        Matcher m_space = p_space.matcher(htmlStr);  
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签 
        
        htmlStr = htmlStr.replace("&nbsp;", " ");
        return htmlStr.trim(); // 返回文本字符串  
    }  
    
    //取第一句话
    public static String getTextFromHtml(String htmlStr){  
        htmlStr = delHTMLTag(htmlStr);  
        htmlStr = htmlStr.replaceAll(" ", "");  
        htmlStr = htmlStr.substring(0, htmlStr.indexOf("。")+1);  
        return htmlStr;  
    } 
    
    //获取文章摘要，提取前100字,文章的内容可能存储的是HTML字符串
    public static String getArticleBrief(String htmlStr){ 
        return getArticleBrief(htmlStr,100);  
    }
    
  //获取文章摘要，文章的内容可能存储的是HTML字符串
    public static String getArticleContent(String htmlStr){ 
        return getArticleBrief(htmlStr,-1);  
    }
    
    /**
     * 获取文章摘要
     * @param htmlStr
     * @param briefLen 如果小于1， 则默认读取文章全部内容
     * @return
     */
    public static String getArticleBrief(String htmlStr,int briefLen){
    	if (htmlStr == null || "".equals(htmlStr.trim())) {
    		return "";
    	}
    	
    	//截取文章正文:对于编辑录入的文章，文章标题、作者与正文是混合在一起的，所以需要剔除正文之外的内容
    	Document xmlDoc = null;
    	Element rootElt = null;
    	try {
    		//替换"&nbsp;"，负责解析XML时抛异常（Nested exception: 引用了实体 "nbsp", 但未声明它。）
    		htmlStr = htmlStr.replace("&nbsp;", " ");
    		
    		xmlDoc = DocumentHelper.parseText(htmlStr);
    		 // 获取根节点  
    		rootElt = xmlDoc.getRootElement();
    		
    		//删除标题
    		delElementsByAttribute(rootElt,"class","eps-title");
    		
    		//删除作者
    		delElementsByAttribute(rootElt,"class","eps-author");
    		
    		htmlStr = xmlDoc.asXML();
    	} catch (Exception e) {
    		rootElt = null;
    		xmlDoc = null;
    		//Logger logger = LogManager.getLogger();
    		//logger.error("2016061917180 文章内容提取失败："+e.getMessage());
    		//logger.error("2016061917180 文章内容全文：\r\n"+htmlStr);
    	}

        htmlStr = delHTMLTag(htmlStr);
        htmlStr = htmlStr.trim();
        if ("".equals(htmlStr)) {        	
        	
        	if (rootElt != null) {
        		List<Element> elements = new ArrayList<Element>();
            	getElementsByTagName(rootElt,"img",elements);
            	if (elements.size() > 0) {
            		//如果仅有图片，则返回图片的缩略图,进取第一张图片,且限定其大小
            		htmlStr = "<img src='"+elements.get(0).attributeValue("src")+"' style='max-width:300px;max-height:100px;'></img>";
            	}
        	}
        } else if (briefLen > 0 && htmlStr.length() > briefLen) {
        	htmlStr = htmlStr.substring(0,briefLen); 
        	htmlStr +="...";
        } 
        
        return htmlStr;  
    }
    
    //删除由属性值attrValue指定的属性attrName的所属节点
    private static void delElementsByAttribute(Element parentElt,String attrName,String attrValue) {
    	if (attrValue.equalsIgnoreCase(parentElt.attributeValue(attrName))) {
    		parentElt.detach();
    		return ;
		}
    	
    	List<Element> eles = parentElt.elements();
    	for (int i=eles.size()-1; i>-1; i--) {
    		delElementsByAttribute(eles.get(i),attrName,attrValue);
    	}
    }
    
    private static void getElementsByTagName(Element parentElt,String tagName,List<Element> elements) {
    	if (parentElt == null || "".equals(tagName)) {
    		return ;
    	}
    	if (tagName.equalsIgnoreCase(parentElt.getName())) {
    		elements.add(parentElt);
    		return ;
    	}
    	
    	List<Element> eles = parentElt.elements();
    	for (int i=eles.size()-1; i>-1; i--) {
    		getElementsByTagName(eles.get(i),tagName,elements);
    	}
    }
}
