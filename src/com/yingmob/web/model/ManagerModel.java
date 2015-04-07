package com.yingmob.web.model;

import java.util.LinkedList;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.yingmob.base.common.Pager;
import com.yingmob.core.plugin.annotation.Table;
@SuppressWarnings("serial")
@Table(tableName = "manager")
public class ManagerModel extends  Model<ManagerModel>{
	public static final ManagerModel dao = new ManagerModel();
	/**
	 * 高管风采列表数据
	 * 
	 * @param pager
	 * @return
	 */
	public Page<ManagerModel> pageList(Pager pager) {
		StringBuffer sqlExecpt = new StringBuffer(" from manager where 1=1 ");
		LinkedList<Object> params = new LinkedList<Object>();
		if (pager.getParamsMap().get("name") != null && !"".equals(pager.getParamsMap().get("name"))) {
			sqlExecpt.append(" and name like CONCAT('%',?,'%') ");
			params.add(pager.getParamsMap().get("name"));
		}
		sqlExecpt.append("  order by sort asc");
		return dao
				.paginate(
						pager.getPageNo(),
						pager.getPageSize(),
						" SELECT id, name,image_name, image_path, summary, enable, sort, create_time, update_time ",
						sqlExecpt.toString(), params.toArray());
	}
	/**
	 * 批量删除高管风采
	 * @param ids
	 */
	public int batchDel(String ids) {
		int count = 0;
		String[] idsArr = ids.split("\\|");
		for (String id : idsArr) {
			if ("".equals(id))
				continue;
			Db.update(" delete from manager where id=? ", id);
			count++;
		}
		return count;	
	}
}
