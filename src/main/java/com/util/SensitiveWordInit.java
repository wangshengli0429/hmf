/**   
 * Copyright © 2017 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: utils 
 * @author: think   
 * @date: 2017-12-12 上午10:22:04 
 */
package com.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: SensitiveWordInit 
 * @Description: TODO
 * @author: think
 * @date: 2017-12-12 上午10:22:04  
 */
public class SensitiveWordInit {
    public HashMap sensitiveWordMap;
    
    public SensitiveWordInit(){
        super();
    }
    
    @SuppressWarnings("rawtypes")
    public Map initKeyWord(){
        try {
            //读取敏感词库
            Set<String> keyWordSet = readSensitiveWordFile();
            //将敏感词库加入到HashMap中
            addSensitiveWordToHashMap(keyWordSet);
            //spring获取application，然后application.setAttribute("sensitiveWordMap",sensitiveWordMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }

    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
        String key = null;  
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while(iterator.hasNext()){
            key = iterator.next();    //关键字
            nowMap = sensitiveWordMap;
            for(int i = 0 ; i < key.length() ; i++){
                char keyChar = key.charAt(i);       //转换成char型
                Object wordMap = nowMap.get(keyChar);       //获取
                
                if(wordMap != null){        //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                }
                else{     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String,String>();
                    newWorMap.put("isEnd", "0");     //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }
                
                if(i == key.length() - 1){
                    nowMap.put("isEnd", "1");    //最后一个
                }
            }
        }
    }
    
    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
     */
    private Set<String> readSensitiveWordFile() throws Exception{
        Set<String> set = new HashSet<>();
        
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("minganci.mgc");
    	InputStreamReader read = new InputStreamReader(is,"UTF-8");
    	BufferedReader br = new BufferedReader(read);
    	String temp = "";
    	while ((temp = br.readLine()) != null){
    		set.add(temp);
    	}
    	br.close();
    	read.close();
    	is.close();
        return set;
    }
    
}