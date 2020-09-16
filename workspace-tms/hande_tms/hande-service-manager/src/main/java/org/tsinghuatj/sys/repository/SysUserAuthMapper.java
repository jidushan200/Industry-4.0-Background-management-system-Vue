package org.tsinghuatj.sys.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.sys.domain.SysUserAuth;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface SysUserAuthMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param sysUserAuth
	 * @return
	 */
	Integer insert(@Param("sysUserAuth") SysUserAuth sysUserAuth);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param sysUserAuth
	 * @param sysUserAuthId
	 * @return
	 */
	Integer updateActiveById(@Param("sysUserAuth") SysUserAuth sysUserAuth, @Param("sysUserAuthId") Long sysUserAuthId);

	/**
	 * 查询列表
	 * 
	 * @param sysUserAuth
	 * @return
	 */
	List<SysUserAuth> select(@Param("sysUserAuth") SysUserAuth sysUserAuth);
	
	/**
	 * 分页查询列表
	 * 
	 * @param sysUserAuth
	 * @return
	 */
	Page<SysUserAuth> selectPageList(@Param("sysUserAuth") SysUserAuth sysUserAuth,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param sysUserAuthId
	 * @return
	 */
	SysUserAuth selectById(@Param("sysUserAuthId") Long sysUserAuthId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param sysUserAuthId
	 * @return
	 */
	Integer deleteById(@Param("sysUserAuth") SysUserAuth sysUserAuth);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param sysUserAuthId
	 * @return
	 */
	Integer removeById(@Param("sysUserAuthId") Long sysUserAuthId);
	
	/**
	 * 根据用户名删除
	 * 
	 * @param sysUserAuthId
	 * @return
	 */
	Integer deleteByUserId(@Param("userId") Long userId);
	/**
	 * 根据用户名查询权限
	 * @param userId
	 * @return
	 */
	List<String> selectByUserId(@Param("userId") Long userId);

	/**
	 * 批量新增
	 * 
	 * @param userAuthList
	 * @return
	 */
	Integer batchInsert(@Param("userAuthList") List<SysUserAuth> userAuthList);
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	List<Long> selectUserIdByAuthCode(@Param("authCode") String authCode);
	
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	List<String> selectAuditAuthCode(@Param("userId") Long userId,@Param("authArray") String[] authArray);
	
}
