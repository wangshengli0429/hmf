//保存批改图片
function file_update(){
	var can = document.getElementById("mycanvas");
	var dataurl = can.toDataURL('image/png'); //base64图片数据
	var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
	bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
	while(n--){
		u8arr[n] = bstr.charCodeAt(n);
	}
	var obj = new Blob([u8arr], {type:mime});
	var fd = new FormData();
	fd.append("upfile", obj,"image.png");
	$.ajax({
	    url: "http://localhost:8080/pingdian/rest/uploadService/test",
	    type: "POST",
	    processData: false,
	    contentType: false,
	    data: fd,
	    success: function (data) {
	    	//console.log(data);
	    	alert("保存成功！");
	    	window.close();
	    },
	    error: function() {  
            alert("加载失败");  
        }
	});
}

var img_width;
var img_height;
//通过图片路径打开图片
function open_img(){
	//var route = location.search..substring(30);//图片路径
	var imgUrl = location.search.replace('?http://123.56.186.12:8101', '');//图片路径
	var route = "https://api.pingdianedu.com:443/download";
	var img = new Image();
	img.src = location.search.replace('?', '')
	img_width = img.width;//获取图片宽度
	img_height = img.height;//获取图片高度
	location.hash = "load:" + encodeURIComponent(route + imgUrl);
	
	/*function.js  --  file_load_from_url()  通过路径打开图片*/
	/*if($file_load_from_url_window){
		$file_load_from_url_window.close();
	}
	var $w = new $FormWindow().addClass("dialogue-window");
	$file_load_from_url_window = $w;
	$w.title("Load from URL");
	// TODO: URL validation (input has to be in a form (and we don't want the form to submit))
	$w.$main.html("<label>URL: <input type='url' required value='' class='url-input'/></label>");
	var $input = $w.$main.find(".url-input");
	$w.$Button("Load", function(){
		$w.close();
		// TODO: retry loading if same URL entered
		// actually, make it change the hash only after loading successfully
		// (but still load from the hash when necessary)
		// make sure it doesn't overwrite the old session before switching
		location.hash = "load:" + encodeURIComponent($input.val());
	}).focus();
	$w.$Button("Cancel", function(){
		$w.close();
	});
	$w.center();
	$input.focus();*/
}
//打开图片
window.onload = function(){
	//var route = location.search.substring(5);//图片路径
	//alert(route);
	open_img();
	//var can = document.getElementById("mycanvas");
	//var canWidth = can.width;
	//var canHeight = can.height;
	//var g = (700/canWidth)*canHeight;
	if(img_width>700){
		var g = (700/img_width)*img_height;
		var h = Math.round(g);//四舍五入
		document.getElementById("mycanvas").style.maxWidth = "700px";
		document.getElementById("mycanvas").style.maxHeight = h+"px";
		//document.getElementById("mycanvas").style.margin = "0 auto";//居中
		$canvas_area.trigger("resize");
	}
	
	/*function.js  --  file_open()  打开图片*/
	/*if(window.chrome && chrome.fileSystem && chrome.fileSystem.chooseEntry){
		chrome.fileSystem.chooseEntry({
			type: "openFile",
			accepts: [{mimeTypes: ["image/*"]}]
		}, function(entry){
			file_entry = entry;
			if(chrome.runtime.lastError){
				return console.error(chrome.runtime.lastError.message);
			}
			open_from_FileEntry(entry, function(err){
				if(err){
					show_error_message("Failed to open file:", err);
				}
			});
		});
	}else{
		get_FileList_from_file_select_dialog(function(files){
			open_from_FileList(files, "selected");
		});
	}*/
}

//批注
function add_comment(){
	comment = prompt("请输入您的点评:","");
	if (comment != null){
		var le = comment.length;
		if(le==0){
			alert("您输入的点评内容为空!");
		}else{
			//alert("您输入的点评内容是：" + comment);
			globalArr[globalArr.length-1].commentConcent = comment;
			var str = JSON.stringify(globalArr);
			alert("坐标信息集合-globalArr："+str);
			
			var ctx = document.getElementById("mycanvas").getContext("2d");

			ctx.font = "10px PingFang-SC-Bold";
			var gradient=ctx.createLinearGradient(0,0,c.width,0);
			ctx.fillStyle=gradient;
			ctx.fillText("此处有批注",655,370);
			
		}
	}else{
		//alert("你按了[取消]按钮");
	}
}


//TODO: rename these functions to lowercase (and maybe say "files" in this case)
function get_FileList_from_file_select_dialog(callback){
	// TODO: specify mime types?
	create_and_trigger_input({type: "file"}, function(input){
		callback(input.files);
	});
}
function create_and_trigger_input(attrs, callback){
	var $input = $(E("input")).attr(attrs)
		.on("change", function(){
			callback(this);
			$input.remove();
		})
		.appendTo($app)
		.hide()
		.click();
	return $input;
}
function open_from_FileList(files, user_input_method_verb_past_tense){
	var file = get_image_file_from_FileList_or_show_error(files);
	if(file){
		open_from_File(file, function(err){
			if(err){ return show_error_message("Failed to open file:", err); }
		});
	}
}
function get_image_file_from_FileList_or_show_error(files, file_list_user_input_method_verb_past_tense){
	for(var i=0; i<files.length; i++){
		var file = files[i];
		if(file.type.match(/^image/)){
			return file;
		}
	}
	if(files.length > 1){
		show_error_message("None of the files " + file_list_user_input_method_verb_past_tense + " appear to be images.");
	}else{
		// TODO: ucfirst(file_list_user_input_method_verb_past_tense) + " file" might be more natural
		show_error_message("File " + file_list_user_input_method_verb_past_tense + " does not appear to be an image.");
	}
}