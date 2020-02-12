<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看轮播图</title>
<jsp:include page="../../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">
<c:forEach items="${lunbo}" var="l">
<div style="display:inline;">
<span>轮播图管理>${l.NAME}</span>
</div>
</c:forEach>
<div style="display:inline;float:right;">
	<a href="<%=request.getContextPath()%>/servlet/ManageLunboServlet?do=1" target="mainFrame" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="fwform" class="form-inline" role="form">
<table style="border-collapse:separate;padding-left:500px;border-spacing:0px 10px;">
<c:forEach items="${lunbo}" var="l">
	<tr>
		<td style="text-align:right;">轮播图名称：</td>
		<td>
			<input type="text" name="name" id="name" value="${l.NAME}" class="form-control" style="width:178px;height:19px;" placeholder="请输入轮播图名称">
			<span id="sfwTitle" style="color:red;"></span>
		</td>
	</tr>
	<tr>
		<td style="text-align:right;vertical-align:top;">位置编号：</td>
		<td>
			<input type="radio" name="position" class="form-control" value="1" ${l.POSITION==1?"checked":""}>位置1&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="position" class="form-control" value="2" ${l.POSITION==2?"checked":""}>位置2<br>
			<input type="radio" name="position" class="form-control" value="3" ${l.POSITION==3?"checked":""}>位置3&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="position" class="form-control" value="4" ${l.POSITION==4?"checked":""}>位置4<br>
			<input type="radio" name="position" class="form-control" value="5" ${l.POSITION==5?"checked":""}>位置5
		</td>
	</tr>
	<tr>
		<td style="text-align:right;vertical-align:top;">轮播图片：</td>
	    <td>
	    	<div>
	    		<img src="${img}" width="750" height="250">
				<input type="hidden" id="type" value="${l.TYPE}">
	    	</div>
	    </td>
	</tr>
	<tr>
		<td style="text-align:right;vertical-align:top;">操作类型：</td>
	    <td>${l.TYPE=='1'?"URL":""}${l.TYPE=='2'?"内容":""}</td>
	</tr>
	<tr id="urlId">
		<td style="text-align:right;">连接：</td>
		<td>
	    	<a href="${l.URL}" target="view_window">${l.URL}</a>
	    </td>
	</tr>
	<tr id="contentId">
		<td style="text-align:right;vertical-align:top;">内容：</td>
		<td style="width:200px;">
	    	${l.CONTENT}
	    </td>
	</tr>
</c:forEach>
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
		$("#urlId").hide();
		$("#contentId").hide();
		if($("#type").val()=="1"){
			$("#urlId").show();
			$("#contentId").hide();
		}
		if($("#type").val()=="2"){
			$("#contentId").show();
			$("#urlId").hide();
		}
	})
	//发布
	function addLunbo(){
		$("#fwform").validate({
			rules:{
				name:{
					required:true,
					rangelength:[1,50]
				}
			},
			messages : {
				name:{
					required:"请输入轮播图名称",
					rangelength: "标题：1-50字之间。"
				}
			},
			submitHandler: function(form){
			   	alert("提交事件！");
			    form.submit();
			   	//var data = $("#fwform").serialize();
			   	
			} 
		})
	}
	//暂存
	function addZancun(){
		addLunbo();
	}
</script>

<!-- 取消  quXiao  -->
<script>
	function quXiao(){
		location.href="<%=request.getContextPath()%>/ziYuanGuanLi/fanWen/fanWen.jsp";
	}
</script>

</html>