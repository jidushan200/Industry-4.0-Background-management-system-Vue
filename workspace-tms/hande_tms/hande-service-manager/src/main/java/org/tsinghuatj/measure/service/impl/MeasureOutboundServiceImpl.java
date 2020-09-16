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
import org.tsinghuatj.measure.domain.MeasureOutbound;
import org.tsinghuatj.measure.repository.MeasureOutboundMapper;
import org.tsinghuatj.measure.service.IMeasureOutboundService;


/**
 *
 * MeasureOutbound 表数据服务层接口实现类
 *
 */
@Service("measureOutboundService")
public class MeasureOutboundServiceImpl extends BaseServiceImpl implements IMeasureOutboundService {

	private @Resource MeasureOutboundMapper measureOutboundMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MeasureOutbound measureOutbound) throws BusinessException {	
		measureOutbound.setPkId(getPkId());
		measureOutbound.setCreateTime(new Date());
		measureOutbound.setCreateUser(userId);
		measureOutbound.setUpdateTime(new Date());
		measureOutbound.setUpdateUser(userId);
		measureOutbound.setDelMark(0);
		return measureOutboundMapper.insert(measureOutbound);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MeasureOutbound measureOutbound, Long measureOutboundId) throws BusinessException {
		measureOutbound.setUpdateTime(new Date());
		measureOutbound.setUpdateUser(userId);
		return measureOutboundMapper.updateActiveById(measureOutbound, measureOutboundId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MeasureOutbound selectById(Long userId, Long measureOutboundId) throws BusinessException {
		return measureOutboundMapper.selectById(measureOutboundId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long measureOutboundId) throws BusinessException {
		return measureOutboundMapper.removeById(measureOutboundId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long measureOutboundId) throws BusinessException {
		MeasureOutbound measureOutbound = new MeasureOutbound();
		measureOutbound.setPkId(measureOutboundId);
		measureOutbound.setUpdateTime(new Date());
		measureOutbound.setUpdateUser(userId);
		return measureOutboundMapper.deleteById(measureOutbound);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MeasureOutbound> select(Long userId, MeasureOutbound measureOutbound) throws BusinessException {		
		return measureOutboundMapper.select(measureOutbound);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MeasureOutbound> selectPageList(Long userId, MeasureOutbound measureOutbound,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MeasureOutbound> page = measureOutboundMapper.selectPageList(measureOutbound, queryDto);
		return new Pagination<MeasureOutbound>(page.getTotal(), page.getResult());		
	}
}