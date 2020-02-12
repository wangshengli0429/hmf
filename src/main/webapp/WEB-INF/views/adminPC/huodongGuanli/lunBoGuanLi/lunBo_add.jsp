<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加轮播图</title>
<jsp:include page="../../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<div style="display:inline;">
<span>轮播图管理>新增轮播</span>
</div>

<div style="display:inline;float:right;">
	<a href="<%=request.getContextPath()%>/servlet/ManageLunboServlet?do=1" target="mainFrame" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="lbform" action="<%=request.getContextPath()%>/servlet/ManageLunboServlet?do=6" method="post" enctype="multipart/form-data" class="form-inline" role="form">
<input type="hidden" name="storage" id="storage" value="">
<table style="border-collapse:separate;padding-left:500px;border-spacing:0px 10px;">
	<tr>
		<td style="text-align:right;">轮播图名称：</td>
		<td>
			<input type="text" name="name" id="name" class="form-control" style="width:178px;height:19px;" placeholder="请输入轮播图名称">
			<span id="sfwTitle" style="color:red;"></span>
		</td>
	</tr>
	<tr>
		<td style="text-align:right;vertical-align:top;">位置编号：</td>
		<td>
			<input type="radio" name="position" class="form-control" value="1">位置1&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="position" class="form-control" value="2">位置2<br>
			<input type="radio" name="position" class="form-control" value="3">位置3&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="position" class="form-control" value="4">位置4<br>
			<input type="radio" name="position" class="form-control" value="5">位置5
		</td>
	</tr>
	<tr>
		<td style="text-align:right;vertical-align:top;">轮播图片：</td>
	    <td>
	    	<div>
	    		<img src="" id="show" style = "width: 200px;">
	    		<input type="file" id="image" name="image" onchange="c()" value="点击上传">
	    	</div>
	    </td>
	</tr>
	<tr>
		<td style="text-align:right;">操作类型：</td>
		<td>
	    <select name="type" id="type" class="form-control" style="width:205px;">
	   		<option value="" style="color:#d6d6d6;">请选择</option>
	      	<option value="1">URL</option>
	      	<option value="2">内容</option>
	    </select>
	    </td>
	</tr>
	<tr id="urlId">
		<td style="text-align:right;">连接：</td>
		<td>
	    	<input type="text" name="contentUrl" id="" class="form-control" style="width:178px;height:19px;" placeholder="请输入URL">
	    </td>
	</tr>
	<tr id="contentId">
		<td style="text-align:right;">内容：</td>
		<td>
			<div>
		    <textarea id="editor" name="content" type="text/plain" style="width:700px;height:300px;"></textarea>
			</div>
			<!-- 
	    	<textarea name="content" rows="5" cols="60" class="form-control" style="border:1px;border-color:#d0d0d0;border-style:solid"></textarea>
	    	 -->
	    </td>
	</tr>
	<tr>
		<td></td>
		<td> &nbsp;&nbsp;&nbsp;&nbsp;
			<!-- <button type="button" onclick="quXiao()" class="btn btn-default" data-toggle="button" style="width:90px;">取&nbsp;消</button> -->
			<button type="submit" onclick="temporary()" class="btn btn-default" data-toggle="button" style="width:90px;">暂&nbsp;存</button>
			 &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="submit" onclick="addLunbo()" id="addId" class="btn btn-default" data-toggle="button" style="width:90px;">发&nbsp;布</button>
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
	//暂存
	function temporary(){
		$("#lbform").validate({
			rules:{
				name:{
					required:true,
					rangelength:[1,50]
				},
				position:{
					required:true
				}
			},
			messages : {
				name:{
					required:"请输入轮播图名称",
					rangelength: "标题：1-50字之间。"
				},
				position:{
					required:"请选择位置",
				}
			},
			submitHandler: function(form){
				var f = document.getElementById("image");
				if(f.value.length==0){
					alert("请选择需上传的轮播图！");
				}else{
					$("#storage").val(1);
					$("#lbform").submit();
				}
			} 
		})
	}
	//发布
	function addLunbo(){
		$("#lbform").validate({
			rules:{
				name:{
					required:true,
					rangelength:[1,50]
				},
				position:{
					required:true
				}
			},
			messages : {
				name:{
					required:"请输入轮播图名称",
					rangelength: "标题：1-50字之间。"
				},
				position:{
					required:"请选择位置",
				}
			},
			submitHandler: function(form){
				var f = document.getElementById("image");
				if(f.value.length==0){
					alert("请选择需上传的轮播图！");
				}else{
					$("#storage").val(0);
					$("#lbform").submit();
				}
			} 
		})
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
    autoClearinitialContent:true, //focus时自动清空初始化时的内容
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