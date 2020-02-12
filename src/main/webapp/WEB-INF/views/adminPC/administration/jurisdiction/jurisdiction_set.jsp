<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<span>权限管理>设置权限</span>
</div>
<div style="display:inline;float:right;">
	<a href="<%=request.getContextPath()%>/servlet/AdministrationServlet?do=1" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="qxform" action="<%=request.getContextPath()%>/servlet/AdministrationServlet?do=6" method="post" class="form-inline" role="form">
<table id="table2" align="center" style="border-collapse:separate; border-spacing:0px 10px;">
	<input type="hidden" name="id" value="${id }">
	<input type="hidden" id="permissions" value="${permissions }">
	<tr>
		<td width="80px" valign="top">权限：</td>
	</tr>
	<tr>
			<%-- <c:forEach items="${list }" var="l">
				<td width="150px" valign="top">
				<c:if test="${l.FATHER_ID == '0' }">
					<input type="checkbox" name="${l.ID }" value="${l.ID }" onclick="_chkSelectAll(this.checked,'${l.ID }')" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;">${l.NAME }<br>
					<c:forEach items="${list }" var="ll">
						<c:if test="${ll.FATHER_ID == l.ID }">
							<input type="checkbox" name="${l.ID }" value="${ll.ID }" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">${ll.NAME }<br>
						</c:if>
					</c:forEach>	
				</c:if>
				</td>
			</c:forEach> --%>
		<td width="150px" valign="top">
			<input id="001000" type="checkbox" onclick="_chkSelectAll(this.checked,'001000')" name="001000" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;">资源管理<br>
			<c:forEach items="${list }" var="l">
				<c:if test="${l.FATHER_ID == '001000' }">
					<input type="checkbox" name="001000" value="${l.ID }" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">${l.NAME }<br>
				</c:if>
			</c:forEach>
		</td>
		<td width="150px" valign="top">
			<input id="002000" type="checkbox" onclick="_chkSelectAll(this.checked,'002000')" name="002000" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;">用户管理<br>
			<c:forEach items="${list }" var="l">
				<c:if test="${l.FATHER_ID == '002000' }">
					<input type="checkbox" name="002000" value="${l.ID }" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">${l.NAME }<br>
				</c:if>
			</c:forEach>
		</td>
		<td width="150px" valign="top">
			<input id="003000" type="checkbox" onclick="_chkSelectAll(this.checked,'003000')" name="003000" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;">作文管理<br>
			<c:forEach items="${list }" var="l">
				<c:if test="${l.FATHER_ID == '003000' }">
					<input type="checkbox" name="003000" value="${l.ID }" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">${l.NAME }<br>
				</c:if>
			</c:forEach>
		</td>
		<td width="150px" valign="top">
			<input id="004000" type="checkbox" onclick="_chkSelectAll(this.checked,'004000')" name="004000" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;">交易管理<br>
			<c:forEach items="${list }" var="l">
				<c:if test="${l.FATHER_ID == '004000' }">
					<input type="checkbox" name="004000" value="${l.ID }" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">${l.NAME }<br>
				</c:if>
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td width="150px" valign="top">
			<input id="005000" type="checkbox" onclick="_chkSelectAll(this.checked,'005000')" name="005000" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;">活动管理<br>
			<c:forEach items="${list }" var="l">
				<c:if test="${l.FATHER_ID == '005000' }">
					<input type="checkbox" name="005000" value="${l.ID }" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">${l.NAME }<br>
				</c:if>
			</c:forEach>
		</td>
		<td width="150px" valign="top">
			<input type="checkbox" name="006000" value="006000" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;">用户反馈<br>
		</td> 
		<td width="150px" valign="top">
			<input id="007000" type="checkbox" onclick="_chkSelectAll(this.checked,'007000')" name="007000" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;">系统管理<br>
			<c:forEach items="${list }" var="l">
				<c:if test="${l.FATHER_ID == '007000' }">
					<input type="checkbox" name="007000" value="${l.ID }" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">${l.NAME }<br>
				</c:if>
			</c:forEach>
		</td>
		<td width="150px" valign="top">
			<input id="008000" type="checkbox" onclick="_chkSelectAll(this.checked,'008000')" name="008000" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;">评点卡管理<br>
			<c:forEach items="${list }" var="l">
				<c:if test="${l.FATHER_ID == '008000' }">
					<input type="checkbox" name="008000" value="${l.ID }" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">${l.NAME }<br>
				</c:if>
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td width="150px" valign="top">
			<input id="009000" type="checkbox" onclick="_chkSelectAll(this.checked,'009000')" name="009000" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;">第三方合作<br>
			<c:forEach items="${list }" var="l">
				<c:if test="${l.FATHER_ID == '009000' }">
					<input type="checkbox" name="009000" value="${l.ID }" style="height:13px; vertical-align:bottom; margin-bottom:3px; margin-top:-1px;margin-left:15px;">${l.NAME }<br>
				</c:if>
			</c:forEach>
		</td>
		<td width="150px" valign="top"></td> 
		<td width="150px" valign="top"></td>
		<td width="150px" valign="top"></td>
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
<table id="table1" align="center" style="border-collapse:separate; border-spacing:0px 10px;">
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
	$("#table1").hide();
	//取消
	function quXiao(){
		location.href="<%=request.getContextPath()%>/servlet/AdministrationServlet?do=1";
	}
</script>
<script type="text/javascript">
	$(function() {//页面加载的时候触发
		//回显
	    var boxObj = $("input:checkbox[type=checkbox]")
	    var permissions = $("#permissions").val(); //用el表达式获取在控制层存放的复选框的值为字符串类型
	    var permission = permissions.split(','); //去掉它们之间的分割符“，” 
	    for(i=0;i<boxObj.length;i++){
	        for(j=0;j<permission.length;j++){
	            if(boxObj[i].value == permission[j]){  //如果值与修改前的值相等
	            	boxObj[i].checked= true;
	            	break;
	            }
	        }
	    }  
	});
	$(function(){
		//如果所所有子项都选择则全选
	    var names = "001000,002000,003000,004000,005000,007000,008000,009000";
	    var name = names.split(",");
	    for(i=0;i<name.length;i++){
	    	var boxObj2 = document.getElementsByName(name[i]);
    		var temp = document.getElementById(name[i]);
    		temp.checked = true;
	    	for(j=0;j<boxObj2.length;j++){
	    		if(boxObj2[j].checked == false){
	    			temp.checked = false;
	    		}
	    	}
	    }


	});
</script>
<script type="text/javascript">
	function _chkSelectAll(_ischecked,type){
		var hobbyarr = document.getElementsByName(type);
		for(var i=0;i<hobbyarr.length;i++){
			hobbyarr[i].checked = _ischecked;
		}
	}
	function _reverseChecked(){
		var hobbyarr = document.getElementsByName(type);
		for(var i=0;i<hobbyarr.length;i++){
			hobbyarr[i].checked = !hobbyarr[i].checked;
		}
	}
</script>
</html>