package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum PriceTypeEnum {
	RMB(1, "人民币"), //
	DOLLER(2, "美元"),
	EURO(3, "欧元"),;//
	

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
	
	
	private PriceTypeEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	
	public static String getName(int code) {
		for (PriceTypeEnum entity : EnumSet.allOf(PriceTypeEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (PriceTypeEnum entity : EnumSet.allOf(PriceTypeEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
