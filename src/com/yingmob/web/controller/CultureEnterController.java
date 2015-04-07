package com.yingmob.web.controller;

import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yingmob.base.common.BaseController;
import com.yingmob.base.common.Pager;
import com.yingmob.core.plugin.annotation.Control;
import com.yingmob.web.model.CultureModel;

@Control(controllerKey = "/cultureEnt")
public class CultureEnterController extends BaseController{
	private final static Logger LOG = Logger.getLogger(CultureActiveController.class);

	public void index() {
		render("culture/culture_mgr");
	}
	
	public void list() {
		Pager pager = createPager();
		pager.addParam("title", getPara("title"));
		Page<?> page = CultureModel.dao.pageListEnt(pager);
		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
	}

	@Before(Tx.class)
	public void add() {
		getFile();
		CultureModel culture = getModelWithOutModelName(CultureModel.class,
				true);
		culture.set("create_time", new Date());
		culture.set("update_time", new Date());
		culture.set("module", 1);
		culture.save();

		setAttr(RESULT, true);
		setAttr(MESSAGE, "新增成功！");
		setAttr("id", culture.get("id"));
		renderJson();
	}

	@Before(Tx.class)
	public void update() {
		getFile();
		CultureModel culture = getModelWithOutModelName(CultureModel.class,
				true);
		if (culture.get("id") == null) {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "更新失败！");
			renderJson();
			return;
		}
		culture.set("update_time", new Date());
		culture.update();
		setAttr(RESULT, true);
		setAttr(MESSAGE, "更新成功！");
		renderJson();
	}

	public void delete() {
		Integer id = getParaToInt("id");
		if (id != null && CultureModel.dao.deleteById(id)) {
			setAttr(RESULT, true);
			setAttr(MESSAGE, "删除成功！");
		} else {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "删除失败！");
		}
		renderJson();
	}

	public void batchDel() {
		LOG.debug("--批量删除文化--start");
		String ids = getPara("ids");
		if (ids.isEmpty()) {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "批量删除失败！");
		} else {
			setAttr(RESULT, true);
			CultureModel.dao.batchDelEnt(ids);
			setAttr(MESSAGE, "批量删除成功！");
		}
		LOG.debug("--批量删除文化--end");
		renderJson();
	}
}
