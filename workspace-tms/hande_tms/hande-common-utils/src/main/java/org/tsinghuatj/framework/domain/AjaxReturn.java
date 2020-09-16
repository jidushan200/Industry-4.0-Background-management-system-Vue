package org.tsinghuatj.framework.domain;

public class AjaxReturn {
	public final static int SUCCESS = 200;
	protected java.lang.Integer code = SUCCESS;
	protected java.lang.String info;
	protected java.lang.Object data;

	public AjaxReturn() {
		super();
	}

	public AjaxReturn(Integer code, String info, Object data) {
		super();
		this.code = code;
		this.info = info;
		this.data = data;
	}

	public AjaxReturn(Integer code, String info) {
		super();
		this.code = code;
		this.info = info;
	}

	public java.lang.Integer getCode() {
		return code;
	}

	public void setCode(java.lang.Integer code) {
		this.code = code;
	}

	public java.lang.String getInfo() {
		return info;
	}

	public void setInfo(java.lang.String info) {
		this.info = info;
	}

	public java.lang.Object getData() {
		return data;
	}

	public void setData(java.lang.Object data) {
		this.data = data;
	}

}
