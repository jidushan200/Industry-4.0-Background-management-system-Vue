package org.tsinghuatj.fixture.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.base.domain.Supplier;
import org.tsinghuatj.base.repository.SupplierMapper;
import org.tsinghuatj.fixture.domain.Fixture;
import org.tsinghuatj.fixture.domain.FixtureBase;
import org.tsinghuatj.fixture.domain.FixtureCheck;
import org.tsinghuatj.fixture.domain.FixtureCheckResult;
import org.tsinghuatj.fixture.domain.FixturePurchaseReceipt;
import org.tsinghuatj.fixture.domain.FixtureWaitCheck;
import org.tsinghuatj.fixture.repository.FixtureBaseMapper;
import org.tsinghuatj.fixture.repository.FixtureCheckMapper;
import org.tsinghuatj.fixture.repository.FixtureCheckResultMapper;
import org.tsinghuatj.fixture.repository.FixtureMapper;
import org.tsinghuatj.fixture.repository.FixturePurchaseReceiptMapper;
import org.tsinghuatj.fixture.repository.FixtureWaitCheckMapper;
import org.tsinghuatj.fixture.service.IFixtureCheckService;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.repository.SysParamMapper;
import org.tsinghuatj.tool.domain.ToolAppendix;
import org.tsinghuatj.tool.repository.ToolAppendixMapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;

/**
 *
 * FixtureCheck 表数据服务层接口实现类
 *
 */
