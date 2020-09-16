package org.tsinghuatj.measure.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.measure.domain.Measure;
import org.tsinghuatj.measure.domain.MeasureCheck;

/**
 * @ClassName: IMeasureCheckService
 * @Description: MeasureCheck服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMeasureCheckService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param measureCheck
	 * @return
	 */
	Integer insert(Long userId, MeasureCheck measureCheck) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param measureCheck
	 * @param measureCheckId
	 * @return
	 */
	Integer updateActiveById(Long userId, MeasureCheck measureCheck, Long measureCheckId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param measureCheck
	 * @return
	 */
	List<MeasureCheck> select(Long userId, MeasureCheck measureCheck) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param measureCheck
	 * @return
	 */
	Pagination<MeasureCheck> selectPageList(Long userId, MeasureCheck measureCheck,QueryDto queryDto) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param measureCheck
	 * @return
	 */
	Pagination<MeasureCheck> selectNewPageList(Long userId, MeasureCheck measureCheck,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param measureCheckId
	 * @return
	 */
	MeasureCheck selectById(Long userId, Long measureCheckId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param measureCheckId
	 * @return
	 */
	Integer deleteById(Long userId, Long measureCheckId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param measureCheckId
	 * @return
	 */
	Integer removeById(Long userId, Long measureCheckId) throws BusinessException;
	
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param measureCheckId
	 * @return
	 */
	Integer checkQualified(Long userId,MeasureCheck measureCheck,Measure measure) throws BusinessException;
}
