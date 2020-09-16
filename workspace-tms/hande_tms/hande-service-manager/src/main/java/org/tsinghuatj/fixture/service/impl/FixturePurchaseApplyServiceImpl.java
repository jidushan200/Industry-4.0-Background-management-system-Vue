package org.tsinghuatj.fixture.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.base.domain.Staff;
import org.tsinghuatj.base.domain.Supplier;
import org.tsinghuatj.base.repository.StaffMapper;
import org.tsinghuatj.base.repository.SupplierMapper;
import org.tsinghuatj.fixture.domain.FixtureBase;
import org.tsinghuatj.fixture.domain.FixturePurchaseApply;
import org.tsinghuatj.fixture.domain.FixturePurchaseApplyDetail;
import org.tsinghuatj.fixture.domain.FixturePurchaseReceipt;
import org.tsinghuatj.fixture.domain.FixturePurchaseReceiptDetail;
import org.tsinghuatj.fixture.domain.FixtureWaitCheck;
import org.tsinghuatj.fixture.repository.FixtureBaseMapper;
import org.tsinghuatj.fixture.repository.FixturePurchaseApplyDetailMapper;
import org.tsinghuatj.fixture.repository.FixturePurchaseApplyMapper;
import org.tsinghuatj.fixture.repository.FixturePurchaseReceiptDetailMapper;
import org.tsinghuatj.fixture.repository.FixturePurchaseReceiptMapper;
import org.tsinghuatj.fixture.repository.FixtureWaitCheckMapper;
import org.tsinghuatj.fixture.service.IFixturePurchaseApplyService;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.SysParam;
import org.tsinghuatj.sys.service.ISysMessageService;
import org.tsinghuatj.sys.service.ISysParamService;
import org.tsinghuatj.tool.domain.ToolApplyAudit;
import org.tsinghuatj.tool.repository.ToolAppendixMapper;
import org.tsinghuatj.tool.repository.ToolApplyAuditMapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import fr.opensagres.xdocreport.utils.StringUtils;

/**
 *
 * FixturePurchaseApply 表数据服务层接口实现类
 *
 */
@Service("fixturePurchaseApplyService")
public class FixturePurchaseApplyServiceImpl extends BaseServiceImpl implements IFixturePurchaseApplyService {

