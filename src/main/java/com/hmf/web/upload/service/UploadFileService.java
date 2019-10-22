package com.hmf.web.upload.service;

import com.hmf.web.utils.ApiResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UploadFileService {
    /**
     * 文件上传
     * @param request
     * @param fileSign
     * @return
     */
    ApiResult multiFileUpload(HttpServletRequest request, String fileSign);

    /**
     * 文件下载
     * @param request
     * @param response
     * @param fileName
     * @return
     */
    ApiResult downloadFiles(HttpServletRequest request, HttpServletResponse response, String fileName);
}
