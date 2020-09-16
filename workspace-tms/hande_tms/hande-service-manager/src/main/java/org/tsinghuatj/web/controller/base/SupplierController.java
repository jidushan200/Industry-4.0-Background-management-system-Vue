package org.tsinghuatj.web.controller.base;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tsinghuatj.base.domain.Supplier;
import org.tsinghuatj.base.service.ISupplierService;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.support.annotation.OperateLog;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/base" })
public class SupplierController extends BaseController {

	private @Autowired(required = false) ISupplierService supplierService;

	/**
	 * 供应商主表信息添加
	 */
	@ApiOperation(value = "供应商信息主表添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "供应商主表信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/supplier-add")
	@OperateLog(info = "供应商添加[供应商名称:%s->供应商类型:%s]", params = { "supplierName", "supplierType" })
	public AjaxReturn supplierAdd(@ApiParam(name = "supplierName", value = "供应商名称", required = true) @RequestParam(required = true) String supplierName, 
			@ApiParam(name = "supplierCode", value = "供应商编码", required = true) @RequestParam(required = true) String supplierCode, 
			@ApiParam(name = "evaluation", value = "供应商评价", required = false) @RequestParam(required = false, defaultValue = "") String evaluation,
			@ApiParam(name = "isNewTool", value = "新刀（0-否；1-是）", required = false) @RequestParam(required = false, defaultValue = "") Integer isNewTool, 
			@ApiParam(name = "isRepair", value = "刃磨（0-否；1-是）", required = false) @RequestParam(required = false) Integer isRepair,
			@ApiParam(name = "isCoat", value = "涂层（0-否；1-是）", required = false) @RequestParam(required = false, defaultValue = "") Integer isCoat,
			@ApiParam(name = "isMeasure", value = "量具（0-否；1-是）", required = false) @RequestParam(required = false, defaultValue = "") Integer isMeasure,
			@ApiParam(name = "isFixture", value = "夹具（0-否；1-是）", required = false) @RequestParam(required = false, defaultValue = "") Integer isFixture,
			@ApiParam(name = "isMould", value = "模具（0-否；1-是）", required = false) @RequestParam(required = false, defaultValue = "") Integer isMould
			) throws BusinessException {
		log.debug("SupplierController.toolSupplierAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("supplierName:" + supplierName);
			log.debug("supplierCode:" + supplierCode);
			log.debug("evaluation:" + evaluation);
			log.debug("isNewTool:" + isNewTool);
			log.debug("isRepair:" + isRepair);
			log.debug("isCoat:" + isCoat);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		supplierService.checkSupplierCode(supplierCode, null);
		// 封装参数信息
		Supplier supplier = new Supplier();
		supplier.setSupplierName(supplierName);
		supplier.setSupplierCode(supplierCode);
		supplier.setEvaluation(evaluation);
		supplier.setIsNewTool(isNewTool);
		supplier.setIsRepair(isRepair);
		supplier.setIsCoat(isCoat);
		supplier.setIsMeasure(isMeasure);
		supplier.setIsFixture(isFixture);
		supplier.setIsMould(isMould);
		log.debug("SupplierController.toolSupplierAdd>>>");

		return new AjaxReturn(200, null, supplierService.insert(userId, supplier));
	}

	/**
	 * 供应商主表信息删除
	 */
	@ApiOperation(value = "供应商主表信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "供应商主表信息删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/supplier-delete-by-id")
	@OperateLog(info = "供应商删除[供应商Id:%s]", params = { "pkId" })
	public AjaxReturn supplierDeleteById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("SupplierController.toolSupplierDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("SupplierController.toolSupplierDeleteById>>>");
		return new AjaxReturn(200, null, supplierService.deleteById(userId, pkId));
	}

	/**
	 * 供应商列表查询
	 */
	@ApiOperation(value = "供应商列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "供应商列表查询成功", response = Supplier.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/supplier-list")
	public AjaxReturn supplierList(
			@ApiParam(name = "isNewTool", value = "新刀（0-否;1-是）", required = false) @RequestParam(required = false) Integer isNewTool, 
			@ApiParam(name = "isRepair", value = "刃磨（0-否;1-是）", required = false) @RequestParam(required = false) Integer isRepair,
			@ApiParam(name = "isCoat", value = "涂层（0-否;1-是）", required = false) @RequestParam(required = false) Integer isCoat,
			@ApiParam(name = "isMeasure", value = "量具（0-否;1-是）", required = false) @RequestParam(required = false) Integer isMeasure,
			@ApiParam(name = "isFixture", value = "夹具（0-否;1-是）", required = false) @RequestParam(required = false) Integer isFixture,
			@ApiParam(name = "isMould", value = "模具（0-否；1-是）", required = false) @RequestParam(required = false) Integer isMould
			) throws BusinessException {
		log.debug("SupplierController.supplierList<<<");
		if (log.isDebugEnabled()) {
		}

		// 获取当前用户
		Long userId = getAuthentication();
		Supplier supplier = new Supplier();
		supplier.setIsNewTool(isNewTool);
		supplier.setIsRepair(isRepair);
		supplier.setIsCoat(isCoat);
		supplier.setIsMeasure(isMeasure);
		supplier.setIsFixture(isFixture);
		supplier.setIsMould(isMould);
		log.debug("SupplierController.supplierList>>>");
		return new AjaxReturn(200, null, supplierService.select(userId, supplier));
	}

	/**
	 * 供应商列表查询
	 */
	@ApiResponses({ @ApiResponse(code = 200, message = "供应商查询成功", response = Supplier.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/supplier-get-by-code")
	public AjaxReturn supplierGetByCode(@ApiParam(name = "supplierCode", value = "供应商编号", required = true) @RequestParam(required = true) String supplierCode) throws BusinessException {

		// 获取当前用户
		Long userId = getAuthentication();

		return new AjaxReturn(200, null, supplierService.selectByCode(userId, supplierCode));
	}

	/**
	 * 供应商主表分页列表
	 */
	@ApiOperation(value = "供应商主表分页列表 ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "供应商主表列表查询成功", response = Supplier.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/supplier-page-list")
	public AjaxReturn supplierPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, @ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "supplierName", value = "供应商名称", required = false) @RequestParam(required = false, defaultValue = "") String supplierName, @ApiParam(name = "supplierCode", value = "供应商编码", required = false) @RequestParam(required = false, defaultValue = "") String supplierCode, @ApiParam(name = "supplierType", value = "供应商类型", required = false) @RequestParam(required = false, defaultValue = "") Integer supplierType) throws BusinessException {
		log.debug("SupplierController.toolSupplierPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("supplierName:" + supplierName);
			log.debug("supplierCode:" + supplierCode);
			log.debug("supplierType:" + supplierType);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);

		Supplier supplier = new Supplier();
		supplier.setSupplierName(supplierName);
		supplier.setSupplierCode(supplierCode);

		Pagination<Supplier> pagination = supplierService.selectPageList(curuserId, supplier, queryDto);
		log.debug("SupplierController.toolSupplierPageList>>>");
		return pagination;
	}

	/**
	 * 供应商主表信息更新
	 */
	@ApiOperation(value = "供应商信息主表更新", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "供应商主表信息更新成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/supplier-update")
	@OperateLog(info = "供应商修改[供应商名称:%s->供应商类型:%s]", params = { "supplierName", "supplierType" })
	public AjaxReturn supplierUpdate(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId, 
			@ApiParam(name = "supplierName", value = "供应商名称", required = false) @RequestParam(required = false) String supplierName,
			@ApiParam(name = "supplierCode", value = "供应商编码", required = false) @RequestParam(required = false) String supplierCode,
			@ApiParam(name = "evaluation", value = "供应商评价", required = false) @RequestParam(required = false) String evaluation, 
			@ApiParam(name = "isNewTool", value = "新刀（0-否；1-是）", required = false) @RequestParam(required = false) Integer isNewTool,
			@ApiParam(name = "isRepair", value = "刃磨（0-否；1-是）", required = false) @RequestParam(required = false) Integer isRepair, 
			@ApiParam(name = "isCoat", value = "涂层（0-否；1-是）", required = false) @RequestParam(required = false) Integer isCoat,
			@ApiParam(name = "isMeasure", value = "量具（0-否；1-是）", required = false) @RequestParam(required = false) Integer isMeasure,
			@ApiParam(name = "isFixture", value = "夹具（0-否；1-是）", required = false) @RequestParam(required = false) Integer isFixture,
			@ApiParam(name = "isMould", value = "模具（0-否；1-是）", required = false) @RequestParam(required = false) Integer isMould
			) throws BusinessException {
		log.debug("SupplierController.toolSupplierUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("supplierName:" + supplierName);
			log.debug("supplierCode:" + supplierCode);
			log.debug("evaluation:" + evaluation);
			log.debug("isNewTool:" + isNewTool);
			log.debug("isRepair:" + isRepair);
			log.debug("isCoat:" + isCoat);

		}
		// 获取当前用户
		Long userId = getAuthentication();
		supplierService.checkSupplierCode(supplierCode, pkId);
		// 封装参数信息
		Supplier supplier = new Supplier();
		supplier.setSupplierName(supplierName);
		supplier.setSupplierCode(supplierCode);
		supplier.setEvaluation(evaluation);
		supplier.setIsNewTool(isNewTool);
		supplier.setIsRepair(isRepair);
		supplier.setIsCoat(isCoat);
		supplier.setIsMeasure(isMeasure);
		supplier.setIsFixture(isFixture);
		supplier.setIsMould(isMould);
		log.debug("SupplierController.toolSupplierUpdate>>>");

		return new AjaxReturn(200, null, supplierService.updateActiveById(userId, supplier, pkId));
	}

	/**
	 * 供应商导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "供应商导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "供应商导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/supplier-import")
	// @Secure()
	public AjaxReturn supplierImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<Supplier> supplierList = ExcelUtils.getInstance().readExcel2Objects(inputStream, Supplier.class, 1, 2000, 0);
		inputStream.close();
		supplierService.supplierImport(userId, supplierList);
		return new AjaxReturn(200, null, 1);
	}

	/**
	 * 供应商导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "供应商导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "供应商导出", response = Supplier.class) })
	// @Secure()
	@GetMapping(path = { "/supplier-export" })
	public ResponseEntity<byte[]> downloadsupplierExcel() throws Exception {
		Long userId = getAuthentication();
		List<Supplier> excelVOList = supplierService.select(userId, new Supplier());
		excelVOList.stream().forEach(item -> {
			if (item.getIsNewTool() != null && item.getIsNewTool() == 1) {
				item.setIsNewSupplier("是");
			} else {
				item.setIsNewSupplier("否");
			}
			if (item.getIsRepair() != null && item.getIsRepair() == 1) {
				item.setIsRepairSupplier("是");
			} else {
				item.setIsRepairSupplier("否");
			}
			if (item.getIsCoat() != null && item.getIsCoat() == 1) {
				item.setIsCoatSupplier("是");
			} else {
				item.setIsCoatSupplier("否");
			}
			if (item.getIsMeasure() != null && item.getIsMeasure() == 1) {
				item.setIsMeasureSupplier("是");
			} else {
				item.setIsMeasureSupplier("否");
			}
			if (item.getIsFixture() != null && item.getIsFixture() == 1) {
				item.setIsFixtureSupplier("是");
			} else {
				item.setIsFixtureSupplier("否");
			}
			if (item.getIsMould() != null && item.getIsMould() == 1) {
				item.setIsMouldSupplier("是");
			} else {
				item.setIsMouldSupplier("否");
			}
		});
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, Supplier.class, true, null, true);
		return getResponseEntity(data, "供应商.xlsx");
	}

	/**
	 * 供应商信息同步
	 */
	@ApiOperation(value = "供应商信息同步", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "供应商信息同步", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/supplier-synchro")
	// @OperateLog(info = "部门添加[部门编码:%s->部门名称:%s]", params = {
	// "departmentCode","departmentName" })
	public AjaxReturn supplierSynchro(@ApiParam(name = "supplierCode", value = "供应商编码", required = true) @RequestParam(required = true) String supplierCode) throws BusinessException {
		// 获取当前用户
		Long userId = getAuthentication();
		return new AjaxReturn(200, null, supplierService.supplierSynchro(userId, supplierCode));
	}

}
