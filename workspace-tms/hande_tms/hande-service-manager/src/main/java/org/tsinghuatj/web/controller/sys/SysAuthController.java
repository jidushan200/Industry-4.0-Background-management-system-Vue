package org.tsinghuatj.web.controller.sys;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.sys.domain.SysAuthInfo;
import org.tsinghuatj.sys.service.ISysAuthInfoService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/auth" })
public class SysAuthController extends BaseController{
	
	private @Autowired(required = false) ISysAuthInfoService sysAuthInfoService;
	
	/**
	 * 权限查询
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value="权限列表",code=200,produces = "application/json",notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "权限列表获取成功", response = SysAuthInfo.class) })
	@RequestMapping(method=RequestMethod.POST,value="/sys-auth-list")
	//@Secure(any = {"010101","010102" })
	public AjaxReturn sysAuthList()throws BusinessException{
		log.debug("SysAuthController.sysAuthList<<<");

		//获取当前用户
		Long userId = getAuthentication();
		Validate.isTrue(userId > 0);
		// 封装查询条件		
		log.debug("SysAuthController.sysAuthList>>>");
		return new AjaxReturn(200,null,sysAuthInfoService.selectById(userId, 1l));
	}
	
	/**
	 * 角色权限查询
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value="角色权限查询",code=200,produces = "application/json",notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "角色权限查询", response = SysAuthInfo.class) })
	@RequestMapping(method=RequestMethod.POST,value="/sys-auth-list-by-role-id")
	//@Secure(any = {"010101","010102" })
	public AjaxReturn sysAuthListByRoleId(@ApiParam(name = "roleId",value = "roleId",required = true)@RequestParam(required = true)Long roleId)throws BusinessException{
		log.debug("SysAuthController.sysAuthListByRoleId<<<");

		//获取当前用户
		Long userId = getAuthentication();
		Validate.isTrue(userId > 0);
		// 封装查询条件		
		log.debug("SysAuthController.sysAuthListByRoleId>>>");
		return new AjaxReturn(200,null,sysAuthInfoService.selectAuthTreeByRoleId(userId, roleId));
	}
	
	/**
	 * 用户权限查询
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value="用户权限查询",code=200,produces = "application/json",notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "用户权限查询", response = SysAuthInfo.class) })
	@RequestMapping(method=RequestMethod.POST,value="/sys-auth-list-by-user-id")
	//@Secure(any = {"010101","010102" })
	public AjaxReturn sysAuthListByUserId(@ApiParam(name = "userId",value = "userId",required = true)@RequestParam(required = true)Long userId)throws BusinessException{
		log.debug("SysAuthController.sysAuthListByUserId<<<");
		//获取当前用户
		Long uId = getAuthentication();
		Validate.isTrue(userId > 0);
		// 封装查询条件		
		log.debug("SysAuthController.sysAuthListByUserId>>>");
		return new AjaxReturn(200,null,sysAuthInfoService.selectAuthByUserId(uId, userId));
	}


}
