package com.project.everycloud.service.impl;

import com.project.everycloud.model.AppList;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.request.file.FileListLoadDTO;
import com.project.everycloud.model.response.file.FileDetailDTO;
import com.project.everycloud.model.response.file.FileOptionDTO;
import com.project.everycloud.service.FileService;
import com.project.everycloud.service.mapper.FileDao;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.InvalidPathException;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {

	@Autowired
	FileDao fileDao;

	@Override
	public AppList<FileDetailDTO, FileOptionDTO> fileList(FileListLoadDTO fileListLoad, UserDTO sessionUser) {
		AppList<FileDetailDTO, FileOptionDTO> result = new AppList<FileDetailDTO, FileOptionDTO>();
		FileOptionDTO options = new FileOptionDTO();

		String sharePath = "";
		String realPath = "";
		String path = fileListLoad.getPath();
		String shareLink = fileListLoad.getShareLink();

//		Map<String, String> shareMap = shareService.getShareAuth(shareLink, 0);
//		map.putAll(shareMap);

		if(path.equals("") && shareLink.equals("")) path += "/";

//		if(shareMap.get("invalidString") != null) {
//			return map;
//		} else if(!shareLink.equals("")) {
//			sharePath = shareMap.get("sharePath");
//			path = sharePath + (path.equals("/") ? "" : path);
//		}

		String windowsSharePath = sharePath.replaceAll("/", "\\\\");

		if(!isPathExist(path)) throw new InvalidPathException("","");

		File nowPath = getFile(path);
		// windows folder path processing
		path = path.replaceAll("\\\\", "/");
		try {
			realPath = nowPath.getCanonicalPath().replaceAll("\\\\", "/");
		} catch (IOException e) { throw new RuntimeException(e); }

		if (!realPath.equals(path)) {
			fileListLoad.setPath(realPath.replace(sharePath, ""));
			return fileList(fileListLoad, sessionUser);
		}

		options.setNowPath(nowPath.getPath().replace(sharePath, "").replace(windowsSharePath, ""));
		options.setPath(path.replace(sharePath, ""));

		result.setOption(options);
		result.setLists(fileList(sharePath, path, fileListLoad.getSort(), fileListLoad.getOrder(), fileListLoad.getKeyword(), fileListLoad.isViewHidden()));

		return result;
	}

	private boolean isPathExist(String path) {
		return fileDao.isPathExist(path);
	}


	private List<FileDetailDTO> fileList(String sharePath, String path, String sort, String order, String keyword, boolean viewHidden) {
		List<FileDetailDTO> fileList = new ArrayList<FileDetailDTO>();
		File[] files = null;
		files = fileDao.getPathFiles(path, viewHidden, keyword);
		String windowsSharePath = sharePath.replaceAll("/", "\\\\");

		for(File i : files) {
			FileDetailDTO file = new FileDetailDTO();

			file.setIsDirectory(i.isDirectory());
			file.setIsFile(i.isFile());
			file.setIsHidden(i.isHidden());
			file.setGetAbsolutePath(i.getAbsolutePath().replace(sharePath,"").replace(windowsSharePath,""));
			file.setGetName(i.getName());
			file.setGetExtension(FilenameUtils.getExtension(i.getName()).toLowerCase());
			file.setGetParent(i.getParent().replace(sharePath, "").replace(windowsSharePath,""));
			file.setGetPath(i.getPath().replace(sharePath, "").replace(windowsSharePath,""));
			file.setLastModified(i.lastModified());
			file.setLength(i.length());
			try {
				file.setGetCanonicalPath(i.getCanonicalPath().replace(sharePath,"").replace(windowsSharePath,""));
			} catch (IOException e) { }

			fileList.add(file);
		}

		String sortParam;
		if (sort.equals("name")) { sortParam = "getName";
		} else if (sort.equals("type")) { sortParam = "getExtension";
		} else if (sort.equals("path")) { sortParam = "getPath";
		} else if (sort.equals("date")) { sortParam = "lastModified";
		} else if (sort.equals("size")) { sortParam = "length";
		} else { sortParam = "getName"; }

        if (order.equals("desc")) {
			fileList.sort(
				Comparator.comparing(FileDetailDTO::getIsDirectory)
					.thenComparing(list -> {
						try {
							Field field = list.getClass().getDeclaredField(sortParam);
							field.setAccessible(true);
							return (Comparable) field.get(list);
						} catch (Exception e) { throw new RuntimeException("Error",e); }
					}).reversed()
			);
		} else {
			fileList.sort(
				Comparator.comparing(FileDetailDTO::getIsDirectory)
					.thenComparing(list -> {
						try {
							Field field = list.getClass().getDeclaredField(sortParam);
							field.setAccessible(true);
							return (Comparable) field.get(list);
						} catch (Exception e) { throw new RuntimeException("Error",e); }
					})
			);
		}

		return fileList;
	}


	/* --------------------------- 수정필요 --------------------------- */

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
	private File getFile(String path) {
		return fileDao.getFile(path);
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
