package com.yingmob.core.plugin.sqlinxml;

import com.jfinal.plugin.IPlugin;
import com.yingmob.core.kit.SqlXmlKit;

public class SqlXmlPlugin implements IPlugin {

	public SqlXmlPlugin() {
	}

	@Override
	public boolean start() {
		SqlXmlKit.init();
		return true;
	}

	@Override
	public boolean stop() {
		SqlXmlKit.destory();
		return true;
	}

}
