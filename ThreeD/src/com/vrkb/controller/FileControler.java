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
	 * 1. �� @ModelAttribute ��ǵķ���, ����ÿ��Ŀ�귽��ִ��֮ǰ�� SpringMVC ����! 
	 * 2. @ModelAttribute ע��Ҳ����������Ŀ�귽�� POJO ���͵����, �� value ����ֵ�����µ�����:
	 * 1). SpringMVC ��ʹ�� value ����ֵ�� implicitModel �в��Ҷ�Ӧ�Ķ���, ���������ֱ�Ӵ��뵽Ŀ�귽���������.
	 * 2). SpringMVC ��һ value Ϊ key, POJO ���͵Ķ���Ϊ value, ���뵽 request ��. 
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
		// �ϴ�λ��  
		String path = session.getServletContext().getRealPath("/"+model.getPath()); // �趨�ļ������Ŀ¼  
		// �洢Ҫ���ص��ļ���  
		List<String> files = FileUtil.getFileName(path);
		// ��Map���Ϸ��͵�listfile.jspҳ�������ʾ  
		request.setAttribute("files", files);  
		request.setAttribute("model", model);
		return "listFile";  
	} 

	//����
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
