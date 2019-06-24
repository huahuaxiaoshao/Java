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
    
    <title>page</title>
    
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link href="css/bootstrap.css" type="text/css" rel="stylesheet" media="all">
	<link href="css/style.css" type="text/css" rel="stylesheet" media="all">
	<link href="css/gallery.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="css/aaa.css">
	<script src="js/jquery-1.11.1.min.js"></script>
	<script src="js/bootstrap.js"> </script>
	<style type="text/css">
		.center-block{
			padding-left: 40%;
		}
		.model-container{
			width: 350px;
			height: 220px;
		}
		.view{
			height: 260px;
		}
	</style>
  </head>
  <body>
		<!--header-->
		<div class="headder">
			<nav class="navbar navbar-default">
				<div class="collapse navbar-collapse"
					id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav navbar-right">
						<li><a href="userController/index">首页</a></li>
						<li><a class="active">更多</a></li>
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
				            <li><a href="centerController/uploadHistory" >个人中心</a></li>
				            <li class="divider"></li>
				            <li><a href="userController/logout?email=${sessionScope.user.email }">注销</a></li>
		               </ul>
	               </li>
              </ul>
      		</div>
			<div class="clearfix"></div>
		</div>
  
    <!--start-gallery-->
	<div class="gallery" id="gallery">
		<div class="container">
			<h6>三维图形<small><a href="modelController/upload">(上传模型)</a></small></h6>
			<div class="gallery-bottom" >
			<c:forEach items="${requestScope.models }" var="model">
				 <div class="view view-ninth">
		                <a href="modelController/showObject?id=${model.id }" class="b-link-stripe b-animate-go  swipebox"  title="${mode.name }">
			                <img src="${model.picPath }" alt="" class="img-responsive">
			                <div class="mask mask-1"></div>
			                <div class="mask mask-2"> </div>
			                <div class="content">
			                	<h2>${model.name }</h2>
				                <p>${model.description }</p>
				                <a  class="info">Read More</a>
			                </div>
		        </div>
			</c:forEach>
			</div>
		</div>
	</div>
	<div class="container">
			<nav aria-label="Page navigation" class="center-block">
				  <ul class="pagination">
				    <li>
				    	<c:if test="${currentPage == 1}">
					      <a href="#"  hrearia-label="Previous">
					        <span aria-hidden="true">&laquo;</span>
					      </a>
				      	</c:if>
				      	
				      	 <c:if test="${currentPage != 1}">
				      	 	<a href="pageController/listModel?page=${currentPage-1}" aria-label="Previous">
					        	<span aria-hidden="true">&laquo;</span>
					      	</a>
       		 			 </c:if>
				    </li>
				    
				    <c:if test="${currentPage == 1}">
        				<li class="active"><a >1</a></li>
        			</c:if>
        			
			        <c:if test="${currentPage != 1}">
			        	<li><a href="pageController/listModel?page=1">1</a></li>
			        </c:if>
			        
			        
			         <%
                 int pageTimes = (Integer)request.getAttribute("pageTimes");
                 for(int i=1;i<pageTimes;i++)
                 {
                     request.setAttribute("page", i+1);
         		%>            
	         <c:if test="${currentPage == page}">
	             <li class="active"><a ><%=i+1%></a></li>     
	         </c:if>
	         <c:if test="${currentPage != page}">
	         	  <li><a href="pageController/listModel?page=<%=i+1%>"><%=i+1%></a></li>
	         </c:if>
	         <%} %>
        
		        <c:if test="${currentPage == pageTimes}">
		        	<li>
				      <a  aria-label="Next">
				        <span aria-hidden="true">&raquo;</span>
				      </a>
					</li>
		        </c:if>
		        
		        <c:if test="${currentPage != pageTimes}">
		        	<li>
					      <a href="pageController/listModel?page=${currentPage+1}" aria-label="Next">
					        <span aria-hidden="true">&raquo;</span>
					      </a>
					</li>
		        </c:if>
			        
				  </ul>
			</nav>
		</div>
  </body>
</html>