package org.tsinghuatj.web.controller.fixture;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.fixture.domain.FixtureCheck;
import org.tsinghuatj.fixture.domain.FixtureCheckResult;
import org.tsinghuatj.fixture.domain.FixtureWaitCheck;
import org.tsinghuatj.fixture.service.IFixtureCheckService;
import org.tsinghuatj.fixture.service.IFixtureWaitCheckService;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.JsonUtils;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.tool.domain.ToolBase;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/fixture" })
public class FixtureCheckController extends BaseController {

	private @Autowired(required = false) IFixtureCheckService checkService;
	private @Autowired(required = false) IFixtureWaitCheckService waitCheckService;

	@ApiOperation(value = "查询夹具质检报告分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具质检报告分页列表查询成功", response = FixtureCheck.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/wait-check-page-list")
	public AjaxReturn waitCheckPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "fixtureBarcode", value = "物料条码", required = false) @RequestParam(required = false) String fixtureBarcode,
			@ApiParam(name = "fixtureMap", value = "夹具图号", required = false) @RequestParam(required = false) String fixtureMap,
			@ApiParam(name = "checkType", value = "检验类型", required = false) @RequestParam(required = false) Integer checkType)
			throws BusinessException {
		log.debug("FixtureCheckController.waitCheckPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 0);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);

		FixtureWaitCheck where = new FixtureWaitCheck();
		where.setCheckStatus(0);
		where.setFixtureMap(fixtureMap);
		where.setFixtureBarcode(fixtureBarcode);
		Pagination<FixtureWaitCheck> pagination = waitCheckService.selectPageList(userId, where, queryDto);
		log.debug("FixtureCheckController.waitCheckPageList>>>");
		return pagination;
	}

	/**
	 * 夹具质检报告添加
	 */
	@ApiOperation(value = "夹具质检报告添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "添加夹具质检报告成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-check-add")
	@OperateLog(info = "夹具质检报告新增[夹具编号:%s->夹具条码:%s->质检类型:%s]", params = { "fixtureNumber", "fixtureBarcode",
			"checkTypeName" })
	public AjaxReturn fixtureCheckAdd(
			@ApiParam(name = "fixtureId", value = "夹具Id", required = false) @RequestParam(required = false) Long fixtureId,
			@ApiParam(name = "fixtureBarcode", value = "物料条码", required = false) @RequestParam(required = false) String fixtureBarcode,
			@ApiParam(name = "FixtureNumber", value = "物料编码", required = true) @RequestParam(required = true) String fixtureNumber,
			@ApiParam(name = "checkType", value = "质检类型", required = true) @RequestParam(required = true) Integer checkType,
			@ApiParam(name = "waitCheckId", value = "待检id", required = false) @RequestParam(required = false) Long waitCheckId,
			@ApiParam(name = "supplierId", value = "供应商Id", required = false) @RequestParam(required = false) Long supplierId,
			@ApiParam(name = "standardId", value = "质检标准Id", required = true) @RequestParam(required = true) Long standardId,
			@ApiParam(name = "checkResult", value = "质检结果", required = true) @RequestParam(required = true) Integer checkResult,
			@ApiParam(name = "checkStatus", value = "质检状态", required = true) @RequestParam(required = true) Integer checkStatus,
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false, defaultValue = "") String remark,
			@ApiParam(name = "appendixIds", value = "附件ID", required = false) @RequestParam(required = false) String appendixIds,
			@ApiParam(name = "resultList", value = "检验项", required = true) @RequestParam(required = true) String resultList)
			throws BusinessException {
		log.debug("FixtureCheckController.FixtureCheckAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("FixtureId:" + fixtureId);
			log.debug("fixtureBarcode:" + fixtureBarcode);
			log.debug("checkType:" + checkType);
			log.debug("remark:" + remark);
		}
		// 获取当前用户
		CustomUser user = getCompleteAuthentication();

		// 封装参数信息
		FixtureCheck fixtureCheck = new FixtureCheck();
		fixtureCheck.setFixtureId(fixtureId);
		fixtureCheck.setFixtureBarcode(fixtureBarcode);
		fixtureCheck.setFixtureNumber(fixtureNumber);
		fixtureCheck.setCheckType(checkType);
		fixtureCheck.setStandardId(standardId);
		fixtureCheck.setSupplierId(supplierId);
		fixtureCheck.setCheckTime(new Date());
		fixtureCheck.setChecker(user.getRealname());
		fixtureCheck.setCheckResult(checkResult);
		fixtureCheck.setCheckStatus(checkStatus);
		fixtureCheck.setRemark(remark);
		fixtureCheck.setWaitCheckId(waitCheckId);
		List<FixtureCheckResult> results = JsonUtils.json2list(resultList, FixtureCheckResult.class);
		fixtureCheck.setResultList(results);
		log.debug("FixtureCheckController.FixtureCheckAdd>>>");

		return new AjaxReturn(200, null, checkService.insert(user.getId(), fixtureCheck, waitCheckId, appendixIds));
	}

	/**
	 * 夹具质检报告信息修改
	 */
	@ApiOperation(value = "夹具质检报告信息修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具质检报告信息修改成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-check-update")
	@OperateLog(info = "夹具质检报告修改[夹具编号:%s->夹具条码:%s->质检类型:%s]", params = { "fixtureNumber", "fixtureBarcode",
			"checkType" })
	public AjaxReturn fixtureCheckUpdate(
			@ApiParam(name = "pkId", value = "报告id", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "standardId", value = "质检标准Id", required = true) @RequestParam(required = true) Long standardId,
			@ApiParam(name = "fixtureId", value = "夹具Id", required = false) @RequestParam(required = false) Long fixtureId,
			@ApiParam(name = "fixtureBarcode", value = "夹具编码", required = false) @RequestParam(required = false) String fixtureBarcode,
			@ApiParam(name = "fixtureNumber", value = "物料编码", required = false) @RequestParam(required = false) String fixtureNumber,
			@ApiParam(name = "fixtureSeq", value = "夹具顺序号", required = false) @RequestParam(required = false) String fixtureSeq,
			@ApiParam(name = "checkType", value = "质检类型", required = true) @RequestParam(required = true) Integer checkType,
			@ApiParam(name = "waitCheckId", value = "待检Id", required = false) @RequestParam(required = false) Long waitCheckId,
			@ApiParam(name = "checkTime", value = "质检时间", required = true) @RequestParam(required = true) Date checkTime,
			@ApiParam(name = "checkResult", value = "质检结果", required = true) @RequestParam(required = true) Integer checkResult,
			@ApiParam(name = "checkStatus", value = "质检状态", required = true) @RequestParam(required = true) Integer checkStatus,
			@ApiParam(name = "remark", value = "备注", required = false) @RequestParam(required = false, defaultValue = "") String remark,
			@ApiParam(name = "appendixIds", value = "附件ID", required = false) @RequestParam(required = false) String appendixIds,
			@ApiParam(name = "resultList", value = "检验项", required = true) @RequestParam(required = true) String resultList)
			throws BusinessException {
		log.debug("FixtureCheckController.FixtureCheckUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
			log.debug("checkTime:" + checkTime);
			log.debug("remark:" + remark);
		}
		// 获取当前用户

		// 封装参数信息
		FixtureCheck fixtureCheck = new FixtureCheck();
		CustomUser user = getCompleteAuthentication();
		fixtureCheck.setChecker(user.getRealname());
		fixtureCheck.setCheckTime(checkTime);
		fixtureCheck.setCheckResult(checkResult);
		fixtureCheck.setFixtureBarcode(fixtureBarcode);
		fixtureCheck.setFixtureNumber(fixtureNumber);
		fixtureCheck.setCheckTime(checkTime);
		fixtureCheck.setChecker(user.getRealname());
		fixtureCheck.setCheckResult(checkResult);
		fixtureCheck.setCheckStatus(checkStatus);
		fixtureCheck.setRemark(remark);
		fixtureCheck.setCheckType(checkType);
		fixtureCheck.setWaitCheckId(waitCheckId);
		List<FixtureCheckResult> results = JsonUtils.json2list(resultList, FixtureCheckResult.class);
		fixtureCheck.setResultList(results);
		log.debug("FixtureCheckController.FixtureCheckUpdate>>>");
		return new AjaxReturn(200, null, checkService.updateActiveById(user.getId(), fixtureCheck, pkId, appendixIds));
	}

	/**
	 * 夹具质检报告删除
	 */
	@ApiOperation(value = "夹具质检报告删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具质检报告删除成功", response = AjaxReturn.class) })
	@OperateLog(info = "夹具质检报告删除[id:%s]", params = { "pkId" })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-check-delete-by-id")
	public AjaxReturn fixtureCheckDeleteById(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "checkType", value = "质检类型", required = true) @RequestParam(required = true) Integer checkType)
			throws BusinessException {
		log.debug("FixtureCheckController.FixtureCheckDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("FixtureCheckController.FixtureCheckDeleteById>>>");
		return new AjaxReturn(200, null, checkService.deleteById(userId, pkId, checkType));
	}

	/**
	 * 夹具质检报告id查询
	 */
	@ApiOperation(value = "夹具质检报告id查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具质检报告Id查询成功", response = FixtureCheck.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-check-get-by-id")
	public AjaxReturn fixtureCheckGetById(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId)
			throws BusinessException {
		log.debug("FixtureCheckController.FixtureCheckGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}
		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验
		Validate.isTrue(pkId >= 1);

		log.debug("FixtureCheckController.FixtureCheckGetById>>>");
		return new AjaxReturn(200, null, checkService.selectById(curuserId, pkId));
	}

	/**
	 * 查询夹具质检报告分页列表
	 */
	@ApiOperation(value = "查询夹具质检报告分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具质检报告分页列表查询成功", response = FixtureCheck.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-check-page-list")
	public AjaxReturn fixtureCheckPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "fixtureName", value = "夹具名称", required = false) @RequestParam(required = false) String fixtureName,
			@ApiParam(name = "fixtureMap", value = "夹具图号", required = false) @RequestParam(required = false) String fixtureMap,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate,
			@ApiParam(name = "checkType", value = "检验类型", required = false) @RequestParam(required = false) Integer checkType,
			@ApiParam(name = "checkResult", value = "检验结果", required = false) @RequestParam(required = false) Integer checkResult,
			@ApiParam(name = "checkStatus", value = "检验状态", required = false) @RequestParam(required = false) Integer checkStatus,
			@ApiParam(name = "handleResult", value = "处理结果", required = false) @RequestParam(required = false) Integer handleResult

	) throws BusinessException {
		log.debug("FixtureCheckController.FixtureCheckPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("fixtureName:" + fixtureName);
			log.debug("checkType:" + checkType);
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
		queryDto.setBeginDate(beginDate);
		queryDto.setEndDate(endDate);

		FixtureCheck fixtureCheck = new FixtureCheck();
		fixtureCheck.setFixtureName(fixtureName);
		fixtureCheck.setFixtureMap(fixtureMap);
		fixtureCheck.setCheckType(checkType);
		fixtureCheck.setCheckResult(checkResult);
		fixtureCheck.setCheckStatus(checkStatus);
		fixtureCheck.setHandleResult(handleResult);
		Pagination<FixtureCheck> pagination = checkService.selectPageList(curuserId, fixtureCheck, queryDto);
		log.debug("FixtureCheckController.FixtureCheckPageList>>>");
		return pagination;
	}

	@ApiOperation(value = "夹具保养导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具保养导出", response = ToolBase.class) })
	@Secure()
	@GetMapping(path = { "/fixture-check-export" })
	public ResponseEntity<byte[]> checkExport(
			@ApiParam(name = "fixtureName", value = "夹具名称", required = false) @RequestParam(required = false) String fixtureName,
			@ApiParam(name = "fixtureMap", value = "夹具图号", required = false) @RequestParam(required = false) String fixtureMap,
			@ApiParam(name = "beginDate", value = "开始日期", required = false) @RequestParam(required = false, defaultValue = "") Date beginDate,
			@ApiParam(name = "endDate", value = "结束日期", required = false) @RequestParam(required = false, defaultValue = "") Date endDate,
			@ApiParam(name = "checkType", value = "检验类型", required = false) @RequestParam(required = false) Integer checkType)
			throws Exception {
		Long userId = getAuthentication();
		FixtureCheck fixtureCheck = new FixtureCheck();
		fixtureCheck.setFixtureName(fixtureName);
		fixtureCheck.setFixtureMap(fixtureMap);
		fixtureCheck.setCheckType(checkType);
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(1);
		queryDto.setRows(500000);

		Pagination<FixtureCheck> pagination = checkService.selectPageList(userId, fixtureCheck, queryDto);
		for (FixtureCheck item : pagination.getRows()) {
			if (item.getCheckType() == 4) {
				item.setCheckTypeName("新夹具质检");
			} else if (item.getCheckType() == 5) {
				item.setCheckTypeName("修磨质检");
			} else if (item.getCheckType() == 6) {
				item.setCheckTypeName("夹具点检");
			}
			item.setCheckResultName((item.getCheckResult()==1?"合格":"不合格"));
			item.setCheckTimeStr(DateFormatUtils.format(item.getCheckTime(), "yyyy-MM-dd HH:mm:ss"));
		}
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray((List<FixtureCheck>) pagination.getRows(),
				FixtureCheck.class, true, null, true);
		return getResponseEntity(data, "夹具检验记录.xlsx");
	}

}
