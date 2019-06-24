<i><%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>listFile</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link href="css/bootstrap.css" type="text/css" rel="stylesheet" media="all">
	<link href="css/style.css" type="text/css" rel="stylesheet" media="all">
	<link rel="stylesheet" href="css/aaa.css">
	<script src="js/jquery-1.11.1.min.js"></script>
	<script src="js/bootstrap.js"> </script>
  </head>
  
  <body>
  <!--header-->
		<div class="headder">
			<nav class="navbar navbar-default">
				<div class="collapse navbar-collapse"
					id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav navbar-right">
						<li><a href="userController/index">首页</a></li>
						<li><a class="active">下载</a></li>
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
	    	<h6>三维图形的下载</h6>
	    	<h6><small>下载了${model.downloadCount}次</small></h6>
    		<table class="table table-hover table-bordered">
		    	<tr class="active input-lg">
		    		<th>模型名称</th>
		    		<th>文件名</th>
		    		<th>操作选项</th>
		    	</tr>
				<c:forEach items="${requestScope.files}" var="file">
					<tr>
						<td>${requestScope.model.name}</td>
						<td>${file}</td>
						<td>
							<form action="fileController/download" method="post">
								<input type="hidden" name="modelId" value="${requestScope.model.id}">
								<input type="hidden" name="user" value="${sessionScope.user.id}">
								<input type="hidden" name="fileName" value='${file}'>
								<input type="submit" class="btn btn-info"value="下载">
							</form>
						</td>
					</tr>
				</c:forEach>
			</table>
    	</div> 
    </div>
  </body>
</html>
