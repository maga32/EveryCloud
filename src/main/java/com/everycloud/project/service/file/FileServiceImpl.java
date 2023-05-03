package com.everycloud.project.service.file;

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

import com.everycloud.project.dao.file.FileDao;

@Service
public class FileServiceImpl implements FileService {

	String sortParam;
	
	@Autowired
	FileDao fileDao;
	
	@Override
	public List<Map<String, Object>> fileList(String sharePath, String path, String sort, String order, String keyword, boolean viewHidden) {
		List<Map<String, Object>> fileList = new ArrayList<Map<String,Object>>();
		File[] files = null;
		files = fileDao.getPathFiles(path, viewHidden, keyword);
		String windowsSharePath = sharePath.replaceAll("/", "\\\\");

		for(File i : files) {
			Map<String, Object> param = new HashMap<String, Object>();
			
			param.put("isDirectory", i.isDirectory());
			param.put("isFile", i.isFile());
			param.put("isHidden", i.isHidden());
			param.put("getAbsolutePath", i.getAbsolutePath().replace(sharePath,"").replace(windowsSharePath,""));
			param.put("getName", i.getName());
			param.put("getExtension", FilenameUtils.getExtension(i.getName()).toLowerCase());
			param.put("getParent", i.getParent().replace(sharePath, "").replace(windowsSharePath,""));
			param.put("getPath", i.getPath().replace(sharePath, "").replace(windowsSharePath,""));
			param.put("lastModified", i.lastModified());
			param.put("length", i.length());
			try { param.put("getCanonicalPath", i.getCanonicalPath().replace(sharePath,"").replace(windowsSharePath,""));
			} catch (IOException e) { e.printStackTrace(); }
			
			fileList.add(param);
		}
		
		if(sort.equals("name")) { sortParam = "getName";
		} else if (sort.equals("type")) { sortParam = "getExtension";
		} else if (sort.equals("path")) { sortParam = "getPath";
		} else if (sort.equals("date")) { sortParam = "lastModified";
		} else if (sort.equals("size")) { sortParam = "length";
		}
		
		if(sort.equals("name") || sort.equals("type") || sort.equals("path")) {
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
	public List<Map<String, Object>> folderList(String sharePath, String path) {
		List<Map<String, Object>> folderList = new ArrayList<Map<String,Object>>();
		File[] files = fileDao.getFolderList(path);
		String windowsSharePath = sharePath.replaceAll("/", "\\\\");

		for(File i : files) {
			Map<String, Object> param = new HashMap<String, Object>();
			
			param.put("getName", i.getName());
			param.put("getPath", i.getPath().replace(sharePath, "").replace(windowsSharePath,""));
			param.put("lastModified", i.lastModified());
			param.put("length", i.length());
			
			folderList.add(param);
		}
		
		return folderList;
	}
	@Override
	public File getFile(String path) {
		return fileDao.getFile(path);
	}

	@Override
	public boolean isPathExist(String path) {
		return fileDao.isPathExist(path);
	}

	@Override
	public Map<String, Object> newFolder(String path, String newFolderName) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(fileDao.isPathExist(path + File.separator + newFolderName)) {
			map.put("result", "이미 폴더명이 존재합니다.");
			return map;
		}
		
		fileDao.newFolder(path, newFolderName);
		map.put("result", "ok");
		return map;
	}

	@Override
	public Map<String, Object> newFile(String path, String newFileName) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		if(fileDao.isPathExist(path + File.separator + newFileName)) {
			map.put("result", "이미 파일명이 존재합니다.");
			return map;
		}
		
		fileDao.newFile(path, newFileName);
		map.put("result", "ok");
		return map;
	}

	@Override
	public void changeName(String path, String origFileName, String newFileName) {
		fileDao.changeName(path, origFileName, newFileName);
	}

	@Override
	public Map<String, Object> moveFiles(String path, String moveToPath, String fileNames, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String[] fileList = fileNames.split(",");
		for (String fileName : fileList) {
    		File file = new File(path + File.separator + fileName);
    		if(file.exists()) {
    			fileDao.moveFiles(file, moveToPath, type);
    		}
        }
		
		map.put("result", "ok");
		return map;
	}

	@Override
	public Map<String, Object> deleteFiles(String path, String fileNames) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String[] fileList = fileNames.split(",");
		for (String fileName : fileList) {
    		File file = new File(path + File.separator + fileName);
    		if(file.exists()) {
    			fileDao.deleteFile(file);
    		}
        }
		
		map.put("result", "ok");
		return map;
	}

}
