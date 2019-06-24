package com.vrkb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vrkb.bean.Download;
import com.vrkb.mapper.DownloadMapper;

@Service
public class DownloadService {

	@Autowired
	private DownloadMapper downloadMapper;
	
	public boolean save(Download download){
		boolean flag = false;
		try {
			flag = downloadMapper.save(download);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public Download getDownloadById(int id,int user){
		Download download= null;
		try {
			download = downloadMapper.getDownloadById(id,user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return download;
	}
	
	public List<Download> getDownload(int user){
		List<Download> list= new ArrayList<Download>();
		try {
			list = downloadMapper.getDownload(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean update(Download download){
		boolean flag = false;
		try {
			flag = downloadMapper.update(download);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
}
