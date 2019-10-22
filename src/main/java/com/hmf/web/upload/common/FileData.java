package com.hmf.web.upload.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FileData implements Serializable {

    private List<FileResult> fileResultList;
    @Data
    public static class FileResult implements Serializable {
        private String fileName;
        private String url;
    }
}
