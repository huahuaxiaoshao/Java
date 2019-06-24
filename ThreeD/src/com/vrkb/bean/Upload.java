package com.vrkb.bean;

public class Upload {

	private int id;
	private int user;
	private ModelBean model;
	private String date;
	
	public Upload(int user, ModelBean model, String date) {
		this.user = user;
		this.model = model;
		this.date = date;
	}

	public Upload() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
	}

	public ModelBean getModel() {
		return model;
	}

	public void setModel(ModelBean model) {
		this.model = model;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Upload [id=" + id + ", user=" + user + ", model=" + model + ", date=" + date + "]";
	}

}
