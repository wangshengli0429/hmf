<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.security.MessageDigest"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加管理员</title>
<jsp:include page="../../../inc.jsp"></jsp:include>
<script type="text/ecmascript" src="<%=request.getContextPath()%>/jslib/md5.js"></script>
</head>
<body style="font-family:Microsoft YaHei;">
<div class="viewFramework-body">
<div class="viewFramework-content">
<div class="p20">

<div style="display:inline;">
<span>权限管理>添加管理员</span>
</div>
<div style="display:inline;float:right;">
	<a href="<%=request.getContextPath()%>/servlet/AdministrationServlet?do=1" target="mainFrame2" style="color:#000000;text-decoration:none;">返回</a>
</div>
<hr>

<form id="qxform" action="" method="post" class="form-inline" role="form">
<table id="table1" align="center" style="border-collapse:separate; border-spacing:0px 10px;">
	<tr>
		<td align="right">姓名：</td>
		<td>
			<input type="text" name="name" id="name" class="form-control" style="width:178px;height:19px;" placeholder="请输入姓名">
			<span id="sname" style="color:red;"></span>
		</td>
	</tr>
	<tr>
		<td align="right">管理员账号：</td>
		<td><input type="text" name="userName" id="userName" onkeyup="this.value=this.value.replace(/\s+/g,'')" class="form-control" style="width:178px;height:19px;" placeholder="请输入账号"></td>
	</tr>
	<tr>
		<td>密码：</td>
	    <td>
		    <input type="password" id="pw" onkeyup="this.value=this.value.replace(/\s+/g,'')" maxlength="20" class="form-control" style="width:204px;height:33px;" placeholder="请输入密码">
		    <span id="" style="">6-20位</span>
		    <input type="hidden" name="pwd" id="pwd"/>
	    </td>
	</tr>
	<tr>
		<td>角色（选填）：</td>
		<td>
			<input type="text" name="role" id="role" class="form-control" style="width:178px;height:19px;" placeholder="请输入角色">
			<span id="" style="">如"运营"，"客服"，"财务"。</span>
	    </td>
	</tr>
	<tr>
		<td colspan="2" style="text-align:center"><br>
			<button type="button" onclick="nextStep()" class="btn btn-default" data-toggle="button" style="width:90px;">下&nbsp;一&nbsp;步</button>
	    </td>
	</tr>
</table>
<table id="table2" align="center" style="border-collapse:separate; border-spacing:0px 10px;">
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
		<td></td>
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
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td colspan="5" style="text-align:center">
			<br>
			<button type="button" onclick="quXiao()" class="btn btn-default" data-toggle="button" style="width:90px;">取&nbsp;消</button>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" onclick="addAdministrators()" class="btn btn-default" data-toggle="button" style="width:90px;">确&nbsp;定</button>
		</td>
	</tr>
</table>
</form>

</div>
</div>
</div>
</body>
<script type="text/javascript">
	$(function(){
		$("#table2").hide();
	})
	//下一步
	function nextStep(){
		var name = $("#name").val();//姓名
		var userName = $("#userName").val();//账号
		var pw = $("#pw").val();//密码
		var role = $("#role").val();//角色
		if(name==""){
			dialogs("请输入姓名！");
			return false;
		}
		if(userName==""){
			dialogs("请输入账号！");
			return false;
		}
		if(pw==""||pw.length<6||pw.length>20){
			dialogs("密码长度为6-20位字符！");
			return false;
		}
		$("#table1").hide();
		$("#table2").show();
	}
	//添加
	function addAdministrators(){
		var name = $("#name").val();//姓名
		var userName = $("#userName").val();//账号
		var pw = $("#pw").val();//密码
		var role = $("#role").val();//角色
		var jur = "";
		//权限(获取被选中的)
		var boxObj = $("input:checkbox[type=checkbox]");
		for(i=0;i<boxObj.length;i++){
			if (boxObj[i].checked == true){
				jur += boxObj[i].value + ",";
			}
	    }
		if(name==""){
			dialogs("请输入姓名！");
			return false;
		}
		if(userName==""){
			dialogs("请输入账号！");
			return false;
		}
		if(pw==""||pw.length<6||pw.length>20){
			dialogs("密码长度为6-20位字符！");
			return false;
		}
		
		if(name!=""&&userName!=""&&pw!=""){
				var hash = hex_md5(pw).toString().toUpperCase();//大写
				$("#pwd").val(hash);
				$.ajax({
					type: "POST",  
					url: "<%=request.getContextPath()%>/servlet/AdministrationServlet?do=10",  
					data: {"name":name,"userName":userName,"pw":hash,"role":role,"jurisdiction":jur},//$("#qxform").serialize(),  
					dataType: "text",  
					success: function(data){
						var d = dialog({
							title: '提示',
							width: '300',
							//height: '35',
							content: '<center><h4><b>'+data+'</b></h4></center>',
							okValue: '确 定',
							ok: function () {
								location.href="<%=request.getContextPath()%>/servlet/AdministrationServlet?do=1";
							}
						});
						d.showModal();
					},
					error: function(){
						dialogs("添加失败！");
				    }   
		        });
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