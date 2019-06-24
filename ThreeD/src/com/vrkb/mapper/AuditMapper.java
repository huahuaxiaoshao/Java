package com.vrkb.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.vrkb.bean.Audit;

@Repository
public interface AuditMapper {
	
	public boolean save(Audit audit);
	
	public Audit getAuditById(int id);
	
	public List<Audit> findAll();
	
	public int update(Audit audit);

}
