package org.tsinghuatj.mould.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.Mould;

/**
 * @ClassName: IMouldService
 * @Description: Mould服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMouldService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param mould
	 * @return
	 */
	Integer insert(Long userId, Mould mould) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param mould
	 * @param mouldId
	 * @return
	 */
	Integer updateActiveById(Long userId, Mould mould, Long mouldId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param mould
	 * @return
	 */
	List<Mould> select(Long userId, Mould mould) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param mould
	 * @return
	 */
	Pagination<Mould> selectPageList(Long userId, Mould mould,QueryDto queryDto) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param tool
	 * @return
	 */
	Pagination<Mould> selectLifePageList(Long userId, Mould mould,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param mouldId
	 * @return
	 */
	Mould selectById(Long userId, Long mouldId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param mouldId
	 * @return
	 */
	Integer deleteById(Long userId, Long mouldId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param mouldId
	 * @return
	 */
	Integer removeById(Long userId, Long mouldId) throws BusinessException;
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param fullNumber
	 * @return
	 */
	Mould selectByFullNumber(Long userId, String fullNumber) throws BusinessException;
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param mouldNumber
	 * @return
	 */
	Mould selectSeqByMouldNumber(Long userId, String mouldNumber) throws BusinessException;
	
	/**
	 * 刀具基础信息表导入
	 */
	Integer mouldImport(Long userId, List<Mould> mouldList) throws BusinessException;
}
