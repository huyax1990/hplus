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
import com.yingmob.web.model.CultureModel;
import com.yingmob.web.model.NewsModel;
import com.yingmob.web.model.SysFileModel;

@Control(controllerKey = {"cultureAct"})
public class CultureActiveController extends BaseController 
{
	private final static Logger LOG = Logger.getLogger(CultureActiveController.class);

	public void index() {
		render("culture/culture_act_mgr");
	}

	public void list() {
		Pager pager = createPager();

		pager.addParam("title", getPara("title"));
		
		Page<?> page = CultureModel.dao.pageList(pager);

		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
	}
	
	@Before(Tx.class)
	public void add() {
		getFile();
		HttpServletRequest request = getRequest();
		CultureModel cultureAct = getModelWithOutModelName(CultureModel.class, true);
		cultureAct.set("create_time", new Date());
		cultureAct.set("update_time", new Date());
		cultureAct.set("module", 2);
		cultureAct.save();
		setAttr(RESULT, true);
		setAttr(MESSAGE, "新增成功！");
		setAttr("id", cultureAct.get("id"));
		renderJson();
	}
	
	public void update()
	{
		getFile();
		CultureModel cultureAct = getModelWithOutModelName(CultureModel.class, true);
		if (cultureAct.get("id") == null) {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "更新失败！");
			renderJson();
			return;
		}
		cultureAct.set("update_time", new Date());
		cultureAct.update();
		setAttr(RESULT, true);
		setAttr(MESSAGE, "更新成功！");
		renderJson();
	}
	
	public void del() {
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
	
	public void batch_del() {
		String ids = getPara("ids");
		if (CultureModel.dao.batchDel(ids)>0) {
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
	/*public void imageUpload() {
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
	}*/
}
