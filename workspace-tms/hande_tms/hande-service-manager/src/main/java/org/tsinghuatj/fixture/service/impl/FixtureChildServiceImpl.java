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
import org.tsinghuatj.fixture.domain.FixtureChild;
import org.tsinghuatj.fixture.repository.FixtureChildMapper;
import org.tsinghuatj.fixture.service.IFixtureChildService;


/**
 *
 * FixtureChild 表数据服务层接口实现类
 *
 */
@Service("fixtureChildService")
public class FixtureChildServiceImpl extends BaseServiceImpl implements IFixtureChildService {

	private @Resource FixtureChildMapper fixtureChildMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, FixtureChild fixtureChild) throws BusinessException {	
		fixtureChild.setPkId(getPkId());
		fixtureChild.setCreateTime(new Date());
		fixtureChild.setCreateUser(userId);
		fixtureChild.setUpdateTime(new Date());
		fixtureChild.setUpdateUser(userId);
		fixtureChild.setDelMark(0);
		return fixtureChildMapper.insert(fixtureChild);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, FixtureChild fixtureChild, Long fixtureChildId) throws BusinessException {
		fixtureChild.setUpdateTime(new Date());
		fixtureChild.setUpdateUser(userId);
		return fixtureChildMapper.updateActiveById(fixtureChild, fixtureChildId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixtureChild selectById(Long userId, Long fixtureChildId) throws BusinessException {
		return fixtureChildMapper.selectById(fixtureChildId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long fixtureChildId) throws BusinessException {
		return fixtureChildMapper.removeById(fixtureChildId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long fixtureChildId) throws BusinessException {
		FixtureChild fixtureChild = new FixtureChild();
		fixtureChild.setPkId(fixtureChildId);
		fixtureChild.setUpdateTime(new Date());
		fixtureChild.setUpdateUser(userId);
		return fixtureChildMapper.deleteById(fixtureChild);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<FixtureChild> select(Long userId, FixtureChild fixtureChild) throws BusinessException {		
		return fixtureChildMapper.select(fixtureChild);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<FixtureChild> selectPageList(Long userId, FixtureChild fixtureChild,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<FixtureChild> page = fixtureChildMapper.selectPageList(fixtureChild, queryDto);
		return new Pagination<FixtureChild>(page.getTotal(), page.getResult());		
	}
}