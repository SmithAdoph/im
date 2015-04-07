package com.open.welinks.model;

public class API {

	public static String API_DOMAIN = "http://192.168.1.2/";// http://www.we-links.com/ 192.168.1.14 192.168.0.102
	public static String API_LBS_ACCOUNT_DOMAIN = "http://123.57.58.84/";// 123.57.58.84
	public static String API_LBS_SHARE_DOMAIN = "http://192.168.1.2/";// 182.92.107.229
	public static String API_LBS_GROUP_DOMAIN = "http://123.57.56.119/";// 123.57.56.119
	/**
	 * http://images2.we-links.com/
	 */
	public static String DOMAIN_COMMONIMAGE = "http://images2.we-links.com/";
	/**
	 * http://images3.we-links.com/
	 */
	public static String DOMAIN_OSS_THUMBNAIL = "http://images3.we-links.com/";
	/**
	 * http://www.we-links.com/api2/bug/send
	 */
	public static String BUG_SEND = API_DOMAIN + "api2/bug/send";

	// account
	/**
	 * http://www.we-links.com/api2/account/modify
	 */
	public static String ACCOUNT_MODIFY = API_DOMAIN + "api2/account/modify";
	/**
	 * http://www.we-links.com/api2/account/auth
	 */
	public static String ACCOUNT_AUTH = API_DOMAIN + "api2/account/auth";
	/**
	 * http://www.we-links.com/api2/account/verifycode
	 */
	public static String ACCOUNT_VERIFYCODE = API_DOMAIN + "api2/account/verifycode";
	/**
	 * http://www.we-links.com/api2/account/verifyphone
	 */
	public static String ACCOUNT_VERIFYPHONE = API_DOMAIN + "api2/account/verifyphone";
	/**
	 * http://www.we-links.com/api2/account/get
	 */
	public static String ACCOUNT_GET = API_DOMAIN + "api2/account/get";

	// group
	/**
	 * http://www.we-links.com/api2/group/create
	 */
	public static String GROUP_CREATE = API_DOMAIN + "api2/group/create";
	/**
	 * http://www.we-links.com/api2/group/getgroupmembers
	 */
	public static String GROUP_GETGROUPMEMBERS = API_DOMAIN + "api2/group/getgroupmembers";
	/**
	 * http://www.we-links.com/api2/group/addmembers
	 */
	public static String GROUP_ADDMEMBERS = API_DOMAIN + "api2/group/addmembers";
	/**
	 * http://www.we-links.com/api2/group/removemembers
	 */
	public static String GROUP_REMOVEMEMBERS = API_DOMAIN + "api2/group/removemembers";
	/**
	 * http://www.we-links.com/api2/group/modify
	 */
	public static String GROUP_MODIFY = API_DOMAIN + "api2/group/modify";
	/**
	 * http://www.we-links.com/api2/group/modifygroupcirclesequence
	 */
	public static String GROUP_MODIFYGROUPCIRCLESEQUENCE = API_DOMAIN + "api2/group/modifygroupcirclesequence";
	/**
	 * http://www.we-links.com/api2/group/get
	 */
	public static String GROUP_GET = API_DOMAIN + "api2/group/get";
	/**
	 * http://www.we-links.com/api2/group/getallmembers
	 */
	public static String GROUP_GETALLMEMBERS = API_DOMAIN + "api2/group/getallmembers";
	/**
	 * http://www.we-links.com/api2/group/creategroupcircle
	 */
	public static String GROUP_CREATEGROUPCIRCLE = API_DOMAIN + "api2/group/creategroupcircle";
	/**
	 * http://www.we-links.com/api2/group/deletegroupcircle
	 */
	public static String GROUP_DELETEGROUPCIRCLE = API_DOMAIN + "api2/group/deletegroupcircle";
	/**
	 * http://www.we-links.com/api2/group/modifygroupcircle
	 */
	public static String GROUP_MODIFYGROUPCIRCLE = API_DOMAIN + "api2/group/modifygroupcircle";
	/**
	 * http://www.we-links.com/api2/group/movegroupcirclegroups
	 */
	public static String GROUP_MOVEGROUPCIRCLEGROUPS = API_DOMAIN + "api2/group/movegroupcirclegroups";

