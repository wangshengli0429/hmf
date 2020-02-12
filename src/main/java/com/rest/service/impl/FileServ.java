package com.rest.service.impl;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import com.pc.teacher.servlet.TeacherFindServlet;
import org.apache.log4j.Logger;

/**
 * 文件管理类 分以下几个主文件夹： images（存放图片）、,对应表T_RES_IMAGE medias（存放音视频）、,对应表T_RES_MEDIA
 * docs（存放文档格式：图书、word、pdf）,对应表T_RES_DOC portraits 存放头像,对应表T_RES_PORTRAIT
 * otherimg 存放其它图像的文件夹,用于存储广告、活动等图片,对应表T_RES_OTHERIMG
 * 每个文件夹下，将按照存放文件时的月份生成文件夹，如"201511"
 *
 */
public class FileServ {
	// 存放头像的文件夹名称,对应表T_RES_PORTRAIT
	public static final String PATH_PORTRAIT = "portraits";
	// 存放其它图像的文件夹名称,用于存储广告、活动等图片,对应表T_RES_OTHERIMG
	public static final String PATH_OTHERIMG = "otherimg";
	// 存放附件,对应T_JXFZ_BKZY_AFFIX表
	public static final String PATH_AFFIX = "affixs";

	// 以下三个文件夹用于存放系统中的实际业务资源（图书、音视频、资料等）
	// 存放图片的文件夹名称,对应表T_RES_IMAGE
	public static final String PATH_IMG = "images";
	// 存放音视频的文件夹名称,对应表T_RES_MEDIA
	public static final String PATH_MEDIA = "medias";
	// 存放文档格式（图书、word、pdf）的文件夹名称,对应表T_RES_DOC
	public static final String PATH_DOC = "docs";

	private static Logger logger = Logger.getLogger(TeacherFindServlet.class);
	// 文件类型与文件路径的映射,详见setSupportExts()函数
	private Map<String, String> mapExtToPath = null;

	// 上传文件时的路径，服务器上的绝对路径
	private static String uploadUrl;

	// 下载或查看文件时的路径，Apache服务的地址
	private static String downloadUrl;

	public static String getUploadUrl() {
		return uploadUrl;
	}

	public static void setUploadUrl(String uploadUrl) {
		FileServ.uploadUrl = uploadUrl;
	}

	public static String getDownloadUrl() {
		return downloadUrl;
	}

	public static void setDownloadUrl(String downloadUrl) {
		FileServ.downloadUrl = downloadUrl;
	}

	public FileServ() {
		setExtToPathMap();
	}

	public void setExtToPathMap() {
		mapExtToPath = new HashMap<String, String>();

		// 设置图片类型
		mapExtToPath.put("jpeg", PATH_IMG);
		mapExtToPath.put("jpg", PATH_IMG);
		mapExtToPath.put("bmp", PATH_IMG);
		mapExtToPath.put("png", PATH_IMG);
		// tif格式图片
		mapExtToPath.put("tif", PATH_IMG);
		// gif格式图片
		mapExtToPath.put("gif", PATH_IMG);
		// 设置媒体类型
		mapExtToPath.put("mp4", PATH_MEDIA);
		mapExtToPath.put("mp3", PATH_MEDIA);
		mapExtToPath.put("ogg", PATH_MEDIA);
		mapExtToPath.put("webm", PATH_MEDIA);
		// mapExtToPath.put("swf",PATH_MEDIA);

		// 设置文档类型
		mapExtToPath.put("doc", PATH_DOC);
		mapExtToPath.put("docx", PATH_DOC);
		mapExtToPath.put("pdf", PATH_DOC);
		mapExtToPath.put("txt", PATH_DOC);
		mapExtToPath.put("ppt", PATH_DOC);
	}

	// 获取文件路径，返回PATH_IMG、PATH_MEDIA、PATH_DOC三种
	public String getFilePathByExt(String ext) {
		if (mapExtToPath == null) {
			logger.error("201511031443-FileServ未被有效初始化");
			return "";
		}
		return mapExtToPath.get(ext.trim().toLowerCase());
	}

