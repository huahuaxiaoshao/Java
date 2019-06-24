package com.vrkb.bean;

public class Audit {

	private int id;
	private Upload upload;
	private String type;
	
	public Audit(Upload upload, String type) {
		this.upload = upload;
		this.type = type;
	}

	public Audit() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Upload getUpload() {
		return upload;
	}

	public void setUpload(Upload upload) {
		this.upload = upload;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Aduit [id=" + id + ", upload=" + upload + ", type=" + type
				+ "]";
	}
	
}
