package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum ReportTypeEnum {
	NEWTOOL(1, "新刀不合格"), //
	REPAIR(2, "刃磨不合格"),
	COAT(3, "涂层不合格"),;//
	

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
	
	
	private ReportTypeEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	
	public static String getName(int code) {
		for (ReportTypeEnum entity : EnumSet.allOf(ReportTypeEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (ReportTypeEnum entity : EnumSet.allOf(ReportTypeEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
