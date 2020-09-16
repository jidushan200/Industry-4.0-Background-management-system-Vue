package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum CoatTypeEnum {
	SPECIALCOAT(1, "特殊涂层"); //

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
	
	private CoatTypeEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	public static String getName(int code) {
		for (CoatTypeEnum entity : EnumSet.allOf(CoatTypeEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (CoatTypeEnum entity : EnumSet.allOf(CoatTypeEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
