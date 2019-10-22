package com.hmf.web.upload.controller;


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
@RequestMapping(value = "/file")
public class UploadFileController {
    private static Logger logger = LoggerFactory.getLogger(UploadFileController.class);

    @Autowired
    private UploadFileService uploadFileService;
    /**
     * 实现多文件上传
//     * @param fileSign 文件标识 如case:工单附件 heard:头像
     * */
    @RequestMapping(value="/multifileUpload",method= RequestMethod.POST)
    public ApiResult multifileUpload(HttpServletRequest request){
        logger.info("文件上传接口");
        String fileSign = "case";//暂定义,后期跟前端在调试
        return uploadFileService.multiFileUpload(request, fileSign);
    }

    @RequestMapping(value="/download")
    public ApiResult download(HttpServletRequest request, HttpServletResponse response, String fileName){
        logger.info("文件下载路径"+fileName);
        return uploadFileService.downloadFiles(request,response,fileName);
    }


}
