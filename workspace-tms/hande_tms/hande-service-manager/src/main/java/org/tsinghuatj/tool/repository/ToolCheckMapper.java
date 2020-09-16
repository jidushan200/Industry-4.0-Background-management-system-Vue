package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.ToolCheck;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolCheckMapper {

	/**
	 * 插入数据
	 * 
	 * @param toolCheck
	 * @return
	 */
	Integer insert(@Param("toolCheck") ToolCheck toolCheck);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolCheck
	 * @param toolCheckId
	 * @return
	 */
	Integer updateActiveById(@Param("toolCheck") ToolCheck toolCheck, @Param("toolCheckId") Long toolCheckId);

	/**
	 * 查询列表
	 * 
	 * @param toolCheck
	 * @return
	 */
	List<ToolCheck> select(@Param("toolCheck") ToolCheck toolCheck);

	/**
	 * 分页查询列表
	 * 
	 * @param toolCheck
	 * @return
	 */
	Page<ToolCheck> selectPageList(@Param("toolCheck") ToolCheck toolCheck, @Param("queryDto") QueryDto queryDto);

	

	/**
	 * 根据主键查询
	 * 
	 * @param toolCheckId
	 * @return
	 */
	ToolCheck selectById(@Param("toolCheckId") Long toolCheckId);

	/**
	 * 根据刀具id查询
	 * 
	 * @param toolId
	 * @param checkType
	 * @return
	 */
	ToolCheck selectByToolId(@Param("toolId") Long toolId, @Param("checkType") Integer checkType);

	/**
	 * 根据刀具id查询
	 * 
	 * @param toolId
	 * @param checkType
	 * @return
	 */
	ToolCheck selectBySeq(@Param("toolNumber") String toolNumber, @Param("toolSeq") String toolSeq);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolCheckId
	 * @return
	 */
	Integer deleteById(@Param("toolCheck") ToolCheck toolCheck);

	/**
	 * 按主键物理删除
	 * 
	 * @param toolCheckId
	 * @return
	 */
	Integer removeById(@Param("toolCheckId") Long toolCheckId);
}
