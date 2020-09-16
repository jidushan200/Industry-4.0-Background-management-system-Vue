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
import org.tsinghuatj.fixture.domain.FixtureWaitCheck;
import org.tsinghuatj.fixture.repository.FixtureWaitCheckMapper;
import org.tsinghuatj.fixture.service.IFixtureWaitCheckService;


/**
 *
 * FixtureWaitCheck 表数据服务层接口实现类
 *
 */
@Service("fixtureWaitCheckService")
public class FixtureWaitCheckServiceImpl extends BaseServiceImpl implements IFixtureWaitCheckService {

	private @Resource FixtureWaitCheckMapper fixtureWaitCheckMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, FixtureWaitCheck fixtureWaitCheck) throws BusinessException {	
		fixtureWaitCheck.setPkId(getPkId());
		fixtureWaitCheck.setCreateTime(new Date());
		fixtureWaitCheck.setCreateUser(userId);
		fixtureWaitCheck.setUpdateTime(new Date());
		fixtureWaitCheck.setUpdateUser(userId);
		fixtureWaitCheck.setDelMark(0);
		return fixtureWaitCheckMapper.insert(fixtureWaitCheck);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, FixtureWaitCheck fixtureWaitCheck, Long fixtureWaitCheckId) throws BusinessException {
		fixtureWaitCheck.setUpdateTime(new Date());
		fixtureWaitCheck.setUpdateUser(userId);
		return fixtureWaitCheckMapper.updateActiveById(fixtureWaitCheck, fixtureWaitCheckId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixtureWaitCheck selectById(Long userId, Long fixtureWaitCheckId) throws BusinessException {
		return fixtureWaitCheckMapper.selectById(fixtureWaitCheckId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long fixtureWaitCheckId) throws BusinessException {
		return fixtureWaitCheckMapper.removeById(fixtureWaitCheckId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long fixtureWaitCheckId) throws BusinessException {
		FixtureWaitCheck fixtureWaitCheck = new FixtureWaitCheck();
		fixtureWaitCheck.setPkId(fixtureWaitCheckId);
		fixtureWaitCheck.setUpdateTime(new Date());
		fixtureWaitCheck.setUpdateUser(userId);
		return fixtureWaitCheckMapper.deleteById(fixtureWaitCheck);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<FixtureWaitCheck> select(Long userId, FixtureWaitCheck fixtureWaitCheck) throws BusinessException {		
		return fixtureWaitCheckMapper.select(fixtureWaitCheck);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<FixtureWaitCheck> selectPageList(Long userId, FixtureWaitCheck fixtureWaitCheck,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<FixtureWaitCheck> page = fixtureWaitCheckMapper.selectPageList(fixtureWaitCheck, queryDto);
		return new Pagination<FixtureWaitCheck>(page.getTotal(), page.getResult());		
	}
}