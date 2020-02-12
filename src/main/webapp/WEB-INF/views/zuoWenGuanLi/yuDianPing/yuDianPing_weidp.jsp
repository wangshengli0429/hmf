<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>预点评作文-未点评</title>
<jsp:include page="../../inc.jsp"></jsp:include>
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

<div style="float:left;display:inline;margin-left:30px;margin-top:10px;">
	<div>
		<a href="#a" onclick="weidianping()" style="font-size:17px;color:#54a7fd;text-decoration:none;">未点评</a>
	</div>
	<div style="background:#489ef7;margin-top:7px;margin-left:-3px;float:left;height:2px;width:60px;">
	</div>
</div>
<div style="float:left;display:inline;margin-left:30px;margin-top:10px;">
	<div>
		<a href="#b" onclick="yidianping()" style="font-size:17px;color:#b0b0b0;text-decoration:none;">已点评</a>
	</div>
</div><br><br>

	<!-- 未点评 -->
	<div class="tab-pane fade in active" id="a">
	<form id="yudpId" action="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=7" method="post" class="form-inline" role="form">			<br><br>
		<label class="form-label" for="name">
			<button type="button" onclick="addWeiDianPing()" class="btn btn-default glyphicon glyphicon-plus" data-toggle="button">&nbsp;上传作文</button>
		</label>
		<div class="form-group" style="display:inline;">
			<div style="display:inline;float: right;">
				<input type="text" name="name" value="${name}" class="form-control" style="width:240px;height:19px;" placeholder="作文名称/点评老师/老师账号">
				<button class="btn btn-info" onclick="selectYudp()" style="background:#54a7fd;" type="button">查询</button>
			</div>
		</div>
		<br><br>
		
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
			<c:forEach items="${list}" var="l">
			<input type="hidden" id="title" value="${l.newTitle}"/>
			<tr>
				<td name="one" style="text-align: left;"><a href="##" onclick="selectWeiDianPing(${l.id})">${l.newTitle}</a></td>
				<td name="two" style="text-align: left;">${l.name}</td>
				<td name="three" style="text-align: left;">${l.tphone}</td>
				<td name="four">${l.geade}</td>
				<td name="five">${l.createtimes}</td>
				<td name="six">
					<button type="button" onclick="updateWeiDianPing(${l.id})" class="btn btn-info" data-toggle="button" style="background:#54a7fd;">修改</button>
					<button type="button" onclick="deleteWeiDianPing(${l.id})" class="btn btn-info" data-toggle="button" style="background:#54a7fd;">删除</button>
				</td>
			</tr>
			</c:forEach>
		</tbody>
		</table>
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
</div>
</body>

<script>
	/* 未点评 */
	function weidianping(){
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=7";
	}
	
	/* 已点评 */
	function yidianping(){
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=5";
	}
	
	/* 未点评-条件查询 */
	function selectYudp(){
		$("#yudpId").submit();
	}
	
	/* 查看未点评作文  selectWeiDianPing */
	function selectWeiDianPing(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=11&id="+id;
	}
	
	/* 上传作文  addWeiDianPing */
	function addWeiDianPing(){
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=12";
		<%-- location.href="<%=request.getContextPath()%>/zuoWenGuanLi/yuDianPing/yuDianPing_add.jsp"; --%>
	}
	
	/* 修改作文  updateWeiDianPing */
	function updateWeiDianPing(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=9&id="+id;
	}
	
	/* 删除作文  deleteWeiDianPing */
	function deleteWeiDianPing(id){
				alert(id);
		var d = dialog({
			title: '提示',
			width: '300',
			//height: '35',
			content: '<center><h4><b>是否删除？</b></h4></center>',
			okValue: '确 定',
			ok: function () {
				$.ajax({
	                url:'<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=8&id='+id,
					type: "POST",
	                async: false,
	                success: function(data) {
	                },
	                error: function(request) {
	                	var a = dialog({
	            			title: '提示',
	            			width: '300',
	            			//height: '35',
	            			content: '<center><h4><b>删除失败！</b></h4></center>',
	            			/* okValue: '确 定',
	            			ok: function () {
	            				
	            			},
	            			cancelValue: '取消',
	            			cancel: function () {} */
	            		});
	            		a.showModal();
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
	/* 首页 */
	function homePage(){
		var current = Number($("#currentPage").val());//当前页
		if(current>1){
			$("#currentPage").val("1");
			$("#yudpId").submit();
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
			$("#yudpId").submit();
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
			$("#yudpId").submit();
			
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
			$("#yudpId").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#yudpId").submit();
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
			$("#yudpId").submit();
		}
	}
</script>
</html>