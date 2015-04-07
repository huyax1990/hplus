package com.yingmob.web.model;

import java.util.LinkedList;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.yingmob.base.common.Pager;
import com.yingmob.core.plugin.annotation.Table;
@SuppressWarnings("serial")
@Table(tableName = "recruit")
public class RecruitModel  extends Model<RecruitModel>{
	
	public static final RecruitModel dao = new RecruitModel();

	/**
	 * 获取列表数据
	 * 
	 * @return
	 */
	public Page<RecruitModel> pageList(Pager pager) {
		StringBuffer sqlExecpt = new StringBuffer(" from recruit left join sys_config config1 on category=config1.id left join sys_config config2 on city=config2.id where 1=1 ");
		LinkedList<Object> params = new LinkedList<Object>();
		if (pager.getParamsMap().get("post") != null && !"".equals(pager.getParamsMap().get("post"))) {
			sqlExecpt.append(" and recruit.post like CONCAT('%',?,'%') ");
			params.add(pager.getParamsMap().get("post"));
		}
		
		if (pager.getParamsMap().get("category") != null && !"".equals(pager.getParamsMap().get("category"))) {
			sqlExecpt.append(" and recruit.category = ? ");
			params.add(pager.getParamsMap().get("category"));
		}
		
		if (pager.getParamsMap().get("city") != null && !"".equals(pager.getParamsMap().get("city"))) {
			sqlExecpt.append(" and recruit.city = ? ");
			params.add(pager.getParamsMap().get("city"));
		}
		
		if (pager.getParamsMap().get("enable") != null && !"".equals(pager.getParamsMap().get("enable"))) {
			sqlExecpt.append(" and recruit.enable = ? ");
			params.add(pager.getParamsMap().get("enable"));
		}
		
		if (pager.getParamsMap().get("type") != null && !"".equals(pager.getParamsMap().get("type"))) {
			sqlExecpt.append(" and recruit.type = ? ");
			params.add(pager.getParamsMap().get("type"));
		}
		
		sqlExecpt.append("  order by recruit.update_time desc ");
		return dao.paginate(pager.getPageNo(), pager.getPageSize(),
				" SELECT recruit.id, post, category, num,city,university,place,hold_date,hold_time,release_time,"
				+ "`require`,responsibility,type,deadline,`enable`,"
				+ "recruit.create_time,recruit.update_time,config1.val catename,config2.val cityname ", sqlExecpt.toString(), params.toArray());
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
			Db.update(" delete from recruit where 1=1  and id=?  ", id);
			count++;
		}
		return count;
	}
	
	
}
