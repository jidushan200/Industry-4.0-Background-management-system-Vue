package org.tsinghuatj.web.controller.tool;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
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
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.ToolBase;
import org.tsinghuatj.tool.domain.ToolType;
import org.tsinghuatj.tool.service.IToolBaseService;
import org.tsinghuatj.tool.service.IToolTypeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolBaseController extends BaseController {

	private @Autowired(required = false) IToolBaseService toolBaseService;
	private @Autowired(required = false) IToolTypeService toolTypeService;

	/**
	 * 刀具类型查询
	 */
	@ApiOperation(value = "刀具类型查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具类型查询成功", response = ToolType.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-type-list")
	public AjaxReturn toolTypeList() throws BusinessException {
		log.debug("ToolBaseController.toolTypeList<<<");

		// 获取当前用户
		Long userId = getAuthentication();
		log.debug("ToolBaseController.toolTypeList>>>");
		return new AjaxReturn(200, null, toolTypeService.select(userId, new ToolType()));
	}

	/**
	 * 刀具基础信息添加
	 */
	@ApiOperation(value = "刀具基础信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具基础信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-base-add")
	@OperateLog(info = "刀具基础信息添加[刀具编码:%s->刀具图号:%s]", params = { "toolNumber", "toolMap" })
	public AjaxReturn toolBaseAdd(
			@ApiParam(name = "toolNumber", value = "刀具编码", required = true) @RequestParam(required = true) String toolNumber,
			@ApiParam(name = "toolMap", value = "刀具图号", required = true) @RequestParam(required = true) String toolMap,
			@ApiParam(name = "toolName", value = "刀具名称", required = true) @RequestParam(required = true) String toolName,
			@ApiParam(name = "processEach", value = "每次还刀加工数", required = false) @RequestParam(required = false) Integer processEach,
			@ApiParam(name = "processEachMin", value = "每次还刀最小加工数", required = false) @RequestParam(required = false) Integer processEachMin,
			@ApiParam(name = "processEachMax", value = "每次还刀最大加工数", required = false) @RequestParam(required = false) Integer processEachMax,
			@ApiParam(name = "availableTimes", value = "可加工次数", required = false) @RequestParam(required = false) Integer availableTimes,
			@ApiParam(name = "grindingMax", value = "最大刃磨量", required = false) @RequestParam(required = false) BigDecimal grindingMax,
			@ApiParam(name = "grindingCordon", value = "预警刃磨量", required = false) @RequestParam(required = false) BigDecimal grindingCordon,
			@ApiParam(name = "typeId", value = "类型ID", required = false) @RequestParam(required = false) Integer typeId

	) throws BusinessException {
		log.debug("ToolBaseController.toolBaseAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
			log.debug("toolMap:" + toolMap);
			log.debug("processEach:" + processEach);
			log.debug("availableTimes:" + availableTimes);
			log.debug("grindingMax:" + grindingMax);
			log.debug("grindingCordon:" + grindingCordon);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		toolBaseService.toolNumberCheck(toolNumber, null);
		// 封装参数信息
		ToolBase toolBase = new ToolBase();
		toolBase.setToolNumber(toolNumber);
		toolBase.setToolMap(toolMap);
		toolBase.setToolName(toolName);
		if (null != processEach && null != availableTimes) {
			toolBase.setProcessTotal(processEach * availableTimes);
		}
		toolBase.setProcessEachMax(processEachMax);
		toolBase.setProcessEachMin(processEachMin);
		toolBase.setProcessEach(processEach);
		toolBase.setAvailableTimes(availableTimes);
		toolBase.setGrindingMax(grindingMax);
		toolBase.setGrindingCordon(grindingCordon);
		toolBase.setTypeId(typeId);
		log.debug("ToolBaseController.toolBaseAdd>>>");

		return new AjaxReturn(200, null, toolBaseService.insert(userId, toolBase));
	}

	/**
	 * 刀具基础信息删除
	 */
	@ApiOperation(value = "刀具基础信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具基础信息成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-base-delete-by-id")
	@OperateLog(info = "刀具基础信息删除[刀具编码:%s]", params = { "toolNumber" })
	public AjaxReturn toolBaseDeleteById(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "toolNumber", value = "toolNumber", required = true) @RequestParam(required = true) String toolNumber)
			throws BusinessException {
		log.debug("ToolBaseController.toolBaseDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("ToolBaseController.toolBaseDeleteById>>>");
		return new AjaxReturn(200, null, toolBaseService.deleteById(userId, pkId));
	}

	@ApiOperation(value = "刀具基础信息查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具基础信息查询", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-base-get-by-id")
	public AjaxReturn toolBaseGetById(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId)
			throws BusinessException {
		log.debug("ToolBaseController.toolBaseGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("ToolBaseController.toolBaseGetById>>>");
		return new AjaxReturn(200, null, toolBaseService.selectById(userId, pkId));
	}

	/**
	 * 刀具基础信息列表查询
	 */
	@ApiOperation(value = "刀具基础信息列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具基础信息列表查询成功", response = ToolBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-base-list")
	public AjaxReturn toolBaseList(
			@ApiParam(name = "typeId", value = "类型", required = false) @RequestParam(required = false) Integer typeId

	) throws BusinessException {
		log.debug("ToolBaseController.toolBaseList<<<");
		if (log.isDebugEnabled()) {
		}

		// 获取当前用户
		Long userId = getAuthentication();
		ToolBase toolBase = new ToolBase();
		toolBase.setTypeId(typeId);
		log.debug("ToolBaseController.toolBaseList>>>");
		return new AjaxReturn(200, null, toolBaseService.select(userId, toolBase));
	}

	/**
	 * 刀具图号信息列表查询
	 */
	@ApiOperation(value = "刀具图号信息列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具图号信息列表查询成功", response = ToolBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-map-list")
	public AjaxReturn toolMapList() throws BusinessException {
		log.debug("ToolBaseController.toolMapList<<<");
		if (log.isDebugEnabled()) {
		}

		// 获取当前用户
		Long userId = getAuthentication();
		ToolBase toolBase = new ToolBase();

		log.debug("ToolBaseController.toolMapList>>>");
		return new AjaxReturn(200, null, toolBaseService.selectMapList(userId, toolBase));
	}

	/**
	 * 刀具基础信息列表查询
	 */
	@ApiOperation(value = "刀具基础信息列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具基础信息列表查询成功", response = ToolBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-base-get-by-number")
	public AjaxReturn toolBaseGetByNumber(
			@ApiParam(name = "toolNumber", value = "刀具编码", required = true) @RequestParam(required = true) String toolNumber)
			throws BusinessException {
		log.debug("ToolBaseController.toolBaseGetByNumber<<<");

		// 获取当前用户
		Long userId = getAuthentication();
		log.debug("ToolBaseController.toolBaseGetByNumber>>>");
		return new AjaxReturn(200, null, toolBaseService.selectByNumber(userId, toolNumber));
	}

	/**
	 * 刀具基础信息分页列表
	 */
	@ApiOperation(value = "刀具基础信息分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具基础信息分页列表查询成功", response = ToolBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-base-page-list")
	public AjaxReturn toolBasePageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber,
			@ApiParam(name = "toolMap", value = "刀具图号", required = false) @RequestParam(required = false) String toolMap,
			@ApiParam(name = "sort", value = "排序", required = false) @RequestParam(required = false) String sort,
			@ApiParam(name = "typeId", value = "类型ID", required = false) @RequestParam(required = false) Integer typeId)
			throws BusinessException {
		log.debug("ToolBaseController.toolBasePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("toolNumber:" + toolNumber);
			log.debug("toolMap:" + toolMap);

		}

		// 获取当前用户
		Long curuserId = 1l;
		// 参数校验
		Validate.isTrue(curuserId > 0);
		Validate.isTrue(page >= 0);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		queryDto.setSort(sort);
		ToolBase toolBase = new ToolBase();
		toolBase.setToolNumber(toolNumber);
		toolBase.setToolMap(toolMap);
		toolBase.setTypeId(typeId);
		Pagination<ToolBase> pagination = toolBaseService.selectPageList(curuserId, toolBase, queryDto);
		log.debug("ToolBaseController.toolBasePageList>>>");
		return pagination;
	}

	/**
	 * 刀具基础信息修改
	 */
	@ApiOperation(value = "刀具基础信息修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具基础信息修改成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-base-update")
	@OperateLog(info = "刀具基础信息修改[刀具编码:%s->刀具图号:%s]", params = { "toolNumber", "toolMap" })
	public AjaxReturn toolBaseUpdate(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "toolNumber", value = "刀具编码", required = false) @RequestParam(required = false) String toolNumber,
			@ApiParam(name = "toolMap", value = "刀具图号", required = false) @RequestParam(required = false) String toolMap,
			@ApiParam(name = "toolName", value = "刀具名称", required = false) @RequestParam(required = false) String toolName,
			@ApiParam(name = "processEach", value = "每次还刀加工数", required = false) @RequestParam(required = false) Integer processEach,
			@ApiParam(name = "processEachMin", value = "每次还刀最小加工数", required = false) @RequestParam(required = false) Integer processEachMin,
			@ApiParam(name = "processEachMax", value = "每次还刀最大加工数", required = false) @RequestParam(required = false) Integer processEachMax,
			@ApiParam(name = "availableTimes", value = "可加工次数", required = false) @RequestParam(required = false) Integer availableTimes,
			@ApiParam(name = "grindingMax", value = "最大刃磨量", required = false) @RequestParam(required = false) BigDecimal grindingMax,
			@ApiParam(name = "grindingCordon", value = "预警刃磨量", required = false) @RequestParam(required = false) BigDecimal grindingCordon,
			@ApiParam(name = "typeId", value = "类型ID", required = false) @RequestParam(required = false) Integer typeId,
			@ApiParam(name = "price", value = "价格", required = false) @RequestParam(required = false) BigDecimal price)
			throws BusinessException {
		log.debug("ToolBaseController.toolBaseUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
			log.debug("toolMap:" + toolMap);
			log.debug("processEach:" + processEach);
			log.debug("availableTimes:" + availableTimes);
			log.debug("grindingMax:" + grindingMax);
			log.debug("price:" + price);

		}
		// 获取当前用户
		Long userId = getAuthentication();
		toolBaseService.toolNumberCheck(toolNumber, pkId);
		// 封装参数信息
		ToolBase toolBase = new ToolBase();
		toolBase.setToolNumber(toolNumber);
		toolBase.setToolMap(toolMap);
		toolBase.setToolName(toolName);
		if (null != processEach && null != availableTimes) {
			toolBase.setProcessTotal(processEach * availableTimes);
		}
		toolBase.setProcessEachMax(processEachMax);
		toolBase.setProcessEachMin(processEachMin);
		toolBase.setProcessEach(processEach);
		toolBase.setAvailableTimes(availableTimes);
		toolBase.setGrindingMax(grindingMax);
		toolBase.setGrindingCordon(grindingCordon);
		toolBase.setPrice(price);
		toolBase.setTypeId(typeId);
		log.debug("ToolBaseController.toolBaseUpdate>>>");

		return new AjaxReturn(200, null, toolBaseService.updateActiveById(userId, toolBase, pkId));
	}

	/**
	 * 刀具基础信息导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "刀具基础信息导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具基础信息导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-base-import")
	@OperateLog(info = "刀具基础信息导入", params = { "" })
	@Secure()
	public AjaxReturn toolBaseImport(@RequestParam(value = "file", required = true) MultipartFile file)
			throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<ToolBase> toolBaseList = ExcelUtils.getInstance().readExcel2Objects(inputStream, ToolBase.class, 1, 1000,
				0);
		inputStream.close();
		toolBaseService.toolBaseImport(userId, toolBaseList);
		return new AjaxReturn(200, null, 1);
	}

	/**
	 * 物料信息表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "物料信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "物料信息表导出", response = ToolBase.class) })
	@Secure()
	@GetMapping(path = { "/tool-base-export" })
	public ResponseEntity<byte[]> toolBaseExport() throws Exception {
		Long userId = getAuthentication();
		List<ToolBase> excelVOList = toolBaseService.select(userId, new ToolBase());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, ToolBase.class, true, null,
				true);
		return getResponseEntity(data, "刀具基础信息表.xlsx");
	}

	/**
	 * 刀具基础信息信息同步
	 */
	@ApiOperation(value = "刀具基础信息信息同步", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具基础信息信息同步", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-base-synchro")
	@OperateLog(info = "刀具基础信息信息同步[物料编码:%s]", params = { "toolNumber" })
	public AjaxReturn toolBaseSynchro(
			@ApiParam(name = "toolNumber", value = "刀具基础信息编码", required = true) @RequestParam(required = true) String toolNumber)
			throws BusinessException {
		// 获取当前用户
		Long userId = getAuthentication();
		return new AjaxReturn(200, null, toolBaseService.toolBaseSynchro(userId, toolNumber));
	}

	/**
	 * 校验物料图号
	 */
	@ApiOperation(value = "校验物料图号", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "校验物料图号", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-Map-validate")
	public AjaxReturn toolMapValidate(
			@ApiParam(name = "toolMap", value = "刀具图号", required = true) @RequestParam(required = true) String toolMap)
			throws BusinessException {
		log.debug("ToolBaseController.toolMapValidate<<<");

		log.debug("ToolBaseController.toolMapValidate>>>");
		return new AjaxReturn(200, null, toolBaseService.toolMapValidate(toolMap));
	}

}
