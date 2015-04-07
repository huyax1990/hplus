package com.yingmob.web.controller;

import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yingmob.base.common.BaseController;
import com.yingmob.base.common.Pager;
import com.yingmob.core.plugin.annotation.Control;
import com.yingmob.web.model.HomePageModel;
/**
 * 前台首页管理
 *
 */
@Control(controllerKey = {"homePage"})
public class ClientHomePageController extends BaseController{
	private final static Logger LOG = Logger.getLogger(ClientHomePageController.class);
	//轮播
	public void carousel() {
		render("homepage/carousel");
	}
	
	//简介
	public void introduce() {
		render("homepage/introduce");
	}
	
	//伙伴
	public void partner() {
		render("homepage/partner");
	}
	
	//平台
	public void platform() {
		render("homepage/platform");
	}
	
	public void list() {
		Pager pager = createPager();

		pager.addParam("name", getPara("name"));
		pager.addParam("image_name", getPara("image_name"));
		pager.addParam("module",getPara("module"));
		Page<?> page = HomePageModel.dao.pageList(pager);

		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
	}
	
	@Before(Tx.class)
	public void add() {
		getFile();
		HomePageModel vo = getModelWithOutModelName(HomePageModel.class, true);
		vo.set("create_time", new Date());
		vo.set("update_time", vo.get("create_time"));
		vo.save();
		setAttr(RESULT, true);
		setAttr(MESSAGE, "新增成功！");
		setAttr("id", vo.get("id"));
		renderJson();
	}

	@Before(Tx.class)
	public void update() {
		getFile();
		HomePageModel vo = getModelWithOutModelName(HomePageModel.class, true);
		if (vo.get("id") == null) {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "更新失败！");
			renderJson();
			return;
		}
		vo.set("update_time", new Date());
		vo.update();
		setAttr(RESULT, true);
		setAttr(MESSAGE, "更新成功！");
		renderJson();
	}

	@Before(Tx.class)
	public void del() {
		Integer id = getParaToInt("id");
		if (id != null && HomePageModel.dao.deleteById(id)) {
			setAttr(RESULT, true);
			setAttr(MESSAGE, "删除成功！");
		} else {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "删除失败！");
		}
		renderJson();
	}
	
	@Before(Tx.class)
	public void batch_del() {
		String ids = getPara("ids");
		try {
			String[] arrIds = ids.split("\\|");
			for (String id : arrIds) {
				if ("".equals(id))
					continue;
				HomePageModel.dao.deleteById(id);
			}
			setAttr(RESULT, true);
			setAttr(MESSAGE, "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			setAttr(RESULT, false);
			setAttr(MESSAGE, "删除失败！");
		}
		renderJson();
	}
	

}
