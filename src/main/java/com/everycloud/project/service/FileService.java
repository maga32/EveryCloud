package com.everycloud.project.service;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface FileService {
	List<Map<String, Object>> fileList(String path);
	
	File getFile(String path);
	
	boolean isPathExist(String path);
}
