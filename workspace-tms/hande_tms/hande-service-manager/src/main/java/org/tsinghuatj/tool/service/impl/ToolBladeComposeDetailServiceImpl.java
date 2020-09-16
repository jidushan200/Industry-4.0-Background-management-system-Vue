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
import org.tsinghuatj.tool.domain.ToolBladeComposeDetail;
import org.tsinghuatj.tool.repository.ToolBladeComposeDetailMapper;
import org.tsinghuatj.tool.service.IToolBladeComposeDetailService;


/**
 *
 * ToolBladeComposeDetail 表数据服务层接口实现类
 *
 */
@Service("toolBladeComposeDetailService")
public class ToolBladeComposeDetailServiceImpl extends BaseServiceImpl implements IToolBladeComposeDetailService {

	private @Resource ToolBladeComposeDetailMapper toolBladeComposeDetailMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolBladeComposeDetail toolBladeComposeDetail) throws BusinessException {	
		toolBladeComposeDetail.setPkId(getPkId());
		toolBladeComposeDetail.setCreateTime(new Date());
		toolBladeComposeDetail.setCreateUser(userId);
		toolBladeComposeDetail.setUpdateTime(new Date());
		toolBladeComposeDetail.setUpdateUser(userId);
		toolBladeComposeDetail.setDelMark(0);
		return toolBladeComposeDetailMapper.insert(toolBladeComposeDetail);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolBladeComposeDetail toolBladeComposeDetail, Long toolBladeComposeDetailId) throws BusinessException {
		toolBladeComposeDetail.setUpdateTime(new Date());
		toolBladeComposeDetail.setUpdateUser(userId);
		return toolBladeComposeDetailMapper.updateActiveById(toolBladeComposeDetail, toolBladeComposeDetailId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolBladeComposeDetail selectById(Long userId, Long toolBladeComposeDetailId) throws BusinessException {
		return toolBladeComposeDetailMapper.selectById(toolBladeComposeDetailId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolBladeComposeDetailId) throws BusinessException {
		return toolBladeComposeDetailMapper.removeById(toolBladeComposeDetailId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolBladeComposeDetailId) throws BusinessException {
		ToolBladeComposeDetail toolBladeComposeDetail = new ToolBladeComposeDetail();
		toolBladeComposeDetail.setPkId(toolBladeComposeDetailId);
		toolBladeComposeDetail.setUpdateTime(new Date());
		toolBladeComposeDetail.setUpdateUser(userId);
		return toolBladeComposeDetailMapper.deleteById(toolBladeComposeDetail);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolBladeComposeDetail> select(Long userId, ToolBladeComposeDetail toolBladeComposeDetail) throws BusinessException {		
		return toolBladeComposeDetailMapper.select(toolBladeComposeDetail);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolBladeComposeDetail> selectPageList(Long userId, ToolBladeComposeDetail toolBladeComposeDetail,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolBladeComposeDetail> page = toolBladeComposeDetailMapper.selectPageList(toolBladeComposeDetail, queryDto);
		return new Pagination<ToolBladeComposeDetail>(page.getTotal(), page.getResult());		
	}
}