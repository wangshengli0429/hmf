package com.hmf.web.utils;




import net.sf.json.JSONObject;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: hoobey
 * @Description:
 * @Date: Created in 9:12 2018/1/6
 * @Modified By:
 * *转换器
 * 1:将JavaBean 转换成Map、JSONObject
 * 2:将JSONObject 转换成Map
 */
public class BeanConverter {
    /**
     * 将javaBean转换成Map
     *
     * @param javaBean javaBean
     * @return Map对象
     */
    public static Map<String, String> BeantoMap(Object javaBean) {
        Map<String, String> result = new HashMap<String, String>();
        Method[] methods = javaBean.getClass().getDeclaredMethods();

        for (Method method : methods) {
            try {
                if (method.getName().startsWith("get")) {
                    String field = method.getName();//getName  getPassword
                    field = field.substring(field.indexOf("get") + 3);//Name  Password
                    field = field.toLowerCase().charAt(0) + field.substring(1);//name password
                    Object value = method.invoke(javaBean, (Object[]) null);
                    result.put(field, null == value ? "" : value.toString());
                }
            } catch (Exception e) {
            }
        }

        return result;
    }

    /**
     * 将json对象转换成Map
     *
     * @param jsonObject json对象
     * @return Map对象
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> JsontoMap(JSONObject jsonObject) {
        Map<String, String> result = new HashMap<String, String>();
        Iterator<String> iterator = jsonObject.keys();
        String key = null;
        String value = null;
        while (iterator.hasNext()) {
            key = iterator.next();
            value = jsonObject.getString(key);
            result.put(key, value);
        }
        return result;
    }

    /**
     * 将javaBean转换成JSONObject
     *
     * @param bean javaBean
     * @return json对象
     */
    public static JSONObject toJSON(Object bean) {
        return new JSONObject().fromObject(BeantoMap(bean));
    }

    /**
     * 将map转换成Javabean
     *
     * @param javabean javaBean
     * @param data     map数据
     */
    public static Object toJavaBean(Object javabean, Map<String, String> data) {
        Method[] methods = javabean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            try {
                if (method.getName().startsWith("set")) {
                    String field = method.getName(); //setName   setPassword
                    field = field.substring(field.indexOf("set") + 3);//Name  Password
                    field = field.toLowerCase().charAt(0) + field.substring(1);//name  password
                    method.invoke(javabean, new Object[]
                            {
                                    data.get(field)
                            });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return javabean;
    }

    /**
     * 将javaBean转换成JSONObject
     *
     * @param data javaBean
     * @return json对象
     * @throws ParseException json解析异常
     */
    public static void toJavaBean(Object javabean, String data) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.fromObject(data);
        Map<String, String> datas = BeantoMap(jsonObject);
        toJavaBean(javabean, datas);
    }
}