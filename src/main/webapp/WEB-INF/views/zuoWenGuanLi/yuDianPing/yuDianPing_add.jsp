<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>预点评作文>未点评>上传作文</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body>
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<div style="display:inline;">
<span>预点评作文>未点评>上传作文</span>
</div>

<div style="display:inline;float:right;">
<a href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=7" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="ydpform" action="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=13" method="post" class="form-inline" role="form">
<table align="center" style="border-collapse:separate; border-spacing:0px 10px;">
	<tr>
		<td align="right">作文标题：</td>
		<td>
			<input type="text" name="ydpTitle" id="ydpTitle" class="form-control" style="width:178px;height:19px;" placeholder="请输入作文标题。">&nbsp;&nbsp;
			<a href="#" onclick="addQita()" style="color:#0000ff;text-decoration:none;">其他作文</a>
		</td>
	</tr>
	<tr>
		<td align="right">作文内容：</td>
		<td>
			<div>
		    <textarea id="editor" name="content" type="text/plain" style="width:800px;height:300px;"><span style="color:#d6d6d6;">请输入作文内容。</span></textarea>
			</div>
		</td>
	</tr>
	<tr>
		<td>年级：</td>
	    <td>
	    <select name="ydpGrade" id="ydpGrade" class="form-control" style="width:205px;">
	   		<option value="" style="color:#d6d6d6;">请选择年级</option>
	    	<c:forEach items="${geadelist}" var="g">
	    		<option value="${g.grade}">${g.grade}</option>
	      	</c:forEach>
	      	<!-- <option value="1">一年级</option>
	      	<option value="2">二年级</option>
	      	<option value="3">三年级</option>
	      	<option value="4">四年级</option>
	      	<option value="5">五年级</option>
	      	<option value="6">六年级</option>
	      	<option value="7">七年级</option>
	      	<option value="8">八年级</option>
	      	<option value="9">九年级</option>
	      	<option value="10">高一</option>
	      	<option value="11">高二</option>
	      	<option value="12">高三</option> -->
	    </select>
	    </td>
	</tr>
	<tr>
		<td align="right">点评老师：</td>
		<td>
			<input type="text" name="ydpTeacher" id="ydpTeacher" class="form-control" style="width:178px;height:19px;display:none;">&nbsp;&nbsp;
			<a href="#" onclick="teacher()" style="color:#0000ff;text-decoration:none;">选择老师</a>
		</td>
	</tr>
	<tr>
		<td></td>
		<td> &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="quXiao()" class="btn btn-default" data-toggle="button" style="width:90px;">取&nbsp;消</button>
			 &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="submit" onclick="addYuDianPing()" class="btn btn-default" data-toggle="button" style="width:90px;">发&nbsp;布</button>
		</td>
	</tr>
</table>
</form>

</div>
</div>
</div>

	<!--  其他作文 $('#dialogdiv') -->
	<div id="dialogdiv" style="display: none">
		<form class="form-inline" role="form">
		<div class="form-group" style="display:inline;">
			<div style="display:inline;float: right;">
			  <input type="text" name="" class="form-control" style="width:178px;height:19px;" placeholder="作文名称">
			  <button class="btn btn-info" type="button">查询</button>
			</div>
		</div>
		<br><br>
	</form>
	<table class="table table-hover" style="width: 100%;">
		<thead>
		<tr class="active" align="center">
			<td style="width:10px"></td>
			<td>作文名称</td>
			<td>年级</td>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td style="text-align: center;width:10px"><input type="radio" name="radio"></td>
			<td name="one" style="text-align: center"><a href="##" onclick="selectWeiDianPing()">美丽的心</a></td>
			<td name="three" style="text-align: center">一年级</td>
		</tr>
		<tr>
			<td style="text-align: center;width:10px"><input type="radio" name="radio"></td>
			<td name="one" style="text-align: center"><a href="##" onclick="selectWeiDianPing()">美丽的心</a></td>
			<td name="three" style="text-align: center">一年级</td>
		</tr>
		<tr>
			<td style="text-align: center;width:10px"><input type="radio" name="radio"></td>
			<td name="one" style="text-align: center"><a href="##" onclick="selectWeiDianPing()">美丽的心</a></td>
			<td name="three" style="text-align: center">一年级</td>
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

<!-- 查看 $('#selectdiv') -->
<div id="selectdiv" style="display: none">
	<center><h4>美丽的心</h4><p style="color:#c2c2c2;">五年级</p></center>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	在这个晴空万里的早上，我像往常一样去吃饭，妹妹突然跳了下来说：姐妈妈叫你！我看到妹妹的那种表情，肯定不
	是什么好事！妈妈把我叫到床头前告诉我说：你知道你的成绩吗？！发下来了！你说会努力会努力！不错真不错考了个倒
	数第三名啊！！不单没进步还倒退一个名次！我听到这个消息我大吃一惊，     刚刚的晴空万里如今现在的电闪雷鸣当时
	我还不相信这个事实，现在果真就是这个分数，如果再让我认认真真的考一次，那成绩有可能没那么糟了！我想自己的水
	平本来就不好就算重来一次也未免也能考好吧！如今现在离新年已经不远了，我告诉自己对你什么不好你数学考20几分和
	上次一样！
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
				content:{
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
				content:{
					required:"请输入作文内容",
				}
			},
			submitHandler: function(form){
			   	//alert("提交事件！");
			    $("#ydpform").submit();
			   	//var data = $("#ydpform").serialize();
			   	
			} 
		})
	}
	/* 取消  quXiao */
	function quXiao(){
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=7";
	}
	
</script>

<script >
	/* 添加其他作文    addQita */
	function addQita(){
		var d = dialog({
			title: '其他作文',
			width: '700',
			//height: '35',
			content: $('#dialogdiv'),
			okValue: '确 定',
			ok: function () {
				
				/* 
				$.ajax({
					url:"${pageContext.request.contextPath}/goodsController/updateState.do",
					type:"post",
					data:{"goodsId":id,"goodsState":goodsState},
					success:function (data){
						//alert(data.msg);
						alert("操作成功！");
						location.href="${pageContext.request.contextPath}/wh/zjgl/spgl/searchGoods.jsp";
					},
					error: function (){//reload
						//$("#tableId").datagrid('refresh');
					}
				});
				 */
			},
			cancelValue: '取消',
			cancel: function () {}
		});
		d.showModal();
	}
	
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
	
	/* 查看预点评作文   selectWeiDianPing */
	function selectWeiDianPing(){
		var d = dialog({
			title: '美丽的心（作文标题）',
			width: '700',
			//height: '35',
			content: $('#selectdiv'),
			/* okValue: '确 定',
			ok: function () {
				
			},
			cancelValue: '取消',
			cancel: function () {} */
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