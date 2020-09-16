package org.tsinghuatj.sys.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.sys.domain.OperationLog;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface OperationLogMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param operationLog
	 * @return
	 */
	Integer insert(@Param("operationLog") OperationLog operationLog);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param operationLog
	 * @param operationLogId
	 * @return
	 */
	Integer updateActiveById(@Param("operationLog") OperationLog operationLog, @Param("operationLogId") String operationLogId);

	/**
	 * 查询列表
	 * 
	 * @param operationLog
	 * @return
	 */
	List<OperationLog> select(@Param("operationLog") OperationLog operationLog);
	
	/**
	 * 分页查询列表
	 * 
	 * @param operationLog
	 * @return
	 */
	Page<OperationLog> selectPageList(@Param("operationLog") OperationLog operationLog,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param operationLogId
	 * @return
	 */
	OperationLog selectById(@Param("operationLogId") String operationLogId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param operationLogId
	 * @return
	 */
	Integer deleteById(@Param("operationLog") OperationLog operationLog);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param operationLogId
	 * @return
	 */
	Integer removeById(@Param("operationLogId") String operationLogId);
}
