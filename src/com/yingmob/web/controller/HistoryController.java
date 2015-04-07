package com.yingmob.web.controller;

import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yingmob.base.common.BaseController;
import com.yingmob.base.common.Pager;
import com.yingmob.core.plugin.annotation.Control;
import com.yingmob.web.model.HistoryModel;

@Control(controllerKey = "/history")
public class HistoryController extends BaseController {
	private final static Logger LOG = Logger.getLogger(HistoryController.class);

	public void index() {
		render("history/history_mgr");
	}

	public void list() {
		Pager pager = createPager();
		pager.addParam("time", getPara("time"));
		pager.addParam("introduction", getPara("introduction"));
		Page<?> page = HistoryModel.dao.pageList(pager);
		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
	}

	@Before(Tx.class)
	public void add() {
		getFile();
		HistoryModel history = getModelWithOutModelName(HistoryModel.class,
				true);
		history.set("create_time", new Date());
		history.set("update_time", new Date());
		history.save();

		setAttr(RESULT, true);
		setAttr(MESSAGE, "新增成功！");
		setAttr("id", history.get("id"));
		renderJson();
	}

	@Before(Tx.class)
	public void update() {
		getFile();
		HistoryModel history = getModelWithOutModelName(HistoryModel.class,
				true);
		if (history.get("id") == null) {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "更新失败！");
			renderJson();
			return;
		}
		history.set("update_time", new Date());
		history.update();
		setAttr(RESULT, true);
		setAttr(MESSAGE, "更新成功！");
		renderJson();
	}

	public void delete() {
		Integer id = getParaToInt("id");
		if (id != null && HistoryModel.dao.deleteById(id)) {
			setAttr(RESULT, true);
			setAttr(MESSAGE, "删除成功！");
		} else {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "删除失败！");
		}
		renderJson();
	}

	public void batchDel() {
		LOG.debug("--批量删除历程--start");
		String ids = getPara("ids");
		if (ids.isEmpty()) {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "批量删除失败！");
		} else {
			setAttr(RESULT, true);
			HistoryModel.dao.batchDel(ids);
			setAttr(MESSAGE, "批量删除成功！");
		}
		LOG.debug("--批量删除历程--end");
		renderJson();
	}
}
