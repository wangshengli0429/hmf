<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加范文</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<div style="display:inline;">
<span>资源管理>范文>添加范文</span>
</div>

<div style="display:inline;float:right;">
	<a href="<%=request.getContextPath()%>/servlet/ManageFindFwServlet?do=1" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="fwform" action="<%=request.getContextPath()%>/servlet/ManageFindFwServlet?do=3" method="post" class="form-inline" role="form">
<table align="center" style="border-collapse:separate; border-spacing:0px 10px;">
	<tr>
		<td align="right">范文标题：</td>
		<td>
			<input type="text" name="name" id="name" class="form-control" style="width:178px;height:19px;" placeholder="请输入范文标题">
			<span id="sname" style="color:red;"></span>
		</td>
	</tr>
	<tr>
		<td align="right">作者：</td>
		<td><input type="text" name="author" id="author" class="form-control" style="width:178px;height:19px;" placeholder="请输入作者名称"></td>
	</tr>
	<tr>
		<td>年级：</td>
	    <td>
	    <select name="ageDetail" id="ageDetail" class="form-control" style="width:205px;">
	   		<option value="" style="color:#d6d6d6;">请选择年级</option>
	      	<c:forEach items="${ageDetail}" var="a">
	      	<option value="${a.CODE}">${a.GRADE}</option>
	      	</c:forEach>
	    </select>
	    </td>
	</tr>
	<tr>
		<td>体裁：</td>
		<td>
	    <select name="type" id="type" class="form-control" style="width:205px;">
	   		<option value="" style="color:#d6d6d6;">请选择体裁</option>
	      	<c:forEach items="${type}" var="t">
	      	<option value="${t.CODE}">${t.CODE_NAME}</option>
	      	</c:forEach>
	    </select>
	    </td>
	</tr>
	<tr>
		<td valign="top">内容：</td>
		<td>
			<div>
		    <textarea id="editor" name="content" type="text/plain" style="width:800px;height:300px;" required><span style="color:#d6d6d6;">请输入作文内容，字数为1-2000字之间。</span></textarea>
			</div>
		</td>
	</tr>
	<tr>
		<td></td>
		<td> &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="quXiao()" class="btn btn-default" data-toggle="button" style="width:90px;">取&nbsp;消</button>
			 &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="submit" onclick="return addFanwen()" id="addId" class="btn btn-default" data-toggle="button" style="width:90px;">确&nbsp;定</button>
		</td>
	</tr>
</table>
</form>
 
</div>
</div>
</div>
</body>

<!-- ueditor富文本编辑器 -->
<script>
//实例化编辑器
//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
UE.getEditor('editor', {
	autoHeightEnabled: false,//开启滚动条
    autoClearinitialContent:true, //focus时自动清空初始化时的内容
    wordCount:true,    //开启字数统计   false关闭
    maximumWords:2000,   //允许的最大字符数
    elementPathEnabled:false,	//关闭elementPath
    initialFrameHeight: 400,  //高度
    autoFloatEnabled:false//浮动
});
</script>

<!-- validate表单验证 -->
<style>
.error{
	color:red;
}
</style>
<script>
	function addFanwen(){
		var ue = UE.getEditor('editor');
		var b = ue.hasContents();
		if(!b){
			alert("请输入正文后再提交");
			return false;
		}
		$("#fwform").validate({
			rules:{
				name:{
					required:true,
					rangelength:[1,30]
				},
				author: {
					required:true,
				},
				ageDetail: {
					required:true
				},
				type:{
					required:true
				},
				content:{
					required:true
				},
			},
			messages : {
				name:{
					required:"请输入范文标题。",
					rangelength: "标题：1-30字之间。"
				},
				author:{
					required:"请输入作者名称。"
				},
				ageDetail:{
					required:"请选择年级。"
				},
				type:{
					required:"请选择作文体裁",
				},
				content:{
					required:"请输入作文内容",
				},
			},
			submitHandler: function(form){
			  	return true;
			}
		})
	}
</script>

<!-- 取消  quXiao  -->
<script>
	function quXiao(){
		location.href="<%=request.getContextPath()%>/servlet/ManageFindFwServlet?do=1";
	}
</script>

</html>