package org.tsinghuatj.web.controller.sys;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.sys.domain.SysAuthInfo;
import org.tsinghuatj.sys.domain.SysParam;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.service.ILoginService;
import org.tsinghuatj.sys.service.ISysParamService;
import org.tsinghuatj.sys.service.IUserAccountService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/user" })
public class UserAccountController extends BaseController {

	private @Autowired(required = false) IUserAccountService userAccountService;

	private @Autowired(required = false) ILoginService loginService;

	private @Autowired(required = false) ISysParamService paramService;

	/**
	 * 用户分页查询
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "用户分页列表", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "用户分页列表获取成功", response = UserAccount.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/user-account-page-list")
	@Secure() // 用户分页查询
	public AjaxReturn userAccountPageList(
			@ApiParam(name = "page", value = "页码", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "每页行数", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "departmentId", value = "每页行数", required = false) @RequestParam(required = false) Long departmentId, 
			@ApiParam(name = "loginName", value = "loginName", required = false) @RequestParam(required = false) String loginName, 
			@ApiParam(name = "realName", value = "realName", required = false) @RequestParam(required = false) String realName//
	) throws BusinessException {
		log.debug("UserAccountController.userAccountPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("loginName:" + loginName);
		}
		// 获取当前用户
		long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);
		// Validate.isTrue(curuserId > 0);

		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		UserAccount where = new UserAccount();
		where.setLoginName(loginName);
		where.setRealName(realName);
		where.setDepartmentId(departmentId);
		Pagination<UserAccount> pagination = userAccountService.selectPageList(curuserId, where, queryDto);
		log.debug("UserAccountController.userAccountPageList>>>");
		return pagination;
	}

	@ApiOperation(value = "获取当前用户信息", code = 200, produces = "application/json", notes = "获取当前用户信息")
	@ApiResponses({ @ApiResponse(code = 200, message = "获取当前用户信息", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/user-account-get-current")
	@Secure() //
	public AjaxReturn getCurrentUser() throws BusinessException {
		// 获取当前用户
		long userId = getAuthentication();

		return new AjaxReturn(200, null, userAccountService.selectById(userId, userId));
	}

	/**
	 * 用户名校验
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "登录名校验", code = 200, produces = "application/json", notes = "登录名校验")
	@ApiResponses({ @ApiResponse(code = 200, message = "登录名校验 (false-存在 true-不存在)", response = String.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/user-name-validate")
	// @Secure() //

	public String userNameValidate(@ApiParam(name = "loginName", value = "用户名", required = true) @RequestParam(required = true) String loginName) throws BusinessException {
		log.debug("UserAccountController.UserNameValidate<<<");
		if (log.isDebugEnabled()) {
			log.debug("loginName:" + loginName);
		}

		// 参数校验
		Validate.isTrue(StringUtils.isNotBlank(loginName));
		// 根据登录名查询
		UserAccount userAccount = loginService.getUserInfoByName(loginName);

		log.debug("UserAccountController.userNameValidate>>>");
		return null == userAccount ? "true" : "false";
	}

	/*
	 * 新增用户
	 */
	@ApiOperation(value = "新增用户", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "新增用户成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/user-account-add")
	@Secure()
	@OperateLog(info = "用户新增[账号:%s]", params = { "loginName" })
	public AjaxReturn userAccountAdd(@ApiParam(name = "loginName", value = "用户名", required = true) @RequestParam(required = true) String loginName, @ApiParam(name = "loginPwd", value = "密码", required = true) @RequestParam(required = true) String loginPwd, @ApiParam(name = "realName", value = "真实姓名", required = true) @RequestParam(required = true) String realName, @ApiParam(name = "mobile", value = "联系电话", required = false) @RequestParam(required = false) String mobile, @ApiParam(name = "roleId", value = "角色id", required = true) @RequestParam(required = true) Long roleId, @ApiParam(name = "fullAuthCode", value = "fullAuthCode") @RequestParam(required = false, defaultValue = "") List<String> fullAuthCode,
			@ApiParam(name = "authCodes", value = "authCodes") @RequestParam(required = false, defaultValue = "") List<String> authCodes) throws BusinessException {
		log.debug("UserAccountController.userAccountAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("loginName:" + loginName);
			log.debug("loginPwd:" + loginPwd);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(StringUtils.isNotBlank(loginName));
		Validate.isTrue(StringUtils.isNotBlank(loginPwd));
		userAccountService.checkLoginName(loginName, null);
		UserAccount userAccount = new UserAccount();
		// 封装用户信息参数
		userAccount = new UserAccount();
		userAccount.setLoginName(loginName);
		userAccount.setLoginPwd(loginPwd);
		userAccount.setRealName(realName);
		userAccount.setRoleId(roleId);
		userAccount.setMobile(mobile);
		String s = "";
		if (authCodes.size() > 0) {
			for (String auth : authCodes) {
				s = s + auth + ",";
			}
		}
		userAccount.setAuthCode(s);
		userAccountService.insert(userId, userAccount, fullAuthCode);
		log.debug("UserAccountController.userAccountAdd>>>");
		return new AjaxReturn(200, null, 1);
	}

	/**
	 * 用户修改
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "用户修改", code = 200, produces = "application/json", notes = "用户修改")
	@ApiResponses({ @ApiResponse(code = 200, message = "用户修改成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/user-account-update")
	@Secure() //
	@OperateLog(info = "用户修改[登录名:%s]", params = { "loginName" })
	public AjaxReturn userAccountUpdate(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "loginName", value = "登录名", required = true) @RequestParam(required = true) String loginName, @ApiParam(name = "realName", value = "真实姓名", required = true) @RequestParam(required = true) String realName, @ApiParam(name = "mobile", value = "联系电话", required = false) @RequestParam(required = false) String mobile, @ApiParam(name = "roleId", value = "角色id", required = false) @RequestParam(required = false) Long roleId, @ApiParam(name = "fullAuthCode", value = "fullAuthCode") @RequestParam(required = false, defaultValue = "") List<String> fullAuthCode,
			@ApiParam(name = "authCodes", value = "authCodes") @RequestParam(required = false, defaultValue = "") List<String> authCodes) throws BusinessException {
		log.debug("UserAccountController.userAccountUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
			log.debug("loginName:" + loginName);
			log.debug("realName:" + realName);
			log.debug("roleId:" + roleId);
			log.debug("mobile:" + mobile);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId > 0);
		// Validate.isTrue(userId > 0);
		Validate.isTrue(StringUtils.isNotBlank(loginName));

		// 封装参数信息
		userAccountService.checkLoginName(loginName, pkId);

		UserAccount userAccount = new UserAccount();
		userAccount.setLoginName(loginName);
		userAccount.setRealName(realName);
		userAccount.setRoleId(roleId);
		userAccount.setMobile(mobile);
		String s = "";
		if (authCodes.size() > 0) {
			for (String auth : authCodes) {
				s = s + auth + ",";
			}
		}
		userAccount.setAuthCode(s);
		userAccountService.updateActiveById(userId, userAccount, pkId, fullAuthCode);
		log.debug("UserAccountController.userAccountUpdate>>>");
		return new AjaxReturn(200, null, null);
	}

	/**
	 * 重置密码
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "重置密码", code = 200, produces = "application/json", notes = "重置密码")
	@ApiResponses({ @ApiResponse(code = 200, message = "重置密码", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/user-loginpwd-update")
	@Secure() //
	@OperateLog(info = "重置用户密码[用户名:%s]", params = { "loginName" })
	public AjaxReturn loginPwdUpdate(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("UserAccountController.userAccountUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId > 0);
		// Validate.isTrue(userId > 0);

		// 封装参数信息
		UserAccount userAccount = new UserAccount();
		userAccount.setPkId(pkId);
		SysParam param = paramService.selectByParamKey(userId, "defaultPassword");
		if (null != param) {
			userAccount.setLoginPwd(param.getParamValue());
		} else {
			userAccount.setLoginPwd("123456");
		}

		userAccountService.updateActiveById(userId, userAccount, pkId, null);
		log.debug("UserAccountController.userAccountUpdate>>>");
		return new AjaxReturn(200, null, null);
	}

	/**
	 * 用户密码修改
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "修改密码", code = 200, produces = "application/json", notes = "用户修改")
	@ApiResponses({ @ApiResponse(code = 200, message = "修改密码", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/user-my-loginpwd-update")
	@Secure() //
	@OperateLog(info = "用户密码修改", params = {})
	public AjaxReturn myLoginPwdUpdate(@ApiParam(name = "oldloginPwd", value = "旧密码", required = true) @RequestParam(required = true) String oldloginPwd, @ApiParam(name = "loginPwd", value = "新密码", required = true) @RequestParam(required = true) String loginPwd) throws BusinessException {
		log.debug("UserAccountController.myLoginPwdUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("oldloginPwd:" + oldloginPwd);
			log.debug("loginPwd:" + loginPwd);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(userId > 0);
		Validate.isTrue(StringUtils.isNotBlank(loginPwd));

		// 封装参数信息
		UserAccount userAccount = loginService.getUserInfoById(userId);
		PasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
		if (!bcryptPasswordEncoder.matches(oldloginPwd, userAccount.getLoginPwd())) {
			throw new BusinessException("old.loginpwd.error");
		}
		userAccount.setLoginPwd(loginPwd);
		userAccount.setAuthCode(null);
		userAccountService.updateActiveById(userId, userAccount, userId, null);
		log.debug("UserAccountController.myLoginPwdUpdate>>>");
		return new AjaxReturn(200, null, 1);
	}

	/**
	 * 用户删除
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "用户删除", code = 200, produces = "application/json", notes = "用户删除")
	@ApiResponses({ @ApiResponse(code = 200, message = "用户删除成功", response = AjaxReturn.class) })
	@OperateLog(info = "用户删除[loginName:%s]", params = { "loginName" })
	@RequestMapping(method = RequestMethod.POST, value = "/user-account-delete")
	@Secure() //
	public AjaxReturn userAccountDelete(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("UserAccountController.userAccountDelete<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId > 0);
		// Validate.isTrue(userId > 0);

		userAccountService.deleteById(userId, pkId);
		log.debug("UserAccountController.userAccountDelete>>>");
		return new AjaxReturn(200, null, 1);
	}

	/**
	 * 用户批量删除
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "用户批量删除", code = 200, produces = "application/json", notes = "用户批量删除")
	@ApiResponses({ @ApiResponse(code = 200, message = "用户批量删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/user-account-batch-delete")
	@Secure() //
	@OperateLog(info = "用户批量删除[%s]", params = { "ids" })
	public AjaxReturn batchUserAccountDelete(@ApiParam(name = "ids", value = "主键ID[例如1,2,3,10]", required = true) @RequestParam(required = true) String ids) throws BusinessException {
		log.debug("UserAccountController.batchUserAccountDelete<<<");
		if (log.isDebugEnabled()) {
			log.debug("ids:" + ids);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(ids.length() > 0);
		Validate.isTrue(userId > 0);

		Integer row = userAccountService.batchDeleteAccountByIds(userId, ids);
		log.debug("UserAccountController.batchUserAccountDelete>>>");
		return new AjaxReturn(200, null, row);
	}

	@ApiOperation(value = "获取用户信息", code = 200, produces = "application/json", notes = "")
	@GetMapping("/get_info")
	public AjaxReturn getInfo(String token) throws BusinessException {
		Map<String, Object> data = new HashMap<String, Object>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			CustomUser user = (CustomUser) authentication.getPrincipal();
			String auths = "";
			for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
				auths += grantedAuthority.getAuthority() + ",";
			}
			data.put("token", token);
			data.put("access", auths.split(","));
			data.put("userId", user.getId());
			data.put("roleId", user.getRoleId());
			data.put("departmentId", user.getDepartmentId());
			data.put("realName", user.getRealname());
		}
		return new AjaxReturn(200, "Login Success", data);
	}

	@ApiOperation(value = "刀具生命周期导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具生命周期导出", response = String.class) })
	@Secure()
	@GetMapping(path = { "/user-complete-export" })
	public String userCompleteExport(
			HttpServletRequest request,
			HttpServletResponse response, @ApiParam(name = "loginName", value = "登录名", required = false) @RequestParam(required = false) String loginName, 
			@ApiParam(name = "realName", value = "真实姓名", required = false) @RequestParam(required = false) String realName) throws Exception {

		Long userId = getAuthentication();

		UserAccount where = new UserAccount();
		where.setLoginName(loginName);
		where.setRealName(realName);

		List<UserAccount> list = userAccountService.selectCompletePageList(userId, where);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("账号完整信息");
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		row1.setHeight((short) (25 * 20));//
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个

		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格内容
		row1.createCell(0).setCellValue("用户名");
		row1.createCell(1).setCellValue("真实姓名");
		row1.createCell(2).setCellValue("角色");
		row1.createCell(3).setCellValue("权限编码");
		row1.createCell(4).setCellValue("权限名称");
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		int i = 1;
		int startRow;
		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		for (UserAccount ua : list) {
			int itemCount = 0;
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.setHeight((short) (25 * 20));//
			startRow = i;
			List<SysAuthInfo> itemList = ua.getAuthList();
			if (!CollectionUtils.isEmpty(itemList)) {
				itemCount = itemList.size();
			}

			dataRow.createCell(0).setCellValue(ua.getLoginName());
			dataRow.createCell(1).setCellValue(ua.getRealName());
			dataRow.createCell(2).setCellValue(ua.getRoleName());

			for (int j = 0; j < itemCount; j++) {
				if (j > 0) {
					dataRow = sheet.createRow(i);
					dataRow.setHeight((short) (25 * 20));// 目的是想把行高设置成25px
				}
				SysAuthInfo item = itemList.get(j);
				dataRow.createCell(3).setCellValue(item.getAuthorCode());
				dataRow.createCell(4).setCellValue(item.getTitle());

				i++;
			}
			if (itemCount <= 1) {
				continue;
			}
			int endRow = startRow + itemCount - 1;
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 2, 2));
		}

		// 输出Excel文件
		OutputStream output = response.getOutputStream();
		response.reset();
		String fileName = "用户完整信息.xls";
		// 获得浏览器信息并转换为大写
		String agent = request.getHeader("User-Agent").toUpperCase();
		if (agent.indexOf("MSIE") > 0 || (agent.indexOf("GECKO") > 0 && agent.indexOf("RV:11") > 0)) {
			// 微软的浏览器(IE和Edge浏览器)
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
		}
		response.setHeader("Content-disposition", "attachment; filename="+fileName);
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
		wb.close();
		return null;
	}

}
