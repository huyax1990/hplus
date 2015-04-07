package com.yingmob.web.controller;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.yingmob.base.common.BaseController;
import com.yingmob.base.common.Pager;
import com.yingmob.core.plugin.annotation.Control;
import com.yingmob.tools.DateTools;
import com.yingmob.tools.SysConstants;
import com.yingmob.web.model.ContactModel;
import com.yingmob.web.model.CultureModel;
import com.yingmob.web.model.NewsModel;
import com.yingmob.web.model.SysFileModel;

@Control(controllerKey = {"contact"})
public class ContactController extends BaseController 
{
	private final static Logger LOG = Logger.getLogger(ContactController.class);

	public void index() {
		render("contact/contact_mgr");
	}

	public void list() {
		Pager pager = createPager();

		pager.addParam("title", getPara("title"));
		
		Page<?> page = ContactModel.dao.pageList(pager);

		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
	}
	
	@Before(Tx.class)
	public void add() {
		ContactModel contact = getModelWithOutModelName(ContactModel.class, true);
		contact.set("create_time", new Date());
		contact.set("update_time", new Date());
		contact.save();
		setAttr(RESULT, true);
		setAttr(MESSAGE, "新增成功！");
		setAttr("id", contact.get("id"));
		renderJson();
	}
	
	public void update()
	{
		ContactModel contact = getModelWithOutModelName(ContactModel.class, true);
		if (contact.get("id") == null) {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "更新失败！");
			renderJson();
			return;
		}
		contact.set("update_time", new Date());
		contact.update();
		setAttr(RESULT, true);
		setAttr(MESSAGE, "修改成功！");
		renderJson();
	}
	
	public void del() {
		Integer id = getParaToInt("id");
		if (id != null && ContactModel.dao.deleteById(id)) {
			setAttr(RESULT, true);
			setAttr(MESSAGE, "删除成功！");
		} else {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "删除失败！");
		}
		renderJson();
	}
	
	public void batch_del() {
		String ids = getPara("ids");
		if (ContactModel.dao.batchDel(ids)>0) {
			setAttr(RESULT, true);
			setAttr(MESSAGE, "删除成功！");
		} else {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "删除失败！");
		}
		renderJson();
	}
	
	/**
	 * 图片上传
	 */
	public void imageUpload() {
		try {
			HttpServletRequest request = getRequest();
			String path = request.getServletContext().getRealPath("/");
			path = path + SysConstants.IMG_DIR;
			UploadFile upload = getFile("file", path, SysConstants.MAX_POST_SIZE);
			String fileName = upload.getOriginalFileName();
			String newFileName = DateTools.format(new Date(), DateTools.yyyyMMddHHmmssSSS) + fileName.substring(fileName.lastIndexOf("."), fileName.length());
			String dUrl = SysConstants.IMG_DIR + "/" + newFileName;
			//修改上传文件的文件名称
			upload.getFile().renameTo(new File(path + File.separator + newFileName));
			SysFileModel sysFile = new SysFileModel();
			sysFile.set("oName", fileName).set("nName", newFileName).set("lUrl", upload.getFile().getAbsolutePath()).set("size", upload.getFile().length()).set("cTime", DateTools.format(new Date(), DateTools.yyyy_MM_dd)).set("dUrl", dUrl);
			sysFile.save();
			setAttr("r", true);
			setAttr("url", dUrl);
		} catch (Exception e) {
			LOG.debug(" upload image file  faild ! " + e.getMessage());
			setAttr("r", false);
			setAttr("m", "上传文件失败！");
		}
		renderJson();
	}
}
