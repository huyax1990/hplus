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
import com.yingmob.web.model.NewsModel;
import com.yingmob.web.model.SysFileModel;
/**
 * 新闻
 *
 */
@Control(controllerKey = {"news"})
public class NewsController extends BaseController {
	private final static Logger LOG = Logger.getLogger(NewsController.class);
	
	public void index() {
		render("hplus/test");
	}

	public void list() {
		Pager pager = createPager();

		pager.addParam("title", getPara("title"));
		
		Page<?> page = NewsModel.dao.pageList(pager);

		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
	}
	
	@Before(Tx.class)
	public void add() {
		getFile();
		NewsModel news = getModelWithOutModelName(NewsModel.class, true);
		news.set("create_time", new Date());
		news.set("update_time", news.get("create_time"));
		news.save();
		setAttr(RESULT, true);
		setAttr(MESSAGE, "新增成功！");
		setAttr("id", news.get("id"));
		renderJson();
	}

	@Before(Tx.class)
	public void update() {
		getFile();
		NewsModel news = getModelWithOutModelName(NewsModel.class, true);
		if (news.get("id") == null) {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "更新失败！");
			renderJson();
			return;
		}
		news.set("update_time", new Date());
		news.update();
		setAttr(RESULT, true);
		setAttr(MESSAGE, "更新成功！");
		renderJson();
	}

	@Before(Tx.class)
	public void del() {
		Integer id = getParaToInt("id");
		if (id != null && NewsModel.dao.deleteById(id)) {
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
				NewsModel.dao.deleteById(id);
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
			sysFile.set("oName", fileName).set("nName", newFileName).set("lUrl", upload.getFile().getAbsolutePath()).set("size", upload.getFile().length()).set("dUrl", dUrl);
			sysFile.save();
			setAttr("r", true);
			setAttr("url", dUrl);
			setAttr("filename", newFileName);
		} catch (Exception e) {
			LOG.debug(" upload image file  faild ! " + e.getMessage());
			setAttr("r", false);
			setAttr("m", "上传文件失败！");
		}
		renderJson();
	}
}
