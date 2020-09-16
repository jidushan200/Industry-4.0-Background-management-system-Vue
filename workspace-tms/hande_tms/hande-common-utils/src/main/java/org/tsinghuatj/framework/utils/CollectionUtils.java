package org.tsinghuatj.framework.utils;

import java.util.Map;

public class CollectionUtils extends org.apache.commons.collections.CollectionUtils {
	/**
	 * @Title: isEmpty
	 * @Description: 判断集合类型为空
	 * @param map
	 * @return
	 */
	public static Boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * @Title: isNotEmpty
	 * @Description: 判断集合类型不为空
	 * @param map
	 * @return
	 */
	public static Boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}
}
