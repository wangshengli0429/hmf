var code = '';
var state = 0;
var countdown = 0;
//发送验证码
function sendMessage(v) {
	document.getElementById("codeSpan").style.display ="none";
	var phone = document.getElementById("phone").value;
	if(phone.length==0){
		$("#phoneSpan").html("请输入手机号码");
		document.getElementById("phoneSpan").style.display ="block";
		statePhone = 1;
        return;
	}else if(!(/^1(3|4|5|7|6|8|9)\d{9}$/.test(phone))){ 
		$("#phoneSpan").html("手机号码有误，请重新填入");
		document.getElementById("phoneSpan").style.display ="block";
		statePhone = 1;
        return;
    }else {
    	statePhone = 0;
    }
	$.ajax({
		type:"get",
		dataType:"jsonp",
		url:"https://mobile.pingdianedu.com:8443/pingdian/rest/otherService/h5getVerificationCode?phone="+phone,
		jsonpCallback:"callback_fn3",
		success:function(data){
			if (data != null && data.code != null) {
				code = data.code;
			}else {
				$("#codeSpan").html("验证码发送失败，请重试！");
				document.getElementById("codeSpan").style.display ="block";
				return;
			}
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			alert(XMLHttpRequest.status);
			alert(XMLHttpRequest.readyState);
			alert(textStatus);
			$("#errorSpan").html("请求失败!");
			document.getElementById("errorSpan").style.display ="block";
		}
	});
	
	
	/*$.jsonp({
		type : "post",
		url : "https://mobile.pingdianedu.com:8443/pingdian/rest/otherService/h5getVerificationCode",
		data : '{"BM":{"phone":"'+phone+'"}}',
		success : function(str) {
			if (str.EC != '0') {
				$("#codeSpan").html("验证码发送失败，请重试！");
				document.getElementById("codeSpan").style.display ="block";
				return;
			}
			code = str.BM.code;
		},
		error : function() {
			$("#errorSpan").html("请求失败!");
			document.getElementById("errorSpan").style.display ="block";
		}
	});*/
	var getCode = document.getElementById("getCode");
	//getCode.innerHTML = "重新发送(60)";
	countdown=60;
	settime(v);
}
//倒计时
function settime(v) {
	if (countdown == 0) {
		v.removeAttribute("disabled");
		v.innerHTML = "获取验证码";
		countdown = 60;
		return false;
	} else {
		v.setAttribute("disabled", true);
		v.innerHTML = "重新发送(" + countdown + ")";
		countdown--;
	}  
	setTimeout(function() {
		settime(v);
	},1000);
}
//注册
function register() {
	verificationCode();
	verificationPhone();
	verificationPassword();
	if (state != 0) {
		state = 0;
		return;
	}
	var url = getCookie('url');
	//alert(url);//http://localhost:8080/pingdian/m/m713.htm
	var g = '';
	if(url.length>0){
		var graph = url.indexOf("graph_");
		if(graph!='-1'){
			g = '7';
		}else{
			g = url.substr(url.length-7,1);
		}
	}
	var phone = document.getElementById("phone").value;
	var nickname = phone;
	var grade = '';
	if(g=='7'){
		grade = '027011';
	}else if(g=='8'){
		grade = '027012';
	}else if(g=='9'){
		grade = '027013';
	}
	var password = document.getElementById("password").value;
	password = hex_md5(password).toString().toUpperCase();//加密 大写
	
	$.ajax({
		type:"get",
		dataType:"jsonp",
		url:"https://mobile.pingdianedu.com:8443/pingdian/rest/otherService/h5register?phone="+phone+"&nickname="+nickname
				+"&grade="+grade+"&password="+password,
		jsonpCallback:"callback_fn4",
		success:function(data){
			if (data != null && data.userId != null) {
				var userId = data.userId;
				document.cookie="userId="+userId;
				//alert("注册成功");
				var url = getCookie('url')
				location.href=url;
			}else {
				$("#errorSpan").html("注册失败!");
				document.getElementById("errorSpan").style.display ="block";
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
	
	/*$.jsonp({
		type : "post",
		url : "https://mobile.pingdianedu.com:8443/pingdian/rest/otherService/h5register",
		data : '{"BM":{"phone":"'+phone+'","nickname":"'+nickname+'","grade":"'+grade+'","password":"'+password+'"}}',
		success : function(str) {
			if (str.EC != '0') {
				$("#errorSpan").html("注册失败!");
				document.getElementById("errorSpan").style.display ="block";
				return;
			}
			userId = str.BM.userId;
			document.cookie="userId="+userId;
			//alert("注册成功");
			var url = getCookie('url')
			//alert(url);
			location.href=url;
		},
		error : function() {
			$("#errorSpan").html("请求失败!");
			document.getElementById("errorSpan").style.display ="block";
		}
	});*/
}
//获取cookie
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
//校验手机号
function verificationPhone() {
	var phone = document.getElementById("phone").value;
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
			if (result != 0) {
				$("#phoneSpan").html("手机号已注册!");
				document.getElementById("phoneSpan").style.display ="block";
				state = 1;
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
	
	/*$.jsonp({
		type : "post",
		url : "https://mobile.pingdianedu.com:8443/pingdian/rest/otherService/h5validatePhone",
		data : '{"BM":{"phone":"'+phone+'"}}',
		success : function(str) {
			if (str.EC != '0') {
				$("#phoneSpan").html("手机号已注册!");
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
//校验密码
function verificationPassword() {
	var password = document.getElementById("password").value;
	if (password.length < 6 || password.length > 20) {
		$("#passwordSpan").html("密码长度在6-20之间");
		document.getElementById("passwordSpan").style.display ="block";
		state = 1;
	}
};
//校验手机验证码
function verificationCode() {
	var codeStr = document.getElementById("code").value;
	if (code == '') {
		$("#codeSpan").html("请获取验证码");
		document.getElementById("codeSpan").style.display ="block";
		state = 1;
	}else if (codeStr.length > 0 && code != codeStr) {
		$("#codeSpan").html("验证码错误");
		document.getElementById("codeSpan").style.display ="block";
		state = 1;
	}
};


