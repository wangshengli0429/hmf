package com.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;

import com.rest.service.impl.StudentServiceImpl;

public class FileUpload {
	
	private static Logger logger = Logger.getLogger(StudentServiceImpl.class);

	/**
	 * @param args
	 */
	// 上传图片
	public String uploadContent(File file, String imagePath, String fileName) {
		String rootPath = "F:/server/cedu-files/files/pingdianapp_img/";
		// 如果目录不存在则创建
		File uploadDir = new File(rootPath + imagePath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		System.out.println("------------图片大小" + file.length());
		// 限制图片大小
		if (file.length() > 5242880) {
			return null;
		}
		// System.out.println(file.length());
		String fname = getExtention(fileName);
		try {
			if (fname.equals(".jpg") || fname.equals(".jpeg") || fname.equals(".png")) {
				FileOutputStream fos;
				fos = new FileOutputStream(rootPath + imagePath + fileName);
				FileInputStream fis = new FileInputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = fis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fis.close();
				fos.flush();
				fos.close();
				System.out.println("----------------imagePath:" + imagePath + " , ---------------fileName:" + fileName);
				return "/files/pingdianapp_img/" + imagePath + fileName;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 获取文件类型
	private String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos);
	}

	// 测试上传图
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// FileUpload fu=new FileUpload();
		// File f=new File("G:/image/max.jpg");
		// try {
		// String a=fu.uploadContent(f, "WebRoot/Upload", "max.jpg");
		// System.out.println(a);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		String u = Class.class.getClass().getResource("/").getPath();
		int num = u.indexOf("pingdian");
		String str = u.substring(0, num + "pingdian".length());
		System.out.println(str);
	}

}
