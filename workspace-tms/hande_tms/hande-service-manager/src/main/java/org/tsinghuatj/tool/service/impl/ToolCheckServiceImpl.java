package org.tsinghuatj.tool.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.base.domain.Supplier;
import org.tsinghuatj.base.repository.CheckStandardMapper;
import org.tsinghuatj.base.repository.SupplierMapper;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.service.ISerialNumberService;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolAppendix;
import org.tsinghuatj.tool.domain.ToolBase;
import org.tsinghuatj.tool.domain.ToolBladeCompose;
import org.tsinghuatj.tool.domain.ToolBladeComposeDetail;
import org.tsinghuatj.tool.domain.ToolCheck;
import org.tsinghuatj.tool.domain.ToolCheckResult;
import org.tsinghuatj.tool.domain.ToolCoat;
import org.tsinghuatj.tool.domain.ToolCoatPrice;
import org.tsinghuatj.tool.domain.ToolHead;
import org.tsinghuatj.tool.domain.ToolPurchaseReceipt;
import org.tsinghuatj.tool.domain.ToolUnqualifiedReport;
import org.tsinghuatj.tool.domain.ToolWaitCheck;
import org.tsinghuatj.tool.domain.ToolWaitHandle;
import org.tsinghuatj.tool.repository.ToolAppendixMapper;
import org.tsinghuatj.tool.repository.ToolBaseMapper;
import org.tsinghuatj.tool.repository.ToolBladeComposeDetailMapper;
import org.tsinghuatj.tool.repository.ToolBladeComposeMapper;
import org.tsinghuatj.tool.repository.ToolCheckMapper;
import org.tsinghuatj.tool.repository.ToolCheckResultMapper;
import org.tsinghuatj.tool.repository.ToolCoatMapper;
import org.tsinghuatj.tool.repository.ToolCoatPriceMapper;
import org.tsinghuatj.tool.repository.ToolHeadMapper;
import org.tsinghuatj.tool.repository.ToolMapper;
import org.tsinghuatj.tool.repository.ToolOutboundMapper;
import org.tsinghuatj.tool.repository.ToolPurchaseReceiptMapper;
import org.tsinghuatj.tool.repository.ToolUnqualifiedReportMapper;
import org.tsinghuatj.tool.repository.ToolWaitCheckMapper;
import org.tsinghuatj.tool.repository.ToolWaitHandleMapper;
import org.tsinghuatj.tool.service.IToolCheckService;
import org.tsinghuatj.tool.service.IToolOperLogService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;

/**
 *
 * ToolCheck 表数据服务层接口实现类
 *
 */
@Service("toolCheckService")
public class ToolCheckServiceImpl extends BaseServiceImpl implements IToolCheckService {

