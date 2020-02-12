<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page errorPage="/WEB-INF/view/404.jsp"%>
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
<div class="viewFramework-content">
<c:forEach items="${ydp}" var="y">
	<input type="hidden" value="${y.voice }" id="voiceData">
	<%-- <input type="hidden" value="${y.voice.length / 1024 }" id="voiceDateLength"> --%>
	<div style="display:inline;">
	<span style="color:#b0b0b0;">已点评<img src="<%=request.getContextPath()%>/images/qianjin.png" style="padding-right:5px;padding-left:5px;"/>${y.title}</span>
	</div>
	<div style="display:inline;float:right;">
	<a href="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=2" target="mainFrame4" style="color:#b0b0b0;text-decoration:none;padding-right:20px;">
	<img src="<%=request.getContextPath()%>/images/fanhui.png" style="padding-right:5px;"/>返回</a>
	</div>
	<hr>
	
	<div style="padding-left:150px;padding-right:150px;">
	<div style="display:inline;">
		<center>
			<h3><b>${y.title}</b></h3>
			<pre style="color:#949494;background-color:#ffffff;border:0;">作者：${y.author}      ${y.draft}     ${y.geade}</pre>
		</center>
	</div>
	<div style="display:inline;margin-right:100px;margin-top:-90px;float:right;">
		<font style="color:red;font-size:35px;"><b>${y.score}</b></font>
		<div style="margin-top:-7px;">
			<img src="<%=request.getContextPath()%>/images/xiaxian.png" width="70px"/>
		</div>
	</div>
	<div style="font-size:16px;line-height:35px;">
	${y.content}
	 </div>
	<br>
	
	<!-- // 查看图片 // -->
	<input type="hidden" id="img1Id" value="${y.img1}">
	<input type="hidden" id="img2Id" value="${y.img2}">
	<input type="hidden" id="img3Id" value="${y.img3}">
	<input type="hidden" id="voice" value="${y.voice}">
	<%-- <div id="imgDiv1" style="display:inline;margin-right:20px;">
    	<a name='fff' href='javascript:void(0);' onclick='picBig1()'><img src="${y.img1}" style="width:100px;height:100px;"></a>
	</div>
	<div id="imgDiv2" style="display:inline;margin-right:20px;">
    	<a name='fff' href='javascript:void(0);' onclick='picBig2()'><img src="${y.img2}" style="width:100px;height:100px;"></a>
    </div>
    <div id="imgDiv3" style="display:inline;">
    	<a name='fff' href='javascript:void(0);' onclick='picBig3()'><img src="${y.img3}" style="width:100px;height:100px;"></a>
	</div> --%>
	<div style="display:inline">
		<img class="pimg" id="imgDiv1" src="${y.img1}" style="width:100px;height:100px;">
		<img class="pimg" id="imgDiv2" src="${y.img2}" style="width:100px;height:100px;">
		<img class="pimg" id="imgDiv3" src="${y.img3}" style="width:100px;height:100px;">
	</div>
	<!-- // 查看图片 // -->
	<%-- <div id="divCenter1" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; background-color: white; z-index:1002; overflow: auto;">
        <div style="display:inline;">
        	<img src="${y.img1}" style="width:100%;height:100%;"/>
        </div>
    </div>
    <div id="divCenter2" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; background-color: white; z-index:1002; overflow: auto;">
        <div style="display:inline;">
        	<img src="${y.img2}" style="width:100%;height:100%;"/>
        </div>
    </div>
    <div id="divCenter3" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; background-color: white; z-index:1002; overflow: auto;">
        <div style="display:inline;">
        	<img src="${y.img3}" style="width:100%;height:100%;"/>
        </div>
    </div> --%>
    <div id="divCenter" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; z-index:1002; overflow: auto;">
        <div style="display:inline;">
        	<img id="img" src="" style="width:100%;height:100%;"/>
        </div>
    </div>
	<div style="width:100%;"></div>
	<br><br>
	
	
	<div id="demo1">
	<div style="display:block;margin-top:10px;width:100%;font-size:14px;line-height:35px;background-color:#ffffff;border:0;">
		<!-- <input type="button" style="background:#ff8000;color:#ffffff;width:5px;height:30px;" >
		<span style="font-size:17px;">&nbsp;&nbsp;名师点评</span> -->
		<img src="<%=request.getContextPath()%>/images/1.png" alt="名师点评" style="float:left;width:100%;"><br>
	</div>
	
	<input type="hidden" id="geadeId" value="${y.geade}">
	<input type="hidden" id="scoreId" value="${y.score}">
	<span id="spanId" style="font-size:25px;margin-left:20px;margin-top:20px;"></span>
	<!-- 小学评分标准 -->
	<div id="pfbz1">
	<div style="margin-left:20px;margin-top:10px;">
		<p style="font-size:16px;margin-top:10px;">
	  	内容  &nbsp;&nbsp;&nbsp;&nbsp; 
	  	<input type="button" value="${y.categoryCa}" class="btn btn-default" style="border:1px solid #ff6c00;color:#ff6c00;width:70px;height:30px;">
	  	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dpcontent}<br><br>
	 	 语言 &nbsp;&nbsp;&nbsp;&nbsp; 
	 	<input type="button" value="${y.languageCa}" class="btn btn-default" style="border:1px solid #ff6c00;color:#ff6c00;width:70px;height:30px;">
	 	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dplanguage}<br><br>
	 	 书写  &nbsp;&nbsp;&nbsp;&nbsp;
	 	<input type="button" value="${y.writingCa}" class="btn btn-default" style="border:1px solid #ff6c00;color:#ff6c00;width:70px;height:30px;">
	 	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dpwriting}<br><br>
		</p>
	</div>
	</div>
	<!-- 初中评分标准 -->
	<div id="pfbz2">
	<div style="margin-left:20px;margin-top:10px;">
		<p style="font-size:16px;margin-top:10px;">
	  	内容  &nbsp;&nbsp;&nbsp;&nbsp; 
	  	<input type="button" value="${y.categoryCa}" class="btn btn-default" style="border:1px solid #ff6c00;color:#ff6c00;width:70px;height:30px;">
	  	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dpcontent}<br><br>
	 	 语言 &nbsp;&nbsp;&nbsp;&nbsp; 
	 	<input type="button" value="${y.languageCa}" class="btn btn-default" style="border:1px solid #ff6c00;color:#ff6c00;width:70px;height:30px;">
	 	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dplanguage}<br><br>
	  	结构  &nbsp;&nbsp;&nbsp;&nbsp;
	  	<input type="button" value="${y.structureCa}" class="btn btn-default" style="border:1px solid #ff6c00;color:#ff6c00;width:70px;height:30px;">
	  	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dpstructure}<br><br>
	 	 书写  &nbsp;&nbsp;&nbsp;&nbsp;
	 	<input type="button" value="${y.writingCa}" class="btn btn-default" style="border:1px solid #ff6c00;color:#ff6c00;width:70px;height:30px;">
	 	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dpwriting}<br><br>
		</p>
	</div>
	</div>
	<!-- 高中评分标准 -->
	<div id="pfbz3">
	<div style="margin-left:20px;margin-top:10px;">
		<p style="font-size:16px;margin-top:10px;">
	  	内容  &nbsp;&nbsp;&nbsp;&nbsp; 
	  	<input type="button" value="${y.categoryCa}" class="btn btn-default" style="border:1px solid #ff6c00;color:#ff6c00;width:70px;height:30px;">
	  	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dpcontent}<br><br>
	 	 表达 &nbsp;&nbsp;&nbsp;&nbsp; 
	 	<input type="button" value="${y.languageCa}" class="btn btn-default" style="border:1px solid #ff6c00;color:#ff6c00;width:70px;height:30px;">
	 	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dplanguage}<br><br>
	  	特征  &nbsp;&nbsp;&nbsp;&nbsp;
	  	<input type="button" value="${y.structureCa}" class="btn btn-default" style="border:1px solid #ff6c00;color:#ff6c00;width:70px;height:30px;">
	  	&nbsp;&nbsp;&nbsp;&nbsp; ${y.dpstructure}<br><br>
		</p>
	</div>
	</div>
	</div>
	<div style="font-size:14px;line-height:35px;background-color:#ffffff;border:0;margin-top:10px;" id="voiceDiv">
		<!-- <input type="button" style="background:#ff8000;color:#ffffff;width:5px;height:30px;" >
		<span style="font-size:17px;">&nbsp;&nbsp;语音点评</span> -->
		<img src="<%=request.getContextPath()%>/images/1.png" alt="名师点评" style="float:left;width:100%;"><br>
		<div style="margin-left:20px;margin-top:10px;">
		<%-- <audio src="${y.voice }" controls preload="auto" id="audio"></audio> --%>
		语音点评内容：<br>
		<img src="<%=request.getContextPath()%>/images/voiceOpen.jpg" alt="名师点评" style="float:left;width:40px;" id="voiceOpenButton">
		<img src="<%=request.getContextPath()%>/images/voiceClose.jpg" alt="名师点评" style="float:left;width:40px;" id="voiceCloseButton"><br>
		</div>
	</div>
	
	<div style="font-size:14px;line-height:35px;background-color:#ffffff;border:0;margin-top:30px;">
		<!-- <input type="button" style="background:#ff8000;color:#ffffff;width:5px;height:30px;" >
		<span style="font-size:17px;">&nbsp;&nbsp;总评</span> -->
		<img src="<%=request.getContextPath()%>/images/2.png" alt="总评" style="float:left;width:100%;"><br>
	</div>
	<div style="margin-left:20px;margin-top:10px;">
		<input type="button" id="demo2" value="得分点" class="btn" style="font-size:15px;vertical-align:middle;background:#ff6c00;color:#ffffff;width:70px;height:30px;">
		<p style="font-size:16px;margin-top:10px;">
			${y.scoring}
		</p>
		<input type="button" id="demo3" value="失分点" class="btn" style="font-size:15px;vertical-align:middle;background:#ff6c00;color:#ffffff;width:70px;height:30px;">
		<p style="font-size:16px;">
			${y.points}
		</p>
		<input type="button" id="demo4" value="建议" class="btn" style="font-size:15px;vertical-align:middle;background:#ff6c00;color:#ffffff;width:70px;height:30px;">
		<p style="font-size:16px;">
			${y.suggest}
		</p>
	</div>
	<br><br>
