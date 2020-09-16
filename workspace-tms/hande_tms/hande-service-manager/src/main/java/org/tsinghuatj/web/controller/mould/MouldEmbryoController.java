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
import org.tsinghuatj.base.domain.Equipment;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.mould.domain.MouldEmbryo;
import org.tsinghuatj.mould.service.IMouldEmbryoService;
import org.tsinghuatj.support.annotation.OperateLog;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/mould" })
public class MouldEmbryoController extends BaseController{

	private @Autowired(required = false) IMouldEmbryoService mouldEmbryoService;

	/**
	 * 模具坯信息添加
	 */
	@ApiOperation(value = "模具坯信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "添加模具坯信息成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/mould-embryo-add")
	@OperateLog(info = "模具坯添加[模具坯编码:%s->模具坯名称:%s]", params = { "embryoCode", "embryoName" })
	public AjaxReturn equipmentAdd(@ApiParam(name = "embryoCode", value = "模具坯编码", required = true) @RequestParam(required = true) String embryoCode, 
			@ApiParam(name = "embryoName", value = "模具坯名称", required = false) @RequestParam(required = false, defaultValue = "") String embryoName 
			) throws BusinessException {
		log.debug("EquipmentController.equipmentAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("embryoCode:" + embryoCode);
			log.debug("embryoName:" + embryoName);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		mouldEmbryoService.checkEmbryoCode(embryoCode,null);
		// 封装参数信息
		MouldEmbryo embryo = new MouldEmbryo();
		embryo.setEmbryoCode(embryoCode);
		embryo.setEmbryoName(embryoName);

		log.debug("EquipmentController.equipmentAdd>>>");

		return new AjaxReturn(200, null, mouldEmbryoService.insert(userId, embryo));
	}

	/**
	 * 模具坯信息删除
	 */
	@ApiOperation(value = "模具坯信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具坯信息删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/embryo-delete-by-id")
	@OperateLog(info = "模具坯删除[模具坯编码:%s]", params = { "embryoCode" })
	public AjaxReturn equipmentDeleteById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "embryoCode", value = "embryoCode", required = false) @RequestParam(required = false) String embryoCode
			) throws BusinessException {
		log.debug("EquipmentController.toolCheckDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("EquipmentController.equipmentDeleteById>>>");
		return new AjaxReturn(200, null, mouldEmbryoService.deleteById(userId, pkId));
	}

	/**
	 * 模具坯信息列表查询
	 */
	@ApiOperation(value = "模具坯信息列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具坯信息列表查询成功", response = Equipment.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/get-embryo-list")
	public AjaxReturn equipmentList() throws BusinessException {
		log.debug("EquipmentController.equipmentList<<<");
		if (log.isDebugEnabled()) {
		}

		// 获取当前用户
		Long userId = getAuthentication();

		log.debug("EquipmentController.equipmentList>>>");
		return new AjaxReturn(200, null, mouldEmbryoService.select(userId, new MouldEmbryo()));
	}

	/**
	 * 查询模具坯信息分页列表
	 */
	@ApiOperation(value = "查询模具坯信息分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具坯信息分页列表查询成功", response = MouldEmbryo.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/embryo-page-list")
	public AjaxReturn equipmentPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, 
			@ApiParam(name = "embryoCode", value = "模具坯编码", required = true) @RequestParam(required = true) String embryoCode, 
			@ApiParam(name = "embryoName", value = "模具坯名称", required = false) @RequestParam(required = false, defaultValue = "") String embryoName 
			) throws BusinessException {
		log.debug("EquipmentController.equipmentPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("embryoCode:" + embryoCode);
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

		MouldEmbryo embryo = new MouldEmbryo();
		embryo.setEmbryoCode(embryoCode);
		embryo.setEmbryoName(embryoName);

		Pagination<MouldEmbryo> pagination = mouldEmbryoService.selectPageList(curuserId, embryo, queryDto);
		log.debug("EquipmentController.equipmentPageList>>>");
		return pagination;
	}

	/**
	 * 模具坯信息修改
	 */
	@ApiOperation(value = "模具坯信息修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具坯信息修改成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/embryo-update")
	@OperateLog(info = "模具坯修改[模具坯Id:%s->模具坯名称:%s]", params = { "pkId", "equipmentName" })
	public AjaxReturn equipmentUpdate(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId,
			@ApiParam(name = "embryoCode", value = "模具坯编码", required = true) @RequestParam(required = true) String embryoCode, 
			@ApiParam(name = "embryoName", value = "模具坯名称", required = false) @RequestParam(required = false, defaultValue = "") String embryoName 
			) throws BusinessException {
		log.debug("EquipmentController.equipmentUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
			log.debug("embryoCode:" + embryoCode);
			log.debug("embryoName:" + embryoName);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		mouldEmbryoService.checkEmbryoCode(embryoCode,pkId);
		// 封装参数信息
		MouldEmbryo embryo = new MouldEmbryo();
		embryo.setEmbryoCode(embryoCode);
		embryo.setEmbryoName(embryoName);

		log.debug("EquipmentController.equipmentUpdate>>>");

		return new AjaxReturn(200, null, mouldEmbryoService.updateActiveById(userId, embryo, pkId));
	}

	/**
	 * 模具坯表导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "模具坯表导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具坯表导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/embryo-import")
	@OperateLog(info = "模具坯导入")
	// @Secure()
	public AjaxReturn equipmentImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<MouldEmbryo> embryoList = ExcelUtils.getInstance().readExcel2Objects(inputStream, MouldEmbryo.class, 1, 5000, 0);
		inputStream.close();
		mouldEmbryoService.embryoImport(userId, embryoList);
		return new AjaxReturn(200, null, 1);
	}

	/**
	 * 模具坯表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "模具坯表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具坯表导出", response = MouldEmbryo.class) })
	@OperateLog(info = "模具坯导出")
	// @Secure()
	@GetMapping(path = { "/embryo-export" })
	public ResponseEntity<byte[]> downloadEquipmentExcel() throws Exception {
		Long userId = getAuthentication();
		List<MouldEmbryo> excelVOList = mouldEmbryoService.select(userId, new MouldEmbryo());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, MouldEmbryo.class, true, null, true);
		return getResponseEntity(data, "模具坯表.xlsx");
	}

	/**
	 * 模具坯信息同步
	 */
	@ApiOperation(value = "模具坯信息同步", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "模具坯信息同步", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/embryo-synchro")
	// @OperateLog(info = "部门添加[部门编码:%s->部门名称:%s]", params = {
	// "departmentCode","departmentName" })
	public AjaxReturn equipmentSynchro(@ApiParam(name = "embryoCode", value = "模具坯编码", required = true) @RequestParam(required = true) String embryoCode
			) throws BusinessException {
		// 获取当前用户
		Long userId = getAuthentication();
		return new AjaxReturn(200, null, mouldEmbryoService.embryoSynchro(userId, embryoCode));
	}
}
