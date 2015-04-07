package com.yingmob.web.controller;

import com.yingmob.base.common.BaseController;
import com.yingmob.core.plugin.annotation.Control;

/**
 * 首页
 *
 */
@Control(controllerKey = {"home"})
public class HomePageController extends BaseController {

//	private static final Logger LOG = Logger.getLogger(HomePageController.class);

	public void index() {
		render("home");
	}

}
