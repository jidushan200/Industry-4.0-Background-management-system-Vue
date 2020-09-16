package org.tsinghuatj.sys.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.sys.domain.UserAccount;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface UserAccountMapper {

	/**
	 * 插入数据
	 * 
	 * @param userAccount
	 * @return
	 */
	Integer insert(@Param("userAccount") UserAccount userAccount);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userAccount
	 * @param userAccountId
	 * @return
	 */
	Integer updateActiveById(@Param("userAccount") UserAccount userAccount, @Param("userAccountId") Long userAccountId);

	/**
	 * 查询列表
	 * 
	 * @param userAccount
	 * @return
	 */
	List<UserAccount> select(@Param("userAccount") UserAccount userAccount);

	/**
	 * 分页查询列表
	 * 
	 * @param userAccount
	 * @return
	 */
	Page<UserAccount> selectPageList(@Param("userAccount") UserAccount userAccount, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param userAccountId
	 * @return
	 */
	UserAccount selectById(@Param("userAccountId") Long userAccountId);

	/**
	 * 按主键物理删除
	 * 
	 * @param userAccountId
	 * @return
	 */
	Integer deleteById(@Param("userAccount") UserAccount userAccount);

	/**
	 * 按主键物理删除
	 * 
	 * @param userAccountId
	 * @return
	 */
	Integer removeById(@Param("userAccountId") Long userAccountId);

	/**
	 * 批量删除
	 * 
	 * @param ids
	 *            id多个，已进行,分割
	 * 
	 * @return
	 */
	Integer batchDeleteAccountByIds(@Param("ids") List<Long> ids);

	/**
	 * 查询列表
	 * 
	 * @param userAccount
	 * @return
	 */
	List<UserAccount> selectByIdList(@Param("idList") List<Long> idList);

	/**
	 * 
	 * @param loginName
	 * @param pkId
	 * @return
	 */
	UserAccount checkLoginName(@Param("loginName") String loginName, @Param("pkId") Long pkId);
	
	
}
