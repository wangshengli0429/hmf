package com.hmf.web.utils;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JavaWebTokenUtil {
    private static Logger logger = LoggerFactory.getLogger(JavaWebTokenUtil.class);

    /**
     * @return a
     * @Description 生成token 用例：String token=JavaWebTokenUtil.createJWT("1","www.***.com",subject,1000*60*60*24*2+60000);//2天+60秒
    * @Param id:用户id；iss：发行者；sub:主题/用户名；ttlM:过期时间
     **/
    public  static String createJWT(String id, String issuer, String subject, long ttlMillis) {
        //id，issuer，subject，ttlMillis都是放在payload中的，可根据自己的需要修改
        //签名的算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //当前的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //签名算法的秘钥，解析token时的秘钥需要和此时的一样
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("5YWo5YC+5pyN5Yqhc21i56eY6ZKl");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //构造
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        logger.info("---token生成---");
        //给token设置过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            logger.info("过期时间："+exp);
            builder.setExpiration(exp);
        }

        //压缩
        return builder.compact();
    }

    public  static  Object parseJWT(String jwt) {
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary("5YWo5YC+5pyN5Yqhc21i56eY6ZKl")).parseClaimsJws(jwt).getBody();
            logger.info("------解析token----");
            logger.info("ID: " + claims.getId());
            logger.info("Subject: " + claims.getSubject());
            logger.info("Issuer: " + claims.getIssuer());
            logger.info("IssuerAt:   " + claims.getIssuedAt());
            logger.info("Expiration: " + claims.getExpiration());
            /*
            检验token是或否即将过期，如快要过期，就提前更新token。如果已经过期的，会抛出ExpiredJwtException 的异常

            */
            Long exp=claims.getExpiration().getTime(); //过期的时间
            long nowMillis = System.currentTimeMillis();//现在的时间
            Date now=new Date(nowMillis);
            logger.info("currenTime:"+now);
            long seconds=exp-nowMillis;//剩余的时间 ，若剩余的时间小与48小时，就返回一个新的token给APP
            long days=seconds/(1000*60*60*24);
            long hour=(seconds-days*1000*60*60*24)/3600000;
            long minutes = (seconds-days*1000*60*60*24-hour*3600000) / 60000;
            long remainingSeconds = seconds % 60;
            logger.info(seconds + " seconds is "+days+" days "+hour+" hours " + minutes + " minutes and "+ remainingSeconds + " seconds");
            if (seconds<=1000*60*60*48){
                logger.info("token的有效期小与48小时，请更新token！");
                return  "update";
            }
            return "success";
        }catch (ExpiredJwtException e){
            e.printStackTrace();
            return ExpiredJwtException.class.getName();
        }catch (SignatureException e1){
            e1.printStackTrace();
            return  SignatureException.class.getName();
        }catch (MalformedJwtException e2){
            e2.printStackTrace();
            return MalformedJwtException.class.getName();
        }
    }
}
