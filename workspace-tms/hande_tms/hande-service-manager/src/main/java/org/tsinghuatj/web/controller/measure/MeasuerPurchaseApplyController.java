package org.tsinghuatj.web.controller.measure;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.odftoolkit.odfdom.converter.core.utils.StringUtils;
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
import org.tsinghuatj.framework.utils.JsonUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.measure.domain.MeasurePurchaseApply;
import org.tsinghuatj.measure.domain.MeasurePurchaseReceipt;
import org.tsinghuatj.measure.service.IMeasurePurchaseApplyService;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolApplyAudit;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/measure" }) 
public class MeasuerPurchaseApplyController extends BaseController{

		private @Autowired(required = false) IMeasurePurchaseApplyService measurePurchaseApplyService;

		/**
		  * 量具申购表信息添加
		  */
		@ApiOperation(value = "量具申购表信息添加", code = 200, produces = "application/json", notes = "")
		@ApiResponses({ @ApiResponse(code = 200, message = "量具申购表信息添加成功", response = AjaxReturn.class) })
		@RequestMapping(method = RequestMethod.POST, value = "/measure-purchase-apply-add")
		@OperateLog(info = "量具申购单信息添加[物料编码:%s]", params = { "measureNumber" })
		public AjaxReturn measurePurchaseApplyAdd(
				@ApiParam(name = "measureNumber", value = "量具编码",required = false) @RequestParam(required = false)String measureNumber,
				@ApiParam(name = "model", value = "型号规格",required = false) @RequestParam(required = false)String model,
				@ApiParam(name = "measureName", value = "量具名称",required = false) @RequestParam(required = false)String measureName,
				@ApiParam(name = "applyQty", value = "申购数量",required = false) @RequestParam(required = false)Integer applyQty,			
				@ApiParam(name = "demandTime", value = "需求时间",required = false) @RequestParam(required = false)Date demandTime,
				@ApiParam(name = "purchaseReasion", value = "原因描述",required = false) @RequestParam(required = false,defaultValue="")Integer purchaseReasion,
				@ApiParam(name = "inventoryQty", value = "库存数量",required = false) @RequestParam(required = false,defaultValue="")Integer inventoryQty,
				@ApiParam(name = "erpQty", value = "erp数量",required = false) @RequestParam(required = false,defaultValue="")Integer erpQty,
				@ApiParam(name = "noCheckQty", value = "ERP待检刀具",required = false) @RequestParam(required = false)Integer noCheckQty,
				@ApiParam(name = "applyDepartmentId", value = "申请部门ID",required = false) @RequestParam(required = false)Long applyDepartmentId,
				@ApiParam(name = "applyDepartmentName", value = "申请部门",required = false) @RequestParam(required = false)String applyDepartmentName,
				@ApiParam(name = "useDepartmentId", value = "使用部门ID",required = false) @RequestParam(required = false)Long useDepartmentId,
				@ApiParam(name = "useDepartmentName", value = "使用部门",required = false) @RequestParam(required = false)String useDepartmentName,
				@ApiParam(name = "useTeamId", value = "使用班组ID",required = false) @RequestParam(required = false)Long useTeamId,
				@ApiParam(name = "useTeamName", value = "使用班组",required = false) @RequestParam(required = false)String useTeamName,	
				@ApiParam(name = "userId", value = "使用人ID",required = false) @RequestParam(required = false)Long userId,
				@ApiParam(name = "userName", value = "使用人",required = false) @RequestParam(required = false)String userName,
				@ApiParam(name = "applyStatus", value = "申请状态",required = true) @RequestParam(required = true)Integer applyStatus,
				@ApiParam(name = "remark", value = "备注",required = false) @RequestParam(required = false,defaultValue="")String remark
				)throws BusinessException {
				log.debug("measurePurchaseApplyController.measurePurchaseApplyAdd<<<");
				if (log.isDebugEnabled()) {	
					log.debug("measureNumber:" + measureNumber);
					log.debug("model:" + model);
					log.debug("measureName:" + measureName);
					log.debug("applyQty:" + applyQty);
					log.debug("demandTime:" + demandTime);
					log.debug("purchaseReasion:" + purchaseReasion);
					log.debug("inventoryQty:" + inventoryQty);
					log.debug("erpQty:" + erpQty);
					log.debug("noCheckQty:" + noCheckQty);
					log.debug("applyDepartmentId:" + applyDepartmentId);
					log.debug("applyDepartmentName:" + applyDepartmentName);
					log.debug("useDepartmentId:" + useDepartmentId);
					log.debug("useDepartmentName:" + useDepartmentName);
					log.debug("useTeamId:" + useTeamId);
					log.debug("useTeamName:" + useTeamName);
					log.debug("userId:" + userId);
					log.debug("userName:" + userName);
				}
				//获取当前用户
				CustomUser user = getCompleteAuthentication();
				
				//封装参数信息
				MeasurePurchaseApply measurePurchaseApply = new MeasurePurchaseApply();
				measurePurchaseApply.setMeasureNumber(measureNumber);
				measurePurchaseApply.setMeasureName(measureName);
				measurePurchaseApply.setModel(model);
				measurePurchaseApply.setApplyQty(applyQty);
				measurePurchaseApply.setDemandTime(demandTime);
				measurePurchaseApply.setPurchaseReasion(purchaseReasion);
				measurePurchaseApply.setInventoryQty(inventoryQty);
				measurePurchaseApply.setErpQty(erpQty);
				measurePurchaseApply.setNoCheckQty(noCheckQty);
				measurePurchaseApply.setApplyDepartmentId(applyDepartmentId);
				measurePurchaseApply.setApplyDepartmentName(applyDepartmentName);
				measurePurchaseApply.setApplierId(user.getId());
				measurePurchaseApply.setApplierName(user.getRealname());
				measurePurchaseApply.setUseDepartmentId(useDepartmentId);
				measurePurchaseApply.setUseDepartmentName(useDepartmentName);
				measurePurchaseApply.setUseTeamId(useTeamId);
				measurePurchaseApply.setUseTeamName(useTeamName);
				measurePurchaseApply.setUserId(userId);
				measurePurchaseApply.setUserName(userName);
				measurePurchaseApply.setApplyTime(new Date());
				measurePurchaseApply.setApplyStatus(applyStatus);
				
				log.debug("measurePurchaseApplyController.measurePurchaseApplyAdd>>>");
				
				return new AjaxReturn(200,null,measurePurchaseApplyService.insert(user.getId(), measurePurchaseApply));
		}
		
