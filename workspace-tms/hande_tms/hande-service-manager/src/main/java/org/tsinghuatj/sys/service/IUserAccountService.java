package org.tsinghuatj.sys.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.UserAccount;

/**
 * @ClassName: IUserAccountService
 * @Description: UserAccount服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IUserAccountService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param userAccount
	 * @return
	 */
	Integer insert(Long userId, UserAccount userAccount, List<String> authCodes) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param userAccount
	 * @param userAccountId
	 * @return
	 */
	Integer updateActiveById(Long userId, UserAccount userAccount, Long userAccountId, List<String> authCodes)
			throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param userAccount
	 * @return
	 */
	List<UserAccount> select(Long userId, UserAccount userAccount) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param userAccount
	 * @return
	 */
	Pagination<UserAccount> selectPageList(Long userId, UserAccount userAccount, QueryDto queryDto)
			throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param userAccountId
	 * @return
	 */
	UserAccount selectById(Long userId, Long userAccountId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param userAccountId
	 * @return
	 */
	Integer deleteById(Long userId, Long userAccountId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param userAccountId
	 * @return
	 */
	Integer removeById(Long userId, Long userAccountId) throws BusinessException;

	/**
	 * 通过id批量删除用户
	 * 
	 * @param userId
	 *            用户编号
	 * 
	 * @return
	 * @throws BusinessException
	 */
	Integer batchDeleteAccountByIds(Long userId, String ids) throws BusinessException;

	/**
	 * 根据权限码获取用户
	 * 
	 * @param userId
	 * @param authCode
	 * @return
	 * @throws BusinessException
	 */
	List<Long> getUserIdByAuthCode(Long userId, String authCode) throws BusinessException;
	
	/**
	 * 
	 * @param loginName
	 * @param pkId
	 * @return
	 * @throws BusinessException
	 */
	boolean checkLoginName(String loginName,Long pkId)throws BusinessException;
	
	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param userAccount
	 * @return
	 */
	List<UserAccount> selectCompletePageList(Long userId, UserAccount userAccount) throws BusinessException;

}
