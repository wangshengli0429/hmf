<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page errorPage="/WEB-INF/view/404.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title></title>
<style type="text/css">
/*div1下面 包含着1个图片和1段文字*/
#div1 {
	position: fixed; /*绝对定位*/
	width: 600px;
	height: 600px;
	left:0px;
  	top:0px;
}
#div2 {
	position: fixed; /*绝对定位*/
	width: 600px;
	height: 600px;
	left:700px;
  	top:0px;
}
/*图片部分的设置*/
#img1 { /*position: static;默认定位,可以省略*/
	width: auto;
	height: auto;
	max-height: 600px;
	max-width: 600px;
}
/*文字的设置*/
.span {
	position: fixed; /*绝对定位*/
	text-align: center;
	font-size: 18px;
	color: white;
}
</style>
</head>
<body>
	<form id="imgUpdateId"  method="post" class="form-inline" role="form">
		<div id="div1">
			<img src="${img} " id="img1" onclick="img_onclick(event)" />
			<input type="hidden" id="count" value="1">
			<input type="hidden" id="showCount" value="1">
			<input type="hidden" id="countNum" name="countNum">
		</div>
		<div id="div2">
			<br><span style="font-size:25px;font-weight:800;margin-left:100px;">请您在图片上选择批注位置</span><br><br><br>
			<span style="font-size:20px;font-weight:normal;margin-left:0px;">批注列表：</span>
			<input type="button" onclick="tijiao()" value="提交修改"><br>
			
		</div>
	</form>
</body>
<script type="text/javascript">
	/*点击加载事件*/
	function img_onclick(event) {
		var width = document.getElementById('img1').width;
		var height = document.getElementById('img1').height;
	
		var count = document.getElementById('count').value;
		var showCount = document.getElementById('showCount').value;
		var str = prompt("请您填写批注","");
		var x = event.clientX;
		var y = event.clientY;
		
		var spanId = "span" + count;
		var pizhuId = "pizhu" + count;
		var textId = "text" + count;
		var div1Span = '<span class="span" id="'+ spanId +'" ><font size="3" color="#FF0000" id="'+ pizhuId +'"></font></span><br> ';
		document.getElementById("div1").innerHTML += div1Span;
		document.getElementById(pizhuId).innerText = str;
		
		var span = document.getElementById(spanId);
		var spanWidth = span.offsetWidth;
		if (spanWidth > width){
			alert("您输入的批注过长，请重新输入");
			return false;
		}else if (spanWidth > width - x){
			span.style.top = y + 'px';
			span.style.left = (width - spanWidth) + 'px';
		}else {
			span.style.top = y + 'px';
			span.style.left = x + 'px';
		}
		
		var yincangyu1 = '<input type="hidden" id="spanTop'+count+'" value="'+span.style.top+'" name="spanTop'+count+'">';
		var yincangyu2 = '<input type="hidden" id="spanLeft'+count+'" value="'+span.style.left+'" name="spanLeft'+count+'">';
		
		document.getElementById("div1").innerHTML += yincangyu1 + yincangyu2;
		
		var fontPizhu = document.getElementById(pizhuId).size;
		var fontSizeId = 'fontSize' + count;
		var textPizhuId = '<span id="'+'spanText'+count+'" style="font-size:14px;font-weight:normal;font-style:italic;margin-left:0px;">';
		//var showCountText1 = '<font color="#000000" id="'+'showCountText'+count+'">'+showCount+'</font>';
		//var showCountText2 = '<font color="#000000" >'+':  '+'</font>';
		var text = '<input name="'+ textId +'" type="text" id="'+ textId +'" value="'+ str +'" style="width:300px">';
		var font1 = '<font color="#000000" >修改字体大小：</font>';
		var font2 = '<select name="'+fontSizeId+'" id="'+fontSizeId+'" style="width:50px">';
		var font3 =	'<option value="1" >1</option>';
	    var font4 = '<option value="2" >2</option>';
	    var font5 = '<option value="3" selected="selected">3</option>';
	    var font6 = '<option value="4" >4</option>';
	    var font7 = '<option value="5" >5</option>';
	    var font8 = '<option value="6" >6</option>';
	    var font9 = '<option value="7" >7</option>';
	    var font10 = '</select>';
		var update = '<input type="button" value="更新" onclick="update('+count+','+x+')">';
		var del = '<input type="button" value="删除" onclick="del('+count+')"><br></span>';
		
		//document.getElementById("div2").innerHTML += textPizhuId+showCountText1+showCountText2+text+font1+font2+font3+font4+font5+font6+font7+font8+font9+font10+update+del;
		document.getElementById("div2").innerHTML += textPizhuId+text+font1+font2+font3+font4+font5+font6+font7+font8+font9+font10+update+del;
		document.getElementById('count').value = Number(count) + 1;
		document.getElementById('showCount').value = Number(showCount) + 1;
		
		var countNum = document.getElementById('countNum').value;
		document.getElementById('countNum').value = countNum + ',' + count;
		
		<%-- var url = "<%=request.getContextPath()%>/teacherPC/zuoWenGuanLi/page.jsp"
		window.open (url, "newwindow", "height=300, width=400, top=200, left=200, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no"); --%>
	}
	/*更新*/
	function update(count,x){
		var fontSizeId = 'fontSize' + count;
		var fonSize = document.getElementById(fontSizeId);
		var index = fonSize.selectedIndex ;
		var font = fonSize.options[index].value;
		
		var textId = "text" + count;
		var pizhuId = "pizhu" + count;
		var spanId = "span" + count;
		var text = document.getElementById(textId).value;
		document.getElementById(pizhuId).innerText = text;
		document.getElementById(pizhuId).size = font;
		
		var width = document.getElementById('img1').width;
		var span = document.getElementById(spanId);
		var spanWidth = span.offsetWidth;
		if (spanWidth > width){
			alert("您输入的批注过长，请重新输入");
			return false;
		}else if (spanWidth > width - x){
			span.style.left = (width - spanWidth) + 'px';
		}else {
			span.style.left = x + 'px';
		}
	}
	/*删除*/
	function del(count){
		var textId = "text" + count;
		var spanText = "spanText" + count;
		var spanId = "span" + count;
		var countText = "showCountText" + count;
		document.getElementById(spanId).remove();
		document.getElementById(spanText).remove();
		
		var countNum = document.getElementById('countNum').value;
		var countNums = countNum.split(",");
		var newCountNum = "";
		for(var i=0;i<countNums.length;i++){
			if(countNums[i] != count){
				newCountNum += countNums[i] + ",";
			}
		}
		document.getElementById('countNum').value = newCountNum;
		
		/*var showCount = Number(document.getElementById('showCount').value) - 1;
		document.getElementById('showCount').value = showCount;
		var num = showCount - c;
		if(num > 0){
			for(var i=showCount;i>c;i--){
				alert(i);
				var showCountText = "showCountText" + i;
				var num2 = Number(document.getElementById(showCountText).innerText);
				document.getElementById(showCountText).innerText = num2 - 1;
			}
		} */
	}
	function tijiao(){
		var form = document.getElementById("imgUpdateId");
		
		form.action = "<%=request.getContextPath()%>/servlet/TeacherFindServlet?do=12";
		form.submit(); 
	}
	
	
</script>
</html>