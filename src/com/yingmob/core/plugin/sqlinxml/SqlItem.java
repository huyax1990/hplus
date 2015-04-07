package com.yingmob.core.plugin.sqlinxml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class SqlItem {

	@XmlAttribute
	public String id;

	@XmlValue
	public String value;

	@Override
	public String toString() {
		return "SqlItem [id=" + id + ", value=" + value + "]";
	}

}
