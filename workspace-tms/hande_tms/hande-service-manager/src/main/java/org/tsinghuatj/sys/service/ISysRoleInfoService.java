package org.tsinghuatj.sys.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.SysRoleInfo;

/**
 * @ClassName: ISysRoleInfoService
 * @Description: SysRoleInfo服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface ISysRoleInfoService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param sysRoleInfo
	 * @return
	 */
	Integer insert(Long userId, SysRoleInfo sysRoleInfo) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param sysRoleInfo
	 * @param sysRoleInfoId
	 * @return
	 */
	Integer updateActiveById(Long userId, SysRoleInfo sysRoleInfo, Long sysRoleInfoId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param sysRoleInfo
	 * @return
	 */
	List<SysRoleInfo> select(Long userId, SysRoleInfo sysRoleInfo) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param sysRoleInfo
	 * @return
	 */
	Pagination<SysRoleInfo> selectPageList(Long userId, SysRoleInfo sysRoleInfo,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param sysRoleInfoId
	 * @return
	 */
	SysRoleInfo selectById(Long userId, Long sysRoleInfoId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param sysRoleInfoId
	 * @return
	 */
	Integer deleteById(Long userId, Long sysRoleInfoId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param sysRoleInfoId
	 * @return
	 */
	Integer removeById(Long userId, Long sysRoleInfoId) throws BusinessException;
}
