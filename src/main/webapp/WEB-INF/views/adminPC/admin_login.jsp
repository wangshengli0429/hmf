<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@page import="java.security.MessageDigest"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>管理端登录</title>
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
<body height="100%" onkeydown="keyLogin();">
<div style="background:#ffffff;height:14%;width:100%;">
	<img src="<%=request.getContextPath()%>/images/logo2.png" style="margin-left:15%;margin-top:15px;float:left;height:74px;">
	<span style="margin-left:18px;margin-top:30px;float:left; font-size:28px;color:#333333;font-weight:bold;font-family:Microsoft YaHei;">评点教育</span>
	<span style="margin-left:14px;margin-top:38px;float:left; font-size:18px;color:#999999;font-family:Microsoft YaHei;">|</span>
	<span style="margin-left:14px;margin-top:38px;float:left; font-size:18px;color:#999999;font-family:Microsoft YaHei;">用户登录</span>
</div>
<div style="background:#54a7fd;height:74%;width:100%;">
	<div style="background:#ffffff;margin-top:10%;margin-right:15%;float:right;z-index:1003;text-align:center;">
		<input type="hidden" name="a" id="a" value="${result}">
		<form id="loginForm" action="<%=request.getContextPath()%>/servlet/ManageLoginServlet?do=1" method="post" class="form-inline" role="form" style="margin:20px 20px 20px 20px;border-collapse:separate; border-spacing:0px 10px;">
			登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录<br>
			<input type="text" name="username" id="username" class="form-control" style="width:260px;height:30px;margin-top:10px" placeholder="请输入用户名">
			<br><span style="color:red;" id="nameSid"></span>
			<br>
			<input type="password" id="pw" onkeyup="this.value=this.value.replace(/\s+/g,'')" maxlength="20" class="form-control" style="width:288px;height:43px;margin-top:10px" placeholder="请输入密码">
			<input type="hidden" name="pwd" id="pwd"/>
			<br><span style="color:red;" id="pwSid"></span>
			<br>
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
    $(function (){
        $("#username").focus(function (){
            $("#nameSid").hide();
        })
        $("#pw").focus(function (){
            $("#pwSid").hide();
        })
        /* $("#loginId").click(function(){
            checkInput();
            if(checkInput()){
                //alert("ok");
                var password = document.getElementById("pw").value;
                var hash = hex_md5(password);
                $("#pwd").val(hash);
                document.getElementById("loginForm").submit();
                //$("#loginForm").action("/pingdian/servlet/ManageLoginServlet");
            }else{
                return false;
            }
        }) */
    })
    function login(){
        var name = $("#username").val();
        var pass = $("#pw").val();
        //判断用户名
        var a = /^[a-zA-Z0-9]*$/g;	//数字，字母，或组合
        if($("#username").val() == null || $("#username").val() == ""){
            $("#nameSid").text("请输入用户名");
            $("#nameSid").show();
            //$("#username").focus();
            return false;
        }else if(!$("#username").val().match(a)){
            $("#nameSid").text("用户名为数字，英文字母，或组合");
            $("#nameSid").show();
            return false;
        }
        $("#nameSid").hide();

        //判断密码
        //var b = /^[\da-zA-Z0-9]*$/g;	//包括字母，数字或组合，空格忽略，
        var p=/^(?![\d]$)(?![a-zA-Z]$)(?![^\da-zA-Z]+&).{6,20}$/;//包括符号、字母、数字或者两两组合的密码
        if($("#pw").val() == null || $("#pw").val() == ""){
            $("#pwSid").text("密码不能为空");
            $("#pwSid").show();
            //$("#password").focus();
            return false;
        }else if($("#pw").val().length<6||$("#pw").val().length>20){
            $("#pwSid").text("密码长度为6-20位字符");
            $("#pwSid").show();
            //$("#password").focus();
            return false;
        }else if(!$("#pw").val().match(p)){
            $("#pwSid").text("密码包括符号，字母，数字或组合");
            $("#pwSid").show();
            //$("#password").focus();
            return false;
        }else if(name==pass){
            $("#pwSid").text("不能使用用户名作为密码");
            $("#pwSid").show();
            //$("#password").focus();
            return false;
        }
        $("#pwSid").hide();
        //var hash = hex_md5(pass);//小写
        var hash = hex_md5(pass).toString().toUpperCase();//大写
        $("#pwd").val(hash);
        $("#loginForm").submit();
    }

    /* $("#username").blur(function (){
        //判断用户名
        if($("#username").val() == null || $("#username").val() == ""){
            $("#nameSid").text("请输入用户名");
            $("#nameSid").show();
            //$("#username").focus();
        }else{
            $("#nameSid").hide();
        }
    })
    $("#password").blur(function (){
        //判断密码
        if($("#password").val() == null || $("#password").val() == ""){
            $("#pwSid").text("请输入密码");
            $("#pwSid").show();
            //$("#password").focus();
        }else{
            $("#pwSid").hide();
        }
    }) */


    /* 登录   adminLogin */
    /* function adminLogin(){
        checkInput();
        var a = /^[0-9a-zA-Z]*$/g;
        alert($("#loginName").test(a));
        $("#loginForm").validate({
            rules:{
                username:{
                    required:true,
                    //equalTo:a
                },
                password: {
                    required:true,
                    rangelength:[6,20]
                },
            },
            messages : {
                username:{
                    required:"请输入用户名",
                    //equalTo:"用户名为数字，英文字母，或组合"
                },
                password:{
                    required:"请输入密码",
                    rangelength: "密码长度为6-20位字符"
                }
            },
            submitHandler: function(form){
                   alert("提交事件！");
                form.submit();
                   //var data = $("#ydpform").serialize();

            }
        })
    } */
</script>
</html>