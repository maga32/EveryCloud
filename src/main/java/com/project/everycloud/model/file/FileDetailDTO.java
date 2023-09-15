package com.project.everycloud.model.file;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileDetailDTO {
	private String fileName;
	private String fileExtension;
	private String fileType;
	private Date fileMakeDate;
	private Date fileModifyDate;
	private Date fileUseDate;
	private int fileSize;
}
