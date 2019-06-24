package com.vrkb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vrkb.bean.ModelBean;
import com.vrkb.mapper.ModelBeanMapper;

@Service
public class ModelService{

	@Autowired
	private ModelBeanMapper modelMapper;
	
	public int save(ModelBean model) {
		int count = 0,id = 0;
		try {
			count = modelMapper.save(model);
		} catch (Exception e) {
			e.printStackTrace();
			count = 0;
		}
		if(count > 0)
			id = model.getId();
		return id;
	}
	
	public int updatePart(ModelBean model) {
		int count = 0;
		try {
			count = modelMapper.updatePart(model);
		} catch (Exception e) {
			e.printStackTrace();
			count = 0;
		}
		return count;
	}
	
	public int updateAll(ModelBean model) {
		int count = 0;
		try {
			count = modelMapper.updateAll(model);
		} catch (Exception e) {
			e.printStackTrace();
			count = 0;
		}
		return count;
	}

	public int delete(int id) {
		int count = 0;
		try {
			count = modelMapper.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			count = 0;
		}
		return count;
	}

	public ModelBean findById(int id) {
		ModelBean model = null;
		try {
			model = modelMapper.findById(id);
		} catch (Exception e) {
			e.printStackTrace();
			model = null;
		}
		return model;
	}

	public List<ModelBean> findAll() {
		List<ModelBean> list = new ArrayList<ModelBean>();
		try {
			list = modelMapper.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<ModelBean> getModelByPage(int startRow, int pageSize) {
		List<ModelBean> list = new ArrayList<ModelBean>();
		try {
			list = modelMapper.getModelByPage(startRow, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<ModelBean> getModels( int size) {
		List<ModelBean> list = new ArrayList<ModelBean>();
		try {
			list = modelMapper.getModels(size);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
