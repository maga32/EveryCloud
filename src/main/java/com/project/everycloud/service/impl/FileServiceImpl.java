package com.project.everycloud.service.impl;

import com.project.everycloud.common.exception.ExistNameException;
import com.project.everycloud.common.exception.NotExistFileException;
import com.project.everycloud.model.AppList;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.request.file.FileListLoadDTO;
import com.project.everycloud.model.request.file.NewFileDTO;
import com.project.everycloud.model.request.file.UpdateFileListDTO;
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
	public AppList<FileDetailDTO, FileOptionDTO> getFileList(FileListLoadDTO fileListLoad, UserDTO sessionUser) {
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
		path = winPath(path);
		try {
			realPath = winPath(nowPath.getCanonicalPath());
		} catch (IOException e) { throw new RuntimeException(e); }

		if (!realPath.equals(path)) {
			fileListLoad.setPath(realPath.replace(sharePath, ""));
			return getFileList(fileListLoad, sessionUser);
		}

		options.setNowPath(nowPath.getPath().replace(sharePath, "").replace(windowsSharePath, ""));
		options.setPath(path.replace(sharePath, ""));

		result.setOption(options);
		result.setLists(fileList(sharePath, path, fileListLoad.getSort(), fileListLoad.getOrder(), fileListLoad.getKeyword(), fileListLoad.isViewHidden()));
		result.setTotal(result.getLists().size());

		return result;
	}

	private List<FileDetailDTO> fileList(String sharePath, String path, String sort, String order, String keyword, boolean viewHidden) {
		List<FileDetailDTO> fileList = new ArrayList<FileDetailDTO>();
		File[] files = null;
		files = fileDao.getPathFiles(path, viewHidden, keyword);
		String windowsSharePath = sharePath.replaceAll("/", "\\\\");

		for(File i : files) {
			FileDetailDTO file = new FileDetailDTO();
			if(i.getAbsolutePath().contains(".everyCloud")) continue;

			file.setIsDirectory(i.isDirectory());
			file.setIsFile(i.isFile());
			file.setIsHidden(i.isHidden());
			file.setAbsolutePath(i.getAbsolutePath().replace(sharePath,"").replace(windowsSharePath,""));
			file.setName(i.getName());
			file.setLowerName(i.getName().toLowerCase());
			file.setExtension(FilenameUtils.getExtension(i.getName()).toLowerCase());
			file.setParent(i.getParent().replace(sharePath, "").replace(windowsSharePath,""));
			file.setPath(i.getPath().replace(sharePath, "").replace(windowsSharePath,""));
			file.setDate(i.lastModified());
			file.setSize(i.length());
			try {
				file.setCanonicalPath(i.getCanonicalPath().replace(sharePath,"").replace(windowsSharePath,""));
			} catch (IOException e) { }
			fileList.add(file);
		}

		String sortParam;
		if (sort.equals("name")) { sortParam = "lowerName";
		} else if (sort.equals("type")) { sortParam = "extension";
		} else if (sort.equals("path")) { sortParam = "path";
		} else if (sort.equals("date")) { sortParam = "date";
		} else if (sort.equals("size")) { sortParam = "size";
		} else { sortParam = "lowerName"; }

		if (order.equals("desc")) {
			fileList.sort(
				Comparator.comparing(FileDetailDTO::isIsDirectory)
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
				Comparator.comparing(FileDetailDTO::isIsFile)
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


	@Override
	public AppList<FileDetailDTO, FileOptionDTO> getFolderList(FileListLoadDTO folderListLoad, UserDTO sessionUser) {
		// Map<String, Object> map = new HashMap<String, Object>();
		// Map<String, String> shareMap = shareService.getShareAuth(shareLink, 0, session);
		// map.putAll(shareMap);
		AppList<FileDetailDTO, FileOptionDTO> result = new AppList<FileDetailDTO, FileOptionDTO>();
		FileOptionDTO options = new FileOptionDTO();

		String sharePath = "";
		String path = folderListLoad.getPath();
		String shareLink = folderListLoad.getShareLink();

		/*
		if(shareMap.get("invalidString") != null) {
			return map;
		} else if(!shareLink.equals("")) {
			sharePath = shareMap.get("sharePath");
			path = sharePath + (path.equals("/") ? "" : path);
		}
		*/
		String windowsSharePath = sharePath.replaceAll("/", "\\\\");
		boolean validPath = isPathExist(path);

		if(validPath) {
			File nowPath = getFile(path);
			path = winPath(path);
			String parentPath = winPath(nowPath.getPath()).length() > sharePath.length() && nowPath.getParent() != null
								? winPath(nowPath.getParent()).replace(sharePath, "")
								: "/";

			result.setLists(folderList(sharePath, path));
			options.setNowPath(nowPath.getPath().replace(sharePath, "").replace(windowsSharePath, ""));
			options.setParentPath(parentPath);
		}

		options.setPath(path.replace(sharePath, ""));
		result.setOption(options);

		return result;
	}

	private List<FileDetailDTO> folderList(String sharePath, String path) {
		List<FileDetailDTO> folderList = new ArrayList<FileDetailDTO>();
		File[] files = fileDao.getFolderList(path);
		String windowsSharePath = sharePath.replaceAll("/", "\\\\");

		for(File i : files) {
			FileDetailDTO file = new FileDetailDTO();
			if(i.getAbsolutePath().contains(".everyCloud")) continue;

			file.setName(i.getName());
			file.setLowerName(i.getName().toLowerCase());
			file.setPath(i.getPath().replace(sharePath, "").replace(windowsSharePath,""));
			file.setDate(i.lastModified());
			file.setIsHidden(i.isHidden());

			folderList.add(file);
		}

		folderList.sort(Comparator.comparing(FileDetailDTO::getLowerName));

		return folderList;
	}


	@Override
	public void newFile(NewFileDTO newFile, UserDTO sessionUser, String type) {
		String path = newFile.getPath();
		String newName = newFile.getNewName();
		String shareLink = newFile.getShareLink();

		// Map<String, Object> map = new HashMap<String, Object>();
		// Map<String, String> shareMap = shareService.getShareAuth(shareLink, 1, session);

		String sharePath = "";
		/*
		if(shareMap.get("invalidString") != null) {
			map.put("result",shareMap.get("invalidString"));
			return map;
		} else if(!shareLink.equals("")) {
			sharePath = shareMap.get("sharePath");
			path = sharePath + (path.equals("/") ? "" : path);
		}
		*/

		if(isPathExist(path + File.separator + newName)) {
			throw new ExistNameException(newName);
		} else {
			if(type.equals("file")) {
				try { fileDao.newFile(path, newName);
				} catch (IOException e) { throw new RuntimeException(); }
			} else if(type.equals("folder")) {
				fileDao.newFolder(path, newName);
			}
		}
	}


	@Override
	public void changeName(NewFileDTO newFile, UserDTO sessionUser) {
		String path = newFile.getPath();
		String newName = newFile.getNewName();
		String shareLink = newFile.getShareLink();
		String origName = newFile.getOrigName();

		// Map<String, Object> map = new HashMap<String, Object>();
		// Map<String, String> shareMap = shareService.getShareAuth(shareLink, 1, session);

		String sharePath = "";

		/*
		if(shareMap.get("invalidString") != null) {
			map.put("result",shareMap.get("invalidString"));
			return map;
		} else if(!shareLink.equals("")) {
			sharePath = shareMap.get("sharePath");
			path = sharePath + (path.equals("/") ? "" : path);
		}
		*/

		if(!isPathExist(path + File.separator + origName)) {
			throw new NotExistFileException(origName);
		} else if(isPathExist(path + File.separator + newName)){
			throw new ExistNameException(newName);
		}
		fileDao.changeName(path, origName, newName);
	}


	@Override
	public void moveFiles(UpdateFileListDTO updateFileList, UserDTO sessionUser, String type) {
		String path = updateFileList.getPath();
		String newPath = updateFileList.getNewPath();
		String shareLink = updateFileList.getShareLink();
		List<String> fileList = updateFileList.getFileList();

		// Map<String, Object> map = new HashMap<String, Object>();
		// Map<String, String> shareMap = shareService.getShareAuth(shareLink, 1, session);

		String sharePath = "";

		/*
		if(shareMap.get("invalidString") != null) {
			map.put("result",shareMap.get("invalidString"));
			return map;
		} else if(!shareLink.equals("")) {
			sharePath = shareMap.get("sharePath");
			path = sharePath + (path.equals("/") ? "" : path);
			moveToPath = sharePath + (moveToPath.equals("/") ? "" : moveToPath);
		}
		*/

		for (String fileName : fileList) {
			File file = new File(path + File.separator + fileName);
			if(file.exists()) {
				fileDao.moveFiles(file, newPath, type);
			}
		}
	}


	@Override
	public void deleteFiles(UpdateFileListDTO updateFileList, UserDTO sessionUser) {
		String path = updateFileList.getPath();
		String shareLink = updateFileList.getShareLink();
		List<String> fileList = updateFileList.getFileList();

		// Map<String, Object> map = new HashMap<String, Object>();
		// Map<String, String> shareMap = shareService.getShareAuth(shareLink, 1, session);

		String sharePath = "";

		/*
		if(shareMap.get("invalidString") != null) {
			map.put("result",shareMap.get("invalidString"));
			return map;
		} else if(!shareLink.equals("")) {
			sharePath = shareMap.get("sharePath");
			path = sharePath + (path.equals("/") ? "" : path);
		}
		*/

		for(String fileName : fileList) {
			File file = new File(path + File.separator + fileName);
			if(file.exists()) fileDao.deleteFile(file);
		}
	}


	/**
	 * Replace WINDOWS folder path processing
	 *
	 * @param path
	 * @return path.replaceAll("\\\\", "/")
	 */
	private String winPath(String path) {
		return path.replaceAll("\\\\", "/");
	}

	private boolean isPathExist(String path) {
		return fileDao.isPathExist(path);
	}

	private File getFile(String path) {
		return fileDao.getFile(path);
	}

	/* --------------------------- 수정필요 --------------------------- */

}
