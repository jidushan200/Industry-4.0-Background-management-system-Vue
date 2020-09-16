package org.tsinghuatj.sys.service;

import java.util.List;
import java.util.Map;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.SysMessage;

/**
 * @ClassName: ISysMessageService
 * @Description: SysMessage服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface ISysMessageService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param sysMessage
	 * @return
	 */
	Integer insert(String title, String message, Long userId, String realName, String authCode) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param sysMessage
	 * @param sysMessageId
	 * @return
	 */
	Integer updateActiveById(Long userId, SysMessage sysMessage, Long sysMessageId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param sysMessage
	 * @return
	 */
	List<SysMessage> select(Long userId, SysMessage sysMessage) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param sysMessage
	 * @return
	 */
	Pagination<SysMessage> selectPageList(Long userId, SysMessage sysMessage, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param sysMessageId
	 * @return
	 */
	SysMessage selectById(Long userId, Long sysMessageId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param sysMessageId
	 * @return
	 */
	Integer deleteById(Long userId, Long sysMessageId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param sysMessageId
	 * @return
	 */
	Integer removeById(Long userId, Long sysMessageId) throws BusinessException;

	/**
	 * 
	 * @param userId
	 * @param receiverId
	 * @return
	 * @throws BusinessException
	 */
	Integer[] selectMessageCount(Long userId, Long receiverId) throws BusinessException;

	/**
	 * 查询即时信息
	 * 
	 * @param userId
	 * @param sysMessage
	 * @return
	 */
	List<SysMessage> selectImmediateMessageList(Long userId, int second) throws BusinessException;

	/**
	 * 查询即时信息
	 * 
	 * @param userId
	 * @param sysMessage
	 * @return
	 */
	Map<String, Integer> selectToDoList(Long userId) throws BusinessException;
}
