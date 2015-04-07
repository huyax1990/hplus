package com.yingmob.web.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.yingmob.base.common.BasePage;

public class Paginate implements Interceptor {
	private int defaultPageSize = 40;

	private static ThreadLocal<BasePage> localPage = new ThreadLocal<BasePage>();

	public static BasePage getPage() {
		return localPage.get();
	}

	@Override
	public void intercept(ActionInvocation ai) {

		Controller c = ai.getController();
		Integer pageSize = c.getParaToInt("rows", defaultPageSize);
		Integer pageNo = c.getParaToInt("page", 1);
		BasePage pager = new BasePage(pageSize, pageNo);
		try {
			localPage.set(pager);
			ai.invoke();
		} finally {
			localPage.remove();
		}

		c.setAttr("total", pager.getTotalRow());
		c.setAttr("rows", pager.getList());
		if (pager.getTotal() != null) {
			c.setAttr("sum", pager.getTotal());
		}
		c.renderJson();
	}

}
