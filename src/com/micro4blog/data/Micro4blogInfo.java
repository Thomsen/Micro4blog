package com.micro4blog.data;

public class Micro4blogInfo {
	
	private String m4bStrId;
	private String m4bCreateAt;
	private int m4bId;
	private String m4bText;
	private String m4bSource;
	private boolean m4bFovorited;
	private boolean m4bTruncated;
	private int m4bInReplyToStatusId;
	private int m4bInReplyToUserId;
	private String m4bInReplyToScreenName;
	private int m4bMid;
	private String m4bMiddlePicture;
	private String m4bOriginPicture;
	private String m4bThumbnailPic;
	private int m4bRetweetCount;
	private int m4bCommentCount;
	private int m4bType;
	private int m4bStatus;
	
	private int hasRefresh;		// 有微博更新
	private int hasSelf;		// 是否是自己发的微博
	
	private VideoInfo videoInfo;
	private MusicInfo musicInfo;
	private EmotionInfo emotionInfo;	
	private UserInfo userInfo;
	
	private Micro4blogInfo m4bRetweetInfo;
	private boolean hasRetweet;
	
	
	public boolean isHasRetweet() {
		return hasRetweet;
	}
	public void setHasRetweet(boolean hasRetweet) {
		this.hasRetweet = hasRetweet;
	}
	public Micro4blogInfo getM4bRetweetInfo() {
		return m4bRetweetInfo;
	}
	public void setM4bRetweetInfo(Micro4blogInfo m4bRetweetInfo) {
		this.m4bRetweetInfo = m4bRetweetInfo;
	}
	public String getM4bStrId() {
		return m4bStrId;
	}
	public void setM4bStrId(String m4bStrId) {
		this.m4bStrId = m4bStrId;
	}
	public String getM4bCreateAt() {
		return m4bCreateAt;
	}
	public void setM4bCreateAt(String m4bCreateAt) {
		this.m4bCreateAt = m4bCreateAt;
	}
	public int getM4bId() {
		return m4bId;
	}
	public void setM4bId(int m4bId) {
		this.m4bId = m4bId;
	}
	public String getM4bText() {
		return m4bText;
	}
	public void setM4bText(String m4bText) {
		this.m4bText = m4bText;
	}
	public String getM4bSource() {
		return m4bSource;
	}
	public void setM4bSource(String m4bSource) {
		this.m4bSource = m4bSource;
	}
	public boolean isM4bFovorited() {
		return m4bFovorited;
	}
	public void setM4bFovorited(boolean m4bFovorited) {
		this.m4bFovorited = m4bFovorited;
	}
	public boolean isM4bTruncated() {
		return m4bTruncated;
	}
	public void setM4bTruncated(boolean m4bTruncated) {
		this.m4bTruncated = m4bTruncated;
	}
	public int getM4bInReplyToStatusId() {
		return m4bInReplyToStatusId;
	}
	public void setM4bInReplyToStatusId(int m4bInReplyToStatusId) {
		this.m4bInReplyToStatusId = m4bInReplyToStatusId;
	}
	public int getM4bInReplyToUserId() {
		return m4bInReplyToUserId;
	}
	public void setM4bInReplyToUserId(int m4bInReplyToUserId) {
		this.m4bInReplyToUserId = m4bInReplyToUserId;
	}
	public String getM4bInReplyToScreenName() {
		return m4bInReplyToScreenName;
	}
	public void setM4bInReplyToScreenName(String m4bInReplyToScreenName) {
		this.m4bInReplyToScreenName = m4bInReplyToScreenName;
	}
	public int getM4bMid() {
		return m4bMid;
	}
	public void setM4bMid(int m4bMid) {
		this.m4bMid = m4bMid;
	}
	public String getM4bMiddlePicture() {
		return m4bMiddlePicture;
	}
	public void setM4bMiddlePicture(String m4bMiddlePicture) {
		this.m4bMiddlePicture = m4bMiddlePicture;
	}
	public String getM4bOriginPicture() {
		return m4bOriginPicture;
	}
	public void setM4bOriginPicture(String m4bOriginPicture) {
		this.m4bOriginPicture = m4bOriginPicture;
	}
	public String getM4bThumbnailPic() {
		return m4bThumbnailPic;
	}
	public void setM4bThumbnailPic(String m4bThumbnailPic) {
		this.m4bThumbnailPic = m4bThumbnailPic;
	}
	public int getM4bRetweetCount() {
		return m4bRetweetCount;
	}
	public void setM4bRetweetCount(int m4bRetweetCount) {
		this.m4bRetweetCount = m4bRetweetCount;
	}
	public int getM4bCommentCount() {
		return m4bCommentCount;
	}
	public void setM4bCommentCount(int m4bCommentCount) {
		this.m4bCommentCount = m4bCommentCount;
	}
	public int getM4bType() {
		return m4bType;
	}
	public void setM4bType(int m4bType) {
		this.m4bType = m4bType;
	}
	public int getM4bStatus() {
		return m4bStatus;
	}
	public void setM4bStatus(int m4bStatus) {
		this.m4bStatus = m4bStatus;
	}
	public int getHasRefresh() {
		return hasRefresh;
	}
	public void setHasRefresh(int hasRefresh) {
		this.hasRefresh = hasRefresh;
	}
	public int getHasSelf() {
		return hasSelf;
	}
	public void setHasSelf(int hasSelf) {
		this.hasSelf = hasSelf;
	}
	public VideoInfo getVideoInfo() {
		return videoInfo;
	}
	public void setVideoInfo(VideoInfo videoInfo) {
		this.videoInfo = videoInfo;
	}
	public MusicInfo getMusicInfo() {
		return musicInfo;
	}
	public void setMusicInfo(MusicInfo musicInfo) {
		this.musicInfo = musicInfo;
	}
	public EmotionInfo getEmotionInfo() {
		return emotionInfo;
	}
	public void setEmotionInfo(EmotionInfo emotionInfo) {
		this.emotionInfo = emotionInfo;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	

}
