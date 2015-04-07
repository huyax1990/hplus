package com.yingmob.tools;

import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.yingmob.base.common.BasePage;
import com.yingmob.core.kit.SqlXmlKit;
import com.yingmob.web.interceptor.Paginate;

public class PaginateTools {

	public static PaginateTools instance = new PaginateTools();
	/**
	 * @Author Sean 2014-12-4 下午3:36:37
	 *         自动分页，基于paginate拦截器，
	 * @param columnSQL
	 * @param sqlNameSpace
	 */
	public PaginateTools paginate(String columnSQL, String sqlNameSpace) {
		BasePage pager = Paginate.getPage();
		if (pager == null) {
			throw new RuntimeException("使用自动分页需要添加paginate拦截器");
		}
		LinkedList<Object> param = new LinkedList<Object>();
		String fromSQL = SqlXmlKit.getSql(sqlNameSpace, pager.getParams(), param);

		Page<Record> page = Db.paginate(pager.getPageNumber(), pager.getPageSize(), columnSQL, fromSQL, param.toArray());

		pager.setTotalRow(page.getTotalRow());
		pager.setList(page.getList());
		return instance;
	}

	public PaginateTools addParam(String key, Object object) {
		BasePage pager = Paginate.getPage();
		pager.addParam(key, object);
		return instance;
	}

	public PaginateTools addParams(HttpServletRequest request, String... keys) {
		BasePage pager = Paginate.getPage();
		pager.addParams(request, keys);
		return instance;
	}
}
