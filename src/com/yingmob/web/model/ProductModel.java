package com.yingmob.web.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.yingmob.base.common.Pager;
import com.yingmob.core.plugin.annotation.Table;

@SuppressWarnings("serial")
@Table(tableName = "product")
public class ProductModel extends Model<ProductModel>
{
	public static final ProductModel dao = new ProductModel();
	
	/**
	 * 获取新闻列表数据
	 * 
	 * @return
	 */
	public Page<ProductModel> pageList(Pager pager) {
		//return dao.find("SELECT id, name, image_name, image_path,enable,introduction,sort,create_time,update_time from product order by sort asc");
		return dao.paginate(pager.getPageNo(), pager.getPageSize(), " SELECT id, name, image_name, image_path,enable,introduction,sort,create_time,update_time ", "from product order by sort asc");
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
			Db.update(" delete from product where id=?  ", id);
			count++;
		}
		return count;
	}
}
