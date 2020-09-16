package org.tsinghuatj.tool.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.odftoolkit.odfdom.converter.core.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.JsonUtils;
import org.tsinghuatj.tool.domain.ToolBladeCompose;
import org.tsinghuatj.tool.domain.ToolBladeComposeDetail;
import org.tsinghuatj.tool.domain.ToolBladeComposePart;
import org.tsinghuatj.tool.domain.ToolBladeProcess;
import org.tsinghuatj.tool.domain.ToolBladeWarehouse;
import org.tsinghuatj.tool.domain.ToolCoat;
import org.tsinghuatj.tool.domain.ToolHead;
import org.tsinghuatj.tool.domain.ToolWaitCheck;
import org.tsinghuatj.tool.domain.ToolWaitHandle;
import org.tsinghuatj.tool.repository.ToolBaseMapper;
import org.tsinghuatj.tool.repository.ToolBladeComposeDetailMapper;
import org.tsinghuatj.tool.repository.ToolBladeComposeMapper;
import org.tsinghuatj.tool.repository.ToolBladeComposePartMapper;
import org.tsinghuatj.tool.repository.ToolBladeProcessMapper;
import org.tsinghuatj.tool.repository.ToolBladeWarehouseMapper;
import org.tsinghuatj.tool.repository.ToolCoatMapper;
import org.tsinghuatj.tool.repository.ToolHeadMapper;
import org.tsinghuatj.tool.repository.ToolWaitCheckMapper;
import org.tsinghuatj.tool.repository.ToolWaitHandleMapper;
import org.tsinghuatj.tool.service.IToolOperLogService;
import org.tsinghuatj.tool.service.IToolWaitHandleService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolWaitHandle 表数据服务层接口实现类
 *
 */
@Service("toolWaitHandleService")
public class ToolWaitHandleServiceImpl extends BaseServiceImpl implements IToolWaitHandleService {

