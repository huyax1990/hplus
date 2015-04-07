package com.yingmob.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.jfinal.kit.EncryptionKit;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;

/**
 * CDN手工缓存刷新工具
 * 
 * @author zyz
 *
 */
public class CDNTools {

	private static Logger LOG = Logger.getLogger(CDNTools.class);

	/**
	 * CND刷新缓存地址
	 */
	public static final String CDN_URL = "http://purge.upyun.com/purge/";

	/**
	 * 刷新n个CDN地址
	 * 
	 * @param fileCNDUrls
	 *            地址必须为http开头
	 */
	public static void refreshFiles(String... fileCNDUrls) {
		try {
			if (StrKit.notBlank(fileCNDUrls)) {
				StringBuilder sb = new StringBuilder("");
				for (String url : fileCNDUrls) {
					sb.append(url).append("\n");
				}
				String purge = sb.substring(0, sb.length() - 1);
				String gmtDate = getGMTDate();
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Date", gmtDate);
				headers.put("Authorization", "UpYun " + SysConstants.CDN_SPACENAME + ":" + SysConstants.CDN_USERNAME + ":" + sign(purge, gmtDate));
				HttpKit.post(CDN_URL, "purge=" + purge, headers);
			}
			LOG.debug("CNDTools-refreshFiles[成功]");
		} catch (Exception e) {
			LOG.error("CNDTools-refreshFiles[失败]", e);
		}
	}

	/**
	 * 获取GMT时间
	 * 
	 * @return
	 */
	public static String getGMTDate() {
		SimpleDateFormat formater = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
		formater.setTimeZone(TimeZone.getTimeZone("GMT"));
		return formater.format(new Date());
	}

	/**
	 * 获取加密标记
	 * 
	 * @param purge
	 * @param date
	 * @return
	 */
	private static String sign(String purge, String date) {
		String tmp = purge + "&" + SysConstants.CDN_SPACENAME + "&" + date + "&" + EncryptionKit.md5Encrypt(SysConstants.CDN_PASSWORD);
		return EncryptionKit.md5Encrypt(tmp);
	}

	public static void main(String[] args) {
		// 例子
//		CDNTools.refreshFiles("http://dapian-cdn.b0.upaiyun.com/plist/bc71faa66bdf4bbd958927baa22d92f9.plist");
		CDNTools.refreshFiles("http://dapian-cdn.b0.upaiyun.com/plist/bc71faa66bdf4bbd958927baa22d92f9.plist");
//		CDNTools.refreshFiles("http://dapian-cdn.b0.upaiyun.com/a1.txt", "http://dapian-cdn.b0.upaiyun.com/b2.txt");
	}
}