		/**
		 * 量具申购表信息删除
		 */
		@ApiOperation(value = "量具申购表信息删除", code = 200, produces = "application/json", notes = "")
		@ApiResponses({ @ApiResponse(code = 200, message = "量具申购表信息删除成功", response = AjaxReturn.class) })
		@RequestMapping(method = RequestMethod.POST, value = "/measure-purchase-apply-delete-by-id")
		@OperateLog(info = "量具申购单信息删除[量具编码:%s]", params = { "measureNumber" })
		public AjaxReturn measurePurchaseApplyDeleteById(@ApiParam(name = "pkId", value = "主键",required = true) @RequestParam(required = true) Long pkId,
				@ApiParam(name = "measureNumber", value = "measureNumber",required = false) @RequestParam(required = false) String measureNumber)throws BusinessException {
			log.debug("measurePurchaseApplyController.measurePurchaseApplyDeleteById<<<");
			if (log.isDebugEnabled()) {
				log.debug("pkId:" + pkId);
			}
			
			//获取当前用户
			Long userId = getAuthentication();
			//参数校验
			Validate.isTrue(pkId > 0);
			
			log.debug("measurePurchaseApplyController.measurePurchaseApplyDeleteById>>>");
			return new AjaxReturn(200,null,measurePurchaseApplyService.deleteById(userId, pkId));
		}
		
