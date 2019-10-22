package com.hmf.web.upload.common;

import com.hmf.web.utils.ApiResult;
import com.hmf.web.utils.enums.HttpCodeEnum;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Description 操作文件工具类
 * @author wangshengli
 * @Date 2019年1月7日
 * @Version v1.0
 */
public final class OperationFileUtil {

    private static Logger logger = LoggerFactory.getLogger(OperationFileUtil.class);

    private static final String ENCODING = "utf-8";

    /**
     * 文件下载
     *
     * @param filePath
     *            文件路径
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static ApiResult download(String filePath) throws UnsupportedEncodingException, IOException {
        String fileName = FilenameUtils.getName(filePath);
        return downloadAssist(filePath, fileName);
    }

    /**
     * 文件下载
     *
     * @param filePath
     *            文件路径
     * @param fileName
     *            文件名
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static ApiResult download(String filePath, String fileName) throws UnsupportedEncodingException, IOException {
        return downloadAssist(filePath, fileName);
    }

    /**
     * 文件下载辅助
     *
     * @param filePath
     *            文件路径
     * @param fileName
     *            文件名
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private static ApiResult downloadAssist(String filePath, String fileName) throws UnsupportedEncodingException, IOException {
        File file = new File(filePath);
        if (!file.isFile() || !file.exists()) {
            throw new IllegalArgumentException("filePath 参数必须是真实存在的文件路径:" + filePath);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName, ENCODING));
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
        if(responseEntity.getStatusCode().equals(HttpStatus.CREATED)){
            ApiResult apiResult = new ApiResult(HttpCodeEnum.CODE_0000.getCode(), HttpCodeEnum.CODE_0000.getMessage());
            apiResult.setData(responseEntity);
            return apiResult;
        }

       return new ApiResult(HttpCodeEnum.FILE_UPLOAD_CODE_9002.getCode(), HttpCodeEnum.FILE_UPLOAD_CODE_9002.getMessage());
    }

    /**
     * 多文件上传
     *
     * @param request
     *            当前上传的请求
     * @param basePath
     *            保存文件的路径
     * @throws IOException
     * @throws IllegalStateException
     * @return Map<String, String> 返回上传文件的保存路径 以文件名做map的key;文件保存路径作为map的value
     */
    public static ApiResult multiFileUpload(HttpServletRequest request, String basePath,  String devSwitch) throws  IOException {
        if(devSwitch.equals("close")) {
            ApiResult apiResult = checkFileExists(basePath);
            if (apiResult != null) {
                return apiResult;
            }
        }
        return multifileUploadAssist(request, basePath, null,devSwitch);
    }

    /**
     * 多文件上传
     *
     * @param request
     *            当前上传的请求
     * @param basePath
     *            保存文件的路径
     * @param exclude
     *            排除文件名字符串,以逗号分隔的,默认无可传null
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    public static ApiResult multiFileUpload(HttpServletRequest request, String basePath, String exclude, String devSwitch) throws  IOException {
        ApiResult apiResult = checkFileExists(basePath);
        if(apiResult != null){
            return apiResult;
        }
        return multifileUploadAssist(request, basePath, exclude, devSwitch);
    }

    private static ApiResult checkFileExists(String basePath){
        File file = new File(basePath);
        if (!file.exists()) {
            boolean createFile = file.mkdirs();
            if(createFile){
                logger.info("创建文件夹成功!");
                return null;
            }else{
                logger.info("创建文件夹失败!");
                return new ApiResult(HttpCodeEnum.FILE_UPLOAD_CODE_9001.getCode(), HttpCodeEnum.FILE_UPLOAD_CODE_9001.getMessage());
            }
        }
        return null;
    }
    /**
     * 多文件上传辅助
     *
     * @param request
     *            当前上传的请求
     * @param basePath
     *            保存文件的路径
     * @param exclude
     *            排除文件名字符串,以逗号分隔的,默认无可传null
     * @return
     * @throws IOException
     */
    private static ApiResult multifileUploadAssist(HttpServletRequest request, String basePath, String exclude,String devSwitch) throws IOException {
        exclude = exclude == null ? "" : exclude;
        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        List<FileData.FileResult> fileResults = new ArrayList<>();
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {

            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // get the parameter names of the MULTIPART files contained in this request
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                // 取得上传文件
                List<MultipartFile> multipartFiles = multiRequest.getFiles(iter.next());
                logger.info("附件数量："+multipartFiles.size());

                File directory  = null;// 参数为空
                File file = null;
                FileData.FileResult fileResult = null;
                for (MultipartFile multipartFile : multipartFiles) {
                    String fileName = multipartFile.getOriginalFilename();
                    if (StringUtils.isNotEmpty(fileName) && (!exclude.contains(fileName))) {
                        fileResult = new FileData.FileResult();
                        String newFileName = changeFilename2UUID(fileName);
                        String url = null;
                        logger.info("开发环境开关状态:"+devSwitch);
                        if(devSwitch.equals("open")){
                            directory = new File("F:");
                            String checkPath = directory .getCanonicalPath() + directory.separator + basePath + directory.separator ;
                            checkFileExists(checkPath);
                            url = directory.getCanonicalPath() + directory.separator + basePath + directory.separator + newFileName;
                        }else {
                            directory  = new File("");
                            url = directory .getCanonicalPath() + directory.separator + basePath + directory.separator + newFileName;
                        }
                        file = new File(url);
//                        fileResult.setUrl(file.getPath());
                        fileResult.setUrl(basePath + directory .separator + newFileName);
                        fileResult.setFileName(newFileName);
                        fileResults.add(fileResult);
                        multipartFile.transferTo(file);
                    }
                }
            }
            FileData fileData = new FileData();
            fileData.setFileResultList(fileResults);
            logger.info("上传文件结果fileResults:"+fileResults.toString());
            ApiResult apiResult = new ApiResult(HttpCodeEnum.CODE_0000.getCode(), HttpCodeEnum.CODE_0000.getMessage());
            apiResult.setData(fileData);
            return apiResult;
        }
        return new ApiResult(HttpCodeEnum.FILE_UPLOAD_CODE_9000.getCode(), HttpCodeEnum.FILE_UPLOAD_CODE_9000.getMessage());
    }

    /**
     * 将文件名转变为UUID命名的 ,保留文件后缀
     *
     * @param filename
     * @return
     */
    public static String changeFilename2UUID(String filename) {
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + FilenameUtils.getExtension(filename);
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}