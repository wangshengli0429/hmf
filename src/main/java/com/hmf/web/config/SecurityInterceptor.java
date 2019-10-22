package com.hmf.web.config;

import com.hmf.web.manager.vo.GaCodeVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 权限拦截器
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);
//    @Autowired
//    private PePermissionService pePermissionService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        GaCodeVo gaCodeVo = (GaCodeVo)request.getSession().getAttribute("currentUser");
        if (null!=gaCodeVo){
            // 验证权限
            if (this.hasPermission(handler,gaCodeVo)) {
                return true;
            }
        }
//          null == request.getHeader("x-requested-with") TODO 暂时用这个来判断是否为ajax请求
        // 如果没有权限 则抛403异常 springboot会处理，跳转到 /error/403 页面
        response.sendError(HttpStatus.FORBIDDEN.value(), "无权限");
        return false;
    }

    /**
     * 是否有权限
     *
     * @param handler
     * @return
     */
    private boolean hasPermission(Object handler,GaCodeVo gaCodeVo) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取方法上的注解
            RequiredPermission requiredPermission = handlerMethod.getMethod().getAnnotation(RequiredPermission.class);
            // 如果方法上的注解为空 则获取类的注解
            if (requiredPermission == null) {
                requiredPermission = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequiredPermission.class);
            }
            // 如果标记了注解，则判断权限
            if (requiredPermission != null && StringUtils.isNotBlank(requiredPermission.value())) {
                logger.info("当前用户ID："+gaCodeVo.getId());
                // 数据库 中获取该用户的权限信息 并判断是否有权限
//                Set<String> permissionSet = pePermissionService.selectByUserid(gaCodeVo.getId());
//
//                if (CollectionUtils.isEmpty(permissionSet) ){
//                    return false;
//                }
//                //判断当前用户是否有操作权限
//                return permissionSet.contains(requiredPermission.value());
            }
            return false;
        }
        //无权限注解 不做任何操作 例如公众号 不需要该权限拦截
        return true;
    }

//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//    }

}
