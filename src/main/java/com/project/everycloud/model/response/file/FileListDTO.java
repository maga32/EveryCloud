package com.project.everycloud.model.response.file;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class FileListDTO {
	String shareLink;
	String sharePath;
	String nowPath;
	String path;
	List<Map<String, Object>> fileList;
}