	/**
	 * 向文件服务器上传文件
	 * 
	 * @param filename
	 *            文件名称
	 * @param fileExt
	 *            文件后缀名
	 * @param filebuffer
	 *            文件内容
	 * @param path
	 *            文件保存路径，仅可使用被类中定义的PATH常量
	 * @return 上传成功返回保存后的文件路径（上传文件时将为文件生成一个GUID式的文件名称），失败返回空
	 */
	public String uploadFile(String filename, String fileExt, byte filebuffer[], String path) {
		if (path == null || "".equals(path) || filebuffer == null) {
			return "";
		}

		// 设置savepath
		if (path == null || "".equals(path)) {
			path = getFilePathByExt(fileExt);
		} else if (path.equals("affixs")) { // 路径构建特殊处理
			File filekind = new File(getUploadRealPath(path));
			if (!filekind.exists() && !filekind.isDirectory()) {
				filekind.mkdir();
			}
			String extpath = getFilePathByExt(fileExt);
			path = path + "/" + extpath;
		}
		String newpath = generateNewPath(filename, path);
		if (newpath == null || "".equals(newpath)) {
			return "";
		}

		try {
			FileOutputStream out = new FileOutputStream(getUploadRealPath(newpath));
			out.write(filebuffer);
			out.close();
		} catch (Exception e) {
			logger.error("2016031510180-上传文件失败：" + e.getMessage());
			return "";
		}

		return newpath;
	}

	// 删除文件
	public boolean deleteFile(String path) {
		try {
			File file = new File(getUploadRealPath(path));
			if (!file.exists()) {
				return true;
			}

			if (file.isDirectory()) {
				return deleteDir(file);
			} else {
				return file.delete();
			}
		} catch (Exception e) {
			logger.error("2016031510181-删除文件失败:" + path);
			return false;
		}
	}

	// 删除文件夹及其下属所有内容
	private boolean deleteDir(File f) {
		if (f.isDirectory()) {
			String[] children = f.list();
			for (int i = 0; i < children.length; i++) {
				deleteDir(new File(f, children[i]));
			}
		}
		return f.delete();
	}

	/*
	 * 生成文件路径，上传文件时将为文件生成一个GUID式的文件名称，同时，根据当前月份为其生成一个文件夹 20151120改为到日期按年/月/日创建目录
	 */
	private String generateNewPath(String filename, String savepath) {
		if (savepath == null || "".equals(savepath)) {
			return "";
		}
		// 判断分类目录是否存在，不存在创建20151118
		File filekind = new File(getUploadRealPath(savepath));
		if (!filekind.exists() && !filekind.isDirectory()) {
			filekind.mkdir();
		}
		// 生成文件夹名称
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		String folderyear = savepath + "/" + String.valueOf(year);
		// 判断上传文件的保存目录是否存在
		File fileyear = new File(getUploadRealPath(folderyear));
		if (!fileyear.exists() && !fileyear.isDirectory()) {
			fileyear.mkdir();
		}
		String foldermonth = savepath + "/" + String.valueOf(year) + "/" + String.format("%02d", month);
		// 判断上传文件的保存目录是否存在
		File filemonth = new File(getUploadRealPath(foldermonth));
		if (!filemonth.exists() && !filemonth.isDirectory()) {
			filemonth.mkdir();
		}
		String folderday = savepath + "/" + String.valueOf(year) + "/" + String.format("%02d", month) + "/" + String.valueOf(day);
		// 判断上传文件的保存目录是否存在
		File fileday = new File(getUploadRealPath(folderday));
		if (!fileday.exists() && !fileday.isDirectory()) {
			fileday.mkdir();
		}
		// 获取文件后缀名
		String ext = "";
		int lastindex = filename.lastIndexOf(".");
		if (lastindex > -1) {
			ext = filename.substring(lastindex);
		}

		// 生成文件名称
		String newname = UUID.randomUUID().toString().replace("-", "");

		return folderday + "/" + newname + ext.toLowerCase();
	}

	// 获取文件上传的绝对路径
	private String getUploadRealPath(String path) {
		return uploadUrl + "/" + path;
	}

	// 获取文件下载的绝对路径
	private String getDownlodRealPath(String path) {
		return downloadUrl + "/" + path;
	}

	// 获取swf文件绝对路径
	String getSwfRealpath(String path) {
		if (downloadUrl.endsWith("/")) {
			return downloadUrl + path;
		} else {
			return downloadUrl + "/" + path;
		}
	}

	/**
	 * 增加配置信息
	 * 
	 */
	// OppenOffice 安装目录，如：D:\\sdjxdwork\\oppenoffice
	private static String openOffice_HOME;
	private static String openOffice_IP;
	private static int openOffice_PORT;
	// swftools安装目录
	private static String swfTools_HOME;

	public static String getOpenOffice_HOME() {
		return openOffice_HOME;
	}

	public static void setOpenOffice_HOME(String openOffice_HOME) {
		FileServ.openOffice_HOME = openOffice_HOME;
	}

