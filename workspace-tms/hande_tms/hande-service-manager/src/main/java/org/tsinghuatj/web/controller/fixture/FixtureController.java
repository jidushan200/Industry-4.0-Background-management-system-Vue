package org.tsinghuatj.web.controller.fixture;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tsinghuatj.fixture.domain.Fixture;
import org.tsinghuatj.fixture.domain.FixtureChild;
import org.tsinghuatj.fixture.domain.FixtureMaintain;
import org.tsinghuatj.fixture.service.IFixtureMaintainService;
import org.tsinghuatj.fixture.service.IFixtureService;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.utils.excel.PoiExcelUtil;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolBase;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/fixture" })
public class FixtureController extends BaseController {
	private @Autowired(required = false) IFixtureService fixtureService;

	private @Autowired(required = false) IFixtureMaintainService maintainService;

	/**
	 * 新夹具入库
	 */
	@ApiOperation(value = "新夹具入库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具入库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-add")
	@OperateLog(info = "新夹具入库[夹具码:%s]", params = { "fixtureBarcode" })
	public AjaxReturn fixtureAdd(
			@ApiParam(name = "storeHouse", value = "库位", required = false) @RequestParam(required = false) String storeHouse,
			@ApiParam(name = "receiptId", value = "收货单id", required = false) @RequestParam(required = false) Long receiptId,
			@ApiParam(name = "departmentId", value = "部门id", required = false) @RequestParam(required = false) Long departmentId,
			@ApiParam(name = "departmentName", value = "部门名称", required = false) @RequestParam(required = false) String departmentName

	) throws BusinessException {
		log.debug("FixtureController.fixtureAdd<<<");
		// 获取当前用户
		Long userId = getAuthentication();
		Fixture fixture = new Fixture();
		fixture.setDepartmentId(departmentId);
		fixture.setDepartmentName(departmentName);
		fixture.setStoreHouse(storeHouse);
		return new AjaxReturn(200, null, fixtureService.insert(userId, receiptId, fixture));
	}

