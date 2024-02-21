package com.project.everycloud.service;

import com.project.everycloud.model.AppList;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.file.FileListLoadDTO;
import com.project.everycloud.model.file.NewFileDTO;
import com.project.everycloud.model.file.UpdateFileListDTO;
import com.project.everycloud.model.file.FileDetailDTO;
import com.project.everycloud.model.file.FileOptionDTO;

public interface FileService {

	AppList<FileDetailDTO> getFileList(FileListLoadDTO fileListLoad, UserDTO sessionUser);

	AppList<FileDetailDTO> getFolderList(FileListLoadDTO folderListLoad, UserDTO sessionUser);

	void newFile(NewFileDTO newFile, UserDTO sessionUser, String type);

	void changeName(NewFileDTO newFile, UserDTO sessionUser);

	void moveFiles(UpdateFileListDTO updateFileList, UserDTO sessionUser, String type);

	void deleteFiles(UpdateFileListDTO updateFileList, UserDTO sessionUser);
}