	private @Resource ToolCheckMapper toolCheckMapper;
	private @Resource ToolHeadMapper toolHeadMapper;
	private @Resource ToolBladeComposeDetailMapper composeDetailMapper;
	private @Resource ToolCheckResultMapper toolCheckResultMapper;
	private @Resource ToolAppendixMapper appendixMapper;
	private @Resource CheckStandardMapper standardMapper;
	private @Resource ISerialNumberService serialNumberService;
	private @Resource ToolUnqualifiedReportMapper toolUnqualifiedReportMapper;
	private @Resource ToolBaseMapper toolBaseMapper;
	private @Resource ToolMapper toolMapper;
	private @Resource ToolCoatMapper toolCoatMapper;
	private @Resource SupplierMapper supplierMapper;
	private @Resource ToolOutboundMapper toolOutboundMapper;
	private @Resource ToolWaitCheckMapper toolWaitCheckMapper;
	private @Resource ToolWaitHandleMapper toolWaitHandleMapper;
	private @Resource ToolBladeComposeMapper bladeComposeMapper;
	private @Resource ToolPurchaseReceiptMapper purchaseReceiptMapper;
	private @Resource ToolCoatPriceMapper toolCoatPriceMapper;
	private @Resource IToolOperLogService operLogService;
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolCheck toolCheck, String appendixIds) throws BusinessException {
		Date date = new Date();
		Long checkId = getPkId();
		toolCheck.setPkId(checkId);
		toolCheck.setCreateTime(date);
		toolCheck.setCreateUser(userId);
		toolCheck.setUpdateTime(date);
		toolCheck.setUpdateUser(userId);
		toolCheck.setDelMark(0);

		ToolWaitCheck checkwait;
		if (null == toolCheck.getWaitCheckId()) {
			checkwait = toolWaitCheckMapper.selectByFullNumber(toolCheck.getFullNumber(), toolCheck.getCheckType());
		} else {
			checkwait = toolWaitCheckMapper.selectById(toolCheck.getWaitCheckId());
		}
		if (toolCheck.getCheckType() == 1) {
			// 新刀条质检增加待刃磨记录
			if (toolCheck.getTypeId() == 4) {
				insertWaitRepair(userId, checkwait);
				operLogService.insert(userId, 0, toolCheck.getToolNumber(), toolCheck.getFullNumber(), toolCheck.getCheckResult() == 1 ? "合格" : "不合格" + " 刀条数量(" + toolCheck.getToolQty() + ")", toolCheck.getRemark());
			} else {
				// 增加一条待入库记录
				if (toolCheck.getCheckResult() == 1) {
					insertWaitWhouse(userId, checkwait);
					String fullNumber = checkwait.getToolNumber() + "-" + checkwait.getToolSeq() + "/" + checkwait.getToolMap();
					operLogService.insert(userId, 0, toolCheck.getToolNumber(), fullNumber, toolCheck.getCheckResult() == 1 ? "合格" : "不合格" + "顺序号(" + toolCheck.getToolSeq() + ")", toolCheck.getRemark());
				}
			}
		} else if (toolCheck.getCheckType() == 2) {
			// 刃磨质检
			if (toolCheck.getTypeId() == 4) {
				insertWaitCoat(userId, checkwait);
				operLogService.insert(userId, 6, toolCheck.getToolNumber(), toolCheck.getFullNumber(), toolCheck.getCheckResult() == 1 ? "合格" : "不合格" + "刀条数量(" + toolCheck.getToolQty() + ")", toolCheck.getRemark());
			} else {
				operLogService.insert(userId, 6, toolCheck.getToolNumber(), toolCheck.getFullNumber(), toolCheck.getCheckResult() == 1 ? "合格" : "不合格", toolCheck.getRemark());
			}
		} else if (toolCheck.getCheckType() == 3) {
			// 涂层质检 增加涂层记录
			insertCoat(userId, toolCheck, checkwait);
			if (toolCheck.getTypeId() == 4) {
				// 增加一条待入库记录
				insertWaitWhouse(userId, checkwait);
				operLogService.insert(userId, 8, toolCheck.getToolNumber(), toolCheck.getFullNumber(), toolCheck.getCheckResult() == 1 ? "合格" : "不合格" + " 刀条数量(" + toolCheck.getToolQty() + ")", toolCheck.getRemark());
			} else {
				operLogService.insert(userId, 8, toolCheck.getToolNumber(), toolCheck.getFullNumber(), toolCheck.getCheckResult() == 1 ? "合格" : "不合格", toolCheck.getRemark());
			}
		} else if (toolCheck.getCheckType() == 7) {
			// 修改刀盘组合状态
			insertWaitCoat(userId, checkwait);
			operLogService.insert(userId, 6, "", toolCheck.getFullNumber(), toolCheck.getCheckResult() == 1 ? "合格" : "不合格", toolCheck.getRemark());
		} else if (toolCheck.getCheckType() == 8) {
			// 涂层质检 增加涂层记录
			toolCheck.setTypeId(4);
			insertCoat(userId, toolCheck, checkwait);
			// 修改刀盘组合状态
			ToolBladeCompose compose = new ToolBladeCompose();
			compose.setComposeNumber(checkwait.getFullNumber());
			compose.setToolStatus(6);// 待安装
			compose.setUpdateTime(date);
			compose.setUpdateUser(userId);
			bladeComposeMapper.updateActiveByComposeNumber(compose, checkwait.getFullNumber());
			operLogService.insert(userId, 8, toolCheck.getToolNumber(), toolCheck.getFullNumber(), toolCheck.getCheckResult() == 1 ? "合格" : "不合格", toolCheck.getRemark());
		}
		// 修改待检单状态
		if (null != checkwait) {
			checkwait.setCheckStatus(2);
			toolWaitCheckMapper.updateActiveById(checkwait, checkwait.getPkId());
			toolCheck.setWaitCheckId(checkwait.getPkId());
		}

		// 保存质检记录
		toolCheckMapper.insert(toolCheck);

		// 保存质检明细
		List<ToolCheckResult> resultList = toolCheck.getResultList();
		resultList.forEach(result -> {
			long itemId = result.getPkId();
			result.setItemId(itemId);
			try {
				result.setPkId(getPkId());
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			result.setCheckId(checkId);
			result.setCreateUser(checkId);
			result.setCreateTime(date);
			result.setDelMark(0);
			result.setUpdateTime(date);
			result.setUpdateUser(userId);
			toolCheckResultMapper.insert(result);
		});

		// 如果是刃磨,涂层修改刀具状态(刀条除外)
		if ((toolCheck.getCheckType() == 2 || toolCheck.getCheckType() == 3) && toolCheck.getTypeId() != 4) {
			updateToolCheckResult(userId, toolCheck.getFullNumber(), toolCheck.getCheckResult(), date);
		}

		// 如果质检不合格 增加不合格处理请求
		if (toolCheck.getCheckResult() == 2 && toolCheck.getTypeId() != 4) {
			insertUnqualifiedReport(userId, toolCheck, checkId, date);
		}

		// 更新附件
		if (StringUtil.isEmpty(appendixIds)) {
			return 1;
		}

		ToolAppendix toolAppendix;
		String[] idList = appendixIds.split(",");
		Long appendixId;
		for (String id : idList) {
			toolAppendix = new ToolAppendix();
			appendixId = Long.parseLong(id);
			toolAppendix.setPkId(appendixId);
			toolAppendix.setCheckId(checkId);
			toolAppendix.setToolId(toolCheck.getToolId());
			toolAppendix.setAppdenixType(toolCheck.getCheckType());
			appendixMapper.updateActiveById(toolAppendix, appendixId);
		}
		return 1;
	}

	// 新刀条待刃磨
	private void insertWaitRepair(Long userId, ToolWaitCheck checkwait) throws BusinessException {
		Date date = new Date();
		ToolWaitHandle waitHandle = new ToolWaitHandle();
		waitHandle.setReceiptId(checkwait.getReceiptId());
		waitHandle.setPkId(getPkId());
		waitHandle.setCreateTime(date);
		waitHandle.setCreateUser(userId);
		waitHandle.setUpdateTime(date);
		waitHandle.setUpdateUser(userId);
		waitHandle.setDelMark(0);
		waitHandle.setComposeNumber(checkwait.getFullNumber());
		waitHandle.setToolNumber(checkwait.getToolNumber());
		waitHandle.setToolQty(checkwait.getToolQty());
		waitHandle.setHandleType(2);
		waitHandle.setHandleResult(0);
		waitHandle.setIsNew(checkwait.getIsNew());
		toolWaitHandleMapper.insert(waitHandle);
	}

	// 新刀条待入库
	private void insertWaitWhouse(Long userId, ToolWaitCheck checkwait) throws BusinessException {
		Date date = new Date();
		ToolWaitHandle waitHandle = new ToolWaitHandle();
		waitHandle.setReceiptId(checkwait.getReceiptId());
		waitHandle.setPkId(getPkId());
		waitHandle.setCreateTime(date);
		waitHandle.setCreateUser(userId);
		waitHandle.setUpdateTime(date);
		waitHandle.setUpdateUser(userId);
		waitHandle.setDelMark(0);
		waitHandle.setTypeId(checkwait.getTypeId());
		waitHandle.setToolNumber(checkwait.getToolNumber());
		waitHandle.setToolQty(checkwait.getToolQty());
		waitHandle.setToolSeq(checkwait.getToolSeq());
		waitHandle.setHandleType(1);
		waitHandle.setHandleResult(0);
		waitHandle.setIsNew(1);
		waitHandle.setReceiptId(checkwait.getReceiptId());
		// 修改收货单质检状态
		ToolPurchaseReceipt purchaseReceipt = new ToolPurchaseReceipt();
		purchaseReceipt.setCheckStatus(2);
		purchaseReceipt.setPkId(checkwait.getReceiptId());
		purchaseReceipt.setUpdateTime(date);
		purchaseReceipt.setUpdateUser(userId);
		purchaseReceiptMapper.updateActiveById(purchaseReceipt, checkwait.getReceiptId());
		toolWaitHandleMapper.insert(waitHandle);
	}

	// 待涂层出库记录
	private void insertWaitCoat(Long userId, ToolWaitCheck checkwait) throws BusinessException {
		Date date = new Date();
		ToolWaitHandle waitHandle = new ToolWaitHandle();
		waitHandle.setReceiptId(checkwait.getReceiptId());
		waitHandle.setPkId(getPkId());
		waitHandle.setCreateTime(date);
		waitHandle.setCreateUser(userId);
		waitHandle.setUpdateTime(date);
		waitHandle.setUpdateUser(userId);
		waitHandle.setDelMark(0);
		waitHandle.setComposeNumber(checkwait.getFullNumber());
		waitHandle.setToolNumber(checkwait.getToolNumber());
		waitHandle.setToolQty(checkwait.getToolQty());
		waitHandle.setHandleType(3);
		waitHandle.setHandleResult(0);
		waitHandle.setIsNew(checkwait.getIsNew());
		toolWaitHandleMapper.insert(waitHandle);
		// 修改刀盘组合状态
		if (null != checkwait.getFullNumber()) {
			ToolBladeCompose compose = new ToolBladeCompose();
			compose.setComposeNumber(checkwait.getFullNumber());
			compose.setToolStatus(4);
			compose.setUpdateTime(date);
			compose.setUpdateUser(userId);
			bladeComposeMapper.updateActiveByComposeNumber(compose, checkwait.getFullNumber());
		}
	}

	private void insertCoat(Long userId, ToolCheck toolCheck, ToolWaitCheck waitCheck) throws BusinessException {
		Date date = new Date();
		if (null != toolCheck.getSupplierId()) {
			Supplier supplier = supplierMapper.selectById(toolCheck.getSupplierId());
			// toolCheck.setSupplierCode(supplier.getSupplierCode());
			if (null != supplier) {
				toolCheck.setSupplierName(supplier.getSupplierName());
			}
		}
		
		if (4 != toolCheck.getTypeId()) {
			ToolCoatPrice toolCoatPrice = toolCoatPriceMapper.selectByToolNumberAndSupplierId(toolCheck.getToolNumber(), toolCheck.getSupplierId());
			Tool tool = toolMapper.selectByFullNumber(toolCheck.getFullNumber());
			ToolCoat toolCoat = new ToolCoat();
			toolCoat.setPkId(getPkId());
			toolCoat.setCreateTime(date);
			toolCoat.setCreateUser(userId);
			toolCoat.setUpdateTime(date);
			toolCoat.setUpdateUser(userId);
			toolCoat.setDelMark(0);
			toolCoat.setToolId(tool.getPkId());
			toolCoat.setTypeId(tool.getTypeId());
			toolCoat.setFullNumber(toolCheck.getFullNumber());
			toolCoat.setToolNumber(tool.getToolNumber());
			toolCoat.setSupplierId(toolCheck.getSupplierId());
			toolCoat.setCoatSupplier(toolCheck.getSupplierName());
			toolCoat.setDeliever(toolCheck.getDeliveryer());
			toolCoat.setCoatTime(date);
			toolCoat.setToolQty(1);
			toolCoat.setOutboundTime(waitCheck.getCreateTime());
			toolCoat.setOutboundUserId(waitCheck.getCreateUser());

			if (toolCoatPrice != null) {
				toolCoat.setCoatPrice(toolCoatPrice.getPrice());
			}
			toolCoatMapper.insert(toolCoat);
			Integer coatTimes;
			if (tool.getCoatTimes() != null) {
				coatTimes = tool.getCoatTimes() + 1;
			} else {
				coatTimes = 1;
			}
			tool.setCoatTimes(coatTimes);
			toolMapper.updateActiveById(tool, toolCoat.getToolId());
		} else {
			// 新刀条涂层
			if (null != toolCheck.getToolNumber()) {
				ToolCoatPrice toolCoatPrice = toolCoatPriceMapper.selectByToolNumberAndSupplierId(toolCheck.getToolNumber(), toolCheck.getSupplierId());
				ToolBase toolBase = toolBaseMapper.selectByToolNumber(toolCheck.getToolNumber(), null);
				ToolCoat toolCoat = new ToolCoat();
				toolCoat.setToolNumber(toolBase.getToolNumber());
				toolCoat.setToolQty(toolCheck.getToolQty());
				toolCoat.setTypeId(4);
				toolCoat.setPkId(getPkId());
				toolCoat.setCreateTime(date);
				toolCoat.setCreateUser(userId);
				toolCoat.setUpdateTime(date);
				toolCoat.setUpdateUser(userId);
				toolCoat.setDelMark(0);
				toolCoat.setOutboundTime(waitCheck.getCreateTime());
				toolCoat.setOutboundUserId(waitCheck.getCreateUser());
				toolCoat.setSupplierId(toolCheck.getSupplierId());
				toolCoat.setCoatSupplier(toolCheck.getSupplierName());
				toolCoat.setDeliever(toolCheck.getDeliveryer());
				toolCoat.setCoatTime(date);
				if (toolCoatPrice != null) {
					toolCoat.setCoatPrice(toolCoatPrice.getPrice());
				}
				toolCoatMapper.insert(toolCoat);
			} else {
				ToolBladeComposeDetail where = new ToolBladeComposeDetail();
				where.setComposeNumber(waitCheck.getFullNumber());
				List<ToolBladeComposeDetail> detailList = composeDetailMapper.select(where);
				for(ToolBladeComposeDetail item:detailList){					
					ToolCoatPrice toolCoatPrice = toolCoatPriceMapper.selectByToolNumberAndSupplierId(item.getToolNumber(), toolCheck.getSupplierId());
					ToolCoat toolCoat = new ToolCoat();
					toolCoat.setFullNumber(waitCheck.getFullNumber());
					toolCoat.setToolNumber(item.getToolNumber());
					toolCoat.setToolQty(item.getCoatQty());
					toolCoat.setTypeId(4);
					toolCoat.setPkId(getPkId());
					toolCoat.setCreateTime(date);
					toolCoat.setCreateUser(userId);
					toolCoat.setUpdateTime(date);
					toolCoat.setUpdateUser(userId);
					toolCoat.setDelMark(0);
					toolCoat.setOutboundTime(waitCheck.getCreateTime());
					toolCoat.setOutboundUserId(waitCheck.getCreateUser());
					toolCoat.setSupplierId(toolCheck.getSupplierId());
					toolCoat.setCoatSupplier(toolCheck.getSupplierName());
					toolCoat.setDeliever(toolCheck.getDeliveryer());
					toolCoat.setCoatTime(date);
					if (toolCoatPrice != null) {
						toolCoat.setCoatPrice(toolCoatPrice.getPrice());
					}
					toolCoatMapper.insert(toolCoat);
				}
				
			}

		}
	}

	private void insertUnqualifiedReport(Long userId, ToolCheck toolCheck, Long checkId, Date date) throws BusinessException {
		ToolUnqualifiedReport report = new ToolUnqualifiedReport();
		report.setReportType(toolCheck.getCheckType());
		report.setReportDesc(toolCheck.getRemark());
		report.setReporterId(userId);
		report.setReporterName(toolCheck.getChecker());
		report.setPkId(getPkId());
		report.setCreateTime(date);
		report.setCreateUser(userId);
		report.setUpdateTime(date);
		report.setUpdateUser(userId);
		report.setDelMark(0);
		report.setReportTime(date);
		report.setCheckId(checkId);
		// 新刀检验不合格
		if (report.getReportType() == 1) {
			report.setNewAuditStatus(0);
			report.setToolNumber(toolCheck.getToolNumber());
			report.setSupplierId(toolCheck.getSupplierId());
			report.setToolSeq(toolCheck.getToolSeq());
		} else if (report.getReportType() == 2) {
			report.setRepairAuditStatus(0);
			report.setToolNumber(toolCheck.getToolNumber());
			report.setFullNumber(toolCheck.getFullNumber());
		} else if (report.getReportType() == 3) {
			report.setCoatAuditStatus(0);
			report.setToolNumber(toolCheck.getToolNumber());
			report.setFullNumber(toolCheck.getFullNumber());
			report.setSupplierId(toolCheck.getSupplierId());
		}
		toolUnqualifiedReportMapper.insert(report);
	}

	private void updateToolCheckResult(Long userId, String fullNumber, int checkResult, Date date) {
		Tool tool = new Tool();
		Tool oldTool = toolMapper.selectByFullNumber(fullNumber);
		if (oldTool.getTypeId() == 3) {
			tool.setToolState(3);
		} else {
			tool.setToolState(9);
		}
		tool.setFullNumber(fullNumber);
		tool.setCheckResult(checkResult);

		tool.setCreateTime(date);
		tool.setCreateUser(userId);
		toolMapper.updateActiveByNumber(tool);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolCheck toolCheck, Long toolCheckId, String appendixIds) throws BusinessException {

		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolCheck selectById(Long userId, Long toolCheckId) throws BusinessException {
		ToolCheck toolCheck = toolCheckMapper.selectById(toolCheckId);
		if (null != toolCheck) {
			// CheckStandard checkStandard =
			// standardMapper.selectById(toolCheck.getStandardId());
			// toolCheck.setStandardCode(checkStandard.getStandardCode());
			if (toolCheck.getCheckType() == 1) {
				ToolBase toolBase = toolBaseMapper.selectByToolNumber(toolCheck.getToolNumber(), null);
				toolCheck.setToolNumber(toolBase.getToolNumber());
				toolCheck.setToolName(toolBase.getToolName());
				toolCheck.setToolMap(toolBase.getToolMap());
				if (null != toolCheck.getSupplierId()) {
					Supplier supplier = supplierMapper.selectById(toolCheck.getSupplierId());
					// toolCheck.setSupplierCode(supplier.getSupplierCode());
					if (null != supplier) {
						toolCheck.setSupplierName(supplier.getSupplierName());
					}

				}
			} else if (toolCheck.getCheckType() == 2) {
				if (toolCheck.getTypeId() == 4) {
					ToolBase toolBase = toolBaseMapper.selectByToolNumber(toolCheck.getToolNumber(), null);
					toolCheck.setToolNumber(toolBase.getToolNumber());
					toolCheck.setToolName(toolBase.getToolName());
					toolCheck.setToolMap(toolBase.getToolMap());
				} else {
					Tool tool = toolMapper.selectByFullNumber(toolCheck.getFullNumber());
					toolCheck.setToolNumber(tool.getToolNumber());
					toolCheck.setToolName(tool.getToolName());
					toolCheck.setToolMap(tool.getToolMap());
					toolCheck.setWarehouseCode(tool.getWarehouseCode());
				}
			} else if (toolCheck.getCheckType() == 3) {
				if (toolCheck.getTypeId() == 4) {
					ToolBase toolBase = toolBaseMapper.selectByToolNumber(toolCheck.getToolNumber(), null);
					toolCheck.setToolNumber(toolBase.getToolNumber());
					toolCheck.setToolName(toolBase.getToolName());
					toolCheck.setToolMap(toolBase.getToolMap());
				} else {
					Tool tool = toolMapper.selectByFullNumber(toolCheck.getFullNumber());
					toolCheck.setToolNumber(tool.getToolNumber());
					toolCheck.setToolName(tool.getToolName());
					toolCheck.setToolMap(tool.getToolMap());
					toolCheck.setWarehouseCode(tool.getWarehouseCode());
					Supplier supplier = supplierMapper.selectById(toolCheck.getSupplierId());
					if (null != supplier) {
						toolCheck.setSupplierName(supplier.getSupplierName());
					}
				}
			}
			// toolCheck.setStandardDesc(checkStandard.getStandardDesc());
			toolCheck.setResultList(toolCheckResultMapper.selectByCheckId(toolCheckId));
			toolCheck.setAppendixList(appendixMapper.selectByCheckId(toolCheckId));
		}
		return toolCheck;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolCheckId) throws BusinessException {
		return toolCheckMapper.removeById(toolCheckId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolCheckId) throws BusinessException {
		ToolCheck toolCheck = toolCheckMapper.selectById(toolCheckId);
		toolCheck.setUpdateTime(new Date());
		toolCheck.setUpdateUser(userId);
		if (toolCheck.getCheckType() == 1 || toolCheck.getCheckType() == 2) {
			ToolWaitCheck waitCheck = new ToolWaitCheck();
			waitCheck.setPkId(toolCheck.getWaitCheckId());
			waitCheck.setCheckStatus(0);
			waitCheck.setUpdateUser(userId);
			toolWaitCheckMapper.updateActiveById(waitCheck, waitCheck.getPkId());
		}
		return toolCheckMapper.deleteById(toolCheck);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolCheck> select(Long userId, ToolCheck toolCheck) throws BusinessException {
		return toolCheckMapper.select(toolCheck);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolCheck> selectPageList(Long userId, ToolCheck toolCheck, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolCheck> page = toolCheckMapper.selectPageList(toolCheck, queryDto);
		List<String> numberList = new ArrayList<String>();

		List<String> composeNumberList = new ArrayList<String>();
		List<String> headNumberList = new ArrayList<String>();

		for (ToolCheck item : page.getResult()) {
			if (item.getCheckType() == 7 || item.getCheckType() == 8) {
				String composeNumber = item.getFullNumber();
				composeNumberList.add(composeNumber);
				String headNumber = composeNumber.substring(0, composeNumber.length() - 6);
				headNumberList.add(headNumber);
				item.setToolNumber(headNumber);
			} else {
				numberList.add(item.getToolNumber());
			}

		}
		if (numberList.size() > 0) {
			numberList = numberList.stream().distinct().collect(Collectors.toList());
			List<ToolBase> tbList = toolBaseMapper.selectByNumberList(numberList);
			for (ToolCheck item : page.getResult()) {
				for (ToolBase tb : tbList) {
					if (item.getToolNumber().equals(tb.getToolNumber())) {
						item.setToolName(tb.getToolName());
						item.setToolMap(tb.getToolMap());
						break;
					}
				}
			}
		}

		if (headNumberList.size() > 0) {
			headNumberList = headNumberList.stream().distinct().collect(Collectors.toList());
			List<ToolHead> headList = toolHeadMapper.selectByHeadNumberList(headNumberList);
			Map<String, ToolHead> headMap = headList.stream().collect(Collectors.toMap(ToolHead::getHeadNumber, t -> t, (k1, k2) -> k1));
			composeNumberList = composeNumberList.stream().distinct().collect(Collectors.toList());
			List<ToolBladeComposeDetail> detailList = composeDetailMapper.selectByComposeList(composeNumberList);
			Map<String, List<ToolBladeComposeDetail>> waitHandleMap = detailList.stream().collect(Collectors.groupingBy(ToolBladeComposeDetail::getComposeNumber));
			for (ToolCheck item : page) {
				item.setToolName(headMap.get(item.getToolNumber()).getHeadName());				
				item.setDetailList(waitHandleMap.get(item.getFullNumber()));
			}
		}
		return new Pagination<ToolCheck>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolCheck selectByToolId(Long userId, Long toolId, Integer checkType) throws BusinessException {
		ToolCheck toolCheck = toolCheckMapper.selectByToolId(toolId, checkType);

		if (null != toolCheck) {
			toolCheck.setResultList(toolCheckResultMapper.selectByCheckId(toolCheck.getPkId()));
			toolCheck.setAppendixList(appendixMapper.selectByCheckId(toolCheck.getPkId()));
		}
		return toolCheck;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolCheck toolCheckGetBySeq(Long userId, String toolNumber, String toolSeq) throws BusinessException {
		return toolCheckMapper.selectBySeq(toolNumber, toolSeq);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer toolBladeSetHandle(Long userId, Long toolCheckId, Integer handleType) throws BusinessException {
		ToolCheck toolCheck = toolCheckMapper.selectById(toolCheckId);
		Date date = new Date();
		if (handleType == 2 && toolCheck.getCheckType() == 1 && toolCheck.getTypeId() == 4) {
			ToolWaitCheck toolWaitCheck = toolWaitCheckMapper.selectById(toolCheck.getWaitCheckId());
			ToolWaitHandle waitHandle = new ToolWaitHandle();
			waitHandle.setToolNumber(toolCheck.getToolNumber());
			waitHandle.setHandleType(handleType);
			waitHandle.setIsNew(1);
			waitHandle.setReceiptId(toolWaitCheck.getReceiptId());
			waitHandle.setToolQty(toolCheck.getToolQty());
			waitHandle.setSupplierId(toolCheck.getSupplierId());
			waitHandle.setPkId(getPkId());
			waitHandle.setCreateTime(date);
			waitHandle.setCreateUser(userId);
			waitHandle.setUpdateTime(date);
			waitHandle.setUpdateUser(userId);
			waitHandle.setDelMark(0);
			waitHandle.setHandleResult(0);
			toolWaitHandleMapper.insert(waitHandle);

			toolCheckMapper.updateActiveById(toolCheck, toolCheckId);
		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer toolBladeSetCoat(Long userId, Long toolCheckId) throws BusinessException {
		ToolCheck toolCheck = toolCheckMapper.selectById(toolCheckId);
		Date date = new Date();
		if (toolCheck.getCheckType() == 2 && toolCheck.getTypeId() == 4) {
			ToolWaitCheck toolWaitCheck = toolWaitCheckMapper.selectById(toolCheck.getWaitCheckId());
			ToolWaitCheck waitCheck = new ToolWaitCheck();
			waitCheck.setPkId(getPkId());
			waitCheck.setCreateTime(date);
			waitCheck.setCreateUser(userId);
			waitCheck.setUpdateTime(date);
			waitCheck.setUpdateUser(userId);
			waitCheck.setDelMark(0);
			waitCheck.setCheckType(3);
			waitCheck.setCheckStatus(0);
			waitCheck.setToolNumber(toolWaitCheck.getToolNumber());
			// waitCheck.setToolName(toolWaitCheck.getToolName());
			// waitCheck.setToolMap(toolWaitCheck.getToolMap());
			waitCheck.setTypeId(toolWaitCheck.getTypeId());
			waitCheck.setToolQty(toolWaitCheck.getToolQty());
			waitCheck.setIsNew(toolWaitCheck.getIsNew());
			waitCheck.setReceiptId(toolWaitCheck.getReceiptId());
			waitCheck.setSetCheckTime(date);
			toolWaitCheckMapper.insert(waitCheck);
			toolCheckMapper.updateActiveById(toolCheck, toolCheckId);
		}
		return 1;
	}

}