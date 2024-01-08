package com.project.everycloud.model.file;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileOptionDTO {
	String shareLink;
	String sharePath;
	String nowPath;
	String path;
	String parentPath;
	Integer shareAuth;
}
