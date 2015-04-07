package com.yingmob.tools;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Model;
import com.yingmob.common.cache.Cache;
import com.yingmob.common.cache.impl.RedisClient;

/**
 * redis工具类
 * 
 * @author zyz
 *
 */
public class RedisTools {

	private static final Logger LOG = Logger.getLogger(RedisTools.class);
	public static Cache cache = new RedisClient(DevConstants.YINGMOB_CACHE_FILENAME);

	public static void main(String[] args) {
		System.out.println("========");
	}

	/**
	 * redis累加统计
	 * 
	 * @param model
	 * @param redisPrefix redis前缀
	 * @param timeFieldName 时间字段
	 * @param reportNumName 上报次数字段
	 * @param dateFormat 格式化类型
	 * @param veidooFieldNames 维度字段数组
	 */
	public static void incrStats(Model<?> model, String redisPrefix, String timeFieldName, String reportNumName, ThreadLocal<DateFormat> dateFormat, String... veidooFieldNames) {
		if (null == model.get(timeFieldName)) {
			model.set(timeFieldName, new Date());
		}
		// 没有上报统计次数 默认为一条
		if (null == model.get(reportNumName)) {
			model.set(reportNumName, 1);
		}
		Map<String, Object> veidooMap = new LinkedHashMap<String, Object>();
		for (String veidooFieldName : veidooFieldNames) {
			veidooMap.put(veidooFieldName, model.get(veidooFieldName));
		}
		cache.hincr(redisPrefix + DateTools.format(model.getDate(timeFieldName), dateFormat), JSON.toJSONString(veidooMap), model.getInt(reportNumName));
	}

	/**
	 * 保存model到redis缓存
	 * 
	 * @param model
	 * @param redisPrefix redis前缀
	 * @param timeFieldName 时间字段
	 * @param dateFormat 格式化类型
	 */
	public static void saveModelToRedis(Model<?> model, String redisPrefix, String timeFieldName, ThreadLocal<DateFormat> dateFormat) {
		if (model.get(timeFieldName) == null) {
			model.set(timeFieldName, new Date());
		}
		cache.hset((redisPrefix + DateTools.format(model.getDate(timeFieldName), dateFormat)).getBytes(), UUIDTools.getUUID().getBytes(), SerializeTools.serialize(model));
	}

	/**
	 * 保存redis中的统计数据到DB
	 * 并返回保存list
	 * 
	 * @param modelClass model类
	 * @param redisPrefix redis前缀
	 * @param timeFieldName 时间字段
	 * @param StatsFieldName 统计累计字段
	 * @param dateFormat 格式化类型
	 * @param beforHour 统计前几个小时的时间
	 * @return 返回保存list
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> saveStatsModelListFromRedisToDBAndBack(Class<T> modelClass, String redisPrefix, String timeFieldName, String StatsFieldName, ThreadLocal<DateFormat> dateFormat,
			int beforHour) {
		String searchTime = DateTools.getHourBeforeTimeStr(dateFormat, beforHour + RedisConstants.DEL_CACHETIME);
		String hkey = redisPrefix + searchTime;
		try {
			// 删除一天前的数据
			cache.del(hkey);
		} catch (Exception e) {
			LOG.error("---saveStatsModelListFromRedisToDBAndBack[删除失败]:hkey=" + hkey, e);
		}
		searchTime = DateTools.getHourBeforeTimeStr(dateFormat, beforHour);
		hkey = redisPrefix + searchTime;
		Set<String> hkeySet = cache.hkeys(hkey);
		String value = null;
		Map<String, Object> map = null;
		List<T> statsModelList = new ArrayList<T>();
		Model<?> model = null;
		for (String field : hkeySet) {
			try {
				value = cache.hget(hkey, field);
				if (null != value && Integer.parseInt(value) > 0) {
					map = JSON.parseObject(field, Map.class);
					model = (Model<?>) modelClass.newInstance();
					model.setAttrs(map).set(StatsFieldName, value).set(timeFieldName, DateTools.parseDate(searchTime, dateFormat)).save();
					statsModelList.add((T) model);
				}
			} catch (Exception e) {
				LOG.error("---saveStatsModelListFromRedisToDBAndBack[失败]:hkey=" + hkey + ",field=" + field, e);
				continue;
			}
		}
		return statsModelList;
	}

	/**
	 * 保存redis中的统计数据到DB
	 * 
	 * @param modelClass model类
	 * @param redisPrefix redis前缀
	 * @param timeFieldName 时间字段
	 * @param StatsFieldName 统计累计字段
	 * @param dateFormat 格式化类型
	 * @param beforHour 统计前几个小时的时间
	 */
	@SuppressWarnings("unchecked")
	public static void saveStatsModelListFromRedisToDB(Class<?> modelClass, String redisPrefix, String timeFieldName, String StatsFieldName, ThreadLocal<DateFormat> dateFormat, int beforHour) {
		String searchTime = DateTools.getHourBeforeTimeStr(dateFormat, beforHour + RedisConstants.DEL_CACHETIME);
		String hkey = redisPrefix + searchTime;
		try {
			// 删除一天前的数据
			cache.del(hkey);
		} catch (Exception e) {
			LOG.error("---saveStatsModelListFromRedisToDB[删除失败]:hkey=" + hkey, e);
		}
		searchTime = DateTools.getHourBeforeTimeStr(dateFormat, beforHour);
		hkey = redisPrefix + searchTime;
		Set<String> hkeySet = cache.hkeys(hkey);
		String value = null;
		Map<String, Object> map = null;
		for (String field : hkeySet) {
			try {
				value = cache.hget(hkey, field);
				if (null != value && Integer.parseInt(value) > 0) {
					map = JSON.parseObject(field, Map.class);
					((Model<?>) modelClass.newInstance()).setAttrs(map).set(StatsFieldName, value).set(timeFieldName, DateTools.parseDate(searchTime, dateFormat)).save();
				}
			} catch (Exception e) {
				LOG.error("---saveStatsModelListFromRedisToDB[失败]:hkey=" + hkey + ",field=" + field, e);
				continue;
			}
		}
	}

