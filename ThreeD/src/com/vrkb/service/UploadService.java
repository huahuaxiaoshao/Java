package com.vrkb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vrkb.bean.Upload;
import com.vrkb.mapper.UploadMapper;

@Service
public class UploadService {

	@Autowired
	private UploadMapper uploadMapper;
	
	public int save(Upload upload){
		int count = 0,id = 0;
		try {
			count = uploadMapper.save(upload);
		} catch (Exception e) {
			e.printStackTrace();
			count = 0;
		}
		if(count > 0)
			id = upload.getId();
		return id;
	}
	
	public boolean delete(int id){
		boolean flag = false;
		try {
			flag = uploadMapper.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public List<Upload> getUpload(int user){
		List<Upload> list = new ArrayList<Upload>();
		try {
			list = uploadMapper.getUpload(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
