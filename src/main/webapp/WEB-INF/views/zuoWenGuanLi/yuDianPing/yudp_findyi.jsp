<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>预点评作文-已点评-查看已点评作文</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body>
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

	<c:forEach items="${yidplist}" var="yi">
		<div style="display:inline;">
		<span>作文管理>预点评作文>已点评>${yi.newTitle}</span>
		</div>
		
		<div style="display:inline;float:right;">
		<a href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=5" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
		</div>
		<hr>
		
		<c:if test="${not empty yi.propo}">
			要求：${yi.propo}
		</c:if>
		
		<center>
			<h3>
			<b>${yi.newTitle}</b>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span style="color:red;size:8;">${yi.score}</span>
			</h3>
			<p style="color:#b2b2b2">
				作者昵称：${yi.author} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				${yi.draft}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				${yi.geade}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				${yi.createTimes}
			</p>
		</center>
		${yi.content}
		<br>
		
		<!-- // 查看图片 // -->
		<c:if test="${not empty yi.img1}">
	    	<a name='fff' href='javascript:void(0);' onclick='picBig1()'><img id="i1" src="<%=request.getContextPath()%>${yi.img1}" style="width:100px;"></a>
		</c:if>
		<c:if test="${not empty yi.img2}">
	    	<a name='fff' href='javascript:void(0);' onclick='picBig2()'><img id="i2" src="<%=request.getContextPath()%>${yi.img2}" style="width:100px;"></a>
	    </c:if>
	    <c:if test="${not empty yi.img3}">
	    	<a name='fff' href='javascript:void(0);' onclick='picBig3()'><img id="i3" src="<%=request.getContextPath()%>${yi.img3}" style="width:100px;"></a>
		</c:if>
		<!-- // 查看图片 // left:200px;-->
		<div id="divCenter1" align="center" style="position: absolute; z-index: 9999; display: none;left:50%;top:50%;margin-left:-350px;margin-top:-100px;">
	        <div style="display:inline;" onclick="picClose()">
	        	<img src="<%=request.getContextPath()%>${yi.img1}" width=700px/>
	        </div>
	    </div>
	    <div id="divCenter2" align="center" style="position: absolute; z-index:9999; display: none;left:50%;top:50%;margin-left:-350px;margin-top:-100px;">
	        <div style="display:inline;" onclick="picClose()">
	        	<img src="<%=request.getContextPath()%>${yi.img2}" width=700px/>
	        </div>
	    </div>
	    <div id="divCenter3" align="center" style="position: absolute; z-index: 9999; display: none;left:50%;top:50%;margin-left:-350px;margin-top:-100px;">
	        <div style="display:inline;" onclick="picClose()">
	        	<img src="<%=request.getContextPath()%>${yi.img3}" width=700px/>
	        </div>
	    </div>
		<!-- // 查看图片 // -->
		<br>
		
		<img src="<%=request.getContextPath()%>/images/1.png" alt="alttext" title="名师点评" style="float:left;width:1280px;">
		<table style="border-collapse:separate; border-spacing:0px 10px;">
			<tr>
				<td width="40px"></td>
				<td width="200px">点评老师：${yi.name}</td>
				<td width="600px">点评时间：${yi.comTimes}</td>
			</tr>
		</table>
		<input type="hidden" id="scoreId" value="${yi.score}">
		<table>
			<tr>
				<td width="100px"></td>
				<td width="400px"><h4><span id="spanId"></span></h4></td>
			</tr>
		</table>
		<input type="hidden" id="geadeId" value="${yi.geade}">
		<input type="hidden" name="pcontent" id="pcontent" value="${yi.dpcontent}">
		<input type="hidden" name="planguage" id="planguage" value="${yi.dplanguage}">
		<input type="hidden" name="pstructure" id="pstructure" value="${yi.dpstructure}">
		<input type="hidden" name="pwriting" id="pwriting" value="${yi.dpwriting}">
		<div id="id1">
		<span style="font-size:17px;margin-left:40px;">小学作文评分标准：</span>
		<span style="font-size:17px;margin-left:40px;color:#ff9900;" name="spanId"></span>
		<table id="tableTime1" border="1" style="border-collapse:separate; margin-left:100px;">
			<tr>
				<td width="140px" style="text-align:center"></td>
				<c:forEach items="${pingfen}" var="p">
				<td width="1000px" style="text-align:center"></td>
				</c:forEach>
			</tr>
			<tr>
				<td width="140px" style="text-align:center"><img src="<%=request.getContextPath()%>/images/3.png" alt="alttext" title="名师点评" style="float:left;width:100px;height:40px"></td>
				<c:forEach items="${pingfen}" var="p">
				<td width="1000px" style="text-align:center">${p.category}（${p.ScoreValue}分）</td>
				</c:forEach>
			</tr>
			<tr id="A1">
				<td width="140px" style="text-align:center">内容</td>
				<c:forEach items="${pingfen}" var="p">
				<td width="1000px" name="content1">${p.content}</td>
				</c:forEach>
			</tr>
			<tr id="B1">
				<td width="140px" style="text-align:center">语言</td>
				<c:forEach items="${pingfen}" var="p">
				<td width="1000px" name="language1">${p.language}</td>
				</c:forEach>
			</tr>
			<tr id="C1">
				<td width="140px" style="text-align:center">书面</td>
				<c:forEach items="${pingfen}" var="p">
				<td width="1000px" name="writing1">${p.writing}</td>
				</c:forEach>
			</tr>
		</table>
		</div>
		<div id="id2">
		<span style="font-size:17px;margin-left:40px;">初中作文评分标准：</span>
		<span style="font-size:17px;margin-left:40px;color:#ff9900;" name="spanId"></span>
		<table id="tableTime2" border="1" style="border-collapse:separate; margin-left:100px;">
			<tr>
				<td width="140px" style="text-align:center"><img src="<%=request.getContextPath()%>/images/3.png" alt="alttext" title="名师点评" style="float:left;width:100px;height:40px"></td>
				<c:forEach items="${pingfen}" var="p">
				<td width="1000px" style="text-align:center">${p.category}（${p.ScoreValue}分）</td>
				</c:forEach>
			</tr>
			<tr id="A2">
				<td width="140px" style="text-align:center">内容</td>
				<c:forEach items="${pingfen}" var="p">
				<td width="1000px" name="content2">${p.content}</td>
				</c:forEach>
			</tr>
			<tr id="B2">
				<td width="140px" style="text-align:center">语言</td>
				<c:forEach items="${pingfen}" var="p">
				<td width="1000px" name="language2">${p.language}</td>
				</c:forEach>
			</tr>
			<tr id="C2">
				<td width="140px" style="text-align:center">结构</td>
				<c:forEach items="${pingfen}" var="p">
				<td width="1000px" name="structure2">${p.structure}</td>
				</c:forEach>
			</tr>
			<tr id="D2">
				<td width="140px" style="text-align:center">书面</td>
				<c:forEach items="${pingfen}" var="p">
				<td width="1000px" name="writing2">${p.writing}</td>
				</c:forEach>
			</tr>
		</table>
		</div>
		<div id="id3">
		<span style="font-size:17px;margin-left:40px;">高中作文评分标准：</span>
		<span style="font-size:17px;margin-left:40px;color:#ff9900;" name="spanId"></span>
		<table id="tableTime3" border="1" style="border-collapse:separate; margin-left:100px;">
			<tr>
				<td width="140px" style="text-align:center"><img src="<%=request.getContextPath()%>/images/3.png" alt="alttext" title="名师点评" style="float:left;width:100px;height:40px"></td>
				<c:forEach items="${pingfen}" var="p">
				<td width="1000px" style="text-align:center">${p.category}（${p.ScoreValue}分）</td>
				</c:forEach>
			</tr>
			<tr id="A3">
				<td width="140px" style="text-align:center">内容</td>
				<c:forEach items="${pingfen}" var="p">
				<td width="1000px" name="content3">${p.content}</td>
				</c:forEach>
			</tr>
			<tr id="B3">
				<td width="140px" style="text-align:center">表达</td>
				<c:forEach items="${pingfen}" var="p">
				<td width="1000px" name="express3">${p.express}</td>
				</c:forEach>
			</tr>
			<tr id="C3">
				<td width="140px" style="text-align:center">特征</td>
				<c:forEach items="${pingfen}" var="p">
				<td width="1000px" name="feature3">${p.feature}</td>
				</c:forEach>
			</tr>
		</table>
		</div>
		
		
		<img src="<%=request.getContextPath()%>/images/2.png" alt="alttext" title="评点教育" style="float:left;width:1280px;">
		<table style="border-collapse:separate; border-spacing:0px 20px;">
			<tr>
				<td width="140px"></td>
				<td width="70px" style="vertical-align:top">得分点：</td>
				<td width="1070px">
				${yi.scoring}
				</td>
			</tr>
			<tr>
				<td width="140px"></td>
				<td width="70px" style="vertical-align:top">失分点：</td>
				<td width="1070px">
				${yi.points}
				</td>
			</tr>
			<tr>
				<td width="140px"></td>
				<td width="70px" style="vertical-align:top">建议：</td>
				<td width="1070px">
				${yi.suggest}
				</td>
			</tr>
		</table>
	</c:forEach>

