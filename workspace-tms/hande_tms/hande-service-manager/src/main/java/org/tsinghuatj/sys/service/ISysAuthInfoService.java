package org.tsinghuatj.sys.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.SysAuthInfo;

/**
 * @ClassName: ISysAuthInfoService
 * @Description: SysAuthInfo服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface ISysAuthInfoService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param sysAuthInfo
	 * @return
	 */
	Integer insert(Long userId, SysAuthInfo sysAuthInfo) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param sysAuthInfo
	 * @param sysAuthInfoId
	 * @return
	 */
	Integer updateActiveById(Long userId, SysAuthInfo sysAuthInfo, Long sysAuthInfoId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param sysAuthInfo
	 * @return
	 */
	List<SysAuthInfo> select(Long userId, SysAuthInfo sysAuthInfo) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param sysAuthInfo
	 * @return
	 */
	Pagination<SysAuthInfo> selectPageList(Long userId, SysAuthInfo sysAuthInfo,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param sysAuthInfoId
	 * @return
	 */
	SysAuthInfo selectById(Long userId, Long sysAuthInfoId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param sysAuthInfoId
	 * @return
	 */
	Integer deleteById(Long userId, Long sysAuthInfoId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param sysAuthInfoId
	 * @return
	 */
	Integer removeById(Long userId, Long sysAuthInfoId) throws BusinessException;
	
	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param sysUserAuth
	 * @return
	 */
	SysAuthInfo selectAuthTreeByRoleId(Long userId, Long sysRoleId) throws BusinessException;
	
	/**
	 * 
	 * @param userId
	 * @param sysUserId
	 * @return
	 * @throws BusinessException
	 */
	List<String> selectAuthByUserId(Long userId, Long sysUserId) throws BusinessException;
	
	int batchInsert(Long userId, List<SysAuthInfo> authList) throws BusinessException;
}
