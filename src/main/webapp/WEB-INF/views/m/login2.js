window.onload=function(){
	$("#phone").focus(function(){
		document.getElementById("phoneSpan").style.display ="none";
		document.getElementById("errorSpan").style.display ="none";
	});
	$("#password").focus(function(){
		document.getElementById("passwordSpan").style.display ="none";
		document.getElementById("errorSpan").style.display ="none";
	});
	$("#code").focus(function(){
		document.getElementById("codeSpan").style.display ="none";
		document.getElementById("errorSpan").style.display ="none";
	});
}
//显示密码
function displayCipher(){
	document.getElementById("invisible").style.display = "none";
	document.getElementById("visible").style.display = "block";
	document.getElementById("password").type = "text";
}
//隐藏密码
function hiddenPassword(){
	document.getElementById("visible").style.display = "none";
	document.getElementById("invisible").style.display = "block";
	document.getElementById("password").type = "password";
}
//跳转注册
function register(){
	location.href="http://www.pingdianedu.com:8101/files/html/registerPage.html";
}
//下载app
function appDownload(){
	//alert("下载app");
	//安卓学生版
	window.location.href = "http://123.56.186.12:8101/files/app/stu/app_stu_5_28.apk";
	//IOS学生版
	//window.location.href = "https://itunes.apple.com/cn/app/%20%E4%B8%80%E5%A0%82%E4%BD%9C%E6%96%87%E8%AF%BE%E5%AD%A6%E7%94%9F%E7%89%88/id1343683401?l=zh&ls=1&mt=8";
}
//关闭下载App div
function closeDownload(){
	document.getElementById("content2").style.display ="none";
}

//校验手机号
function verificationPhone1(phone) {
	if(phone.length==0){
		$("#phoneSpan").html("请输入手机号码");
		document.getElementById("phoneSpan").style.display ="block";
		state = 1;
        return;
	}else if(!(/^1(3|4|5|7|6|8|9)\d{9}$/.test(phone))){ 
		$("#phoneSpan").html("手机号码有误，请重新填入");
		document.getElementById("phoneSpan").style.display ="block";
		state = 1;
        return;
    }
	$.ajax({
		type:"get",
		dataType:"jsonp",
		url:"https://mobile.pingdianedu.com:8443/pingdian/rest/otherService/h5validatePhone?phone="+phone,
		jsonpCallback:"callback_fn2",
		success:function(data){
			var result = data.state;
			if (result == 0) {
				$("#phoneSpan").html("手机号未注册");
				document.getElementById("phoneSpan").style.display ="block";
				state = 1;
				return;
			}else{
				document.getElementById("phoneSpan").style.display ="none";
			}
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			//alert(XMLHttpRequest.status);
			//alert(XMLHttpRequest.readyState);
			//alert(textStatus);
			$("#errorSpan").html("请求失败!");
			document.getElementById("errorSpan").style.display ="block";
		}
	});
	/*$.ajax({
		async : true,
		type : "post",
		url : "https://api.pingdianedu.com:443/pingdian/rest/otherService/h5validatePhone",
		dataType: "jsonp",
		data : '{"BM":{"phone":"'+phone+'"}}',
		success : function(str) {
			alert(2);
			if (str.EC == '0') {
				$("#phoneSpan").html("手机号未注册");
				document.getElementById("phoneSpan").style.display ="block";
				state = 1;
				return;
			}else{
				document.getElementById("phoneSpan").style.display ="none";
			}
		},
		error : function() {
			$("#errorSpan").html("请求失败!");
			document.getElementById("errorSpan").style.display ="block";
		}
	});*/
};
function sign_in(){
	document.getElementById("phoneSpan").style.display ="none";
	document.getElementById("passwordSpan").style.display ="none";
	document.getElementById("errorSpan").style.display ="none";
	var phone = document.getElementById("phone").value;
	verificationPhone1(phone);
	verificationPassword();
	if (state == 1) {
		state = 0;
		return;
	}
	var password = document.getElementById("password").value;
	password = hex_md5(password).toString().toUpperCase();//加密 大写
	
	$.ajax({
		type:"get",
		dataType:"jsonp",
		url:"https://mobile.pingdianedu.com:8443/pingdian/rest/otherService/h5TouGaoLogin?phone="+phone+"&password="+password,
		jsonpCallback:"callback_fn1",
		success:function(data){
			var result = data.state;
			if (result == 0) {
				userId = data.userId;
				userType = data.userType;
				document.cookie="userId="+userId;
				var url = getCookie('url')
				location.href=url;
			}else if (result == -1){
				location.href="http://www.pingdianedu.com:8101/files/html/registerPage.html";
			}else {
				$("#passwordSpan").html("密码错误");
				document.getElementById("passwordSpan").style.display ="block";
			}
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			//alert(XMLHttpRequest.status);
            //alert(XMLHttpRequest.readyState);
            //alert(textStatus);
			$("#errorSpan").html("请求失败!");
			document.getElementById("errorSpan").style.display ="block";
		}

	});
	
	/*$.ajax({
		type : "post",
		url : "https://api.pingdianedu.com:443/pingdian/rest/otherService/h5TouGaoLogin",
		dataType: "jsonp",
		data : '{"BM":{"phone":"'+phone+'","password":"'+password+'"}}',
		success : function(str) {
			alert(0);
			if (str.EC != '0') {
				$("#errorSpan").html("请求错误！");
				document.getElementById("errorSpan").style.display ="block";
				return;
			}
			var state = str.BM.state;
			if (state == 0) {
				userId = str.BM.userId;
				userType = str.BM.userType;
				alert(userId);
				document.cookie="userId="+userId;
				//alert("登录成功");
				var url = getCookie('url')
								location.href=url;
			}else if (state == -1){
				$(location).attr('href', "registerPage.html");
			}else {
				$("#passwordSpan").html("密码错误");
				document.getElementById("passwordSpan").style.display ="block";
			}
		},
		error : function() {
			$("#errorSpan").html("请求失败!");
			document.getElementById("errorSpan").style.display ="block";
		}
	});*/
}
function getCookie(c_name){
	if (document.cookie.length>0){
		c_start=document.cookie.indexOf(c_name + "=")
		if (c_start!=-1){ 
			c_start=c_start + c_name.length+1 
			c_end=document.cookie.indexOf(";",c_start)
			if (c_end==-1) c_end=document.cookie.length
			return unescape(document.cookie.substring(c_start,c_end))
		}
	}
	return ""
}

//跳转音频播放页面
function play(){
	var url = getCookie('playurl');
	location.href=url;
}