package com.everycloud.project.dao;

import java.io.File;
import java.util.ArrayList;

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
	public File[] getPathFiles(String path, String keyword) {
		ArrayList<File> fileList = findFiles(path, keyword);
		return (File[]) fileList.toArray(new File[fileList.size()]);
	}
	
	private ArrayList<File> findFiles(String directoryPath, String keyword) {
		ArrayList<File> matchingFiles = new ArrayList<File>();
		File directory = new File(directoryPath);
		
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			for (File file : files) {
			    if (file.isDirectory()) {
			    	matchingFiles.addAll(findFiles(file.getAbsolutePath(), keyword));
			    } else if (file.getName().contains(keyword)) {
			        matchingFiles.add(file);
			    }
			}
	    }
	    return matchingFiles;
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
