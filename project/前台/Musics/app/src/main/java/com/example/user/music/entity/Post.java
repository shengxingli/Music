package com.example.user.music.entity;

public class Post {

	private int postId;
	private String postName;
	private String postCover;
	private String postUrl;
	private String description;
	private int userId;
	private int likeCont;

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getLikeCont() {
		return likeCont;
	}

	public void setLikeCont(int likeCont) {
		this.likeCont = likeCont;
	}

	public String getPostCover() {
		return postCover;
	}

	public void setPostCover(String postCover) {
		this.postCover = postCover;
	}

	public String getPostUrl() {
		return postUrl;
	}

	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}
}
