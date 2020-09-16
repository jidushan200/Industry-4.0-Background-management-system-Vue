package org.tsinghuatj.sys.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.sys.domain.SysAuthInfo;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface SysAuthInfoMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param sysAuthInfo
	 * @return
	 */
	Integer insert(@Param("sysAuthInfo") SysAuthInfo sysAuthInfo);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param sysAuthInfo
	 * @param sysAuthInfoId
	 * @return
	 */
	Integer updateActiveById(@Param("sysAuthInfo") SysAuthInfo sysAuthInfo, @Param("sysAuthInfoId") Long sysAuthInfoId);

	/**
	 * 查询列表
	 * 
	 * @param sysAuthInfo
	 * @return
	 */
	List<SysAuthInfo> select(@Param("sysAuthInfo") SysAuthInfo sysAuthInfo);
	
	/**
	 * 分页查询列表
	 * 
	 * @param sysAuthInfo
	 * @return
	 */
	Page<SysAuthInfo> selectPageList(@Param("sysAuthInfo") SysAuthInfo sysAuthInfo,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param sysAuthInfoId
	 * @return
	 */
	SysAuthInfo selectById(@Param("sysAuthInfoId") Long sysAuthInfoId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param sysAuthInfoId
	 * @return
	 */
	Integer deleteById(@Param("sysAuthInfo") SysAuthInfo sysAuthInfo);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param sysAuthInfoId
	 * @return
	 */
	Integer removeById(@Param("sysAuthInfoId") Long sysAuthInfoId);
	
	/**
	 * 根据角色查询权限列表
	 * 
	 * @param sysAuthInfo
	 * @return
	 */
	List<SysAuthInfo> selectByRoleId(@Param("roleId") Long roleId);
	
	/**
	 * 查询列表
	 * 
	 * @param sysUserAuth
	 * @return
	 */
	List<SysAuthInfo> selectByUserId(@Param("userId") Long userId);
}
