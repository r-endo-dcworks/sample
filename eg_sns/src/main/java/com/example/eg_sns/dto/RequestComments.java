package com.example.eg_sns.dto;

public class RequestComments {

	private Long postsId;
	private Long usersId;
	private String comment;

	// getter, setter

	 public Long getPostsId() { return postsId; }
	    public void setPostsId(Long postsId) { this.postsId = postsId; }

	    public Long getUsersId() { return usersId; }
	    public void setUsersId(Long usersId) { this.usersId = usersId; }

	    public String getComment() { return comment; }
	    public void setComment(String comment) { this.comment = comment; }
}
