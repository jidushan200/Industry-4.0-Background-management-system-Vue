package org.tsinghuatj.sys.domain;

import java.io.Serializable;
import java.util.List;



import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AuthDto implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiParam(name = "userId", value = "用户ID", required = true)	
	private Long userId;

	@ApiParam(name = "roleId", value = "角色ID", required = true)	
	private Long roleId;
	
	

	@ApiParam(name = "roleName", value = "角色名称")	
	private String roleName;


	@ApiParam(name = "authCodes", value = "授权码集合（逗号隔开）", required = true)	
	private List<String> authCodes;
	
	@ApiParam(name = "fullAuths", value = "完整的授权码集合（逗号隔开）")	
	private List<String> fullAuths;

}
