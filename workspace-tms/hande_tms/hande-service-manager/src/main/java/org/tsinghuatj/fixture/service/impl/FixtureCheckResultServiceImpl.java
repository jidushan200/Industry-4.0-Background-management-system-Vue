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
import org.tsinghuatj.fixture.domain.FixtureCheckResult;
import org.tsinghuatj.fixture.repository.FixtureCheckResultMapper;
import org.tsinghuatj.fixture.service.IFixtureCheckResultService;


/**
 *
 * FixtureCheckResult 表数据服务层接口实现类
 *
 */
@Service("fixtureCheckResultService")
public class FixtureCheckResultServiceImpl extends BaseServiceImpl implements IFixtureCheckResultService {

	private @Resource FixtureCheckResultMapper fixtureCheckResultMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, FixtureCheckResult fixtureCheckResult) throws BusinessException {	
		fixtureCheckResult.setPkId(getPkId());
		fixtureCheckResult.setCreateTime(new Date());
		fixtureCheckResult.setCreateUser(userId);
		fixtureCheckResult.setUpdateTime(new Date());
		fixtureCheckResult.setUpdateUser(userId);
		fixtureCheckResult.setDelMark(0);
		return fixtureCheckResultMapper.insert(fixtureCheckResult);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, FixtureCheckResult fixtureCheckResult, Long fixtureCheckResultId) throws BusinessException {
		fixtureCheckResult.setUpdateTime(new Date());
		fixtureCheckResult.setUpdateUser(userId);
		return fixtureCheckResultMapper.updateActiveById(fixtureCheckResult, fixtureCheckResultId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixtureCheckResult selectById(Long userId, Long fixtureCheckResultId) throws BusinessException {
		return fixtureCheckResultMapper.selectById(fixtureCheckResultId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long fixtureCheckResultId) throws BusinessException {
		return fixtureCheckResultMapper.removeById(fixtureCheckResultId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long fixtureCheckResultId) throws BusinessException {
		FixtureCheckResult fixtureCheckResult = new FixtureCheckResult();
		fixtureCheckResult.setPkId(fixtureCheckResultId);
		fixtureCheckResult.setUpdateTime(new Date());
		fixtureCheckResult.setUpdateUser(userId);
		return fixtureCheckResultMapper.deleteById(fixtureCheckResult);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<FixtureCheckResult> select(Long userId, FixtureCheckResult fixtureCheckResult) throws BusinessException {		
		return fixtureCheckResultMapper.select(fixtureCheckResult);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<FixtureCheckResult> selectPageList(Long userId, FixtureCheckResult fixtureCheckResult,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<FixtureCheckResult> page = fixtureCheckResultMapper.selectPageList(fixtureCheckResult, queryDto);
		return new Pagination<FixtureCheckResult>(page.getTotal(), page.getResult());		
	}
}