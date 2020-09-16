package org.tsinghuatj.framework.constants;

import java.util.EnumSet;

public enum RoleEnum {
	admin(1, "管理员"), //
	warehouseKeeper(2, "仓库"),
	quality(3, "质检"), //
	repaire(4, "刃磨"), //
	technology(5, "工艺"),;//

	private Integer code;
	private String roleName;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	private RoleEnum(int code, String roleName) {
		this.setCode(code);
		this.setRoleName(roleName);
	}

	public static String getName(int code) {
		for (RoleEnum entity : EnumSet.allOf(RoleEnum.class)) {
			if (code == entity.getCode()) {
				return entity.getRoleName();
			}
		}
		return null;
	}

	public static int getCode(String name) {
		for (RoleEnum entity : EnumSet.allOf(RoleEnum.class)) {
			if (name.equals(entity.getRoleName())) {
				return entity.getCode();
			}
		}
		return 0;
	}
}
