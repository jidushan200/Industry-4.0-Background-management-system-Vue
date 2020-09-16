package org.tsinghuatj.web.controller.fixture;

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
import org.tsinghuatj.fixture.domain.FixturePart;
import org.tsinghuatj.fixture.service.IFixturePartService;
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
@RequestMapping({ "/fixture" })
public class FixturePartController extends BaseController {

	private @Autowired(required = false) IFixturePartService fixturePartService;

	/**
	 * 夹具制件分页
	 */
	@ApiOperation(value = "夹具制件分页列表  ", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具制件分页列表查询成功", response = FixturePart.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixturePart-page-list")
	public AjaxReturn fixturePartPageList(
			@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page,
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows,
			@ApiParam(name = "partCode", value = "零件编号", required = false) @RequestParam(required = false) String partCode,
			@ApiParam(name = "partName", value = "零件编号", required = false) @RequestParam(required = false) String partName,
			@ApiParam(name = "fixtureNumber", value = "夹具编号", required = false) @RequestParam(required = false) String fixtureNumber,
			@ApiParam(name = "fixtureName", value = "夹具名称", required = false) @RequestParam(required = false) String fixtureName
			)
			throws BusinessException {
		log.debug("FixturePartController.fixturePartPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("partCode:" + partCode);
			log.debug("fixtureNumber:" + fixtureNumber);
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

		FixturePart fixturePart = new FixturePart();
		fixturePart.setFixtureNumber(fixtureNumber);
		fixturePart.setFixtureName(fixtureName);
		fixturePart.setPartCode(partCode);
		fixturePart.setPartName(partName);
		Pagination<FixturePart> pagination = fixturePartService.selectPageList(curuserId, fixturePart, queryDto);
		log.debug("ToolPartController.toolPartPageList>>>");
		return pagination;
	}

	/**
	 * 夹具制件新增
	 */
	@ApiOperation(value = "夹具制件新增", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具制件新增成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixturePart-add")
	@OperateLog(info = "夹具制件新增[夹具编号:%s->制件编号:%s]", params = { "fixtureNumber", "fixtureNumber" })
	public AjaxReturn fixturePartAdd(
			@ApiParam(name = "fixtureId", value = "夹具ID", required = true) @RequestParam(required = true) Long fixtureId,
			@ApiParam(name = "partId", value = "制件ID", required = true) @RequestParam(required = true) Long partId

	) throws BusinessException {
		log.debug("ToolPartController.fixturePartAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("fixtureId:" + fixtureId);
			log.debug("partId:" + partId);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		fixturePartService.checkFixturePart(null, fixtureId, partId);
		// 封装参数信息
		FixturePart fixturePart = new FixturePart();
		fixturePart.setFixtureId(fixtureId);
		fixturePart.setPartId(partId);
		log.debug("ToolPartController.fixturePartAdd>>>");
		return new AjaxReturn(200, null, fixturePartService.insert(userId, fixturePart));
	}

	/**
	 * 夹具制件更新
	 */
	@ApiOperation(value = "夹具制件更新", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具制件更新成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixturePart-update")
	@OperateLog(info = "夹具制件修改[夹具编号:%s->制件编号:%s]", params = { "fixtureNumber", "fixtureNumber" })
	public AjaxReturn fixturePartUpdate(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "fixtureId", value = "夹具ID", required = false) @RequestParam(required = false) Long fixtureId,
			@ApiParam(name = "partId", value = "制件ID", required = false) @RequestParam(required = false) Long partId

	) throws BusinessException {
		log.debug("ToolPartController.fixturePartUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("fixtureId:" + fixtureId);
			log.debug("partId:" + partId);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		fixturePartService.checkFixturePart(null, fixtureId, partId);
		// 封装参数信息
		FixturePart fixturePart = new FixturePart();
		fixturePart.setFixtureId(pkId);
		fixturePart.setFixtureId(fixtureId);
		fixturePart.setPartId(partId);
		log.debug("ToolPartController.fixturePartUpdate>>>");
		return new AjaxReturn(200, null, fixturePartService.updateActiveById(userId, fixturePart, pkId));
	}

	/**
	 * 夹具制件删除
	 */
	@ApiOperation(value = "夹具制件删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具制件删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixturepart-delete")
	@OperateLog(info = "夹具制件删除[夹具编号:%s->制件编号:%s]", params = { "fixtureNumber", "fixtureNumber" })
	public AjaxReturn fixturePartDelete(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId)
			throws BusinessException {
		log.debug("ToolPartController.toolPartDelete<<<");

		// 获取当前用户
		Long userId = getAuthentication();
		log.debug("ToolPartController.toolPartDelete>>>");
		return new AjaxReturn(200, null, fixturePartService.deleteById(userId, pkId));
	}

	/**
	 * 根据编码查找
	 * 
	 * @param toolNumber
	 * @param partCode
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/fixturepart-get-by-number")
	public AjaxReturn toolPartGetByCode(
			@ApiParam(name = "fixtureNumber", value = "夹具编号", required = false) @RequestParam(required = false) String fixtureNumber,
			@ApiParam(name = "partCode", value = "制件编号", required = false) @RequestParam(required = false) String partCode

	) throws BusinessException {
		log.debug("ToolPartController.toolPartGetByCode<<<");
		if (log.isDebugEnabled()) {
			log.debug("fixtureNumber:" + fixtureNumber);
			log.debug("partCode:" + partCode);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		// 封装参数信息
		FixturePart fixturePart = new FixturePart();
		fixturePart.setFixtureNumber(fixtureNumber);
		fixturePart.setPartCode(partCode);
		log.debug("ToolPartController.toolPartGetByCode>>>");
		return new AjaxReturn(200, null, fixturePartService.select(userId, fixturePart));
	}

	/**
	 * 夹具制件信息表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "夹具制件信息表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "夹具制件信息表导出", response = FixturePart.class) })
	// @Secure()
	@GetMapping(path = { "/fixture-part-export" })
	public ResponseEntity<byte[]> downloadfixturePartExcel() throws Exception {
		Long userId = getAuthentication();
		List<FixturePart> excelVOList = fixturePartService.select(userId, new FixturePart());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, FixturePart.class, true, null,
				true);
		return getResponseEntity(data, "夹具制件信息表.xlsx");
	}

	/**
	 * 物料制件导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "物料制件导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "物料制件导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/fixture-part-import")
	@Secure()
	@OperateLog(info = "夹具制件导入", params = {})
	public AjaxReturn fixturePartImport(@RequestParam(value = "file", required = true) MultipartFile file)
			throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<FixturePart> fpList = ExcelUtils.getInstance().readExcel2Objects(inputStream, FixturePart.class, 1, 1000,
				0);
		inputStream.close();
		fixturePartService.importFixture(userId, fpList);
		return new AjaxReturn(200, null, 1);
	}

}
