<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改作文（推荐）</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<div style="display:inline;">
<span>作文管理>作文推荐>修改作文</span>
</div>

<div style="display:inline;float:right;">
<a href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=14" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="tjform" action="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=17" method="post" class="form-inline cmxform">
<table align="center" style="border-collapse:separate; border-spacing:0px 10px;">
	<c:forEach items="${tuijian}" var="t">
	<input type="hidden" name="id" value="${t.id}"/>
	<tr>
		<td align="right">作文标题：</td>
		<td>
			<input type="text" name="zwTitle" id="zwTitle" value="${t.name}" class="form-control" style="width:178px;height:19px;" placeholder="请输入作文标题">&nbsp;&nbsp;
		</td>
	</tr>
	<tr>
		<td align="right">作文内容：</td>
		<td>
			<div>
		    <textarea id="editor" name="content" type="text/plain" style="width:800px;height:300px;" required>${t.content}</textarea>
			</div>
		</td>
	</tr>
	<tr>
		<td align="right">作者昵称：</td>
		<td><input type="text" name="zwName" id="zwName" value="${t.author}" class="form-control" style="width:178px;height:19px;" placeholder="请输入作者昵称"></td>
	</tr>
	<tr>
		<td>年级：</td>
	    <td>
	    <select name="zwGrade" id="zwGrade" class="form-control" style="width:205px;">
	   		<option value="" style="color:#d6d6d6;">请选择年级</option>
	      	<c:forEach items="${geadelist}" var="g">
	    		<option value="${g.code}" ${t.ageDetail==g.grade?"selected":""}>${g.grade}</option>
	      	</c:forEach>
	      	<%-- <c:forEach items="${geadelist2}" var="g2">
	    		<option value="${g2.code}" ${t.ageDetail==g2.grade?"selected":""}>${g2.grade}</option>
	      	</c:forEach> --%>
	    </select>
	    </td>
	</tr>
	<tr>
		<td>体裁：</td>
	    <td>
	    <select name="zwStyle" id="zwStyle" class="form-control" style="width:205px;">
	   		<option value="" style="color:#d6d6d6;">请选择体裁</option>
	      	<c:forEach items="${stylelist}" var="s">
	    		<option value="${s.CODE}" ${t.style==s.CODE?"selected":""}>${s.CODE_NAME}</option>
	      	</c:forEach>
	    </select>
	    </td>
	</tr>
	</c:forEach>
	<tr>
		<td></td>
		<td> &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="quXiao()" class="btn btn-default" data-toggle="button" style="width:90px;">取&nbsp;消</button>
			 &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="submit" onclick="return updateTuiJian()" class="btn btn-default" data-toggle="button" style="width:90px;">修&nbsp;改</button>
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
<script type="text/javascript">
	/* 修改  推荐作文  updateTuiJian */
	function updateTuiJian(){
		var ue = UE.getEditor('editor');
		var b = ue.hasContents();
		if(!b){
			alert("请输入正文后再提交");
			return false;
		}
		$("#tjform").validate({
			rules:{
				zwTitle:{
					required:true,
					rangelength:[1,30]
				},
				zwGrade: {
					required:true
				},
				content:{
					required:true
				},
			},
			messages : {
				zwTitle:{
					required:"请输入标题。",
					rangelength: "标题：1-30字之间。"
				},
				zwGrade:{
					required:"请选择年级。"
				},
				content:{
					required:"请输入作文内容",
				}
			},
			submitHandler: function(form){
				return true;
			    //$("#tjform").submit();
			   	//var data = $("#zwform").serialize();
			   	
			} 
		})
	}
	
	/* 取消  quXiao */
	function quXiao(){
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=14";
	}
	
</script>

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
    elementPathEnabled:false//关闭elementPath
});
/* ue.addListener('wordcountoverflow',function(){
	var chars = ue.getContentTxt();
	var schars = chars.substring(0,maximumWords);
	UE.getEditor('editor').setContent(schars);
}) */
</script>

</html>