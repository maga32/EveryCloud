package com.project.everycloud.model.response.file;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileDetailDTO {
	boolean isIsDirectory;
	boolean isIsFile;
	boolean isIsHidden;
	String absolutePath;
	String name;
	String lowerName;
	String extension;
	String parent;
	String path;
	String canonicalPath;
	long date;
	long size;
}
