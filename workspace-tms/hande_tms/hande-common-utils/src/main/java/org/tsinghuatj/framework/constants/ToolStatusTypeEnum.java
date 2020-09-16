package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum ToolStatusTypeEnum {
	STAY(1, "在库"), //
	USING(2, "使用"),
	WAITREPAIR(3, "待刃磨"), //
	REPAIRING(4, "刃磨"), //
	WAITCOAT(5, "已送涂"), //
	COATING(6, "涂层"), //
	WAITCHECK(7, "刃磨待检"), //
	RETURN(8, "交回在库"),
	CHECKED(9, "质检完成"), //
	SCRIP(10, "已报废"),; //
	

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
	
	
	private ToolStatusTypeEnum(int code,String typeName){
		this.setCode(code);
		this.setTypeName(typeName);
	}
	
	
	public static String getName(int code) {
		for (ToolStatusTypeEnum entity : EnumSet.allOf(ToolStatusTypeEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getTypeName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (ToolStatusTypeEnum entity : EnumSet.allOf(ToolStatusTypeEnum.class)) {
			if (name.equals(entity.getTypeName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
