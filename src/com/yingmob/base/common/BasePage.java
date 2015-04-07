package com.yingmob.base.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

public class BasePage {
	private int pageNumber;
	private int pageSize;
	private Map<String, Object> params;

	private int totalRow;
	private List<Record> list;
	private Record total;

	public BasePage(int pageSize, int pageNumber) {
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
		this.params = new HashMap<String, Object>();
	}

	public BasePage addParam(String key, Object object) {
		params.put(key, object);
		return this;
	};
	public BasePage addParams(HttpServletRequest request, String... keys) {
		for (String key : keys) {
			String temp = request.getParameter(key);
			if (StrKit.notBlank(temp)) {
				params.put(key, temp);
			}
		}
		return this;
	};

	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public int getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	public List<Record> getList() {
		return list;
	}

	public void setList(List<Record> list) {
		this.list = list;
	}

	public Record getTotal() {
		return total;
	}

	public void setTotal(Record total) {
		this.total = total;
	}

}
