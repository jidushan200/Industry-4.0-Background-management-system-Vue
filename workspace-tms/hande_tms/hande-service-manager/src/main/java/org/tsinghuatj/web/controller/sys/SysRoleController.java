package org.tsinghuatj.web.controller.sys;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.StringUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.sys.domain.SysRoleInfo;
import org.tsinghuatj.sys.service.ISysRoleInfoService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/role" })
public class SysRoleController extends BaseController{
	
	private @Autowired(required = false) ISysRoleInfoService sysRoleInfoService;
	
	/**
	 * 角色分页查询
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value="角色列表",code=200,produces = "application/json",notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "角色列表获取成功", response = SysRoleInfo.class) })
	@RequestMapping(method=RequestMethod.POST,value="/sys-role-page-list")
	@Secure()
	public AjaxReturn sysRolePageList(
			@ApiParam(name = "page",value = "page",required = true)@RequestParam(required = true)Integer page,
			@ApiParam(name = "rows",value = "rows",required = false)@RequestParam(required = false,defaultValue = "10")Integer rows,
			@ApiParam(name = "roleName",value = "角色名称",required = false)@RequestParam(required = false,defaultValue = "")String roleName)throws BusinessException{
		log.debug("SysRoleController.sysRolePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("roleName:" + roleName);
		}
		//获取当前用户
		Long userId = getAuthentication();
		//参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);
		//Validate.isTrue(userId > 0);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		SysRoleInfo sysRoleInfo = new SysRoleInfo();
		sysRoleInfo.setRoleName(roleName);
		
		Pagination<SysRoleInfo> pagination = sysRoleInfoService.selectPageList(userId, sysRoleInfo, queryDto);
		log.debug("SysRoleController.sysRolePageList>>>");
		return pagination;
	}
	
	/**
	 * 角色列表查询
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value="角色列表查询",code=200,produces = "application/json",notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "角色列表查询成功", response = SysRoleInfo.class) })
	@RequestMapping(method=RequestMethod.POST,value="/sys-role-list")
	@Secure()
	public AjaxReturn sysRoleList()throws BusinessException{
		log.debug("SysRoleController.sysRoleList<<<");

		//获取当前用户
		Long userId = getAuthentication();
		//参数校验
		//Validate.isTrue(userId > 0);
		SysRoleInfo sysRoleInfo = new SysRoleInfo();
		
		log.debug("SysRoleController.sysRoleList>>>");
		return new AjaxReturn(200, null, sysRoleInfoService.select(userId, sysRoleInfo));
	}
	
	/**
	 * 角色信息根据ID查询
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value="角色ID查询",code=200,produces = "application/json",notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "角色信息获取成功", response = SysRoleInfo.class) })
	@RequestMapping(method=RequestMethod.POST,value="/sys-role-get-by-id")
	@Secure()
	public AjaxReturn sysRoleById(@ApiParam(name = "pkId",value = "pkId",required = true)@RequestParam(required = true)Long pkId)throws BusinessException{
		log.debug("SysRoleController.sysRoleById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		//获取当前用户
		Long userId = getAuthentication();
		//参数校验
		Validate.isTrue(pkId > 0);
		//Validate.isTrue(userId > 0);
		
		log.debug("SysRoleController.sysRoleById>>>");
		return new AjaxReturn(200, null, sysRoleInfoService.selectById(userId, pkId));
	}
	
	/**
	 * 角色信息修改
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value="角色信息修改",code=200,produces = "application/json",notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "角色修改成功", response = SysRoleInfo.class) })
	@OperateLog(info = "角色信息修改[角色名称:%s]", params = { "roleName"})
	@RequestMapping(method=RequestMethod.POST,value="/sys-role-update")
	@Secure()
	public AjaxReturn sysRoleUpdate(@ApiParam(name = "pkId",value = "pkId",required = true)@RequestParam(required = true)Long pkId,
			@ApiParam(name = "roleName",value = "角色名称",required = false)@RequestParam(required = true)String roleName,
			@ApiParam(name = "remark",value = "角色简介",required = false)@RequestParam(required = true)String remark,
			@ApiParam(name = "authCodes",value = "权限码",required = false)@RequestParam(required = false,defaultValue = "") List<String> authCodes
			)throws BusinessException{
		log.debug("SysRoleController.sysRoleUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
			log.debug("roleName:" + roleName);
			log.debug("remark:" + remark);
			log.debug("authCodes:" + authCodes);
		}
		//获取当前用户
		Long userId = getAuthentication();
		//参数校验
		//Validate.isTrue(userId > 0);
		Validate.isTrue(pkId > 0);
		Validate.isTrue(StringUtils.isNotBlank(roleName));
		Validate.isTrue(StringUtils.isNotBlank(remark));
		
		// 封装参数信息
		SysRoleInfo sysRoleInfo = new SysRoleInfo();
		sysRoleInfo.setRoleName(roleName);
		sysRoleInfo.setRemark(remark);
		
		sysRoleInfoService.updateActiveById(userId, sysRoleInfo, pkId);
		log.debug("SysRoleController.sysRoleUpdate>>>");
		return new AjaxReturn(200,null,null);
	}
	
	/**
	 * 角色信息添加
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value="角色信息添加",code=200,produces = "application/json",notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "角色信息添加成功", response = SysRoleInfo.class) })
	@OperateLog(info = "角色信息添加[角色名称:%s]", params = { "roleName"})
	@RequestMapping(method=RequestMethod.POST,value="/sys-role-add")
	@Secure()
	public AjaxReturn sysRoleAdd(@ApiParam(name = "roleName",value = "角色名称",required = true)@RequestParam(required = true)String roleName,
			@ApiParam(name = "remark",value = "角色简介",required = false)@RequestParam(required = false,defaultValue = "")String remark,
			@ApiParam(name = "authCodes",value = "权限码",required = false)@RequestParam(required = false,defaultValue = "") List<String> authCodes)throws BusinessException{
		log.debug("SysRoleController.sysRoleAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("roleName:" + roleName);
			log.debug("remark:" + remark);
		}
		//获取当前用户
		Long userId = getAuthentication();
		//参数校验
		//Validate.isTrue(userId > 0);
		Validate.isTrue(StringUtils.isNotBlank(roleName));
		
		// 封装查询条件
		SysRoleInfo sysRoleInfo = new SysRoleInfo();
		sysRoleInfo.setRoleName(roleName);
		sysRoleInfo.setRemark(remark);
		sysRoleInfo.setRoleAuth("");
		sysRoleInfoService.insert(userId, sysRoleInfo);
		log.debug("SysRoleController.sysRoleAdd>>>");
		return new AjaxReturn(200,null,null);
	}
	
	/**
	 * 角色信息删除
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value="角色信息删除",code=200,produces = "application/json",notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "角色信息删除成功", response = SysRoleInfo.class) })
	@OperateLog(info = "角色信息删除[roleName:%s]", params = { "roleName"})
	@RequestMapping(method=RequestMethod.POST,value="/sys-role-delete")
	@Secure()
	public AjaxReturn sysRoleDelete(@ApiParam(name = "pkId",value = "pkId",required = true)@RequestParam(required = true)Long pkId)throws BusinessException{
		log.debug("SysRoleController.sysRoleDelete<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		//获取当前用户
		Long userId = getAuthentication();
		//参数校验
		//Validate.isTrue(userId > 0);
		Validate.isTrue(pkId > 0);
		
		sysRoleInfoService.deleteById(userId, pkId);
		log.debug("SysRoleController.sysRoleDelete>>>");
		return new AjaxReturn(200,null,1);
	}
	
	/**
	 * 角色批量删除
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "角色批量删除", code = 200, produces = "application/json", notes = "用户批量删除")
	@ApiResponses({ @ApiResponse(code = 200, message = "角色批量删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/sys-role-batch-delete")
	@Secure() //
	public AjaxReturn batchRoleDelete(@ApiParam(name = "ids", value = "ids",required = true) @RequestParam(required = true) String ids)throws BusinessException {
		log.debug("SysRoleController.batchRoleDelete<<<");
		if (log.isDebugEnabled()) {
			log.debug("ids:" + ids);
		}
		//获取当前用户
		Long userId = getAuthentication();
		//参数校验
		Validate.isTrue(ids.length() > 0);
		Validate.isTrue(userId > 0);
		
		//Integer row = sysRoleInfoService.batchDeleteRoleByIds(userId, ids);
		log.debug("SysRoleController.batchRoleDelete>>>");
		return new AjaxReturn(200, null, null);
	}

}
