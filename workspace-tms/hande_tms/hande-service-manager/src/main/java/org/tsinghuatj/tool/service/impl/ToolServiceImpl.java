package org.tsinghuatj.tool.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.tsinghuatj.base.domain.Staff;
import org.tsinghuatj.base.domain.Supplier;
import org.tsinghuatj.base.repository.StaffMapper;
import org.tsinghuatj.base.repository.SupplierMapper;
import org.tsinghuatj.erp.domain.ErpMaterial;
import org.tsinghuatj.erp.repository.ErpMaterialMapper;
import org.tsinghuatj.framework.constants.ToolStatusTypeEnum;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.SysParam;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.SysParamMapper;
import org.tsinghuatj.sys.repository.UserAccountMapper;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolBase;
import org.tsinghuatj.tool.domain.ToolBlade;
import org.tsinghuatj.tool.domain.ToolCoat;
import org.tsinghuatj.tool.domain.ToolPart;
import org.tsinghuatj.tool.domain.ToolProcess;
import org.tsinghuatj.tool.domain.ToolRepair;
import org.tsinghuatj.tool.domain.ToolScripApply;
import org.tsinghuatj.tool.domain.ToolType;
import org.tsinghuatj.tool.domain.ToolUnqualifiedReport;
import org.tsinghuatj.tool.domain.ToolWaitCheck;
import org.tsinghuatj.tool.domain.ToolWaitHandle;
import org.tsinghuatj.tool.domain.ToolWarehouse;
import org.tsinghuatj.tool.repository.ToolBaseMapper;
import org.tsinghuatj.tool.repository.ToolBladeMapper;
import org.tsinghuatj.tool.repository.ToolCoatMapper;
import org.tsinghuatj.tool.repository.ToolMapper;
import org.tsinghuatj.tool.repository.ToolOutboundMapper;
import org.tsinghuatj.tool.repository.ToolPartMapper;
import org.tsinghuatj.tool.repository.ToolProcessMapper;
import org.tsinghuatj.tool.repository.ToolRepairMapper;
import org.tsinghuatj.tool.repository.ToolTypeMapper;
import org.tsinghuatj.tool.repository.ToolUnqualifiedReportMapper;
import org.tsinghuatj.tool.repository.ToolWaitCheckMapper;
import org.tsinghuatj.tool.repository.ToolWaitHandleMapper;
import org.tsinghuatj.tool.repository.ToolWarehouseMapper;
import org.tsinghuatj.tool.service.IToolOperLogService;
import org.tsinghuatj.tool.service.IToolScripApplyService;
import org.tsinghuatj.tool.service.IToolService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * Tool 表数据服务层接口实现类
 *
 */
@Service("toolService")
public class ToolServiceImpl extends BaseServiceImpl implements IToolService {

