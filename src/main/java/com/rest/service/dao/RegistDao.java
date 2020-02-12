package com.rest.service.dao;

import com.entity.*;



public interface RegistDao {


    
    //学生注册
    public int insertStudent( Student student ,String token );
    
    
    //老师注册
    public int insertTeacher( Teacher teacher ,String token, String deviceType );

    //老师注册2 -年级
	public int insertTeacher2(Teacher teacher, String token, String deviceType );
   
}
