package com.yingmob.web.model;

import java.io.File;

import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Model;
import com.yingmob.core.plugin.annotation.Table;

/**
 * 
 * 
 * @Filename SysFileModel.java
 *
 * @Description
 *
 * @Version 1.0
 *
 * @Author mzl
 *
 * @Email lexiangtaotao1988@gmail.com,344242022@qq.com
 * 
 * @History
 *          <li>Author: mzl</li> <li>Date: 2015年1月13日</li> <li>Version: 1.0</li> <li>Content: create</li>
 *
 *
 *
 *
 *
 *          CREATE TABLE `sys_file` (
 *          `id` int(11) NOT NULL AUTO_INCREMENT,
 *          `oName` varchar(255) DEFAULT NULL COMMENT '旧文件名',
 *          `nName` varchar(255) DEFAULT NULL COMMENT '新文件名',
 *          `lUrl` varchar(255) DEFAULT NULL COMMENT '本地路径',
 *          `size` int(11) DEFAULT NULL COMMENT '大小',
 *          `cTime` date DEFAULT NULL COMMENT '上传时间',
 *          PRIMARY KEY (`id`)
 *          ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 * 
 *
 */
@SuppressWarnings("serial")
@Table(tableName = "sys_file")
public class SysFileModel extends Model<SysFileModel> {
	
	private static final Logger LOG = Logger.getLogger(SysFileModel.class);
	
	public final static SysFileModel dao = new SysFileModel();

	/**
	 * 删除指定的文件和记录
	 * 
	 * @param sfid
	 * @return
	 */
	public boolean delFile(Integer sfid) {
		SysFileModel model = dao.findById(sfid);
		if (model == null || StrKit.isBlank(model.getStr("lUrl"))) {
			return false;
		}
		String lurl = model.getStr("lUrl");
		File sysFile = new File(lurl);
		if (sysFile.exists()) {
			sysFile.delete();
			LOG.info("- 删除文件【"+sfid+"】"+model.getStr("nName"));
		}
		model.delete();
		return true;
	}
}
