package com.everycloud.project.dao;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class FileDaoImpl implements FileDao {
	
	@Override
	public File[] getPathFiles(String path, boolean viewHidden, String keyword) {
	    List<File> fileList = new ArrayList<>();
	    addFileList(path, viewHidden, keyword.toLowerCase(), fileList);
	    return fileList.toArray(new File[0]);
	}

	private void addFileList(String path, boolean viewHidden, String keyword, List<File> fileList) {
	    File file = new File(path);
	    File[] list = null;
		if(file.isFile()) {
			fileList.add(file);
		} else {
			list = file.listFiles(fileFilter(viewHidden));
			if(list != null) {
		    	if(!keyword.equals("")) {
					for(File subFile : list) {
					    if(subFile.getName().toLowerCase().contains(keyword)) fileList.add(subFile);
					    if(subFile.isDirectory()) addFileList(subFile.getAbsolutePath(), viewHidden, keyword, fileList);
					}
				} else {
					fileList.addAll(Arrays.asList(list));
				}
			}
		}
	}

	// hidden file filter
	private FileFilter fileFilter(boolean viewHidden) {
	    if(viewHidden) {
	        return file -> true;
	    } else {
	        return file -> !file.isHidden();
	    }
	}

	@Override
	public File[] getFolderList(String path) {
		File file = new File(path);
	    File[] list = file.listFiles(folderFilter());
	    
		return list;
	}
	
	// only view folder filter
	private FileFilter folderFilter() {
	    return file -> file.isDirectory();
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

	@Override
	public void newFolder(String path, String newFolderName) {
		File newFolder = new File(path + File.separator + newFolderName);
		newFolder.mkdir();
	}

	@Override
	public void newFile(String path, String newFileName) throws IOException {
		File newFile = new File(path + File.separator + newFileName);
		newFile.createNewFile();
	}

	@Override
	public void changeName(String path, String origFileName, String newFileName) {
		File origFile = new File(path + File.separator + origFileName);
		File newFile = new File(path + File.separator + newFileName);
		if(origFile.exists()) {
			origFile.renameTo(newFile);
		}
	}

	@Override
	public void moveFiles(File file, String moveToPath) {
		File newFile = new File(moveToPath + File.separator + file.getName());
		try {
			Files.move(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteFile(File file) {
		if(file.isFile()) {
			file.delete();
		} else {
			File[] deleteFolderList = file.listFiles();
			for(File deleteOne : deleteFolderList) {
				deleteFile(deleteOne);
			}
			file.delete();
		}
	}

}