	public static String getOpenOffice_IP() {
		return openOffice_IP;
	}

	public static void setOpenOffice_IP(String openOffice_IP) {
		FileServ.openOffice_IP = openOffice_IP;
	}

	public static int getOpenOffice_PORT() {
		return openOffice_PORT;
	}

	public static void setOpenOffice_PORT(int openOffice_PORT) {
		FileServ.openOffice_PORT = openOffice_PORT;
	}

	public static String getSwfTools_HOME() {
		return swfTools_HOME;
	}

	public static void setSwfTools_HOME(String swfTools_HOME) {
		FileServ.swfTools_HOME = swfTools_HOME;
	}

	/**
	 * copy txt文件转为odt文件 20151225
	 */
	public String copyFile(String sourceFile, String uploadUrl) throws IOException {

		String filetxt = uploadUrl + "/" + sourceFile;
		String sourceFileexceptExt = null;
		String retpath = null;
		int index = sourceFile.lastIndexOf('.');
		if (0 < index && index < sourceFile.length() - 1) {
			// 获取扩展名外的文件相对路径
			sourceFileexceptExt = sourceFile.substring(0, index);
		}
		String fileodt = uploadUrl + "/" + sourceFileexceptExt + ".odt";
		FileInputStream fis = new FileInputStream(filetxt);
		FileOutputStream fos = new FileOutputStream(fileodt);
		int temp;
		while ((temp = fis.read()) != -1) {
			fos.write(temp);
		}
		fis.close();
		fos.close();
		File file = new File(fileodt);
		if (file.exists()) {
			retpath = sourceFileexceptExt + ".odt"; // 用于返回的相对路径
		}
		return retpath;
	}

	// 查看oppenoffice进程是否就开启
	public static boolean isoppenexe() {
		boolean flag = false;
		try {
			Process p = Runtime.getRuntime().exec("cmd /c tasklist ");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream os = p.getInputStream();
			byte b[] = new byte[256];
			while (os.read(b) > 0)
				baos.write(b);
			String s = baos.toString(); //
			if (s.indexOf("soffice.exe ") >= 0) {
				System.out.println("oppenoffice进程已经开启，开始创建连接");
				flag = true;
			} else {
				System.out.println("oppenoffice进程未开启，启动进程");
				flag = false;
			}
		} catch (java.io.IOException ioe) {

		}
		return flag;
	}

	/**
	 * 将PDF文档转换为swf格式的FLASH文件. 运行该函数需要用到SWFTools, 下载地址为
	 * http://www.swftools.org/download.html
	 * 
	 * <pre>
	 * 示例:
	 * String sourcePath = "F:\\PDF\\source.pdf";
	 * String destFile = "F:\\SWF\\dest.swf";
	 * try {
	 * 	Converter.pdf2SWF(sourcePath, destFile);
	 * } catch (IOException e) {
	 * 	e.printStackTrace();
	 * }
	 * </pre>
	 * 
	 * @param sourceFile
	 *            源文件(即PDF文档)路径, 包括源文件的文件名. 示例: D:\\PDF\\source.pdf
	 * @param destFile
	 *            目标文件路径, 即需要保存的文件路径(包括文件名). 示例: D:\\SWF\\dest.swf
	 * @return 操作成功与否的提示信息. 如果返回 -1, 表示找不到源PDF文件, 或配置文件url.properties配置错误; 如果返回 0,
	 *         则表示操作成功; 返回1或其他, 则表示转换失败
	 */

