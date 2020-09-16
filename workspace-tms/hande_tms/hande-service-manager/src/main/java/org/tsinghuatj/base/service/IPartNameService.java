package org.tsinghuatj.base.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.base.domain.PartName;

/**
 * @ClassName: IPartNameService
 * @Description: PartName服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IPartNameService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param partName
	 * @return
	 */
	Integer insert(Long userId, PartName partName) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param partName
	 * @param partNameId
	 * @return
	 */
	Integer updateActiveById(Long userId, PartName partName, Long partNameId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param partName
	 * @return
	 */
	List<PartName> select(Long userId, PartName partName) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param partName
	 * @return
	 */
	Pagination<PartName> selectPageList(Long userId, PartName partName,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param partNameId
	 * @return
	 */
	PartName selectById(Long userId, Long partNameId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param partNameId
	 * @return
	 */
	Integer deleteById(Long userId, Long partNameId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param partNameId
	 * @return
	 */
	Integer removeById(Long userId, Long partNameId) throws BusinessException;
}
