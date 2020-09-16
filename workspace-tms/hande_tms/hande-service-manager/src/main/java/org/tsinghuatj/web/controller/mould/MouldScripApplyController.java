package org.tsinghuatj.web.controller.mould;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.tsinghuatj.mould.domain.MouldScripApply;
import org.tsinghuatj.mould.service.IMouldScripApplyService;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.ToolApplyAudit;
import org.tsinghuatj.tool.domain.ToolScripApply;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/mould" })
public class MouldScripApplyController extends BaseController{

	private @Autowired(required = false) IMouldScripApplyService mouldScripApplyService;
	
	/**
	  * 模具报废表信息添加
	  */
	@ApiOperation(value = "模具报废表信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具报废表信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-scrip-apply-add")
	@OperateLog(info = "模具报废申请添加[物料条码:%s]", params = { "fullNumber" })
	public AjaxReturn mouldScripApplyAdd(
			@ApiParam(name = "mouldNumber", value = "模具编码",required = true) @RequestParam(required = true)String mouldNumber,
			@ApiParam(name = "mouldMap", value = "模具图号",required = false) @RequestParam(required = false)String mouldMap,
			@ApiParam(name = "fullNumber", value = "物料条码",required = false) @RequestParam(required = false)String fullNumber,
			@ApiParam(name = "mouldName", value = "模具名称",required = false) @RequestParam(required = false)String mouldName,
			@ApiParam(name = "applierCode", value = "申请人编码",required = false) @RequestParam(required = false)String applierCode,
			@ApiParam(name = "departmentId", value = "申请部门Id",required = false) @RequestParam(required = false)Long departmentId,
			@ApiParam(name = "departmentName", value = "申请部门",required = false) @RequestParam(required = false)String departmentName,
			@ApiParam(name = "scripRemark", value = "报废原因说明",required = false) @RequestParam(required = false)String scripRemark,
			@ApiParam(name = "applyStatus", value = "报废申请状态",required = false) @RequestParam(required = false)Integer applyStatus,
			@ApiParam(name = "scripResion", value = "报废原因",required = false) @RequestParam(required = false)Integer scripResion
			)throws BusinessException {
			log.debug("MouldScripApplyController.mouldScripApplyAdd<<<");
			if (log.isDebugEnabled()) {	
				log.debug("mouldNumber:" + mouldNumber);
				log.debug("mouldMap:" + mouldMap);
				log.debug("fullNumber:" + fullNumber);
				log.debug("mouldName:" + mouldName);
				log.debug("departmentId:" + departmentId);
				log.debug("departmentName:" + departmentName);
				log.debug("scripResion:" + scripResion);
			
			}
			//获取当前用户
			CustomUser user = getCompleteAuthentication();
			
			mouldScripApplyService.fullNumberCheck(fullNumber,null);
			
			//封装参数信息
			MouldScripApply mouldScripApply = new MouldScripApply();
			mouldScripApply.setMouldNumber(mouldNumber);
			mouldScripApply.setMouldMap(mouldMap);
			mouldScripApply.setFullNumber(fullNumber);
			mouldScripApply.setMouldName(mouldName);
			mouldScripApply.setApplierId(user.getId());
			mouldScripApply.setApplierCode(applierCode);
			mouldScripApply.setApplierName(user.getRealname());
			mouldScripApply.setDepartmentId(departmentId);
			mouldScripApply.setDepartmentName(departmentName);
			mouldScripApply.setApplyTime(new Date());
			mouldScripApply.setApplyStatus(applyStatus);
			mouldScripApply.setScripResion(scripResion);
			mouldScripApply.setScripRemark(scripRemark);
			
			log.debug("MouldScripApplyController.mouldScripApplyAdd>>>");
			
			return new AjaxReturn(200,null,mouldScripApplyService.insert(user.getId(), mouldScripApply));
	}
  
