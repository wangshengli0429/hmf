<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://wwl.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看图片</title>
<jsp:include page="/inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div id="fade" style="display: none; position: absolute; top: 0%; left: 0%; width: 100%; height: 100%; background-color: black; z-index:1001; -moz-opacity: 0.6; opacity:.60; filter: alpha(opacity=60);"> 
	<div style="position: absolute; top: 20%; left: 80%; padding: 0px; border: 0px solid orange; z-index:1003; overflow: auto;">
        <div style="display:inline;" onclick="picClose()">
        	<img src="<%=request.getContextPath()%>/images/guanbi-.png" style="width:100%;height:100%;"/>
        </div>
        <div style="display:inline;" onclick="edit()">
        	<button type="button" class="btn btn-danger" style="background:#ff0000;" data-toggle="button">编辑</button>
        </div>
	</div>
</div>
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

	<input type="hidden" id="img1" value="http://123.56.186.12:8101/files/pingdianapp_img/composition/2018/1/1517214112425.jpg">
	<!-- // 查看图片 // -->
   	<a id="imga" href='javascript:void(0);' onclick='picBig1()'><img id="i1" src="http://123.56.186.12:8101/files/pingdianapp_img/composition/2018/1/1517214112425.jpg" style="width:100px;height:100px;"></a>
	<!-- // 查看图片 // left:200px;-->
	<div id="divCenter1" style="display: none; position: absolute; top: 20%; left: 20%; width: 60%; padding: 0px; border: 0px solid orange; background-color: white; z-index:1002; overflow: auto;">
        <div style="display:inline;">
        	<img src="http://123.56.186.12:8101/files/pingdianapp_img/composition/2018/1/1517214112425.jpg" width="100%" height="100%"/>
        </div>
    </div>
	<!-- // 查看图片 // -->
	
</div>
</div>
</div>
</body>

	<!-- // 查看图片 // -->
	<script>
		function picBig1() {
	    	picClose();
	    	document.getElementById('fade').style.display='block';
	        document.getElementById("divCenter1").style.display="block";
	    }
	    /* 点击大图关闭 */
	    function picClose() {
	        document.getElementById("divCenter1").style.display="none";
	        document.getElementById('fade').style.display='none';
	    }
	    /* 编辑 */
	    function edit (){
	    	
	    }
	</script>

</html>