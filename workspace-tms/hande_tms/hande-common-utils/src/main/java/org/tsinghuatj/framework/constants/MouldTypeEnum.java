package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum MouldTypeEnum {

	COMMON(1,"普锻模具"),
	PRECISION(2,"精锻模具"),; //

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
	
	private MouldTypeEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	public static String getName(int code) {
		for (MouldTypeEnum entity : EnumSet.allOf(MouldTypeEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (MouldTypeEnum entity : EnumSet.allOf(MouldTypeEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
