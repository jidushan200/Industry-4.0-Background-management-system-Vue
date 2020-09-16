package org.tsinghuatj.web.controller.sys;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.sys.domain.SysParam;
import org.tsinghuatj.sys.service.ISysParamService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/sys" })
public class SysParamController extends BaseController {

	private @Autowired(required = false) ISysParamService sysParamService;

	/**
	 * 系统参数更新
	 */
	@ApiOperation(value = "系统参数更新", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "参数更新成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/sys-param-update")
	@OperateLog(info = "系统参数修改[参数名称:%s->参数值:%s]", params = { "paramName", "paramValue" })
	public AjaxReturn sysParamUpdate(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "paramKey", value = "参数键", required = false) @RequestParam(required = false) String paramKey, @ApiParam(name = "paramName", value = "参数名称", required = false) @RequestParam(required = false, defaultValue = "") String paramName, @ApiParam(name = "paramValue", value = "参数值", required = false) @RequestParam(required = false, defaultValue = "") String paramValue

	) throws BusinessException {
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		SysParam sysParam = new SysParam();
		sysParam.setParamKey(paramKey);
		sysParam.setParamName(paramName);
		sysParam.setParamValue(paramValue);

		return new AjaxReturn(200, null, sysParamService.updateActiveById(user.getId(), sysParam, pkId));
	}

	/**
	 * 系统参数分页
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "系统参数分页列表", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "系统参数分页列表获取成功", response = SysParam.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/sys-param-page-list")
	@Secure() // 用户分页查询
	public AjaxReturn sysParamPageList(@ApiParam(name = "page", value = "页码", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "每页行数", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "paramName", value = "参数名称", required = false) @RequestParam(required = false, defaultValue = "") String paramName //
	) throws BusinessException {
		log.debug("SysParamController.sysParamPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("paramName:" + paramName);
		}
		// 获取当前用户
		long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);
		// Validate.isTrue(curuserId > 0);

		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		SysParam sysParam = new SysParam();
		sysParam.setParamName(paramName);

		Pagination<SysParam> pagination = sysParamService.selectPageList(curuserId, sysParam, queryDto);
		log.debug("SysParamController.sysParamPageList>>>");
		return pagination;
	}

	/**
	 * 系统参数更新
	 */
	@ApiOperation(value = "根据参数键查询参数值", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据参数键查询参数值", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/get-sysparam-by-key")
	public AjaxReturn getSysParamByKey(@ApiParam(name = "paramKey", value = "参数键", required = true) @RequestParam(required = true) String paramKey) throws BusinessException {
		// 获取当前用户
		Long userId = getAuthentication();
		return new AjaxReturn(200, null, sysParamService.selectByParamKey(userId, paramKey));
	}

}
