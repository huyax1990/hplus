package com.yingmob.core.kit;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.beetl.core.BeetlKit;

import com.jfinal.log.Logger;
import com.yingmob.core.plugin.sqlinxml.SqlGroup;
import com.yingmob.core.plugin.sqlinxml.SqlItem;

/**
 * 处理Sql Map
 * 
 * 说明：加载sql map中的sql到map中，并提供动态长度sql处理
 */
public class SqlXmlKit {

	protected static final Logger LOG = Logger.getLogger(SqlXmlKit.class);

	/**
	 * xml中所有的sql语句
	 */
	private static final Map<String, String> sqlMap = new HashMap<String, String>();

	/**
	 * 过滤掉的sql关键字
	 */
	private static final List<String> badKeyWordList = new ArrayList<String>();

	/**
	 * 加载关键字到List
	 */
	static {
		String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|"
				+ "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" + "table|from|grant|use|group_concat|column_name|"
				+ "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" + "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";
		badKeyWordList.addAll(Arrays.asList(badStr.split("\\|")));
	}

	/**
	 * sql查询关键字过滤效验
	 * 
	 * @param queryStr
	 * @return
	 */
	public static boolean keywordVali(String queryStr) {
		queryStr = queryStr.toLowerCase();//统一转为小写
		for (String badKeyWord : badKeyWordList) {
			if (queryStr.indexOf(badKeyWord) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取SQL，固定SQL
	 * 
	 * @param sqlId
	 * @return
	 */
	public static String getSql(String sqlId) {
		String sql = sqlMap.get(sqlId);
		if (null == sql || sql.isEmpty()) {
			LOG.error("sql语句不存在:id=" + sqlId);
		}
		return sql.replaceAll("[\\s]{2,}", " ");
	}

	/**
	 * 获取SQL，动态SQL
	 * 
	 * @param sqlId
	 * @param param
	 * @return
	 */
	public static String getSql(String sqlId, Map<String, Object> param) {
		String sqlTemplete = sqlMap.get(sqlId);
		if (null == sqlTemplete || sqlTemplete.isEmpty()) {
			LOG.error("sql语句不存在:id=" + sqlId);
		}
		return BeetlKit.render(sqlTemplete, param).replaceAll("[\\s]{2,}", " ");
	}

	/**
	 * 获取SQL，动态SQL
	 * 
	 * @param sqlId
	 * @param param 查询参数
	 * @param list 用于接收预处理的值
	 * @return
	 */
	public static String getSql(String sqlId, Map<String, Object> param, LinkedList<Object> list) {
		String sqlTemplete = sqlMap.get(sqlId);
		if (null == sqlTemplete || sqlTemplete.isEmpty()) {
			LOG.error("sql语句不存在:id=" + sqlId);
		}
		String sql = BeetlKit.render(sqlTemplete, param);
		Pattern pattern = Pattern.compile("#[\\w\\d\\$\\'\\%\\_]+#"); //#[\\w\\d]+#    \\$
		Pattern pattern2 = Pattern.compile("\\$[\\w\\d\\_]+\\$");
		Matcher matcher = pattern.matcher(sql);
		while (matcher.find()) {
			String clounm = matcher.group(0); // 得到的结果形式：#'%$names$%'#
			Matcher matcher2 = pattern2.matcher(clounm);
			matcher2.find();
			String clounm2 = matcher2.group(0); // 得到的结果形式：$names$
			String clounm3 = clounm2.replace("$", "");
			String val = String.valueOf(param.get(clounm3));
			if (clounm.equals("#" + clounm2 + "#")) { // 数值型，可以对应处理int、long、bigdecimal、double等等
				try {
					Integer.parseInt(val);
					sql = sql.replace(clounm, val);
				} catch (NumberFormatException e) {
					LOG.error("查询参数值错误,整型值传入了字符串,非法字符串:val=" + val);
					return null;
				}
			} else { // 字符串，主要是字符串模糊查询、日期比较的查询
				String clounm4 = clounm.replace("#", "").replace("'", "").replace(clounm2, val);
				list.add(clounm4);
				sql = sql.replace(clounm, "?");
			}
		}
		return sql.replaceAll("[\\s]{2,}", " ");
	}

	/**
	 * 清楚加载的sql
	 */
	public static void destory() {
		sqlMap.clear();
	}

	/**
	 * 初始化加载sql语句到map
	 */
	public static void init() {
		File file = new File(SqlXmlKit.class.getClassLoader().getResource("").getFile());
		List<File> files = new ArrayList<File>();
		findFiles(file, files);
		for (File xmlfile : files) {
			String fileName = xmlfile.getName();
			try {
				SqlGroup group = JaxbKit.unmarshal(xmlfile, SqlGroup.class);
				String namespace = group.namespace;
				if (null == namespace || namespace.trim().isEmpty()) {
					LOG.error("sqlxml文件命名空间不能为空:fileName=" + fileName);
					continue;
				}
				for (SqlItem sqlItem : group.sqlItems) {
					String id = sqlItem.id;
					if (null == id || id.trim().isEmpty()) {
						LOG.error("sqlxml文件存在没有id的sql语句:fileName=" + fileName);
						continue;
					}
					String sql = sqlItem.value;
					if (null == sql || sql.trim().isEmpty()) {
						LOG.error("sqlxml文件存在没有内容的sql语句:fileName=" + fileName);
						continue;
					}
					String sqlKey = namespace + "." + id;
					if (sqlMap.containsKey(sqlKey)) {
						LOG.error("sqlxml文件sql语句存在重复空间名称和ID:fileName=" + fileName + ",sqlKey=" + sqlKey);
						continue;
					}
					sql = sql.replaceAll("[\\s]{2,}", " ");
					sqlMap.put(sqlKey, sql);
					LOG.debug("sqlxml文件添加成功:fileName=" + fileName + ",sqlKey=" + sqlKey + ",sql=" + sql);
				}
			} catch (Exception e) {
				LOG.error("sqlxml文件解析异常:fileName=" + fileName);
				continue;
			}
		}
	}

	/**
	 * 递归查找文件
	 * 
	 * @param baseFile
	 * @param sqlXmlFiles
	 * @return
	 */
	private static List<File> findFiles(File baseFile, List<File> sqlXmlFiles) {
		if (!baseFile.isDirectory()) {
			if (baseFile.getName().endsWith(".sql.xml")) {
				sqlXmlFiles.add(baseFile);
			}
		} else {
			File[] fileList = baseFile.listFiles();
			for (File file : fileList) {
				if (file.isDirectory()) {
					findFiles(file, sqlXmlFiles);
				} else {
					if (file.getName().endsWith(".sql.xml")) {
						sqlXmlFiles.add(file);
					}
				}
			}
		}
		return sqlXmlFiles;
	}

}