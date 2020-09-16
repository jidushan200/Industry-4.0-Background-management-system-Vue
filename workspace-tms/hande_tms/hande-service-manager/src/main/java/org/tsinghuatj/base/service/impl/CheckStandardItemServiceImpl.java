package org.tsinghuatj.base.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.base.domain.CheckStandardItem;
import org.tsinghuatj.base.repository.CheckStandardItemMapper;
import org.tsinghuatj.base.service.ICheckStandardItemService;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


/**
 *
 * CheckStandardItem 表数据服务层接口实现类
 *
 */
@Service("checkStandardItemService")
public class CheckStandardItemServiceImpl extends BaseServiceImpl implements ICheckStandardItemService {

	private @Resource CheckStandardItemMapper checkStandardItemMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, CheckStandardItem checkStandardItem) throws BusinessException {	
		checkStandardItem.setPkId(getPkId());
		checkStandardItem.setCreateTime(new Date());
		checkStandardItem.setCreateUser(userId);
		checkStandardItem.setUpdateTime(new Date());
		checkStandardItem.setUpdateUser(userId);
		checkStandardItem.setDelMark(0);
		return checkStandardItemMapper.insert(checkStandardItem);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, CheckStandardItem checkStandardItem, Long checkStandardItemId) throws BusinessException {
		checkStandardItem.setUpdateTime(new Date());
		checkStandardItem.setUpdateUser(userId);
		return checkStandardItemMapper.updateActiveById(checkStandardItem, checkStandardItemId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public CheckStandardItem selectById(Long userId, Long checkStandardItemId) throws BusinessException {
		return checkStandardItemMapper.selectById(checkStandardItemId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long checkStandardItemId) throws BusinessException {
		return checkStandardItemMapper.removeById(checkStandardItemId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long checkStandardItemId) throws BusinessException {
		CheckStandardItem checkStandardItem = new CheckStandardItem();
		checkStandardItem.setPkId(checkStandardItemId);
		checkStandardItem.setUpdateTime(new Date());
		checkStandardItem.setUpdateUser(userId);
		return checkStandardItemMapper.deleteById(checkStandardItem);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<CheckStandardItem> select(Long userId, CheckStandardItem checkStandardItem) throws BusinessException {		
		return checkStandardItemMapper.select(checkStandardItem);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<CheckStandardItem> selectPageList(Long userId, CheckStandardItem checkStandardItem,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<CheckStandardItem> page = checkStandardItemMapper.selectPageList(checkStandardItem, queryDto);
		return new Pagination<CheckStandardItem>(page.getTotal(), page.getResult());		
	}
}