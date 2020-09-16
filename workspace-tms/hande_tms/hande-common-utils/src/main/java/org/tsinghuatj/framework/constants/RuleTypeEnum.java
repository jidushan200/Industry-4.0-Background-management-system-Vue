package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum RuleTypeEnum {
	TOOLRULE(1, "刀具编码"), //
	WAREHOUSErULE(2, "库位编码");

	private int code;
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
	private String typeName;
	
	private RuleTypeEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	
	public static String getName(int code) {
		for (RuleTypeEnum entity : EnumSet.allOf(RuleTypeEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (RuleTypeEnum entity : EnumSet.allOf(RuleTypeEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
