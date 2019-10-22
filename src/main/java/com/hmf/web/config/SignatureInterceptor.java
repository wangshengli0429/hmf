package com.hmf.web.config;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hmf.web.entity.PeApps;
import com.hmf.web.app.service.AppSignService;
import com.hmf.web.utils.SignUtil;
import com.hmf.web.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ClassName: SignatureInterceptor
 * @Version: 1.0
 */
public class SignatureInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(SignatureInterceptor.class);
        @Autowired
        private AppSignService appSignService;
    /**
     * 进入拦截器后首先进入的方法
     * 返回false则不再继续执行
     * 返回true则继续执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            // 验证签名
            if (this.hasPermission(handler,request)) {
                return true;
            }
        // 如果没有权限 则抛403异常 springboot会处理，跳转到 /error/403 页面
        response.sendError(HttpStatus.FORBIDDEN.value(), "签名不正确");
        return false;
    }

    /**
     * 是否有权限
     *
     * @param handler
     * @return
     */
    private boolean hasPermission(Object handler,HttpServletRequest request) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取方法上的注解
            SignatureAnnotation signatsureAnnotation = handlerMethod.getMethod().getAnnotation(SignatureAnnotation.class);
            // 如果方法上的注解为空 则获取类的注解
            if (signatsureAnnotation == null) {
                signatsureAnnotation = handlerMethod.getMethod().getDeclaringClass().getAnnotation(SignatureAnnotation.class);
            }
            // 如果标记了注解，则校验签名
            if (signatsureAnnotation != null) {
                logger.info("开始校验签名");
                Map<String, String> map = SignUtil.toVerifyMap(request.getParameterMap(),false);
                if(map==null||map.size()==0){
                    logger.info("请求为post body方式，开始获取参数");
                    RequestWrapper requestWrapper = new RequestWrapper(request);
                    String body = requestWrapper.getBody();
                    logger.info("=========body"+body);
                    JSONObject jsonObject = JSON.parseObject(body);
                    map = JSONObject.parseObject(body, new TypeReference<Map<String, String>>(){});
//                Set<String> strings = jsonObject.keySet();
//                Map<String, String> stringStringMap = BeanConverter.JsontoMap(jsonObject);
//                    return  true;
                }
                String appid =  map.get("appid");

                if (StringUtil.isEmpty(appid)){
                    logger.info("请求签名 appid is null");
                    return false;
                }
                PeApps peApps = appSignService.selectByAppId(Integer.valueOf(appid));
                if (null==peApps){
                    logger.info("请求签名 appid is err");
                    return false;
                }
                if (SignUtil.verify(map,peApps.getAppSecret())){
                    return true;
                }else {
                    return false;
                }
            }
            return false;
        }
        return true;
    }
}
