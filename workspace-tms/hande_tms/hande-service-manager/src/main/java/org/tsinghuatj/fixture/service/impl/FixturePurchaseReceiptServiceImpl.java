package org.tsinghuatj.fixture.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.fixture.domain.FixturePurchaseReceipt;
import org.tsinghuatj.fixture.domain.FixtureWaitCheck;
import org.tsinghuatj.fixture.repository.FixturePurchaseReceiptDetailMapper;
import org.tsinghuatj.fixture.repository.FixturePurchaseReceiptMapper;
import org.tsinghuatj.fixture.repository.FixtureWaitCheckMapper;
import org.tsinghuatj.fixture.service.IFixturePurchaseReceiptService;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * FixturePurchaseReceipt 表数据服务层接口实现类
 *
 */
@Service("fixturePurchaseReceiptService")
public class FixturePurchaseReceiptServiceImpl extends BaseServiceImpl implements IFixturePurchaseReceiptService {

	private @Resource FixturePurchaseReceiptMapper fixturePurchaseReceiptMapper;
	private @Resource FixturePurchaseReceiptDetailMapper receiptDetailMapper;
	private @Resource FixtureWaitCheckMapper fixtureWaitCheckMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, FixturePurchaseReceipt fixturePurchaseReceipt) throws BusinessException {
		fixturePurchaseReceipt.setPkId(getPkId());
		fixturePurchaseReceipt.setCreateTime(new Date());
		fixturePurchaseReceipt.setCreateUser(userId);
		fixturePurchaseReceipt.setUpdateTime(new Date());
		fixturePurchaseReceipt.setUpdateUser(userId);
		fixturePurchaseReceipt.setDelMark(0);
		return fixturePurchaseReceiptMapper.insert(fixturePurchaseReceipt);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, FixturePurchaseReceipt fixturePurchaseReceipt, Long fixturePurchaseReceiptId) throws BusinessException {
		fixturePurchaseReceipt.setUpdateTime(new Date());
		fixturePurchaseReceipt.setUpdateUser(userId);
		return fixturePurchaseReceiptMapper.updateActiveById(fixturePurchaseReceipt, fixturePurchaseReceiptId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixturePurchaseReceipt selectById(Long userId, Long receiptId) throws BusinessException {
		FixturePurchaseReceipt receipt = fixturePurchaseReceiptMapper.selectById(receiptId);
		receipt.setDetailList(receiptDetailMapper.selectByReceiptId(receiptId));
		return receipt;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long fixturePurchaseReceiptId) throws BusinessException {
		return fixturePurchaseReceiptMapper.removeById(fixturePurchaseReceiptId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long fixturePurchaseReceiptId) throws BusinessException {
		FixturePurchaseReceipt fixturePurchaseReceipt = fixturePurchaseReceiptMapper.selectById(fixturePurchaseReceiptId);
		fixturePurchaseReceipt.setUpdateTime(new Date());
		fixturePurchaseReceipt.setUpdateUser(userId);
		FixtureWaitCheck fixtureWaitCheck = new FixtureWaitCheck();
		fixtureWaitCheck.setPkId(fixturePurchaseReceipt.getWaitCheckId());
		fixtureWaitCheck.setUpdateTime(new Date());
		fixtureWaitCheck.setUpdateUser(userId);
		fixtureWaitCheckMapper.deleteById(fixtureWaitCheck);
		fixturePurchaseReceiptMapper.deleteDetailByReceiptId(fixturePurchaseReceiptId);
		return fixturePurchaseReceiptMapper.deleteById(fixturePurchaseReceipt);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<FixturePurchaseReceipt> select(Long userId, FixturePurchaseReceipt fixturePurchaseReceipt) throws BusinessException {
		return fixturePurchaseReceiptMapper.select(fixturePurchaseReceipt);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<FixturePurchaseReceipt> selectPageList(Long userId, FixturePurchaseReceipt fixturePurchaseReceipt, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<FixturePurchaseReceipt> page = fixturePurchaseReceiptMapper.selectPageList(fixturePurchaseReceipt, queryDto);
		return new Pagination<FixturePurchaseReceipt>(page.getTotal(), page.getResult());
	}
}