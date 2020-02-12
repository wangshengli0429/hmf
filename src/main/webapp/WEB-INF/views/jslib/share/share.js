var idtemp = getRequest();
window.onload=function(){
	var nextId = '';
	var arr = idtemp.split("&");
	var id = arr[0];
	var condition = '';
	if (arr.length > 1) {
		condition = arr[1];
		condition = decodeURI(condition);
	}
	$.ajax({  
        type: "post",  
        url: "/pingdian/rest/otherService/shareComposition",
        data: '{"BM":{"id":"'+id+'","condition":"'+condition+'"}}',
        success: function(s) {
        	if (s.EC != 0) {
				alert('请求错误！');
				return;
			}
        	var str = s.BM.list;
        	nextId = str.nextId;
        	if(str.propo!=null&&str.propo!=""){
        		document.getElementById("propoDiv1").style.display ="block";
        		var l = str.propo.length;
        		if(l<=60){
        			$("#propo1").html("要求："+str.propo);
        		}else{
        			$("#propo1").html("要求："+str.propo.substring(0,60));//要求
        			$("#propo1").append("<a onclick='propoOpen()'><span style='font-size:1.2rem;color:#54a7fd;'>>></span><span style='padding-left:0.2rem;font-size:1rem;color:#54a7fd;'>展开</span></a>");//收起
        			$("#propo2").html("要求："+str.propo);//要求
        			$("#propo2").append("<a onclick='propoStop()'><span style='padding-left:0.2rem;font-size:1rem;color:#54a7fd;'>收起</span><span style='font-size:1.2rem;color:#54a7fd;'><<</span></a>");//收起
        		}
        	}
        	$("#score").text(str.score);//分数
        	$("#titleid").text(str.title);//标题
        	$("#author").text(str.sname);//作者
        	$("#draft").text(str.draft);//稿数
        	$("#grade").text(str.grade);//年级
        	var geadeId = str.grade;
        	$("#content").html(str.content);//作文内容
        	if(str.image1!=null&&str.image1!=""){
        		$("#i1").attr('src',str.image1);//图片1
        		//$("#a1").attr('href',str.image1);//图片1
        	}else {
        		document.getElementById("imga").style.display ="none"; 
        	}
        	if(str.image2!=null&&str.image2!=""){
        		$("#i2").attr('src',str.image2);//图片2
        		//$("#a2").attr('href',str.image2);//图片2
        	}else {
        		document.getElementById("imgb").style.display ="none"; 
        	}
        	if(str.image3!=null&&str.image3!=""){
        		$("#i3").attr('src',str.image3);//图片3
        		//$("#a3").attr('href',str.image3);//图片3
        	}else {
        		document.getElementById("imgc").style.display ="none"; 
        	}
        	if(str.image1==""&&str.image2==""&&str.image3==""){
        		document.getElementById("imgDiv").style.display ="none"; 
        	}
        	$("#img1").attr('src',str.image1);//图片1
        	$("#img2").attr('src',str.image2);//图片2
        	$("#img3").attr('src',str.image3);//图片3
        	$("#createTime").text(str.create_time);//上传时间
        	$("#teaName").text(str.tname);//老师姓名
        	$("#comTime").text(str.com_time);//点评时间
        	$("#voiceData").text(str.voice);//语音点评
        	//老师头像
        	$("#headid").css({'background':'url('+str.thead+')','background-size':'cover','background-position':'50% 50%','background-repeat':'no-repeat'});
        	//语音点评
	        if(str.voice!=null&&str.voice!=""){
	    		document.getElementById("voiceDiv").style.display ="block";
	    		document.getElementById("voiceOpenButton").style.display="block";
	    		document.getElementById("voiceCloseButton").style.display="none";
	    		$("#voiceData").val(str.voice);//语音
	    		$("#suggest2").text(str.suggest);//建议
	    	}else{
	    		//文字点评
	    		document.getElementById("writtenDiv").style.display ="block";
	        	if(geadeId=="低年级"||geadeId=="小学"||geadeId=="一年级"||geadeId=="二年级"||geadeId=="三年级"||geadeId=="四年级"||geadeId=="五年级"||geadeId=="六年级"){
	        		document.getElementById("pfbz1").style.display ="block";
	        		$("#content1").text(str.dp_content);//内容
	        		$("#content_ca1").text(str.dp_content_ca);
	        		$("#language1").text(str.dp_language);//语音
	        		$("#language_ca1").text(str.dp_language_ca);
	        		$("#writing1").text(str.dp_writing);//书写
	        		$("#writing_ca1").text(str.dp_writing_ca);
	        	}else if(geadeId=="初中"||geadeId=="初一"||geadeId=="初二"||geadeId=="初三"||geadeId=="七年级"||geadeId=="八年级"||geadeId=="九年级"){
	        		document.getElementById("pfbz2").style.display ="block";
	        		$("#content2").text(str.dp_content);//内容
	        		$("#content_ca2").text(str.dp_content_ca);
	        		$("#language2").text(str.dp_language);//语音
	        		$("#language_ca2").text(str.dp_language_ca);
	        		$("#structure2").text(str.dp_structure);//结构
	        		$("#structure_ca2").text(str.dp_structure_ca);
	        		$("#writing2").text(str.dp_writing);//书写
	        		$("#writing_ca2").text(str.dp_writing_ca);
	        	}else if(geadeId=="高一"||geadeId=="高二"||geadeId=="高三"){
	        		document.getElementById("pfbz3").style.display ="block";
	        		$("#content3").text(str.dp_content);//内容
	        		$("#content_ca3").text(str.dp_content_ca);
	        		$("#language3").text(str.dp_language);//表达
	        		$("#language_ca3").text(str.dp_language_ca);
	        		$("#structure3").text(str.dp_structure);//特征
	        		$("#structure_ca3").text(str.dp_structure_ca);
	        	}
	        	$("#scoring").text(str.scoring);//得分点
	        	$("#points").text(str.points);//失分点
	        	$("#suggest").text(str.suggest);//建议
	    	}
	    	$("#grading").text(str.grading);//作文等级
	    	//评价
	    	if(str.sati!=null&&str.sati!=""){//满意度
	    		document.getElementById("evaluate").style.display ="block";
	    		var radios = document.getElementsByName("satis");
	    		for (i=0;i<radios.length;i++){
	    			if(radios[i].value==str.sati){
	    				radios[i].click();
	    			}else{
	    				radios[i].disabled=true;
	    			}
	    		}
	    		var attit = str.attit;//负责态度
	    		var c = "";
	    		for(var i=0;i<attit;i++){
	    			c += "<img src='images/stars_a.png' style='margin-left:0.4rem;'>"
	    		}
	    		for(var i=0;i<(5-attit);i++){
	    			c += "<img src='images/stars_b.png' style='margin-left:0.4rem;'>"
	    		}
	    		$("#attits").append(c);
	    		$("#attit").text(attit + "分");//负责态度
	    		var profe = str.profe;//专业水平
	    		var d = "";
	    		for(var i=0;i<profe;i++){
	    			d += "<img src='images/stars_a.png' style='margin-left:0.4rem;'>"
	    		}
	    		for(var i=0;i<(5-profe);i++){
	    			d += "<img src='images/stars_b.png' style='margin-left:0.4rem;'>"
	    		}
	    		$("#profes").append(d);
	    		$("#profe").text(profe + "分");//专业水平
	    		$("#ave").text(str.ave);//平均分
	    	}
	    	/*//留言
	    	if(str.message.length>0){
	    		document.getElementById("message").style.display ="block";
	    		//学生头像
	        	$("#stuheadid").css({'background':'url('+str.shead+')','background-size':'cover','background-position':'50% 50%','background-repeat':'no-repeat'});
	        	//("#sheadid").css({'background':'url('+str.shead+')','background-size':'cover','background-position':'50% 50%','background-repeat':'no-repeat','display':'block'});
	        	$("#stuTime").text(str.message[0][0].time);//学生留言时间
	        	$("#stuMessage1").text(str.message[0][0].message);//学生第一次留言
	        	var t1 = "<tr><td colspan='2' class='span_message'><div class='top'><span class='top_triangle'></span><span>";
	        	var t2 = "</span></div></td></tr>";
	        	var t3 = "";
	        	for(i=0;i<str.message.length;i++){
	    			var list = str.message[i];
	    			for(j=0;j<list.length;j++){
	    				var m = list[j];
	    				if(i == 0 && j == 0){
	    					t3 = "";
	    				}else {
	    					if(m.userType=="stu"){
	    						t3 += t1 + "学生回复： " + m.message + t2;
	    					}
	    					if(m.userType=="tea"){
	    						t3 += t1 + "老师回复： " + m.message + t2;
	    					}
						}
	    			}
	    	    }
	        	$("#tableid").append(t3);
	    	}*/
	    	
	    	//留言
	    	if(str.message.length>0){
	        	var t1 = '<div class="div_xian7"></div>';
	        	var t2 = '<div class="pjmain"><div class="pjleft"><div class="img_student "'+ 
	        		'style="background:url('+str.shead+');background-size:cover;background-position:50% 50%;'+
	        		'background-repeat:no-repeat;"></div><br><div class="namefont">';
	        	var t3 = '</div></div><div class="pjright"><div class="pjrighttop"><a class="messagefont">';
	        	var t4 = '</a></div><div class="pjrightmiddle"><div class="pjfloatright"><a class="timefont">';
	        	var t5 = '</a>';
	        	var t6 = '<a onclick="showMessage(';
	        	var t7 = ')" class="showmessage" id="showMessage';
	        	var t8 = '" href="javascript:void(0)"  style="font-size: 0.9rem">查看回复（'
	        	var t9 = '）</a><a onclick="hideMessage(';
	        	var t10 = ')" class="hidemessage" id="hideMessage';
	        	var t11 = '" href="javascript:void(0)"  style="font-size: 0.9rem">收起回复</a>';
	        	var t12 = '<div class="pjrightbottom" id="messagedetail';
	        	var t13 = '"><div class="pjfloatright2">';
	        	var t14 = '<div class="img_small" style="background:url(';
	        	var t15 = ');background-size:cover;background-position:50% 50%;background-repeat:no-repeat;"></div>'+
					'<div class="pjdetail"><div class="pjdetailmessage"><a class="namefont2">';
	        	var t16 = '：</a><a class="messagefont">';
	        	var t17 = '</a></div><br><div class="pjdetailtime"><a class="timefont">';
	        	var t18 = ' 回复</a></div></div>';
	        	var t19 = '<br>';
	        	var t20 = '</div></div>';
	        	var t21 = '<div class="div_xianend"></div>';
	        	
	        	var result = "";
	        	
	        	for(i=0;i<str.message.length;i++){
	    			var list = str.message[i];
	    			var id = '' + i;
	    			if (i != 0) {
	    				result += t1;
					}
    				result += t2 + str.sname + t3 + list[0].message + t4 + list[0].time + t5;
    				if(list.length > 1){
    					result += t6 + id + t7 + id + t8 + (list.length - 1) + t9 + id + t10 + id + t11 + t20;
    				}else {
    					result += t20;
    				}
	    			if (list.length > 1) {
	    				result += t12 + id + t13;
						for(j=1;j<list.length;j++){
							if (list[j].userType == 'stu') {
								result += t14 + str.shead + t15 + str.sname + t16 + list[j].message + t17 + list[j].time + t18; 
							}else {
								result += t14 + str.thead + t15 + str.tname + t16 + list[j].message + t17 + list[j].time + t18; 
							}
							if (j != list.length - 1) {
								result += t19;
							}
						}
						result += t20;
					}
	    			result += t20;
	    	    }
	        	result += t21;
	        	$("#showMessage").append(result); 
	    	}
	    	
	    	if(str.sati==null||str.sati==""){
	    		if(str.message.length==0){
	    			document.getElementById("appraise").style.display="none";
	    		}
	    	}	
        },
        error: function (){
        	alert("请求失败！");
		}
    });
	//初始化图片放大展示
	baguetteBox.run('.baguetteBoxOne');
		baguetteBox.run('.baguetteBoxTwo');
	    baguetteBox.run('.baguetteBoxThree', {
	        animation: 'fadeIn',
	        noScrollbars: true
	    });
    baguetteBox.run('.baguetteBoxFour', {
        buttons: false
    });
    baguetteBox.run('.baguetteBoxFive', {
        captions: function(element) {
            return element.getElementsByTagName('img')[0].alt;
        }
    });
    
	//图片点击跳转
	$(".imglocation").click(function(){
		var v = $(this).attr("id").substr(1);
		$(location).attr('href', 'pic.html?id='+id+'-'+v);
	});
	//链接作文原文页面
	$("#compositionlist").click(function(){
		$(location).attr('href', 'home.html');
	});
	//下一篇
	$("#nextCom").click(function(){
		if (condition != null && condition != '') {
			$(location).attr('href', encodeURI('share.html?id='+nextId+'&'+condition));
		}else {
			$(location).attr('href', 'share.html?id='+nextId);
		}
	});
    
}
function propoOpen(){
	document.getElementById("propoDiv1").style.display ="none";
	document.getElementById("propoDiv2").style.display ="block";
}
function propoStop(){
	document.getElementById("propoDiv1").style.display ="block";
	document.getElementById("propoDiv2").style.display ="none";
}

