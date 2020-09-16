package org.tsinghuatj.web.controller.tool;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.odftoolkit.odfdom.converter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.JsonUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.ToolBladeComposePart;
import org.tsinghuatj.tool.domain.ToolBladeProcess;
import org.tsinghuatj.tool.domain.ToolWaitHandle;
import org.tsinghuatj.tool.service.IToolWaitHandleService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolWaitHandleController extends BaseController {

	private @Autowired(required = false) IToolWaitHandleService toolWaitHandleService;

	@ApiOperation(value = "查询待磨刀条信息列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询待磨刀条信息列表", response = ToolWaitHandle.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-wait-repair-page-list")
	public AjaxReturn bladeRepairPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, @ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "toolNumber", value = "刀具码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "composeNumber", value = "刀条组合条码", required = false) @RequestParam(required = false) String composeNumber, @ApiParam(name = "handleResult", value = "处理结果", required = false) @RequestParam(required = false) Integer handleResult) throws BusinessException {

		log.debug("ToolWaitHandleController.bladeRepairPageList<<<");
		if (log.isDebugEnabled()) {

		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		ToolWaitHandle waitHandle = new ToolWaitHandle();
		waitHandle.setToolNumber(toolNumber);
		waitHandle.setComposeNumber(composeNumber);
		waitHandle.setHandleType(2);
		waitHandle.setHandleResult(handleResult);
		Pagination<ToolWaitHandle> pagination = toolWaitHandleService.selectPageList(userId, waitHandle, queryDto);
		log.debug("ToolWaitHandleController.bladeRepairPageList>>>");
		return pagination;
	}

	@ApiOperation(value = "刀条送检", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条送检", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-set-check")
	@OperateLog(info = "刀条送检[刀具编码:%s]", params = { "pkId" })
	public AjaxReturn bladeSetCheck(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {

		// 获取当前用户
		Long userId = getAuthentication();
		return new AjaxReturn(200, null, toolWaitHandleService.setCheck(userId, pkId));
	}

	@ApiOperation(value = "刀条刃磨入库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条刃磨入库", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-stock")
	public AjaxReturn bladeSetStock(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {

		// 获取当前用户
		Long userId = getAuthentication();
		return new AjaxReturn(200, null, toolWaitHandleService.setStock(userId, pkId));
	}

	@ApiOperation(value = "查询待涂层刀条信息列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询待涂层刀条信息列表", response = ToolWaitHandle.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-wait-coat-page-list")
	public AjaxReturn bladeCoatPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, @ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "composeNumber", value = "刀条组合条码", required = false) @RequestParam(required = false) String composeNumber, @ApiParam(name = "toolNumber", value = "刀具码", required = false) @RequestParam(required = false) String toolNumber) throws BusinessException {

		log.debug("ToolWaitHandleController.bladeCoatPageList<<<");
		if (log.isDebugEnabled()) {

		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);

		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		ToolWaitHandle waitHandle = new ToolWaitHandle();
		waitHandle.setToolNumber(toolNumber);
		waitHandle.setComposeNumber(composeNumber);
		waitHandle.setHandleType(3);
		Pagination<ToolWaitHandle> pagination = toolWaitHandleService.selectCoatPageList(userId, waitHandle, queryDto);
		log.debug("ToolWaitHandleController.bladeCoatPageList>>>");
		return pagination;
	}

	@ApiOperation(value = "刀条涂层出库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条涂层出库", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-set-coat")
	@OperateLog(info = "刀条涂层出库[刀具编码:%s->刀条数量:%s]", params = { "toolNumber", "toolQty" })
	public AjaxReturn bladeSetCoat(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId, 
			@ApiParam(name = "equipmentCode", value = "设备编码", required = false) @RequestParam(required = false) String equipmentCode, 
			@ApiParam(name = "departmentId", value = "使用部门ID", required = true) @RequestParam(required = false) Long departmentId, 
			@ApiParam(name = "departmentName", value = "使用部门名称", required = false) @RequestParam(required = false) String departmentName, 
			@ApiParam(name = "staffCode", value = "员工工号", required = false) @RequestParam(required = false) String staffCode,
			@ApiParam(name = "supplierId", value = "供应商", required = true) @RequestParam(required = true) Long supplierId, 
			@ApiParam(name = "supplierName", value = "供应商名称", required = false) @RequestParam(required = false) String supplierName,
			@ApiParam(name = "deliever", value = "送货人", required = false) @RequestParam(required = false) String deliever, 
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false) String remark,
			@ApiParam(name = "detailList", value = "明细", required = false) @RequestParam(required = false) String detailList,
			@ApiParam(name = "partList", value = "制件", required = false) @RequestParam(required = false) String partList

	) throws BusinessException {

		// 获取当前用户
		Long userId = getAuthentication();
		List<ToolBladeComposePart> parts = JsonUtils.json2list(partList, ToolBladeComposePart.class);
		ToolBladeProcess process = new ToolBladeProcess();
		if (StringUtils.isNotEmpty(staffCode)) {
			process.setEquipmentCode(equipmentCode);
			process.setDepartmentId(departmentId);
			process.setDepartmentName(departmentName);
			process.setStaffCode(staffCode);
		}

		return new AjaxReturn(200, null, toolWaitHandleService.setBladeCoat(userId, pkId, supplierId, supplierName, deliever, process, parts, remark, detailList));
	}

	@ApiOperation(value = "待入库新刀", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "待入库新刀", response = ToolWaitHandle.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/wait-warehouse-page-list")
	public AjaxReturn waitWarehousePageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "typeId", value = "刀具类型", required = true) @RequestParam(required = true) Integer typeId, //
			@ApiParam(name = "toolNumber", value = "刀具编码", required = true) @RequestParam(required = true) String toolNumber //
	) throws BusinessException {
		log.debug("ToolBladeController.waitWarehousePageList<<<");
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
		ToolWaitHandle waitHandle = new ToolWaitHandle();
		waitHandle.setToolNumber(toolNumber);
		waitHandle.setHandleType(1);
		waitHandle.setHandleResult(0);
		waitHandle.setTypeId(typeId);
		Pagination<ToolWaitHandle> pagination = toolWaitHandleService.selectNewToolPageList(userId, waitHandle, queryDto);
		log.debug("ToolWaitHandleController.bladeRepairPageList>>>");
		return pagination;
	}

}