		/**
		 * 根据主键查找
		 */
		@ApiOperation(value = "刀具申购表id查询列表", code = 200, produces = "application/json", notes = "")
		@ApiResponses({ @ApiResponse(code = 200, message = "刀具申购表Id查询列表查询成功", response = MeasurePurchaseApply.class) })
		@RequestMapping(method = RequestMethod.POST, value = "/measure-purchase-apply-get-by-id")
		public AjaxReturn measurePurchaseApplyGetById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
			log.debug("measurePurchaseApplyController.measurePurchaseApplyGetById<<<");
			if (log.isDebugEnabled()) {
				log.debug("pkId:" + pkId);
			}

			// 获取当前用户
			Long curuserId = getAuthentication();
			// 参数校验
			Validate.isTrue(pkId >= 1);

			log.debug("measurePurchaseApplyController.measurePurchaseApplyGetById>>>");
			return new AjaxReturn(200, null, measurePurchaseApplyService.selectById(curuserId, pkId));
		}
		
		/**
		 * 查询量具申购表信息分页列表 
		 */
		@ApiOperation(value = "查询量具申购表信息分页列表  ", code = 200, produces = "application/json", notes = "")
		@ApiResponses({ @ApiResponse(code = 200, message = "查询量具申购表信息分页列表 查询成功", response = MeasurePurchaseApply.class) })
		@RequestMapping(method = RequestMethod.POST, value = "/measure-purchase-apply-page-list")
		public AjaxReturn measurePurchaseApplyPageList(
				@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, 
				@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
				@ApiParam(name = "isNew", value = "isNew", required = false) @RequestParam(required = false) Integer isNew,
				@ApiParam(name = "departmentId", value = "需求部门Id",required = false) @RequestParam(required = false)Long departmentId,
				@ApiParam(name = "applierId", value = "申请人", required = false) @RequestParam(required = false) Long applierId,
				@ApiParam(name = "applyStatus", value = "采购状态", required = false) @RequestParam(required = false) Integer applyStatus,
				@ApiParam(name = "applyStatusList", value = "采购状态列表", required = false) @RequestParam(required = false) String applyStatusList,
				@ApiParam(name = "beginDate", value = "开始日期",required = false) @RequestParam(required = false,defaultValue = "") Date beginDate,
				@ApiParam(name = "endDate", value = "结束日期",required = false) @RequestParam(required = false,defaultValue = "") Date endDate,
				@ApiParam(name = "measureNumber", value = "刀具编码",required = false) @RequestParam(required = false)String measureNumber
				)throws BusinessException {
			log.debug("measurePurchaseApplyController.measurePurchaseApplyPageList<<<");
			if (log.isDebugEnabled()) {
				log.debug("page:" + page);
				log.debug("rows:" + rows);
				log.debug("measureNumber:" + measureNumber);
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
			queryDto.setBeginDate(beginDate);
			queryDto.setEndDate(endDate);
			
			//判断状态列表是否为空，如果是空，前端访问的是刀具采购部分，未传递状态列表。如果非空，访问的为申购部分，按权限赋值，拆分并放入list便于查询
			Integer id;
			List<Integer> statusGroup = new ArrayList<Integer>();
			if(applyStatusList != null && applyStatusList != ""){
				String[] arr = applyStatusList.split(",");
				for (String str : arr) {
					id = Integer.parseInt(str);
					statusGroup.add(id);
				}
			}
			
			MeasurePurchaseApply measurePurchaseApply = new MeasurePurchaseApply();
			measurePurchaseApply.setApplyDepartmentId(departmentId);
			measurePurchaseApply.setMeasureNumber(measureNumber);
			measurePurchaseApply.setApplyStatus(applyStatus);
			measurePurchaseApply.setStatusList(statusGroup);
			if(applierId != null){
				measurePurchaseApply.setApplierId(applierId);
			}
			measurePurchaseApply.setIsNew(isNew);
			
			Pagination<MeasurePurchaseApply> pagination = measurePurchaseApplyService.selectPageList(userId, measurePurchaseApply, queryDto);
			log.debug("measurePurchaseApplyController.measurePurchaseApplyPageList>>>");
			return pagination;
		}
		
		/**
		 * 查询刀具申购表信息分页列表
		 */
		@ApiOperation(value = "查询刀具申购表信息分页列表  ", code = 200, produces = "application/json", notes = "")
		@ApiResponses({ @ApiResponse(code = 200, message = "查询刀具申购表信息分页列表 查询成功", response = MeasurePurchaseApply.class) })
		@RequestMapping(method = RequestMethod.POST, value = "/audited-apply-page-list")
		public AjaxReturn auditedApplyPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, 
				@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, 
				@ApiParam(name = "departmentId", value = "需求部门Id", required = false) @RequestParam(required = false) Long departmentId,
				@ApiParam(name = "auditorId", value = "审核人", required = false) @RequestParam(required = false) Long auditorId,
				@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate, 
				@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate, 
				@ApiParam(name = "measureNumber", value = "物料编码", required = false) @RequestParam(required = false) String measureNumber) throws BusinessException {
			log.debug("ToolPurchaseApplyController.auditedApplyPageList<<<");
			if (log.isDebugEnabled()) {
				log.debug("page:" + page);
				log.debug("rows:" + rows);
				log.debug("measureNumber:" + measureNumber);
			}

			// 获取当前用户
			Long userId = getAuthentication();
			// 参数校验
			Validate.isTrue(page >= 1);
			Validate.isTrue(rows <= 100);
			// 封装查询条件
			QueryDto queryDto = new QueryDto();
			queryDto.setPage(page);
			queryDto.setRows(rows);
			queryDto.setBeginDate(beginDate);
			queryDto.setEndDate(endDate);

			MeasurePurchaseApply measurePurchaseApply = new MeasurePurchaseApply();
			measurePurchaseApply.setApplyDepartmentId(departmentId);
			measurePurchaseApply.setMeasureNumber(measureNumber);
			if(auditorId != null){
				measurePurchaseApply.setAuditorId(auditorId);
			}

			Pagination<MeasurePurchaseApply> pagination = measurePurchaseApplyService.selectAuditedPageList(userId, measurePurchaseApply, queryDto);
			log.debug("ToolPurchaseApplyController.auditedApplyPageList>>>");
			return pagination;
		}
		
		/**
		 * 申购报告审核
		 */
		@ApiOperation(value = "申购报告审核", code = 200, produces = "application/json", notes = "")
		@ApiResponses({ @ApiResponse(code = 200, message = "申购报告审核", response = AjaxReturn.class) })
		@RequestMapping(method = RequestMethod.POST, value = "/measure-purchase-report-audit")
		@OperateLog(info = "量具申购单信息审核[量具编码:%s]", params = { "measureNumber" })
		public AjaxReturn purchaseReportAduit(
				@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId,
				@ApiParam(name = "measureNumber", value = "measureNumber",required = false) @RequestParam(required = false)String measureNumber,
				@ApiParam(name = "applyStatus", value = "审核状态", required = true) @RequestParam(required = true) Integer applyStatus,
				@ApiParam(name = "auitDepartmentId", value = "审核部门Id",required = true) @RequestParam(required = true)Long auitDepartmentId,
				@ApiParam(name = "auitDepartmentName", value = "审核部门",required = true) @RequestParam(required = true)String auitDepartmentName,
				@ApiParam(name = "auditRemark", value = "审核备注", required = false) @RequestParam(required = false) String auditRemark,
				@ApiParam(name = "purchasePrice", value = "采购价格", required = false) @RequestParam(required = false) BigDecimal purchasePrice)
				throws BusinessException {
			log.debug("measurePurchaseApplyController.purchaseReportAduit<<<");
			if (log.isDebugEnabled()) {
				log.debug("applyId:" + pkId);
				log.debug("applyStatus:" + applyStatus);
				log.debug("auditRemark:" + auditRemark);
			}
			// 获取当前用户
			CustomUser user = getCompleteAuthentication();
			Validate.isTrue(user.getId() > 0);
			// 封装参数信息
			MeasurePurchaseApply purchaseReport = measurePurchaseApplyService.selectById(user.getId(), pkId);
			purchaseReport.setApplyStatus(applyStatus);
			purchaseReport.setPurchasePrice(purchasePrice);
			
			ToolApplyAudit applyAudit = new ToolApplyAudit();
			applyAudit.setAuditorId(user.getId());
			applyAudit.setAuditorName(user.getRealname());
			applyAudit.setAuditDepartmentId(auitDepartmentId);
			applyAudit.setAuditDepartmentName(auitDepartmentName);
			applyAudit.setRemark(auditRemark);
			
			log.debug("measurePurchaseApplyController.purchaseReportAduit>>>");		
			return new AjaxReturn(200, null, measurePurchaseApplyService.reportAudit(user.getId(),user.getRealname(), applyAudit, purchaseReport));
		}
		
		/**
		  * 量具申购表信息更新
		  */
		@ApiOperation(value = "量具申购表信息更新", code = 200, produces = "application/json", notes = "")
		@ApiResponses({ @ApiResponse(code = 200, message = "量具申购表信息更新成功", response = AjaxReturn.class) })
		@RequestMapping(method = RequestMethod.POST, value = "/measure-purchase-apply-update")
		@OperateLog(info = "量具申购单信息修改[量具编码:%s]", params = { "measureNumber" })
		public AjaxReturn measurePurchaseApplyUpdate(
				@ApiParam(name = "pkId", value = "pkId",required = true) @RequestParam(required = true)Long pkId,
				@ApiParam(name = "measureNumber", value = "量具编码",required = false) @RequestParam(required = false)String measureNumber,
				@ApiParam(name = "model", value = "型号规格",required = false) @RequestParam(required = false)String model,
				@ApiParam(name = "measureName", value = "量具名称",required = false) @RequestParam(required = false)String measureName,
				@ApiParam(name = "applyQty", value = "申购数量",required = false) @RequestParam(required = false)Integer applyQty,			
				@ApiParam(name = "demandTime", value = "需求时间",required = false) @RequestParam(required = false)Date demandTime,
				@ApiParam(name = "purchaseReasion", value = "原因描述",required = false) @RequestParam(required = false,defaultValue="")Integer purchaseReasion,
				@ApiParam(name = "inventoryQty", value = "库存数量",required = false) @RequestParam(required = false,defaultValue="")Integer inventoryQty,
				@ApiParam(name = "erpQty", value = "erp数量",required = false) @RequestParam(required = false,defaultValue="")Integer erpQty,
				@ApiParam(name = "noCheckQty", value = "ERP待检刀具",required = false) @RequestParam(required = false)Integer noCheckQty,
				@ApiParam(name = "applyDepartmentId", value = "申请部门ID",required = false) @RequestParam(required = false)Long applyDepartmentId,
				@ApiParam(name = "applyDepartmentName", value = "申请部门",required = false) @RequestParam(required = false)String applyDepartmentName,
				@ApiParam(name = "useDepartmentId", value = "使用部门ID",required = false) @RequestParam(required = false)Long useDepartmentId,
				@ApiParam(name = "useDepartmentName", value = "使用部门",required = false) @RequestParam(required = false)String useDepartmentName,
				@ApiParam(name = "useTeamId", value = "使用班组ID",required = false) @RequestParam(required = false)Long useTeamId,
				@ApiParam(name = "useTeamName", value = "使用班组",required = false) @RequestParam(required = false)String useTeamName,	
				@ApiParam(name = "userId", value = "使用人ID",required = false) @RequestParam(required = false)Long userId,
				@ApiParam(name = "userName", value = "使用人",required = false) @RequestParam(required = false)String userName,
				@ApiParam(name = "applyStatus", value = "申请状态",required = true) @RequestParam(required = true)Integer applyStatus,
				@ApiParam(name = "remark", value = "备注",required = false) @RequestParam(required = false,defaultValue="")String remark
				)throws BusinessException {
				log.debug("measurePurchaseApplyController.measurePurchaseApplyUpdate<<<");
				if (log.isDebugEnabled()) {	
					log.debug("pkId:" + pkId);
					log.debug("measureNumber:" + measureNumber);
					log.debug("model:" + model);
					log.debug("measureName:" + measureName);
					log.debug("applyQty:" + applyQty);
					log.debug("demandTime:" + demandTime);
					log.debug("purchaseReasion:" + purchaseReasion);
					log.debug("inventoryQty:" + inventoryQty);
					log.debug("erpQty:" + erpQty);
					log.debug("noCheckQty:" + noCheckQty);
					log.debug("applyDepartmentId:" + applyDepartmentId);
					log.debug("applyDepartmentName:" + applyDepartmentName);
					log.debug("useDepartmentId:" + useDepartmentId);
					log.debug("useDepartmentName:" + useDepartmentName);
					log.debug("useTeamId:" + useTeamId);
					log.debug("useTeamName:" + useTeamName);
					log.debug("userId:" + userId);
					log.debug("userName:" + userName);
				}
				//获取当前用户
				CustomUser user = getCompleteAuthentication();
				
				//封装参数信息
				
				MeasurePurchaseApply measurePurchaseApply = new MeasurePurchaseApply();
				measurePurchaseApply.setMeasureNumber(measureNumber);
				measurePurchaseApply.setMeasureName(measureName);
				measurePurchaseApply.setModel(model);
				measurePurchaseApply.setApplyQty(applyQty);
				measurePurchaseApply.setDemandTime(demandTime);
				measurePurchaseApply.setPurchaseReasion(purchaseReasion);
				measurePurchaseApply.setInventoryQty(inventoryQty);
				measurePurchaseApply.setErpQty(erpQty);
				measurePurchaseApply.setNoCheckQty(noCheckQty);
				measurePurchaseApply.setApplyDepartmentId(applyDepartmentId);
				measurePurchaseApply.setApplyDepartmentName(applyDepartmentName);
//				measurePurchaseApply.setApplierId(user.getId());
//				measurePurchaseApply.setApplierName(user.getRealname());
				measurePurchaseApply.setUseDepartmentId(useDepartmentId);
				measurePurchaseApply.setUseDepartmentName(useDepartmentName);
				measurePurchaseApply.setUseTeamId(useTeamId);
				measurePurchaseApply.setUseTeamName(useTeamName);
				measurePurchaseApply.setUserId(userId);
				measurePurchaseApply.setUserName(userName);
				measurePurchaseApply.setApplyStatus(applyStatus);
//				MeasurePurchaseApply measurePurchase = measurePurchaseApplyService.selectById(user.getId(), pkId);
//				Integer purchaseQty = toolPurchase.getArrivaledQty();
//				if(purchaseQty != null && purchaseQty != 0){
//					measurePurchaseApply.setArrivaledQty(arrivaledQty + purchaseQty);
//				}else{
//					measurePurchaseApply.setArrivaledQty(arrivaledQty);
//				}
				
				log.debug("measurePurchaseApplyController.measurePurchaseApplyUpdate>>>");
				
				return new AjaxReturn(200,null,measurePurchaseApplyService.updateActiveById(user.getId(), measurePurchaseApply, pkId));
		}

		/**
		 * 采购根据编号查找
		 */
		@ApiOperation(value = "根据编码查找", code = 200, produces = "application/json", notes = "")
		@ApiResponses({ @ApiResponse(code = 200, message = "根据编码查找", response = Tool.class) })
		@RequestMapping(method = RequestMethod.POST, value = "/purchase-get-by-measure-number")
		public AjaxReturn purchaseGetByMeasureNumber(
				@ApiParam(name = "measureNumber", value = "物料编码", required = true) @RequestParam(required = true) String measureNumber)
				throws BusinessException {
			log.debug("measurePurchaseApplyController.purchaseGetByMeasureNumber<<<");
			if (log.isDebugEnabled()) {
				log.debug("measureNumber:" + measureNumber);
			}

			// 获取当前用户
			Long curuserId = getAuthentication();
			// 参数校验
			Validate.isTrue(StringUtils.isNotEmpty(measureNumber));

			log.debug("measurePurchaseApplyController.purchaseGetByMeasureNumber>>>");
			return new AjaxReturn(200, null, measurePurchaseApplyService.purchaseGetByMeasureNumber(curuserId, measureNumber));
		}
		
		/**
		 * 采购到货
		 */
		@ApiOperation(value = "采购到货", code = 200, produces = "application/json", notes = "")
		@ApiResponses({ @ApiResponse(code = 200, message = "采购到货", response = AjaxReturn.class) })
		@RequestMapping(method = RequestMethod.POST, value = "/purchase-apply-receipt")
		@OperateLog(info = "采购到货[量具编码:%s]", params = { "measureNumber" })
		public AjaxReturn purchaseApplyReceipt(@ApiParam(name = "pkId", value = "申请单id", required = true) @RequestParam(required = true) Long pkId, 
				//@ApiParam(name = "arrivalQty", value = "本次到货数量", required = true) @RequestParam(required = true) Integer arrivalQty,		
				@ApiParam(name = "receiptList", value = "到货明细", required = true) @RequestParam(required = true) String receiptList,		
				@ApiParam(name = "measureNumber", value = "量具编码", required = true) @RequestParam(required = true) String measureNumber) throws BusinessException {
			log.debug("measurePurchaseApplyController.purchaseApplyReceipt<<<");
			if (log.isDebugEnabled()) {
				log.debug("pkId:" + pkId);
				//log.debug("arrivalQty:" + arrivalQty);
				log.debug("receiptList:" + receiptList);
			}
			// 获取当前用户
			Long userId = getAuthentication();
		
			// 封装参数信息
			List<MeasurePurchaseReceipt> list = null;
			if (StringUtils.isNotEmpty(receiptList)) {
				list = JsonUtils.json2list(receiptList, MeasurePurchaseReceipt.class);
			}
			log.debug("measurePurchaseApplyController.purchaseApplyReceipt>>>");
			return new AjaxReturn(200, null, measurePurchaseApplyService.updatePurchaseReceipt(userId,pkId, null, list));
		}
		
		/**
		 * 采购到货
		 */
		@ApiOperation(value = "采购到货", code = 200, produces = "application/json", notes = "")
		@ApiResponses({ @ApiResponse(code = 200, message = "刀具申购表信息更新成功", response = AjaxReturn.class) })
		@RequestMapping(method = RequestMethod.POST, value = "/get-purchase-apply-receipt-list")
		public AjaxReturn getPurchaseApplyReceipt(@ApiParam(name = "pkId", value = "申请单ID", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
			log.debug("measurePurchaseApplyController.getPurchaseApplyReceipt<<<");

			// 获取当前用户
			Long userId = getAuthentication();
			// 封装参数信息
			log.debug("measurePurchaseApplyController.getPurchaseApplyReceipt>>>");

			return new AjaxReturn(200, null, measurePurchaseApplyService.selectByApplyId(userId, pkId));
		}
}
