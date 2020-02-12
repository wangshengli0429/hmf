<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>预点评作文</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/console/js/jquery/jquery.min.js"></script>
<link  href="<%=request.getContextPath()%>/console/css/base.css" rel="stylesheet" >
<link  href="<%=request.getContextPath()%>/console/css/index.css" rel="stylesheet" >
<link  href="<%=request.getContextPath()%>/jslib/bootstrap/css/bootstrap.min.css" rel="stylesheet" >
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/bootstrap/js/bootstrap.min.js"></script>

<style>
td {
 text-align: center;
} 
</style>
</head>
<body>

<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<ul id="myTab" class="nav nav-tabs" role="tablist">
	<li class="active">
		<a href="#a" data-toggle="tab" role="tab">未点评</a>
	</li>
	<li>
		<a href="#b"  data-toggle="tab" role="tab">已点评</a>
	</li>
</ul>

<div id="myTabContent" class="tab-content">
	<!-- 已点评 -->
   <div class="tab-pane fade in active" id="a">
       <form class="form-inline" role="form">			<br><br>
			<label class="form-label" for="name">
				<button type="button" onclick="addWeiDianPing()" class="btn btn-default glyphicon glyphicon-plus" data-toggle="button">&nbsp;上传作文</button>
				</label>
			<div class="form-group" style="display:inline;">
				<div style="display:inline;float: right;">
				  <input type="text" name="" class="form-control" style="width:240px;height:19px;" placeholder="作文名称/点评老师/老师账号">
				  <button class="btn btn-info" type="button">查询</button>
				</div>
			</div>
			<br><br>
		</form>
		
		<!-- 数据表格 展示 -->
		<table class="table table-hover" style="width: 100%;">
		<thead>
		<tr class="active" align="center">
		<td style="text-align: left;">作文名称</td>
		<td style="text-align: left;">点评老师</td>
		<td style="text-align: left;">老师账号</td>
		<td>年级</td>
		<td>上传时间</td>
		<td>操作</td>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td name="one" style="text-align: left;"><a href="##" onclick="selectWeiDianPing()">美丽的心</a></td>
			<td name="two" style="text-align: left;">王一</td>
			<td name="three" style="text-align: left;">130000000009</td>
			<td name="four">一年级</td>
			<td name="five">2017-03-20 10:34</td>
			<td name="six">
				<button type="button" class="btn btn-info" data-toggle="button" onclick="updateWeiDianPing()">修改</button>
				<button type="button" class="btn btn-info" data-toggle="button" onclick="deleteWeiDianPing()">删除</button>
			</td>
		</tr>
		<tr>
			<td name="one" style="text-align: left;"><a href="##" onclick="selectWeiDianPing()">美丽的心</a></td>
			<td name="two" style="text-align: left;">王一</td>
			<td name="three" style="text-align: left;">130000000009</td>
			<td name="four">一年级</td>
			<td name="five">2017-03-20 10:34</td>
			<td name="six">
				<button type="button" class="btn btn-info" data-toggle="button" onclick="updateWeiDianPing()">修改</button>
				<button type="button" class="btn btn-info" data-toggle="button" onclick="deleteWeiDianPing()">删除</button>
			</td>
		</tr>
		<tr onclick="addA(this)" name='atr' >
			<td name="one" style="text-align: left;"><a href="##" onclick="selectWeiDianPing()">美丽的心</a></td>
			<td name="two" style="text-align: left;">王一</td>
			<td name="three" style="text-align: left;">130000000009</td>
			<td name="four">一年级</td>
			<td name="five">2017-03-20 10:34</td>
			<td name="six">
				<button type="button" class="btn btn-info" data-toggle="button" onclick="updateWeiDianPing()">修改</button>
				<button type="button" class="btn btn-info" data-toggle="button" onclick="deleteWeiDianPing()">删除</button>
			</td>
		</tr>
		</tbody>
		</table>
		<!-- /数据表格 展示 -->
		
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
		
		<!-- 查看 $('#selectdiv') -->
		<div id="selectdiv" style="display: none">
			<!-- <center>
				<h4><b>美丽的心</b></h4>
				<div style="margin-right:100px;">
					<span style="color:red;font-size:17px;float:right"><b>30</b></span>
				</div>
				<div style="margin-left:120px;float:center;">
					<p style="color:#c2c2c2;">五年级</p>
				</div>
			</center> -->
			<center>
				<h3><b>美丽的心</b></h3>
				<span style="font-size:17px;color:red;margin-left:500px;"><b>30</b></span>
				<p>
					<span style="color:#c2c2c2;">五年级</span>
				</p>
			</center>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			在这个晴空万里的早上，我像往常一样去吃饭，妹妹突然跳了下来说：姐妈妈叫你！我看到妹妹的那种表情，肯定不
			是什么好事！妈妈把我叫到床头前告诉我说：你知道你的成绩吗？！发下来了！你说会努力会努力！不错真不错考了个倒
			数第三名啊！！不单没进步还倒退一个名次！我听到这个消息我大吃一惊，     刚刚的晴空万里如今现在的电闪雷鸣当时
			我还不相信这个事实，现在果真就是这个分数，如果再让我认认真真的考一次，那成绩有可能没那么糟了！我想自己的水
			平本来就不好就算重来一次也未免也能考好吧！如今现在离新年已经不远了，我告诉自己对你什么不好你数学考20几分和
			上次一样！
		</div>
   </div>
   
   <!-- 未点评 -->
   <div class="tab-pane fade" id="b">
      <form class="form-inline" role="form">			<br><br>
			<div class="form-group" style="display:inline;">
				<div style="display:inline;float: right;">
				  <input type="text" name="" class="form-control" style="width:240px;height:19px;" placeholder="作文名称/点评老师/老师账号">
				  <button class="btn btn-info" type="button">查询</button>
				</div>
			</div>
			<br><br>
		</form>
		
		<!-- 数据表格 展示 -->
		<table class="table table-hover" style="width: 100%;">
		<thead>
		<tr class="active" align="center">
		<td style="text-align: left;">作文名称</td>
		<td style="text-align: left;">点评老师</td>
		<td style="text-align: left;">老师账号</td>
		<td>年级</td>
		<td>上传时间</td>
		<td>点评时间</td>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td name="one" style="text-align: left;"><a href="##" onclick="selectYiDianPing()">美丽的心</a></td>
			<td name="two" style="text-align: left;">王一</td>
			<td name="three" style="text-align: left;">130000000009</td>
			<td name="four">一年级</td>
			<td name="five">2017-03-20 10:34</td>
			<td name="six">2017-03-20 10:34</td>
		</tr>
		<tr>
			<td name="one" style="text-align: left;"><a href="##" onclick="selectYiDianPing()">美丽的心</a></td>
			<td name="two" style="text-align: left;">王一</td>
			<td name="three" style="text-align: left;">130000000009</td>
			<td name="four">一年级</td>
			<td name="five">2017-03-20 10:34</td>
			<td name="six">2017-03-20 10:34</td>
		</tr>
		<tr onclick="addA(this)" name='atr' >
			<td name="one" style="text-align: left;"><a href="##" onclick="selectYiDianPing()">美丽的心</a></td>
			<td name="two" style="text-align: left;">王一</td>
			<td name="three" style="text-align: left;">130000000009</td>
			<td name="four">一年级</td>
			<td name="five">2017-03-20 10:34</td>
			<td name="six">2017-03-20 10:34</td>
		</tr>
		</tbody>
		</table>
		<!-- /数据表格 展示 -->
		
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
   
