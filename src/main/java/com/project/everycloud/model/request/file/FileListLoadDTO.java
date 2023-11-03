package com.project.everycloud.model.request.file;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class FileListLoadDTO {
	@NotNull(message = "필수항목: shareLink")
	private String shareLink;
	@NotNull(message = "필수항목: path")
	private String path;
	private String sort = "name";
	private String order = "asc";
	private String keyword = "";

	private boolean viewHidden;
}
