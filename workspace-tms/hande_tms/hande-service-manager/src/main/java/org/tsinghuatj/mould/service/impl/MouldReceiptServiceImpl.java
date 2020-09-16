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
import org.tsinghuatj.mould.domain.MouldReceipt;
import org.tsinghuatj.mould.repository.MouldReceiptMapper;
import org.tsinghuatj.mould.service.IMouldReceiptService;


/**
 *
 * MouldReceipt 表数据服务层接口实现类
 *
 */
@Service("mouldReceiptService")
public class MouldReceiptServiceImpl extends BaseServiceImpl implements IMouldReceiptService {

	private @Resource MouldReceiptMapper mouldReceiptMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MouldReceipt mouldReceipt) throws BusinessException {	
		mouldReceipt.setPkId(getPkId());
		mouldReceipt.setCreateTime(new Date());
		mouldReceipt.setCreateUser(userId);
		mouldReceipt.setUpdateTime(new Date());
		mouldReceipt.setUpdateUser(userId);
		mouldReceipt.setDelMark(0);
		return mouldReceiptMapper.insert(mouldReceipt);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MouldReceipt mouldReceipt, Long mouldReceiptId) throws BusinessException {
		mouldReceipt.setUpdateTime(new Date());
		mouldReceipt.setUpdateUser(userId);
		return mouldReceiptMapper.updateActiveById(mouldReceipt, mouldReceiptId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MouldReceipt selectById(Long userId, Long mouldReceiptId) throws BusinessException {
		return mouldReceiptMapper.selectById(mouldReceiptId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long mouldReceiptId) throws BusinessException {
		return mouldReceiptMapper.removeById(mouldReceiptId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long mouldReceiptId) throws BusinessException {
		MouldReceipt mouldReceipt = new MouldReceipt();
		mouldReceipt.setPkId(mouldReceiptId);
		mouldReceipt.setUpdateTime(new Date());
		mouldReceipt.setUpdateUser(userId);
		return mouldReceiptMapper.deleteById(mouldReceipt);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MouldReceipt> select(Long userId, MouldReceipt mouldReceipt) throws BusinessException {		
		return mouldReceiptMapper.select(mouldReceipt);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MouldReceipt> selectPageList(Long userId, MouldReceipt mouldReceipt,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MouldReceipt> page = mouldReceiptMapper.selectPageList(mouldReceipt, queryDto);
		return new Pagination<MouldReceipt>(page.getTotal(), page.getResult());		
	}

	
}