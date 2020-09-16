package org.tsinghuatj.web.controller.fixture;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
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
import org.odftoolkit.odfdom.converter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tsinghuatj.base.domain.Supplier;
import org.tsinghuatj.fixture.domain.FixtureBase;
import org.tsinghuatj.fixture.domain.FixtureBaseChild;
import org.tsinghuatj.fixture.service.IFixtureBaseService;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.JsonUtils;
import org.tsinghuatj.framework.utils.excel.PoiExcelUtil;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;

import com.github.pagehelper.util.StringUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/fixture" })
public class FixtureBaseController extends BaseController {

	private @Autowired(required = false) IFixtureBaseService fixtureBaseService;

	/**
	 * 夹具新增
	 */
	@ApiOperation(value = "夹具新增", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具入库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-base-add")
	@OperateLog(info = "夹具基础新增[夹具码:%s->夹具名称:%s]", params = { "fixtureNumber", "fixtureName" })
	public AjaxReturn fixtureBaseAdd(@ApiParam(name = "fixtureNumber", value = "夹具编码", required = true) @RequestParam(required = true) String fixtureNumber, @ApiParam(name = "fixtureName", value = "夹具名称", required = false) @RequestParam(required = false) String fixtureName, @ApiParam(name = "fixtureMap", value = "图号", required = false) @RequestParam(required = false) String fixtureMap, @ApiParam(name = "fixtureType", value = "类型", required = false) @RequestParam(required = false) Integer fixtureType, @ApiParam(name = "price", value = "价格", required = false) @RequestParam(required = false) BigDecimal price,
			@ApiParam(name = "childList", value = "配件", required = false) @RequestParam(required = false) String childList) throws BusinessException {
		log.debug("FixtureBaseController.fixtureBaseAdd<<<");
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		FixtureBase fixtureBase = new FixtureBase();
		fixtureBase.setFixtureName(fixtureName);
		fixtureBase.setFixtureNumber(fixtureNumber);
		fixtureBase.setFixtureMap(fixtureMap);
		fixtureBase.setFixtureType(fixtureType);
		fixtureBase.setPrice(price);
		if (fixtureType == 1) {
			List<FixtureBaseChild> items = JsonUtils.json2list(childList, FixtureBaseChild.class);
			fixtureBase.setChildList(items);
		}
		return new AjaxReturn(200, null, fixtureBaseService.insert(user.getId(), fixtureBase));
	}

	/**
	 * 夹具删除
	 */
	@ApiOperation(value = "夹具删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-base-delete-by-id")
	@OperateLog(info = "夹具基础删除[夹具编号:%s]", params = { "fixtureNumber" })
	public AjaxReturn fixtureBaseDeleteById(@ApiParam(name = "fixtureNumber", value = "夹具编码", required = true) @RequestParam(required = true) String fixtureNumber, @ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId > 0);
		return new AjaxReturn(200, null, fixtureBaseService.deleteById(userId, pkId, fixtureNumber));
	}

	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "夹具id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具Id查询列表查询成功", response = FixtureBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-base-get-by-id")
	public AjaxReturn fixtureBaseGetById(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "postType", value = "请求方式1-采购请求", required = false) @RequestParam(required = false) Integer postType) throws BusinessException {

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId >= 1);

		return new AjaxReturn(200, null, fixtureBaseService.selectById(curuserId, pkId, postType));
	}

	/**
	 * 根据编码查找
	 */
	@ApiOperation(value = "夹具基础信息列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具基础信息列表查询成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-base-get-by-number")
	public AjaxReturn fixtureBaseGetByNumber(@ApiParam(name = "fixtureNumber", value = "夹具编码", required = true) @RequestParam(required = true) String fixtureNumber) throws BusinessException {
		log.debug("FixtureBaseController.toolBaseGetByNumber<<<");

		// 获取当前用户
		Long userId = getAuthentication();
		log.debug("FixtureBaseController.toolBaseGetByNumber>>>");
		return new AjaxReturn(200, null, fixtureBaseService.selectByNumber(userId, fixtureNumber));
	}

	/**
	 * 夹具基础信息分页列表
	 */
	@ApiOperation(value = "夹具基础信息分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具基础信息分页列表查询成功", response = FixtureBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-base-page-list")
	public AjaxReturn fixtureBasePageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "fixtureNumber", value = "夹具编码", required = false) @RequestParam(required = false) String fixtureNumber, @ApiParam(name = "fixtureName", value = "夹具名称", required = false) @RequestParam(required = false) String fixtureName, @ApiParam(name = "fixtureMap", value = "图号", required = false) @RequestParam(required = false) String fixtureMap, @ApiParam(name = "fixtureType", value = "价格", required = false) @RequestParam(required = false) Integer fixtureType) throws BusinessException {
		log.debug("FixtureBaseController.toolBasePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);

		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(userId > 0);
		Validate.isTrue(page >= 0);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);

		FixtureBase fixtureBase = new FixtureBase();
		fixtureBase.setFixtureName(fixtureName);
		fixtureBase.setFixtureNumber(fixtureNumber);
		fixtureBase.setFixtureMap(fixtureMap);
		fixtureBase.setFixtureType(fixtureType);

		Pagination<FixtureBase> pagination = fixtureBaseService.selectPageList(userId, fixtureBase, queryDto);
		log.debug("FixtureBaseController.toolBasePageList>>>");
		return pagination;
	}

	@ApiOperation(value = "夹具基础信息", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具基础信息分页列表查询成功", response = FixtureBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/get-base-list")
	public AjaxReturn getBaseList(@ApiParam(name = "fixtureNumber", value = "夹具编码", required = false) @RequestParam(required = false) String fixtureNumber, @ApiParam(name = "fixtureMap", value = "图号", required = false) @RequestParam(required = false) String fixtureMap, @ApiParam(name = "fixtureType", value = "夹具类型1-组合2-配件", required = false) @RequestParam(required = false) Integer fixtureType

	) throws BusinessException {

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(userId > 0);

		FixtureBase fixtureBase = new FixtureBase();
		fixtureBase.setFixtureNumber(fixtureNumber);
		fixtureBase.setFixtureMap(fixtureMap);
		fixtureBase.setFixtureType(fixtureType);
		return new AjaxReturn(200, null, fixtureBaseService.select(userId, fixtureBase));
	}

	/**
	 * 夹具更新
	 */
	@ApiOperation(value = "夹具更新", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具更新成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-base-update")
	@OperateLog(info = "夹具入库[夹具码:%s->夹具名称:%s]", params = { "fixtureNumber", "fixtureName" })
	public AjaxReturn fixtureBaseUpdate(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "fixtureNumber", value = "夹具编码", required = false) @RequestParam(required = false) String fixtureNumber, @ApiParam(name = "fixtureName", value = "夹具名称", required = false) @RequestParam(required = false) String fixtureName, @ApiParam(name = "fixtureMap", value = "图号", required = false) @RequestParam(required = false) String fixtureMap, @ApiParam(name = "fixtureType", value = "类型", required = false) @RequestParam(required = false) Integer fixtureType, @ApiParam(name = "price", value = "价格", required = false) @RequestParam(required = false) BigDecimal price,
			@ApiParam(name = "childList", value = "配件", required = false) @RequestParam(required = false) String childList) throws BusinessException {
		log.debug("FixtureBaseController.toolAdd<<<");
		// 获取当前用户
		Long userId = getAuthentication();

		// 封装参数信息
		FixtureBase fixtureBase = new FixtureBase();
		fixtureBase.setFixtureName(fixtureName);
		fixtureBase.setFixtureNumber(fixtureNumber);
		fixtureBase.setFixtureMap(fixtureMap);
		fixtureBase.setPrice(price);
		fixtureBase.setFixtureType(fixtureType);
		fixtureBase.setPkId(pkId);
		if (fixtureType == 1) {
			List<FixtureBaseChild> items = JsonUtils.json2list(childList, FixtureBaseChild.class);
			fixtureBase.setChildList(items);
		}
		return new AjaxReturn(200, null, fixtureBaseService.updateActiveById(userId, fixtureBase, pkId));
	}

	/**
	 * 夹具基础信息导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "夹具基础信息导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具基础信息导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-base-import")
	@Secure()
	@OperateLog(info = "夹具基础信息导入", params = {})
	public AjaxReturn fixtureBaseImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();

		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		fixtureBaseService.fixtureBaseImport(userId, read(file.getOriginalFilename(), inputStream));
		return new AjaxReturn(200, null, 1);
	}

	private List<FixtureBase> read(String fileName, InputStream inputStream) throws IOException {
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

		List<FixtureBase> fbList = new ArrayList<FixtureBase>();
		String cellValue;
		for (int i = 1; i < count; i++) {
			Row row = sheet.getRow(i);
			FixtureBase fb = new FixtureBase();
			String FixtureNumber = poiExcelUtil.getCellValue(row.getCell(0));
			fb.setFixtureNumber(FixtureNumber);
			fb.setFixtureName(poiExcelUtil.getCellValue(row.getCell(1)));
			fb.setFixtureMap(poiExcelUtil.getCellValue(row.getCell(2)));
			String type = poiExcelUtil.getCellValue(row.getCell(3));
			if ("组合".equals(type)) {
				fb.setFixtureType(1);
			} else {
				fb.setFixtureType(2);
			}
			cellValue = poiExcelUtil.getCellValue(row.getCell(4));
			if (StringUtil.isNotEmpty(cellValue)) {
				fb.setPrice(new BigDecimal(cellValue));
			}

			List<FixtureBaseChild> items = new ArrayList<FixtureBaseChild>();
			if (poiExcelUtil.isMergedRegion(sheet, i, 0)) {
				int lastRow = poiExcelUtil.getRowNum(cras, sheet.getRow(i).getCell(0), sheet);
				for (; i <= lastRow; i++) {
					row = sheet.getRow(i);
					FixtureBaseChild item = new FixtureBaseChild();
					item.setFixtureNumber(poiExcelUtil.getCellValue(row.getCell(5)));
					item.setFixtureName(poiExcelUtil.getCellValue(row.getCell(6)));
					item.setFixtureMap(poiExcelUtil.getCellValue(row.getCell(7)));
					item.setParentNumber(FixtureNumber);
					cellValue = poiExcelUtil.getCellValue(row.getCell(8));
					if (StringUtil.isNotEmpty(cellValue)) {
						try {
							item.setFixtureQty(new Integer(cellValue));
						} catch (NumberFormatException e) {
							item.setFixtureQty(new BigDecimal(cellValue).intValue());
						}
					}
					items.add(item);
				}
				i--;
			} else {
				row = sheet.getRow(i);
				FixtureBaseChild item = new FixtureBaseChild();
				item.setFixtureNumber(poiExcelUtil.getCellValue(row.getCell(5)));
				item.setFixtureName(poiExcelUtil.getCellValue(row.getCell(6)));
				item.setFixtureMap(poiExcelUtil.getCellValue(row.getCell(7)));
				item.setParentNumber(FixtureNumber);
				cellValue = poiExcelUtil.getCellValue(row.getCell(8));
				if (StringUtil.isNotEmpty(cellValue)) {
					if (StringUtil.isNotEmpty(cellValue)) {
						try {
							item.setFixtureQty(new Integer(cellValue));
						} catch (NumberFormatException e) {
							item.setFixtureQty(new BigDecimal(cellValue).intValue());
						}
					}
				}
				items.add(item);
			}
			fb.setChildList(items);
			fbList.add(fb);
		}
		wb.close();
		inputStream.close();
		return fbList;
	}

	@ApiResponses({ @ApiResponse(code = 200, message = "夹具基础导出", response = Supplier.class) })
	@Secure()
	@GetMapping(path = { "/fixture-base-export" })
	public String fixtureBaseExport(HttpServletRequest request, HttpServletResponse response, @ApiParam(name = "fixtureType", value = "夹具类型", required = false) @RequestParam(required = false) Integer fixtureType) throws Exception {

		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(100000);

		FixtureBase fixtureBase = new FixtureBase();
		fixtureBase.setFixtureType(fixtureType);

		Long userId = getAuthentication();
		Pagination<FixtureBase> pagination = fixtureBaseService.selectPageList(userId, fixtureBase, queryDto);
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("夹具基础");
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("夹具编码");
		row1.createCell(1).setCellValue("夹具名称");
		row1.createCell(2).setCellValue("夹具图号");
		row1.createCell(3).setCellValue("夹具类型");
		row1.createCell(4).setCellValue("夹具价格");
		row1.createCell(5).setCellValue("配件编号");
		row1.createCell(6).setCellValue("配件名称");
		row1.createCell(7).setCellValue("配件图号");
		row1.createCell(8).setCellValue("标准数量");

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		int i = 1;
		int startRow;
		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		for (FixtureBase fixturebase : pagination.getRows()) {

			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//

			dataRow.createCell(0).setCellValue(fixturebase.getFixtureNumber());
			dataRow.createCell(1).setCellValue(fixturebase.getFixtureName());
			dataRow.createCell(2).setCellValue(fixturebase.getFixtureMap());
			Integer fixType = fixturebase.getFixtureType();
			String typeName = "";
			if (fixType == 1) {
				typeName = "组合";
			} else if (fixType == 2) {
				typeName = "配件";
			}

			dataRow.createCell(3).setCellValue(typeName);
			if (null != fixturebase.getPrice()) {
				dataRow.createCell(4).setCellValue(fixturebase.getPrice().toString());
			}

			int itemCount = 0;
			List<FixtureBaseChild> childList = fixturebase.getChildList();
			if (!CollectionUtils.isEmpty(childList)) {
				itemCount = childList.size();
			} else {
				itemCount = 0;
				i++;
			}

			startRow = i;
			for (int j = 0; j < itemCount; j++) {
				if (j > 0) {
					dataRow = sheet.createRow(i);
					dataRow.setHeight((short) (25 * 20));// 目的是想把行高设置成25px
				}
				FixtureBaseChild item = childList.get(j);
				dataRow.createCell(5).setCellValue(item.getFixtureNumber());
				dataRow.createCell(6).setCellValue(item.getFixtureName());
				dataRow.createCell(7).setCellValue(item.getFixtureMap());
				dataRow.createCell(8).setCellValue(item.getFixtureQty());
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

		}
		String fileName = "夹具基础信息.xls";
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

	/**
	 * 夹具基础信息信息同步
	 */
	@ApiOperation(value = "夹具基础信息信息同步", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具基础信息信息同步", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-base-synchro")
	@OperateLog(info = "夹具基础信息同步[夹具码:%s]", params = { "fixtureNumber" })
	public AjaxReturn FixtureBaseSynchro(@ApiParam(name = "fixtureNumber", value = "夹具编码", required = true) @RequestParam(required = true) String fixtureNumber) throws BusinessException {
		// 获取当前用户
		Long userId = getAuthentication();
		return new AjaxReturn(200, null, fixtureBaseService.fixtureBaseSynchro(userId, fixtureNumber));
	}

	/**
	 * 申购时根据编号查找
	 */
	@ApiOperation(value = "根据编码查找", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据编码查找", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/purchase-get-by-fixture-number")
	public AjaxReturn purchaseGetByFixtureNumber(@ApiParam(name = "fixtureNumber", value = "fixtureNumber", required = true) @RequestParam(required = true) String fixtureNumber) throws BusinessException {
		log.debug("FixtureBaseController.purchaseGetByFixtureNumber<<<");
		if (log.isDebugEnabled()) {
			log.debug("fixtureNumber:" + fixtureNumber);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(StringUtils.isNotEmpty(fixtureNumber));

		log.debug("FixtureBaseController.purchaseGetByFixtureNumber>>>");
		return new AjaxReturn(200, null, fixtureBaseService.purchaseGetByFixtureNumber(curuserId, fixtureNumber));
	}

}
