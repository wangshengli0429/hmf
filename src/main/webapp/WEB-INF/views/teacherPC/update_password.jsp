<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改密码</title>
<jsp:include page="../inc.jsp"></jsp:include>
</head>
<body>
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<div style="display:inline;">
修改密码<hr>
</div>
<form id="pwForm" class="form-inline" role="form" action="<%=request.getContextPath()%>" method="post">
<table align="center" style="border-collapse:separate; border-spacing:0px 10px;">
	<tr>
		<td>
			<input type="hidden" name="id" id="id" value="${id }" class="form-control" style="width:300px;height:30px;">
			<input type="hidden" name="pw" id="pw" value="${pw }" class="form-control" style="width:300px;height:30px;">
		</td>
	</tr>
	<tr>
		<td style="text-align:right">
			原密码：&nbsp;&nbsp;
		</td>
		<td>
			<input type="text" name="oldPw" id="oldPw" class="form-control" style="width:300px;height:30px;" placeholder="请输入原密码">
			<br><span style="color:red;" id="oldPwSid"></span>
		</td>
	</tr>
	<tr>
		<td style="text-align:right">
			新密码：&nbsp;&nbsp;
		</td>
		<td>
			<input type="text" name="newPw" id="newPw" class="form-control" style="width:300px;height:30px;" placeholder="请输入新密码">
			<br><span style="color:red;" id="newPwSid"></span>
		</td>
	</tr>
	<tr>
		<td style="text-align:right">
			再次输入新密码：&nbsp;&nbsp;
		</td>
		<td>
			<input type="text" name="tonewPw" id="tonewPw" class="form-control" style="width:300px;height:30px;" placeholder="再次输入新密码">
			<br><span style="color:red;" id="tonewPwSid"></span>
		</td>
	</tr>
	<tr>
		<td></td>
		<td> &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="quXiao()" target="mainFrame3" class="btn btn-default" data-toggle="button" style="width:90px;">取&nbsp;消</button>
			 &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="updatePw()" id="updateId" class="btn btn-default" data-toggle="button" style="width:90px;">确&nbsp;定</button>
		</td>
	</tr>
</table>
</form>	
</div>
</div>
</div>
</body>
<script>
	$(function (){
		$("#oldPw").focus(function (){
			$("#oldPwSid").hide();
		})
		$("#newPw").focus(function (){
			$("#newPwSid").hide();
		})
		$("#tonewPw").focus(function (){
			$("#tonewPwSid").hide();
		})
		$("#updateId").click(function(){
			testPw();
			if(testPw()){
				//alert("ok");
				document.getElementById("loginForm").submit();
			}else{
				return false;
			}
		})
	})
	
	function testPw(){
		var pw = $("#pw").val();
		var oldPw = $("#oldPw").val();
		var newPw = $("#newPw").val();
		var tonewPw = $("#tonewPw").val();
		if(oldPw==null||oldPw==""){
			$("#oldPwSid").text("请输入原密码");
			$("#oldPwSid").show();
			return false;
		}else if(pw!=oldPw){
			$("#oldPwSid").text("原密码错误");
			$("#oldPwSid").show();
			return false;
		}
		$("#oldPwSid").hide();
		var b = /^[a-zA-Z0-9]*$/g;	//数字，字母，或组合
		if(newPw==null||newPw==""){
			$("#newPwSid").text("请输入新密码");
			$("#newPwSid").show();
			return false;
		}else if(newPw==oldPw){
			$("#newPwSid").text("不能与原密码一致");
			$("#newPwSid").show();
			return false;
		}else if(newPw.length<6||newPw.length>20){
			$("#newPwSid").text("密码长度为6-20位字符");
			$("#newPwSid").show();
			return false;
		}else if(!newPw.match(b)){
			$("#newPwSid").text("密码包括符号，字母，数字或组合");
			$("#newPwSid").show();
			return false;
		}
		$("#newPwSid").hide();
		if(tonewPw==null||tonewPw==""){
			$("#tonewPwSid").text("请再次输入密码");
			$("#tonewPwSid").show();
			return false;
		}else if(tonewPw!=newPw){
			$("#tonewPwSid").text("两次输入的密码不一致，请重新输入");
			$("#tonewPwSid").show();
			return false;
		}
		$("#tonewPwSid").hide();
		
		return true;
	}

	
	/* 取消  quXiao */
	function quXiao(){
		location.href="<%=request.getContextPath()%>/teacherPC/zuoWenGuanLi/zwgl_center.jsp";
	}
</script>
</html>