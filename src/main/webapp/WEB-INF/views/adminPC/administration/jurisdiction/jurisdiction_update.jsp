<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理员-修改权限</title>
<jsp:include page="../../../inc.jsp"></jsp:include>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<div style="display:inline;">
<span>权限管理>修改权限</span>
</div>
<div style="display:inline;float:right;">
	<a href="<%=request.getContextPath()%>/servlet/AdministrationServlet?do=1" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="qxform" action="<%=request.getContextPath()%>/servlet/ManageFindFwServlet?do=6" method="post" class="form-inline" role="form">
<table id="table2" align="center" style="border-collapse:separate; border-spacing:0px 10px;">
	<tr>
		<td width="80px" valign="top">权限：</td>
		<td width="150px" valign="top">
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;">资源管理<br>
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">范文<br>
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">素材<br>
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">技法<br>
		</td>
		<td width="150px" valign="top">
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;">用户管理<br>
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">老师用户管理<br>
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">师资认证<br>
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">学生用户管理<br>
		
		</td>
		<td width="150px" valign="top">
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;">作文管理<br>
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">待点评作文管理<br>
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">已点评作文管理<br>
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">作文推荐<br>
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">评价管理<br>
		</td>
		<td width="150px" valign="top">
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;">交易管理<br>
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">订单管理<br>
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">退款/售后<br>
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">结算管理<br>
		</td>
	</tr>
	<tr>
		<td></td>
		<td width="150px" valign="top">
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;">活动管理<br>
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">轮播图管理<br>
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">评点卡<br>
		</td width="150px">
		<td width="150px" valign="top">
			<input type="checkbox" name="jurisdiction" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;">用户反馈<br>
		</td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td colspan="5" style="text-align:center">
			<br>
			<button type="button" onclick="quXiao()" class="btn btn-default" data-toggle="button" style="width:90px;">取&nbsp;消</button>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<button type="submit" onclick="addAdministrators()" class="btn btn-default" data-toggle="button" style="width:90px;">确&nbsp;定</button>
		</td>
	</tr>
</table>
</form>

</div>
</div>
</div>
</body>
<script type="text/javascript">
	//修改权限
	function updateAdministrators(){
		
	}
	//取消
	function quXiao(){
		location.href="<%=request.getContextPath()%>/servlet/AdministrationServlet?do=1";
	}
</script>
</html>