	/**
	 * http://www.we-links.com/api2/group/movegroupstocircle
	 */
	public static String GROUP_MOVEGROUPSTOCIRCLE = API_DOMAIN + "api2/group/movegroupstocircle";
	/**
	 * http://www.we-links.com/api2/group/follow 关注群组
	 */
	public static String GROUP_FOLLOW = API_DOMAIN + "api2/group/follow";

	// label
	/**
	 * http://www.we-links.com/api2/label/modifygrouplabel
	 */
	public static String LABEL_MODIFYGROUPLABEL = API_DOMAIN + "api2/label/modifygrouplabel";
	/**
	 * http://www.we-links.com/api2/label/getgrouplabels
	 */
	public static String LABEL_GETGROUPLABELS = API_DOMAIN + "api2/label/getgrouplabels";
	/**
	 * http://www.we-links.com/api2/label/gethotlabels
	 */
	public static String LABEL_GETHOTLABELS = API_DOMAIN + "api2/label/gethotlabels";
	/**
	 * http://www.we-links.com/api2/label/getlabelsgroups
	 */
	public static String LABEL_GETLABELSGROUPS = API_DOMAIN + "api2/label/getlabelsgroups";

	// share
	/**
	 * http://www.we-links.com/api2/share/sendboardshare
	 */
	public static String SHARE_SENDSHARE = API_DOMAIN + "api2/share/sendboardshare";
	/**
	 * http://www.we-links.com/api2/share/getboardshares
	 */
	public static String SHARE_GETSHARES = API_DOMAIN + "api2/share/getboardshares";
	/**
	 * http://www.we-links.com/api2/share/getboardshare
	 */
	public static String SHARE_GETBOARDSHARE = API_DOMAIN + "api2/share/getboardshare";
	/**
	 * http://www.we-links.com/api2/share/getusershares
	 */
	public static String SHARE_GETUSERSHARES = API_DOMAIN + "api2/share/getusershares";
	/**
	 * http://www.we-links.com/api2/share/score
	 */
	public static String SHARE_SCORE = API_DOMAIN + "api2/share/score";
	/**
	 * http://www.we-links.com/api2/share/addcomment
	 */
	public static String SHARE_ADDCOMMENT = API_DOMAIN + "api2/share/addcomment";
	/**
	 * http://www.we-links.com/api2/share/delete
	 */
	public static String SHARE_DELETE = API_DOMAIN + "api2/share/delete";
	/**
	 * http://www.we-links.com/api2/share/getgroupboards
	 */
	public static String SHARE_GETGROUPBOARDS = API_DOMAIN + "api2/share/getgroupboards";
	/**
	 * http://www.we-links.com/api2/share/addboard
	 */
	public static String SHARE_ADDBOARD = API_DOMAIN + "api2/share/addboard";
	/**
	 * http://www.we-links.com/api2/share/modifyboard
	 */
	public static String SHARE_MODIFYBOARD = API_DOMAIN + "api2/share/modifyboard";
	/**
	 * http://www.we-links.com/api2/share/deleteboard
	 */
	public static String SHARE_DELETEBOARD = API_DOMAIN + "api2/share/deleteboard";
	/**
	 * http://www.we-links.com/api2/share/modifysquence
	 */
	public static String SHARE_MODIFYSQUENCE = API_DOMAIN + "api2/share/modifysquence";
	// message
	/**
	 * http://www.we-links.com/api2/message/send
	 */
	public static String MESSAGE_SEND = API_DOMAIN + "api2/message/send";
	/**
	 * http://www.we-links.com/api2/message/get
	 */
	public static String MESSAGE_GET = API_DOMAIN + "api2/message/get";

