package org.tsinghuatj.web.controller.tool;

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
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.ToolOutbound;
import org.tsinghuatj.tool.service.IToolOutboundService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolOutboundController extends BaseController  {

	private @Autowired(required = false) IToolOutboundService toolOutboundService;
	
	/**
	  * 刀具出库记录信息添加
	  */
	@ApiOperation(value = "刀具出库记录信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具出库记录信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-outbound")
	@OperateLog(info = "刀具出库[刀具条码:%s->出库类型:%s->领用人:%s]", params = { "fullNumber","outTypeName","staffCode" })
	public AjaxReturn toolOutboundAdd(
			@ApiParam(name = "toolId", value = "刀具Id",required = true) @RequestParam(required = true)Long toolId,
			@ApiParam(name = "toolNumber", value = "刀具编码",required = true) @RequestParam(required = true)String toolNumber,
			@ApiParam(name = "toolName", value = "刀具名称",required = true) @RequestParam(required = true)String toolName,
			@ApiParam(name = "fullNumber", value = "物料条码",required = true) @RequestParam(required = true)String fullNumber,
			@ApiParam(name = "typeId", value = "物料类型",required = true) @RequestParam(required = true)Integer typeId,
			@ApiParam(name = "toolMap", value = "刀具图号",required = true) @RequestParam(required = true)String toolMap,
			@ApiParam(name = "warehouse", value = "库位",required = false) @RequestParam(required = false)String warehouse,
			@ApiParam(name = "outType", value = "出库类型",required = true) @RequestParam(required = true)Integer outType,
			@ApiParam(name = "departmentId", value = "领用部门ID",required = false) @RequestParam(required = false)Long departmentId,
			@ApiParam(name = "departmentName", value = "领用部门名称",required = false) @RequestParam(required = false)String departmentName,
			@ApiParam(name = "teamId", value = "班组ID",required = false) @RequestParam(required = false)Long teamId,
			@ApiParam(name = "teamName", value = "班组名称",required = false) @RequestParam(required = false)String teamName,
			@ApiParam(name = "staffCode", value = "领用人",required = false) @RequestParam(required = false)String staffCode,
			@ApiParam(name = "staffName", value = "领用人",required = false) @RequestParam(required = false)String staffName,
			@ApiParam(name = "supplierId", value = "供应商ID",required = false) @RequestParam(required = false)Long supplierId,
			@ApiParam(name = "supplierName", value = "供应商",required = false) @RequestParam(required = false)String supplierName,
			@ApiParam(name = "remark", value = "说明",required = false) @RequestParam(required = false)String remark
			)throws BusinessException {
			log.debug("ToolOutboundController.toolOutboundAdd<<<");
			if (log.isDebugEnabled()) {	
				log.debug("toolId:" + toolId);
				log.debug("toolNumber:" + toolNumber);			
				log.debug("toolMap:" + toolMap);
				log.debug("warehouse:" + warehouse);
				log.debug("outType:" + outType);
				log.debug("departmentId:" + departmentId);
				log.debug("departmentName:" + departmentName);
				log.debug("supplierId:" + supplierId);
				log.debug("supplierName:" + supplierName);
				log.debug("remark:" + remark);
			}
			//获取当前用户
			CustomUser user = getCompleteAuthentication();
			
			//封装参数信息
			ToolOutbound toolOutbound = new ToolOutbound();
		    toolOutbound.setToolId(toolId);
		    toolOutbound.setToolNumber(toolNumber);
		    toolOutbound.setToolName(toolName);
		    toolOutbound.setToolMap(toolMap);
		    toolOutbound.setWarehouse(warehouse);
		    toolOutbound.setOutType(outType);
		    toolOutbound.setDepartmentId(departmentId);
		    toolOutbound.setDepartmentName(departmentName);
		    toolOutbound.setTeamId(teamId);
		    toolOutbound.setTeamName(teamName);
		    toolOutbound.setReceiveTime(new Date());
		    toolOutbound.setStaffCode(staffCode);
		    toolOutbound.setStaffName(staffName);
		    toolOutbound.setToolAmount(1);
		    if(outType==3){
		    	toolOutbound.setSupplierId(supplierId);
		    	toolOutbound.setSupplierName(supplierName);
		    }
		  
		    toolOutbound.setKeeperId(user.getId());
		    toolOutbound.setKeeper(user.getRealname());
		    toolOutbound.setRemark(remark);
		    toolOutbound.setFullNumber(fullNumber);
			log.debug("ToolOutboundController.toolOutboundAdd>>>");
			
			return new AjaxReturn(200,null,toolOutboundService.insert(user.getId(), toolOutbound,typeId));
	}
	
	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "刀具出库id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具出库Id查询列表查询成功", response = ToolOutbound.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-outbound-get-by-tool-id")
	public AjaxReturn toolOutboundGetByToolId(@ApiParam(name = "toolId", value = "toolId",required = true) @RequestParam(required = true) Long toolId)throws BusinessException {
		log.debug("ToolOutboundController.toolOutboundGetByToolId<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolId:" + toolId);
		}
		
		//获取当前用户
		Long curuserId = getAuthentication();
		//参数校验
		
		
		log.debug("ToolOutboundController.toolOutboundGetByToolId>>>");
		return new AjaxReturn(200,null,toolOutboundService.selectRowByToolId(curuserId, toolId));
	}
	
	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "刀具出库id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具出库Id查询列表查询成功", response = ToolOutbound.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-outbound-get-by-id")
	public AjaxReturn toolOutboundGetById(@ApiParam(name = "pkId", value = "pkId",required = true) @RequestParam(required = true) Long pkId)throws BusinessException {
		log.debug("ToolOutboundController.toolOutboundGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		
		//获取当前用户
		Long curuserId = getAuthentication();
		//参数校验
		
		Validate.isTrue(pkId >= 1);
		
		log.debug("ToolOutboundController.toolOutboundGetById>>>");
		return new AjaxReturn(200,null,toolOutboundService.selectById(curuserId, pkId));
	}
	
	/**
	 * 查询刀具出库记录分页列表 
	 */
	@ApiOperation(value = "查询刀具出库记录分页列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具出库记录分页列表查询成功", response = ToolOutbound.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-outbound-page-list")
	public AjaxReturn toolOutboundPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, 
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "toolId", value = "刀具Id",required = false) @RequestParam(required = false,defaultValue = "")Long toolId,
			@ApiParam(name = "toolName", value = "刀具名称",required = false) @RequestParam(required = false,defaultValue = "")String toolName,
			@ApiParam(name = "toolNumber", value = "刀具编码",required = false) @RequestParam(required = false,defaultValue = "")String toolNumber,
			@ApiParam(name = "keeper", value = "库管员",required = false) @RequestParam(required = false)String keeper
			)throws BusinessException {
		log.debug("ToolOutboundController.toolOutboundPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolId:" + toolId);
			log.debug("toolName:" + toolName);
			log.debug("toolNumber:" + toolNumber);		
			log.debug("keeper:" + keeper);
		}
		
		//获取当前用户
		Long curuserId = getAuthentication();
		//参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		
		ToolOutbound toolOutbound = new ToolOutbound();
		toolOutbound.setToolId(toolId);
		toolOutbound.setToolName(toolName);
		toolOutbound.setToolNumber(toolNumber);		
		toolOutbound.setKeeper(keeper);
		
		Pagination<ToolOutbound> pagination = toolOutboundService.selectPageList(curuserId, toolOutbound, queryDto);
		log.debug("ToolOutboundController.toolOutboundPageList>>>");
		return pagination;
	}
}
