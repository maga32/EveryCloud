package com.everycloud.project.dao;

import java.io.File;

public interface FileDao {
	File[] getPathFiles(String path);
	
	File getFile(String path);

	boolean isPathExist(String path);
}
