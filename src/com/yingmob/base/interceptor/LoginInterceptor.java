package com.yingmob.base.interceptor;

import java.util.Arrays;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.yingmob.tools.SysConstants;

public class LoginInterceptor implements Interceptor {

//	private static final Logger LOG = Logger.getLogger(LoginInterceptor.class);

	@Override
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		if (Arrays.binarySearch(SysConstants.CONTROLLER_UNLIMIT_URL, ai.getControllerKey()) >= 0 || Arrays.binarySearch(SysConstants.ACTION_UNLIMIT_URL, ai.getActionKey()) >= 0) {
			ai.invoke();
		} else {
			if (null != controller.getSession().getAttribute(SysConstants.SESSION_USER)) {
				ai.invoke();
			} else {
				controller.redirect("/");
			}
		}
	}

}
