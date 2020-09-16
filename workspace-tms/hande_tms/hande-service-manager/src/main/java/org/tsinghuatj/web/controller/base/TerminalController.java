package org.tsinghuatj.web.controller.base;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tsinghuatj.base.domain.Terminal;
import org.tsinghuatj.base.service.ITerminalService;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.QRCodeUtil;
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
public class TerminalController extends BaseController{

	private @Autowired(required = false) ITerminalService terminalService;
	private @Value("${file.save.path:/}") String filePath; 
	
	/**
	  * 终端信息添加
	  */
	@ApiOperation(value = "终端信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "终端信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/terminal-add")
	@OperateLog(info = "供应商添加[终端编码:%s->Ip:%s]", params = { "terminalCode","ip" })
	public AjaxReturn 终端(
			@ApiParam(name = "terminalCode", value = "终端编码",required = true) @RequestParam(required = true)String terminalCode,
			@ApiParam(name = "departmentId", value = "部门id",required = false) @RequestParam(required = false, defaultValue = "")Long departmentId,
			@ApiParam(name = "departmentName", value = "部门名称",required = false) @RequestParam(required = false, defaultValue = "")String departmentName,
			@ApiParam(name = "managerId", value = "管理者id",required = false) @RequestParam(required = false, defaultValue = "")Long managerId,
			@ApiParam(name = "managerName", value = "管理者名称",required = false) @RequestParam(required = false, defaultValue = "")String managerName,
			@ApiParam(name = "ip", value = "IP地址",required = false) @RequestParam(required = false, defaultValue = "")String ip
			)throws BusinessException {
			log.debug("TerminalController.terminalAdd<<<");
			if (log.isDebugEnabled()) {	
				log.debug("terminalCode:" + terminalCode);
				log.debug("departmentId:" + departmentId);
				log.debug("departmentName:" + departmentName);
				log.debug("managerId:" + managerId);
				log.debug("managerName:" + managerName);
				log.debug("ip:" + ip);
			}
			//获取当前用户
			Long userId = getAuthentication();
			
			//封装参数信息
			Terminal terminal = new Terminal();
			terminal.setTerminalCode(terminalCode);
			terminal.setDepartmentId(departmentId);
			terminal.setDepartmentName(departmentName);
			terminal.setManagerId(managerId);
			terminal.setManagerName(managerName);
			terminal.setIp(ip);
			
			log.debug("TerminalController.terminalAdd>>>");
			
			return new AjaxReturn(200,null,terminalService.insert(userId, terminal));
	}

	/**
	 * 终端信息删除
	 */
	@ApiOperation(value = "终端信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "终端信息删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/terminal-delete-by-id")
	public AjaxReturn terminalDeleteById(@ApiParam(name = "pkId", value = "主键",required = true) @RequestParam(required = true) Long pkId)throws BusinessException {
		log.debug("TerminalController.toolPartDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		
		//获取当前用户
		Long userId = getAuthentication();
		//参数校验
		
		Validate.isTrue(pkId > 0);
		
		log.debug("TerminalController.toolPartDeleteById>>>");
		return new AjaxReturn(200,null,terminalService.deleteById(userId, pkId));
	}
	
	/**
	 * 终端分页列表 
	 */
	@ApiOperation(value = "终端分页列表  ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "终端分页列表查询成功", response = Terminal.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/terminal-page-list")
	public AjaxReturn terminalPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, 
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "terminalCode", value = "终端编码",required = false) @RequestParam(required = false, defaultValue = "")String terminalCode
			)throws BusinessException {
		log.debug("TerminalController.toolPartPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("terminalCode:" + terminalCode);
		}
		
		//获取当前用户
		Long curuserId = getAuthentication();
		//参数校验
		Validate.isTrue(page >= 1);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		
		Terminal terminal = new Terminal();
		terminal.setTerminalCode(terminalCode);
		
		Pagination<Terminal> pagination = terminalService.selectPageList(curuserId, terminal, queryDto);
		log.debug("TerminalController.toolPartPageList>>>");
		return pagination;
	}
	
	/**
	  * 终端信息更新
	  */
	@ApiOperation(value = "终端信息更新", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "终端信息更新成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/terminal-update")
	@OperateLog(info = "供应商添加[终端编码:%s->管理者:%s]", params = { "terminalCode","managerName" })
	public AjaxReturn terminalUpdate(
			@ApiParam(name = "pkId", value = "主键",required = true) @RequestParam(required = true)Long pkId,
			@ApiParam(name = "terminalCode", value = "终端编码",required = true) @RequestParam(required = true)String terminalCode,
			@ApiParam(name = "departmentId", value = "部门id",required = false) @RequestParam(required = false, defaultValue = "")Long departmentId,
			@ApiParam(name = "departmentName", value = "部门名称",required = false) @RequestParam(required = false, defaultValue = "")String departmentName,
			@ApiParam(name = "managerId", value = "管理者id",required = false) @RequestParam(required = false, defaultValue = "")Long managerId,
			@ApiParam(name = "managerName", value = "管理者名称",required = false) @RequestParam(required = false, defaultValue = "")String managerName,
			@ApiParam(name = "ip", value = "IP地址",required = false) @RequestParam(required = false, defaultValue = "")String ip
			)throws BusinessException {
			log.debug("TerminalController.terminalUpdate<<<");
			if (log.isDebugEnabled()) {	
				log.debug("terminalCode:" + terminalCode);
				log.debug("departmentId:" + departmentId);
				log.debug("departmentName:" + departmentName);
				log.debug("managerId:" + managerId);
				log.debug("managerName:" + managerName);
				log.debug("ip:" + ip);
				log.debug("pkId:" + pkId);
			}
			//获取当前用户
			Long userId = getAuthentication();
			
			//封装参数信息
			Terminal terminal = new Terminal();
			terminal.setTerminalCode(terminalCode);
			terminal.setDepartmentId(departmentId);
			terminal.setDepartmentName(departmentName);
			terminal.setManagerId(managerId);
			terminal.setManagerName(managerName);
			terminal.setIp(ip);
			
			log.debug("TerminalController.terminalUpdate>>>");
			
			return new AjaxReturn(200,null,terminalService.updateActiveById(userId, terminal, pkId));
	}
	
	/**
	 * 终端信息导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "终端信息导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "终端信息导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/terminal-import")
	// @Secure()
	public AjaxReturn terminalImport(@RequestParam(value = "file", required = true) MultipartFile file)
			throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<Terminal> terminalList = ExcelUtils.getInstance().readExcel2Objects(inputStream, Terminal.class, 1, 100,
				0);
		inputStream.close();
		terminalService.terminalImport(userId, terminalList);
		return new AjaxReturn(200, null, 1);
	}
	/**
	 * 物料制件信息表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "物料制件信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "物料制件信息表导出", response = Terminal.class) })
	// @Secure()
	@GetMapping(path = { "/terminal-export" })
	public ResponseEntity<byte[]> terminalExport() throws Exception {
		Long userId = getAuthentication();
		List<Terminal> excelVOList = terminalService.select(userId, new Terminal());
//		excelVOList.stream().forEach(item -> {
//			if (item.getGender() == 1) {
//				item.setGenderName("男");
//			} else {
//				item.setGenderName("女");
//			}
//		});
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, Terminal.class, true, null, true);
		return getResponseEntity(data, "终端信息表.xlsx");
	}
	
	@ApiOperation(value = "二维码打印", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "二维码打印", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/draw-image")
	@Secure()
	public AjaxReturn drawImage(
			@ApiParam(name = "type", value = "图片类型", required = true) @RequestParam(required = true) String imageType,
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId, 
			@ApiParam(name = "count", value = "张数", required = false) @RequestParam(required = false) Integer count) throws Exception {
		String imgName = filePath + "qrcode//"+imageType+"//" + pkId + ".png";
		QRCodeUtil.drawImage(imgName, count);
		return new AjaxReturn(200, null, 1);
	}
}
