package org.tsinghuatj.base.service;

import java.util.List;

import org.tsinghuatj.base.domain.CheckStandard;
import org.tsinghuatj.base.domain.Material;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;

/**
 * @ClassName: ICheckStandardService
 * @Description: CheckStandard服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface ICheckStandardService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param checkStandard
	 * @return
	 */
	Integer insert(Long userId, CheckStandard checkStandard,String appendixIds) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param checkStandard
	 * @param checkStandardId
	 * @return
	 */
	Integer updateActiveById(Long userId, CheckStandard checkStandard, Long checkStandardId,String appendixIds) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param checkStandard
	 * @return
	 */
	List<CheckStandard> select(Long userId, CheckStandard checkStandard) throws BusinessException;
	
	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param checkStandard
	 * @return
	 */
	List<Material> selectMaterialList(Long userId,Integer materialType) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolNumber
	 * @return
	 */
	CheckStandard selectByMaterialNumber(Long userId, Integer materialType,String materialNumber, Integer checkType) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param checkStandard
	 * @return
	 */
	Pagination<CheckStandard> selectPageList(Long userId, CheckStandard checkStandard, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param checkStandardId
	 * @return
	 */
	CheckStandard selectById(Long userId, Long checkStandardId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param checkStandardId
	 * @return
	 */
	Integer deleteById(Long userId, Long checkStandardId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param checkStandardId
	 * @return
	 */
	Integer removeById(Long userId, Long checkStandardId) throws BusinessException;
	
	/**
	 * 质检标准校验 一种物料一种类型只有一种质检标准
	 * @param toolNumber
	 * @param checkType
	 * @param pkId
	 * @return
	 * @throws BusinessException
	 */
	boolean checkStandardVerification(Integer materialType,String materailNumber, Integer checkType, Long pkId) throws BusinessException;
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param checkStandardId
	 * @return
	 */
	Integer importCheckStandard(Long userId, List<CheckStandard> list) throws BusinessException;
}
