package com.project.everycloud.service;

import com.project.everycloud.model.AppList;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.request.file.FileListLoadDTO;
import com.project.everycloud.model.response.file.FileDetailDTO;
import com.project.everycloud.model.response.file.FileOptionDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileService {

	AppList<FileDetailDTO, FileOptionDTO> fileList(FileListLoadDTO fileListLoad, UserDTO sessionUser);

	List<Map<String, Object>> folderList(String sharePath, String path);
	
	Map<String, Object> newFolder(String path, String newFolderName);

	Map<String, Object> newFile(String path, String newFileName) throws IOException;

	void changeName(String path, String origFileName, String newFileName);

	Map<String, Object> moveFiles(String path, String moveToPath, String fileNames, String type);

	Map<String, Object> deleteFiles(String path, String fileNames);
}
