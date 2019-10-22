package com.hmf.web.config;

/**
 * @ClassName: SignatureAnnotation
 * @Description: ${校验签名}
 * @Version: 1.0
 */

import java.lang.annotation.*;
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SignatureAnnotation {

}
