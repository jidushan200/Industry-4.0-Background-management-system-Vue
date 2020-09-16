package org.tsinghuatj.framework.utils;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateFormatUtils extends org.apache.commons.lang3.time.DateFormatUtils {
	private final static DateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");
	private final static DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
	private final static DateFormat df3 = new SimpleDateFormat("HHmmss");

	/**
	 * 获取格式化之后的时间字符串
	 * 
	 * @param date
	 *            时间
	 * @param pattern
	 *            样式 如： yyyy-MM-dd HH:mm:ss
	 * @return 时间字符串
	 */
	public static String getDateString(Date date, String pattern) {
		return null == date ? "" : new DateTime(date).toString(pattern);
	}

	/**
	 * 解析时间日期
	 * 
	 * @param text
	 * @return
	 */
	public static Date parse(String text) {
		Date date = null;
		try {
			if (StringUtils.isNotBlank(text)) {
				//text = StringUtils.replacePattern(text, "\\D", "");
				if (StringUtils.length(text) == 14) {
					date = df1.parse(text);
				} else if (StringUtils.length(text) == 8) {
					date = df2.parse(text);
				} else if (StringUtils.length(text) == 6) {
					date = df3.parse(text);
				}
			}
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * 解析时间日期
	 * 
	 * @param text
	 * @param def
	 * @return
	 */
	public static Date parse(String text, Date def) {
		Date date = null;
		try {
			if (StringUtils.isNotBlank(text)) {
				//text = StringUtils.replacePattern(text, "\\D", "");
				if (StringUtils.length(text) == 14) {
					date = df1.parse(text);
				} else if (StringUtils.length(text) == 8) {
					date = df2.parse(text);
				} else if (StringUtils.length(text) == 6) {
					date = df3.parse(text);
				}
			}
		} catch (Exception e) {
		}
		return date == null ? def : date;
	}

	/**
	 * 解析时间日期
	 *
	 * @param text
	 * @param format
	 * @return
	 */
	public static Date parse(String text, String format) {
		Date date = null;
		try {
			date = new SimpleDateFormat(format).parse(text);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * 解析时间日期
	 *
	 * @param text
	 * @param format
	 * @return
	 */
	public static Date parse(long date) {
		return new Date(date);
	}

	public static void main(String args[]) {

	}
}
