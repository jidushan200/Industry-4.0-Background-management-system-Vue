package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum ApplyStatusEnum {
	PRODUCTION(1, "待生产部领导审核"), //
	TECHNICAL(2, "待技术部审核"),
	FINALL(3, "最终审核"),;//
	

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
	
	
	private ApplyStatusEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	
	public static String getName(int code) {
		for (ApplyStatusEnum entity : EnumSet.allOf(ApplyStatusEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (ApplyStatusEnum entity : EnumSet.allOf(ApplyStatusEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
