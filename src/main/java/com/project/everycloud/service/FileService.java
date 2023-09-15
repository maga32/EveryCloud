package com.project.everycloud.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileService {
	List<Map<String, Object>> fileList(String sharePath, String path, String sort, String order, String keyword, boolean viewHidden);
	
	List<Map<String, Object>> folderList(String sharePath, String path);
	
	File getFile(String path);
	
	boolean isPathExist(String path);

	Map<String, Object> newFolder(String path, String newFolderName);

	Map<String, Object> newFile(String path, String newFileName) throws IOException;

	void changeName(String path, String origFileName, String newFileName);

	Map<String, Object> moveFiles(String path, String moveToPath, String fileNames, String type);

	Map<String, Object> deleteFiles(String path, String fileNames);
}
