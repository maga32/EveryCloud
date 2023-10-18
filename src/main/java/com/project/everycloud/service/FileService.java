package com.project.everycloud.service;

import com.project.everycloud.model.AppList;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.request.file.FileListLoadDTO;
import com.project.everycloud.model.request.file.NewFileDTO;
import com.project.everycloud.model.response.file.FileDetailDTO;
import com.project.everycloud.model.response.file.FileOptionDTO;

import java.util.Map;

public interface FileService {

	AppList<FileDetailDTO, FileOptionDTO> getFileList(FileListLoadDTO fileListLoad, UserDTO sessionUser);

	AppList<FileDetailDTO, FileOptionDTO> getFolderList(FileListLoadDTO folderListLoad, UserDTO sessionUser);

	void newFile(NewFileDTO newFile, UserDTO sessionUser, String type);

	void changeName(String path, String origFileName, String newFileName);

	Map<String, Object> moveFiles(String path, String moveToPath, String fileNames, String type);

	Map<String, Object> deleteFiles(String path, String fileNames);
}
