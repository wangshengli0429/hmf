/**   
 * Copyright © 2017 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.rest.service 
 * @author: think   
 * @date: 2017-11-20 下午4:46:16 
 */
package com.rest.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @ClassName: TomcatListener 
 * @Description: TODO
 * @author: think
 * @date: 2017-11-20 下午4:46:16  
 */
public class TomcatListener implements ServletContextListener{
	//存储token
	public static List<String> sessionList = new ArrayList<>();
	//后台jdbc注入
	public static JdbcTemplate jt = null;
	//文件服务器地址
	public static String serverHost = null;
	//微信回调地址
	public static String wxPayNotifyUrl = null;
	
	private static Logger logger = Logger.getLogger(TomcatListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("服务器关闭");
		String filePath = null;
		String path = System.getProperty("catalina.home");
		if (path.contains("pingdiantest")) {
			filePath ="F:\\server\\tokentest.ser";
		}else {
			filePath ="F:\\server\\token.ser";
		}
		OutputStreamWriter writer = null;
		try {
			//先清空上一次记录
			File file = new File(filePath);
			file.delete();
			File newFile = new File(filePath);
			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			//存入新的数据
			writer = new OutputStreamWriter(new FileOutputStream(filePath, true));
			for (String string : sessionList) {
				writer.write(string);
				writer.write("/");
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.info("---------------------服务器启动，token输出流未正常关闭----------------------------");
				}
			}
		}
	}
	@Override
	public void contextInitialized(ServletContextEvent config) {
		/*启动时需要先配置一下参数：
			1.文件服务器地址
			2.微信支付回调地址
			3.读取token文件
			4.设置读取数据库
			5.是否开启定时任务
		*/
		System.out.println("服务器开启");
		String filePath = null;
		String path = System.getProperty("catalina.home");
		if (path.contains("XiaoChengXu")) {
			serverHost = "https://api.pingdianedu.com:443/download";
			wxPayNotifyUrl = "https://api.pingdianedu.com:443/pingdian/rest/payService/wxNotify";
			filePath ="F:\\server\\token.ser";
			System.setProperty("env", "db");
			System.setProperty("scheduler", "scheduler");
		}else if (path.contains("pingdiantest")) {
			serverHost = "http://123.56.186.12:8101";
			wxPayNotifyUrl = "http://mobile.pingdianedu.com:8088/pingdian/rest/payService/wxNotify";
			filePath ="F:\\server\\tokentest.ser";
			System.setProperty("env", "dbtest");
			System.setProperty("scheduler", "scheduler2");
		}else {
			serverHost = "http://123.56.186.12:8101";
			wxPayNotifyUrl = "https://mobile.pingdianedu.com:8443/pingdian/rest/payService/wxNotify";
			filePath ="F:\\server\\token.ser";
			System.setProperty("env", "db");
			System.setProperty("scheduler", "scheduler2");
		}
		//获取jdbc
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		jt = (JdbcTemplate) ctx.getBean("jdbcTemplate");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			String string = reader.readLine();
			String[] sts = string.split("/");
			for (String string2 : sts) {
				if (!sessionList.contains(string2)) {
					sessionList.add(string2);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.info("---------------------服务启动，token输入流未正常关闭----------------------------");
				}
			}
		}
	}
	/*@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("服务器关闭");
		FileOutputStream fs = null;
		OutputStreamWriter writer = null;
		try {
			//先清空上一次记录
			File file = new File(filePath);
			file.delete();
			File newFile = new File(filePath);
			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			//存入新的数据
			fs = new FileOutputStream(filePath, true);
			writer = new OutputStreamWriter(fs);
			for (String string : sessionList) {
				writer.write(string);
				writer.write("/");
			}
			System.out.println(sessionList.size());
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.info("---------------------服务器启动，writer token输出流未正常关闭----------------------------");
				}
			}
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					logger.info("---------------------服务器启动，fs token输出流未正常关闭----------------------------");
				}
			}
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("服务器开启");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			List<String> newSessionList = new ArrayList<>();
			String string = reader.readLine();
			String[] sts = string.split("/"); 
			for (String string2 : sts) {
				sessionList.add(string2);
			}
			System.out.println(sessionList.size()); 
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.info("---------------------服务启动，token输入流未正常关闭----------------------------");
				}
			}
		}
	}*/
}
