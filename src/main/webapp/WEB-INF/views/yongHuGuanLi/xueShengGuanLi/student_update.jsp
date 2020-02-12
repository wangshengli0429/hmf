<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改学生</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<div style="display:inline;">
<span>学生用户管理>修改学生</span>
</div>

<div style="display:inline;float:right;">
<a href="<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=10" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="teacherform" action="<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=12" method="post" class="form-inline cmxform" role="form">
<table align="center" style="border-collapse:separate; border-spacing:0px 10px;">
	<c:forEach items="${student}" var="s">
	<input type="hidden" name="id" value="${s.id}"/>
	<tr>
		<td align="right">用户名：</td>
		<td>${s.phone}</td>
	</tr>
	<tr>
		<td align="right">昵称：</td>
		<td><input type="text" name="userName" id="userName" value="${s.userName}" class="form-control" style="width:178px;height:19px;"></td>
	</tr>
	<tr>
		<td align="right">姓名：</td>
		<td><input type="text" name="name" id="name" value="${s.name}" class="form-control" style="width:178px;height:19px;"></td>
	</tr>
	<tr>
		<td align="right">性别：</td>
		<td>
			<select name="sex" id="sex" class="form-control" style="width:205px;">
	   			<option value="">请选择</option>
	   			<option value="男" ${s.sex=="男"?"selected":""}>男</option>
	   			<option value="女" ${s.sex=="女"?"selected":""}>女</option>
	   		</select>
		</td>
	</tr>
	<tr>
		<td align="right">学校：</td>
		<td><input type="text" name="school" id="school" value="${s.school}" class="form-control" style="width:178px;height:19px;"></td>
	</tr>
	<tr>
		<td align="right">年级：</td>
	    <td>
	    <select name="fwGrade" id="fwGrade" class="form-control" style="width:205px;">
	   		<option value="" style="color:#d6d6d6;">请选择年级</option>
	      	<c:forEach items="${ageDetail}" var="a">
	      	<option value="${a.CODE}" ${a.CODE==s.grade?'selected':''}>${a.GRADE}</option>
	      	</c:forEach>
	    </select>
	    </td>
	</tr>
	<tr>
		<td align="right">省市：</td>
		<td>
		    <select id="district" onchange="sheng(this.value)" class="form-control" style="width:178px;">
	        	<option value="-1">请选择</option>
	        	<c:forEach items="${districts}" var="district">
	            <option value="${district.id}" ${s.sheng==district.name?'selected':''}>${district.name}</option>
	        	</c:forEach>
		    </select>
		    <select id="street" name="shi1" onchange="shi(this.value)" class="form-control" style="width:178px;display:none;">
		    	<option value="-1">请选择</option>
		    	<c:forEach items="${shiList}" var="shi">
	            <option value="${shi.id}" ${s.shi==shi.name?'selected':''}>${shi.name}</option>
	        	</c:forEach>
		    </select>
		    <select id="street2" name="xian1" class="form-control" style="width:178px;display:none">
		    	<option value="-1">请选择</option>
		    	<c:forEach items="${xianList}" var="x">
	            <option value="${x.id}" ${s.xian==x.name?'selected':''}>${x.name}</option>
	        	</c:forEach>
		    </select>
		    <input type="hidden" name="shengshi1" id="shengshi1" value="${s.sheng}"/>
		    <input type="hidden" name="shengshi2" id="shengshi2" value="${s.shi}"/>
		    <input type="hidden" name="shengshi3" id="shengshi3" value="${s.xian}"/>
	    </td>
	</tr>
	<tr>
		<td align="right">注册时间：</td>
		<td>${s.createTime}</td>
	</tr>
	</c:forEach>
	<tr>
		<td></td>
		<td> &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="quXiao()" class="btn btn-default" data-toggle="button" style="width:90px;">取&nbsp;消</button>
			 &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="submit" onclick="return updateStudent()" class="btn btn-default" data-toggle="button" style="width:90px;">确&nbsp;定</button>
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
	$(function(){
		var shi = $("#shengshi2").val();
		var xian = $("#shengshi3").val();
		if(shi!=null||shi!=""){
			document.getElementById("street").style.display='';
		}
		if(xian!=null||xian!=""){
			document.getElementById("street2").style.display='';
		}
	})
	
	/* 修改 保存  验证 */
	function updateStudent(){
		var a = $("#district").val();
		if(a==-1){
			alert("请选择省份");
			return false;
		}
		$("#teacherform").validate({
			rules:{
				userName: {
					required:true,
					minlength: 1,
				},
				name:{
					required:true,
					minlength: 1,
					maxlength: 20,
				},
				sex: {
					required:true,
				},
				school: {
					required:true,
					minlength: 1,
					maxlength: 20,
				},
				fwGrade: {
					required:true,
				},
			},
			messages : {
				userName: {
					required:"请输入学生昵称1-20个字之间。",
					minlength: "不得少于1个字",
					maxlength: "不得多于20个字",
				},
				name:{
					required:"请输入学生姓名1-20个字之间。",
					minlength: "不得少于1个字",
					maxlength: "不得多于20个字",
				},
				sex:{
					required:"请选择性别。",
				},
				school:{
					required:"请输入学校",
					minlength: "不得少于1个字",
					maxlength: "不得多于20个字",
				},
				fwGrade:{
					required:"请选择年级。",
				},
			},
			submitHandler: function(form){
				var shengshi1 = $("#district").find("option:selected").text();
				var shengshi2 = $("#street").find("option:selected").text();
				var shengshi3 = $("#street2").find("option:selected").text();
				$("#shengshi1").val(shengshi1);
				$("#shengshi2").val(shengshi2);
				$("#shengshi3").val(shengshi3);
				return true;
				//$("#teacherform").submit();
			} 
		})
	}
	
	/* 取消  quXiao */
	function quXiao(){
		location.href="<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=10";
	}
	
	/* 省市  二级联动 */
	function sheng(id){
		if(id==-1){
			document.getElementById("street").style.display='none';
			document.getElementById("street2").style.display='none';
		}
		if(id>0){
			$.ajax({
				type:"post",
				url:"<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=0&id="+id,
				async:false,
				success:function(json){
					//alert(json);  //接受的是表单
					if(json!=null||json!=""){
						document.getElementById("street").style.display='';
						document.getElementById("street2").style.display='none';
						document.getElementById("street").innerHTML=json;
					}else{
						document.getElementById("street").style.display='none';
						document.getElementById("street2").style.display='none';
					}
				}
			})
		}
	}
	function shi(id){
		if(id==-1){
			document.getElementById("street2").style.display='none';
		}
		if(id>0){
			$.ajax({
				type:"post",
				url:"<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=0&id="+id,
				async:false,
				success:function(json){
					//alert(json);  //接受的是表单
					if(json!=null||json!=""){
						document.getElementById("street").style.display='';
						document.getElementById("street2").style.display='';
						document.getElementById("street2").innerHTML=json;
					}else{
						document.getElementById("street").style.display='none';
						document.getElementById("street2").style.display='none';
					}
				}
			})
		}
	}
</script>

</html>