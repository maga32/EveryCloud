package com.project.everycloud.model.response.file;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class FileDetailDTO {
	boolean isDirectory;
	boolean isFile;
	boolean isHidden;
	String getAbsolutePath;
	String getName;
	String getExtension;
	String getParent;
	String getPath;
	String getCanonicalPath;
	long lastModified;
	long length;

	public boolean getIsDirectory() {
		return isDirectory;
	}
	public void setIsDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}
	public boolean getIsFile() {
		return isFile;
	}
	public void setIsFile(boolean isFile) {
		this.isFile = isFile;
	}
	public boolean getIsHidden() {
		return isHidden;
	}
	public void setIsHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}
	public String getGetAbsolutePath() {
		return getAbsolutePath;
	}
	public void setGetAbsolutePath(String getAbsolutePath) {
		this.getAbsolutePath = getAbsolutePath;
	}
	public String getGetName() {
		return getName;
	}
	public void setGetName(String getName) {
		this.getName = getName;
	}
	public String getGetExtension() {
		return getExtension;
	}
	public void setGetExtension(String getExtension) {
		this.getExtension = getExtension;
	}
	public String getGetParent() {
		return getParent;
	}
	public void setGetParent(String getParent) {
		this.getParent = getParent;
	}
	public String getGetPath() {
		return getPath;
	}
	public void setGetPath(String getPath) {
		this.getPath = getPath;
	}
	public String getGetCanonicalPath() {
		return getCanonicalPath;
	}
	public void setGetCanonicalPath(String getCanonicalPath) {
		this.getCanonicalPath = getCanonicalPath;
	}
	public long getLastModified() {
		return lastModified;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
}
