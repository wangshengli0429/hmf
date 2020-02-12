<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page errorPage="/WEB-INF/view/404.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>未点评-批改</title>
<jsp:include page="../../inc2.jsp"></jsp:include>
</head>
<style>
body{
  font-family:"Microsoft YaHei";
  }
</style>
<style>
    input::-webkit-outer-spin-button,
    input::-webkit-inner-spin-button {
        -webkit-appearance: none;
    }
    input[type="number"]{
        -moz-appearance: textfield;
    }
</style>
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

<form id="updateForm" method="post">
<c:forEach items="${wdp}" var="w">
	<div style="display:inline;">
	<input type="hidden" name="id" value="${w.id}">
	<input type="hidden" name="title" value="${w.title}">
	<input type="hidden" name="author" value="${w.author}">
	<input type="hidden" name="draft" value="${w.draft}">
	<input type="hidden" name="geade" value="${w.geade}">
	<input type="hidden" name="udid" value="${w.udid}">
	<input type="hidden" name="pcontent" id="pcontent" value="${w.dpcontent}">
	<input type="hidden" name="planguage" id="planguage" value="${w.dplanguage}">
	<input type="hidden" name="pstructure" id="pstructure" value="${w.dpstructure}">
	<input type="hidden" name="pwriting" id="pwriting" value="${w.dpwriting}">
	<input type="hidden" id="img1" value="${w.img1}">
	<input type="hidden" id="img2" value="${w.img2}">
	<input type="hidden" id="img3" value="${w.img3}">
	<div id="content" style="display:inline;display:none;">
	${w.content}
	</div>
	<span style="color:#b0b0b0;">未点评<img src="<%=request.getContextPath()%>/images/qianjin.png" style="padding-right:5px;padding-left:5px;"/>${w.title}</span>
	</div>
	<div style="display:inline;float:right;">
	<a href="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=1" target="mainFrame4" style="color:#b0b0b0;text-decoration:none;padding-right:20px;">
	<img src="<%=request.getContextPath()%>/images/fanhui.png" style="padding-right:5px;"/>返回</a>
	</div>
	<hr>
	
	<div style="padding-left:150px;padding-right:150px;">
	<c:if test="${w.toteacher==1}">
		<p id="assignid" style="color:#ff6c00;"><b>注：这篇作文是您指定的作文，请尽快为学生点评哦~。</b></p>
	</c:if>
	<center>
		<h3><b>${w.title}</b></h3>
		<pre style="color:#949494;background-color:#ffffff;border:0;">作者：${w.author}      ${w.draft}     ${w.geade1}</pre>
	</center>
	<textarea id="editor" name="content" type="text/plain" onblur="getContentTxt()" required>${w.content}</textarea >
	<br>
	
	<!-- // 查看图片 // -->
    <%-- <div id="imga" style="display:inline">
    <a href='javascript:void(0);' onclick='picBig1()'><img id="i1" src="${w.img1}" style="width:100px;height:100px;"></a>
	 </div>
    <div id="imgb" style="display:inline">
    <a href='javascript:void(0);' onclick='picBig2()'><img id="i2" src="${w.img2}" style="width:100px;height:100px;"></a>
	</div>
    <div id="imgc" style="display:inline">
    <a href='javascript:void(0);' onclick='picBig3()'><img id="i3" src="${w.img3}" style="width:100px;height:100px;"></a>
	</div> --%>
	<div style="display:inline">
		<img class="pimg" id="imga" src="${w.img1}" style="width:100px;height:100px;">
		<img class="pimg" id="imgb" src="${w.img2}" style="width:100px;height:100px;">
		<img class="pimg" id="imgc" src="${w.img3}" style="width:100px;height:100px;">
		
	</div>
	<!-- // 查看图片 // -->
	<%-- <div id="divCenter1" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; z-index:1002; overflow: auto;">
        <div style="display:inline;;">
        	<img src="${w.img1}" style="width:100%;height:100%;"/>
        </div>
    </div>
    <div id="divCenter2" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; z-index:1002; overflow: auto;">
        <div style="display:inline;">
        	<img src="${w.img2}" style="width:100%;height:100%;"/>
        </div>
    </div>
    <div id="divCenter3" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; z-index:1002; overflow: auto;">
        <div style="display:inline;">
        	<img src="${w.img3}" style="width:100%;height:100%;"/>
        </div>
    </div> --%>
    
    <div id="divCenter" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; z-index:1002; overflow: auto;">
        <div style="display:inline;">
        	<img id="img" src="" style="width:100%;height:100%;"/>
        </div>
    </div>
    
	<br><br>
	<div style="display:block;margin-top:10px;width:100%;">
	<img src="<%=request.getContextPath()%>/images/1.png" alt="名师点评" style="float:left;width:100%;"><br>
	</div>
	<div style="display:block;margin-top:25px;">
		<span style="font-size:17px;margin-left:0px;margin-top:20px;">打分：</span>
		<input type="number" name="score" id="score" value="${w.score}" class="form-control" style="width:178px;height:35px;display:inline;" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" placeholder="请输入作文分值">
		<span id="spanId" style="display:inline;"></span>
	</div>
	
	<input type="hidden" name="grade" id="geadeId" value="${grade}">
	<div id="id1" style="margin-top:10px;">
	<span style="font-size:17px;margin-left:0px;">小学作文评分标准：</span>
	<table id="tableTime1" border="1" style="border-collapse:separate; margin-left:0px;">
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
	<div id="id2" style="margin-top:10px;">
	<span style="font-size:17px;margin-left:0px;">初中作文评分标准：</span>
	<table id="tableTime2" border="1" style="border-collapse:separate; margin-left:0px;">
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
	<div id="id3" style="margin-top:10px;">
	<span style="font-size:17px;margin-left:0px;">高中作文评分标准：</span>
	<table id="tableTime3" border="1" style="border-collapse:separate; margin-left:0px;">
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
	
	<div style="display:block;;margin-left:0px;">
	<span style="color:#949494;">注：请按照不同的维度选择不同的作文等级，不可漏选。</span>
	</div>
	
	<div style="display:block;margin-top:20px;margin-bottom:0px;">
	<img src="<%=request.getContextPath()%>/images/2.png" alt="总评" style="float:left;width:100%;"><br>
	</div>
	<!-- <div style="margin-left:100px;border-collapse:separate; border-spacing:0px 20px; background-color:#c9c9c9;">
		<div style="margin-left:10px;margin-right:10px;height:9px;background-color:#c9c9c9"></div>
	</div> -->
	<div style="display:block;margin-top:25px;margin-left:0px;border-collapse:separate; border-spacing:0px 20px; background-color:#c9c9c9;">
		<div style="margin-left:10px;margin-right:10px;height:5px;background-color:#c9c9c9"></div>
		<div style="margin-left:10px;margin-right:10px;margin-top:5px;background-color:#ffffff">
			<span style="font-size:17px;">&nbsp;&nbsp;得分点：</span>
		</div>
		<div style="margin-left:10px;margin-right:10px;margin-top:0px;background-color:#ffffff;height:200px;">
			 <textarea name="scoring" id="editor1" onchange="validateString1(this.value)" onblur="validateString1(this.value)" maxlength="2000" style="width:100%;height:100%;background-color: #ffffff; border: 1px #D7D7D7 solid;font-size: 16px;">${w.scoring}</textarea>
			 <!-- <textarea id="editor1" name="scoring" type="text/plain" required></textarea> -->
		</div>
		<div style="margin-left:10px;margin-right:10px;margin-top:0px;background-color:#ffffff;">
		(<span id="lengthover1" style="color:red;">2000</span>/2000字)
		</div>
		
		<div style="margin-left:10px;margin-right:10px;margin-top:10px;background-color:#ffffff">
			<span style="font-size:17px;">&nbsp;&nbsp;失分点：</span>
		</div>
		<div style="margin-left:10px;margin-right:10px;margin-top:0px;background-color:#ffffff;height:200px;">
			 <textarea name="points" id="editor2" onchange="validateString2(this.value)" onblur="validateString2(this.value)" maxlength="2000" style="width:100%;height:100%;background-color: #ffffff; border: 1px #D7D7D7 solid;font-size: 16px;">${w.points}</textarea>
			 <!-- <textarea id="editor2" name="points" type="text/plain" required></textarea> -->
		</div>
		<div style="margin-left:10px;margin-right:10px;margin-top:0px;background-color:#ffffff;">
		(<span id="lengthover2" style="color:red;">2000</span>/2000字)
		</div>
		
		<div style="margin-left:10px;margin-right:10px;margin-top:10px;background-color:#ffffff">
			<span style="font-size:17px;">&nbsp;&nbsp;建议：</span>
		</div>
		<div style="margin-left:10px;margin-right:10px;margin-top:0px;background-color:#ffffff;height:200px;">
			 <textarea name="suggest" id="editor3" onchange="validateString3(this.value)" onblur="validateString3(this.value)" maxlength="2000" style="width:100%;height:100%;background-color: #ffffff; border: 1px #D7D7D7 solid;font-size: 16px;">${w.suggest}</textarea>
			 <!-- <textarea id="editor3" name="suggest" type="text/plain" required></textarea> -->
		</div>
		<div style="margin-left:10px;margin-right:10px;margin-top:0px;background-color:#ffffff;">
		(<span id="lengthover3" style="color:red;">2000</span>/2000字)
		</div>
		
		<div style="margin-left:10px;margin-right:10px;height:10px;background-color:#c9c9c9"></div>
	</div>
	
	<div style="margin-right:10px;margin-top:10px;float:right;">
		<!-- 
		<input type="button" value="取消" onclick="cancel()" style="background:#ff8000;color:#ffffff;width:90px;height:35px; " >
		 -->
		<c:if test="${w.toteacher==0}">
			<input type="button" value="暂存" onclick="temporary()" style="background:#ff6c00;color:#ffffff;width:90px;height:35px; " >
		</c:if>
		<input type="button" value="提交点评" onclick="tiJiao()" class="btn btn-info" style="background:#54a7fd;color:#ffffff;width:90px;height:35px; " >
	</div>
