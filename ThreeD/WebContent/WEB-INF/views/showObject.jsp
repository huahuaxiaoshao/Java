<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
  <head>
    <base href="<%=basePath%>">
    <title>show</title>
	<meta charset="utf-8">
	<link href="css/bootstrap.min.css" rel="stylesheet" />
	<link href="css/bootstrap-dialog.min.css" type="text/css" rel="stylesheet" media="all">
	<script src="js/jquery-1.11.1.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/bootstrap-dialog.min.js"></script>
    <meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
    <style>
        body {
            color: #000;
            font-family:Monospace;
            font-size:13px;
            text-align:center;

            background-color: #fff;
            margin: 0px;
            overflow: hidden;
        }
         #info {
            position: absolute;
            top: 0px;
        }

        a {
            color: #0af;
        }

        .logo a{
        	color:  #ffffff;
        }
        .lanren{width:450px; border: 1px solid #2BB7E4; height: 25px;margin:100px auto;}
		#bar{ display: block;background: #2BB7E4; float: left; height: 100%;widtext-align: center;line-height: 25px; color: #FFF;}
		#container{
			background-repeat: no-repeat;
		}
    </style>
</head>

<body>

<div id="container">
	<div class="lanren" id="lanren">
		<div id="bar" style="width:1%;"></div>
	</div>
</div>

<div id="info" class="col-md-offset-9" style="margin-top:20px">
    <div class="logo" >
    	
        <a style="height: 20px;" href="fileController/listFile?modelId=${requestScope.model.id}">模型下载</a>
    
    	<div id="updateScale" class="form-inline" method="get" style="margin-top:20px">
       		<input type="hidden" name="id" id="id" value="${requestScope.model.id}">
       		<c:if test="${sessionScope.user.grade=='admin' }">
       			<div class="form-group">
				    <label style="color:#5BC0DE">大小：</label>
	        		<input style="width: 130px;" type="text" id="scale" class="form-control " name="scale" >
			    </div>
	        	<button  onclick="updateScale()" class="btn btn-info">更新</button>
	        	<button  onclick="confirmScale()" class="btn btn-info">确认</button>
       		</c:if>
       		
        </div>
    </div>
</div>

<script src="viewmodel/three.min-r84.js"></script>
<script src="viewmodel/DDSLoader.js"></script>
<script src="viewmodel/MTLLoader.js"></script>
<script src="viewmodel/OBJLoader.js"></script>
<script src="viewmodel/OrbitControls.js"></script>
<script src="viewmodel/stats.min.js"></script>
<script src="viewmodel/CopyShader.js"></script>
<script src="viewmodel/EffectComposer.js"></script>
<script src="viewmodel/CubeTexturePass.js"></script>
<script src="viewmodel/ShaderPass.js"></script>
<script src="viewmodel/RenderPass.js"></script>
<script src="viewmodel/ClearPass.js"></script>

<script type="text/javascript">
	
