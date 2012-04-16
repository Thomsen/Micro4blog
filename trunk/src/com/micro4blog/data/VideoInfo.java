package com.micro4blog.data;

public class VideoInfo {

	private String videoPicurl;		// 缩略图
	private String videoPlayer;		// 播放器地址
	private String videoRealUrl;	// 视频源地址
	private String videoShortUrl;	// 视频的短地址
	private String videoTitle;		// 视频标题
	
	
	public String getVideoPicurl() {
		return videoPicurl;
	}
	public void setVideoPicurl(String videoPicurl) {
		this.videoPicurl = videoPicurl;
	}
	public String getVideoPlayer() {
		return videoPlayer;
	}
	public void setVideoPlayer(String videoPlayer) {
		this.videoPlayer = videoPlayer;
	}
	public String getVideoRealUrl() {
		return videoRealUrl;
	}
	public void setVideoRealUrl(String videoRealUrl) {
		this.videoRealUrl = videoRealUrl;
	}
	public String getVideoShortUrl() {
		return videoShortUrl;
	}
	public void setVideoShortUrl(String videoShortUrl) {
		this.videoShortUrl = videoShortUrl;
	}
	public String getVideoTitle() {
		return videoTitle;
	}
	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}
	
	
}
