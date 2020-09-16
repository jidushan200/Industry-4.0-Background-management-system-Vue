package org.tsinghuatj.web.controller.mould;

import java.util.Date;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.mould.domain.Mould;
import org.tsinghuatj.mould.domain.MouldProcess;
import org.tsinghuatj.mould.domain.MouldWarehouse;
import org.tsinghuatj.mould.service.IMouldProcessService;
import org.tsinghuatj.mould.service.IMouldService;
import org.tsinghuatj.mould.service.IMouldWarehouseService;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolProcess;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/mould" })
public class MouldProcessController extends BaseController{
	private @Autowired(required = false) IMouldService mouldService;
	private @Autowired(required = false) IMouldProcessService mouldProcessService;
	private @Autowired(required = false) IMouldWarehouseService mouldWarehouseService;
	
	/**
	  * 刀具加工信息添加
	  */
	@ApiOperation(value = "刀具加工信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具加工信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-process-add")
	@OperateLog(info = "模具加工[模具条码:%s->制件名称:%s->加工数量:%s]", params = { "fullNumber","partName","processAmount" })
	public AjaxReturn mouldProcessAdd(
			@ApiParam(name = "mouldId", value = "刀具Id",required = true) @RequestParam(required = true)Long mouldId,
			@ApiParam(name = "fullNumber", value = "物料条码",required = true) @RequestParam(required = true)String fullNumber,
			@ApiParam(name = "mouldNumber", value = "物料编码", required = true) @RequestParam(required = true) String mouldNumber,
			@ApiParam(name = "mouldMap", value = "物料图号", required = false) @RequestParam(required = false) String mouldMap,
			@ApiParam(name = "mouldName", value = "物料名称", required = false) @RequestParam(required = false) String mouldName,
			@ApiParam(name = "warehouse", value = "库位", required = false) @RequestParam(required = false) String warehouse,
			@ApiParam(name = "departmentId", value = "部门ID",required = false) @RequestParam(required = false)Long departmentId,
			@ApiParam(name = "departmentName", value = " 部门名称",required = false) @RequestParam(required = false)String departmentName,
			@ApiParam(name = "partId", value = "绑定设备ID",required = false) @RequestParam(required = false)Long partId,
			@ApiParam(name = "partCode", value = "绑定设备编码",required = false) @RequestParam(required = false)String partCode,
			@ApiParam(name = "partName", value = " 设备名称",required = false) @RequestParam(required = false)String partName,
			@ApiParam(name = "equipmentId", value = "绑定设备ID",required = false) @RequestParam(required = false)Long equipmentId,
			@ApiParam(name = "equipmentCode", value = "绑定设备编码",required = false) @RequestParam(required = false)String equipmentCode,
			@ApiParam(name = "equipmentName", value = " 设备名称",required = false) @RequestParam(required = false)String equipmentName,
			@ApiParam(name = "processAmount", value = " 加工数量",required = true) @RequestParam(required = true)Integer processAmount,
			@ApiParam(name = "returnResion", value = " 交回原因",required = true) @RequestParam(required = true)Integer returnResion,
			@ApiParam(name = "remark", value = " 异常说明",required = false) @RequestParam(required = false)String remark
			)throws BusinessException {
			log.debug("MouldProcessController.mouldProcessAdd<<<");
			if (log.isDebugEnabled()) {
				log.debug("fullNumber:" + fullNumber);
				log.debug("mouldNumber:" + mouldNumber);
				log.debug("mouldMap" + mouldMap);
				log.debug("mouldName:" + mouldName);
				log.debug("departmentId:" + departmentId);
				log.debug("departmentName:" + departmentName);
				log.debug("partId:" + partId);
				log.debug("partCode:" + partCode);
				log.debug("partName:" + partName);
				log.debug("equipmentId:" + equipmentId);
				log.debug("equipmentCode:" + equipmentCode);
				log.debug("equipmentName:" + equipmentName);
				log.debug("processAmount:" + processAmount);
				log.debug("returnResion:" + returnResion);
				log.debug("remark:" + remark);
			}
			//获取当前用户
			CustomUser user = getCompleteAuthentication();
			Date date = new Date();
			
			//封装参数信息
			MouldProcess mouldProcess =new MouldProcess();
			mouldProcess.setMouldId(mouldId);
			mouldProcess.setFullNumber(fullNumber);
			mouldProcess.setMouldNumber(mouldNumber);
			mouldProcess.setMouldMap(mouldMap);
			mouldProcess.setMouldName(mouldName);
			mouldProcess.setDepartmentId(departmentId);
			mouldProcess.setDepartmentName(departmentName);
			mouldProcess.setPartId(partId);
			mouldProcess.setPartCode(partCode);
			mouldProcess.setPartName(partName);
			mouldProcess.setEquipmentId(equipmentId);
			mouldProcess.setEquipmentCode(equipmentCode);
			mouldProcess.setEquipmentName(equipmentName);
			mouldProcess.setEndTime(date);
			mouldProcess.setProcessAmount(processAmount);
			mouldProcess.setReturnResion(returnResion);
			mouldProcess.setRemark(remark);
			
			MouldWarehouse mouldWarehouse = new MouldWarehouse();
			mouldWarehouse.setMouldId(mouldId);
			mouldWarehouse.setMouldNumber(mouldNumber);
			mouldWarehouse.setMouldName(mouldName);
			mouldWarehouse.setMouldMap(mouldMap);
			mouldWarehouse.setFullNumber(fullNumber);
			mouldWarehouse.setKeeperId(user.getId());
			mouldWarehouse.setKeeper(user.getRealname());
			mouldWarehouse.setWarehouse(warehouse);
			mouldWarehouse.setRemark(remark);
			mouldWarehouse.setWarehosingTime(date);
			mouldWarehouse.setInType(2);
			mouldWarehouseService.insert(user.getId(),mouldWarehouse);
			
			log.debug("MouldProcessController.mouldProcessAdd>>>");
			
			return new AjaxReturn(200,null,mouldProcessService.insert(user.getId(), mouldProcess));
	}


	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "刀具加工id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具加工Id查询列表查询成功", response = ToolProcess.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-process-get-by-id")
	public AjaxReturn mouldProcessGetById(@ApiParam(name = "pkId", value = "pkId",required = true) @RequestParam(required = true) Long pkId)throws BusinessException {
		log.debug("MouldProcessController.mouldProcessGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		
		//获取当前用户
		Long curuserId = getAuthentication();
		//参数校验
		
		Validate.isTrue(pkId >= 1);
		
		log.debug("MouldProcessController.mouldProcessGetById>>>");
		return new AjaxReturn(200,null,mouldProcessService.selectById(curuserId, pkId));
	}
	
	/**
	 * 查询刀具加工信息分页列表 
	 */
	@ApiOperation(value = "查询刀具加工信息分页列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀具加工信息分页列表查询成功", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-process-page-list")
	public AjaxReturn mouldProcessPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, 
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "mouldBarcode", value = "模具条码",required = false) @RequestParam(required = false)String mouldBarcode,
			@ApiParam(name = "mouldNumber", value = "模具编码",required = false) @RequestParam(required = false)String mouldNumber,
			@ApiParam(name = "useTeamId", value = "使用班组",required = false) @RequestParam(required = false)Long useTeamId
			)throws BusinessException {
		log.debug("MouldProcessController.mouldProcessPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("mouldNumber:" + mouldNumber);
			log.debug("mouldBarcode:" + mouldBarcode);
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
		
		Mould mould = new Mould();
		mould.setMouldNumber(mouldNumber);
		mould.setMouldBarcode(mouldBarcode);
		mould.setMouldStatus(2);
		mould.setIsList(1);
		mould.setUseTeamId(useTeamId);
		Pagination<Mould> pagination = mouldService.selectPageList(userId, mould, queryDto);
		log.debug("MouldProcessController.mouldProcessPageList>>>");
		return pagination;
	}

}
