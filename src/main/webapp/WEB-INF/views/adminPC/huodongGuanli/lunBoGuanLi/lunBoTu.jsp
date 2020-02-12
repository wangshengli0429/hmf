<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>轮播图管理</title>
<jsp:include page="../../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">

<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<form id="lbform" action="<%=request.getContextPath()%>/servlet/ManageLunboServlet?do=1" method="post" class="form-inline" role="form">
	<input type="hidden" id="aa" value="${m}">
	<div class="form-group">
		<label class="form-label" for="name">状态：</label>
	    <select name="state" id="state" class="form-control" style="width:178px">
	   		<option value="">全部</option>
	      	<option value="1" ${state==1?"selected":""}>已发布</option>
	      	<option value="2" ${state==2?"selected":""}>待发布</option>
	      	<option value="3" ${state==3?"selected":""}>下架</option>
	    </select>
	</div>
	<br><br>
	
	<div class="form-group" style="display:inline;">
		<label class="form-label" for="name">
		<button type="button" onclick="addLunBo()" class="btn btn-default glyphicon glyphicon-plus" data-toggle="button">&nbsp;添加轮播</button>
		</label>
		<div style="display:inline;float: right;">
		  <input type="text" name="name" id="seachName" value="${name}" class="form-control" style="width:178px;height:19px;" placeholder="轮播名称">
		  <button class="btn" onclick="findLunbo()" style="background:#54a7fd;" type="button">查询</button>
		</div>
	</div>
	<br><br>
	
	<!-- 数据表格 展示 -->
	<table class="table table-hover">
	<thead>
		<tr class="active">
			<td>轮播名称</td>
			<td style="text-align:center">轮播位置</td>
			<td>操作人员账号</td>
			<td>状态</td>
			<td style="text-align:center">上传时间</td>
			<td style="text-align:center;width:190px;">操作</td>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="l">
		<tr>
			<td name="one"><a href="##" onclick="selectLunbo(${l.id})">${l.name}</a></td>
			<td name="two" style="text-align:center">${l.position}</td>
			<td name="three">${l.userNo}</td>
			<td name="four">${l.state==1?"已发布":""}${l.state==2?"待发布":""}${l.state==3?"下架":""}</td>
			<td name="five" style="text-align:center">${l.times}</td>
			<td name="six">
				<c:if test="${l.state==1}">
					<button type="button" onclick="xiajia(${l.id})" class="btn" style="background:#54a7fd;">下架</button>
					<button type="button" onclick="toUpdate(${l.id})" class="btn" style="background:#54a7fd;">修改</button>
				</c:if>
				<c:if test="${l.state==2}">
					<button type="button" onclick="fabu(${l.id},${l.position})" class="btn" style="background:#54a7fd;">发布</button>
					<button type="button" onclick="toUpdate(${l.id})" class="btn" style="background:#54a7fd;">修改</button>
					<button type="button" onclick="deleteLunbo(${l.id})" class="btn" style="background:#54a7fd;">删除</button>
				</c:if>
				<c:if test="${l.state==3}">
					--
				</c:if>
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

<script type="text/javascript">
	$(function (){
		var a =  $("#aa").val();
		if(a.length>0){
			document.getElementById('nodata').style.display='block';
		}
	})
	
	/* 条件查询   findLunbo */
	function findLunbo(){
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
		$("#lbform").submit();
	}
	
	/* 添加轮播  addLunBo */
	function addLunBo(){
		location.href="<%=request.getContextPath()%>/adminPC/huodongGuanli/lunBoGuanLi/lunBo_add.jsp";
	}
	
	/* 查看轮播selectLunbo */
	function selectLunbo(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageLunboServlet?do=3&id="+id;
	}
	
	/* 删除轮播  deleteLunbo */
	function deleteLunbo(id){
		var d = dialog({
			title: '提示',
			width: '300',
			//height: '35',
			content: '<center><h4><b>是否删除？</b></h4></center>',
			okValue: '确 定',
			ok: function () {
				$.ajax({
					url:"${pageContext.request.contextPath}/servlet/ManageLunboServlet?do=2&id="+id,
					type:"post",
					success:function (data){
						location.href="${pageContext.request.contextPath}/servlet/ManageLunboServlet?do=1";
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
	
	/* 修改轮播   toUpdate */
	function toUpdate(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageLunboServlet?do=4&id="+id;
	}
	
	/* 发布  fabu */
	function fabu(id,position){
		location.href="<%=request.getContextPath()%>/servlet/ManageLunboServlet?do=7&id="+id+"&position="+position;
	}
	
	/* 下架  xiajia */
	function xiajia(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageLunboServlet?do=8&id="+id;
	}
</script>
<!-- 分页 -->
<script>
	$("#state").change(function name() {
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
			$("#lbform").submit();
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
			$("#lbform").submit();
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
			$("#lbform").submit();
			
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
			$("#lbform").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#lbform").submit();
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
			$("#lbform").submit();
		}
	}
</script>
</html>