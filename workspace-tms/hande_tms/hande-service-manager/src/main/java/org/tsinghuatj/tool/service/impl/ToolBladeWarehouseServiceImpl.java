package org.tsinghuatj.tool.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolBlade;
import org.tsinghuatj.tool.domain.ToolBladeWarehouse;
import org.tsinghuatj.tool.domain.ToolWaitHandle;
import org.tsinghuatj.tool.repository.ToolBladeMapper;
import org.tsinghuatj.tool.repository.ToolBladeWarehouseMapper;
import org.tsinghuatj.tool.repository.ToolWaitHandleMapper;
import org.tsinghuatj.tool.service.IToolBladeWarehouseService;
import org.tsinghuatj.tool.service.IToolOperLogService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolBladeWarehouse 表数据服务层接口实现类
 *
 */
@Service("toolBladeWarehouseService")
public class ToolBladeWarehouseServiceImpl extends BaseServiceImpl implements IToolBladeWarehouseService {

	private @Resource ToolBladeMapper toolBladeMapper;
	private @Resource ToolBladeWarehouseMapper toolBladeWarehouseMapper;
	private @Resource ToolWaitHandleMapper toolWaitHandleMapper;
	private @Resource IToolOperLogService operLogService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, String realname, ToolBladeWarehouse toolBladeWarehouse, Long handleId) throws BusinessException {
		Date date = new Date();
		toolBladeWarehouse.setPkId(getPkId());
		toolBladeWarehouse.setCreateTime(date);
		toolBladeWarehouse.setCreateUser(userId);
		toolBladeWarehouse.setUpdateTime(date);
		toolBladeWarehouse.setUpdateUser(userId);
		toolBladeWarehouse.setDelMark(0);
		toolBladeWarehouseMapper.insert(toolBladeWarehouse);
		// 新刀条入库
		if (toolBladeWarehouse.getWarehouseType() == 1) {
			ToolBlade toolBlade = toolBladeMapper.selectByNumber(toolBladeWarehouse.getToolNumber(), toolBladeWarehouse.getDepartmentId());
			if (null == toolBlade) {
				toolBlade = new ToolBlade();
				toolBlade.setPkId(getPkId());
				toolBlade.setToolNumber(toolBladeWarehouse.getToolNumber());
				// toolBlade.setToolName(toolBladeWarehouse.getToolName());
				// toolBlade.setToolMap(toolBladeWarehouse.getToolMap());
				toolBlade.setInventoryQty(toolBladeWarehouse.getToolQty());
				toolBlade.setDepartmentId(toolBladeWarehouse.getDepartmentId());
				toolBlade.setCreateTime(date);
				toolBlade.setCreateUser(userId);
				toolBlade.setUpdateTime(date);
				toolBlade.setUpdateUser(userId);
				toolBlade.setDelMark(0);
				toolBladeMapper.insert(toolBlade);
			} else {
				toolBlade.setInventoryQty(toolBlade.getInventoryQty() + toolBladeWarehouse.getToolQty());
				toolBlade.setUpdateTime(date);
				toolBlade.setUpdateUser(userId);
				toolBladeMapper.updateActiveById(toolBlade, toolBlade.getPkId());
			}
			operLogService.insert(userId, 1, toolBlade.getToolNumber(), "", "入库人:" + realname + " 入库数量:" + toolBladeWarehouse.getToolQty(),"");

		}

		ToolWaitHandle waitHandle = new ToolWaitHandle();
		waitHandle.setPkId(handleId);
		waitHandle.setHandleResult(1);
		waitHandle.setUpdateTime(date);
		waitHandle.setUpdateUser(userId);
		toolWaitHandleMapper.updateActiveById(waitHandle, handleId);
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolBladeWarehouse toolBladeWarehouse, Long toolBladeWarehouseId) throws BusinessException {
		toolBladeWarehouse.setUpdateTime(new Date());
		toolBladeWarehouse.setUpdateUser(userId);
		return toolBladeWarehouseMapper.updateActiveById(toolBladeWarehouse, toolBladeWarehouseId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolBladeWarehouse selectById(Long userId, Long toolBladeWarehouseId) throws BusinessException {
		return toolBladeWarehouseMapper.selectById(toolBladeWarehouseId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolBladeWarehouseId) throws BusinessException {
		return toolBladeWarehouseMapper.removeById(toolBladeWarehouseId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolBladeWarehouseId) throws BusinessException {
		ToolBladeWarehouse toolBladeWarehouse = new ToolBladeWarehouse();
		toolBladeWarehouse.setPkId(toolBladeWarehouseId);
		toolBladeWarehouse.setUpdateTime(new Date());
		toolBladeWarehouse.setUpdateUser(userId);
		return toolBladeWarehouseMapper.deleteById(toolBladeWarehouse);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolBladeWarehouse> select(Long userId, ToolBladeWarehouse toolBladeWarehouse) throws BusinessException {
		return toolBladeWarehouseMapper.select(toolBladeWarehouse);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolBladeWarehouse> selectPageList(Long userId, ToolBladeWarehouse toolBladeWarehouse, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolBladeWarehouse> page = toolBladeWarehouseMapper.selectPageList(toolBladeWarehouse, queryDto);
		return new Pagination<ToolBladeWarehouse>(page.getTotal(), page.getResult());
	}
}