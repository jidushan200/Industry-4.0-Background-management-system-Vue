package org.tsinghuatj.framework.support;

/**
 * @author Cobain
 * @category 全局异常统一处理类
 * @version 2016-09-27 初始化
 */
public class BusinessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4735166167741570391L;

	private Integer code = 500;
	private String info = "";
	private Object[] param;

	public BusinessException() {
		super();
	}

	public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.info = message;
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		this.info = message;
	}

	public BusinessException(String message) {
		super(message);
		this.info = message;
	}

	public BusinessException(String message, Object[] param) {
		super(message);
		this.info = message;
		this.param = param;
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Object[] getParam() {
		return param;
	}

	public void setParam(Object[] param) {
		this.param = param;
	}

}
