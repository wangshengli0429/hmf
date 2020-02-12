<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看老师资料</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<c:forEach items="${list}" var="l">
<div style="display:inline;">
<span>已审核>${l.name}</span>
</div>
<div style="display:inline;float:right;">
<a href="<%=request.getContextPath()%>/servlet/ManageUsersServlet?do=8" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="teacherform" class="form-inline cmxform" role="form">
<table align="center" style="border-collapse:separate; border-spacing:0px 10px;">
	<tr>
		<td align="right">姓名：</td>
		<td>${l.name}</td>
	</tr>
	<tr>
		<td valign="top" align="right">资料：</td>
		<td>
			<!-- // 查看图片 // -->
			<div onclick="picClose()" id="divCenter" align="center" style="position:absolute;z-index:9999;display:none;margin-right:30px;vertical-align:middle;text-align:center;">
		        <img id="imgID" src="" style="width:100%;height:100%;"/>
		    </div>
		    <!-- 资料图片 -->
		    <c:if test="${empty l.certUrl && empty l.card1 &&empty l.card2}">
				 未上传
			</c:if>
			<div style="display:inline;">
				<c:if test="${not empty l.certUrl}">
				<a name='fff' href='javascript:void(0);' onclick='picBig1()'>
					<img id="img1" src="${l.certUrl}" width="200px" height="200px">
				</a>
				</c:if>
			</div>
			<div style="display:inline;padding-left:5px;">
				<c:if test="${not empty l.card1}">
				<a name='fff' href='javascript:void(0);' onclick='picBig2()'>
					<img id="img2" src="${l.card1}" width="200px" height="200px">
				</a>
				</c:if>
			</div>
			<div style="display:inline;padding-left:5px;">
				<c:if test="${not empty l.card2}">
				<a name='fff' href='javascript:void(0);' onclick='picBig3()'>
					<img id="img3" src="${l.card2}" width="200px" height="200px">
				</a>
				</c:if>
			</div>
			<br>
			${l.austste=="2"?"未通过 ":"通过"}
		</td>
	</tr>
	<tr>
		<td valign="top">预批改作文：</td>
		<td>
			<c:if test="${empty l.title}">
				 未上传
			</c:if>
			<a href="javascript:findZuoWen()" style="text-decoration:none;"><span id="title">${l.title}</span></a>
			<br>
			<c:forEach items="${ydp}" var="y">
				${y.state==1?"通过 ":""} ${y.state==2?"未通过 ":""}
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td align="right">个人信息：</td>
	    <td>
	    	<a href="javascript:findPersonal()" style="text-decoration:none;">查看个人信息</a>
	    </td>
	</tr>
