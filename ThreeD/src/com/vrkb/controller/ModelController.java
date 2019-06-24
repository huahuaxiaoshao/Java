package com.vrkb.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.vrkb.bean.Audit;
import com.vrkb.bean.ModelBean;
import com.vrkb.bean.Upload;
import com.vrkb.bean.User;
import com.vrkb.service.AuditService;
import com.vrkb.service.ModelService;
import com.vrkb.service.UploadService;
import com.vrkb.utils.CommonUtil;
import com.vrkb.utils.Constants;
import com.vrkb.utils.JsonResult;
import com.vrkb.utils.PropertiesUtil;

@Controller
@RequestMapping("modelController")
public class ModelController {

	@Autowired
	private ModelService modelService;
	@Autowired
	private UploadService uploadService;
	@Autowired
	private AuditService auditService;

	/**
	 * 1. 有 @ModelAttribute 标记的方法, 会在每个目标方法执行之前被 SpringMVC 调用! 
	 * 2. @ModelAttribute 注解也可以来修饰目标方法 POJO 类型的入参, 其 value 属性值有如下的作用:
	 * 1). SpringMVC 会使用 value 属性值在 implicitModel 中查找对应的对象, 若存在则会直接传入到目标方法的入参中.
	 * 2). SpringMVC 会一 value 为 key, POJO 类型的对象为 value, 存入到 request 中. 
	 */

	@ModelAttribute
	public void getModel(@RequestParam(value="id",required=false) Integer id,
			Map<String, Object> map){
		if(id !=null){
			ModelBean model= modelService.findById(id);
			map.put("model", model);
		}
	}

	@RequestMapping("showObject")
	public String show(@ModelAttribute("model")ModelBean model,HttpServletRequest request){
		request.setAttribute("model", model);
		return "showObject";
	}

