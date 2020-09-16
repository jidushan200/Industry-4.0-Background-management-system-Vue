package org.tsinghuatj.tool.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.base.domain.Supplier;
import org.tsinghuatj.base.repository.StaffMapper;
import org.tsinghuatj.base.repository.SupplierMapper;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.service.ISysMessageService;
import org.tsinghuatj.tool.domain.Tool;
import org.tsinghuatj.tool.domain.ToolApplyAudit;
import org.tsinghuatj.tool.domain.ToolPurchaseApply;
import org.tsinghuatj.tool.domain.ToolPurchaseReceipt;
import org.tsinghuatj.tool.domain.ToolWaitCheck;
import org.tsinghuatj.tool.repository.ToolApplyAuditMapper;
import org.tsinghuatj.tool.repository.ToolMapper;
import org.tsinghuatj.tool.repository.ToolPurchaseApplyMapper;
import org.tsinghuatj.tool.repository.ToolPurchaseReceiptMapper;
import org.tsinghuatj.tool.repository.ToolWaitCheckMapper;
import org.tsinghuatj.tool.service.IToolPurchaseApplyService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolPurchaseApply 表数据服务层接口实现类
 *
 */
@Service("toolPurchaseApplyService")
public class ToolPurchaseApplyServiceImpl extends BaseServiceImpl implements IToolPurchaseApplyService {

