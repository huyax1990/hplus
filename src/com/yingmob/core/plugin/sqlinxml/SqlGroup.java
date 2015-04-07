package com.yingmob.core.plugin.sqlinxml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SqlGroup {

	@XmlAttribute
	public String namespace;

	@XmlElement(name = "sql")
	public List<SqlItem> sqlItems = new ArrayList<SqlItem>();

	public void addSqlgroup(SqlItem sqlGroup) {
		sqlItems.add(sqlGroup);
	}

	@Override
	public String toString() {
		return "SqlGroup [namespace=" + namespace + ", sqlItems=" + sqlItems + "]";
	}

}
