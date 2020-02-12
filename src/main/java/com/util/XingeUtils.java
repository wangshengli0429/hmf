/**   
 * Copyright © 2017 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: tuisong 
 * @author: think   
 * @date: 2017-11-1 下午3:14:28 
 */
package com.util;

import java.util.List;

import org.json.JSONObject;

import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.TimeInterval;
import com.tencent.xinge.XingeApp;

/**
 * @ClassName: XingeUtils 
 * @Description: TODO
 * @author: think
 * @date: 2017-11-1 下午3:14:28  
 */
public class XingeUtils {
	
	private static long accessId_ios = 2200269850L;
	private static String accessKey_ios = "IX4JHN72Q38P";
	private static String secretKey_ios = "92ad920d4068191275c548163cc34da2";
	
	private static long accessId_android = 2100269849L;
	private static String accessKey_android = "A2ASM523JC8B";
	private static String secretKey_android = "5f39dd1cd96162fa0beadc62555c446f";
	
	private static String account_ios = "739915548AC9E315769E59DCF2C03F1C864CFCFA";
	private static String account_android = "1CA2E20BF59AF4F9DC5E193FB3847EC30301A572";
	
	public static JSONObject pushSingleAccountIos(String account, int badge){
		XingeApp push = new XingeApp(XingeUtils.accessId_ios, XingeUtils.secretKey_ios);
		JSONObject jsonObject = push.pushSingleAccount(0, account, getMessageIOS(badge), XingeApp.IOSENV_PROD);
		return jsonObject;
	}
	
	public static JSONObject pushSingleAccountAndroid(String account){
		XingeApp push = new XingeApp(XingeUtils.accessId_android, XingeUtils.secretKey_android);
		JSONObject jsonObject = push.pushSingleAccount(0, account, getMessage());
		return jsonObject;
	}
	public static JSONObject pushAccountAndroidList(List<String> accountList, String information){
		XingeApp push = new XingeApp(XingeUtils.accessId_android, XingeUtils.secretKey_android);
		JSONObject jsonObject = push.pushAccountList(0, accountList, getMessage2(information));
		return jsonObject;
	}
	public static JSONObject pushAccountIosList(List<String> accountList, String information){
		XingeApp push = new XingeApp(XingeUtils.accessId_ios, XingeUtils.secretKey_ios);
		JSONObject jsonObject = push.pushAccountList(0, accountList, getMessageIOS2(information), XingeApp.IOSENV_PROD);
		return jsonObject;
	}
	
	private static MessageIOS getMessageIOS(int badge){
		MessageIOS message = new MessageIOS();
		message.setExpireTime(86400);
		JSONObject js = new JSONObject();
		js.put("body", "您收到新的点评邀请，请在消息列表中查看 ");
		js.put("title", "新消息提醒 ");
		//js.put("launch-image", "icon.icon");
		//js.put("subtitle", "副标题");
        message.setAlert(js);
        TimeInterval acceptTime = new TimeInterval(7, 0, 21, 59);
        message.addAcceptTime(acceptTime);
        message.setBadge(badge);
        message.setSound("beep.wav");
        //message.setCategory("sssssssssss");
       /* Map<String, Object> custom = new HashMap<String, Object>();
        custom.put("新消息提醒1", "您收到新的点评邀请，请在消息列表中查看1");
        custom.put("新消息提醒2", "您收到新的点评邀请，请在消息列表中查看2");
        custom.put("新消息提醒3", "您收到新的点评邀请，请在消息列表中查看3");
        message.setCustom(custom);*/
        return message;
	}
	private static MessageIOS getMessageIOS2(String information){
		MessageIOS message = new MessageIOS();
		message.setExpireTime(86400);
		JSONObject js = new JSONObject();
		js.put("body", "系统通知：" + information);
		js.put("title", "新消息提醒 ");
		//js.put("launch-image", "icon.icon");
		//js.put("subtitle", "副标题");
		message.setAlert(js);
		TimeInterval acceptTime = new TimeInterval(7, 0, 21, 59);
		message.addAcceptTime(acceptTime);
		message.setBadge(1);
		message.setSound("beep.wav");
		//message.setCategory("sssssssssss");
		/* Map<String, Object> custom = new HashMap<String, Object>();
        custom.put("新消息提醒1", "您收到新的点评邀请，请在消息列表中查看1");
        custom.put("新消息提醒2", "您收到新的点评邀请，请在消息列表中查看2");
        custom.put("新消息提醒3", "您收到新的点评邀请，请在消息列表中查看3");
        message.setCustom(custom);*/
		return message;
	}
	private static Message getMessage(){
		Message message = new Message();
		message.setTitle("新消息提醒");
	    message.setContent("您收到新的点评邀请，请在消息列表中查看");
	    //TYPE_MESSAGE透传消息  TYPE_NOTIFICATION通知
	    message.setType(Message.TYPE_NOTIFICATION);
	    message.setExpireTime(86400);
	    TimeInterval acceptTime = new TimeInterval(7, 0, 21, 59);
        message.addAcceptTime(acceptTime);
        ClickAction action = new ClickAction();
        action.setActionType(ClickAction.TYPE_ACTIVITY);
        action.setActivity("");
        message.setAction(action);
        return message;
	}
	private static Message getMessage2(String information){
		Message message = new Message();
		message.setTitle("新消息提醒");
		message.setContent("系统通知：" + information);
		//TYPE_MESSAGE透传消息  TYPE_NOTIFICATION通知
		message.setType(Message.TYPE_NOTIFICATION);
		message.setExpireTime(86400);
		TimeInterval acceptTime = new TimeInterval(7, 0, 21, 59);
		message.addAcceptTime(acceptTime);
		ClickAction action = new ClickAction();
		action.setActionType(ClickAction.TYPE_ACTIVITY);
		action.setActivity("");
		message.setAction(action);
		return message;
	}
	public static void main(String[] args) {
		//JSONObject jsonObject = pushSingleAccountAndroid(account_android);
		//System.out.println(jsonObject);
		
		
	}
}
