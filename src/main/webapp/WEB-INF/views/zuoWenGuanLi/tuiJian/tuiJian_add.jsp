<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加作文（推荐）</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<div style="display:inline;">
<span>作文管理>作文推荐>添加作文</span>
</div>

<div style="display:inline;float:right;">
<a href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=14" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="tjform" action="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=21" method="post" class="form-inline cmxform" role="form">
<table align="center" style="border-collapse:separate; border-spacing:0px 10px;">
	<tr>
		<td align="right">作文标题：</td>
		<td>
			<input type="text" name="zwTitle" id="zwTitle" class="form-control" style="width:178px;height:19px;" placeholder="请输入作文标题。">&nbsp;&nbsp;
			<a href="#" onclick="addQita()" style="color:#0000ff;text-decoration:none;">其他作文</a>
		</td>
	</tr>
	<tr>
		<td align="right">作文内容：</td>
		<td>
			<div>
<!-- 		    <textarea id="editor" name="content" type="text/plain" style="width:800px;height:300px;"><span style="color:#d6d6d6;">请输入作文内容，字数为1-2000字之间。</span></textarea>
 -->			<textarea id="editor" name="content" type="text/plain" style="width:800px;height:300px;" required><span style="color:#d6d6d6;">请输入作文内容，字数为1-2000字之间。</span></textarea>
			</div>
		</td>
	</tr>
	<tr>
		<td align="right">作者昵称：</td>
		<td><input type="text" name="zwName" id="zwName" class="form-control" style="width:178px;height:19px;" placeholder="请输入作者昵称。"></td>
	</tr>
	<tr>
		<td>年级：</td>
	    <td>
	    <select name="zwGrade" id="zwGrade" class="form-control" style="width:205px;">
	   		<option value="" style="color:#d6d6d6;">请选择年级</option>
	      	<c:forEach items="${geadelist}" var="g">
	    		<option value="${g.grade}">${g.grade}</option>
	      	</c:forEach>
	      	<%-- <c:forEach items="${geadelist2}" var="g2">
	    		<option value="${g2.code}">${g2.grade}</option>
	      	</c:forEach> --%>
	    </select>
	    </td>
	</tr>
	<tr>
		<td>体裁：</td>
	    <td>
	    <select name="zwStyle" id="zwStyle" class="form-control" style="width:205px;">
	   		<option value="" style="color:#d6d6d6;">请选择体裁</option>
	      	<c:forEach items="${zwStyle}" var="s" >
	    		<option value="${s.code}">${s.code_name}</option>
	      	</c:forEach>
	    </select>
	    
	    </td>
	</tr>
	
	<tr>
		<td></td>
		<td> &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="quXiao()" class="btn btn-default" data-toggle="button" style="width:90px;">取&nbsp;消</button>
			 &nbsp;&nbsp;&nbsp;&nbsp;
			<button type="submit" onclick="addTuiJian()" class="btn btn-default" data-toggle="button" style="width:90px;">发&nbsp;布</button>
		</td>
	</tr>
</table>
</form>

</div>
</div>
</div>

<!-- $('#dialogdiv') -->
<div id="dialogdiv" style="display: none">
	<form id="qtForm" action="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=20" method="post" class="form-inline" role="form">
	<div class="form-group" style="display:inline;">
		<label class="form-label" for="name">作文类型：</label>
	    <select name="type" id="type" class="form-control" style="width:178px;">
	      	<option value="1" ${type=="1"?"selected":""}>范文</option>
	      	<option value="2" ${type=="2"?"selected":""}>技法</option>
	      	<option value="3" ${type=="3"?"selected":""}>素材</option>
	    </select>
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<label class="form-label" for="name">年级：</label>
	    <select name="ageDetail" id="ageDetail" class="form-control" style="width:178px">
	   		<option value="">全部</option>
	   		<c:forEach items="${geadelist}" var="g">
	    		<option value="${g.grade}" ${nj==g.grade?"selected":"" }>${g.grade}</option>
	      	</c:forEach>
	      	<!-- <option value="">一年级</option>
	      	<option value="">二年级</option>
	      	<option value="">三年级</option>
	      	<option value="">四年级</option>
	      	<option value="">五年级</option>
	      	<option value="">六年级</option>
	      	<option value="">七年级</option>
	      	<option value="">八年级</option>
	      	<option value="">九年级</option>
	      	<option value="">高一</option>
	      	<option value="">高二</option>
	      	<option value="">高三</option> -->
	    </select>	    
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<div style="display:inline;float: right;">
		  <input type="text" name="name" id="name" value="${name}" class="form-control" style="width:178px;height:19px;" placeholder="作文名称">
		  <button class="btn btn-info" onclick="tiaojian()" type="button" style="background:#54a7fd;">查询</button>
		</div>
	</div>
	<br><br>

<!-- 数据表格 展示 -->
<table class="table table-hover" style="width: 100%;">
	<thead>
	<tr class="active" align="center">
		<td style="width:10px"></td>
		<td>作文名称</td>
		<td>作者昵称</td>
		<td>年级</td>
	</tr>
	</thead>
	<tbody>
	<c:forEach items="${fwlist}" var="l">
	<tr>
		<td style="text-align: center;width:10px"><input type="radio" name="radio" value="${l.id}"></td>
		<td name="one" style="text-align: center"><a href="##" onclick="selectFanwen(${l.id})">${l.name}</a></td>
		<td name="two" style="text-align: center">${l.author}</td>
		<td name="three" style="text-align: center">${l.ageDetail}</td>
	</tr>
	</c:forEach>
	</tbody>
