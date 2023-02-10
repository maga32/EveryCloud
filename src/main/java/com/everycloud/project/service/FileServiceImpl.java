package com.everycloud.project.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everycloud.project.dao.FileDao;

@Service
public class FileServiceImpl implements FileService {

	@Autowired
	FileDao fileDao;
	
	@Override
	public File[] getPathFiles(String path) {
		return fileDao.getPathFiles(path);
	}

	@Override
	public File getFile(String path) {
		return fileDao.getFile(path);
	}

	@Override
	public boolean isPathExist(String path) {
		return fileDao.isPathExist(path);
	}

}
