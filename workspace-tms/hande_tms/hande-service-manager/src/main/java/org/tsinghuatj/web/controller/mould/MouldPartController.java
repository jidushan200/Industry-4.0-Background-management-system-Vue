package org.tsinghuatj.web.controller.mould;

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
import org.tsinghuatj.mould.domain.MouldPart;
import org.tsinghuatj.mould.service.IMouldPartService;
import org.tsinghuatj.support.annotation.OperateLog;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/mould" })
public class MouldPartController extends BaseController{
	private @Autowired(required = false) IMouldPartService mouldPartService;
	
	/**
	 * 模具制件
	 */
	@ApiOperation(value = "模具制件分页列表  ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具制件分页列表查询成功", response = Part.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-part-page-list")
	public AjaxReturn mouldPartPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, 
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, 
			@ApiParam(name = "partCode", value = "零件编号", required = false) @RequestParam(required = false, defaultValue = "") String partCode, 
			@ApiParam(name = "mouldNumber", value = "模具编号", required = false) @RequestParam(required = false, defaultValue = "") String mouldNumber) throws BusinessException {
		log.debug("MouldPartController.mouldPartPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("partCode:" + partCode);
			log.debug("mouldNumber:" + mouldNumber);
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
		MouldPart mouldPart = new MouldPart();
		mouldPart.setPartCode(partCode);
		mouldPart.setMouldNumber(mouldNumber);

		Pagination<MouldPart> pagination = mouldPartService.selectPageList(curuserId, mouldPart, queryDto);
		log.debug("MouldPartController.mouldPartPageList>>>");
		return pagination;
	}

	/**
	 * 模具制件新增
	 */
	@ApiOperation(value = "模具制件新增", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具制件新增成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-part-add")
	@OperateLog(info = "模具制件新增[模具编码:%s->制件编码:%s]", params = { "mouldNumber", "partCode" })
	public AjaxReturn mouldPartAdd(@ApiParam(name = "mouldId", value = "模具ID", required = true) @RequestParam(required = true) Long mouldId, 
			@ApiParam(name = "partId", value = "制件ID", required = true) @RequestParam(required = true) Long partId, 
			@ApiParam(name = "partCode", value = "零件编号", required = false) @RequestParam(required = false, defaultValue = "") String partCode, 
			@ApiParam(name = "mouldNumber", value = "模具编号", required = false) @RequestParam(required = false, defaultValue = "") String mouldNumber

	) throws BusinessException {
		log.debug("MouldPartController.mouldPartAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("mouldId:" + mouldId);
			log.debug("partId:" + partId);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		mouldPartService.mouldPartCheck(null, mouldId, partId);
		// 封装参数信息
		MouldPart mouldPart = new MouldPart();
		mouldPart.setMouldId(mouldId);
		mouldPart.setPartId(partId);
		log.debug("MouldPartController.mouldPartAdd>>>");
		return new AjaxReturn(200, null, mouldPartService.insert(userId, mouldPart));
	}

	/**
	 * 模具制件新增
	 */
	@ApiOperation(value = "模具制件新增", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具制件新增成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-part-update")
	@OperateLog(info = "模具制件修改[模具编码:%s->制件编码:%s]", params = { "mouldNumber", "partCode" })
	public AjaxReturn mouldPartUpdate(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId, 
			@ApiParam(name = "mouldId", value = "模具ID", required = true) @RequestParam(required = true) Long mouldId, 
			@ApiParam(name = "partId", value = "制件ID", required = true) @RequestParam(required = true) Long partId, 
			@ApiParam(name = "partCode", value = "零件编号", required = false) @RequestParam(required = false, defaultValue = "") String partCode, 
			@ApiParam(name = "mouldNumber", value = "模具编号", required = false) @RequestParam(required = false, defaultValue = "") String mouldNumber
	) throws BusinessException {
		log.debug("MouldPartController.mouldPartUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("mouldId:" + mouldId);
			log.debug("partId:" + partId);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		mouldPartService.mouldPartCheck(pkId, mouldId, partId);
		// 封装参数信息
		MouldPart mouldPart = new MouldPart();
		mouldPart.setPkId(pkId);
		mouldPart.setMouldId(mouldId);
		mouldPart.setPartId(partId);
		log.debug("MouldPartController.mouldPartUpdate>>>");
		return new AjaxReturn(200, null, mouldPartService.updateActiveById(userId, mouldPart, pkId));
	}

	/**
	 * 模具制件删除
	 */
	@ApiOperation(value = "模具制件删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具制件删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-part-delete")
	@OperateLog(info = "模具制件删除[模具编码:%s->制件编码:%s]", params = { "mouldNumber", "partCode" })
	public AjaxReturn mouldPartDelete(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId, 
			@ApiParam(name = "partCode", value = "零件编号", required = false) @RequestParam(required = false, defaultValue = "") String partCode, 
			@ApiParam(name = "mouldNumber", value = "模具编号", required = false) @RequestParam(required = false, defaultValue = "") String mouldNumber) throws BusinessException {
		log.debug("MouldPartController.mouldPartDelete<<<");

		// 获取当前用户
		Long userId = getAuthentication();
		log.debug("MouldPartController.mouldPartDelete>>>");
		return new AjaxReturn(200, null, mouldPartService.deleteById(userId, pkId));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/mould-part-get-by-number")
	public AjaxReturn mouldPartGetByCode(@ApiParam(name = "mouldNumber", value = "模具编号", required = false) @RequestParam(required = false) String mouldNumber,
			@ApiParam(name = "partCode", value = "制件编号", required = false) @RequestParam(required = false) String partCode
	) throws BusinessException {
		log.debug("MouldPartController.mouldPartGetByCode<<<");
		if (log.isDebugEnabled()) {
			log.debug("mouldNumber:" + mouldNumber);
			log.debug("partCode:" + partCode);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		// 封装参数信息
		MouldPart mouldPart = new MouldPart();
		mouldPart.setPartCode(partCode);
		mouldPart.setMouldNumber(mouldNumber);
		log.debug("MouldPartController.mouldPartGetByCode>>>");
		return new AjaxReturn(200, null, mouldPartService.select(userId, mouldPart));
	}
	
	/**
	 * 模具制件信息表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "模具制件信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具制件信息表导出", response = MouldPart.class) })
	// @Secure()
	@GetMapping(path = { "/mould-part-export" })
	@OperateLog(info = "模具制件导出")
	public ResponseEntity<byte[]> downloadMouldPartExcel() throws Exception {
		Long userId = getAuthentication();
		List<MouldPart> excelVOList = mouldPartService.select(userId, new MouldPart());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, MouldPart.class, true, null, true);
		return getResponseEntity(data, "模具制件信息表.xlsx");
	}
	
	/**
	 * 模具制件导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "模具制件导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具制件导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-part-import")
	@OperateLog(info = "模具制件导入")
	// @Secure()
	public AjaxReturn mouldPartImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<MouldPart> mouldPartList = ExcelUtils.getInstance().readExcel2Objects(inputStream, MouldPart.class, 1, 1000, 0);
		inputStream.close();
		mouldPartService.mouldPartImport(userId, mouldPartList);
		return new AjaxReturn(200, null, 1);
	}
}
