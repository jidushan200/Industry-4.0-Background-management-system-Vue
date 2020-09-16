package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolHead;

/**
 * @ClassName: IToolHeadService
 * @Description: ToolHead服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolHeadService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolHead
	 * @return
	 */
	Integer insert(Long userId, ToolHead toolHead) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolHead
	 * @param toolHeadId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolHead toolHead, Long toolHeadId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolHead
	 * @return
	 */
	List<ToolHead> select(Long userId, ToolHead toolHead) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolHead
	 * @return
	 */
	Pagination<ToolHead> selectPageList(Long userId, ToolHead toolHead,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolHeadId
	 * @return
	 */
	ToolHead selectById(Long userId, Long toolHeadId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolHeadId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolHeadId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolHeadId
	 * @return
	 */
	Integer removeById(Long userId, Long toolHeadId) throws BusinessException;
	
	Integer toolHeadImport(Long userId, List<ToolHead> partList) throws BusinessException;
	
	boolean checkHeadNumber(String headNumber, Long pkId) throws BusinessException;
}
