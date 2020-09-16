package org.tsinghuatj.web.controller.base;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tsinghuatj.base.domain.CheckStandard;
import org.tsinghuatj.base.domain.CheckStandardItem;
import org.tsinghuatj.base.service.ICheckStandardService;
import org.tsinghuatj.fixture.domain.FixturePurchaseReceipt;
import org.tsinghuatj.fixture.service.IFixturePurchaseReceiptService;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.JsonUtils;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.utils.excel.PoiExcelUtil;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolBase;
import org.tsinghuatj.tool.domain.ToolOutbound;
import org.tsinghuatj.tool.domain.ToolPurchaseReceipt;
import org.tsinghuatj.tool.service.IToolBaseService;
import org.tsinghuatj.tool.service.IToolOutboundService;
import org.tsinghuatj.tool.service.IToolPurchaseReceiptService;
import org.tsinghuatj.tool.service.IToolService;

import com.github.pagehelper.util.StringUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/base" })
public class CheckStandardController extends BaseController {

	private @Autowired(required = false) ICheckStandardService standardService;
	private @Autowired(required = false) IToolService toolService;
	private @Autowired(required = false) IToolBaseService toolBaseService;
	private @Autowired(required = false) IToolOutboundService toolOutboundService;
	private @Autowired(required = false) IToolPurchaseReceiptService purchaseReceiptService;
	private @Autowired(required = false) IFixturePurchaseReceiptService fixturepurchaseReceiptService;

