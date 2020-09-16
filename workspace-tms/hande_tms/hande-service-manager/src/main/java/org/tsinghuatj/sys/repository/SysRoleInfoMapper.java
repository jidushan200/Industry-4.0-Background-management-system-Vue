package org.tsinghuatj.sys.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.sys.domain.SysRoleInfo;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface SysRoleInfoMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param sysRoleInfo
	 * @return
	 */
	Integer insert(@Param("sysRoleInfo") SysRoleInfo sysRoleInfo);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param sysRoleInfo
	 * @param sysRoleInfoId
	 * @return
	 */
	Integer updateActiveById(@Param("sysRoleInfo") SysRoleInfo sysRoleInfo, @Param("sysRoleInfoId") Long sysRoleInfoId);

	/**
	 * 查询列表
	 * 
	 * @param sysRoleInfo
	 * @return
	 */
	List<SysRoleInfo> select(@Param("sysRoleInfo") SysRoleInfo sysRoleInfo);
	
	/**
	 * 分页查询列表
	 * 
	 * @param sysRoleInfo
	 * @return
	 */
	Page<SysRoleInfo> selectPageList(@Param("sysRoleInfo") SysRoleInfo sysRoleInfo,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param sysRoleInfoId
	 * @return
	 */
	SysRoleInfo selectById(@Param("sysRoleInfoId") Long sysRoleInfoId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param sysRoleInfoId
	 * @return
	 */
	Integer deleteById(@Param("sysRoleInfo") SysRoleInfo sysRoleInfo);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param sysRoleInfoId
	 * @return
	 */
	Integer removeById(@Param("sysRoleInfoId") Long sysRoleInfoId);
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param sysRoleInfo
	 * @param sysRoleInfoId
	 * @return
	 */
	Integer updateRoleAuth(@Param("roleAuth") String roleAuth, @Param("pkId") Long pkId);	
	
	/**
	 * 查询角色关联的用户数
	 * 
	 * @param sysRoleInfoId
	 * @return
	 */
	int selectUserCountByRoleId(@Param("roleId") Long roleId);
}
