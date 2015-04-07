/**
 * www.yingmob.com Inc.
 * Copyright (c) 2014 All Rights Reserved.
 */
package com.yingmob.tools.plist;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
 *                       
 * @Filename ZipUtil.java
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
 *<li>Author: huqing</li>
 *<li>Date: 2014年7月30日</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */

/**
 * ZIP压缩工具类
 * 
 * @author obullxl@gmail.com
 * @version $Id: MbillZipUtil.java, v 0.1 2011-11-24 下午07:32:37 obullxl Exp $
 */
public class ZipUtil {

	/**
	 * ZIP文件后缀名
	 */
	public static final String ZIP_FILE_SUFFIX = ".zip";

	/**
	 * 对文件列表进行压缩
	 * <p/>
	 * 注意：<br/>
	 * 1、文件如果为目录，会循环递归压缩目录和里面的文件；<br/>
	 * 2、如果文件不存在，则直接忽略；
	 * 
	 * @param zip ZIP包文件路径
	 * @param files 文件列表
	 */
	public static final void zip(String zip, List<String> files) {
		try {
			// 压缩流
			FileOutputStream fileOutputStream = new FileOutputStream(zip);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
			ZipOutputStream out = new ZipOutputStream(cos);

			for (String file : files) {
				// 单个文件或目录
				compress(out, new File(file));
			}

			// 完成
			IOUtils.closeQuietly(out);
		} catch (Exception e) {
			throw new RuntimeException("[压缩文件]-压缩文件异常, ZIP[" + zip + "], Files[" + files + "]！", e);
		}
	}

	/**
	 * 压缩文件
	 *
	 * @param out ZIP输出流
	 * @param file 待压缩的文件或是目录
	 */
	public static final void compress(ZipOutputStream out, File file) {
		// 目录
		if (file.isDirectory()) {
			compressDirectory(out, file);
		}

		// 文件
		else {
			compressFile(out, file);
		}
	}

	/**
	 * 压缩目录 <br/>
	 * 注意：只压缩目录下所有非ZIP文件
	 * 
	 * @param out ZIP输出流
	 * @param dir 文件目录
	 */
	public static final void compressDirectory(ZipOutputStream out, File dir) {
		try {
			// 目录不存在
			if (!dir.exists()) {
				return;
			}

			// 压缩目录下所有的文件
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				// 遍历压缩所有非ZIP文件
				if (files[i].getName().indexOf(ZIP_FILE_SUFFIX) < 0) {
					compressFile(out, files[i]);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("[压缩文件]-压缩目录异常, ZIP[" + out + "], DIR[" + dir + "]！", e);
		}
	}

	/**
	 * 压缩一个文件，如果文件不存在，则直接忽略
	 * 
	 * @param out ZIP输出流
	 * @param file 待压缩的文件
	 */
	public static final void compressFile(ZipOutputStream out, File file) {
		// 文件不存在
		if (!file.exists()) {
			return;
		}

		try {
			// 输入流
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			ZipEntry entry = new ZipEntry(file.getName());
			out.putNextEntry(entry);

			// 复制数据
			IOUtils.copy(bis, out);

			// 关闭输入流
			IOUtils.closeQuietly(bis);
		} catch (Exception e) {
			throw new RuntimeException("[压缩文件]-压缩目录异常, ZIP[" + out + "], File[" + file + "]！", e);
		}
	}

	/**
	 * 解压缩ZIP文件，递归解压缩ZIP内的文件夹
	 * 
	 * @param zip ZIP文件绝对路径
	 * @param path 解压的文件目录
	 * @return 解压缩后的文件名列表，包含ZIP内的文件子目录，对于空ZIP文件，返回为空列表。
	 * @throws Exception 解压异常。
	 */
	public static final void unzip(String zipFile, String path) throws Exception {

		// 文件列表
		List<String> files = new ArrayList<String>();

		ZipFile zipfile = new ZipFile(zipFile);
		try {
			Enumeration<ZipEntry> entries = zipfile.getEntries();
			if (entries == null || !entries.hasMoreElements()) {
				// 空ZIP包
				return;
			}

			// 创建目标文件目录
			FileUtils.forceMkdir(new File(path));

			// 遍历所有文件
			while (entries.hasMoreElements()) {
				ZipEntry zipEntry = entries.nextElement();
				String fname = zipEntry.getName();

				// 创建目录
				if (zipEntry.isDirectory()) {
					String fpath = FilenameUtils.normalize(path + File.separator + fname);
					FileUtils.forceMkdir(new File(fpath));
					continue;
				}

				// 复制文件目录
				if (StringUtils.contains(fname, File.separator)) {
					String tpath = StringUtils.substringBeforeLast(fname, File.separator);
					String fpath = FilenameUtils.normalize(path + File.separator + tpath);
					FileUtils.forceMkdir(new File(fpath));
				}

				String file = FilenameUtils.normalize(path + File.separator + fname);
				File f = new File(file);
				f = new File(f.getParent());
				if (!f.exists()) {
					f.mkdirs();
				}

				// 复制文件内容
				InputStream input = null;
				OutputStream output = null;
				try {
					input = zipfile.getInputStream(zipEntry);

					output = new FileOutputStream(file);

					IOUtils.copy(input, output);
				} finally {
					IOUtils.closeQuietly(input);
					IOUtils.closeQuietly(output);
				}

				// 解压成功单个文件名
				files.add(fname);
			}

		} finally {
			ZipFile.closeQuietly(zipfile);
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println("D:\\workspace\\hy-qvod\\WebRoot\\assets\\upload/1407235030485\\Payload/ddd.app/00_1.png".replaceAll("/", "\\\\"));
	}
}
