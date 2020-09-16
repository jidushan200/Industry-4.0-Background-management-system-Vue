package org.tsinghuatj.mould.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.mould.domain.MouldPart;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MouldPartMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param mouldPart
	 * @return
	 */
	Integer insert(@Param("mouldPart") MouldPart mouldPart);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param mouldPart
	 * @param mouldPartId
	 * @return
	 */
	Integer updateActiveById(@Param("mouldPart") MouldPart mouldPart, @Param("mouldPartId") Long mouldPartId);

	/**
	 * 查询列表
	 * 
	 * @param mouldPart
	 * @return
	 */
	List<MouldPart> select(@Param("mouldPart") MouldPart mouldPart);
	
	/**
	 * 分页查询列表
	 * 
	 * @param mouldPart
	 * @return
	 */
	Page<MouldPart> selectPageList(@Param("mouldPart") MouldPart mouldPart,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param mouldPartId
	 * @return
	 */
	MouldPart selectById(@Param("mouldPartId") Long mouldPartId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldPartId
	 * @return
	 */
	Integer deleteById(@Param("mouldPart") MouldPart mouldPart);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldPartId
	 * @return
	 */
	Integer removeById(@Param("mouldPartId") Long mouldPartId);
	
	/**
	 * 
	 * @param pkId
	 * @param toolId
	 * @param partId
	 * @return
	 */
	MouldPart checkMouldPart(@Param("pkId") Long pkId, @Param("mouldId") Long mouldId, @Param("partId") Long partId);
}