	private @Resource ToolMapper toolMapper;
	private @Resource ToolBaseMapper toolBaseMapper;
	private @Resource ToolOutboundMapper toolOutboundMapper;
	private @Resource ToolTypeMapper toolTypeMapper;
	private @Resource ToolWarehouseMapper toolWarehouseMapper;
	private @Resource ToolPartMapper toolPartMapper;
	private @Resource ToolCoatMapper toolCoatMapper;
	private @Resource ToolRepairMapper toolRepairMapper;
	private @Resource ToolProcessMapper toolProcessMapper;
	private @Resource ErpMaterialMapper materialMapper;
	private @Resource UserAccountMapper userAccountMapper;
	private @Resource ToolWaitCheckMapper toolWaitCheckMapper;
	private @Resource ToolWaitHandleMapper toolWaitHandleMapper;
	private @Resource StaffMapper staffMapper;
	private @Resource SupplierMapper supplierMapper;
	private @Resource ToolBladeMapper toolBladeMapper;
	private @Resource SysParamMapper paramMapper;
	private @Resource IToolOperLogService operLogService;
	private @Resource IToolScripApplyService toolScripApplyService;
	private @Resource ToolUnqualifiedReportMapper toolUnqualifiedReportMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, Tool tool, ToolWarehouse toolWarehouse, Long handleId) throws BusinessException {
		Long pkId = getPkId();
		tool.setPkId(pkId);
		tool.setCreateTime(new Date());
		tool.setCreateUser(userId);
		tool.setUpdateTime(new Date());
		tool.setUpdateUser(userId);
		tool.setDelMark(0);
		tool.setToolState(1);
		tool.setCompromiseFlag(0);
		tool.setToolAmount(1);
		String toolMaterialNumber = tool.getFullNumber();
		String[] toolArray = toolMaterialNumber.split("/");
		String toolSeqNumber = toolArray[0];
		String[] warehouseNumber = toolSeqNumber.split("-");
		String warehouseSeq = warehouseNumber[1];
		Integer seq;
		if (warehouseSeq != null && warehouseSeq != "") {
			seq = Integer.parseInt(warehouseSeq);
		} else {
			seq = 1;
		}
		tool.setToolSeq(seq);
		ToolBase toolBase = toolBaseMapper.selectByToolNumber(tool.getToolNumber(), null);
		if (toolBase != null) {
			//tool.setProcessTotal(toolBase.getProcessTotal());
			//tool.setProcessEach(toolBase.getProcessEach());
			tool.setAvailableTimes(toolBase.getAvailableTimes());
			tool.setPrice(toolBase.getPrice());
		}
		toolWarehouse.setPkId(getPkId());
		toolWarehouse.setCreateTime(new Date());
		toolWarehouse.setCreateUser(userId);
		toolWarehouse.setUpdateTime(new Date());
		toolWarehouse.setUpdateUser(userId);
		toolWarehouse.setDelMark(0);
		toolWarehouse.setInType(1);
		toolWarehouse.setWarehosingTime(new Date());
		toolWarehouseMapper.insert(toolWarehouse);
		ToolWaitHandle toolWaitHandle = new ToolWaitHandle();
		toolWaitHandle.setHandleResult(1);
		toolWaitHandle.setPkId(handleId);
		toolWaitHandleMapper.updateActiveById(toolWaitHandle, handleId);
		operLogService.insert(userId, 1, tool.getToolNumber(), tool.getFullNumber(), "", "");
		return toolMapper.insert(tool);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, Tool tool, Long toolId, ToolWarehouse toolWarehouse) throws BusinessException {
		Date date = new Date();
		tool.setUpdateTime(date);
		tool.setUpdateUser(userId);
		if (toolWarehouse != null) {
			tool.setToolState(1);
			toolWarehouse.setPkId(getPkId());
			toolWarehouse.setCreateTime(date);
			toolWarehouse.setCreateUser(userId);
			toolWarehouse.setUpdateTime(date);
			toolWarehouse.setUpdateUser(userId);
			toolWarehouse.setDelMark(0);
			toolWarehouse.setWarehosingTime(new Date());
			toolWarehouseMapper.insert(toolWarehouse);
		}
		// 刃磨送检
		if (tool.getToolState() == 7 && tool.getCheckType() == 2) {
			ToolWaitCheck toolWaitCheck = new ToolWaitCheck();
			Long waitCheckId = getPkId();
			toolWaitCheck.setPkId(waitCheckId);
			Tool oldtool = toolMapper.selectById(toolId);
			toolWaitCheck.setFullNumber(oldtool.getFullNumber());
			toolWaitCheck.setToolNumber(oldtool.getToolNumber());
			// toolWaitCheck.setToolName(oldtool.getToolName());
			// toolWaitCheck.setToolMap(oldtool.getToolMap());
			toolWaitCheck.setTypeId(oldtool.getTypeId());
			toolWaitCheck.setCheckType(2);
			toolWaitCheck.setCheckStatus(0);
			toolWaitCheck.setSetCheckTime(date);
			toolWaitCheck.setCreateUser(userId);
			toolWaitCheck.setCreateTime(date);
			toolWaitCheck.setCheckStatus(0);
			toolWaitCheck.setUpdateTime(date);
			toolWaitCheck.setDelMark(0);
			toolWaitCheck.setUpdateUser(userId);
			toolWaitCheck.setToolQty(1);
			toolWaitCheckMapper.insert(toolWaitCheck);

		}
		tool.setDepartmentId(0l);

		return toolMapper.updateActiveById(tool, toolId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Tool selectById(Long userId, Long toolId) throws BusinessException {
		return toolMapper.selectById(toolId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolId) throws BusinessException {
		return toolMapper.removeById(toolId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolId) throws BusinessException {
		Tool tool = new Tool();
		tool.setUpdateTime(new Date());
		tool.setUpdateUser(userId);
		toolMapper.updateActiveById(tool, toolId);
		return toolMapper.deleteById(tool);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<Tool> select(Long userId, Tool tool) throws BusinessException {
		List<Tool> list = toolMapper.select(tool);
		List<Long> idList = new ArrayList<Long>();
		for (Tool item : list) {
			idList.add(item.getUpdateUser());
		}

		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (Tool item : list) {
				UserAccount account = accoutMap.get(item.getUpdateUser());
				if(null!=account){
					item.setUpdateUserName(account.getRealName());
				}				
			}
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Tool> selectPageList(Long userId, Tool tool, QueryDto queryDto) throws BusinessException {
		tool.setDelMark(0);
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Tool> page = toolMapper.selectPageList(tool, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (Tool item : page.getResult()) {
			idList.add(item.getUpdateUser());
		}

		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (Tool item : page.getResult()) {
				UserAccount account = accoutMap.get(item.getUpdateUser());
				if(null!=account){
					item.setUpdateUserName(account.getRealName());
				}
			}
		}
		return new Pagination<Tool>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Tool> selectLifePageList(Long userId, Tool tool, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Tool> page = toolMapper.selectPageList(tool, queryDto);
		// 循环遍历
		List<Long> toolIdList = new ArrayList<Long>();
		page.forEach(item -> {
			toolIdList.add(item.getPkId());
		});

		if (!CollectionUtils.isEmpty(toolIdList)) {
			List<ToolCoat> coatlist = toolCoatMapper.selectBytoolIdList(toolIdList);
			List<ToolRepair> repairList = toolRepairMapper.selectBytoolIdList(toolIdList);
			List<ToolProcess> processList = toolProcessMapper.selectBytoolIdList(toolIdList);
			Map<Long, List<ToolCoat>> coatMap = coatlist.stream().collect(Collectors.groupingBy(ToolCoat::getToolId));
			Map<Long, List<ToolRepair>> repairMap = repairList.stream().collect(Collectors.groupingBy(ToolRepair::getToolId));
			Map<Long, List<ToolProcess>> processMap = processList.stream().collect(Collectors.groupingBy(ToolProcess::getToolId));
			page.forEach(item -> {
				item.setCoatList(coatMap.get(item.getPkId()));
				item.setRepairList(repairMap.get(item.getPkId()));
				item.setProcessList(processMap.get(item.getPkId()));
			});
		}

		return new Pagination<Tool>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Tool selectByFullNumber(Long userId, String fullNumber) throws BusinessException {
		return toolMapper.selectByFullNumber(fullNumber);
	}

	@Override
	public Pagination<Tool> selectBydelMark(Long userId, Tool tool, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Tool> page = toolMapper.selectBydelMark(tool, queryDto);
		return new Pagination<Tool>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Tool toolGetSeqByToolNumber(Long userId, String toolNumber) throws BusinessException {
		return toolMapper.toolGetSeqByToolNumber(toolNumber);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public void exportToolLife(HttpServletResponse response, HttpServletRequest request, Long userId, Tool tool, QueryDto queryDto) throws Exception {

	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolBase toolGetByToolNumber(Long userId, String toolNumber) throws BusinessException {
		ToolBase toolBase = toolBaseMapper.selectByToolNumber(toolNumber, null);
		ToolPart toolPart = new ToolPart();
		toolPart.setToolNumber(toolNumber);
		List<ToolPart> toolPartList = toolPartMapper.select(toolPart);
		if (toolPartList.size() > 0 && toolBase != null) {
			toolBase.setToolPartList(toolPartList);
		}

		return toolBase;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public ToolBase purchaseGetByToolNumber(Long userId, String toolNumber) throws BusinessException {
		ToolBase toolBase = toolBaseMapper.selectByToolNumber(toolNumber, null);
		ToolPart toolPart = new ToolPart();
		toolPart.setToolNumber(toolNumber);
		List<ToolPart> toolPartList = toolPartMapper.select(toolPart);
		if (toolPartList.size() > 0 && toolBase != null) {
			toolBase.setToolPartList(toolPartList);
		}
		if (null == toolBase) {
			ErpMaterial material = materialMapper.selectByMaterialNumber(toolNumber);
			if (null == material) {
				throw new BusinessException("material.not.exists.error");
			}
			SysParam param = paramMapper.selectByParamKey("prefixTool");
			String[] codeArray = param.getParamValue().split(",");
			boolean flag = false;
			for (String code : codeArray) {
				if (material.getItemCode().startsWith(code)) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				throw new BusinessException("material.not.exists.error");
			}
			toolBase = new ToolBase();
			toolBase.setPkId(getPkId());
			toolBase.setCreateTime(new Date());
			toolBase.setCreateUser(userId);
			toolBase.setUpdateTime(new Date());
			toolBase.setUpdateUser(userId);
			toolBase.setDelMark(0);
			toolBase.setToolNumber(material.getItemCode());
			toolBase.setToolName(material.getItemName());
			toolBase.setToolAmount(0);
			toolBase.setPrice(material.getItemPrice());
			toolBaseMapper.insert(toolBase);

			toolBase.setErpAmount(material.getSumOnhandQuantity());
			toolBase.setNoCheckQty(material.getSumNocheckQuantity());

		} else {
			ErpMaterial material = materialMapper.selectByMaterialNumber(toolNumber);
			if (null != material) {
				toolBase.setErpAmount(material.getSumOnhandQuantity());
				toolBase.setNoCheckQty(material.getSumNocheckQuantity());
			}
			// 刀条
			if (null == toolBase.getTypeId()) {
				throw new BusinessException("material.type.null.error");
			}
			if (toolBase.getTypeId() == 4) {
				Staff staff = staffMapper.selectByUserId(userId);
				ToolBlade toolBlade = toolBladeMapper.selectByNumber(toolNumber, staff.getDepartmentId());
				if (null != toolBlade && null != toolBlade.getInventoryQty()) {
					toolBase.setToolAmount(toolBlade.getInventoryQty());
				} else {
					toolBase.setToolAmount(0);
				}
			} else {
				toolBase.setToolAmount(toolMapper.countByToolNumber(toolNumber));
			}
		}
		return toolBase;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public ToolBase purchaseGetByToolName(Long userId, String toolName) throws BusinessException {
		ToolBase toolBase = toolBaseMapper.selectByToolName(toolName, null);
		if (null != toolBase) {
			String toolNumber = toolBase.getToolNumber();
			toolBase.setToolAmount(toolMapper.countByToolNumber(toolNumber));
			ErpMaterial material = materialMapper.selectByMaterialNumber(toolNumber);
			if (null != material) {
				toolBase.setErpAmount(material.getSumOnhandQuantity());
				toolBase.setNoCheckQty(material.getSumNocheckQuantity());
			}
		} else {
			throw new BusinessException("material.not.exists.error");
		}
		return toolBase;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Tool> selectWaitCheckPageList(Tool tool, QueryDto queryDto) {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Tool> page;
		// 新刀刃磨涂层质检
		if (null == tool.getCheckType()) {
			if (StringUtils.isNotEmpty(tool.getFullNumber())) {
				page = toolMapper.selectWaitCheckPageList(tool, queryDto);
			} else {
				page = toolMapper.selectUnionWaitCheckPageList(tool, queryDto);
			}
		} else if (1 == tool.getCheckType()) {
			// 新刀质检
			if (StringUtils.isNotEmpty(tool.getFullNumber())) {
				return new Pagination<Tool>();
			}
			page = toolMapper.selectNewWaitCheckPageList(tool, queryDto);
		} else {
			// 刃磨涂层
			page = toolMapper.selectWaitCheckPageList(tool, queryDto);
		}
		return new Pagination<Tool>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Tool> selectWaitRepairPageList(Long userId, Tool tool, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Tool> page = toolMapper.selectWaitRepairPageList(tool, queryDto);
		/*
		 * for (Tool item : page.getResult()) { ToolOutbound outbound =
		 * toolOutboundMapper.selectByToolIdAndOutType(item.getPkId(), 2);
		 * if(outbound != null){
		 * item.setOutboundTime(outbound.getReceiveTime()); }else{
		 * item.setOutboundTime(null); } }
		 */

		return new Pagination<Tool>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer toolImport(Long userId, List<Tool> toolList) throws BusinessException {
		Date date = new Date();
		List<ToolType> typeList = toolTypeMapper.select(new ToolType());
		List<Supplier> supplierList = supplierMapper.select(new Supplier());
		toolList.forEach(tool -> {
			tool.setUpdateTime(date);
			tool.setUpdateUser(userId);
			tool.setToolSeq(Integer.parseInt(tool.getFullNumber()));
			String warehouseNumber = tool.getToolNumber() + '-' + tool.getFullNumber();
			String fullNumber = warehouseNumber + '/' + tool.getToolMap();
			tool.setWarehouseCode(warehouseNumber);
			tool.setFullNumber(fullNumber);
			ToolBase toolBase = toolBaseMapper.selectByToolNumber(tool.getToolNumber(), null);
			tool.setProcessEach(toolBase.getProcessEach());
			tool.setAvailableTimes(toolBase.getAvailableTimes());
			tool.setProcessTotal(toolBase.getProcessTotal());
			tool.setGrindingMax(toolBase.getGrindingMax());
			try {
				operLogService.insert(userId, 1, tool.getToolNumber(), tool.getFullNumber(), "期初导入", "");
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (toolBase.getGrindingCordon() != null) {
				tool.setRepairCordon(toolBase.getGrindingCordon());
			}
			if (toolBase.getPrice() != null) {
				tool.setPrice(toolBase.getPrice());
			}
			Staff staff = staffMapper.selectByUserId(userId);
			tool.setKeeper(staff.getStaffName());

			for (ToolType toolType : typeList) {
				if (toolType.getTypeName().equals(tool.getTypeName())) {
					tool.setTypeId(toolType.getPkId().intValue());
				}
			}
			tool.setToolState(ToolStatusTypeEnum.getCode(tool.getToolStateName()));
			for (Supplier supplier : supplierList) {
				if (supplier.getSupplierName().equals(tool.getSupplierName())) {
					tool.setSupplierId(supplier.getPkId());
				}
			}
			Tool old = toolMapper.selectByFullNumber(fullNumber);
			if (null != old) {
				toolMapper.updateActiveById(tool, old.getPkId());
			} else {
				try {
					tool.setPkId(getPkId());
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				tool.setCreateTime(date);
				tool.setCreateUser(userId);
				tool.setDelMark(0);
				tool.setProcessAmount(0);
				tool.setToolAmount(1);
				tool.setCoatTimes(0);
				tool.setProcessTimes(0);
				tool.setRepairTimes(0);
				toolMapper.insert(tool);
			}
		});

		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Tool> selectStatisticsPageList(Long userId, Tool tool, QueryDto queryDto) throws BusinessException {
		tool.setDelMark(0);
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Tool> page = toolMapper.selectStatisticsPageList(tool, queryDto);
		for (Tool item : page.getResult()) {
			// 制件名称
			ToolPart part = toolPartMapper.selectByToolIdLimit(item.getToolNumber());
			if (part != null) {
				item.setPartCode(part.getPartCode());
				item.setPartName(part.getPartName());
			}
			// 加工总数

			Integer processAmount = toolProcessMapper.sumProcessQtyByToolId(item.getPkId());
			item.setProcessAmount(processAmount);
			// 涂层总价
			BigDecimal toolCoatAmount = toolCoatMapper.sumCoatPriceByToolId(item.getPkId());			
			item.setCoatPriceAmount(toolCoatAmount);
			// 单件消耗
			BigDecimal coatStatistics = new BigDecimal("0");
			if (item.getProcessAmount() != null && item.getProcessAmount() > 0 && null != toolCoatAmount) {
				BigDecimal processAmountQty = new BigDecimal(String.valueOf(processAmount));
				coatStatistics = toolCoatAmount.divide(processAmountQty, 2, RoundingMode.HALF_UP);
			}
			item.setCoatStatistics(coatStatistics);
		}
		return new Pagination<Tool>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Tool> selectToolStatisticsPageList(Long userId, Tool tool, QueryDto queryDto) throws BusinessException {
		tool.setDelMark(0);
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Tool> page = toolMapper.selectStatisticsPageList(tool, queryDto);
		for (Tool item : page.getResult()) {
			// 制件名称
			ToolPart part = toolPartMapper.selectByToolIdLimit(item.getToolNumber());
			if (part != null) {
				item.setPartCode(part.getPartCode());
				item.setPartName(part.getPartName());
			}
			// 加工总数
			Integer processAmount = toolProcessMapper.sumProcessQtyByToolId(item.getPkId());
			item.setProcessAmount(processAmount);
			// 涂层总价
			BigDecimal toolCoatAmount = toolCoatMapper.sumCoatPriceByToolId(item.getPkId());
			item.setCoatTimes(toolCoatMapper.countCoatQtyByToolId(item.getPkId()));
			item.setCoatPriceAmount(toolCoatAmount);

			// 刃磨总价
			BigDecimal toolRepairAmount = toolRepairMapper.sumRepairPriceByToolId(item.getPkId());
			item.setRepairTimes(toolRepairMapper.countRepairQtyByToolId(item.getPkId()));
			item.setRepairPriceAmount(toolRepairAmount);
			// 单件消耗
			BigDecimal toolStatistics = new BigDecimal("0");
			BigDecimal priceTotal = new BigDecimal("0");
			if (null == toolCoatAmount) {
				toolCoatAmount = BigDecimal.ZERO;
			}
			if (null == toolRepairAmount) {
				toolRepairAmount = BigDecimal.ZERO;
			}
			if (item.getPrice() == null) {
				priceTotal = toolCoatAmount.add(toolRepairAmount);
			} else {
				priceTotal = item.getPrice().add(toolCoatAmount).add(toolRepairAmount);
			}

			if (item.getProcessAmount() != null && item.getProcessAmount() > 0) {
				BigDecimal processAmountQty = new BigDecimal(String.valueOf(processAmount));
				toolStatistics = priceTotal.divide(processAmountQty, 2, RoundingMode.HALF_UP);
			}
			item.setCoatStatistics(toolStatistics);
		}
		return new Pagination<Tool>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer toolScripById(Long userId, String realName, Long toolId) throws BusinessException {
		Tool tool = toolMapper.selectById(toolId);
		tool.setScripHandler(realName);
		tool.setScripHandlerTime(new Date());
		tool.setToolState(10);
		tool.setUpdateTime(new Date());
		tool.setUpdateUser(userId);
		// 是通过报废申请
		ToolScripApply scripApply = toolScripApplyService.applyGetByfullNumber(userId, tool.getFullNumber());
		if (null != scripApply) {
			scripApply.setApplyStatus(3);
			toolScripApplyService.updateActiveById(userId, scripApply, scripApply.getPkId());
		} else {
			// 是刃磨不合格
			ToolUnqualifiedReport toolUnqualifiedReport = toolUnqualifiedReportMapper.selectFullNumberReportType(tool.getFullNumber(), 2);
			if (null != toolUnqualifiedReport) {
				toolUnqualifiedReport.setRepairAuditStatus(2);
				toolUnqualifiedReport.setUpdateTime(new Date());
				toolUnqualifiedReport.setUpdateUser(userId);
				toolUnqualifiedReportMapper.updateActiveById(toolUnqualifiedReport, toolUnqualifiedReport.getPkId());
			}
		}
		toolMapper.updateActiveById(tool, toolId);
		return 1;
	}
}