<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看评价作文</title>
<jsp:include page="../../inc.jsp"></jsp:include>
<script src="<%=request.getContextPath()%>/jslib/voice/voice-2.0.js"></script>
<script src="<%=request.getContextPath()%>/jslib/voice/Gruntfile.js"></script>
<script src="<%=request.getContextPath()%>/jslib/voice/pcmdata.min.js"></script>
<script src="<%=request.getContextPath()%>/jslib/voice/libamr-min.js"></script>
<script src="<%=request.getContextPath()%>/jslib/voice/swfobject.js"></script>
</head>
<body style="font-family:Microsoft YaHei;">
<div id="fade" style="display: none;"> 
	<div style="position: absolute; top: 0%; left: 0%; width: 100%; height: 100%; background-color: black; z-index:1001; -moz-opacity: 0.6; opacity:.60; filter: alpha(opacity=60);"></div>
	<div style="position: absolute; top: 20%; left: 80%; padding: 0px; border: 0px solid orange; z-index:1004; overflow: auto;">
        <div style="display:inline;" onclick="picClose1()">
        	<img title="关闭" src="<%=request.getContextPath()%>/images/guanbi-.png" style="width:100%;height:100%;"/>
        </div>
	</div>
	<div style="position: absolute; top: 28%; left: 80%; padding: 0px; border: 0px solid orange; z-index:1004; overflow: auto;">
        <img id="xuanzhuan" title="旋转" src="<%=request.getContextPath()%>/images/xuanzhuan.png" style="width:100%;height:100%;"/>
	</div>