@Service("fixtureCheckService")
public class FixtureCheckServiceImpl extends BaseServiceImpl implements IFixtureCheckService {
	private @Resource FixtureCheckResultMapper fixtureCheckResultMapper;
	private @Resource FixtureCheckMapper fixtureCheckMapper;
	private @Resource ToolAppendixMapper appendixMapper;
	private @Resource FixturePurchaseReceiptMapper receiptMapper;
	private @Resource FixtureBaseMapper fixtureBaseMapper;
	private @Resource SupplierMapper supplierMapper;
	private @Resource FixtureMapper fixtureMapper;
	private @Resource FixtureWaitCheckMapper waitCheckMapper;
	private @Resource SysParamMapper sysParamMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, FixtureCheck fixtureCheck, Long waitCheckId, String appendixIds) throws BusinessException {
		Long checkId = getPkId();
		Date date = new Date();
		fixtureCheck.setPkId(checkId);
		fixtureCheck.setCreateTime(new Date());
		fixtureCheck.setCreateUser(userId);
		fixtureCheck.setUpdateTime(new Date());
		fixtureCheck.setUpdateUser(userId);
		fixtureCheck.setDelMark(0);

		List<FixtureCheckResult> resultList = fixtureCheck.getResultList();
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
			fixtureCheckResultMapper.insert(result);
		});
		if (fixtureCheck.getCheckStatus() == 1) {
			// 新夹具质检
			if (fixtureCheck.getCheckType() == 4) {
				// 新夹具
				FixtureWaitCheck fixtureWaitCheck = waitCheckMapper.selectById(waitCheckId);
				fixtureWaitCheck.setCheckStatus(1);
				waitCheckMapper.updateActiveById(fixtureWaitCheck, waitCheckId);
				if (fixtureCheck.getCheckResult() == 1) {
					// 把采购收货单改为待入库
					receiptMapper.updateByWaitCheckId(1,1,waitCheckId);
				} else {
					// 把采购收货单改为待退货
					receiptMapper.updateByWaitCheckId(1,2,waitCheckId);
				}
			} else if (fixtureCheck.getCheckType() == 5) {
				// 刃磨质检
				FixtureWaitCheck fixtureWaitCheck = waitCheckMapper.selectById(waitCheckId);
				fixtureWaitCheck.setCheckStatus(1);
				waitCheckMapper.updateActiveById(fixtureWaitCheck, waitCheckId);

				Fixture fixture = fixtureMapper.selectByFixtureBarcode(fixtureCheck.getFixtureBarcode());
				fixture.setFixtureStatus(5);
				fixture.setInUse(2);
				fixtureCheck.setFixtureId(fixture.getPkId());
				fixtureMapper.updateActiveById(fixture, fixture.getPkId());

			} else if (fixtureCheck.getCheckType() == 6) {
				// 点检
				Fixture fixture = fixtureMapper.selectByFixtureBarcode(fixtureCheck.getFixtureBarcode());
				fixtureCheck.setFixtureId(fixture.getPkId());
				if(null!=fixture.getSpotTimes()){
					fixture.setSpotTimes(fixture.getSpotTimes() + 1);
				}else{
					fixture.setSpotTimes(1);
				}				
				fixture.setLastSpotTime(new Date());
				// 如果点检不合格状态改为待修磨
				if (fixtureCheck.getCheckResult() != 1) {
					fixture.setFixtureStatus(2);
					fixture.setSetRepairTime(new Date());
				}
				fixtureMapper.updateActiveById(fixture, fixture.getPkId());
			}
		}

		fixtureCheckMapper.insert(fixtureCheck);
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
			toolAppendix.setAppdenixType(5);
			appendixMapper.updateActiveById(toolAppendix, appendixId);
		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, FixtureCheck fixtureCheck, Long fixtureCheckId, String appendixIds) throws BusinessException {
		Date date = new Date();
		fixtureCheck.setUpdateTime(date);
		fixtureCheck.setUpdateUser(userId);
		fixtureCheckMapper.updateActiveById(fixtureCheck, fixtureCheckId);

		List<FixtureCheckResult> resultList = fixtureCheck.getResultList();
		resultList.forEach(result -> {
			result.setUpdateTime(date);
			result.setUpdateUser(userId);
			fixtureCheckResultMapper.updateActiveById(result, result.getPkId());
		});

		// 更新附件
		if (fixtureCheck.getCheckStatus() == 1) {
			if (fixtureCheck.getCheckType() == 4 || fixtureCheck.getCheckType() == 5) {
				// 新夹具 夹具修磨
				FixtureWaitCheck fixtureWaitCheck = waitCheckMapper.selectById(fixtureCheck.getWaitCheckId());
				fixtureWaitCheck.setCheckStatus(1);
				waitCheckMapper.updateActiveById(fixtureWaitCheck, fixtureCheck.getWaitCheckId());

			}
			if (fixtureCheck.getCheckType() == 5 || fixtureCheck.getCheckType() == 6) {
				// 点检
				if (fixtureCheck.getCheckStatus() == 1) {
					Fixture fixture = fixtureMapper.selectByFixtureBarcode(fixtureCheck.getFixtureBarcode());
					fixture.setSpotTimes(fixture.getSpotTimes() + 1);
					fixture.setLastSpotTime(new Date());
					fixtureMapper.updateActiveById(fixture, fixture.getPkId());
				}
			}
		}

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
			toolAppendix.setCheckId(fixtureCheck.getPkId());
			toolAppendix.setAppdenixType(5);
			appendixMapper.updateActiveById(toolAppendix, appendixId);
		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixtureCheck selectById(Long userId, Long fixtureCheckId) throws BusinessException {
		FixtureCheck fixtureCheck = fixtureCheckMapper.selectById(fixtureCheckId);

		/*
		 * FixtureBase fixtureBase =
		 * fixtureBaseMapper.selectByNumber(fixtureCheck.getFixtureNumber(),
		 * null); fixtureCheck.setFixtureNumber(fixtureBase.getFixtureNumber());
		 * fixtureCheck.setFixtureName(fixtureBase.getFixtureName());
		 * fixtureCheck.setFixtureMap(fixtureBase.getFixtureMap());
		 */
		if (null != fixtureCheck.getSupplierId()) {
			Supplier supplier = supplierMapper.selectById(fixtureCheck.getSupplierId());
			// toolCheck.setSupplierCode(supplier.getSupplierCode());
			if (null != supplier) {
				fixtureCheck.setSupplierName(supplier.getSupplierName());
			}
		}
		fixtureCheck.setResultList(fixtureCheckResultMapper.selectByCheckId(fixtureCheckId));
		fixtureCheck.setAppendixList(appendixMapper.selectByCheckId(fixtureCheckId));
		return fixtureCheck;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long fixtureCheckId) throws BusinessException {
		return fixtureCheckMapper.removeById(fixtureCheckId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long fixtureCheckId, Integer checkType) throws BusinessException {
		FixtureCheck fixtureCheck = fixtureCheckMapper.selectById(fixtureCheckId);
		fixtureCheck.setPkId(fixtureCheckId);
		fixtureCheck.setUpdateTime(new Date());
		fixtureCheck.setUpdateUser(userId);
		if (checkType == 4 || checkType == 5) {
			FixtureWaitCheck waitcheck = new FixtureWaitCheck();
			waitcheck.setPkId(fixtureCheck.getWaitCheckId());
			waitcheck.setCheckStatus(0);
			waitcheck.setUpdateUser(userId);
			waitCheckMapper.updateActiveById(waitcheck, waitcheck.getPkId());
		}
		return fixtureCheckMapper.deleteById(fixtureCheck);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<FixtureCheck> select(Long userId, FixtureCheck fixtureCheck) throws BusinessException {
		return fixtureCheckMapper.select(fixtureCheck);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<FixtureCheck> selectPageList(Long userId, FixtureCheck fixtureCheck, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<FixtureCheck> page = fixtureCheckMapper.selectPageList(fixtureCheck, queryDto);

		List<String> numberList = new ArrayList<String>();
		for (FixtureCheck item : page.getResult()) {
			numberList.add(item.getFixtureNumber());
		}
		if (numberList.size() > 0) {
			numberList = numberList.stream().distinct().collect(Collectors.toList());
			List<FixtureBase> tbList = fixtureBaseMapper.selectByNumberList(numberList);
			for (FixtureCheck item : page.getResult()) {
				for (FixtureBase tb : tbList) {
					if (item.getFixtureNumber().equals(tb.getFixtureNumber())) {
						item.setFixtureName(tb.getFixtureName());
						item.setFixtureMap(tb.getFixtureMap());
						break;
					}
				}
			}
		}
		return new Pagination<FixtureCheck>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<FixturePurchaseReceipt> selectWaitCheckPageList(Long userId, FixturePurchaseReceipt receipt, QueryDto queryDto) {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<FixturePurchaseReceipt> page = receiptMapper.selectPageList(receipt, queryDto);
		return new Pagination<FixturePurchaseReceipt>(page.getTotal(), page.getResult());
	}

	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer fixtureTakeBack(Long userId, Long fixtureCheckId, Long fixtureId, String staffName) throws BusinessException {
		FixtureCheck fixtureCheck = new FixtureCheck();
		fixtureCheck.setPkId(fixtureCheckId);
		fixtureCheck.setHandleResult(1);
		fixtureCheck.setUpdateUser(userId);
		fixtureCheck.setUpdateTime(new Date());
		fixtureCheckMapper.updateActiveById(fixtureCheck, fixtureCheckId);

		Fixture fixture = new Fixture();
		fixture.setPkId(fixtureId);
		fixture.setFixtureStatus(1);
		fixtureMapper.updateActiveById(fixture, fixtureId);
		return 1;
	}
}