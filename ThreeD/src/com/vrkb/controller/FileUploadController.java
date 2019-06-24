package com.vrkb.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.vrkb.bean.FileMeta;
import com.vrkb.utils.CommonUtil;
import com.vrkb.utils.Constants;
import com.vrkb.utils.PropertiesUtil;

@Controller
@RequestMapping(value="fileUpload")
public class FileUploadController {

	/**
	 * ���ͬʱ�ϴ��ļ���
	 */
	private final static int MAX_UPLOAD_COUNT = 10;

	/**
	 * GET��ʽ���ʣ��г��ļ�
	 * @param uuid
	 *            �ļ���ʱĿ¼
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public Map<String, LinkedList<FileMeta>> fileList(HttpServletRequest request,@RequestParam("tmpDirs") String uuid) {
		Map<String, LinkedList<FileMeta>> fileMap = new HashMap<String, LinkedList<FileMeta>>();
		LinkedList<FileMeta> fileList = new LinkedList<FileMeta>();
		File file = new File(getUploadPath(request) + uuid);
		File[] list = file.listFiles();// �г���ʱĿ¼���ļ�
		for (File f : list) {
			FileMeta fileMeta = new FileMeta();
			fileMeta.setName(f.getName());
			fileMeta.setSize(CommonUtil.getFileSize(f.length(), 'B'));
			fileMeta.setType("");
			fileMeta.setUrl("downloadModel?fileId=" + f.getName());
			fileMeta.setThumbnailUrl("");
			fileMeta.setDeleteUrl("uploadModel?fileName=" + f.getName() + "&tmpDirs=" + uuid);
			fileMeta.setDeleteType("DELETE");
			fileMeta.setTmpDirs(uuid);
			fileMeta.setFileType(CommonUtil.getFileType(f.getName()));
			fileList.add(fileMeta);
		}
		fileMap.put("files", fileList);

		return fileMap;
	}

	/**
	 * POST�ϴ��ļ�
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public Map<String, LinkedList<FileMeta>> fileUpload(MultipartHttpServletRequest request) {
		Map<String, LinkedList<FileMeta>> fileMap = new HashMap<String, LinkedList<FileMeta>>();
		LinkedList<FileMeta> files = new LinkedList<FileMeta>();
		Map<String, MultipartFile> multipartFileMap = request.getFileMap();

		String tmpDirs = request.getParameter("tmpDirs");// ��ʱ�ϴ�Ŀ¼

		MultipartFile multipartFile = null;
		for (String key : multipartFileMap.keySet()) {
			multipartFile = multipartFileMap.get(key);

			// ����ϴ��ļ���������
			if (files.size() >= MAX_UPLOAD_COUNT)
				files.pop(); 

			// �����ļ�Ԫ��Ϣ
			FileMeta fileMeta = new FileMeta();

			String newName = fomartStr(multipartFile.getOriginalFilename());
			fileMeta.setName(newName);
			fileMeta.setSize(CommonUtil.getFileSize(multipartFile.getSize(), 'B'));
			fileMeta.setType(multipartFile.getContentType());
			fileMeta.setUrl("downloadModel?fileId=" + newName);
			fileMeta.setThumbnailUrl("");
			fileMeta.setDeleteUrl("fileUpload?fileName=" + newName + "&tmpDirs=" + tmpDirs);
			fileMeta.setDeleteType("DELETE");
			fileMeta.setTmpDirs(tmpDirs);
			fileMeta.setFileType(CommonUtil.getFileType(newName));

			try {
				// �����ļ���ָ��Ŀ¼
				File file = new File(getUploadPath(request) + tmpDirs + Constants.FILE_SEPARATOR);
				if (!(file.exists() && file.isDirectory())) {
					file.mkdirs();
				}
				FileCopyUtils.copy(multipartFile.getBytes(),
						new FileOutputStream(getUploadPath(request) + tmpDirs + Constants.FILE_SEPARATOR + newName));
			} catch (IOException e) {
				e.printStackTrace();
			}
			files.add(fileMeta);
			fileMap.put("files", files);
		}
		return fileMap;
	}

	/**
	 * DELETE��ʽɾ���ļ�
	 * @param fileName
	 * @param tmpDirs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE)
	public Map<String, Boolean> fileDelete(HttpServletRequest request,@RequestParam(value = "fileName") String fileName,
			@RequestParam(value = "tmpDirs") String tmpDirs) {
		try {
			fileName = new String(fileName.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		File file = new File(getUploadPath(request) + tmpDirs + Constants.FILE_SEPARATOR + fileName);
		if (file.exists() && file.isFile()) {
			result.put(fileName, file.delete());
		} else {
			result.put(fileName, false);
		}
		return result;
	}

	private String getUploadPath(HttpServletRequest request) {
		System.out.println(FileUploadController.class.getClassLoader().getResource("").getPath());
		String path = PropertiesUtil.getProperty(FileUploadController.class.getClassLoader().getResource("").getPath(),
				"config.properties", "upload_file_tmp_path");
		if(path == null){
			path = "";
		}
		if(!path.endsWith("\\") && !path.endsWith("/"))
			path += Constants.FILE_SEPARATOR;
		path = request.getSession().getServletContext().getRealPath("") + path;

		File tmpFile = new File(path);
		if (!tmpFile.exists()) {
			tmpFile.mkdirs();
		}
		return path;
	}

	private String fomartStr(String str){
		str = str.replaceAll("&", "").replaceAll("=", "");
		return str;
	}
}
