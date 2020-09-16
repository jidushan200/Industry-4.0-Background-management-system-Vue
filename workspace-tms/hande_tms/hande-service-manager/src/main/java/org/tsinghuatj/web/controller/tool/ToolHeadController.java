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
import org.tsinghuatj.tool.domain.ToolHead;
import org.tsinghuatj.tool.service.IToolHeadService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolHeadController extends BaseController {
	private @Autowired(required = false) IToolHeadService toolHeadService;

	@ApiOperation(value = "刀条组合分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合分页列表", response = ToolHead.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/head-page-list")
	public AjaxReturn headPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "headNumber", value = "headNumber", required = false) @RequestParam(required = false) String headNumber, @ApiParam(name = "headType", value = "组合类型", required = false) @RequestParam(required = false) Integer headType) throws BusinessException {
		log.debug("ToolHeadController.headPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("headNumber:" + headNumber);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		ToolHead where = new ToolHead();
		where.setHeadNumber(headNumber);
		where.setHeadType(headType);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);

		log.debug("ToolHeadController.headPageList>>>");
		return toolHeadService.selectPageList(userId, where, queryDto);
	}

	@ApiOperation(value = "刀条组合表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合表", response = ToolHead.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/head-list")
	public AjaxReturn headList(@ApiParam(name = "headNumber", value = "headNumber", required = false) @RequestParam(required = false) String headNumber, @ApiParam(name = "headType", value = "headType", required = false) @RequestParam(required = false) Integer headType) throws BusinessException {
		log.debug("ToolPController.headList<<<");
		if (log.isDebugEnabled()) {
			log.debug("headNumber:" + headNumber);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		ToolHead where = new ToolHead();
		where.setHeadNumber(headNumber);
		where.setHeadType(headType);
		log.debug("ToolPartPartController.headList>>>");
		return new AjaxReturn(200, null, toolHeadService.select(userId, where));
	}

	@ApiOperation(value = "刀条组合删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合删除", response = AjaxReturn.class) })
	@OperateLog(info = "刀条组合制件信息修改[刀条组合编码:%s]", params = { "headNumber" })
	@RequestMapping(method = RequestMethod.POST, value = "/head-delete-by-id")
	public AjaxReturn headDeleteById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("ToolHeadController.HeadDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		// 获取当前用户
		Long userId = getAuthentication();

		log.debug("ToolHeadController.HeadDeleteById>>>");
		return new AjaxReturn(200, null, toolHeadService.deleteById(userId, pkId));
	}

	@ApiOperation(value = "刀条组合添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合添加", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/head-add")
	@OperateLog(info = "刀条组合信息添加[刀条组合编码:%s]", params = { "headNumber" })
	public AjaxReturn headAdd(@ApiParam(name = "headNumber", value = "刀条组合编码", required = true) @RequestParam(required = true) String headNumber, @ApiParam(name = "headName", value = "刀条组合名称", required = true) @RequestParam(required = true) String headName, @ApiParam(name = "headType", value = "刀条组合类型", required = true) @RequestParam(required = true) Integer headType) throws BusinessException {
		log.debug("ToolHeadController.HeadAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("headNumber:" + headNumber);
			log.debug("headName:" + headName);
			log.debug("headType:" + headType);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		ToolHead head = new ToolHead();
		head.setHeadNumber(headNumber);
		head.setHeadName(headName);
		head.setHeadType(headType);
		log.debug("ToolHeadController.HeadAdd>>>");
		return new AjaxReturn(200, null, toolHeadService.insert(userId, head));
	}

	@ApiOperation(value = "刀条组合修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合修改", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/head-update")
	@OperateLog(info = "刀条组合信息修改[刀条组合编码:%s]", params = { "headNumber" })
	public AjaxReturn HeadUpdate(@ApiParam(name = "pkId", value = "主键id", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "headNumber", value = "刀条组合编码", required = true) @RequestParam(required = true) String headNumber, @ApiParam(name = "headName", value = "刀条组合名称", required = true) @RequestParam(required = true) String headName, @ApiParam(name = "headType", value = "刀条组合类型", required = true) @RequestParam(required = true) Integer headType) throws BusinessException {
		log.debug("ToolHeadController.HeadUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("headNumber:" + headNumber);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		toolHeadService.checkHeadNumber(headNumber, pkId);
		ToolHead head = new ToolHead();
		head.setPkId(pkId);
		head.setHeadNumber(headNumber);
		head.setHeadName(headName);
		head.setHeadType(headType);
		log.debug("ToolPartPartController.HeadUpdate>>>");
		return new AjaxReturn(200, null, toolHeadService.updateActiveById(userId, head, pkId));
	}

	@ApiOperation(value = "刀条组合信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合信息表导出", response = ToolHead.class) })
	@Secure()
	@GetMapping(path = { "/head-export" })
	public ResponseEntity<byte[]> downloadExcel() throws Exception {
		Long userId = getAuthentication();
		List<ToolHead> excelVOList = toolHeadService.select(userId, new ToolHead());
		excelVOList.forEach(item -> {
			if (2 == item.getHeadType()) {
				item.setHeadTypeName("两面刃");
			} else if (3 == item.getHeadType()) {
				item.setHeadTypeName("三面刃");
			}
		});
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, ToolHead.class, true, null, true);
		return getResponseEntity(data, "刀条组合信息表.xlsx");
	}

	@ApiOperation(value = "刀条组合信息导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀条组合信息导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/head-import")
	@Secure()
	@OperateLog(info = "刀条组合信息导入", params = {})
	public AjaxReturn HeadImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<ToolHead> thlist = ExcelUtils.getInstance().readExcel2Objects(inputStream, ToolHead.class, 1, 2000, 0);
		inputStream.close();
		toolHeadService.toolHeadImport(userId, thlist);
		return new AjaxReturn(200, null, 1);
	}

}
