package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum AppendixTypeEnum {
	NEWTOOL(1, "新刀质检报告"), //
	REPAIR(2, "刃磨质检报告"),
	COAT(3, "涂层质检报告"),;//
	

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
	
	
	private AppendixTypeEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	
	public static String getName(int code) {
		for (AppendixTypeEnum entity : EnumSet.allOf(AppendixTypeEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (AppendixTypeEnum entity : EnumSet.allOf(AppendixTypeEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
