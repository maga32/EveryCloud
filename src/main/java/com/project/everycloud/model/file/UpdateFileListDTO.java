package com.project.everycloud.model.file;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class UpdateFileListDTO {
	@NotNull(message = "필수항목: shareLink")
	private String shareLink;
	@NotNull(message = "필수항목: fileList")
	private List<String> fileList;
	@NotNull(message = "필수항목: path")
	private String path;
	private String newPath;
}
