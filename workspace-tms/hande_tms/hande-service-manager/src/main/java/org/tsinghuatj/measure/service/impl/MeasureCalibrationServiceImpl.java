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
import org.tsinghuatj.measure.domain.MeasureCalibration;
import org.tsinghuatj.measure.repository.MeasureCalibrationMapper;
import org.tsinghuatj.measure.service.IMeasureCalibrationService;


/**
 *
 * MeasureCalibration 表数据服务层接口实现类
 *
 */
@Service("measureCalibrationService")
public class MeasureCalibrationServiceImpl extends BaseServiceImpl implements IMeasureCalibrationService {

	private @Resource MeasureCalibrationMapper measureCalibrationMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MeasureCalibration measureCalibration) throws BusinessException {	
		measureCalibration.setPkId(getPkId());
		measureCalibration.setCreateTime(new Date());
		measureCalibration.setCreateUser(userId);
		measureCalibration.setUpdateTime(new Date());
		measureCalibration.setUpdateUser(userId);
		measureCalibration.setDelMark(0);
		return measureCalibrationMapper.insert(measureCalibration);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MeasureCalibration measureCalibration, Long measureCalibrationId) throws BusinessException {
		measureCalibration.setUpdateTime(new Date());
		measureCalibration.setUpdateUser(userId);
		return measureCalibrationMapper.updateActiveById(measureCalibration, measureCalibrationId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MeasureCalibration selectById(Long userId, Long measureCalibrationId) throws BusinessException {
		return measureCalibrationMapper.selectById(measureCalibrationId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long measureCalibrationId) throws BusinessException {
		return measureCalibrationMapper.removeById(measureCalibrationId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long measureCalibrationId) throws BusinessException {
		MeasureCalibration measureCalibration = new MeasureCalibration();
		measureCalibration.setPkId(measureCalibrationId);
		measureCalibration.setUpdateTime(new Date());
		measureCalibration.setUpdateUser(userId);
		return measureCalibrationMapper.deleteById(measureCalibration);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MeasureCalibration> select(Long userId, MeasureCalibration measureCalibration) throws BusinessException {		
		return measureCalibrationMapper.select(measureCalibration);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MeasureCalibration> selectPageList(Long userId, MeasureCalibration measureCalibration,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MeasureCalibration> page = measureCalibrationMapper.selectPageList(measureCalibration, queryDto);
		return new Pagination<MeasureCalibration>(page.getTotal(), page.getResult());		
	}
}