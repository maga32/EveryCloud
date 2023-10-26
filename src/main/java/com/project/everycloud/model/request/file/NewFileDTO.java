package com.project.everycloud.model.request.file;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class NewFileDTO {
	@NotNull(message = "필수항목: shareLink")
	private String shareLink;
	@NotNull(message = "필수항목: path")
	private String path;
	@NotNull(message = "필수항목: name")
	private String name;
	private String origName;
}
