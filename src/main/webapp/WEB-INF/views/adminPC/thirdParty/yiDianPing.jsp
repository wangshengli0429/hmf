<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>已点评作文</title>
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
<form id="yidpId" action="<%=request.getContextPath()%>/servlet/ThirdPartyServlet?do=2" method="post" class="form-inline" role="form">

	<div class="form-group" style="display:inline;">
		<label class="form-label" for="name">年级：</label>
	    <select name="grade" id="grade" class="form-control" style="width:178px;">
	   		<option value="">全部</option>
	   		<c:forEach items="${ageDetail}" var="a">
	      	<option value="${a.CODE}" ${a.CODE==grade?'selected':''}>${a.GRADE}</option>
	      	</c:forEach>
	      	<option value="1" ${grade=='1'?"selected":""}>小学</option>
	      	<option value="2" ${grade=='2'?"selected":""}>初中</option>
	      	<option value="3" ${grade=='3'?"selected":""}>高中</option>
	    </select>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    <label class="form-label" for="name">作文等级：</label>
	    <select name="grading" id="grading" class="form-control" style="width:178px;">
	   		<option value="">全部</option>
	      	<option value="一类文" ${grading=='一类文'?"selected":""}>一类文</option>
	      	<option value="二类文" ${grading=='二类文'?"selected":""}>二类文</option>
	      	<option value="三类文" ${grading=='三类文'?"selected":""}>三类文</option>
	      	<option value="四类文" ${grading=='四类文'?"selected":""}>四类文</option>
	      	<option value="五类文" ${grading=='五类文'?"selected":""}>五类文</option>
	    </select>
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    <label class="form-label" for="name">点评时间：</label>
	    <input type="text" name="startDate" id="startDate" value="${startDate}" class="form-control Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd 00:00:00'})" style="width:178px;height:19px;" placeholder="开始时间">
	    <input type="text" name="endDate" id="endDate" value="${endDate}" class="form-control Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd 23:59:59'})" style="width:178px;height:19px;" placeholder="结束时间">
		<br><br>
		<div style="display:inline;float: right;">
		  <input type="text" name="name" id="seachName" value="${name}" class="form-control" style="width:240px;height:19px;" placeholder="作文名称/作者昵称/点评老师/老师账号">
		  <button class="btn btn-info" onclick="selectYidp()" style="background:#54a7fd;" type="button">查询</button>
		</div>
	</div>
	<br><br>

	<!-- 数据表格 展示 -->
	<table class="table table-hover" style="width: 100%;">
	<thead>
		<tr class="active" align="center">
			<td>作文名称</td>
			<td>作者昵称</td>
			<td>学生账号</td>
			<td>年级</td>
			<td>点评老师</td>
			<td>老师账号</td>
			<td>稿数</td>
			<td>原标题</td>
			<td>作文等级</td>
			<td>上传时间</td>
			<td>点评时间</td>
			<!-- <td>操作</td> -->
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="l">
		<tr>
			<td><a href="##" onclick="selectYiDianPing(${l.id})">${l.newTitle}</a></td>
			<td>${l.author}</td>
			<td>${l.sphone}</td>
			<td>${l.geade}</td>
			<td>${l.name}</td>
			<td>${l.tphone}</td>
			<td align="center">${l.draft}</td>
			<td>
				<c:if test="${ empty l.oldTitle}">-</c:if>
				<c:if test="${not empty l.oldTitle}">${l.oldTitle}</c:if>
			</td>
			<td>
				<c:if test="${ empty l.grading}">-</c:if>
				<c:if test="${not empty l.grading}">${l.grading}</c:if>
			</td>
			<td>${l.createtimes}</td>
			<td>${l.comtimes}</td>
			<%-- <td>
				<button type="button" class="btn btn-info" style="background:#54a7fd;" data-toggle="button" onclick="recommend(${l.comp_id})" ${l.open?"": "disabled"}>推荐</button>
				<button type="button" class="btn btn-danger" style="background:#ff0000;" data-toggle="button" onclick="deleteYiDianPing(${l.comp_id})">删除</button>
			</td> --%>
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
	function selectYidp(){
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
		$("#yidpId").submit();
	}
	
	/* 查看  selectYiDianPing */
	function selectYiDianPing(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=4&id="+id;
	}
	
	/* 删除  deleteYiDianPing */
	function deleteYiDianPing(id){
		var d = dialog({
			title: '提示',
			width: '300',
			//height: '35',
			content: '<center><h4><b>是否删除？</b></h4></center>',
			okValue: '确 定',
			ok: function () {
				$.ajax({
					url:"<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=31&id="+id,
					type:"post",
					success:function (data){
						var d = dialog({
							title: '提示',
							width: '300',
							//height: '35',
							content: '<center><h4><b>'+data+'</b></h4></center>',
							okValue: '确 定',
							ok: function () {
								location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=3";
							}
						});
						d.showModal();
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
	
	/* 推荐 */
	function recommend (id){
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=37&id="+id;
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
			$("#yidpId").submit();
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
			$("#yidpId").submit();
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
			$("#yidpId").submit();
			
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
			$("#yidpId").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#yidpId").submit();
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
			$("#yidpId").submit();
		}
	}
</script>

</html>