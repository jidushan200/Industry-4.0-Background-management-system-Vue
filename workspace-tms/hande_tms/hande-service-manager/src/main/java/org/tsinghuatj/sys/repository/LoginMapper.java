package org.tsinghuatj.sys.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.sys.domain.SysAuthInfo;
import org.tsinghuatj.sys.domain.UserAccount;

@Mapper
@Repository
public interface LoginMapper {
	/**
	 * 根据账户名查询用户信息
	 * 
	 * @param userName
	 * @return
	 */

	UserAccount getUserInfoByName(@Param("loginName") String loginName);

	/**
	 * 根据账户名查询用户信息
	 * 
	 * @param userName
	 * @return
	 */
	UserAccount getUserInfoById(@Param("pkId") Long pkId);

	/**
	 * @param userId
	 * @return
	 */

	List<SysAuthInfo> listAuthListByUserId(@Param("userId") Long userId);
	
	/**
	 * @param userId
	 * @return
	 */

	List<String> listUserAuths(@Param("userId") Long userId);
}
