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
import org.tsinghuatj.tool.domain.ToolUnusual;
import org.tsinghuatj.tool.service.IToolUnusualService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolUnusualController  extends BaseController{
	
	private @Autowired(required = false) IToolUnusualService toolUnusualService;
	
	 /**
	  * 刀具异常报告添加
	  */
	@ApiOperation(value = "刀具异常报告添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "添加刀具异常报告成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-unusual-add")
	@OperateLog(info = "刀具异常报告[刀具条码:%s->异常时间:%s->处理措施:%s]", params = { "fullNumber","unusualTime","measures" })
	public AjaxReturn toolUnusualAdd(
			@ApiParam(name = "toolId", value = "刀具Id",required = true) @RequestParam(required = true)Long toolId,
			@ApiParam(name = "toolName", value = "刀具名称",required = false) @RequestParam(required = false)String toolName,
			@ApiParam(name = "toolNumber", value = "刀具编码",required = false) @RequestParam(required = false)String toolNumber,
			@ApiParam(name = "fullNumber", value = "物料条码",required = false) @RequestParam(required = false)String fullNumber,
			@ApiParam(name = "partId", value = "制件ID",required = false) @RequestParam(required = false)Long partId,
			@ApiParam(name = "partName", value = "制件名称",required = false) @RequestParam(required = false,defaultValue = "")String partName,
			@ApiParam(name = "equipmentId", value = "设备ID",required = false) @RequestParam(required = false,defaultValue = "")Long equipmentId,
			@ApiParam(name = "equipmentName", value = "设备名称",required = false) @RequestParam(required = false,defaultValue = "")String equipmentName,
			@ApiParam(name = "unusualResion", value = "异常原因（手动填写）",required = false) @RequestParam(required = false,defaultValue = "")String unusualResion,
			@ApiParam(name = "measures", value = "处理措施",required = false) @RequestParam(required = false,defaultValue = "")Integer measures,
			@ApiParam(name = "remark", value = "备注",required = false) @RequestParam(required = false,defaultValue = "")String remark
			
			)throws BusinessException {
			log.debug("ToolUnusualController.toolUnusualAdd<<<");
			if (log.isDebugEnabled()) {	
				log.debug("toolId:" + toolId);
				log.debug("toolNumber:" + toolNumber);
				log.debug("toolName:" + toolName);
				log.debug("partId:" + partId);
				log.debug("partName:" + partName);
				log.debug("equipmentId:" + equipmentId);
				log.debug("partName:" + partName);
				log.debug("equipmentName:" + equipmentName);
				log.debug("equipmentId:" + equipmentId);
				log.debug("unusualResion:" + unusualResion);
				log.debug("measures:" + measures);
				log.debug("remark:" + remark);
			}
			//获取当前用户
			Long userId = getAuthentication();
			
			//封装参数信息
			ToolUnusual toolUnusual = new ToolUnusual();
			toolUnusual.setToolNumber(toolNumber);
			toolUnusual.setToolId(toolId);
			toolUnusual.setToolName(toolName);
			toolUnusual.setFullNumber(fullNumber);
			toolUnusual.setPartId(partId);
			toolUnusual.setPartName(partName);
			toolUnusual.setEquipmentId(equipmentId);
			toolUnusual.setEquipmentName(equipmentName);
			toolUnusual.setUnusualResion(unusualResion);
			toolUnusual.setUnusualTime(new Date());
			toolUnusual.setMeasures(measures);
			toolUnusual.setRemark(remark);
			
           log.debug("ToolUnusualController.toolUnusualAdd>>>");
			
			return new AjaxReturn(200,null,toolUnusualService.insert(userId, toolUnusual));
	}
	
	/**
	 * 刀具删除
	 */
	@ApiOperation(value = "刀具删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-unusual-delete-by-id")
	@OperateLog(info = "刀具异常报告删除[异常报告Id:%s]", params = { "pkId" })
	public AjaxReturn unusualDeleteById(@ApiParam(name = "pkId", value = "主键",required = true) @RequestParam(required = true) Long pkId)throws BusinessException {
		log.debug("ToolUnusualController.unusualDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		
		//获取当前用户
		Long userId = getAuthentication();
		//参数校验
		
		Validate.isTrue(pkId > 0);
		
		log.debug("ToolUnusualController.unusualDeleteById>>>");
		return new AjaxReturn(200,null,toolUnusualService.deleteById(userId, pkId));
	}
	
	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "刀具异常报告id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具异常报告Id查询列表查询成功", response = ToolUnusual.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-unusual-get-by-id")
	public AjaxReturn toolUnusualGetById(@ApiParam(name = "pkId", value = "pkId",required = true) @RequestParam(required = true) Long pkId)throws BusinessException {
		log.debug("ToolUnusualController.toolGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		
		//获取当前用户
		Long curuserId = getAuthentication();
		//参数校验
		
		Validate.isTrue(pkId >= 1);
		
		log.debug("ToolUnusualController.toolGetById>>>");
		return new AjaxReturn(200,null,toolUnusualService.selectById(curuserId, pkId));
	}
	/**
	 * 查询刀具异常报告分页列表 
	 */
	@ApiOperation(value = "查询刀具异常报告分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具异常报告分页列表查询成功", response = ToolUnusual.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-unusual-page-list")
	public AjaxReturn toolUnusualPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "toolNumber", value = "刀具编码",required = false) @RequestParam(required = false,defaultValue = "") String toolNumber,
			@ApiParam(name = "fullNumber", value = "刀具编码",required = false) @RequestParam(required = false,defaultValue = "") String fullNumber,
			@ApiParam(name = "beginDate", value = "开始日期",required = false) @RequestParam(required = false,defaultValue = "") Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期",required = false) @RequestParam(required = false,defaultValue = "") Date endDate)
			throws BusinessException {
		log.debug("ToolUnusualController.toolUnusualPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("toolNumber:" + toolNumber);
			log.debug("fullNumber:" + fullNumber);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
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
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);
		
		ToolUnusual toolUnusual = new ToolUnusual();
		toolUnusual.setToolNumber(toolNumber);
		toolUnusual.setFullNumber(fullNumber);
		
		Pagination<ToolUnusual> pagination = toolUnusualService.selectPageList(curuserId, toolUnusual, queryDto);
		log.debug("ToolUnusualController.toolUnusualPageList>>>");
		return pagination;
	}
	
	/**
	  * 刀具异常报告信息修改
	  */
	@ApiOperation(value = "刀具异常报告信息修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具异常报告信息修改成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-unusual-update")
	@OperateLog(info = "刀具异常报告修改[刀具条码:%s->异常原因:%s->处理措施:%s]", params = { "fullNumber","unusualResion","measures" })
	public AjaxReturn toolUnusualUpdate(
			@ApiParam(name = "pkId", value = "刀具编码",required = true) @RequestParam(required = true)Long pkId,
			@ApiParam(name = "toolId", value = "刀具Id",required = false) @RequestParam(required = false,defaultValue = "")Long toolId,
			@ApiParam(name = "toolName", value = "刀具名称",required = false) @RequestParam(required = false,defaultValue = "")String toolName,
			@ApiParam(name = "toolNumber", value = "刀具编码",required = false) @RequestParam(required = false,defaultValue = "")String toolNumber,
			@ApiParam(name = "fullNumber", value = "物料条码",required = false) @RequestParam(required = false,defaultValue = "")String fullNumber,
			@ApiParam(name = "partId", value = "制件ID",required = true) @RequestParam(required = true)Long partId,
			@ApiParam(name = "partName", value = "制件名称",required = false) @RequestParam(required = false,defaultValue = "")String partName,
			@ApiParam(name = "equipmentId", value = "设备ID",required = false) @RequestParam(required = false,defaultValue = "")Long equipmentId,
			@ApiParam(name = "equipmentName", value = "设备名称",required = false) @RequestParam(required = false,defaultValue = "")String equipmentName,
			@ApiParam(name = "unusualResion", value = "异常原因（手动填写）",required = false) @RequestParam(required = false,defaultValue = "")String unusualResion,
			@ApiParam(name = "measures", value = "处理措施",required = false) @RequestParam(required = false,defaultValue = "")Integer measures,
			@ApiParam(name = "remark", value = "备注",required = false) @RequestParam(required = false,defaultValue = "")String remark
			
			)throws BusinessException {
			log.debug("ToolUnusualController.toolUnusualUpdate<<<");
			if (log.isDebugEnabled()) {
				log.debug("pkId:" + pkId);
				log.debug("toolId:" + toolId);
				log.debug("toolNumber:" + toolNumber);
				log.debug("toolName:" + toolName);
				log.debug("partId:" + partId);
				log.debug("partName:" + partName);
				log.debug("equipmentId:" + equipmentId);
				log.debug("partName:" + partName);
				log.debug("equipmentName:" + equipmentName);
				log.debug("equipmentId:" + equipmentId);
				log.debug("unusualResion:" + unusualResion);
				log.debug("measures:" + measures);
				log.debug("remark:" + remark);
			}
			//获取当前用户
			Long userId = getAuthentication();
			
			//封装参数信息
			ToolUnusual toolUnusual = new ToolUnusual();
			toolUnusual.setToolNumber(toolNumber);
			toolUnusual.setFullNumber(fullNumber);
			toolUnusual.setToolId(toolId);
			toolUnusual.setToolName(toolName);
			toolUnusual.setPartId(partId);
			toolUnusual.setPartName(partName);
			toolUnusual.setEquipmentId(equipmentId);
			toolUnusual.setEquipmentName(equipmentName);
			toolUnusual.setUnusualResion(unusualResion);
			toolUnusual.setMeasures(measures);
			toolUnusual.setRemark(remark);
			
           log.debug("ToolUnusualController.toolUnusualUpdate>>>");

			return new AjaxReturn(200,null,toolUnusualService.updateActiveById(userId, toolUnusual, pkId));
	}
}
