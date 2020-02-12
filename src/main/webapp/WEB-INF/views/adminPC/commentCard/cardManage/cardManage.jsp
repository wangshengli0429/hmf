<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评点卡管理</title>
<jsp:include page="../../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">

<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<form id="cardId" action="<%=request.getContextPath()%>/servlet/CardManageServlet?do=2" method="post" class="form-inline" role="form">
	<input type="hidden" id="aa" value="${m}">
	<%-- 
	<div class="form-group">
		<label class="form-label" for="type">会员卡类型：</label>
	    <select name="type" id="type" class="form-control" style="width:178px">
	   		<option value="">全部</option>
	      	<option value="1" ${type=="1"?"selected":""}>月卡</option>
	      	<option value="2" ${type=="2"?"selected":""}>年卡</option>
	    </select>
		<label class="form-label" for="failure">状态：</label>
	    <select name="failure" id="failure" class="form-control" style="width:178px">
	   		<option value="">全部</option>
	      	<option value="0" ${failure=="0"?"selected":""}>有效</option>
	      	<option value="1" ${failure=="1"?"selected":""}>失效</option>
	    </select>
	</div>
	<br><br>
	 --%>
	<div class="form-group" style="display:inline;">
		<label class="form-label" for="">
		<button type="button" onclick="addCard()" class="btn btn-default" data-toggle="button">创建评点卡</button>
		</label>
		<label class="form-label" for="">
		<button type="button" onclick="giveCard()" class="btn btn-default" data-toggle="button">赠送评点卡</button>
		</label>
		<div style="display:inline;float: right;">
		  <input type="text" name="name" id="seachName" value="${name}" class="form-control" style="width:178px;height:19px;" placeholder="评点卡名称">
		  <button class="btn btn-info" onclick="selectYueKa()" style="background:#54a7fd;" type="button">查询</button>
		</div>
	</div>
	<br><br>

<!-- 数据表格 展示 -->
<table class="table table-hover">
<thead>
	<tr class="active">
		<td></td>
		<td>评点卡名称</td>
		<td>可使用次数</td>
		<td>创建人</td>
		<td>现价</td>
		<td>原价</td>
		<td>创建人账户</td>
		<td>有效期</td>
		<td>创建时间</td>
		<td style="text-align:center;">操作</td>
	</tr>
</thead>
<tbody>
	<c:forEach items="${list}" var="l">
	<tr>
		<td><input type="radio" name="radios" value="${l.ID}"></td>
		<td>${l.NAME}</td>
		<td>${l.COUNT}</td>
		<td>${l.USERNAME}</td>
		<td>${l.PRICE}</td>
		<td>${l.OLD_PRICE}</td>
		<td>${l.USERACCOUNT}</td>
		<td>${l.TIME}</td>
		<td>${l.CREATE_TIME}</td>
		<td style="text-align:center;">
			<button type="button" class="btn btn-info" style="background:#54a7fd;" data-toggle="button" onclick="toUpdate(${l.ID})">修改</button>
			<button type="button" class="btn btn-danger" style="background:#ff0000;" data-toggle="button" onclick="deleteCard(${l.ID})">删除</button>
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

<div id="addcardId" style="display:none;align:center;">
	<center>
	<form id="addCard" action="<%=request.getContextPath()%>/servlet/CardManageServlet?do=4" method="post" class="form-inline" role="form">
	<table style="border-collapse:separate; border-spacing:0px 10px;">
		<tr>
			<td style="float:right;text-align:center;">评点卡名称：</td>
			<td><input type="text" name="name" class="form-control" style="width:178px;height:19px;"></td>
		</tr>
		<tr>
			<td style="float:right;">评点卡可使用次数：</td>
			<td>
				<input type="number" name="count" class="form-control" style="width:205px;height:30px;display:inline;">
				<!-- <select name="count" class="form-control" style="width:205px;height:30px;">
			      	<option value="1">1</option>
			      	<option value="5">5</option>
			      	<option value="10">10</option>
			    </select> -->次
	    	</td>
		</tr>
		<tr>
			<td style="float:right;">有效期：</td>
			<td>
				<select name="validitytime" id="" class="form-control" style="width:205px;height:30px;">
			      	<option value="90">90天</option>
			      	<option value="180">180天</option>
			      	<option value="365">365天</option>
			    </select>
			</td>
		</tr>
		<tr>
			<td style="float:right;">现价：</td>
			<td><input type="number" name="price" step="0.01" class="form-control" style="width:205px;height:30px;display:inline;">元</td>
		</tr>
		<tr>
			<td style="float:right;">原价：</td>
			<td><input type="number" name="old_price" step="0.01" class="form-control" style="width:205px;height:30px;display:inline;">元</td>
		</tr>
	</table>
	</form>
	</center>
