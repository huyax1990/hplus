package com.yingmob.base.controller;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.log.Logger;
import com.jfinal.upload.UploadFile;
import com.yingmob.base.common.BaseController;
import com.yingmob.core.plugin.annotation.Control;
import com.yingmob.tools.DateTools;
import com.yingmob.tools.SysConstants;
import com.yingmob.web.model.SysFileModel;
@Control(controllerKey = {"imageUpload"})
public class ImageUploadController extends BaseController {
	private static final Logger LOG = Logger.getLogger(ImageUploadController.class);

	/**
	 * 图片上传
	 */
	public void upload() {
		try {
			HttpServletRequest request = getRequest();
			String path = request.getServletContext().getRealPath("/");
			path = path + SysConstants.IMG_DIR;
			UploadFile upload = getFile("file", path, SysConstants.MAX_POST_SIZE);
			String fileName = upload.getOriginalFileName();
			String newFileName = DateTools.format(new Date(), DateTools.yyyyMMddHHmmssSSS) + fileName.substring(fileName.lastIndexOf("."), fileName.length());
			String dUrl = SysConstants.IMG_DIR + "/" + newFileName;
			// 修改上传文件的文件名称
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
