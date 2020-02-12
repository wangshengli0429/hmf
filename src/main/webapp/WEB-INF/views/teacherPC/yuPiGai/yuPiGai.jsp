<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page errorPage="/WEB-INF/view/404.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>预批改</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
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
<div class="viewFramework-content">
<form id="updateForm" action="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=7" method="post">
	<input type="hidden" name="pcontent" id="pcontent" value="">
	<input type="hidden" name="planguage" id="planguage" value="">
	<input type="hidden" name="pstructure" id="pstructure" value="">
	<input type="hidden" name="pwriting" id="pwriting" value="">
<div style="display:inline;">
<span>预批改作文</span>
</div>
<hr>
<div style="padding-left:200px;padding-right:200px;">
	<center>
		<h3><b>${list.name}</b></h3>
		<p style="color:#949494;">
			${list.grade}
		</p>
	</center>
	
	<textarea id="editor" name="content" type="text/plain" required>
		${list.content}
	</textarea>
	
	<input type="hidden" name="id" id="ydpId" value="${list.id}">
	<input type="hidden" name="grade" id="ydpGrade" value="${list.grade}">
	<br><br>
	
	<div style="margin-top:10px;">
	<img src="<%=request.getContextPath()%>/images/1.png" alt="alttext" style="float:left;width:100%;">
	</div>
	<div style="margin-top:20px;">
		<span style="font-size:17px;margin-left:0px;margin-top:20px;">打分：</span>
		<input type="number" name="score" id="score" class="form-control" style="width:178px;height:35px;display:inline;" placeholder="请输入作文分值">
		<span id="spanId" style="display:inline;"></span>
	</div>
	<!-- <table style="border-collapse:separate; margin-left:0px;">
		<tr>
			<td width="140px">
			</td>
			<td width="120px" style="text-align:center">
			</td>
		</tr>
	</table> -->
	
	<!-- 评分标准 -->
	<div id="id1" style="margin-top:10px;">
	<span style="font-size:17px;margin-left:0px;">小学作文评分标准：</span>
	<table id="tableTime1" border="1" style="border-collapse:separate; margin-left:0px;">
		<tr>
			<td width="140px" style="text-align:center"><img src="<%=request.getContextPath()%>/images/3.png" alt="alttext" style="float:left;width:100px;height:40px"></td>
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
			<td width="140px" style="text-align:center"><img src="<%=request.getContextPath()%>/images/3.png" alt="alttext" style="float:left;width:100px;height:40px"></td>
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
	
	<div style="display:inline;margin-left:0px;">
	<span style="color:#949494;">注：请按照不同的维度选择不同的作文等级，不可漏选。</span>
	</div>
	
	<div style="margin-top:10px;margin-bottom:0px;">
	<img src="<%=request.getContextPath()%>/images/2.png" alt="alttext" title="总评" style="float:left;width:1150px;">
	</div>
	<!-- <div style="margin-top:10px;margin-left:0px;border-collapse:separate; border-spacing:0px 20px; background-color:#c9c9c9;">
		<div style="margin-left:10px;margin-right:10px;height:9px;background-color:#c9c9c9"></div>
	</div> -->
	<div style="margin-top:20px;margin-left:0px;border-collapse:separate; border-spacing:0px 20px; background-color:#c9c9c9;">
		<div style="margin-left:10px;margin-right:10px;height:10px;background-color:#c9c9c9"></div><br>
		<div style="margin-left:10px;margin-right:10px;margin-top:10px;background-color:#ffffff">
			<span style="font-size:17px;">&nbsp;&nbsp;得分点：</span>
		</div>
		<div style="margin-left:10px;margin-right:10px;margin-top:0px;background-color:#ffffff;height:200px;">
			 <textarea name="scoring" id="editor1" onchange="validateString1(this.value)" onblur="validateString1(this.value)" maxlength="500" style="width:100%;height:100%;background-color: #ffffff; border: 1px #D7D7D7 solid;font-size: 16px;"></textarea>
			 <!-- <textarea id="editor1" name="scoring" type="text/plain" required></textarea> -->
		</div>
		<div style="margin-left:10px;margin-right:10px;margin-top:0px;background-color:#ffffff;">
		(<span id="lengthover1" style="color:red;">500</span>/500字)
		</div>
		
		<div style="margin-left:10px;margin-right:10px;margin-top:10px;background-color:#ffffff">
			<span style="font-size:17px;">&nbsp;&nbsp;失分点：</span>
		</div>
		<div style="margin-left:10px;margin-right:10px;margin-top:0px;background-color:#ffffff;height:200px;">
			 <textarea name="points" id="editor2" onchange="validateString2(this.value)" onblur="validateString2(this.value)" maxlength="500" style="width:100%;height:100%;background-color: #ffffff; border: 1px #D7D7D7 solid;font-size: 16px;"></textarea>
			 <!-- <textarea id="editor2" name="points" type="text/plain" required></textarea> -->
		</div>
		<div style="margin-left:10px;margin-right:10px;margin-top:0px;background-color:#ffffff;">
		(<span id="lengthover2" style="color:red;">500</span>/500字)
		</div>
		
		<div style="margin-left:10px;margin-right:10px;margin-top:10px;background-color:#ffffff">
			<span style="font-size:17px;">&nbsp;&nbsp;建议：</span>
		</div>
		<div style="margin-left:10px;margin-right:10px;margin-top:0px;background-color:#ffffff;height:200px;">
			 <textarea name="suggest" id="editor3" onchange="validateString3(this.value)" onblur="validateString3(this.value)" maxlength="800" style="width:100%;height:100%;background-color: #ffffff; border: 1px #D7D7D7 solid;font-size: 16px;"></textarea>
			 <!-- <textarea id="editor3" name="suggest" type="text/plain" required></textarea> -->
		</div>
		<div style="margin-left:10px;margin-right:10px;margin-top:0px;background-color:#ffffff;">
		(<span id="lengthover3" style="color:red;">800</span>/800字)
		</div>
		
		<div style="margin-left:10px;margin-right:10px;height:10px;background-color:#c9c9c9"></div>
	</div>
	
	<div style="margin-right:10px;margin-top:10px;float:right;">
		<input type="button" value="提交点评" onclick="tiJiao()" style="background:#ff8000;color:#ffffff;width:90px;height:35px; " >
	</div>
