<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>登录界面</title>
	<link href="css/bootstrap.css" type="text/css" rel="stylesheet" media="all">
	<link rel="stylesheet" type="text/css" href="css/htmleaf-demo.css">
	<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
	<style type="text/css">
		.form-bg{
		    background: #00b4ef;
		}
		.form-horizontal{
		    background: #fff;
		    padding-bottom: 40px;
		    border-radius: 15px;
		    text-align: center;
		}
		.form-horizontal .heading{
		    display: block;
		    font-size: 35px;
		    font-weight: 700;
		    padding: 35px 0;
		    border-bottom: 1px solid #f0f0f0;
		    margin-bottom: 30px;
		}
		.form-horizontal .form-group{
		    padding: 0 40px;
		    margin: 0 0 25px 0;
		    position: relative;
		}
		.form-horizontal .form-control{
		    background: #f0f0f0;
		    border: none;
		    border-radius: 20px;
		    box-shadow: none;
		    padding: 0 20px 0 45px;
		    height: 40px;
		    transition: all 0.3s ease 0s;
		}
		.form-horizontal .form-control:focus{
		    background: #e0e0e0;
		    box-shadow: none;
		    outline: 0 none;
		}
		.form-horizontal .form-group i{
		    position: absolute;
		    top: 12px;
		    left: 60px;
		    font-size: 17px;
		    color: #c8c8c8;
		    transition : all 0.5s ease 0s;
		}
		.form-horizontal .form-control:focus + i{
		    color: #00b4ef;
		}
		.form-horizontal .fa-question-circle{
		    display: inline-block;
		    position: absolute;
		    top: 12px;
		    right: 60px;
		    font-size: 20px;
		    color: #808080;
		    transition: all 0.5s ease 0s;
		}
		.form-horizontal .fa-question-circle:hover{
		    color: #000;
		}
		.form-horizontal .main-checkbox{
		    float: left;
		    width: 20px;
		    height: 20px;
		    background: #11a3fc;
		    border-radius: 50%;
		    position: relative;
		    margin: 5px 0 0 5px;
		    border: 1px solid #11a3fc;
		}
		.form-horizontal .main-checkbox label{
		    width: 20px;
		    height: 20px;
		    position: absolute;
		    top: 0;
		    left: 0;
		    cursor: pointer;
		}
		.form-horizontal .main-checkbox label:after{
		    content: "";
		    width: 10px;
		    height: 5px;
		    position: absolute;
		    top: 5px;
		    left: 4px;
		    border: 3px solid #fff;
		    border-top: none;
		    border-right: none;
		    background: transparent;
		    opacity: 0;
		    -webkit-transform: rotate(-45deg);
		    transform: rotate(-45deg);
		}
		.form-horizontal .main-checkbox input[type=checkbox]{
		    visibility: hidden;
		}
		.form-horizontal .main-checkbox input[type=checkbox]:checked + label:after{
		    opacity: 1;
		}
		.form-horizontal .text{
		    float: left;
		    margin-left: 7px;
		    line-height: 20px;
		    padding-top: 5px;
		    text-transform: capitalize;
		}
		.form-horizontal .btn{
		    float: right;
		    font-size: 14px;
		    color: #fff;
		    background: #00b4ef;
		    border-radius: 30px;
		    padding: 10px 25px;
		    border: none;
		    text-transform: capitalize;
		    transition: all 0.5s ease 0s;
		}
		@media only screen and (max-width: 479px){
		    .form-horizontal .form-group{
		        padding: 0 25px;
		    }
		    .form-horizontal .form-group i{
		        left: 45px;
		    }
		    .form-horizontal .btn{
		        padding: 10px 20px;
		    }
		}
	</style>
	<!--[if IE]>
		<script src="http://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
	<![endif]-->
