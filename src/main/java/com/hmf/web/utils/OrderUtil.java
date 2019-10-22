package com.hmf.web.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;
public class OrderUtil {

    //生成订单编号
    public static String get15UUId() {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        String UUId = String.format("%015d", hashCodeV);
        System.out.println("生成UUId："+UUId);
        return UUId;
    }
    //生成订单编号
    public static String get5UUId() {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        String UUId = String.format("%6d", hashCodeV);
        UUId=UUId.substring(0,5);
        System.out.println("生成UUId："+UUId);
        return UUId;
    }
    //获得cpu序列号
    public static String getProcessID() {
        String serial = "";
        try {
            Process process = Runtime.getRuntime().exec(
                    new String[]{"wmic", "cpu", "get", "ProcessorId"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            String property = sc.next();
             serial = sc.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serial;
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
        String newDate=sdf.format(new Date());
        System.out.println(newDate+1+get5UUId());
        System.out.println(newDate+1+get5UUId());
        System.out.println(newDate+1+get5UUId());
        System.out.println(newDate+1+get5UUId());
    }
}