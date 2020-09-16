package org.tsinghuatj.mould.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.mould.domain.MouldScripApply;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MouldScripApplyMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param mouldScripApply
	 * @return
	 */
	Integer insert(@Param("mouldScripApply") MouldScripApply mouldScripApply);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param mouldScripApply
	 * @param mouldScripApplyId
	 * @return
	 */
	Integer updateActiveById(@Param("mouldScripApply") MouldScripApply mouldScripApply, @Param("mouldScripApplyId") Long mouldScripApplyId);

	/**
	 * 查询列表
	 * 
	 * @param mouldScripApply
	 * @return
	 */
	List<MouldScripApply> select(@Param("mouldScripApply") MouldScripApply mouldScripApply);
	
	/**
	 * 分页查询列表
	 * 
	 * @param mouldScripApply
	 * @return
	 */
	Page<MouldScripApply> selectPageList(@Param("mouldScripApply") MouldScripApply mouldScripApply,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param mouldScripApplyId
	 * @return
	 */
	MouldScripApply selectById(@Param("mouldScripApplyId") Long mouldScripApplyId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldScripApplyId
	 * @return
	 */
	Integer deleteById(@Param("mouldScripApply") MouldScripApply mouldScripApply);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldScripApplyId
	 * @return
	 */
	Integer removeById(@Param("mouldScripApplyId") Long mouldScripApplyId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param mouldScripApplyId
	 * @return
	 */
	MouldScripApply selectByFullNumber(@Param("fullNumber") String fullNumber);
}
