package org.tsinghuatj.measure.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.measure.domain.MeasureBase;

/**
 * @ClassName: IMeasureBaseService
 * @Description: MeasureBase服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMeasureBaseService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param measureBase
	 * @return
	 */
	Integer insert(Long userId, MeasureBase measureBase) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param measureBase
	 * @param measureBaseId
	 * @return
	 */
	Integer updateActiveById(Long userId, MeasureBase measureBase, Long measureBaseId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param measureBase
	 * @return
	 */
	List<MeasureBase> select(Long userId, MeasureBase measureBase) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param measureBase
	 * @return
	 */
	Pagination<MeasureBase> selectPageList(Long userId, MeasureBase measureBase,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param measureBaseId
	 * @return
	 */
	MeasureBase selectById(Long userId, Long measureBaseId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param measureBaseId
	 * @return
	 */
	Integer deleteById(Long userId, Long measureBaseId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param measureBaseId
	 * @return
	 */
	Integer removeById(Long userId, Long measureBaseId) throws BusinessException;
    /**
     * 按编码查找
     * @param userId
     * @param measureNumber
     * @return
     * @throws BusinessException
     */
	MeasureBase selectByNumber(Long userId, String measureNumber)throws BusinessException;

	/**
	 * 刀具信息同步
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	Integer measureBaseSynchro(Long userId,String measureNumber)throws BusinessException;
	
	/**
	 * 刀具基础信息表导入
	 */
	Integer measureBaseImport(Long userId, List<MeasureBase> measureBaseList) throws BusinessException;
}
