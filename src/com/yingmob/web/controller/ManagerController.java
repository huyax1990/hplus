package com.yingmob.web.controller;

import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yingmob.base.common.BaseController;
import com.yingmob.base.common.Pager;
import com.yingmob.core.plugin.annotation.Control;
import com.yingmob.web.model.ManagerModel;

@Control(controllerKey = "/manager")
public class ManagerController extends BaseController{
	private final static Logger LOG = Logger.getLogger(ManagerController.class);

	public void index() {
		render("manager/manager_mgr");
	}

	public void list() {
		Pager pager = createPager();
		pager.addParam("name", getPara("name"));
		Page<?> page = ManagerModel.dao.pageList(pager);
		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
	}

	@Before(Tx.class)
	public void add() {
		getFile();
		ManagerModel manager = getModelWithOutModelName(ManagerModel.class,
				true);
		manager.set("create_time", new Date());
		manager.set("update_time", new Date());
		manager.save();

		setAttr(RESULT, true);
		setAttr(MESSAGE, "新增成功！");
		setAttr("id", manager.get("id"));
		renderJson();
	}

	@Before(Tx.class)
	public void update() {
		getFile();
		ManagerModel manager = getModelWithOutModelName(ManagerModel.class,
				true);
		if (manager.get("id") == null) {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "更新失败！");
			renderJson();
			return;
		}
		manager.set("update_time", new Date());
		manager.update();
		setAttr(RESULT, true);
		setAttr(MESSAGE, "更新成功！");
		renderJson();
	}

	public void delete() {
		Integer id = getParaToInt("id");
		if (id != null && ManagerModel.dao.deleteById(id)) {
			setAttr(RESULT, true);
			setAttr(MESSAGE, "删除成功！");
		} else {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "删除失败！");
		}
		renderJson();
	}
	
	public void batchDel() {
		LOG.debug("--批量删除风采--start");
		String ids = getPara("ids");
		if (ids.isEmpty()) {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "批量删除失败！");
		} else {
			setAttr(RESULT, true);
			ManagerModel.dao.batchDel(ids);
			setAttr(MESSAGE, "批量删除成功！");
		}
		LOG.debug("--批量删除风采--end");
		renderJson();
	}
	/**
	 * 编号验证
	 */
//	public void checkSort(){
//		String sort=getPara("sort");
//		if(ManagerModel.dao.checkSort(sort)>0){
//			setAttr(RESULT, true);
//			setAttr(MESSAGE, "该编号已存在！");
//		}else{
//			setAttr(RESULT, false);
//			setAttr(MESSAGE, "该编号可以选择！");
//		}
//		renderJson();
//	}
}
