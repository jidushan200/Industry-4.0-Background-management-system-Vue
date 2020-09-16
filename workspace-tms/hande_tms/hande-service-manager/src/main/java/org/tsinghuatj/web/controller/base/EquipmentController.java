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
import org.tsinghuatj.base.domain.Equipment;
import org.tsinghuatj.base.service.IEquipmentService;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.support.annotation.OperateLog;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/base" })
public class EquipmentController extends BaseController {

	private @Autowired(required = false) IEquipmentService equipmentService;

	/**
	 * 设备信息添加
	 */
	@ApiOperation(value = "设备信息添加", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "添加设备信息成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/equipment-add")
	@OperateLog(info = "设备添加[设备编码:%s->设备名称:%s]", params = { "equipmentCode", "equipmentName" })
	public AjaxReturn equipmentAdd(
			@ApiParam(name = "equipmentCode", value = "设备编码", required = true) @RequestParam(required = true) String equipmentCode, 
			@ApiParam(name = "equipmentName", value = "设备名称", required = false) @RequestParam(required = false, defaultValue = "") String equipmentName, 
			@ApiParam(name = "departmentId", value = "部门Id", required = true) @RequestParam(required = true) Long departmentId, 
			@ApiParam(name = "departmentName", value = "存放地点", required = true) @RequestParam(required = true) String departmentName, @ApiParam(name = "amount", value = "数量", required = true) @RequestParam(required = true) Integer amount) throws BusinessException {
		log.debug("EquipmentController.equipmentAdd<<<");
		if (log.isDebugEnabled()) {
			log.debug("equipmentCode:" + equipmentCode);
			log.debug("equipmentName:" + equipmentName);
			log.debug("departmentId:" + departmentId);
			log.debug("departmentName:" + departmentName);
			log.debug("amount:" + amount);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		equipmentService.checkEquipmentCode(equipmentCode,null);
		// 封装参数信息
		Equipment equipment = new Equipment();
		equipment.setEquipmentCode(equipmentCode);
		equipment.setEquipmentName(equipmentName);
		equipment.setDepartmentId(departmentId);
		equipment.setDepartmentName(departmentName);
		equipment.setAmount(amount);

		log.debug("EquipmentController.equipmentAdd>>>");

		return new AjaxReturn(200, null, equipmentService.insert(userId, equipment));
	}

	/**
	 * 设备信息删除
	 */
	@ApiOperation(value = "设备信息删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "设备信息删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/equipment-delete-by-id")
	@OperateLog(info = "设备删除[设备编号:%s]", params = { "equipmentCode" })
	public AjaxReturn equipmentDeleteById(@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId) throws BusinessException {
		log.debug("EquipmentController.toolCheckDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("EquipmentController.equipmentDeleteById>>>");
		return new AjaxReturn(200, null, equipmentService.deleteById(userId, pkId));
	}

	/**
	 * 设备信息列表查询
	 */
	@ApiOperation(value = "设备信息列表查询", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "设备信息列表查询成功", response = Equipment.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/get-equipment-list")
	public AjaxReturn equipmentList() throws BusinessException {
		log.debug("EquipmentController.equipmentList<<<");
		if (log.isDebugEnabled()) {
		}

		// 获取当前用户
		Long userId = getAuthentication();

		log.debug("EquipmentController.equipmentList>>>");
		return new AjaxReturn(200, null, equipmentService.select(userId, new Equipment()));
	}

	/**
	 * 查询设备信息分页列表
	 */
	@ApiOperation(value = "查询设备信息分页列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "设备信息分页列表查询成功", response = Equipment.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/equipment-page-list")
	public AjaxReturn equipmentPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, 
			@ApiParam(name = "equipmentCode", value = "刀具编码", required = false) @RequestParam(required = false) String equipmentCode, 
			@ApiParam(name = "equipmentName", value = "刀具名称", required = false) @RequestParam(required = false) String equipmentName,
			@ApiParam(name = "tagNumber", value = "标签号", required = false) @RequestParam(required = false) String tagNumber,
			@ApiParam(name = "departmentName", value = "所属部门", required = false) @RequestParam(required = false) String departmentName
			) throws BusinessException {
		log.debug("EquipmentController.equipmentPageList<<<");
		if (log.isDebugEnabled()) {
			log.debug("page:" + page);
			log.debug("rows:" + rows);
			log.debug("equipmentCode:" + equipmentCode);
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

		Equipment equipment = new Equipment();
		equipment.setEquipmentCode(equipmentCode);
		equipment.setEquipmentName(equipmentName);
		equipment.setDepartmentName(departmentName);
		equipment.setTagNumber(tagNumber);
		Pagination<Equipment> pagination = equipmentService.selectPageList(curuserId, equipment, queryDto);
		log.debug("EquipmentController.equipmentPageList>>>");
		return pagination;
	}

	/**
	 * 设备信息修改
	 */
	@ApiOperation(value = "设备信息修改", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "设备信息修改成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/equipment-update")
	@OperateLog(info = "设备修改[设备Id:%s->设备名称:%s]", params = { "pkId", "equipmentName" })
	public AjaxReturn equipmentUpdate(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId, @ApiParam(name = "equipmentCode", value = "设备编码", required = true) @RequestParam(required = true) String equipmentCode, @ApiParam(name = "equipmentName", value = "设备名称", required = false) @RequestParam(required = false, defaultValue = "") String equipmentName, @ApiParam(name = "departmentId", value = "部门Id", required = true) @RequestParam(required = true) Long departmentId, @ApiParam(name = "departmentName", value = "存放地点", required = true) @RequestParam(required = true) String departmentName,
			@ApiParam(name = "amount", value = "数量", required = true) @RequestParam(required = true) Integer amount) throws BusinessException {
		log.debug("EquipmentController.equipmentUpdate<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
			log.debug("equipmentCode:" + equipmentCode);
			log.debug("equipmentName:" + equipmentName);
			log.debug("departmentId:" + departmentId);
			log.debug("departmentName:" + departmentName);
			log.debug("amount:" + amount);
		}
		// 获取当前用户
		Long userId = getAuthentication();
		equipmentService.checkEquipmentCode(equipmentCode,pkId);
		// 封装参数信息
		Equipment equipment = new Equipment();
		equipment.setEquipmentName(equipmentName);
		equipment.setEquipmentCode(equipmentCode);
		equipment.setDepartmentId(departmentId);
		equipment.setDepartmentName(departmentName);
		equipment.setAmount(amount);

		log.debug("EquipmentController.equipmentUpdate>>>");

		return new AjaxReturn(200, null, equipmentService.updateActiveById(userId, equipment, pkId));
	}

	/**
	 * 设备表导入
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "设备表导入", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "设备表导入", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/equipment-import")
	// @Secure()
	public AjaxReturn equipmentImport(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
		Long userId = getAuthentication();
		byte[] data = file.getBytes();
		InputStream inputStream = new ByteArrayInputStream(data);
		List<Equipment> equipmentList = ExcelUtils.getInstance().readExcel2Objects(inputStream, Equipment.class, 1, 5000, 0);
		inputStream.close();
		equipmentService.equipmentImport(userId, equipmentList);
		return new AjaxReturn(200, null, 1);
	}

	/**
	 * 设备表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "设备表导出", notes = "", code = 200, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "设备表导出", response = Equipment.class) })
	// @Secure()
	@GetMapping(path = { "/equipment-export" })
	public ResponseEntity<byte[]> downloadEquipmentExcel() throws Exception {
		Long userId = getAuthentication();
		List<Equipment> excelVOList = equipmentService.select(userId, new Equipment());
		byte[] data = ExcelUtils.getInstance().exportObjects2ExcelByteArray(excelVOList, Equipment.class, true, null, true);
		return getResponseEntity(data, "设备表.xlsx");
	}

	/**
	 * 设备信息同步
	 */
	@ApiOperation(value = "设备信息同步", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "设备信息同步", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/equipment-synchro")
	// @OperateLog(info = "部门添加[部门编码:%s->部门名称:%s]", params = {
	// "departmentCode","departmentName" })
	public AjaxReturn equipmentSynchro(@ApiParam(name = "equipmentCode", value = "部门编码", required = true) @RequestParam(required = true) String equipmentCode) throws BusinessException {
		// 获取当前用户
		Long userId = getAuthentication();
		return new AjaxReturn(200, null, equipmentService.equipmentSynchro(userId, equipmentCode));
	}
}
