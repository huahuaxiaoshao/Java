package com.vrkb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.vrkb.bean.Download;

@Repository
public interface DownloadMapper {

	public boolean save(Download upload);
	
	public List<Download> getDownload(int user);
	
	public Download getDownloadById(@Param("id")Integer id,@Param("user")Integer user);
	
	public boolean delete(int id);
	
	public boolean update(Download download);
}
