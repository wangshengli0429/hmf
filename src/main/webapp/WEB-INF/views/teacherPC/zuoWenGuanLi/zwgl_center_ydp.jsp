<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page errorPage="/WEB-INF/view/404.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>作文管理center-已点评</title>
<jsp:include page="../../inc.jsp"></jsp:include>
<style>
body{
  font-family:"Microsoft YaHei";
  }
td {
 text-align: center;
} 
</style>
</head>
<body>
<input type="hidden" id="aa" value="${m}">
<div class="viewFramework-content">

<div style="float:left;display:inline;margin-left:30px;margin-top:10px;">
	<div>
		<a href="#a" id="aid" onclick="weidianping()" style="font-size:17px;color:#b0b0b0;text-decoration:none;">未点评</a>
	</div>
</div>
<div style="float:left;display:inline;margin-left:30px;margin-top:10px;">
	<div>
		<a href="#b" id="bid" onclick="yidianping()" style="font-size:17px;color:#54a7fd;text-decoration:none;">已点评</a>
	</div>
	<div style="background:#489ef7;margin-top:7px;margin-left:-3px;float:left;height:2px;width:60px;">
	</div>
</div><br><br>

	<!-- 已点评 -->
	<div class="tab-pane fade in active" id="b">
		<form id="ydpId" class="form-inline" role="form" action="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=2" method="post">			<br><br>
			<label class="form-label" for="geade">年级：</label>
		    <select name="geade" id="ageDetail" class="form-control" style="width:178px">
		   		<option value="">全部</option>
		   		<c:forEach items="${ageDetail}" var="a">
		      		<option value="${a.CODE}" ${a.CODE==geade?'selected':''}>${a.GRADE}</option>
		      	</c:forEach>
		    </select>
		    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		    <label class="form-label" for="name">点评时间：</label>
	    	<input type="text" name="startDate" id="startDate" value="${startDate}" class="form-control Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endDate\')||\'new Date()\'}',})" style="width:178px;height:19px;" placeholder="开始时间">
	    	至
	    	<input type="text" name="endDate" id="endDate" value="${endDate}" class="form-control Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startDate\')}',maxDate:'#F{\'new Date()\'}',})" style="width:178px;height:19px;" placeholder="结束时间">
		    
			<div class="form-group" style="display:inline;">
				<div style="display:inline;float: right;">
				  <input type="text" name="name" id="seachName" value="${name}" class="form-control" style="width:240px;height:19px;" placeholder="作文名称/作者昵称">
				  <button class="btn btn-info" onclick="ydpSelect()" style="background:#55a7fd;" type="button">查询</button>
				</div>
			</div>
			<br><br>
		
		<!-- 数据表格 展示 -->
		<table class="table table-hover" style="width: 100%;">
		<thead>
			<tr class="active" align="center">
				<td style="text-align: left;">作文名称</td>
				<td style="text-align: left;">作者昵称</td>
				<td>年级</td>
				<td>分数</td>
				<td>稿数</td>
				<td>原标题</td>
				<td>提交时间</td>
				<td>点评时间</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${ylist}" var="y">
			<tr>
				<td style="text-align: left;">
					<a href="#" if="findydpId" style="color:#54a7fd;" target="mainFrame4" onclick="selectYiDianPing(${y.id})">${y.title}</a>
				</td>
				<td style="text-align: left;">${y.author}</td>
				<td>${y.geade}</td>
				<td><span style="color:red;font-size:17px"><b>${y.score}</b></span></td>
				<td>${y.draft}</td>
				<td>${y.oldtitle}</td>
				<td>${y.createTime1}</td>
				<td>${y.comtime1}</td>
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
</body>
<script type="text/javascript">
$(function (){
	var a =  $("#aa").val();
	if(a.length>0){
		document.getElementById('nodata').style.display='block';
	}
})
</script>
<script>
	
	/* 未点评  */
	function weidianping(){
		location.href="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=1";
	}
	
	/* 已点评  */
	function yidianping(){
		location.href="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=2";
	}
	
	/* 条件查询-已点评 */
	function ydpSelect(){
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
		$("#ydpId").submit();
	}
	
	/* 查看已点评详情 */
	function selectYiDianPing(id){
		location.href="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=4&id="+id;
	}
	
</script>

<!-- 分页 -->
<script>
	/* 首页 */
	function homePage(){
		var current = Number($("#currentPage").val());//当前页
		if(current>1){
			$("#currentPage").val("1");
			$("#ydpId").submit();
		}else{
			alert("已是第一页");
		}
	}
	/* 上一页 */
	function previous (){
		var current = $("#currentPage").val();//当前页
		if(current>1){
			var a  = Number(current)-1;
			$("#currentPage").val(a);
			$("#ydpId").submit();
		}else{
			alert("已是第一页");
		}
	}
	/* 下一页 */
	function next(){
		var current = Number($("#currentPage").val());//当前页
		var total = Number($("#totalPages").text());//总页数
		if(current<total){
			var a  = Number(current)+1;
			$("#currentPage").val(a);
			$("#ydpId").submit();
			
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
			$("#ydpId").submit();
		}else{
			alert("已是最后一页");
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#ydpId").submit();
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
			$("#ydpId").submit();
		}
	}
	$("#ageDetail").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	$("#seachName").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	$("#startDate").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	$("#endDate").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	
</script>
</html>