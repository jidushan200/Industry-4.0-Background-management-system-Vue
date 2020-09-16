package org.tsinghuatj.framework.support;

/**
 * 描述：
 * <p>
 *
 * @author: 杨宏亮
 * @date: 2019/4/11 10:24
 */
public class TokenException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer code = 501;
	private String info = "";
	private Object[] param;

	public TokenException(String message, Throwable cause) {
		super(message, cause);
		this.info = message;
	}

	public TokenException(String message) {
		super(message);
		this.info = message;
	}

	public TokenException(String message, Object[] param) {
		super(message);
		this.info = message;
		this.param = param;
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
