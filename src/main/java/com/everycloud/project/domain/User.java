package com.everycloud.project.domain;

public class User {
	private String userId;
	private String userPass;
	private String userNickname;
	private String userEmail;
	private String userAuthority;	
	
	public User() {}
	
	public User(String userId, String userPass, String userNickname, String userEmail, String userAuthority) {
		this.userId = userId;
		this.userPass = userPass;
		this.userNickname = userNickname;
		this.userEmail = userEmail;
		this.userAuthority = userAuthority;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}
	
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserAuthority() {
		return userAuthority;
	}

	public void setUserAuthority(String userAuthority) {
		this.userAuthority = userAuthority;
	}

	@Override
	public String toString() {
		return "User{" +
				"userId='" + userId + '\'' +
				", userPass='" + userPass + '\'' +
				", userNickname='" + userNickname + '\'' +
				", userEmail='" + userEmail + '\'' +
				", userAuthority='" + userAuthority + '\'' +
				'}';
	}
}
