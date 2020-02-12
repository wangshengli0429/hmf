<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>权限管理</title>
<jsp:include page="../../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">
<input type="hidden" id="aa" value="${m}">
<form id="jurId" class="form-inline" role="form" action="<%=request.getContextPath()%>/servlet/AdministrationServlet?do=1" method="post">
	<div class="form-group" style="display:inline;">
		<label class="form-label" for="name">
		<button type="button" onclick="addAdministrators()" class="btn btn-default" data-toggle="button">&nbsp;添加管理员</button>
		</label>
	</div>
	<br><br>
	
	<!-- 数据表格 展示 -->
	<table class="table table-hover">
		<thead>
			<tr class="active">
				<td>管理员账号</td>
				<td>管理员姓名</td>
				<td>管理员角色</td>
				<td>创建时间</td>
				<td style="text-align:center">操作</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="l">
			<tr>
				<td>${l.USERNAME}</td>
				<td>${l.NAME}</td>
				<td>
					<c:if test="${not empty l.ROLE}">
					${l.ROLE}
					</c:if>
					<c:if test="${empty l.ROLE}">
					--
					</c:if>
				</td>
				<td>${l.CREATE_TIME}</td>
				<td style="text-align:center">
					<c:if test="${l.JURISDICTION==2}">
					<button type="button" class="btn btn-info" style="background:#54a7fd;" data-toggle="button" onclick="setJurisdiction(${l.ID})">设置权限</button>
					</c:if>
					<c:if test="${l.JURISDICTION==1}">
					<button type="button" class="btn btn-info" style="background:#54a7fd;" data-toggle="button" onclick="updateJurisdiction(${l.ID})">修改权限</button>
					</c:if>
					<button type="button" class="btn btn-info" style="background:#54a7fd;" data-toggle="button" onclick="toUpdate(${l.ID})">修改</button>
					<button type="button" class="btn btn-info" style="background:#54a7fd;" data-toggle="button" onclick="deleteAdministrators(${l.ID})">删除</button>
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
	<a href="javascript:homePage()">首页</a>
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
	/* 添加管理员  addAdministrators */
	function addAdministrators(){
		location.href="<%=request.getContextPath()%>/servlet/AdministrationServlet?do=3";
	}
	/* 设置权限   setJurisdiction */
	function setJurisdiction(id){
		location.href="<%=request.getContextPath()%>/servlet/AdministrationServlet?do=2&id="+id;
	}
	/* 修改权限   updateJurisdiction */
	function updateJurisdiction(id){
		location.href="<%=request.getContextPath()%>/servlet/AdministrationServlet?do=2&id="+id;
	}
	/* 修改信息   toUpdate */
	function toUpdate(id){
		location.href="<%=request.getContextPath()%>/servlet/AdministrationServlet?do=7&id="+id;
	}
	/* 删除  deleteAdministrators */
	function deleteAdministrators(id){
		var d = dialog({
			title: '提示',
			width: '300',
			//height: '35',
			content: '<center><h4><b>是否删除？</b></h4></center>',
			okValue: '确 定',
			ok: function () {
				$.ajax({
					url:"${pageContext.request.contextPath}/servlet/AdministrationServlet?do=9",
					type:"post",
					data:{"id":id},
					success:function (data){
						var d = dialog({
							title: '提示',
							width: '300',
							//height: '35',
							content: '<center><h4><b>'+data+'</b></h4></center>',
							okValue: '确 定',
							ok: function () {
								location.href="<%=request.getContextPath()%>/servlet/AdministrationServlet?do=1";
							}
						});
						d.showModal();
					},
					error: function (){
						dialogs("删除失败！");
					}
				});
			},
			cancelValue: '取消',
			cancel: function () {}
		});
		d.showModal();
	}
	//弹框
	function dialogs(text){
		var d = dialog({
			title: '提示',
			width: '300',
			//height: '35',
			content: '<center><h4><b>'+text+'</b></h4></center>',
			okValue: '确 定',
			ok: function () {}
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
			$("#jurId").submit();
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
			$("#jurId").submit();
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
			$("#jurId").submit();
			
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
			$("#jurId").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#jurId").submit();
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
			$("#jurId").submit();
		}
	}
</script>
</html>