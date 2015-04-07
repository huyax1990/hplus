package com.yingmob.web.controller;

import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yingmob.base.common.BaseController;
import com.yingmob.base.common.Pager;
import com.yingmob.core.plugin.annotation.Control;
import com.yingmob.web.model.ProfileModel;

/**
 * 公司简介
 * 
 * @author shimiao
 * 
 */
@Control(controllerKey = "/profile")
public class ProfileController extends BaseController {
	private final static Logger LOG = Logger.getLogger(ProfileController.class);

	public void index() {
		render("profile/profile_mgr");
	}

	public void list() {
		Pager pager = createPager();
		Page<?> page = ProfileModel.dao.pageList(pager);
		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
	}

	@Before(Tx.class)
	public void add() {
		getFile();
		ProfileModel profile = getModelWithOutModelName(ProfileModel.class,
				true);
		profile.set("create_time", new Date());
		profile.set("update_time", new Date());
		profile.save();

		setAttr(RESULT, true);
		setAttr(MESSAGE, "新增成功！");
		setAttr("id", profile.get("id"));
		renderJson();
	}

	@Before(Tx.class)
	public void update() {
		getFile();
		ProfileModel profile = getModelWithOutModelName(ProfileModel.class,
				true);
		if (profile.get("id") == null) {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "更新失败！");
			renderJson();
			return;
		}
		profile.set("update_time", new Date());
		profile.update();
		setAttr(RESULT, true);
		setAttr(MESSAGE, "更新成功！");
		renderJson();
	}

	public void del() {
		Integer id = getParaToInt("id");
		if (id != null && ProfileModel.dao.deleteById(id)) {
			setAttr(RESULT, true);
			setAttr(MESSAGE, "删除成功！");
		} else {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "删除失败！");
		}
		renderJson();
	}

}
