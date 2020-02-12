<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>作文推荐</title>
<jsp:include page="../../inc.jsp"></jsp:include>
<style>
td {
 text-align: center;
} 
</style>
</head>
<body style="font-family:Microsoft YaHei;">

<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">
<input type="hidden" id="aa" value="${m}">
<form id="yudpForm" action="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=14" method="post" class="form-inline" role="form">
	<label class="form-label" for="name">
		<button type="button" onclick="addTuiJian()" class="btn btn-default glyphicon glyphicon-plus" data-toggle="button">&nbsp;添加作文</button>
		</label>
	<div class="form-group" style="display:inline;">
		<div style="display:inline;float: right;">
		  <input type="text" name="name" id="seachName" value="${name}" class="form-control" style="width:240px;height:19px;" placeholder="作文名称/作者昵称">
		  <button class="btn btn-info" onclick="findTuijian()" type="button" style="background:#54a7fd;">查询</button>
		</div>
	</div>
	<br><br>

<!-- 数据表格 展示 -->
<table class="table table-hover" style="width: 100%;">
<thead>
	<tr class="active" align="center">
		<td>作文名称</td>
		<td>作者昵称</td>
		<td>年级</td>
		<td>体裁</td>
		<td>阅读人数（人）</td>
		<td>收藏人数（人）</td>
		<td>状态</td>
		<td>发布时间</td>
		<td>操作</td>
	</tr>
</thead>
<tbody>
	<c:forEach items="${list}" var="l">
	<tr>
		<td><a href="##" onclick="selectTuijian(${l.id},${l.comId})">${l.name}</a></td>
		<td>${l.author}</td>
		<td>${l.ageDetail}</td>
		<td>${l.style}</td>
		<td>${l.yuedi}</td>
		<td>${l.shoucang}</td>
		<td>${l.display==1?"显示":""}${l.display==2?"隐藏":""}</td>
		<td>${l.publishTimes}</td>
		<td>
			<c:if test="${l.display==1}">
			<button type="button" class="btn btn-danger" style="background:#ff0000;" onclick="updateType('${l.id}','${l.display}')">隐藏</button>
			</c:if>
			<c:if test="${l.display==2}">
			<button type="button" class="btn btn-info" style="background:#54a7fd;" onclick="updateType('${l.id}','${l.display}')">显示</button>
			</c:if>
			<%-- 
			<button type="button" class="btn btn-info" style="background:#54a7fd;" onclick="updateTuiJian(${l.id})" ${l.comId!=null?"disabled": ""}>修改</button>
			 --%>
			<button type="button" class="btn btn-info" style="background:#54a7fd;" onclick="updateTuiJian(${l.id})">修改</button>
			<button type="button" class="btn btn-info" style="background:#54a7fd;" onclick="deleteTuiJian(${l.id})">删除</button>
		</td>
	</tr>
	</c:forEach>
</tbody>
</table>
<div id="nodata" style="display:none; width:100%; padding-left:50%;">无数据</div>
<!-- /数据表格 展示 -->

	<!-- 分页 -->
	<div class="text-center">
	<ul id="visible-pages-example" class="pagination">
	<li class="first" data-page="1">
	<a href="javascript:homePage()" >首页</a>
	</li>
	<li class="prev" data-page="1">
	<a href="javascript:previous()">上一页</a>
	</li>
	<li class="prev" data-page="10">
	<a>
	当前页<input value="${page.currentPage }" name="currentPage" id="currentPage" size="3" style="height:20px;text-align:center;vertical-align:middle;" onkeydown="javascript:if(event.keyCode==13) toPage();">页
	每页<input value="${page.numPerPage }" name="numPerPage" id="numPerPage" size="3" style="height:20px;text-align:center;vertical-align:middle;" onkeydown="javascript:if(event.keyCode==13) toCount();">条
	总<span id="totalPages">${page.totalPages }</span>页 总${page.totalRows }条
	</a>
	</li>
	<li class="next" data-page="2">
	<a href="javascript:next()">下一页</a>
	</li>
	<li class="last" data-page="35">
	<a href="javascript:last()">末页</a>
	</li>
	</ul>
	</div>
	<!-- /分页 -->
