package org.tsinghuatj.sys.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.AuthDto;
import org.tsinghuatj.sys.domain.SysRoleAuth;

/**
 * @ClassName: ISysRoleAuthService
 * @Description: SysRoleAuth服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface ISysRoleAuthService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param sysRoleAuth
	 * @return
	 */
	Integer insert(Long userId, SysRoleAuth sysRoleAuth) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param sysRoleAuth
	 * @param sysRoleAuthId
	 * @return
	 */
	Integer updateActiveById(Long userId, SysRoleAuth sysRoleAuth, Long sysRoleAuthId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param sysRoleAuth
	 * @return
	 */
	List<SysRoleAuth> select(Long userId, SysRoleAuth sysRoleAuth) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param sysRoleAuth
	 * @return
	 */
	Pagination<SysRoleAuth> selectPageList(Long userId, SysRoleAuth sysRoleAuth,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param sysRoleAuthId
	 * @return
	 */
	SysRoleAuth selectById(Long userId, Long sysRoleAuthId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param sysRoleAuthId
	 * @return
	 */
	Integer deleteById(Long userId, Long sysRoleAuthId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param sysRoleAuthId
	 * @return
	 */
	Integer removeById(Long userId, Long sysRoleAuthId) throws BusinessException;
	
	
	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param sysUserAuth
	 * @return
	 */
	List<String> selectByRoleId(Long userId, Long sysRoleId) throws BusinessException;
	
	
	/**
	 * 
	 * @param userId
	 * @param authDto
	 * @return
	 * @throws BusinessException
	 */
	Integer batchSave(Long userId, AuthDto authDto) throws BusinessException;
}
