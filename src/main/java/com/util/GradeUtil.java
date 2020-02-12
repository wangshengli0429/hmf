package com.util;

import java.util.Arrays;

public class GradeUtil {

	public static String[] gradeNameArray = { "低年级", "小考", "中考", "一年级", "二年级", "三年级", "四年级", "五年级", "五年级", "六年级", "七年级", "八年级", "九年级", "高一", "高二", "高三" };

	public static String[] gradeCodeArray = { "014001", "027007", "027014", "027001", "027002", "027003", "027004", "027005", "027006", "027011", "027012", "027013", "027021", "027022", "027023" };

	public static String getGradeCode(String gradeName) {
		int index = Arrays.asList(gradeNameArray).indexOf(gradeName);
		if (index > -1 && index < gradeCodeArray.length) {
			return gradeCodeArray[index];
		}
		return "027014";// default value
	}

	public static String getGradeName(String gradeCode) {
		int index = Arrays.asList(gradeCodeArray).indexOf(gradeCode);
		if (index > -1 && index < gradeNameArray.length) {
			return gradeNameArray[index];
		}
		return "中考";// default value
	}

}
