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
import org.tsinghuatj.base.domain.Department;
import org.tsinghuatj.base.domain.Team;
import org.tsinghuatj.base.service.ITeamService;
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
public class TeamController extends BaseController {

	private @Autowired(required = false) ITeamService teamService;

	/**
	 * 班组信息添加
	 */
	@ApiOperation(value = "班组信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "班组信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/team-add")
	@OperateLog(info = "班组添加[部门id:%s->班组名称:%s]", params = { "departmentName", "teamName" })
	public AjaxReturn TeamAdd(@ApiParam(name = "departmentId", value = "部门ID", required = true) @RequestParam(required = true) Long departmentId, @ApiParam(name = "departmentName", value = "部门名称", required = false) @RequestParam(required = false, defaultValue = "") String departmentName, @ApiParam(name = "teamName", value = "班组名称", required = false) @RequestParam(required = false, defaultValue = "") String teamName) throws BusinessException {
		log.debug("TeamController.teamAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("departmentId:" + departmentId);
			log.debug("departmentName:" + departmentName);
			log.debug("teamName:" + teamName);
		}
		// 获取当前用户
		Long userId = getAuthentication();

		// 封装参数信息
		Team team = new Team();
		team.setDepartmentId(departmentId);
		team.setDepartmentName(departmentName);
		team.setTeamName(teamName);

		log.debug("TeamController.teamAdd>>>");

		return new AjaxReturn(200, null, teamService.insert(userId, team));
	}

	/**
	 * 班组信息删除
	 */
	@ApiOperation(value = "班组信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "班组信息删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/team-delete-by-id")
	public AjaxReturn teamDeleteById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("TeamController.teamDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("TeamController.teamDeleteById>>>");
		return new AjaxReturn(200, null, teamService.deleteById(userId, pkId));
	}

	/**
	 * 班组列表 
	 */
	@ApiOperation(value = "班组列表  ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "班组列表查询成功", response = Team.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/team-list")
	public AjaxReturn teamList(
			@ApiParam(name = "departmentId", value = "部门ID",required = false) @RequestParam(required = false)Long departmentId
			)throws BusinessException {
		log.debug("TeamController.teamPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("departmentId:" + departmentId);
		}
		
		//获取当前用户
		Long curuserId = getAuthentication();
		
		Team team = new Team();
		team.setDepartmentId(departmentId);
		
		List<Team> teamList= teamService.select(curuserId, team);
		log.debug("TeamController.teamPageList>>>");
		return new AjaxReturn(200, null, teamList);
	}
	
	/**
	 * 班组分页列表
	 */
	@ApiOperation(value = "班组分页列表  ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "班组分页列表查询成功", response = Team.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/team-page-list")
	public AjaxReturn teamPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, @ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "departmentId", value = "部门ID", required = false) @RequestParam(required = false) Long departmentId, @ApiParam(name = "departmentName", value = "部门名称", required = false) @RequestParam(required = false, defaultValue = "") String departmentName, @ApiParam(name = "teamName", value = "班组名称", required = false) @RequestParam(required = false, defaultValue = "") String teamName) throws BusinessException {
		log.debug("TeamController.teamPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("departmentId:" + departmentId);
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

		Team team = new Team();
		team.setDepartmentId(departmentId);
		team.setDepartmentName(departmentName);
		team.setTeamName(teamName);

		Pagination<Team> pagination = teamService.selectPageList(curuserId, team, queryDto);
		log.debug("TeamController.teamPageList>>>");
		return pagination;
	}

	/**
	 * 班组信息更新
	 */
	@ApiOperation(value = "班组信息更新", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "班组信息更新成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/team-update")
	@OperateLog(info = "班组更新[部门id:%s->班组名称:%s]", params = { "departmentName", "teamName" })
	public AjaxReturn teamUpdate(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "departmentId", value = "部门ID", required = true) @RequestParam(required = true) Long departmentId, @ApiParam(name = "departmentName", value = "部门名称", required = false) @RequestParam(required = false, defaultValue = "") String departmentName, @ApiParam(name = "teamName", value = "班组名称", required = false) @RequestParam(required = false, defaultValue = "") String teamName) throws BusinessException {
		log.debug("TeamController.teamUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("departmentId:" + departmentId);
			log.debug("departmentName:" + departmentName);
			log.debug("teamName:" + teamName);
			log.debug("pkId:" + pkId);
		}
		// 获取当前用户
		Long userId = getAuthentication();

		// 封装参数信息
		Team team = new Team();
		team.setDepartmentId(departmentId);
		team.setDepartmentName(departmentName);
		team.setTeamName(teamName);

		log.debug("TeamController.teamUpdate>>>");

		return new AjaxReturn(200, null, teamService.updateActiveById(userId, team, pkId));
	}

	/**
	 * 根据部门查班组
	 */
	@ApiOperation(value = "部门id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "部门Id查询列表查询成功", response = Team.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/team-get-by-departmentId")
	public AjaxReturn teamGetBydepartmentId(@ApiParam(name = "departmentId", value = "部门ID", required = true) @RequestParam(required = true) Long departmentId) throws BusinessException {

		// 获取当前用户
		Long curuserId = getAuthentication();		
		return new AjaxReturn(200, null, teamService.selectByDepartmentId(curuserId, departmentId));
	}

	/**
	 * 根据部门查班组
	 */
	@ApiResponses({ @ApiResponse(code = 200, message = "班组列表", response = Team.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/team-get-list")
	public AjaxReturn teamGetList() throws BusinessException {

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		return new AjaxReturn(200, null, teamService.select(userId, new Team()));
	}
	
	
	
	@ApiOperation(value = "班组信息表导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "班组信息表导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/team-import")
	@Secure()
	public AjaxReturn teamImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<Team> teamList = ExcelUtils.getInstance().readExcel2Objects(inputStream, Team.class);
		inputStream.close();
		
		return new AjaxReturn(200, null, teamService.teamImport(userId, teamList));
	}

	
	@ApiOperation(value = "班组信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "班组信息表导出", response = Department.class) })
	@Secure()
	@GetMapping(path = { "/team-export" })
	public ResponseEntity<byte[]> downloadTeamExcel() throws Exception {
		Long userId = getAuthentication();
		List<Team> excelVOList = teamService.select(userId, new Team());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, Team.class, true, null, true);
		return getResponseEntity(data, "班组信息表.xlsx");
	}
	
	
	
}
