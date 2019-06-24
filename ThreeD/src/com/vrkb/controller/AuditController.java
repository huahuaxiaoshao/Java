package com.vrkb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vrkb.bean.Audit;
import com.vrkb.bean.ModelBean;
import com.vrkb.bean.User;
import com.vrkb.service.AuditService;
import com.vrkb.service.ModelService;
import com.vrkb.service.UserService;

@Controller
@RequestMapping("auditController")
public class AuditController {

	@Autowired
	private AuditService auditService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private UserService userService;
	
	@RequestMapping("check")
	public String check(@RequestParam("id")String idStr,@RequestParam("checkType")String type){
		int id = Integer.parseInt(idStr);
		Audit audit = auditService.getAuditById(id);
		audit.setType(type);
		ModelBean model = audit.getUpload().getModel();
		model.setCheckType(type);
		modelService.updateAll(model);
		auditService.update(audit);
		return "redirect:/auditController/audit";
	}
	
	@RequestMapping("audit")
	public String audit(HttpServletRequest request){
		List<Audit> audits = auditService.findAll();
		request.setAttribute("audits", audits);
		return "audit";
	}
	
	@RequestMapping("auditUser")
	public String auditUser(HttpServletRequest request){
		List<User> users = userService.getOrdinaryUsers();
		request.setAttribute("users", users);
		return "auditUser";
	}
	
	@RequestMapping("changeGrade")
	public String changeGrade(@RequestParam("name")String name,@RequestParam("grade")String grade){
		User user = userService.getPassword(name);
		user.setGrade(grade);
		userService.updateGrade(user);
		return "redirect:/auditController/auditUser";
	}
	
}
