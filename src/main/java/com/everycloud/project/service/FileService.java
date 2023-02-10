package com.everycloud.project.service;

import java.io.File;

public interface FileService {
	File[] getPathFiles(String path);
	
	File getFile(String path);
	
	boolean isPathExist(String path);
}
