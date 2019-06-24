<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link href="css/bootstrap.css" type="text/css" rel="stylesheet" media="all">
	<link href="css/bootstrap-dialog.min.css" type="text/css" rel="stylesheet" media="all">
	<link href="css/style.css" type="text/css" rel="stylesheet" media="all">
	<link rel="stylesheet" href="css/style.css">
	<link rel="stylesheet" href="css/blueimp-gallery.min.css">
	<link rel="stylesheet" href="css/jquery.fileupload.css">
	<link rel="stylesheet" href="css/jquery.fileupload-ui.css">
	<link rel="stylesheet" href="css/aaa.css">
  </head>
  
  <body>
  <!--header-->
		<div class="headder">
			<nav class="navbar navbar-default">
				<div class="collapse navbar-collapse"
					id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav navbar-right">
						<li><a href="userController/index">首页</a></li>
						<li><a class="active">上传</a></li>
					</ul>
				</div>
			</nav>
			<div class="logo">
				<a class="navbar-brand" href="userController/index">
				<img src="images/logo.png"></a>
			</div>
			<div class="header-right-text">
				<h3>
					Call Us:<span>(800)</span><i>1444 000 4040</i>
				</h3>
			</div>
			<div class="header-right-text  collapse navbar-collapse">
				<ul class="nav navbar-nav navbar-right" >
		        	<li class="dropdown">
			            <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">${sessionScope.user.name } <span class="caret"></span></a>
			            <ul class="dropdown-menu" style="margin-top:20px;background-color:#53BFE8">
				            <li style="color:black"><a href="centerController/uploadHistory" >个人中心</a></li>
				            <li class="divider"></li>
				            <li ><a href="userController/logout?email=${sessionScope.user.email }">注销</a></li>
		               </ul>
	               </li>
              </ul>
      		</div>
			<div class="clearfix"></div>
		</div>
	<div class="gallery" id="gallery">
	    <div class="container">
	    	<h6>三维图形的上传</h6>
			<form id="modelDetail" action="modelController/saveModel" method="POST" enctype="multipart/form-data">
				<fieldset>
				<legend>模型信息</legend>
				<div class="input-group input-group-lg">
		                <span class="input-group-addon" id="sizing-addon3">模型名称：</span>
		                <input name="modelName"  type="text" class="form-control" placeholder="模型名称" required>
		                <span class="input-group-addon" style="color:red"></span>
		          </div>
		          <br>
		          <div class="input-group input-group-lg">
		                <span  class="col-md-2 ">缩略图文件：</span>
						<div class="col-md-9">
							<div class="col-md-5 form-group">
								<span class="btn btn-success fileinput-button" >
						                <i class="glyphicon glyphicon-plus"></i>
						                <span>选择图片</span>
									<input id="picFileId" class="form-control" name="picFileName" type="file" onchange="uploadImg()" required >
									</span> 
							</div>
							<div class="col-md-5 form-group">
								<div style="position: relative;" id="imgArea"></div>
								<div>图片显示区域</div>
							</div>
						</div>
		          </div>
		          <br>
		          <div class="input-group input-group-lg">
		          		<span class="input-group-addon" id="sizing-addon3">模型描述：</span>
							<textarea id="remark" name="description" rows="20" cols="80" class="form-control" placeholder="请输入模型信息描述" required style="height:150px;"></textarea>
						<span class="input-group-addon" style="color:red"></span>
					</div>
		            <div class="input-group input-group-lg">
		          		<input id="tmpDirs" type="hidden" name="tmpDirs" value="${tmpDirs}">
		          </div>
		          </fieldset>
		        <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
		        <br><br>
		        <fieldset id="fileupload" class="form-horizontal" role="form">
				<legend>文件上传</legend>
		        <div class="row fileupload-buttonbar">
		            <div class="col-lg-7">
		                <!-- The fileinput-button span is used to style the file input field as button -->
		                <span class="btn btn-success fileinput-button">
		                    <i class="glyphicon glyphicon-plus"></i>
		                    <span>添加文件</span>
		                    <input type="file" name="files[]" multiple>
		                </span>
		                <button type="submit" class="btn btn-primary start">
		                    <i class="glyphicon glyphicon-upload"></i>
		                    <span>批量上传</span>
		                </button>
		                <button type="reset" class="btn btn-warning cancel">
		                    <i class="glyphicon glyphicon-ban-circle"></i>
		                    <span>取消上传</span>
		                </button>
		                <button type="button" class="btn btn-danger delete">
		                    <i class="glyphicon glyphicon-trash"></i>
		                    <span>批量删除</span>
		                </button>
		                <input type="checkbox" class="toggle">
		                <!-- The global file processing state -->
		                <span class="fileupload-process"></span>
		            </div>
		            <!-- The global progress state -->
		            <div class="col-lg-5 fileupload-progress fade">
		                <!-- The global progress bar -->
		                <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
		                    <div class="progress-bar progress-bar-success" style="width:0%;"></div>
		                </div>
		                <!-- The extended global progress state -->
		                <div class="progress-extended">&nbsp;</div>
		            </div>
		        </div>
		        <!-- The table listing the files available for upload/download -->
		         <table role="presentation" class="table table-striped"><tbody class="files"></tbody></table>
		         <div class="btn-group col-md-7 col-md-offset-4" role="group" >
		               <button id="saveModel" class="btn btn-primary input-lg col-md-7 disabled" type="submit">保存</button>
		          </div>
		          </fieldset>
		    </form>
	    </div>
	 </div>
