package com.rest.service;

import java.text.ParseException;

public interface ITMService {

	/*
	 *技法，范文，素材模块接口
	 * 
	 */
	
	//技法、范文、素材详细
    public String detail(String json) throws ParseException;
    
    //范文list
    public String ModelList(String json) throws ParseException;
    
    //素材list
    public String MaterialList(String json) throws ParseException;
    
    //技法list
    public String TechniqueList(String json) throws ParseException;
}
