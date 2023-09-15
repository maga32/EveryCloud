package com.project.everycloud.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
	private String id;
	private String pass;
	private String nickname;
	private String email;
	private String auth;
	private Integer groupNo;
	private String groupName;
}
