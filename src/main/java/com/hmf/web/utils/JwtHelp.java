package com.hmf.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

public class JwtHelp {
    private static Logger logger = LoggerFactory.getLogger(JwtHelp.class);

    public ApiResult codeFilter(HttpServletResponse response){
        ApiResult result = new ApiResult();
        String code = response.getHeader("code");
        logger.info("code:" + code);
//    String msg = response.getHeader("msg");
//    logger.info(msg);
        if (code.equals("0")) { //如果code为空，则说明对token认证成功
            result.setStatus("200");
            return result;
        } else if (code.equals("1")) { //code是1 表示：token解析成功，但是token即将过期，需要更换新的token
            //String newToken = response.getHeader("newToken");
            result.setStatus("200");
//            result.setToken("newToken");
            result.setMessage("请求成功，token即将过期");
//            //把更新的token保存到数据库的user表中。
//            Integer id=tokenUtil.getCurrentUserId(newToken);
//            User user1=new User();
//            user1=userRepo.findOne(id);
//            System.out.println(id);
//            user1.setId(id);
//            user1.setToken(newToken);
//            userRepo.save(user1);
//            logger.info("token已经被替换！");
//            Token token = new Token();
//            token.setToken(newToken);
            return result;
        } else if (code.equals("100")) { //code是100 表示： token解析失败
            result.setStatus("500");
            result.setMessage("token解析失败");
            return result;
        } else {
            result.setStatus("501");
            result.setMessage("token解析异常");
            return result;
        }
    }
}
