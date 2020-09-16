package org.tsinghuatj.sys.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.sys.domain.SysRoleAuth;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface SysRoleAuthMapper {

	/**
	 * 插入数据
	 * 
	 * @param sysRoleAuth
	 * @return
	 */
	Integer insert(@Param("sysRoleAuth") SysRoleAuth sysRoleAuth);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param sysRoleAuth
	 * @param sysRoleAuthId
	 * @return
	 */
	Integer updateActiveById(@Param("sysRoleAuth") SysRoleAuth sysRoleAuth, @Param("sysRoleAuthId") Long sysRoleAuthId);

	/**
	 * 查询列表
	 * 
	 * @param sysRoleAuth
	 * @return
	 */
	List<SysRoleAuth> select(@Param("sysRoleAuth") SysRoleAuth sysRoleAuth);

	/**
	 * 分页查询列表
	 * 
	 * @param sysRoleAuth
	 * @return
	 */
	Page<SysRoleAuth> selectPageList(@Param("sysRoleAuth") SysRoleAuth sysRoleAuth,
			@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param sysRoleAuthId
	 * @return
	 */
	SysRoleAuth selectById(@Param("sysRoleAuthId") Long sysRoleAuthId);

	/**
	 * 按主键物理删除
	 * 
	 * @param sysRoleAuthId
	 * @return
	 */
	Integer deleteById(@Param("sysRoleAuth") SysRoleAuth sysRoleAuth);

	/**
	 * 按主键物理删除
	 * 
	 * @param sysRoleAuthId
	 * @return
	 */
	Integer removeById(@Param("sysRoleAuthId") Long sysRoleAuthId);

	/**
	 * 根据角色名查询权限
	 * 
	 * @param userId
	 * @return
	 */
	List<String> selectByRoleId(@Param("roleId") Long roleId);
	
	/**
	 * 根据角色名删除
	 * 
	 * @param sysUserAuthId
	 * @return
	 */
	Integer deleteByRoleId(@Param("roleId") Long roleId);

	/**
	 * 批量新增
	 * 
	 * @param userAuthList
	 * @return
	 */
	Integer batchInsert(@Param("roleAuthList") List<SysRoleAuth> roleAuthList);
}
