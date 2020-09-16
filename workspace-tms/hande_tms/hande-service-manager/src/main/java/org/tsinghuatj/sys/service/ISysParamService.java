package org.tsinghuatj.sys.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.SysParam;

/**
 * @ClassName: ISysParamService
 * @Description: SysParam服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface ISysParamService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param sysParam
	 * @return
	 */
	Integer insert(Long userId, SysParam sysParam) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param sysParam
	 * @param sysParamId
	 * @return
	 */
	Integer updateActiveById(Long userId, SysParam sysParam, Long sysParamId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param sysParam
	 * @return
	 */
	List<SysParam> select(Long userId, SysParam sysParam) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param sysParam
	 * @return
	 */
	Pagination<SysParam> selectPageList(Long userId, SysParam sysParam, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param sysParamId
	 * @return
	 */
	SysParam selectById(Long userId, Long sysParamId) throws BusinessException;
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param sysParamId
	 * @return
	 */
	SysParam selectByParamKey(Long userId, String paramKey) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param sysParamId
	 * @return
	 */
	List<Long> selectDepartMentByParamKey(Long userId, String paramKey) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param sysParamId
	 * @return
	 */
	Integer deleteById(Long userId, Long sysParamId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param sysParamId
	 * @return
	 */
	Integer removeById(Long userId, Long sysParamId) throws BusinessException;
}
