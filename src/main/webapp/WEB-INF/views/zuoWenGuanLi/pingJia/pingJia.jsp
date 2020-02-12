<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评价管理</title>
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
<form id="pingjiaId" action="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=22" method="post" class="form-inline" role="form">
	<div class="form-group" style="display:inline;">
		<label class="form-label" for="name">满意程度：</label>
	    <select name="satisfaction" id="satisfaction" class="form-control" style="width:178px;">
	   		<option value="">全部</option>
	      	<option value="1" ${satisfaction==1?"selected":""}>非常满意</option>
	      	<option value="2" ${satisfaction==2?"selected":""}>满意</option>
	      	<option value="3" ${satisfaction==3?"selected":""}>不满意</option>
	    </select>
		<div style="display:inline;float: right;">
		  <input type="text" name="name" id="seachName" value="${name}" class="form-control" style="width:178px;height:19px;" placeholder="所属作文/所属老师/评价人">
		  <button class="btn btn-info" onclick="findPingjia()" style="background:#54a7fd;" type="button">查询</button>
		</div>
	</div>
	<br><br>

<!-- 数据表格 展示 -->
<table class="table table-hover" style="width: 100%;">
	<thead>
		<tr class="active" align="center">
			<td style="text-align:left">评价人</td>
			<td style="text-align:left">所属作文</td>
			<td style="text-align:left">所属老师</td>
			<td>满意程度</td>
			<td>负责态度（分）</td>
			<td>专业水平（分）</td>
			<td>平均分（分）</td>
			<td>年级</td>
			<td>评价时间</td>
			<td>回复时间</td>
			<!-- <td>操作</td> -->
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="l">
		<tr>
			<td name="one" style="text-align:left">${l.sname}</td>
			<td name="two" style="text-align:left"><a href="#" onclick="selectPingJia(${l.comId})">${l.title}</a></td>
			<td name="three" style="text-align:left">${l.tname}</td>
			<td name="four">${l.satisfaction==1?"非常满意":""}${l.satisfaction==2?"满意":""}${l.satisfaction==3?"不满意":""}</td>
			<td name="five">${l.attit}</td>
			<td name="six">${l.profes}</td>
			<td name="seven">${l.average}</td>
			<td name="eight">${l.grade}</td>
			<td name="nine">${l.apprTimes}</td>
			<td name="ten">${l.messageTimes}</td>
			<%-- <td>
				<button type="button" class="btn btn-danger" style="background:#ff0000;" data-toggle="button" onclick="deletePingJia(${l.app_id })">删除</button>
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
	function findPingjia(){
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
		$("#pingjiaId").submit();
	}
	/* 查看  selectPingJia */
	function selectPingJia(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=23&id="+id;
	}
	/* 删除   */
	function deletePingJia(id){
		var d = dialog({
			title: '提示',
			width: '300',
			//height: '35',
			content: '<center><h4><b>是否删除？</b></h4></center>',
			okValue: '确 定',
			ok: function () {
				$.ajax({
					url:"<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=33&id="+id,
					type:"post",
					success:function (data){
						//alert(data.msg);
						//alert("操作成功！");
						location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=1";
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
	$("#satisfaction").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	$("#seachName").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	/* 首页 */
	function homePage(){
		var current = Number($("#currentPage").val());//当前页
		if(current>1){
			$("#currentPage").val("1");
			$("#pingjiaId").submit();
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
			$("#pingjiaId").submit();
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
			$("#pingjiaId").submit();
			
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
			$("#pingjiaId").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#pingjiaId").submit();
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
			$("#pingjiaId").submit();
		}
	}
</script>

</html>