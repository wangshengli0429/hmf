//package com.edianzu.ais;
//
//import com.hmf.web.utils.GoogleAuthenticator;
//import org.junit.Test;
//import org.slf4j.LoggerFactory;
//
//import java.math.BigDecimal;
//
///**
// * @ClassName: AuthTest
// * @Description: ${description}
// * @Version: 1.0
// */
//public class AuthTest {
//    private static String           secret = "F6EUJJMYK7GDC4KI";
//    private static org.slf4j.Logger logger = LoggerFactory.getLogger(AuthTest.class);
//    @Test
//    public void verifyTest() {
//        String savedSecret = "EIY5Z6UXTPWYXLSX";
//        long code = 781867;
//        GoogleAuthenticator ga = new GoogleAuthenticator();
////         ga.verifCode(secret, code);
////        System.out.println("是否正确：" + r);
//        long t = System.currentTimeMillis();
//        ga.setWindowSize(5); //should give 5 * 30 seconds of grace...
//        boolean r = ga.check_code(savedSecret, code, t);
//        System.out.println("是否正确 = " + r);
//    }
//    @Test
//    public void testBigDecimal() {
//        BigDecimal bg = new BigDecimal(30.12);
//        BigDecimal f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP);
//        System.out.println(f1);
//    }
//    @Test
//    public void testSecretTest() {
//        String secret = GoogleAuthenticator.generateSecretKey();
//        String qrcode = GoogleAuthenticator.getQRBarcodeURL("王二小", "wanghongbo.oicp.io:36247", secret);
//        System.out.println("二维码地址:" + qrcode);
//        System.out.println("密钥:" + secret);
//    }
//    @Test
//    public void newSecretTest() {// 生成密钥
//        secret = GoogleAuthenticator.generateSecretKey();
//        // 把这个qrcode生成二维码，用google身份验证器扫描二维码就能添加成功
//        String qrcode = GoogleAuthenticator.getQRBarcode("王二小","wanghongbo.oicp.io:36247", secret);
//        System.out.println("qrcode:" + qrcode + ",key:" + secret);
//    }
//
//    @Test
//    public void urlTest() {// 生成密钥
//        String url1 ="/static";
//        String url2 ="/4d3afabd-1c98-45d2-8204-18c419a075db.jpg";
//        String url3 ="/uploadFile/case/4d3afabd-1c98-45d2-8204-18c419a075db.jpg";
////        StringBuffer sbuffer = new StringBuffer(url);
////        String substring = sbuffer.sp
//        String s1 = url2.replaceFirst("/", "");
//        logger.info(s1);
//        String uploadpath = s1.split("/")[0];
//        logger.info(uploadpath);
//    }
//    @Test
//    public void TimeStampTest() {// 生成时间戳
//        long l = System.currentTimeMillis() / 1000;
//        System.out.println(l);
//    }
//}
