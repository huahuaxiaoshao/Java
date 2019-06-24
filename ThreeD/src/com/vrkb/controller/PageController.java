package com.vrkb.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.vrkb.bean.ModelBean;
import com.vrkb.service.ModelService;

@Controller
@RequestMapping("pageController")
public class PageController {

	//@Value("#{configProperties['modelPageSize']}")
	private String modelPageSize="9";

	@Autowired
	private ModelService modelService;

	@RequestMapping("/listModel")
	public ModelAndView listModel(@RequestParam("page")String page,HttpServletRequest request){    

		//ÿҳ��ʾ������
		int pageSize = Integer.parseInt(modelPageSize);
		ModelAndView mv = new ModelAndView("page");

		List<ModelBean> models = new ArrayList<ModelBean>();        
		models = modelService.findAll();

		//�鵽�����û���
		mv.addObject("modelNum", models.size());

		//��ҳ��
		int pageTimes = 0;
		if(models.size()%pageSize == 0){
			pageTimes = models.size()/pageSize;
		}else{
			pageTimes = models.size()/pageSize + 1;
		}
		mv.addObject("pageTimes", pageTimes);

		//ҳ���ʼ��ʱ��pageû��ֵ
		if(null == page){
			page = "1";
		}

		//ÿҳ��ʼ�ĵڼ�����¼            
		int startRow = (Integer.parseInt(page)-1) * pageSize;
		models = modelService.getModelByPage(startRow, pageSize);
		mv.addObject("currentPage", Integer.parseInt(page));
		mv.addObject("models", models);
		return mv; 
	}
}