<!-- The template to display files available for upload -->
<script id="template-upload" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-upload fade">
        <td>
            <span class="preview"></span>
        </td>
        <td>
            <p class="name">{%=file.name%}</p>
            <strong class="error text-danger"></strong>
        </td>
        <td>
            <p class="size">进行中...</p>
            <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="progress-bar progress-bar-success" style="width:0%;"></div></div>
        </td>
        <td>
            {% if (!i && !o.options.autoUpload) { %}
                <button class="btn btn-primary start" disabled>
                    <i class="glyphicon glyphicon-upload"></i>
                    <span>开始</span>
                </button>
            {% } %}
            {% if (!i) { %}
                <button class="btn btn-warning cancel">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>取消</span>
                </button>
            {% } %}
        </td>
    </tr>
{% } %}
</script>
<!-- The template to display files available for download -->
<script id="template-download" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        <td>
            <span class="preview">
                {% if (file.thumbnailUrl) { %}
                    <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" data-gallery><img src="{%=file.thumbnailUrl%}"></a>
                {% } %}
            </span>
        </td>
        <td>
            <p class="name">
                {% if (file.url) { %}
                    <a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" {%=file.thumbnailUrl?'data-gallery':''%}>{%=file.name%}</a>
                {% } else { %}
                    <span>{%=file.name%}</span>
                {% } %}
            </p>
            {% if (file.error) { %}
                <div><span class="label label-danger">错误</span> {%=file.error%}</div>
            {% } %}
        </td>
        <td>
            <span class="size">{%=o.formatFileSize(file.size)%}</span>
        </td>
        <td>
            {% if (file.deleteUrl) { %}
                <button class="btn btn-danger delete" data-type="{%=file.deleteType%}" data-url="{%=file.deleteUrl%}"{% if (file.deleteWithCredentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
                    <i class="glyphicon glyphicon-trash"></i>
                    <span>删除</span>
                </button>
                <input type="checkbox" name="delete" value="1" class="toggle">
            {% } else { %}
                <button class="btn btn-warning cancel">
                    <i class="glyphicon glyphicon-ban-circle"></i>
                    <span>取消</span>
                </button>
            {% } %}
        </td>
    </tr>
{% } %}
</script>
	<script src="js/jquery-1.11.1.min.js"></script>
	<script src="js/bootstrap.js"> </script>
	<script type="text/javascript" src="js/bootstrap-dialog.min.js"></script>
	<!-- 缺少jquery.form.js这个文件，回导致AJAX回调success方法不成功 -->
	<script type="text/javascript" src="js/jquery.form.js"></script>
	<script type="text/javascript" src="js/jquery.formtowizard.js"></script>
	<script type="text/javascript" src="js/jquery.validate.min.js"></script>
	<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
	<script src="js/vendor/jquery.ui.widget.js"></script>
	<!-- The Templates plugin is included to render the upload/download listings -->
	<script src="js/tmpl.min.js"></script>
	<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
	<script src="js/load-image.all.min.js"></script>
	<!-- The Canvas to Blob plugin is included for image resizing functionality -->
	<script src="js/canvas-to-blob.min.js"></script>
	<!-- Bootstrap JS is not required, but included for the responsive demo navigation -->
	<script src="js/bootstrap.min.js"></script>
	<!-- blueimp Gallery script -->
	<script src="js/jquery.blueimp-gallery.min.js"></script>
	<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
	<script src="js/jquery.iframe-transport.js"></script>
	<!-- The basic File Upload plugin -->
	<script src="js/jquery.fileupload.js"></script>
	<!-- The File Upload processing plugin -->
	<script src="js/jquery.fileupload-process.js"></script>
	<!-- The File Upload image preview & resize plugin -->
	<script src="js/jquery.fileupload-image.js"></script>
	<!-- The File Upload audio preview plugin -->
	<script src="js/jquery.fileupload-audio.js"></script>
	<!-- The File Upload video preview plugin -->
	<script src="js/jquery.fileupload-video.js"></script>
	<!-- The File Upload validation plugin -->
	<script src="js/jquery.fileupload-validate.js"></script>
	<!-- The File Upload user interface plugin -->
	<script src="js/jquery.fileupload-ui.js"></script>
	<!-- The main application script -->
	<script src="js/main.js"></script>
	<script type="text/javascript">
		$('.dropdown-toggle').dropdown();
	</script>
  </body>
</html>