	/**
	 * ajax请求不需要返回页面，只需要得到response中的数据即可，所以方法签名为void即可
	 * 
	 * @param request
	 * @param response 
	 */
	@RequestMapping(value = "findModels", method = RequestMethod.POST)
	public void ajaxDatas(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="size",required=false) String size) {
		String jsonResult = getJSONString(request,size);
		renderData(response, jsonResult);
	}
	

	private String getJSONString(HttpServletRequest request,String sizeStr) {
		int size = Integer.parseInt(sizeStr);
		List<ModelBean> list = modelService.getModels(size);
		Map<String, ModelBean> map = new HashMap<String, ModelBean>();
		//可以获得ajax请求中的参数
		for (int i = 0; i < list.size(); i++) {
			map.put(list.get(i).getId()+"", list.get(i));
		}
		JSONObject jsonObject = new JSONObject(map);
		String jsonResult = jsonObject.toString();
		return jsonResult;
	}

	/**
	 * 通过PrintWriter将响应数据写入response，ajax可以接受到这个数据
	 * 
	 * @param response
	 * @param data 
	 */
	private void renderData(HttpServletResponse response, String data) {
		PrintWriter printWriter = null;
		try {
			printWriter = response.getWriter();
			printWriter.print(data);
		} catch (IOException ex) {
			Logger.getLogger(ModelController.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (null != printWriter) {
				printWriter.flush();
				printWriter.close();
			}
		}
	}
	
	@RequestMapping("/upload")
	public String upload(HttpServletRequest request){
		request.setAttribute("tmpDirs", UUID.randomUUID().toString());
		return "upload";
	}
	
	@ResponseBody
	@RequestMapping(value="/changeScale",method=RequestMethod.POST)
	public JsonResult changeScale(@RequestParam("scale")String scale,@ModelAttribute("model")ModelBean model){
		JsonResult result = new JsonResult();
		model.setScale(Float.parseFloat(scale));
		int count = modelService.updateAll(model);
		if(count > 0){
			result.setStatus(1);
			result.setMessage("修改成功，已存入数据库！");
		}else{
			result.setStatus(0);
			result.setMessage("修改失败，请重新修改！");
		}
		return result;
	}

	@ResponseBody
	@RequestMapping("/saveModel")
	public JsonResult saveModel(@RequestParam("description") String description,@RequestParam("tmpDirs") String tmpDirs,@RequestParam("modelName") String modelName,HttpServletRequest request,@RequestParam(value = "picFileName") MultipartFile thumbFile){
		/*Integer userId = (Integer) request.getSession().getAttribute("userId");
		if (userId == null) {
			result.setStatus(Constants.STATUS_NOT_LOGIN);
			result.setMessage("请登录后操作");
			return result;
		}*/
		JsonResult result = new JsonResult();
 		ModelBean model = new ModelBean();
		model.setName(modelName);
		model.setCheckType("none");
		model.setScale(new Float(0));
		model.setDescription(description);
		
		File uploadFiles = new File(getUploadRealPath(request, "upload_file_tmp_path") + tmpDirs);

		// 条件临时目录存在并且临时目录文件不为空
		if (uploadFiles.exists() && uploadFiles.isDirectory() && uploadFiles.listFiles() != null && uploadFiles.listFiles().length > 0) {
			
			int modelId = modelService.save(model);
			model.setId(modelId);
			if (modelId > 0) {

				// 模型文件保存目录
				String modelFilePath = getUploadRealPath(request, "upload_file_path") + modelId;
				// =====上传文件、拷贝文件 begin======
				File modelFileDir = new File(modelFilePath);
				// 缩略图文件路径
				String thumbFilePath = modelFileDir.getPath() + Constants.FILE_SEPARATOR + fomartStr(thumbFile.getOriginalFilename());

				if (!modelFileDir.exists()) {
					modelFileDir.mkdirs();
				}
				try {
					// 保存缩略图文件
					FileCopyUtils.copy(thumbFile.getBytes(), new File(thumbFilePath));
					CommonUtil.copyFile(uploadFiles, modelFileDir,model);
					model.setPicPath(getUploadpositivePath(request, "positive_upload_file_path")+modelId+"/"+thumbFile.getOriginalFilename());
					// =====上传文件、拷贝文件 end======
					model.setPath(getUploadpositivePath(request, "positive_upload_file_path")+modelId+"/");
					int  saveResult = modelService.updatePart(model);
					
					if (saveResult > 0) {
						
						Upload upload = new Upload();
						SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
						upload.setDate(sdf.format(new Date()));
						upload.setUser(((User)request.getSession().getAttribute("user")).getId());
						upload.setModel(model);
						int uploadId = uploadService.save(upload);
						upload.setId(uploadId);
						
						result.setStatus(Constants.STATUS_SUCCESS);
						result.setMessage("保存成功");
						result.setData(new HashMap() {
							{
								put("redirect", "modelController/upload");
							}
						});
						Audit audit = new Audit();
						audit.setType("none");
						audit.setUpload(upload);
						auditService.save(audit);
					} else {
						result.setStatus(Constants.STATUS_RUNTIME_EXCEPTIONS);
						result.setMessage("保存模型失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					// 出现异常删除模型
					modelService.delete(modelId);
					result.setStatus(Constants.STATUS_RUNTIME_EXCEPTIONS);
					result.setMessage("保存模型失败");
					result.setData(new HashMap() {
						{
							put("redirect", "modelController/upload");
						}
					});
				}

				CommonUtil.delete(uploadFiles);// 删除临时文件
			} else {
				result.setStatus(Constants.STATUS_FILE_PATH_ERROR);
				result.setMessage("文件路径错误，请重新上传");
				result.setData(new HashMap() {
					{
						put("redirect", "modelController/upload");
					}
				});
			}
		} else {
			result.setStatus(Constants.STATUS_FILE_NOT_UPLOADED);
			result.setMessage("请上传模型文件");
		}
		return result;
	}
	
	private String getUploadRealPath(HttpServletRequest request, String key) {
		String path = PropertiesUtil.getProperty(ModelController.class.getClassLoader().getResource("").getPath(), "config.properties", key);
		if (path == null) {
			path = "";
		}
		if (!path.endsWith("\\") && !path.endsWith("/"))
			path += Constants.FILE_SEPARATOR;
		path = request.getSession().getServletContext().getRealPath("") + path;
		return path;
	}
	
	private String getUploadpositivePath(HttpServletRequest request, String key) {
		String path = PropertiesUtil.getProperty(ModelController.class.getClassLoader().getResource("").getPath(), "config.properties", key);
		if (path == null) {
			path = "";
		}
		if (!path.endsWith("\\") && !path.endsWith("/"))
			path += Constants.FILE_SEPARATOR;
		return path;
	}
	
	private String fomartStr(String str) {
		str = str.replaceAll("&", "").replaceAll("=", "");
		return str;
	}
	
}
