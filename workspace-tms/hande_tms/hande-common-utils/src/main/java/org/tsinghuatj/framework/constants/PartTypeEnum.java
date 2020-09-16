package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum PartTypeEnum {
	BEVEL(1, "螺伞齿轮"), //
	CYLINDRICAL(2, "圆柱齿轮");

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
	
	
	private PartTypeEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	
	public static String getName(int code) {
		for (PartTypeEnum entity : EnumSet.allOf(PartTypeEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (PartTypeEnum entity : EnumSet.allOf(PartTypeEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
