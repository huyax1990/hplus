/**
 * www.yingmob.com Inc.
 * Copyright (c) 2014 All Rights Reserved.
 */
package com.yingmob.tools.plist;

import java.io.StringBufferInputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * 
 * @Filename NoOpEntityResolver.java
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
 *          <li>Author: huqing</li> <li>Date: 2014年7月31日</li> <li>Version: 1.0</li> <li>Content: create</li>
 *
 */
@SuppressWarnings("deprecation")
public class NoOpEntityResolver implements EntityResolver {
	@Override
	public InputSource resolveEntity(String publicId, String systemId) {
		return new InputSource(new StringBufferInputStream(""));
	}
}
