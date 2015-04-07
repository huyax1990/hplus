package com.yingmob.tools;

import java.net.URLDecoder;

import com.jfinal.log.Logger;
import com.maxmind.geoip.LookupService;

/**
 * 根据IP地址查询国家code
 * 
 */
public class GeoIPTools {

	private static final Logger LOG = Logger.getLogger(GeoIPTools.class);

	/**
	 * 定义IP库
	 */
	private static LookupService ls;

	static {
		try {
			// 获取资源文件路径
			String dbfile = URLDecoder.decode(GeoIPTools.class.getResource("/GeoIP.dat").getFile(), "UTF-8");
			// 初始化IP库
			ls = new LookupService(dbfile, LookupService.GEOIP_MEMORY_CACHE);
			LOG.debug("IP库初始化完成");
		} catch (Exception e) {
			LOG.error("IP库初始化失败", e);
		}
	}

	/**
	 * 根据IP地址返回对应的国家代码
	 * 
	 * @param ip
	 * @return CountryCode
	 */
	public static String getCountryCode(String ip) {
		return ls.getCountry(ip).getCode();
	}

}