</table>
</form>
</c:forEach>

	<!-- 查看预点评作文 $('#selectdiv') -->
	<div id="selectdiv" style="display: none">
	<c:forEach items="${ydp}" var="y">
		<div style="display:inline;">
			<center>
				<h3><b>${y.title}</b></h3>
				<pre style="color:#949494;background-color:#ffffff;border:0;">${y.geade}</pre>
			</center>
		</div>
		<div style="display:inline;margin-right:100px;margin-top:-90px;float:right;">
			<font style="color:red;font-size:35px;"><b>${y.score}</b></font>
			
		</div>
		<div style="font-size:16px;line-height:35px;">
		${y.content}
		 </div>
		
	<br>
	<div style="font-size:14px;line-height:35px;background-color:#ffffff;border:0;">
		<input type="button" style="background:#ff8000;color:#ffffff;width:5px;height:30px;" >
		<span style="font-size:17px;">&nbsp;&nbsp;名师点评</span>
	</div>
	
	<input type="hidden" id="scoreId" value="${y.score}">
	<input type="hidden" id="geadeId" value="${y.geade}">
	<!-- 小学评分标准 -->
	<div id="pfbz1">
	<div style="margin-left:20px;margin-top:5px;">
		<p style="font-size:16px;margin-top:10px;">
	  	内容  &nbsp;&nbsp;&nbsp;&nbsp; 
	  	<input type="button" value="${y.categoryCa}" class="btn btn-default" style="border:1px solid #ff8000;color:#ff8000;width:70px;height:30px;">
	  	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dpcontent}<br><br>
	 	 语言 &nbsp;&nbsp;&nbsp;&nbsp; 
	 	<input type="button" value="${y.languageCa}" class="btn btn-default" style="border:1px solid #ff8000;color:#ff8000;width:70px;height:30px;">
	 	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dplanguage}<br><br>
	 	 书写  &nbsp;&nbsp;&nbsp;&nbsp;
	 	<input type="button" value="${y.writingCa}" class="btn btn-default" style="border:1px solid #ff8000;color:#ff8000;width:70px;height:30px;">
	 	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dpwriting}<br><br>
		</p>
	</div>
	</div>
	<!-- 初中评分标准 -->
	<div id="pfbz2">
	<div style="margin-left:20px;margin-top:5px;">
		<p style="font-size:16px;margin-top:10px;">
	  	内容  &nbsp;&nbsp;&nbsp;&nbsp; 
	  	<input type="button" value="${y.categoryCa}" class="btn btn-default" style="border:1px solid #ff8000;color:#ff8000;width:70px;height:30px;">
	  	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dpcontent}<br><br>
	 	 语言 &nbsp;&nbsp;&nbsp;&nbsp; 
	 	<input type="button" value="${y.languageCa}" class="btn btn-default" style="border:1px solid #ff8000;color:#ff8000;width:70px;height:30px;">
	 	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dplanguage}<br><br>
	  	结构  &nbsp;&nbsp;&nbsp;&nbsp;
	  	<input type="button" value="${y.structureCa}" class="btn btn-default" style="border:1px solid #ff8000;color:#ff8000;width:70px;height:30px;">
	  	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dpstructure}<br><br>
	 	 书写  &nbsp;&nbsp;&nbsp;&nbsp;
	 	<input type="button" value="${y.writingCa}" class="btn btn-default" style="border:1px solid #ff8000;color:#ff8000;width:70px;height:30px;">
	 	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dpwriting}<br><br>
		</p>
	</div>
	</div>
	<!-- 高中评分标准 -->
	<div id="pfbz3">
	<div style="margin-left:20px;margin-top:5px;">
		<p style="font-size:16px;margin-top:10px;">
	  	内容  &nbsp;&nbsp;&nbsp;&nbsp; 
	  	<input type="button" value="${y.categoryCa}" class="btn btn-default" style="border:1px solid #ff8000;color:#ff8000;width:70px;height:30px;">
	  	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dpcontent}<br><br>
	 	 表达 &nbsp;&nbsp;&nbsp;&nbsp; 
	 	<input type="button" value="${y.languageCa}" class="btn btn-default" style="border:1px solid #ff8000;color:#ff8000;width:70px;height:30px;">
	 	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dplanguage}<br><br>
	  	特征  &nbsp;&nbsp;&nbsp;&nbsp;
	  	<input type="button" value="${y.structureCa}" class="btn btn-default" style="border:1px solid #ff8000;color:#ff8000;width:70px;height:30px;">
	  	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dpstructure}<br><br>
		</p>
	</div>
	</div>
	
	<div style="font-size:14px;line-height:35px;background-color:#ffffff;border:0;">
		<input type="button" style="background:#ff8000;color:#ffffff;width:5px;height:30px;" >
		<span style="font-size:17px;">&nbsp;&nbsp;总评</span>
	</div>
	<div style="margin-left:20px;">
		<br>
		<input type="button" value="得分点" class="btn" style="font-size:15px;vertical-align:middle;background:#ff8000;color:#ffffff;width:70px;height:30px;">
		<p style="font-size:16px;margin-top:10px;">
			${y.scoring}
		</p>
		<input type="button" value="失分点" class="btn" style="font-size:15px;vertical-align:middle;background:#ff8000;color:#ffffff;width:70px;height:30px;">
		<p style="font-size:16px;">
			${y.points}
		</p>
		<input type="button" value="建议" class="btn" style="font-size:15px;vertical-align:middle;background:#ff8000;color:#ffffff;width:70px;height:30px;">
		<p style="font-size:16px;">
			${y.suggest}
		</p>
	</div>
	<br>
	</c:forEach>
	<div style="font-size:14px;line-height:35px;background-color:#ffffff;border:0;">
		<input type="button" style="background:#ff8000;color:#ffffff;width:5px;height:30px;" >
		<span style="font-size:17px;">&nbsp;&nbsp;作文等级</span>
	</div>
	<div style="margin-left:20px;margin-top:5px;">
		<span style="font-size:10px;margin-left:10px;font-size:25px;" id="spanId"></span>
	</div>
	</div>
	
	<!-- 查看  个人信息 $('#personaldiv') -->
	<div id="personaldiv" style="display: none">
	<c:forEach items="${list}" var="l">
		<table align="center" width="600px" style="line-height:200%">
			<tr>
				<td width="20%" style="text-align:right;">用户名：</td>
				<td>${l.userName}</td>
			</tr>
			<tr>
				<td width="20%" style="text-align:right;">姓名：</td>
				<td>${l.name}</td>
			</tr>
			<tr>
				<td width="20%" style="text-align:right;">性别：</td>
				<td>${l.sex}</td>
			</tr>
			<tr>
				<td width="20%" style="text-align:right;">学校：</td>
				<td>${l.school}</td>
			</tr>
			<tr>
				<td width="20%" style="text-align:right;">年级：</td>
				<td>${l.grade}</td>
			</tr>
			<tr>
				<td width="20%" style="text-align:right;">省市：</td>
				<td>${l.city}</td>
			</tr>
			<tr>
				<td width="20%" style="text-align:right;">教龄时间（年）：</td>
				<td>${l.eduTime}</td>
			</tr>
			<tr>
				<td width="20%" style="text-align:right;">中高考阅卷经验：</td>
				<td>${l.exper=="0"?"无":""}${l.exper=="1"?"有":""}</td>
			</tr>
			<tr>
				<td valign="top" width="20%" style="text-align:right;">教学成就：</td>
				<td>${l.acv}</td>
			</tr>
			<tr>
				<td valign="top" width="20%" style="text-align:right;">个人荣誉：</td>
				<td>${l.honor}</td>
			</tr>
			<tr>
				<td valign="top" width="20%" style="text-align:right;">教学特色：</td>
				<td>${l.features}</td>
			</tr>
		</table>
	</c:forEach>
	</div>

