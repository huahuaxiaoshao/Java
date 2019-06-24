package com.vrkb.utils;

import java.io.Serializable;
import java.util.Map;

public class JsonResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -667429748928189145L;
	private int status;
	private String message;
	private Map<String, Object> data;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
