package com.example.user.music.entity;

//关注列表
public class WatchList {

	private int listId;
	private int userId;
	private int watchId;
	private String watchName;

	public int getListId() {
		return listId;
	}

	public void setListId(int listId) {
		this.listId = listId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getWatchId() {
		return watchId;
	}

	public void setWatchId(int watchId) {
		this.watchId = watchId;
	}

	public String getWatchName() {
		return watchName;
	}

	public void setWatchName(String watchName) {
		this.watchName = watchName;
	}
}
