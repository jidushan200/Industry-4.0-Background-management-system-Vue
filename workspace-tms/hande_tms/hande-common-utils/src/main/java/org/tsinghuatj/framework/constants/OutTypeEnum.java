package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum OutTypeEnum {
	USEDTOOL(1,"领用出库"),
	REPAIRTOOL(2,"刃磨出库"),
	COATTOOL(3,"涂层出库"); //

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
	
	private OutTypeEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	public static String getName(int code) {
		for (OutTypeEnum entity : EnumSet.allOf(OutTypeEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (OutTypeEnum entity : EnumSet.allOf(OutTypeEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
