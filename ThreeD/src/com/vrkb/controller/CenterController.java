package com.vrkb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vrkb.bean.Download;
import com.vrkb.bean.Upload;
import com.vrkb.bean.User;
import com.vrkb.service.DownloadService;
import com.vrkb.service.UploadService;

@Controller
@RequestMapping("centerController")
public class CenterController {
	
	@Autowired
	private UploadService uploadService;
	@Autowired
	private DownloadService downloadService;
	
	@RequestMapping("uploadHistory")
	public String uploadHistory(HttpServletRequest request){
		List<Upload> uploads = uploadService.getUpload(((User)request.getSession().getAttribute("user")).getId());
		request.setAttribute("uploads", uploads);
		return "uploadHistory";
	}
	
	@RequestMapping("downloadHistory")
	public String downloadHistory(HttpServletRequest request){
		List<Download> downloads = downloadService.getDownload(((User)request.getSession().getAttribute("user")).getId());
		request.setAttribute("downloads", downloads);
		return "downloadHistory";
	}
	
	@RequestMapping("dashboard")
	public String dashboard(HttpServletRequest request){
		return "dashboard";
	}
	
	
}