</table>
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

<div id="findzwDiv" style="display: none">
	<center>
		<h3>
		<span id="titleId"></span>
		</h3>
		<p style="color:#b2b2b2">
			<span id="authorId"></span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span id="geadeId"></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span id="typeId"></span>
		</p>
	</center>
	<span id="contentId"></span>
	<br>

</div>
</body>

<!-- validate表单验证 -->
<style>
.error{
	color:red;
}
</style>
<script type="text/javascript">
	/* 发布  推荐作文   addTuiJian */
	function addTuiJian(){
		//var a = $('#zwTitle').val();
		//alert(a);
		
		var ue = UE.getEditor('editor');
		var b = ue.hasContents();
		if(!b){
			alert("请输入正文后再提交");
			return false;
		}
		
		$("#tjform").validate({
			rules:{
				zwTitle:{
					required:true,
					rangelength:[1,30]
				},
				content:{
					required:true
				},
				zwName:{
					required:true
				},
				zwGrade: {
					required:true
				},
				zwStyle:{
					required:true
				},
			},
			messages : {
				zwTitle:{
					required:"请输入标题。",
					rangelength: "标题：1-30字之间。"
				},
				content:{
					required:"请输入作文内容",
				},
				zwName:{
					required:"请输入作者",
				},
				zwGrade:{
					required:"请选择年级。"
				},
				zwStyle:{
					required:"请选择体裁",
				},
			},
			submitHandler: function(form){
			    form.submit();
			   	//var data = $("#zwform").serialize();
			   	
			} 
		})
	}
	
	/* 取消  quXiao */
	function quXiao(){
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=14";
	}

	/* 添加其他作文    addQita */
	function addQita(){
		var d = dialog({
			title: '其他作文',
			//width: '300',
			//height: '35',
			content: $('#dialogdiv'),
			//href:'fanWen.jsp',
			okValue: '确 定',
			ok: function () {
				var radio=document.getElementsByName("radio");
				var selectvalue=null;   //  selectvalue为radio中选中的值
				for(var i=0;i<radio.length;i++){
					if(radio[i].checked==true) {
						selectvalue=radio[i].value;
					}
				}
				location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=25&id="+selectvalue;
			},
			cancelValue: '取消',
			cancel: function () {}
		});
		d.showModal();
	}
	
	//其他作文--	条件
	function tiaojian(){
		var type = $("#type").val();
		var nj = $("#ageDetail").val();
		var name = $("#name").val();
		var currentPage = $("#currentPage").val();
		var numPerPage = $("#numPerPage").val();
		var a = "&type="+type+"&ageDetail="+nj+"&name="+name;
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=24"+a;
	}
	
	//其他作文--	查看作文
	function selectFanwen(id){
		$.ajax({
			url:"<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=26&id="+id,
			type:"post",
			dataType: "json",
			success:function (data){
				var a = data.name;
				var b = data.author;
				var c = data.ageDetail;
				var d = data.content;
				var e = data.type;
				$("#titleId").text(a);
				$("#authorId").text(b);
				$("#geadeId").text(c);
				$("#typeId").text(e);
				$("#contentId").append(d);
				var d1 = dialog({
					title: '其他作文',
					//width: '300',
					//height: '35',
					content: $('#findzwDiv'),
					okValue: '确 定',
					ok: function () {
					},
					cancelValue: '取消',
					cancel: function () {}
				});
				d1.showModal();
			},
			error: function (){//reload
			}
		});
	}
	
	//其他作文--	分页
	function fenye(){
		var type = $("#type").val();
		var nj = $("#ageDetail").val();
		var name = $("#name").val();
		var currentPage = $("#currentPage").val();
		var numPerPage = $("#numPerPage").val();
		var a = "&type="+type+"&ageDetail="+nj+"&name="+name+"&currentPage="+currentPage+"&numPerPage="+numPerPage;
		location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=24"+a;
	}
	
</script>
<!-- 分页 -->
<script>
	/* 首页 */
	function homePage(){
		var current = Number($("#currentPage").val());//当前页
		if(current>1){
			$("#currentPage").val("1");
			//$("#qtForm").submit();
			fenye();
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
			//$("#qtForm").submit();
			fenye();
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
			//$("#qtForm").submit();
			fenye();
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
			//$("#qtForm").submit();
			fenye();
		}else{
			alert("已是最后一页")
		}
	}
	/*每页条数，页码从第一页开始*/
	function toCount(){
		$("#currentPage").val("1");
		$("#daidpId").submit();
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
			$("#daidpId").submit();
		}
	}
</script>
<!-- ueditor富文本编辑器 -->
<script>
//实例化编辑器
//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
UE.getEditor('editor', {
	autoHeightEnabled: false,//开启滚动条
	initialFrameHeight: 400,//高度
    autoClearinitialContent:true, //focus时自动清空初始化时的内容
    wordCount:true,    //开启字数统计   false关闭
    maximumWords:2000,   //允许的最大字符数
    elementPathEnabled:false,//关闭elementPath
    autoFloatEnabled:false//浮动
    
});
/* ue.addListener('wordcountoverflow',function(){
	var chars = ue.getContentTxt();
	var schars = chars.substring(0,maximumWords);
	UE.getEditor('editor').setContent(schars);
}) */
</script>

</html>