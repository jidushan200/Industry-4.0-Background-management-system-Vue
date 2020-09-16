package org.tsinghuatj.web.controller.tool;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.ToolBlade;
import org.tsinghuatj.tool.domain.ToolBladeCompose;
import org.tsinghuatj.tool.domain.ToolBladeOutbound;
import org.tsinghuatj.tool.domain.ToolBladeWarehouse;
import org.tsinghuatj.tool.service.IToolBladeOutboundService;
import org.tsinghuatj.tool.service.IToolBladeService;
import org.tsinghuatj.tool.service.IToolBladeWarehouseService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolBladeController extends BaseController {
	private @Autowired(required = false) IToolBladeService toolBladeService;
	private @Autowired(required = false) IToolBladeWarehouseService toolBladeWarehouseService;
	private @Autowired(required = false) IToolBladeOutboundService toolBladeOutboundService;

	/**
	 * 查询刀具分页列表
	 */
	@ApiOperation(value = "查询刀条分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条分页列表查询成功", response = ToolBlade.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-page-list")
	public AjaxReturn bladePageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber) throws BusinessException {
		log.debug("ToolBladeController.toolBladePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("toolNumber:" + toolNumber);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(curuserId > 0);
		Validate.isTrue(page >= 0);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);

		ToolBlade toolBlade = new ToolBlade();
		toolBlade.setToolNumber(toolNumber);
		Pagination<ToolBlade> pagination = toolBladeService.selectPageList(curuserId, toolBlade, queryDto);
		log.debug("ToolBladeController.toolBladePageList>>>");
		return pagination;
	}

	
	/**
	 * 刀具入库
	 */
	@ApiOperation(value = "刀具入库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具入库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-warehouse")
	@OperateLog(info = "刀具入库[刀具编码:%s]", params = { "toolNumber" })
	public AjaxReturn bladeWarehouse(
			@ApiParam(name = "handleId", value = "待处理Id", required = true) @RequestParam(required = true) Long handleId, 
			@ApiParam(name = "toolNumber", value = "物料编码", required = true) @RequestParam(required = true) String toolNumber, 
			@ApiParam(name = "toolMap", value = "物料图号", required = false) @RequestParam(required = false) String toolMap, 
			@ApiParam(name = "toolName", value = "物料名称", required = false) @RequestParam(required = false) String toolName, 
			@ApiParam(name = "toolQty", value = "物料数量", required = false) @RequestParam(required = false) Integer toolQty, 
			@ApiParam(name = "warehouseType", value = "入库类型", required = false) @RequestParam(required = false) Integer warehouseType,
			@ApiParam(name = "warehouse", value = "库位", required = false) @RequestParam(required = false) String warehouse, 
			@ApiParam(name = "departmentId", value = "部门Id", required = false) @RequestParam(required = false) Long departmentId, 
			@ApiParam(name = "departmentName", value = "部门称", required = false) @RequestParam(required = false) String departmentName, 
			@ApiParam(name = "supplierId", value = "供应商Id", required = false) @RequestParam(required = false) Long supplierId, 
			@ApiParam(name = "supplierName", value = "供应商名称", required = false) @RequestParam(required = false) String supplierName, 
			@ApiParam(name = "deliever", value = "送货人", required = false) @RequestParam(required = false) String deliever,
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false) String remark

	) throws BusinessException {
		log.debug("ToolBladeController.bladeWarehouse<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
			log.debug("toolMap:" + toolMap);
			log.debug("toolName:" + toolName);
			log.debug("warehouse:" + warehouse);
			log.debug("supplierId:" + supplierId);
			log.debug("supplierName:" + supplierName);
			log.debug("deliever:" + deliever);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		ToolBladeWarehouse bladeWarehouse = new ToolBladeWarehouse();
		bladeWarehouse.setToolNumber(toolNumber);
		bladeWarehouse.setToolName(toolName);
		bladeWarehouse.setToolMap(toolMap);
		bladeWarehouse.setToolQty(toolQty);
		bladeWarehouse.setDepartmentId(departmentId);
		bladeWarehouse.setSupplierId(supplierId);
		bladeWarehouse.setSupplierName(supplierName);
		bladeWarehouse.setDeliever(deliever);
		bladeWarehouse.setWarehouse(warehouse);
		bladeWarehouse.setWarehouseType(warehouseType);
		log.debug("ToolBladeController.bladeWarehouse>>>");

		return new AjaxReturn(200, null, toolBladeWarehouseService.insert(user.getId(),user.getUsername(), bladeWarehouse, handleId));
	}

	/**
	 * 刀具出库记录信息添加
	 */
	@ApiOperation(value = "刀具出库记录信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具出库记录信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-outbound")
	@OperateLog(info = "刀具出库记录信息添加[刀盘刀条组合条码:%s]", params = { "composeNumber"})
	public AjaxReturn bladeOutbound(
			@ApiParam(name = "pkId", value = "刀条记录id", required = false) @RequestParam(required = false) Long pkId, 
			@ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, 
			@ApiParam(name = "composeNumber", value = "刀条组合条码", required = false) @RequestParam(required = false) String composeNumber, 
			@ApiParam(name = "plateNumber", value = "刀盘编码", required = false) @RequestParam(required = false) String plateNumber, 
			//@ApiParam(name = "partCode", value = "制件码", required = false) @RequestParam(required = false) String partCode, 
			@ApiParam(name = "headNumber", value = "刀条组合码", required = false) @RequestParam(required = false) String headNumber, 
			@ApiParam(name = "outType", value = "出库类型", required = true) @RequestParam(required = true) Integer outType,
			@ApiParam(name = "departmentId", value = "领用部门ID", required = false) @RequestParam(required = false) Long departmentId, 
			@ApiParam(name = "departmentName", value = "领用部门名称", required = false) @RequestParam(required = false) String departmentName, @ApiParam(name = "teamId", value = "领用班组", required = false) @RequestParam(required = false) Long teamId, @ApiParam(name = "teamName", value = "领用班组名称", required = false) @RequestParam(required = false) String teamName, @ApiParam(name = "supplierId", value = "供应商id", required = false) @RequestParam(required = false) Long supplierId, @ApiParam(name = "supplierName", value = "供应商名称", required = false) @RequestParam(required = false) String supplierName,
			@ApiParam(name = "receiver", value = "领用人", required = false) @RequestParam(required = false) String receiver, @ApiParam(name = "outQty", value = "需求数量", required = false) @RequestParam(required = false) Integer outQty, @ApiParam(name = "remark", value = "说明", required = false) @RequestParam(required = false) String remark) throws BusinessException {
		log.debug("ToolBladeController.toolOutboundAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
			log.debug("outType:" + outType);
			log.debug("departmentId:" + departmentId);
			log.debug("departmentName:" + departmentName);
			log.debug("receiver:" + receiver);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		ToolBladeOutbound toolOutbound = new ToolBladeOutbound();
		toolOutbound.setToolNumber(toolNumber);
		toolOutbound.setToolQty(outQty);
		toolOutbound.setComposeNumber(composeNumber);
		toolOutbound.setOutType(outType);
		toolOutbound.setDepartmentId(departmentId);
		toolOutbound.setDepartmentName(departmentName);
		toolOutbound.setTeamId(teamId);
		toolOutbound.setTeamName(teamName);

		toolOutbound.setReceiver(receiver);
		toolOutbound.setRemark(remark);

		// 构建刀盘刀条组合
		ToolBladeCompose bladeCompose = new ToolBladeCompose();
		bladeCompose.setComposeNumber(composeNumber);

		if (outType == 1) {			
			bladeCompose.setToolStatus(1);
			bladeCompose.setHeadNumber(headNumber);			
			bladeCompose.setPlateNumber(plateNumber);
			bladeCompose.setDepartmentId(departmentId);
			bladeCompose.setDepartmentName(departmentName);
			bladeCompose.setTeamId(teamId);
			bladeCompose.setTeamName(teamName);
		} else if (outType == 4) {
			// 补充刀条
			bladeCompose.setComposeNumber(composeNumber);
		}

		log.debug("ToolBladeController.toolOutboundAdd>>>");
		return new AjaxReturn(200, null, toolBladeOutboundService.insert(user.getId(), toolOutbound, bladeCompose, pkId));
	}
	
	
	/**
	 * 刀具出库记录信息添加
	 */
	@ApiOperation(value = "刀具出库记录信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具出库记录信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-complex-outbound")
	@OperateLog(info = "刀具出库记录信息添加[刀盘刀条组合条码:%s]", params = { "composeNumber" })
	public AjaxReturn bladeComplexOutbound(@ApiParam(name = "composeNumber", value = "刀条组合条码", required = false) @RequestParam(required = false) String composeNumber, 
			@ApiParam(name = "plateNumber", value = "刀盘编码", required = false) @RequestParam(required = false) String plateNumber, 
			@ApiParam(name = "headNumber", value = "刀条组合码", required = false) @RequestParam(required = false) String headNumber, 
			@ApiParam(name = "departmentId", value = "领用部门ID", required = false) @RequestParam(required = false) Long departmentId, 
			@ApiParam(name = "departmentName", value = "领用部门名称", required = false) @RequestParam(required = false) String departmentName,
			@ApiParam(name = "teamId", value = "领用班组", required = false) @RequestParam(required = false) Long teamId, 
			@ApiParam(name = "teamName", value = "领用班组名称", required = false) @RequestParam(required = false) String teamName, 
			@ApiParam(name = "receiver", value = "领用人", required = false) @RequestParam(required = false) String receiver, 
			@ApiParam(name = "remark", value = "说明", required = false) @RequestParam(required = false) String remark, 
			@ApiParam(name = "bladeJson", value = "刀条组合", required = false) @RequestParam(required = false) String bladeJson

	) throws BusinessException {
		log.debug("ToolBladeController.bladeComplexOutbound<<<");
		if (log.isDebugEnabled()) {
			log.debug("departmentId:" + departmentId);
			log.debug("departmentName:" + departmentName);
			log.debug("receiver:" + receiver);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		log.debug("ToolBladeController.bladeComplexOutbound>>>");
		return new AjaxReturn(200, null, toolBladeOutboundService.complexOutbound(user.getId(), headNumber, composeNumber, plateNumber, departmentId, departmentName, teamId, teamName, receiver, remark, bladeJson));
	}
	
	

	@ApiOperation(value = "查询刀条库存量", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀条库存量", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/get-blade-inventory-qty")
	public AjaxReturn getBladeInventoryQty(@ApiParam(name = "toolNumber", value = "物料编码", required = true) @RequestParam(required = true) String toolNumber

	) throws BusinessException {
		log.debug("ToolBladeController.getBladeInventoryQty<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		ToolBlade toolBlade = toolBladeService.selectByNumber(userId, toolNumber);
		log.debug("ToolBladeController.getBladeInventoryQty>>>");
		return new AjaxReturn(200, null, toolBlade.getInventoryQty());
	}
	
	
	@ApiOperation(value = "刀条台账导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条台账表导出", response = ToolBlade.class) })
	@Secure()
	@GetMapping(path = { "/tool-blade-export" })
	public ResponseEntity<byte[]> downloadBladeExcel() throws Exception {
		Long userId = getAuthentication();
		List<ToolBlade> excelVOList = toolBladeService.select(userId, new ToolBlade());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, ToolBlade.class, true, null, true);
		return getResponseEntity(data, "刀条台账表.xlsx");
	}
	
	
	
	@ApiOperation(value = "刀条台账导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条台账导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/blade-import")
	@Secure()
	@OperateLog(info = "刀条台账导入", params = {})
	public AjaxReturn bladeImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<ToolBlade> hblist = ExcelUtils.getInstance().readExcel2Objects(inputStream, ToolBlade.class, 1, 2000, 0);
		inputStream.close();
		toolBladeService.toolBladeImport(userId,hblist);
		return new AjaxReturn(200, null, 1);
	}
	

}
