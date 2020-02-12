var content2 = getContent2();
$(function(){
	document.getElementById("visible").style.display ="none";
	var con2 = document.getElementById("content2");
	con2.innerHTML = content2;
})

//下载App div
function getContent2(){
	var str = "";
	var str1 = "<div class='contents'><div class='footer'><img class='icon' src='images/about_us_ic.png'><div class='text_div' style='float: left;'><font class='text_title1'>一堂作文课</font><br><font class='text_title2'>好作文是批改出来的！</font></div>";
	var str2 = "<img class='guanbi' src='images/guanbi-.png' onclick='closeDownload()'>";
	var str3 = "<div class='download_but' onclick='appDownload()'>下载app</div>";
	var str4 = "</div></div>";
	str = str1 + str2 + str3 +str4;
	return str;
}