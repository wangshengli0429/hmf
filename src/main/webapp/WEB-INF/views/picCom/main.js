var globalArr = [];
var roadObj = {
	stsrtPosi:"",
	endPosi:"",
	passRoute:[],
	commentConcent:""
};
var lastImgArr = [];
var yyy = document.getElementById('xxx');
var context = yyy.getContext('2d');
var lineWidth = 2
var url = '';
var imageIndex = '';
var compId = '';
var messagewidth = '';
var newwidth = 0;
var newheight = 0;

autoSetCanvasSize(yyy)

listenToUser(yyy)
/*点击开始批改*/
comment.onclick = function(){
	if (globalArr.length == 0) {
		alert("您还没有选择批注区域！");
		return;
	}
	if (globalArr[globalArr.length -1].commentConcent != '') {
		alert("请您重新选择批注区域！");
		return;
	}
	//移除textarea的title属性
	document.getElementById('textarea').removeAttribute('title');
	yyy.classList.add('nopointer')
	toolleft.classList.add('nopointer')
	message.style.display ="inline"
}
/*确定批改内容*/
messagesure.onclick = function(){
	var index = globalArr.length - 1;
	var textarea = document.getElementById('textarea');
	var text = textarea.value;
	//检查内容是否为空
	if (text == '') {
		alert('请输入批改内容！');
		return;
	}
	//移除鼠标不可点击
	yyy.classList.remove('nopointer');
	toolleft.classList.remove('nopointer');
	message.style.display ="none";
	//取出textarea title属性，里面存放id
	var arrid = textarea.getAttribute('title');
	//如果id为空，向集合添加新元素，不为空，更改批注内容
	if (arrid == null || arrid == '') {
		//向集合添加元素
		var tempObj = globalArr.pop();
		tempObj.commentConcent = text;
		globalArr.push(tempObj);
		//在信息区添加进去评价内容
		var temp1 = '<a class="mya" id="';
		var temp2 = '">';
		var temp3 = '</a><div class="xian"></div>';
		$('#messagelist').append(temp1 + 'a' + index + temp2 + text + temp3);
		$("#textarea").val(""); 
	}else {
		arrid = arrid.substr(1);
		//更改批注列表内容
		var aid = 'a' + arrid;
		$("#" + aid).text(text);
		//更改集合
		globalArr[arrid].commentConcent = text;
	}
	$("#textarea").val(""); 
	/*$.each($("textarea"), function(i, n){
        autoTextarea($(n)[0]);
    });*/
}
/*取消批注*/
cancel.onclick = function(){
	yyy.classList.remove('nopointer');
	toolleft.classList.remove('nopointer');
	message.style.display ="none";
	$("#textarea").val(""); 
}
/*修改批注内容*/
$("#messagelist").delegate("a","click",function(){
	yyy.classList.add('nopointer')
	toolleft.classList.add('nopointer')
	var id = $(this).attr("id").substr(1);
	var text = $(this).text();
	$("#textarea").val(text); 
	document.getElementById('textarea').setAttribute("title", "t"+id);
	message.style.display ="inline"
 });



var eraserEnabled = false
pen.onclick = function(){
  eraserEnabled = false
  lineWidth = 2
  thin.classList.add('myactive')
  thick.classList.remove('myactive')
  //eraser.classList.remove('myactive')
  yyy.classList.add('mycursorpen')
  yyy.classList.remove('mycursoreraser')
}
/*eraser.onclick = function(){
  eraserEnabled = true
  eraser.classList.add('myactive')
  thin.classList.remove('myactive')
  thick.classList.remove('myactive')
  green.classList.remove('myactive')
  blue.classList.remove('myactive')
  red.classList.remove('myactive')
  yyy.classList.remove('mycursorpen')
  yyy.classList.add('mycursoreraser')
}*/
//撤销
clear.onclick = function(){
  context.clearRect(0, 0, yyy.width, yyy.height);
  if (lastImgArr.length > 1) {
	  var temp = lastImgArr[lastImgArr.length - 2];
	  context.putImageData(temp,0,0,0,0, temp.width, temp.height);
	  lastImgArr.pop();
	  var tempObj = globalArr.pop();
	  if (tempObj.commentConcent != '') {
		  $("#messagelist div:last").remove();
		  $("#messagelist a:last").remove();
	  }
  }else if (lastImgArr.length = 1){
	  if (globalArr.length > 0) {
		  var tempObj = globalArr.pop();
		  if (tempObj.commentConcent != '') {
			  $("#messagelist div:last").remove();
			  $("#messagelist a:last").remove();
		  }
	  }
	  lastImgArr = [];
	  globalArr = [];
  }else {
	  lastImgArr = [];
	  globalArr = [];
  }
}
//保存图片
download.onclick = function(){
	var r = confirm("您确定保存吗？保存后将关闭本页面！")
	if (r==true){
		file_update();
	}
}
bucket.onclick = function(){
	red.click()
}

