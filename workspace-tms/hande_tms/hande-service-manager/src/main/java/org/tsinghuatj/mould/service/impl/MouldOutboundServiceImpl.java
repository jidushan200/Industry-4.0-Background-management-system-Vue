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
import org.tsinghuatj.mould.domain.MouldOutbound;
import org.tsinghuatj.mould.repository.MouldOutboundMapper;
import org.tsinghuatj.mould.service.IMouldOutboundService;


/**
 *
 * MouldOutbound 表数据服务层接口实现类
 *
 */
@Service("mouldOutboundService")
public class MouldOutboundServiceImpl extends BaseServiceImpl implements IMouldOutboundService {

	private @Resource MouldOutboundMapper mouldOutboundMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MouldOutbound mouldOutbound) throws BusinessException {	
		mouldOutbound.setPkId(getPkId());
		mouldOutbound.setCreateTime(new Date());
		mouldOutbound.setCreateUser(userId);
		mouldOutbound.setUpdateTime(new Date());
		mouldOutbound.setUpdateUser(userId);
		mouldOutbound.setDelMark(0);
		return mouldOutboundMapper.insert(mouldOutbound);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MouldOutbound mouldOutbound, Long mouldOutboundId) throws BusinessException {
		mouldOutbound.setUpdateTime(new Date());
		mouldOutbound.setUpdateUser(userId);
		return mouldOutboundMapper.updateActiveById(mouldOutbound, mouldOutboundId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MouldOutbound selectById(Long userId, Long mouldOutboundId) throws BusinessException {
		return mouldOutboundMapper.selectById(mouldOutboundId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long mouldOutboundId) throws BusinessException {
		return mouldOutboundMapper.removeById(mouldOutboundId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long mouldOutboundId) throws BusinessException {
		MouldOutbound mouldOutbound = new MouldOutbound();
		mouldOutbound.setPkId(mouldOutboundId);
		mouldOutbound.setUpdateTime(new Date());
		mouldOutbound.setUpdateUser(userId);
		return mouldOutboundMapper.deleteById(mouldOutbound);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MouldOutbound> select(Long userId, MouldOutbound mouldOutbound) throws BusinessException {		
		return mouldOutboundMapper.select(mouldOutbound);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MouldOutbound> selectPageList(Long userId, MouldOutbound mouldOutbound,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MouldOutbound> page = mouldOutboundMapper.selectPageList(mouldOutbound, queryDto);
		return new Pagination<MouldOutbound>(page.getTotal(), page.getResult());		
	}
}