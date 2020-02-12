<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看已点评作文</title>
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

<c:forEach items="${list}" var="l">
	<div style="display:inline;">
	<span>作文管理>已点评作文>${l.newTitle}</span>
	</div>
	
	<div style="display:inline;float:right;">
	<%-- 
	<a href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=3" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
	 --%>
	<a href="javascript:history.go(-1)" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
	</div>
	<hr>
	
	<c:if test="${not empty l.propo}">
		要求：${l.propo}
	</c:if>
	
	<center>
		<h3>
		<b>${l.newTitle}</b>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<span style="color:red;size:8;">${l.score}</span>
		</h3>
		<p style="color:#b2b2b2">
			作者昵称：${l.author} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			${l.draft}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			${l.geade}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			${l.createTimes}
		</p>
	</center>
	${l.content}
	<br>
	<input type="hidden" value="${l.voice }" id="voiceData">
	<input type="hidden" id="img1" value="${l.img1}">
	<input type="hidden" id="img2" value="${l.img2}">
	<input type="hidden" id="img3" value="${l.img3}">
	<!-- // 查看图片 // -->
	<%-- 
   	<a id="imga" href='javascript:void(0);' onclick='picBig1()'><img id="i1" src="${l.img1}" style="width:100px;height:100px;"></a>
   	<a id="imgb" href='javascript:void(0);' onclick='picBig2()'><img id="i2" src="${l.img2}" style="width:100px;height:100px;"></a>
   	<a id="imgc" href='javascript:void(0);' onclick='picBig3()'><img id="i3" src="${l.img3}" style="width:100px;height:100px;"></a>
	 --%>
	<div style="display:inline">
	<img class="pimg" id="imga" src="${l.img1}" style="width:100px;height:100px;">
	<img class="pimg" id="imgb" src="${l.img2}" style="width:100px;height:100px;">
	<img class="pimg" id="imgc" src="${l.img3}" style="width:100px;height:100px;">
</div>
	<!-- // 查看图片 // left:200px;-->
	<%-- <div id="divCenter1" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; background-color: white; z-index:1002; overflow: auto;">
        <div style="display:inline;">
        	<img src="${l.img1}" style="width:100%;height:100%;"/>
        </div>
    </div>
    <div id="divCenter2" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; background-color: white; z-index:1002; overflow: auto;">
        <div style="display:inline;">
        	<img src="${l.img2}" style="width:100%;height:100%;"/>
        </div>
    </div>
    <div id="divCenter3" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; background-color: white; z-index:1002; overflow: auto;">
        <div style="display:inline;">
        	<img src="${l.img3}" style="width:100%;height:100%;"/>
        </div>
    </div> --%>
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
			<td width="200px">点评老师：${l.name}</td>
			<td width="600px">点评时间：${l.comTimes}</td>
		</tr>
	</table>
	</div>
	<input type="hidden" id="scoreId" value="${l.score}">
	<span id="spanId"></span>
	<input type="hidden" id="voice" value="${l.voice}">
	<input type="hidden" id="geadeId" value="${l.geade}">
	<input type="hidden" name="pcontent" id="pcontent" value="${l.dpcontent}">
	<input type="hidden" name="planguage" id="planguage" value="${l.dplanguage}">
	<input type="hidden" name="pstructure" id="pstructure" value="${l.dpstructure}">
	<input type="hidden" name="pwriting" id="pwriting" value="${l.dpwriting}">
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
			${l.scoring}
			</td>
		</tr>
		<tr>
			<td width="70px" style="vertical-align:top">失分点：</td>
			<td width="*">
			${l.points}
			</td>
		</tr>
		<tr>
			<td width="70px" style="vertical-align:top">建议：</td>
			<td width="*">
			${l.suggest}
			</td>
		</tr>
	</table>
	</div>
	<div id="demo5" style="display:block;margin-left:5px;">
		<p style="font-size:16px;margin-top:10px;">
			${l.scoring}
		</p>
		<p style="font-size:16px;">
			${l.points}
		</p>
		<p style="font-size:16px;">
			${l.suggest}
		</p>
	</div>
</c:forEach>

</div>
</div>
</div>
</body>
<script type="text/javascript">
	RongIMLib.RongIMVoice.init();
	document.getElementById("voiceOpenButton").onclick = function(){
		var data = document.getElementById("voiceData").value;
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
	<!-- // 查看图片 // -->
	<script>
		function picBig1() {
	    	picClose();
	    	document.getElementById('fade').style.display='block';
	        document.getElementById("divCenter1").style.display="block";
	    }
	    function picBig2() {
	    	picClose();
	    	document.getElementById('fade').style.display='block';
	        document.getElementById("divCenter2").style.display="block";
	    }
	    function picBig3() {
	    	picClose();
	    	document.getElementById('fade').style.display='block';
	        document.getElementById("divCenter3").style.display="block";
	    }
	    /* 点击大图关闭 */
	    function picClose() {
	        document.getElementById("divCenter1").style.display="none";
	        document.getElementById("divCenter2").style.display="none";
	        document.getElementById("divCenter3").style.display="none";
	        document.getElementById('fade').style.display='none';
	    }
	</script>
</html>