</div>
</c:forEach>
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
<script type="text/javascript">
//评分标准
$(function (){

	document.getElementById("voiceOpenButton").style.display="block"
	document.getElementById("voiceCloseButton").style.display="none";
	
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
			if(fs>=16&&fs<=20){
				$("#spanId").text("一类文");
				$("#spanId").show();
			}
			if(fs>=15&&fs<=11){
				$("#spanId").text("二类文");
				$("#spanId").show();
			}
			if(fs>=10&&fs<=6){
				$("#spanId").text("三类文");
				$("#spanId").show();
			}
			if(fs<=5){
				$("#spanId").text("四类文");
				$("#spanId").show();
			}
		}
	}
})

//图片是否存在
$(function (){
	var a = $("#img1Id").val();
	var b = $("#img2Id").val();
	var c = $("#img3Id").val();
	if(a.length==0){
		$("#imgDiv1").hide();
		//$("#divCenter1").hide();
	}
	if(b.length==0){
		$("#imgDiv2").hide();
		//$("#divCenter2").hide();
	}
	if(c.length==0){
		$("#imgDiv3").hide();
		//$("#divCenter3").hide();
	}
})
/*判断语音点评是否有内容*/
$(function (){
	var a = $("#voice").val();
	if(a.length==0){
		$("#voiceDiv").hide();
		//$("#divCenter1").hide();
	}else{
		$("#demo1").hide();
		$("#demo2").hide();
		$("#demo3").hide();
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
	var e = $("#content").text().replace(/\s+/g,"");
	if(e.length==0){
		//$("#editor").show();//表示display:block, 
		$("#editor").hide();//表示display:none;
	}
	if(a.length==0){
		$("#imga").hide();
		//$("#divCenter1").hide();
	}
	if(b.length==0){
		$("#imgb").hide();
		//$("#divCenter2").hide();
	}
	if(c.length==0){
		$("#imgc").hide();
		//$("#divCenter3").hide();
	}
	if(a.length==0 && b.length==0 && c.length==0){
		$("#editor").show();
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
    /* 上一个 */
    function shang1(){
    	picClose();
    	picBig3();
    }
    function shang2(){
    	picClose();
    	picBig1();
    }
    function shang3(){
    	picClose();
    	picBig2();
    }
    /* 下一个 */
    function xia1(){
    	picClose();
    	picBig2();
    }
    function xia2(){
    	picClose();
    	picBig3();
    }
    function xia3(){
    	picClose();
    	picBig1();
    }
</script>
<!-- // 查看图片 // -->
</html>