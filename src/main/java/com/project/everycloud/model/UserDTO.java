package com.project.everycloud.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@ToString
@NoArgsConstructor
public class UserDTO {
	private String id;
	private String pass;
	private String nickname;
	private String email;
	private String auth;
	private Integer groupNo;
	private String groupName;
	private String sharePass;
}
