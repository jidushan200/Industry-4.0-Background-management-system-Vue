package org.tsinghuatj.web.controller.tool;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.tool.domain.CheckResult;
import org.tsinghuatj.tool.service.ICheckResultService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/base" })
public class ToolCheckResultController extends BaseController{

	
private @Autowired(required = false) ICheckResultService checkResultservice;
	
	/**
	  * 刀具质检结果添加
	  */
	@ApiOperation(value = "刀具质检结果添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "添加刀具质检结果成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/check-result-add")
	public AjaxReturn checkResultAdd(
			@ApiParam(name = "colId", value = "质检项Id",required = true) @RequestParam(required = true)Long colId,
			@ApiParam(name = "checkCol", value = "质检项",required = true) @RequestParam(required = true)String checkCol,
			@ApiParam(name = "checkResult", value = "质检结果",required = true) @RequestParam(required = true)String checkResult
			)throws BusinessException {
			log.debug("CheckResultController.checkColAdd<<<");
			if (log.isDebugEnabled()) {	
				log.debug("checkCol:" + checkCol);
				log.debug("colId:" + colId);
				log.debug("checkResult:" + checkResult);
			}
			//获取当前用户
			Long userId = getAuthentication();
			
			//封装参数信息
			CheckResult result = new CheckResult();
			result.setColId(colId);
			result.setCheckResult(checkResult);
			result.setCheckCol(checkCol);
			
			log.debug("CheckResultController.checkColAdd>>>");
			
			return new AjaxReturn(200,null,checkResultservice.insert(userId, result));
	}
	
	/**
	  * 质检结果修改
	  */
	@ApiOperation(value = "质检结果修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "质检结果修改成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/check-result-update")
	public AjaxReturn checkResultUpdate(
			@ApiParam(name = "pkId", value = "刀具编码",required = true) @RequestParam(required = true)Long pkId,
			@ApiParam(name = "colId", value = "质检项Id",required = true) @RequestParam(required = true)Long colId,
			@ApiParam(name = "checkCol", value = "质检项",required = true) @RequestParam(required = true)String checkCol,
			@ApiParam(name = "checkResult", value = "质检结果",required = true) @RequestParam(required = true)String checkResult
			)throws BusinessException {
			log.debug("CheckResultController.checkResultUpdate<<<");
			if (log.isDebugEnabled()) {
				log.debug("pkId:" + pkId);
				log.debug("checkCol:" + checkCol);
				log.debug("colId:" + colId);
				log.debug("checkResult:" + checkResult);
			}
			//获取当前用户
			Long userId = getAuthentication();
			
			//封装参数信息
			CheckResult result = new CheckResult();
			result.setColId(colId);
			result.setCheckResult(checkResult);
			result.setCheckCol(checkCol);
			
			log.debug("CheckResultController.checkResultUpdate>>>");

			return new AjaxReturn(200,null,checkResultservice.updateActiveById(userId, result, pkId));
	}
	/**
	 * 质检结果导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "质检结果导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "质检结果导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/check-result-import")
	// @Secure()
	public AjaxReturn checkResultImport(@RequestParam(value = "file", required = true) MultipartFile file)
			throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<CheckResult> CheckResultList = ExcelUtils.getInstance().readExcel2Objects(inputStream, CheckResult.class, 1, 100,
				0);
		inputStream.close();
		checkResultservice.checkResultImport(userId, CheckResultList);
		return new AjaxReturn(200, null, 1);
	}
   /**
    * 质检结果导出
    * @return
    * @throws Exception
    */
	@ApiOperation(value = "质检结果导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "质检结果导出", response = CheckResult.class) })
	// @Secure()
	@PostMapping(path = { "/check-result-export" })
	public ResponseEntity<byte[]> downloadCheckResultExcel() throws Exception {
		Long userId = getAuthentication();
		List<CheckResult> excelVOList = checkResultservice.select(userId, new CheckResult());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, CheckResult.class, true, null,
				true);
		return getResponseEntity(data,  "质检结果表.xlsx");
	}
	
}
