package org.tsinghuatj.web.controller.tool;

import java.util.Date;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.ToolWarehouse;
import org.tsinghuatj.tool.service.IToolWarehouseService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolWarehouseController extends BaseController{
	
	private @Autowired(required = false) IToolWarehouseService toolWarehouseService;
	
	/*
	 *添加入库
	 */
	@ApiOperation(value = "添加入库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "添加入库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/warehouse-add")
	@OperateLog(info = "刀具入库[刀具编码:%s->库管员:%s]", params = { "toolNumber","keeper" })
	public AjaxReturn wareHouseAdd(
			@ApiParam(name = "toolId", value = "刀具Id",required = true) @RequestParam(required = true)Long toolId,
			@ApiParam(name = "toolName", value = "刀具名称",required = true) @RequestParam(required = true)String toolName,
			@ApiParam(name = "toolNumber", value = "刀具编码",required = true) @RequestParam(required = true)String toolNumber,
			@ApiParam(name = "inType", value = "入库类型(0-新刀入库；1-用后返库；2-刃磨返库)",required = true) @RequestParam(required = true)Integer inType,
			@ApiParam(name = "keeper", value = "库管员",required = true) @RequestParam(required = true)String keeper,
			@ApiParam(name = "remark", value = "说明",required = false) @RequestParam(required = false,defaultValue = "")String remark,
			@ApiParam(name = "deliever", value = "送货人",required = false) @RequestParam(required = false,defaultValue = "")String deliever,
			@ApiParam(name = "warehosingTime", value = "入库时间",required = true) @RequestParam(required = true)Date warehosingTime
			)throws BusinessException {
			log.debug("ToolWarehouseController.wareHouseAdd<<<");
			if (log.isDebugEnabled()) {			
				log.debug("toolId:" + toolId);
				log.debug("toolName:" + toolName);
				log.debug("toolNumber:" + toolNumber);
				log.debug("inType:" + inType);
				log.debug("keeper:" + keeper);
				log.debug("remark:" + remark);
				log.debug("deliever:" + deliever);
				log.debug("warehosingTime:" + warehosingTime);
			}
			
			//获取当前用户
			Long userId = getAuthentication();
			
			//封装参数信息
			ToolWarehouse toolWarehouse = new ToolWarehouse();
			toolWarehouse.setToolId(toolId);
			toolWarehouse.setToolName(toolName);
			toolWarehouse.setToolNumber(toolNumber);
            toolWarehouse.setInType(inType);
            toolWarehouse.setKeeper(keeper);
            toolWarehouse.setRemark(remark);
            toolWarehouse.setDeliever(deliever);
            toolWarehouse.setWarehosingTime(warehosingTime);
            log.debug("ToolWarehouseController.wareHouseAdd>>>");
			
			toolWarehouseService.insert(userId, toolWarehouse);
			return new AjaxReturn(200,null,toolWarehouse.getPkId());
	}
	
	/**
	 * 查询刀具入库记录分页列表 
	 */
	@ApiOperation(value = "查询刀具入库记录分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具入库记录分页列表查询成功", response = ToolWarehouse.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-warehouse-page-list")
	public AjaxReturn toolWarehousePageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "toolNumber", value = "刀具编码",required = false) @RequestParam(required = false,defaultValue = "") String toolNumber,
			@ApiParam(name = "inType", value = "刀具类型",required = false) @RequestParam(required = false,defaultValue = "") Integer inType,
			@ApiParam(name = "toolName", value = "刀具名称",required = false) @RequestParam(required = false,defaultValue = "") String toolName)
			throws BusinessException {
		log.debug("ToolWarehouseController.toolWarehousePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("toolNumber:" + toolNumber);
			log.debug("inType:" + inType);
			log.debug("toolName:" + toolName);
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
		
		ToolWarehouse toolWarehouse = new ToolWarehouse();
		toolWarehouse.setToolNumber(toolNumber);
		toolWarehouse.setInType(inType);
		toolWarehouse.setToolName(toolName);
		
		Pagination<ToolWarehouse> pagination = toolWarehouseService.selectPageList(curuserId, toolWarehouse, queryDto);
		log.debug("ToolWarehouseController.toolWarehousePageList>>>");
		return pagination;
	}
	
}
