package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum StaffStatusEnum {
	ACTIVE(1, "在职"), //
	QUIT(2, "离职");

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
	
	
	private StaffStatusEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	
	public static String getName(int code) {
		for (StaffStatusEnum entity : EnumSet.allOf(StaffStatusEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (StaffStatusEnum entity : EnumSet.allOf(StaffStatusEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
