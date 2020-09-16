package org.tsinghuatj.fixture.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.fixture.domain.FixturePurchaseApplyDetail;
import org.tsinghuatj.fixture.repository.FixturePurchaseApplyDetailMapper;
import org.tsinghuatj.fixture.service.IFixturePurchaseApplyDetailService;


/**
 *
 * FixturePurchaseApplyDetail 表数据服务层接口实现类
 *
 */
@Service("fixturePurchaseApplyDetailService")
public class FixturePurchaseApplyDetailServiceImpl extends BaseServiceImpl implements IFixturePurchaseApplyDetailService {

	private @Resource FixturePurchaseApplyDetailMapper fixturePurchaseApplyDetailMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, FixturePurchaseApplyDetail fixturePurchaseApplyDetail) throws BusinessException {	
		fixturePurchaseApplyDetail.setPkId(getPkId());
		fixturePurchaseApplyDetail.setCreateTime(new Date());
		fixturePurchaseApplyDetail.setCreateUser(userId);
		fixturePurchaseApplyDetail.setUpdateTime(new Date());
		fixturePurchaseApplyDetail.setUpdateUser(userId);
		fixturePurchaseApplyDetail.setDelMark(0);
		return fixturePurchaseApplyDetailMapper.insert(fixturePurchaseApplyDetail);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, FixturePurchaseApplyDetail fixturePurchaseApplyDetail, Long fixturePurchaseApplyDetailId) throws BusinessException {
		fixturePurchaseApplyDetail.setUpdateTime(new Date());
		fixturePurchaseApplyDetail.setUpdateUser(userId);
		return fixturePurchaseApplyDetailMapper.updateActiveById(fixturePurchaseApplyDetail, fixturePurchaseApplyDetailId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixturePurchaseApplyDetail selectById(Long userId, Long fixturePurchaseApplyDetailId) throws BusinessException {
		return fixturePurchaseApplyDetailMapper.selectById(fixturePurchaseApplyDetailId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long fixturePurchaseApplyDetailId) throws BusinessException {
		return fixturePurchaseApplyDetailMapper.removeById(fixturePurchaseApplyDetailId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long fixturePurchaseApplyDetailId) throws BusinessException {
		FixturePurchaseApplyDetail fixturePurchaseApplyDetail = new FixturePurchaseApplyDetail();
		fixturePurchaseApplyDetail.setPkId(fixturePurchaseApplyDetailId);
		fixturePurchaseApplyDetail.setUpdateTime(new Date());
		fixturePurchaseApplyDetail.setUpdateUser(userId);
		return fixturePurchaseApplyDetailMapper.deleteById(fixturePurchaseApplyDetail);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<FixturePurchaseApplyDetail> select(Long userId, FixturePurchaseApplyDetail fixturePurchaseApplyDetail) throws BusinessException {		
		return fixturePurchaseApplyDetailMapper.select(fixturePurchaseApplyDetail);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<FixturePurchaseApplyDetail> selectPageList(Long userId, FixturePurchaseApplyDetail fixturePurchaseApplyDetail,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<FixturePurchaseApplyDetail> page = fixturePurchaseApplyDetailMapper.selectPageList(fixturePurchaseApplyDetail, queryDto);
		return new Pagination<FixturePurchaseApplyDetail>(page.getTotal(), page.getResult());		
	}
}