</script>
<script>
	
    var SCREEN_WIDTH = window.innerWidth;
    var SCREEN_HEIGHT = window.innerHeight;
    var container,stats;
    var test,obj;
    var camera, scene;
    var renderer,controls;
    var mesh,zoom,rotationFlag=1;
    var cubeTexturePassP,copyPass,renderPass,clearPass;

    var mouseX = 0, mouseY = 0;

    var windowHalfX = window.innerWidth / 2;
    var windowHalfY = window.innerHeight / 2;

    var barElement = document.getElementById("bar");
	var lanrenElement = document.getElementById("lanren");
	var params = {
		clearPass :true,
		clearColor: 'white',
		clearAlpha: 1.0,

		cubeTexturePass: true,
		cubeTexturePassOpacity: 1.0,

		renderPass: true
	};
	
    container = document.getElementById( 'container' );
	
    init();//初始化
    animate();//渲染帧数的迭代
    
    function init() {
        camera = new THREE.PerspectiveCamera( 60, window.innerWidth / window.innerHeight, .01,1e4 );
        camera.position.set( -200, 20, 300 );
        scene = new THREE.Scene();
        
        // LIGHTS
        
         var light = new THREE.DirectionalLight( 0xff5808, 0.5 );
        light.position.set( 100, 140, 500 );//上部光源
        light.position.multiplyScalar( 1.1 );
        light.color.setHSL( 0.6, 0.075, 1 );
        scene.add( light );

        var light2 = new THREE.DirectionalLight( 0xff5808, 0.5 );
        light2.position.set( 0, -1, 0 );//底部光源
        scene.add( light2 );

        var light3 = new THREE.DirectionalLight( 0xff5808, 0.5 );
        light3.position.set( -100, 140, -500 );//上部光源
        light3.position.multiplyScalar( 1.1 );
        light3.color.setHSL( 0.6, 0.075, 1 );
        scene.add( light3 );
		
		
		var light4 = new THREE.AmbientLight( 0xff5808, 1 );
		light4.position.multiplyScalar( 1.1 );
        light4.color.setHSL( 0.6, 0.075, 1 );
        scene.add( light4 );//环境光，可以照亮各个角度
        
        stats = new Stats();//渲染状态

        // RENDERER
        renderer = new THREE.WebGLRenderer( { antialias: true } );
        renderer.setSize( SCREEN_WIDTH, SCREEN_HEIGHT );
        //renderer.setClearColor( 0x70706D );设置背景颜色

     // model
        var onProgressObj = function ( xhr ) {
            if ( xhr.lengthComputable ) {
                var percentComplete = xhr.loaded / xhr.total * 100;
                console.log( Math.round(percentComplete, 2) + '% downloaded' );
                barElement.style.width = Math.round(percentComplete, 2) + "%"; 
                barElement.innerHTML = barElement.style.width ;
                if(parseInt(barElement.style.width) == 100){
                	barElement.innerHTML = "模型下载完成，正在渲染...";
				} 
            }
        };
		
		var onProgressMtl = function ( xhr ) {
            if ( xhr.lengthComputable ) {
                var percentComplete = xhr.loaded / xhr.total * 100;
                console.log( Math.round(percentComplete, 2) + '% downloaded' );
            }
        };

        var onErrorObj = function (xhr) {
			console.log("Obj文件加载出现问题");
		};
		
		var onErrorMtl = function (xhr) {
			console.log("Mtl文件加载出现问题");
		};
        THREE.Loader.Handlers.add(/\.dds$/i, new THREE.DDSLoader());
	        var mtlLoader = new THREE.MTLLoader();
			mtlLoader.setPath("${requestScope.model.path}");
			mtlLoader.load("${requestScope.model.mtlName}", function(materials) {
				materials.preload();
				var objLoader = new THREE.OBJLoader();
				objLoader.setMaterials(materials);
				objLoader.setPath("${requestScope.model.path}");
				objLoader.load("${requestScope.model.objName}", function ( object ) { 
					object.position.set(0,0,0);
					if("${requestScope.model.scale}" == null || "${requestScope.model.scale}" == "0.0")
						zoom = 1;
					else{
						zoom = 	"${requestScope.model.scale}";
					}
					object.scale.set(zoom,zoom,zoom);
				    object.traverse( function( node ) {
					    if( node.material instanceof THREE.MeshPhongMaterial) {
					        node.material.side = THREE.DoubleSide;
					    }else if(node.material instanceof THREE.MultiMaterial){
					    	for(var i = 0;i < node.material.materials.length;i++){
					    		node.material.materials[i].side = THREE.DoubleSide;
					    	}
					    }else if(node.material instanceof THREE.Mesh){
					    	node.material.side = THREE.DoubleSide;
					    }
					});
					//object.children[0].geometry.computeBoundingBox();
					//object.children[0].geometry.center();
					if(parseInt(barElement.style.width) == 100){
			        	clearInterval(bar);
			    		barElement.style.display = "none";
			    		lanrenElement.style.display = "none";
			    		obj = object;
			    		scene.add(object);
			    		container.appendChild( stats.dom );
			    		container.appendChild( renderer.domElement );
					}
	            }, onProgressObj, onErrorObj);
	        },onProgressMtl,onErrorMtl);
		
		//坐标的显示并确定坐标系的长度
        //var axisHelper = new THREE.AxisHelper(100);
        //scene.add( axisHelper );
		
		// postprocessing
		var genCubeUrls = function( prefix, postfix ) {
			return [
				prefix + 'px' + postfix, prefix + 'nx' + postfix,
				prefix + 'py' + postfix, prefix + 'ny' + postfix,
				prefix + 'pz' + postfix, prefix + 'nz' + postfix
			];
		};

		composer = new THREE.EffectComposer( renderer );

		clearPass = new THREE.ClearPass( params.clearColor, params.clearAlpha );//加载的物体拖动时，避免出现物体出现类似于花屏的样子
		composer.addPass( clearPass );

		cubeTexturePassP = new THREE.CubeTexturePass( camera );
		composer.addPass( cubeTexturePassP );

		var ldrUrls = genCubeUrls( "textures/Park3Med/", ".jpg" );
		new THREE.CubeTextureLoader().load( ldrUrls, function ( ldrCubeMap ) {
			cubeTexturePassP.envMap = ldrCubeMap;
			console.log( "loaded envmap");
		});

		renderPass = new THREE.RenderPass( scene, camera );
		renderPass.clear = false;
		composer.addPass( renderPass );

		copyPass = new THREE.ShaderPass( THREE.CopyShader );
		copyPass.renderToScreen = true;
		composer.addPass( copyPass );
		
        //旋转控制
        controls = new THREE.OrbitControls( camera, renderer.domElement );
        controls.center.set(0, 0, 0);
        window.addEventListener('resize', onWindowResize, false );//页面窗体的监听
        document.addEventListener('mousedown', onDocumentMouseDown, false );//页面窗体的监听
        
    }

    function onDocumentMouseDown(){
    	if(rotationFlag == 1)
    		rotationFlag = 0;
    }
    
    
    function onWindowResize() {
    	var width = window.innerWidth;
		var height = window.innerHeight;
        camera.aspect = width / height;
        camera.updateProjectionMatrix();
        renderer.setSize( width, height );
        var pixelRatio = renderer.getPixelRatio();
		var newWidth  = Math.floor( width / pixelRatio );
		var newHeight = Math.floor( height / pixelRatio );
		composer.setSize( newWidth, newHeight );
		composer.render();
    }

    function animate() {
    	try{
	        requestAnimationFrame( animate );
	        stats.begin();
	        if(rotationFlag == 1){
				var timer = Date.now()*0.0005;
				camera.position.x = Math.sin(timer)*100;
				camera.position.z = Math.cos(timer)*100;
			}
	        camera.lookAt(new THREE.Vector3(0, 0, 0));
			camera.updateMatrixWorld( true );
			cubeTexturePassP.enabled = params.cubeTexturePass;
			cubeTexturePassP.opacity = params.cubeTexturePassOpacity;
			renderPass.enabled = params.renderPass;
			composer.render( );
			stats.end();
    	}catch(e){
    		console.log("渲染异常！");
    	}
    }

    function log( text ) {
        var e = document.getElementById( "log" );
        e.innerHTML = text + "<br/>" + e.innerHTML;
    }
    
</script>
<script type="text/javascript">
	function updateScale(){
		var scale = document.getElementById("scale").value;
		obj.scale.set(scale,scale,scale);
	}
	function confirmScale(){
		var scale = document.getElementById("scale").value;
		var id = document.getElementById("id").value;
		$.ajax({
			type:'post',
			url:'modelController/changeScale',
			data:{scale:scale,id:id},
			success:function(data){
				if(data.status == 1){
					BootstrapDialog.alert({title:data.message});
				}else{
					BootstrapDialog.alert({title:data.message});
				}
			},
			error:function(e){
				console.log("用户存在");
			},
   	 		dataType:'json'
		});
	}
	
</script>
</body>
</html>