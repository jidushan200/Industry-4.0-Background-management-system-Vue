package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum CheckTypeEnum {
	NEWTOOL(1, "新刀检验"),
	REPAIRTOOL(2,"刃磨检验"),
	COATTOOL(3,"涂层检验"); //

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
	
	private CheckTypeEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	public static String getName(int code) {
		for (CheckTypeEnum entity : EnumSet.allOf(CheckTypeEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (CheckTypeEnum entity : EnumSet.allOf(CheckTypeEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
