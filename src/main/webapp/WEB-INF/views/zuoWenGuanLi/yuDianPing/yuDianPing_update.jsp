<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>预点评作文>未点评>修改</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<div style="display:inline;">
<span>预点评作文>未点评>修改作文</span>
</div>

<div style="display:inline;float:right;">
<a href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=7" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="ydpform" action="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=10" method="post" class="form-inline" role="form">
<table align="center" style="border-collapse:separate; border-spacing:0px 10px;">
	<c:forEach items="${wdp}" var="w">
	<input type="hidden" name="id" value="${w.id}"/>
	<input type="hidden" name="comId" value="${w.comId}"/>
	<tr>
		<td align="right">作文标题：</td>
		<td>
			<input type="hidden" name="oldTitle" value="${w.newTitle}"/>
			<input type="text" name="newTitle" value="${w.newTitle}" id="ydpTitle" class="form-control" style="width:178px;height:19px;" placeholder="请输入作文标题。">&nbsp;&nbsp;
		</td>
	</tr>
	<tr>
		<td align="right">作文内容：</td>
		<td>
			<div>
		    <textarea id="editor" name="content" type="text/plain" style="width:800px;height:300px;">${w.content}</textarea>
			</div>
		</td>
	</tr>
	<tr>
		<td>年级：</td>
	    <td>
	    <select name="ydpGrade" id="ydpGrade" class="form-control" style="width:205px;">
	   		<option value="" style="color:#d6d6d6;">请选择年级</option>
	      	<option value="一年级" ${w.geade=='一年级'?"selected":""}>一年级</option>
	      	<option value="二年级" ${w.geade=='二年级'?"selected":""}>二年级</option>
	      	<option value="三年级" ${w.geade=='三年级'?"selected":""}>三年级</option>
	      	<option value="四年级" ${w.geade=='四年级'?"selected":""} >四年级</option>
	      	<option value="五年级" ${w.geade=='五年级'?"selected":""}>五年级</option>
	      	<option value="六年级" ${w.geade=='六年级'?"selected":""}>六年级</option>
	      	<option value="初一" ${w.geade=='初一'?"selected":""}>初一</option>
	      	<option value="初二" ${w.geade=='初二'?"selected":""}>初二</option>
	      	<option value="初三" ${w.geade=='初三'?"selected":""}>初三</option>
	      	<option value="高一" ${w.geade=='高一'?"selected":""}>高一</option>
	      	<option value="高二" ${w.geade=='高二'?"selected":""}>高二</option>
	      	<option value="高三" ${w.geade=='高三'?"selected":""}>高三</option>
	    </select>
	    </td>
	</tr>
	<!-- <tr>
		<td align="right">点评老师：</td>
		<td>
			<input type="text" name="ydpTeacher" id="ydpTeacher" class="form-control" style="width:178px;height:19px;display:none;">&nbsp;&nbsp;
			<a href="#" onclick="teacher()" style="color:#0000ff;text-decoration:none;">选择老师</a>
		</td>
	</tr> -->
	</c:forEach>
	<tr>
		<td></td>
		<td> &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="quXiao()" class="btn btn-default" data-toggle="button" style="width:90px;">取&nbsp;消</button>
			 &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="addYuDianPing()" class="btn btn-default" data-toggle="button" style="width:90px;">发&nbsp;布</button>
		</td>
	</tr>
</table>
</form>

</div>
</div>
</div>

<!-- 选择老师  $('#teacherdiv') -->
<div id="teacherdiv" style="display: none">
	<form class="form-inline" role="form">
		<div class="form-group" style="display:inline;">
			<div style="display:inline;float: right;">
			  <input type="text" name="" class="form-control" style="width:178px;height:19px;" placeholder="老师姓名/用户名">
			  <button class="btn btn-info" type="button">查询</button>
			</div>
		</div>
		<br><br>
	</form>
	<table class="table table-hover" style="width: 100%;">
		<thead>
		<tr class="active" align="center">
			<td style="width:10px"></td>
			<td>用户名</td>
			<td>老师姓名</td>
			<td>注册时间</td>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td style="text-align: center;width:10px"><input type="checkbox" name="check"></td>
			<td name="one" style="text-align: center">13000000000</td>
			<td name="two" style="text-align: center">万给</td>
			<td name="three" style="text-align: center">2017-03-20 10:34</td>
		</tr>
		<tr>
			<td style="text-align: center;width:10px"><input type="checkbox" name="check"></td>
			<td name="one" style="text-align: center">13000000001</td>
			<td name="two" style="text-align: center">慢慢</td>
			<td name="three" style="text-align: center">2017-03-20 10:34</td>
		</tr>
		<tr>
			<td style="text-align: center;width:10px"><input type="checkbox" name="check"></td>
			<td name="one" style="text-align: center">13000000002</td>
			<td name="two" style="text-align: center">莉莉</td>
			<td name="three" style="text-align: center">2017-03-20 10:34</td>
		</tr>
		</tbody>
	</table>
	<!-- 分页 -->
	<div class="text-center">
		<ul id="visible-pages-example" class="pagination">
			<li class="first" data-page="1">
				<a href="javascript:search(1)">首页</a>
			</li>
			<li class="prev" data-page="1">
				<a href="javascript:search(2)">上一页</a>
			</li>
			<li class="prev" data-page="10">
				<a>
				当前页<input value="${page.pageIndex }" name="pageIndex" id="pageIndex" size="3" style="height:20px;">页
				每页<input value="${page.pageSize }" name="pageSize" id="pageSize" size="3" style="height:20px;">条
				总${page.totalPage }页 总${page.totalSize }条
				</a>
			</li>
			<li class="next" data-page="2">
				<a href="javascript:search(3)">下一页</a>
			</li>
			<li class="last" data-page="35">
				<a href="javascript:search(4)">末页</a>
			</li>
		</ul>
	</div>
	<!-- /分页 -->
</div>

</body>

<!-- validate表单验证 -->
<style>
.error{
	color:red;
}
</style>
<script type="text/javascript">
	/* 发布  预点评作文  addYuDianPing  */
	function addYuDianPing(){
		$("#ydpform").submit();
	}
		
	/* function addYuDianPing(){
		//var a = $('#zwTitle').val();
		//alert(a);
		$("#ydpform").validate({
			rules:{
				ydpTitle:{
					required:true,
					rangelength:[2,20]
				},
				ydpGrade: {
					required:true
				},
				ydpTeacher:{
					required:true
				},
				editor:{
					required:true
				},
			},
			messages : {
				ydpTitle:{
					required:"请输入范文标题。",
					rangelength: "标题：2-20字之间。"
				},
				ydpGrade:{
					required:"请选择年级。"
				},
				ydpTeacher:{
					required:"请选择点评老师",
				},
				editor:{
					required:"请输入作文内容",
				}
			},
			submitHandler: function(form){
			   	$("#ydpform").submit();
			} 
		})
	} */
	/* 取消  quXiao */
	function quXiao(){
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=7";
	}
	
</script>

<script >
	/* 选择老师  teacher */
	function teacher(){
		var d = dialog({
			title: '选择老师',
			width: '700',
			//height: '35',
			content: $('#teacherdiv'),
			okValue: '确 定',
			ok: function () {
				
			},
			cancelValue: '取消',
			cancel: function () {}
		});
		d.showModal();
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