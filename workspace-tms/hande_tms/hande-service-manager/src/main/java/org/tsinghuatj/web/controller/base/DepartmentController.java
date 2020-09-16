package org.tsinghuatj.web.controller.base;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tsinghuatj.base.domain.Department;
import org.tsinghuatj.base.domain.StaffDepartment;
import org.tsinghuatj.base.service.IDepartmentService;
import org.tsinghuatj.base.service.IStaffDepartmentService;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
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
@RequestMapping({ "/base" })
public class DepartmentController extends BaseController {

	private @Autowired(required = false) IDepartmentService departmentService;

	private @Autowired(required = false) IStaffDepartmentService staffdepartService;

	/**
	 * 部门信息添加
	 */
	@ApiOperation(value = "部门信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "添加部门信息成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/department-add")
	@OperateLog(info = "部门添加[部门编码:%s->部门名称:%s]", params = { "departmentCode", "departmentName" })
	public AjaxReturn departmentAdd(@ApiParam(name = "departmentCode", value = "部门编码", required = true) @RequestParam(required = true) String departmentCode, @ApiParam(name = "departmentName", value = "部门名称", required = false) @RequestParam(required = false, defaultValue = "") String departmentName, @ApiParam(name = "departmentDesc", value = "部门描述", required = false) @RequestParam(required = false, defaultValue = "") String departmentDesc) throws BusinessException {
		log.debug("DepartmentController.departmentAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("departmentCode:" + departmentCode);
			log.debug("departmentName:" + departmentName);
			log.debug("departmentDesc:" + departmentDesc);
		}
		// 获取当前用户
		Long userId = getAuthentication();

		// 封装参数信息
		Department department = new Department();
		department.setDepartmentCode(departmentCode);
		department.setDepartmentName(departmentName);
		department.setDepartmentDesc(departmentDesc);

		log.debug("DepartmentController.departmentAdd>>>");

		return new AjaxReturn(200, null, departmentService.insert(userId, department));
	}

	/**
	 * 部门信息删除
	 */
	@ApiOperation(value = "部门信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "部门信息删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/department-delete-by-id")
	@OperateLog(info = "部门删除[部门Id:%s]", params = { "pkId" })
	public AjaxReturn departmentDeleteById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("DepartmentController.toolCheckDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("DepartmentController.equipmentDeleteById>>>");
		return new AjaxReturn(200, null, departmentService.deleteById(userId, pkId));
	}

	/**
	 * 部门信息列表查询
	 */
	@ApiOperation(value = "部门信息列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "部门信息列表查询成功", response = Department.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/department-list")
	public AjaxReturn departmentList() throws BusinessException {
		log.debug("DepartmentController.departmentList<<<");
		if (log.isDebugEnabled()) {
		}

		// 获取当前用户
		Long userId = getAuthentication();
		Department department = new Department();

		log.debug("DepartmentController.departmentList>>>");
		return new AjaxReturn(200, null, departmentService.select(userId, department));
	}

	/**
	 * 查询部门信息分页列表
	 */
	@ApiOperation(value = "查询部门信息分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "部门信息分页列表查询成功", response = Department.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/department-page-list")
	public AjaxReturn departmentPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "departmentCode", value = "部门编码", required = false) @RequestParam(required = false, defaultValue = "") String departmentCode) throws BusinessException {
		log.debug("DepartmentController.departmentPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("departmentCode:" + departmentCode);
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

		Department department = new Department();
		department.setDepartmentCode(departmentCode);

		Pagination<Department> pagination = departmentService.selectPageList(curuserId, department, queryDto);
		log.debug("DepartmentController.departmentPageList>>>");
		return pagination;
	}

	/**
	 * 部门信息修改
	 */
	@ApiOperation(value = "部门信息修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "部门信息修改成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/department-update")
	@OperateLog(info = "部门删除[部门Id:%s->部门名称:%s]", params = { "pkId", "departmentName" })
	public AjaxReturn departmentUpdate(@ApiParam(name = "pkId", value = "刀具编码", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "departmentCode", value = "部门编码", required = false) @RequestParam(required = false, defaultValue = "") String departmentCode, @ApiParam(name = "departmentName", value = "部门名称", required = false) @RequestParam(required = false, defaultValue = "") String departmentName, @ApiParam(name = "departmentDesc", value = "部门描述", required = false) @RequestParam(required = false, defaultValue = "") String departmentDesc) throws BusinessException {
		log.debug("EquipmentController.departmentUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
			log.debug("departmentCode:" + departmentCode);
			log.debug("departmentName:" + departmentName);
			log.debug("departmentDesc:" + departmentDesc);
		}
		// 获取当前用户
		Long userId = getAuthentication();

		// 封装参数信息
		Department department = new Department();
		department.setDepartmentCode(departmentCode);
		department.setDepartmentName(departmentName);
		department.setDepartmentDesc(departmentDesc);

		log.debug("EquipmentController.departmentUpdate>>>");

		return new AjaxReturn(200, null, departmentService.updateActiveById(userId, department, pkId));
	}

	/**
	 * 部门信息表导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "部门信息表导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "部门信息表导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/department-import")
	@Secure()
	public AjaxReturn departmentImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<Department> departmentList = ExcelUtils.getInstance().readExcel2Objects(inputStream, Department.class);
		inputStream.close();
		departmentService.departmentImport(userId, departmentList);
		return new AjaxReturn(200, null, 1);
	}

	/**
	 * 部门信息表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "部门信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "部门信息表导出", response = Department.class) })
	// @Secure()
	@PostMapping(path = { "/department-export" })
	public ResponseEntity<byte[]> downloadDepartmentExcel() throws Exception {
		Long userId = getAuthentication();
		List<Department> excelVOList = departmentService.select(userId, new Department());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, Department.class, true, null, true);
		return getResponseEntity(data, "部门信息表.xlsx");
	}

	/**
	 * 部门信息同步
	 */
	@ApiOperation(value = "同步部门信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "同步部门信息成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/department-synchro")
	// @OperateLog(info = "部门添加[部门编码:%s->部门名称:%s]", params = {
	// "departmentCode","departmentName" })
	public AjaxReturn departmentSynchro(@ApiParam(name = "departmentCode", value = "部门编码", required = true) @RequestParam(required = true) String departmentCode) throws BusinessException {
		log.debug("DepartmentController.departmentSynchro<<<");
		// 获取当前用户
		Long userId = getAuthentication();
		log.debug("DepartmentController.departmentSynchro>>>");
		return new AjaxReturn(200, null, departmentService.departmentSynchro(userId, departmentCode));
	}

	/**
	 * 部门信息列表查询
	 */
	@ApiOperation(value = "部门信息列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "部门信息列表查询成功", response = Department.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-department-list")
	public AjaxReturn staffDepartList() throws BusinessException {
		log.debug("DepartmentController.departmentList<<<");
		if (log.isDebugEnabled()) {
		}

		// 获取当前用户
		Long userId = getAuthentication();

		log.debug("DepartmentController.departmentList>>>");
		return new AjaxReturn(200, null, staffdepartService.select(userId, new StaffDepartment()));
	}
}
