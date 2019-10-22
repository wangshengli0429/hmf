package com.hmf.web.upload.common;


public class FileType {


    public static String getFileType(String fileName) {
        String[] strArray = fileName.split("\\.");
        int suffixIndex = strArray.length -1;
        return  strArray[suffixIndex];
    }



    public static void main(String[] args) throws Exception {
        String type = getFileType("C:/Users/1/Desktop/1546865887.jpg");
        System.out.println("1546865887.jpg : "+type);
        System.out.println();
    }
}