</div>
</c:forEach>
</form>

</div>
</body>
<!-- 查看图片 -->
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
<script>
	$(function(){
		var a = $("#score").val();
		if(a.length>0){
			$("#score").blur();
		}
		$("#editor1").blur();
		$("#editor2").blur();
		$("#editor3").blur();
	} )
	function validateString1(value){
	    if (value.length > 2000) {
	        value = value.substr(0, 2000);
	        $("#editor1").val(value);
	    }
	    $("#lengthover1").text(value.length );
	}
	function validateString2(value){
	    if (value.length > 2000) {
	        value = value.substr(0, 2000);
	        $("#editor2").val(value);
	    }
	    $("#lengthover2").text(value.length );
	}
	function validateString3(value){
	    if (value.length > 2000) {
	        value = value.substr(0, 2000);
	        $("#editor3").val(value);
	    }
	    $("#lengthover3").text(value.length );
	}
	/* 评分标准 */
	$(function (){
		var tableTime1= document.getElementById("id1"); 
		var tableTime2= document.getElementById("id2"); 
		var tableTime3= document.getElementById("id3"); 
		tableTime1.style.display ="none";
		tableTime2.style.display ="none";
		tableTime3.style.display ="none";
		var geadeId = $("#geadeId").val();
		if(geadeId=="低年级"||geadeId=="小学"||geadeId=="一年级"||geadeId=="二年级"||geadeId=="三年级"||geadeId=="四年级"||geadeId=="五年级"||geadeId=="六年级"){
			tableTime1.style.display="block";
			$('#tableTime1 tr').each(function(){
				$("td[name='content1']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
				    if($(this).text()==$("#pcontent").val()){
				    	$(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $('#A1 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    }
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#pcontent").val($(this).text());
				        $('#A1 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    });
				});
				$("td[name='language1']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
					if($(this).text()==$("#planguage").val()){
						$(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $('#B1 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    }
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#planguage").val($(this).text());
				        $('#B1 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    });
				});
				$("td[name='writing1']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
					if($(this).text()==$("#pwriting").val()){
						$(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $('#C1 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    }
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#pwriting").val($(this).text());
				        $('#C1 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    });
				});
			});
		}else if(geadeId=="初中"||geadeId=="初一"||geadeId=="初二"||geadeId=="初三"||geadeId=="七年级"||geadeId=="八年级"||geadeId=="九年级"){
			tableTime2.style.display="block";
			$('#tableTime2 tr').each(function(){
				$("td[name='content2']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
				    if($(this).text()==$("#pcontent").val()){
				    	$(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $('#A2 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    }
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#pcontent").val($(this).text());
				        $('#A2 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    });
				});
				$("td[name='language2']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
				    if($(this).text()==$("#planguage").val()){
				    	$(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $('#B2 td').not(this).css("background-color","#ffffff");	//还原其他的全部设置为白色
				    }
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#planguage").val($(this).text());
				        $('#B2 td').not(this).css("background-color","#ffffff");	//还原其他的全部设置为白色
				    });
				});
				$("td[name='structure2']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
				    if($(this).text()==$("#pstructure").val()){
				    	$(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $('#C2 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    }
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#pstructure").val($(this).text());
				        $('#C2 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    });
				});
				$("td[name='writing2']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
					if($(this).text()==$("#pwriting").val()){
						$(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $('#D2 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    }
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#pwriting").val($(this).text());
				        $('#D2 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    });
				});
			});
		}else if(geadeId=="高一"||geadeId=="高二"||geadeId=="高三"){
			tableTime3.style.display="block";
			$('#tableTime3 tr').each(function(){
				$("td[name='content3']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
					if($(this).text()==$("#pcontent").val()){
						$(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $('#A3 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    }
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#pcontent").val($(this).text());
				        $('#A3 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    });
				});
				$("td[name='express3']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
					if($(this).text()==$("#planguage").val()){
						$(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $('#B3 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    }
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#planguage").val($(this).text());
				        $('#B3 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    });
				});
				$("td[name='feature3']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
					if($(this).text()==$("#pstructure").val()){
						$(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $('#C3 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    }
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#pstructure").val($(this).text());
				        $('#C3 td').not(this).css("background-color","#ffffff");	//其他的全部设置为白色
				    });
				});
			});
		}
	})
	
	/* 分值 */
	$("#score").blur(function (){
		var fs = $("#score").val();
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
				if(fs>30){
					$("#spanId").text("打分不得超过最高分值（30分）");
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
				if(fs>50){
					$("#spanId").text("打分不得超过最高分值（50分）");
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
				if(fs>60){
					$("#spanId").text("打分不得超过最高分值（60分）");
					$("#spanId").show();
				}
			}
		}
		if(fs==null||fs==""){
			$("#spanId").hide();
		}
	})
	
</script>

<!-- ueditor富文本编辑器 -->
<script>
	//实例化编辑器
	//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
	UE.getEditor('editor', {
		//autoHeightEnabled: false,//开启滚动条
	    autoClearinitialContent:false, //focus时自动清空初始化时的内容
	    wordCount:true,    //开启字数统计   false关闭
	    //maximumWords:500,   //允许的最大字符数
	    elementPathEnabled:false,	//关闭elementPath
	    enterTag:'br',
	    //initialFrameWidth : 1010,
	    //initialFrameHeight: 500,
	    /* toolbars: [
		['indent','bold','italic','underline','strikethrough','fontfamily','fontsize','forecolor', 'backcolor',
		 'justifyleft','justifyright','justifycenter','justifyjustify','fullscreen']
		] */
	});
/* 
'indent', //首行缩进
'bold', //加粗
'italic', //斜体
'underline', //下划线
'strikethrough', //删除线
'fontfamily', //字体
'fontsize', //字号
'forecolor', //字体颜色
'backcolor', //背景色
'justifyleft', //居左对齐
'justifyright', //居右对齐
'justifycenter', //居中对齐
'justifyjustify', //两端对齐
'fullscreen', //全屏
 */
	/* 
	UE.getEditor('editor1', {
		toolbars:false,		//关闭工具栏
	    autoClearinitialContent:false, //focus时自动清空初始化时的内容
	    wordCount:true,    //开启字数统计   false关闭
	    maximumWords:500,   //允许的最大字符数
	    elementPathEnabled:false,	//关闭elementPath
	    //initialFrameWidth : 1010,
	    //initialFrameHeight: 60,
	});
	UE.getEditor('editor2', {
		toolbars:false,		//关闭工具栏
	    autoClearinitialContent:false, //focus时自动清空初始化时的内容
	    wordCount:true,    //开启字数统计   false关闭
	    maximumWords:500,   //允许的最大字符数
	    elementPathEnabled:false,	//关闭elementPath
	    //initialFrameWidth : 1010,
	    //initialFrameHeight: 60,
	});
	UE.getEditor('editor3', {
		toolbars:false,		//关闭工具栏
	    autoClearinitialContent:false, //focus时自动清空初始化时的内容
	    wordCount:true,    //开启字数统计   false关闭
	    maximumWords:500,   //允许的最大字符数
	    elementPathEnabled:false,	//关闭elementPath
	    //initialFrameWidth : 1010,
	    //initialFrameHeight: 60,
	});
	 */
</script>

<script>
	/* 提交点评 */
	function tiJiao(){
		var b1 = UE.getEditor('editor').hasContents();
		var img1Val = $("#img1").val();
		var img2Val = $("#img2").val();
		var img3Val = $("#img3").val();
		var b2 = (img1Val != "" || img2Val != "" || img3Val != "");
		if (b1==false && b2==false){
			dialogs("点评不完善，不能提交点评！");
			//alert("点评不完善，不能提交点评！");
			return false;
		}
		var geadeId = $("#geadeId").val();//年级
		var score = $("#score").val();//分数
		var pcontent = $("#pcontent").val();//内容
		var planguage = $("#planguage").val();//语言(高中表达)
		var pstructure = $("#pstructure").val();//结构（高中特征feature）
		var pwriting = $("#pwriting").val();//书写
		var editor1 = $("#editor1").val();//得分点
		var editor2 = $("#editor2").val();//失分点
		var editor3 = $("#editor3").val();//建议
		var span = $("#spanId").text();//评分等级
		if(geadeId=="低年级"||geadeId=="小学"||geadeId=="一年级"||geadeId=="二年级"||geadeId=="三年级"||geadeId=="四年级"||geadeId=="五年级"||geadeId=="六年级"){
			if(score!=""&&editor1!=""&&editor2!=""&&editor3!=""&&pcontent!=""&&planguage!=""&&pwriting!=""){
				if(span.length>3){
					dialogs(span);
				}else{
					preservation();
					<%-- document.forms.updateForm.action="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=5";
				    document.forms.updateForm.submit(); --%>
					//$("#updateForm").submit();
				}
			}else{
				dialogs("点评不完善，不能提交点评！");
				//alert("点评不完善，不能提交点评！");
			}
		}else if(geadeId=="初中"||geadeId=="初一"||geadeId=="初二"||geadeId=="初三"||geadeId=="七年级"||geadeId=="八年级"||geadeId=="九年级"){
			if(score!=""&&editor1!=""&&editor2!=""&&editor3!=""&&pcontent!=""&&planguage!=""&&pstructure!=""&&pwriting!=""){
				if(span.length>3){
					dialogs(span);
				}else{
					preservation();
					<%-- document.forms.updateForm.action="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=5";
				    document.forms.updateForm.submit(); --%>
					//$("#updateForm").submit();
				}
			}else{
				dialogs("点评不完善，不能提交点评！");
				//alert("点评不完善，不能提交点评！");
			}
		}else if(geadeId=="高一"||geadeId=="高二"||geadeId=="高三"){
			if(score!=""&&editor1!=""&&editor2!=""&&editor3!=""&&pcontent!=""&&planguage!=""&&pstructure!=""){
				if(span.length>3){
					dialogs(span);
				}else{
					preservation();
					<%-- document.forms.updateForm.action="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=5";
				    document.forms.updateForm.submit(); --%>
					//$("#updateForm").submit();
				}
			}else{
				dialogs("点评不完善，不能提交点评！");
				//alert("点评不完善，不能提交点评！");
			}
		}
		
	}
	//弹框
	function dialogs(text){
		var d = dialog({
			title: '提示',
			width: '300',
			//height: '35',
			content: '<center><h4><b>'+text+'</b></h4></center>',
			okValue: '确 定',
			ok: function () {}
		});
		d.showModal();
	}
	//提交点评
	function preservation(){
		$.ajax({  
			type: "POST",  
			url: "<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=5",  
			data: $("#updateForm").serialize(),  
			dataType: "text",  
			success: function(data){
				var d = dialog({
					title: '提示',
					width: '300',
					//height: '35',
					content: '<center><h4><b>'+data+'</b></h4></center>',
					okValue: '确 定',
					ok: function () {
						location.href="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=1";
					}
				});
				d.showModal();
			},
			error: function(){
				dialogs("提交点评失败！");
		    }   
        });
	}
	
	/* 取消 */
	function cancel(){
		location.href="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=1";
	}
	
	/* 暂存 */
	function temporary(){
		$.ajax({  
			type: "POST",  
			url: "<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=10",  
			data: $("#updateForm").serialize(),  
			dataType: "text",  
			success: function(data){
				var d = dialog({
					title: '提示',
					width: '300',
					//height: '35',
					content: '<center><h4><b>'+data+'</b></h4></center>',
					okValue: '确 定',
					ok: function () {
						location.href="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=1";
					}
				});
				d.showModal();
			},
			error: function(){
				dialogs("暂存失败！");
		    }  
        });  
		<%-- document.forms.updateForm.action="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=10"; --%>
	}
	
</script>
</html>