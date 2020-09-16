package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.CheckResult;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface CheckResultMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param checkResult
	 * @return
	 */
	Integer insert(@Param("checkResult") CheckResult checkResult);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param checkResult
	 * @param checkResultId
	 * @return
	 */
	Integer updateActiveById(@Param("checkResult") CheckResult checkResult, @Param("checkResultId") Long checkResultId);

	/**
	 * 查询列表
	 * 
	 * @param checkResult
	 * @return
	 */
	List<CheckResult> select(@Param("checkResult") CheckResult checkResult);
	
	/**
	 * 分页查询列表
	 * 
	 * @param checkResult
	 * @return
	 */
	Page<CheckResult> selectPageList(@Param("checkResult") CheckResult checkResult,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param checkResultId
	 * @return
	 */
	CheckResult selectById(@Param("checkResultId") Long checkResultId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param checkResultId
	 * @return
	 */
	Integer deleteById(@Param("checkResult") CheckResult checkResult);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param checkResultId
	 * @return
	 */
	Integer removeById(@Param("checkResultId") Long checkResultId);
}
