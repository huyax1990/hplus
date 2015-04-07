package com.yingmob.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yingmob.base.common.BaseController;
import com.yingmob.base.common.Pager;
import com.yingmob.core.plugin.annotation.Control;
import com.yingmob.web.model.CultureModel;
import com.yingmob.web.model.ProductModel;

@Control(controllerKey = {"product"})
public class ProductController extends BaseController
{
	private final static Logger LOG = Logger.getLogger(ProductController.class);

	public void index() {
		render("product/product_mgr");
	}

	public void list() 
	{
		Pager pager = createPager();
		Page<?> page = ProductModel.dao.pageList(pager);
		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
	}
	
	@Before(Tx.class)
	public void add() {
		getFile();
		HttpServletRequest request = getRequest();
		ProductModel product = getModelWithOutModelName(ProductModel.class, true);
		product.set("create_time", new Date());
		product.set("update_time", new Date());
		product.save();
		setAttr(RESULT, true);
		setAttr(MESSAGE, "新增成功！");
		setAttr("id", product.get("id"));
		renderJson();
	}
	
	public void update()
	{
		getFile();
		ProductModel product = getModelWithOutModelName(ProductModel.class, true);
		if (product.get("id") == null) {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "更新失败！");
			renderJson();
			return;
		}
		product.set("update_time", new Date());
		product.update();
		setAttr(RESULT, true);
		setAttr(MESSAGE, "更新成功！");
		renderJson();
	}
	
	public void batch_del() {
		String ids = getPara("ids");
		if (ProductModel.dao.batchDel(ids)>0) {
			setAttr(RESULT, true);
			setAttr(MESSAGE, "删除成功！");
		} else {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "删除失败！");
		}
		renderJson();
	}
	
	public void del() {
		Integer id = getParaToInt("id");
		if (id != null && ProductModel.dao.deleteById(id)) {
			setAttr(RESULT, true);
			setAttr(MESSAGE, "删除成功！");
		} else {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "删除失败！");
		}
		renderJson();
	}
}
