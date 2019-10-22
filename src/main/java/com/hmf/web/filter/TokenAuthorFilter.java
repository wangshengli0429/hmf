package com.hmf.web.filter;

import com.hmf.web.manager.vo.GaCodeVo;
import com.hmf.web.exception.TokenException;
import com.hmf.web.utils.JavaWebTokenUtil;
import com.hmf.web.entity.LoginResultEntity;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName: TokenAuthorFilter
 * @Description: ${description}
 * @Version: 1.0
 */
@WebFilter(urlPatterns = {"/111111"}, filterName = "tokenAuthorFilter")
@Order(value = 1)
public class TokenAuthorFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(TokenAuthorFilter.class);
    //优先级/静态资源/登录注册/其他接口
//    @Autowired
//    private JWTService jwtService;
    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
        Arrays.asList("/crm/users/login","/wechat_user/back","/wechat_user/reset",
                "/wechat_user/code","/wechat_user/checkCode","/wechat_user/sms",
                "/wechat_user/sign_up","/wechat_user/sign_in",
                "/static/MP_verify_TAbjOaLzcrb7Tmgg.txt", "/MP_verify_TAbjOaLzcrb7Tmgg.txt", "/test/index/test", "/crm/users/qrcode")));
    private static final String uploadFile = "uploadFile";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length()).replaceAll("[/]+$", "");
        //增加uploadFile校验
        boolean allowedPath = ALLOWED_PATHS.contains(path);
        //单独校验查询上传文件拦截路径示例：String url3 ="/uploadFile/case/4d3afabd-1c98-45d2-8204-18c419a075db.jpg";
        String newpath = path.replaceFirst("/", "");
        logger.info("开始替换路径："+newpath);
        String uploadpath = newpath.split("/")[0];
        logger.info("按照规则生最终路径："+uploadpath);
        if(uploadpath.equals(uploadFile)){
            allowedPath=true;
        }
        if (allowedPath) {
            logger.info("此url"+path+"不需要过滤器过滤");
            chain.doFilter(request, response);
        } else {
            //这里填写你允许进行跨域的主机ip（正式上线时可以动态配置具体允许的域名和IP）
            rep.setHeader("Access-Control-Allow-Origin",  req.getHeader("Origin"));
            // 允许的访问方法
            rep.setHeader("Access-Control-Allow-Methods","GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
            rep.setHeader("Access-Control-Allow-Credentials", "true");
            rep.setHeader("Access-Control-Max-Age", "0");
            // Access-Control-Max-Age 用于 CORS 相关配置的缓存
            //rep.setHeader("Access-Control-Max-Age", "3600");
            //rep.setHeader("Access-Control-Allow-Headers","token");
            //rep.setHeader("Access-Control-Expose-Headers","Cache-Control,Content-Type,Expires,Pragma,Content-Language,Last-Modified,token");
            //rep.setHeader("Access-Control-Allow-Headers","token,Origin, X-Requested-With, Content-Type, Accept");
            rep.setHeader("Access-Control-Allow-Headers","token,Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token,Access-Control-Allow-Headers");
            rep.setHeader("XDomainRequestAllowed","1");
            //设置服务器和客户端统一编码格式
            rep.setCharacterEncoding("UTF-8");
            rep.setContentType("application/json;charset=UTF-8");
            String token = req.getHeader("token");//header方式
            String method = ((HttpServletRequest) request).getMethod();

            if (method.equals("OPTIONS")) {
                rep.setStatus(HttpServletResponse.SC_OK);
            }else{
                if (null == token || token.isEmpty()) {
                    logger.error("用户授权认证没有通过!客户端请求参数中无token信息");
                    rep.setHeader("code","100");
                    rep.setHeader("msg","User authorization authentication has not passed! No token information in client request parameter ");
                    chain.doFilter(req, rep);
                    throw new TokenException("用户授权认证没有通过!客户端请求参数中无token信息");
                } else {
                    String exception= JavaWebTokenUtil.parseJWT(token).toString();
                    if (exception!=null){
                        if (ExpiredJwtException.class.getName().equals(exception)){
                            logger.error("token已过期！");
                            rep.setHeader("code","100");
                            rep.setHeader("msg","token is overtime！");
                            chain.doFilter(req, rep);
                            throw new TokenException("Token已过期");
                        }else if (SignatureException.class.getName().equals(exception)){
                            logger.error("token sign解析失败");
                            rep.setHeader("code","100");
                            rep.setHeader("msg","token is invalid! ");
                            chain.doFilter(req, rep);
                            throw new TokenException("token sign解析失败");
                        }else if (MalformedJwtException.class.getName().equals(exception)){
                            logger.error("token的head解析失败");
                            rep.setHeader("code","100");
                            rep.setHeader("msg","token is invalid! ");
                            chain.doFilter(req, rep);
                            throw new TokenException("token的head解析失败");
                        }else if ("success".equals(exception)){
                            logger.error("用户授权认证通过!");
                            rep.setHeader("code","0");
                            chain.doFilter(req, rep);
                        }else {
                            logger.error("token的有效期小与2天！");
                            //String newToken=jwtService.updateToken(exception);
                            String userId= "";
                            String userName= "";
                            if(null!=req.getSession().getAttribute("appCurrentUser")){
                                LoginResultEntity loginResultEntity = (LoginResultEntity)req.getSession().getAttribute("appCurrentUser");
                                userId=String.valueOf(loginResultEntity.getId());
                                userName=loginResultEntity.getUserName();
                            }else if(null!=req.getSession().getAttribute("currentUser")){
                                GaCodeVo gaCodeVo = (GaCodeVo)req.getSession().getAttribute("currentUser");
                                userId=String.valueOf(gaCodeVo.getId());
                                userName=gaCodeVo.getUsername();
                            }
                            String newToken = JavaWebTokenUtil.createJWT(userId, "allits.com.cn", userName, 1000 * 60 * 60 * 24 * 2 + 60000);
                            //logger.info("已生成新的token："+newToken);
                            rep.setHeader("code","1");
                            rep.setHeader("msg","token will invalid at 2 days！please refresh");
                            rep.setHeader("token",newToken);
                            chain.doFilter(req, rep);
                        }
                    }
                }
            }
        }
    }
    @Override
    public void destroy() {
        logger.info("销毁----------filter");
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("初始化-----------filter");
    }
}