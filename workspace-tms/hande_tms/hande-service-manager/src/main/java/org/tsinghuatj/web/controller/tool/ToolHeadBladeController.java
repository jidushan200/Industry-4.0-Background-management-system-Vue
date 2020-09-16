package org.tsinghuatj.web.controller.tool;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.ToolHeadBlade;
import org.tsinghuatj.tool.domain.ToolPart;
import org.tsinghuatj.tool.service.IToolHeadBladeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolHeadBladeController extends BaseController {
	private @Autowired(required = false) IToolHeadBladeService toolHeadBladePartService;

	@ApiOperation(value = "刀条组合分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合分页列表", response = ToolHeadBlade.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/head-blade-page-list")
	public AjaxReturn headBladePageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, 
			@ApiParam(name = "headNumber", value = "组合编码", required = false) @RequestParam(required = false) String headNumber, 
			@ApiParam(name = "toolNumber", value = "刀条编码", required = false) @RequestParam(required = false) String toolNumber
			
			) throws BusinessException {
		log.debug("ToolBladePartController.headBladePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		ToolHeadBlade where = new ToolHeadBlade();
		where.setHeadNumber(headNumber);
		where.setToolNumber(toolNumber);		
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);

		log.debug("ToolBladePartController.headBladePageList>>>");
		return toolHeadBladePartService.selectPageList(userId, where, queryDto);
	}

	@ApiOperation(value = "刀条组合表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合表", response = ToolHeadBlade.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/head-blade-list")
	public AjaxReturn headBladeList(
			@ApiParam(name = "toolNumber", value = "toolNumber", required = false) @RequestParam(required = false) String toolNumber,
			@ApiParam(name = "headNumber", value = "headNumber", required = false) @RequestParam(required = false) String headNumber
			) throws BusinessException {
		log.debug("ToolBladePartController.bladePartList<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
			log.debug("headNumber:" + headNumber);
			
		}
		// 获取当前用户
		Long userId = getAuthentication();
		ToolHeadBlade where = new ToolHeadBlade();
		where.setToolNumber(toolNumber);
		where.setHeadNumber(headNumber);
		log.debug("ToolBladePartController.bladePartList>>>");
		return new AjaxReturn(200, null, toolHeadBladePartService.select(userId, where));
	}

	@ApiOperation(value = "刀条组合删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合删除", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/head-blade-delete-by-id")
	public AjaxReturn headBladeDeleteById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("ToolBladePartController.headBladeDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		// 获取当前用户
		Long userId = getAuthentication();

		log.debug("ToolBladePartController.headBladeDeleteById>>>");
		return new AjaxReturn(200, null, toolHeadBladePartService.deleteById(userId, pkId));
	}

	@ApiOperation(value = "刀条组合添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合添加", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/head-blade-add")
	@OperateLog(info = "刀条组合添加[刀盘:%s->刀条编码:%s]", params = { "headNumber", "toolNumber" })
	public AjaxReturn headBladeAdd(@ApiParam(name = "headNumber", value = "刀盘编码", required = true) @RequestParam(required = true) String headNumber, @ApiParam(name = "toolNumber", value = "物料编码", required = true) @RequestParam(required = true) String toolNumber, @ApiParam(name = "toolName", value = "物料名称", required = true) @RequestParam(required = true) String toolName, @ApiParam(name = "toolQty", value = "物料数量", required = true) @RequestParam(required = true) Integer toolQty, @ApiParam(name = "eachProcessQty", value = "每次换刀加工数", required = true) @RequestParam(required = true) Integer eachProcessQty,
			@ApiParam(name = "processTimes", value = "加工次数", required = true) @RequestParam(required = true) Integer processTimes, @ApiParam(name = "grindingMax", value = "最大刃磨量", required = true) @RequestParam(required = true) BigDecimal grindingMax, @ApiParam(name = "grindingCordon", value = "刃磨预警量", required = true) @RequestParam(required = true) BigDecimal grindingCordon

	) throws BusinessException {
		log.debug("ToolBladePartController.headBladeAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("headNumber:" + headNumber);
			log.debug("toolNumber:" + toolNumber);
			log.debug("toolName:" + toolName);
			log.debug("toolQty:" + toolQty);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		ToolHeadBlade headBlade = new ToolHeadBlade();
		headBlade.setHeadNumber(headNumber);
		headBlade.setHeadName(headNumber + "刀条组合");
		headBlade.setToolNumber(toolNumber);
		headBlade.setToolName(toolName);
		headBlade.setToolQty(toolQty);
		headBlade.setEachProcessQty(eachProcessQty);
		headBlade.setProcessTimes(processTimes);
		headBlade.setGrindingMax(grindingMax);
		headBlade.setGrindingCordon(grindingCordon);
		log.debug("ToolBladePartController.headBladeAdd>>>");
		return new AjaxReturn(200, null, toolHeadBladePartService.insert(userId, headBlade));
	}

	@ApiOperation(value = "刀条组合修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合修改", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/head-blade-update")
	@OperateLog(info = "刀条组合修改[刀盘:%s->刀条编码:%s]", params = { "headNumber", "toolNumber" })
	public AjaxReturn headBladeUpdate(@ApiParam(name = "pkId", value = "主键id", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "headNumber", value = "刀盘编码", required = true) @RequestParam(required = true) String headNumber, @ApiParam(name = "toolNumber", value = "物料编码", required = true) @RequestParam(required = true) String toolNumber, @ApiParam(name = "toolName", value = "物料名称", required = true) @RequestParam(required = true) String toolName, @ApiParam(name = "toolQty", value = "物料数量", required = true) @RequestParam(required = true) Integer toolQty, @ApiParam(name = "eachProcessQty", value = "每次换刀加工数", required = true) @RequestParam(required = true) Integer eachProcessQty,
			@ApiParam(name = "processTimes", value = "加工次数", required = true) @RequestParam(required = true) Integer processTimes,
			@ApiParam(name = "grindingMax", value = "最大刃磨量", required = true) @RequestParam(required = true) BigDecimal grindingMax, @ApiParam(name = "grindingCordon", value = "刃磨预警量", required = true) @RequestParam(required = true) BigDecimal grindingCordon) throws BusinessException {
		log.debug("ToolBladePartController.headBladeAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("headNumber:" + headNumber);
			log.debug("toolNumber:" + toolNumber);
			log.debug("toolName:" + toolName);
			log.debug("toolQty:" + toolQty);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		ToolHeadBlade headBlade = new ToolHeadBlade();
		headBlade.setPkId(pkId);
		headBlade.setHeadNumber(headNumber);
		headBlade.setHeadName(headNumber + "刀盘组合");
		headBlade.setToolNumber(toolNumber);
		headBlade.setToolName(toolName);
		headBlade.setToolQty(toolQty);
		headBlade.setEachProcessQty(eachProcessQty);
		headBlade.setProcessTimes(processTimes);
		headBlade.setGrindingMax(grindingMax);
		headBlade.setGrindingCordon(grindingCordon);
		log.debug("ToolBladePartController.headBladeAdd>>>");
		return new AjaxReturn(200, null, toolHeadBladePartService.updateActiveById(userId, headBlade, pkId));
	}

	@ApiOperation(value = "刀条组合信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合信息表导出", response = ToolPart.class) })
	@Secure()
	@GetMapping(path = { "/head-blade-export" })
	public ResponseEntity<byte[]> downloadHeadBladeExcel() throws Exception {
		Long userId = getAuthentication();
		List<ToolHeadBlade> excelVOList = toolHeadBladePartService.select(userId, new ToolHeadBlade());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, ToolHeadBlade.class, true, null, true);
		return getResponseEntity(data, "刀条组合信息表.xlsx");
	}

	@ApiOperation(value = "刀条组合信息导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合信息导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/head-blade-import")
	@Secure()
	@OperateLog(info = "刀条组合导入", params = {})
	public AjaxReturn headBladeImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<ToolHeadBlade> hblist = ExcelUtils.getInstance().readExcel2Objects(inputStream, ToolHeadBlade.class, 1, 2000, 0);
		inputStream.close();
		toolHeadBladePartService.headBladeImport(userId, hblist);
		return new AjaxReturn(200, null, 1);
	}
	
	
	@ApiOperation(value = "复合刀条组合表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "复合刀条组合表", response = ToolHeadBlade.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/get-complex-head-blade-list")
	public AjaxReturn getComplexheadBladeList(@ApiParam(name = "toolNumber", value = "toolNumber", required = false) @RequestParam(required = false) String toolNumber,
			@ApiParam(name = "headNumber", value = "headNumber", required = false) @RequestParam(required = false) String headNumber) throws BusinessException {
		log.debug("ToolBladePartController.getComplexheadBladeList<<<");
		
		
		Long userId = getAuthentication();
		ToolHeadBlade where = new ToolHeadBlade();
		where.setHeadNumber(headNumber);
		log.debug("ToolBladePartController.getComplexheadBladeList>>>");
		return new AjaxReturn(200, null, toolHeadBladePartService.getComplexheadBladeList(userId, where));
	}

}
