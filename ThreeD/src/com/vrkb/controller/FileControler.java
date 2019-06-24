package com.vrkb.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vrkb.bean.Download;
import com.vrkb.bean.ModelBean;
import com.vrkb.bean.User;
import com.vrkb.service.DownloadService;
import com.vrkb.service.ModelService;
import com.vrkb.utils.FileUtil;

@Controller
@RequestMapping("fileController")
public class FileControler {

	@Autowired
	private ModelService modelService;
	@Autowired
	private DownloadService downloadService;

	/**
	 * 1. 有 @ModelAttribute 标记的方法, 会在每个目标方法执行之前被 SpringMVC 调用! 
	 * 2. @ModelAttribute 注解也可以来修饰目标方法 POJO 类型的入参, 其 value 属性值有如下的作用:
	 * 1). SpringMVC 会使用 value 属性值在 implicitModel 中查找对应的对象, 若存在则会直接传入到目标方法的入参中.
	 * 2). SpringMVC 会一 value 为 key, POJO 类型的对象为 value, 存入到 request 中. 
	 */
	
	@ModelAttribute
	public void getModel(@RequestParam(value="modelId",required=false) Integer id,
			Map<String, Object> map){
		if(id !=null){
			ModelBean model= modelService.findById(id);
			map.put("model", model);
		}
	} 
	
	@RequestMapping("/listFile")  
	public String listFile(HttpSession session,HttpServletRequest request,@ModelAttribute("model")ModelBean model) {  
		// 上传位置  
		String path = session.getServletContext().getRealPath("/"+model.getPath()); // 设定文件保存的目录  
		// 存储要下载的文件名  
		List<String> files = FileUtil.getFileName(path);
		// 将Map集合发送到listfile.jsp页面进行显示  
		request.setAttribute("files", files);  
		request.setAttribute("model", model);
		return "listFile";  
	} 

	//下载
	@ResponseBody
	@RequestMapping(value="download",method=RequestMethod.POST)
	public ResponseEntity<byte[]> responseEntity(@ModelAttribute("model")ModelBean model,@RequestParam("fileName")String fileName,HttpServletRequest request,
			@RequestParam("modelId") int id,@RequestParam("user") int user){
		byte[] body = null;
		ServletContext servletContext = request.getSession().getServletContext();
		InputStream in = servletContext.getResourceAsStream("/"+model.getPath()+fileName);
		ResponseEntity<byte[]> response = null;
		Download download = null;
		try {
			download = downloadService.getDownloadById(id, user);
			body = new byte[in.available()];
			in.read(body);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment;filename="+fileName);
			HttpStatus statusCode = HttpStatus.OK;

			response = new ResponseEntity<byte[]>(body, headers, statusCode);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		model.setDownloadCount(model.getDownloadCount()+1);
		modelService.updateAll(model);
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		try {
			if(download == null){
				download = new Download();
				download.setCount(1);
				download.setModel(model);
				download.setUser(((User)request.getSession().getAttribute("user")).getId());
				download.setDate(sdf.format(new Date()));
				downloadService.save(download);
			}else{
				download.setCount(download.getCount()+1);
				download.setModel(model);
				download.setUser(((User)request.getSession().getAttribute("user")).getId());
				download.setDate(sdf.format(new Date()));
				downloadService.update(download);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
