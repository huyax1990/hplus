package com.yingmob.web.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

public class JSONResult implements Interceptor {

	private final String SUCCESS_MESSAGE = "操作成功";
	private final String FAIL_MESSAGE = "操作失败";
	private static final Logger LOG = Logger.getLogger(JSONResult.class);

	@Override
	public void intercept(ActionInvocation ai) {
		Controller c = ai.getController();
		boolean result = true;
		try {
			ai.invoke();
		} catch (Exception e) {
			result = false;
			LOG.error(e.getMessage(), e);
		}
		c.setAttr("r", result);
		String message = c.getAttrForStr(result ? "sm" : "fm");
		String defaultMessage = result ? SUCCESS_MESSAGE : FAIL_MESSAGE;
		c.setAttr("m", message == null ? defaultMessage : message);
		c.setAttr("sm", null);
		c.setAttr("fm", null);
		c.renderJson();
	}

}