</head>
<body>
	<div class="htmleaf-container">
		<header class="htmleaf-header">
			<h1>注册界面 <span>三维模型的上传与下载平台</span></h1>
			<div class="htmleaf-links">
				<c:if test="${requestScope.message=='twoInputPasswordNotSame'}">
					<span style="color:red">两次密码输入不一致，请重新注册！</span>
				</c:if>
				<c:if test="${requestScope.message=='userAlreadyExits'}">
					<span style="color:red">用户已经存在，请重新注册！</span>
				</c:if>
				<c:if test="${requestScope.message=='codeError'}">
					<span style="color:red">验证码输入错误，请重新注册！</span>
				</c:if>
				<span style="color:red" id="error"></span>
			</div>
		</header>
		<div class="demo form-bg" style="padding: 20px 0;">
		        <div class="container">
		            <div class="row">
		                <div class="col-md-offset-3 col-md-6">
		                    <form class="form-horizontal" method="post" action="userController/save">
		                        <span class="heading">用户注册</span>
		                        <div class="form-group">
		                            <input type="email" class="form-control" id="inputEmail3" name="userEmail" placeholder="用户名邮箱" required>
		                            <i class="fa fa-user"></i>
		                        </div>
		                        <div class="form-group help">
		                            <input type="password" class="form-control" id="inputPassword3" name="userPassword" placeholder="密　码" required> 
		                            <i class="fa fa-lock"></i>
		                            <a href="#" class="fa fa-question-circle"></a>
		                        </div>
		                         <div class="form-group help">
		                            <input type="password" class="form-control" id="inputPassword4" name="userPassword2" placeholder="确认密码" required>
		                            <i class="fa fa-lock"></i>
		                            <a href="#" class="fa fa-question-circle"></a>
		                        </div>
		                         <div class="form-group">
		                            <input type="text" class="form-control" id="phone" name="userPhone" placeholder="用户手机号" required>
		                            <i class="fa fa-user"></i>
		                        </div>
		                         <div class="form-group">
		                            	<input style="width:120px;display:inline" type="text" class="form-control" id="authCode" name="authCode" placeholder="验证码" required >
								        <label><img type="image" src="" id="codeImage" onclick="chageCode()" title="图片看不清？点击重新得到验证码" style="cursor:pointer;"/></label>
								        <label><a onclick="chageCode()" style="color:#00b4ef;" class="text"><b>换一张</b></a></label>
		                            <!--这里img标签的src属性的值为后台实现图片验证码方法的请求地址-->
		                        </div>
		                        <div class="form-group">
		                            <span class="text">有账号？<a href="login.jsp" style="color:#00b4ef"><b>登录</b></a></span>
		                            <input type="submit" value="注册" class="btn btn-default">
		                         </div>
		                    </form>
		                </div>
		            </div>
		        </div>
		    </div>
	</div>
	<script type="text/javascript">
	$(function() {
		chageCode();
		
		$('#inputEmail3').blur(function() { 
			$.ajax({
				type:'post',
				url:'userController/validUserName',
				data:{userName:$('#inputEmail3').val()},
				success:function(data){
					if(data.status == 1){
						//$("#error").attr("value","用户已经存在");
						$("#error").text("用户已经存在，请重新注册！");
					}else{
						$("#error").text("");
					}
				},
				error:function(e){
					console.log("用户存在");
				},
	   	 		dataType:'json'
			});
		}); 
		
		$('#inputPassword3').blur(function() { 
			if($('#inputPassword3').val().length < 6 || $('#inputPassword3').val().length > 20){
				$("#error").text("密码个数在6-20个字符，请重新注册！");
			}else{
				$("#error").text("");
			}
		});
		
		
		$('#inputPassword4').blur(function() { 
			if($('#inputPassword3').val() != $('#inputPassword4').val()){
				$("#error").text("两次密码输入不一致，请重新注册！");
			}else{
				$("#error").text("");
			}
		});
		
		$('#phone').blur(function() { 
			var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
			if(!myreg.test($("#phone").val())) { 
				$("#error").text("手机号码输入有误，请重新注册！");
			}else{
				$("#error").text("");
			}
		});
		
		$('#authCode').blur(function() { 
			$.ajax({
				type:'post',
				url:'userController/getCode',
				data:{authCode:$('#authCode').val()},
				success:function(data){
					if(data.status != 1){
						$("#error").text("验证码输入错误，请重新注册！");
					}else{
						$("#error").text("");
					}
				},
				error:function(e){
					console.log("error");
				},
	   	 		dataType:'json'
			});
		}); 
		
    });
 	function chageCode(){
        $('#codeImage').attr('src','userController/authCode?abc='+Math.random());//链接后添加Math.random，确保每次产生新的验证码，避免缓存问题。
    }
	</script>
</body>
</html>