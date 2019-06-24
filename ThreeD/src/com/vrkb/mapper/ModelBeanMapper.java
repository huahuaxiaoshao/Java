package com.vrkb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.vrkb.bean.ModelBean;

@Repository
public interface ModelBeanMapper {
	
	public int save(ModelBean model);

	public int updatePart(ModelBean model);
	
	public int updateAll(ModelBean model);

	public int delete(int id);
	
	public ModelBean findById(int id);

	public List<ModelBean> findAll();
	
	public List<ModelBean> getModelByPage(@Param("startRow")Integer startRow,@Param("pageSize")Integer pageSize);
	
	public List<ModelBean> getModels(@Param("size")Integer size);

}
