package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolHeadPart;

/**
 * @ClassName: IToolHeadPartService
 * @Description: ToolHeadPart服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolHeadPartService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolHeadPart
	 * @return
	 */
	Integer insert(Long userId, ToolHeadPart toolHeadPart) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolHeadPart
	 * @param toolHeadPartId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolHeadPart toolHeadPart, Long toolHeadPartId) throws BusinessException;

	Integer headPartImport(Long userId, List<ToolHeadPart> headPartList) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolHeadPart
	 * @return
	 */
	List<ToolHeadPart> select(Long userId, ToolHeadPart toolHeadPart) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolHeadPart
	 * @return
	 */
	Pagination<ToolHeadPart> selectPageList(Long userId, ToolHeadPart toolHeadPart, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolHeadPartId
	 * @return
	 */
	ToolHeadPart selectById(Long userId, Long toolHeadPartId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolHeadPartId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolHeadPartId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolHeadPartId
	 * @return
	 */
	Integer removeById(Long userId, Long toolHeadPartId) throws BusinessException;
}
