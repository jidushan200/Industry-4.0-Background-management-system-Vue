package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum GenderEnum {
	MEN(1, "男"), //
	WOMEN(2, "女");

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
	
	
	private GenderEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	
	public static String getName(int code) {
		for (GenderEnum entity : EnumSet.allOf(GenderEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (GenderEnum entity : EnumSet.allOf(GenderEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
