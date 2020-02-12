package com.util.QrCodeUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document; 

public class word2Html {
	public static void main(String[] args) throws Exception {
		beginTurn("F:\\word", "oldHtml", "newHtml");
		buildQRCode("F:\\word", "newHtml", "qr");
	}
	
	/**
	 * 生成二维码
	 */
	public static void buildQRCode(String path, String newOut, String orOut) throws Exception{
		Path p = Paths.get(path, newOut);
		File file = p.toFile();
		File[] files = file.listFiles();
		for (File file2 : files) {
			String serverHost = "https://api.pingdianedu.com:443/download/files/erweima/";
			String fileName = file2.getName();
			String imageName = file2.getName().replace("html", "png");
			File file3 = new File(path, orOut);
			file3.mkdir();
			Path p2 = Paths.get(path, orOut, imageName);
			File orFile = p2.toFile();
			QrCode qr0 = QrCode.encodeText(serverHost + fileName, QrCode.Ecc.MEDIUM);
			BufferedImage img = qr0.toImage(4, 10);
			ImageIO.write(img, "png", orFile);
		}
	}
	
	/**
	 * 执行，oldOut为word源文件转html后输出的文件夹，newOut为刷选过的输出文件夹
	 */
	public static void beginTurn(String path, String oldOut, String newOut) throws Exception{
		//第一步
		batchFilesDoc(path, oldOut);
		//第二步
		batchFilesHtml(path, oldOut, newOut);
	}
	
	
	
	/**
	 * 第二步
	 * 对转化后的html进行筛选，改变样式，输出在newOut文件夹
	 */
	//输出为html
	public static void outHtml(String path, String htmlStr, String out, String fileName) throws Exception{
		Path p1 = Paths.get(path, out);
		File file1 = p1.toFile();
		file1.mkdir();
		Path p2 = Paths.get(path, out, fileName);
		File file2 = p2.toFile();
		file2.createNewFile();
		Files.write(p2, htmlStr.getBytes(StandardCharsets.UTF_8));
	}
	
	//遍历文件夹
	public static void batchFilesHtml(String path, String oldPath, String out) throws Exception{
		File file = new File(path, oldPath);
		File[] files = file.listFiles();
		for (File file2 : files) {
			readFile2String(file2, path, out);
		}
	}
	
	//提取需要内容（蓝色字体）输出为string
	public static void readFile2String(File input, String path, String out) throws Exception{
		org.jsoup.nodes.Document doc = Jsoup.parse(input, "UTF-8", "http://www.oschina.net/");
		Elements as = doc.select("a");
		//删除超链接
		for (Element element : as) {
			element.removeAttr("href");
		}
		//删除蓝色字体以外的文字
		List<String> css = getBlueCss(doc);
		Elements ps = doc.select("p");
		loop1:for (Element element : ps) {
			Elements spans = element.select("span");
			for (Element element2 : spans) {
				String classname = element2.className();
				if (!css.contains(classname)) {
					element.remove();
					continue loop1;
				}
			}
		}
		//css样式转化，蓝色字体转为黑色
		String htmlStr = doc.outerHtml().replace("color:blue", "color:black");
		outHtml(path, htmlStr, out, input.getName());
	}

	//获得蓝色字体的css
	public static List<String> getBlueCss(org.jsoup.nodes.Document doc){
		String data = doc.data();
		List<String> cssList = new ArrayList<>();
		int start = 0;
		int i;
		while ((i = data.indexOf("color:blue", start)) != -1) {
			int j = data.lastIndexOf("{", i);
			int k = data.lastIndexOf(".", j);
			cssList.add(data.substring(k + 1, j));
			start = i + 10;
		}
		return cssList;
	}
	
	/**
	 * 第一步
	 * 读取word源文件，转为html，样式不变，输出在oldOut文件夹
	 */
	//遍历文件夹
	public static void batchFilesDoc(String path, String out) throws Exception{
		File file = new File(path);
		String[] sts = file.list();
		for (String string : sts) {
			if (string.contains("doc")) {
				Word2003ToHtml(path + "/" +string, out);
			}
		}
	}
	
	//word2003轉html
	public static void Word2003ToHtml(String filepath, String out) throws Exception{
		Path path = Paths.get(filepath);
		String fileName = path.getFileName().toString();
		String parentPath = path.getParent().toString();
		String outPath = parentPath + "/" + out + "/";
		File file = new File(outPath);
		file.mkdir();
		String htmlName = outPath + fileName.replace("doc", "html");
		InputStream input = new FileInputStream(new File(filepath));
		HWPFDocument wordDocument = new HWPFDocument(input);
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
		/*//设置图片存放的位置
		wordToHtmlConverter.setPicturesManager(new PicturesManager() {
			@Override
			public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
				File imgPath = new File(imagepath);
				if(!imgPath.exists()){//图片目录不存在则创建
					imgPath.mkdirs();
				}
				File file = new File(imagepath + suggestedName);
				try {
					OutputStream os = new FileOutputStream(file);
					os.write(content);
					os.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return imagepath + suggestedName;
			}
		});*/
 
		//解析word文档
		wordToHtmlConverter.processDocument(wordDocument);
		Document htmlDocument = wordToHtmlConverter.getDocument();
 
		File htmlFile = new File(htmlName);
		OutputStream outStream = new FileOutputStream(htmlFile);
 
		//也可以使用字符数组流获取解析的内容
		//ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		//OutputStream outStream = new BufferedOutputStream(baos);
 
		DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);
 
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer serializer = factory.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
         
        serializer.transform(domSource, streamResult);
 
        input.close();
        outStream.close();
	}
}

