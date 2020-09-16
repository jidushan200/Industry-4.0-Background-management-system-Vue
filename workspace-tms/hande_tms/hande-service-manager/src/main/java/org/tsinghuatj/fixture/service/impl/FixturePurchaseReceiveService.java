package org.tsinghuatj.fixture.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.fixture.domain.FixturePurchaseApply;
import org.tsinghuatj.fixture.repository.FixturePurchaseApplyMapper;
import org.tsinghuatj.fixture.service.IFixturePurchaseReceiveService;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service("fixturePurchaseReceiveService")
public class FixturePurchaseReceiveService extends BaseServiceImpl implements IFixturePurchaseReceiveService {
	private @Resource FixturePurchaseApplyMapper fixturePurchaseApplyMapper;

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<FixturePurchaseApply> selectPageList(Long userId, FixturePurchaseApply fixturePurchaseApply, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<FixturePurchaseApply> page = fixturePurchaseApplyMapper.selectPageList(fixturePurchaseApply, queryDto);
		return new Pagination<FixturePurchaseApply>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixturePurchaseApply selectById(Long userId, Long fixturePurchaseApplyId) throws BusinessException {
		return fixturePurchaseApplyMapper.selectById(fixturePurchaseApplyId);
	}

}
