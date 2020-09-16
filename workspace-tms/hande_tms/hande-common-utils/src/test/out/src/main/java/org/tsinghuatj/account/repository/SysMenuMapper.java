package org.tsinghuatj.account.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.account.domain.SysMenu;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface SysMenuMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param sysMenu
	 * @return
	 */
	Integer insert(@Param("sysMenu") SysMenu sysMenu);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param sysMenu
	 * @param sysMenuId
	 * @return
	 */
	Integer updateActiveById(@Param("sysMenu") SysMenu sysMenu, @Param("sysMenuId") Long sysMenuId);

	/**
	 * 查询列表
	 * 
	 * @param sysMenu
	 * @return
	 */
	List<SysMenu> select(@Param("sysMenu") SysMenu sysMenu);
	
	/**
	 * 分页查询列表
	 * 
	 * @param sysMenu
	 * @return
	 */
	Page<SysMenu> selectPageList(@Param("sysMenu") SysMenu sysMenu,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param sysMenuId
	 * @return
	 */
	SysMenu selectById(@Param("sysMenuId") Long sysMenuId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param sysMenuId
	 * @return
	 */
	Integer deleteById(@Param("sysMenuId") Long sysMenuId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param sysMenuId
	 * @return
	 */
	Integer removeById(@Param("sysMenuId") Long sysMenuId);
}
