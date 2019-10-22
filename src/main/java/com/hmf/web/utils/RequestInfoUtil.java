package com.hmf.web.utils;

import java.util.HashMap;
import java.util.Map;

public class RequestInfoUtil {
    public static Map<String, String> requestUtil(String request)
    {
        Map<String, String> map = new HashMap<String, String>();
        strRequest(map, request);
        return map;
    }

    public static String strRequest(Map<String, String> map, String s)
    {
        int length = s.length();
        int index1 = s.indexOf("=");
        String parm1 = s.substring(0, index1);
        int index2 = s.indexOf("&");
        if (index2 == -1)
        {
            String parm2 = s.substring(index1 + 1);
            map.put(parm1, parm2);
            return null;
        }
        String parm2 = s.substring(index1 + 1, index2);
        map.put(parm1, parm2);
        return strRequest(map, s.substring(index2 + 1));
    }
}
