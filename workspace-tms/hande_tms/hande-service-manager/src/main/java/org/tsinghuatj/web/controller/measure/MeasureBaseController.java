package org.tsinghuatj.web.controller.measure;

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
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.measure.domain.MeasureBase;
import org.tsinghuatj.measure.service.IMeasureBaseService;
import org.tsinghuatj.support.annotation.OperateLog;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/measure" })
public class MeasureBaseController extends BaseController{
	
	private @Autowired(required = false) IMeasureBaseService measureBaseService;
	
	/**
	 * 量具基础信息添加
	 */
	@ApiOperation(value = "量具基础信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具基础信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-base-add")
	@OperateLog(info = "量具基础信息添加[量具编码:%s->名称规格:%s]", params = { "measureNumber", "measureName" })
	public AjaxReturn measureBaseAdd(@ApiParam(name = "measureNumber", value = "量具编码", required = true) @RequestParam(required = true) String measureNumber,
			@ApiParam(name = "measureName", value = "名称规格", required = true) @RequestParam(required = true) String measureName, 
			@ApiParam(name = "model", value = "规格型号", required = true) @RequestParam(required = true) String model

	) throws BusinessException {
		log.debug("MeasureBaseController.measureBaseAdd<<<");
		if (log.isDebugEnabled()) {
		}
		// 获取当前用户
		Long userId = getAuthentication();
		// 封装参数信息
        MeasureBase measureBase = new MeasureBase();
        measureBase.setMeasureNumber(measureNumber);
        measureBase.setMeasureName(measureName);
        measureBase.setModel(model);
		log.debug("MeasureBaseController.measureBaseAdd>>>");

		return new AjaxReturn(200, null, measureBaseService.insert(userId, measureBase));
	}

