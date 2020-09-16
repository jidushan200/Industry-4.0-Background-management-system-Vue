package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum MeasureCheckTypeEnum {

	NEW(1, "新量具定检"), //
	CYCLE(2, "周期定检"),; //
	

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
	
	
	private MeasureCheckTypeEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	
	public static String getName(int code) {
		for (MeasureCheckTypeEnum entity : EnumSet.allOf(MeasureCheckTypeEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (MeasureCheckTypeEnum entity : EnumSet.allOf(MeasureCheckTypeEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
