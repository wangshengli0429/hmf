<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加推荐作文</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">

<div id="fade" style="display: none; position: absolute; top: 0%; left: 0%; width: 100%; height: 100%; background-color: black; z-index:1001; -moz-opacity: 0.6; opacity:.60; filter: alpha(opacity=60);"> 
	<div style="position: absolute; top: 20%; left: 80%; padding: 0px; border: 0px solid orange; z-index:1003; overflow: auto;">
        <div style="display:inline;" onclick="picClose()">
        	<img src="<%=request.getContextPath()%>/images/guanbi-.png" style="width:100%;height:100%;"/>
        </div>
	</div>
</div>

<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<div style="display:inline;">
<span>作文管理>已点评作文>添加推荐作文</span>
</div>

<div style="display:inline;float:right;">
<a href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=3" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="tjform" action="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=38" method="post" class="form-inline cmxform" role="form">
<table align="center" style="border-collapse:separate; border-spacing:0px 10px;">
<c:forEach items="${list}" var="list">
	<tr>
		<td align="right">作文标题：</td>
		<td>
			<input type="hidden" name="zwId" value="${list.id}" id="zwId">
			<input type="text" name="zwTitle" value="${list.title}" id="zwTitle" class="form-control" style="width:178px;height:19px;" placeholder="请输入作文标题。">&nbsp;&nbsp;
		</td>
	</tr>
	<tr>
		<td align="right">作文内容：</td>
		<td>
			<textarea id="spanId" style="display: none;">${list.content}</textarea>
			<div>
		    <textarea id="editor" name="content" type="text/plain" style="width:800px;height:300px;">${list.content}</textarea>
			</div>
			
			<input type="hidden" id="img1" value="${list.img1}">
			<input type="hidden" id="img2" value="${list.img2}">
			<input type="hidden" id="img3" value="${list.img3}">
			<!-- // 查看图片 // -->
		   	<a id="imga" href='javascript:void(0);' onclick='picBig1()'><img id="i1" src="${list.img1}" style="width:100px;height:100px;"></a>
		   	<a id="imgb" href='javascript:void(0);' onclick='picBig2()'><img id="i2" src="${list.img2}" style="width:100px;height:100px;"></a>
		   	<a id="imgc" href='javascript:void(0);' onclick='picBig3()'><img id="i3" src="${list.img3}" style="width:100px;height:100px;"></a>
			<!-- // 查看图片 // left:200px;-->
			<div id="divCenter1" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; background-color: white; z-index:1002; overflow: auto;">
		        <div style="display:inline;">
		        	<img src="${list.img1}" style="width:100%;height:100%;"/>
		        </div>
		    </div>
		    <div id="divCenter2" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; background-color: white; z-index:1002; overflow: auto;">
		        <div style="display:inline;">
		        	<img src="${list.img2}" style="width:100%;height:100%;"/>
		        </div>
		    </div>
		    <div id="divCenter3" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; background-color: white; z-index:1002; overflow: auto;">
		        <div style="display:inline;">
		        	<img src="${list.img3}" style="width:100%;height:100%;"/>
		        </div>
		    </div>
			<!-- // 查看图片 // -->
	
		</td>
	</tr>
	<tr>
		<td align="right">作者昵称：</td>
		<td><input type="text" name="zwName" value="${list.author}" id="zwName" class="form-control" style="width:178px;height:19px;" placeholder="请输入作者昵称。"></td>
	</tr>
	<tr>
		<td>年级：</td>
	    <td>
	    <select name="zwGrade" id="zwGrade" class="form-control" style="width:205px;">
	   		<option value="" style="color:#d6d6d6;">请选择年级</option>
	      	<c:forEach items="${geadelist}" var="g">
	    		<option value="${g.grade}" ${list.age_detail==g.grade?"selected":""}>${g.grade}</option>
	      	</c:forEach>
	    </select>
	    </td>
	</tr>
	<tr>
		<td>体裁：</td>
	    <td>
	    <select name="zwStyle" id="zwStyle" class="form-control" style="width:205px;">
	   		<option value="" style="color:#d6d6d6;">请选择体裁</option>
	      	<c:forEach items="${stylelist}" var="s">
	    		<option value="${s.CODE}" ${list.style==s.CODE?"selected":""}>${s.CODE_NAME}</option>
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
			<button type="submit" onclick="return addTuiJian()" class="btn btn-default" data-toggle="button" style="width:90px;">发&nbsp;布</button>
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
	var img1 = $("#img1").val();
	var img2 = $("#img2").val();
	var img3 = $("#img3").val();
	
	$(function(){
		if(img1.length==0){
			$("#imga").hide();//表示display:block, 
		}
		if(img2.length==0){
			$("#imgb").hide();
		}
		if(img3.length==0){
			$("#imgc").hide();
		}
		
		var a = $("#spanId").text();
		if(a.length==0){
			document.getElementById('editor').style.display='none';
		}
		//alert(a);
		//UE.getEditor('editor').setContent(a);
		UE.getEditor('editor').execCommand( 'inserthtml', a);
	})
	
	/* 发布  推荐作文   addTuiJian */
	function addTuiJian(){
		if(img1.length==0&&img2.length==0&&img3.length==0){
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
					content:{
						required:true
					},
					zwName:{
						required:true
					},
					zwGrade: {
						required:true
					},
					zwStyle:{
						required:true
					},
				},
				messages : {
					zwTitle:{
						required:"请输入标题。",
						rangelength: "标题：1-30字之间。"
					},
					content:{
						required:"请输入作文内容",
					},
					zwName:{
						required:"请输入作者",
					},
					zwGrade:{
						required:"请选择年级。"
					},
					zwStyle:{
						required:"请选择体裁",
					},
				},
				submitHandler: function(form){
					return true;
					//form.submit();
				} 
			})
		}else{
			$("#tjform").validate({
				rules:{
					zwTitle:{
						required:true,
						rangelength:[1,30]
					},
					zwName:{
						required:true
					},
					zwGrade: {
						required:true
					},
					zwStyle:{
						required:true
					},
				},
				messages : {
					zwTitle:{
						required:"请输入标题。",
						rangelength: "标题：1-30字之间。"
					},
					zwName:{
						required:"请输入作者",
					},
					zwGrade:{
						required:"请选择年级。"
					},
					zwStyle:{
						required:"请选择体裁",
					}
				},
				submitHandler: function(form){
					return true;
					//form.submit();
				} 
			})
		}
		
	}
	
	/* 取消  quXiao */
	function quXiao(){
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=14";
	}
</script>

<!-- // 查看图片 // -->
<script>
	function picBig1() {
    	picClose();
    	document.getElementById('fade').style.display='block';
        document.getElementById("divCenter1").style.display="block";
    }
    function picBig2() {
    	picClose();
    	document.getElementById('fade').style.display='block';
        document.getElementById("divCenter2").style.display="block";
    }
    function picBig3() {
    	picClose();
    	document.getElementById('fade').style.display='block';
        document.getElementById("divCenter3").style.display="block";
    }
    /* 点击大图关闭 */
    function picClose() {
        document.getElementById("divCenter1").style.display="none";
        document.getElementById("divCenter2").style.display="none";
        document.getElementById("divCenter3").style.display="none";
        document.getElementById('fade').style.display='none';
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
    elementPathEnabled:false,//关闭elementPath
});
/* ue.addListener('wordcountoverflow',function(){
	var chars = ue.getContentTxt();
	var schars = chars.substring(0,maximumWords);
	UE.getEditor('editor').setContent(schars);
}) */
</script>

</html>