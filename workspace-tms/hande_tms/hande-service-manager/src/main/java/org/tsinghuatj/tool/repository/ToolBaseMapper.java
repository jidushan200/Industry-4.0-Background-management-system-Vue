package org.tsinghuatj.tool.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.tool.domain.ToolBase;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ToolBaseMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param toolBase
	 * @return
	 */
	Integer insert(@Param("toolBase") ToolBase toolBase);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param toolBase
	 * @param toolBaseId
	 * @return
	 */
	Integer updateActiveById(@Param("toolBase") ToolBase toolBase, @Param("toolBaseId") Long toolBaseId);

	/**
	 * 查询列表
	 * 
	 * @param toolBase
	 * @return
	 */
	List<ToolBase> select(@Param("toolBase") ToolBase toolBase);
	
	/**
	 * 分页查询列表
	 * 
	 * @param toolBase
	 * @return
	 */
	Page<ToolBase> selectPageList(@Param("toolBase") ToolBase toolBase,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param toolBaseId
	 * @return
	 */
	ToolBase selectById(@Param("toolBaseId") Long toolBaseId);
	
	/**
	 * 根据刀具编码查询
	 * 
	 * @param toolBaseId
	 * @return
	 */
	ToolBase selectByToolNumber(@Param("toolNumber") String toolNumber,@Param("pkId") Long pkId);
	
	/**
	 * 根据刀具编码查询
	 * 
	 * @param toolBaseId
	 * @return
	 */
	ToolBase selectByToolName(@Param("toolName") String toolName,@Param("pkId") Long pkId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolBaseId
	 * @return
	 */
	Integer deleteById(@Param("toolBase") ToolBase toolBase);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param toolBaseId
	 * @return
	 */
	Integer removeById(@Param("toolBaseId") Long toolBaseId);
	

	/**
	 * 查询列表
	 * 
	 * @param ToolBase
	 * @return
	 */
	List<ToolBase> selectByNumberList(@Param("numberList") List<String> numberList);

    /**
     * 校验图号
     * @param toolMap
     * @return
     */
	int toolMapValidate(String toolMap);

	/**
	 * 查询列表
	 * 
	 * @param toolBase
	 * @return
	 */
	List<ToolBase> selectMapList(@Param("toolBase") ToolBase toolBase);

}
