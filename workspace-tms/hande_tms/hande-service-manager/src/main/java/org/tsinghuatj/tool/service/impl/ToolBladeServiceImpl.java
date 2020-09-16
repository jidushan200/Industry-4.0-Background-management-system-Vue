package org.tsinghuatj.tool.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.base.domain.Staff;
import org.tsinghuatj.base.domain.StaffDepartment;
import org.tsinghuatj.base.repository.StaffDepartmentMapper;
import org.tsinghuatj.base.repository.StaffMapper;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolBase;
import org.tsinghuatj.tool.domain.ToolBlade;
import org.tsinghuatj.tool.repository.ToolBaseMapper;
import org.tsinghuatj.tool.repository.ToolBladeMapper;
import org.tsinghuatj.tool.service.IToolBladeService;
import org.tsinghuatj.tool.service.IToolOperLogService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolBlade 表数据服务层接口实现类
 *
 */
@Service("toolBladeService")
public class ToolBladeServiceImpl extends BaseServiceImpl implements IToolBladeService {

	private @Resource ToolBladeMapper toolBladeMapper;
	private @Resource StaffMapper staffMapper;
	private @Resource StaffDepartmentMapper staffdepartmentMapper;
	private @Resource ToolBaseMapper toolBaseMapper;
	private @Resource IToolOperLogService operLogService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolBlade toolBlade) throws BusinessException {
		toolBlade.setPkId(getPkId());
		toolBlade.setCreateTime(new Date());
		toolBlade.setCreateUser(userId);
		toolBlade.setUpdateTime(new Date());
		toolBlade.setUpdateUser(userId);
		toolBlade.setDelMark(0);
		return toolBladeMapper.insert(toolBlade);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolBlade toolBlade, Long toolBladeId) throws BusinessException {
		toolBlade.setUpdateTime(new Date());
		toolBlade.setUpdateUser(userId);
		return toolBladeMapper.updateActiveById(toolBlade, toolBladeId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolBlade selectById(Long userId, Long toolBladeId) throws BusinessException {
		return toolBladeMapper.selectById(toolBladeId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolBladeId) throws BusinessException {
		return toolBladeMapper.removeById(toolBladeId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolBladeId) throws BusinessException {
		ToolBlade toolBlade = new ToolBlade();
		toolBlade.setPkId(toolBladeId);
		toolBlade.setUpdateTime(new Date());
		toolBlade.setUpdateUser(userId);
		return toolBladeMapper.deleteById(toolBlade);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolBlade> select(Long userId, ToolBlade toolBlade) throws BusinessException {
		return toolBladeMapper.select(toolBlade);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolBlade> selectPageList(Long userId, ToolBlade toolBlade, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolBlade> page = toolBladeMapper.selectPageList(toolBlade, queryDto);
		return new Pagination<ToolBlade>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolBlade selectByNumber(Long userId, String number) throws BusinessException {
		Staff staff = staffMapper.selectByUserId(userId);
		return toolBladeMapper.selectByNumber(number, staff.getDepartmentId());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer toolBladeImport(Long userId, List<ToolBlade> bladeList) throws BusinessException {
		Date date = new Date();
		List<StaffDepartment> dList = staffdepartmentMapper.select(new StaffDepartment());
		for (ToolBlade item : bladeList) {
			Long departmentId = getDepartmentId(item.getDepartmentName(), dList);
			ToolBlade toolBlade = toolBladeMapper.selectByNumber(item.getToolNumber(), departmentId);

			if (null != toolBlade) {
				toolBlade.setInventoryQty(item.getInventoryQty());
				toolBlade.setUseQty(item.getUseQty());
				toolBlade.setScrapQty(item.getScrapQty());
				toolBlade.setUpdateTime(date);
				toolBlade.setUpdateUser(userId);
				toolBladeMapper.updateActiveById(toolBlade, toolBlade.getPkId());
			} else {
				ToolBase toolBase = toolBaseMapper.selectByToolNumber(item.getToolNumber(), null);
				if (null != toolBase && toolBase.getTypeId() == 4) {
					item.setPkId(getPkId());
					item.setToolName(toolBase.getToolName());
					item.setToolMap(toolBase.getToolMap());
					item.setDepartmentId(departmentId);
					item.setCreateTime(new Date());
					item.setCreateUser(userId);
					item.setUpdateTime(new Date());
					item.setUpdateUser(userId);
					item.setDelMark(0);
					toolBladeMapper.insert(item);
				}
			}
			operLogService.insert(userId, 1, toolBlade.getToolNumber(), "", "", "入库数量(" + item.getInventoryQty() + ")" + "刀条期初台账导入");

		}

		return 1;
	}

	private Long getDepartmentId(String departmentName, List<StaffDepartment> dList) {
		for (StaffDepartment item : dList) {
			if (departmentName.equals(item.getDepartmentName())) {
				return item.getPkId();
			}
		}
		return null;
	}
}