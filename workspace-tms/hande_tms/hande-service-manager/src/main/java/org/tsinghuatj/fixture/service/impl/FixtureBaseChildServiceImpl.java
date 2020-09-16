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
import org.tsinghuatj.fixture.domain.FixtureBaseChild;
import org.tsinghuatj.fixture.repository.FixtureBaseChildMapper;
import org.tsinghuatj.fixture.service.IFixtureBaseChildService;


/**
 *
 * FixtureBaseChild 表数据服务层接口实现类
 *
 */
@Service("fixtureBaseChildService")
public class FixtureBaseChildServiceImpl extends BaseServiceImpl implements IFixtureBaseChildService {

	private @Resource FixtureBaseChildMapper fixtureBaseChildMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, FixtureBaseChild fixtureBaseChild) throws BusinessException {	
		fixtureBaseChild.setPkId(getPkId());
		fixtureBaseChild.setCreateTime(new Date());
		fixtureBaseChild.setCreateUser(userId);
		fixtureBaseChild.setUpdateTime(new Date());
		fixtureBaseChild.setUpdateUser(userId);
		fixtureBaseChild.setDelMark(0);
		return fixtureBaseChildMapper.insert(fixtureBaseChild);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, FixtureBaseChild fixtureBaseChild, Long fixtureBaseChildId) throws BusinessException {
		fixtureBaseChild.setUpdateTime(new Date());
		fixtureBaseChild.setUpdateUser(userId);
		return fixtureBaseChildMapper.updateActiveById(fixtureBaseChild, fixtureBaseChildId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixtureBaseChild selectById(Long userId, Long fixtureBaseChildId) throws BusinessException {
		return fixtureBaseChildMapper.selectById(fixtureBaseChildId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long fixtureBaseChildId) throws BusinessException {
		return fixtureBaseChildMapper.removeById(fixtureBaseChildId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long fixtureBaseChildId) throws BusinessException {
		FixtureBaseChild fixtureBaseChild = new FixtureBaseChild();
		fixtureBaseChild.setPkId(fixtureBaseChildId);
		fixtureBaseChild.setUpdateTime(new Date());
		fixtureBaseChild.setUpdateUser(userId);
		return fixtureBaseChildMapper.deleteById(fixtureBaseChild);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<FixtureBaseChild> select(Long userId, FixtureBaseChild fixtureBaseChild) throws BusinessException {		
		return fixtureBaseChildMapper.select(fixtureBaseChild);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<FixtureBaseChild> selectPageList(Long userId, FixtureBaseChild fixtureBaseChild,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<FixtureBaseChild> page = fixtureBaseChildMapper.selectPageList(fixtureBaseChild, queryDto);
		return new Pagination<FixtureBaseChild>(page.getTotal(), page.getResult());		
	}
}