package com.example.user.music.entity;

public class Music {

	private int musicId;
	/** 歌曲名称 */
	private String musicName;
	/** 歌手名称 */
	private String musicSingerName;
	/** 歌手id */
	private int musicSingerId;
	private int countCollected;
	private String musicUrl;


	public int getMusicId() {
		return musicId;
	}

	public void setMusicId(int musicId) {
		this.musicId = musicId;
	}

	public String getMusicName() {
		return musicName;
	}

	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}

	public String getMusicSingerName() {
		return musicSingerName;
	}

	public void setMusicSingerName(String musicSingerName) {
		this.musicSingerName = musicSingerName;
	}

	public int getMusicSingerId() {
		return musicSingerId;
	}

	public void setMusicSingerId(int musicSingerId) {
		this.musicSingerId = musicSingerId;
	}

	public int getCountCollected() {
		return countCollected;
	}

	public void setCountCollected(int countCollected) {
		this.countCollected = countCollected;
	}

	public String getMusicUrl() {
		return musicUrl;
	}

	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}
}
