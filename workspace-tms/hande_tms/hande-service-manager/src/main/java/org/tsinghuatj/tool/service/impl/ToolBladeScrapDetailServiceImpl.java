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
import org.tsinghuatj.tool.domain.ToolBladeScrapDetail;
import org.tsinghuatj.tool.repository.ToolBladeScrapDetailMapper;
import org.tsinghuatj.tool.service.IToolBladeScrapDetailService;


/**
 *
 * ToolBladeScrapDetail 表数据服务层接口实现类
 *
 */
@Service("toolBladeScrapDetailService")
public class ToolBladeScrapDetailServiceImpl extends BaseServiceImpl implements IToolBladeScrapDetailService {

	private @Resource ToolBladeScrapDetailMapper toolBladeScrapDetailMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolBladeScrapDetail toolBladeScrapDetail) throws BusinessException {	
		toolBladeScrapDetail.setPkId(getPkId());
		toolBladeScrapDetail.setCreateTime(new Date());
		toolBladeScrapDetail.setCreateUser(userId);
		toolBladeScrapDetail.setUpdateTime(new Date());
		toolBladeScrapDetail.setUpdateUser(userId);
		toolBladeScrapDetail.setDelMark(0);
		return toolBladeScrapDetailMapper.insert(toolBladeScrapDetail);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolBladeScrapDetail toolBladeScrapDetail, Long toolBladeScrapDetailId) throws BusinessException {
		toolBladeScrapDetail.setUpdateTime(new Date());
		toolBladeScrapDetail.setUpdateUser(userId);
		return toolBladeScrapDetailMapper.updateActiveById(toolBladeScrapDetail, toolBladeScrapDetailId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolBladeScrapDetail selectById(Long userId, Long toolBladeScrapDetailId) throws BusinessException {
		return toolBladeScrapDetailMapper.selectById(toolBladeScrapDetailId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolBladeScrapDetailId) throws BusinessException {
		return toolBladeScrapDetailMapper.removeById(toolBladeScrapDetailId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolBladeScrapDetailId) throws BusinessException {
		ToolBladeScrapDetail toolBladeScrapDetail = new ToolBladeScrapDetail();
		toolBladeScrapDetail.setPkId(toolBladeScrapDetailId);
		toolBladeScrapDetail.setUpdateTime(new Date());
		toolBladeScrapDetail.setUpdateUser(userId);
		return toolBladeScrapDetailMapper.deleteById(toolBladeScrapDetail);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolBladeScrapDetail> select(Long userId, ToolBladeScrapDetail toolBladeScrapDetail) throws BusinessException {		
		return toolBladeScrapDetailMapper.select(toolBladeScrapDetail);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolBladeScrapDetail> selectPageList(Long userId, ToolBladeScrapDetail toolBladeScrapDetail,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolBladeScrapDetail> page = toolBladeScrapDetailMapper.selectPageList(toolBladeScrapDetail, queryDto);
		return new Pagination<ToolBladeScrapDetail>(page.getTotal(), page.getResult());		
	}
}