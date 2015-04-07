package com.yingmob.tools;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.DocType;
import org.jdom2.Element;

import com.jfinal.log.Logger;

/**
 * 
 * 创建生成plist文件
 * 
 * @author lx
 *
 */
public class PistFileUtil {

	private static Logger LOG = Logger.getLogger(PistFileUtil.class);

	public static Object creatPlistFile(Map<String, Object> target, String tempSavePath, String accessPath) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Element root = new Element("plist");
			root.setAttribute("version", "1.0");
			Element key = new Element("key");
			key.addContent("items");
			Element dict = new Element("dict");
			Element array = new Element("array");
			Element dict1 = new Element("dict");
			Element key11 = new Element("key");
			key11.addContent("assets");
			Element array11 = new Element("array");
			Element dict111 = new Element("dict");
			Element key1111 = new Element("key");
			key1111.addContent("kind");
			Element string1111 = new Element("string");
			string1111.addContent("software-package");
			Element key1112 = new Element("key");
			key1112.addContent("url");
			Element string1112 = new Element("string");
			string1112.addContent(target.get("path").toString());
			dict111.addContent(key1111);
			dict111.addContent(string1111);
			dict111.addContent(key1112);
			dict111.addContent(string1112);
			array11.addContent(dict111);
			Element key12 = new Element("key");
			key12.addContent("metadata");
			Element dict12 = new Element("dict");
			Element key121 = new Element("key");
			key121.addContent("bundle-identifier");
			Element string121 = new Element("string");
			string121.addContent(target.get("bundleId").toString());
			Element key122 = new Element("key");
			key122.addContent("bundle-version");
			Element string122 = new Element("string");
			string122.addContent(target.get("version").toString());
			Element key123 = new Element("key");
			key123.addContent("kind");
			Element string123 = new Element("string");
			string123.addContent("software");
			Element key124 = new Element("key");
			key124.addContent("title");
			Element string124 = new Element("string");
			string124.addContent("试用先锋");
			dict12.addContent(key121);
			dict12.addContent(string121);
			dict12.addContent(key122);
			dict12.addContent(string122);
			dict12.addContent(key123);
			dict12.addContent(string123);
			dict12.addContent(key124);
			dict12.addContent(string124);
			dict1.addContent(key11);
			dict1.addContent(array11);
			dict1.addContent(key12);
			dict1.addContent(dict12);
			array.addContent(dict1);
			dict.addContent(key);
			dict.addContent(array);
			root.addContent(dict);
			String xmlPatch = tempSavePath + target.get("newFileName").toString() + ".plist";
			// System.out.println("xmlPatch=" + xmlPatch);
			DocType docType = new DocType("plist", "-//Apple//DTD PLIST 1.0//EN", "http://www.apple.com/DTDs/PropertyList-1.0.dtd");
			GetXml.createXml(root, xmlPatch, docType);
			resultMap.put("xmlTempPath", xmlPatch);
			resultMap.put("path", accessPath + target.get("newFileName").toString() + ".plist");
			resultMap.put("r", true);
		} catch (Exception e) {
			LOG.debug("生成plist文件失败！" + e.getMessage());
			resultMap.put("r", false);
			resultMap.put("m", "生成plist文件失败！");
		}
		return resultMap;

	}

	public static void creatPlistFile(Map<String, Object> app) {
		try {
			Element root = new Element("plist");
			root.setAttribute("version", "1.0");

			Element dict = new Element("dict");
			Element key = new Element("key");
			key.addContent("items");
			dict.addContent(key);

			Element array = new Element("array");

			Element dict2 = new Element("dict");
			Element key2 = new Element("key");
			key2.addContent("assets");
			dict2.addContent(key2);

			Element array2 = new Element("array");
			Element dict3 = new Element("dict");

			Element key3 = new Element("key");
			key3.addContent("kind");
			Element string1 = new Element("string");
			string1.addContent("software-package");
			Element key4 = new Element("key");
			key4.addContent("url");
			Element string2 = new Element("string");
			string2.addContent(app.get("DownloadUrl").toString());
			dict3.addContent(key3);
			dict3.addContent(string1);
			dict3.addContent(key4);
			dict3.addContent(string2);

			array2.addContent(dict3);

			dict2.addContent(array2);

			Element key5 = new Element("key");
			key5.addContent("metadata");
			dict2.addContent(key5);

			Element dict4 = new Element("dict");

			Element key6 = new Element("key");
			key6.addContent("bundle-identifier");
			dict4.addContent(key6);
			Element string3 = new Element("string");
			string3.addContent(app.get("BundleID").toString());
			dict4.addContent(string3);
			Element key7 = new Element("key");
			key7.addContent("bundle-version");
			dict4.addContent(key7);
			Element string4 = new Element("string");
			string4.addContent(app.get("AppVer").toString());
			dict4.addContent(string4);
			Element key8 = new Element("key");
			key8.addContent("kind");
			dict4.addContent(key8);
			Element string5 = new Element("string");
			string5.addContent("software");
			dict4.addContent(string5);
			Element key9 = new Element("key");
			key9.addContent("title");
			dict4.addContent(key9);
			Element string10 = new Element("string");
			string10.addContent(app.get("SWName").toString());
			dict4.addContent(string10);

			dict2.addContent(dict4);

			array.addContent(dict2);

			dict.addContent(array);

			root.addContent(dict);

			String xmlPatch = SysConstants.PLIST_DIR + File.separator + app.get("SWID").toString() + ".plist";
//            System.out.println("xmlPatch=" + xmlPatch);
			DocType docType = new DocType("plist", "-//Apple//DTD PLIST 1.0//EN", "http://www.apple.com/DTDs/PropertyList-1.0.dtd");
			GetXml.createXml(root, xmlPatch, docType);
		} catch (Exception e) {
			e.printStackTrace();
//            log.error("=========="+ new Date() + "保存plist异常：app ="+ app + ExceptionInfo.getMsg(e));
		}

	}

}
