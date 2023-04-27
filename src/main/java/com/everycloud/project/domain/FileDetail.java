package com.everycloud.project.domain;

import java.util.Date;

public class FileDetail {
	private String fileName, fileExtension, fileType;
	private Date fileMakeDate, fileModifyDate, fileUseDate;
	private int fileSize;
	
	public FileDetail() {}
	public FileDetail(String fileName, String fileExtension, String fileType,
			Date fileMakeDate, Date fileModifyDate, Date fileUseDate, int fileSize) {
		this.fileName = fileName;
		this.fileExtension = fileExtension;
		this.fileType = fileType;
		this.fileMakeDate = fileMakeDate;
		this.fileModifyDate = fileModifyDate;
		this.fileUseDate = fileUseDate;
		this.fileSize = fileSize;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileExtension() {
		return fileExtension;
	}
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public Date getFileMakeDate() {
		return fileMakeDate;
	}
	public void setFileMakeDate(Date fileMakeDate) {
		this.fileMakeDate = fileMakeDate;
	}
	public Date getFileModifyDate() {
		return fileModifyDate;
	}
	public void setFileModifyDate(Date fileModifyDate) {
		this.fileModifyDate = fileModifyDate;
	}
	public Date getFileUseDate() {
		return fileUseDate;
	}
	public void setFileUseDate(Date fileUseDate) {
		this.fileUseDate = fileUseDate;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	@Override
	public String toString() {
		return "FileDetail{" +
				"fileName='" + fileName + '\'' +
				", fileExtension='" + fileExtension + '\'' +
				", fileType='" + fileType + '\'' +
				", fileMakeDate=" + fileMakeDate +
				", fileModifyDate=" + fileModifyDate +
				", fileUseDate=" + fileUseDate +
				", fileSize=" + fileSize +
				'}';
	}
}
