package com.everycloud.project.dao;

import java.io.File;

public interface FileDao {
	File[] getPathFiles(String path, boolean viewHidden, String keyword);
	
	File getFile(String path);
	
	boolean isPathExist(String path);

	void changeName(String path, String origFileName, String newFileName);

	void deleteFile(File file);
}
