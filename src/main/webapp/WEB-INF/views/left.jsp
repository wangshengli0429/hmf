<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>left</title>
<jsp:include page="inc.jsp"></jsp:include>
<style>
	body{
		background:#f1f1f1;
		font-family:Microsoft YaHei;
		font-size:15px;
		text-decoration:none;
		font-weight:normal;
		height: 90%;
		overflow-x: hidden;
	}
</style>
	<style type="text/css">
      .m{color:#ff9900;font-size:15px;}
      .n{color:#54a7fd;;font-size:15px;}
      a:link {color: #54a7fd}      /* 未访问的链接 */
	  a:hover {color: #ff9900}   /* 当有鼠标悬停在链接上 */
	  a:active {color: #ff9900}   /* 被选择的链接 */
    </style>
</head>
<body>
	<c:forEach items="${list }" var="l">
		<c:if test="${l.FATHER_ID == '0'}">
			<div style="margin-top:40px;margin-left:20px;display:block;">
					<a href="<%=request.getContextPath()%>${l.URL }" onclick="${l.FUNCTION }gl()" id="${l.FUNCTION }id" target="mainFrame">${l.NAME }</a>
			</div>
		</c:if>
	</c:forEach>
	<%-- <div style="margin-top:40px;margin-left:20px;float:left;">
		<a href="<%=request.getContextPath()%>/ziYuanGuanLi/zyglCenter.jsp" onclick="zygl()" id="zyid" target="mainFrame" style="font-size:15px;text-decoration:none;">资源管理</a>
	</div>
	<div style="margin-top:40px;margin -left:20px;float:left;">
		<a href="<%=request.getContextPath()%>/yongHuGuanLi/yhglCenter.jsp" onclick="yhgl()" id="yhid" target="mainFrame" style="font-size:15px;text-decoration:none;">用户管理</a>
	</div>
	<div style="margin-top:40px;margin-left:20px;float:left;">
		<a href="<%=request.getContextPath()%>/zuoWenGuanLi/zwglCenter.jsp" onclick="zwgl()" id="zwid" target="mainFrame" style="font-size:15px;text-decoration:none;">作文管理</a>
	</div>
	<div style="margin-top:40px;margin-left:20px;float:left;">
		<a href="<%=request.getContextPath()%>/adminPC/jiaoYiGuanLi/jyglCenter.jsp" onclick="jygl()" id="jyid" target="mainFrame" style="font-size:15px;text-decoration:none;">交易管理</a>
	</div>
	<div style="margin-top:40px;margin-left:20px;float:left;">
		<a href="<%=request.getContextPath()%>/adminPC/huodongGuanli/hdglCenter.jsp" onclick="lbgl()" id="lbid" target="mainFrame" style="font-size:15px;text-decoration:none;">活动管理</a>
	</div>
	<div style="margin-top:40px;margin-left:20px;float:left;">
		<a href="<%=request.getContextPath()%>/servlet/ManageFankuiServlet?do=1" onclick="yhfk()" id="yhfkid"  target="mainFrame" style="font-size:15px;text-decoration:none;">用户反馈</a>
	</div>
	<div style="margin-top:40px;margin-left:20px;float:left;">
		<a href="<%=request.getContextPath()%>/adminPC/administration/manageCenter.jsp" onclick="administration()" id="administrationid"  target="mainFrame" style="font-size:15px;text-decoration:none;">系统管理</a>
	</div> --%>
<!-- 导航栏 -->
<%-- 
<nav class="navbar navbar-default" role="navigation"> 
    <div class="container-fluid">
        <ul class="nav navbar-header"> 
            <li>
				<a href="<%=request.getContextPath()%>/ziYuanGuanLi/zyglCenter.jsp" onclick="zygl()" id="zyid" target="mainFrame" class="navbar-brand" style="font-size:15px;">资源管理</a>
            </li>
            <li>
				<a href="<%=request.getContextPath()%>/yongHuGuanLi/yhglCenter.jsp" onclick="yhgl()" id="yhid" target="mainFrame" class="navbar-brand" style="font-size:15px;">用户管理</a>
            </li>
            <li>
            	<a href="<%=request.getContextPath()%>/zuoWenGuanLi/zwglCenter.jsp" onclick="zwgl()" id="zwid" target="mainFrame" class="navbar-brand" style="font-size:15px;">作文管理</a>
            </li>
            <li>
            	<a href="<%=request.getContextPath()%>/adminPC/jiaoYiGuanLi/jyglCenter.jsp" onclick="jygl()" id="jyid" target="mainFrame" class="navbar-brand" style="font-size:15px;">交易管理</a>
            </li>
            <li>
            	<a href="<%=request.getContextPath()%>/adminPC/lunBoGuanLi/lunBoTu.jsp" onclick="lbgl()" id="lbid" target="mainFrame" class="navbar-brand" style="font-size:15px;">轮播图管理</a>
            </li>
            <li>
            	<a href="##" onclick="yhfk()" id="yhfkid"  target="mainFrame" class="navbar-brand" style="font-size:15px;">用户反馈</a>
            </li>
        </ul> 
    </div> 
</nav>
 --%>
<!-- /导航栏 -->

</body>
<script type="text/javascript">
	$('a').each(function(){
		$(this).click(function(){
			$(this).css("color","#ff9900");
			$('a').not(this).css("color","#54a7fd");
		})
	})
	$(function (){
		$("a").css("color","#54a7fd");
	})
	$(function(){
		var zyid = document.getElementById("zyid");
		zyid.setAttribute("class", "n");
		var yhid = document.getElementById("yhid");
		yhid.setAttribute("class", "n");
		var zwid = document.getElementById("zwid");
		zwid.setAttribute("class", "n");
		var jyid = document.getElementById("jyid");
		jyid.setAttribute("class", "n");
		var hdid = document.getElementById("hdid");
		hdid.setAttribute("class", "n");
		var fkid = document.getElementById("fkid");
		fkid.setAttribute("class", "n");
		var xtid = document.getElementById("xtid");
		xtid.setAttribute("class", "n");
	})
	
	/* 资源管理   zygl*/
	function zygl(){
		var zyid = document.getElementById("zyid");
		zyid.setAttribute("class", "m");
		var yhid = document.getElementById("yhid");
		yhid.setAttribute("class", "n");
		var zwid = document.getElementById("zwid");
		zwid.setAttribute("class", "n");
		var jyid = document.getElementById("jyid");
		jyid.setAttribute("class", "n");
		var hdid = document.getElementById("hdid");
		hdid.setAttribute("class", "n");
		var fkid = document.getElementById("fkid");
		fkid.setAttribute("class", "n");
		var xtid = document.getElementById("xtid");
		xtid.setAttribute("class", "n");
	}
	/* 用户管理  yhgl */
	function yhgl(){
		var zyid = document.getElementById("zyid");
		zyid.setAttribute("class", "n");
		var yhid = document.getElementById("yhid");
		yhid.setAttribute("class", "m");
		var zwid = document.getElementById("zwid");
		zwid.setAttribute("class", "n");
		var jyid = document.getElementById("jyid");
		jyid.setAttribute("class", "n");
		var hdid = document.getElementById("hdid");
		hdid.setAttribute("class", "n");
		var fkid = document.getElementById("fkid");
		fkid.setAttribute("class", "n");
		var xtid = document.getElementById("xtid");
		xtid.setAttribute("class", "n");
	}
	/* 作文管理  zwgl */
	function zwgl(){
		var zyid = document.getElementById("zyid");
		zyid.setAttribute("class", "n");
		var yhid = document.getElementById("yhid");
		yhid.setAttribute("class", "n");
		var zwid = document.getElementById("zwid");
		zwid.setAttribute("class", "m");
		var jyid = document.getElementById("jyid");
		jyid.setAttribute("class", "n");
		var hdid = document.getElementById("hdid");
		hdid.setAttribute("class", "n");
		var fkid = document.getElementById("fkid");
		fkid.setAttribute("class", "n");
		var xtid = document.getElementById("xtid");
		xtid.setAttribute("class", "n");
	}
	/* 交易管理  jygl */
	function jygl(){
		var zyid = document.getElementById("zyid");
		zyid.setAttribute("class", "n");
		var yhid = document.getElementById("yhid");
		yhid.setAttribute("class", "n");
		var zwid = document.getElementById("zwid");
		zwid.setAttribute("class", "n");
		var jyid = document.getElementById("jyid");
		jyid.setAttribute("class", "m");
		var hdid = document.getElementById("hdid");
		hdid.setAttribute("class", "n");
		var fkid = document.getElementById("fkid");
		fkid.setAttribute("class", "n");
		var xtid = document.getElementById("xtid");
		xtid.setAttribute("class", "n");
	}
	/* 活动管理  lbgl */
	function hdgl(){
		var zyid = document.getElementById("zyid");
		zyid.setAttribute("class", "n");
		var yhid = document.getElementById("yhid");
		yhid.setAttribute("class", "n");
		var zwid = document.getElementById("zwid");
		zwid.setAttribute("class", "n");
		var jyid = document.getElementById("jyid");
		jyid.setAttribute("class", "n");
		var hdid = document.getElementById("hdid");
		hdid.setAttribute("class", "m");
		var fkid = document.getElementById("fkid");
		fkid.setAttribute("class", "n");
		var xtid = document.getElementById("xtid");
		xtid.setAttribute("class", "n");
	}
	/* 用户反馈  yhfk */
	function fkgl(){
		var zyid = document.getElementById("zyid");
		zyid.setAttribute("class", "n");
		var yhid = document.getElementById("yhid");
		yhid.setAttribute("class", "n");
		var zwid = document.getElementById("zwid");
		zwid.setAttribute("class", "n");
		var jyid = document.getElementById("jyid");
		jyid.setAttribute("class", "n");
		var hdid = document.getElementById("hdid");
		hdid.setAttribute("class", "n");
		var fkid = document.getElementById("fkid");
		fkid.setAttribute("class", "m");
		var xtid = document.getElementById("xtid");
		xtid.setAttribute("class", "n");
	}
	/* 系统管理   administration */
	function xtgl(){
		var zyid = document.getElementById("zyid");
		zyid.setAttribute("class", "n");
		var yhid = document.getElementById("yhid");
		yhid.setAttribute("class", "n");
		var zwid = document.getElementById("zwid");
		zwid.setAttribute("class", "n");
		var jyid = document.getElementById("jyid");
		jyid.setAttribute("class", "n");
		var hdid = document.getElementById("hdid");
		hdid.setAttribute("class", "n");
		var fkid = document.getElementById("fkid");
		fkid.setAttribute("class", "n");
		var xtid = document.getElementById("xtid");
		xtid.setAttribute("class", "m");
	}
</script>
</html>