</form>

</div>
</div>
</div>
</body>

<script>
	$(function(){
		var a =  $("#aa").val();
		if(a.length>0){
			document.getElementById('nodata').style.display='block';
		}
	})
	/* 条件查询 */
	function findTuijian(){
		var v = $("#seachName").val();
		
		var regu = "^[ ]+$";
		var re = new RegExp(regu);
		var b = re.test(v);
		if(b){
			alert("您输入的搜索条件无效，请重新输入");
			return false;
		}
		var regEx = new RegExp(/^(([^\^\.<>%&',;=?$"':#@!~\]\[{}\\/`\|])*)$/);
   		var result = v.match(regEx);
   		if(result == null){
	        alert("您输入的：" + v + "，含有特殊字符，请重新输入");  
	        return false;  
   		} 
		$("#yudpForm").submit();
	}
	/* 添加作文  addTuiJian */
	function addTuiJian(){
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=20";
	}
	/* 查看  selectTuijian */
	function selectTuijian(id,comId){
		if(comId!=null&&comId!=""){
			location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=39&id="+comId;
		}else{
			location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=19&id="+id;
		}
	}
	/* 修改状态 */
	function updateType(id,display){
		var d = dialog({
			title: '提示',
			width: '300',
			//height: '35',
			content: '<center><h4><b>是否修改状态？</b></h4></center>',
			okValue: '确 定',
			ok: function () {
				$.ajax({
					url:"<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=15&id="+id+"&display="+display,
					type:"post",
					success:function (data){
						location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=14";
					},
					error: function (){//reload
						//$("#tableId").datagrid('refresh');
					}
				});
			},
			cancelValue: '取消',
			cancel: function () {}
		});
		d.showModal();
	}
	/* 修改推荐作文 */
	function updateTuiJian(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=16&id="+id;
	}
	/* 删除推荐作文 */
	function deleteTuiJian(id){
		var d = dialog({
			title: '提示',
			width: '300',
			//height: '35',
			content: '<center><h4><b>是否删除？</b></h4></center>',
			okValue: '确 定',
			ok: function () {
				$.ajax({
					url:"<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=18&id="+id,
					type:"post",
					success:function (data){
						//alert(data.msg);
						//alert("操作成功！");
						location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=14";
					},
					error: function (){//reload
						//$("#tableId").datagrid('refresh');
					}
				});
			},
			cancelValue: '取消',
			cancel: function () {}
		});
		d.showModal();
	}
</script>

<!-- 分页 -->
<script>
	$("#seachName").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	/* 首页 */
	function homePage(){
		var current = Number($("#currentPage").val());//当前页
		if(current>1){
			$("#currentPage").val("1");
			$("#yudpForm").submit();
		}else{
			alert("已是第一页")
		}
	}
	/* 上一页 */
	function previous (){
		var current = $("#currentPage").val();//当前页
		if(current>1){
			var a  = Number(current)-1;
			$("#currentPage").val(a);
			$("#yudpForm").submit();
		}else{
			alert("已是第一页")
		}
	}
	/* 下一页 */
	function next(){
		var current = Number($("#currentPage").val());//当前页
		var total = Number($("#totalPages").text());//总页数
		if(current<total){
			var a  = Number(current)+1;
			$("#currentPage").val(a);
			$("#yudpForm").submit();
			
		}else{
			alert("已是最后一页");
		}
	}
	/* 末页 */
	function last(){
		var current = Number($("#currentPage").val());//当前页
		var total = Number($("#totalPages").text());//总页数
		if(current<total){
			$("#currentPage").val(total);
			$("#yudpForm").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#yudpForm").submit();
	}
	/*跳转到指定页码*/
	function toPage(){
		var current = Number($("#currentPage").val());//当前页
		var total = Number($("#totalPages").text());//总页数
		if(current>total){
			alert("输入页码超过总页数！")
		}else if(current < 1){
			alert("请您输入正确页码！")
		}else{
			$("#currentPage").val(current);
			$("#yudpForm").submit();
		}
	}
</script>

</html>