red.onclick = function(){
  eraserEnabled = false
  context.fillStyle = 'red'
  context.strokeStyle = 'red'
  red.classList.add('myactive')
  green.classList.remove('myactive')
  blue.classList.remove('myactive')
  //eraser.classList.remove('myactive')
  if (lineWidth == 2) {
	  thin.classList.add('myactive')
	  thick.classList.remove('myactive')
  }else {
	  thick.classList.add('myactive')
	  thin.classList.remove('myactive')
  }
  yyy.classList.add('mycursorpen')
  yyy.classList.remove('mycursoreraser')
  thin.classList.remove('backgroundgreen')
  thick.classList.remove('backgroundgreen')
  thin.classList.remove('backgroundblue')
  thick.classList.remove('backgroundblue')
  thin.classList.add('backgroundred')
  thick.classList.add('backgroundred')
}
green.onclick = function(){
  eraserEnabled = false
  context.fillStyle = 'green'
  context.strokeStyle = 'green'
  red.classList.remove('myactive')
  green.classList.add('myactive')
  blue.classList.remove('myactive')
  //eraser.classList.remove('myactive')
  if (lineWidth == 2) {
	  thin.classList.add('myactive')
	  thick.classList.remove('myactive')
  }else {
	  thick.classList.add('myactive')
	  thin.classList.remove('myactive')
  }
  yyy.classList.add('mycursorpen')
  yyy.classList.remove('mycursoreraser')
  thin.classList.remove('backgroundred')
  thick.classList.remove('backgroundred')
  thin.classList.remove('backgroundblue')
  thick.classList.remove('backgroundblue')
  thin.classList.add('backgroundgreen')
  thick.classList.add('backgroundgreen')
}
blue.onclick = function(){
  eraserEnabled = false
  context.fillStyle = 'blue'
  context.strokeStyle = 'blue'
  red.classList.remove('myactive')
  green.classList.remove('myactive')
  blue.classList.add('myactive')
  //eraser.classList.remove('myactive')
  if (lineWidth == 2) {
	  thin.classList.add('myactive')
	  thick.classList.remove('myactive')
  }else {
	  thick.classList.add('myactive')
	  thin.classList.remove('myactive')
  }
  yyy.classList.add('mycursorpen')
  yyy.classList.remove('mycursoreraser')
  thin.classList.remove('backgroundred')
  thick.classList.remove('backgroundred')
  thin.classList.remove('backgroundgreen')
  thick.classList.remove('backgroundgreen')
  thin.classList.add('backgroundblue')
  thick.classList.add('backgroundblue')
}

thin.onclick = function(){
  eraserEnabled = false
  lineWidth = 2
  thin.classList.add('myactive')
  thick.classList.remove('myactive')
  //eraser.classList.remove('myactive')
  yyy.classList.add('mycursorpen')
  yyy.classList.remove('mycursoreraser')
}
thick.onclick = function(){
  eraserEnabled = false
  lineWidth = 4
  thick.classList.add('myactive')
  thin.classList.remove('myactive')
  //eraser.classList.remove('myactive')
  yyy.classList.add('mycursorpen')
  yyy.classList.remove('mycursoreraser')
}

/*初始化*/

function autoSetCanvasSize(canvas) {
  messagewidth = document.body.offsetWidth  - 870;
  if (messagewidth > 30) {
	  message.style.width = messagewidth + 'px';
  }else {
	  message.style.width = 30 + 'px';
  }
  textarea.style.resize = 'none'
  red.classList.add('myactive')
  thin.classList.add('myactive')
  thin.classList.add('backgroundred')
  thick.classList.add('backgroundred')
  yyy.classList.add('mycursorpen')
  var data = location.search.replace('?', '');
  var sts = data.split("-");
  imageIndex = sts[1];
  compId = sts[0];
  $.ajax({
		type : "post",
		url : "/pingdian/rest/otherService/findCompositionImg",
		data : '{"BM":{"data":"'+data+'"}}',
		success : function(str) {
			if (str.EC != 0) {
				alert('请求错误！');
				return;
			}
			url = str.BM.url;
			var newurl = 'https://api.pingdianedu.com:443/download' + str.BM.url;
			yyy.style.backgroundImage="url("+newurl+")";
			newwidth = 700;
			newheight = Math.floor(700 * str.BM.height / str.BM.width);
			yyy.width = newwidth;
			yyy.height = newheight;
			context.fillStyle = 'red'
			context.strokeStyle = 'red'
			document.getElementById('messagemain').style.minHeight  = newheight + 'px';
		},
		error : function() {
			alert("请求失败！");
		}
	});
  
  
  
  /*$.each($("textarea"), function(i, n){
      autoTextarea($(n)[0]);
  });*/
  /*setCanvasSize()
  window.onresize = function() {
    setCanvasSize()
  }

  function setCanvasSize() {
    var pageWidth = document.documentElement.clientWidth
    var pageHeight = document.documentElement.clientHeight

    canvas.width = pageWidth
    canvas.height = pageHeight
  }*/
}


