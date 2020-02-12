<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改轮播图</title>
<jsp:include page="../../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">
<c:forEach items="${lunbo}" var="l">
<div style="display:inline;">
<span>轮播图管理>修改轮播</span>
</div>
</c:forEach>
<div style="display:inline;float:right;">
	<a href="<%=request.getContextPath()%>/servlet/ManageLunboServlet?do=1" target="mainFrame" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="lbform" action="<%=request.getContextPath()%>/servlet/ManageLunboServlet?do=5" enctype="multipart/form-data" method="post" class="form-inline" role="form">
<input type="hidden" name="storage" id="storage" value="">
<table style="border-collapse:separate;padding-left:500px;border-spacing:0px 10px;">
<c:forEach items="${lunbo}" var="l">
	<tr>
		<td style="text-align:right;">轮播图名称：</td>
		<td>
			<input type="hidden" name="id" value="${l.ID}">
			<input type="hidden" name="oldPosition" value="${l.POSITION}">
			<input type="hidden" name="state" id="state" value="${l.STATE}">
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
	    		<img src="${img}" id="show" style ="width: 200px;">
	    		<input type="file" id="image" name="image" onchange="c()" value="点击上传">
	    	</div>
	    </td>
	</tr>
	<tr>
		<td style="text-align:right;">操作类型：</td>
		<td>
		<input type="hidden" id="type1" value="${l.TYPE}">
	    <select name="type" id="type" class="form-control" style="width:205px;">
	   		<option value="" style="color:#d6d6d6;">请选择</option>
	      	<option value="1" ${l.TYPE=="1"?"selected":""}>URL</option>
	      	<option value="2" ${l.TYPE=="2"?"selected":""}>内容</option>
	    </select>
	    </td>
	</tr>
	<tr id="urlId">
		<td style="text-align:right;">连接：</td>
		<td>
	    	<input type="text" name="contentUrl" value="${l.URL}" class="form-control" style="width:178px;height:19px;" placeholder="请输入URL">
	    </td>
	</tr>
	<tr id="contentId">
		<td style="text-align:right;">内容：</td>
		<td>
			<div>
		    <textarea id="editor" name="content" type="text/plain" style="width:700px;height:300px;">${l.CONTENT}</textarea>
			</div>
	    </td>
	</tr>
	<tr>
		<td></td>
		<td> &nbsp;&nbsp;&nbsp;&nbsp;
			<c:if test="${l.STATE==1}">
			<button type="button" onclick="quXiao()" class="btn btn-default" data-toggle="button" style="width:90px;">取&nbsp;消</button>
			</c:if>
			<c:if test="${l.STATE==2}">
			<button type="button" onclick="temporary()" class="btn btn-default" data-toggle="button" style="width:90px;">暂&nbsp;存</button>
			</c:if>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<button type="submit" onclick="addLunbo()" id="addId" class="btn btn-default" data-toggle="button" style="width:90px;">发&nbsp;布</button>
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
	//图片预览
	function c(){
		var r = new FileReader();
		f = document.getElementById("image").files[0];
		r.readAsDataURL(f);
		r.onload = function(e){
			document.getElementById("show").src = this.result;
		}
	}
	$(function (){
		$("#urlId").hide();
		$("#contentId").hide();
		if($("#type1").val()=="1"){
			$("#urlId").show();
			$("#contentId").hide();
		}
		if($("#type1").val()=="2"){
			$("#contentId").show();
			$("#urlId").hide();
		}
		$("select#type").click(function(){
			if($(this).val()=="1"){
				$("#urlId").show();
				$("#contentId").hide();
			}
			if($(this).val()=="2"){
				$("#contentId").show();
				$("#urlId").hide();
			}
		})
	})
	
	//发布
	function addLunbo(){
		$("#lbform").validate({
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
				$("#storage").val(0);
			    $("#lbform").submit();
			   	//var data = $("#lbform").serialize();
			   	
			} 
		})
	}
	//暂存
	function temporary(){
		$("#storage").val(1);
		$("#lbform").submit();
	}

	//取消  quXiao
	function quXiao(){
		location.href="<%=request.getContextPath()%>/servlet/ManageLunboServlet?do=1";
	}
</script>
<!-- ueditor富文本编辑器 -->
<script>
//实例化编辑器
//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
UE.getEditor('editor', {
	autoHeightEnabled: false,//开启滚动条
	initialFrameHeight: 300,//高度
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