package com.hmf.web.config;

import java.lang.annotation.*;

/**
 * @author
 * @description 与拦截器结合使用 验证权限
 * @date
 * @since 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequiredPermission {
    String value();
}
