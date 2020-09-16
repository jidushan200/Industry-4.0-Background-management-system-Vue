package org.tsinghuatj.web.controller.base;

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
import org.tsinghuatj.base.domain.Part;
import org.tsinghuatj.base.domain.PartName;
import org.tsinghuatj.base.service.IPartNameService;
import org.tsinghuatj.base.service.IPartService;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
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
public class PartController extends BaseController {

	private @Autowired(required = false) IPartService partService;

	private @Autowired(required = false) IPartNameService partNameService;

	/**
	 * 加工零件表信息添加
	 */
	@ApiOperation(value = "加工零件表信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "加工零件表信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-part-add")
	@OperateLog(info = "制件添加[制件编码:%s->制件名称:%s]", params = { "partCode", "partName" })
	public AjaxReturn toolPartAdd(
			@ApiParam(name = "partName", value = "零件名称", required = true) @RequestParam(required = true) String partName, 
			@ApiParam(name = "partCode", value = "零件编码", required = false) @RequestParam(required = false, defaultValue = "") String partCode, 
			@ApiParam(name = "price", value = "单价", required = false) @RequestParam(required = false) BigDecimal price, 
			@ApiParam(name = "partType", value = "零件类型Id", required = false) @RequestParam(required = false) Long partType, 
			@ApiParam(name = "partTypeName", value = "零件类型", required = false) @RequestParam(required = false) String partTypeName,
			@ApiParam(name = "departmentId", value = "生产部门id", required = false) @RequestParam(required = false) Long departmentId, 
			@ApiParam(name = "departmentName", value = "生产部门", required = false) @RequestParam(required = false) String departmentName, 
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false, defaultValue = "") String remark

	) throws BusinessException {
		log.debug("PartController.toolPartAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("partName:" + partName);
			log.debug("partCode:" + partCode);
			log.debug("price:" + price);
			log.debug("partType:" + partType);
			log.debug("departmentId:" + departmentId);
			log.debug("departmentName:" + departmentName);
			log.debug("remark:" + remark);

		}
		// 获取当前用户
		Long userId = getAuthentication();
		partService.checkPartCode(partCode, null);
		// 封装参数信息
		Part part = new Part();
		part.setPartName(partName);
		part.setPartCode(partCode);
		part.setPrice(price);
		part.setPartType(partType);
		part.setPartTypeName(partTypeName);
		part.setDepartmentId(departmentId);
		part.setDepartmentName(departmentName);
		part.setRemark(remark);

		log.debug("PartController.toolPartAdd>>>");

		return new AjaxReturn(200, null, partService.insert(userId, part));
	}