</div>
</div>
</div>
</body>

<script>
	/* 评分标准 */
	$(function (){
		var geadeId = $("#geadeId").val();
		var a = $("#pcontent").val();
		var b = $("#planguage").val();
		var c = $("#pstructure").val();
		var d = $("#pwriting").val();
		var tableTime1 = document.getElementById("id1"); 
		var tableTime2 = document.getElementById("id2"); 
		var tableTime3 = document.getElementById("id3"); 
		tableTime1.style.display ="none";
		tableTime2.style.display ="none";
		tableTime3.style.display ="none";
		
		var fs = $("#scoreId").val();
		var spanId = document.getElementById("spanId");
		spanId.style.display ="none";
		
		if(geadeId=="低年级"||geadeId=="小学"||geadeId=="一年级"||geadeId=="二年级"||geadeId=="三年级"||geadeId=="四年级"||geadeId=="五年级"||geadeId=="六年级"){
			if(fs!=null||fs!=""){
				if(fs>=27&&fs<=30){
					$("span[name='spanId']").text("一类文");
					$("span[name='spanId']").show();
				}
				if(fs>=21&&fs<=26){
					$("span[name='spanId']").text("二类文");
					$("span[name='spanId']").show();
				}
				if(fs>=18&&fs<=20){
					$("span[name='spanId']").text("三类文");
					$("span[name='spanId']").show();
				}
				if(fs>=12&&fs<=17){
					$("span[name='spanId']").text("四类文");
					$("span[name='spanId']").show();
				}
				if(fs<=11){
					$("span[name='spanId']").text("五类文");
					$("span[name='spanId']").show();
				}
			}else{
				$("span[name='spanId']").hide();
			}
			tableTime1.style.display="block";
			$('#tableTime1 tr').each(function(){
				$("td[name='content1']").each(function(){
					if($(this).text()==a){
				    	$(this).css("background-color","#ffdcb9");
				    }
				});
				$("td[name='language1']").each(function(){
					if($(this).text()==b){
				    	$(this).css("background-color","#ffdcb9");
				    }
				});
				$("td[name='writing1']").each(function(){
					if($(this).text()==d){
				    	$(this).css("background-color","#ffdcb9");
				    }
				});
			});
		}else if(geadeId=="初中"||geadeId=="初一"||geadeId=="初二"||geadeId=="初三"||geadeId=="七年级"||geadeId=="八年级"||geadeId=="九年级"){
			if(fs!=null||fs!=""){
				if(fs>=46&&fs<=60){
					$("span[name='spanId']").text("一类文");
					$("span[name='spanId']").show();
				}
				if(fs>=40&&fs<=45){
					$("span[name='spanId']").text("二类文");
					$("span[name='spanId']").show();
				}
				if(fs>=35&&fs<=39){
					$("span[name='spanId']").text("三类文");
					$("span[name='spanId']").show();
				}
				if(fs>=30&&fs<=34){
					$("span[name='spanId']").text("四类文");
					$("span[name='spanId']").show();
				}
				if(fs<=29){
					$("span[name='spanId']").text("五类文");
					$("span[name='spanId']").show();
				}
			}else{
				$("span[name='spanId']").hide();
			}
			tableTime2.style.display="block";
			$('#tableTime2 tr').each(function(){
				$("td[name='content2']").each(function(){
					if($(this).text()==a){
				    	$(this).css("background-color","#ffdcb9");
				    }
				});
				$("td[name='language2']").each(function(){
					if($(this).text()==b){
				    	$(this).css("background-color","#ffdcb9");
				    }
				});
				$("td[name='structure2']").each(function(){
					if($(this).text()==c){
				    	$(this).css("background-color","#ffdcb9");
				    }
				});
				$("td[name='writing2']").each(function(){
					if($(this).text()==d){
				    	$(this).css("background-color","#ffdcb9");
				    }
				});
			});
		}else if(geadeId=="高中"||geadeId=="高一"||geadeId=="高二"||geadeId=="高三"){
			if(fs!=null||fs!=""){
				if(fs>=32&&fs<=40){
					$("span[name='spanId']").text("一类文");
					$("span[name='spanId']").show();
				}
				if(fs>=22&&fs<=30){
					$("span[name='spanId']").text("二类文");
					$("span[name='spanId']").show();
				}
				if(fs>=12&&fs<=20){
					$("span[name='spanId']").text("三类文");
					$("span[name='spanId']").show();
				}
				if(fs<=10){
					$("span[name='spanId']").text("四类文");
					$("span[name='spanId']").show();
				}
			}else{
				$("span[name='spanId']").hide();
			}
			tableTime3.style.display="block";
			$('#tableTime3 tr').each(function(){
				$("td[name='content3']").each(function(){
				    if($(this).text()==a){
				    	$(this).css("background-color","#ffdcb9");
				    }
				});
				$("td[name='express3']").each(function(){
					if($(this).text()==b){
				    	$(this).css("background-color","#ffdcb9");
				    }
				});
				$("td[name='feature3']").each(function(){
					if($(this).text()==c){
				    	$(this).css("background-color","#ffdcb9");
				    }
				});
			});
		}
	})
</script>
	<!-- // 查看图片 // -->
	<script>
		function picBig1() {
	    	picClose();
	        document.getElementById("divCenter1").style.display="block";
	    }
	    function picBig2() {
	    	picClose();
	        document.getElementById("divCenter2").style.display="block";
	    }
	    function picBig3() {
	    	picClose();
	        document.getElementById("divCenter3").style.display="block";
	    }
	    /* 点击大图关闭 */
	    function picClose() {
	        document.getElementById("divCenter1").style.display="none";
	        document.getElementById("divCenter2").style.display="none";
	        document.getElementById("divCenter3").style.display="none";
	    }
	</script>
</html>