	/**
	 * 量具基础信息删除
	 */
	@ApiOperation(value = "量具基础信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具基础信息", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-base-delete-by-id")
	@OperateLog(info = "量具基础信息删除[量具编码:%s]", params = { "measureNumber" })
	public AjaxReturn measureBaseDeleteById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "measureNumber", value = "measureNumber", required = true) @RequestParam(required = true) String measureNumber
			) throws BusinessException {
		log.debug("MeasureBaseController.measureBaseDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("MeasureBaseController.measureBaseDeleteById>>>");
		return new AjaxReturn(200, null, measureBaseService.deleteById(userId, pkId));
	}
	
	/**
	 * 根据编码查找
	 */
	@ApiOperation(value = "量具基础信息列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具基础信息列表查询成功", response = MeasureBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-base-get-by-number")
	public AjaxReturn measureBaseGetByNumber(@ApiParam(name = "measureNumber", value = "量具编码", required = true) @RequestParam(required = true) String measureNumber) throws BusinessException {
		log.debug("MeasureBaseController.measureBaseGetByNumber<<<");

		// 获取当前用户
		Long userId = getAuthentication();
		log.debug("MeasureBaseController.measureBaseGetByNumber>>>");
		return new AjaxReturn(200, null, measureBaseService.selectByNumber(userId, measureNumber));
	}
	
	/**
	 * 量具基础信息列表查询
	 */
	@ApiOperation(value = "量具基础信息列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具基础信息列表查询成功", response = MeasureBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-base-list")
	public AjaxReturn measureBaseList() throws BusinessException {
		log.debug("MeasureBaseController.measureBaseList<<<");
		if (log.isDebugEnabled()) {
		}

		// 获取当前用户
		Long userId = getAuthentication();
		MeasureBase measureBase = new MeasureBase();

		log.debug("MeasureBaseController.measureBaseList>>>");
		return new AjaxReturn(200, null, measureBaseService.select(userId, measureBase));
	}
	
	/**
	 * 量具基础信息分页列表
	 */
	@ApiOperation(value = "量具基础信息分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具基础信息分页列表查询成功", response = MeasureBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-base-page-list")
	public AjaxReturn measureBasePageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, 
			@ApiParam(name = "measureNumber", value = "量具编码", required = false) @RequestParam(required = false) String measureNumber,
			@ApiParam(name = "measureName", value = "名称规格", required = false) @RequestParam(required = false) String measureName, 
			@ApiParam(name = "model", value = "规格型号", required = false) @RequestParam(required = false) String model
			) throws BusinessException {
		log.debug("MeasureBaseController.measureBasePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
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

		MeasureBase measureBase = new MeasureBase();
        measureBase.setMeasureNumber(measureNumber);
        measureBase.setMeasureName(measureName);
        measureBase.setModel(model);

		Pagination<MeasureBase> pagination = measureBaseService.selectPageList(curuserId, measureBase, queryDto);
		log.debug("MeasureBaseController.measureBasePageList>>>");
		return pagination;
	}
	
	/**
	 * 量具基础信息更新
	 */
	@ApiOperation(value = "量具基础信息更新", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具基础信息更新成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-base-update")
	@OperateLog(info = "量具基础信息更新[量具编码:%s->名称规格:%s]", params = { "measureNumber", "measureName" })
	public AjaxReturn measureBaseUpdate(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "measureNumber", value = "量具编码", required = false) @RequestParam(required = false) String measureNumber,
			@ApiParam(name = "measureName", value = "名称规格", required = false) @RequestParam(required = false) String measureName, 
			@ApiParam(name = "model", value = "规格型号", required = false) @RequestParam(required = false) String model

	) throws BusinessException {
		log.debug("MeasureBaseController.measureBaseUpdate<<<");
		if (log.isDebugEnabled()) {
		}
		// 获取当前用户
		Long userId = getAuthentication();
		// 封装参数信息
        MeasureBase measureBase = new MeasureBase();
        measureBase.setMeasureNumber(measureNumber);
        measureBase.setMeasureName(measureName);
        measureBase.setModel(model);
		log.debug("MeasureBaseController.measureBaseUpdate>>>");

		return new AjaxReturn(200, null, measureBaseService.updateActiveById(userId, measureBase, pkId));
	}

	/**
	 * 量具基础信息导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "量具基础信息导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具基础信息导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-base-import")
	@OperateLog(info = "量具基础信息导入[%s]", params = { "" })
	// @Secure()
	public AjaxReturn measureBaseImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<MeasureBase> measureBaseList = ExcelUtils.getInstance().readExcel2Objects(inputStream, MeasureBase.class, 1, 1000, 0);
		inputStream.close();
		measureBaseService.measureBaseImport(userId, measureBaseList);
		return new AjaxReturn(200, null, 1);
	}
	
	/**
	 * 量具信息表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "量具信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "量具信息表导出", response = MeasureBase.class) })
	// @Secure()
	@GetMapping(path = { "/measure-base-export" })
	@OperateLog(info = "量具基础信息导出[%s]", params = { "" })
	public ResponseEntity<byte[]> downloadMeasureBaseExcel() throws Exception {
		Long userId = getAuthentication();
		List<MeasureBase> excelVOList = measureBaseService.select(userId, new MeasureBase());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, MeasureBase.class, true, null, true);
		return getResponseEntity(data, "量具信息表.xlsx");
	}
	
	/**
	 * 量具基础信息信息同步
	 */
	@ApiOperation(value = "量具基础信息信息同步", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具基础信息信息同步", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/measure-base-synchro")
	// @OperateLog(info = "部门添加[部门编码:%s->部门名称:%s]", params = {
	// "departmentCode","departmentName" })
	public AjaxReturn MeasureBaseSynchro(@ApiParam(name = "measureNumber", value = "量具编码", required = true) @RequestParam(required = true) String measureNumber) throws BusinessException {
		// 获取当前用户
		Long userId = getAuthentication();
		return new AjaxReturn(200, null, measureBaseService.measureBaseSynchro(userId, measureNumber));
	}

}
