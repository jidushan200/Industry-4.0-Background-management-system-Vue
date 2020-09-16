package org.tsinghuatj.web.controller.tool;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.tool.domain.ToolApplyAudit;
import org.tsinghuatj.tool.service.IToolApplyAuditService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolApplyAuditController extends BaseController {

	private @Autowired(required = false) IToolApplyAuditService toolApplyAuditService;

	/**
	 * 审核日志查询
	 */
	@ApiOperation(value = "审核日志查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "审核日志查询", response = ToolApplyAudit.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/apply-audit-get-list")
	public AjaxReturn applyAuditListGetList(
			@ApiParam(name = "applyId", value = "主键", required = true) @RequestParam(required = true) Long applyId)
			throws BusinessException {
		log.debug("ToolApplyAuditController.applyAuditList<<<");
		if (log.isDebugEnabled()) {
			log.debug("applyId:" + applyId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		Validate.isTrue(userId > 0 && applyId > 0);
		log.debug("ToolApplyAuditController.applyAuditList>>>");
		return new AjaxReturn(200, null, toolApplyAuditService.selectByApplyId(userId, applyId));
	}
}
