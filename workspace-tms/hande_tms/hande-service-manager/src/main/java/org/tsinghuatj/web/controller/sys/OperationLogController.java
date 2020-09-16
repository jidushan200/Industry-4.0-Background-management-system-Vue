package org.tsinghuatj.web.controller.sys;

import java.util.Date;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.sys.domain.OperationLog;
import org.tsinghuatj.sys.domain.SysRoleAuth;
import org.tsinghuatj.sys.service.IOperationLogService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/auth" })
public class OperationLogController extends BaseController {

	private @Autowired(required = false) IOperationLogService operationLogService;

	@ApiOperation(value = "操作日志列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "操作日志列表查询成功", response = SysRoleAuth.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/operation-log-pagelist")
	public AjaxReturn operationLogList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "beginDate", value = "开始日期",required = false) @RequestParam(required = false,defaultValue = "") Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期",required = false) @RequestParam(required = false,defaultValue = "") Date endDate,
			@ApiParam(name = "operateInfo", value = "操作内容",required = false) @RequestParam(required = false) String operateInfo,
			@ApiParam(name = "operateName", value = "操作人", required = false) @RequestParam(required = false ) String operateName
	) throws BusinessException {
		log.debug("OperationLogController.operationLogList<<<");

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 0);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);	
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);
		OperationLog operationLog = new OperationLog();
		operationLog.setOperateName(operateName);
		operationLog.setOperateInfo(operateInfo);
		log.debug("OperationLogController.operationLogList>>>");
		return new AjaxReturn(200, null, operationLogService.selectPageList(userId, operationLog, queryDto));
	}
}