	private @Resource FixturePurchaseApplyMapper fixturePurchaseApplyMapper;
	private @Resource ToolApplyAuditMapper toolApplyAuditMapper;
	private @Resource ISysMessageService sysMessageService;
	private @Resource StaffMapper staffMapper;
	private @Resource ToolAppendixMapper appendixMapper;
	private @Resource FixturePurchaseReceiptMapper receiptMapper;
	private @Resource SupplierMapper supplierMapper;
	private @Resource FixtureBaseMapper fixtureBaseMapper;
	private @Resource FixtureWaitCheckMapper waitCheckMapper;
	private @Resource ISysParamService sysParamService;
	private @Resource FixturePurchaseApplyDetailMapper applyDetailMapper;
	private @Resource FixturePurchaseReceiptDetailMapper fixturePurchaseReceiptDetailMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, FixturePurchaseApply fixturePurchaseApply) throws BusinessException {
		Date date = new Date();
		Long applyId = getPkId();
		fixturePurchaseApply.setPkId(applyId);
		fixturePurchaseApply.setApplyTime(date);
		fixturePurchaseApply.setCreateTime(date);
		fixturePurchaseApply.setCreateUser(userId);
		fixturePurchaseApply.setUpdateTime(date);
		fixturePurchaseApply.setUpdateUser(userId);
		fixturePurchaseApply.setDelMark(0);
		List<FixturePurchaseApplyDetail> detailList = fixturePurchaseApply.getDetailList();
		if (detailList.size() > 0) {
			for (FixturePurchaseApplyDetail detail : detailList) {
				detail.setApplyId(applyId);
				detail.setPkId(getPkId());
				detail.setCreateTime(date);
				detail.setCreateUser(userId);
				detail.setUpdateTime(date);
				detail.setUpdateUser(userId);
				detail.setDelMark(0);
				applyDetailMapper.insert(detail);
			}
		}
		return fixturePurchaseApplyMapper.insert(fixturePurchaseApply);
	}

	private String createAvailableNumber(String fixtureNumber, Integer qty) {
		String availableNumber = "";
		FixtureBase fixtureBase = fixtureBaseMapper.selectByNumber(fixtureNumber, null);
		int maxSeq;
		if (StringUtils.isNotEmpty(fixtureBase.getMaxSeq())) {
			maxSeq = Integer.parseInt(fixtureBase.getMaxSeq());
		} else {
			maxSeq = 0;
		}
		for (int i = 1; i <= qty; i++) {
			++maxSeq;
			if (i == 1) {
				availableNumber = fixtureBase.getFixtureMap() + " " + maxSeq + "#";
			} else {
				availableNumber = availableNumber + ";" + fixtureBase.getFixtureMap() + " " + maxSeq + "#";
			}
		}
		fixtureBase.setMaxSeq(maxSeq + "");
		fixtureBaseMapper.updateActiveById(fixtureBase, fixtureBase.getPkId());
		return availableNumber;
	}

	private void createAllAvailableNumber(FixturePurchaseApply purchaseApply) {
		int qty = purchaseApply.getPurchaseQty();
		purchaseApply.setAvailableNumber(createAvailableNumber(purchaseApply.getFixtureNumber(), qty));
		// String parentSeq[] = purchaseApply.getAvailableNumber().split(";");
		if (purchaseApply.getFixtureType() == 1) {
			List<FixturePurchaseApplyDetail> list = applyDetailMapper.selectByApplyId(purchaseApply.getPkId());
			for (FixturePurchaseApplyDetail detail : list) {
				String allSeq = "";
				String childSeq[] = createAvailableNumber(detail.getFixtureNumber(), detail.getFixtureQty()).split(";");
				int index = detail.getFixtureQty() / qty;
				String chSeq = "";
				for (int i = 0; i < qty; i++) {
					chSeq = "";
					for (int j = 0; j < index; j++) {
						chSeq = chSeq + childSeq[index * i + j] + ";";
					}
					chSeq = chSeq.substring(0, chSeq.length() - 1);
					if(i==0){
						allSeq = allSeq + "[" + chSeq + "]";
					}else{
						allSeq = allSeq +" | "+ "[" + chSeq + "]";
					}
					
				}
				detail.setAvailableNumber(allSeq);
				applyDetailMapper.updateActiveByApplyId(detail);
			}
		}
	}

	/*
	 * private void createAllAvailableNumber(FixturePurchaseApply purchaseApply)
	 * { int qty = purchaseApply.getPurchaseQty();
	 * purchaseApply.setAvailableNumber(createAvailableNumber(purchaseApply.
	 * getFixtureNumber(), qty)); String parentSeq[] =
	 * purchaseApply.getAvailableNumber().split(";"); if
	 * (purchaseApply.getFixtureType() == 1) { List<FixturePurchaseApplyDetail>
	 * list = applyDetailMapper.selectByApplyId(purchaseApply.getPkId()); for
	 * (FixturePurchaseApplyDetail detail : list) { String allSeq = ""; String
	 * childSeq[] = createAvailableNumber(detail.getFixtureNumber(),
	 * detail.getFixtureQty()).split(";"); int index = detail.getFixtureQty() /
	 * qty; String chSeq = ""; for (int i = 0; i < qty; i++) { String parentseq
	 * = parentSeq[i]; chSeq=""; for (int j = 0; j < index; j++) { chSeq = chSeq
	 * + childSeq[index * i + j] + " "; } allSeq = allSeq + parentseq + "-[" +
	 * chSeq + "]"; } detail.setAvailableNumber(allSeq);
	 * applyDetailMapper.updateActiveByApplyId(detail); } } }
	 */

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, FixturePurchaseApply fixturePurchaseApply, Long applyId) throws BusinessException {
		fixturePurchaseApply.setUpdateTime(new Date());
		fixturePurchaseApply.setUpdateUser(userId);
		/*
		 * if (fixturePurchaseApply.getApplyStatus() == 1 &&
		 * StringUtil.isEmpty(fixturePurchaseApply.getAvailableNumber())) {
		 * fixturePurchaseApply.setAvailableNumber(createAvailableNumber(
		 * fixturePurchaseApply.getFixtureNumber(),
		 * fixturePurchaseApply.getPurchaseQty())); }
		 */
		Date date = new Date();
		List<FixturePurchaseApplyDetail> detailList = fixturePurchaseApply.getDetailList();
		if (null != detailList && detailList.size() > 0) {
			for (FixturePurchaseApplyDetail detail : detailList) {
				detail.setApplyId(applyId);
				detail.setCreateTime(date);
				detail.setCreateUser(userId);
				detail.setUpdateTime(date);
				detail.setUpdateUser(userId);
				detail.setDelMark(0);
				applyDetailMapper.updateActiveByApplyId(detail);
			}
		}
		return fixturePurchaseApplyMapper.updateActiveById(fixturePurchaseApply, applyId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixturePurchaseApply selectById(Long userId, Long applyId) throws BusinessException {
		FixturePurchaseApply apply = fixturePurchaseApplyMapper.selectById(applyId);
		if (apply.getFixtureType() == 1) {
			apply.setDetailList(applyDetailMapper.selectByApplyId(applyId));
		}
		apply.setAuditList(toolApplyAuditMapper.select(applyId));
		return apply;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long fixturePurchaseApplyId) throws BusinessException {
		return fixturePurchaseApplyMapper.removeById(fixturePurchaseApplyId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long fixturePurchaseApplyId) throws BusinessException {
		FixturePurchaseApply fixturePurchaseApply = new FixturePurchaseApply();
		fixturePurchaseApply.setPkId(fixturePurchaseApplyId);
		fixturePurchaseApply.setUpdateTime(new Date());
		fixturePurchaseApply.setUpdateUser(userId);
		return fixturePurchaseApplyMapper.deleteById(fixturePurchaseApply);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<FixturePurchaseApply> select(Long userId, FixturePurchaseApply fixturePurchaseApply) throws BusinessException {
		return fixturePurchaseApplyMapper.select(fixturePurchaseApply);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<FixturePurchaseApply> selectPageList(Long userId, FixturePurchaseApply fixturePurchaseApply, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<FixturePurchaseApply> page = fixturePurchaseApplyMapper.selectPageList(fixturePurchaseApply, queryDto);
		return new Pagination<FixturePurchaseApply>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer auditPurchaseApply(Long userId, String realName, Long pkId, Integer auditStatus, BigDecimal amount, String remark) throws BusinessException {
		Date date = new Date();
		FixturePurchaseApply purchaseApply = fixturePurchaseApplyMapper.selectById(pkId);
		Staff staff = staffMapper.selectByUserId(userId);
		String departName = staff.getDepartmentName();

		String message = "";
		String dt = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm");
		String authCode = "";
		purchaseApply.setUpdateTime(date);
		purchaseApply.setUpdateUser(userId);
		String auditResult = "";

		Integer purchaseType = purchaseApply.getPurchaseType();// 1.新品开发2.常用夹具
		// 待工艺部审核,如果新品夹具不需要工艺部审核,跳转到圆柱分厂审核
		if (auditStatus == 2) {
			auditResult = "分厂领导审核通过";
			message = departName + "-" + realName + "于" + dt + "审核了夹具申购单,夹具编码为" + purchaseApply.getFixtureNumber() + ",审核意见为同意采购,请您及时审核";
			authCode = "01050103";
			if (purchaseType == 1) {
				auditResult = "分厂领导审核通过";
				auditStatus = 3;
				authCode = "01050104";
			}
		} else if (auditStatus == 3) {
			auditResult = "工艺部审核通过";
			message = departName + "-" + realName + "于" + dt + "审核了夹具申购单,夹具编码为" + purchaseApply.getFixtureNumber() + ",审核意见为同意采购,请您及时接收";
			authCode = "01050104";
		} else if (auditStatus == 4) {
			purchaseApply.setProcurementType(1);
			createAllAvailableNumber(purchaseApply);
		} else if (auditStatus == 5) {
			purchaseApply.setProcurementType(2);
			auditResult = "装备分厂审核为外购";
			message = departName + "-" + realName + "于" + dt + "审核了夹具申购单,夹具编码为" + purchaseApply.getFixtureNumber() + ",审核意见为外购,请您及时接收";
			authCode = "01050105";
		} else if (auditStatus == 6) {
			// 采购询价
			purchaseApply.setAmount(amount);
			// 总价大于等于金额上限 需要主管领导审批
			SysParam sysParam = sysParamService.selectByParamKey(userId, "defaultPrice");
			if (amount.compareTo(new BigDecimal(sysParam.getParamValue())) >= 0) {
				auditStatus = 6;
				auditResult = "采购部询价";
				message = departName + "-" + realName + "于" + dt + "审核了夹具申购单,夹具编码为" + purchaseApply.getFixtureNumber() + ",请您及时审核";
				// 新品 常用
				if (purchaseType == 1) {
					authCode = "01050107";
				} else {
					authCode = "01050106";
				}
			} else {
				auditStatus = 7;
				auditResult = "采购部询价";
				message = departName + "-" + realName + "于" + dt + "审核了夹具申购单,夹具编码为" + purchaseApply.getFixtureNumber() + ",请您及时接收";
				authCode = "010502";
				createAllAvailableNumber(purchaseApply);
			}
		} else if (auditStatus == 7) {
			createAllAvailableNumber(purchaseApply);
			auditResult = "主管领导审核";
			message = departName + "-" + realName + "于" + dt + "审核了夹具申购单,夹具编码为" + purchaseApply.getFixtureNumber() + ",请您及时接收";
			authCode = "010502";
		} else if (auditStatus == 8) {
			auditResult = "采购已接收";
		} else if (auditStatus == 9) {
			auditResult = "装备分厂已接收";
		} else if (auditStatus == -1) {
			auditResult = "分厂领导审核,驳回";
			message = departName + "-" + realName + "于" + dt + "驳回了夹具申购单,夹具编码为" + purchaseApply.getFixtureNumber() + ",审核意见为不同意采购,请您及时处理";
			authCode = "01050101";
		} else if (auditStatus == -2) {
			auditResult = "工艺部审核未通过";
			message = departName + "-" + realName + "于" + dt + "审核了夹具申购单,夹具编码为" + purchaseApply.getFixtureNumber() + ",审核意见为不同意采购,请您及时接收";
			authCode = "01050101";
		} else if (auditStatus == -9) {
			// 总价大于等于金额上限 需要主管领导审批
			SysParam sysParam = sysParamService.selectByParamKey(userId, "defaultPrice");
			if (purchaseApply.getAmount().compareTo(new BigDecimal(sysParam.getParamValue())) >= 0) {
				auditStatus = 6;
				// 新品 常用
				if (purchaseType == 1) {
					authCode = "01050107";
				} else {
					authCode = "01050106";
				}
			} else {
				auditStatus = 5;
				authCode = "01050104";
			}
			auditResult = "采购部驳回";
			message = departName + "-" + realName + "于" + dt + "驳回了夹具申购单,夹具编码为" + purchaseApply.getFixtureNumber() + ",请您及时审核";
		}

		purchaseApply.setApplyStatus(auditStatus);
		fixturePurchaseApplyMapper.updateActiveById(purchaseApply, pkId);
		// 审核日志
		saveAudit(userId, staff.getDepartmentId(), staff.getDepartmentName(), realName, pkId, auditResult, remark);
		// 系统消息
		if (!"".equals(authCode)) {
			sysMessageService.insert("夹具申购", message, userId, realName, authCode);
		}
		return 1;
	}

	private void saveAudit(Long userId, Long departmentId, String departmentName, String realName, Long reportId, String auditResult, String remark) throws BusinessException {
		ToolApplyAudit applyAudit = new ToolApplyAudit();
		Date date = new Date();
		applyAudit.setPkId(getPkId());
		applyAudit.setCreateTime(date);
		applyAudit.setCreateUser(userId);
		applyAudit.setUpdateTime(date);
		applyAudit.setUpdateUser(userId);
		applyAudit.setDelMark(0);
		applyAudit.setApplyId(reportId);
		applyAudit.setApplyType(4);
		applyAudit.setAuditResult(auditResult);
		applyAudit.setAuditorId(userId);
		applyAudit.setAuditorName(realName);
		applyAudit.setAuditDepartmentId(departmentId);
		applyAudit.setAuditDepartmentName(departmentName);
		applyAudit.setRemark(remark);
		toolApplyAuditMapper.insert(applyAudit);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixturePurchaseApply selectByPkId(Long userId, Long pkId) throws BusinessException {
		FixturePurchaseApply apply = fixturePurchaseApplyMapper.selectById(pkId);
		if (apply.getFixtureType() == 1) {
			apply.setDetailList(applyDetailMapper.selectByApplyId(pkId));
		}
		apply.setReceiptList(receiptMapper.selectByApplyId(pkId));
		return apply;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updatePurchaseReceipt(Long userId, Long applyId, List<FixturePurchaseReceipt> receiptList) throws BusinessException {
		Date date = new Date();
		FixturePurchaseApply fixturePurchaseApply = fixturePurchaseApplyMapper.selectById(applyId);
		Integer procurementType = fixturePurchaseApply.getProcurementType();
		for (FixturePurchaseReceipt item : receiptList) {
			// 在检过程中的不允许修改
			if (null == item.getPkId()) {
				FixtureWaitCheck waitcheck = new FixtureWaitCheck();
				Long receiptId = getPkId();
				Long waitCheckId = getPkId();
				waitcheck.setPkId(waitCheckId);

				item.setPkId(receiptId);
				item.setApplyId(applyId);
				item.setWaitCheckId(waitCheckId);
				item.setProcurementType(procurementType);
				item.setFixtureNumber(fixturePurchaseApply.getFixtureNumber());
				item.setFixtureName(fixturePurchaseApply.getFixtureName());
				item.setFixtureMap(fixturePurchaseApply.getFixtureMap());
				item.setFixtureType(fixturePurchaseApply.getFixtureType());
				if (procurementType == 2) {
					Supplier supplier = supplierMapper.selectById(item.getSupplierId());
					if (null != supplier) {
						item.setSupplierName(supplier.getSupplierName());
						waitcheck.setSupplierId(item.getSupplierId());
						waitcheck.setSupplierName(supplier.getSupplierName());
					}
				}
				insertReceiptDetail(userId, item);

				// 配件明细
				if (fixturePurchaseApply.getFixtureType() == 1) {
					String[] parentBarcode = fixturePurchaseApply.getAvailableNumber().split(";");
					int index = getIndex(parentBarcode, item.getFixtureBarcode());
					List<FixturePurchaseApplyDetail> detailList = applyDetailMapper.selectByApplyId(applyId);
					for (FixturePurchaseApplyDetail detail : detailList) {
						String[] childBarcode = detail.getAvailableNumber().split(" \\| ");
						String childBarcodes = childBarcode[index];
						String[] detailBarcode = childBarcodes.substring(1, childBarcodes.length() - 1).split(";");
						for (String detailcode : detailBarcode) {
							FixturePurchaseReceiptDetail receiptDetail = new FixturePurchaseReceiptDetail();
							receiptDetail.setFixtureBarcode(detailcode);
							String fixtureMap = detailcode.substring(0, detailcode.indexOf(" "));
							FixtureBase fixtureBase = fixtureBaseMapper.selectByFixtureMap(fixtureMap);
							receiptDetail.setFixtureNumber(fixtureBase.getFixtureNumber());
							receiptDetail.setReceiptId(receiptId);
							insertReceiptDetail(userId, receiptDetail);
						}
					}
				}

				// 待检
				waitcheck.setFixtureNumber(fixturePurchaseApply.getFixtureNumber());
				waitcheck.setFixtureName(fixturePurchaseApply.getFixtureName());
				waitcheck.setFixtureMap(fixturePurchaseApply.getFixtureMap());
				waitcheck.setFixtureBarcode(item.getFixtureBarcode());
				insertFixtureWaitCheck(userId, waitcheck);

			} else if (item.getCheckStatus() == 0) {
				if (null != item.getPkId()) {
					item.setUpdateTime(date);
					item.setUpdateUser(userId);
					Supplier supplier = supplierMapper.selectById(item.getSupplierId());
					if (null != supplier) {
						item.setSupplierName(supplier.getSupplierName());
					}
					receiptMapper.updateActiveById(item, item.getPkId());
				}
			}
		}

		int qty = receiptMapper.selectCountByApplyId(applyId);
		fixturePurchaseApply.setArrivedQty(qty);
		if (qty == fixturePurchaseApply.getPurchaseQty()) {
			fixturePurchaseApply.setFinishTime(date);
			fixturePurchaseApply.setApplyStatus(10);
		}

		fixturePurchaseApply.setUpdateTime(date);
		fixturePurchaseApply.setUpdateUser(userId);
		fixturePurchaseApplyMapper.updateActiveById(fixturePurchaseApply, applyId);
		return 1;
	}

	private int getIndex(String[] arr, String value) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(value)) {
				return i;
			}
		}
		return -1;// 如果未找到返回-1
	}

	// 收货明细
	private void insertReceiptDetail(Long userId, FixturePurchaseReceipt purchaseReceipt) throws BusinessException {
		purchaseReceipt.setCreateUser(userId);
		purchaseReceipt.setCreateTime(new Date());
		purchaseReceipt.setUpdateTime(new Date());
		purchaseReceipt.setDelMark(0);
		purchaseReceipt.setUpdateUser(userId);
		receiptMapper.insert(purchaseReceipt);
	}

	// 收货明细配件
	private void insertReceiptDetail(Long userId, FixturePurchaseReceiptDetail fixturePurchaseReceiptDetail) throws BusinessException {
		fixturePurchaseReceiptDetail.setPkId(getPkId());
		fixturePurchaseReceiptDetail.setCreateTime(new Date());
		fixturePurchaseReceiptDetail.setCreateUser(userId);
		fixturePurchaseReceiptDetail.setUpdateTime(new Date());
		fixturePurchaseReceiptDetail.setUpdateUser(userId);
		fixturePurchaseReceiptDetail.setDelMark(0);
		fixturePurchaseReceiptDetailMapper.insert(fixturePurchaseReceiptDetail);
	}

	// 待检夹具
	private void insertFixtureWaitCheck(Long userId, FixtureWaitCheck waitCheck) throws BusinessException {
		waitCheck.setSetCheckTime(new Date());
		waitCheck.setCheckStatus(0);
		waitCheck.setCheckType(4);
		waitCheck.setCreateUser(userId);
		waitCheck.setCreateTime(new Date());
		waitCheck.setUpdateTime(new Date());
		waitCheck.setUpdateUser(userId);
		waitCheck.setDelMark(0);
		waitCheckMapper.insert(waitCheck);
	}

}