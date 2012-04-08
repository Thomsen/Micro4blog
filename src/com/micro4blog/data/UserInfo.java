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
	private boolean onlineStatus;
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
	

}
