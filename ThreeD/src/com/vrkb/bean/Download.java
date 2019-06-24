package com.vrkb.bean;

public class Download {
	
	private int id;
	private int user;
	private ModelBean model;
	private int count;
	private String date;
	
	public Download(int user, ModelBean model, int count, String date) {
		this.user = user;
		this.model = model;
		this.count = count;
		this.date = date;
	}
	
	public Download() {
		
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
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Download [id=" + id + ", user=" + user + ", model=" + model
				+ ", count=" + count + ", date=" + date + "]";
	}

}
