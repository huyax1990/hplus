/**
 * www.yingmob.com Inc.
 * Copyright (c) 2014 All Rights Reserved.
 */
package com.yingmob.tools.plist;

/**
 *                       
 * @Filename JavaCallPerl.java
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
 *<li>Date: 2014年7月31日</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jfinal.log.Logger;

public class JavaCallPerl {

	private static final Logger log = Logger.getLogger(JavaCallPerl.class);

	public static String plist(String plistFilePath) throws IOException {

		StringBuffer resultStringBuffer = new StringBuffer();

		String lineToRead = "";
		int exitValue = 0;

		try {

			//note plutil 安装 先装 perl 再装 libplist 
			Process proc = Runtime.getRuntime().exec("plutil -i " + plistFilePath);
			InputStream inputStream = proc.getInputStream();
			BufferedReader bufferedRreader = new BufferedReader(new InputStreamReader(inputStream));

			// save first line  
			if ((lineToRead = bufferedRreader.readLine()) != null) {
				resultStringBuffer.append(lineToRead);
			}

			// save next lines  
			while ((lineToRead = bufferedRreader.readLine()) != null) {
				resultStringBuffer.append("\r\n");
				resultStringBuffer.append(lineToRead);
			}

			// Always reading STDOUT first, then STDERR, exitValue last  
			proc.waitFor(); // wait for reading STDOUT and STDERR over  
			exitValue = proc.exitValue();
		} catch (Exception ex) {
			resultStringBuffer = new StringBuffer("");
			exitValue = 2;
		}

		log.debug("exit:" + exitValue);
		log.debug(resultStringBuffer.toString());

		return resultStringBuffer.toString();
	}
}
