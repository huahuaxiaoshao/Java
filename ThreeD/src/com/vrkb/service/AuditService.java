package com.vrkb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vrkb.bean.Audit;
import com.vrkb.mapper.AuditMapper;

@Service
public class AuditService {

	@Autowired
	private AuditMapper auditMapper;
	
	public boolean save(Audit audit){
		boolean flag = false;
		try {
			flag = auditMapper.save(audit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public int update(Audit audit){
		int count = 0;
		try {
			count = auditMapper.update(audit);
		} catch (Exception e) {
			e.printStackTrace();
			count = 0;
		}
		return count;
	}
	
	public Audit getAuditById(int id){
		Audit audit = null;
		try {
			audit = auditMapper.getAuditById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return audit;
	}
	
	public List<Audit> findAll(){
		List<Audit> list = new ArrayList<Audit>();
		try {
			list = auditMapper.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