</div>
</div>
</div>
</body>

<script type="text/javascript">
	/* 查看预批改作文 */
	function findZuoWen(){
		var a = $("#title").text();
		var d = dialog({
			title: a,
			width: '1000',
			//height: '35',
			content: $('#selectdiv'),
			/* okValue: '确 定',
			ok: function () {},
			cancelValue: '取消',
			cancel: function () {} */
		});
		d.showModal();
	}
	
	/* 查看个人信息 */
	function findPersonal(){
		var d = dialog({
			title: '查看个人信息',
			width: '1000',
			//height: '35',
			content: $('#personaldiv'),
			/* okValue: '确 定',
			ok: function () {},
			cancelValue: '取消',
			cancel: function () {} */
		});
		d.showModal();
	}
</script>

<!-- 评分标准 -->
<script type="text/javascript">
$(function (){
	var pf1 = document.getElementById("pfbz1"); 
	var pf2 = document.getElementById("pfbz2"); 
	var pf3 = document.getElementById("pfbz3"); 
	pf1.style.display = "none";
	pf2.style.display = "none";
	pf3.style.display = "none";
	var geadeId = $("#geadeId").val();
	if(geadeId=="低年级"||geadeId=="小学"||geadeId=="一年级"||geadeId=="二年级"||geadeId=="三年级"||geadeId=="四年级"||geadeId=="五年级"||geadeId=="六年级"){
		pf1.style.display = "block";
	}else if(geadeId=="初中"||geadeId=="初一"||geadeId=="初二"||geadeId=="初三"||geadeId=="七年级"||geadeId=="八年级"||geadeId=="九年级"){
		pf2.style.display = "block";
	}else if(geadeId=="高一"||geadeId=="高二"||geadeId=="高三"){
		pf3.style.display = "block";
	}
})
//分值（作文等级）
$(function (){
	var fs = $("#scoreId").val();
	var geadeId = $("#geadeId").val();
	if(fs!=null){
		if(geadeId=="低年级"||geadeId=="小学"||geadeId=="一年级"||geadeId=="二年级"||geadeId=="三年级"||geadeId=="四年级"||geadeId=="五年级"||geadeId=="六年级"){
			if(fs>=27&&fs<=30){
				$("#spanId").text("一类文");
				$("#spanId").show();
			}
			if(fs>=21&&fs<=26){
				$("#spanId").text("二类文");
				$("#spanId").show();
			}
			if(fs>=18&&fs<=20){
				$("#spanId").text("三类文");
				$("#spanId").show();
			}
			if(fs>=12&&fs<=17){
				$("#spanId").text("四类文");
				$("#spanId").show();
			}
			if(fs<=11){
				$("#spanId").text("五类文");
				$("#spanId").show();
			}
		}
		if(geadeId=="初中"||geadeId=="初一"||geadeId=="初二"||geadeId=="初三"||geadeId=="七年级"||geadeId=="八年级"||geadeId=="九年级"){
			if(fs>=46&&fs<=50){
				$("#spanId").text("一类文");
				$("#spanId").show();
			}
			if(fs>=40&&fs<=45){
				$("#spanId").text("二类文");
				$("#spanId").show();
			}
			if(fs>=35&&fs<=39){
				$("#spanId").text("三类文");
				$("#spanId").show();
			}
			if(fs>=30&&fs<=34){
				$("#spanId").text("四类文");
				$("#spanId").show();
			}
			if(fs<=29){
				$("#spanId").text("五类文");
				$("#spanId").show();
			}
		}
		if(geadeId=="高中"||geadeId=="高一"||geadeId=="高二"||geadeId=="高三"){
			if(fs>=52&&fs<=60){
				$("#spanId").text("一类文");
				$("#spanId").show();
			}
			if(fs>=45&&fs<=51){
				$("#spanId").text("二类文");
				$("#spanId").show();
			}
			if(fs>=36&&fs<=44){
				$("#spanId").text("三类文");
				$("#spanId").show();
			}
			if(fs>=30&&fs<=35){
				$("#spanId").text("四类文");
				$("#spanId").show();
			}
			if(fs<=29){
				$("#spanId").text("五类文");
				$("#spanId").show();
			}
		}
	}
})
</script>

<!-- // 查看图片 // -->
<script>
       function picBig1() {
       	var s = document.getElementById("img1").src;
       	document.getElementById("imgID").src = s;
           document.getElementById("divCenter").style.display="block";
       }
       function picBig2() {
       	var s = document.getElementById("img2").src;
       	document.getElementById("imgID").src = s;
           document.getElementById("divCenter").style.display="block";
       }
       function picBig3() {
       	var s = document.getElementById("img3").src;
       	document.getElementById("imgID").src = s;
           document.getElementById("divCenter").style.display="block";
       }
       function picClose() {
           document.getElementById("divCenter").style.display="none";
       }
   </script>

</html>