package org.tsinghuatj.framework.utils;

import org.tsinghuatj.framework.utils.support.sequence.Sequence;

/**
 * 序列化工具
 */
public class SequenceUtils {
	private static Sequence sequence = new Sequence(1L, 1L);

	/**
	 * 生成序列号
	 * 
	 * @return
	 */
	public static synchronized Long nextId() {
		return sequence.nextId();
	}
}
