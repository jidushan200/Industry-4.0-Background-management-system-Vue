package org.tsinghuatj.web.controller.tool;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
import org.tsinghuatj.tool.domain.ToolPlate;
import org.tsinghuatj.tool.service.IToolPlateService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolPlateController extends BaseController {
	private @Autowired(required = false) IToolPlateService toolplateService;

	@ApiOperation(value = "刀盘分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀盘分页列表", response = ToolPlate.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/plate-page-list")
	public AjaxReturn platePageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "plateNumber", value = "plateNumber", required = false) @RequestParam(required = false) String plateNumber) throws BusinessException {
		log.debug("ToolplateController.platePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("plateNumber:" + plateNumber);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		ToolPlate where = new ToolPlate();
		where.setPlateNumber(plateNumber);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);

		log.debug("ToolplateController.platePageList>>>");
		return toolplateService.selectPageList(userId, where, queryDto);
	}

	@ApiOperation(value = "刀盘表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀盘表", response = ToolPlate.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/plate-list")
	public AjaxReturn plateList(
			@ApiParam(name = "useStatus", value = "useStatus", required = false) @RequestParam(required = false) Integer useStatus,
			@ApiParam(name = "plateName", value = "plateName", required = false) @RequestParam(required = false) String plateName) throws BusinessException {
		log.debug("ToolPController.plateList<<<");
		if (log.isDebugEnabled()) {
			log.debug("plateName:" + plateName);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		ToolPlate where = new ToolPlate();
		where.setUseStatus(useStatus);
		where.setPlateName(plateName);
		log.debug("ToolPartPartController.plateList>>>");
		return new AjaxReturn(200, null, toolplateService.select(userId, where));
	}
	
	@ApiOperation(value = "刀盘删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀盘删除", response = AjaxReturn.class) })
	@OperateLog(info = "刀盘信息修改[刀盘编码:%s]", params = { "plateNumber"})
	@RequestMapping(method = RequestMethod.POST, value = "/plate-delete-by-id")
	public AjaxReturn plateDeleteById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("ToolplateController.plateDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		// 获取当前用户
		Long userId = getAuthentication();

		log.debug("ToolplateController.plateDeleteById>>>");
		return new AjaxReturn(200, null, toolplateService.deleteById(userId, pkId));
	}
	
	@ApiOperation(value = "刀盘添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀盘添加", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/plate-add")
	@OperateLog(info = "刀盘信息添加[刀盘编码:%s]", params = { "plateNumber"})
	public AjaxReturn plateAdd(
			@ApiParam(name = "plateNumber", value = "刀盘编码", required = true) @RequestParam(required = true) String plateNumber,
			@ApiParam(name = "plateName", value = "刀盘名称", required = true) @RequestParam(required = true) String plateName,
			@ApiParam(name = "useStatus", value = "刀盘状态", required = true) @RequestParam(required = true) Integer useStatus
			) throws BusinessException {
		log.debug("ToolplateController.plateAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("plateNumber:" + plateNumber);
			log.debug("plateName:" + plateName);			
		}
		// 获取当前用户
		Long userId = getAuthentication();
		ToolPlate plate = new ToolPlate();
		plate.setPlateNumber(plateNumber);
		plate.setPlateName(plateName);
		plate.setUseStatus(useStatus);
		log.debug("ToolplateController.plateAdd>>>");
		return new AjaxReturn(200, null, toolplateService.insert(userId, plate));
	}
	
	@ApiOperation(value = "刀盘修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀盘修改", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/plate-update")
	@OperateLog(info = "刀盘信息修改[刀盘编码:%s]", params = { "plateNumber"})
	public AjaxReturn plateUpdate(
			@ApiParam(name = "pkId", value = "主键id", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "plateNumber", value = "刀盘编码", required = true) @RequestParam(required = true) String plateNumber,
			@ApiParam(name = "plateName", value = "刀盘名称", required = true) @RequestParam(required = true) String plateName,
			@ApiParam(name = "useStatus", value = "刀盘状态", required = true) @RequestParam(required = true) Integer useStatus
			) throws BusinessException {
		log.debug("ToolplateController.plateUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("plateNumber:" + plateNumber);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		ToolPlate plate = new ToolPlate();
		plate.setPkId(pkId);
		plate.setPlateNumber(plateNumber);
		plate.setPlateName(plateName);
		plate.setUseStatus(useStatus);
		log.debug("ToolPartPartController.plateUpdate>>>");
		return new AjaxReturn(200, null, toolplateService.updateActiveById(userId, plate, pkId));		
	}
	
	
	@ApiOperation(value = "刀盘信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀盘信息表导出", response = ToolPlate.class) })
	@Secure()
	@GetMapping(path = { "/plate-export" })
	public ResponseEntity<byte[]> downloadplateExcel() throws Exception {
		Long userId = getAuthentication();
		List<ToolPlate> excelVOList = toolplateService.select(userId, new ToolPlate());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, ToolPlate.class, true, null, true);
		return getResponseEntity(data, "刀盘信息表.xlsx");
	}
	
	
	
	@ApiOperation(value = "刀盘信息导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀盘信息导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/plate-import")
	@Secure()
	@OperateLog(info = "刀盘信息导入", params = {})
	public AjaxReturn plateImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<ToolPlate> tplist = ExcelUtils.getInstance().readExcel2Objects(inputStream, ToolPlate.class, 1, 2000, 0);
		inputStream.close();
		toolplateService.importToolPlate(userId, tplist);
		return new AjaxReturn(200, null, 1);
	}
	
}
