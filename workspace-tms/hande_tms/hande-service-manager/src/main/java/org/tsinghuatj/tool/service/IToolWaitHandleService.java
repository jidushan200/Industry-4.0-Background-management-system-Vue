package org.tsinghuatj.tool.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolBladeComposePart;
import org.tsinghuatj.tool.domain.ToolBladeProcess;
import org.tsinghuatj.tool.domain.ToolWaitHandle;

/**
 * @ClassName: IToolWaitHandleService
 * @Description: ToolWaitHandle服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IToolWaitHandleService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param toolWaitHandle
	 * @return
	 */
	Integer insert(Long userId, ToolWaitHandle toolWaitHandle) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolWaitHandle
	 * @param toolWaitHandleId
	 * @return
	 */
	Integer updateActiveById(Long userId, ToolWaitHandle toolWaitHandle, Long toolWaitHandleId) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param pkId
	 * @return
	 */
	Integer setCheck(Long userId, Long pkId) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param pkId
	 * @return
	 */
	Integer setStock(Long userId, Long pkId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param toolWaitHandle
	 * @return
	 */
	List<ToolWaitHandle> select(Long userId, ToolWaitHandle toolWaitHandle) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolWaitHandle
	 * @return
	 */
	Pagination<ToolWaitHandle> selectPageList(Long userId, ToolWaitHandle toolWaitHandle, QueryDto queryDto) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param toolWaitHandle
	 * @return
	 */
	Pagination<ToolWaitHandle> selectCoatPageList(Long userId, ToolWaitHandle toolWaitHandle, QueryDto queryDto) throws BusinessException;
	
	

	Pagination<ToolWaitHandle> selectNewToolPageList(Long userId, ToolWaitHandle toolWaitHandle, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolWaitHandleId
	 * @return
	 */
	ToolWaitHandle selectById(Long userId, Long toolWaitHandleId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param toolWaitHandleId
	 * @return
	 */
	Integer deleteById(Long userId, Long toolWaitHandleId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param toolWaitHandleId
	 * @return
	 */
	Integer removeById(Long userId, Long toolWaitHandleId) throws BusinessException;

	/**
	 * 刀条送涂
	 * 
	 * @param userId
	 * @param pkId
	 * @return
	 */
	Integer setBladeCoat(Long userId, Long pkId, Long supplierId,String supplierName, String deliever, ToolBladeProcess process, List<ToolBladeComposePart> parts,String remark,String detailList) throws BusinessException;
}
