package com.everycloud.project.service;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface FileService {
	List<Map<String, Object>> fileList(String path, String sort, String order, String keyword, boolean viewHidden);
	
	File getFile(String path);
	
	boolean isPathExist(String path);

	void changeName(String path, String origFileName, String newFileName);

	Map<String, Object> deleteFiles(String path, String fileNames);
}
