/**   
 * Copyright © 2017 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.util 
 * @author: think   
 * @date: 2017-12-8 上午11:34:45 
 */
package com.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: TeacherSortUtils 
 * @Description: TODO
 * @author: think
 * @date: 2017-12-8 上午11:34:45  
 */
public class TeacherSortUtils {
	//负责态度基点
	private static double attitBase = 1.0;
	//满意度基点
	private static double satisfactionBase = 1.0;
	//专业水平基点
	private static double profexBase = 1.0;
	//点评数量基点
	private static double countBase = 1.0;
	//教龄基点
	private static double eduTimeBase = 1.0;
	
	//排序比较
	public static List<Map<String, Object>> sortTeacher(List<Map<String, Object>> list){
		List<Map<String, Object>> newList = new ArrayList<>();
		for (Map<String, Object> m : list) {
			String id = m.get("id").toString();
			double attit = 0;
			double satisfaction = 0;
			double profex = 0;
			double count = 0;
			double eduTime = 0;
			if (m.get("ATTIT") != null && !m.get("ATTIT").equals("")) {
				attit = Double.parseDouble(m.get("ATTIT").toString());
			}
			if (m.get("PROFES") != null && !m.get("PROFES").equals("")) {
				profex = Double.parseDouble(m.get("PROFES").toString());
			}
			if (m.get("SATISFACTION") != null && !m.get("SATISFACTION").equals("")) {
				satisfaction = Double.parseDouble(m.get("SATISFACTION").toString());
			}
			if (m.get("EDU_TIME") != null && !m.get("EDU_TIME").equals("")) {
				eduTime = Double.parseDouble(m.get("EDU_TIME").toString());
			}
			if (m.get("count") != null && !m.get("count").equals("")) {
				count = Double.parseDouble(m.get("count").toString());
			}
			Map<String, Object> map = new HashMap<>();
			map.put(id, attit * attitBase + (3-satisfaction) * satisfactionBase + profex * profexBase + count * countBase + eduTime * eduTimeBase);
			newList.add(map);
		}
		return newList;
	}
	//算法拿到比较值
	public static double getTeacherNum(Map<String, Object> m){
		double attit = 0;
		double satisfaction = 0;
		double profex = 0;
		double count = 0;
		double eduTime = 0;
		if (m.get("ATTIT") != null && !m.get("ATTIT").equals("")) {
			attit = Double.parseDouble(m.get("ATTIT").toString());
		}
		if (m.get("PROFES") != null && !m.get("PROFES").equals("")) {
			profex = Double.parseDouble(m.get("PROFES").toString());
		}
		if (m.get("SATISFACTION") != null && !m.get("SATISFACTION").equals("")) {
			satisfaction = Double.parseDouble(m.get("SATISFACTION").toString());
		}
		if (m.get("EDU_TIME") != null && !m.get("EDU_TIME").equals("")) {
			eduTime = Double.parseDouble(m.get("EDU_TIME").toString());
		}
		if (m.get("count") != null && !m.get("count").equals("")) {
			count = Double.parseDouble(m.get("count").toString());
		}
		return attit * attitBase + (3-satisfaction) * satisfactionBase + profex * profexBase + count * countBase + eduTime * eduTimeBase;
	}
}
