package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum MeasureStatusEnum {

	WAIT(1, "待入库"), //
	STAY(2, "在库"),
	CYCLE(3, "待完善周期"), //
	USING(4, "使用"), //
	CHECK(5, "返库待检"), //
	SEAL(6, "封存"), //
	LOST(7, "丢失"), //
	SCRIP(8, "报废"),; //
	

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
	
	
	private MeasureStatusEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	
	public static String getName(int code) {
		for (MeasureStatusEnum entity : EnumSet.allOf(MeasureStatusEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (MeasureStatusEnum entity : EnumSet.allOf(MeasureStatusEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
