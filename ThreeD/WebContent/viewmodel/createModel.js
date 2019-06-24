function Model3D(id,path,objName,mtlName,scale){
		var modelTemp = new Object;
		modelTemp.container = document.getElementById(id);
		if(id=="container"){
			modelTemp.width = window.innerWidth;
			modelTemp.height = window.innerHeight;
		}else{
			modelTemp.width = modelTemp.container.offsetWidth;
			modelTemp.height = modelTemp.container.offsetHeight;
		}
		modelTemp.camera = null;
		modelTemp.scene = null;
		modelTemp.renderer = null;
		modelTemp.controls = null;
		modelTemp.mouseX = 20;
		modelTemp.mouseY = 0;
		document.addEventListener( 'mousemove', modelTemp.onDocumentMouseMove, false );//���Ӷ�����¼��ļ���
		modelTemp.scope = this;
		modelTemp.onLoadComplete = function () {};
		modelTemp.path = path;
		modelTemp.objName = objName;
		modelTemp.mtlName = mtlName;
		modelTemp.scaleX = scale;
		modelTemp.scaleY = scale;
		modelTemp.scaleZ = scale;
		
    	modelTemp.init = function () {
			
	        modelTemp.camera = new THREE.PerspectiveCamera( 60, modelTemp.width / modelTemp.height, .01,1e4 );
	        modelTemp.camera.position.set( 100, 200, 500 );
	        modelTemp.scene = new THREE.Scene();
	
	        // LIGHTS
	
	        modelTemp.light = new THREE.DirectionalLight( 0xe3eef2, 1 );
	        modelTemp.light.position.set( 100, 140, 500 );//�ϲ���Դ
	        modelTemp.light.position.multiplyScalar( 1.1 );
	        modelTemp.light.color.setHSL( 0.6, 0.075, 1 );
	        modelTemp.scene.add( modelTemp.light );

	        modelTemp.light2 = new THREE.DirectionalLight( 0xe3eef2, 1 );
	        modelTemp.light2.position.set( 0, -1, 0 );//�ײ���Դ
	        modelTemp.scene.add( modelTemp.light2 );
	
	        modelTemp.light3 = new THREE.DirectionalLight( 0xe3eef2, 1 );
	        modelTemp.light3.position.set( -100, 140, -500 );//�ϲ���Դ
	        modelTemp.light3.position.multiplyScalar( 1.1 );
	        modelTemp.light3.color.setHSL( 0.6, 0.075, 1 );
	        modelTemp.scene.add( modelTemp.light3 );

	        // RENDERER
	
	        modelTemp.renderer = new THREE.WebGLRenderer( { antialias: true } );
	        modelTemp.renderer.setSize( modelTemp.width, modelTemp.height );
	        modelTemp.renderer.setClearColor( 0x70706D );
	        modelTemp.container.appendChild( modelTemp.renderer.domElement );
	        
	        // model
	        modelTemp.onProgress = function ( xhr ) {
	            if ( xhr.lengthComputable ) {
	                modelTemp.percentComplete = xhr.loaded / xhr.total * 100;
	               console.log( Math.round(modelTemp.percentComplete, 2) + '% downloaded' );
	            }
	        };

	        modelTemp.onError = function ( xhr ) { };
	
	        THREE.Loader.Handlers.add( /\.dds$/i, new THREE.DDSLoader() );
	
	        modelTemp.mtlLoader = new THREE.MTLLoader();
	        modelTemp.mtlLoader.setPath( modelTemp.path );
	        modelTemp.mtlLoader.load( modelTemp.mtlName, function( materials ) {  //��mtl�м��ص�����ͼ��UVչ��ͼ
	            materials.preload();
	            modelTemp.objLoader = new THREE.OBJLoader();
	            modelTemp.objLoader.setMaterials( materials );
	            modelTemp.objLoader.setPath( modelTemp.path );
	            modelTemp.objLoader.load( modelTemp.objName, function ( object ) { //��Ϊ��ָģ��
	               object.position.x = 0;
	               object.position.y = 0;
	               object.position.z = 0;
	               object.scale.x = modelTemp.scaleX;
	               object.scale.y = modelTemp.scaleY;
	               object.scale.z = modelTemp.scaleZ;
	               //object.children[0].geometry.computeBoundingBox();
	              // object.children[0].geometry.center();
	               modelTemp.scene.add( object );
	            }, modelTemp.onProgress, modelTemp.onError );
	        });
			
	        window.addEventListener( 'resize', modelTemp.onWindowResize, false );//ҳ�洰��ļ���
	
	        //��ת����
	        modelTemp.controls = new THREE.OrbitControls( modelTemp.camera, modelTemp.renderer.domElement );
	        modelTemp.controls.center.set( 0, 0, 0);
	
	        modelTemp.controls.addEventListener( 'change', modelTemp.render );//����Ⱦ�����½��м���

    	};


	    modelTemp.checkLoadingComplete = function () {
	        modelTemp.scope.loadCounter -= 1;
	        if ( scope.loadCounter === 0 ) modelTemp.scope.onLoadComplete();
	    };


	    modelTemp.onWindowResize = function () {
	
	        modelTemp.camera.aspect = modelTemp.width / modelTemp.height;
	        modelTemp.camera.updateProjectionMatrix();
	
	        modelTemp.renderer.setSize( modelTemp.width, modelTemp.height );
	
	    };

	    modelTemp.onDocumentMouseMove = function ( event ) {
	        modelTemp.mouseX = ( event.clientX - modelTemp.width ) * 10;
	        modelTemp.mouseY = ( event.clientY - modelTemp.height ) * 10;
	    };

	    modelTemp.animate = function () {
	        requestAnimationFrame( modelTemp.animate );
	        modelTemp.controls.update();
	        modelTemp.render();
	    };

	    modelTemp.render = function () {
	        modelTemp.renderer.render( modelTemp.scene, modelTemp.camera );
	    };     
	    
	    return modelTemp;
	}
