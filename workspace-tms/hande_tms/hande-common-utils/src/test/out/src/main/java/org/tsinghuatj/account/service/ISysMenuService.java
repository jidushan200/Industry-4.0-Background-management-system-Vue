package org.tsinghuatj.account.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.account.domain.SysMenu;

/**
 * @ClassName: ISysMenuService
 * @Description: SysMenu服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface ISysMenuService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param sysMenu
	 * @return
	 */
	Integer insert(Long userId, SysMenu sysMenu) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param sysMenu
	 * @param sysMenuId
	 * @return
	 */
	Integer updateActiveById(Long userId, SysMenu sysMenu, Long sysMenuId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param sysMenu
	 * @return
	 */
	List<SysMenu> select(Long userId, SysMenu sysMenu) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param sysMenu
	 * @return
	 */
	Pagination<SysMenu> selectPageList(Long userId, SysMenu sysMenu,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param sysMenuId
	 * @return
	 */
	SysMenu selectById(Long userId, Long sysMenuId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param sysMenuId
	 * @return
	 */
	Integer deleteById(Long userId, Long sysMenuId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param sysMenuId
	 * @return
	 */
	Integer removeById(Long userId, Long sysMenuId) throws BusinessException;
}
