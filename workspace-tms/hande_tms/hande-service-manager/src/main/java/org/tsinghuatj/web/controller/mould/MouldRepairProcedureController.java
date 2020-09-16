package org.tsinghuatj.web.controller.mould;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.mould.domain.MouldRepairProcedure;
import org.tsinghuatj.mould.service.IMouldRepairProcedureService;
import org.tsinghuatj.support.annotation.OperateLog;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/mould" })
public class MouldRepairProcedureController extends BaseController{

	private @Autowired(required = false) IMouldRepairProcedureService repairProcedureService;
	
	/**
	 * 查询修磨工序分页列表 
	 */
	@ApiOperation(value = "查询修磨工序分页列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询修磨工序分页列表查询成功", response = MouldRepairProcedure.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-repair-procedure-page-list")
	public AjaxReturn repairProcedurePageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, 
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "procedureName", value = "工序名称",required = false) @RequestParam(required = false)String procedureName
			)throws BusinessException {
		log.debug("MouldRepairProcedureController.repairProcedurePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("procedureName:" + procedureName);
		}
		
		//获取当前用户
		Long userId = getAuthentication();
		//参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		
		MouldRepairProcedure repairProcedure = new MouldRepairProcedure();
		repairProcedure.setProcedureName(procedureName);
		
		Pagination<MouldRepairProcedure> pagination = repairProcedureService.selectPageList(userId, repairProcedure, queryDto);
		log.debug("MouldRepairProcedureController.repairProcedurePageList>>>");
		return pagination;
	}
	
	/**
	 * 查询修磨工序分页列表 
	 */
	@ApiOperation(value = "查询修磨工序列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询修磨工序分页列表查询成功", response = MouldRepairProcedure.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-repair-procedure-list")
	public AjaxReturn repairProcedureList(
			)throws BusinessException {
		log.debug("MouldRepairProcedureController.repairProcedureList<<<");
		if (log.isDebugEnabled()) {
		}
		
		//获取当前用户
		Long userId = getAuthentication();
		// 封装查询条件
		MouldRepairProcedure repairProcedure = new MouldRepairProcedure();
		
		log.debug("MouldRepairProcedureController.repairProcedureList>>>");
		return new AjaxReturn(200, null, repairProcedureService.select(userId, repairProcedure));
	}
	
	/**
	 * 模具修磨工序新增
	 */
	@ApiOperation(value = "模具修磨工序新增", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具修磨工序新增成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-repair-procedure-add", produces = "application/json;charset=UTF-8")
	@OperateLog(info = "模具修磨工序新增[工序名称:%s]", params = { "procedureName" })
	public AjaxReturn mouldRepairProcedureAdd(
			@ApiParam(name = "procedureName", value = "工序名称", required = true) @RequestParam(required = true) String procedureName,
			@ApiParam(name = "procedureDesc", value = "工序描述", required = false) @RequestParam(required = false) String procedureDesc) throws BusinessException {
		log.debug("MouldRepairProcedureController.mouldRepairProcedureAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("procedureName:" + procedureName);
			log.debug("procedureDesc:" + procedureDesc);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		Validate.isTrue(userId >= 0);

		MouldRepairProcedure repairProcedure = new MouldRepairProcedure();
		repairProcedure.setProcedureName(procedureName);
		repairProcedure.setProcedureDesc(procedureDesc);
		log.debug("MouldRepairProcedureController.mouldRepairProcedureAdd>>>");
		return new AjaxReturn(200, null, repairProcedureService.insert(userId, repairProcedure));
	}
	
	/**
	 * 模具修磨工序修改
	 */
	@ApiOperation(value = "模具修磨工序修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具修磨工序修改成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-repair-procedure-update", produces = "application/json;charset=UTF-8")
	@OperateLog(info = "模具修磨工序修改[工序名称:%s]", params = { "procedureName" })
	public AjaxReturn mouldRepairProcedureUpdate(
			@ApiParam(name = "pkId", value = "工序名称", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "procedureName", value = "工序名称", required = true) @RequestParam(required = true) String procedureName,
			@ApiParam(name = "procedureDesc", value = "工序描述", required = false) @RequestParam(required = false) String procedureDesc) throws BusinessException {
		log.debug("MouldRepairProcedureController.mouldRepairProcedureUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("procedureName:" + procedureName);
			log.debug("procedureDesc:" + procedureDesc);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		Validate.isTrue(userId >= 0);

		MouldRepairProcedure repairProcedure = new MouldRepairProcedure();
		repairProcedure.setProcedureName(procedureName);
		repairProcedure.setProcedureDesc(procedureDesc);
		log.debug("MouldRepairProcedureController.mouldRepairProcedureUpdate>>>");
		return new AjaxReturn(200, null, repairProcedureService.updateActiveById(userId, repairProcedure, pkId));
	}
	
	
	/**
	 * 模具修磨工序删除
	 */
	@ApiOperation(value = "模具修磨工序删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具修磨工序删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-repair-procedure-delete-by-id")
	@OperateLog(info = "模具修磨工序删除[工序名称:%s]", params = { "procedureName" })
	public AjaxReturn toolPartDeleteById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "procedureName", value = "procedureName", required = false) @RequestParam(required = false) String procedureName
			) throws BusinessException {
		log.debug("PartController.toolPartDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId > 0);

		log.debug("PartController.toolPartDeleteById>>>");
		return new AjaxReturn(200, null, repairProcedureService.deleteById(userId, pkId));
	}
	
	/**
	 * 模具修磨工序表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "模具修磨工序表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具修磨工序表导出", response = MouldRepairProcedure.class) })
	// @Secure()
	@GetMapping(path = { "/mould-repair-procedure-export" })
	@OperateLog(info = "模具修磨工序导出[%s]", params = { "" })
	public ResponseEntity<byte[]> downloadmouldBaseExcel() throws Exception {
		Long userId = getAuthentication();
		List<MouldRepairProcedure> excelVOList = repairProcedureService.select(userId, new MouldRepairProcedure());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, MouldRepairProcedure.class, true, null, true);
		return getResponseEntity(data, "模具修磨工序表.xlsx");
	}
}
