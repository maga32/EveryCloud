package com.project.everycloud.model.response.file;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileOptionDTO {
	String shareLink;
	String sharePath;
	String nowPath;
	String path;
}
