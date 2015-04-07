package com.yingmob.web.model;

import java.util.LinkedList;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.yingmob.base.common.Pager;
import com.yingmob.core.plugin.annotation.Table;

@SuppressWarnings("serial")
@Table(tableName = "about_us")
public class ProfileModel extends Model<ProfileModel> {
	public static final ProfileModel dao = new ProfileModel();

	/**
	 * 公司简介列表数据
	 * 
	 * @param pager
	 * @return
	 */
	public Page<ProfileModel> pageList(Pager pager) {
		StringBuffer sqlExecpt = new StringBuffer(" from about_us where 1=1 ");
		LinkedList<Object> params = new LinkedList<Object>();
		sqlExecpt.append("  order by update_time desc");
		return dao
				.paginate(
						pager.getPageNo(),
						pager.getPageSize(),
						" SELECT id, image_name, image_path, introduction, create_time, update_time ",
						sqlExecpt.toString(), params.toArray());
	}
	
}
