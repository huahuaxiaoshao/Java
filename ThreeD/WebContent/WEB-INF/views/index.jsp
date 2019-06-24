<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>首页</title>
<link href="css/bootstrap.css" type="text/css" rel="stylesheet" media="all">
<link href="css/style.css" type="text/css" rel="stylesheet" media="all">
<link href="css/gallery.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="css/swipebox.css">
<link rel="stylesheet" href="css/aaa.css">
<!-- Custom Theme files -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="keywords" content="3d 3d模型">
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
<!-- //Custom Theme files -->
<!-- //js -->
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.js"> </script>
<script src="js/jquery.swipebox.min.js"></script>
<script type="text/javascript" src="js/move-top.js"></script>
<script type="text/javascript" src="js/easing.js"></script>
<!--fonts-->
<link
	href="http://fonts.googleapis.com/css?family=Titillium+Web:400,200,200italic,300,300italic,400italic,600,600italic,700,700italic,900"
	rel="stylesheet" type="text/css">
<link
	href="http://fonts.googleapis.com/css?family=Signika+Negative:300,400,600,700"
	rel="stylesheet" type="text/css">
<link
	href="http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,300,600,700,800,400"
	rel="stylesheet" type="text/css">
<!--/fonts-->
<!-- start-smoth-scrolling-->
<script type="text/javascript">

		jQuery(document).ready(function($) {
		
			$(".scroll").click(function(event){		
				event.preventDefault();
				$('html,body').animate({scrollTop:$(this.hash).offset().top},1000);
			});
			
			$.ajax({
				type:'post',
				url:'modelController/findModels',
				data:{size:9},
				success:function(data){
					var json = eval(data);
		           	 $.each(json,function(key,val) {
						$("#models").append("<div class='view view-ninth'>"+
		                "<a href='modelController/showObject?id="+val.id+"' class='b-link-stripe b-animate-go  swipebox'  title='"+val.name+"'><img src='"+val.picPath+"' alt='' class='img-responsive'>"+
		                "<div class='mask mask-1'> </div>"+
		                "<div class='mask mask-2'> </div>"+
		                "<div class='content'><h2>"+val.name+"</h2>"+
		                "<p>"+val.description+"</p>"+
		                "<a href='#' class='info'>Read More</a></div></div>");
		            });
				},
	   	 		dataType:'json'
			});
		});
		
</script>
<!--//end-smoth-scrolling-->
</head>
<body>
	<!--banner-->
	<div class="banner">
		<!--header-->
		<div class="headder">
			<nav class="navbar navbar-default">
				<!-- Brand and toggle get grouped for better mobile display -->
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed"
						data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
						<span class="sr-only">Toggle navigation</span> <span
							class="icon-bar"> </span> <span class="icon-bar"> </span> <span
							class="icon-bar"> </span>
					</button>
				</div>
				<!-- Collect the nav links, forms, and other content for toggling -->
				<div class="collapse navbar-collapse"
					id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav navbar-right">
						<li><a href="userController/index" class="active">首页</a></li>
						<li><a href="#gallery" class="scroll">三维图形</a></li>
						<li><a href="#about" class="scroll">关于我们</a></li>
					</ul>
				</div>
			</nav>
			<div class="logo">
				<a class="navbar-brand" href="userController/index"><img
					src="images/logo.png"></a>
			</div>
			<div class="header-right-text">
				<h3>
					Call Us:<span>(800)</span><i>1444 000 4040</i>
				</h3>
			</div>
			<!-- <div class="search-bar">
				<input type="text" value="Search" onFocus="this.value = '';"
					onBlur="if (this.value == '') {this.value = 'Search';}"> <input
					type="submit" value="">
			</div> -->
			<div class="header-right-text  collapse navbar-collapse">
				<ul class="nav navbar-nav navbar-right" >
		        	<li class="dropdown">
			            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
			            	${sessionScope.user.name}
			            <span class="caret"></span></a>
			            <ul class="dropdown-menu" style="margin-top:20px;background-color:#53BFE8">
				            <li style="color:black"><a href="centerController/uploadHistory" >个人中心</a></li>
				            <li class="divider"></li>
				            <li ><a href="userController/logout">注销</a></li>
		               </ul>
	               </li>
              </ul>
      		</div>
			<div class="clearfix"></div>
		</div>
		<!--//header-->
		<h1>IMMOVABLE</h1>
	</div>
	<!--start-gallery-->
	<div class="gallery" id="gallery">
		<div class="container">
			<h6>三维图形<small><a href="modelController/upload">(上传模型,</a></small><small><a href="pageController/listModel?page=1">更多)</a></small></h6>
			<div class="gallery-bottom" id="models">
				
			</div>
			
		</div>
	</div>
	<!--about-->
	<div class="about" id="about">
		<div class="container">
			<h6>关于我们</h6>
			<div class="about-grids">
				<div class="col-md-6 about-info">
					<img src="images/hebut.jpg" alt="河北工业大学">
				</div>
				<div class="col-md-6 about-info">
					<h3>河工大人</h3>
					<p>河工大人是勇敢的、坚强的、肯吃苦、有创新能力、志同道和的一群人。</p>
					<p class="ab-text">男生帅气，女生美丽。</p>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
	</div>
	<!--/abouot-->
	<script type="text/javascript">
			jQuery(function($) {
				$(".swipebox").swipebox();
			});
</script>
	<div class="footer">
		<div class="container">
			<p>
				Copyright &copy; 2015.Company name All rights reserved.More
				Templates <a href="#" target="_blank" title="三维平台">三维平台</a> -
				Collect from <a href="#" title="三维平台" target="_blank">三维平台</a>
			</p>
		</div>
	</div>
	<!--//contact-->
	<script type="text/javascript">
			$(document).ready(function() {
				$().UItoTop({ easingType: 'easeOutQuart' });
			});
		</script>
	<a href="#" id="toTop" style="display: block;"> <span
		id="toTopHover" style="opacity: 1;"> </span></a>
	
</body>
</html> 