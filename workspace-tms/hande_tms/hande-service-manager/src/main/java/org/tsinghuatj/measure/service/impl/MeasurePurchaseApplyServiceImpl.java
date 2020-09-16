package org.tsinghuatj.measure.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.tsinghuatj.base.domain.Supplier;
import org.tsinghuatj.base.repository.SupplierMapper;
import org.tsinghuatj.erp.domain.ErpMaterial;
import org.tsinghuatj.erp.repository.ErpMaterialMapper;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.measure.domain.Measure;
import org.tsinghuatj.measure.domain.MeasureBase;
import org.tsinghuatj.measure.domain.MeasureCheck;
import org.tsinghuatj.measure.domain.MeasurePurchaseApply;
import org.tsinghuatj.measure.domain.MeasurePurchaseReceipt;
import org.tsinghuatj.measure.repository.MeasureBaseMapper;
import org.tsinghuatj.measure.repository.MeasureCheckMapper;
import org.tsinghuatj.measure.repository.MeasureMapper;
import org.tsinghuatj.measure.repository.MeasurePurchaseApplyMapper;
import org.tsinghuatj.measure.repository.MeasurePurchaseReceiptMapper;
import org.tsinghuatj.measure.service.IMeasurePurchaseApplyService;
import org.tsinghuatj.sys.domain.SysMessage;
import org.tsinghuatj.sys.repository.SysMessageMapper;
import org.tsinghuatj.sys.repository.SysUserAuthMapper;
import org.tsinghuatj.sys.service.ISysMessageService;
import org.tsinghuatj.tool.domain.ToolApplyAudit;
import org.tsinghuatj.tool.repository.ToolApplyAuditMapper;


/**
 *
 * MeasurePurchaseApply 表数据服务层接口实现类
 *
 */
@Service("measurePurchaseApplyService")
public class MeasurePurchaseApplyServiceImpl extends BaseServiceImpl implements IMeasurePurchaseApplyService {