	/**
	 * 质检标准校验
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "质检标准校验", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "质检标准校验 (false-存在 true-不存在)", response = String.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/standard-number-validate")
	// @Secure() //
	public boolean standardValidate(@ApiParam(name = "materialType", value = "物料类型", required = true) @RequestParam(required = true) Integer materialType, @ApiParam(name = "materialNumber", value = "物料编码", required = true) @RequestParam(required = true) String materialNumber, @ApiParam(name = "checkType", value = "检验类型", required = true) @RequestParam(required = true) Integer checkType) throws BusinessException {
		log.debug("CheckStandardController.standardumberValidate<<<");
		if (log.isDebugEnabled()) {
			log.debug("materialNumber:" + materialNumber);
		}
		// 参数校验
		Validate.isTrue(StringUtils.isNotBlank(materialNumber));
		// 根据登录名查询

		log.debug("CheckStandardController.standardumberValidate>>>");
		return standardService.checkStandardVerification(materialType, materialNumber, checkType, null);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/material-base-list")
	@Secure() //
	public AjaxReturn materialBaseList(@ApiParam(name = "materialType", value = "物料类型", required = true) @RequestParam(required = true) Integer materialType) throws BusinessException {

		Long userId = getAuthentication();
		return new AjaxReturn(200, null, standardService.selectMaterialList(userId, materialType));
	}

	/**
	 * 质检标准分页列表
	 */
	@ApiOperation(value = "质检标准分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "质检标准分页列表", response = CheckStandard.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/check-standard-page-list")
	public AjaxReturn checkStandardPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false) Integer rows, @ApiParam(name = "checkType", value = "质检类型", required = false) @RequestParam(required = false) Integer checkType, @ApiParam(name = "materialType", value = "物料类型1-刀具2-夹具", required = false) @RequestParam(required = false) Integer materialType, @ApiParam(name = "typeId", value = "刀具类型", required = false) @RequestParam(required = false) Integer typeId, @ApiParam(name = "materialNumber", value = "物料编码", required = false) @RequestParam(required = false) String materialNumber, @ApiParam(name = "materialMap", value = "物料图号", required = false) @RequestParam(required = false) String materialMap)
			throws BusinessException {
		log.debug("CheckStandardController.checkColPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
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

		CheckStandard standard = new CheckStandard();
		standard.setMaterialNumber(materialNumber);
		standard.setMaterialType(materialType);
		if (materialType == 1) {
			standard.setTypeId(typeId);
		}
		standard.setCheckType(checkType);
		standard.setMaterialMap(materialMap);
		Pagination<CheckStandard> pagination = standardService.selectPageList(curuserId, standard, queryDto);
		log.debug("CheckStandardController.checkColPageList>>>");
		return pagination;
	}

	/**
	 * 质检项id查询
	 */
	@ApiOperation(value = "质检项id查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "质检项Id查询成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/check-standard-get-by-id")
	public AjaxReturn checkStandardGetById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("CheckStandardController.checkStandardGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId >= 1);
		log.debug("CheckStandardController.checkStandardGetById>>>");
		return new AjaxReturn(200, null, standardService.selectById(userId, pkId));
	}

	/**
	 * 质检项id删除
	 */
	@ApiOperation(value = "质检项id删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "质检项id删除", response = AjaxReturn.class) })
	@OperateLog(info = "质检标准删除[物料编码:%s]->[质检类型:%s]", params = { "materialNumber", "checkTypeName" })
	@RequestMapping(method = RequestMethod.POST, value = "/check-standard-delete-by-id")
	public AjaxReturn checkStandardDeleteById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("CheckStandardController.checkStandardDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId >= 1);
		log.debug("CheckStandardController.checkStandardDeleteById>>>");
		return new AjaxReturn(200, null, standardService.deleteById(userId, pkId));
	}

	/**
	 * 质检项修改
	 */
	@ApiOperation(value = "质检项修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "质检项修改成功", response = AjaxReturn.class) })
	@OperateLog(info = "质检标准修改[物料编码:%s]->[质检类型:%s]", params = { "materialNumber", "checkTypeName" })
	@RequestMapping(method = RequestMethod.POST, value = "/check-standard-update", produces = "application/json;charset=UTF-8")
	public AjaxReturn checkStandardUpdate(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "materialNumber", value = "物料编码", required = true) @RequestParam(required = true) String materialNumber, @ApiParam(name = "checkType", value = "检验类型", required = true) @RequestParam(required = true) Integer checkType, @ApiParam(name = "standardDesc", value = "标准描述", required = false) @RequestParam(required = false) String standardDesc, @ApiParam(name = "appendixIds", value = "附件ID", required = false) @RequestParam(required = false) String appendixIds,
			@ApiParam(name = "itemList", value = "检验项", required = true) @RequestParam(required = true) String itemList) throws BusinessException {
		log.debug("CheckStandardController.checkStandardUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
			log.debug("itemList:" + itemList);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		Validate.isTrue(pkId >= 1);
		// if (!standardService.checkStandardVerification(toolNumber, checkType,
		// pkId)) {
		// throw new BusinessException("checkstandard.exists.error");
		// }
		List<CheckStandardItem> items = JsonUtils.json2list(itemList, CheckStandardItem.class);
		CheckStandard standard = new CheckStandard();
		standard.setPkId(pkId);
		standard.setMaterialNumber(materialNumber);
		standard.setCheckType(checkType);
		standard.setStandardDesc(standardDesc);
		standard.setItemList(items);
		// 参数校验
		log.debug("CheckStandardController.checkStandardUpdate>>>");
		return new AjaxReturn(200, null, standardService.updateActiveById(userId, standard, pkId, appendixIds));
	}

	/**
	 * 质检项新增
	 */
	@ApiOperation(value = "质检项新增", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "质检项新增成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/check-standard-add", produces = "application/json;charset=UTF-8")
	@OperateLog(info = "质检标准添加[物料编码:%s]->[质检类型:%s]", params = { "materialNumber", "checkTypeName" })
	public AjaxReturn checkStandardAdd(@ApiParam(name = "materialType", value = "检验类型", required = true) @RequestParam(required = true) Integer materialType, @ApiParam(name = "materialNumber", value = "物料编码", required = true) @RequestParam(required = true) String materialNumber, @ApiParam(name = "materialName", value = "物料名称", required = true) @RequestParam(required = true) String materialName, @ApiParam(name = "checkType", value = "检验类型", required = true) @RequestParam(required = true) Integer checkType, @ApiParam(name = "standardDesc", value = "标准描述", required = false) @RequestParam(required = false) String standardDesc,
			@ApiParam(name = "appendixIds", value = "附件ID", required = false) @RequestParam(required = false) String appendixIds, @ApiParam(name = "itemList", value = "检验项", required = true) @RequestParam(required = true) String itemList) throws BusinessException {

		log.debug("CheckStandardController.checkStandardAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("itemList:" + itemList);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		Validate.isTrue(userId >= 0);
		// if (!standardService.checkStandardVerification(toolNumber, checkType,
		// null)) {
		// throw new BusinessException("checkstandard.exists.error");
		// }
		List<CheckStandardItem> items = JsonUtils.json2list(itemList, CheckStandardItem.class);
		CheckStandard standard = new CheckStandard();
		standard.setMaterialType(materialType);
		standard.setMaterialNumber(materialNumber);
		standard.setMaterialName(materialName);
		standard.setStandardDesc(standardDesc);
		standard.setCheckType(checkType);
		standard.setItemList(items);
		log.debug("CheckStandardController.checkStandardAdd>>>");
		return new AjaxReturn(200, null, standardService.insert(userId, standard, appendixIds));
	}

	/**
	 * 根据刀具编号质检标准查询
	 */
	@ApiOperation(value = "根据刀具编号质检标准查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据刀具编号质检标准查询", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/check-standard-get-by-full-number", produces = "application/json;charset=UTF-8")
	public AjaxReturn checkStandardGetByFullNumber(@ApiParam(name = "materialType", value = "物料类型", required = true) @RequestParam(required = true) Integer materialType, @ApiParam(name = "fullNumber", value = "刀具编码", required = true) @RequestParam(required = true) String fullNumber, @ApiParam(name = "checkType", value = "检验类型", required = true) @RequestParam(required = true) Integer checkType) throws BusinessException {
		log.debug("CheckStandardController.checkStandardGetByFullNumber<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
			log.debug("checkType:" + checkType);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		Validate.isTrue(userId >= 0);
		Map<String, Object> data = new HashMap<String, Object>();
		if (materialType == 1) {
			Tool tool = toolService.selectByFullNumber(userId, fullNumber);
			if (null == tool) {
				throw new BusinessException("tool.not.exists.error");
			}
			if (checkType == 3) {
				ToolOutbound toolOutbound = toolOutboundService.selectByToolIdAndOutType(userId, tool.getPkId(), 3);
				if (null != toolOutbound) {
					tool.setSupplierId(toolOutbound.getSupplierId());
					tool.setSupplierName(toolOutbound.getSupplierName());
				}
			}
			CheckStandard standard = standardService.selectByMaterialNumber(userId, 1, tool.getToolNumber(), checkType);
			if (null == standard) {
				throw new BusinessException("checkstandard.not.exists.error");
			}

			data.put("tool", tool);
			data.put("checkStandard", standard);
		}

		log.debug("CheckStandardController.checkStandardGetByFullNumber>>>");
		return new AjaxReturn(200, null, data);
	}

	/**
	 * 根据刀具编号质检标准查询
	 */
	@ApiOperation(value = "根据刀具编号质检标准查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据刀具编号质检标准查询", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/check-standard-get-by-material-number", produces = "application/json;charset=UTF-8")
	public AjaxReturn checkStandardGetByMaterialNumber(@ApiParam(name = "materialType", value = "物料类型", required = true) @RequestParam(required = true) Integer materialType, @ApiParam(name = "materialNumber", value = "物料编码", required = true) @RequestParam(required = true) String materialNumber, @ApiParam(name = "checkType", value = "检验类型", required = true) @RequestParam(required = true) Integer checkType) throws BusinessException {
		log.debug("CheckStandardController.checkStandardGetByToolNumber<<<");
		if (log.isDebugEnabled()) {
			log.debug("materialNumber:" + materialNumber);
			log.debug("checkType:" + checkType);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		Validate.isTrue(userId >= 0);
		Map<String, Object> data = new HashMap<String, Object>();
		if (materialType == 1) {
			ToolBase toolBase = toolBaseService.selectByNumber(userId, materialNumber);
			if (null == toolBase) {
				throw new BusinessException("tool.not.exists.error");
			}
			CheckStandard standard = standardService.selectByMaterialNumber(userId, 1, materialNumber, checkType);
			if (null == standard) {
				throw new BusinessException("checkstandard.not.exists.error");
			}

			data.put("tool", toolBase);
			data.put("checkStandard", standard);
		} else if (materialType == 2 || materialType == 3) {
			/*
			 * ToolBase toolBase = toolBaseService.selectByNumber(userId,
			 * materialNumber); if (null == toolBase) { throw new
			 * BusinessException("tool.not.exists.error"); }
			 */
			CheckStandard standard = standardService.selectByMaterialNumber(userId, materialType, materialNumber, checkType);
			if (null == standard) {
				throw new BusinessException("checkstandard.not.exists.error");
			}

			// data.put("tool", toolBase);
			data.put("checkStandard", standard);
		}

		log.debug("CheckStandardController.checkStandardGetByToolNumber>>>");
		return new AjaxReturn(200, null, data);
	}

	/**
	 * 根据采购收货单ID查询质检标准
	 */
	@ApiOperation(value = "根据采购收货单ID查询质检标准", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据采购收货单ID查询质检标准", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/check-standard-get-by-receipt-id", produces = "application/json;charset=UTF-8")
	public AjaxReturn checkStandardGetByReceiptId(@ApiParam(name = "materialType", value = "物料类型", required = true) @RequestParam(required = true) Integer materialType, @ApiParam(name = "receiptId", value = "收货单id", required = true) @RequestParam(required = true) Long receiptId) throws BusinessException {
		log.debug("CheckStandardController.checkStandardGetByReceiptId<<<");

		// 获取当前用户
		Long userId = getAuthentication();
		Validate.isTrue(userId >= 0);
		Map<String, Object> data = new HashMap<String, Object>();
		String materialNumber = "";
		Integer checkType = 1;
		if (materialType == 1) {
			ToolPurchaseReceipt toolPurchaseReceipt = purchaseReceiptService.selectById(userId, receiptId);
			if (null == toolPurchaseReceipt) {
				throw new BusinessException("tool.not.exists.error");
			}
			materialNumber = toolPurchaseReceipt.getToolNumber();
			checkType = 1;
			data.put("tool", toolPurchaseReceipt);
		} else if (materialType == 2) {
			FixturePurchaseReceipt fixturePurchaseReceipt = fixturepurchaseReceiptService.selectById(userId, receiptId);
			data.put("fixture", fixturePurchaseReceipt);
			materialNumber = fixturePurchaseReceipt.getFixtureNumber();
			checkType = 4;
		}

		CheckStandard standard = standardService.selectByMaterialNumber(userId, materialType, materialNumber, checkType);
		if (null == standard) {
			throw new BusinessException("checkstandard.not.exists.error");
		}

		data.put("checkStandard", standard);
		log.debug("CheckStandardController.checkStandardGetByReceiptId>>>");
		return new AjaxReturn(200, null, data);
	}

	@ApiOperation(value = "质检标准导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "质检标准导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/check-standard-import")
	@Secure()
	public AjaxReturn checkStandardImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		standardService.importCheckStandard(userId, read(file.getOriginalFilename(), inputStream));
		return new AjaxReturn(200, null, 1);
	}

	private List<CheckStandard> read(String fileName, InputStream inputStream) throws IOException {
		// 基于注解,将Excel内容读至List<SysAuthInfo>对象内
		boolean isE2007 = false; // 判断是否是excel2007格式
		if (fileName.endsWith("xlsx")) {
			isE2007 = true;
		}

		Workbook wb = null;
		// 根据文件格式(2003或者2007)来初始化
		if (isE2007) {
			wb = new XSSFWorkbook(inputStream);
		} else {
			wb = new HSSFWorkbook(inputStream);
		}
		Sheet sheet = wb.getSheetAt(0); // 获得第一个表单

		// System.out.println("总行数:"+sheet.getLastRowNum());
		PoiExcelUtil poiExcelUtil = new PoiExcelUtil();
		List<CellRangeAddress> cras = poiExcelUtil.getCombineCell(sheet);
		// isMergedRegion(Sheet sheet,int row ,int column);判断是不是合并单元格\
		int count = sheet.getLastRowNum() + 1;// 总行数

		List<CheckStandard> csList = new ArrayList<CheckStandard>();
		String cellValue;
		for (int i = 1; i < count; i++) {
			Row row = sheet.getRow(i);
			CheckStandard cs = new CheckStandard();
			cs.setMaterialTypeName(poiExcelUtil.getCellValue(row.getCell(0)));
			cs.setMaterialNumber(poiExcelUtil.getCellValue(row.getCell(1)));
			cs.setMaterialName(poiExcelUtil.getCellValue(row.getCell(2)));
			cs.setMaterialMap(poiExcelUtil.getCellValue(row.getCell(3)));
			cs.setCheckTypeName(poiExcelUtil.getCellValue(row.getCell(4)));
			cs.setStandardDesc(poiExcelUtil.getCellValue(row.getCell(5)));

			List<CheckStandardItem> items = new ArrayList<CheckStandardItem>();
			if (poiExcelUtil.isMergedRegion(sheet, i, 0)) {
				int lastRow = poiExcelUtil.getRowNum(cras, sheet.getRow(i).getCell(0), sheet);

				for (; i <= lastRow; i++) {
					row = sheet.getRow(i);
					CheckStandardItem item = new CheckStandardItem();
					item.setCheckItem(poiExcelUtil.getCellValue(row.getCell(6)));
					item.setItemStandard(poiExcelUtil.getCellValue(row.getCell(7)));
					cellValue = poiExcelUtil.getCellValue(row.getCell(8));
					if (StringUtil.isNotEmpty(cellValue)) {
						item.setUpDeviation(new BigDecimal(cellValue));
					}
					cellValue = poiExcelUtil.getCellValue(row.getCell(9));
					if (StringUtil.isNotEmpty(cellValue)) {
						item.setDownDeviation(new BigDecimal(cellValue));
					}
					item.setUnit(poiExcelUtil.getCellValue(row.getCell(10)));
					items.add(item);
				}
				i--;
			} else {
				row = sheet.getRow(i);
				CheckStandardItem item = new CheckStandardItem();
				item.setCheckItem(poiExcelUtil.getCellValue(row.getCell(6)));
				item.setItemStandard(poiExcelUtil.getCellValue(row.getCell(7)));
				cellValue = poiExcelUtil.getCellValue(row.getCell(8));
				if (StringUtil.isNotEmpty(cellValue)) {
					item.setUpDeviation(new BigDecimal(cellValue));
				}
				cellValue = poiExcelUtil.getCellValue(row.getCell(9));
				if (StringUtil.isNotEmpty(cellValue)) {
					item.setDownDeviation(new BigDecimal(cellValue));
				}
				item.setUnit(poiExcelUtil.getCellValue(row.getCell(10)));
				items.add(item);
			}
			cs.setItemList(items);
			csList.add(cs);
		}
		wb.close();
		inputStream.close();
		return csList;
	}

	@ApiResponses({ @ApiResponse(code = 200, message = "质检标准导出", response = AjaxReturn.class) })
	@Secure()
	@GetMapping(path = { "/check-standard-export" })
	public String checkStandardExport(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "checkType", value = "质检类型", required = false) @RequestParam(required = false) Integer checkType, @ApiParam(name = "typeId", value = "刀具类型", required = false) @RequestParam(required = false) Integer typeId, @ApiParam(name = "materialType", value = "物料类型1-刀具2-夹具", required = false) @RequestParam(required = false) Integer materialType, @ApiParam(name = "materialNumber", value = "物料编码", required = false) @RequestParam(required = false) String materialNumber, @ApiParam(name = "materialMap", value = "物料图号", required = false) @RequestParam(required = false) String materialMap

	) throws Exception {

		CheckStandard where = new CheckStandard();
		where.setMaterialType(materialType);
		where.setCheckType(checkType);
		where.setMaterialNumber(materialNumber);
		where.setMaterialMap(materialMap);
		if (1 == materialType) {
			where.setTypeId(typeId);
		}
		Long userId = getAuthentication();

		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(50000);

		List<CheckStandard> csList = standardService.select(userId, where);
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("质检标准");
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("物料类型");
		row1.createCell(1).setCellValue("物料编码");
		row1.createCell(2).setCellValue("物料名称");
		row1.createCell(3).setCellValue("物料图号");
		row1.createCell(4).setCellValue("检验类型");
		row1.createCell(5).setCellValue("标准描述");
		row1.createCell(6).setCellValue("质检项");
		row1.createCell(7).setCellValue("标准值");
		row1.createCell(8).setCellValue("上偏差值");
		row1.createCell(9).setCellValue("下偏差值");
		row1.createCell(10).setCellValue("单位");
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		int i = 1;
		int startRow;
		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		for (CheckStandard cs : csList) {
			int itemCount = 0;
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			startRow = i;
			List<CheckStandardItem> itemList = cs.getItemList();
			if (!CollectionUtils.isEmpty(itemList)) {
				itemCount = itemList.size();
			} else {
				itemCount = 0;
				i++;
			}

			String materialTypeName = "";
			if (cs.getMaterialType() == 1) {
				materialTypeName = "刀具";
			} else if (cs.getMaterialType() == 2) {
				materialTypeName = "夹具";
			} else {
				materialTypeName = "刀条组合";
			}
			dataRow.createCell(0).setCellValue(materialTypeName);
			dataRow.createCell(1).setCellValue(cs.getMaterialNumber());
			dataRow.createCell(2).setCellValue(cs.getMaterialName());
			dataRow.createCell(3).setCellValue(cs.getMaterialMap());
			Integer chType = cs.getCheckType();
			String typeName = "";
			if (chType == 1) {
				typeName = "新刀质检";
			} else if (chType == 2 || chType == 7) {
				typeName = "刃磨质检";
			} else if (chType == 3 || chType == 8) {
				typeName = "涂层质检";
			} else if (chType == 4) {
				typeName = "新夹具质检";
			} else if (chType == 5) {
				typeName = "夹具修磨质检";
			} else if (chType == 6) {
				typeName = "夹具点检";
			}
			dataRow.createCell(4).setCellValue(typeName);
			dataRow.createCell(5).setCellValue(cs.getStandardDesc());

			for (int j = 0; j < itemCount; j++) {
				if (j > 0) {
					dataRow = sheet.createRow(i);
					dataRow.setHeight((short) (25 * 20));// 目的是想把行高设置成25px
				}
				CheckStandardItem item = itemList.get(j);
				dataRow.createCell(6).setCellValue(item.getCheckItem());
				dataRow.createCell(7).setCellValue(item.getItemStandard());
				if (null != item.getUpDeviation()) {
					dataRow.createCell(8).setCellValue(item.getUpDeviation().toString());
				}
				if (null != item.getDownDeviation()) {
					dataRow.createCell(9).setCellValue(item.getDownDeviation().toString());
				}
				dataRow.createCell(10).setCellValue(item.getUnit());
				i++;
			}
			if (itemCount <= 1) {
				continue;
			}
			int endRow = startRow + itemCount - 1;
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 2, 2));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 3, 3));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 4, 4));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 5, 5));
		}
		// 输出Excel文件
		String fileName = "质检标准.xls";
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

	@ApiOperation(value = "质检项导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "质检项导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/check-standard-item-import")
	@Secure()
	public AjaxReturn standardItemImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<CheckStandardItem> itemList = ExcelUtils.getInstance().readExcel2Objects(inputStream, CheckStandardItem.class, 1, 5000, 0);
		inputStream.close();
		return new AjaxReturn(200, null, itemList);
	}

}
