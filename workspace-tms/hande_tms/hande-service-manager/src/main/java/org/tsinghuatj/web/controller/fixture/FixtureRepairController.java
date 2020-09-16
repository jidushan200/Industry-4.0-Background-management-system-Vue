package org.tsinghuatj.web.controller.fixture;

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
import org.tsinghuatj.fixture.domain.FixtureRepair;
import org.tsinghuatj.fixture.service.IFixtureRepairService;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/fixture" })
public class FixtureRepairController extends BaseController {
	private @Autowired(required = false) IFixtureRepairService fixtureRepairService;

	/**
	 * 待检夹具列表
	 */
	@ApiOperation(value = "查询夹具质检报告分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具质检报告分页列表查询成功", response = FixtureRepair.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/repair-page-list")
	public AjaxReturn repairPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, 
			@ApiParam(name = "fixtureBarcode", value = "物料条码", required = false) @RequestParam(required = false) String fixtureBarcode,
			@ApiParam(name = "fixtureName", value = "物料名称", required = false) @RequestParam(required = false) String fixtureName,
			@ApiParam(name = "fixtureMap", value = "物料图号", required = false) @RequestParam(required = false) String fixtureMap

	) throws BusinessException {
		log.debug("FixtureRepairController.repairePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 0);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);

		FixtureRepair where = new FixtureRepair();
		where.setFixtureBarcode(fixtureBarcode);
		where.setFixtureMap(fixtureMap);
		where.setFixtureName(fixtureName);
		Pagination<FixtureRepair> pagination = fixtureRepairService.selectPageList(userId, where, queryDto);
		log.debug("FixtureRepairController.repairePageList>>>");
		return pagination;
	}
	
	
	/**
	 * 夹具修磨
	 */
	@ApiOperation(value = "夹具修磨", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具修磨", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/repair-add")
	@OperateLog(info = "夹具修磨新增[夹具条码:%s->修磨量:%s]", params = { "fixtureBarcode", "repairMeasure" })
	public AjaxReturn fixtureRepairAdd(
			@ApiParam(name = "fixtureId", value = "夹具Id", required = false) @RequestParam(required = false) Long fixtureId,
			@ApiParam(name = "fixtureBarcode", value = "物料条码", required = false) @RequestParam(required = false) String fixtureBarcode, 
			@ApiParam(name = "FixtureName", value = "物料名称", required = true) @RequestParam(required = true) String fixtureName, 
			@ApiParam(name = "repairMeasure", value = "修磨量", required = true) @RequestParam(required = true) BigDecimal repairMeasure, 
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false) String remark,
			@ApiParam(name = "repairStatus", value = "修磨状态", required = false) @RequestParam(required = false)Integer repairStatus			
			) throws BusinessException {
		log.debug("FixtureRepairController.fixtureRepairAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("FixtureId:" + fixtureId);
			log.debug("fixtureBarcode:" + fixtureBarcode);
			log.debug("repairMeasure:" + repairMeasure);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		Long userId = getAuthentication();

		// 封装参数信息
		FixtureRepair fixtureRepair = new FixtureRepair();
		fixtureRepair.setFixtureId(fixtureId);
		fixtureRepair.setFixtureBarcode(fixtureBarcode);
		fixtureRepair.setFixtureName(fixtureName);
		fixtureRepair.setRepairMeasure(repairMeasure);
		fixtureRepair.setRemark(remark);
		fixtureRepair.setRepairStatus(repairStatus);
		log.debug("FixtureRepairController.fixtureRepairAdd>>>");
		return new AjaxReturn(200, null, fixtureRepairService.insert(userId, fixtureRepair));
	}
	
	
	/**
	 * 夹具修磨
	 */
	@ApiOperation(value = "夹具修磨", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具修磨", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/repair-update")
	@OperateLog(info = "夹具修磨修改[夹具条码:%s->修磨量:%s]", params = { "fixtureBarcode", "repairMeasure" })
	public AjaxReturn fixtureRepairUpdate(
			@ApiParam(name = "pkId", value = "夹具Id", required = false) @RequestParam(required = false) Long pkId,
			@ApiParam(name = "repairMeasure", value = "修磨量", required = true) @RequestParam(required = true) BigDecimal repairMeasure, 
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false) String remark,
			@ApiParam(name = "repairStatus", value = "修磨状态", required = false) @RequestParam(required = false)Integer repairStatus			
			) throws BusinessException {
		log.debug("FixtureRepairController.fixtureRepairUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("repairMeasure:" + repairMeasure);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		Long userId = getAuthentication();

		// 封装参数信息
		FixtureRepair fixtureRepair = new FixtureRepair();
		fixtureRepair.setPkId(pkId);
		fixtureRepair.setRepairMeasure(repairMeasure);
		fixtureRepair.setRemark(remark);
		fixtureRepair.setRepairStatus(repairStatus);
		log.debug("FixtureRepairController.fixtureRepairUpdate>>>");
		return new AjaxReturn(200, null, fixtureRepairService.updateActiveById(userId, fixtureRepair, pkId));
	}
	
	/**
	 * 夹具修磨删除
	 */
	@ApiOperation(value = "夹具修磨", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具修磨删除", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/repair-delete")
	@OperateLog(info = "夹具修磨删除[主键:%s]", params = { "pkId" })
	public AjaxReturn fixtureRepairDelete(@ApiParam(name = "pkId", value = "夹具Id", required = false) @RequestParam(required = false) Long pkId) throws BusinessException {
		log.debug("FixtureRepairController.fixtureRepairDelete<<<");

		// 获取当前用户
		Long userId = getAuthentication();
		return new AjaxReturn(200, null, fixtureRepairService.deleteById(userId, pkId));
	}
	
	
	
	@ApiOperation(value = "修磨记录导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "修磨记录导出", response = FixtureRepair.class) })
	@Secure()
	@GetMapping(path = { "/fixture-repair-export" })
	public ResponseEntity<byte[]> fixtureRepairExport(@ApiParam(name = "fixtureBarcode", value = "物料条码", required = false) @RequestParam(required = false) String fixtureBarcode,
			@ApiParam(name = "fixtureName", value = "物料名称", required = false) @RequestParam(required = false) String fixtureName,
			@ApiParam(name = "fixtureMap", value = "物料图号", required = false) @RequestParam(required = false) String fixtureMap) throws Exception {
		Long userId = getAuthentication();
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(50000);

		FixtureRepair where = new FixtureRepair();
		where.setFixtureBarcode(fixtureBarcode);
		where.setFixtureMap(fixtureMap);
		where.setFixtureName(fixtureName);
		Pagination<FixtureRepair> pagination = fixtureRepairService.selectPageList(userId, where, queryDto);
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray((List<FixtureRepair>) pagination.getRows(), FixtureRepair.class, true, null, true);
		return getResponseEntity(data, "夹具修磨记录.xlsx");
	}
	

}