function drawCircle(x, y, radius) {
  context.beginPath()
  context.arc(x, y, radius, 0, Math.PI * 2);
  context.fill()
}

function drawLine(x1, y1, x2, y2) {
  context.beginPath();
  context.moveTo(x1, y1) // 起点
  context.lineWidth = lineWidth
  context.lineTo(x2, y2) // 终点
  context.stroke()
  context.closePath()
}

function listenToUser(canvas) {


  var using = false
  var lastPoint = {
    x: undefined,
    y: undefined
  }
  // 特性检测
  if(document.body.ontouchstart !== undefined){
    // 触屏设备 苏菲就是个触屏设备啊哥
    canvas.ontouchstart = function(aaa){
      var x = aaa.touches[0].clientX-138
      var y = aaa.touches[0].clientY+23
      using = true
      if (eraserEnabled) {
        context.clearRect(x - 5, y - 5, 10, 10)
      } else {
        lastPoint = {
          "x": x,
          "y": y
        }
      }
      roadObj.stsrtPosi = (x+138-150) + "," + (y-23);//设置开始位置
    }
    canvas.ontouchmove = function(aaa){
      var x = aaa.touches[0].clientX-138
      var y = aaa.touches[0].clientY+23

      if (!using) {return}

      if (eraserEnabled) {
        context.clearRect(x - 5, y - 5, 10, 10)
      } else {
        var newPoint = {
          "x": x,
          "y": y
        }
        drawLine(lastPoint.x, lastPoint.y, newPoint.x, newPoint.y)
        lastPoint = newPoint
        var position = {
			x:"",
			y:""
		};
		position.x=x+138-150;
		position.y=y-23;
		roadObj.passRoute.push(position);
	      }
    }
    canvas.ontouchend = function(aaa){
    	var x = aaa.touches[0].clientX-138
        var y = aaa.touches[0].clientY+23
      using = false
      roadObj.endPosi = (x+138-150) + "," + (y-23);//设置结束位置
        var tempObj = {
  				stsrtPosi:roadObj.stsrtPosi,
  				endPosi:roadObj.endPosi,
  				passRoute:roadObj.passRoute,
  				commentConcent:roadObj.commentConcent
  		};
  		globalArr.push(tempObj);
  		roadObj.passRoute = [];
  		var lastImg = context.getImageData(0,  0,  canvas.width,  canvas.height);
  		lastImgArr.push(lastImg);
    }
  }else{
    // 非触屏设备
    canvas.onmousedown = function(aaa) {
      var x = turncoordinate(aaa, canvas, 'x') + 13;
      var y = turncoordinate(aaa, canvas, 'y') + 25;
      //var x = aaa.clientX
      //var y = aaa.clientY
      using = true
      if (eraserEnabled) {
        context.clearRect(x - 5, y - 5, 10, 10)
      } else {
        lastPoint = {
          "x": x,
          "y": y
        }
      }
      roadObj.stsrtPosi = (x) + "," + (y);//设置开始位置
      
      
    }
    canvas.onmousemove = function(aaa) {
      //var x = aaa.clientX - 138 
      //var y = aaa.clientY + 23
    	var x = turncoordinate(aaa, canvas, 'x') + 13;
        var y = turncoordinate(aaa, canvas, 'y') + 25;
      if (!using) {return}

      if (eraserEnabled) {
        context.clearRect(x - 5, y - 5, 10, 10)
      } else {
        var newPoint = {
          "x": x,
          "y": y
        }
        drawLine(lastPoint.x, lastPoint.y, newPoint.x, newPoint.y)
        lastPoint = newPoint
      }
      var position = {
				x:"",
				y:""
		};
		position.x=x;
		position.y=y;
		roadObj.passRoute.push(position);
    }
    canvas.onmouseup = function(aaa) {
    	var x = turncoordinate(aaa, canvas, 'x') + 13;
        var y = turncoordinate(aaa, canvas, 'y') + 25;
    	//var x = aaa.clientX - 138
        //var y = aaa.clientY + 23
      using = false
      roadObj.endPosi = (x) + "," + (y);//设置结束位置
      var tempObj = {
				stsrtPosi:roadObj.stsrtPosi,
				endPosi:roadObj.endPosi,
				passRoute:roadObj.passRoute,
				commentConcent:roadObj.commentConcent
		};
		globalArr.push(tempObj);
		roadObj.passRoute = [];
		var lastImg = context.getImageData(0,  0,  canvas.width,  canvas.height);
		lastImgArr.push(lastImg);
		
		//var str = JSON.stringify(globalArr);
		//alert("坐标信息集合-globalArr："+str);
    }
  }

}