</div>
</form>
<div style="display:block;">
	<table style="margin-top:50px;" width="100%" topmargin="0" leftmargin="0">
		<tr>
			<td align="absmiddle">
				<hr><p style="text-align:center;color:#6a6a6a;font-size:15px;">
				<span>Copyright</span><span>© 作文评点网 版权所有 www.pingdianedu.com&nbsp; &nbsp; 京ICP备16007121号-1</span>
				</p>
			</td>
		</tr>
	</table>
</div>
</div>
</body>

<script>
	function validateString1(value){
	    if (value.length > 500) {
	        value = value.substr(0, 500);
	        $("#editor1").val(value);
	    }
	    $("#lengthover1").text(value.length );
	}
	function validateString2(value){
	    if (value.length > 500) {
	        value = value.substr(0, 500);
	        $("#editor2").val(value);
	    }
	    $("#lengthover2").text(value.length );
	}
	function validateString3(value){
	    if (value.length > 800) {
	        value = value.substr(0, 800);
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
		var geadeId = $("#ydpGrade").val();
		if(geadeId=="低年级"||geadeId=="小学"||geadeId=="一年级"||geadeId=="二年级"||geadeId=="三年级"||geadeId=="四年级"||geadeId=="五年级"||geadeId=="六年级"){
			tableTime1.style.display="block";
			$('#tableTime1 tr').each(function(){
				$("td[name='content1']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#pcontent").val($(this).text());
				        $('#A1 td').not(this).css("background-color","#ffffff");	//其他的全部设置为红色
				    });
				});
				$("td[name='language1']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#planguage").val($(this).text());
				        $('#B1 td').not(this).css("background-color","#ffffff");	//其他的全部设置为红色
				    });
				});
				$("td[name='writing1']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#pwriting").val($(this).text());
				        $('#C1 td').not(this).css("background-color","#ffffff");	//其他的全部设置为红色
				    });
				});
			});
		}else if(geadeId=="初中"||geadeId=="初一"||geadeId=="初二"||geadeId=="初三"||geadeId=="七年级"||geadeId=="八年级"||geadeId=="九年级"){
			tableTime2.style.display="block";
			$('#tableTime2 tr').each(function(){
				$("td[name='content2']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#pcontent").val($(this).text());
				        $('#A2 td').not(this).css("background-color","#ffffff");	//其他的全部设置为红色
				    });
				});
				$("td[name='language2']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#planguage").val($(this).text());
				        $('#B2 td').not(this).css("background-color","#ffffff");	//其他的全部设置为红色
				    });
				});
				$("td[name='structure2']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#pstructure").val($(this).text());
				        $('#C2 td').not(this).css("background-color","#ffffff");	//其他的全部设置为红色
				    });
				});
				$("td[name='writing2']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#pwriting").val($(this).text());
				        $('#D2 td').not(this).css("background-color","#ffffff");	//其他的全部设置为红色
				    });
				});
			});
		}else if(geadeId=="高中"||geadeId=="高一"||geadeId=="高二"||geadeId=="高三"){
			tableTime3.style.display="block";
			$('#tableTime3 tr').each(function(){
				$("td[name='content3']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#pcontent").val($(this).text());
				        $('#A3 td').not(this).css("background-color","#ffffff");	//其他的全部设置为红色
				    });
				});
				$("td[name='express3']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#planguage").val($(this).text());
				        $('#B3 td').not(this).css("background-color","#ffffff");	//其他的全部设置为红色
				    });
				});
				$("td[name='feature3']").each(function(){
				    //$(this).css("background-color","#ffffff");//还原所有td的颜色
				    $(this).click(function(){
				        $(this).css("background-color","#ffdcb9");//设置点击td的颜色
				        $("#pstructure").val($(this).text());
				        $('#C3 td').not(this).css("background-color","#ffffff");	//其他的全部设置为红色
				    });
				});
			});
		}
	})

	/* 分值 */
	$("#score").blur(function (){
		var fs = $("#score").val();
		var geadeId = $("#ydpGrade").val();
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
	/* 提交点评 */
	function tiJiao(){
		var geadeId = $("#ydpGrade").val();//年级
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
					alert(span);
				}else{
					$("#updateForm").submit();
				}
			}else{
				alert("点评不完善，不能提交点评！");
			}
		}else if(geadeId=="初中"||geadeId=="初一"||geadeId=="初二"||geadeId=="初三"||geadeId=="七年级"||geadeId=="八年级"||geadeId=="九年级"){
			if(score!=""&&editor1!=""&&editor2!=""&&editor3!=""&&pcontent!=""&&planguage!=""&&pstructure!=""&&pwriting!=""){
				if(span.length>3){
					alert(span);
				}else{
					$("#updateForm").submit();
				}
			}else{
				alert("点评不完善，不能提交点评！");
			}
		}else if(geadeId=="高一"||geadeId=="高二"||geadeId=="高三"){
			if(score!=""&&editor1!=""&&editor2!=""&&editor3!=""&&pcontent!=""&&planguage!=""&&pstructure!=""){
				if(span.length>3){
					alert(span);
				}else{
					$("#updateForm").submit();
				}
			}else{
				alert("点评不完善，不能提交点评！");
			}
		}
	}

</script>

<!-- ueditor富文本编辑器 -->
<script>
	//实例化编辑器
	//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
	UE.getEditor('editor', {
	    autoClearinitialContent:false, //focus时自动清空初始化时的内容
	    wordCount:false,    //开启字数统计   false关闭
	    //maximumWords:500,   //允许的最大字符数
	    elementPathEnabled:false,	//关闭elementPath
	    enterTag:'br',
	    //initialFrameWidth : 1010,
	    //initialFrameHeight: 500,
	});
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

</html>