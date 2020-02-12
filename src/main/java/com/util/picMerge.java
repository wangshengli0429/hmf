package com.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.ImageFormatException;

/**
 * 两张图片合并输出
 * @author think
 *
 */
public class picMerge {
	public static List<String> merger2pic(InputStream in, String path) throws Exception{
		BufferedImage image1 = loadImageLocal(path);//原图片
		BufferedImage image2 = loadImageInput(in);//添加批注图片
		//获得两张图片合并后的输出流
		BufferedImage result = mergeImage(image1, image2);
		//获取原图片像素点大小，用于创建热区时使用
		int w1 = image1.getWidth();
		int h1 = image1.getHeight();
		
		Calendar a = Calendar.getInstance();
		int year = a.get(Calendar.YEAR);
		int month = a.get(Calendar.MONTH) + 1;
		String rootPath = CommUtils.imgRootPath + "/com_composition/" + year + "/" + month + "/";
		File rootFolder = new File(rootPath);
		if (!rootFolder.exists()) {
			rootFolder.mkdirs();
		}
		File file2 = new File(rootPath);
		if (!file2.exists()) {
			file2.mkdir();
		}
		String imgName = new Date().getTime() + ".jpg";
		
		File file = new File(rootPath + imgName); 
		boolean write = ImageIO.write(result, "png", file); 
		if (write) {
			List<String> list = new ArrayList<>();
			list.add((rootPath + imgName).substring(20));
			list.add(w1+"");
			list.add(h1+"");
			return list;
		}else {
			return null;
		}
	}
	/**  
     * 导入本地图片到缓冲区  
     */  
    private static BufferedImage loadImageLocal(String path) throws IOException { 
    	return ImageIO.read(new File(path));  
    }  
  
    /**  
     * 导入流图片到缓冲区  
     */  
    private static BufferedImage loadImageInput(InputStream in) throws ImageFormatException, IOException {  
    	return ImageIO.read(in);
    }
    /**
     * 合并两张图片
     */
    private static BufferedImage mergeImage(BufferedImage img1, BufferedImage img2) throws IOException { 
    	
    	Graphics2D g2d = img1.createGraphics();
    	int w1 = img1.getWidth(); 
        int h1 = img1.getHeight(); 
        float alpha = (float) 1.0;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha ));
    	g2d.drawImage(img2, 0, 0, w1, h1, null);
    	g2d.dispose();
    	return img1;
    	
    } 
}
