package com.hmf.web.upload.service.impl;

import com.hmf.web.upload.common.OperationFileUtil;
import com.hmf.web.upload.service.UploadFileService;
import com.hmf.web.utils.ApiResult;
import com.hmf.web.utils.StringUtil;
import com.hmf.web.utils.enums.HttpCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    private static Logger logger = LoggerFactory.getLogger(UploadFileServiceImpl.class);

    @Value("${xcloud.uploadPath}")
    private String uploadPath;
    @Value("${xcloud.devSwitch}")
    private String devSwitch;


    @Override
    public ApiResult multiFileUpload(HttpServletRequest request, String fileSign) {
        try {
            logger.info("uploadPath："+uploadPath+"fileSign："+fileSign);
            String filePath = uploadPath;
            if(StringUtil.isNotEmpty(fileSign)) {
                 File file = new File("");
                 filePath = uploadPath + file.separator + fileSign;
            }
            return OperationFileUtil.multiFileUpload(request, filePath,devSwitch);
        }catch (IOException ex){
            logger.info("文件上传IO异常："+ex.getMessage());
            ex.printStackTrace();
        }
        return new ApiResult(HttpCodeEnum.FILE_UPLOAD_CODE_9000.getCode(), HttpCodeEnum.FILE_UPLOAD_CODE_9000.getMessage());
    }

    @Override
    public ApiResult downloadFiles(HttpServletRequest request, HttpServletResponse response,String fileName) {
//        return  downloadFile(request,response,fileName);
        try {
            return OperationFileUtil.download(fileName);
        }catch (UnsupportedEncodingException ex){
            logger.info("文件下载编码异常："+ex.getMessage());
            ex.printStackTrace();
        }catch (IOException ex){
            logger.info("文件下载IO异常："+ex.getMessage());
            ex.printStackTrace();
        }
        return new ApiResult(HttpCodeEnum.FILE_UPLOAD_CODE_9002.getCode(), HttpCodeEnum.FILE_UPLOAD_CODE_9002.getMessage());
    }

/*
    private ApiResult downloadFile(HttpServletRequest request, HttpServletResponse response,String fileName) {
        if (fileName != null) {
            //设置文件路径
            File file = new File(uploadPath+fileName);
            //File file = new File(realPath , fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    return new ApiResult(DOWNLOAD_CODE_2000.getCode(),DOWNLOAD_CODE_2000.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return new ApiResult(DOWNLOAD_CODE_2001.getCode(),DOWNLOAD_CODE_2001.getMessage());
    }
*/
}
