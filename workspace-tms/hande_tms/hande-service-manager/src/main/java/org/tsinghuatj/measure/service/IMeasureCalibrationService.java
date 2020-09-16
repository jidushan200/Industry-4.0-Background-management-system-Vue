package org.tsinghuatj.measure.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.measure.domain.MeasureCalibration;

/**
 * @ClassName: IMeasureCalibrationService
 * @Description: MeasureCalibration服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMeasureCalibrationService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param measureCalibration
	 * @return
	 */
	Integer insert(Long userId, MeasureCalibration measureCalibration) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param measureCalibration
	 * @param measureCalibrationId
	 * @return
	 */
	Integer updateActiveById(Long userId, MeasureCalibration measureCalibration, Long measureCalibrationId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param measureCalibration
	 * @return
	 */
	List<MeasureCalibration> select(Long userId, MeasureCalibration measureCalibration) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param measureCalibration
	 * @return
	 */
	Pagination<MeasureCalibration> selectPageList(Long userId, MeasureCalibration measureCalibration,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param measureCalibrationId
	 * @return
	 */
	MeasureCalibration selectById(Long userId, Long measureCalibrationId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param measureCalibrationId
	 * @return
	 */
	Integer deleteById(Long userId, Long measureCalibrationId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param measureCalibrationId
	 * @return
	 */
	Integer removeById(Long userId, Long measureCalibrationId) throws BusinessException;
}
