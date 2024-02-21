package com.project.everycloud.service.impl;

import com.project.everycloud.common.exception.ExistNameException;
import com.project.everycloud.common.exception.NeedLoginException;
import com.project.everycloud.common.exception.NotExistFileException;
import com.project.everycloud.common.util.FileUtil;
import com.project.everycloud.model.AppList;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.file.FileListLoadDTO;
import com.project.everycloud.model.file.NewFileDTO;
import com.project.everycloud.model.file.UpdateFileListDTO;
import com.project.everycloud.model.file.FileDetailDTO;
import com.project.everycloud.model.file.FileOptionDTO;
import com.project.everycloud.service.FileService;
import com.project.everycloud.service.SettingsService;
import com.project.everycloud.service.ShareService;
import com.project.everycloud.service.UserService;
import com.project.everycloud.service.mapper.FileDao;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

	@Autowired
	ShareService shareService;

	@Autowired
	UserService userService;

	@Autowired
	SettingsService settingsService;

	@Autowired
	FileDao fileDao;

	@Override
	public AppList<FileDetailDTO> getFileList(FileListLoadDTO fileListLoad, UserDTO sessionUser) {
		AppList<FileDetailDTO> result = new AppList<FileDetailDTO>();
		FileOptionDTO options = new FileOptionDTO();

		boolean isAdmin = userService.isAdmin(sessionUser);
		if(isAdmin && StringUtils.hasText(fileListLoad.getShareLink())) {
			fileListLoad.setPath(shareService.getShareByLink(fileListLoad.getShareLink()).getPath());
			fileListLoad.setShareLink("");
		} else if(!isAdmin && !StringUtils.hasText(fileListLoad.getShareLink())) {
			throw new NeedLoginException();
		}

		String sharePath = "";
		String realPath = "";
		String path = fileListLoad.getPath();
		String shareLink = fileListLoad.getShareLink();

		shareService.verifyAuth(shareLink, 0, sessionUser);

		if(path.equals("") && shareLink.equals("")) path += "/";

		if(StringUtils.hasText(shareLink)) {
			sharePath = shareService.getShareByLink(shareLink).getPath();
			path = sharePath + (path.equals("/") ? "" : path);
		}

		String windowsSharePath = FileUtil.winPath(sharePath);

		if(!isPathExist(path)) throw new InvalidPathException("","");

		File nowPath = getFile(path);
		path = FileUtil.macPath(path);
		try {
			realPath = FileUtil.macPath(nowPath.getCanonicalPath());
		} catch (IOException e) { throw new RuntimeException(e); }

		if (!realPath.equals(path)) {
			fileListLoad.setPath(realPath.replace(sharePath, ""));
			return getFileList(fileListLoad, sessionUser);
		}

		options.setNowPath(nowPath.getPath().replace(sharePath, "").replace(windowsSharePath, ""));
		options.setPath(path.replace(sharePath, ""));
		options.setShareLink(shareLink);
		options.setShareAuth(isAdmin ? 1 : shareService.getShareByLink(shareLink).getAuth());

		String parentPath = FileUtil.macPath(nowPath.getPath()).length() > sharePath.length() && nowPath.getParent() != null
				? FileUtil.macPath(nowPath.getParent()).replace(sharePath, "")
				: "/";

		options.setParentPath(parentPath);

		result.setOption(options);
		result.setLists(fileList(sharePath, path, fileListLoad.getSort(), fileListLoad.getOrder(), fileListLoad.getKeyword(), fileListLoad.isViewHidden()));
		result.setTotal(result.getLists().size());

		return result;
	}

	private List<FileDetailDTO> fileList(String sharePath, String path, String sort, String order, String keyword, boolean viewHidden) {
		List<FileDetailDTO> fileList = new ArrayList<FileDetailDTO>();
		File[] files = null;
		files = fileDao.getPathFiles(path, viewHidden, keyword);
		String windowsSharePath = FileUtil.winPath(sharePath);

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
	public AppList<FileDetailDTO> getFolderList(FileListLoadDTO folderListLoad, UserDTO sessionUser) {
		AppList<FileDetailDTO> result = new AppList<FileDetailDTO>();
		FileOptionDTO options = new FileOptionDTO();

		boolean isAdmin = userService.isAdmin(sessionUser);
		if(isAdmin && StringUtils.hasText(folderListLoad.getShareLink())) {
			folderListLoad.setPath(shareService.getShareByLink(folderListLoad.getShareLink()).getPath());
			folderListLoad.setShareLink("");
		} else if(!isAdmin && !StringUtils.hasText(folderListLoad.getShareLink())) {
			throw new NeedLoginException();
		}

		String sharePath = "";
		String path = folderListLoad.getPath();
		String shareLink = folderListLoad.getShareLink();

		shareService.verifyAuth(shareLink, 0, sessionUser);

		if(StringUtils.hasText(shareLink)) {
			sharePath = shareService.getShareByLink(shareLink).getPath();
			path = sharePath + (path.equals("/") ? "" : path);
		}

		String windowsSharePath = FileUtil.winPath(sharePath);
		boolean validPath = isPathExist(path);

		if(validPath) {
			File nowPath = getFile(path);
			path = FileUtil.macPath(path);
			String parentPath = FileUtil.macPath(nowPath.getPath()).length() > sharePath.length() && nowPath.getParent() != null
								? FileUtil.macPath(nowPath.getParent()).replace(sharePath, "")
								: "/";

			result.setLists(folderList(sharePath, path));
			options.setNowPath(nowPath.getPath().replace(sharePath, "").replace(windowsSharePath, ""));
			options.setParentPath(parentPath);
		}

		options.setPath(path.replace(sharePath, ""));
		options.setShareLink(shareLink);
		options.setShareAuth(isAdmin ? 1 : shareService.getShareByLink(shareLink).getAuth());

		result.setOption(options);

		return result;
	}

	private List<FileDetailDTO> folderList(String sharePath, String path) {
		List<FileDetailDTO> folderList = new ArrayList<FileDetailDTO>();
		File[] files = fileDao.getFolderList(path);
		String windowsSharePath = FileUtil.winPath(sharePath);

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
		String newName = newFile.getName();
		String shareLink = newFile.getShareLink();

		shareService.verifyAuth(shareLink, 1, sessionUser);

		if(StringUtils.hasText(shareLink)) {
			path = shareService.getShareByLink(shareLink).getPath() + (path.equals("/") ? "" : path);
		}

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
		String newName = newFile.getName();
		String shareLink = newFile.getShareLink();
		String origName = newFile.getOrigName();

		shareService.verifyAuth(shareLink, 1, sessionUser);

		if(StringUtils.hasText(shareLink)) {
			path = shareService.getShareByLink(shareLink).getPath() + (path.equals("/") ? "" : path);
		}

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

		shareService.verifyAuth(shareLink, 1, sessionUser);

		if(StringUtils.hasText(shareLink)) {
			String sharePath = shareService.getShareByLink(shareLink).getPath();
			path = sharePath + (path.equals("/") ? "" : path);
			newPath = sharePath + (newPath.equals("/") ? "" : newPath);
		}

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
//		쓰레기통기능 추가예정
//		SettingsDTO settings = settingsService.getSettings("admin");
//		if(settings.getUseTrash().equals("Y")) {
//
//		}

		shareService.verifyAuth(shareLink, 1, sessionUser);

		if(StringUtils.hasText(shareLink)) {
			path = shareService.getShareByLink(shareLink).getPath() + (path.equals("/") ? "" : path);
		}

		for(String fileName : fileList) {
			File file = new File(path + File.separator + fileName);
			if(file.exists()) fileDao.deleteFile(file);
		}
	}

	private boolean isPathExist(String path) {
		return fileDao.isPathExist(path);
	}

	private File getFile(String path) {
		return fileDao.getFile(path);
	}

}
