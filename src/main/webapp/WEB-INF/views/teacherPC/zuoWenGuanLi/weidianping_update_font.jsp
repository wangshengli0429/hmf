<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>未点评-批改</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body>
<div class="viewFramework-content">
<form id="updateForm" action="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=5" method="post">
<c:forEach items="${wdp}" var="w">
	<div style="display:inline;">
	<input type="hidden" name="id" value="${w.id}">
	<input type="hidden" name="title" value="${w.title}">
	<input type="hidden" name="author" value="${w.author}">
	<input type="hidden" name="draft" value="${w.draft}">
	<input type="hidden" name="geade" value="${w.geade}">
	<input type="hidden" name="udid" value="${w.udid}">
	<input type="hidden" name="pcontent" id="pcontent" value="">
	<input type="hidden" name="planguage" id="planguage" value="">
	<input type="hidden" name="pstructure" id="pstructure" value="">
	<input type="hidden" name="pwriting" id="pwriting" value="">
	<span>未点评>>${w.title}</span>
	</div>
	<div style="display:inline;float:right;">
	<a href="<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=1" target="mainFrame4" style="color:#000000;text-decoration:none;">返回</a>
	</div>
	<hr>
	
	<div style="padding-left:150px;padding-right:150px;">
	<center>
		<h3><b>${w.title}</b></h3>
		<pre style="color:#949494;background-color:#ffffff;border:0;">作者：${w.author}      ${w.draft}     ${w.geade}</pre>
	</center>
	<textarea  id="editor" name="content" type="text/plain" required>
	${w.content}
	</textarea >
	
	<br><br>
	<img src="<%=request.getContextPath()%>/images/1.png" alt="alttext" title="名师点评" style="float:left;width:1137px;">
	<br><span style="font-size:17px;margin-left:40px;">打分：</span><br>
		<table style="border-collapse:separate; margin-left:100px;">
			<tr>
			<td width="140px">
				<input type="text" name="score" id="score" class="form-control" style="width:178px;height:25px;" placeholder="请输入作文分值">
			</td>
			<td width="120px" style="text-align:center">
				<span id="spanId"></span>
			</td>
		</tr>
		</table>
	<br>
	
	<c:forEach items="${gradeList}" var="g">
	<input type="hidden" name="grade" id="geadeId" value="${g.grade}">
	</c:forEach>
	<div id="id1">
	<span style="font-size:17px;margin-left:40px;">小学作文评分标准：</span>
	<table id="tableTime1" border="1" style="border-collapse:separate; margin-left:100px;">
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
	
	<div style="display:inline;margin-left:100px;">
	<span style="color:#949494;">注：请按照不同的维度选择不同的作文等级，不可漏选。</span>
	</div>
	
	<img src="<%=request.getContextPath()%>/images/2.png" alt="alttext" title="评点教育" style="float:left;width:1137px;">
	<br>
	<div style="margin-left:100px;border-collapse:separate; border-spacing:0px 20px; background-color:#c9c9c9;">
		<div style="margin-left:10px;margin-right:10px;height:9px;background-color:#c9c9c9"></div>
	</div>
	<div style="margin-left:100px;border-collapse:separate; border-spacing:0px 20px; background-color:#c9c9c9;">
		<div style="margin-left:10px;margin-right:10px;height:10px;background-color:#c9c9c9"></div><br>
		<div style="margin-left:10px;margin-right:10px;margin-top:10px;background-color:#ffffff">&nbsp;&nbsp;得分点：</div>
		<div style="margin-left:10px;margin-right:10px;margin-top:0px;background-color:#ffffff">
			 <textarea id="editor1" name="scoring" type="text/plain" required></textarea>
		</div>
		
		<div style="margin-left:10px;margin-right:10px;margin-top:10px;background-color:#ffffff">&nbsp;&nbsp;失分点：</div>
		<div style="margin-left:10px;margin-right:10px;margin-top:0px;background-color:#ffffff">
			 <textarea id="editor2" name="points" type="text/plain" required></textarea>
		</div>
		
		<div style="margin-left:10px;margin-right:10px;margin-top:10px;background-color:#ffffff">&nbsp;&nbsp;建议：</div>
		<div style="margin-left:10px;margin-right:10px;margin-top:0px;background-color:#ffffff">
			 <textarea id="editor3" name="suggest" type="text/plain" required></textarea>
		</div>
	</div>
	<div style="margin-left:100px;border-collapse:separate; border-spacing:0px 20px; background-color:#c9c9c9;">
		<div style="margin-left:10px;margin-right:10px;height:9px;background-color:#c9c9c9"></div>
	</div>
	<div style="margin-right:10px;margin-top:10px;float:right;">
		<input type="button" value="暂存" onclick="" style="background:#ff8000;color:#ffffff;width:90px;height:35px; " >
		<input type="button" value="提交点评" onclick="tiJiao()" class="btn btn-info" style="background:#55a7fd;color:#ffffff;width:90px;height:35px; " >
	</div>
</div>
</c:forEach>
</form>
</div>
</body>

<script>
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
				if(fs>=46&&fs<=60){
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
	    autoClearinitialContent:false, //focus时自动清空初始化时的内容
	    wordCount:false,    //开启字数统计   false关闭
	    //maximumWords:500,   //允许的最大字符数
	    elementPathEnabled:false,	//关闭elementPath
	    //initialFrameWidth : 1010,
	    //initialFrameHeight: 500,
	});
	UE.getEditor('editor1', {
		toolbars:false,		//关闭工具栏
	    autoClearinitialContent:false, //focus时自动清空初始化时的内容
	    wordCount:true,    //开启字数统计   false关闭
	    maximumWords:500,   //允许的最大字符数
	    elementPathEnabled:false,	//关闭elementPath
	    initialFrameWidth : 1010,
	    initialFrameHeight: 60,
	});
	UE.getEditor('editor2', {
		toolbars:false,		//关闭工具栏
	    autoClearinitialContent:false, //focus时自动清空初始化时的内容
	    wordCount:true,    //开启字数统计   false关闭
	    maximumWords:500,   //允许的最大字符数
	    elementPathEnabled:false,	//关闭elementPath
	    initialFrameWidth : 1010,
	    initialFrameHeight: 60,
	});
	UE.getEditor('editor3', {
		toolbars:false,		//关闭工具栏
	    autoClearinitialContent:false, //focus时自动清空初始化时的内容
	    wordCount:true,    //开启字数统计   false关闭
	    maximumWords:500,   //允许的最大字符数
	    elementPathEnabled:false,	//关闭elementPath
	    initialFrameWidth : 1010,
	    initialFrameHeight: 60,
	});
</script>

<script>
	/* 提交点评 */
	function tiJiao(){
		/* var tab=document.getElementById("tableTime");
        var rows=tab.rows;
        console.log(rows.length);//获取表格的行数

        for(var i=0;i<rows.length;i++){ //遍历表格的行
           for(var j=0;j<rows[i].cells.length;j++){  //遍历每行的列
        	   if(rows[i].cells[j].css("background-color")){
          			console.log("第"+(i+1)+"行，第"+(j+1)+"列的值是:"+rows[i].cells[j].innerHTML);
       		   		
       	   		}
   		    }
        } */

		$("#updateForm").submit();
	}
</script>

</html>