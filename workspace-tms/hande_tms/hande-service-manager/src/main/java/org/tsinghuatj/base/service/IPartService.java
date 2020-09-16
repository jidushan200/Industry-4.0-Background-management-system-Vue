package org.tsinghuatj.base.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.base.domain.Part;

/**
 * @ClassName: IPartService
 * @Description: Part服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IPartService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param part
	 * @return
	 */
	Integer insert(Long userId, Part part) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param part
	 * @param partId
	 * @return
	 */
	Integer updateActiveById(Long userId, Part part, Long partId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param part
	 * @return
	 */
	List<Part> select(Long userId, Part part) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param part
	 * @return
	 */
	Pagination<Part> selectPageList(Long userId, Part part, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param partId
	 * @return
	 */
	Part selectById(Long userId, Long partId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param partId
	 * @return
	 */
	Integer deleteById(Long userId, Long partId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param partId
	 * @return
	 */
	Integer removeById(Long userId, Long partId) throws BusinessException;

	/**
	 * 加工零件表导入
	 */
	Integer partImport(Long userId, List<Part> partList) throws BusinessException;

	Integer partSynchro(Long userId, String partCode) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param partId
	 * @return
	 */
	Part selectByCode(Long userId, String partCode) throws BusinessException;
	
	boolean checkPartCode(String partCode,Long pkId) throws BusinessException;

}
