package org.tsinghuatj.web.controller.sys;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.CollectionUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.sys.domain.AuthDto;
import org.tsinghuatj.sys.domain.SysRoleAuth;
import org.tsinghuatj.sys.domain.SysRoleInfo;
import org.tsinghuatj.sys.service.ISysRoleAuthService;
import org.tsinghuatj.sys.service.ISysRoleInfoService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping({ "/auth" })
public class RoleAuthController extends BaseController{
	
	private @Autowired(required = false) ISysRoleAuthService sysRoleAuthService;
	
	private @Autowired(required = false) ISysRoleInfoService roleInfoService;
	
	/**
	 * 角色权限删除
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value="角色权限删除",code=200,produces = "application/json",notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "角色权限删除", response = SysRoleAuth.class) })
	@RequestMapping(method=RequestMethod.POST,value="/sys-role-auth-delete")
	@OperateLog(info = "角色权限删除[id:%s]", params = { "pkId"})
	//@Secure(has = {"010102" })
	public AjaxReturn sysRoleAuthDelete(@ApiParam(name = "pkId",value = "pkId",required = true)@RequestParam(required = true)Long pkId)throws BusinessException{
		log.debug("RoleAuthController.sysRoleAuthDelete<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		//获取当前用户
		Long userId = getAuthentication();
		//参数校验
		//Validate.isTrue(userId > 0);
		Validate.isTrue(pkId > 0);
		
		sysRoleAuthService.deleteById(userId, pkId);
		log.debug("RoleAuthController.sysRoleAuthDelete>>>");
		return new AjaxReturn(200,null,1);
	}
	
	/**
	 * 角色权限列表
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value="角色权限列表",code=200,produces = "application/json",notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "角色权限列表查询成功", response = SysRoleAuth.class) })
	@RequestMapping(method=RequestMethod.POST,value="/sys-role-auth-list")
	//@Secure(has = {"010102" })
	public AjaxReturn sysRoleAuthList()throws BusinessException{
		log.debug("RoleAuthController.sysRoleAuthList<<<");

		//获取当前用户
		Long userId = getAuthentication();
		//参数校验
		//Validate.isTrue(userId > 0);
		// 封装查询条件
		SysRoleAuth sysRoleAuth = new SysRoleAuth();
		
		log.debug("RoleAuthController.sysRoleAuthList>>>");
		return new AjaxReturn(200,null,sysRoleAuthService.select(userId, sysRoleAuth));
	}
	
	/**
	 * 角色权限ById
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value="角色权限ById",code=200,produces = "application/json",notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "角色权限列表Id查询成功", response = SysRoleAuth.class) })
	@RequestMapping(method=RequestMethod.POST,value="/role-auth-get-by-role-id")
	//@Secure(has = {"010102" })
	public AjaxReturn sysRoleAuthById(@ApiParam(name = "roleId",value = "roleId",required = true)@RequestParam(required = true)Long roleId)throws BusinessException{
		log.debug("RoleAuthController.sysRoleAuthById<<<");
		if (log.isDebugEnabled()) {
			log.debug("roleId:" + roleId);
		}
		//获取当前用户
		Long userId = getAuthentication();
		//参数校验
		//Validate.isTrue(userId > 0);
		Validate.isTrue(roleId > 0);
		SysRoleInfo role = roleInfoService.selectById(userId, roleId);
		log.debug("RoleAuthController.sysRoleAuthById>>>");
		return new AjaxReturn(200,null,role.getRoleAuth().split(","));
	}
	
	/**
	 * 角色权限保存
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value="角色权限保存",code=200,produces = "application/json",notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "角色权限保存", response = SysRoleAuth.class) })
	@OperateLog(info = "角色权限保存[角色名称:%s]", params = { "roleName"})
	@RequestMapping(method = RequestMethod.POST, value = "/role-auth-save")
	@Secure()
	public AjaxReturn roleAuthSave(@ApiIgnore HttpServletRequest httpServletRequest, @Validated @ModelAttribute AuthDto authDto) throws BusinessException {
		log.debug("RoleAuthController.roleAuthSave<<<");
		if (log.isDebugEnabled()) {
			log.debug("authDto:" + authDto);
		}
		// 获取当前用户
		Long currUserId = getAuthentication();
		// 参数校验
		//Validate.isTrue(currUserId > 0);
		Validate.isTrue(CollectionUtils.isNotEmpty(authDto.getAuthCodes()));
		sysRoleAuthService.batchSave(currUserId, authDto);

		log.debug("RoleAuthController.roleAuthSave>>>");
		return new AjaxReturn(200, null, null);
	}

}
