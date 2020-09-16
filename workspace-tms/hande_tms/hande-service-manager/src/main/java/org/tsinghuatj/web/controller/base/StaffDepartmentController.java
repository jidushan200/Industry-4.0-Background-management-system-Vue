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
import org.tsinghuatj.base.domain.StaffDepartment;
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
public class StaffDepartmentController extends BaseController{
  
	private @Autowired(required = false) IStaffDepartmentService staffDepartmentService;
	/**
	 * 人员部门信息添加
	 */
	@ApiOperation(value = "人员部门信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "添加人员部门信息成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-department-add")
	@OperateLog(info = "员工部门添加[部门编码:%s->部门名称:%s]", params = { "departmentCode", "departmentName" })
	public AjaxReturn staffDepartmentAdd(@ApiParam(name = "departmentName", value = "部门名称", required = true) @RequestParam(required = true) String departmentName,
			@ApiParam(name = "departmentCode", value = "部门编码",required = true) @RequestParam(required = true)String departmentCode ) throws BusinessException {
		log.debug("StaffController.staffAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("departmentName:" + departmentName);
			log.debug("departmentCode:" + departmentCode);

		}
		// 获取当前用户
		Long userId = getAuthentication();

		// 封装参数信息
		StaffDepartment staffDepartment = new StaffDepartment();
		staffDepartment.setDepartmentCode(departmentCode);
		staffDepartment.setDepartmentName(departmentName);

		log.debug("StaffController.staffAdd>>>");

		return new AjaxReturn(200, null, staffDepartmentService.insert(userId, staffDepartment));
	}
	
	/**
	 * 查询人员部门信息分页列表
	 */
	@ApiOperation(value = "查询人员部门信息分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "人员部门信息分页列表查询成功", response = StaffDepartment.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-department-page-list")
	public AjaxReturn staffDepartmentPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "departmentName", value = "部门名称", required = false) @RequestParam(required = false) String departmentName,
			@ApiParam(name = "departmentCode", value = "部门编码",required = false) @RequestParam(required = false)String departmentCode 
			) throws BusinessException {
		log.debug("StaffController.staffPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			
			log.debug("departmentName:" + departmentName);
			
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

		StaffDepartment staffDepartment = new StaffDepartment();
		staffDepartment.setDepartmentCode(departmentCode);
		staffDepartment.setDepartmentName(departmentName);
		

		Pagination<StaffDepartment> pagination = staffDepartmentService.selectPageList(curuserId, staffDepartment, queryDto);
		log.debug("StaffController.staffPageList>>>");
		return pagination;
	}
	
	/**
	 * 人员部门信息修改
	 */
	@ApiOperation(value = "人员部门信息修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "修改人员部门信息成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-department-update")
	@OperateLog(info = "员工部门添加[部门编码:%s->部门名称:%s]", params = { "departmentCode", "departmentName" })
	public AjaxReturn staffDepartmentUpdate(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "departmentName", value = "部门名称", required = false) @RequestParam(required = false) String departmentName,
			@ApiParam(name = "departmentCode", value = "部门编码",required = false) @RequestParam(required = false)String departmentCode ) throws BusinessException {
		log.debug("StaffController.staffAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("departmentName:" + departmentName);
			log.debug("departmentCode:" + departmentCode);

		}
		// 获取当前用户
		Long userId = getAuthentication();

		// 封装参数信息
		StaffDepartment staffDepartment = new StaffDepartment();
		staffDepartment.setDepartmentCode(departmentCode);
		staffDepartment.setDepartmentName(departmentName);
		
		

		log.debug("StaffController.staffAdd>>>");

		return new AjaxReturn(200, null, staffDepartmentService.updateActiveById(userId, staffDepartment, pkId));
	}
	
	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "职工号id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "职工号Id查询列表查询成功", response = StaffDepartment.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-department-get-by-id")
	public AjaxReturn staffDepartmentGetById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("ToolOutboundController.toolOutboundGetByToolId<<<");
		if (log.isDebugEnabled()) {
		
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		log.debug("ToolOutboundController.toolOutboundGetByToolId>>>");
		return new AjaxReturn(200, null, staffDepartmentService.selectById(userId, pkId));
	}
	
	/**
	 * 人员部门信息表导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "人员部门信息表导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "人员信息表导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/staff-department-import")
	@Secure()
	public AjaxReturn staffDepartmentImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<StaffDepartment> staffDepartmentList = ExcelUtils.getInstance().readExcel2Objects(inputStream, StaffDepartment.class, 1, 1000, 0);
		inputStream.close();
		staffDepartmentService.staffDepartmentImport(userId, staffDepartmentList);
		return new AjaxReturn(200, null, 1);
	}
	
	/**
	 * 人员部门信息表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "人员部门信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "人员部门信息表导出", response = StaffDepartment.class) })
	// @Secure()
	@GetMapping(path = { "/staff-department-export" })
	public ResponseEntity<byte[]> downloadDepartmentExcel() throws Exception {
		Long userId = getAuthentication();
		List<StaffDepartment> excelVOList = staffDepartmentService.select(userId, new StaffDepartment());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, StaffDepartment.class, true, null, true);
		return getResponseEntity(data, "部门信息表.xlsx");
	}
}