	private @Resource MeasurePurchaseApplyMapper measurePurchaseApplyMapper;
	private @Resource MeasurePurchaseReceiptMapper measurePurchaseReceiptMapper;
	private @Resource MeasureCheckMapper measureCheckMapper;
	private @Resource ToolApplyAuditMapper toolApplyAuditMapper;
	private @Resource SupplierMapper supplierMapper;
	private @Resource SysMessageMapper sysMessageMapper;
	private @Resource ISysMessageService sysMessageService;
	private @Resource SysUserAuthMapper sysUserAuthMapper;
	private @Resource MeasureBaseMapper measurebaseMapper;
	private @Resource MeasureMapper measureMapper;
	private @Resource ErpMaterialMapper materialMapper;
	private @Value("${erp.tool.code:/}") String erpToolCode;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MeasurePurchaseApply measurePurchaseApply) throws BusinessException {	
		measurePurchaseApply.setPkId(getPkId());
		measurePurchaseApply.setCreateTime(new Date());
		measurePurchaseApply.setCreateUser(userId);
		measurePurchaseApply.setUpdateTime(new Date());
		measurePurchaseApply.setUpdateUser(userId);
		measurePurchaseApply.setDelMark(0);
		
		String message = "";
		String authCode = "";
		String applyDepartment = measurePurchaseApply.getApplyDepartmentName();
		String applier = measurePurchaseApply.getApplierName();
		String measureNumber = measurePurchaseApply.getMeasureNumber();
		if (measurePurchaseApply.getApplyStatus() == 1) {
			message = "收到" + applyDepartment + "-" + applier + "于" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm") + "提交的量具编号为" + measureNumber + "的采购报告,请您及时处理";
			authCode = "01040102";
		}
		// 系统消息
		sysMessageService.insert("量具申购", message, userId, applier, authCode);
		return measurePurchaseApplyMapper.insert(measurePurchaseApply);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MeasurePurchaseApply measurePurchaseApply, Long measurePurchaseApplyId) throws BusinessException {
		measurePurchaseApply.setUpdateTime(new Date());
		measurePurchaseApply.setUpdateUser(userId);
		
		String message = "";
		String authCode = "";
		String applyDepartment = measurePurchaseApply.getApplyDepartmentName();
		String applier = measurePurchaseApply.getApplierName();
		String measureNumber = measurePurchaseApply.getMeasureNumber();
		if (measurePurchaseApply.getApplyStatus() == 1) {
			message = "收到" + applyDepartment + "-" + applier + "于" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm") + "提交的量具编号为" + measureNumber + "的采购报告,请您及时处理";
			authCode = "01040102";
		}
		// 系统消息
		sysMessageService.insert("量具申购", message, userId, applier, authCode);

		return measurePurchaseApplyMapper.updateActiveById(measurePurchaseApply, measurePurchaseApplyId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MeasurePurchaseApply selectById(Long userId, Long measurePurchaseApplyId) throws BusinessException {
		MeasurePurchaseApply measurePurchaseApply = measurePurchaseApplyMapper.selectById(measurePurchaseApplyId);
		List<ToolApplyAudit> auditList = toolApplyAuditMapper.select(measurePurchaseApplyId);
		measurePurchaseApply.setAuditList(auditList);
		return measurePurchaseApply;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long measurePurchaseApplyId) throws BusinessException {
		return measurePurchaseApplyMapper.removeById(measurePurchaseApplyId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long measurePurchaseApplyId) throws BusinessException {
		MeasurePurchaseApply measurePurchaseApply = new MeasurePurchaseApply();
		measurePurchaseApply.setPkId(measurePurchaseApplyId);
		measurePurchaseApply.setUpdateTime(new Date());
		measurePurchaseApply.setUpdateUser(userId);
		return measurePurchaseApplyMapper.deleteById(measurePurchaseApply);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MeasurePurchaseApply> select(Long userId, MeasurePurchaseApply measurePurchaseApply) throws BusinessException {		
		return measurePurchaseApplyMapper.select(measurePurchaseApply);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MeasurePurchaseApply> selectPageList(Long userId, MeasurePurchaseApply measurePurchaseApply,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MeasurePurchaseApply> page = measurePurchaseApplyMapper.selectPageList(measurePurchaseApply, queryDto);
		return new Pagination<MeasurePurchaseApply>(page.getTotal(), page.getResult());		
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MeasurePurchaseApply> selectAuditedPageList(Long userId, MeasurePurchaseApply measurePurchaseApply, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MeasurePurchaseApply> page = measurePurchaseApplyMapper.selectAuditedPageList(measurePurchaseApply, queryDto);
		return new Pagination<MeasurePurchaseApply>(page.getTotal(), page.getResult());
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public MeasureBase purchaseGetByMeasureNumber(Long userId, String measureNumber) throws BusinessException {
		MeasureBase measureBase = measurebaseMapper.selectByNumber(measureNumber);
		if (null == measureBase) {
			ErpMaterial material = materialMapper.selectByMaterialNumber(measureNumber);
			if (null == material) {
				throw new BusinessException("material.not.exists.error");
			}
			String[] codeArray = erpToolCode.split(",");
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
			measureBase = new MeasureBase();
			measureBase.setPkId(getPkId());
			measureBase.setCreateTime(new Date());
			measureBase.setCreateUser(userId);
			measureBase.setUpdateTime(new Date());
			measureBase.setUpdateUser(userId);
			measureBase.setDelMark(0);
			measureBase.setMeasureNumber(material.getItemCode());
			measurebaseMapper.insert(measureBase);

			measureBase.setErpAmount(material.getSumOnhandQuantity());
			measureBase.setNoCheckQty(material.getSumNocheckQuantity());

		} else {
			measureBase.setMeasureAmount(measureMapper.countByMeasureNumber(measureNumber));
			ErpMaterial material = materialMapper.selectByMaterialNumber(measureNumber);
			if (null != material) {
				measureBase.setErpAmount(material.getSumOnhandQuantity());
				measureBase.setNoCheckQty(material.getSumNocheckQuantity());
			}
		}
		return measureBase;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer reportAudit(Long userId, String realName, ToolApplyAudit applyAudit, MeasurePurchaseApply measurePurchaseApply) throws BusinessException {
		measurePurchaseApply.setUpdateTime(new Date());
		measurePurchaseApply.setUpdateUser(userId);

		String auditResult = "";
		String message = "";
		String authCode = "";
		String measureNumber  = measurePurchaseApply.getMeasureNumber();
		String applyDepartment = measurePurchaseApply.getApplyDepartmentName();
		String applier = measurePurchaseApply.getApplierName();

		int auditStatus = measurePurchaseApply.getApplyStatus();
		if (auditStatus == -1) {
			auditResult = "分厂领导审核未通过";
			message = "分厂领导驳回了" + applyDepartment + "-"+ applier +"提交的量具编号为"+ measureNumber +"的量具采购申请,请您及时处理";
			authCode = "01040101";
		} else if (auditStatus == 2) {
			auditResult = "分厂领导审核通过";
			message = applyDepartment + "-"+ applier + "提交的量具编号为"+ measureNumber +"的采购申请需要判定是否外购,请您及时处理";
			authCode = "01040103";
		} else if (auditStatus == 3) {
			String availableNumber = "";
			Measure measure = measureMapper.getSeqByMeasureNumber(measureNumber);
			Integer needQty = measurePurchaseApply.getApplyQty();
			Integer startIndex;
			if (measure != null) {
				startIndex = measure.getMeasureSeq() + 1;
			} else {
				startIndex = 1;
			}

			for (int i = 1; i <= needQty; i++) {
				availableNumber = availableNumber + measureNumber + "-" + fillString(startIndex, 5) + "  ";
				startIndex++;
			}
			measurePurchaseApply.setAvailableNumber(availableNumber);
			
			auditResult = "圆柱分厂判断自制";
			message =  applyDepartment + "-"+ applier + "提交的量具编号为"+ measureNumber +"的采购申请已转圆柱分厂自制,请您及时处理";
			authCode = "01040102";
		}else if (auditStatus == 4) {
			auditResult = "圆柱分厂判断外购";
			message =  applyDepartment + "-"+ applier + "提交的量具编号为"+ measureNumber +"的采购申请已转采购部外购,请您及时处理";
			authCode = "01040104";
		}else if (auditStatus == -1) {
			auditResult = "采购部驳回";
			message =  applyDepartment + "-"+ applier + "提交的量具编号为"+ measureNumber +"的采购申请已被采购部驳回,请您及时处理";
			authCode = "01040101";
		}else if (auditStatus == 5) {
			auditResult = "价格偏高，待主管领导审核";
			message =  applyDepartment + "-"+ applier + "提交的量具编号为"+ measureNumber +"的采购申请价格偏高,请您及时处理";
			authCode = "01040105";
		}else if (auditStatus == -5) {
			auditResult = "主管领导驳回";
			message =  applyDepartment + "-"+ applier + "提交的量具编号为"+ measureNumber +"的采购申请已被主管领导驳回,请您及时处理";
			authCode = "01030104";
		}else if (auditStatus == 6) {
			String availableNumber = "";
			Measure measure = measureMapper.getSeqByMeasureNumber(measureNumber);
			Integer needQty = measurePurchaseApply.getApplyQty();
			Integer startIndex;
			if (measure != null) {
				startIndex = measure.getMeasureSeq() + 1;
			} else {
				startIndex = 1;
			}

			for (int i = 1; i <= needQty; i++) {
				availableNumber = availableNumber + measureNumber + "-" + fillString(startIndex, 5) + "  ";
				startIndex++;
			}
			measurePurchaseApply.setAvailableNumber(availableNumber);
			auditResult = "审核通过，待采购部接受";
			message =  applyDepartment + "-"+ applier + "提交的量具编号为"+ measureNumber +"的采购申请已通过审核,请您及时安排采购";
			authCode = "010302";
		}

		// 审核日志
		saveAudit(userId, realName, measurePurchaseApply.getPkId(), auditResult, applyAudit);
		// 系统消息
		setMessage("量具申购",message, userId,realName, authCode);

		return measurePurchaseApplyMapper.updateActiveById(measurePurchaseApply, measurePurchaseApply.getPkId());
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
		applyAudit.setApplyType(3);
		applyAudit.setAuditResult(auditResult);
		applyAudit.setAuditorId(userId);
		applyAudit.setAuditorName(realName);
		applyAudit.setAuditSeq(auditSeq);
		toolApplyAuditMapper.insert(applyAudit);

	}

	private void setMessage(String title,String message, Long userId,String realName, String authCode) throws BusinessException {
		// 获取拥有此项权限的用户ID列表
		List<Long> userList = sysUserAuthMapper.selectUserIdByAuthCode(authCode);
		Date date = new Date();
		SysMessage sysMessage = new SysMessage();
		sysMessage.setTitle(title);
		sysMessage.setMessage(message);
		sysMessage.setSenderId(userId);
		sysMessage.setSenderName(realName);
		sysMessage.setSendTime(date);
		sysMessage.setCreateTime(date);
		sysMessage.setCreateUser(userId);
		sysMessage.setUpdateTime(date);
		sysMessage.setUpdateUser(userId);
		sysMessage.setDelMark(0);
		// 遍历拥有权限的人，并设置为该条推送信息的接收人。循环封装并执行添加
		for (Long receiverId : userList) {
			if(userId.equals(receiverId)){
				continue;
			}
			sysMessage.setPkId(getPkId());
			sysMessage.setReceiverId(receiverId);
			sysMessageMapper.insert(sysMessage);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MeasurePurchaseReceipt> selectByApplyId(Long userId, Long applyId) throws BusinessException {
		return measurePurchaseReceiptMapper.selectByApplyId(applyId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updatePurchaseReceipt(Long userId, Long pkId, Integer arrivaledQty, List<MeasurePurchaseReceipt> receiptList) throws BusinessException {
		Date date = new Date();
		MeasurePurchaseApply measurePurchaseApply = measurePurchaseApplyMapper.selectById(pkId);

		List<MeasurePurchaseReceipt> list = measurePurchaseReceiptMapper.selectByApplyId(pkId);

		for (MeasurePurchaseReceipt receipt : list) {
			if (!receiptList.contains(receipt)) {
				receipt.setUpdateTime(date);
				receipt.setUpdateUser(userId);
				measurePurchaseReceiptMapper.deleteById(receipt);
			}
		}

		for (MeasurePurchaseReceipt item : receiptList) {
			// 在检过程中的不允许修改
			if (null == item.getPkId()) {
				MeasureCheck measureCheck = new MeasureCheck();
				item.setPkId(getPkId());
				item.setApplyId(pkId);
				item.setMeasureName(measurePurchaseApply.getMeasureName());
				item.setMeasureNumber(measurePurchaseApply.getMeasureNumber());
				item.setModel(measurePurchaseApply.getModel());
				Supplier supplier = supplierMapper.selectById(item.getSupplierId());
				if (null != supplier) {
					item.setSupplierName(supplier.getSupplierName());
					measureCheck.setSupplierName(supplier.getSupplierName());
				}

				item.setCreateUser(userId);
				item.setCreateTime(date);
				item.setUpdateTime(date);
				item.setDelMark(0);
				item.setUpdateUser(userId);
				measurePurchaseReceiptMapper.insert(item);
				
				measureCheck.setPkId(getPkId());
				measureCheck.setCreateUser(userId);
				measureCheck.setCreateTime(date);
				measureCheck.setUpdateUser(userId);
				measureCheck.setUpdateTime(date);
				measureCheck.setDelMark(0);
				
				
				measureCheck.setMeasureSeq(item.getSequenceNumber());
				measureCheck.setMeasureNumber(measurePurchaseApply.getMeasureNumber());
				measureCheck.setMeasureName(measurePurchaseApply.getMeasureName());
				measureCheck.setModel(measurePurchaseApply.getModel());
				measureCheck.setSupplierId(item.getSupplierId());
				measureCheck.setDeliveryer(item.getDeliverer());
				measureCheck.setDeliveryTime(date);
				measureCheck.setCheckType(1);
				measureCheck.setReceiptId(item.getPkId());
				measureCheckMapper.insert(measureCheck);
			} else if(null != item.getPkId()) {
					item.setUpdateTime(date);
					item.setUpdateUser(userId);
					Supplier supplier = supplierMapper.selectById(item.getSupplierId());
					if (null != supplier) {
						item.setSupplierName(supplier.getSupplierName());
					}
					measurePurchaseReceiptMapper.updateActiveById(item, item.getPkId());
			}
		}

		int qty = measurePurchaseReceiptMapper.selectCountByApplyId(pkId);
		measurePurchaseApply.setArrivaledQty(qty);
		if (qty == measurePurchaseApply.getApplyQty()) {
			measurePurchaseApply.setFinishTime(date);
			measurePurchaseApply.setApplyStatus(8);
		}

		measurePurchaseApply.setUpdateTime(date);
		measurePurchaseApply.setUpdateUser(userId);
		measurePurchaseApplyMapper.updateActiveById(measurePurchaseApply, pkId);

		return 1;
	}
}