	/**
	 * 保存redis中的统计数据到DB
	 * 
	 * @param modelClass model类
	 * @param redisPrefix redis前缀
	 * @param dayFieldName 每天字段名称
	 * @param hourFiledName 每小时字段名称
	 * @param StatsFieldName 统计累计字段
	 * @param dateFormat 格式化类型
	 * @param beforHour 统计前几个小时的时间
	 */
	@SuppressWarnings("unchecked")
	public static void saveStatsModelListFromRedisToDB(Class<?> modelClass, String redisPrefix, String dayFieldName, String hourFiledName, String StatsFieldName, ThreadLocal<DateFormat> dateFormat,
			int beforHour) {
		String searchTime = DateTools.getHourBeforeTimeStr(dateFormat, beforHour + RedisConstants.DEL_CACHETIME);
		String hkey = redisPrefix + searchTime;
		try {
			// 删除一天前的数据
			cache.del(hkey);
		} catch (Exception e) {
			LOG.error("---saveStatsModelListFromRedisToDB[删除失败]:hkey=" + hkey, e);
		}
		searchTime = DateTools.getHourBeforeTimeStr(dateFormat, beforHour);
		hkey = redisPrefix + searchTime;
		Set<String> hkeySet = cache.hkeys(hkey);
		String value = null;
		Map<String, Object> map = null;
		Model<?> model = null;
		for (String field : hkeySet) {
			try {
				value = cache.hget(hkey, field);
				if (null != value && Integer.parseInt(value) > 0) {
					map = JSON.parseObject(field, Map.class);
					model = (Model<?>) modelClass.newInstance();
					model.setAttrs(map).set(StatsFieldName, value);
					model.set(dayFieldName, DateTools.getDateBySearchTime(searchTime, dateFormat));
					model.set(hourFiledName, DateTools.getTimeBySearchTime(searchTime, dateFormat, Calendar.HOUR_OF_DAY));
					model.save();
				}
			} catch (Exception e) {
				LOG.error("---saveStatsModelListFromRedisToDB[失败]:hkey=" + hkey + ",field=" + field, e);
				continue;
			}
		}
	}

	/**
	 * 保存redis中的model详情到DB
	 * 并返回保存list
	 * 
	 * @param modelClass model类
	 * @param redisPrefix redis前缀
	 * @param dateFormat 格式化类型
	 * @param beforHour 查找前几个小时的时间
	 * @return 返回保存list
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> saveModelListFromRedisToDBAndBack(Class<T> modelClass, String redisPrefix, ThreadLocal<DateFormat> dateFormat, int beforHour) {
		String searchTime = DateTools.getHourBeforeTimeStr(dateFormat, beforHour + RedisConstants.DEL_CACHETIME);
		byte[] hkey = (redisPrefix + searchTime).getBytes();
		try {
			// 删除一天前的数据
			cache.del(hkey);
		} catch (Exception e) {
			LOG.error("---saveModelListFromRedisToDBAndBack[删除失败]:hkey=" + hkey, e);
		}
		searchTime = DateTools.getHourBeforeTimeStr(dateFormat, beforHour);
		hkey = (redisPrefix + searchTime).getBytes();
		Set<byte[]> hkeySet = cache.hkeys(hkey);
		byte[] value = null;
		List<T> modelList = new ArrayList<T>();
		Model<?> model = null;
		for (byte[] field : hkeySet) {
			try {
				value = cache.hget(hkey, field);
				if (null != value && value.length > 0) {
					model = (Model<?>) SerializeTools.deserialize(value);
					model.save();
					modelList.add((T) model);
				}
			} catch (Exception e) {
				LOG.error("---saveModelListFromRedisToDBAndBack[失败]:hkey=" + new String(hkey) + ",field=" + new String(field), e);
				continue;
			}
		}
		return modelList;
	}

	/**
	 * 保存redis中的model详情到DB
	 * 
	 * @param redisPrefix redis前缀
	 * @param dateFormat 格式化类型
	 * @param beforHour 查找前几个小时的时间
	 */
	public static void saveModelListFromRedisToDB(String redisPrefix, ThreadLocal<DateFormat> dateFormat, int beforHour) {
		String searchTime = DateTools.getHourBeforeTimeStr(dateFormat, beforHour + RedisConstants.DEL_CACHETIME);
		byte[] hkey = (redisPrefix + searchTime).getBytes();
		try {
			// 删除一天前的数据
			cache.del(hkey);
		} catch (Exception e) {
			LOG.error("---saveModelListFromRedisToDB[删除失败]:hkey=" + hkey, e);
		}
		searchTime = DateTools.getHourBeforeTimeStr(dateFormat, beforHour);
		hkey = (redisPrefix + searchTime).getBytes();
		Set<byte[]> hkeySet = cache.hkeys(hkey);
		byte[] value = null;
		for (byte[] field : hkeySet) {
			try {
				value = cache.hget(hkey, field);
				if (null != value && value.length > 0) {
					((Model<?>) SerializeTools.deserialize(value)).save();
				}
			} catch (Exception e) {
				LOG.error("---saveModelListFromRedisToDB[失败]:hkey=" + new String(hkey) + ",field=" + new String(field), e);
				continue;
			}
		}
	}

}
