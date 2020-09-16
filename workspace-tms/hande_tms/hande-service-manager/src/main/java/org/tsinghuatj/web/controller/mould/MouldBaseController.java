package org.tsinghuatj.web.controller.mould;

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
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.mould.service.IMouldBaseService;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.mould.domain.MouldBase;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/mould" })
public class MouldBaseController extends BaseController {

	private @Autowired(required = false) IMouldBaseService mouldBaseService;

	/**
	 * 模具基础信息添加
	 */
	@ApiOperation(value = "模具基础信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具基础信息添加成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-base-add")
	@OperateLog(info = "模具基础信息添加[模具编码:%s->模具图号:%s]", params = { "mouldNumber", "mouldMap" })
	public AjaxReturn mouldBaseAdd(@ApiParam(name = "mouldNumber", value = "模具编码", required = true) @RequestParam(required = true) String mouldNumber, @ApiParam(name = "mouldMap", value = "模具图号", required = true) @RequestParam(required = true) String mouldMap, @ApiParam(name = "mouldName", value = "模具图号", required = true) @RequestParam(required = true) String mouldName, @ApiParam(name = "mouldType", value = "模具类型", required = true) @RequestParam(required = true) Integer mouldType, @ApiParam(name = "lifeMax", value = "最大寿命", required = false) @RequestParam(required = false, defaultValue = "") Integer lifeMax) throws BusinessException {
		log.debug("mouldBaseController.mouldBaseAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("mouldNumber:" + mouldNumber);
			log.debug("mouldMap:" + mouldMap);
			log.debug("mouldName:" + mouldName);
			log.debug("mouldType:" + mouldType);
			log.debug("lifeMax:" + lifeMax);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		mouldBaseService.mouldNumberCheck(mouldNumber, null);
		// 封装参数信息
		MouldBase mouldBase = new MouldBase();
		mouldBase.setMouldNumber(mouldNumber);
		mouldBase.setMouldMap(mouldMap);
		mouldBase.setMouldName(mouldName);
		mouldBase.setMouldType(mouldType);
		mouldBase.setLifeMax(lifeMax);
		log.debug("mouldBaseController.mouldBaseAdd>>>");

		return new AjaxReturn(200, null, mouldBaseService.insert(userId, mouldBase));
	}

	/**
	 * 模具基础信息删除
	 */
	@ApiOperation(value = "模具基础信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具基础信息成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-base-delete-by-id")
	@OperateLog(info = "模具基础信息删除[模具编码:%s]", params = { "mouldNumber" })
	public AjaxReturn mouldBaseDeleteById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "mouldNumber", value = "mouldNumber", required = true) @RequestParam(required = true) String mouldNumber) throws BusinessException {
		log.debug("mouldBaseController.mouldBaseDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("mouldBaseController.mouldBaseDeleteById>>>");
		return new AjaxReturn(200, null, mouldBaseService.deleteById(userId, pkId));
	}

	/**
	 * 模具基础信息删除
	 */
	@ApiOperation(value = "模具基础信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具基础信息成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-base-get-by-id")
	@OperateLog(info = "模具基础信息删除[模具编码:%s]", params = { "pkId" })
	public AjaxReturn mouldBaseGetById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("mouldBaseController.mouldBaseGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("mouldBaseController.mouldBaseGetById>>>");
		return new AjaxReturn(200, null, mouldBaseService.selectById(userId, pkId));
	}

	/**
	 * 模具基础信息列表查询
	 */
	@ApiOperation(value = "模具基础信息列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具基础信息列表查询成功", response = MouldBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-base-list")
	public AjaxReturn mouldBaseList() throws BusinessException {
		log.debug("mouldBaseController.mouldBaseList<<<");
		if (log.isDebugEnabled()) {
		}

		// 获取当前用户
		Long userId = getAuthentication();
		MouldBase mouldBase = new MouldBase();

		log.debug("mouldBaseController.mouldBaseList>>>");
		return new AjaxReturn(200, null, mouldBaseService.select(userId, mouldBase));
	}

	/**
	 * 模具基础信息列表查询
	 */
	@ApiOperation(value = "模具基础信息列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具基础信息列表查询成功", response = MouldBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-base-get-by-number")
	public AjaxReturn mouldBaseGetByNumber(@ApiParam(name = "mouldNumber", value = "模具编码", required = true) @RequestParam(required = true) String mouldNumber) throws BusinessException {
		log.debug("mouldBaseController.mouldBaseGetByNumber<<<");

		// 获取当前用户
		Long userId = getAuthentication();
		log.debug("mouldBaseController.mouldBaseGetByNumber>>>");
		return new AjaxReturn(200, null, mouldBaseService.selectByNumber(userId, mouldNumber));
	}

	/**
	 * 模具基础信息分页列表
	 */
	@ApiOperation(value = "模具基础信息分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具基础信息分页列表查询成功", response = MouldBase.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-base-page-list")
	public AjaxReturn mouldBasePageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "mouldNumber", value = "模具编码", required = false) @RequestParam(required = false) String mouldNumber) throws BusinessException {
		log.debug("mouldBaseController.mouldBasePageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("mouldNumber:" + mouldNumber);

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

		MouldBase mouldBase = new MouldBase();
		mouldBase.setMouldNumber(mouldNumber);

		Pagination<MouldBase> pagination = mouldBaseService.selectPageList(curuserId, mouldBase, queryDto);
		log.debug("mouldBaseController.mouldBasePageList>>>");
		return pagination;
	}

	/**
	 * 模具基础信息修改
	 */
	@ApiOperation(value = "模具基础信息修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具基础信息修改成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-base-update")
	@OperateLog(info = "模具基础信息修改[模具编码:%s->模具图号:%s]", params = { "mouldNumber", "mouldMap" })
	public AjaxReturn mouldBaseUpdate(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "mouldNumber", value = "模具编码", required = false) @RequestParam(required = false) String mouldNumber, @ApiParam(name = "mouldMap", value = "模具图号", required = false) @RequestParam(required = false) String mouldMap, @ApiParam(name = "mouldName", value = "模具图号", required = false) @RequestParam(required = false) String mouldName, @ApiParam(name = "mouldType", value = "模具类型", required = false) @RequestParam(required = false) Integer mouldType,
			@ApiParam(name = "lifeMax", value = "最大寿命", required = false) @RequestParam(required = false, defaultValue = "") Integer lifeMax) throws BusinessException {
		log.debug("mouldBaseController.mouldBaseUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("mouldNumber:" + mouldNumber);
			log.debug("mouldMap:" + mouldMap);
			log.debug("mouldName:" + mouldName);
			log.debug("lifeMax:" + lifeMax);
			log.debug("pkId:" + pkId);

		}
		// 获取当前用户
		Long userId = getAuthentication();
		mouldBaseService.mouldNumberCheck(mouldNumber, pkId);
		// 封装参数信息
		MouldBase mouldBase = new MouldBase();
		mouldBase.setMouldNumber(mouldNumber);
		mouldBase.setMouldMap(mouldMap);
		mouldBase.setMouldType(mouldType);
		mouldBase.setMouldName(mouldName);
		mouldBase.setLifeMax(lifeMax);

		log.debug("mouldBaseController.mouldBaseUpdate>>>");
		return new AjaxReturn(200, null, mouldBaseService.updateActiveById(userId, mouldBase, pkId));
	}

	/**
	 * 模具基础信息导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "模具基础信息导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具基础信息导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-base-import")
	@OperateLog(info = "模具基础信息导入")
	// @Secure()
	public AjaxReturn measureBaseImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<MouldBase> mouldBaseList = ExcelUtils.getInstance().readExcel2Objects(inputStream, MouldBase.class, 1, 1000, 0);
		inputStream.close();
		mouldBaseService.mouldBaseImport(userId, mouldBaseList);
		return new AjaxReturn(200, null, 1);
	}

	/**
	 * 物料信息表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "物料信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "物料信息表导出", response = MouldBase.class) })
	@Secure()
	@GetMapping(path = { "/mould-base-export" })
	// @OperateLog(info = "模具基础信息导出")
	public ResponseEntity<byte[]> downloadmouldBaseExcel() throws Exception {
		Long userId = getAuthentication();
		List<MouldBase> excelVOList = mouldBaseService.select(userId, new MouldBase());
		excelVOList.stream().forEach(item -> {
			if (item.getMouldType() == 1) {
				item.setMouldTypeName("普锻模具");
			} else if (item.getMouldType() == 2) {
				item.setMouldTypeName("精锻模具");
			} else if (item.getMouldType() == 3) {
				item.setMouldTypeName("淬火模具");
			}
		});
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, MouldBase.class, true, null, true);
		return getResponseEntity(data, "模具信息表.xlsx");
	}
}
