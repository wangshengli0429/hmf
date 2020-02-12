<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.security.MessageDigest"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改密码</title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/ecmascript" src="<%=request.getContextPath()%>/jslib/md5.js"></script>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<div style="display:inline;">
修改密码<hr>
</div>
<form id="pwForm" action="<%=request.getContextPath()%>/servlet/ManageUpdateServlet?do=2" method="post" class="form-inline" role="form">
<table align="center" style="border-collapse:separate; border-spacing:0px 10px;">
	<tr>
		<td>
			<input type="hidden" name="id" id="aid" value="${id }" class="form-control" style="width:300px;height:30px;">
			<input type="hidden" name="pw" id="pw" value="${pw }" class="form-control" style="width:300px;height:30px;">
		</td>
	</tr>
	<tr>
		<td style="text-align:right">
			原密码：&nbsp;&nbsp;
		</td>
		<td>
			<input type="password" name="oldPw" id="oldPw" onkeyup="this.value=this.value.replace(/\s+/g,'')" class="form-control" style="width:300px;height:30px;" placeholder="请输入原密码">
			<br><span style="color:red;" id="oldpwSid"></span>
		</td>
	</tr>
	<tr>
		<td style="text-align:right">
			新密码：&nbsp;&nbsp;
		</td>
		<td>
			<input type="password" name="newPw" id="newPw" onblur="testPw()" onkeyup="this.value=this.value.replace(/\s+/g,'')" class="form-control" style="width:300px;height:30px;" placeholder="请输入新密码">
			<br><span style="color:red;" id="newpwSid"></span>
		</td>
	</tr>
	<tr>
		<td style="text-align:right">
			再次输入新密码：&nbsp;&nbsp;
		</td>
		<td>
			<input type="password" name="tonewPw" id="tonewPw" onkeyup="this.value=this.value.replace(/\s+/g,'')" class="form-control" style="width:300px;height:30px;" placeholder="再次输入新密码">
			<br><span style="color:red;" id="topwSid"></span>
		</td>
	</tr>
	<tr>
		<td></td>
		<td> &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="quXiao()" class="btn btn-default" data-toggle="button" style="width:90px;">取&nbsp;消</button>
			 &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" id="updateId" target="index1" class="btn btn-default" data-toggle="button" style="width:90px;">确&nbsp;定</button>
		</td>
	</tr>
</table>
</form>	
	
</div>
</div>
</div>
</body>
<!-- validate表单验证 -->
<style>
.error{
	color:red;
}
</style>
<script>
	
	$(function (){	
		$("#oldPw").focus(function (){
			$("#oldpwSid").hide();
		})
		$("#newPw").focus(function (){
			$("#newpwSid").hide();
		})
		$("#tonewPw").focus(function (){
			$("#topwSid").hide();
		})
		$("#updateId").click(function(){
			checkInput();
			if(checkInput()){
				//alert("ok");
				var newPw = $("#newPw").val();
				var md5newPw = hex_md5(newPw);
				$("#newPw").val(md5newPw);
				$("#tonewPw").val(md5newPw);
				document.getElementById("pwForm").submit();
			}else{
				return false;
			}
		})
	})	

	function checkInput(){
		var pw = $("#pw").val();
		var oldPw = $("#oldPw").val();
		var md5oldPw = hex_md5(oldPw);
		var newPw = $("#newPw").val();
		var tonewPw = $("#tonewPw").val();
		//var a = /^[a-zA-Z0-9]*$/g;	//数字，字母，或组合
		var p=/^(?![\d]$)(?![a-zA-Z]$)(?![^\da-zA-Z]+&).{6,20}$/;//包括符号、字母、数字或者两两组合的密码
		if(oldPw == null || oldPw == ""){
			$("#oldpwSid").text("请输入原密码");
			$("#oldpwSid").show();
			return false;
		}else if(oldPw.length<6||oldPw.length>20){
			$("#oldpwSid").text("密码长度6-20位字符之间");
			$("#oldpwSid").show();
			return false;
		}else if(md5oldPw!=pw){
			$("#oldpwSid").text("原密码错误");
			$("#oldpwSid").show();
			return false;
		}
		$("#oldpwSid").hide();
		
		if(newPw== null || newPw== ""){
			$("#newpwSid").text("请输入新密码");
			$("#newpwSid").show();
			return false;
		}else if(newPw.length<6||newPw.length>20){
			$("#newpwSid").text("密码长度6-20位字符之间");
			$("#newpwSid").show();
			return false;
		}else if(oldPw==newPw){
			$("#newpwSid").text("您的新密码与原密码一致，请重新输入新密码。");
			$("#newpwSid").show();
			return false;
		}else if(!p.test(newPw)){
	    	$("#newpwSid").text("密码包括符号，字母，数字或组合");
			$("#newpwSid").show();
			return false;
		}	
		$("#newpwSid").hide();
		
		if(tonewPw== null || tonewPw== ""){
			$("#topwSid").text("确认密码不能为空");
			$("#topwSid").show();
			return false;
		}else if(tonewPw.length<6||tonewPw.length>20){
			$("#topwSid").text("密码长度6-20位字符之间");
			$("#topwSid").show();
			return false;
		}else if(tonewPw!=newPw){
			$("#topwSid").text("两次输入的密码不一致，请重新输入");
			$("#topwSid").show();
			return false;
		}
		$("#topwSid").hide();
		return true;
	}
	
	/* 取消  quXiao */
	function quXiao(){
		location.href="<%=request.getContextPath()%>/admin_index.jsp";
	}
	
	
	
	
	/* jQuery.validator.setDefaults({
	  debug: true,
	  success: "valid"
	}); */
	/* 
	function checkInput(){
		$("#pwForm").validate({
		rules:{
			oldPw:{
				required:true,
				rangelength:[6,20]
			},
			newPw: {
				required:true,
				rangelength:[6,20],
			},
			tonewPw: {
				required:true,
				rangelength:[6,20],
				equalTo:"#newPw"
			}
		},
		messages : {
			oldPw:{
				required:"请输入原密码",
				rangelength: "密码长度6-20位字符之间"
			},
			newPw:{
				required:"请输入新密码",
				rangelength: "密码长度6-20位字符之间"
			},
			tonewPw:{
				required:"确认密码不能为空",
				rangelength: "密码长度6-20位字符之间",
				equalTo:"两次输入的密码不一致，请重新输入"
			}
		},
		submitHandler: function(form){
		   	alert("提交事件！");
		    form.submit();
		   	//var data = $("#fwform").serialize();
		   	
		} 
		})
	}
*/
</script>
</html>