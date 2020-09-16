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
import org.tsinghuatj.mould.domain.MouldWarehouse;
import org.tsinghuatj.mould.repository.MouldWarehouseMapper;
import org.tsinghuatj.mould.service.IMouldWarehouseService;


/**
 *
 * MouldWarehouse 表数据服务层接口实现类
 *
 */
@Service("mouldWarehouseService")
public class MouldWarehouseServiceImpl extends BaseServiceImpl implements IMouldWarehouseService {

	private @Resource MouldWarehouseMapper mouldWarehouseMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MouldWarehouse mouldWarehouse) throws BusinessException {	
		mouldWarehouse.setPkId(getPkId());
		mouldWarehouse.setCreateTime(new Date());
		mouldWarehouse.setCreateUser(userId);
		mouldWarehouse.setUpdateTime(new Date());
		mouldWarehouse.setUpdateUser(userId);
		mouldWarehouse.setDelMark(0);
		return mouldWarehouseMapper.insert(mouldWarehouse);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MouldWarehouse mouldWarehouse, Long mouldWarehouseId) throws BusinessException {
		mouldWarehouse.setUpdateTime(new Date());
		mouldWarehouse.setUpdateUser(userId);
		return mouldWarehouseMapper.updateActiveById(mouldWarehouse, mouldWarehouseId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MouldWarehouse selectById(Long userId, Long mouldWarehouseId) throws BusinessException {
		return mouldWarehouseMapper.selectById(mouldWarehouseId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long mouldWarehouseId) throws BusinessException {
		return mouldWarehouseMapper.removeById(mouldWarehouseId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long mouldWarehouseId) throws BusinessException {
		MouldWarehouse mouldWarehouse = new MouldWarehouse();
		mouldWarehouse.setPkId(mouldWarehouseId);
		mouldWarehouse.setUpdateTime(new Date());
		mouldWarehouse.setUpdateUser(userId);
		return mouldWarehouseMapper.deleteById(mouldWarehouse);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MouldWarehouse> select(Long userId, MouldWarehouse mouldWarehouse) throws BusinessException {		
		return mouldWarehouseMapper.select(mouldWarehouse);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MouldWarehouse> selectPageList(Long userId, MouldWarehouse mouldWarehouse,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MouldWarehouse> page = mouldWarehouseMapper.selectPageList(mouldWarehouse, queryDto);
		return new Pagination<MouldWarehouse>(page.getTotal(), page.getResult());		
	}
}