package com.everycloud.project.domain;

public class User {
	private String id;
	private String pass;
	private String nickname;
	private String email;
	private String auth;
	private Integer groupNo;
	private String groupName;
	
	public User() {}
	
	public User(String id, String pass, String nickname, String email,
		String auth, Integer groupNo, String groupName) {
		this.id = id;
		this.pass = pass;
		this.nickname = nickname;
		this.email = email;
		this.auth = auth;
		this.groupNo = groupNo;
		this.groupName = groupName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public Integer getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(Integer groupNo) {
		this.groupNo = groupNo;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", pass='" + pass + '\'' +
				", nickname='" + nickname + '\'' +
				", email='" + email + '\'' +
				", auth='" + auth + '\'' +
				", groupNo=" + groupNo +
				", groupName='" + groupName + '\'' +
				'}';
	}
}
