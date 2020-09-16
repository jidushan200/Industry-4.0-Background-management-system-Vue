package org.tsinghuatj.tool.service.impl;

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
import org.tsinghuatj.tool.domain.ToolWarehouse;
import org.tsinghuatj.tool.repository.ToolWarehouseMapper;
import org.tsinghuatj.tool.service.IToolWarehouseService;


/**
 *
 * ToolWarehouse 表数据服务层接口实现类
 *
 */
@Service("toolWarehouseService")
public class ToolWarehouseServiceImpl extends BaseServiceImpl implements IToolWarehouseService {

	private @Resource ToolWarehouseMapper toolWarehouseMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolWarehouse toolWarehouse) throws BusinessException {	
		toolWarehouse.setPkId(getPkId());
		toolWarehouse.setCreateTime(new Date());
		toolWarehouse.setCreateUser(userId);
		toolWarehouse.setUpdateTime(new Date());
		toolWarehouse.setUpdateUser(userId);
		toolWarehouse.setDelMark(0);
	
		return toolWarehouseMapper.insert(toolWarehouse);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolWarehouse toolWarehouse, Long toolWarehouseId) throws BusinessException {
		toolWarehouse.setUpdateTime(new Date());
		toolWarehouse.setUpdateUser(userId);
		return toolWarehouseMapper.updateActiveById(toolWarehouse, toolWarehouseId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolWarehouse selectById(Long userId, Long toolWarehouseId) throws BusinessException {
		return toolWarehouseMapper.selectById(toolWarehouseId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolWarehouseId) throws BusinessException {
		return toolWarehouseMapper.removeById(toolWarehouseId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolWarehouseId) throws BusinessException {
		ToolWarehouse toolWarehouse = new ToolWarehouse();
		toolWarehouse.setPkId(toolWarehouseId);
		toolWarehouse.setUpdateTime(new Date());
		toolWarehouse.setUpdateUser(userId);
		return toolWarehouseMapper.deleteById(toolWarehouse);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolWarehouse> select(Long userId, ToolWarehouse toolWarehouse) throws BusinessException {		
		return toolWarehouseMapper.select(toolWarehouse);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolWarehouse> selectPageList(Long userId, ToolWarehouse toolWarehouse,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolWarehouse> page = toolWarehouseMapper.selectPageList(toolWarehouse, queryDto);
		return new Pagination<ToolWarehouse>(page.getTotal(), page.getResult());		
	}
}