package com.micro4blog.data;

public class UserInfo {
	
	private long userId;
	private String userScreenName;
	private String userName;
	private int province;		// 用户所在地区ID
	private int city;			// 用户所在城市ID
	private String location;	// 用户所在地
	private String description;
	private String blogUrl;
	private String profileImageUrl;
	private String domain;		// 用户个性化域名
	private String gender;
	private int followersCount;	// 粉丝数
	private int friendsCount;	// 关注数 
	private int m4bCount;		// 已发微博数
	private int favouritesCount;
	private String createAt;
	private boolean following; 		 // 当前登录用户是否关注该用户
	private boolean allowAllActMsg;  //允许所有人给我发私信
	private boolean geoEnabled;
	private boolean verified;		// 用户个人认证
	private boolean enterpriseVerified; // 企业认证
	private boolean allowAllComment; // 是否所有人对我的微博进行评论
	private String avatarLarge;		// 用户大头像地址
	private String verifiedReason;
	private boolean followMe;		// 该用户是否关注当前登录用户
	private int onlineStatus;
	private int biFollowersCount;	// 用户互粉数
	
	private String email;
	private String birthYear;
	private String birthMonth;
	private String birthDay;
	
	private String eduId;
	private String eduYear;
	private String eduSchoolId;
	private String eduDepartmentId;
	private String eduLevel;
	
	private Micro4blogInfo m4bInfo;  // 用户最近一条微博信息段

	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserScreenName() {
		return userScreenName;
	}

	public void setUserScreenName(String userScreenName) {
		this.userScreenName = userScreenName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBlogUrl() {
		return blogUrl;
	}

	public void setBlogUrl(String blogUrl) {
		this.blogUrl = blogUrl;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public int getM4bCount() {
		return m4bCount;
	}

	public void setM4bCount(int m4bCount) {
		this.m4bCount = m4bCount;
	}

	public int getFavouritesCount() {
		return favouritesCount;
	}

	public void setFavouritesCount(int favouritesCount) {
		this.favouritesCount = favouritesCount;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public boolean isFollowing() {
		return following;
	}

	public void setFollowing(boolean following) {
		this.following = following;
	}

	public boolean isAllowAllActMsg() {
		return allowAllActMsg;
	}

	public void setAllowAllActMsg(boolean allowAllActMsg) {
		this.allowAllActMsg = allowAllActMsg;
	}

	public boolean isGeoEnabled() {
		return geoEnabled;
	}

	public void setGeoEnabled(boolean geoEnabled) {
		this.geoEnabled = geoEnabled;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public boolean isEnterpriseVerified() {
		return enterpriseVerified;
	}

	public void setEnterpriseVerified(boolean enterpriseVerified) {
		this.enterpriseVerified = enterpriseVerified;
	}

	public boolean isAllowAllComment() {
		return allowAllComment;
	}

	public void setAllowAllComment(boolean allowAllComment) {
		this.allowAllComment = allowAllComment;
	}

	public String getAvatarLarge() {
		return avatarLarge;
	}

	public void setAvatarLarge(String avatarLarge) {
		this.avatarLarge = avatarLarge;
	}

	public String getVerifiedReason() {
		return verifiedReason;
	}

	public void setVerifiedReason(String verifiedReason) {
		this.verifiedReason = verifiedReason;
	}

	public boolean isFollowMe() {
		return followMe;
	}

	public void setFollowMe(boolean followMe) {
		this.followMe = followMe;
	}

	public int isOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(int onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public int getBiFollowersCount() {
		return biFollowersCount;
	}

	public void setBiFollowersCount(int biFollowersCount) {
		this.biFollowersCount = biFollowersCount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(String birthYear) {
		this.birthYear = birthYear;
	}

	public String getBirthMonth() {
		return birthMonth;
	}

	public void setBirthMonth(String birthMonth) {
		this.birthMonth = birthMonth;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public String getEduId() {
		return eduId;
	}

	public void setEduId(String eduId) {
		this.eduId = eduId;
	}

	public String getEduYear() {
		return eduYear;
	}

	public void setEduYear(String eduYear) {
		this.eduYear = eduYear;
	}

	public String getEduSchoolId() {
		return eduSchoolId;
	}

	public void setEduSchoolId(String eduSchoolId) {
		this.eduSchoolId = eduSchoolId;
	}

	public String getEduDepartmentId() {
		return eduDepartmentId;
	}

	public void setEduDepartmentId(String eduDepartmentId) {
		this.eduDepartmentId = eduDepartmentId;
	}

	public String getEduLevel() {
		return eduLevel;
	}

	public void setEduLevel(String eduLevel) {
		this.eduLevel = eduLevel;
	}

	public Micro4blogInfo getM4bInfo() {
		return m4bInfo;
	}

	public void setM4bInfo(Micro4blogInfo m4bInfo) {
		this.m4bInfo = m4bInfo;
	}
	
	
	

}
