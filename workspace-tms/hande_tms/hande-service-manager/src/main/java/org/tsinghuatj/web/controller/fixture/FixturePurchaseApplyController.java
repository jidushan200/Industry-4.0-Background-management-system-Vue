package org.tsinghuatj.web.controller.fixture;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.fixture.domain.FixturePurchaseApply;
import org.tsinghuatj.fixture.domain.FixturePurchaseApplyDetail;
import org.tsinghuatj.fixture.service.IFixturePurchaseApplyService;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.JsonUtils;
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
public class FixturePurchaseApplyController extends BaseController {
	private @Autowired(required = false) IFixturePurchaseApplyService purchaseApplyService;

	/**
	 * 夹具申购表信息添加
	 */
	@ApiOperation(value = "夹具申购表信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具申购表信息添加成功", response = AjaxReturn.class) })
	@PostMapping(path = { "/purchase-apply-add" })
	@OperateLog(info = "夹具申购单信息新增[夹具编码:%s->申购数量-%s]", params = { "fixtureNumber", "purchaseQty" })
	public AjaxReturn fixturePurchaseApplyAdd(
			@ApiParam(name = "fixtureNumber", value = "夹具编码", required = false) @RequestParam(required = false) String fixtureNumber,
			@ApiParam(name = "fixtureMap", value = "夹具图号", required = false) @RequestParam(required = false) String fixtureMap,
			@ApiParam(name = "fixtureName", value = "夹具名称", required = false) @RequestParam(required = false) String fixtureName,
			@ApiParam(name = "fixtureType", value = "夹具名称", required = false) @RequestParam(required = false) Integer fixtureType,
			@ApiParam(name = "partCode", value = "制件编码", required = false) @RequestParam(required = false) String partCode,
			@ApiParam(name = "partName", value = "制件名称", required = false) @RequestParam(required = false) String partName,
			@ApiParam(name = "demandTime", value = "需求时间", required = false) @RequestParam(required = false) Date demandTime,
			@ApiParam(name = "purchaseType", value = "申购类型(1-新品开发;2-常用夹具)", required = false) @RequestParam(required = false) Integer purchaseType,
			@ApiParam(name = "purchaseResion", value = "申购原因", required = false) @RequestParam(required = false) Integer purchaseResion,
			@ApiParam(name = "applyDepartmentId", value = "需求部门Id", required = false) @RequestParam(required = false) Long applyDepartmentId,
			@ApiParam(name = "applyDepartmentName", value = "需求部门", required = false) @RequestParam(required = false) String applyDepartmentName,
			@ApiParam(name = "erpQty", value = "ERP库存", required = false) @RequestParam(required = false) Integer erpQty,
			@ApiParam(name = "noCheckQty", value = "ERP待检数量", required = false) @RequestParam(required = false) Integer noCheckQty,
			@ApiParam(name = "inventoryQty", value = "库存数量", required = false) @RequestParam(required = false) Integer inventoryQty,
			@ApiParam(name = "purchaseQty", value = "申请数量", required = false) @RequestParam(required = false) Integer purchaseQty,
			@ApiParam(name = "applyStatus", value = "申请状态", required = true) @RequestParam(required = true) Integer applyStatus,
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false, defaultValue = "") String remark,
			@ApiParam(name = "details", value = "明细", required = false) @RequestParam(required = false) String details

	) throws BusinessException {
		log.debug("FixturePurchaseApplyController.fixturePurchaseApplyAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("purchaseType:" + purchaseType);
			log.debug("fixtureNumber:" + fixtureNumber);
			log.debug("purchaseResion:" + purchaseResion);
			log.debug("applyStatus:" + applyStatus);
			log.debug("remark:" + remark);

		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		FixturePurchaseApply purchaseApply = new FixturePurchaseApply();
		purchaseApply.setFixtureNumber(fixtureNumber);
		purchaseApply.setFixtureName(fixtureName);
		purchaseApply.setFixtureMap(fixtureMap);
		purchaseApply.setFixtureType(fixtureType);
		purchaseApply.setPartCode(partCode);
		purchaseApply.setPartName(partName);

		purchaseApply.setDemandTime(demandTime);
		purchaseApply.setPurchaseQty(purchaseQty);
		purchaseApply.setPurchaseType(purchaseType);
		purchaseApply.setPurchaseResion(purchaseResion);

		purchaseApply.setErpQty(erpQty);
		purchaseApply.setNoCheckQty(noCheckQty);
		purchaseApply.setInventoryQty(inventoryQty);

		purchaseApply.setApplyDepartmentId(applyDepartmentId);
		purchaseApply.setApplyDepartmentName(applyDepartmentName);

		purchaseApply.setApplierId(user.getId());
		purchaseApply.setApplierName(user.getRealname());

		purchaseApply.setApplyStatus(applyStatus);
		purchaseApply.setRemark(remark);
		if (StringUtils.isNotEmpty(details)) {
			List<FixturePurchaseApplyDetail> detailList = JsonUtils.json2list(details,
					FixturePurchaseApplyDetail.class);
			purchaseApply.setDetailList(detailList);
		}
		log.debug("FixturePurchaseApplyController.fixturePurchaseApplyAdd>>>");
		return new AjaxReturn(200, null, purchaseApplyService.insert(user.getId(), purchaseApply));
	}

	/**
	 * 夹具申购表信息修改
	 */
	@ApiOperation(value = "夹具申购表信息修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具申购表信息修改成功", response = AjaxReturn.class) })
	@PostMapping(path = { "/purchase-apply-update" })
	@OperateLog(info = "夹具申购单信息修改[夹具编码:%s->申购数量-%s]", params = { "fixtureNumber", "purchaseQty" })
	public AjaxReturn fixturePurchaseApplyUpdate(
			@ApiParam(name = "pkId", value = "id", required = false) @RequestParam(required = false) Long pkId,
			@ApiParam(name = "fixtureNumber", value = "夹具编码", required = false) @RequestParam(required = false) String fixtureNumber,
			@ApiParam(name = "fixtureMap", value = "夹具图号", required = false) @RequestParam(required = false) String fixtureMap,
			@ApiParam(name = "fixtureName", value = "夹具名称", required = false) @RequestParam(required = false) String fixtureName,
			@ApiParam(name = "partCode", value = "制件编码", required = false) @RequestParam(required = false) String partCode,
			@ApiParam(name = "partName", value = "制件名称", required = false) @RequestParam(required = false) String partName,
			@ApiParam(name = "demandTime", value = "需求时间", required = false) @RequestParam(required = false) Date demandTime,
			@ApiParam(name = "purchaseType", value = "申购类型(1-新品开发;2-常用夹具)", required = false) @RequestParam(required = false) Integer purchaseType,
			@ApiParam(name = "purchaseResion", value = "申购原因", required = false) @RequestParam(required = false) Integer purchaseResion,
			@ApiParam(name = "erpQty", value = "ERP库存", required = false) @RequestParam(required = false) Integer erpQty,
			@ApiParam(name = "noCheckQty", value = "ERP待检数量", required = false) @RequestParam(required = false) Integer noCheckQty,
			@ApiParam(name = "inventoryQty", value = "库存数量", required = false) @RequestParam(required = false) Integer inventoryQty,
			@ApiParam(name = "purchaseQty", value = "申请数量", required = false) @RequestParam(required = false) Integer purchaseQty,
			@ApiParam(name = "applyStatus", value = "申请状态", required = true) @RequestParam(required = true) Integer applyStatus,
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false, defaultValue = "") String remark,
			@ApiParam(name = "details", value = "明细", required = false) @RequestParam(required = false) String details)
			throws BusinessException {
		log.debug("FixturePurchaseApplyController.fixturePurchaseApplyUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("purchaseType:" + purchaseType);
			log.debug("fixtureNumber:" + fixtureNumber);
			log.debug("purchaseResion:" + purchaseResion);
			log.debug("applyStatus:" + applyStatus);
			log.debug("remark:" + remark);

		}
		// 获取当前用户
		Long userId = getAuthentication();

		// 封装参数信息
		FixturePurchaseApply purchaseApply = new FixturePurchaseApply();
		purchaseApply.setPkId(pkId);
		purchaseApply.setFixtureNumber(fixtureNumber);
		purchaseApply.setFixtureName(fixtureName);
		purchaseApply.setFixtureMap(fixtureMap);
		purchaseApply.setPartCode(partCode);
		purchaseApply.setPartName(partName);
		purchaseApply.setDemandTime(demandTime);
		purchaseApply.setPurchaseQty(purchaseQty);
		purchaseApply.setPurchaseType(purchaseType);
		purchaseApply.setPurchaseResion(purchaseResion);

		purchaseApply.setErpQty(erpQty);
		purchaseApply.setNoCheckQty(noCheckQty);
		purchaseApply.setInventoryQty(inventoryQty);

		purchaseApply.setApplyStatus(applyStatus);
		purchaseApply.setRemark(remark);
		if (StringUtils.isNotEmpty(details)) {
			List<FixturePurchaseApplyDetail> detailList = JsonUtils.json2list(details,
					FixturePurchaseApplyDetail.class);
			purchaseApply.setDetailList(detailList);
		}
		log.debug("FixturePurchaseApplyController.fixturePurchaseApplyUpdate>>>");

		return new AjaxReturn(200, null, purchaseApplyService.updateActiveById(userId, purchaseApply, pkId));
	}

	/**
	 * 夹具申购表信息删除
	 */
	@ApiOperation(value = "夹具申购表信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具申购表信息删除", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/purchase-apply-delete-by-id")
	@OperateLog(info = "夹具申购单信息删除[夹具编码:%s->申购数量-%s]", params = { "fixtureNumber", "purchaseQty" })
	public AjaxReturn purchaseApplyDeleteById(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId)
			throws BusinessException {
		log.debug("FixturePurchaseApplyController.purchaseApplyDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId > 0);
		log.debug("FixturePurchaseApplyController.purchaseApplyDeleteById>>>");
		return new AjaxReturn(200, null, purchaseApplyService.deleteById(userId, pkId));
	}

	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "夹具申购表id查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具申购表id查询", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/purchase-apply-get-by-id")
	public AjaxReturn purchaseApplyGetById(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId)
			throws BusinessException {
		log.debug("FixturePurchaseApplyController.purchaseApplyGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId >= 1);

		log.debug("FixturePurchaseApplyController.purchaseApplyGetById>>>");
		return new AjaxReturn(200, null, purchaseApplyService.selectById(curuserId, pkId));
	}

	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "夹具申购表id查询包括收货明细", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具申购表id查询包括收货明细", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/receipt-get-by-apply-id")
	public AjaxReturn receiptGetByApplyId(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId)
			throws BusinessException {
		log.debug("FixturePurchaseApplyController.receiptGetByApplyId<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId >= 1);

		log.debug("FixturePurchaseApplyController.receiptGetByApplyId>>>");
		return new AjaxReturn(200, null, purchaseApplyService.selectByPkId(curuserId, pkId));
	}

	/**
	 * 夹具申购表信息分页查询
	 */
	@ApiOperation(value = "查询夹具申购表信息分页列表  ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具申购表信息分页查询", response = FixturePurchaseApply.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/purchase-apply-page-list")
	public AjaxReturn purchaseApplyPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page,
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "fixtureMap", value = "夹具编码", required = false) @RequestParam(required = false) String fixtureMap,
			@ApiParam(name = "purchaseType", value = "采购类型", required = false) @RequestParam(required = false) Integer purchaseType,
			@ApiParam(name = "departmentId", value = "需求部门Id", required = false) @RequestParam(required = false) Long departmentId,
			@ApiParam(name = "applierId", value = "申请人", required = false) @RequestParam(required = false) Long applierId,
			@ApiParam(name = "procurementType", value = "采购类型(1-自制;2-外购)", required = false) @RequestParam(required = false) Integer procurementType,
			@ApiParam(name = "applyStatus", value = "采购状态", required = false) @RequestParam(required = false) Integer applyStatus,
			@ApiParam(name = "applyStatusList", value = "采购状态列表", required = false) @RequestParam(required = false) String applyStatusList,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate

	) throws BusinessException {
		log.debug("FixturePurchaseApplyController.purchaseApplyPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("purchaseType:" + purchaseType);
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
		Integer id;
		List<Integer> statusList = new ArrayList<Integer>();

		// 判断状态列表是否为空，如果是空，前端访问的是夹具采购部分，未传递状态列表。如果非空，访问的为申购部分，按权限赋值，拆分并放入list便于查询
		if (applyStatusList != null && applyStatusList != "") {
			String[] arr = applyStatusList.split(",");
			for (String str : arr) {
				id = Integer.parseInt(str);
				statusList.add(id);
			}
		}

		FixturePurchaseApply purchaseApply = new FixturePurchaseApply();
		purchaseApply.setApplyDepartmentId(departmentId);
		purchaseApply.setApplyStatus(applyStatus);
		purchaseApply.setPurchaseType(purchaseType);
		purchaseApply.setFixtureMap(fixtureMap);
		purchaseApply.setStatusList(statusList);
		if (applierId != null) {
			purchaseApply.setApplierId(applierId);
		}
		purchaseApply.setProcurementType(procurementType);

		Pagination<FixturePurchaseApply> pagination = purchaseApplyService.selectPageList(userId, purchaseApply,
				queryDto);
		log.debug("FixturePurchaseApplyController.purchaseApplyPageList>>>");
		return pagination;
	}

	/**
	 * 夹具申购审核
	 */
	@ApiOperation(value = "夹具申购审核", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具申购审核", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/purchase-apply-audit")
	@OperateLog(info = "夹具申购审核[id:%s->状态:%s]", params = { "pkId", "auditStatus" })
	public AjaxReturn purchaseApplyAduit(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "auditStatus", value = "审核状态", required = true) @RequestParam(required = true) Integer auditStatus,
			@ApiParam(name = "amount", value = "金额", required = false) @RequestParam(required = false) BigDecimal amount,
			@ApiParam(name = "remark", value = "审核备注", required = false) @RequestParam(required = false) String remark)
			throws BusinessException {
		log.debug("FixturePurchaseApplyController.purchaseApplyAduit<<<");
		if (log.isDebugEnabled()) {
			log.debug("applyId:" + pkId);
			log.debug("auditStatus:" + auditStatus);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		Validate.isTrue(user.getId() > 0);

		log.debug("FixturePurchaseApplyController.purchaseApplyAduit>>>");
		return new AjaxReturn(200, null, purchaseApplyService.auditPurchaseApply(user.getId(), user.getRealname(), pkId,
				auditStatus, amount, remark));
	}

	@ApiOperation(value = "夹具申购导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具申购导出", response = String.class) })
	@Secure()
	@GetMapping(path = { "/purchase-apply-export" })
	public String purchaseApplyExport(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(name = "fixtureMap", value = "夹具编码", required = false) @RequestParam(required = false) String fixtureMap,
			@ApiParam(name = "purchaseType", value = "采购类型", required = false) @RequestParam(required = false) Integer purchaseType,
			@ApiParam(name = "departmentId", value = "需求部门Id", required = false) @RequestParam(required = false) Long departmentId,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate)
			throws Exception {
		Long userId = getAuthentication();
		FixturePurchaseApply purchaseApply = new FixturePurchaseApply();
		purchaseApply.setApplyDepartmentId(departmentId);
		purchaseApply.setPurchaseType(purchaseType);
		purchaseApply.setFixtureMap(fixtureMap);
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(50000);
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		Pagination<FixturePurchaseApply> pagination = purchaseApplyService.selectPageList(userId, purchaseApply,
				queryDto);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("夹具申购单");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容

		row1.createCell(0).setCellValue("物料编码");
		row1.createCell(1).setCellValue("物料名称");
		row1.createCell(2).setCellValue("物料图号");
		row1.createCell(3).setCellValue("物料类型");
		row1.createCell(4).setCellValue("制件编码");
		row1.createCell(5).setCellValue("制件名称");
		row1.createCell(6).setCellValue("申购数量");
		row1.createCell(7).setCellValue("需求时间");
		row1.createCell(8).setCellValue("申购时间");
		row1.createCell(9).setCellValue("申购类型");
		row1.createCell(10).setCellValue("申购原因");
		row1.createCell(11).setCellValue("申请部门");
		row1.createCell(12).setCellValue("申请人");
		row1.createCell(13).setCellValue("申购备注");
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i <= 13; i++) {
			row1.getCell(i).setCellStyle(cellStyle);
		}

		row1.setHeight((short) 400);// 目的是想把行高设置成25px
		sheet.setColumnWidth(0, 7000);
		sheet.setColumnWidth(1, 7000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);

		sheet.setColumnWidth(6, 5000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 5000);
		sheet.setColumnWidth(10, 5000);
		sheet.setColumnWidth(11, 5000);
		sheet.setColumnWidth(12, 5000);
		sheet.setColumnWidth(13, 5000);
		int i = 1;

		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);

		for (FixturePurchaseApply item : pagination.getRows()) {
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			dataRow.createCell(0).setCellValue(item.getFixtureNumber());
			dataRow.createCell(1).setCellValue(item.getFixtureName());
			dataRow.createCell(2).setCellValue(item.getFixtureMap());
			dataRow.createCell(3).setCellValue(item.getFixtureType() == 1 ? "组合" : "配件");
			dataRow.createCell(4).setCellValue(item.getPartCode());
			dataRow.createCell(5).setCellValue(item.getPartName());
			dataRow.createCell(6).setCellValue(item.getPurchaseQty());
			dataRow.createCell(7).setCellValue(DateFormatUtils.format(item.getApplyTime(), "yyyy-MM-dd"));
			dataRow.createCell(8).setCellValue(DateFormatUtils.format(item.getDemandTime(), "yyyy-MM-dd HH:mm:ss"));
			dataRow.createCell(9).setCellValue(item.getPurchaseType() == 1 ? "新品开发" : "常用夹具");
			String purchaseResion = "";
			if (item.getPurchaseResion() == 1) {
				purchaseResion = "产量提升";
			} else if (item.getPurchaseResion() == 2) {
				purchaseResion = "夹具报废";
			} else if (item.getPurchaseResion() == 3) {
				purchaseResion = "新品开发";
			} else if (item.getPurchaseResion() == 3) {
				purchaseResion = "其他";
			}
			dataRow.createCell(10).setCellValue(purchaseResion);
			dataRow.createCell(11).setCellValue(item.getApplyDepartmentName());
			dataRow.createCell(12).setCellValue(item.getApplierName());
			dataRow.createCell(13).setCellValue(item.getRemark());
			i++;
		}
		// 输出Excel文件
		String fileName = "夹具申购.xls";
		// 获得浏览器信息并转换为大写
		String agent = request.getHeader("User-Agent").toUpperCase();
		if (agent.indexOf("MSIE") > 0 || (agent.indexOf("GECKO") > 0 && agent.indexOf("RV:11") > 0)) {
			// 微软的浏览器(IE和Edge浏览器)
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
		}
		// 输出Excel文件
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
		wb.close();
		return null;

	}

}
