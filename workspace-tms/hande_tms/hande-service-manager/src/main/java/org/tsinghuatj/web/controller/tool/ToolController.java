package org.tsinghuatj.web.controller.tool;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tsinghuatj.framework.constants.ToolStatusTypeEnum;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.StringUtils;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolCoat;
import org.tsinghuatj.tool.domain.ToolOperLog;
import org.tsinghuatj.tool.domain.ToolProcess;
import org.tsinghuatj.tool.domain.ToolRepair;
import org.tsinghuatj.tool.domain.ToolWarehouse;
import org.tsinghuatj.tool.service.IToolOperLogService;
import org.tsinghuatj.tool.service.IToolService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolController extends BaseController {

	private @Autowired(required = false) IToolService toolService;

	private @Autowired(required = false) IToolOperLogService operLogService;

	/**
	 * 序号校验
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "报废校验", code = 200, produces = "application/json", notes = "登录名校验")
	@ApiResponses({ @ApiResponse(code = 200, message = "报废校验 (false-存在 true-不存在)", response = String.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-fullNumber-validate")
	@Secure()
	public String toolFullNumberValidate(@ApiParam(name = "fullNumber", value = "物料条码", required = true) @RequestParam(required = true) String fullNumber) throws BusinessException {
		log.debug("ToolController.fullNumberValidate<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
		}

		CustomUser user = getCompleteAuthentication();
		// 参数校验
		// 根据登录名查询
		Tool tool = toolService.selectByFullNumber(user.getId(), fullNumber);
		log.debug("ToolController.fullNumberValidate>>>");
		return null == tool ? "true" : "false";
	}

	/**
	 * 刀具入库
	 */
	@ApiOperation(value = "刀具入库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具入库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-warehouse")
	@OperateLog(info = "刀具入库[刀具条码:%s]", params = { "fullNumber" })
	@Secure()
	public AjaxReturn toolAdd(@ApiParam(name = "toolNumber", value = "物料编码", required = true) @RequestParam(required = true) String toolNumber, @ApiParam(name = "warehouseCode", value = "入库编码", required = false) @RequestParam(required = false) String warehouseCode, @ApiParam(name = "toolMap", value = "物料图号", required = false) @RequestParam(required = false) String toolMap, @ApiParam(name = "toolName", value = "物料名称", required = false) @RequestParam(required = false) String toolName, @ApiParam(name = "fullNumber", value = "物料条码", required = false) @RequestParam(required = true) String fullNumber, @ApiParam(name = "typeId", value = "类型Id", required = false) @RequestParam(required = false) Integer typeId,
			@ApiParam(name = "supplierId", value = "供应商Id", required = false) @RequestParam(required = false) Long supplierId, @ApiParam(name = "supplierName", value = "供应商名称", required = false) @RequestParam(required = false) String supplierName, @ApiParam(name = "toolType", value = "刀具类型", required = false) @RequestParam(required = false) Integer toolType, @ApiParam(name = "departmentId", value = "部门Id", required = false) @RequestParam(required = false) Long departmentId, @ApiParam(name = "departmentName", value = "部门名称", required = false) @RequestParam(required = false) String departmentName, @ApiParam(name = "warehouse", value = "库位", required = false) @RequestParam(required = false) String warehouse,
			@ApiParam(name = "deliever", value = "送货人", required = false) @RequestParam(required = false) String deliever, @ApiParam(name = "repairCordon", value = "预警刃磨量", required = true) @RequestParam(required = true) BigDecimal repairCordon, @ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false) String remark, @ApiParam(name = "handleId", value = "待处理Id", required = false) @RequestParam(required = false) Long handleId) throws BusinessException {
		log.debug("ToolController.toolAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
			log.debug("toolMap:" + toolMap);
			log.debug("toolName:" + toolName);
			log.debug("fullNumber:" + fullNumber);
			log.debug("warehouseCode:" + warehouseCode);
			log.debug("warehouse:" + warehouse);
			log.debug("supplierId:" + supplierId);
			log.debug("supplierName:" + supplierName);
			log.debug("deliever:" + deliever);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		Tool tool = new Tool();
		tool.setToolNumber(toolNumber);
		tool.setFullNumber(fullNumber);
		tool.setToolMap(toolMap);
		tool.setToolName(toolName);
		tool.setTypeId(typeId);
		tool.setSupplierId(supplierId);
		tool.setSupplierName(supplierName);
		tool.setWarehouseCode(warehouseCode);
		tool.setWarehouse(warehouse);
		tool.setKeeper(user.getRealname());
		tool.setDepartmentId(departmentId);
		tool.setDepartmentName(departmentName);
		// tool.setRepairCordon(repairCordon);
		tool.setIsScrip(2);
		ToolWarehouse toolWarehouse = new ToolWarehouse();
		toolWarehouse.setToolId(tool.getPkId());
		toolWarehouse.setToolNumber(toolNumber);
		toolWarehouse.setToolName(toolName);
		toolWarehouse.setWarehouseCode(warehouseCode);
		toolWarehouse.setToolMap(toolMap);
		toolWarehouse.setFullNumber(fullNumber);
		toolWarehouse.setKeeperId(user.getId());
		toolWarehouse.setKeeper(user.getRealname());
		toolWarehouse.setWarehouse(warehouse);
		toolWarehouse.setDeliever(deliever);
		toolWarehouse.setRemark(remark);

		log.debug("ToolController.toolAdd>>>");

		return new AjaxReturn(200, null, toolService.insert(user.getId(), tool, toolWarehouse, handleId));
	}

	/**
	 * 刀具删除
	 */
	@ApiOperation(value = "刀具删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-delete-by-id")
	@OperateLog(info = "刀具报废[刀具条码%s]", params = { "fullNumber" })
	@Secure()
	public AjaxReturn toolDeleteById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("ToolController.toolDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("ToolController.toolDeleteById>>>");
		return new AjaxReturn(200, null, toolService.toolScripById(user.getId(), user.getRealname(), pkId));
	}

	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "刀具id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具Id查询列表查询成功", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-get-by-id")
	@Secure()
	public AjaxReturn toolGetById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("ToolController.toolGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId >= 1);

		log.debug("ToolController.toolGetById>>>");
		return new AjaxReturn(200, null, toolService.selectById(curuserId, pkId));
	}

	/**
	 * 根据编码查找
	 */
	@ApiOperation(value = "根据编码查找", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据编码查找", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-get-by-tool-number")
	@Secure()
	public AjaxReturn toolGetByToolNumber(@ApiParam(name = "toolNumber", value = "toolNumber", required = true) @RequestParam(required = true) String toolNumber) throws BusinessException {
		log.debug("ToolController.toolGetByToolNumber<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(StringUtils.isNotEmpty(toolNumber));

		log.debug("ToolController.toolGetByToolNumber>>>");
		return new AjaxReturn(200, null, toolService.toolGetByToolNumber(curuserId, toolNumber));
	}

	/**
	 * 采购根据编号查找
	 */
	@ApiOperation(value = "根据编码查找", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据编码查找", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/purchase-get-by-tool-number")
	@Secure()
	public AjaxReturn purchaseGetByToolNumber(@ApiParam(name = "toolNumber", value = "toolNumber", required = true) @RequestParam(required = true) String toolNumber) throws BusinessException {
		log.debug("ToolController.toolGetByToolNumber<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(StringUtils.isNotEmpty(toolNumber));

		log.debug("ToolController.toolGetByToolNumber>>>");
		return new AjaxReturn(200, null, toolService.purchaseGetByToolNumber(curuserId, toolNumber));
	}

	/**
	 * 采购根据编号查找
	 */
	@ApiOperation(value = "根据编码查找", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据编码查找", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/purchase-get-by-tool-name")
	@Secure()
	public AjaxReturn purchaseGetByToolName(@ApiParam(name = "toolName", value = "toolName", required = true) @RequestParam(required = true) String toolName) throws BusinessException {
		log.debug("ToolController.purchaseGetByToolName<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolName:" + toolName);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(StringUtils.isNotEmpty(toolName));

		log.debug("ToolController.purchaseGetByToolName>>>");
		return new AjaxReturn(200, null, toolService.purchaseGetByToolName(curuserId, toolName));
	}

	/**
	 * 根据完整编码查找
	 */
	@ApiOperation(value = "根据编码查找", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据编码查找", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-get-by-full-number")
	@Secure()
	public AjaxReturn toolGetByFullNumber(@ApiParam(name = "fullNumber", value = "fullNumber", required = true) @RequestParam(required = true) String fullNumber) throws BusinessException {
		log.debug("ToolController.toolGetByFullNumber<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(StringUtils.isNotEmpty(fullNumber));

		log.debug("ToolController.toolGetByFullNumber>>>");
		return new AjaxReturn(200, null, toolService.selectByFullNumber(curuserId, fullNumber));
	}

	/**
	 * 查询刀具分页列表
	 */
	@ApiOperation(value = "查询刀具分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具分页列表查询成功", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-page-list")
	@Secure()
	public AjaxReturn toolPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "fullNumber", value = "刀具码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "number", value = "编码编码相似查询", required = false) @RequestParam(required = false) String number, @ApiParam(name = "toolState", value = "刀具状态", required = false) @RequestParam(required = false) Integer toolState, @ApiParam(name = "isHead", value = "类型id", required = false) @RequestParam(required = false) Integer isHead,
			@ApiParam(name = "typeId", value = "类型id", required = false) @RequestParam(required = false) Integer typeId, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate) throws BusinessException {
		log.debug("ToolController.toolPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("fullNumber:" + fullNumber);
			log.debug("toolNumber:" + toolNumber);
			log.debug("toolState:" + toolState);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
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
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		Tool tool = new Tool();
		tool.setFullNumber(fullNumber);
		tool.setToolNumber(toolNumber);
		tool.setIsHead(isHead);
		tool.setToolState(toolState);
		tool.setTypeId(typeId);
		tool.setNumber(number);
		Pagination<Tool> pagination = toolService.selectPageList(curuserId, tool, queryDto);
		log.debug("ToolController.toolPageList>>>");
		return pagination;
	}

	/**
	 * 查询刀具分页列表
	 */
	@ApiOperation(value = "查询刀具分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具分页列表查询成功", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-wait-repair-page-list")
	@Secure()
	public AjaxReturn toolWaitRepairPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "fullNumber", value = "刀具码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "toolState", value = "刀具状态", required = false) @RequestParam(required = false) Integer toolState, @ApiParam(name = "typeId", value = "类型id", required = false) @RequestParam(required = false) Integer typeId, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate, @ApiParam(name = "warehouseCode", value = "入库编号", required = false) @RequestParam(required = false) String warehouseCode) throws BusinessException {
		log.debug("ToolController.toolPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("fullNumber:" + fullNumber);
			log.debug("toolNumber:" + toolNumber);
			log.debug("warehouseCode:" + warehouseCode);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
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
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		Tool tool = new Tool();
		tool.setFullNumber(fullNumber);
		tool.setToolNumber(toolNumber);
		tool.setWarehouseCode(warehouseCode);
		tool.setTypeId(typeId);
		tool.setToolState(toolState);
		Pagination<Tool> pagination = toolService.selectWaitRepairPageList(curuserId, tool, queryDto);
		log.debug("ToolController.toolPageList>>>");
		return pagination;
	}

	/**
	 * 查询刀具全生命周期分页列表
	 */
	@ApiOperation(value = "查询刀具全生命周期分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具全生命周期分页列表查询成功", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-life-page-list")
	public AjaxReturn toolLifePageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "fullNumber", value = "刀具码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "toolMap", value = "刀具图号", required = false) @RequestParam(required = false) String toolMap)
			throws BusinessException {
		log.debug("ToolController.toolLifePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("fullNumber:" + fullNumber);
			log.debug("toolNumber:" + toolNumber);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
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
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		Tool tool = new Tool();
		tool.setFullNumber(fullNumber);
		tool.setToolNumber(toolNumber);
		tool.setToolMap(toolMap);
		tool.setIsLife(1);

		Pagination<Tool> pagination = toolService.selectLifePageList(curuserId, tool, queryDto);
		log.debug("ToolController.toolLifePageList>>>");
		return pagination;
	}

	/**
	 * 刀具返库
	 */
	@ApiOperation(value = "刀具返库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具返库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-return")
	public AjaxReturn toolReturn(@ApiParam(name = "pkId", value = "刀具编码", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "toolName", value = "刀具名称", required = true) @RequestParam(required = true) String toolName, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false, defaultValue = "") String toolNumber, @ApiParam(name = "inType", value = "入库类型", required = true) @RequestParam(required = true) Integer inType, @ApiParam(name = "keeper", value = "库管员", required = false) @RequestParam(required = false, defaultValue = "") String keeper,
			@ApiParam(name = "warehouse", value = "库位", required = false) @RequestParam(required = false, defaultValue = "") String warehouse, @ApiParam(name = "deliever", value = "送货人", required = false) @RequestParam(required = false, defaultValue = "") String deliever, @ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false, defaultValue = "") String remark) throws BusinessException {
		log.debug("ToolController.toolReturn<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
			log.debug("toolNumber:" + toolNumber);
			log.debug("inType:" + inType);
			log.debug("keeper:" + keeper);
			log.debug("warehouse:" + warehouse);
			log.debug("deliever:" + deliever);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		Tool tool = new Tool();
		tool.setKeeper(user.getRealname());

		ToolWarehouse toolWarehouse = new ToolWarehouse();
		toolWarehouse.setInType(inType);
		toolWarehouse.setToolName(toolName);
		toolWarehouse.setToolId(tool.getPkId());
		toolWarehouse.setToolNumber(toolNumber);
		toolWarehouse.setKeeper(user.getRealname());
		toolWarehouse.setWarehouse(warehouse);
		toolWarehouse.setDeliever(deliever);
		toolWarehouse.setRemark(remark);

		log.debug("ToolController.toolReturn>>>");

		return new AjaxReturn(200, null, toolService.updateActiveById(user.getId(), tool, pkId, toolWarehouse));
	}

	/**
	 * 刀具状态修改
	 */
	@ApiOperation(value = "刀具状态修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具状态修改成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-state-update")
	public AjaxReturn toolUpdateState(@ApiParam(name = "pkId", value = "刀具编码", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "toolState", value = "刀具状态", required = true) @RequestParam(required = true) Integer toolState, @ApiParam(name = "checkType", value = "刀具状态", required = true) @RequestParam(required = true) Integer checkType) throws BusinessException {
		log.debug("ToolController.toolUpdateState<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
			log.debug("toolState:" + toolState);
		}
		// 获取当前用户
		Long userId = getAuthentication();

		// 封装参数信息
		Tool tool = new Tool();
		tool.setToolState(toolState);
		tool.setCheckType(checkType);
		tool.setToolNumber(toolNumber);
		log.debug("ToolController.toolUpdateState>>>");

		return new AjaxReturn(200, null, toolService.updateActiveById(userId, tool, pkId, null));
	}

	/**
	 * 已经报废的刀具列表
	 */
	@ApiOperation(value = "已经报废的刀具列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "已经报废的刀具列表查询成功", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-scrip-get-by-delMark")
	public AjaxReturn toolScripGetByDelMark(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "toolMap", value = "刀具编码", required = false) @RequestParam(required = false) String toolMap, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate) throws BusinessException {

		log.debug("ToolController.toolScripGetByDelMark<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("toolNumber:" + toolNumber);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
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
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		Tool tool = new Tool();
		tool.setToolNumber(toolNumber);
		tool.setToolMap(toolMap);

		Pagination<Tool> pagination = toolService.selectBydelMark(curuserId, tool, queryDto);
		log.debug("ToolController.toolScripGetByDelMark>>>");
		return pagination;
	}

	/**
	 * 根据完整编码查找
	 */
	@ApiOperation(value = "根据编码查找", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据编码查找", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/get-seq-by-tool-number")
	public AjaxReturn toolGetSeqByToolNumber(@ApiParam(name = "toolNumber", value = "toolNumber", required = true) @RequestParam(required = true) String toolNumber) throws BusinessException {
		log.debug("ToolController.toolGetSeqByToolNumber<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(StringUtils.isNotEmpty(toolNumber));

		log.debug("ToolController.toolGetByFullNumber>>>");
		return new AjaxReturn(200, null, toolService.toolGetSeqByToolNumber(curuserId, toolNumber));
	}

	/**
	 * 刀具台账信息导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "刀具台账信息导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具台账信息导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-import")
	// @Secure()
	public AjaxReturn toolImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		CustomUser user = getCompleteAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<Tool> toolList = ExcelUtils.getInstance().readExcel2Objects(inputStream, Tool.class, 1, 1000, 0);
		inputStream.close();
		toolService.toolImport(user.getId(), toolList);
		return new AjaxReturn(200, null, 1);
	}

	/**
	 * 刀具台账导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "刀具台账导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具台账导出", response = Tool.class) })
	@Secure()
	@GetMapping(path = { "/tool-list-export" })
	public String downloadToolListExcel(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "isHead", value = "刀具类型(1-刀头;2-其他)", required = true) @RequestParam(required = true) Integer isHead, @ApiParam(name = "typeId", value = "刀具类型", required = false) @RequestParam(required = false) Integer typeId, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "number", value = "编码编码相似查询", required = false) @RequestParam(required = false) String number

	) throws Exception {
		Long userId = getAuthentication();
		Tool where = new Tool();
		where.setIsHead(isHead);
		where.setTypeId(typeId);
		where.setToolNumber(toolNumber);
		where.setNumber(number);
		List<Tool> excelVOList = toolService.select(userId, where);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("刀具生命周期");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容

		row1.createCell(0).setCellValue("物料编码");
		row1.createCell(1).setCellValue("物料名称");
		row1.createCell(2).setCellValue("物料图号");
		row1.createCell(3).setCellValue("物料条码");
		row1.createCell(4).setCellValue("物料类型");
		row1.createCell(5).setCellValue("库位");
		row1.createCell(6).setCellValue("库管员");
		row1.createCell(7).setCellValue("供应商");
		row1.createCell(8).setCellValue("物料状态");
		row1.createCell(9).setCellValue("已加工次数");
		row1.createCell(10).setCellValue("单次加工标准");
		row1.createCell(11).setCellValue("本次加工数量");
		row1.createCell(12).setCellValue("已加工数量");
		row1.createCell(13).setCellValue("理论加工数量");
		row1.createCell(14).setCellValue("已涂层次数");
		row1.createCell(15).setCellValue("已刃磨次数");
		row1.createCell(16).setCellValue("最大刃磨量");
		row1.createCell(17).setCellValue("累计刃磨量");
		row1.createCell(18).setCellValue("操作人");
		row1.createCell(19).setCellValue("操作时间");
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i <= 19; i++) {
			row1.getCell(i).setCellStyle(cellStyle);
		}

		row1.setHeight((short) 400);// 目的是想把行高设置成25px
		sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 7000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 4000);
		sheet.setColumnWidth(10, 4000);
		sheet.setColumnWidth(11, 4000);
		sheet.setColumnWidth(12, 4000);
		sheet.setColumnWidth(13, 4000);
		sheet.setColumnWidth(14, 4000);
		sheet.setColumnWidth(15, 4000);
		sheet.setColumnWidth(16, 4000);
		sheet.setColumnWidth(17, 4000);
		sheet.setColumnWidth(18, 3000);
		sheet.setColumnWidth(19, 4000);
		int i = 1;

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);

		for (Tool item : excelVOList) {
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			dataRow.createCell(0).setCellValue(item.getToolNumber());
			dataRow.createCell(1).setCellValue(item.getToolName());
			dataRow.createCell(2).setCellValue(item.getToolMap());
			dataRow.createCell(3).setCellValue(item.getFullNumber());
			dataRow.createCell(4).setCellValue(item.getTypeName());
			dataRow.createCell(5).setCellValue(item.getWarehouse());
			dataRow.createCell(6).setCellValue(item.getKeeper());
			dataRow.createCell(7).setCellValue(item.getSupplierName());
			dataRow.createCell(8).setCellValue(ToolStatusTypeEnum.getName(item.getToolState()));
			if (null != item.getProcessTimes()) {
				dataRow.createCell(9).setCellValue(item.getProcessTimes());
			}
			if (null != item.getProcessEach()) {
				dataRow.createCell(10).setCellValue(item.getProcessEach());
			}
			if (null != item.getProcessCur()) {
				dataRow.createCell(11).setCellValue(item.getProcessCur());
			}
			if (null != item.getProcessAmount()) {
				dataRow.createCell(12).setCellValue(item.getProcessAmount());
			}
			if (null != item.getProcessTotal()) {
				dataRow.createCell(13).setCellValue(item.getProcessTotal());
			}
			if (null != item.getCoatTimes()) {
				dataRow.createCell(14).setCellValue(item.getCoatTimes());
			}
			if (null != item.getRepairTimes()) {
				dataRow.createCell(15).setCellValue(item.getRepairTimes());
			}
			if (null != item.getGrindingMax()) {
				dataRow.createCell(16).setCellValue(item.getGrindingMax().toString());
			}
			if (null != item.getRepairAmount()) {
				dataRow.createCell(17).setCellValue(item.getRepairAmount().toString());
			}
			dataRow.createCell(18).setCellValue(item.getUpdateUserName());
			dataRow.createCell(19).setCellValue(DateFormatUtils.format(item.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
			i++;
		}
		// 输出Excel文件
		String fileName = "刀具台账.xls";
		// 获得浏览器信息并转换为大写
		String agent = request.getHeader("User-Agent").toUpperCase();
		if (agent.indexOf("MSIE") > 0 || (agent.indexOf("GECKO") > 0 && agent.indexOf("RV:11") > 0)) {
			// 微软的浏览器(IE和Edge浏览器)
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
		}
		// 输出Excel文件
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
		wb.close();
		return null;
	}

	@ApiOperation(value = "刀具生命周期导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具生命周期导出", response = String.class) })
	@Secure()

	@GetMapping(path = { "/tool-life-export" })
	public String toolLifeExport(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "fullNumber", value = "刀具码", required = false) @RequestParam(required = false, defaultValue = "") String fullNumber, @ApiParam(name = "toolMap", value = "刀具码", required = false) @RequestParam(required = false, defaultValue = "") String toolMap, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate,
			@ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false, defaultValue = "") String toolNumber) throws Exception {

		Long curuserId = getAuthentication();
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(100000);

		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		Tool tool = new Tool();
		tool.setFullNumber(fullNumber);
		tool.setToolNumber(toolNumber);
		tool.setToolMap(toolMap);
		tool.setIsLife(1);

		Pagination<Tool> pagination = toolService.selectLifePageList(curuserId, tool, queryDto);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("刀具生命周期");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("刀具码");
		row1.createCell(1).setCellValue("刀具名称");
		row1.createCell(2).setCellValue("刀具图号");
		row1.createCell(3).setCellValue("物料编码");
		row1.createCell(4).setCellValue("刀具类型");
		row1.createCell(5).setCellValue("刀具状态");
		row1.createCell(6).setCellValue("入库编码");
		row1.createCell(7).setCellValue("单价");
		row1.createCell(8).setCellValue("已加工数量");
		row1.createCell(9).setCellValue("加工记录");
		row1.createCell(14).setCellValue("刃磨记录");
		row1.createCell(17).setCellValue("涂层记录");

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i <= 9; i++) {
			row1.getCell(i).setCellStyle(cellStyle);
		}
		row1.getCell(14).setCellStyle(cellStyle);
		row1.getCell(17).setCellStyle(cellStyle);

		row1.setHeight((short) 400);// 目的是想把行高设置成25px
		sheet.setColumnWidth(0, 7000);
		sheet.setColumnWidth(1, 7000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 5000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 5000);
		sheet.setColumnWidth(10, 5000);
		sheet.setColumnWidth(11, 3000);
		sheet.setColumnWidth(12, 5000);
		sheet.setColumnWidth(13, 3000);
		sheet.setColumnWidth(14, 3000);
		sheet.setColumnWidth(15, 5000);
		// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 6, 6));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 7, 7));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 8, 8));

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 9, 14));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 15, 17));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 18, 19));
		// 在sheet里创建第二行
		HSSFRow row2 = sheet.createRow(1);
		// 创建单元格并设置单元格内容
		row2.createCell(9).setCellValue("加工开始时间");
		row2.createCell(10).setCellValue("加工结束时间");
		row2.createCell(11).setCellValue("加工数量");
		row2.createCell(12).setCellValue("制件编号");
		row2.createCell(13).setCellValue("制件名称");
		row2.createCell(14).setCellValue("制件数量");

		row2.createCell(15).setCellValue("刃磨时间");
		row2.createCell(16).setCellValue("刃磨量");
		row2.createCell(17).setCellValue("刃磨人");

		row2.createCell(18).setCellValue("涂层时间");
		row2.createCell(19).setCellValue("涂层厂家");
		row2.setHeight((short) (25 * 20));//
		for (int i = 9; i < 20; i++) {
			row2.getCell(i).setCellStyle(cellStyle);
		}
		int i = 2;
		int startRow;

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		List<Tool> list = (List<Tool>) pagination.getRows();
		for (Tool item : list) {
			int pCount = 0, rCount = 0, cCount = 0, maxCount = 0;
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			startRow = i;
			List<ToolProcess> processList = item.getProcessList();
			if (!CollectionUtils.isEmpty(processList)) {
				pCount = processList.size();
			}
			List<ToolRepair> repairList = item.getRepairList();
			if (!CollectionUtils.isEmpty(repairList)) {
				rCount = repairList.size();
			}
			List<ToolCoat> coatList = item.getCoatList();
			if (!CollectionUtils.isEmpty(coatList)) {
				cCount = coatList.size();
			}
			maxCount = pCount > rCount ? pCount : rCount;
			maxCount = maxCount > cCount ? maxCount : cCount;

			dataRow.createCell(0).setCellValue(item.getFullNumber());

			dataRow.createCell(1).setCellValue(item.getToolName());
			dataRow.createCell(2).setCellValue(item.getToolMap());

			dataRow.createCell(3).setCellValue(item.getToolNumber());
			dataRow.createCell(4).setCellValue(item.getTypeName());
			dataRow.createCell(5).setCellValue(ToolStatusTypeEnum.getName(item.getToolState()));

			dataRow.createCell(6).setCellValue(item.getWarehouseCode());
			dataRow.createCell(7).setCellValue(null == item.getPrice() ? "" : String.valueOf(item.getPrice()));
			dataRow.createCell(8).setCellValue(null == item.getProcessAmount() ? "" : String.valueOf(item.getProcessAmount()));

			if (maxCount == 0) {
				i++;
				continue;
			}

			for (int l = 0; l < 9; l++) {
				dataRow.getCell(l).setCellStyle(cellStyle1);
			}

			for (int j = 0; j < maxCount; j++) {
				if (j > 0) {
					dataRow = sheet.createRow(i);
					dataRow.setHeight((short) (25 * 20));// 目的是想把行高设置成25px
				}
				if (pCount <= j) {
					// dataRow.createCell(9).setCellValue("");
					// dataRow.createCell(10).setCellValue("");
					// dataRow.createCell(11).setCellValue("");
				} else {
					ToolProcess process = processList.get(j);

					dataRow.createCell(9).setCellValue(null == process.getBeginTime() ? "" : DateFormatUtils.format(process.getBeginTime(), "yyyy-MM-dd HH:mm"));
					dataRow.createCell(10).setCellValue(null == process.getEndTime() ? "" : DateFormatUtils.format(process.getEndTime(), "yyyy-MM-dd HH:mm"));

					dataRow.createCell(11).setCellValue(process.getProcessQty());
					dataRow.createCell(12).setCellValue(process.getPartCode());
					dataRow.createCell(13).setCellValue(process.getPartName());
					dataRow.createCell(14).setCellValue(process.getProcessQty());
				}
				if (rCount <= j) {
					// dataRow.createCell(12).setCellValue("");
					// dataRow.createCell(13).setCellValue("");
					// dataRow.createCell(14).setCellValue("");
				} else {
					ToolRepair repair = repairList.get(j);

					dataRow.createCell(15).setCellValue(null == repair.getExecutTime() ? "" : DateFormatUtils.format(repair.getExecutTime(), "yyyy-MM-dd HH:mm"));
					dataRow.createCell(16).setCellValue(null == repair.getRepairMeasure() ? "" : repair.getRepairMeasure().toString());

					dataRow.createCell(17).setCellValue(repair.getExecutor());
				}
				if (cCount <= j) {
					// dataRow.createCell(15).setCellValue("");
					// dataRow.createCell(16).setCellValue("");
				} else {
					ToolCoat coat = coatList.get(j);
					dataRow.createCell(18).setCellValue(null == coat.getCoatTime() ? "" : DateFormatUtils.format(coat.getCoatTime(), "yyyy-MM-dd HH:mm"));
					dataRow.createCell(19).setCellValue(coat.getCoatSupplier());
				}
				i++;
			}

			if (maxCount == 1) {
				continue;
			}
			int endRow = startRow + maxCount - 1;
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 2, 2));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 3, 3));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 4, 4));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 5, 5));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 6, 6));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 7, 7));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 8, 8));
		}

		// 输出Excel文件
		String fileName = "刀具生命周期.xls";
		// 获得浏览器信息并转换为大写
		String agent = request.getHeader("User-Agent").toUpperCase();
		if (agent.indexOf("MSIE") > 0 || (agent.indexOf("GECKO") > 0 && agent.indexOf("RV:11") > 0)) {
			// 微软的浏览器(IE和Edge浏览器)
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
		}
		// 输出Excel文件
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
		wb.close();
		return null;
	}

	/**
	 * 查询刀具涂层分页列表
	 */
	@ApiOperation(value = "查询刀具涂层分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具涂层分页列表查询成功", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-coat-statistics-page-list")
	@Secure()
	public AjaxReturn toolCoatPriceStatisticsPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "fullNumber", value = "刀具码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "toolName", value = "刀具编码", required = false) @RequestParam(required = false) String toolName, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate)
			throws BusinessException {
		log.debug("ToolController.toolCoatPriceStatisticsPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("fullNumber:" + fullNumber);
			log.debug("toolNumber:" + toolNumber);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
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
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		Tool tool = new Tool();
		tool.setFullNumber(fullNumber);
		tool.setToolNumber(toolNumber);
		tool.setToolName(toolName);
		tool.setIsCoatStatistics(1);

		Pagination<Tool> pagination = toolService.selectStatisticsPageList(curuserId, tool, queryDto);
		log.debug("ToolController.toolCoatPriceStatisticsPageList>>>");
		return pagination;
	}

	/**
	 * 查询刀具涂层分页列表
	 * 
	 * @throws IOException
	 */
	@ApiOperation(value = "刀具涂层性价比导出", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具涂层性价比导出", response = Tool.class) })
	@RequestMapping(method = RequestMethod.GET, value = "/tool-coat-statistics-export")
	@Secure()
	public String coatPriceStatisticsExport(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "fullNumber", value = "刀具码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate) throws BusinessException, IOException {
		log.debug("ToolController.coatPriceStatisticsExport<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
			log.debug("toolNumber:" + toolNumber);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(curuserId > 0);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(200000);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		Tool tool = new Tool();
		tool.setFullNumber(fullNumber);
		tool.setToolNumber(toolNumber);
		tool.setIsCoatStatistics(1);

		Pagination<Tool> pagination = toolService.selectStatisticsPageList(curuserId, tool, queryDto);

		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("刀具生命周期");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("物料条码");
		row1.createCell(1).setCellValue("物料编码");
		row1.createCell(2).setCellValue("物料名称");
		row1.createCell(3).setCellValue("物料图号");
		row1.createCell(4).setCellValue("入库时间");
		row1.createCell(5).setCellValue("制件编码");
		row1.createCell(6).setCellValue("制件名称");
		row1.createCell(7).setCellValue("加工总数");
		row1.createCell(8).setCellValue("涂层次数");
		row1.createCell(9).setCellValue("涂层总价");
		// row1.createCell(10).setCellValue("刀具状态");
		row1.createCell(10).setCellValue("单件消耗");

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i <= 10; i++) {
			row1.getCell(i).setCellStyle(cellStyle);
		}

		row1.setHeight((short) 400);// 目的是想把行高设置成25px
		sheet.setColumnWidth(0, 7000);
		sheet.setColumnWidth(1, 7000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 5000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 5000);
		sheet.setColumnWidth(10, 5000);

		int i = 2;

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		List<Tool> list = (List<Tool>) pagination.getRows();
		for (Tool item : list) {
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			dataRow.createCell(0).setCellValue(item.getFullNumber());
			dataRow.createCell(1).setCellValue(item.getToolNumber());
			dataRow.createCell(2).setCellValue(item.getToolName());
			dataRow.createCell(3).setCellValue(item.getToolMap());
			dataRow.createCell(4).setCellValue(DateFormatUtils.format(item.getCreateTime(), "yyyy-MM-dd"));
			dataRow.createCell(5).setCellValue(item.getPartCode());

			dataRow.createCell(6).setCellValue(item.getPartName());
			dataRow.createCell(7).setCellValue(null == item.getProcessAmount() ? "" : String.valueOf(item.getProcessAmount()));
			dataRow.createCell(8).setCellValue(null == item.getCoatTimes() ? "" : String.valueOf(item.getCoatTimes()));
			dataRow.createCell(9).setCellValue(null == item.getCoatPriceAmount() ? "" : String.valueOf(item.getCoatPriceAmount()));
			dataRow.createCell(10).setCellValue(null == item.getCoatStatistics() ? "" : String.valueOf(item.getCoatStatistics()));
			i++;
		}
		// 输出Excel文件
		String fileName = "刀具涂层性价比.xls";
		// 获得浏览器信息并转换为大写
		String agent = request.getHeader("User-Agent").toUpperCase();
		if (agent.indexOf("MSIE") > 0 || (agent.indexOf("GECKO") > 0 && agent.indexOf("RV:11") > 0)) {
			// 微软的浏览器(IE和Edge浏览器)
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
		}
		// 输出Excel文件
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
		wb.close();
		return null;
	}

	/**
	 * 查询刀具涂层分页列表
	 * 
	 * @throws IOException
	 */
	@ApiOperation(value = "刀具性价比导出", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具性价比导出", response = Tool.class) })
	@RequestMapping(method = RequestMethod.GET, value = "/tool-statistics-export")
	@Secure()
	public String toolStatisticsExport(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "fullNumber", value = "刀具码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate) throws BusinessException, IOException {
		log.debug("ToolController.coatPriceStatisticsExport<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
			log.debug("toolNumber:" + toolNumber);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(curuserId > 0);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(200000);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		Tool tool = new Tool();
		tool.setFullNumber(fullNumber);
		tool.setToolNumber(toolNumber);
		// tool.setIsCoatStatistics(1);

		Pagination<Tool> pagination = toolService.selectToolStatisticsPageList(curuserId, tool, queryDto);

		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("刀具生命周期");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("物料条码");
		row1.createCell(1).setCellValue("物料编码");
		row1.createCell(2).setCellValue("物料名称");
		row1.createCell(3).setCellValue("物料图号");
		row1.createCell(4).setCellValue("入库时间");
		row1.createCell(5).setCellValue("制件编码");
		row1.createCell(6).setCellValue("制件名称");
		row1.createCell(7).setCellValue("加工总数");
		row1.createCell(8).setCellValue("单价");
		row1.createCell(9).setCellValue("刃磨次数");
		row1.createCell(10).setCellValue("刃磨总价");
		row1.createCell(11).setCellValue("涂层次数");
		row1.createCell(12).setCellValue("涂层总价");
		row1.createCell(13).setCellValue("单件消耗");

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i <= 10; i++) {
			row1.getCell(i).setCellStyle(cellStyle);
		}

		row1.setHeight((short) 400);// 目的是想把行高设置成25px
		sheet.setColumnWidth(0, 7000);
		sheet.setColumnWidth(1, 7000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 5000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 5000);
		sheet.setColumnWidth(10, 5000);
		sheet.setColumnWidth(11, 5000);
		sheet.setColumnWidth(12, 5000);
		sheet.setColumnWidth(13, 5000);
		int i = 2;

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		List<Tool> list = (List<Tool>) pagination.getRows();
		for (Tool item : list) {
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			dataRow.createCell(0).setCellValue(item.getFullNumber());
			dataRow.createCell(1).setCellValue(item.getToolNumber());
			dataRow.createCell(2).setCellValue(item.getToolName());
			dataRow.createCell(3).setCellValue(item.getToolMap());
			dataRow.createCell(4).setCellValue(DateFormatUtils.format(item.getCreateTime(), "yyyy-MM-dd"));
			dataRow.createCell(5).setCellValue(item.getPartCode());

			dataRow.createCell(6).setCellValue(item.getPartName());
			dataRow.createCell(7).setCellValue(null == item.getProcessAmount() ? "" : String.valueOf(item.getProcessAmount()));
			dataRow.createCell(8).setCellValue(null == item.getPrice() ? "" : String.valueOf(item.getPrice()));
			dataRow.createCell(9).setCellValue(null == item.getRepairTimes() ? "" : String.valueOf(item.getRepairTimes()));
			dataRow.createCell(10).setCellValue(null == item.getRepairPriceAmount() ? "" : String.valueOf(item.getRepairPriceAmount()));
			dataRow.createCell(11).setCellValue(null == item.getCoatStatistics() ? "" : String.valueOf(item.getCoatStatistics()));
			dataRow.createCell(12).setCellValue(null == item.getCoatPriceAmount() ? "" : String.valueOf(item.getCoatPriceAmount()));
			dataRow.createCell(13).setCellValue(null == item.getCoatStatistics() ? "" : String.valueOf(item.getCoatStatistics()));
			i++;
		}

		// 输出Excel文件
		String fileName = "刀具性价比.xls";
		// 获得浏览器信息并转换为大写
		String agent = request.getHeader("User-Agent").toUpperCase();
		if (agent.indexOf("MSIE") > 0 || (agent.indexOf("GECKO") > 0 && agent.indexOf("RV:11") > 0)) {
			// 微软的浏览器(IE和Edge浏览器)
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
		}
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
		wb.close();
		return null;
	}

	/**
	 * 查询刀具分页列表
	 */
	@ApiOperation(value = "查询刀具性价比分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀具性价比分页列表", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-statistics-page-list")
	@Secure()
	public AjaxReturn toolStatisticsPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "fullNumber", value = "刀具码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "toolName", value = "刀具名称", required = false) @RequestParam(required = false) String toolName, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate)
			throws BusinessException {
		log.debug("ToolController.toolStatisticsPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("fullNumber:" + fullNumber);
			log.debug("toolNumber:" + toolNumber);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
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
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		Tool tool = new Tool();
		tool.setFullNumber(fullNumber);
		tool.setToolNumber(toolNumber);
		tool.setToolName(toolName);
		Pagination<Tool> pagination = toolService.selectToolStatisticsPageList(curuserId, tool, queryDto);
		log.debug("ToolController.toolStatisticsPageList>>>");
		return pagination;
	}

	@ApiOperation(value = "查询刀具操作日志列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀具操作日志列表", response = ToolOperLog.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-oper-page-list")
	@Secure()
	public AjaxReturn toolOperPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "fullNumber", value = "刀具码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate) throws BusinessException {
		log.debug("ToolController.toolOperPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("fullNumber:" + fullNumber);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
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
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		ToolOperLog where = new ToolOperLog();
		where.setFullNumber(fullNumber);

		Pagination<ToolOperLog> pagination = operLogService.selectPageList(curuserId, where, queryDto);
		log.debug("ToolController.toolOperPageList>>>");
		return pagination;
	}

	@ApiOperation(value = "刀具操作记录导出", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具操作记录导出", response = String.class) })
	@RequestMapping(method = RequestMethod.GET, value = "/tool-oper-export")
	@Secure()
	public String toolOperExport(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "fullNumber", value = "刀具码", required = false) @RequestParam(required = false) String fullNumber, @ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false) Date beginDate, @ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false) Date endDate) throws BusinessException, IOException {
		log.debug("ToolController.toolOperExport<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(curuserId > 0);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(200000);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		ToolOperLog where = new ToolOperLog();
		where.setFullNumber(fullNumber);

		Pagination<ToolOperLog> pagination = operLogService.selectPageList(curuserId, where, queryDto);

		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("刀具操作日志");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("物料条码");
		row1.createCell(1).setCellValue("物料编码");
		row1.createCell(2).setCellValue("物料名称");
		row1.createCell(3).setCellValue("物料图号");
		row1.createCell(4).setCellValue("操作类型");
		row1.createCell(5).setCellValue("操作时间");
		row1.createCell(6).setCellValue("操作人");
		row1.createCell(7).setCellValue("操作内容");
		row1.createCell(8).setCellValue("备注");
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i <= 8; i++) {
			row1.getCell(i).setCellStyle(cellStyle);
		}

		row1.setHeight((short) 400);// 目的是想把行高设置成25px
		sheet.setColumnWidth(0, 7000);
		sheet.setColumnWidth(1, 7000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 5000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		int i = 2;

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		List<ToolOperLog> list = (List<ToolOperLog>) pagination.getRows();
		for (ToolOperLog item : list) {
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			dataRow.createCell(0).setCellValue(item.getFullNumber());
			dataRow.createCell(1).setCellValue(item.getToolNumber());
			dataRow.createCell(2).setCellValue(item.getToolName());
			dataRow.createCell(3).setCellValue(item.getToolMap());
			dataRow.createCell(4).setCellValue(getOperType(item.getOperType()));
			dataRow.createCell(5).setCellValue(DateFormatUtils.format(item.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			dataRow.createCell(6).setCellValue(item.getCreateUserName());
			dataRow.createCell(7).setCellValue(item.getOperateInfo());
			dataRow.createCell(8).setCellValue(item.getOperateRemark());
			i++;
		}

		// 输出Excel文件
		String fileName = "刀具操作日志.xls";
		// 获得浏览器信息并转换为大写
		String agent = request.getHeader("User-Agent").toUpperCase();
		if (agent.indexOf("MSIE") > 0 || (agent.indexOf("GECKO") > 0 && agent.indexOf("RV:11") > 0)) {
			// 微软的浏览器(IE和Edge浏览器)
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
		}
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
		wb.close();
		return null;
	}

	private String getOperType(int index) {
		String[] operTypeArr = { "新刀质检", "新刀入库", "领用出库", "生产返库", "刃磨出库", "刃磨", "刃磨检验", "涂层出库", "涂层检验", "刀盘安装", "刀具报废申请", "报废审核", "执行报废" };
		return operTypeArr[index];
	}

}