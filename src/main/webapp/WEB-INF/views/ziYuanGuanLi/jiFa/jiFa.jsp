<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>技法</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">

<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">
<input type="hidden" id="aa" value="${m}">
<form id="jfId" action="<%=request.getContextPath()%>/servlet/ManageFindFwServlet?do=15" method="post" class="form-inline" role="form">

	<div class="form-group">
		<label class="form-label" for="name">年级：</label>
	    <select name="ageDetail" id="ageDetail" class="form-control" style="width:178px">
	   		<option value="">全部</option>
	      	<c:forEach items="${ageDetail}" var="a">
	      	<option value="${a.CODE}" ${a.CODE==nj?'selected':''}>${a.CODE_NAME}</option>
	      	</c:forEach>
	    </select>
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    <label class="form-label" for="name">分类：</label>
	    <select name="type" id="type" class="form-control" style="width:178px;">
	   		<option value="">全部</option>
	      	<c:forEach items="${type}" var="t">
	      	<option value="${t.CODE}" ${t.CODE==fl?'selected':''}>${t.CODE_NAME}</option>
	      	</c:forEach>
	    </select>
	</div>
	<br><br>
	
	<div class="form-group" style="display:inline;">
		<label class="form-label" for="name">
		<button type="button" onclick="addJiFa()" class="btn btn-default glyphicon glyphicon-plus" data-toggle="button">&nbsp;添加技法</button>
		</label>
		<div style="display:inline;float: right;">
		  <input type="text" name="name" id="seachName" value="${name}" class="form-control" style="width:178px;height:19px;" placeholder="技法标题/关键字">
		  <button class="btn" onclick="findJiFa()" type="button" style="background:#54a7fd;">查询</button>
		</div>
	</div>
	<br><br>


<!-- 数据表格 展示 -->
<table class="table table-hover" style="width: 100%;">
<thead>
	<tr class="active">
		<td>技法标题</td>
		<td>年级</td>
		<td>分类</td>
		<td>阅读人数（人）</td>
		<td>收藏人数（人）</td>
		<td style="text-align:center">发布时间</td>
		<td style="text-align:center">操作</td>
	</tr>
</thead>
<tbody>
	<c:forEach items="${jflist}" var="j">
	<tr>
		<td name="one"><a href="##" onclick="selectJifa(${j.id})">${j.name}</a></td>
		<td name="three">${j.ageDetail}</td>
		<td name="four">${j.type1}</td>
		<td name="five">${j.yuedi}</td>
		<td name="six">${j.shoucang}</td>
		<td name="seven" style="text-align:center">${j.times}</td>
		<td name="eight" style="text-align:center">
			<button type="button" class="btn btn-info" data-toggle="button" onclick="toUpdate(${j.id})" style="background:#54a7fd;">修改</button>
			<button type="button" class="btn btn-danger" data-toggle="button" onclick="deleteJifa(${j.id})" style="background:#ff0000;">删除</button>
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
	/* 条件查询 */
	function findJiFa(){
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
		$("#jfId").submit();
	}
	/* 添加范文addJiFa */
	function addJiFa(){
		location.href="<%=request.getContextPath()%>/servlet/ManageFindFwServlet?do=16";
	}

	/* 查看技法 */
	function selectJifa(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageFindFwServlet?do=21&id="+id;
	}
	
	/* 删除技法 */
	function deleteJifa(id){
		var d = dialog({
			title: '提示',
			width: '300',
			//height: '35',
			content: '<center><h4><b>是否删除？</b></h4></center>',
			okValue: '确 定',
			ok: function () {
				$.ajax({
					url:"${pageContext.request.contextPath}/servlet/ManageFindFwServlet?do=18",
					type:"post",
					data:{"id":id},
					success:function (data){
						//alert(data.msg);
						//alert("操作成功！");
						location.href="${pageContext.request.contextPath}/servlet/ManageFindFwServlet?do=15";
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
	
	/* 修改技法  toUpdate */
	function toUpdate(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageFindFwServlet?do=19&id="+id;
	}
	
	$("#ageDetail").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	$("#type").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	$("#seachName").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	
</script>


<!-- 分页 -->
<script>
	/* 首页 */
	function homePage(){
		var current = Number($("#currentPage").val());//当前页
		if(current>1){
			$("#currentPage").val("1");
			$("#jfId").submit();
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
			$("#jfId").submit();
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
			$("#jfId").submit();
			
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
			$("#jfId").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#jfId").submit();
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
			$("#jfId").submit();
		}
	}
</script>

</html>