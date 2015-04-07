package com.yingmob.tools;

import java.net.URLEncoder;
import java.text.MessageFormat;

import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.jfinal.log.Logger;

/**
 * 翻译工具
 * 
 */
public class GoogleTools {

	private static Logger LOG = Logger.getLogger(GoogleTools.class);
	protected static final String URL_TEMPLATE = "http://translate.google.com/?langpair={0}&text={1}";
	protected static final String ID_RESULTBOX = "result_box";
	protected static final String ENCODING = "UTF-8";
	protected static final String AUTO = "auto";

	/**
	 * 翻译
	 *
	 * @param text
	 * @param targetLang 翻译成的语言code
	 * @return
	 */
	public static String translate(String text, String targetLang) {
		return translate(text, AUTO, targetLang);
	}

	/**
	 * 翻译
	 * 
	 * @param text
	 * @param srcLang 源语言code
	 * @param targetLang 翻译成的语言code
	 * @return
	 */
	public static String translate(String text, String srcLang, String targetLang) {
		try {
			String url = MessageFormat.format(URL_TEMPLATE, URLEncoder.encode(srcLang + "|" + targetLang, ENCODING), URLEncoder.encode(text, ENCODING));
			Document doc = Jsoup.parse(Request.Get(url).execute().returnContent().asString(), ENCODING);
			return doc.getElementById(ID_RESULTBOX).text();
		} catch (Exception e) {
			LOG.error("GoogleTools-translate[失败]:text=" + text + ",targetLang=" + targetLang, e);
			return text;
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(translate("去吧,皮卡丘", "en"));
		System.out.println(translate("去吧,皮卡丘", "ru"));
	}
}
