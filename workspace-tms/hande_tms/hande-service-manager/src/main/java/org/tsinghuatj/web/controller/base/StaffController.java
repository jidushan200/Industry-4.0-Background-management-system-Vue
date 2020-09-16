package org.tsinghuatj.web.controller.base;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
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
import org.tsinghuatj.base.domain.Staff;

import org.tsinghuatj.base.service.IStaffService;
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
public class StaffController extends BaseController {

	private @Autowired(required = false) IStaffService staffService;

	/**
	 * 员工信息添加
	 */
	@ApiOperation(value = "员工信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "添加员工信息成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-add")
	@OperateLog(info = "员工添加[员工编码:%s->员工名称:%s]", params = { "staffCode", "staffName" })
	public AjaxReturn staffAdd(
			@ApiParam(name = "staffName", value = "员工姓名", required = true) @RequestParam(required = true) String staffName,
			@ApiParam(name = "staffCode", value = "员工编码", required = true) @RequestParam(required = true) String staffCode,
			@ApiParam(name = "gender", value = "员工性别", required = true) @RequestParam(required = true) Integer gender,
			@ApiParam(name = "position", value = "员工职务", required = true) @RequestParam(required = true) String position,
			@ApiParam(name = "departmentId", value = "部门Id", required = false) @RequestParam(required = false) Long departmentId,
			@ApiParam(name = "departmentName", value = "部门名称", required = false) @RequestParam(required = false, defaultValue = "") String departmentName,
			@ApiParam(name = "teamId", value = "班组Id", required = false) @RequestParam(required = false) Long teamId,
			@ApiParam(name = "teamName", value = "班组名称", required = false) @RequestParam(required = false, defaultValue = "") String teamName,
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false, defaultValue = "") String remark)
			throws BusinessException {
		log.debug("StaffController.staffAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("staffName:" + staffName);
			log.debug("staffCode:" + staffCode);
			log.debug("gender:" + gender);
			log.debug("position:" + position);
			log.debug("departmentId:" + departmentId);
			log.debug("departmentName:" + departmentName);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		staffService.checkStaffCode(staffCode, null);
		// 封装参数信息
		Staff staff = new Staff();
		staff.setStaffName(staffName);
		staff.setStaffCode(staffCode);
		staff.setGender(gender);
		staff.setPosition(position);
		staff.setDepartmentId(departmentId);
		staff.setDepartmentName(departmentName);
		staff.setTeamId(teamId);
		staff.setTeamName(teamName);
		staff.setRemark(remark);

		log.debug("StaffController.staffAdd>>>");

		return new AjaxReturn(200, null, staffService.insert(userId, staff));
	}

	/**
	 * 员工主表信息删除
	 */
	@ApiOperation(value = "员工主表信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "员工主表信息删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-delete-by-id")
	@OperateLog(info = "员工删除[员工Id:%s]", params = { "pkId" })
	public AjaxReturn staffDeleteById(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId)
			throws BusinessException {
		log.debug("StaffController.staffDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("StaffController.staffDeleteById>>>");
		return new AjaxReturn(200, null, staffService.deleteById(userId, pkId));
	}

	/**
	 * 人员列表查询
	 */
	@ApiOperation(value = "人员列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "人员列表查询成功", response = Staff.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-list")
	public AjaxReturn staffList(
			@ApiParam(name = "departmentId", value = "部门ID", required = false) @RequestParam(required = false, defaultValue = "") Long departmentId,
			@ApiParam(name = "teamId", value = "班组ID", required = false) @RequestParam(required = false, defaultValue = "") Long teamId)
			throws BusinessException {
		log.debug("StaffController.staffList<<<");
		if (log.isDebugEnabled()) {
		}

		// 获取当前用户
		Long userId = getAuthentication();
		Staff staff = new Staff();
		staff.setDepartmentId(departmentId);
		staff.setTeamId(teamId);

		log.debug("StaffController.staffList>>>");
		return new AjaxReturn(200, null, staffService.select(userId, staff));
	}

	/**
	 * 根据职工号查找
	 */
	@ApiOperation(value = "职工号id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "职工号Id查询列表查询成功", response = Staff.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-get-by-code")
	public AjaxReturn staffGetByStaffCode(
			@ApiParam(name = "staffCode", value = "staffCode", required = true) @RequestParam(required = true) String staffCode)
			throws BusinessException {

		if (log.isDebugEnabled()) {
			log.debug("staffCode:" + staffCode);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		return new AjaxReturn(200, null, staffService.staffGetByStaffCode(curuserId, staffCode));
	}

	/**
	 * 根据职工号查找
	 */
	@ApiOperation(value = "职工号id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "职工号Id查询列表查询成功", response = Staff.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-get-by-id")
	public AjaxReturn staffGetById(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId)
			throws BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long curuserId = getAuthentication(); // 参数校验

		return new AjaxReturn(200, null, staffService.selectById(curuserId, pkId));
	}

	/**
	 * 根据职工号查找
	 */
	@ApiOperation(value = "职工号id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "职工号Id查询列表查询成功", response = Staff.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-get-by-user-id")
	public AjaxReturn staffGetByUserId(
			@ApiParam(name = "userPkId", value = "userPkId", required = true) @RequestParam(required = true) Long userPkId)
			throws BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("userPkId:" + userPkId);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		return new AjaxReturn(200, null, staffService.selectByUserId(curuserId, userPkId));
	}

	/**
	 * 获取当前登录职工信息
	 */
	@ApiOperation(value = "获取当前登录职工信息", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "获取当前登录职工信息", response = Staff.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-get-current")
	public AjaxReturn staffGetCurrent() throws BusinessException {
		// 获取当前用户
		Long curuserId = getAuthentication();
		return new AjaxReturn(200, null, staffService.selectByUserId(curuserId, curuserId));
	}

	/**
	 * 查询员工信息分页列表
	 */
	@ApiOperation(value = "查询员工信息分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "员工信息分页列表查询成功", response = Staff.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-page-list")
	public AjaxReturn staffPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "staffName", value = "员工姓名", required = false) @RequestParam(required = false, defaultValue = "") String staffName,
			@ApiParam(name = "staffCode", value = "员工编码", required = false) @RequestParam(required = false, defaultValue = "") String staffCode,
			@ApiParam(name = "departmentId", value = "部门Id", required = false) @RequestParam(required = false, defaultValue = "") Long departmentId,
			@ApiParam(name = "teamId", value = "班组Id", required = false) @RequestParam(required = false, defaultValue = "") Long teamId)
			throws BusinessException {
		log.debug("StaffController.staffPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("staffName:" + staffName);
			log.debug("staffCode:" + staffCode);

			log.debug("departmentId:" + departmentId);
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

		Staff staff = new Staff();
		staff.setStaffName(staffName);
		staff.setStaffCode(staffCode);
		staff.setDepartmentId(departmentId);
		staff.setTeamId(teamId);
		Pagination<Staff> pagination = staffService.selectPageList(curuserId, staff, queryDto);
		log.debug("StaffController.staffPageList>>>");
		return pagination;
	}

	/**
	 * 员工信息修改
	 */
	@ApiOperation(value = "员工信息修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "员工信息修改成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-update")
	@OperateLog(info = "员工修改[员工姓名:%s->员工状态:%s]", params = { "staffName", "staffStatus" })
	public AjaxReturn staffUpdate(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "staffName", value = "员工姓名", required = false) @RequestParam(required = false, defaultValue = "") String staffName,
			@ApiParam(name = "staffCode", value = "员工编码", required = false) @RequestParam(required = false, defaultValue = "") String staffCode,
			@ApiParam(name = "gender", value = "员工性别", required = false) @RequestParam(required = false, defaultValue = "") Integer gender,
			@ApiParam(name = "birthday", value = "出生年月", required = false) @RequestParam(required = false, defaultValue = "") Date birthday,
			@ApiParam(name = "position", value = "员工职务", required = false) @RequestParam(required = false, defaultValue = "") String position,
			@ApiParam(name = "departmentId", value = "部门Id", required = false) @RequestParam(required = false, defaultValue = "") Long departmentId,
			@ApiParam(name = "departmentName", value = "部门名称", required = false) @RequestParam(required = false) String departmentName,
			@ApiParam(name = "teamId", value = "班组Id", required = false) @RequestParam(required = false) Long teamId,
			@ApiParam(name = "teamName", value = "班组名称", required = false) @RequestParam(required = false) String teamName,
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false, defaultValue = "") String remark)
			throws BusinessException {
		log.debug("StaffController.staffUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
			log.debug("staffName:" + staffName);
			log.debug("staffCode:" + staffCode);
			log.debug("gender:" + gender);
			log.debug("birthday:" + birthday);
			log.debug("position:" + position);
			log.debug("departmentId:" + departmentId);
			log.debug("departmentName:" + departmentName);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		staffService.checkStaffCode(staffCode, pkId);
		// 封装参数信息
		Staff staff = new Staff();
		staff.setStaffName(staffName);
		staff.setStaffCode(staffCode);
		staff.setGender(gender);
		staff.setPosition(position);
		staff.setDepartmentId(departmentId);
		staff.setDepartmentName(departmentName);
		staff.setTeamId(teamId);
		staff.setTeamName(teamName);
		staff.setRemark(remark);

		log.debug("StaffController.staffUpdate>>>");

		return new AjaxReturn(200, null, staffService.updateActiveById(userId, staff, pkId));
	}

	/**
	 * 员工信息表导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "员工信息表导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "员工信息表导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-import")
	// @Secure()
	public AjaxReturn staffImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<Staff> staffList = ExcelUtils.getInstance().readExcel2Objects(inputStream, Staff.class, 1, 2000, 0);
		inputStream.close();
		staffService.staffImport(userId, staffList);
		return new AjaxReturn(200, null, 1);
	}

	/**
	 * 员工信息表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "员工信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "员工信息表导出", response = Staff.class) })
	// @Secure()
	@GetMapping(path = { "/staff-export" })
	public ResponseEntity<byte[]> downloadStaffExcel() throws Exception {
		Long userId = getAuthentication();
		List<Staff> excelVOList = staffService.select(userId, new Staff());
		excelVOList.stream().forEach(item -> {
			if (item.getGender() == 1) {
				item.setGenderName("男");
			} else {
				item.setGenderName("女");
			}
		});
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, Staff.class, true, null, true);
		return getResponseEntity(data, "员工信息表.xlsx");
	}

	/**
	 * 员工信息同步
	 */
	@ApiOperation(value = "员工信息同步", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "员工信息同步", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-synchro")
	@OperateLog(info = "员工信息同步[员工编码:%s]", params = { "staffCode" })
	public AjaxReturn staffSynchro(
			@ApiParam(name = "staffCode", value = "员工编码", required = true) @RequestParam(required = true) String staffCode)
			throws BusinessException {
		// 获取当前用户
		Long userId = getAuthentication();
		return new AjaxReturn(200, null, staffService.staffSynchro(userId, staffCode));
	}
}