</div>
</div>
</div>
</div>
</body>

<script>
	$('#myTab a').click(function (e) {
	  e.preventDefault();
	  $(this).tab('show');
	})
</script>

<script>
	/* 查看未点评作文  selectWeiDianPing */
	function selectWeiDianPing(){
		var d = dialog({
			title: '作文标题',
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
	
	/* 查看已点评作文  selectYiDianPing */
	function selectYiDianPing(){
		selectWeiDianPing();
	}
	
	/* 上传作文  addWeiDianPing */
	function addWeiDianPing(){
		location.href="<%=request.getContextPath()%>/zuoWenGuanLi/yuDianPing/yuDianPing_add.jsp";
	}
	
	/* 修改作文  updateWeiDianPing */
	function updateWeiDianPing(){
		location.href="<%=request.getContextPath()%>/zuoWenGuanLi/yuDianPing/yuDianPing_update.jsp";
	}
	
	/* 删除作文  deleteWeiDianPing */
	function deleteWeiDianPing(){
		var d = dialog({
			title: '提示',
			width: '300',
			//height: '35',
			content: '<center><h4><b>是否删除？</b></h4></center>',
			okValue: '确 定',
			ok: function () {
				
			},
			cancelValue: '取消',
			cancel: function () {}
		});
		d.showModal();
	}
	
</script>
</html>