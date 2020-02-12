<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>预点评作文-已点评-查看未点评作文</title>
<jsp:include page="../../inc.jsp"></jsp:include>
</head>
<body>
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

	<c:forEach items="${weidplist}" var="wei">
		<div style="display:inline;">
		<span>作文管理>预点评作文>未点评>${wei.newTitle}</span>
		</div>
		
		<div style="display:inline;float:right;">
		<a href="<%=request.getContextPath()%>/servlet/ManageZuowenServlet?do=7" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
		</div>
		<hr>
		
		<center>
			<h3>
			<b>${wei.newTitle}</b>
			</h3>
			<p style="color:#b2b2b2">
				${wei.geade}
			</p>
		</center>
		${wei.content}
		<br><br>
		
		<!-- // 查看图片 // -->
		<c:if test="${not empty wei.img1}">
	    	<a name='fff' href='javascript:void(0);' onclick='picBig1()'><img id="i1" src="${wei.img1}" style="width:100px;"></a>
		</c:if>
		<c:if test="${not empty wei.img2}">
	    	<a name='fff' href='javascript:void(0);' onclick='picBig2()'><img id="i2" src="${wei.img2}" style="width:100px;"></a>
	    </c:if>
	    <c:if test="${not empty wei.img3}">
	    	<a name='fff' href='javascript:void(0);' onclick='picBig3()'><img id="i3" src="${wei.img3}" style="width:100px;"></a>
		</c:if>
		<!-- // 查看图片 // left:200px;-->
		<div id="divCenter1" align="center" style="position: absolute; z-index: 9999; display: none;left:50%;top:50%;margin-left:-350px;margin-top:-100px;">
	        <div style="display:inline;" onclick="picClose()">
	        	<img src="${wei.img1}" width=700px/>
	        </div>
	    </div>
	    <div id="divCenter2" align="center" style="position: absolute; z-index:9999; display: none;left:50%;top:50%;margin-left:-350px;margin-top:-100px;">
	        <div style="display:inline;" onclick="picClose()">
	        	<img src="${wei.img2}" width=700px/>
	        </div>
	    </div>
	    <div id="divCenter3" align="center" style="position: absolute; z-index: 9999; display: none;left:50%;top:50%;margin-left:-350px;margin-top:-100px;">
	        <div style="display:inline;" onclick="picClose()">
	        	<img src="${wei.img3}" width=700px/>
	        </div>
	    </div>
		<!-- // 查看图片 // -->
		<br>
	</c:forEach>

</div>
</div>
</div>
</body>
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