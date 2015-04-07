/**
 * www.yingmob.com Inc.
 * Copyright (c) 2014 All Rights Reserved.
 */
package com.yingmob.tools.plist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.dd.plist.PropertyListParser;
import com.jfinal.log.Logger;

/**
 * 
 * @Filename PlistUtil.java
 *
 * @Description
 *
 * @Version 1.0
 *
 * @Author huqing
 *
 * @Email 492988286@qq.com
 * 
 * @History
 *          <li>Author: huqing</li> <li>Date: 2014年7月30日</li> <li>Version: 1.0</li> <li>Content: create</li>
 *
 */
public class PlistUtil {

	private static final Logger log = Logger.getLogger(PlistUtil.class);

	/**
	 * @param ipaPath ipa文件路径
	 * @param ipaFileName ipa文件名
	 * @return BundleIdentifier
	 * @throws Exception
	 *
	 *             获取ipa文件中 BundleIdentifier
	 *             基本思路:
	 *             1 将 ipa 改成 zip
	 *             2 解压 zip
	 *             3 找到Info.plist文件
	 *             4 windows下用dd-plist jar包/linux下用 perl plutl 来解析Info.plist文件
	 *             5 清理过程中产生的垃圾文件
	 */
	public static String resolveBundleId(String ipaPath, String ipaFileName) throws Exception {

		ipaPath += "/";
		// 压缩ipa为zip 
		String zipIpa = zipIpa(ipaPath, ipaFileName);

		// 解压 ipa.zip
		int i = zipIpa.lastIndexOf(".");
		String unzipFile = zipIpa.substring(0, i);
		ZipUtil.unzip(zipIpa, unzipFile);

		// 找到Info.plist文件
		List<File> resultList = new ArrayList<File>();
		FileSearcher.findFiles(zipIpa.substring(0, i), "Info.plist", resultList);

		log.debug("找到Info.plist文件 resultList : " + resultList);

		if (resultList == null || resultList.size() == 0) {
			return null;
		}

		String plist = minplist(resultList);
		log.debug("plist 路径 : " + plist);

		i = plist.lastIndexOf(File.separator);
		String bundleId = getBundleIdentifier(plist.substring(0, i), plist.substring(i + 1, plist.length()));

		// 清理文件 
		File f = new File(zipIpa);
		if (f.isFile() && f.exists()) {
			log.debug("删除 文件 zipIpa " + zipIpa);
			f.delete();
		}
		log.debug("删除 文件 unzipFile " + unzipFile);
		FileUtils.deleteDirectory(new File(unzipFile));

		return bundleId;
	}

	/**
	 * 压缩IPA
	 */
	private static String zipIpa(String srcPath, String ipaFileName) throws IOException {

		String tmpFileName = srcPath + (System.currentTimeMillis() + 100) + ".ipa";
		File src = new File(srcPath + ipaFileName);

		File tmp = new File(tmpFileName);
		FileUtils.copyFile(src, tmp);

		String zipFile = tmpFileName.substring(0, tmpFileName.lastIndexOf(".")) + ".zip";

		tmp.renameTo(new File(zipFile));
		return zipFile;
	}

	/**
	 * 获取plist文件中BundleIdentifier
	 **/
	@SuppressWarnings("deprecation")
	private static String getBundleIdentifier(String plistPath, String plistFileName) throws Exception {

		String bundleId = null;
		File file = new File(plistPath + File.separator + plistFileName);
		File file1 = new File(plistPath + File.separator + "_tmp" + plistFileName);

		String osname = System.getProperties().getProperty("os.name");

		SAXBuilder sb = new SAXBuilder();
		sb.setValidation(false);
		sb.setEntityResolver(new NoOpEntityResolver());

		Document doc = null;
		log.debug("--------------- os name " + osname);
		if (osname.trim().toLowerCase().startsWith("windows")) {
			// windows
			PropertyListParser.convertToXml(file, file1);
		} else {
			// linux 
			// 用java调用 perl(plutil)获取Info.plist文件的内容 并重新成为一个xml文件
			String pfile = plistPath + File.separator + plistFileName;
			String pfile1 = plistPath + File.separator + System.currentTimeMillis() + "_" + plistFileName;
			String plistContent = JavaCallPerl.plist(pfile);
			file1 = new File(pfile1);
			FileOutputStream out = new FileOutputStream(file1);
			out.write(plistContent.getBytes());
			out.close();

		}

		log.debug("plist file :" + file1);
		doc = sb.build(new FileInputStream(file1)); //构造文档对象

		Element root = doc.getRootElement(); //获取根元素
		List<Element> list = root.getChildren("dict").get(0).getChildren();
		for (int i = 0; i < list.size(); i++) {
			Element e = list.get(i);
			if (e.getText().toLowerCase().equals("CFBundleIdentifier".toLowerCase())) {
				bundleId = list.get(i + 1).getText();
			}
		}
		// 清理过程中产生的文件
		file1.deleteOnExit();
		return bundleId;
	}

	/**
	 * 找出Info.plist路径最短的文件 (解压后有多个Info.plist文件)
	 */
	private static String minplist(List<File> plists) {

		String p = plists.get(0).toString();
		for (int i = 1; i < plists.size(); i++) {
			if (p.length() > plists.get(i).toString().length()) {
				p = plists.get(i).toString();
			}
		}
		return p;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("windows 7".startsWith("windows"));
		/*
		 * SAXBuilder sb = new SAXBuilder();
		 * Document doc = null;
		 * File file1 = new File("D:/1/Info.plist");
		 * doc = sb.build(new FileInputStream(file1)); //构造文档对象
		 * 
		 * Element root = doc.getRootElement(); //获取根元素
		 * List<Element> list = root.getChildren("dict").get(0).getChildren();
		 * for (int i = 0; i < list.size(); i++) {
		 * Element e = list.get(i);
		 * if (e.getText().toLowerCase().equals("CFBundleIdentifier".toLowerCase())) {
		 * System.out.println(list.get(i + 1).getText());
		 * }
		 * }
		 */}
}
