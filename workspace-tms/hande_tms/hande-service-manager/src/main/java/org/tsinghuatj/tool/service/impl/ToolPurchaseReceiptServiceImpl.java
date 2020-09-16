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
import org.tsinghuatj.tool.domain.ToolPurchaseReceipt;
import org.tsinghuatj.tool.repository.ToolPurchaseReceiptMapper;
import org.tsinghuatj.tool.service.IToolPurchaseReceiptService;


/**
 *
 * ToolPurchaseReceipt 表数据服务层接口实现类
 *
 */
@Service("toolPurchaseReceiptService")
public class ToolPurchaseReceiptServiceImpl extends BaseServiceImpl implements IToolPurchaseReceiptService {

	private @Resource ToolPurchaseReceiptMapper toolPurchaseReceiptMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolPurchaseReceipt toolPurchaseReceipt) throws BusinessException {	
		toolPurchaseReceipt.setPkId(getPkId());
		toolPurchaseReceipt.setCreateTime(new Date());
		toolPurchaseReceipt.setCreateUser(userId);
		toolPurchaseReceipt.setUpdateTime(new Date());
		toolPurchaseReceipt.setUpdateUser(userId);
		toolPurchaseReceipt.setDelMark(0);
		return toolPurchaseReceiptMapper.insert(toolPurchaseReceipt);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolPurchaseReceipt toolPurchaseReceipt, Long toolPurchaseReceiptId) throws BusinessException {
		toolPurchaseReceipt.setUpdateTime(new Date());
		toolPurchaseReceipt.setUpdateUser(userId);
		return toolPurchaseReceiptMapper.updateActiveById(toolPurchaseReceipt, toolPurchaseReceiptId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolPurchaseReceipt selectById(Long userId, Long toolPurchaseReceiptId) throws BusinessException {
		return toolPurchaseReceiptMapper.selectById(toolPurchaseReceiptId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolPurchaseReceiptId) throws BusinessException {
		return toolPurchaseReceiptMapper.removeById(toolPurchaseReceiptId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolPurchaseReceiptId) throws BusinessException {
		ToolPurchaseReceipt toolPurchaseReceipt = new ToolPurchaseReceipt();
		toolPurchaseReceipt.setPkId(toolPurchaseReceiptId);
		toolPurchaseReceipt.setUpdateTime(new Date());
		toolPurchaseReceipt.setUpdateUser(userId);
		return toolPurchaseReceiptMapper.deleteById(toolPurchaseReceipt);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolPurchaseReceipt> select(Long userId, ToolPurchaseReceipt toolPurchaseReceipt) throws BusinessException {		
		return null;
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolPurchaseReceipt> selectPageList(Long userId, ToolPurchaseReceipt toolPurchaseReceipt,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolPurchaseReceipt> page = toolPurchaseReceiptMapper.selectPageList(toolPurchaseReceipt, queryDto);
		return new Pagination<ToolPurchaseReceipt>(page.getTotal(), page.getResult());		
	}
}