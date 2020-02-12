<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改范文</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<div style="display:inline;">
<span>资源管理>范文>修改范文</span>
</div>

<div style="display:inline;float:right;">
	<a href="<%=request.getContextPath()%>/servlet/ManageFindFwServlet?do=1" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="fwform" action="<%=request.getContextPath()%>/servlet/ManageFindFwServlet?do=6" method="post" class="form-inline" role="form">
<table align="center" style="border-collapse:separate; border-spacing:0px 10px;">
<c:forEach items="${fanwen}" var="f">
<input type="hidden" name="id" value="${f.id}">
	<tr>
		<td align="right">范文标题：</td>
		<td><input type="text" name="fwTitle" id="fwTitle" value="${f.name}" class="form-control" style="width:178px;height:19px;" placeholder="请输入范文标题"></td>
	</tr>
	<tr>
		<td align="right">作者：</td>
		<td><input type="text" name="fwName" id="fwName" value="${f.author}" class="form-control" style="width:178px;height:19px;" placeholder="请输入作者名称"></td>
	</tr>
	<tr>
		<td>年级：</td>
	    <td>
	    <select name="fwGrade" id="fwGrade" class="form-control" style="width:205px;">
	   		<option value="" style="color:#d6d6d6;">请选择年级</option>
	      	<c:forEach items="${ageDetail}" var="a">
	      	<option value="${a.CODE}" ${f.ageDetail==a.GRADE?'selected':''}>${a.GRADE}</option>
	      	</c:forEach>
	    </select>
	    </td>
	</tr>
	<tr>
		<td>体裁：</td>
		<td>
	    <select name="fwGenre" id="fwGenre" class="form-control" style="width:205px;">
	   		<option value="" style="color:#d6d6d6;">请选择体裁</option>
	      	<c:forEach items="${type}" var="t">
	      		<option value="${t.CODE}" ${t.CODE_NAME==f.type1?'selected':''}>${t.CODE_NAME}</option>
	      	</c:forEach>
	    </select>
	    </td>
	</tr>
	<tr>
		<td valign="top">内容：</td>
		<td>
			<div>
		    <textarea id="editor" name="content" type="text/plain" style="width:800px;height:300px;" required>${f.content}</textarea>
			</div>
		</td>
	</tr>
	</c:forEach>
	<tr>
		<td></td>
		<td> &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="quXiao()" class="btn btn-default" data-toggle="button" style="width:90px;">取&nbsp;消</button>
			 &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="submit" onclick="updateFanwen()" id="addId" class="btn btn-default" data-toggle="button" style="width:90px;">修&nbsp;改</button>
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
	initialFrameHeight: 400,//高度
    autoClearinitialContent:false, //focus时自动清空初始化时的内容
    wordCount:true,    //开启字数统计   false关闭
    maximumWords:2000,   //允许的最大字符数
    elementPathEnabled:false	//关闭elementPath
});
</script>

<!-- validate表单验证 -->
<style>
.error{
	color:red;
}
</style>
<script>
	/* 修改范文  updateFanwen */
	function updateFanwen(){
		//alert($("#fwTitle").val());
		$("#fwform").validate({
			rules:{
				fwTitle:{
					required:true,
					rangelength:[2,30]
				},
				fwName: {
					required:true,
				},
				fwGrade: {
					required:true
				},
				fwGenre:{
					required:true
				},
				content:{
					required:true
				}
			},
			messages : {
				fwTitle:{
					required:"请输入范文标题。",
					rangelength: "标题：2-30字之间。"
				},
				fwName:{
					required:"请输入作者名称。"
				},
				fwGrade:{
					required:"请选择年级。"
				},
				fwGenre:{
					required:"请选择作文体裁",
				},
				content:{
					required:"请输入作文内容",
				}
			},
			submitHandler: function(form){
			   	//alert("提交事件！");
			    $("#fwform").submit();
			   	//var data = $("#fwform").serialize();
			   	
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