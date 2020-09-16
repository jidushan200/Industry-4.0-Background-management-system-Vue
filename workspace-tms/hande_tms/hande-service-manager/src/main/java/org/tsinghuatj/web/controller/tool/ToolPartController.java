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
import org.tsinghuatj.base.domain.Part;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.ToolPart;
import org.tsinghuatj.tool.service.IToolPartService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/base" })
public class ToolPartController extends BaseController {
	private @Autowired(required = false) IToolPartService toolpartService;

	/**
	 * 物料制件
	 */
	@ApiOperation(value = "物料制件分页列表  ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "物料制件分页列表查询成功", response = Part.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/toolpart-page-list")
	public AjaxReturn toolPartPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, 
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, 
			@ApiParam(name = "partCode", value = "零件编号", required = false) @RequestParam(required = false, defaultValue = "") String partCode, 
			@ApiParam(name = "typeId", value = "物料类型", required = false) @RequestParam(required = false, defaultValue = "") Integer typeId,
			@ApiParam(name = "toolNumber", value = "刀具编号", required = false) @RequestParam(required = false, defaultValue = "") String toolNumber) throws BusinessException {
		log.debug("ToolPartController.toolPartPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("partCode:" + partCode);
			log.debug("toolNumber:" + toolNumber);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		ToolPart toolPart = new ToolPart();
		toolPart.setPartCode(partCode);
		toolPart.setToolNumber(toolNumber);
		toolPart.setTypeId(typeId);
		Pagination<ToolPart> pagination = toolpartService.selectPageList(curuserId, toolPart, queryDto);
		log.debug("ToolPartController.toolPartPageList>>>");
		return pagination;
	}

	/**
	 * 物料制件新增
	 */
	@ApiOperation(value = "物料制件新增", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "物料制件新增成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/toolpart-add")
	@OperateLog(info = "物料制件新增[物料:%s->制件:%s]", params = { "toolNumber", "partCode" })
	public AjaxReturn toolpartAdd(@ApiParam(name = "toolId", value = "物料ID", required = true) @RequestParam(required = true) Long toolId, @ApiParam(name = "partId", value = "制件ID", required = true) @RequestParam(required = true) Long partId, @ApiParam(name = "toolNumber", value = "物料ID", required = true) @RequestParam(required = true) String toolNumber, @ApiParam(name = "partCode", value = "物料ID", required = true) @RequestParam(required = true) String partCode) throws BusinessException {
		log.debug("ToolPartController.toolPartAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolId:" + toolId);
			log.debug("partId:" + partId);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		toolpartService.checkToolPart(null, toolId, partId);
		// 封装参数信息
		ToolPart toolpart = new ToolPart();
		toolpart.setToolId(toolId);
		toolpart.setPartId(partId);
		log.debug("ToolPartController.toolPartAdd>>>");
		return new AjaxReturn(200, null, toolpartService.insert(userId, toolpart));
	}

	/**
	 * 物料制件新增
	 */
	@ApiOperation(value = "物料制件新增", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "物料制件新增成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/toolpart-update")
	@OperateLog(info = "物料制件修改[物料:%s->制件:%s]", params = { "toolNumber", "partCode" })
	public AjaxReturn toolpartupdate(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "toolId", value = "物料ID", required = true) @RequestParam(required = true) Long toolId, @ApiParam(name = "partId", value = "制件ID", required = true) @RequestParam(required = true) Long partId, @ApiParam(name = "toolNumber", value = "物料ID", required = true) @RequestParam(required = true) String toolNumber, @ApiParam(name = "partCode", value = "物料ID", required = true) @RequestParam(required = true) String partCode) throws BusinessException {
		log.debug("ToolPartController.toolPartUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolId:" + toolId);
			log.debug("partId:" + partId);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		toolpartService.checkToolPart(pkId, toolId, partId);
		// 封装参数信息
		ToolPart toolpart = new ToolPart();
		toolpart.setPkId(pkId);
		toolpart.setToolId(toolId);
		toolpart.setPartId(partId);
		log.debug("ToolPartController.toolPartUpdate>>>");
		return new AjaxReturn(200, null, toolpartService.updateActiveById(userId, toolpart, pkId));
	}

	/**
	 * 物料制件删除
	 */
	@ApiOperation(value = "物料制件删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "物料制件删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/toolpart-delete")
	@OperateLog(info = "物料制件删除[物料:%s->制件:%s]", params = { "toolNumber", "partCode" })
	public AjaxReturn toolPartDelete(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "toolNumber", value = "物料ID", required = true) @RequestParam(required = true) String toolNumber, @ApiParam(name = "partCode", value = "物料ID", required = true) @RequestParam(required = true) String partCode) throws BusinessException {
		log.debug("ToolPartController.toolPartDelete<<<");

		// 获取当前用户
		Long userId = getAuthentication();
		log.debug("ToolPartController.toolPartDelete>>>");
		return new AjaxReturn(200, null, toolpartService.deleteById(userId, pkId));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/toolpart-get-by-number")
	public AjaxReturn toolPartGetByCode(@ApiParam(name = "toolNumber", value = "物料编号", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "partCode", value = "制件编号", required = false) @RequestParam(required = false) String partCode

	) throws BusinessException {
		log.debug("ToolPartController.toolPartGetByCode<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
			log.debug("partCode:" + partCode);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		// 封装参数信息
		ToolPart toolpart = new ToolPart();
		toolpart.setPartCode(partCode);
		toolpart.setToolNumber(toolNumber);
		log.debug("ToolPartController.toolPartGetByCode>>>");
		return new AjaxReturn(200, null, toolpartService.select(userId, toolpart));
	}

	/**
	 * 物料制件信息表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "物料制件信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "物料制件信息表导出", response = ToolPart.class) })
	// @Secure()
	@GetMapping(path = { "/tool-part-export" })
	@OperateLog(info = "刀具物料制件导出[%s]", params = { "" })
	public ResponseEntity<byte[]> downloadToolPartExcel() throws Exception {
		Long userId = getAuthentication();
		List<ToolPart> excelVOList = toolpartService.select(userId, new ToolPart());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, ToolPart.class, true, null, true);
		return getResponseEntity(data, "刀具制件信息表.xlsx");
	}

	/**
	 * 物料制件导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "物料制件导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "物料制件导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/toolpart-import")
	@OperateLog(info = "刀具物料制件导入[%s]", params = { "" })
	// @Secure()
	public AjaxReturn toolpartImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<ToolPart> tpList = ExcelUtils.getInstance().readExcel2Objects(inputStream, ToolPart.class, 1, 1000, 0);
		inputStream.close();
		toolpartService.toolPartImport(userId, tpList);
		return new AjaxReturn(200, null, 1);
	}

}
