package org.tsinghuatj.mould.service.impl;

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
import org.tsinghuatj.mould.domain.MouldCheckItem;
import org.tsinghuatj.mould.repository.MouldCheckItemMapper;
import org.tsinghuatj.mould.service.IMouldCheckItemService;


/**
 *
 * MouldCheckItem 表数据服务层接口实现类
 *
 */
@Service("mouldCheckItemService")
public class MouldCheckItemServiceImpl extends BaseServiceImpl implements IMouldCheckItemService {

	private @Resource MouldCheckItemMapper mouldCheckItemMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MouldCheckItem mouldCheckItem) throws BusinessException {	
		mouldCheckItem.setPkId(getPkId());
		mouldCheckItem.setCreateTime(new Date());
		mouldCheckItem.setCreateUser(userId);
		mouldCheckItem.setUpdateTime(new Date());
		mouldCheckItem.setUpdateUser(userId);
		mouldCheckItem.setDelMark(0);
		return mouldCheckItemMapper.insert(mouldCheckItem);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MouldCheckItem mouldCheckItem, Long mouldCheckItemId) throws BusinessException {
		mouldCheckItem.setUpdateTime(new Date());
		mouldCheckItem.setUpdateUser(userId);
		return mouldCheckItemMapper.updateActiveById(mouldCheckItem, mouldCheckItemId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MouldCheckItem selectById(Long userId, Long mouldCheckItemId) throws BusinessException {
		return mouldCheckItemMapper.selectById(mouldCheckItemId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long mouldCheckItemId) throws BusinessException {
		return mouldCheckItemMapper.removeById(mouldCheckItemId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long mouldCheckItemId) throws BusinessException {
		MouldCheckItem mouldCheckItem = new MouldCheckItem();
		mouldCheckItem.setPkId(mouldCheckItemId);
		mouldCheckItem.setUpdateTime(new Date());
		mouldCheckItem.setUpdateUser(userId);
		return mouldCheckItemMapper.deleteById(mouldCheckItem);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MouldCheckItem> select(Long userId, MouldCheckItem mouldCheckItem) throws BusinessException {		
		return mouldCheckItemMapper.select(mouldCheckItem);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MouldCheckItem> selectPageList(Long userId, MouldCheckItem mouldCheckItem,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MouldCheckItem> page = mouldCheckItemMapper.selectPageList(mouldCheckItem, queryDto);
		return new Pagination<MouldCheckItem>(page.getTotal(), page.getResult());		
	}
}