<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.security.MessageDigest"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>教师端登录</title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/ecmascript" src="<%=request.getContextPath()%>/jslib/md5.js"></script>
<script language="JavaScript">
function keyLogin(){
	var theEvent = window.event || arguments.callee.caller.arguments[0];
	var code = theEvent.keyCode;
    if (code==13||code=="13"){//回车键的键值为13
	    document.getElementById("loginId").click(); //调用登录按钮的登录事件 
    }   
} 
</script>
</head>
<style>
	body{
		text-align: center;
		width:100%;
		height:100%;
		margin-right: auto; margin-left: auto;
		background:#ffffff;
		overflow:auto;
		font-family:"Microsoft YaHei";
	}
	
</style>
<body onkeydown="keyLogin();">
<div style="background:#ffffff;height:14%;width:100%;">
	<img src="<%=request.getContextPath()%>/images/logo2.png" style="margin-left:15%;margin-top:15px;float:left;height:74px;">
	<span style="margin-left:18px;margin-top:30px;float:left; font-size:28px;color:#333333;font-weight:bold;font-family:Microsoft YaHei;">评点教育</span>
	<span style="margin-left:14px;margin-top:38px;float:left; font-size:18px;color:#999999;font-family:Microsoft YaHei;">|</span>
	<span style="margin-left:14px;margin-top:38px;float:left; font-size:18px;color:#999999;font-family:Microsoft YaHei;">用户登录</span>
</div>
<div style="background:#54a7fd;height:74%;width:100%;">
	<div style="background:#ffffff;margin-top:10%;margin-right:15%;float:right;z-index:1003;text-align:center;">
		<input type="hidden" name="a" id="a" value="${result}">
		<form id="loginForm" action="<%=request.getContextPath()%>/servlet/LoginServlet" method="post" class="form-inline" role="form" style="margin:20px 20px 20px 20px;border-collapse:separate; border-spacing:0px 10px;">
			登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录<br>
			<input type="text" name="loginName" id="loginName" maxlength="11" class="form-control" style="width:260px;height:30px;margin-top:10px" placeholder="请输入手机号"><br>
			<input type="password" id="pw" class="form-control" maxlength="20" onkeyup="this.value=this.value.replace(/\s+/g,'')" style="width:288px;height:43px;margin-top:10px" placeholder="请输入密码"><br>
			<input type="hidden" name="pwd" id="pwd"/>
			<button type="button" id="loginId" onclick="login()" class="btn btn-primary" data-toggle="button" style="width:284px;height:42px;margin-top:20px">登&nbsp;录</button>
		</form>
	</div>
	<img src="<%=request.getContextPath()%>/images/logo3.png" style="margin-left:15%;margin-top:7%;float:left;width:40%;">
</div>
<div style="margin-top:1%;height:9%;width:100%;text-align:center;">
	<p style="font-size:12px;color:#999999;font-family:Microsoft YaHei;">
	联系我们&nbsp;010-88333186&nbsp;&nbsp;service@pingdianedu.com
	</p>
	<p style="font-size:12px;color:#999999;font-family:Microsoft YaHei;">
	© 作文评点网 版权所有 www.pingdianedu.com&nbsp;&nbsp;京ICP备16007121号-1
	</p>
</div>
</body>
<!-- validate表单验证 -->
<style>
.error{
	color:red;
}
</style>
<script type="text/javascript"> 
/* 禁用浏览器后退按钮 */
$(function() {
	if (window.history && window.history.pushState) {
		$(window).on('popstate', function () {
			window.history.pushState('forward', null, '#');
			window.history.forward(1);
		});
	}
	window.history.pushState('forward', null, '#'); //在IE中必须得有这两行
	window.history.forward(1);
})
</script> 
<script type="text/javascript">
	
$(function (){
	var a =  $("#a").val();
	if(a.length>0){
		alert(a);
	}
})

/* 登录   login */
function login(){
 	var result = document.getElementById("loginName").value;
	var password = document.getElementById("pw").value;
	if(result == "" ){
		alert("手机号不能为空");
		return false;
	}
	if(result.length != 11){  
		alert("请输入11位手机号");  
		return false;  
	}  
    var re =  /^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/;  //判断字符串是否为数字和字母组合     //判断正整数 /^[1-9]+[0-9]*]*$/    
    if (!re.test(result)){  
    	alert("请输入有效手机号");
        return false;  
     }
	if(password == ""  ){
		alert("密码不能为空");
		return false;
	}
	if(password.length<6 || password.length>20 ){
		alert("密码长度为6-20位字符");
		return false;
	}
	//var hash = hex_md5(password);//小写
	var hash = hex_md5(password).toString().toUpperCase();//大写
	$("#pwd").val(hash);
	$("#loginForm").submit();
}
</script>
</html>