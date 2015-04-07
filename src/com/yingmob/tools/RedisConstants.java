package com.yingmob.tools;

/**
 * redis常量
 * 
 * @author zyz
 *
 */
public class RedisConstants {
	/**
	 * 默认分隔符
	 */
	public static final String SEPARATOR = "_";

	/**
	 * 删除缓存时间(单位天)
	 */
	public static final int DEL_CACHETIME = 24;

	/**
	 * 默认缓存时间一天(单位秒)
	 */
	public static final int CACHETIME_DAY = 3600 * 24;

	/**
	 * 默认缓存时间二天(单位秒)
	 */
	public static final int CACHETIME_2DAY = 3600 * 24 * 2;

	/**
	 * 默认缓存时间一周(单位秒)
	 */
	public static final int CACHETIME_WEEK = 3600 * 24 * 7;

	/**
	 * 默认缓存时间一月(单位秒)
	 */
	public static final int CACHETIME_MONTH = 3600 * 24 * 30;

	/**
	 * 默认缓存时间三月(单位秒)
	 */
	public static final int CACHETIME_3MONTH = 3600 * 24 * 90;

	/**
	 * 默认缓存时间六月(单位秒)
	 */
	public static final int CACHETIME_6MONTH = 3600 * 24 * 180;

	/**
	 * 默认缓存时间年(单位秒)
	 */
	public static final int CACHETIME_YEAR = 3600 * 24 * 365;

	/**
	 * 例子
	 * redis的key尽量短,但注释要写清楚
	 */
	public static final String ADS_NOICON_REPORT = "anr_";
	
	/**
	 * 广告每天安装限量key
	 */
	public static final String AD_DAY_INSTALL_LIMIT = "adil" + SEPARATOR;
	
	/**
	 * 弹出广告弹出限制key
	 */
	public static final String AD_DAY_TAN_LIMIT = "adtl" + SEPARATOR;

}
