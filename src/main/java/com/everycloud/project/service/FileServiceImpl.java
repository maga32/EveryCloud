package com.everycloud.project.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everycloud.project.dao.FileDao;

@Service
public class FileServiceImpl implements FileService {

	String sortParam;
	
	@Autowired
	FileDao fileDao;
	
	@Override
	public List<Map<String, Object>> fileList(String path, String sort, String order) {
		List<Map<String, Object>> fileList = new ArrayList<Map<String,Object>>();
		
		for(File i : fileDao.getPathFiles(path)) {
			Map<String, Object> param = new HashMap<String, Object>();
			
			param.put("isDirectory", i.isDirectory());
			param.put("isFile", i.isFile());
			param.put("isHidden", i.isHidden());
			param.put("getAbsolutePath", i.getAbsolutePath());
			param.put("getName", i.getName());
			param.put("getExtension", FilenameUtils.getExtension(i.getName()).toLowerCase());
			param.put("getParent", i.getParent());
			param.put("getPath", i.getPath());
			param.put("lastModified", i.lastModified());
			param.put("length", i.length());
			try { param.put("getCanonicalPath", i.getCanonicalPath());
			} catch (IOException e) { e.printStackTrace(); }
			
			fileList.add(param);
		}
		
		if(sort.equals("name")) { sortParam = "getName";
		} else if (sort.equals("type")) { sortParam = "getExtension";
		} else if (sort.equals("date")) { sortParam = "lastModified";
		} else if (sort.equals("size")) { sortParam = "length";
		}
		
		if(sort.equals("name") || sort.equals("type")) {
			if(order.equals("asc")) {
				fileList.sort(
					Comparator.comparing((Map<String, Object> param) -> (Boolean) param.get("isFile"))
					.thenComparing((Map<String, Object> param) -> (String) param.get(sortParam))
				);
			} else {
				fileList.sort(
					Comparator.comparing((Map<String, Object> param) -> (Boolean) param.get("isDirectory"))
					.thenComparing((Map<String, Object> param) -> (String) param.get(sortParam)).reversed()
				);
			}
		} else {
			if(order.equals("asc")) {
				fileList.sort(
					Comparator.comparing((Map<String, Object> param) -> (Boolean) param.get("isFile"))
					.thenComparing((Map<String, Object> param) -> (long) param.get(sortParam))
				);
			} else {
				fileList.sort(
					Comparator.comparing((Map<String, Object> param) -> (Boolean) param.get("isDirectory"))
					.thenComparing((Map<String, Object> param) -> (long) param.get(sortParam)).reversed()
				);
			}
		}
		return fileList;
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
