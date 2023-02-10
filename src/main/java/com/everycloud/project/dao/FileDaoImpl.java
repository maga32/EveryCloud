package com.everycloud.project.dao;

import java.io.File;

import org.springframework.stereotype.Repository;

@Repository
public class FileDaoImpl implements FileDao {

	@Override
	public File[] getPathFiles(String path) {
		File file = new File(path);
		File[] list = null;
		if(file.isFile()) {
			list = new File[1];
			list[0] = file;
		} else {
			list = file.listFiles();
		}
		
		return list;
	}

	@Override
	public File getFile(String path) {
		File file = new File(path);
		return file;
	}
	
	@Override
	public boolean isPathExist(String path) {
		File file = new File(path);
		return file.exists();
	}

}
