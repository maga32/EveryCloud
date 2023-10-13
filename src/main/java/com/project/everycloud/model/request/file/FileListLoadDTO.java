package com.project.everycloud.model.request.file;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class FileListLoadDTO {
	@NotNull(message = "필수항목: shareLink")
	String shareLink;
	@NotNull(message = "필수항목: path")
	String path;
	@NotNull(message = "필수항목: sort")
	String sort;
	@NotNull(message = "필수항목: order")
	String order;
	@NotNull(message = "필수항목: keyword")
	String keyword;

	boolean viewHidden;
}
