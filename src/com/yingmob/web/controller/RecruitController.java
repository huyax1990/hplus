package com.yingmob.web.controller;

import java.text.ParseException;
import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yingmob.base.common.BaseController;
import com.yingmob.base.common.Pager;
import com.yingmob.core.plugin.annotation.Control;
import com.yingmob.tools.DateTools;
import com.yingmob.web.model.RecruitModel;
import com.yingmob.web.model.SysConfigModel;

/**
 * 招聘
 *
 */
@Control(controllerKey = { "recruit" })
public class RecruitController extends BaseController {
	private final static Logger LOG = Logger.getLogger(NewsController.class);

	// 社会招聘
	public void social() {
		setAttr("categorys", SysConfigModel.dao.getConfigs("recruit_category"));
		setAttr("citys", SysConfigModel.dao.getConfigs("recruit_city"));
		render("recruit/social");
	}

	// 校园招聘
	public void school() {
		setAttr("categorys", SysConfigModel.dao.getConfigs("recruit_category"));
		setAttr("citys", SysConfigModel.dao.getConfigs("recruit_city"));
		render("recruit/school");
	}

	// 宣讲
	public void careerTalk() {
		setAttr("citys", SysConfigModel.dao.getConfigs("recruit_city"));
		render("recruit/career_talk");
	}

	

	public void list() {
		Pager pager = createPager();

		pager.addParam("post", getPara("post"));
		pager.addParam("category", getPara("category"));
		pager.addParam("city", getPara("city"));
		pager.addParam("enable", getPara("enable"));
		pager.addParam("type", getPara("type"));
		Page<?> page = RecruitModel.dao.pageList(pager);

		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
	}

	@Before(Tx.class)
	public void add() {
		RecruitModel vo = getModelWithOutModelName(RecruitModel.class, true);
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
		RecruitModel vo = getModelWithOutModelName(RecruitModel.class, true);
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
		if (id != null && RecruitModel.dao.deleteById(id)) {
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
				RecruitModel.dao.deleteById(id);
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