	/**
	 * 模具报废表信息删除
	 */
	@ApiOperation(value = "模具报废表信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具报废表信息删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-scrip-apply-delete-by-id")
	@OperateLog(info = "模具报废申请删除[模具条码:%s]", params = { "fullNumber" })
	public AjaxReturn mouldScripApplyDeleteById(@ApiParam(name = "pkId", value = "主键",required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "fullNumber", value = "fullNumber",required = true) @RequestParam(required = true) String fullNumber
			)throws BusinessException {
		log.debug("MouldScripApplyController.mouldScripApplyDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		
		//获取当前用户
		Long userId = getAuthentication();
		//参数校验
		
		Validate.isTrue(pkId > 0);
		
		log.debug("MouldScripApplyController.mouldScripApplyDeleteById>>>");
		return new AjaxReturn(200,null,mouldScripApplyService.deleteById(userId, pkId));
	}
	
	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "模具报废表id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具报废表Id查询列表查询成功", response = ToolScripApply.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-scrip-apply-get-by-id")
	public AjaxReturn mouldScripApplyGetById(@ApiParam(name = "pkId", value = "pkId",required = true) @RequestParam(required = true) Long pkId)throws BusinessException {
		log.debug("MouldScripApplyController.mouldScripApplyGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		
		//获取当前用户
		Long curuserId = getAuthentication();
		//参数校验
		
		Validate.isTrue(pkId >= 1);
		
		log.debug("MouldScripApplyController.mouldScripApplyGetById>>>");
		return new AjaxReturn(200,null,mouldScripApplyService.selectById(curuserId, pkId));
	}
	
//	/**
//	 * 根据fullNumber查找
//	 */
//	@ApiOperation(value = "模具刃磨id查询列表", code = 200, produces = "application/json", notes = "")
//	@ApiResponses({ @ApiResponse(code = 200, message = "模具刃磨Id查询列表查询成功", response = ToolRepair.class) })
//	@RequestMapping(method = RequestMethod.POST, value = "/apply-get-by-full")
//	public AjaxReturn applyGetByfullNumber(@ApiParam(name = "fullNumber", value = "pkId",required = true) @RequestParam(required = true) String fullNumber)throws BusinessException {
//		log.debug("ToolRepairController.applyGetByfullNumber<<<");
//		if (log.isDebugEnabled()) {
//			log.debug("fullNumber:" + fullNumber);
//		}
//		
//		//获取当前用户
//		Long curuserId = getAuthentication();
//		//参数校验
//		
//		log.debug("ToolRepairController.applyGetByfullNumber>>>");
//		return new AjaxReturn(200,null,mouldScripApplyService.applyGetByfullNumber(curuserId, fullNumber));
//	}
//	
	/**
	 * 查询模具报废表信息分页列表 
	 */
	@ApiOperation(value = "查询模具报废表信息分页列表  ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询模具报废表信息分页列表 查询成功", response = ToolScripApply.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-scrip-apply-page-list")
	public AjaxReturn mouldScripApplyPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, 
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "fullNumber", value = "物料条码",required = false) @RequestParam(required = false)String fullNumber,
			@ApiParam(name = "scripResion", value = "报废原因",required = false) @RequestParam(required = false)Integer scripResion,
			@ApiParam(name = "applierId", value = "申请人", required = false) @RequestParam(required = false) Long applierId,
			@ApiParam(name = "applyStatusList", value = "报废状态列表", required = false) @RequestParam(required = false) String applyStatusList,
			@ApiParam(name = "beginDate", value = "开始日期",required = false) @RequestParam(required = false,defaultValue = "") Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期",required = false) @RequestParam(required = false,defaultValue = "") Date endDate,
			@ApiParam(name = "mouldNumber", value = "模具编码",required = false) @RequestParam(required = false)String mouldNumber
			)throws BusinessException {
		log.debug("MouldScripApplyController.mouldScripApplyPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
		}
		
		//获取当前用户
		CustomUser user = getCompleteAuthentication();
		//参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);
		
		MouldScripApply mouldScripApply = new MouldScripApply();
		mouldScripApply.setMouldNumber(mouldNumber);
		mouldScripApply.setFullNumber(fullNumber);
		mouldScripApply.setScripResion(scripResion);
		if(applierId != null){
			mouldScripApply.setApplierId(applierId);
		}
		
	    
		Integer id;
		List<Integer> statusGroup = new ArrayList<Integer>();
		//判断状态列表是否为空，如果是空，前端访问的是模具采购部分，未传递状态列表。如果非空，访问的为申购部分，按权限赋值，拆分并放入list便于查询
		if(applyStatusList != null && applyStatusList != ""){
			String[] arr = applyStatusList.split(",");
			for (String str : arr) {
				id = Integer.parseInt(str);
				statusGroup.add(id);
			}
		}
		mouldScripApply.setStatusList(statusGroup);
		
		Pagination<MouldScripApply> pagination = mouldScripApplyService.selectPageList(user.getId(), mouldScripApply, queryDto);
		log.debug("MouldScripApplyController.mouldScripApplyPageList>>>");
		return pagination;
	}
	
	/**
	 * 报废报告审核
	 */
	@ApiOperation(value = "报废报告审核", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "报废报告审核", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/scrip-report-audit")
	@OperateLog(info = "模具报废申请审核[模具条码:%s]", params = { "fullNumber" })
	public AjaxReturn scripReportAduit(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "fullNumber", value = "fullNumber",required = true) @RequestParam(required = true)String fullNumber,
			@ApiParam(name = "applyStatus", value = "审核状态", required = true) @RequestParam(required = true) Integer applyStatus,			
			@ApiParam(name = "auitDepartmentId", value = "审核部门Id",required = true) @RequestParam(required = true)Long auitDepartmentId,
			@ApiParam(name = "auitDepartmentName", value = "审核部门",required = true) @RequestParam(required = true)String auitDepartmentName,
			@ApiParam(name = "disagreeRemark", value = "审核备注", required = false) @RequestParam(required = false) String disagreeRemark)
			throws BusinessException {
		log.debug("MouldScripApplyController.scripReportAduit<<<");
		if (log.isDebugEnabled()) {
			log.debug("applyId:" + pkId);
			log.debug("applyStatus:" + applyStatus);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		Validate.isTrue(user.getId() > 0);
		// 封装参数信息
		MouldScripApply mouldScripApply =mouldScripApplyService.selectById(user.getId(), pkId);
		mouldScripApply.setApplyStatus(applyStatus);
		
		ToolApplyAudit applyAudit = new ToolApplyAudit();
		applyAudit.setAuditorId(user.getId());
		applyAudit.setAuditorName(user.getRealname());
		applyAudit.setAuditDepartmentId(auitDepartmentId);
		applyAudit.setAuditDepartmentName(auitDepartmentName);
		applyAudit.setRemark(disagreeRemark);
		
		log.debug("MouldScripApplyController.scripReportAduit>>>");		
		return new AjaxReturn(200, null, mouldScripApplyService.reportAudit(user.getId(),user.getRealname(), applyAudit, mouldScripApply));
	}
	
	/**
	  * 模具报废表信息更新
	  */
	@ApiOperation(value = "模具报废表信息更新", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具报废表信息更新成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-scrip-apply-update")
	@OperateLog(info = "模具报废申请更新[模具条码:%s]", params = { "fullNumber" })
	public AjaxReturn mouldScripApplyUpdate(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "mouldNumber", value = "模具编码",required = true) @RequestParam(required = true)String mouldNumber,
			@ApiParam(name = "mouldMap", value = "模具图号",required = false) @RequestParam(required = false)String mouldMap,
			@ApiParam(name = "fullNumber", value = "物料条码",required = false) @RequestParam(required = false)String fullNumber,
			@ApiParam(name = "mouldName", value = "模具名称",required = false) @RequestParam(required = false)String mouldName,
			@ApiParam(name = "applierCode", value = "申请人编码",required = false) @RequestParam(required = false)String applierCode,
			@ApiParam(name = "departmentId", value = "申请部门Id",required = false) @RequestParam(required = false)Long departmentId,
			@ApiParam(name = "departmentName", value = "申请部门",required = false) @RequestParam(required = false)String departmentName,
			@ApiParam(name = "scripRemark", value = "报废原因说明",required = false) @RequestParam(required = false)String scripRemark,
			@ApiParam(name = "scripResion", value = "报废原因",required = false) @RequestParam(required = false)Integer scripResion,
			@ApiParam(name = "applyStatus", value = "审核状态", required = true) @RequestParam(required = true) Integer applyStatus
			
			)throws BusinessException {
			log.debug("MouldScripApplyController.mouldScripApplyUpdate<<<");
			if (log.isDebugEnabled()) {	
				log.debug("mouldNumber:" + mouldNumber);
				log.debug("mouldMap:" + mouldMap);
				log.debug("fullNumber:" + fullNumber);
				log.debug("mouldName:" + mouldName);
				log.debug("departmentId:" + departmentId);
				log.debug("departmentName:" + departmentName);
				log.debug("scripResion:" + scripResion);
			
			}
			//获取当前用户
			CustomUser user = getCompleteAuthentication();
			
			//封装参数信息
			MouldScripApply mouldScripApply = new MouldScripApply();
			mouldScripApply.setMouldNumber(mouldNumber);
			mouldScripApply.setMouldMap(mouldMap);
			mouldScripApply.setFullNumber(fullNumber);
			mouldScripApply.setMouldName(mouldName);
			mouldScripApply.setApplierId(user.getId());
			mouldScripApply.setApplierCode(applierCode);
			mouldScripApply.setApplierName(user.getRealname());
			mouldScripApply.setDepartmentId(departmentId);
			mouldScripApply.setDepartmentName(departmentName);
			mouldScripApply.setApplyStatus(applyStatus);
			mouldScripApply.setScripResion(scripResion);
			mouldScripApply.setScripRemark(scripRemark);
			log.debug("MouldScripApplyController.mouldScripApplyUpdate>>>");
			
			return new AjaxReturn(200,null,mouldScripApplyService.updateActiveById(user.getId(), mouldScripApply, pkId));
	}
}
