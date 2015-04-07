package com.yingmob.web.model;

import java.util.LinkedList;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.yingmob.base.common.Pager;
import com.yingmob.core.plugin.annotation.Table;

@SuppressWarnings("serial")
@Table(tableName = "culture")
public class CultureModel extends Model<CultureModel>
{
	public static final CultureModel dao = new CultureModel();
	
	/**
	 * 获取新闻列表数据
	 * 
	 * @return
	 */
	public Page<CultureModel> pageList(Pager pager) {
		StringBuffer sqlExecpt = new StringBuffer(" from culture where module=2 ");
		LinkedList<Object> params = new LinkedList<Object>();
		if (pager.getParamsMap().get("title") != null && !"".equals(pager.getParamsMap().get("title"))) {
			sqlExecpt.append(" and title like CONCAT('%',?,'%') ");
			params.add(pager.getParamsMap().get("title"));
		}
		
		sqlExecpt.append("  order by sort asc");
		return dao.paginate(pager.getPageNo(), pager.getPageSize(), " SELECT id, title, image_name, image_path,enable,module,introduction,sort,create_time,update_time ", sqlExecpt.toString(), params.toArray());
	}

	/**
	 * 获取文化列表数据
	 * 
	 * @return
	 */
	public Page<CultureModel> pageListEnt(Pager pager) {
		StringBuffer sqlExecpt = new StringBuffer(" from culture where module=1 ");
		LinkedList<Object> params = new LinkedList<Object>();
		if (pager.getParamsMap().get("title") != null && !"".equals(pager.getParamsMap().get("title"))) {
			sqlExecpt.append(" and title like CONCAT('%',?,'%') ");
			params.add(pager.getParamsMap().get("title"));
		}
		
		sqlExecpt.append("  order by sort asc");
		return dao.paginate(pager.getPageNo(), pager.getPageSize(), " SELECT id, title, image_name, image_path,enable,module,introduction,sort,create_time,update_time ", sqlExecpt.toString(), params.toArray());
	}
	/**
	 * 批量删除指定ID的数据
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
			Db.update(" delete from culture where module=2  and id=?  ", id);
			count++;
		}
		return count;
	}
	
	/**
	 * 批量删除文化列表中指定ID的数据
	 * 
	 * @param ids
	 * @return
	 */
	public int batchDelEnt(String ids) {
		int count = 0;
		String[] idsArr = ids.split("\\|");
		for (String id : idsArr) {
			if ("".equals(id))
				continue;
			Db.update(" delete from culture where module=1  and id=?  ", id);
			count++;
		}
		return count;
	}
}
