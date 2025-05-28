package com.example.eg_sns.dto;

public class ResponseComments {

	private Long id;
	private String comment;
	private String userName;
	private String userIconUri;
	private String userLoginId;
	private Long postsId;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserIconUri() {
		return userIconUri;
	}
	public void setUserIconUri(String userIconUri) {
		this.userIconUri = userIconUri;
	}

	public String getUserLoginId() {
		return userLoginId;
	}
	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}
	public Long getPostsId() {
		return postsId;
	}
	public void setPostsId(Long postsId) {
		this.postsId = postsId;
	}
}
