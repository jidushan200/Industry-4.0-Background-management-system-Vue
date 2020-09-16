package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum MouldCompromiseEnum {

	COMPROMISE(1,"让步使用"),
	NORMAL(2,"正常使用"),; //

	private int code;
	private String typeName;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	private MouldCompromiseEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	public static String getName(int code) {
		for (MouldCompromiseEnum entity : EnumSet.allOf(MouldCompromiseEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (MouldCompromiseEnum entity : EnumSet.allOf(MouldCompromiseEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
