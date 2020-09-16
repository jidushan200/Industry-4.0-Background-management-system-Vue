package org.tsinghuatj.sys.repository;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.sys.domain.MessageCount;
import org.tsinghuatj.sys.domain.SysMessage;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface SysMessageMapper {

	/**
	 * 插入数据
	 * 
	 * @param sysMessage
	 * @return
	 */
	Integer insert(@Param("sysMessage") SysMessage sysMessage);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param sysMessage
	 * @param sysMessageId
	 * @return
	 */
	Integer updateActiveById(@Param("sysMessage") SysMessage sysMessage, @Param("sysMessageId") Long sysMessageId);

	/**
	 * 查询列表
	 * 
	 * @param sysMessage
	 * @return
	 */
	List<SysMessage> select(@Param("sysMessage") SysMessage sysMessage);

	/**
	 * 分页查询列表
	 * 
	 * @param sysMessage
	 * @return
	 */
	Page<SysMessage> selectPageList(@Param("sysMessage") SysMessage sysMessage, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param sysMessageId
	 * @return
	 */
	SysMessage selectById(@Param("sysMessageId") Long sysMessageId);

	/**
	 * 按主键物理删除
	 * 
	 * @param sysMessageId
	 * @return
	 */
	Integer deleteById(@Param("sysMessage") SysMessage sysMessage);

	/**
	 * 按主键物理删除
	 * 
	 * @param sysMessageId
	 * @return
	 */
	Integer removeById(@Param("sysMessageId") Long sysMessageId);

	/**
	 * 
	 * @param receiverId
	 * @return
	 */
	List<MessageCount> selectMessageGroupCount(@Param("receiverId") Long receiverId);

	List<SysMessage> selectImmediateMessageList(@Param("receiverId") Long receiverId, @Param("beginTime") Date beginTime);

	/**
	 * 根据主键查询
	 * 
	 * @param sysMessageId
	 * @return
	 */
	int updateSendFlag(@Param("pkIdList") List<Long> pkIdList);
	/**
	 * 统计待审核的刀具申请单
	 * @param departmentId
	 * @param applyStatus
	 * @return
	 */
	int countToAuditToolPruchase(@Param("departmentId") Long departmentId, @Param("applyStatus") Integer applyStatus, @Param("purchaseType") Integer purchaseType);

	/**
	 * 统计待审核的量具申请单
	 * @param departmentId
	 * @param applyStatus
	 * @return
	 */
	int countToAuditMeasurePruchase(@Param("departmentId") Long departmentId, @Param("applyStatus") Integer applyStatus, @Param("purchaseReasion") Integer purchaseReasion);
	/**
	 * 统计待审核的夹具申请单
	 * @param departmentId
	 * @param applyStatus
	 * @return
	 */
	int countToAuditFixturePruchase(@Param("departmentId") Long departmentId, @Param("applyStatus") Integer applyStatus, @Param("purchaseType") Integer purchaseType);

	/**
	 * 统计所有待审核的刀具申请单
	 * @param departmentId
	 * @param applyStatus
	 * @return
	 */
	int countAllToAuditToolPruchase();

	/**
	 * 统计所有待审核的量具申请单
	 * @param departmentId
	 * @param applyStatus
	 * @return
	 */
	int countAllToAuditMeasurePruchase();
	/**
	 * 统计所有待审核的夹具申请单
	 * @param departmentId
	 * @param applyStatus
	 * @return
	 */
	int countAllToAuditFixturePruchase();

}
