package com.yingmob.web.model;

import java.util.LinkedList;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.yingmob.base.common.Pager;
import com.yingmob.core.plugin.annotation.Table;
@SuppressWarnings("serial")
@Table(tableName = "home_page")
public class HomePageModel extends Model<HomePageModel>{
	
	public static final HomePageModel dao = new HomePageModel();

	/**
	 * 获取新闻列表数据
	 * 
	 * @return
	 */
	public Page<HomePageModel> pageList(Pager pager) {
		StringBuffer sqlExecpt = new StringBuffer(" from home_page where 1=1 ");
		LinkedList<Object> params = new LinkedList<Object>();
		if (pager.getParamsMap().get("name") != null && !"".equals(pager.getParamsMap().get("name"))) {
			sqlExecpt.append(" and name like CONCAT('%',?,'%') ");
			params.add(pager.getParamsMap().get("name"));
		}
		
		if (pager.getParamsMap().get("image_name") != null && !"".equals(pager.getParamsMap().get("image_name"))) {
			sqlExecpt.append(" and image_name like CONCAT('%',?,'%') ");
			params.add(pager.getParamsMap().get("image_name"));
		}
		
		if (pager.getParamsMap().get("module") != null && !"".equals(pager.getParamsMap().get("module"))) {
			sqlExecpt.append(" and module = ? ");
			params.add(pager.getParamsMap().get("module"));
		}
		
		sqlExecpt.append("  order by sort asc");
		return dao.paginate(pager.getPageNo(), pager.getPageSize(), " SELECT id, name, image_name, image_path,summary,module,enable,sort,create_time,update_time,url ", sqlExecpt.toString(), params.toArray());
	}

	/**
	 * 删除指定ID的数据
	 * 
	 * @param ids
	 * @return
	 */
	public int batchDel(String ids) {
		int count = 0;
		String[] idsArr = ids.split("\\|");
		for (String id : idsArr) {
			if ("".equals(id))
				continue;
			Db.update(" delete from home_page where 1=1  and id=?  ", id);
			count++;
		}
		return count;
	}
	
	
}
