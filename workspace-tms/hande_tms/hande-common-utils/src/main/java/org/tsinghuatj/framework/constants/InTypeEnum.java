package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum InTypeEnum {
	NEWTOOL(1, "新刀入库"),
	USEDTOOL(2,"用后返库"),
	REPAIRTOOL(3,"刃磨返库"),
	COATTOOL(4,"涂层返库"); //

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
	
	private InTypeEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	public static String getName(int code) {
		for (InTypeEnum entity : EnumSet.allOf(InTypeEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (InTypeEnum entity : EnumSet.allOf(InTypeEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
