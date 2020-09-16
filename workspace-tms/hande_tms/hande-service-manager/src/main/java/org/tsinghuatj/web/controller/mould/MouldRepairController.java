package org.tsinghuatj.web.controller.mould;

import java.util.Date;
import java.util.List;

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
import org.tsinghuatj.framework.utils.JsonUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.mould.domain.Mould;
import org.tsinghuatj.mould.domain.MouldRepair;
import org.tsinghuatj.mould.domain.MouldRepairItem;
import org.tsinghuatj.mould.service.IMouldRepairItemService;
import org.tsinghuatj.mould.service.IMouldRepairService;
import org.tsinghuatj.mould.service.IMouldService;
import org.tsinghuatj.support.annotation.OperateLog;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/mould" })
public class MouldRepairController extends BaseController{

	private @Autowired(required = false) IMouldService mouldService;
	private @Autowired(required = false) IMouldRepairService mouldRepairService;
	private @Autowired(required = false) IMouldRepairItemService mouldRepairItemService;
	
	/**
	 * 查询刀具加工信息分页列表 
	 */
	@ApiOperation(value = "查询刀具加工信息分页列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询刀具加工信息分页列表查询成功", response = Mould.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-repair-page-list")
	public AjaxReturn mouldRepairPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, 
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "mouldBarcode", value = "领用部门ID",required = false) @RequestParam(required = false)String mouldBarcode,
			@ApiParam(name = "mouldNumber", value = "领用部门ID",required = false) @RequestParam(required = false)String mouldNumber
			)throws BusinessException {
		log.debug("MouldRepairController.mouldRepairPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("mouldBarcode:" + mouldBarcode);
			log.debug("mouldNumber:" + mouldNumber);
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
		mould.setMouldBarcode(mouldBarcode);
		mould.setMouldNumber(mouldNumber);
		mould.setMouldStatus(3);
		mould.setIsList(1);
		
		Pagination<Mould> pagination = mouldService.selectPageList(userId, mould, queryDto);
		log.debug("MouldRepairController.mouldRepairPageList>>>");
		return pagination;
	}
	
	/**
	 * 模具修磨新增
	 */
	@ApiOperation(value = "模具修磨新增", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具修磨成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-repair-add", produces = "application/json;charset=UTF-8")
	@OperateLog(info = "模具修模[模具条码:%s]", params = { "fullNumber" })
	public AjaxReturn mouldRepairAdd(
			@ApiParam(name = "mouldId", value = "检验类型", required = true) @RequestParam(required = true) Long mouldId, 
			@ApiParam(name = "fullNumber", value = "刀具编码", required = true) @RequestParam(required = true) String fullNumber, 
			@ApiParam(name = "repairUserId", value = "刀具编码", required = false) @RequestParam(required = false) Long repairUserId,
			@ApiParam(name = "repairUser", value = "刀具编码", required = false) @RequestParam(required = false) String repairUser, 
			@ApiParam(name = "repairTime", value = "检验类型", required = true) @RequestParam(required = true) Date repairTime,
			@ApiParam(name = "remark", value = "备注说明", required = false) @RequestParam(required = false) String remark,
			@ApiParam(name = "itemList", value = "检验项", required = true) @RequestParam(required = true) String itemList) throws BusinessException {
		log.debug("MouldRepairController.mouldRepairAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("itemList:" + itemList);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		Validate.isTrue(userId >= 0);

		List<MouldRepairItem> items = JsonUtils.json2list(itemList, MouldRepairItem.class);
		MouldRepair mouldRepair = new MouldRepair();
		mouldRepair.setMouldId(mouldId);
		mouldRepair.setFullNumber(fullNumber);
		mouldRepair.setRepairUserId(repairUserId);
		mouldRepair.setRepairUser(repairUser);
		mouldRepair.setRepairTime(repairTime);
		mouldRepair.setRemark(remark);
		mouldRepair.setItemList(items);
		log.debug("MouldRepairController.mouldRepairAdd>>>");
		return new AjaxReturn(200, null, mouldRepairService.insert(userId, mouldRepair));
	}
	
	/**
	 * 查询模具修模信息分页列表 
	 */
	@ApiOperation(value = "查询模具修模信息分页列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询模具修模信息分页列表查询成功", response = MouldRepair.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/repair-page-list")
	public AjaxReturn repairPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, 
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "fullNumber", value = "模具条码",required = false) @RequestParam(required = false)String fullNumber,
			@ApiParam(name = "beginDate", value = "开始日期",required = false) @RequestParam(required = false,defaultValue = "") Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期",required = false) @RequestParam(required = false,defaultValue = "") Date endDate
			)throws BusinessException {
		log.debug("MouldRepairController.repairPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
			log.debug("beginDate:" + beginDate);
			log.debug("endDate:" + endDate);
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
		
		MouldRepair mouldRepair = new MouldRepair();
		mouldRepair.setFullNumber(fullNumber);
		
		Pagination<MouldRepair> pagination = mouldRepairService.selectPageList(userId, mouldRepair, queryDto);
		log.debug("MouldRepairController.repairPageList>>>");
		return pagination;
	}
	
	/**
	 * 修磨工序列表查询
	 */
	@ApiOperation(value = "修磨工序列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "修磨工序列表查询", response = MouldRepairItem.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/repair-item-get-list")
	public AjaxReturn repairItemGetList(
			@ApiParam(name = "repairId", value = "主键", required = true) @RequestParam(required = true) Long repairId)
			throws BusinessException {
		log.debug("MouldRepairController.repairItemGetList<<<");
		if (log.isDebugEnabled()) {
			log.debug("repairId:" + repairId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		log.debug("MouldRepairController.repairItemGetList>>>");
		return new AjaxReturn(200, null, mouldRepairItemService.selectByRepairId(userId, repairId));
	}
}
