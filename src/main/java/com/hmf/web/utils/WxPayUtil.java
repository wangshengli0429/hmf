package com.hmf.web.utils;

/**
 * @ClassName: WxPayUtil
 * @Description: ${description}
 * @Version: 1.0
 */
public class WxPayUtil {

    /**
     * 自定义函数，官方没有
     * @param return_code
     * @param return_msg
     * @return
     */
    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg
                + "]]></return_msg></xml>";
    }

}
