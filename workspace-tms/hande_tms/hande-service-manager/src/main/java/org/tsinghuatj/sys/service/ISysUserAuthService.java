package org.tsinghuatj.sys.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.SysUserAuth;

/**
 * @ClassName: ISysUserAuthService
 * @Description: SysUserAuth服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface ISysUserAuthService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param sysUserAuth
	 * @return
	 */
	Integer insert(Long userId, SysUserAuth sysUserAuth) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param sysUserAuth
	 * @param sysUserAuthId
	 * @return
	 */
	Integer updateActiveById(Long userId, SysUserAuth sysUserAuth, Long sysUserAuthId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param sysUserAuth
	 * @return
	 */
	List<SysUserAuth> select(Long userId, SysUserAuth sysUserAuth) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param sysUserAuth
	 * @return
	 */
	Pagination<SysUserAuth> selectPageList(Long userId, SysUserAuth sysUserAuth,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param sysUserAuthId
	 * @return
	 */
	SysUserAuth selectById(Long userId, Long sysUserAuthId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param sysUserAuthId
	 * @return
	 */
	Integer deleteById(Long userId, Long sysUserAuthId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param sysUserAuthId
	 * @return
	 */
	Integer removeById(Long userId, Long sysUserAuthId) throws BusinessException;
}