	/**
	 * 加工零件表信息删除
	 */
	@ApiOperation(value = "加工零件表信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "加工零件表信息删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-part-delete-by-id")
	@OperateLog(info = "制件删除[制件Id:%s]", params = { "pkId" })
	public AjaxReturn toolPartDeleteById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("PartController.toolPartDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("PartController.toolPartDeleteById>>>");
		return new AjaxReturn(200, null, partService.deleteById(userId, pkId));
	}

	/**
	 * 制件信息列表查询
	 */
	@ApiOperation(value = "制件信息列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "制件信息列表查询成功", response = Part.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/part-list")
	public AjaxReturn partList(@ApiParam(name = "departmentId", value = "部门ID", required = false) @RequestParam(required = false) Long departmentId) throws BusinessException {
		log.debug("PartController.partList<<<");
		if (log.isDebugEnabled()) {
		}

		// 获取当前用户
		Long userId = getAuthentication();
		Part part = new Part();
		part.setDepartmentId(departmentId);
		log.debug("PartController.partList>>>");
		return new AjaxReturn(200, null, partService.select(userId, part));
	}

	/**
	 * 加工零件表分页列表
	 */
	@ApiOperation(value = "加工零件表分页列表  ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "加工零件表分页列表查询成功", response = Part.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-part-page-list")
	public AjaxReturn partPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, @ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "partCode", value = "零件名称", required = false) @RequestParam(required = false, defaultValue = "") String partCode, @ApiParam(name = "partType", value = "零件类型", required = false) @RequestParam(required = false, defaultValue = "") Long partType, @ApiParam(name = "departmentName", value = "生产部门", required = false) @RequestParam(required = false, defaultValue = "") String departmentName) throws BusinessException {
		log.debug("PartController.toolPartPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("partCode:" + partCode);
			log.debug("partType:" + partType);
			log.debug("departmentName:" + departmentName);
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

		Part part = new Part();
		part.setPartCode(partCode);
		part.setPartType(partType);
		part.setDepartmentName(departmentName);

		Pagination<Part> pagination = partService.selectPageList(curuserId, part, queryDto);
		log.debug("PartController.toolPartPageList>>>");
		return pagination;
	}

	/**
	 * 制件信息列表查询
	 */
	@ApiOperation(value = "制件信息列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "制件信息列表查询成功", response = Part.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/part-name-list")
	public AjaxReturn partNameList() throws BusinessException {
		log.debug("PartController.partNameList<<<");
		if (log.isDebugEnabled()) {
		}

		// 获取当前用户
		Long userId = getAuthentication();
		PartName partName = new PartName();

		log.debug("PartController.partNameList>>>");
		return new AjaxReturn(200, null, partNameService.select(userId, partName));
	}

	/**
	 * 制件信息列表查询
	 */

	@ApiResponses({ @ApiResponse(code = 200, message = "制件信息列表查询成功", response = Part.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/part-get-code")
	public AjaxReturn partGetByCode(@ApiParam(name = "partCode", value = "零件编码", required = true) @RequestParam(required = true) String partCode

	) throws BusinessException {
		log.debug("PartController.partGetByCode<<<");
		// 获取当前用户
		Long userId = getAuthentication();
		log.debug("PartController.partGetByCode>>>");
		return new AjaxReturn(200, null, partService.selectByCode(userId, partCode));
	}

	/**
	 * 加工零件表信息更新
	 */
	@ApiOperation(value = "加工零件表信息更新", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "加工零件表信息更新成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-part-update")
	@OperateLog(info = "制件修改[制件Id:%s->制件编码:%s]", params = { "pkId", "partCode" })
	public AjaxReturn toolPartUpdate(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId, 
			@ApiParam(name = "partName", value = "零件名称", required = false) @RequestParam(required = false) String partName, 
			@ApiParam(name = "partCode", value = "零件编码", required = false) @RequestParam(required = false) String partCode, 
			@ApiParam(name = "price", value = "单价", required = false) @RequestParam(required = false) BigDecimal price, 
			@ApiParam(name = "partType", value = "零件类型", required = false) @RequestParam(required = false) Long partType,
			@ApiParam(name = "departmentName", value = "生产部门", required = false) @RequestParam(required = false) String partTypeName, 
			@ApiParam(name = "departmentId", value = "生产部门id", required = false) @RequestParam(required = false) Long departmentId, 
			@ApiParam(name = "departmentName", value = "生产部门", required = false) @RequestParam(required = false) String departmentName) throws BusinessException {
		log.debug("PartController.toolPartUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("partName:" + partName);
			log.debug("partCode:" + partCode);
			log.debug("price:" + price);
			log.debug("partType:" + partType);
			log.debug("departmentId:" + departmentId);
			log.debug("departmentName:" + departmentName);

		}
		// 获取当前用户
		Long userId = getAuthentication();
		partService.checkPartCode(partCode, pkId);
		// 封装参数信息
		Part part = new Part();
		part.setPartName(partName);
		part.setPartCode(partCode);
		part.setPrice(price);
		part.setPartType(partType);
		part.setPartTypeName(partTypeName);
		part.setDepartmentId(departmentId);
		part.setDepartmentName(departmentName);

		log.debug("PartController.toolPartUpdate>>>");

		return new AjaxReturn(200, null, partService.updateActiveById(userId, part, pkId));
	}

	/**
	 * 加工零件表导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "加工零件表导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "加工零件表导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/part-import")
	// @Secure()
	public AjaxReturn partImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		CustomUser user = getCompleteAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<Part> partList = ExcelUtils.getInstance().readExcel2Objects(inputStream, Part.class, 1, 2000, 0);
		inputStream.close();
		partService.partImport(user.getId(), partList);
		return new AjaxReturn(200, null, 1);
	}

	/**
	 * 加工零件表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "加工零件表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "加工零件表导出", response = Part.class) })
	// @Secure()
	@GetMapping(path = { "/part-export" })
	public ResponseEntity<byte[]> downloadPartExcel() throws Exception {
		Long userId = getAuthentication();
		List<Part> excelVOList = partService.select(userId, new Part());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, Part.class, true, null, true);
		return getResponseEntity(data, "制件表.xlsx");
	}

	/**
	 * 制件信息同步
	 */
	@ApiOperation(value = "制件信息同步", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "制件信息同步", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/part-synchro")
	// @OperateLog(info = "部门添加[部门编码:%s->部门名称:%s]", params = {
	// "departmentCode","departmentName" })
	public AjaxReturn partSynchro(@ApiParam(name = "partCode", value = "制件编码", required = true) @RequestParam(required = true) String partCode) throws BusinessException {
		// 获取当前用户
		Long userId = getAuthentication();
		return new AjaxReturn(200, null, partService.partSynchro(userId, partCode));
	}

}
