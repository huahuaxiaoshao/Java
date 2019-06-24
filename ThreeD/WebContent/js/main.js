/*
 * jQuery File Upload Plugin JS Example
 * https://github.com/blueimp/jQuery-File-Upload
 *
 * Copyright 2010, Sebastian Tschan
 * https://blueimp.net
 *
 * Licensed under the MIT license:
 * http://www.opensource.org/licenses/MIT
 */

/* global $, window */

$(function () {
    'use strict';

    $.ImgSrc = function(file, id) {
		for (var i = 0; i < 1; i++) {
			if (!/image\/\w+/.test(file[i].type)) {// 测试是否符合要求的三种文件类型
				BootstrapDialog.alert({title:"请选择图片文件"});
				return false;
			}
			if (file[i].size > 2048 * 1024) {
				BootstrapDialog.alert({title:"图片不能大于2M"});
				ClearImg();
				continue;
			}
			var img;
			// console.log(document.getElementById("imgArea"));
			// console.log(file[i]);
			// console.log("file-size=" + file[i].size);
			var reader = new FileReader();//
			// 文件上载
			reader.onload = function(e) {
				// console.log("成功读取....");
				var div = document.createElement("div"); // 外层 div
				div.setAttribute("style", "position:relative;width:inherit;height:inherit;float:left;z-index:2;width:150px;margin-left:8px;margin-right:8px;");
				var del = document.createElement("div"); // 删除按钮div
				del.setAttribute("style", "position: absolute; bottom: 4px; right: 0px; z-index: 99; width: 30px; height:30px;border-radius:50%;")
				var delicon = document.createElement("img");
				delicon.setAttribute("src", "images/shanchu.png");
				delicon.setAttribute("title", "删除");
				delicon.setAttribute("style", "cursor:pointer;width: 30px; height:30px");
				del.onclick = function() {
					this.parentNode.parentNode.removeChild(this.parentElement);
					clearImg();
				};
				del.appendChild(delicon);
				div.appendChild(del);
				var imgs = document.createElement("img"); // 上传的图片
				imgs.setAttribute("name", "loadimgs");
				imgs.setAttribute("src", e.target.result);
				imgs.setAttribute("width", 150);
				// childNodes.length > 0 代表 最多传一张，再上传，就把前面的替换掉
				if (document.getElementById(id).childNodes.length > 0) {
					document.getElementById(id).removeChild(
							document.getElementById(id).firstChild);
				}
				div.appendChild(imgs)
				document.getElementById(id).appendChild(div);
			}
			reader.readAsDataURL(file[i]);
		}
	}
    
    // Initialize the jQuery File Upload widget:
    $('#fileupload').fileupload({
    	type : 'post',
    	dataType : 'json',
		// iframe : iframe,
		formData : {
			tmpDirs : !!$('#tmpDirs').val() ? $('#tmpDirs').val() : ''
		},
		url : 'fileUpload'
	}).bind('fileuploadprogressall', function (e, data) {
		if(data.loaded==data.total){
			$('#saveModel').removeClass('disabled');
		}
    });
    
    var $modelDetail = $('#modelDetail');

	$modelDetail.validate({
		errorElement : 'em',
		submitHandler : function(form) {
			if(!$('tr .template-download .fade')){
				parent.BootstrapDialog.alert("请先上传文件");
			}
			var options = {
					success: function(responseText){//提交后的回调函数
						var responseText = eval(responseText);
						switch(responseText.status){
							case 0:
							case 100:
								BootstrapDialog.alert({title:responseText.message,callback:function(result){
									if(result)
										window.location.href = responseText.data.redirect;
								}});
								break;
							case 102://文件未上传
								BootstrapDialog.alert({title:responseText.message});
								break;
						}
						
					},      
					error:function(e){
						console.error(e);
					},
					url: 'modelController/saveModel',       //默认是form的action， 如果申明，则会覆盖  
					type: 'POST',              //默认是form的method（get or post），如果申明，则会覆盖  
					dataType: 'json',          //html(默认), xml, script, json...接受服务端返回的类型  
					clearForm: false,           //成功提交后，清除所有表单元素的值  
					//resetForm: true,         //成功提交后，重置所有表单元素的值  
					timeout: 3000              //限制请求的时间，当请求大于3秒后，跳出请求  
			}
			$(form).ajaxForm(options);
			$(form).ajaxSubmit(options);
			
		}
	});
	

});
//图片上传主程序
function uploadImg() {
	$.ImgSrc(document.getElementById("picFileId").files, "imgArea");
}
// 清除file标签的src缓存
function clearImg() {
	var file = $("#picFileId")
	file.after(file.clone().val(""));
	file.remove();
}
