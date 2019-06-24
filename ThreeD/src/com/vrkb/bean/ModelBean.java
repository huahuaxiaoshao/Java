package com.vrkb.bean;

import org.hibernate.validator.constraints.NotEmpty;

public class ModelBean {

	private Integer id;
	
	private String path;
	@NotEmpty
	private String name;
	
	private String objName;

	private String mtlName;
	
	private String picPath;
	
	private Float scale;
	
	private int downloadCount;
	
	private String checkType;
	
	private String description;
	

	public ModelBean(String path, String name, String objName, String mtlName, String picPath, Float scale,
			int downloadCount, String checkType, String description) {
		this.path = path;
		this.name = name;
		this.objName = objName;
		this.mtlName = mtlName;
		this.picPath = picPath;
		this.scale = scale;
		this.downloadCount = downloadCount;
		this.checkType = checkType;
		this.description = description;
	}

	public ModelBean() {
		super();
	}
	
	public int getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getMtlName() {
		return mtlName;
	}

	public void setMtlName(String mtlName) {
		this.mtlName = mtlName;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public Float getScale() {
		return scale;
	}

	public void setScale(Float scale) {
		this.scale = scale;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ModelBean [id=" + id + ", path=" + path + ", name=" + name + ", objName=" + objName + ", mtlName="
				+ mtlName + ", picPath=" + picPath + ", scale=" + scale + ", downloadCount=" + downloadCount
				+ ", checkType=" + checkType + ", description=" + description + "]";
	}

}
