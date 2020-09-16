package org.tsinghuatj.measure.service.impl;

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
import org.tsinghuatj.measure.domain.MeasureWarehouse;
import org.tsinghuatj.measure.repository.MeasureWarehouseMapper;
import org.tsinghuatj.measure.service.IMeasureWarehouseService;


/**
 *
 * MeasureWarehouse 表数据服务层接口实现类
 *
 */
@Service("measureWarehouseService")
public class MeasureWarehouseServiceImpl extends BaseServiceImpl implements IMeasureWarehouseService {

	private @Resource MeasureWarehouseMapper measureWarehouseMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MeasureWarehouse measureWarehouse) throws BusinessException {	
		measureWarehouse.setPkId(getPkId());
		measureWarehouse.setCreateTime(new Date());
		measureWarehouse.setCreateUser(userId);
		measureWarehouse.setUpdateTime(new Date());
		measureWarehouse.setUpdateUser(userId);
		measureWarehouse.setDelMark(0);
		return measureWarehouseMapper.insert(measureWarehouse);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MeasureWarehouse measureWarehouse, Long measureWarehouseId) throws BusinessException {
		measureWarehouse.setUpdateTime(new Date());
		measureWarehouse.setUpdateUser(userId);
		return measureWarehouseMapper.updateActiveById(measureWarehouse, measureWarehouseId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MeasureWarehouse selectById(Long userId, Long measureWarehouseId) throws BusinessException {
		return measureWarehouseMapper.selectById(measureWarehouseId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long measureWarehouseId) throws BusinessException {
		return measureWarehouseMapper.removeById(measureWarehouseId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long measureWarehouseId) throws BusinessException {
		MeasureWarehouse measureWarehouse = new MeasureWarehouse();
		measureWarehouse.setPkId(measureWarehouseId);
		measureWarehouse.setUpdateTime(new Date());
		measureWarehouse.setUpdateUser(userId);
		return measureWarehouseMapper.deleteById(measureWarehouse);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MeasureWarehouse> select(Long userId, MeasureWarehouse measureWarehouse) throws BusinessException {		
		return measureWarehouseMapper.select(measureWarehouse);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MeasureWarehouse> selectPageList(Long userId, MeasureWarehouse measureWarehouse,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MeasureWarehouse> page = measureWarehouseMapper.selectPageList(measureWarehouse, queryDto);
		return new Pagination<MeasureWarehouse>(page.getTotal(), page.getResult());		
	}
}