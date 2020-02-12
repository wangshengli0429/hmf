package com.util;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class ComPicUtils {
	public static void main(String[] args) throws IOException {
	}
	//像素转换
	public static int turnPixel(int oldInt, int newInt, int i){
		return i * oldInt / newInt;
	}
	
	//读取json
	public static List<Map<String, String>> readJsonFromFile(String json) throws IOException{
		/*Path path = Paths.get("F://new 1.txt");
		BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
		json = reader.readLine();*/
		List<Map> data = JSONObject.parseArray(json, Map.class);
		List<Map> temp = new ArrayList<>();
		for (int i = data.size() - 1; i >=0 ; i--) {
			String commentConcent = data.get(data.size() - 1).get("commentConcent").toString();
			if ("".equals(commentConcent)) {
				data.get(data.size() - 1).put("commentConcent", "此处没有批注！");
			}
			List<Integer> xList = new ArrayList<>();
			List<Integer> yList = new ArrayList<>();
			String[] sts1 = data.get(i).get("stsrtPosi").toString().split(",");
			xList.add(Integer.parseInt(sts1[0]));
			yList.add(Integer.parseInt(sts1[1]));
			String[] sts2 = data.get(i).get("endPosi").toString().split(",");
			xList.add(Integer.parseInt(sts2[0]));
			yList.add(Integer.parseInt(sts2[1]));
			List<Map<String, Integer>> passRouteList = (List<Map<String, Integer>>) data.get(i).get("passRoute");
			for (Map<String, Integer> map2 : passRouteList) {
				xList.add(map2.get("x"));
				yList.add(map2.get("y"));
			}
			if ("".equals(data.get(i).get("commentConcent").toString())) {
				List<Integer> xListMap = (List<Integer>) temp.get(temp.size() - 1).get("xList");
				List<Integer> yListMap = (List<Integer>) temp.get(temp.size() - 1).get("yList");
				xListMap.addAll(xList);
				yListMap.addAll(yList);
				temp.get(temp.size() - 1).put("xList", xListMap);
				temp.get(temp.size() - 1).put("yList", yListMap);
				
			}else {
				Map<String, Object> map = new HashMap<>();
				map.put("commentConcent", data.get(i).get("commentConcent").toString());
				map.put("xList", xList);
				map.put("yList", yList);
				temp.add(map);
			}
		}
		List<Map<String,String>> list = filerElement2List(temp);
		
		//createNewArea(list.get(0), list.get(1));
		recurList(list);
		return list;
	}
	
	
	//筛选临时集合，获得新集合
	private static List<Map<String, String>> filerElement2List(List<Map> list){
		List<Map<String, String>> returnList = new ArrayList<>();
		for (Map map : list) {
			List<Integer> xList = (List<Integer>) map.get("xList");
			List<Integer> yList = (List<Integer>) map.get("yList");
			int[] xMaxAndMin = getMaxAndMixInt(xList);
			int[] yMaxAndMin = getMaxAndMixInt(yList);
			Map<String, String> returnMap = new HashMap<>();
			returnMap.put("content", map.get("commentConcent").toString());
			returnMap.put("cords_x1", xMaxAndMin[1] + "");
			returnMap.put("cords_x2", xMaxAndMin[0] + "");
			returnMap.put("cords_y1", yMaxAndMin[1] + "");
			returnMap.put("cords_y2", yMaxAndMin[0] + "");
			returnList.add(returnMap);
		}
		return returnList;
	}
	
	//所有x或者y坐标集合得到最小和最大值
	private static int[] getMaxAndMixInt(List<Integer> list){
		int max = 0;
		int min = Integer.MAX_VALUE;
		for (Integer integer : list) {
			if (integer > max) {
				max = integer;
			}
			if (integer < min) {
				min = integer;
			}
		}
		int[] arr = new int[2];
		arr[0] = max;
		arr[1] = min;
		return arr;
	}
	
	//比较两个热区面积，前者大返回true，后者面积更大返回false
	private static boolean compare2AreaWhoMoreBig(Map<String,String> map1, Map<String,String> map2){
		int map1MinX = Integer.parseInt(map1.get("cords_x1"));
		int map1MinY = Integer.parseInt(map1.get("cords_y1"));
		int map1MaxX = Integer.parseInt(map1.get("cords_x2"));
		int map1MaxY = Integer.parseInt(map1.get("cords_y2"));
		int map2MinX = Integer.parseInt(map2.get("cords_x1"));
		int map2MinY = Integer.parseInt(map2.get("cords_y1"));
		int map2MaxX = Integer.parseInt(map2.get("cords_x2"));
		int map2MaxY = Integer.parseInt(map2.get("cords_y2"));
		//计算面积
		BigInteger map1X = new BigInteger((map1MaxX - map1MinX) + "");
		BigInteger map1Y = new BigInteger((map1MaxY - map1MinY) + "");
		BigInteger map2X = new BigInteger((map2MaxX - map2MinX) + "");
		BigInteger map2Y = new BigInteger((map2MaxY - map2MinY) + "");
		BigInteger areaX = map1X.multiply(map1Y);
		BigInteger areaY = map2X.multiply(map2Y);
		//比较面积
		int areai = areaX.compareTo(areaY);//areai大于0，map1面积更,否则map1面积更小
		if (areai >= 0) {
			return true;
		}else {
			return false;
		}
	}
	
	
	//如果有交叉创建热区，解除交叉
	private static List<Map<String,String>> createNewArea(Map<String,String> map1, Map<String,String> map2){
		int map1MinX = Integer.parseInt(map1.get("cords_x1"));
		int map1MinY = Integer.parseInt(map1.get("cords_y1"));
		int map1MaxX = Integer.parseInt(map1.get("cords_x2"));
		int map1MaxY = Integer.parseInt(map1.get("cords_y2"));
		int map2MinX = Integer.parseInt(map2.get("cords_x1"));
		int map2MinY = Integer.parseInt(map2.get("cords_y1"));
		int map2MaxX = Integer.parseInt(map2.get("cords_x2"));
		int map2MaxY = Integer.parseInt(map2.get("cords_y2"));
		
		boolean b1 = map1MinX > map2MinX && map1MinX < map2MaxX && map1MinY > map2MinY && map1MinY < map2MaxY;
		boolean b2 = map1MaxX > map2MinX && map1MaxX < map2MaxX && map1MaxY > map2MinY && map1MaxY < map2MaxY;
		boolean b3 = map2MinX > map1MinX && map2MinX < map1MaxX && map2MinY > map1MinY && map2MinY < map1MaxY;
		boolean b4 = map2MaxX > map1MinX && map2MaxX < map1MaxX && map2MaxY > map1MinY && map2MaxY < map1MaxY;
		
		boolean b9 = map1MinX < map2MinX && map1MinY > map2MinY;
		boolean b10 = map2MinX < map1MinX && map2MinY > map1MinY;
		boolean b11 = map1MinX > map2MinX && map1MinY < map2MinX;
		boolean b12 = map2MinX > map1MinX && map2MinY < map1MinX;
		
		boolean b13 = map1MaxX > map2MaxX && map1MaxY < map2MaxY;
		boolean b14 = map2MaxX > map1MaxX && map2MaxY < map1MaxY;
		boolean b15 = map1MaxX < map2MaxX && map1MaxY > map2MaxY;
		boolean b16 = map2MaxX < map1MaxX && map2MaxY > map1MaxY;
		
		boolean b17 = map1MinX > map2MinX && map1MinX < map2MaxX && map1MinY < map2MinY;
		boolean b18 = map1MaxX > map2MinX && map1MaxX < map2MaxX && map1MaxY > map2MaxY;
		boolean b19 = map2MinX > map1MinX && map2MinX < map1MaxX && map2MinY < map1MinY;
		boolean b20 = map2MaxX > map1MinX && map2MaxX < map1MaxX && map2MaxY > map1MaxY;
		
		boolean oneDown = map1MinY == map2MinY && ((map1MinX <= map2MinX && map1MaxX > map2MinX) || (map1MinX >= map2MinX && map1MinX < map2MaxX));
		boolean oneUp = map1MaxY == map2MaxY && ((map1MinX <= map2MinX && map1MaxX > map2MinX) || (map1MinX >= map2MinX && map1MinX < map2MaxX));
		boolean oneLeft = map1MinX == map2MinX && ((map1MaxY >= map2MaxY && map1MinY < map2MaxY) || (map2MaxY >= map1MaxY && map2MinY < map1MaxY));
		boolean oneRight = map1MaxX == map2MaxX && ((map1MaxY >= map2MaxY && map1MinY < map2MaxY) || (map2MaxY >= map1MaxY && map2MinY < map1MaxY));
		
		
		//单边或者多边重合
		if (oneDown || oneUp || oneLeft || oneRight) {
			return createAreaCoincide(map1, map2, oneDown, oneUp, oneLeft, oneRight);
		}
		
		//交叉第一种情况  ： 完全包含
		if ((b1 && b2) || (b3 && b4)) {
			return createAreaSheerCross(map1, map2);
		}
		//第二种情况：部分包含右交叉
		if ((b1 && b13) || (b3 && b14)) {
			return createAreaRightCross(map1, map2);
		}
		//部分包含上交叉
		if ((b1 && b15) || (b3 && b16)) {
			return createAreaUpCross(map1, map2);
		}
		//部分包含左交叉
		if ((b2 && b9) || (b4 && b10)) {
			return createAreaLeftCross(map1, map2);
		}
		//部分包含下交叉
		if ((b2 && b11) || (b4 && b12)) {
			return createAreaDownCross(map1, map2);
		}
		//第三种情况：十字交叉
		if ((b17 && b18) || (b19 && b20)){
			return createAreaTenCross(map1, map2);
		}
		//第四种情况：对角交叉
		if ((b1 && b4) || (b2 && b3)){
			return createAreaOppositeCross(map1, map2);
		}
		return null;
	}
	
	//递归调用，list集合内如果有重合的map，就创建新的map添加到list中
	private static void recurList(List<Map<String,String>> list){
		loop1:for (int i = 0; i < list.size() - 1; i++) {
			for (int j = i + 1; j < list.size(); j++) {
				List<Map<String,String>> new2AreaList = createNewArea(list.get(i), list.get(j));
				if (new2AreaList != null) {
					List<Map<String,String>> tempList = new ArrayList<>();
					for (int k = 0; k < list.size(); k++) {
						if (k != i && k != j) {
							tempList.add(list.get(k));
						}
					}
					list.clear();
					list.addAll(tempList);
					list.addAll(new2AreaList);
					recurList(list);
					break loop1;
				}else {
					continue;
				}
			}
		}
	}
	
	//解除交叉：1完全包含
	private static List<Map<String,String>> createAreaSheerCross(Map<String,String> map1, Map<String,String> map2){
		List<Map<String,String>> returnList = new ArrayList<>();
		boolean b = compare2AreaWhoMoreBig(map1, map2);
		if (!b) {
			Map<String,String> temp = new HashMap<>();
			temp.putAll(map1);
			map1.clear();
			map1.putAll(map2);
			map2.clear();
			map2.putAll(temp);
		}
		int map1MinX = Integer.parseInt(map1.get("cords_x1"));
		int map1MinY = Integer.parseInt(map1.get("cords_y1"));
		int map1MaxX = Integer.parseInt(map1.get("cords_x2"));
		int map1MaxY = Integer.parseInt(map1.get("cords_y2"));
		int map2MinX = Integer.parseInt(map2.get("cords_x1"));
		int map2MinY = Integer.parseInt(map2.get("cords_y1"));
		int map2MaxX = Integer.parseInt(map2.get("cords_x2"));
		int map2MaxY = Integer.parseInt(map2.get("cords_y2"));
		//第一区域
		int newMapMinX1 = map1MinX;
		int newMapMinY1 = map1MinY;
		int newMapMaxX1 = map2MinX;
		int newMapMaxY1 = map1MaxY;
		//第二区域
		int newMapMinX2 = map2MaxX;
		int newMapMinY2 = map1MinY;
		int newMapMaxX2 = map1MaxX;
		int newMapMaxY2 = map1MaxY;			
		//第三区域
		int newMapMinX3 = map2MinX;
		int newMapMinY3 = map1MinY;
		int newMapMaxX3 = map2MaxX;
		int newMapMaxY3 = map2MinY;
		//第四区域
		int newMapMinX4 = map2MinX;
		int newMapMinY4 = map2MaxY;
		int newMapMaxX4 = map2MaxX;
		int newMapMaxY4 = map1MaxY;
		Map<String,String> newMap1 = new HashMap<>();
		Map<String,String> newMap2 = new HashMap<>();
		Map<String,String> newMap3 = new HashMap<>();
		Map<String,String> newMap4 = new HashMap<>();
		//添加元素
		newMap1.put("content", map1.get("content"));
		newMap1.put("cords_x1", newMapMinX1 + "");
		newMap1.put("cords_y1", newMapMinY1 + "");
		newMap1.put("cords_x2", newMapMaxX1 + "");
		newMap1.put("cords_y2", newMapMaxY1 + "");
		//添加元素
		newMap2.put("content", map1.get("content"));
		newMap2.put("cords_x1", newMapMinX2 + "");
		newMap2.put("cords_y1", newMapMinY2 + "");
		newMap2.put("cords_x2", newMapMaxX2 + "");
		newMap2.put("cords_y2", newMapMaxY2 + "");
		//添加元素
		newMap3.put("content", map1.get("content"));
		newMap3.put("cords_x1", newMapMinX3 + "");
		newMap3.put("cords_y1", newMapMinY3 + "");
		newMap3.put("cords_x2", newMapMaxX3 + "");
		newMap3.put("cords_y2", newMapMaxY3 + "");
		//添加元素
		newMap4.put("content", map1.get("content"));
		newMap4.put("cords_x1", newMapMinX4 + "");
		newMap4.put("cords_y1", newMapMinY4 + "");
		newMap4.put("cords_x2", newMapMaxX4 + "");
		newMap4.put("cords_y2", newMapMaxY4 + "");
		List<Map<String,String>> tempList = new ArrayList<>();
		tempList.add(newMap1);
		tempList.add(newMap2);
		tempList.add(newMap3);
		tempList.add(newMap4);
		tempList.add(map2);
		filerElementInMaps(returnList, tempList);
		System.out.println("完全包含");
		return returnList;
	}
	
	//接触交叉：部分包含上交叉
	private static List<Map<String,String>> createAreaUpCross(Map<String,String> map1, Map<String,String> map2){
		List<Map<String,String>> returnList = new ArrayList<>();
		boolean b = Integer.parseInt(map1.get("cords_x1")) < Integer.parseInt(map2.get("cords_x1"));
		//始终保持map1为底元素
		if (!b) {
			Map<String,String> temp = new HashMap<>();
			temp.putAll(map1);
			map1.clear();
			map1.putAll(map2);
			map2.clear();
			map2.putAll(temp);
		}
		int map1MinX = Integer.parseInt(map1.get("cords_x1"));
		int map1MinY = Integer.parseInt(map1.get("cords_y1"));
		int map1MaxX = Integer.parseInt(map1.get("cords_x2"));
		int map1MaxY = Integer.parseInt(map1.get("cords_y2"));
		int map2MinX = Integer.parseInt(map2.get("cords_x1"));
		int map2MinY = Integer.parseInt(map2.get("cords_y1"));
		int map2MaxX = Integer.parseInt(map2.get("cords_x2"));
		int map2MaxY = Integer.parseInt(map2.get("cords_y2"));
		boolean b1 = compare2AreaWhoMoreBig(map1, map2);
		
		if (b1) {
			//第一区域
			int newMapMinX1 = map1MinX;
			int newMapMinY1 = map1MinY;
			int newMapMaxX1 = map2MinX;
			int newMapMaxY1 = map1MaxY;
			//第二区域
			int newMapMinX2 = map2MaxX;
			int newMapMinY2 = map1MinY;
			int newMapMaxX2 = map1MaxX;
			int newMapMaxY2 = map1MaxY;	
			//第三区域
			int newMapMinX3 = map2MinX;
			int newMapMinY3 = map1MinY;
			int newMapMaxX3 = map2MaxX;
			int newMapMaxY3 = map2MinY;
			Map<String,String> newMap1 = new HashMap<>();
			Map<String,String> newMap2 = new HashMap<>();
			Map<String,String> newMap3 = new HashMap<>();
			//添加元素
			newMap1.put("content", map1.get("content"));
			newMap1.put("cords_x1", newMapMinX1 + "");
			newMap1.put("cords_y1", newMapMinY1 + "");
			newMap1.put("cords_x2", newMapMaxX1 + "");
			newMap1.put("cords_y2", newMapMaxY1 + "");
			//添加元素
			newMap2.put("content", map1.get("content"));
			newMap2.put("cords_x1", newMapMinX2 + "");
			newMap2.put("cords_y1", newMapMinY2 + "");
			newMap2.put("cords_x2", newMapMaxX2 + "");
			newMap2.put("cords_y2", newMapMaxY2 + "");
			//添加元素
			newMap3.put("content", map1.get("content"));
			newMap3.put("cords_x1", newMapMinX3 + "");
			newMap3.put("cords_y1", newMapMinY3 + "");
			newMap3.put("cords_x2", newMapMaxX3 + "");
			newMap3.put("cords_y2", newMapMaxY3 + "");
			List<Map<String,String>> tempList = new ArrayList<>();
			tempList.add(newMap1);
			tempList.add(newMap2);
			tempList.add(newMap3);
			tempList.add(map2);
			filerElementInMaps(returnList, tempList);
		}else {
			//第一区域
			int newMapMinX1 = map2MinX;
			int newMapMinY1 = map1MaxY;
			int newMapMaxX1 = map2MaxX;
			int newMapMaxY1 = map2MaxY;
			Map<String,String> newMap1 = new HashMap<>();
			//添加元素
			newMap1.put("content", map2.get("content"));
			newMap1.put("cords_x1", newMapMinX1 + "");
			newMap1.put("cords_y1", newMapMinY1 + "");
			newMap1.put("cords_x2", newMapMaxX1 + "");
			newMap1.put("cords_y2", newMapMaxY1 + "");
			List<Map<String,String>> tempList = new ArrayList<>();
			tempList.add(newMap1);
			tempList.add(map1);
			filerElementInMaps(returnList, tempList);
		}
		System.out.println("部分包含上交叉");
		return returnList;
	}
	
	//接触交叉：部分包含下交叉
	private static List<Map<String,String>> createAreaDownCross(Map<String,String> map1, Map<String,String> map2){
		List<Map<String,String>> returnList = new ArrayList<>();
		boolean b = Integer.parseInt(map1.get("cords_x2")) > Integer.parseInt(map2.get("cords_x2"));
		//始终保持map1为底元素
		if (!b) {
			Map<String,String> temp = new HashMap<>();
			temp.putAll(map1);
			map1.clear();
			map1.putAll(map2);
			map2.clear();
			map2.putAll(temp);
		}
		int map1MinX = Integer.parseInt(map1.get("cords_x1"));
		int map1MinY = Integer.parseInt(map1.get("cords_y1"));
		int map1MaxX = Integer.parseInt(map1.get("cords_x2"));
		int map1MaxY = Integer.parseInt(map1.get("cords_y2"));
		int map2MinX = Integer.parseInt(map2.get("cords_x1"));
		int map2MinY = Integer.parseInt(map2.get("cords_y1"));
		int map2MaxX = Integer.parseInt(map2.get("cords_x2"));
		int map2MaxY = Integer.parseInt(map2.get("cords_y2"));
		boolean b1 = compare2AreaWhoMoreBig(map1, map2);
		
		if (b1) {
			//第一区域
			int newMapMinX1 = map1MinX;
			int newMapMinY1 = map1MinY;
			int newMapMaxX1 = map2MinX;
			int newMapMaxY1 = map1MaxY;
			//第二区域
			int newMapMinX2 = map2MaxX;
			int newMapMinY2 = map1MinY;
			int newMapMaxX2 = map1MaxX;
			int newMapMaxY2 = map1MaxY;	
			//第三区域
			int newMapMinX3 = map2MinX;
			int newMapMinY3 = map2MaxY;
			int newMapMaxX3 = map2MaxX;
			int newMapMaxY3 = map1MaxY;
			Map<String,String> newMap1 = new HashMap<>();
			Map<String,String> newMap2 = new HashMap<>();
			Map<String,String> newMap3 = new HashMap<>();
			//添加元素
			newMap1.put("content", map1.get("content"));
			newMap1.put("cords_x1", newMapMinX1 + "");
			newMap1.put("cords_y1", newMapMinY1 + "");
			newMap1.put("cords_x2", newMapMaxX1 + "");
			newMap1.put("cords_y2", newMapMaxY1 + "");
			//添加元素
			newMap2.put("content", map1.get("content"));
			newMap2.put("cords_x1", newMapMinX2 + "");
			newMap2.put("cords_y1", newMapMinY2 + "");
			newMap2.put("cords_x2", newMapMaxX2 + "");
			newMap2.put("cords_y2", newMapMaxY2 + "");
			//添加元素
			newMap3.put("content", map1.get("content"));
			newMap3.put("cords_x1", newMapMinX3 + "");
			newMap3.put("cords_y1", newMapMinY3 + "");
			newMap3.put("cords_x2", newMapMaxX3 + "");
			newMap3.put("cords_y2", newMapMaxY3 + "");
			List<Map<String,String>> tempList = new ArrayList<>();
			tempList.add(newMap1);
			tempList.add(newMap2);
			tempList.add(newMap3);
			tempList.add(map2);
			filerElementInMaps(returnList, tempList);
		}else {
			//第一区域
			int newMapMinX1 = map2MinX;
			int newMapMinY1 = map2MinY;
			int newMapMaxX1 = map2MaxX;
			int newMapMaxY1 = map1MinY;
			Map<String,String> newMap1 = new HashMap<>();
			//添加元素
			newMap1.put("content", map2.get("content"));
			newMap1.put("cords_x1", newMapMinX1 + "");
			newMap1.put("cords_y1", newMapMinY1 + "");
			newMap1.put("cords_x2", newMapMaxX1 + "");
			newMap1.put("cords_y2", newMapMaxY1 + "");
			List<Map<String,String>> tempList = new ArrayList<>();
			tempList.add(newMap1);
			tempList.add(map1);
			filerElementInMaps(returnList, tempList);
		}
		System.out.println("部分包含下交叉");
		return returnList;
	}
	
	//接触交叉：部分包含左交叉
	private static List<Map<String,String>> createAreaLeftCross(Map<String,String> map1, Map<String,String> map2){
		List<Map<String,String>> returnList = new ArrayList<>();
		boolean b = Integer.parseInt(map1.get("cords_x2")) > Integer.parseInt(map2.get("cords_x2"));
		//始终保持map1为底元素
		if (!b) {
			Map<String,String> temp = new HashMap<>();
			temp.putAll(map1);
			map1.clear();
			map1.putAll(map2);
			map2.clear();
			map2.putAll(temp);
		}
		int map1MinX = Integer.parseInt(map1.get("cords_x1"));
		int map1MinY = Integer.parseInt(map1.get("cords_y1"));
		int map1MaxX = Integer.parseInt(map1.get("cords_x2"));
		int map1MaxY = Integer.parseInt(map1.get("cords_y2"));
		int map2MinX = Integer.parseInt(map2.get("cords_x1"));
		int map2MinY = Integer.parseInt(map2.get("cords_y1"));
		int map2MaxX = Integer.parseInt(map2.get("cords_x2"));
		int map2MaxY = Integer.parseInt(map2.get("cords_y2"));
		boolean b1 = compare2AreaWhoMoreBig(map1, map2);
		
		if (b1) {
			//第一区域
			int newMapMinX1 = map1MinX;
			int newMapMinY1 = map1MinY;
			int newMapMaxX1 = map1MaxX;
			int newMapMaxY1 = map2MinY;
			//第二区域
			int newMapMinX2 = map1MinX;
			int newMapMinY2 = map2MaxY;
			int newMapMaxX2 = map1MaxX;
			int newMapMaxY2 = map1MaxY;	
			//第三区域
			int newMapMinX3 = map2MaxX;
			int newMapMinY3 = map2MinY;
			int newMapMaxX3 = map1MaxX;
			int newMapMaxY3 = map2MaxY;
			Map<String,String> newMap1 = new HashMap<>();
			Map<String,String> newMap2 = new HashMap<>();
			Map<String,String> newMap3 = new HashMap<>();
			//添加元素
			newMap1.put("content", map1.get("content"));
			newMap1.put("cords_x1", newMapMinX1 + "");
			newMap1.put("cords_y1", newMapMinY1 + "");
			newMap1.put("cords_x2", newMapMaxX1 + "");
			newMap1.put("cords_y2", newMapMaxY1 + "");
			//添加元素
			newMap2.put("content", map1.get("content"));
			newMap2.put("cords_x1", newMapMinX2 + "");
			newMap2.put("cords_y1", newMapMinY2 + "");
			newMap2.put("cords_x2", newMapMaxX2 + "");
			newMap2.put("cords_y2", newMapMaxY2 + "");
			//添加元素
			newMap3.put("content", map1.get("content"));
			newMap3.put("cords_x1", newMapMinX3 + "");
			newMap3.put("cords_y1", newMapMinY3 + "");
			newMap3.put("cords_x2", newMapMaxX3 + "");
			newMap3.put("cords_y2", newMapMaxY3 + "");
			List<Map<String,String>> tempList = new ArrayList<>();
			tempList.add(newMap1);
			tempList.add(newMap2);
			tempList.add(newMap3);
			tempList.add(map2);
			filerElementInMaps(returnList, tempList);
		}else {
			//第一区域
			int newMapMinX1 = map2MinX;
			int newMapMinY1 = map2MinY;
			int newMapMaxX1 = map1MinX;
			int newMapMaxY1 = map2MaxY;
			Map<String,String> newMap1 = new HashMap<>();
			//添加元素
			newMap1.put("content", map2.get("content"));
			newMap1.put("cords_x1", newMapMinX1 + "");
			newMap1.put("cords_y1", newMapMinY1 + "");
			newMap1.put("cords_x2", newMapMaxX1 + "");
			newMap1.put("cords_y2", newMapMaxY1 + "");
			List<Map<String,String>> tempList = new ArrayList<>();
			tempList.add(newMap1);
			tempList.add(map1);
			filerElementInMaps(returnList, tempList);
		}
		System.out.println("部分包含左交叉");
		return returnList;
	}
	
	//接触交叉：部分包含右交叉
	private static List<Map<String,String>> createAreaRightCross(Map<String,String> map1, Map<String,String> map2){
		List<Map<String,String>> returnList = new ArrayList<>();
		boolean b = Integer.parseInt(map1.get("cords_x1")) < Integer.parseInt(map2.get("cords_x1"));
		//始终保持map1为底元素
		if (!b) {
			Map<String,String> temp = new HashMap<>();
			temp.putAll(map1);
			map1.clear();
			map1.putAll(map2);
			map2.clear();
			map2.putAll(temp);
		}
		int map1MinX = Integer.parseInt(map1.get("cords_x1"));
		int map1MinY = Integer.parseInt(map1.get("cords_y1"));
		int map1MaxX = Integer.parseInt(map1.get("cords_x2"));
		int map1MaxY = Integer.parseInt(map1.get("cords_y2"));
		int map2MinX = Integer.parseInt(map2.get("cords_x1"));
		int map2MinY = Integer.parseInt(map2.get("cords_y1"));
		int map2MaxX = Integer.parseInt(map2.get("cords_x2"));
		int map2MaxY = Integer.parseInt(map2.get("cords_y2"));
		boolean b1 = compare2AreaWhoMoreBig(map1, map2);
		
		if (b1) {
			//第一区域
			int newMapMinX1 = map1MinX;
			int newMapMinY1 = map1MinY;
			int newMapMaxX1 = map1MaxX;
			int newMapMaxY1 = map2MinY;
			//第二区域
			int newMapMinX2 = map1MinX;
			int newMapMinY2 = map2MaxY;
			int newMapMaxX2 = map1MaxX;
			int newMapMaxY2 = map1MaxY;	
			//第三区域
			int newMapMinX3 = map1MinX;
			int newMapMinY3 = map2MinY;
			int newMapMaxX3 = map2MinX;
			int newMapMaxY3 = map2MaxY;
			Map<String,String> newMap1 = new HashMap<>();
			Map<String,String> newMap2 = new HashMap<>();
			Map<String,String> newMap3 = new HashMap<>();
			//添加元素
			newMap1.put("content", map1.get("content"));
			newMap1.put("cords_x1", newMapMinX1 + "");
			newMap1.put("cords_y1", newMapMinY1 + "");
			newMap1.put("cords_x2", newMapMaxX1 + "");
			newMap1.put("cords_y2", newMapMaxY1 + "");
			//添加元素
			newMap2.put("content", map1.get("content"));
			newMap2.put("cords_x1", newMapMinX2 + "");
			newMap2.put("cords_y1", newMapMinY2 + "");
			newMap2.put("cords_x2", newMapMaxX2 + "");
			newMap2.put("cords_y2", newMapMaxY2 + "");
			//添加元素
			newMap3.put("content", map1.get("content"));
			newMap3.put("cords_x1", newMapMinX3 + "");
			newMap3.put("cords_y1", newMapMinY3 + "");
			newMap3.put("cords_x2", newMapMaxX3 + "");
			newMap3.put("cords_y2", newMapMaxY3 + "");
			List<Map<String,String>> tempList = new ArrayList<>();
			tempList.add(newMap1);
			tempList.add(newMap2);
			tempList.add(newMap3);
			tempList.add(map2);
			filerElementInMaps(returnList, tempList);
		}else {
			//第一区域
			int newMapMinX1 = map1MaxX;
			int newMapMinY1 = map2MinY;
			int newMapMaxX1 = map2MaxX;
			int newMapMaxY1 = map2MaxY;
			Map<String,String> newMap1 = new HashMap<>();
			//添加元素
			newMap1.put("content", map2.get("content"));
			newMap1.put("cords_x1", newMapMinX1 + "");
			newMap1.put("cords_y1", newMapMinY1 + "");
			newMap1.put("cords_x2", newMapMaxX1 + "");
			newMap1.put("cords_y2", newMapMaxY1 + "");
			List<Map<String,String>> tempList = new ArrayList<>();
			tempList.add(newMap1);
			tempList.add(map1);
			filerElementInMaps(returnList, tempList);
		}
		System.out.println("部分包含右交叉");
		return returnList;
	}
	
	//十字交叉
	private static List<Map<String,String>> createAreaTenCross(Map<String,String> map1, Map<String,String> map2){
		List<Map<String,String>> returnList = new ArrayList<>();
		boolean b = Integer.parseInt(map1.get("cords_y2")) > Integer.parseInt(map2.get("cords_y2"));
		//始终保持map1为竖向元素
		if (!b) {
			Map<String,String> temp = new HashMap<>();
			temp.putAll(map1);
			map1.clear();
			map1.putAll(map2);
			map2.clear();
			map2.putAll(temp);
		}
		int map1MinX = Integer.parseInt(map1.get("cords_x1"));
		int map1MinY = Integer.parseInt(map1.get("cords_y1"));
		int map1MaxX = Integer.parseInt(map1.get("cords_x2"));
		int map1MaxY = Integer.parseInt(map1.get("cords_y2"));
		int map2MinX = Integer.parseInt(map2.get("cords_x1"));
		int map2MinY = Integer.parseInt(map2.get("cords_y1"));
		int map2MaxX = Integer.parseInt(map2.get("cords_x2"));
		int map2MaxY = Integer.parseInt(map2.get("cords_y2"));
		boolean b1 = compare2AreaWhoMoreBig(map1, map2);
		
		if (b1) {
			//第一区域
			int newMapMinX1 = map1MinX;
			int newMapMinY1 = map2MaxY;
			int newMapMaxX1 = map1MaxX;
			int newMapMaxY1 = map1MaxY;
			//第二区域
			int newMapMinX2 = map1MinX;
			int newMapMinY2 = map1MinY;
			int newMapMaxX2 = map1MaxX;
			int newMapMaxY2 = map2MinY;	
			Map<String,String> newMap1 = new HashMap<>();
			Map<String,String> newMap2 = new HashMap<>();
			//添加元素
			newMap1.put("content", map1.get("content"));
			newMap1.put("cords_x1", newMapMinX1 + "");
			newMap1.put("cords_y1", newMapMinY1 + "");
			newMap1.put("cords_x2", newMapMaxX1 + "");
			newMap1.put("cords_y2", newMapMaxY1 + "");
			//添加元素
			newMap2.put("content", map1.get("content"));
			newMap2.put("cords_x1", newMapMinX2 + "");
			newMap2.put("cords_y1", newMapMinY2 + "");
			newMap2.put("cords_x2", newMapMaxX2 + "");
			newMap2.put("cords_y2", newMapMaxY2 + "");
			List<Map<String,String>> tempList = new ArrayList<>();
			tempList.add(newMap1);
			tempList.add(newMap2);
			tempList.add(map2);
			filerElementInMaps(returnList, tempList);
		}else {
			//第一区域
			int newMapMinX1 = map2MinX;
			int newMapMinY1 = map2MinY;
			int newMapMaxX1 = map1MinX;
			int newMapMaxY1 = map2MaxY;
			//第二区域
			int newMapMinX2 = map1MaxX;
			int newMapMinY2 = map2MinY;
			int newMapMaxX2 = map2MaxX;
			int newMapMaxY2 = map2MaxY;	
			Map<String,String> newMap1 = new HashMap<>();
			Map<String,String> newMap2 = new HashMap<>();
			//添加元素
			newMap1.put("content", map2.get("content"));
			newMap1.put("cords_x1", newMapMinX1 + "");
			newMap1.put("cords_y1", newMapMinY1 + "");
			newMap1.put("cords_x2", newMapMaxX1 + "");
			newMap1.put("cords_y2", newMapMaxY1 + "");
			//添加元素
			newMap2.put("content", map2.get("content"));
			newMap2.put("cords_x1", newMapMinX2 + "");
			newMap2.put("cords_y1", newMapMinY2 + "");
			newMap2.put("cords_x2", newMapMaxX2 + "");
			newMap2.put("cords_y2", newMapMaxY2 + "");
			List<Map<String,String>> tempList = new ArrayList<>();
			tempList.add(newMap1);
			tempList.add(newMap2);
			tempList.add(map1);
			filerElementInMaps(returnList, tempList);
		}
		System.out.println("十字交叉");
		return returnList;
	}
	
	//对角交叉
	private static List<Map<String,String>> createAreaOppositeCross(Map<String,String> map1, Map<String,String> map2){
		List<Map<String,String>> returnList = new ArrayList<>();
		boolean b = Integer.parseInt(map1.get("cords_x1")) < Integer.parseInt(map2.get("cords_x1"));
		//始终保持map1为左下角元素
		if (!b) {
			Map<String,String> temp = new HashMap<>();
			temp.putAll(map1);
			map1.clear();
			map1.putAll(map2);
			map2.clear();
			map2.putAll(temp);
		}
		int map1MinX = Integer.parseInt(map1.get("cords_x1"));
		int map1MinY = Integer.parseInt(map1.get("cords_y1"));
		int map1MaxX = Integer.parseInt(map1.get("cords_x2"));
		int map1MaxY = Integer.parseInt(map1.get("cords_y2"));
		int map2MinX = Integer.parseInt(map2.get("cords_x1"));
		int map2MinY = Integer.parseInt(map2.get("cords_y1"));
		int map2MaxX = Integer.parseInt(map2.get("cords_x2"));
		int map2MaxY = Integer.parseInt(map2.get("cords_y2"));
		boolean b1 = compare2AreaWhoMoreBig(map1, map2);
		
		if (b1) {
			//第一区域
			int newMapMinX1 = map1MinX;
			int newMapMinY1 = map1MinY;
			int newMapMaxX1 = map1MaxX;
			int newMapMaxY1 = map2MinY;
			//第二区域
			int newMapMinX2 = map1MinX;
			int newMapMinY2 = map2MinY;
			int newMapMaxX2 = map2MinX;
			int newMapMaxY2 = map1MaxY;	
			Map<String,String> newMap1 = new HashMap<>();
			Map<String,String> newMap2 = new HashMap<>();
			//添加元素
			newMap1.put("content", map1.get("content"));
			newMap1.put("cords_x1", newMapMinX1 + "");
			newMap1.put("cords_y1", newMapMinY1 + "");
			newMap1.put("cords_x2", newMapMaxX1 + "");
			newMap1.put("cords_y2", newMapMaxY1 + "");
			//添加元素
			newMap2.put("content", map1.get("content"));
			newMap2.put("cords_x1", newMapMinX2 + "");
			newMap2.put("cords_y1", newMapMinY2 + "");
			newMap2.put("cords_x2", newMapMaxX2 + "");
			newMap2.put("cords_y2", newMapMaxY2 + "");
			List<Map<String,String>> tempList = new ArrayList<>();
			tempList.add(newMap1);
			tempList.add(newMap2);
			tempList.add(map2);
			filerElementInMaps(returnList, tempList);
		}else {
			//第一区域
			int newMapMinX1 = map1MaxX;
			int newMapMinY1 = map2MinY;
			int newMapMaxX1 = map2MaxX;
			int newMapMaxY1 = map2MaxY;
			//第二区域
			int newMapMinX2 = map2MinX;
			int newMapMinY2 = map1MaxY;
			int newMapMaxX2 = map1MaxX;
			int newMapMaxY2 = map2MaxY;	
			Map<String,String> newMap1 = new HashMap<>();
			Map<String,String> newMap2 = new HashMap<>();
			//添加元素
			newMap1.put("content", map2.get("content"));
			newMap1.put("cords_x1", newMapMinX1 + "");
			newMap1.put("cords_y1", newMapMinY1 + "");
			newMap1.put("cords_x2", newMapMaxX1 + "");
			newMap1.put("cords_y2", newMapMaxY1 + "");
			//添加元素
			newMap2.put("content", map2.get("content"));
			newMap2.put("cords_x1", newMapMinX2 + "");
			newMap2.put("cords_y1", newMapMinY2 + "");
			newMap2.put("cords_x2", newMapMaxX2 + "");
			newMap2.put("cords_y2", newMapMaxY2 + "");
			List<Map<String,String>> tempList = new ArrayList<>();
			tempList.add(newMap1);
			tempList.add(newMap2);
			tempList.add(map1);
			filerElementInMaps(returnList, tempList);
		}
		System.out.println("对角交叉");
		return returnList;
	}
	
	//单边或者多边重合
	private static List<Map<String, String>> createAreaCoincide(Map<String, String> map1, Map<String, String> map2,
			boolean oneDown, boolean oneUp, boolean oneLeft, boolean oneRight) {
		List<Map<String,String>> returnList = new ArrayList<>();
		int map1MinX = Integer.parseInt(map1.get("cords_x1"));
		int map1MinY = Integer.parseInt(map1.get("cords_y1"));
		int map1MaxX = Integer.parseInt(map1.get("cords_x2"));
		int map1MaxY = Integer.parseInt(map1.get("cords_y2"));
		int map2MinX = Integer.parseInt(map2.get("cords_x1"));
		int map2MinY = Integer.parseInt(map2.get("cords_y1"));
		int map2MaxX = Integer.parseInt(map2.get("cords_x2"));
		int map2MaxY = Integer.parseInt(map2.get("cords_y2"));
		
		int minX = map1MinX < map2MinX ? map1MinX : map2MinX;
		int minY = map1MinY < map2MinY ? map1MinY : map2MinY;
		int maxX = map1MaxX > map2MaxX ? map1MaxX : map2MaxX;
		int maxY = map1MaxY > map1MaxY ? map1MaxY : map2MaxY;
		
		Map<String, String> temp = new HashMap<>();
		String m1Content = map1.get("content");
		String m2Content = map2.get("content");
		if (m1Content.contains(m2Content)) {
			temp.put("content", map1.get("content"));
 		}else if (m2Content.contains(m1Content)) {
 			temp.put("content", map2.get("content"));
		}else {
			temp.put("content", map1.get("content") + "。" + map2.get("content"));
		}
		temp.put("cords_x1", minX + "");
		temp.put("cords_x2", maxX + "");
		temp.put("cords_y1", minY + "");
		temp.put("cords_y2", maxY + "");
		returnList.add(temp);
		return returnList;
	}
	
	//筛选元素，判断map内坐标是否是矩形
	private static void filerElementInMaps(List<Map<String, String>> returnList, List<Map<String, String>> tempList) {
		for (Map<String, String> map : tempList) {
			boolean b1 = Integer.parseInt(map.get("cords_x1")) != Integer.parseInt(map.get("cords_x2"));
			boolean b2 = Integer.parseInt(map.get("cords_y1")) != Integer.parseInt(map.get("cords_y2"));
			if (b1 && b2) {
				returnList.add(map);
			}
		}
	}
}
