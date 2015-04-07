package com.yingmob.tools;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

/**
 * 开发者模式判断
 * 
 * @author zyz
 *
 */
public class DevConstants {

	private static final Prop prop = PropKit.use("dev.properties");

	public static final boolean DEV_MODE = prop.getBoolean("devMode", false);
	public static final String DB_CONFIG_FILENAME = DEV_MODE ? prop.get("dbConfigDevFileName") : prop.get("dbConfigFileName");
	public static final String SYS_CONFIG_FILENAME = DEV_MODE ? prop.get("sysConfigDevFileName") : prop.get("sysConfigFileName");
	public static final String YINGMOB_CACHE_FILENAME = DEV_MODE ? prop.get("yingmobCacheDevFileName") : prop.get("yingmobCacheFileName");

}
