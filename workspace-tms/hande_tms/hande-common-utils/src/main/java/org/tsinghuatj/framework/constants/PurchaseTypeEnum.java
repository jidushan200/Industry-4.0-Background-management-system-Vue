package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum PurchaseTypeEnum {
	NEWTOOL(1, "新刀开发"), //
	COMMON(2, "常用刀具"),;//
	

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
	
	
	private PurchaseTypeEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	
	public static String getName(int code) {
		for (PurchaseTypeEnum entity : EnumSet.allOf(PurchaseTypeEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (PurchaseTypeEnum entity : EnumSet.allOf(PurchaseTypeEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
