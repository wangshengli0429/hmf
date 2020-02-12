/**   
 * Copyright © 2017 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: utils 
 * @author: think   
 * @date: 2017-12-12 上午10:23:32 
 */
package com.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: SensitivewordFilter 
 * @Description: TODO
 * @author: think
 * @date: 2017-12-12 上午10:23:32  
 */
public class SensitivewordFilter {
    private Map sensitiveWordMap = null;
    public static int minMatchTYpe = 1;      //最小匹配规则
    public static int maxMatchType = 2;      //最大匹配规则
    
    /**
     * 构造函数，初始化敏感词库
     */
    public SensitivewordFilter(){
        sensitiveWordMap = new SensitiveWordInit().initKeyWord();
    }
    
    /**
     * 判断文字是否包含敏感字符
     * @param matchType  匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
     * @return 若包含返回true，否则返回false
     * @version 1.0
     */
    public boolean isContaintSensitiveWord(String txt,int matchType){
        boolean flag = false;
        for(int i = 0 ; i < txt.length() ; i++){
            int matchFlag = this.CheckSensitiveWord(txt, i, matchType); //判断是否包含敏感字符
            if(matchFlag > 0){    //大于0存在，返回true
                flag = true;
            }
        }
        return flag;
    }
    
    /**
     * 获取文字中的敏感词
     * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
     * @version 1.0
     */
    public Set<String> getSensitiveWord(String txt , int matchType){
        Set<String> sensitiveWordList = new HashSet<String>();
        
        for(int i = 0 ; i < txt.length() ; i++){
            int length = CheckSensitiveWord(txt, i, matchType);    //判断是否包含敏感字符
            if(length > 0){    //存在,加入list中
                sensitiveWordList.add(txt.substring(i, i+length));
                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }
        
        return sensitiveWordList;
    }
    
    /**
     * 替换敏感字字符
     * @param replaceChar 替换字符，默认*
     * @version 1.0
     */
    public String replaceSensitiveWord(String txt,int matchType,String replaceChar){
        String resultTxt = txt;
        Set<String> set = getSensitiveWord(txt, matchType);     //获取所有的敏感词
        Iterator<String> iterator = set.iterator();
        String word = null;
        String replaceString = null;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }
        
        return resultTxt;
    }
    
    /**
     * 获取替换字符串
     * @author chenming 
     * @date 2014年4月20日 下午5:21:19
     * @param replaceChar
     * @param length
     * @return
     * @version 1.0
     */
    private String getReplaceChars(String replaceChar,int length){
        String resultReplace = replaceChar;
        for(int i = 1 ; i < length ; i++){
            resultReplace += replaceChar;
        }
        
        return resultReplace;
    }
    
    /**
     * 检查文字中是否包含敏感字符，检查规则如下：<br>
     * @return，如果存在，则返回敏感词字符的长度，不存在返回0
     * @version 1.0
     */
    @SuppressWarnings({ "rawtypes"})
    public int CheckSensitiveWord(String txt,int beginIndex,int matchType){
        boolean  flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        char word = 0;
        Map nowMap = sensitiveWordMap;
        for(int i = beginIndex; i < txt.length() ; i++){
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);     //获取指定key
            if(nowMap != null){     //存在，则判断是否为最后一个
                matchFlag++;     //找到相应key，匹配标识+1 
                if("1".equals(nowMap.get("isEnd"))){       //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true;       //结束标志位为true   
                    if(SensitivewordFilter.minMatchTYpe == matchType){    //最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            }
            else{     //不存在，直接返回
                break;
            }
        }
        if(matchFlag < 2 || !flag){        //长度必须大于等于1，为词 
            matchFlag = 0;
        }
        return matchFlag;
    }
    /**
     * 停顿词库剔除
     */
    public String deleteStopWord(String str) throws Exception{
    	InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("stopword.mgc");
    	InputStreamReader read = new InputStreamReader(is,"UTF-8");
    	BufferedReader br = new BufferedReader(read);
    	StringBuffer sb = new StringBuffer();
    	String temp = "";
    	while ((temp = br.readLine()) != null){
    		sb.append(temp);
    	}
    	String string = sb.toString();
    	String[] split = string.split("");
    	List<String> list = new ArrayList<>();
    	for (String  s: split) {
			list.add(s);
		}
    	StringBuffer sb2 = new StringBuffer();
    	String[] split2 = str.split("");
    	for (String s : split2) {
			if (!list.contains(s)) {
				sb2.append(s);
			}
		}
    	br.close();
    	read.close();
    	is.close();
    	return sb2.toString();
    }
    
    /*判断敏感词*/
    public static List<String> sensitiveWord(String str) throws Exception{
    	List<String> list = new  ArrayList<>();
    	SensitivewordFilter filter = new SensitivewordFilter();
    	String string = filter.deleteStopWord(str);
    	Set<String> set = filter.getSensitiveWord(string, 1);
    	for (String s : set) {
			list.add(s);
		}
    	return list;
    }
    
}
