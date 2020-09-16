package org.tsinghuatj.web.controller.sys;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.StringUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.sys.domain.SysUserAuth;
import org.tsinghuatj.sys.service.ISysUserAuthService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/auth" })
public class UserAuthController extends BaseController {

	private @Autowired(required = false) ISysUserAuthService sysUserAuthService;

	/**
	 * 用户权限添加
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "用户权限添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "用户权限添加成功", response = SysUserAuth.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/sys-user-auth-add")
	@OperateLog(info = "用户权限添加[用户id:%s]", params = { "userId"})
	@Secure(has = { "010101" })
	public AjaxReturn sysUserAuthAdd(
			@ApiParam(name = "userId", value = "userId", required = true) @RequestParam(required = true) Long userId,
			@ApiParam(name = "authCode", value = "authCode", required = true) @RequestParam(required = true) String authCode)
			throws BusinessException {
		log.debug("SysAuthController.sysUserAuthAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("roleName:" + userId);
			log.debug("roleDesc:" + authCode);
		}

		// 获取当前用户
		Long curUserId = getAuthentication();
		// 参数校验
		Validate.isTrue(curUserId > 0);
		Validate.isTrue(userId > 0);
		Validate.isTrue(StringUtils.isNotBlank(authCode));
		// 封装查询条件
		SysUserAuth sysUserAuth = new SysUserAuth();
		sysUserAuth.setUserId(userId);
		sysUserAuth.setAuthCode(authCode);

		sysUserAuthService.insert(curUserId, sysUserAuth);
		log.debug("SysAuthController.sysUserAuthAdd>>>");
		return new AjaxReturn(200, null, null);
	}

	/**
	 * 用户权限删除
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "用户权限删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "用户权限删除成功", response = SysUserAuth.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/sys-user-auth-delete")
	@OperateLog(info = "用户权限删除[id:%s]", params = { "pkId"})
	@Secure(has = { "010101" })
	public AjaxReturn sysUserAuthDelete(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId)
			throws BusinessException {
		log.debug("SysAuthController.sysUserAuthDelete<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long curUserId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId > 0);
		Validate.isTrue(curUserId > 0);

		sysUserAuthService.deleteById(curUserId, pkId);
		log.debug("SysAuthController.sysUserAuthDelete>>>");
		return new AjaxReturn(200, null, 1);
	}

	// /**
	// * 用户权限查询byUserId
	// *
	// * @param request
	// * @return
	// * @throws BusinessException
	// */
	// @ApiOperation(value = "用户权限Id查询列表", code = 200, produces =
	// "application/json", notes = "")
	// @ApiResponses({ @ApiResponse(code = 200, message = "用户权限Id查询获取成功",
	// response = SysUserAuth.class) })
	// @RequestMapping(method = RequestMethod.POST, value =
	// "/user-auth-get-by-user-id")
	// @Secure()
	// public AjaxReturn sysUserAuthById(@ApiParam(name = "userId", value =
	// "userId", required = true) @RequestParam(required = true) Long userId)
	// throws BusinessException {
	// log.debug("SysAuthController.sysUserAuthList<<<");
	// if (log.isDebugEnabled()) {
	// log.debug("userId:" + userId);
	// }
	// // 获取当前用户
	// Long currUserId = getAuthentication();
	// // 参数校验
	// Validate.isTrue(userId > 0);
	// Validate.isTrue(currUserId > 0);
	//
	// log.debug("SysAuthController.sysAuthList>>>");
	// return new AjaxReturn(200, null,
	// sysUserAuthService.selectByUserId(currUserId, userId));
	// }
	//
	// @RequestMapping(method = RequestMethod.POST, value = "/user-auth-save")
	// @Secure(has = {"010101" })
	// public AjaxReturn userAuthSave(@ApiIgnore HttpServletRequest
	// httpServletRequest, @Validated @ModelAttribute AuthDto authDto) throws
	// BusinessException {
	// log.debug("SysAuthController.userAuthSave<<<");
	// if (log.isDebugEnabled()) {
	// log.debug("authDto:" + authDto);
	// }
	// // 获取当前用户
	// Long currUserId = getAuthentication();
	// // 参数校验
	// Validate.isTrue(currUserId > 0);
	// Validate.isTrue(CollectionUtils.isNotEmpty(authDto.getAuthCodes()));
	// sysUserAuthService.batchSave(currUserId, authDto);
	//
	// log.debug("SysAuthController.userAuthSave>>>");
	// return new AjaxReturn(200, null, null);
	// }

}
