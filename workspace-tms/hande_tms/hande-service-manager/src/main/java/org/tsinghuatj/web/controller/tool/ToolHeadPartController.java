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
import org.tsinghuatj.tool.domain.ToolHeadPart;
import org.tsinghuatj.tool.domain.ToolPart;
import org.tsinghuatj.tool.service.IToolHeadPartService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolHeadPartController extends BaseController {
	private @Autowired(required = false) IToolHeadPartService toolHeadPartService;

	@ApiOperation(value = "刀条组合制件分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合制件分页列表", response = ToolHeadPart.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/head-part-page-list")
	public AjaxReturn headPartPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "headNumber", value = "headNumber", required = false) @RequestParam(required = false) String headNumber) throws BusinessException {
		log.debug("ToolPartPartController.headPartPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("headNumber:" + headNumber);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		ToolHeadPart where = new ToolHeadPart();
		where.setHeadNumber(headNumber);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);

		log.debug("ToolPartPartController.headPartPageList>>>");
		return toolHeadPartService.selectPageList(userId, where, queryDto);
	}

	@ApiOperation(value = "刀条组合制件表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合制件表", response = ToolHeadPart.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/head-part-list")
	public AjaxReturn headPartList(@ApiParam(name = "headNumber", value = "headNumber", required = false) @RequestParam(required = false) String headNumber) throws BusinessException {
		log.debug("ToolPartPartController.PartPartList<<<");
		if (log.isDebugEnabled()) {
			log.debug("headNumber:" + headNumber);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		ToolHeadPart where = new ToolHeadPart();
		where.setHeadNumber(headNumber);
		log.debug("ToolPartPartController.PartPartList>>>");
		return new AjaxReturn(200, null, toolHeadPartService.select(userId, where));
	}
	
	@ApiOperation(value = "刀条组合制件删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合制件删除", response = AjaxReturn.class) })
	@OperateLog(info = "刀条组合制件信息修改[刀条组合编码:%s->制件编码:%s]", params = { "headNumber", "partNumber" })
	@RequestMapping(method = RequestMethod.POST, value = "/head-part-delete-by-id")
	public AjaxReturn headPartDeleteById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("ToolPartPartController.headPartDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		// 获取当前用户
		Long userId = getAuthentication();

		log.debug("ToolPartPartController.headPartDeleteById>>>");
		return new AjaxReturn(200, null, toolHeadPartService.deleteById(userId, pkId));
	}
	
	@ApiOperation(value = "刀条组合制件添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合制件添加", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/head-part-add")
	@OperateLog(info = "刀条组合制件信息添加[刀条组合编码:%s->制件编码:%s]", params = { "headNumber", "partNumber" })
	public AjaxReturn headPartAdd(
			@ApiParam(name = "headNumber", value = "刀条组合编码", required = true) @RequestParam(required = true) String headNumber,
			@ApiParam(name = "partNumber", value = "制件编码", required = true) @RequestParam(required = true) String partCode
			) throws BusinessException {
		log.debug("ToolPartPartController.headPartAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("headNumber:" + headNumber);
			log.debug("partCode:" + partCode);
			
		}
		// 获取当前用户
		Long userId = getAuthentication();
		ToolHeadPart headPart = new ToolHeadPart();
		headPart.setHeadNumber(headNumber);
		headPart.setHeadName(headNumber+"刀条组合组合");
		headPart.setPartCode(partCode);		
		log.debug("ToolPartPartController.headPartAdd>>>");
		return new AjaxReturn(200, null, toolHeadPartService.insert(userId, headPart));
	}
	
	@ApiOperation(value = "刀条组合制件修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合制件修改", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/head-part-update")
	@OperateLog(info = "刀条组合制件信息修改[刀条组合编码:%s->制件编码:%s]", params = { "headNumber", "partNumber" })
	public AjaxReturn headPartUpdate(
			@ApiParam(name = "pkId", value = "主键id", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "headNumber", value = "刀条组合编码", required = true) @RequestParam(required = true) String headNumber,
			@ApiParam(name = "partNumber", value = "制件编码", required = true) @RequestParam(required = true) String partCode
			) throws BusinessException {
		log.debug("ToolPartPartController.headPartUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("headNumber:" + headNumber);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		ToolHeadPart headPart = new ToolHeadPart();
		headPart.setPkId(pkId);
		headPart.setHeadNumber(headNumber);
		headPart.setHeadName(headNumber+"刀条组合组合");
		headPart.setPartCode(partCode);
		log.debug("ToolPartPartController.headPartUpdate>>>");
		return new AjaxReturn(200, null, toolHeadPartService.updateActiveById(userId, headPart, pkId));		
	}
	
	
	@ApiOperation(value = "刀条组合制件信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合制件信息表导出", response = ToolPart.class) })
	@Secure()
	@GetMapping(path = { "/head-part-export" })
	public ResponseEntity<byte[]> downloadHeadPartExcel() throws Exception {
		Long userId = getAuthentication();
		List<ToolHeadPart> excelVOList = toolHeadPartService.select(userId, new ToolHeadPart());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, ToolHeadPart.class, true, null, true);
		return getResponseEntity(data, "刀条组合制件信息表.xlsx");
	}
	
	
	
	@ApiOperation(value = "刀条组合制件信息导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合制件信息导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/head-part-import")
	@Secure()
	@OperateLog(info = "刀条组合制件信息导入", params = {})
	public AjaxReturn headpartImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<ToolHeadPart> hblist = ExcelUtils.getInstance().readExcel2Objects(inputStream, ToolHeadPart.class, 1, 2000, 0);
		inputStream.close();
		toolHeadPartService.headPartImport(userId, hblist);
		return new AjaxReturn(200, null, 1);
	}
	
	
	
}
