package org.tsinghuatj.framework.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public final class CommUtil {
	private static final Gson gson = new Gson();

	private static final ThreadLocal<SimpleDateFormat> sf = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private static final ThreadLocal<DecimalFormat> df2 = new ThreadLocal<DecimalFormat>() {
		@Override
		protected DecimalFormat initialValue() {
			return new DecimalFormat("#.##");
		}
	};

	private static final ThreadLocal<DecimalFormat> df4 = new ThreadLocal<DecimalFormat>() {
		@Override
		protected DecimalFormat initialValue() {
			return new DecimalFormat("#.####");
		}
	};

	/**
	 * 字符串时间转成日期格式，格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param time
	 * @return
	 */
	public static Date parseTime(String time) {
		if (StringUtils.isBlank(time)) {
			return null;
		}
		try {
			return sf.get().parse(time);
		} catch (ParseException e) {
		}
		return null;
	}

	/**
	 * 日期格式化成字符串，格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String formatTime(Date date) {
		if (date == null) {
			return "";
		}
		return sf.get().format(date);
	}

	/**
	 * 数值格式化成2位小数
	 * 
	 * @param decimal
	 * @return
	 */
	public static String formatDouble(double decimal) {
		return df2.get().format(decimal);
	}

	/**
	 * 数值格式化成2位小数
	 * 
	 * @param decimal
	 * @return
	 */
	public static Double formatDouble2(double decimal) {
		return Double.valueOf(formatDouble(decimal));
	}

	/**
	 * 数值格式化成4位小数
	 * 
	 * @param decimal
	 * @return
	 */
	public static Double formatDouble4(double decimal) {
		return Double.valueOf(df4.get().format(decimal));
	}

	/**
	 * json转成List对象
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> json2list(String json, Class<T> clazz) {
		if (StringUtils.isBlank(json)) {
			return null;
		}
		TypeToken<List<Map<String, Object>>> token = new TypeToken<List<Map<String, Object>>>() {
		};
		List<Map<String, Object>> list = gson.fromJson(json, token.getType());
		List<T> result = new ArrayList<T>();
		for (Map<String, Object> map : list) {
			result.add(gson.fromJson(gson.toJson(map), clazz));
		}
		return result;
	}

	/**
	 * json转成obj对象
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T json2obj(String json, Class<T> clazz) {
		if (StringUtils.isBlank(json)) {
			return null;
		}
		return gson.fromJson(json, clazz);
	}

	/**
	 * 获取用户真实IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		// 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}
}
