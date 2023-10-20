package com.project.everycloud.service.mapper;

import java.io.File;
import java.io.IOException;

public interface FileDao {
	File[] getPathFiles(String path, boolean viewHidden, String keyword);

	File[] getFolderList(String path);
	
	File getFile(String path);
	
	boolean isPathExist(String path);

	void newFolder(String path, String newFolderName);

	void newFile(String path, String newFileName) throws IOException;

	void changeName(String path, String origName, String newName);

	void moveFiles(File file, String newPath, String type);

	void deleteFile(File file);
}
