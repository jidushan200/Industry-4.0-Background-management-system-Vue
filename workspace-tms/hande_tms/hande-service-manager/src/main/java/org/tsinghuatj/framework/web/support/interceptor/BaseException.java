package org.tsinghuatj.framework.web.support.interceptor;

/**
 * 描述：
 * <p>
 *
 * @author: 杨宏亮
 * @date: 2019/4/11 23:06
 */
public class BaseException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

