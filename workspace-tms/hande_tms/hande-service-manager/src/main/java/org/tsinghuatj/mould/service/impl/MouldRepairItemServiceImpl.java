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
import org.tsinghuatj.mould.domain.MouldRepairItem;
import org.tsinghuatj.mould.repository.MouldRepairItemMapper;
import org.tsinghuatj.mould.service.IMouldRepairItemService;


/**
 *
 * MouldRepairItem 表数据服务层接口实现类
 *
 */
@Service("mouldRepairItemService")
public class MouldRepairItemServiceImpl extends BaseServiceImpl implements IMouldRepairItemService {

	private @Resource MouldRepairItemMapper mouldRepairItemMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MouldRepairItem mouldRepairItem) throws BusinessException {	
		mouldRepairItem.setPkId(getPkId());
		mouldRepairItem.setCreateTime(new Date());
		mouldRepairItem.setCreateUser(userId);
		mouldRepairItem.setUpdateTime(new Date());
		mouldRepairItem.setUpdateUser(userId);
		mouldRepairItem.setDelMark(0);
		return mouldRepairItemMapper.insert(mouldRepairItem);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MouldRepairItem mouldRepairItem, Long mouldRepairItemId) throws BusinessException {
		mouldRepairItem.setUpdateTime(new Date());
		mouldRepairItem.setUpdateUser(userId);
		return mouldRepairItemMapper.updateActiveById(mouldRepairItem, mouldRepairItemId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MouldRepairItem selectById(Long userId, Long mouldRepairItemId) throws BusinessException {
		return mouldRepairItemMapper.selectById(mouldRepairItemId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long mouldRepairItemId) throws BusinessException {
		return mouldRepairItemMapper.removeById(mouldRepairItemId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long mouldRepairItemId) throws BusinessException {
		MouldRepairItem mouldRepairItem = new MouldRepairItem();
		mouldRepairItem.setPkId(mouldRepairItemId);
		mouldRepairItem.setUpdateTime(new Date());
		mouldRepairItem.setUpdateUser(userId);
		return mouldRepairItemMapper.deleteById(mouldRepairItem);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MouldRepairItem> select(Long userId, MouldRepairItem mouldRepairItem) throws BusinessException {		
		return mouldRepairItemMapper.select(mouldRepairItem);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MouldRepairItem> selectPageList(Long userId, MouldRepairItem mouldRepairItem,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MouldRepairItem> page = mouldRepairItemMapper.selectPageList(mouldRepairItem, queryDto);
		return new Pagination<MouldRepairItem>(page.getTotal(), page.getResult());		
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MouldRepairItem> selectByRepairId(Long userId, Long repairId) throws BusinessException {
		return mouldRepairItemMapper.selectByRepairId(repairId);
	}
}