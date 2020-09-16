package org.tsinghuatj.web.controller.tool;

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
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.ToolBladeCompose;
import org.tsinghuatj.tool.domain.ToolBladeScrap;
import org.tsinghuatj.tool.domain.ToolScripApply;
import org.tsinghuatj.tool.service.IToolBladeScrapService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolBladeScrapController extends BaseController {
	private @Autowired(required = false) IToolBladeScrapService bladeScrapService;

	@ApiOperation(value = "查询刀条报废申请列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀条报废申请列表", response = ToolBladeScrap.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-scrap-apply-page-list")
	public AjaxReturn bladeScrapApplyPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "composeNumber", value = "组合条码", required = false) @RequestParam(required = false) String composeNumber) throws BusinessException {
		log.debug("ToolBladeScrapController.bladeScrapApplyPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(userId > 0);
		Validate.isTrue(page >= 0);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);

		ToolBladeScrap where = new ToolBladeScrap();
		where.setComposeNumber(composeNumber);
		Pagination<ToolBladeScrap> pagination = bladeScrapService.selectPageList(userId, where, queryDto);
		log.debug("ToolBladeScrapController.bladeScrapApplyPageList>>>");
		return pagination;
	}

	@ApiOperation(value = "查询已报废刀条列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询已报废刀条列表", response = ToolBladeCompose.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-scrap-page-list")
	public AjaxReturn bladeScrapPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "composeNumber", value = "组合条码", required = false) @RequestParam(required = false) String composeNumber) throws BusinessException {
		log.debug("ToolBladeScrapController.bladeScrapPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);

		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(userId > 0);
		Validate.isTrue(page >= 0);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);

		ToolBladeCompose where = new ToolBladeCompose();
		where.setComposeNumber(composeNumber);
		Pagination<ToolBladeCompose> pagination = bladeScrapService.selectScrapPageList(userId, where, queryDto);
		log.debug("ToolBladeScrapController.bladeScrapPageList>>>");
		return pagination;
	}

	@ApiOperation(value = "刀盘信息查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀盘信息查询", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/scrap-get-by-compose-number")
	public AjaxReturn scrapGetByComposeNumber(@ApiParam(name = "composeNumber", value = "组合编码", required = true) @RequestParam(required = true) String composeNumber) throws BusinessException {
		log.debug("ToolBladeScrapController.scrapGetByComposeNumber<<<");
		if (log.isDebugEnabled()) {
			log.debug("composeNumber:" + composeNumber);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		log.debug("ToolBladeScrapController.scrapGetByComposeNumber>>>");
		return new AjaxReturn(200, null, bladeScrapService.selectByComposeNumber(userId, composeNumber));
	}

	@ApiOperation(value = "刀条报废表信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条报废表信息添加", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-scrap-apply-add")
	@OperateLog(info = "刀条报废表信息添加[刀盘组合码:%s]", params = { "composeNumber" })
	public AjaxReturn bladeScrapApplyAdd(
			@ApiParam(name = "composeNumber", value = "刀盘组合条码", required = false) @RequestParam(required = false) String composeNumber, 
			@ApiParam(name = "detailJson", value = "明细", required = false) @RequestParam(required = false) String detailJson, 
			@ApiParam(name = "departmentId", value = "申请部门Id", required = false) @RequestParam(required = false) Long departmentId, @
			ApiParam(name = "departmentName", value = "申请部门", required = false) @RequestParam(required = false) String departmentName, 
			@ApiParam(name = "scrapRemark", value = "报废原因说明", required = false) @RequestParam(required = false) String scrapRemark, 
			@ApiParam(name = "applyStatus", value = "申请单状态", required = false) @RequestParam(required = false) Integer applyStatus,
			@ApiParam(name = "scrapResion", value = "报废原因", required = false) @RequestParam(required = false) Integer scrapResion) throws BusinessException {
		log.debug("ToolBladeScrapController.bladeScrapApplyAdd<<<");
		if (log.isDebugEnabled()) {			
			log.debug("departmentId:" + departmentId);
			log.debug("departmentName:" + departmentName);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		ToolBladeScrap scrapApply = new ToolBladeScrap();
		scrapApply.setComposeNumber(composeNumber);	
		scrapApply.setApplierId(user.getId());
		scrapApply.setApplierName(user.getRealname());
		scrapApply.setDepartmentId(departmentId);
		scrapApply.setDepartmentName(departmentName);
		scrapApply.setApplyStatus(applyStatus);
		
		scrapApply.setScrapResion(scrapResion);
		scrapApply.setScrapRemark(scrapRemark);
		log.debug("ToolBladeScrapController.bladeScrapApplyAdd>>>");
		return new AjaxReturn(200, null, bladeScrapService.insert(user.getId(), scrapApply,detailJson));
	}

	@ApiOperation(value = "刀条报废申请修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条报废申请修改", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-scrap-apply-update")
	@OperateLog(info = "刀条报废申请修改[刀盘组合码:%s]", params = { "pkId" })
	public AjaxReturn bladeScrapApplyUpdate(
			@ApiParam(name = "pkId", value = "刀盘组合id", required = false) @RequestParam(required = false) Long pkId,
			@ApiParam(name = "composeNumber", value = "刀盘组合条码", required = false) @RequestParam(required = false) String composeNumber, 
			@ApiParam(name = "detailJson", value = "明细", required = false) @RequestParam(required = false) String detailJson, 
			@ApiParam(name = "scrapRemark", value = "报废原因说明", required = false) @RequestParam(required = false) String scrapRemark,
			@ApiParam(name = "applyStatus", value = "申请单状态", required = false) @RequestParam(required = false) Integer applyStatus, 
			@ApiParam(name = "scrapResion", value = "报废原因", required = false) @RequestParam(required = false) Integer scrapResion) throws BusinessException {
		log.debug("ToolBladeScrapController.bladeScrapApplyUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("detailJson:" + detailJson);			
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		ToolBladeScrap scrapApply = new ToolBladeScrap();
		scrapApply.setComposeNumber(composeNumber);		
		scrapApply.setApplyStatus(applyStatus);
		
		scrapApply.setScrapResion(scrapResion);
		scrapApply.setScrapRemark(scrapRemark);
		log.debug("ToolBladeScrapController.bladeScrapApplyUpdate>>>");
		return new AjaxReturn(200, null, bladeScrapService.updateActiveById(user.getId(), scrapApply, pkId,detailJson));
	}

	@ApiOperation(value = "刀条报废表信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条报废表信息删除", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-scrap-apply-delete-by-id")
	@OperateLog(info = "刀条报废申请删除[id:%s]", params = { "pkId" })
	public AjaxReturn bladeScripApplyDeleteById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("ToolBladeScrapController.bladeScripApplyDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("ToolBladeScrapController.bladeScripApplyDeleteById>>>");
		return new AjaxReturn(200, null, bladeScrapService.deleteById(userId, pkId));
	}

	@ApiOperation(value = "根据ID查询刀条报废申请", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据ID查询刀条报废申请", response = ToolScripApply.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-scrap-apply-get-by-id")
	public AjaxReturn bladeScrapApplyGetById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("ToolBladeScrapController.bladeScrapApplyGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId >= 1);

		log.debug("ToolBladeScrapController.bladeScrapApplyGetById>>>");
		return new AjaxReturn(200, null, bladeScrapService.selectById(curuserId, pkId));
	}

	@ApiOperation(value = "刀条报废审核", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条报废审核", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-scrap-apply-audit")
	@OperateLog(info = "刀条报废审核[刀盘组合码:%s]", params = { "pkId" })
	public AjaxReturn bladeScrapApplyAudit(@ApiParam(name = "pkId", value = "刀盘组合id", required = false) @RequestParam(required = false) Long pkId, @ApiParam(name = "composeNumber", value = "刀盘组合码", required = false) @RequestParam(required = false) String composeNumber, @ApiParam(name = "applyStatus", value = "申请单状态", required = false) @RequestParam(required = false) Integer applyStatus, @ApiParam(name = "auditRemark", value = "审核意见说明", required = false) @RequestParam(required = false) String auditRemark, @ApiParam(name = "departmentId", value = "审核部门id", required = false) @RequestParam(required = false) Long departmentId,
			@ApiParam(name = "departmentName", value = "审核部门名称", required = false) @RequestParam(required = false) String departmentName) throws BusinessException {
		log.debug("ToolBladeScrapController.bladeScrapApplyAudit<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
			log.debug("applyStatus:" + applyStatus);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		ToolBladeScrap scrapApply = new ToolBladeScrap();
		scrapApply.setPkId(pkId);
		scrapApply.setComposeNumber(composeNumber);
		scrapApply.setApplyStatus(applyStatus);

		log.debug("ToolBladeScrapController.bladeScrapApplyAudit>>>");
		return new AjaxReturn(200, null, bladeScrapService.audit(user.getId(), user.getRealname(), departmentId, departmentName, auditRemark, scrapApply));
	}

	@ApiOperation(value = "刀条报废", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条报废", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-scrap")
	@OperateLog(info = "刀条报废[刀盘组合码:%s]", params = { "composeNumber" })
	public AjaxReturn bladeScrap(@ApiParam(name = "composeNumber", value = "刀盘组合码", required = false) @RequestParam(required = false) String composeNumber

	) throws BusinessException {
		log.debug("ToolBladeScrapController.bladeScrap<<<");
		if (log.isDebugEnabled()) {

			log.debug("composeNumber:" + composeNumber);
		}
		// 获取当前用户
		Long curuserId = getAuthentication();

		log.debug("ToolBladeScrapController.bladeScrap>>>");
		return new AjaxReturn(200, null, bladeScrapService.bladeScrap(curuserId, composeNumber));
	}

}
