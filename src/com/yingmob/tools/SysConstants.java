package com.yingmob.tools;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class SysConstants {

	private static final Prop prop = PropKit.use(DevConstants.SYS_CONFIG_FILENAME);

	/**
	 * controller不限制的url(不带方法名)
	 */
	public static final String[] CONTROLLER_UNLIMIT_URL = prop.get("controllerUnLimitUrl", "").split(";");

	/**
	 * action不限制的url(带方法名)
	 */
	public static final String[] ACTION_UNLIMIT_URL = prop.get("actionUnLimitUrl", "").split(";");

	/**
	 * 默认用户密码
	 */
	public static final String DEFAULT_PASSWORD = "111111";

	/**
	 * DEBUG模式
	 */
	public static final boolean DEBUG = false;

	/**
	 * 是否压缩HTML代码
	 */
	public static final boolean HTML_IS_COMPRESS = false;

	/**
	 * session user
	 */
	public static final String SESSION_USER = "sessionUser";

	/**
	 * 视图默认路径
	 */
	public static final String VIEW_BASE_PATH = "biz-logic";

	/**
	 * 接口权限密匙
	 */
	public static final String AUTH_LOCAL_KEY = prop.get("authLocalKey", "!hy@jszs#jk$");

	/**
	 * 统计提前时间时间
	 */
	public static final int STATS_BEFORE_TIME = prop.getInt("statsBeforeTime", 1);

	/**
	 * 请求时间与服务器时间间隔
	 */
	public static final int AUTH_INTERVAL_TIME = prop.getInt("authIntervalTime", 60000);

	/**
	 * 图标CDN下载地址
	 */
	public static final String ADS_NOICON_DOWNLOAD_URL = prop.get("adsNoiconDownloadUrl", "http://iosads.cdn.bb800.com/");

	/**
	 * 豆瓣抓取正在上映地区集合
	 * 
	 */
	public static String[] DOUBAN_DIQU_ARRAY = prop.get("videoCity", "beijing;shanghai;").split(";");

	/**
	 * plist文件的存放地址
	 */
	public static final String PLIST_DIR = prop.get("plistDir");

	/**
	 * ipaDir ipa的存放地址
	 */
	public static final String IPA_DIR = prop.get("ipaDir");

	/**
	 * videoDir 视频文件的存放地址
	 */
	public static final String VIDEO_DIR = prop.get("videoDir");

	/**
	 * 图片Dir 图片文件的存放地址
	 */
	public static final String IMG_DIR = prop.get("imgDir");

	/**
	 * cdn的目录地址（http）
	 */
	public static final String CDN_HTTP_URL = prop.get("cdnHttpUrl");

	/**
	 * cdn的目录地址（https）
	 */
	public static final String CDN_HTTPS_URL = prop.get("cdnHttpsUrl");

	/**
	 * 默认上传的产品的bundleId
	 */
	public static final String APP_BUNDLEID = prop.get("appBundleId");

	/**
	 * 文件上传大小限制 默认200M
	 */
	public static final int MAX_POST_SIZE = prop.getInt("maxPostSize", 200 * 1024 * 1024);

	/**
	 * CDN空间名称
	 */
	public static final String CDN_SPACENAME = prop.get("cdnSpaceName", "dapian-cdn");

	/**
	 * CDN用户名
	 */
	public static final String CDN_USERNAME = prop.get("cdnUserName", "zouyizhu");

	/**
	 * CDN密码
	 */
	public static final String CDN_PASSWORD = prop.get("cdnPassword", "zouyizhu1122");

	/**
	 * 
	 * 默认国家版本
	 * 
	 */
	public static final String COUNTRY_CODE = prop.get("countryCode", "CN");
	
	/**
	 * 默认的app名称
	 */
	public static final String APP_NAME = "快播私密版";
}
