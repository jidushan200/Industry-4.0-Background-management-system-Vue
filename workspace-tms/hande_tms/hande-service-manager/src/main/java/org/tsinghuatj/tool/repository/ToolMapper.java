package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.tool.domain.Tool;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param tool
	 * @return
	 */
	Integer insert(@Param("tool") Tool tool);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param tool
	 * @param toolId
	 * @return
	 */
	Integer updateActiveById(@Param("tool") Tool tool, @Param("toolId") Long toolId);

	/**
	 * 查询列表
	 * 
	 * @param tool
	 * @return
	 */
	List<Tool> select(@Param("tool") Tool tool);
	
	/**
	 * 分页查询列表
	 * 
	 * @param tool
	 * @return
	 */
	Page<Tool> selectPageList(@Param("tool") Tool tool,@Param("queryDto") QueryDto queryDto);
	
	

	/**
	 * 分页查询待检刀具列表(刃磨涂层)
	 * 
	 * @param tool
	 * @return
	 */
	Page<Tool> selectWaitCheckPageList(@Param("tool") Tool tool,@Param("queryDto") QueryDto queryDto);
	

	/**
	 * 分页查询待检刀具列表(刃磨涂层)
	 * 
	 * @param tool
	 * @return
	 */
	Page<Tool> selectWaitRepairPageList(@Param("tool") Tool tool,@Param("queryDto") QueryDto queryDto);
	
	
	/**
	 * 分页查询待检刀具列表(新刀)
	 * 
	 * @param tool
	 * @return
	 */
	Page<Tool> selectNewWaitCheckPageList(@Param("tool") Tool tool,@Param("queryDto") QueryDto queryDto);
	
	/**
	 * 分页查询待检刀具列表(刃磨涂层新刀)
	 * 
	 * @param tool
	 * @return
	 */
	Page<Tool> selectUnionWaitCheckPageList(@Param("tool") Tool tool,@Param("queryDto") QueryDto queryDto);
	

	/**
	 * 根据主键查询
	 * 
	 * @param toolId
	 * @return
	 */
	Tool selectById(@Param("toolId") Long toolId);
	
	/**
	 * 根据编号查询
	 * 
	 * @param toolNumber
	 * @return
	 */
	Tool selectByFullNumber(@Param("fullNumber") String fullNumber);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolId
	 * @return
	 */
	Integer deleteById(@Param("tool") Tool tool);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolId
	 * @return
	 */
	Integer removeById(@Param("toolId") Long toolId);
	
	/**
	 * 根据刀具序号查询
	 * 
	 * @param toolId
	 * @return
	 */
	Tool selectSeqMax(@Param("tool") Tool tool);
	/**
	 * 根据删除标识查询
	 */
	Page<Tool> selectBydelMark(@Param("tool") Tool tool,@Param("queryDto") QueryDto queryDto);
	
	/**
	 * 根据主键查询
	 * 
	 * @param toolNumber
	 * @return
	 */
	Tool toolGetSeqByToolNumber(@Param("toolNumber") String toolNumber);
	
	/**
	 * 根据主键查询
	 * 
	 * @param toolNumber
	 * @return
	 */
	List<Tool> toolGetByToolNumber(@Param("toolNumber") String toolNumber);
	
	Integer countByToolNumber(@Param("toolNumber") String toolNumber);
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param tool
	 * @param toolId
	 * @return
	 */
	Integer updateActiveByNumber(@Param("tool") Tool tool);
	
	/**
	 * 分页查询列表
	 * 
	 * @param tool
	 * @return
	 */
	Page<Tool> selectStatisticsPageList(@Param("tool") Tool tool,@Param("queryDto") QueryDto queryDto);
	
}
