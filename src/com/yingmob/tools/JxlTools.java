package com.yingmob.tools;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.LabelCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;

public class JxlTools {

	public final static Sheet initExcel(File upload) {
		Workbook rwb = null;
		Sheet sheet = null;
		try {
			rwb = Workbook.getWorkbook(upload);
			sheet = rwb.getSheet(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sheet;
	}

	/**
	 * 读取excel数据进入excelValue数组中
	 */
	public final static String[][] readExcel(Sheet sheet) {
		String[][] excelValue = new String[sheet.getRows()][sheet.getColumns()];
		for (int i = 0; i < sheet.getRows(); i++) {
			for (int j = 0; j < sheet.getColumns(); j++) {
				Cell cell = sheet.getCell(j, i);
				if ("".equals(cell.getContents().toString().trim())) {
					excelValue[i][j] = "";
				}
				if (cell.getType() == CellType.LABEL) {
					LabelCell lablecell = (LabelCell) cell;
					excelValue[i][j] = lablecell.getString().trim();
				} else if (cell.getType() == CellType.NUMBER) {
					excelValue[i][j] = cell.getContents();
				} else if (cell.getType() == CellType.DATE) {
					DateCell datcell = (DateCell) cell;
					Date excelDate = null;
					excelDate = datcell.getDate();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					excelValue[i][j] = format.format(excelDate);
				} else {
					excelValue[i][j] = cell.getContents().toString().trim();
				}
			}
		}
		return excelValue;
	}
	/**
	 * 根据提供的【列标】、【行标】、【对象值】构建一个Excel列对象。
	 * 
	 * @param beginRow
	 *            【行标】
	 * @param beginColumn
	 *            【列标】
	 * @param obj
	 *            【对象值】
	 * @return
	 */
	public final static WritableCell getWritableCellByObject(int beginRow, int beginColumn, Object obj) {
		WritableCell cell = null;

		if (obj.getClass().getName().equals(String.class.getName())) {
			cell = new Label(beginColumn, beginRow, filterHTML(obj.toString()));
		} else if (obj.getClass().getName().equals(int.class.getName()) || obj.getClass().getName().equals(Integer.class.getName())) {
			// jxl.write.Number
			cell = new Number(beginColumn, beginRow, Integer.parseInt(obj.toString()));
		} else if (obj.getClass().getName().equals(float.class.getName()) || obj.getClass().getName().equals(Float.class.getName())) {
			cell = new Number(beginColumn, beginRow, Float.parseFloat(obj.toString()));
		} else if (obj.getClass().getName().equals(double.class.getName()) || obj.getClass().getName().equals(Double.class.getName())) {
			cell = new Number(beginColumn, beginRow, Double.parseDouble(obj.toString()));
		} else if (obj.getClass().getName().equals(long.class.getName()) || obj.getClass().getName().equals(Long.class.getName())) {
			cell = new Number(beginColumn, beginRow, Long.parseLong(obj.toString()));
		} else if (obj.getClass().getName().equals(Date.class.getName())) {
			cell = new DateTime(beginColumn, beginRow, (Date) obj);
		} else {
			cell = new Label(beginColumn, beginRow, filterHTML(obj.toString()));
		}
		return cell;
	}

	private static String filterHTML(String str) {
		if (str == null)
			return null;
		// 去掉标签
		String regEx = "<.+?>"; // 表示标签
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		str = m.replaceAll("");
		return str;
	}
}
