<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>结算管理</title>
<jsp:include page="../../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">

<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<form id="zdform" action="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=10" method="post" class="form-inline" role="form">
	<input type="hidden" id="aa" value="${m}">
	<div class="form-group">
	    <label class="form-label" for="name">生成账单截止时间：</label>
	    <!-- <input type="text" name="" class="form-control Wdate" onclick="WdatePicker()" style="width:178px;height:19px;" placeholder="开始时间"> -->
	    <input type="text" name="clearingTime" id="clearingTime" value="${clearingTime}" class="form-control Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd 00:00:00'})" style="width:178px;height:19px;" placeholder="生成账单时间">
	</div>
	<br><br>
	
	<div class="form-group" style="display:inline;">
		<label class="form-label" for="name">
		<button type="button" onclick="exportJieSuan()" class="btn btn-default glyphicon glyphicon-share-alt" data-toggle="button">&nbsp;导出</button>
		</label>
	    &nbsp;&nbsp;&nbsp;&nbsp;
		<label class="form-label" for="name">
		<button type="button" onclick="scDingDan()" class="btn btn-default" data-toggle="button">生成账单</button>
		</label>
		<div style="display:inline;float: right;">
		  <input type="text" name="name" id="seachName" value="${name}" class="form-control" style="width:178px;height:19px;" placeholder="老师姓名/老师账号">
		  <button class="btn btn-info" onclick="selectZhangdan()" style="background:#54a7fd;" type="button">查询</button>
		</div>
	</div>
	<br><br>

<!-- 数据表格 展示 -->
<table class="table table-hover">
<thead>
	<tr class="active">
		<td><input type="checkbox" id="checkAll" onclick="quanxuan()"/></td>
		<td>老师姓名</td>
		<td>老师账号</td>
		<td>点评篇数（篇）</td>
		<td>应结金额（元）</td>
		<td>操作人员账号</td>
		<td>结算状态</td>
		<td style="text-align:center;">生成账单时间</td>
		<td style="text-align:center;">结算时间</td>
		<td style="text-align:center;">操作</td>
	</tr>
</thead>
<tbody>
	<c:forEach items="${zdlist}" var="z">
	<tr>
		<td><input type="checkbox" name="checkAll" value="${z.id}"/></td>
		<td>${z.tname}</td>
		<td>${z.tphone}</td>
		<td>${z.num}</td>
		<td>${z.price}</td>
		<td>${z.userName}</td>
		<td>${z.state==1?"已结算":"未结算"}</td>
		<td style="text-align:center;">${z.billTime}</td>
		<td style="text-align:center;">${z.cleartime}</td>
		<td>
			<c:if test="${not empty z.state}">
			<button type="button" onclick="toFindyi(${z.id})" class="btn" style="background:#54a7fd;">查看</button>
			</c:if>
			<c:if test="${empty z.state}">
			<button type="button" onclick="toFindwei(${z.id})" class="btn" style="background:#54a7fd;">查看</button>
			</c:if>
			<c:if test="${empty z.cleartime}">
			<button type="button" onclick="jieSuan('${z.id}','${z.num}','${z.price}')" class="btn" style="background:#54a7fd;">结算</button>
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
	
	/* 导出   export */
	function exportJieSuan(){
		var ids ="";
		var array = $('input[name="checkAll"]:checked').each(
			function(){ 
				ids += ","+$(this).val(); 
			}
		); 
		ids=ids.substr(1);
		if (ids.length>0) {
			location.href="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=14&ids="+ids;
		}else {
			alert("请选择需要导出的数据！")
		}
	}
	/* 生产订单   scDingDan */
	function scDingDan(){
		var a = $("#clearingTime").val();
		if(a==null||a==""){
			alert("请选择时间！");
		}else{
			location.href="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=9&clearingTime="+a;
		}
	}
	
	/* 条件查询 */
	function selectZhangdan(){
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
		$("#zdform").submit();
	}
	
	/* 查看已结算   toFindyi */
	function toFindyi(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=12&id="+id;
	}
	/* 查看未结算   toFindwei */
	function toFindwei(id){
		location.href="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=11&id="+id;
	}
	
	/* 结算   jieSuan */
	function jieSuan(id,num,price){
		var d = dialog({
			title: '提示',
			width: '600',
			//height: '35',
			content: '<center><h4><b>是否结算？</b></h4>点评篇数（篇）为：'+num+'&nbsp;&nbsp;&nbsp;&nbsp;应结金额（元）为：<span style="color:red">'+price+'</span></center>',
			okValue: '确 定',
			ok: function () {
				$.ajax({
					url:"<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=13&id="+id+"&num="+num+"&price="+price,
					type:"post",
					success:function (){
						location.href="<%=request.getContextPath()%>/servlet/ManageJiaoyiServlet?do=10";
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

<!-- 全选  反选 -->
<script>
	function quanxuan(){
		if($("#checkAll").prop("checked")){
			$("input[name='checkAll']").prop("checked", true);
		}else{
			$("input[name='checkAll']").prop("checked", false);
		}
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
			$("#zdform").submit();
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
			$("#zdform").submit();
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
			$("#zdform").submit();
			
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
			$("#zdform").submit();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#zdform").submit();
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
			$("#zdform").submit();
		}
	}
</script>
<!-- <script>  /* 复选框  全选 */
       $(function(){  
           function initTableCheckbox() {  
               var $thr = $('table thead tr');  
               var $checkAllTh = $('<th><input type="checkbox" id="checkAll" name="checkAll" /></th>');  
               /*将全选/反选复选框添加到表头最前，即增加一列*/  
               $thr.prepend($checkAllTh);  
               /*“全选/反选”复选框*/  
               var $checkAll = $thr.find('input');  
               $checkAll.click(function(event){  
                   /*将所有行的选中状态设成全选框的选中状态*/  
                   $tbr.find('input').prop('checked',$(this).prop('checked'));  
                   /*并调整所有选中行的CSS样式*/  
                   if ($(this).prop('checked')) {  
                       $tbr.find('input').parent().parent().addClass('warning');  
                   } else{  
                       $tbr.find('input').parent().parent().removeClass('warning');  
                   }  
                   /*阻止向上冒泡，以防再次触发点击操作*/  
                   event.stopPropagation();  
               });  
               /*点击全选框所在单元格时也触发全选框的点击操作*/  
               $checkAllTh.click(function(){  
                   $(this).find('input').click();  
               });  
               var $tbr = $('table tbody tr');  
               var $checkItemTd = $('<td><input type="checkbox" name="checkItem" /></td>');  
               /*每一行都在最前面插入一个选中复选框的单元格*/  
               $tbr.prepend($checkItemTd);  
               /*点击每一行的选中复选框时*/  
               $tbr.find('input').click(function(event){  
                   /*调整选中行的CSS样式*/  
                   $(this).parent().parent().toggleClass('warning');  
                   /*如果已经被选中行的行数等于表格的数据行数，将全选框设为选中状态，否则设为未选中状态*/  
                   $checkAll.prop('checked',$tbr.find('input:checked').length == $tbr.length ? true : false);  
                   /*阻止向上冒泡，以防再次触发点击操作*/  
                   event.stopPropagation();  
               });  
               /*点击每一行时也触发该行的选中操作*/  
               $tbr.click(function(){  
                   $(this).find('input').click();  
               });  
           }  
           initTableCheckbox();  
       });  
</script> -->
</html>