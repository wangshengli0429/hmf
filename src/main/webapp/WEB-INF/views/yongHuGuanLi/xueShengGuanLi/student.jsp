<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学生管理student</title>
<jsp:include page="../../inc.jsp"></jsp:include>
<style>
td {
 text-align: center;
 font-family:Microsoft YaHei;
} 
</style>
</head>
<body style="font-family:Microsoft YaHei;">

<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">
<input type="hidden" id="aa" value="${m}">
<form id="userId" action="<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=10" method="post" class="form-inline" role="form">

	<div class="form-group">
		<label class="form-label" for="name">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年级：</label>
	    <select name="ageDetail" id="ageDetail" class="form-control" style="width:178px">
	      	<option value="">全部</option>
	   		<c:forEach items="${ageDetail}" var="a">
	      	<option value="${a.CODE}" ${a.CODE==nj?'selected':''}>${a.GRADE}</option>
	      	</c:forEach>
	    </select>
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    <label class="form-label" for="school">学校：</label>
	    <select name="school" id="school" class="form-control" style="width:178px;">
	   		<option value="">全部</option>
	      	
	    </select>
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    <label class="form-label" for="name">省市：</label>
	    <select id="district" onchange="sheng(this.value)" class="form-control" style="width:178px;">
        	<option value="-1">请选择</option>
        	<c:forEach items="${districts}" var="district">
            <option value="${district.id}" ${sheng==district.name?"selected":""}>${district.name}</option>
        	</c:forEach>
	    </select>
	    <input type="hidden" value="${shi}" id="shi1"/>
	    <input type="hidden" value="${xian}" id="xian1"/>
	    <select id="street" onchange="shi(this.value)" class="form-control" style="width:178px;display:none;">
	    </select>
	    <select id="street2" name="xian" class="form-control" style="width:178px;display:none">
	    </select>
	    <input type="hidden" name="shengshi1" id="shengshi1"/>
	    <input type="hidden" name="shengshi2" id="shengshi2"/>
	    <input type="hidden" name="shengshi3" id="shengshi3"/>
	</div>
	<br><br>
	
	<div class="form-group" style="display:inline;">
		<div style="display:inline;float: right;">
		  <input type="text" name="name" id="name" value="${name}" class="form-control" style="width:178px;height:19px;" placeholder="用户名/昵称/姓名" >
		  <button class="btn btn-info" onclick="selectTeacher()" style="background:#54a7fd;" type="button">查询</button>
		</div>
	</div>
	<br><br>


<!-- 数据表格 展示 -->
<table class="table table-hover" style="width: 100%;">
<thead>
	<tr class="active" align="center">
		<td>用户名</td>
		<td style="text-align:left">昵称</td>
		<td style="text-align:left">姓名</td>
		<td>性别</td>
		<td style="text-align:left">学校</td>
		<td>年级</td>
		<td style="text-align:left">省市</td>
		<td>注册时间</td>
		<td>操作</td>
	</tr>
</thead>
<tbody>
	<c:forEach items="${list}" var="l">
	<tr align="center">
		<td>${l.phone}</td>
		<td style="text-align:left">${l.userName}</td>
		<td style="text-align:left">${l.name}</td>
		<td >${l.sex}</td>
		<td style="text-align:left">${l.school}</td>
		<td>${l.grade}</td>
		<td style="text-align:left">${l.city}</td>
		<td>${l.createTime}</td>
		<td style="text-align:left">
			<button type="button" class="btn btn-info" style="background:#54a7fd;" data-toggle="button" onclick="updateStudent(${l.ID})">修改</button>
			<%-- 
			<button type="button" class="btn btn-danger" style="background:#ff0000;" data-toggle="button" onclick="deleteStudent(${l.ID})">删除</button>
			 --%>
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
		var shi = document.getElementById("shi1").value;
		var xian = document.getElementById("xian1").value;
		if(shi!=null&&shi!=""){
			$("#district").change();
			var s = document.getElementById("street").options;
			for(var i=0;i<s.length;i++){
				if(s[i].text==shi){
					s[i].selected=true;
				}
			}
		}
		if(xian!=null&&xian!=""){
			$("#street").change();
			var s2 = document.getElementById("street2").options;
			for(var j=0;j<s2.length;j++){
				if(s2[j].text==xian){
					s2[j].selected=true;
				}
			}
		}
		var a =  $("#aa").val();
		if(a.length>0){
			document.getElementById('nodata').style.display='block';
		}
	})
	/* 省市  二级联动 */
	function sheng(id){
		if(id==-1){
			document.getElementById("street").style.display='none';
			document.getElementById("street2").style.display='none';
		}
		if(id>0){
			$.ajax({
				type:"post",
				url:"<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=0&id="+id,
				async:false,
				success:function(json){
					//alert(json);  //接受的是表单
					if(json!=null||json!=""){
						document.getElementById("street").style.display='';
						document.getElementById("street2").style.display='none';
						document.getElementById("street").innerHTML=json;
					}
				}
			})
		}
	}
	function shi(id){
		$("#shengshi3").val("");
		if(id==-1){
			document.getElementById("street2").style.display='none';
		}
		if(id>0){
			$.ajax({
				type:"post",
				url:"<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=0&id="+id,
				async:false,
				success:function(json){
					//alert(json);  //接受的是表单
					if(json!=null||json!=""){
						document.getElementById("street").style.display='';
						document.getElementById("street2").style.display='';
						document.getElementById("street2").innerHTML=json;
					}
				}
			})
		}
	}
	/* 条件查询 */
	function selectTeacher(){
		var v = $("#name").val();
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
	
   		shengshhi();
		$("#userId").submit(); 
	}
	function shengshhi(){
		var shengshi1 = $("#district").find("option:selected").text();
		var shengshi2 = $("#street").find("option:selected").text();
		var shengshi3 = $("#street2").find("option:selected").text();
		$("#shengshi1").val(shengshi1);
		$("#shengshi2").val(shengshi2);
		$("#shengshi3").val(shengshi3);
	}
	
	/* 修改学生  updateStudent */
	function updateStudent(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=11&id="+id;
	}
	
	/* 删除学生  deleteStudent */
	function deleteStudent(id){
		var d = dialog({
			title: '提示',
			width: '300',
			//height: '35',
			content: '<center><h4><b>是否删除？</b></h4></center>',
			okValue: '确 定',
			ok: function () {
				$.ajax({
					url:"${pageContext.request.contextPath}/servlet/ManageUsersServlet?do=13&id="+id,
					type:"post",
					success:function (data){
						location.href="${pageContext.request.contextPath}/servlet/ManageUsersServlet?do=10";
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
	
	$("#ageDetail").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	$("#school").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	$("#district").change(function name() {
		var obj1 = document.getElementById("street2");    
		obj1.selectedIndex = -1;
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	$("#name").change(function name() {
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
			shengshhi();
			$("#userId").submit();
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
			shengshhi();
			$("#userId").submit();
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
			shengshhi();
			$("#userId").submit();
			
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
			shengshhi();
			$("#userId").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		shengshhi();
		$("#userId").submit();
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
			shengshhi();
			$("#userId").submit();
		}
	}
</script>

</html>