</div>

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
	
	/* 条件查询 */
	function selectYueKa(){
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
		$("#cardId").submit();
	}

	/* 删除   deleteCard */
	function deleteCard(id){
		var d = dialog({
			title: '提示',
			width: '300',
			//height: '35',
			content: '<center><h4><b>是否删除？</b></h4></center>',
			okValue: '确 定',
			ok: function () {
				$.ajax({
					url:"${pageContext.request.contextPath}/servlet/CardManageServlet?do=3",
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
								location.href="<%=request.getContextPath()%>/servlet/CardManageServlet?do=2";
							}
						});
						d.showModal();
					},
					error: function (){//reload
						alert("请求失败！");
					}
				});
			},
			cancelValue: '取消',
			cancel: function () {}
		});
		d.showModal();
	}
	
	/* 创建评点卡 addCard */
	function addCard(){
		var d = dialog({
			title: '创建评点卡',
			width: '500px',
			//height: '35',
			content: $("#addcardId"),
			okValue: '确 定',
			ok: function () {
				$.ajax({
					url:"${pageContext.request.contextPath}/servlet/CardManageServlet?do=4",
					type:"post",
					data:$("#addCard").serialize(),
					success:function (data){
						var d = dialog({
							title: '提示',
							width: '300',
							//height: '35',
							content: '<center><h4><b>'+data+'</b></h4></center>',
							okValue: '确 定',
							ok: function () {
								location.href="<%=request.getContextPath()%>/servlet/CardManageServlet?do=2";
							}
						});
						d.showModal();
					},
					error: function (){//reload
						alert("请求失败！");
					}
				});
			},
			cancelValue: '取消',
			cancel: function () {}
		});
		d.showModal();
	}
	
	/* 赠送评点卡 giveCard */
	function giveCard(){
		var c = $('input:radio:checked').val();
		if (c == "" || c == null){
			alert("请选择赠送类型！");
			return false;
		}
		var d = dialog({
			title: '选择人员',
			width: '1000px',
			height: '450px',
			content: '<iframe id="newFream" frameborder="0" src="<%=request.getContextPath()%>/servlet/CardManageServlet?do=1" width="100%" height="100%"></iframe>',
			okValue: '确 定',
			ok: function () {
				var obj = $("#newFream").contents().find("input[name='stuid']");
				var a = "";
				for(var i=0; i<obj.length; i++){ 
					if(obj[i].checked){
						a += obj[i].value+','; //如果选中，将value添加到变量s中 
					}
				}
				$.ajax({
					type: "POST",  
					url: "<%=request.getContextPath()%>/servlet/CardManageServlet?do=7&sids="+a+"&cid="+c,  
					dataType: "text",  
					success: function(data){
						var d = dialog({
							title: '提示',
							width: '300',
							//height: '35',
							content: '<center><h4><b>'+data+'</b></h4></center>',
							okValue: '确 定',
							ok: function () {
								location.href="<%=request.getContextPath()%>/servlet/CardManageServlet?do=2";
							}
						});
						d.showModal();
					}  
		        });
			},
			cancelValue: '取消',
			cancel: function () {}
		});
		d.showModal();
	}
	
	/* 修改   toUpdate */
	function toUpdate(id){
		var d = dialog({
			title: '修改评点卡',
			width: '800px',
			height: '230px',
			content: '<iframe id="newFream" frameborder="0" src="<%=request.getContextPath()%>/servlet/CardManageServlet?do=5&id='+id+'" width="100%" height="100%"></iframe>',
			okValue: '确 定',
			ok: function () {
				var name = $("#newFream").contents().find("input[name='name']").val();
				var count = $("#newFream").contents().find("[name='count']").val();
				var validitytime = $("#newFream").contents().find("[name='validitytime']").val();
				var price = $("#newFream").contents().find("input[name='price']").val();
				var old_price = $("#newFream").contents().find("input[name='old_price']").val();
				$.ajax({
					type: "POST",  
					url: "<%=request.getContextPath()%>/servlet/CardManageServlet?do=6",
					data: {"id":id,"name":name,"count":count,"validitytime":validitytime,"price":price,"old_price":old_price},
					dataType: "text",  
					success: function(data){
						var d = dialog({
							title: '提示',
							width: '300',
							//height: '35',
							content: '<center><h4><b>'+data+'</b></h4></center>',
							okValue: '确 定',
							ok: function () {
								location.href="<%=request.getContextPath()%>/servlet/CardManageServlet?do=2";
							}
						});
						d.showModal();
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
	$("#state").change(function name() {
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
	$("#seachName").change(function name() {
		$("#currentPage").val('1');
		$("#numPerPage").val('10');
	})
	/* 首页 */
	function homePage(){
		var current = Number($("#currentPage").val());//当前页
		if(current>1){
			$("#currentPage").val("1");
			$("#cardId").submit();
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
			$("#cardId").submit();
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
			$("#cardId").submit();
			
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
			$("#cardId").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#cardId").submit();
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
			$("#cardId").submit();
		}
	}
</script>
</html>