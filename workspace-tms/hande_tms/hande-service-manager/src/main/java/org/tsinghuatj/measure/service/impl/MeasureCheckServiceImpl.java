package org.tsinghuatj.measure.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.measure.domain.Measure;
import org.tsinghuatj.measure.domain.MeasureCheck;
import org.tsinghuatj.measure.domain.MeasurePurchaseApply;
import org.tsinghuatj.measure.repository.MeasureCheckMapper;
import org.tsinghuatj.measure.repository.MeasureMapper;
import org.tsinghuatj.measure.repository.MeasurePurchaseApplyMapper;
import org.tsinghuatj.measure.service.IMeasureCheckService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


/**
 *
 * MeasureCheck 表数据服务层接口实现类
 *
 */
@Service("measureCheckService")
public class MeasureCheckServiceImpl extends BaseServiceImpl implements IMeasureCheckService {

	private @Resource MeasureMapper measureMapper;
	private @Resource MeasureCheckMapper measureCheckMapper;
	private @Resource MeasurePurchaseApplyMapper measurePurchaseApplyMapper;
	private @Value("${file.save.path:/}") String filePath; 

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MeasureCheck measureCheck) throws BusinessException {	
		measureCheck.setPkId(getPkId());
		measureCheck.setCreateTime(new Date());
		measureCheck.setCreateUser(userId);
		measureCheck.setUpdateTime(new Date());
		measureCheck.setUpdateUser(userId);
		measureCheck.setDelMark(0);
		return measureCheckMapper.insert(measureCheck);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MeasureCheck measureCheck, Long measureCheckId) throws BusinessException {
		measureCheck.setUpdateTime(new Date());
		measureCheck.setUpdateUser(userId);
		return measureCheckMapper.updateActiveById(measureCheck, measureCheckId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MeasureCheck selectById(Long userId, Long measureCheckId) throws BusinessException {
		return measureCheckMapper.selectById(measureCheckId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long measureCheckId) throws BusinessException {
		return measureCheckMapper.removeById(measureCheckId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long measureCheckId) throws BusinessException {
		MeasureCheck measureCheck = new MeasureCheck();
		measureCheck.setPkId(measureCheckId);
		measureCheck.setUpdateTime(new Date());
		measureCheck.setUpdateUser(userId);
		return measureCheckMapper.deleteById(measureCheck);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MeasureCheck> select(Long userId, MeasureCheck measureCheck) throws BusinessException {		
		return measureCheckMapper.select(measureCheck);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MeasureCheck> selectPageList(Long userId, MeasureCheck measureCheck,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MeasureCheck> page = measureCheckMapper.selectPageList(measureCheck, queryDto);
		return new Pagination<MeasureCheck>(page.getTotal(), page.getResult());		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer checkQualified(Long userId, MeasureCheck measureCheck, Measure measure) throws BusinessException {
		
		if(measureCheck.getCheckType() == 1){
			Long pkId  = getPkId();
			measure.setPkId(pkId);
			measure.setCreateTime(new Date());
			measure.setCreateUser(userId);
			measure.setUpdateTime(new Date());
			measure.setUpdateUser(userId);
			measure.setDelMark(0);
			
			MeasurePurchaseApply measurePurchaseApply = measurePurchaseApplyMapper.selectByReceiptId(measureCheck.getReceiptId());
			measure.setApplyDepartmentId(measurePurchaseApply.getApplyDepartmentId());
			measure.setApplyDepartmentName(measurePurchaseApply.getApplyDepartmentName());
			measure.setApplierId(measurePurchaseApply.getApplierId());
			measure.setApplierName(measurePurchaseApply.getApplierName());			
			measureMapper.insert(measure);
			
		}
		measureCheck.setMeasureId(measure.getPkId());
		measureCheck.setUpdateUser(userId);
		measureCheck.setUpdateTime(new Date());
		measureCheck.setHandleResult(1);
		return measureCheckMapper.updateActiveById(measureCheck, measureCheck.getPkId());
	}

	@Override
	public Pagination<MeasureCheck> selectNewPageList(Long userId, MeasureCheck measureCheck, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MeasureCheck> page = measureCheckMapper.selectNewPageList(measureCheck, queryDto);
		return new Pagination<MeasureCheck>(page.getTotal(), page.getResult());		
	}
}