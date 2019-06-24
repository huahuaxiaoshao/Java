package com.vrkb.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.vrkb.bean.Upload;

@Repository
public interface UploadMapper {

	public int save(Upload upload);
	
	public List<Upload> getUpload(int user);
	
	public boolean delete(int id);
	
}
