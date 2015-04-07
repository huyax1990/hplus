package com.yingmob.web.model;

import java.util.LinkedList;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.yingmob.base.common.Pager;
import com.yingmob.core.plugin.annotation.Table;

@SuppressWarnings("serial")
@Table(tableName = "dev_history")
public class HistoryModel extends Model<HistoryModel>{
	public static final HistoryModel dao = new HistoryModel();
	/**
	 * 发展历程列表数据
	 * 
	 * @param pager
	 * @return
	 */
	public Page<HistoryModel> pageList(Pager pager) {
		StringBuffer sqlExecpt = new StringBuffer(" from dev_history where 1=1 ");
		LinkedList<Object> params = new LinkedList<Object>();
		if (pager.getParamsMap().get("introduction") != null && !"".equals(pager.getParamsMap().get("introduction"))) {
			sqlExecpt.append(" and introduction like CONCAT('%',?,'%') ");
			params.add(pager.getParamsMap().get("introduction"));
		}
		if (pager.getParamsMap().get("time") != null && !"".equals(pager.getParamsMap().get("time"))) {
			sqlExecpt.append(" and time = ? ");
			params.add(pager.getParamsMap().get("time"));
		}
		sqlExecpt.append("  order by time asc");
		return dao
				.paginate(
						pager.getPageNo(),
						pager.getPageSize(),
						" SELECT id, time,image_name, image_path, image_note, introduction, enable, create_time, update_time ",
						sqlExecpt.toString(), params.toArray());
	}
	/**
	 * 批量删除发展历程
	 * @param ids
	 */
	public int batchDel(String ids) {
		int count = 0;
		String[] idsArr = ids.split("\\|");
		for (String id : idsArr) {
			if ("".equals(id))
				continue;
			Db.update(" delete from dev_history where id=? ", id);
			count++;
		}
		return count;	
	}
}
