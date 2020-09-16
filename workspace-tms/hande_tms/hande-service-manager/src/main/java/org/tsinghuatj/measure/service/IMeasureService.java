package org.tsinghuatj.measure.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.measure.domain.Measure;

/**
 * @ClassName: IMeasureService
 * @Description: Measure服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMeasureService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param measure
	 * @return
	 */
	Integer insert(Long userId, Measure measure) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param measure
	 * @param measureId
	 * @return
	 */
	Integer updateActiveById(Long userId, Measure measure, Long measureId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param measure
	 * @return
	 */
	List<Measure> select(Long userId, Measure measure) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param measure
	 * @return
	 */
	Pagination<Measure> selectPageList(Long userId, Measure measure,QueryDto queryDto) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param tool
	 * @return
	 */
	Pagination<Measure> selectLifePageList(Long userId, Measure measure,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param measureId
	 * @return
	 */
	Measure selectById(Long userId, Long measureId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param measureId
	 * @return
	 */
	Integer deleteById(Long userId, Long measureId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param measureId
	 * @return
	 */
	Integer removeById(Long userId, Long measureId) throws BusinessException;
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param measureId
	 * @return
	 */
	Measure selectByFullNumber(Long userId, String fullNumber) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param measure
	 * @return
	 */
	Pagination<Measure> selectScripPageList(Long userId, Measure measure,QueryDto queryDto) throws BusinessException;
	
	/**
	 * 刀具基础信息表导入
	 */
	Integer measureImport(Long userId, List<Measure> measureList) throws BusinessException;
}
