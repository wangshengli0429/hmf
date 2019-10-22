package com.hmf.web.app.controller;


import com.hmf.web.config.SignatureAnnotation;
import com.hmf.web.upload.service.UploadFileService;
import com.hmf.web.utils.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/app/file")
public class AppUploadFileController {
    private static Logger logger = LoggerFactory.getLogger(AppUploadFileController.class);

    @Autowired
    private UploadFileService uploadFileService;
    /**
     * 实现多文件上传
     * @param fileSign 文件标识 如case:工单附件 heard:头像 visit 拜访
     *   form 提交 fileNames字段类型为file
     * */
    @RequestMapping(value="/upload",method= RequestMethod.POST)
    @SignatureAnnotation
    public ApiResult multifileUpload(HttpServletRequest request, String fileSign){
        logger.info("文件上传接口");
        return uploadFileService.multiFileUpload(request, fileSign);
    }

    @RequestMapping(value="/download")
    @SignatureAnnotation
    public ApiResult download(HttpServletRequest request, HttpServletResponse response, String fileName){
        logger.info("文件下载路径"+fileName);
        return uploadFileService.downloadFiles(request,response,fileName);
    }


}
