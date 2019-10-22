<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <%
        String appId = (String)request.getAttribute("appId");
        String timeStamp = (String)request.getAttribute("timeStamp");
        String nonceStr = (String)request.getAttribute("nonceStr");
        String _package = (String)request.getAttribute("package");
        String signType = (String)request.getAttribute("signType");
        String paySign = (String)request.getAttribute("paySign");
    %>
    <script type="text/javascript">
        function callpay()
        {
            if (typeof WeixinJSBridge == "undefined") {
                if (document.addEventListener) {
                    document.addEventListener('WeixinJSBridgeReady', onBridgeReady,
                        false);
                } else if (document.attachEvent) {
                    document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                    document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
                }
            } else {
                onBridgeReady();
            }
        }

        function onBridgeReady() {

            alert("appId=<%=appId %>,timeStamp=<%=timeStamp %>,nonceStr=<%=nonceStr %>,package=<%=_package %>,signType=<%=signType %>,paySign=<%=paySign %>");

            WeixinJSBridge.invoke('getBrandWCPayRequest', {
                "appId" : "<%=appId %>",
                "timeStamp" : "<%=timeStamp %>",
                "nonceStr" : "<%=nonceStr %>",
                "package" : "<%=_package %>",
                "signType" : "<%=signType %>",
                "paySign" : "<%=paySign %>"
            }, function(res) { // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回  ok，但并不保证它绝对可靠。
                //alert(res.err_msg);
                if (res.err_msg == "get_brand_wcpay_request:ok") {
                    alert("支付成功");
                }
                if (res.err_msg == "get_brand_wcpay_request:cancel") {
                    alert("交易取消");
                }
                if (res.err_msg == "get_brand_wcpay_request:fail") {
                    alert("支付失败");
                }
            });
        }
    </script>
</head>
<body>
<div style="text-align: center;margin-top: 50px;"><h1><button type="button" style="display:inline-block;width:600px;height:200px;border-radius:30px;font-size:50px" onclick="callpay()">确认支付</button></h1></div>
</body>
</html>