	/**
	 * 修改说明：改变参数定义： sourceFile 为pdf格式的文件存储路径，如doc/... destFile 为文件的硬盘路径
	 * destFile/sourceFile 为文件的硬盘绝对路径
	 */
	public static String toSWF(String sourceFile, String destFile) {

		// // 目标路径不存在则建立目标路径
		// File dest = new File(destFile);
		// if (!dest.getParentFile().exists())
		// dest.getParentFile().mkdirs();
		String outFilePath = null;
		String sourceFileexceptExt = null;
		String retpath = null;
		// 源文件不存在则返回 -1
		String sourcepath = destFile + "/" + sourceFile; //
		int index = sourceFile.lastIndexOf('.');
		if (0 < index && index < sourceFile.length() - 1) {
			// 获取扩展名外的文件相对路径
			sourceFileexceptExt = sourceFile.substring(0, index);
			outFilePath = destFile + "/" + sourceFileexceptExt + ".swf";
			retpath = sourceFileexceptExt + ".swf"; // 用于返回的相对路径
		}
		// sourcepath =sourcepath.replaceAll("/", "\\"); //
		File source = new File(sourcepath);
		if (!source.exists())
			return null;

		// 转换后pdf文件是否存在
		File outputFile = new File(outFilePath);
		if (outputFile.exists()) {
			return retpath;// 找不到源文件, 则返回-1
		}
		// SWFTools的安装路径。在我的项目中，我为了便于拓展接口，没有直接将SWFTools的安装路径写在这里，详见附件
		// 待修改为读取配置
		// String SWFTools_HOME = "D:\\sdjxdwork\\swfserv";
		// SWFTools_HOME
		String SWFTools_HOME = swfTools_HOME;
		// 如果从文件中读取的URL地址最后一个字符不是 '\'，则添加'\'
		try {
			// 调用pdf2swf命令进行转换swfextract -i - sourceFilePath.pdf -o destFilePath.swf
			// String command = SWFTools_HOME + "\\pdf2swf.exe -i " + sourceFile + " -o "
			// + destFile;

			String command = SWFTools_HOME + "\\pdf2swf.exe  -i " + sourcepath + " -s flashversion=9 -p 1 -o " + outFilePath;
			Process pro = Runtime.getRuntime().exec(command);

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			while (bufferedReader.readLine() != null) {

			}
			pro.waitFor();
			// return pro.exitValue();
			return retpath;
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("20151118InterruptedException--PDF文件转换swf异常：" + e.getMessage());
			return null;
		} catch (IOException eio) {
			eio.printStackTrace();
			logger.error("20151118IOException--PDF文件转换swf异常：" + eio.getMessage());
			return null;
		}
	}

	// 获取当前日期 年月日 时分秒
	public static String getSysdate() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		int ms = c.get(Calendar.SECOND);
		return year + "" + month + "" + day + "" + hour + "" + min + "" + ms;

	}

	// 静态代码块处理com.sun.media.jai.disableMediaLib 异常
	static {
		System.setProperty("com.sun.media.jai.disableMediaLib", "true");
	}

	/*
	 * 图片压缩方法 图片,使用这个方法 4MB左右压缩完，约300k左右，比较清晰。
	 */
	public static String compressPic(String srcFilePath, String descFilePath) {

		String outputsourceFile = null;
		String outFilePath = null;
		int index = srcFilePath.lastIndexOf('.');
		if (0 < index && index < srcFilePath.length() - 1) {

			outputsourceFile = srcFilePath.substring(0, index) + "zip.jpg";
			outFilePath = descFilePath + "/" + outputsourceFile;
			srcFilePath = descFilePath + "/" + srcFilePath;
		}

		File file = null;
		BufferedImage src = null;
		FileOutputStream out = null;
		ImageWriter imgWrier;
		ImageWriteParam imgWriteParams;

		// 指定写图片的方式为 jpg
		imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
		imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);
		// 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
		imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
		// 这里指定压缩的程度，参数qality是取值0~1范围内，
		imgWriteParams.setCompressionQuality((float) 0.1);
		imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
		ColorModel colorModel = ColorModel.getRGBdefault();
		// 指定压缩时使用的色彩模式
		imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));

		try {
			file = new File(srcFilePath);
			src = ImageIO.read(file);
			out = new FileOutputStream(outFilePath);
			imgWrier.reset();
			// 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何 OutputStream构造
			imgWrier.setOutput(ImageIO.createImageOutputStream(out));
			// 调用write方法，就可以向输入流写图片
			imgWrier.write(null, new IIOImage(src, null, null), imgWriteParams);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return outputsourceFile;
	}

	/*
	 * 接触pdf转化图片映射关系 否则pdf文件无法删除
	 **/
	public static <T> void unmap(final Object buffer) {
		AccessController.doPrivileged(new PrivilegedAction<T>() {
			@Override
			public T run() {
				try {
					Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
					getCleanerMethod.setAccessible(true);
					sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
					cleaner.clean();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

	/** 增加文件上传大小配置 **/
	private static String portraits;
	private static String otherimg;
	private static String docs;
	private static String medias;

	public static String getPortraits() {
		return portraits;
	}

	public static void setPortraits(String portraits) {
		FileServ.portraits = portraits;
	}

	public static String getOtherimg() {
		return otherimg;
	}

	public static void setOtherimg(String otherimg) {
		FileServ.otherimg = otherimg;
	}

	public static String getDocs() {
		return docs;
	}

	public static void setDocs(String docs) {
		FileServ.docs = docs;
	}

	public static String getMedias() {
		return medias;
	}

	public static void setMedias(String medias) {
		FileServ.medias = medias;
	}

}