//回复查看
function showMessage(id){
	var id1 = 'messagedetail' + id;
	var id2 = 'hideMessage' + id;
	var id3 = 'showMessage' + id;
	document.getElementById(id1).style.display ="block";
	document.getElementById(id2).style.display ="inline";
	document.getElementById(id3).style.display ="none";
}
function hideMessage(id){
	var id1 = 'messagedetail' + id;
	var id2 = 'hideMessage' + id;
	var id3 = 'showMessage' + id;
	document.getElementById(id1).style.display ="none";
	document.getElementById(id2).style.display ="none";
	document.getElementById(id3).style.display ="inline";
}

//语音播放
function voiceOpen() {
	RongIMLib.RongIMVoice.init();
	var data = document.getElementById("voiceData").value;
	var dataLength = data.length / 1024;
    RongIMLib.RongIMVoice.play(data, dataLength);
    document.getElementById("voiceOpenButton").style.display="none";
    document.getElementById("voiceCloseButton").style.display="block";
}
function voiceClose() {
	document.getElementById("voiceOpenButton").style.display="block";
	document.getElementById("voiceCloseButton").style.display="none";
    RongIMLib.RongIMVoice.stop();
}
//获取传入id
function getRequest() {
	var url = location.search; //获取url中"?"符后的字串 
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		//var splits = url.split("=", 0);
		return url.substring(4, url.length);
	};
}