function turncoordinate(aaa, canvas,which){
	var x = aaa.clientX;
	var y = aaa.clientY;
	var rect = canvas.getBoundingClientRect();
    var t = document.defaultView.getComputedStyle(canvas);
    if (which == 'x') {
    	var leftB = parseInt(t.borderLeftWidth);//获取的是样式，需要转换为数值  
    	var i = (x - rect.left) - leftB;  
		return i;
	}else {
		var topB = parseInt(t.borderTopWidth);  
		var j = (y - rect.top) - topB;  
		return j;
	}
}

//保存批改图片
function file_update(){
	var dataurl = yyy.toDataURL('image/png'); //base64图片数据
	var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
	bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
	while(n--){
		u8arr[n] = bstr.charCodeAt(n);
	}
	var obj = new Blob([u8arr], {type:mime});
	var fd = new FormData();
	fd.append("upfile", obj,"image.png"); 
	fd.append("url", url);
	var str = JSON.stringify(globalArr);
	fd.append("data", str); 
	fd.append("imageIndex", imageIndex); 
	fd.append("compId", compId); 
	fd.append("newwidth", newwidth); 
	fd.append("newheight", newheight); 
	$.ajax({
	    url: "/pingdian/rest/uploadService/teaComPic",
	    type: "POST",
	    processData: false,
	    contentType: false,
	    data: fd,
	    success: function (data) {
	    	window.close();
	    },
	    error: function() {  
            alert("加载失败");  
        }
	});
}

//textarea高度自适应
/*function autoTextarea(elem, extra, maxHeight) {
    extra = extra || 0;
    var isFirefox = !!document.getBoxObjectFor || 'mozInnerScreenX' in window,
    isOpera = !!window.opera && !!window.opera.toString().indexOf('Opera'),
        addEvent = function (type, callback) {
            elem.addEventListener ?
                elem.addEventListener(type, callback, false) :
                elem.attachEvent('on' + type, callback);
        },
        getStyle = elem.currentStyle ? 
        function (name) {
            var val = elem.currentStyle[name];
            if (name === 'height' && val.search(/px/i) !== 1) {
                var rect = elem.getBoundingClientRect();
                return rect.bottom - rect.top -
                       parseFloat(getStyle('paddingTop')) -
                       parseFloat(getStyle('paddingBottom')) + 'px';       
            };
            return val;
        } : function (name) {
            return getComputedStyle(elem, null)[name];
        },
    minHeight = parseFloat(getStyle('height'));
    elem.style.resize = 'none';//如果不希望使用者可以自由的伸展textarea的高宽可以设置其他值

    var change = function () {
        var scrollTop, height,
            padding = 0,
            style = elem.style;

        if (elem._length === elem.value.length) return;
        elem._length = elem.value.length;

        if (!isFirefox && !isOpera) {
            padding = parseInt(getStyle('paddingTop')) + parseInt(getStyle('paddingBottom'));
        };
        scrollTop = document.body.scrollTop || document.documentElement.scrollTop;

        elem.style.height = minHeight + 'px';
        if (elem.scrollHeight > minHeight) {
            if (maxHeight && elem.scrollHeight > maxHeight) {
                height = maxHeight - padding;
                style.overflowY = 'auto';
            } else {
                height = elem.scrollHeight - padding;
                style.overflowY = 'hidden';
            };
            style.height = height + extra + 'px';
            scrollTop += parseInt(style.height) - elem.currHeight;
            document.body.scrollTop = scrollTop;
            document.documentElement.scrollTop = scrollTop;
            elem.currHeight = parseInt(style.height);
        };
    };

    addEvent('propertychange', change);
    addEvent('input', change);
    addEvent('focus', change);
    change();
};*/