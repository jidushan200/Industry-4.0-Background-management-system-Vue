package org.tsinghuatj.web.controller.tool;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
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
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.ToolCoat;
import org.tsinghuatj.tool.domain.ToolCoatPrice;
import org.tsinghuatj.tool.service.IToolCoatPriceService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/base" })
public class ToolCoatPriceController extends BaseController {

	private @Autowired(required = false) IToolCoatPriceService coatPriceService;

	/**
	 * 查询涂层价格分页列表
	 */
	@ApiOperation(value = "查询刀具涂层价格分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具涂层价格分页列表查询成功", response = ToolCoat.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/coat-price-page-list")
	public AjaxReturn coatPricePageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "toolNumber", value = "刀具编号", required = false) @RequestParam(required = false, defaultValue = "") String toolNumber, @ApiParam(name = "supplierCode", value = "供应商编号", required = false) @RequestParam(required = false, defaultValue = "") String supplierCode

	) throws BusinessException {
		log.debug("ToolCoatPriceController.coatPricePageList<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("toolNumber:" + toolNumber);
			log.debug("supplierCode:" + supplierCode);
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

		ToolCoatPrice coatprice = new ToolCoatPrice();
		coatprice.setSupplierCode(supplierCode);
		coatprice.setToolNumber(toolNumber);
		Pagination<ToolCoatPrice> pagination = coatPriceService.selectPageList(curuserId, coatprice, queryDto);
		log.debug("ToolCoatPriceController.coatPricePageList>>>");
		return pagination;
	}

	/**
	 * 刀具涂层价格添加
	 */
	@ApiOperation(value = "刀具涂层价格添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具涂层价格添加", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/coat-price-add")
	@OperateLog(info = "刀具涂层价格新增[物料编码:s%->涂层供应商:%s]", params = { "toolNumber", "supplierName" })
	public AjaxReturn coatPriceAdd(@ApiParam(name = "toolId", value = "刀具id", required = true) @RequestParam(required = true) Long toolId,
			@ApiParam(name = "toolNumber", value = "toolNumber", required = true) @RequestParam(required = true) String toolNumber, 
			@ApiParam(name = "supplierName", value = "供应商名称", required = true) @RequestParam(required = true) String supplierName,
			@ApiParam(name = "supplierId", value = "供应商id", required = true) @RequestParam(required = true) Long supplierId, 
			@ApiParam(name = "price", value = "价格", required = true) @RequestParam(required = true) BigDecimal price) throws BusinessException {
		log.debug("ToolCoatPriceController.toolCoatAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolId:" + toolId);
			log.debug("supplierId:" + supplierId);

		}
		// 获取当前用户
		Long userId = getAuthentication();
		coatPriceService.coatPriceCheck(toolId, supplierId, null);
		// 封装参数信息
		ToolCoatPrice coatprice = new ToolCoatPrice();
		coatprice.setToolId(toolId);
		coatprice.setSupplierId(supplierId);
		coatprice.setPrice(price);
		log.debug("ToolCoatPriceController.toolCoatAdd>>>");

		return new AjaxReturn(200, null, coatPriceService.insert(userId, coatprice));
	}

	/**
	 * 刀具涂层价格修改
	 */
	@ApiOperation(value = "刀具涂层价格修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具涂层价格修改成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/coat-price-update")
	@OperateLog(info = "刀具涂层价格修改[物料编码:s%->涂层供应商:s%->价格:%s]", params = { "toolNumber","supplierName", "price" })
	public AjaxReturn coatPriceUpdate(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId, 
			@ApiParam(name = "toolId", value = "刀具Id", required = true) @RequestParam(required = false, defaultValue = "") Long toolId, 
			@ApiParam(name = "toolNumber", value = "toolNumber", required = true) @RequestParam(required = true) String toolNumber, 
			@ApiParam(name = "supplierName", value = "供应商名称", required = true) @RequestParam(required = true) String supplierName, 
			@ApiParam(name = "supplierId", value = "供应商id", required = true) @RequestParam(required = true) Long supplierId, 
			@ApiParam(name = "price", value = "价格", required = true) @RequestParam(required = true) BigDecimal price) throws BusinessException {
		log.debug("ToolCoatPriceController.coatPriceUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
			log.debug("toolId:" + toolId);
			log.debug("supplierId:" + supplierId);

		}
		// 获取当前用户
		Long userId = getAuthentication();
		coatPriceService.coatPriceCheck(toolId, supplierId, pkId);
		// 封装参数信息
		ToolCoatPrice coatprice = new ToolCoatPrice();
		coatprice.setToolId(toolId);
		coatprice.setSupplierId(supplierId);
		coatprice.setPrice(price);
		coatprice.setPkId(pkId);
		log.debug("ToolCoatPriceController.coatPriceUpdate>>>");

		return new AjaxReturn(200, null, coatPriceService.updateActiveById(userId, coatprice, pkId));
	}

	/**
	 * 刀具涂层价格删除
	 */
	@ApiOperation(value = "刀具涂层价格删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具涂层价格成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/coat-price-delete-by-id")
	@OperateLog(info = "刀具涂层价格删除[物料编码:s%->涂层供应商:s%]", params = { "toolNumber","supplierName" })
	public AjaxReturn coatPriceDeleteById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "toolNumber", value = "toolNumber", required = true) @RequestParam(required = true) String toolNumber,
			@ApiParam(name = "supplierName", value = "supplierName", required = false) @RequestParam(required = false) String supplierName) throws BusinessException {
		log.debug("ToolCoatPriceController.coatPriceDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId > 0);
		log.debug("ToolCoatPriceController.coatPriceDeleteById>>>");
		return new AjaxReturn(200, null, coatPriceService.deleteById(userId, pkId));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/coat-price-get-by-number")
	public AjaxReturn coatPriceGetByCode(@ApiParam(name = "toolNumber", value = "物料编号", required = false) @RequestParam(required = false) String toolNumber, @ApiParam(name = "supplierCode", value = "供应商编号", required = false) @RequestParam(required = false) String supplierCode

	) throws BusinessException {
		log.debug("ToolCoatPriceController.coatPriceGetByCode<<<");
		if (log.isDebugEnabled()) {
			log.debug("toolNumber:" + toolNumber);
			log.debug("supplierCode:" + supplierCode);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		// 封装参数信息
		ToolCoatPrice coatPrice = new ToolCoatPrice();
		coatPrice.setSupplierCode(supplierCode);
		coatPrice.setToolNumber(toolNumber);
		log.debug("ToolCoatPriceController.coatPriceGetByCode>>>");
		return new AjaxReturn(200, null, coatPriceService.select(userId, coatPrice));
	}
	
	/**
	 * 涂层价格信息表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "涂层价格信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "涂层价格信息表导出", response = ToolCoatPrice.class) })
	// @Secure()
	@GetMapping(path = { "/coat-price-export" })
	@OperateLog(info = "刀具涂层价格导出[%s]", params = { "" })
	public ResponseEntity<byte[]> downloadCoatPriceExcel() throws Exception {
		Long userId = getAuthentication();
		List<ToolCoatPrice> excelVOList = coatPriceService.select(userId, new ToolCoatPrice());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, ToolCoatPrice.class, true, null, true);
		return getResponseEntity(data, "涂层价格信息表.xlsx");
	}

	/**
	 * 涂层价格导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "涂层价格导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "涂层价格导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/coat-supplier-coat-import")
	@OperateLog(info = "刀具涂层价格导入[%s]", params = { "" })
	// @Secure()
	public AjaxReturn coatSupplierCoatimport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<ToolCoatPrice> tpList = ExcelUtils.getInstance().readExcel2Objects(inputStream, ToolCoatPrice.class, 1, 100, 0);
		inputStream.close();
		coatPriceService.coatPriceImport(userId, tpList);
		return new AjaxReturn(200, null, 1);
	}

}
