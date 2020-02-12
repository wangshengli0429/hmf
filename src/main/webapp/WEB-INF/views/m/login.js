var userId = document.cookie.indexOf("userId");
var url = window.location.href;
document.cookie="url="+url;

if(userId==-1){
	//alert("没有登录");
	var url = window.location.href;
	document.cookie="url="+url;
	location.href="http://www.pingdianedu.com:8101/files/html/loginPage.html";
	//location.href="/pingdian/m/loginPage.html";
}else{
	//userid = getCookie('userId')
	//if (userid!=null && userid!=""){
		//alert('用户id：'+userid);
	//}
}

/*var playState = getCookie("playState");
//alert("aa   "+playState);
if(playState!=null||playState!=undefined||playState!=''){
	//alert("bbb");
	
	var a = url.substring(url.length-5);
	if(a == '2.htm'){
		if(playState=='1'){
			//alert("ccc");
			longin1();
		}else{
			location.href="http://www.pingdianedu.com:8101/files/html/videoPlayback.html";
		}
	}else{
		longin1();
	}
}else{
	longin1();
}

function longin1(){
	if(userId==-1){
		//alert("没有登录");
		var url = window.location.href;
		document.cookie="url="+url;
		location.href="http://www.pingdianedu.com:8101/files/html/loginPage.html";
	}
}
*/

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