	// session
	/**
	 * http://www.we-links.com/api2/session/event
	 */
	public static String SESSION_EVENT = API_DOMAIN + "api2/session/event";

	// relation
	/**
	 * http://www.we-links.com/api2/relation/modifyalias
	 */
	public static String RELATION_MODIFYALIAS = API_DOMAIN + "api2/relation/modifyalias";
	/**
	 * http://www.we-links.com/api2/relation/deletefriend
	 */
	public static String RELATION_DELETEFRIEND = API_DOMAIN + "api2/relation/deletefriend";
	/**
	 * http://www.we-links.com/api2/relation/blacklist
	 */
	public static String RELATION_BLACKLIST = API_DOMAIN + "api2/relation/blacklist";
	/**
	 * http://www.we-links.com/api2/relation/fellow
	 */
	public static String RELATION_FOLLOW = API_DOMAIN + "api2/relation/follow";
	/**
	 * http://www.we-links.com/api2/relation/modifysequence
	 */
	public static String RELATION_MODIFYCIRCLESEQUENCE = API_DOMAIN + "api2/relation/modifysequence";
	/**
	 * http://www.we-links.com/api2/relation/intimatefriends
	 */
	public static String RELATION_GETINTIMATEFRIENDS = API_DOMAIN + "api2/relation/intimatefriends";

	/**
	 * http://www.we-links.com/api2/relation/updatecontact
	 */
	public static String RELATION_UPDATECONTACT = API_DOMAIN + "api2/relation/updatecontact";
	/**
	 * http://www.we-links.com/api2/relation/modifycircle
	 */
	public static String RELATION_MODIFYCIRCLE = API_DOMAIN + "api2/relation/modifycircle";

	// circle
	/**
	 * http://www.we-links.com/api2/circle/modify
	 */
	public static String CIRCLE_MODIFY = API_DOMAIN + "api2/circle/modify";
	/**
	 * http://www.we-links.com/api2/circle/delete
	 */
	public static String CIRCLE_DELETE = API_DOMAIN + "api2/circle/delete";
	/**
	 * http://www.we-links.com/api2/circle/createcircle
	 */
	public static String CIRCLE_CREATECIRCLE = API_DOMAIN + "api2/circle/createcircle";

	// lbs
	/**
	 * http://www.we-links.com/api2/lbs/search
	 */
	public static String LBS_ACCOUNT_SEARCH = API_LBS_ACCOUNT_DOMAIN + "api2/lbs/search";
	public static String LBS_SHARE_SEARCH = API_LBS_SHARE_DOMAIN + "api2/lbs/search";
	public static String LBS_GROUP_SEARCH = API_LBS_GROUP_DOMAIN + "api2/lbs/search";
	public static String LBS_ACCOUNT_GETNEWEST = API_LBS_ACCOUNT_DOMAIN + "api2/lbs/getnewest";
	// image

	/**
	 * http://www.we-links.com/image/checkfileexist
	 */
	public static String IMAGE_CHECKFILEEXIST = API_DOMAIN + "image/checkfileexist";
	/**
	 * http://www.we-links.com/image/uploadfilename
	 */
	public static String IMAGE_UPLOADFILENAME = API_DOMAIN + "image/uploadfilename";
	// lbs
	public static String LBS_DATA_CREATE = "http://yuntuapi.amap.com/datamanage/data/create";
	public static String LBS_DATA_UPDATA = "http://yuntuapi.amap.com/datamanage/data/update";
	public static String LBS_DATA_SEARCH = "http://yuntuapi.amap.com/datamanage/data/list";
	public static String LBS_DATA_DELETE = "http://yuntuapi.amap.com/datamanage/data/delete";
	public static String LBS_DATASEARCH_AROUND = "http://yuntuapi.amap.com/datasearch/around";

}
