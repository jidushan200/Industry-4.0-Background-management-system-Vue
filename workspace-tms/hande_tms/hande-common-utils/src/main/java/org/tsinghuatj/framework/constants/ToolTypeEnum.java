package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum ToolTypeEnum {
	HOB(1, "滚刀"), //
	BROACH(2, "拉刀"),
	SHAVING(3, "剃齿刀"), //
	CUTTERHEAD(4, "刀头"), //
	CUTTERBAR(5, "刀条"),;//
	

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
	
	
	private ToolTypeEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	
	public static String getName(int code) {
		for (ToolTypeEnum entity : EnumSet.allOf(ToolTypeEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (ToolTypeEnum entity : EnumSet.allOf(ToolTypeEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
