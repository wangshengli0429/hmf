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
import org.springframework.web.servlet.config.annotation.*;

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
    @Value("${xcloud.uploadPath}")
    private String uploadPath;
    @Value("${xcloud.devSwitch}")
    private String devSwitch;
//    @Value("${spring.servlet.multipart.max-file-size}")
//    private String maxFileSize;
//    @Value("${spring.servlet.multipart.max-request-size}")
//    private String maxRequestSize;
    @Value("${spring.profiles.active}")
    private String active;
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // 自定义注解 用于传参多个对象
        argumentResolvers.add(new MultiRequestBodyArgumentResolver());
        // 自定义注解 用于crm工单后台登录后 获取当前登录者信息
        argumentResolvers.add(currentUserMethodArgumentResolver());
        super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器
        // /apis/wechat
        // /apis/crm
        //token拦截器
//        String[] excludes = new String[]{"/crm/users/login","/wechat_user/**","/resources/**"};
//        registry.addInterceptor(new JwtInterceptor()).addPathPatterns("/**").excludePathPatterns(excludes);
    //签名拦截
//        String[] permissionExclude = new String[]{"/static/*","/uploadFile/*","/crm/users/login","/error"};
//        registry.addInterceptor(signatureInterceptor()).excludePathPatterns(permissionExclude).addPathPatterns("/app/**");
        //权限拦截
//        String[] permissionExclude = new String[]{"/static/*","/uploadFile/*","/crm/users/login","/error"};
//        registry.addInterceptor(securityInterceptor()).excludePathPatterns(permissionExclude).addPathPatterns("/crm/**");
//        if(!active.equals("dev")) {
//            String[] crmExcludes = new String[]{"/crm/users/login"};
//            registry.addInterceptor(sysCurrentUserInterceptor()).addPathPatterns("/crm/**").excludePathPatterns(crmExcludes);//后台crm拦截器判断用户是否登录
//        }
        super.addInterceptors(registry);
    }
    /**
     * 修改springboot中默认的静态文件路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //addResourceHandler请求路径
        //addResourceLocations 在项目中的资源路径
        //setCacheControl 设置静态资源缓存时间
        registry.addResourceHandler("/**").addResourceLocations("classpath:/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
        //访问图片
        try {
            logger.info("开发环境开关状态:" + devSwitch);
            File directory = null;// 参数为空
            String url = null;//绝对路径
            String fileUrl = null;//文件对外暴露的访问路径
            if (devSwitch.equals("open")) {
                directory = new File("D:");
                String checkPath = directory.getCanonicalPath() + directory.separator + uploadPath + directory.separator;
                checkFileExists(checkPath);
                url = directory.getCanonicalPath() + "//" + uploadPath + "/" ;
                fileUrl = "/" + uploadPath +  "/**";
            } else {
                directory = new File("");
                url = directory.getCanonicalPath() + directory.separator + uploadPath + directory.separator;
                checkFileExists(url);
                fileUrl = directory.separator + uploadPath + directory.separator + "**";
            }
            logger.info("fileUrl:"+fileUrl+",url:"+url);
            registry.addResourceHandler(fileUrl).addResourceLocations("file:"+url);

        }catch (IOException e){
            logger.info("创建访问图片路径异常："+e.getMessage());
            e.printStackTrace();
        }
        super.addResourceHandlers(registry);
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

    @Bean
    public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
        return new CurrentUserMethodArgumentResolver();
    }

    @Bean
    public SysCurrentUserInterceptor  sysCurrentUserInterceptor(){
        return new SysCurrentUserInterceptor();
    }
    @Bean
    public SecurityInterceptor securityInterceptor() {
        return new SecurityInterceptor();
    }
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
}
