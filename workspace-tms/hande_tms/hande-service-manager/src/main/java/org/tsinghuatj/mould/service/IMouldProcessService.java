package org.tsinghuatj.mould.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.MouldProcess;

/**
 * @ClassName: IMouldProcessService
 * @Description: MouldProcess服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IMouldProcessService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param mouldProcess
	 * @return
	 */
	Integer insert(Long userId, MouldProcess mouldProcess) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param mouldProcess
	 * @param mouldProcessId
	 * @return
	 */
	Integer updateActiveById(Long userId, MouldProcess mouldProcess, Long mouldProcessId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param mouldProcess
	 * @return
	 */
	List<MouldProcess> select(Long userId, MouldProcess mouldProcess) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param mouldProcess
	 * @return
	 */
	Pagination<MouldProcess> selectPageList(Long userId, MouldProcess mouldProcess,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param mouldProcessId
	 * @return
	 */
	MouldProcess selectById(Long userId, Long mouldProcessId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param mouldProcessId
	 * @return
	 */
	Integer deleteById(Long userId, Long mouldProcessId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param mouldProcessId
	 * @return
	 */
	Integer removeById(Long userId, Long mouldProcessId) throws BusinessException;
}