</div>
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<c:forEach items="${pingjia}" var="t">
	<div style="display:inline;">
	<span>作文管理>已点评作文>${t.newTitle}</span>
	</div>
	
	<div style="display:inline;float:right;">
	<a href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=22" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
	</div>
	<hr>
	
	<c:if test="${not empty t.propo}">
		要求：${t.propo}
	</c:if>
	
	<center>
		<h3>
		<b>${t.newTitle}</b>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<span style="color:red;size:8;">${t.score}</span>
		</h3>
		<p style="color:#b2b2b2">
			作者昵称：${t.author} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			${t.draft}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			${t.geade}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			${t.createTimes}
		</p>
	</center>
	${t.content}
	<br>
	
	<input type="hidden" id="img1" value="${t.img1}">
	<input type="hidden" id="img2" value="${t.img2}">
	<input type="hidden" id="img3" value="${t.img3}">
	<!-- // 查看图片 // -->
	<div style="display:inline">
		<img class="pimg" id="imga" src="${t.img1}" style="width:100px;height:100px;">
		<img class="pimg" id="imgb" src="${t.img2}" style="width:100px;height:100px;">
		<img class="pimg" id="imgc" src="${t.img3}" style="width:100px;height:100px;">
	</div>
	<!-- // 查看图片 // left:200px;-->
	<div id="divCenter" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; z-index:1002; overflow: auto;">
		<div style="display:inline;">
			<img id="img" src="" style="width:100%;height:100%;"/>
		</div>
	</div>
	<!-- // 查看图片 // -->
	<br><br>
	
	
	<img src="<%=request.getContextPath()%>/images/a1.png" alt="名师点评" style="float:right;width:100%;">
	<div style="display:block;margin-left:5px;">
	<table style="border-collapse:separate; border-spacing:0px 10px;">
		<tr>
			<td width="200px">点评老师：${t.name}</td>
			<td width="600px">点评时间：${t.comTimes}</td>
		</tr>
	</table>
	</div>
	<input type="hidden" id="scoreId" value="${t.score}">
	<span id="spanId"></span>
	<input type="hidden" id="voice" value="${t.voice}">
	<input type="hidden" id="geadeId" value="${t.geade}">
	<input type="hidden" name="pcontent" id="pcontent" value="${t.dpcontent}">
	<input type="hidden" name="planguage" id="planguage" value="${t.dplanguage}">
	<input type="hidden" name="pstructure" id="pstructure" value="${t.dpstructure}">
	<input type="hidden" name="pwriting" id="pwriting" value="${t.dpwriting}">
	<div id="demo1">
	<div id="id1">
	<span style="font-size:17px;margin-left:5px;">小学作文评分标准：</span><br>
	<span style="font-size:17px;margin-left:10px;font-size:25px;" name="spanId"></span>
	<table id="tableTime1" border="1" style="border-collapse:separate; margin-left:5px;">
		<tr>
			<td width="140px" style="text-align:center"><img src="<%=request.getContextPath()%>/images/3.png" alt="维度等级" style="float:left;height:40px;"></td>
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
	<span style="font-size:17px;margin-left:5px;">初中作文评分标准：</span><br>
	<span style="font-size:17px;margin-left:10px;font-size:25px;" name="spanId"></span>
	<table id="tableTime2" border="1" style="border-collapse:separate; margin-left:5px;">
		<tr>
			<td width="140px" style="text-align:center"><img src="<%=request.getContextPath()%>/images/3.png" alt="维度等级" style="float:left;height:40px;"></td>
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
	<span style="font-size:17px;margin-left:5px;">高中作文评分标准：</span><br>
	<span style="font-size:17px;margin-left:10px;font-size:25px;" name="spanId"></span>
	<table id="tableTime3" border="1" style="border-collapse:separate; margin-left:5px;">
		<tr>
			<td width="140px" style="text-align:center"><img src="<%=request.getContextPath()%>/images/3.png" alt="维度等级" style="float:left;height:40px;"></td>
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
	</div>
	
	<div style="font-size:14px;line-height:35px;background-color:#ffffff;border:0;margin-top:0px;" id="voiceDiv">
		<%-- <audio src="${t.voice }" controls preload="auto" id="audio"></audio> --%>
		语音点评内容：<br>
		<img src="<%=request.getContextPath()%>/images/voiceOpen.jpg" alt="名师点评" style="float:left;width:40px;" id="voiceOpenButton">
		<img src="<%=request.getContextPath()%>/images/voiceClose.jpg" alt="名师点评" style="float:left;width:40px;" id="voiceCloseButton"><br>
	</div>
	
	<div style="display:block;margin-top:20px;">
	<img src="<%=request.getContextPath()%>/images/a2.png" alt="总评" style="float:left;width:100%;">
	</div>
	<div id="demo4" style="display:block;margin-left:5px;width:100%;">
	<table style="border-collapse:separate; border-spacing:0px 20px;width:100%;">
		<tr>
			<td width="70px" style="vertical-align:top">得分点：</td>
			<td width="*">
			${t.scoring}
			</td>
		</tr>
		<tr>
			<td width="70px" style="vertical-align:top">失分点：</td>
			<td width="*">
			${t.points}
			</td>
		</tr>
		<tr>
			<td width="70px" style="vertical-align:top">建议：</td>
			<td width="*">
			${t.suggest}
			</td>
		</tr>
	</table>
	</div>
	<div id="demo5" style="display:block;margin-left:5px;">
		<p style="font-size:16px;margin-top:10px;">
			${t.scoring}
		</p>
		<p style="font-size:16px;">
			${t.points}
		</p>
		<p style="font-size:16px;">
			${t.suggest}
		</p>
	</div>
	<div style="display:block;margin-left:5px;margin-top:30px;">学生评价：</div>
	<center>
	<div style="display:block;">
		<input type="radio" value="1" ${t.satisfaction==1?"checked":""} name="satis" style="vertical-align:middle; margin-top:-2px; margin-bottom:1px;">非常满意&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="radio" value="2" ${t.satisfaction==2?"checked":""} name="satis" style="vertical-align:middle; margin-top:-2px; margin-bottom:1px;">满意&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="radio" value="3" ${t.satisfaction==3?"checked":""} name="satis" style="vertical-align:middle; margin-top:-2px; margin-bottom:1px;">不满意
	</div>
	<!-- <div style="display:block;width:100px;height:5px;"></div> -->
	</center>
	<div style="display:block;margin-left:37%;">
		<h3 style="color:#ff6c00;"><b>${t.average}分</b></h3>
	</div>
	<div style="display:block;margin-left:43%;margin-top:-45px;">
		<input type="hidden" id="attitId" value="${t.attit}">
		<input type="hidden" id="profesId" value="${t.profes}">
		负责态度：<span id="attit"></span><span style="margin-left:10px;">${t.attit}分</span><br>
		专业水平：<span id="profes"></span><span style="margin-left:10px;">${t.profes}分</span>
	</div>
	<%-- 
	<div style="display:block;margin-left:5px;width:100%;">
		<span style="float:left;">学生留言：</span>
		<span style="float:right;">${t.apprtime}</span>
	</div><br>
	<div style="display:block;margin-left:35px;width:100%;">${t.stumessage}</div>
	<c:if test="${not empty t.teachermessage}">
	<div style="display:block;margin-left:5px;margin-top:10px;width:100%;">
		<span style="float:left;">老师回复：</span>
		<span style="float:right;">${t.messagetime}</span>
	</div><br>
	<div style="display:block;margin-left:35px;width:100%;">${t.teachermessage}</div>
	 --%>
	<c:forEach items="${message}" var="mes" varStatus="st">   
	    <c:forEach items="${mes}" var="m" varStatus="index"> 
		    <c:if test="${m.userType=='stu'}">
				<div style="display:block;margin-left:5px;width:100%;">
					<span style="float:left;">学生留言：${m.message}</span>
				</div><br>
				<div style="display:block;color:#999999;width:100%;float:right;">
					<a href="javascript:void(0);" onclick="delectMessage('${m.aid}' , '${t.id}' , '${m.userType}' , '${st.index}' + '-' +'${index.index}' + '-'  + '${fn:length(message)}')" style="float:right;color:red;margin-left:15px;">删除</a>
					<span style="float:right;">${m.time}</span>
				</div>
				
			</c:if>
			<c:if test="${m.userType=='tea'}">
				<div style="display:block;margin-left:5px;margin-top:10px;width:100%;">
					<span style="float:left;">老师回复：${m.message}</span>
				</div><br>
				<div style="display:block;color:#999999;width:100%;float:right;">
					<a href="javascript:void(0);" onclick="delectMessage('${m.aid}' , '${t.id}' , '${m.userType}' , '${st.index}' + '-' +'${index.index}' + '-'  + '${fn:length(message)}')" style="float:right;color:red;margin-left:15px;">删除</a>
					<span style="float:right;">${m.time}</span>
				</div>
				
			</c:if>
		</c:forEach> 
	</c:forEach> 
