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
import org.tsinghuatj.measure.domain.MeasurePurchaseReceipt;
import org.tsinghuatj.measure.repository.MeasurePurchaseReceiptMapper;
import org.tsinghuatj.measure.service.IMeasurePurchaseReceiptService;


/**
 *
 * MeasurePurchaseReceipt 表数据服务层接口实现类
 *
 */
@Service("measurePurchaseReceiptService")
public class MeasurePurchaseReceiptServiceImpl extends BaseServiceImpl implements IMeasurePurchaseReceiptService {

	private @Resource MeasurePurchaseReceiptMapper measurePurchaseReceiptMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MeasurePurchaseReceipt measurePurchaseReceipt) throws BusinessException {	
		measurePurchaseReceipt.setPkId(getPkId());
		measurePurchaseReceipt.setCreateTime(new Date());
		measurePurchaseReceipt.setCreateUser(userId);
		measurePurchaseReceipt.setUpdateTime(new Date());
		measurePurchaseReceipt.setUpdateUser(userId);
		measurePurchaseReceipt.setDelMark(0);
		return measurePurchaseReceiptMapper.insert(measurePurchaseReceipt);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MeasurePurchaseReceipt measurePurchaseReceipt, Long measurePurchaseReceiptId) throws BusinessException {
		measurePurchaseReceipt.setUpdateTime(new Date());
		measurePurchaseReceipt.setUpdateUser(userId);
		return measurePurchaseReceiptMapper.updateActiveById(measurePurchaseReceipt, measurePurchaseReceiptId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MeasurePurchaseReceipt selectById(Long userId, Long measurePurchaseReceiptId) throws BusinessException {
		return measurePurchaseReceiptMapper.selectById(measurePurchaseReceiptId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long measurePurchaseReceiptId) throws BusinessException {
		return measurePurchaseReceiptMapper.removeById(measurePurchaseReceiptId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long measurePurchaseReceiptId) throws BusinessException {
		MeasurePurchaseReceipt measurePurchaseReceipt = new MeasurePurchaseReceipt();
		measurePurchaseReceipt.setPkId(measurePurchaseReceiptId);
		measurePurchaseReceipt.setUpdateTime(new Date());
		measurePurchaseReceipt.setUpdateUser(userId);
		return measurePurchaseReceiptMapper.deleteById(measurePurchaseReceipt);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MeasurePurchaseReceipt> select(Long userId, MeasurePurchaseReceipt measurePurchaseReceipt) throws BusinessException {		
		return measurePurchaseReceiptMapper.select(measurePurchaseReceipt);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MeasurePurchaseReceipt> selectPageList(Long userId, MeasurePurchaseReceipt measurePurchaseReceipt,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MeasurePurchaseReceipt> page = measurePurchaseReceiptMapper.selectPageList(measurePurchaseReceipt, queryDto);
		return new Pagination<MeasurePurchaseReceipt>(page.getTotal(), page.getResult());		
	}
}