	/**
	 * 待检夹具列表
	 */
	@ApiOperation(value = "待检夹具", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "待检夹具", response = Fixture.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/compose-page-list")
	public AjaxReturn waitrepairPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "fixtureBarcode", value = "物料条码", required = false) @RequestParam(required = false) String fixtureBarcode,
			@ApiParam(name = "fixtureName", value = "物料名称", required = false) @RequestParam(required = false) String fixtureName,
			@ApiParam(name = "fixtureMap", value = "物料图号", required = false) @RequestParam(required = false) String fixtureMap,
			@ApiParam(name = "fixtureStatus", value = "夹具状态", required = false) @RequestParam(required = false) Integer fixtureStatus

	) throws BusinessException {
		log.debug("FixtureController.waitrepairPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 0);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);

		Fixture where = new Fixture();
		where.setFixtureBarcode(fixtureBarcode);
		where.setFixtureMap(fixtureMap);
		where.setFixtureName(fixtureName);
		where.setFixtureStatus(fixtureStatus);
		Pagination<Fixture> pagination = fixtureService.selectPageList(userId, where, queryDto);
		log.debug("FixtureController.waitrepairPageList>>>");
		return pagination;
	}

	/**
	 * 夹具分页查询
	 */
	@ApiOperation(value = "夹具分页查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具分页查询", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-page-list")
	public AjaxReturn fixturePageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "fixtureBarcode", value = "夹具条码", required = false) @RequestParam(required = false) String fixtureBarcode,
			@ApiParam(name = "fixtureNumber", value = "夹具编码", required = false) @RequestParam(required = false) String fixtureNumber,
			@ApiParam(name = "fixtureName", value = "夹具名称", required = false) @RequestParam(required = false) String fixtureName,
			@ApiParam(name = "fixtureMap", value = "图号", required = false) @RequestParam(required = false) String fixtureMap,
			@ApiParam(name = "fixtureStatus", value = "夹具状态", required = false) @RequestParam(required = false) Integer fixtureStatus,
			@ApiParam(name = "departmentId", value = "部门id", required = false) @RequestParam(required = false) Long departmentId,
			@ApiParam(name = "inUse", value = "是否在用", required = false) @RequestParam(required = false) Integer inUse)
			throws BusinessException {

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

		Fixture fixture = new Fixture();
		fixture.setFixtureBarcode(fixtureBarcode);
		fixture.setFixtureName(fixtureName);
		fixture.setFixtureNumber(fixtureNumber);
		fixture.setFixtureMap(fixtureMap);
		fixture.setFixtureStatus(fixtureStatus);
		fixture.setInUse(inUse);
		fixture.setDepartmentId(departmentId);
		Pagination<Fixture> pagination = fixtureService.selectPageList(userId, fixture, queryDto);
		return pagination;
	}

	@ApiOperation(value = "夹具配件查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具配件查询", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-get-part-list")
	public AjaxReturn getFixturePartList(
			@ApiParam(name = "fixtureNumber", value = "夹具编码", required = false) @RequestParam(required = false) String fixtureNumber,
			@ApiParam(name = "parentBarcode", value = "夹具组合码", required = false) @RequestParam(required = false) String parentBarcode,
			@ApiParam(name = "departmentId", value = "部门id", required = false) @RequestParam(required = false) Long departmentId,
			@ApiParam(name = "inUse", value = "是否在用", required = false) @RequestParam(required = false) Integer inUse)
			throws BusinessException {

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(userId > 0);
		// 封装查询条件
		Fixture fixture = new Fixture();
		fixture.setFixtureNumber(fixtureNumber);
		fixture.setFixtureBarcode(parentBarcode);
		fixture.setInUse(2);
		fixture.setDepartmentId(departmentId);
		return new AjaxReturn(200, null, fixtureService.selectPartList(userId, fixture));
	}

	@ApiOperation(value = "夹具配件替换", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具配件替换", response = AjaxReturn.class) })
	@OperateLog(info = "夹具配件替换[原夹具条码:%s->新夹具条码:%s]", params = { "oldBarcode", "fixtrueBarcode" })
	@RequestMapping(method = RequestMethod.POST, value = "/replace-fixtrue")
	public AjaxReturn replaceFixture(
			@ApiParam(name = "fixtureBarcode", value = "配件条码", required = false) @RequestParam(required = false) String fixtureBarcode,
			@ApiParam(name = "parentBarcode", value = "夹具组合码", required = false) @RequestParam(required = false) String parentBarcode,
			@ApiParam(name = "oldParentBarcode", value = "原组合条码", required = false) @RequestParam(required = false) String oldParentBarcode,
			@ApiParam(name = "oldBarcode", value = "原配件条码", required = false) @RequestParam(required = false) String oldBarcode,
			@ApiParam(name = "oldNumber", value = "原配件编码", required = false) @RequestParam(required = false) String oldNumber)
			throws BusinessException {

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(userId > 0);
		// 封装查询条件
		Fixture fixture = new Fixture();
		fixture.setInUse(2);
		//
		return new AjaxReturn(200, null,
				fixtureService.replace(userId, parentBarcode, fixtureBarcode, oldParentBarcode, oldBarcode, oldNumber));
	}

	/**
	 * 新夹具领用出库
	 */
	@ApiOperation(value = "夹具领用出库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具领用成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-use")
	@OperateLog(info = "夹具领用出库[夹具条码:%s->领用人工号:%s]", params = { "fixtureBarcode", "staffCode" })
	public AjaxReturn fixtureUse(
			@ApiParam(name = "pkId", value = "夹具id", required = false) @RequestParam(required = false) Long pkId,
			@ApiParam(name = "fixtureBarcode", value = "夹具条码", required = false) @RequestParam(required = false) String fixtureBarcode,
			@ApiParam(name = "staffCode", value = "领用人工号", required = false) @RequestParam(required = false) String staffCode,
			@ApiParam(name = "staffName", value = "领用人名称", required = false) @RequestParam(required = false) String staffName,
			@ApiParam(name = "equipmentId", value = "设备id", required = false) @RequestParam(required = false) Long equipmentId,
			@ApiParam(name = "equipmentName", value = "设备名称", required = false) @RequestParam(required = false) String equipmentName,
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false) String remark

	) throws BusinessException {
		log.debug("FixtureController.fixtureOutbound<<<");
		// 获取当前用户
		Long userId = getAuthentication();
		Fixture fixture = new Fixture();
		fixture.setPkId(pkId);
		fixture.setFixtureBarcode(fixtureBarcode);
		fixture.setStaffCode(staffCode);
		fixture.setStaffName(staffName);
		fixture.setEquipmentId(equipmentId);
		fixture.setEquipmentName(equipmentName);
		return new AjaxReturn(200, null, fixtureService.use(userId, fixture, remark));
	}

	@ApiOperation(value = "夹具返仓库", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具返库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-return-warehouse")
	@OperateLog(info = "夹具返仓库[夹具条码:%s]", params = { "fixtureBarcode" })
	public AjaxReturn fixtureReturnWarehouse(
			@ApiParam(name = "pkId", value = "夹具id", required = false) @RequestParam(required = false) Long pkId,
			@ApiParam(name = "fixtureBarcode", value = "夹具条码", required = false) @RequestParam(required = false) String fixtureBarcode,
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false) String remark

	) throws BusinessException {
		log.debug("FixtureController.fixtureReturnWarehouse<<<");
		// 获取当前用户
		Long userId = getAuthentication();
		Fixture fixture = new Fixture();
		fixture.setPkId(pkId);
		fixture.setFixtureBarcode(fixtureBarcode);
		return new AjaxReturn(200, null, fixtureService.returnWarehouse(userId, fixture, remark));
	}

	@ApiOperation(value = "夹具质检取回", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具返库成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/go-back")
	@OperateLog(info = "夹具质检取回[夹具条码:%s]", params = { "fixtureBarcode" })
	public AjaxReturn fixtureGoBack(
			@ApiParam(name = "checkId", value = "夹具id", required = false) @RequestParam(required = false) Long checkId,
			@ApiParam(name = "fixtureBarcode", value = "夹具条码", required = false) @RequestParam(required = false) String fixtureBarcode

	) throws BusinessException {
		log.debug("FixtureController.fixtureGoBack<<<");
		// 获取当前用户
		Long userId = getAuthentication();
		Fixture fixture = new Fixture();
		fixture.setFixtureBarcode(fixtureBarcode);
		fixture.setFixtureStatus(1);
		fixture.setInUse(2);
		return new AjaxReturn(200, null, fixtureService.goBack(userId, fixture, checkId));
	}

	@ApiOperation(value = "夹具送修磨", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具送修磨", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/set-repair")
	@OperateLog(info = "夹具送修磨[夹具条码:%s]", params = { "fixtureBarcode" })
	public AjaxReturn fixtureSetRepair(
			@ApiParam(name = "pkId", value = "夹具id", required = false) @RequestParam(required = false) Long pkId,
			@ApiParam(name = "checkId", value = "检验单id", required = false) @RequestParam(required = false) Long checkId,
			@ApiParam(name = "fixtureBarcode", value = "夹具条码", required = false) @RequestParam(required = false) String fixtureBarcode

	) throws BusinessException {
		log.debug("FixtureController.fixtureSetRepair<<<");
		// 获取当前用户
		Long userId = getAuthentication();
		Fixture fixture = new Fixture();
		fixture.setPkId(pkId);
		fixture.setFixtureBarcode(fixtureBarcode);
		return new AjaxReturn(200, null, fixtureService.setRepair(userId, fixture, checkId));
	}

	/**
	 * 夹具保养分页查询
	 */
	@ApiOperation(value = "夹具保养分页查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具保养分页查询", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/maintain-page-list")
	public AjaxReturn maintainPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "fixtureBarcode", value = "夹具条码", required = false) @RequestParam(required = false) String fixtureBarcode,
			@ApiParam(name = "fixtureName", value = "夹具名称", required = false) @RequestParam(required = false) String fixtureName,
			@ApiParam(name = "fixtureMap", value = "夹具图号", required = false) @RequestParam(required = false) String fixtureMap)
			throws BusinessException {

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
		FixtureMaintain maintain = new FixtureMaintain();
		maintain.setFixtureBarcode(fixtureBarcode);
		maintain.setFixtureName(fixtureName);
		maintain.setFixtureMap(fixtureMap);
		Pagination<FixtureMaintain> pagination = maintainService.selectPageList(userId, maintain, queryDto);
		return pagination;
	}

	@ApiOperation(value = "夹具保养导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具保养导出", response = ToolBase.class) })
	@Secure()
	@GetMapping(path = { "/maintain-export" })
	public ResponseEntity<byte[]> maintainExport(
			@ApiParam(name = "fixtureBarcode", value = "夹具条码", required = false) @RequestParam(required = false) String fixtureBarcode,
			@ApiParam(name = "fixtureName", value = "夹具名称", required = false) @RequestParam(required = false) String fixtureName,
			@ApiParam(name = "fixtureMap", value = "夹具图号", required = false) @RequestParam(required = false) String fixtureMap)
			throws Exception {
		Long userId = getAuthentication();
		FixtureMaintain maintain = new FixtureMaintain();
		maintain.setFixtureBarcode(fixtureBarcode);
		maintain.setFixtureName(fixtureName);
		maintain.setFixtureMap(fixtureMap);
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(500000);

		Pagination<FixtureMaintain> pagination = maintainService.selectPageList(userId, maintain, queryDto);
		for (FixtureMaintain item : pagination.getRows()) {
			if (item.getUseStatus() == 1) {
				item.setUseStatusName("正常使用");
			} else if (item.getUseStatus() == 2) {
				item.setUseStatusName("修磨");
			} else if (item.getUseStatus() == 3) {
				item.setUseStatusName("建议报废");
			}
		}
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(
				(List<FixtureMaintain>) pagination.getRows(), FixtureMaintain.class, true, null, true);
		return getResponseEntity(data, "夹具保养记录.xlsx");
	}

	/**
	 * 夹具删除
	 */
	@ApiOperation(value = "夹具删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具报废", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-delete-by-id")
	@OperateLog(info = "夹具报废[夹具条码%s]", params = { "fixtureBarcode" })
	public AjaxReturn fixtureDeleteById(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId)
			throws BusinessException {
		log.debug("FixtureController.fixtureDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);
		log.debug("FixtureController.fixtureDeleteById>>>");
		return new AjaxReturn(200, null, fixtureService.deleteById(user.getId(), pkId));
	}

	/**
	 * 根据完整编码查找
	 */
	@ApiOperation(value = "根据编码查找", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "根据编码查找", response = Tool.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-get-by-full-number")
	public AjaxReturn fixtureGetByFullNumber(
			@ApiParam(name = "fullNumber", value = "fullNumber", required = true) @RequestParam(required = true) String fullNumber)
			throws BusinessException {
		log.debug("FixtureController.fixtureGetByFullNumber<<<");
		if (log.isDebugEnabled()) {
			log.debug("fullNumber:" + fullNumber);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(StringUtils.isNotEmpty(fullNumber));

		log.debug("ToolController.fixtureGetByFullNumber>>>");
		return new AjaxReturn(200, null, fixtureService.selectByFullNumber(curuserId, fullNumber));
	}

	/**
	 * 夹具保养
	 */
	@ApiOperation(value = "夹具保养", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具保养", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/maintain-add")
	@OperateLog(info = "夹具保养[夹具条码:%s->夹具保养状态:%s]", params = { "fixtureBarcode", "maintainStatus" })
	public AjaxReturn maintainAdd(
			@ApiParam(name = "pkId", value = "夹具id", required = false) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "fixtureBarcode", value = "夹具条码", required = false) @RequestParam(required = false) String fixtureBarcode,
			@ApiParam(name = "fixtureName", value = "夹具名称", required = false) @RequestParam(required = false) String fixtureName,
			@ApiParam(name = "maintainItem", value = "保养项目", required = false) @RequestParam(required = false) String maintainItem,
			@ApiParam(name = "useStatus", value = "使用状态", required = false) @RequestParam(required = false) Integer useStatus,
			@ApiParam(name = "maintainStatus", value = "单据状态", required = false) @RequestParam(required = false) Integer maintainStatus,
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false) String remark

	) throws BusinessException {
		log.debug("FixtureController.maintainAdd<<<");
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();
		FixtureMaintain fixtureMaintain = new FixtureMaintain();
		fixtureMaintain.setFixtureId(pkId);
		fixtureMaintain.setFixtureBarcode(fixtureBarcode);
		fixtureMaintain.setFixtureName(fixtureName);
		fixtureMaintain.setMaintainItem(maintainItem);
		fixtureMaintain.setUseStatus(useStatus);
		fixtureMaintain.setMaintainStatus(maintainStatus);
		fixtureMaintain.setRemark(remark);
		return new AjaxReturn(200, null, maintainService.insert(user.getId(), user.getRealname(), fixtureMaintain));
	}

	/**
	 * 夹具台账信息导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "夹具台账信息导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具台账信息导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-import")
	@Secure()
	public AjaxReturn fixtureImport(@RequestParam(value = "file", required = true) MultipartFile file)
			throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		fixtureService.importFixture(userId, read(file.getOriginalFilename(), inputStream));
		return new AjaxReturn(200, null, 1);
	}

	private List<Fixture> read(String fileName, InputStream inputStream) throws IOException {
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

		List<Fixture> fbList = new ArrayList<Fixture>();
		String cellValue;
		for (int i = 1; i < count; i++) {
			Row row = sheet.getRow(i);
			Fixture fb = new Fixture();

			fb.setFixtureNumber(poiExcelUtil.getCellValue(row.getCell(0)));
			fb.setFixtureName(poiExcelUtil.getCellValue(row.getCell(1)));
			fb.setFixtureMap(poiExcelUtil.getCellValue(row.getCell(2)));
			String type = poiExcelUtil.getCellValue(row.getCell(3));
			if ("组合".equals(type)) {
				fb.setFixtureType(1);
			} else {
				fb.setFixtureType(2);
			}
			String fixtureBarcode = poiExcelUtil.getCellValue(row.getCell(4));
			fb.setFixtureBarcode(fixtureBarcode);
			cellValue = poiExcelUtil.getCellValue(row.getCell(5));
			fb.setDepartmentName(cellValue);

			List<FixtureChild> items = new ArrayList<FixtureChild>();
			if (poiExcelUtil.isMergedRegion(sheet, i, 0)) {
				int lastRow = poiExcelUtil.getRowNum(cras, sheet.getRow(i).getCell(0), sheet);
				for (; i <= lastRow; i++) {
					row = sheet.getRow(i);
					FixtureChild item = new FixtureChild();
					item.setFixtureNumber(poiExcelUtil.getCellValue(row.getCell(6)));
					item.setFixtureName(poiExcelUtil.getCellValue(row.getCell(7)));
					item.setFixtureMap(poiExcelUtil.getCellValue(row.getCell(8)));
					item.setFixtureBarcode(poiExcelUtil.getCellValue(row.getCell(9)));
					item.setParentBarcode(fixtureBarcode);
					items.add(item);
				}
				i--;
			} else {
				row = sheet.getRow(i);
				FixtureChild item = new FixtureChild();
				item.setFixtureNumber(poiExcelUtil.getCellValue(row.getCell(6)));
				item.setFixtureName(poiExcelUtil.getCellValue(row.getCell(7)));
				item.setFixtureMap(poiExcelUtil.getCellValue(row.getCell(8)));
				item.setFixtureBarcode(poiExcelUtil.getCellValue(row.getCell(9)));
				item.setParentBarcode(fixtureBarcode);
				items.add(item);
			}
			fb.setChildList(items);
			fbList.add(fb);
		}
		wb.close();
		inputStream.close();
		return fbList;
	}

	@ApiOperation(value = "夹具台账信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具台账信息表导出", response = String.class) })
	@Secure()
	@GetMapping(path = { "/fixture-export" })
	public String downloadFixtrueExcel(HttpServletRequest request,HttpServletResponse response,
			@ApiParam(name = "departmentId", value = "部门id", required = false) @RequestParam(required = false) Long departmentId,
			@ApiParam(name = "fixtureNumber", value = "夹具编码", required = false) @RequestParam(required = false) String fixtureNumber,			
			@ApiParam(name = "fixtureMap", value = "图号", required = false) @RequestParam(required = false) String fixtureMap,
			@ApiParam(name = "fixtureType", value = "夹具类型", required = false) @RequestParam(required = false) Integer fixtureType)
			throws Exception {
		Long userId = getAuthentication();
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(100000);

		Fixture where = new Fixture();
		where.setFixtureType(fixtureType);
		where.setDepartmentId(departmentId);
		where.setFixtureNumber(fixtureNumber);
		where.setFixtureMap(fixtureMap);
		Pagination<Fixture> pagination = fixtureService.selectPageList(userId, where, queryDto);
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("夹具台账");
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("夹具编码");
		row1.createCell(1).setCellValue("夹具名称");
		row1.createCell(2).setCellValue("夹具图号");
		row1.createCell(3).setCellValue("夹具类型");
		row1.createCell(4).setCellValue("夹具条码");
		row1.createCell(5).setCellValue("所属部门");

		row1.createCell(6).setCellValue("配件编号");
		row1.createCell(7).setCellValue("配件名称");
		row1.createCell(8).setCellValue("配件图号");
		row1.createCell(9).setCellValue("配件条码");

		row1.createCell(10).setCellValue("机床");
		row1.createCell(11).setCellValue("领用人");
		row1.createCell(12).setCellValue("保养次数");
		row1.createCell(13).setCellValue("最后保养时间");
		row1.createCell(14).setCellValue("下次保养时间");
		row1.createCell(15).setCellValue("点检次数");
		row1.createCell(16).setCellValue("最后点检时间");
		row1.createCell(17).setCellValue("状态");

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 2000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 6000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 6000);
		sheet.setColumnWidth(10, 5000);
		sheet.setColumnWidth(13, 5000);
		sheet.setColumnWidth(14, 5000);
		sheet.setColumnWidth(16, 5000);
		int i = 1;
		int startRow;
		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		for (Fixture fixture : pagination.getRows()) {

			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//

			dataRow.createCell(0).setCellValue(fixture.getFixtureNumber());
			dataRow.createCell(1).setCellValue(fixture.getFixtureName());
			dataRow.createCell(2).setCellValue(fixture.getFixtureMap());
			Integer fixType = fixture.getFixtureType();
			String typeName = "";
			if (fixType == 1) {
				typeName = "组合";
			} else if (fixType == 2) {
				typeName = "配件";
			}

			dataRow.createCell(3).setCellValue(typeName);
			dataRow.createCell(4).setCellValue(fixture.getFixtureBarcode());
			dataRow.createCell(5).setCellValue(fixture.getDepartmentName());

			dataRow.createCell(10).setCellValue(fixture.getEquipmentName());
			dataRow.createCell(11).setCellValue(fixture.getStaffName());
			if (null != fixture.getMaintainTimes()) {
				dataRow.createCell(12).setCellValue(fixture.getMaintainTimes());
			}
			if (null != fixture.getLastMaintainTime()) {
				dataRow.createCell(13)
						.setCellValue(DateFormatUtils.format(fixture.getLastMaintainTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			if (null != fixture.getNextMaintainTime()) {
				dataRow.createCell(14)
						.setCellValue(DateFormatUtils.format(fixture.getNextMaintainTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			if (null != fixture.getSpotTimes()) {
				dataRow.createCell(15).setCellValue(fixture.getSpotTimes());
			}
			if (null != fixture.getLastSpotTime()) {
				dataRow.createCell(16)
						.setCellValue(DateFormatUtils.format(fixture.getLastSpotTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			String statusName = "";
			if (fixture.getFixtureStatus() == 1) {
				statusName = "正常使用";
			} else if (fixture.getFixtureStatus() == 2) {
				statusName = "待修磨";
			} else if (fixture.getFixtureStatus() == 3) {
				statusName = "待送检";
			} else if (fixture.getFixtureStatus() == 4) {
				statusName = "待检验";
			}else if (fixture.getFixtureStatus() == 5) {
				statusName = "待返库";
			}else if (fixture.getFixtureStatus() == 6) {
				statusName = "待报废";
			}
			dataRow.createCell(17).setCellValue(statusName);
			int itemCount = 0;
			List<FixtureChild> childList = fixture.getChildList();
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
				FixtureChild item = childList.get(j);
				dataRow.createCell(6).setCellValue(item.getFixtureNumber());
				dataRow.createCell(7).setCellValue(item.getFixtureName());
				dataRow.createCell(8).setCellValue(item.getFixtureMap());
				dataRow.createCell(9).setCellValue(item.getFixtureBarcode());
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
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 10, 10));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 11, 11));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 12, 12));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 13, 13));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 14, 14));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 15, 15));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 16, 16));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 17, 17));
		}
		String fileName = "夹具台账.xls";
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
		response.setHeader("Content-disposition", "attachment; filename="+fileName);
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
		wb.close();
		return null;
	}

}