package com.vrkb.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class FileMeta {

	private String name;
	private String size;
	private String type;
	@JsonInclude(Include.NON_NULL)
	private byte[] bytes;
	private String url;
	private String thumbnailUrl;
	private String deleteUrl;
	private String deleteType;
	private String tmpDirs;
	/**
	 * 文件类型：<br/>0：jpg缩略图；<br/>1：3d模型源文件；<br/>2：jpg材质文件；<br/>3：js文件；<br/>4：bin文件;<br/>5：obj文件；<br/>6：mtl文件
	 */
	private int fileType;
	
	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public String getTmpDirs() {
		return tmpDirs;
	}

	public void setTmpDirs(String tmpDirs) {
		this.tmpDirs = tmpDirs;
	}

	public String getDeleteType() {
		return deleteType;
	}

	public void setDeleteType(String deleteType) {
		this.deleteType = deleteType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}