</c:forEach>
	
</div>
</div>
</div>
</body>
<script type="text/javascript">
	RongIMLib.RongIMVoice.init();
	document.getElementById("voiceOpenButton").onclick = function(){
		alert("qqq");
		var data = document.getElementById("voice").value;
		var dataLength = data.length / 1024;
	    RongIMLib.RongIMVoice.play(data, dataLength);
	    document.getElementById("voiceOpenButton").style.display="none";
	    document.getElementById("voiceCloseButton").style.display="block";
	};
	document.getElementById("voiceCloseButton").onclick = function(){
		document.getElementById("voiceOpenButton").style.display="block";
		document.getElementById("voiceCloseButton").style.display="none";
	    RongIMLib.RongIMVoice.stop();
	};
</script>
<script>
//删除评论
function delectMessage(aid,cid,userType,index){
	var type = "";
	if(userType=="stu"){
		type = "确定删除学生的这条评论及相关回复吗？";
	}else if(userType=="tea"){
		type = "确定删除老师的这条回复及相关回复吗？";
	}
	var d = dialog({
		title: '提示',
		width: '300',
		//height: '35',
		content: '<center><h4><b>'+type+'</b></h4></center>',
		okValue: '确 定',
		ok: function () {
			$.ajax({  
				type: "POST",  
				url: "<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=33&id="+aid+"&cid="+cid+"&userType="+userType+"&index="+index,  
				dataType: "text",  
				success: function(data){
					var d = dialog({
						title: '提示',
						width: '300',
						//height: '35',
						content: '<center><h4><b>删除成功！</b></h4></center>',
						okValue: '确 定',
						ok: function () {
							if (data == 'no') {
								location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=22";
							}else {
								location.href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=23&id="+cid;
							}
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
	//判断图片是否为空
	$(function (){
		document.getElementById("voiceOpenButton").style.display="block"
		document.getElementById("voiceCloseButton").style.display="none";
		var a = $("#img1").val();
		var b = $("#img2").val();
		var c = $("#img3").val();
		if(a.length==0){
			$("#imga").hide();//表示display:block, 
			//$("#divCenter1").hide();//表示display:none;
		}
		if(b.length==0){
			$("#imgb").hide();
			//$("#divCenter2").hide();
		}
		if(c.length==0){
			$("#imgc").hide();
			//$("#divCenter3").hide();
		}
	})
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
				if(fs>=46&&fs<=50){
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
				if(fs>=52&&fs<=60){
					$("span[name='spanId']").text("一类文");
					$("span[name='spanId']").show();
				}
				if(fs>=45&&fs<=51){
					$("span[name='spanId']").text("二类文");
					$("span[name='spanId']").show();
				}
				if(fs>=36&&fs<=44){
					$("span[name='spanId']").text("三类文");
					$("span[name='spanId']").show();
				}
				if(fs>=30&&fs<=35){
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
	/*判断语音点评是否有内容*/
	$(function (){
		var a = $("#voice").val();
		if(a.length==0){
			$("#voiceDiv").hide();
			$("#demo5").hide();
		}else{
			$("#demo1").hide();
			$("#demo4").hide();
			
		}
	})
	
	$(function (){
		var a = $("#attitId").val();
		var b = $("#profesId").val();
		var c = "";
		for(var i=0;i<a;i++){
			c += "<img src='<%=request.getContextPath()%>/images/stars_a.png' style='margin-left:10px;'>"
		}
		for(var i=0;i<(5-a);i++){
			c += "<img src='<%=request.getContextPath()%>/images/stars_b.png' style='margin-left:10px;'>"
		}
		$("#attit").append(c);
		var d = "";
		for(var i=0;i<b;i++){
			d += "<img src='<%=request.getContextPath()%>/images/stars_a.png' style='margin-left:10px;'>"
		}
		for(var i=0;i<(5-b);i++){
			d += "<img src='<%=request.getContextPath()%>/images/stars_b.png' style='margin-left:10px;'>"
		}
		$("#profes").append(d);
	})
	
</script>
<!-- // 查看图片 // -->
<script type="text/javascript">
var num = 0;
$("#xuanzhuan").click(function(){
	num+=90;
	$("#divCenter").css('transform','rotate('+num+'deg)');
	
})
$(function(){  
    $(".pimg").click(function(){  
        var _this = $(this);//将当前的pimg元素作为_this传入函数  
        imgShow( _this);  
    });  
});
//判断作文内容、图片是否为空
$(function (){
	var a = $("#img1").val();
	var b = $("#img2").val();
	var c = $("#img3").val();
	if(a.length==0){
		$("#imga").hide();
	}
	if(b.length==0){
		$("#imgb").hide();
	}
	if(c.length==0){
		$("#imgc").hide();
	}
})

function imgShow( _this){  
    var src = _this.attr("src");//获取当前点击的pimg元素中的src属性 
    $("#img").attr("src", src);//设置#bigimg元素的src属性  
    document.getElementById('fade').style.display='block';
    document.getElementById("divCenter").style.display="block";
}
function picClose1() {
    document.getElementById("divCenter").style.display="none";
    document.getElementById('fade').style.display='none';
}
</script>
<!-- // 查看图片 // -->
</html>