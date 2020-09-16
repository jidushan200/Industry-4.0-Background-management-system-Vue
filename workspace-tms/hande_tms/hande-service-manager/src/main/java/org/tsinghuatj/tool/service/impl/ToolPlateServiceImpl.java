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
import org.tsinghuatj.tool.domain.ToolPlate;
import org.tsinghuatj.tool.repository.ToolPlateMapper;
import org.tsinghuatj.tool.service.IToolPlateService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolPlate 表数据服务层接口实现类
 *
 */
@Service("toolPlateService")
public class ToolPlateServiceImpl extends BaseServiceImpl implements IToolPlateService {

	private @Resource ToolPlateMapper toolPlateMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolPlate toolPlate) throws BusinessException {
		ToolPlate old = toolPlateMapper.selectByPlateNumber(toolPlate.getPlateNumber());
		if(null!=old){
			throw new BusinessException("toolplate.exists.error");
		}
		toolPlate.setPkId(getPkId());
		toolPlate.setCreateTime(new Date());
		toolPlate.setCreateUser(userId);
		toolPlate.setUpdateTime(new Date());
		toolPlate.setUpdateUser(userId);
		toolPlate.setDelMark(0);
		return toolPlateMapper.insert(toolPlate);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolPlate toolPlate, Long toolPlateId) throws BusinessException {
		toolPlate.setUpdateTime(new Date());
		toolPlate.setUpdateUser(userId);
		return toolPlateMapper.updateActiveById(toolPlate, toolPlateId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolPlate selectById(Long userId, Long toolPlateId) throws BusinessException {
		return toolPlateMapper.selectById(toolPlateId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolPlateId) throws BusinessException {
		return toolPlateMapper.removeById(toolPlateId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolPlateId) throws BusinessException {
		ToolPlate toolPlate = new ToolPlate();
		toolPlate.setPkId(toolPlateId);
		toolPlate.setUpdateTime(new Date());
		toolPlate.setUpdateUser(userId);
		return toolPlateMapper.deleteById(toolPlate);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolPlate> select(Long userId, ToolPlate toolPlate) throws BusinessException {
		List<ToolPlate> list = toolPlateMapper.select(toolPlate);
		for (ToolPlate tp : list) {
			if (1 == tp.getUseStatus()) {
				tp.setUseStatusName("在用");
			} else {
				tp.setUseStatusName("在库");
			}
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolPlate> selectPageList(Long userId, ToolPlate toolPlate, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolPlate> page = toolPlateMapper.selectPageList(toolPlate, queryDto);
		return new Pagination<ToolPlate>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer importToolPlate(Long userId, List<ToolPlate> toolPlateList) throws BusinessException {
		for (ToolPlate tp : toolPlateList) {
			ToolPlate toolPlate = toolPlateMapper.selectByPlateNumber(tp.getPlateNumber());
			if (null == toolPlate) {
				tp.setPkId(getPkId());
				if (null != tp.getUseStatusName() && "在库".equals(tp.getUseStatusName())) {
					tp.setUseStatus(2);
				} else {
					tp.setUseStatus(1);
				}
				tp.setCreateTime(new Date());
				tp.setCreateUser(userId);
				tp.setUpdateTime(new Date());
				tp.setUpdateUser(userId);
				tp.setDelMark(0);
				toolPlateMapper.insert(tp);
			} else {
				toolPlate.setPlateName(tp.getPlateName());
				toolPlate.setUseStatus(tp.getUseStatus());
				tp.setUpdateTime(new Date());
				tp.setUpdateUser(userId);
				if (null != tp.getUseStatusName() && "在库".equals(tp.getUseStatusName())) {
					tp.setUseStatus(2);
				} else {
					tp.setUseStatus(1);
				}
				toolPlateMapper.updateActiveById(toolPlate, toolPlate.getPkId());
			}
		}
		return 1;
	}
}