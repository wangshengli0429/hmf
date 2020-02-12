package com.hmf.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**********
 * 跨域 CORS:使用 方法3
 * 方法:
 1服务端设置Respone Header头中Access-Control-Allow-Origin
 2配合前台使用jsonp
 3继承WebMvcConfigurerAdapter 添加配置类
 http://blog.csdn.net/hanghangde/article/details/53946366
 * @author xialeme
 *
 */
@Configuration
@EnableWebMvc
public class WebConfig  extends WebMvcConfigurerAdapter implements WebMvcConfigurer {

    private static Logger logger = LoggerFactory.getLogger(WebConfig.class);
//    @Value("${xcloud.uploadPath}")
//    private String uploadPath;
//    @Value("${xcloud.devSwitch}")
//    private String devSwitch;
//    @Value("${spring.servlet.multipart.max-file-size}")
//    private String maxFileSize;
//    @Value("${spring.servlet.multipart.max-request-size}")
//    private String maxRequestSize;
//    @Value("${spring.profiles.active}")
//    private String active;

    /**
     * 配置静态访问资源
     *
     * @param registry
     */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").
                addResourceLocations("/WEB-INF/views/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // 自定义注解 用于传参多个对象
        argumentResolvers.add(new MultiRequestBodyArgumentResolver());
        // 自定义注解 用于后台登录后 获取当前登录者信息
//        argumentResolvers.add(currentUserMethodArgumentResolver());
        super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);
    }
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1
        corsConfiguration.addAllowedHeader("*"); // 2
        corsConfiguration.addAllowedMethod("*"); // 3
        return corsConfiguration;
    }
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(source);
    }

//    @Bean
//    public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
//        return new CurrentUserMethodArgumentResolver();
//    }

//    @Bean
//    public SysCurrentUserInterceptor  sysCurrentUserInterceptor(){
//        return new SysCurrentUserInterceptor();
//    }
    //注入签名拦截器
    @Bean
    public SignatureInterceptor signatureInterceptor() {
        return new SignatureInterceptor();
    }
//    /**
//     * 文件上传配置
//     * @return
//     */
//     @Bean
//     public MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//            /* 文件最大 */
//        factory.setMaxFileSize(maxRequestSize);
//        // 设置总上传数据总大小
//        factory.setMaxRequestSize(maxFileSize);
//        return factory.createMultipartConfig();
//    }

    private boolean checkFileExists(String basePath){
        File file = new File(basePath);
        if (!file.exists()) {
            boolean createFile = file.mkdirs();
            if(createFile){
                logger.info("创建文件夹成功!");
                return true;
            }else{
                logger.info("创建文件夹失败!");
                return false;
            }
        }
        return true;
    }

    /**
     * 添加对jsp支持
     *
     */
    @Bean
    public ViewResolver getJspViewResolver() {
        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setPrefix("/WEB-INF/");//前缀
        internalResourceViewResolver.setSuffix(".jsp");//后缀
        internalResourceViewResolver.setOrder(0);//优先级
        return internalResourceViewResolver;
    }

    /**
     * 添加对Freemarker支持
     *
     */
    @Bean
    public FreeMarkerViewResolver getFreeMarkerViewResolver() {
        FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
        freeMarkerViewResolver.setCache(false);
        freeMarkerViewResolver.setPrefix("/WEB-INF/");//前缀
        freeMarkerViewResolver.setSuffix(".html");//后缀
        freeMarkerViewResolver.setRequestContextAttribute("request");
        freeMarkerViewResolver.setOrder(1);//优先级
        freeMarkerViewResolver.setContentType("text/html;charset=UTF-8");
        return freeMarkerViewResolver;

    }
}