	private @Resource ToolWaitCheckMapper toolWaitCheckMapper;
	private @Resource ToolApplyAuditMapper toolApplyAuditMapper;
	private @Resource ToolMapper toolMapper;
	private @Resource SupplierMapper supplierMapper;
	private @Resource StaffMapper staffMapper;
	private @Resource ISysMessageService sysMessageService;
	private @Resource ToolPurchaseApplyMapper toolPurchaseApplyMapper;
	private @Resource ToolPurchaseReceiptMapper toolPurchaseReceiptMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolPurchaseApply toolPurchaseApply) throws BusinessException {
		toolPurchaseApply.setPkId(getPkId());
		toolPurchaseApply.setCreateTime(new Date());
		toolPurchaseApply.setCreateUser(userId);
		toolPurchaseApply.setUpdateTime(new Date());
		toolPurchaseApply.setUpdateUser(userId);
		toolPurchaseApply.setDelMark(0);

		Tool tool = new Tool();
		tool.setToolNumber(toolPurchaseApply.getToolNumber());
		tool.setToolState(1);
		List<Tool> toolList = toolMapper.select(tool);
		Integer total = toolList.size();
		toolPurchaseApply.setInventoryQty(total);

		String message = "";
		String authCode = "";
		String applyDepartment = toolPurchaseApply.getDepartmentName();
		String applier = toolPurchaseApply.getApplierName();
		String toolNumber = toolPurchaseApply.getToolNumber();
		if (toolPurchaseApply.getApplyStatus() == 1) {
			message = "收到" + applyDepartment + "-" + applier + "于" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm") + "提交的物料编号为" + toolNumber + "的采购报告,请您及时处理";
			authCode = "01030102";
		}
		// 系统消息
		sysMessageService.insert("刀具申购", message, userId, applier, authCode);

		return toolPurchaseApplyMapper.insert(toolPurchaseApply);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolPurchaseApply toolPurchaseApply, Long toolPurchaseApplyId) throws BusinessException {
		toolPurchaseApply.setUpdateTime(new Date());
		toolPurchaseApply.setUpdateUser(userId);
		String message = "";
		String authCode = "";
		String applyDepartment = toolPurchaseApply.getDepartmentName();
		String applier = toolPurchaseApply.getApplierName();
		String toolNumber = toolPurchaseApply.getToolNumber();
		if (toolPurchaseApply.getApplyStatus() == 1) {
			message = "收到" + applyDepartment + "-" + applier + "于" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm") + "提交了物料编号为" + toolNumber + "的采购报告,请您及时处理";
			authCode = "01030102";
		}
		// 系统消息
		sysMessageService.insert("刀具申购", message, userId, applier, authCode);
		return toolPurchaseApplyMapper.updateActiveById(toolPurchaseApply, toolPurchaseApplyId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolPurchaseApply selectById(Long userId, Long toolPurchaseApplyId) throws BusinessException {
		ToolPurchaseApply toolPurchaseApply = toolPurchaseApplyMapper.selectById(toolPurchaseApplyId);
		List<ToolApplyAudit> auditList = toolApplyAuditMapper.select(toolPurchaseApplyId);
		toolPurchaseApply.setAuditList(auditList);

		// Staff staff =
		// staffMapper.selectById(toolPurchaseApply.getApplierId());
		/*
		 * if(staff != null){ toolPurchaseApply.setd(staff.getDepartmentName() +
		 * (StringUtils.isNotEmpty(staff.getStaffClass()) ? "-" +
		 * staff.getStaffClass() : "") +
		 * (StringUtils.isNotEmpty(staff.getPosition()) ? "-" +
		 * staff.getPosition() : "") + "-" + staff.getStaffName()); }
		 */

		return toolPurchaseApply;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolPurchaseApplyId) throws BusinessException {
		return toolPurchaseApplyMapper.removeById(toolPurchaseApplyId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolPurchaseApplyId) throws BusinessException {
		ToolPurchaseApply toolPurchaseApply = new ToolPurchaseApply();
		toolPurchaseApply.setPkId(toolPurchaseApplyId);
		toolPurchaseApply.setUpdateTime(new Date());
		toolPurchaseApply.setUpdateUser(userId);
		return toolPurchaseApplyMapper.deleteById(toolPurchaseApply);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolPurchaseApply> select(Long userId, ToolPurchaseApply toolPurchaseApply) throws BusinessException {
		return toolPurchaseApplyMapper.select(toolPurchaseApply);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolPurchaseApply> selectPageList(Long userId, ToolPurchaseApply toolPurchaseApply, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolPurchaseApply> page = toolPurchaseApplyMapper.selectPageList(toolPurchaseApply, queryDto);
		return new Pagination<ToolPurchaseApply>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolPurchaseApply> selectAuditedPageList(Long userId, ToolPurchaseApply toolPurchaseApply, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolPurchaseApply> page = toolPurchaseApplyMapper.selectAuditedPageList(toolPurchaseApply, queryDto);
		return new Pagination<ToolPurchaseApply>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer reportAudit(Long userId, String realName, ToolApplyAudit applyAudit, ToolPurchaseApply toolPurchaseApply) throws BusinessException {
		toolPurchaseApply.setUpdateTime(new Date());
		toolPurchaseApply.setUpdateUser(userId);

		String auditResult = "";
		String message = "";
		String authCode = "";
		String toolNumber = toolPurchaseApply.getToolNumber();
		String applyDepartment = toolPurchaseApply.getDepartmentName();
		String applier = toolPurchaseApply.getApplierName();

		int auditStatus = toolPurchaseApply.getApplyStatus();
		if (auditStatus == -1) {
			auditResult = "分厂领导审核未通过";
			message = "分厂领导驳回了" + applyDepartment + "-" + applier + "提交的物料编号为" + toolNumber + "的物料采购申请,请您及时处理";
			authCode = "01030101";
		} else if (auditStatus == -2) {
			auditResult = "工艺部审核未通过";
			message = "工艺部驳回了" + applyDepartment + "-" + applier + "提交的物料编号为" + toolNumber + "的采购报告,请您及时处理";
			authCode = "01030102";
		} else if (auditStatus == 2) {
			auditResult = "分厂领导审核通过";
			message = applyDepartment + "-" + applier + "提交的物料编号为" + toolNumber + "的采购申请已通过分厂领导审核,请您及时处理";
			authCode = "01030103";
		} else if (auditStatus == -3) {
			auditResult = "主管领导审核未通过";
			message = "主管领导驳回了" + applyDepartment + "-" + applier + "提交的物料编号为" + toolNumber + "的采购报告,请您及时处理";
			authCode = "01030103";
		} else if (auditStatus == -4) {
			auditResult = "采购部未通过";
			message = "采购部驳回了" + applyDepartment + "-" + applier + "提交的物料编号为" + toolNumber + "的采购报告,请您及时处理";
			authCode = "01030104";
		} else if (auditStatus == 3) {
			auditResult = "工艺部审核通过";
			message = applyDepartment + "-" + applier + "提交的物料编号为" + toolNumber + "的采购申请已通过工艺部审核,请您及时处理";
			authCode = "01030104";
		} else if (auditStatus == 4) {
			String availableNumber = "";
			Tool tool = toolMapper.toolGetSeqByToolNumber(toolPurchaseApply.getToolNumber());
			// 刀头是批量采购，所以不需要生成顺序号

			Integer needQty = toolPurchaseApply.getNeedQty();
			Integer startIndex;
			if (tool.getToolSeq() != null) {
				startIndex = tool.getToolSeq() + 1;
			} else {
				startIndex = 1;
			}

			if (!tool.getTypeId().equals(4)) {
				for (int i = 1; i <= needQty; i++) {
					availableNumber = availableNumber + toolPurchaseApply.getToolNumber() + "-" + fillString(startIndex, 5) + "  ";
					startIndex++;
				}
				toolPurchaseApply.setAvailableNumber(availableNumber);
			}

			auditResult = "主管领导审核通过";
			message = applyDepartment + "-" + applier + "提交的物料编号为" + toolNumber + "的采购申请已通过主管领导审核,请您及时安排采购";
			authCode = "010302";
		}

		// 审核日志
		saveAudit(userId, realName, toolPurchaseApply.getPkId(), auditResult, applyAudit);
		// 系统消息
		sysMessageService.insert("刀具申购", message, userId, realName, authCode);

		return toolPurchaseApplyMapper.updateActiveById(toolPurchaseApply, toolPurchaseApply.getPkId());
	}

	private String fillString(int num, int digit) {
		/**
		 * 0：表示前面补0 digit：表示保留数字位数 d：表示参数为正数类型
		 */
		return String.format("%0" + digit + "d", num);
	}

	private void saveAudit(Long userId, String realName, Long reportId, String auditResult, ToolApplyAudit applyAudit) throws BusinessException {
		ToolApplyAudit applyAuditSeq = toolApplyAuditMapper.selectSeqByApplyId(reportId);
		Integer auditSeq;
		if (applyAuditSeq != null) {
			Integer seqMax = applyAuditSeq.getAuditSeq();
			auditSeq = seqMax + 1;
		} else {
			auditSeq = 1;
		}
		Date date = new Date();
		applyAudit.setPkId(getPkId());
		applyAudit.setCreateTime(date);
		applyAudit.setCreateUser(userId);
		applyAudit.setUpdateTime(date);
		applyAudit.setUpdateUser(userId);
		applyAudit.setDelMark(0);
		applyAudit.setApplyId(reportId);
		applyAudit.setApplyType(1);
		applyAudit.setAuditResult(auditResult);
		applyAudit.setAuditorId(userId);
		applyAudit.setAuditorName(realName);
		applyAudit.setAuditSeq(auditSeq);
		toolApplyAuditMapper.insert(applyAudit);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolPurchaseReceipt> selectByApplyId(Long userId, Long applyId) throws BusinessException {
		return toolPurchaseReceiptMapper.selectByApplyId(applyId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updatePurchaseReceipt(Long userId, Long pkId, Integer arrivaledQty, List<ToolPurchaseReceipt> receiptList) throws BusinessException {
		Date date = new Date();
		ToolPurchaseApply toolPurchaseApply = toolPurchaseApplyMapper.selectById(pkId);

		List<ToolPurchaseReceipt> list = new ArrayList<ToolPurchaseReceipt>();
		if (null != toolPurchaseApply.getArrivaledQty()) {
			list = toolPurchaseReceiptMapper.selectByApplyId(pkId);
		}
		for (ToolPurchaseReceipt receipt : list) {
			if (!receiptList.contains(receipt)) {
				receipt.setUpdateTime(date);
				receipt.setUpdateUser(userId);
				toolPurchaseReceiptMapper.deleteById(receipt);
			}
		}
		for (ToolPurchaseReceipt item : receiptList) {
			// 在检过程中的不允许修改
			if (null == item.getPkId()) {

				ToolWaitCheck toolWaitCheck = new ToolWaitCheck();
				Long waitCheckId = getPkId();
				toolWaitCheck.setPkId(waitCheckId);
				toolWaitCheck.setSupplierId(item.getSupplierId());
				if (4 != toolPurchaseApply.getTypeId()) {
					toolWaitCheck.setFullNumber(toolPurchaseApply.getToolNumber() + "-" + item.getSequenceNumber() + "/" + toolPurchaseApply.getToolMap());
				}
				toolWaitCheck.setToolNumber(toolPurchaseApply.getToolNumber());
				toolWaitCheck.setToolName(toolPurchaseApply.getToolName());
				toolWaitCheck.setToolMap(toolPurchaseApply.getToolMap());
				toolWaitCheck.setToolSeq(item.getSequenceNumber());
				toolWaitCheck.setTypeId(toolPurchaseApply.getTypeId());
				toolWaitCheck.setCheckType(1);
				toolWaitCheck.setCheckStatus(0);
				toolWaitCheck.setSetCheckTime(new Date());
				toolWaitCheck.setCreateUser(userId);
				toolWaitCheck.setCreateTime(date);
				toolWaitCheck.setCheckStatus(0);
				toolWaitCheck.setUpdateTime(date);
				toolWaitCheck.setDelMark(0);
				toolWaitCheck.setUpdateUser(userId);
				toolWaitCheck.setToolQty(item.getToolQty());
				item.setPkId(getPkId());
				toolWaitCheck.setIsNew(1);
				toolWaitCheck.setReceiptId(item.getPkId());
				item.setApplyId(pkId);
				item.setToolNumber(toolPurchaseApply.getToolNumber());
				item.setToolName(toolPurchaseApply.getToolName());
				item.setToolMap(toolPurchaseApply.getToolMap());
				Supplier supplier = supplierMapper.selectById(item.getSupplierId());
				if (null != supplier) {
					item.setSupplierName(supplier.getSupplierName());
					toolWaitCheck.setSupplierName(supplier.getSupplierName());
				}

				item.setCreateUser(userId);
				item.setCreateTime(date);
				item.setCheckStatus(0);
				item.setUpdateTime(date);
				item.setDelMark(0);
				item.setUpdateUser(userId);
				item.setWaitCheckId(waitCheckId);
				toolWaitCheckMapper.insert(toolWaitCheck);
				toolPurchaseReceiptMapper.insert(item);

			} else if (item.getCheckStatus() == 0) {
				if (null != item.getPkId()) {
					item.setUpdateTime(date);
					item.setUpdateUser(userId);
					Supplier supplier = supplierMapper.selectById(item.getSupplierId());
					if (null != supplier) {
						item.setSupplierName(supplier.getSupplierName());
					}
					toolPurchaseReceiptMapper.updateActiveById(item, item.getPkId());
				}
			}
		}

		int qty = 0;
		if (4 == toolPurchaseApply.getTypeId()) {
			qty = receiptList.stream().mapToInt(ToolPurchaseReceipt::getToolQty).sum();
		} else {
			qty = toolPurchaseReceiptMapper.selectCountByApplyId(pkId);
		}

		toolPurchaseApply.setArrivaledQty(qty);
		if (qty >= toolPurchaseApply.getNeedQty()) {
			toolPurchaseApply.setFinishTime(date);
			toolPurchaseApply.setApplyStatus(6);
		}

		toolPurchaseApply.setUpdateTime(date);
		toolPurchaseApply.setUpdateUser(userId);
		toolPurchaseApplyMapper.updateActiveById(toolPurchaseApply, pkId);

		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolPurchaseApply> selectPurchaseReceiptPageList(Long userId, ToolPurchaseApply toolPurchaseApply, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolPurchaseApply> page = toolPurchaseApplyMapper.selectPurchaseReceiptPageList(toolPurchaseApply, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (ToolPurchaseApply item : page) {
			idList.add(item.getPkId());
		}
		if (idList.size() > 0) {
			List<ToolPurchaseReceipt> list = toolPurchaseReceiptMapper.selectByApplyIdList(idList);
			Map<Long, List<ToolPurchaseReceipt>> applyMap = list.stream().collect(Collectors.groupingBy(ToolPurchaseReceipt::getApplyId));
			for (ToolPurchaseApply item : page.getResult()) {
				item.setReceipList(applyMap.get(item.getPkId()));
			}
		}
		return new Pagination<ToolPurchaseApply>(page.getTotal(), page.getResult());
	}
}