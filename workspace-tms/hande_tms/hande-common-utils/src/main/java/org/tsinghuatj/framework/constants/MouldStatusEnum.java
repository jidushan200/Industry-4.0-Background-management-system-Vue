package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum MouldStatusEnum {

	WAIT(0, "待入库"), //
	STAY(1, "在库"), //
	USE(2, "使用"),
	REPAIR(3, "待修磨"), //
	WAITCHECK(4, "待检"), //
	RETURN(5, "待返库"), //
	SCRIP(6, "不合格待处理"),
	UNQUALITY(7, "报废"),; //
	

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
	
	
	private MouldStatusEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	
	public static String getName(int code) {
		for (MouldStatusEnum entity : EnumSet.allOf(MouldStatusEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (MouldStatusEnum entity : EnumSet.allOf(MouldStatusEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
