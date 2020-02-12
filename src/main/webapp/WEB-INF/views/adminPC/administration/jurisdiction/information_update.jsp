<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.security.MessageDigest"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理员-修改基本信息</title>
<jsp:include page="../../../inc.jsp"></jsp:include>
<script type="text/ecmascript" src="<%=request.getContextPath()%>/jslib/md5.js"></script>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<div style="display:inline;">
<span>权限管理>修改</span>
</div>
<div style="display:inline;float:right;">
	<a href="<%=request.getContextPath()%>/servlet/AdministrationServlet?do=1" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="qxform" method="post" class="form-inline" role="form">
<c:forEach items="${list}" var="l">
<input type="hidden" name="id" value="${l.id}"/>
<table id="table1" align="center" style="border-collapse:separate; border-spacing:0px 10px;">
	<tr>
		<td align="right">姓名：</td>
		<td>
			<input type="text" name="name" id="name" value="${l.name}" class="form-control" style="width:178px;height:19px;" placeholder="请输入姓名">
			<span id="sname" style="color:red;"></span>
		</td>
	</tr>
	<tr>
		<td align="right">管理员账号：</td>
		<td><input type="text" name="userName" id="userName" value="${l.userName}" class="form-control" style="width:178px;height:19px;" placeholder="请输入账号"></td>
	</tr>
	<tr>
		<td>密码：</td>
	    <td>
		    <input type="password" id="pw" class="form-control" value="" style="width:204px;height:33px;" placeholder="请输入密码">
		    <span id="pwSid" style="">6-20位</span>
		    <input type="hidden" name="pwd" id="pwd"/>
	    </td>
	</tr>
	<tr>
		<td>角色（选填）：</td>
		<td>
			<input type="text" name="role" id="role" value="${l.role}" class="form-control" style="width:178px;height:19px;" placeholder="请输入角色">
			<span id="" style="">如"运营"，"客服"，"财务"。</span>
	    </td>
	</tr>
	<tr>
		<td colspan="2" style="text-align:center">
			<br>
			<button type="button" onclick="quXiao()" class="btn btn-default" data-toggle="button" style="width:90px;">取&nbsp;消</button>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="updateInformation()" class="btn btn-default" data-toggle="button" style="width:90px;">确&nbsp;定</button>
		</td>
	</tr>
</table>
</c:forEach>
</form>

</div>
</div>
</div>
</body>
<script type="text/javascript">
	//修改信息
	function updateInformation(){
		var name = $("#name").val();//姓名
		var userName = $("#userName").val();//账号
		var pw = $("#pw").val();//密码
		if(name==""&&name.length==0){
			dialogs("请输入姓名！");
			return false;
		}
		if(userName==""&&userName.length==0){
			dialogs("请输入管理员账号！");
			return false;
		}
		if(pw!=""&&pw.length>0){
			if(pw.length<6||pw.length>20){
				dialogs("密码长度为6-20位字符！");
				return false;
			}else{
				var hash = hex_md5(pw).toString().toUpperCase();//大写
				$("#pwd").val(hash);
			}
		}
		$.ajax({
			type: "POST",  
			url: "<%=request.getContextPath()%>/servlet/AdministrationServlet?do=8",  
			data: $("#qxform").serialize(),  
			dataType: "text",  
			success: function(data){
				var d = dialog({
					title: '提示',
					width: '300',
					//height: '35',
					content: '<center><h4><b>'+data+'</b></h4></center>',
					okValue: '确 定',
					ok: function () {
						location.href="<%=request.getContextPath()%>/servlet/AdministrationServlet?do=1";
					}
				});
				d.showModal();
			},
			error: function(){
				dialogs("修改失败！");
		    }   
        });
	}
	//弹框
	function dialogs(text){
		var d = dialog({
			title: '提示',
			width: '300',
			//height: '35',
			content: '<center><h4><b>'+text+'</b></h4></center>',
			okValue: '确 定',
			ok: function () {}
		});
		d.showModal();
	}
	//取消
	function quXiao(){
		location.href="<%=request.getContextPath()%>/servlet/AdministrationServlet?do=1";
	}
</script>
</html>