	private @Resource ToolWaitHandleMapper toolWaitHandleMapper;
	private @Resource ToolWaitCheckMapper toolWaitCheckMapper;
	private @Resource ToolBladeWarehouseMapper bladeWarehouseMapper;
	private @Resource ToolBladeComposeMapper bladeComposeMapper;
	private @Resource ToolBladeProcessMapper bladeProcessMapper;
	private @Resource ToolCoatMapper toolCoatMapper;
	private @Resource ToolHeadMapper toolHeadMapper;
	private @Resource ToolBaseMapper toolbaseMapper;
	private @Resource ToolBladeComposePartMapper composePartMapper;
	private @Resource ToolBladeComposeDetailMapper composeDetailMapper;
	private @Resource IToolOperLogService operLogService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolWaitHandle toolWaitHandle) throws BusinessException {
		toolWaitHandle.setPkId(getPkId());
		toolWaitHandle.setCreateTime(new Date());
		toolWaitHandle.setCreateUser(userId);
		toolWaitHandle.setUpdateTime(new Date());
		toolWaitHandle.setUpdateUser(userId);
		toolWaitHandle.setDelMark(0);
		return toolWaitHandleMapper.insert(toolWaitHandle);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolWaitHandle toolWaitHandle, Long toolWaitHandleId) throws BusinessException {
		toolWaitHandle.setUpdateTime(new Date());
		toolWaitHandle.setUpdateUser(userId);
		return toolWaitHandleMapper.updateActiveById(toolWaitHandle, toolWaitHandleId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolWaitHandle selectById(Long userId, Long toolWaitHandleId) throws BusinessException {
		return toolWaitHandleMapper.selectById(toolWaitHandleId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolWaitHandleId) throws BusinessException {
		return toolWaitHandleMapper.removeById(toolWaitHandleId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolWaitHandleId) throws BusinessException {
		ToolWaitHandle toolWaitHandle = new ToolWaitHandle();
		toolWaitHandle.setPkId(toolWaitHandleId);
		toolWaitHandle.setUpdateTime(new Date());
		toolWaitHandle.setUpdateUser(userId);
		return toolWaitHandleMapper.deleteById(toolWaitHandle);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolWaitHandle> select(Long userId, ToolWaitHandle toolWaitHandle) throws BusinessException {
		return toolWaitHandleMapper.select(toolWaitHandle);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolWaitHandle> selectPageList(Long userId, ToolWaitHandle toolWaitHandle, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolWaitHandle> page = toolWaitHandleMapper.selectPageList(toolWaitHandle, queryDto);

		List<String> headNumberList = new ArrayList<String>();
		List<String> composeNumberList = new ArrayList<String>();
		for (ToolWaitHandle item : page) {
			if (StringUtils.isNotEmpty(item.getComposeNumber())) {
				String composeNumber = item.getComposeNumber();
				composeNumberList.add(composeNumber);
				String headNumber = composeNumber.substring(0, composeNumber.length() - 6);
				headNumberList.add(headNumber);
				item.setHeadNumber(headNumber);
			} else {
				List<ToolBladeComposeDetail> detailList = new ArrayList<ToolBladeComposeDetail>();
				ToolBladeComposeDetail detail = new ToolBladeComposeDetail();
				detail.setToolNumber(item.getToolNumber());
				detail.setToolName(item.getToolName());
				detail.setToolMap(item.getToolMap());
				detail.setToolQty(item.getToolQty());
				detailList.add(detail);
				item.setDetailList(detailList);
			}
		}
		if (headNumberList.size() > 0) {
			headNumberList = headNumberList.stream().distinct().collect(Collectors.toList());
			List<ToolHead> headList = toolHeadMapper.selectByHeadNumberList(headNumberList);
			Map<String, ToolHead> headMap = headList.stream().collect(Collectors.toMap(ToolHead::getHeadNumber, t -> t, (k1, k2) -> k1));

			List<ToolBladeComposeDetail> detailList = composeDetailMapper.selectByComposeList(composeNumberList);

			Map<String, List<ToolBladeComposeDetail>> waitHandleMap = detailList.stream().collect(Collectors.groupingBy(ToolBladeComposeDetail::getComposeNumber));

			for (ToolWaitHandle item : page) {
				if (StringUtils.isNotEmpty(item.getComposeNumber())) {
					item.setHeadName(headMap.get(item.getHeadNumber()).getHeadName());
					item.setDetailList(waitHandleMap.get(item.getComposeNumber()));
				}
			}

		}
		return new Pagination<ToolWaitHandle>(page.getTotal(), page);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolWaitHandle> selectNewToolPageList(Long userId, ToolWaitHandle toolWaitHandle, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolWaitHandle> page = toolWaitHandleMapper.selectNewToolPageList(toolWaitHandle, queryDto);
		return new Pagination<ToolWaitHandle>(page.getTotal(), page.getResult());
	}

	@Override
	public Integer setCheck(Long userId, Long pkId) throws BusinessException {
		Date date = new Date();
		ToolWaitHandle toolWaitHandle = toolWaitHandleMapper.selectById(pkId);
		ToolWaitCheck waitcheck = new ToolWaitCheck();
		waitcheck.setPkId(getPkId());
		waitcheck.setCreateTime(date);
		waitcheck.setCreateUser(userId);
		waitcheck.setUpdateTime(date);
		waitcheck.setUpdateUser(userId);
		waitcheck.setDelMark(0);
		waitcheck.setCheckStatus(0);
		waitcheck.setSetCheckTime(date);
		waitcheck.setCheckType(2);
		waitcheck.setFullNumber(toolWaitHandle.getComposeNumber());
		waitcheck.setToolNumber(toolWaitHandle.getToolNumber());
		// waitcheck.setToolName(toolWaitHandle.getToolName());
		// waitcheck.setToolMap(toolWaitHandle.getToolMap());
		waitcheck.setTypeId(toolWaitHandle.getTypeId());
		waitcheck.setToolQty(toolWaitHandle.getToolQty());
		waitcheck.setIsNew(toolWaitHandle.getIsNew());
		waitcheck.setReceiptId(toolWaitHandle.getReceiptId());
		toolWaitCheckMapper.insert(waitcheck);

		toolWaitHandle.setPkId(pkId);
		toolWaitHandle.setUpdateTime(date);
		toolWaitHandle.setUpdateUser(userId);
		toolWaitHandle.setHandleResult(2);
		toolWaitHandleMapper.updateActiveById(toolWaitHandle, pkId);
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer setStock(Long userId, Long pkId) throws BusinessException {
		Date date = new Date();
		ToolWaitHandle toolWaitHandle = toolWaitHandleMapper.selectById(pkId);
		toolWaitHandle.setPkId(pkId);
		toolWaitHandle.setUpdateTime(date);
		toolWaitHandle.setUpdateUser(userId);
		toolWaitHandle.setHandleResult(2);
		toolWaitHandleMapper.updateActiveById(toolWaitHandle, pkId);

		// 刀条入库记录
		ToolBladeWarehouse bladeWarehouse = new ToolBladeWarehouse();
		bladeWarehouse.setComposeNumber(toolWaitHandle.getComposeNumber());
		bladeWarehouse.setToolNumber(toolWaitHandle.getToolNumber());
		bladeWarehouse.setToolQty(toolWaitHandle.getToolQty());
		bladeWarehouse.setWarehouseType(2);
		bladeWarehouse.setHandleResult(0);
		bladeWarehouse.setIsNew(toolWaitHandle.getIsNew());
		bladeWarehouse.setReceiptId(toolWaitHandle.getReceiptId());
		bladeWarehouse.setPkId(getPkId());
		bladeWarehouse.setCreateTime(date);
		bladeWarehouse.setCreateUser(userId);
		bladeWarehouse.setUpdateTime(date);
		bladeWarehouse.setUpdateUser(userId);
		bladeWarehouse.setDelMark(0);
		bladeWarehouseMapper.insert(bladeWarehouse);
		// 待涂层记录
		ToolWaitHandle waitHandle = new ToolWaitHandle();
		waitHandle.setReceiptId(toolWaitHandle.getReceiptId());
		waitHandle.setPkId(getPkId());
		waitHandle.setCreateTime(date);
		waitHandle.setCreateUser(userId);
		waitHandle.setUpdateTime(date);
		waitHandle.setUpdateUser(userId);
		waitHandle.setDelMark(0);
		waitHandle.setComposeNumber(toolWaitHandle.getComposeNumber());
		waitHandle.setToolNumber(toolWaitHandle.getToolNumber());
		waitHandle.setToolQty(toolWaitHandle.getToolQty());
		waitHandle.setHandleType(3);
		waitHandle.setHandleResult(0);
		waitHandle.setIsNew(toolWaitHandle.getIsNew());
		toolWaitHandleMapper.insert(waitHandle);
		ToolBladeCompose toolBladeCompose = new ToolBladeCompose();
		toolBladeCompose.setComposeNumber(toolWaitHandle.getComposeNumber());
		toolBladeCompose.setToolStatus(3);
		toolBladeCompose.setUpdateTime(date);
		toolBladeCompose.setUpdateUser(userId);
		bladeComposeMapper.updateActiveByComposeNumber(toolBladeCompose, toolBladeCompose.getComposeNumber());
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolWaitHandle> selectCoatPageList(Long userId, ToolWaitHandle toolWaitHandle, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolWaitHandle> page = toolWaitHandleMapper.selectCoatPageList(toolWaitHandle, queryDto);
		List<String> headNumberList = new ArrayList<String>();
		List<String> composeNumberList = new ArrayList<String>();
		for (ToolWaitHandle item : page) {
			if (StringUtils.isNotEmpty(item.getComposeNumber())) {
				String composeNumber = item.getComposeNumber();
				composeNumberList.add(composeNumber);
				String headNumber = composeNumber.substring(0, composeNumber.length() - 6);
				headNumberList.add(headNumber);
				item.setHeadNumber(headNumber);
			} else {
				List<ToolBladeComposeDetail> detailList = new ArrayList<ToolBladeComposeDetail>();
				ToolBladeComposeDetail detail = new ToolBladeComposeDetail();
				detailList.add(detail);
				detail.setToolNumber(item.getToolNumber());
				detail.setToolName(item.getToolName());
				detail.setToolMap(item.getToolMap());
				detail.setToolQty(item.getToolQty());
				item.setDetailList(detailList);
			}
		}
		headNumberList = headNumberList.stream().distinct().collect(Collectors.toList());
		List<ToolHead> headList = toolHeadMapper.selectByHeadNumberList(headNumberList);
		Map<String, ToolHead> headMap = headList.stream().collect(Collectors.toMap(ToolHead::getHeadNumber, t -> t, (k1, k2) -> k1));
		List<ToolBladeComposeDetail> detailList = composeDetailMapper.selectByComposeList(composeNumberList);
		Map<String, List<ToolBladeComposeDetail>> waitHandleMap = detailList.stream().collect(Collectors.groupingBy(ToolBladeComposeDetail::getComposeNumber));
		for (ToolWaitHandle item : page) {
			if (StringUtils.isNotEmpty(item.getComposeNumber())) {
				item.setHeadName(headMap.get(item.getHeadNumber()).getHeadName());
				item.setDetailList(waitHandleMap.get(item.getComposeNumber()));
			}
		}
		return new Pagination<ToolWaitHandle>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer setBladeCoat(Long userId, Long pkId, Long supplierId, String supplierName, String deliever, ToolBladeProcess process, List<ToolBladeComposePart> parts, String remark, String detailList) throws BusinessException {
		Date date = new Date();

		ToolWaitHandle toolWaitHandle = toolWaitHandleMapper.selectById(pkId);
		toolWaitHandle.setPkId(pkId);
		toolWaitHandle.setUpdateTime(date);
		toolWaitHandle.setUpdateUser(userId);
		toolWaitHandle.setHandleResult(2);
		toolWaitHandleMapper.updateActiveById(toolWaitHandle, pkId);
		ToolWaitCheck waitcheck = new ToolWaitCheck();
		waitcheck.setPkId(getPkId());
		waitcheck.setCreateTime(date);
		waitcheck.setCreateUser(userId);
		waitcheck.setUpdateTime(date);
		waitcheck.setUpdateUser(userId);
		waitcheck.setDelMark(0);
		waitcheck.setCheckStatus(0);
		waitcheck.setSetCheckTime(date);
		waitcheck.setSupplierId(supplierId);
		waitcheck.setSupplierName(supplierName);
		// 新刀条
		if (StringUtils.isEmpty(toolWaitHandle.getComposeNumber())) {
			waitcheck.setCheckType(3);
			waitcheck.setToolNumber(toolWaitHandle.getToolNumber());
			waitcheck.setTypeId(toolWaitHandle.getTypeId());
			waitcheck.setToolQty(toolWaitHandle.getToolQty());
			waitcheck.setIsNew(1);
			waitcheck.setReceiptId(toolWaitHandle.getReceiptId());
		} else {
			waitcheck.setFullNumber(toolWaitHandle.getComposeNumber());
			waitcheck.setCheckType(8);
			waitcheck.setTypeId(4);
			ToolBladeCompose bladeCompose = bladeComposeMapper.selectByComposeNumber(toolWaitHandle.getComposeNumber());
			bladeCompose.setToolStatus(5);
			ToolCoat toolCoat = toolCoatMapper.selectSeqByFullNumber(toolWaitHandle.getComposeNumber());
			List<ToolBladeComposePart> partlist = composePartMapper.selectByComposeNumber(toolWaitHandle.getComposeNumber());
			int processQty = 0;
			for (ToolBladeComposePart item : parts) {
				if (null == item.getPartQty() || item.getPartQty() < 1) {
					continue;
				}
				Long processId = getPkId();
				process.setComposeNumber(toolWaitHandle.getComposeNumber());
				process.setPartCode(item.getPartCode());
				process.setProcessQty(item.getPartQty());
				process.setEndTime(date);
				process.setPkId(processId);
				process.setCreateTime(date);
				process.setCreateUser(userId);
				process.setUpdateTime(date);
				process.setUpdateUser(userId);
				process.setDelMark(0);
				if (null != toolCoat) {
					process.setBeginTime(toolCoat.getCoatTime());
				} else {
					process.setBeginTime(bladeCompose.getCreateTime());
				}
				processQty = processQty + item.getPartQty();
				bladeProcessMapper.insert(process);
				boolean exist = false;
				for (ToolBladeComposePart comPart : partlist) {
					if (item.getPartCode().equals(comPart.getPartCode())) {
						comPart.setPartQty(item.getPartQty() + comPart.getPartQty());
						comPart.setUpdateTime(date);
						comPart.setUpdateUser(userId);
						composePartMapper.updateActiveById(comPart, comPart.getPkId());
						exist = true;
						break;
					}
				}
				// 不存在则增加一条记录
				if (!exist) {
					item.setPkId(getPkId());
					item.setCreateTime(date);
					item.setCreateUser(userId);
					item.setUpdateTime(date);
					item.setUpdateUser(userId);
					item.setDelMark(0);
					composePartMapper.insert(item);
				}
			}

			bladeCompose.setToolStatus(5);
			if (null != bladeCompose.getProcessTimes()) {
				bladeCompose.setProductionQty(bladeCompose.getProductionQty() + processQty);
				bladeCompose.setProcessTimes(bladeCompose.getProcessTimes() + 1);
			} else {
				bladeCompose.setProductionQty(processQty);
				bladeCompose.setProcessTimes(1);
			}

			List<ToolBladeComposeDetail> details = JsonUtils.json2list(detailList, ToolBladeComposeDetail.class);
			for (ToolBladeComposeDetail item : details) {
				item.setComposeNumber(toolWaitHandle.getComposeNumber());
				composeDetailMapper.updateActiveByNumber(item);
			}

			// 刀条生产计数暂不考虑-2020-01-10
			/*
			 * ToolBase toolbase =
			 * toolbaseMapper.selectByToolNumber(toolWaitHandle.getToolNumber(),
			 * null); if (null != toolCoat) {
			 * toolCoat.setProcessQty(processQty);
			 * toolCoatMapper.updateActiveById(toolCoat, toolCoat.getPkId()); }
			 * if (null != toolbase.getProcessEach()) {
			 * toolCoat.setTheoreticalQty(toolbase.getProcessEach());
			 * toolCoat.setCompletionDegree(new
			 * BigDecimal(processQty).divide(new
			 * BigDecimal(toolbase.getProcessEach()), 2, RoundingMode.HALF_UP));
			 * }
			 */
			bladeComposeMapper.updateActiveByComposeNumber(bladeCompose, toolWaitHandle.getComposeNumber());
			operLogService.insert(userId, 7, "", toolWaitHandle.getComposeNumber(), "涂层供应商:" + supplierName + " 制件数量:" + processQty, remark);
		}
		toolWaitCheckMapper.insert(waitcheck);

		return 1;
	}

}