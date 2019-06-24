<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<title>个人中心</title>
	<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
    <meta name="viewport" content="width=device-width" />


    <!-- Bootstrap core CSS     -->
    <link href="css/bootstrap.min.css" rel="stylesheet" />


    <!--  Light Bootstrap Table core CSS    -->
    <link href="css/light-bootstrap-dashboard.css" rel="stylesheet"/>

    <!--     Fonts and icons     -->
    <link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <link href='http://fonts.googleapis.com/css?family=Roboto:400,700,300' rel='stylesheet' type='text/css'>
    <link href="css/pe-icon-7-stroke.css" rel="stylesheet" />
</head>
<body>

<div class="wrapper">
    <div class="sidebar" data-color="azure" data-image="images/sidebar-5.jpg">

    <!--   you can change the color of the sidebar using: data-color="blue | azure | green | orange | red | purple" -->


    	<div class="sidebar-wrapper">
            <div class="logo">
                <a class="simple-text">
                    个人中心
                </a>
            </div>

            <ul class="nav">
            
             	<li>
                    <a href="centerController/uploadHistory">
                        <i class="pe-7s-cloud-upload"></i>
                        <p>我的上传</p>
                    </a>
                </li>
                
                 <li>
                    <a href="centerController/downloadHistory">
                        <i class="pe-7s-cloud-download"></i>
                        <p>我的下载</p>
                    </a>
                </li>
                
                <c:if test="${sessionScope.user.grade=='admin' }">
                	 <li class="active">
                    <a href="auditController/audit">
                        <i class="pe-7s-look"></i>
                        <p>我的审核</p>
                    </a>
                </li>
                
                <li>
                    <a href="auditController/auditUser">
                        <i class="pe-7s-note2"></i>
                        <p>用户级别设置</p>
                    </a>
                </li>
                
                <li>
                    <a href="centerController/dashboard">
                        <i class="pe-7s-note2"></i>
                        <p>报表</p>
                    </a>
                </li>
                
                </c:if>
                
            </ul>
    	</div>
    </div>

    <div class="main-panel">
        <nav class="navbar navbar-default navbar-fixed" >
            <div class="container-fluid">
                
                <div class="collapse navbar-collapse">
                    
					<ul class="nav navbar-nav navbar-left">
                   		<li><a href="userController/index">首页</a></li>
                   	</ul>
					
                    <ul class="nav navbar-nav navbar-right">
                        
                         <li class="dropdown">
                              <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                    ${sessionScope.user.email }
                                    <b class="caret"></b>
                              </a>
                              <ul class="dropdown-menu">
                                <li><a href="userController/logout?email=${sessionScope.user.email }">注销</a></li>
                              </ul>
                        </li>
                      
                    </ul>
                </div>
            </div>
        </nav>


        <div class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="header">
                                <h4 class="title">我的审核</h4>
                                <p class="category">审核的历史记录</p>
                            </div>
                            <div class="content table-responsive table-full-width">
                                <table class="table table-hover table-striped">
                                    <thead>
                                        <th>编号</th>
                                    	<th>模型名称</th>
                                    	<th>详情</th>
                                    	<th>操作</th>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${requestScope.audits}" var="audit">
                                    	<tr>
                                        	<td>${audit.id}</td>
                                        	<td>${audit.upload.model.name}</td>
                                        	<td><a href="modelController/showObject?id=${audit.upload.model.id}">详情</a></td>
                                        	<td>
                                        	<c:if test="${audit.type=='yes' }">
                                        		已通过
                                        	</c:if>
                                        	<c:if test="${audit.type=='no' }">
                                        		未通过
                                        	</c:if>
                                        	<c:if test="${audit.type=='none' }">
                                        		<form action="auditController/check">
	                                        		<input type="hidden" name="id" value="${audit.id}">
	                                        		<input type="hidden" name="checkType" value="yes">
	                                        		<input type="submit" class="btn btn-info" value="通过">
	                                        	</form>
	                                        	<form action="auditController/check">
	                                        		<input type="hidden" name="id" value="${audit.id}">
	                                        		<input type="hidden" name="checkType" value="no">
	                                        		<input type="submit" class="btn btn-info" value="不通过">
	                                        	</form>
                                        	</c:if>
                                        		
                                        	</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


</body>

   <!--   Core JS Files   -->
    <script src="js/jquery-1.11.1.min.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
</html>