package org.tsinghuatj.mould.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.mould.domain.MouldBase;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MouldBaseMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param mouldBase
	 * @return
	 */
	Integer insert(@Param("mouldBase") MouldBase mouldBase);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param mouldBase
	 * @param mouldBaseId
	 * @return
	 */
	Integer updateActiveById(@Param("mouldBase") MouldBase mouldBase, @Param("mouldBaseId") Long mouldBaseId);

	/**
	 * 查询列表
	 * 
	 * @param mouldBase
	 * @return
	 */
	List<MouldBase> select(@Param("mouldBase") MouldBase mouldBase);
	
	/**
	 * 分页查询列表
	 * 
	 * @param mouldBase
	 * @return
	 */
	Page<MouldBase> selectPageList(@Param("mouldBase") MouldBase mouldBase,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param mouldBaseId
	 * @return
	 */
	MouldBase selectById(@Param("mouldBaseId") Long mouldBaseId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldBaseId
	 * @return
	 */
	Integer deleteById(@Param("mouldBase") MouldBase mouldBase);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldBaseId
	 * @return
	 */
	Integer removeById(@Param("mouldBaseId") Long mouldBaseId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldBaseId
	 * @return
	 */
	Integer removeByNumber(@Param("mouldNumber") String mouldNumber);
	
	/**
	 * 根据模具编码查询
	 * 
	 * @param mouldNumber
	 * @return
	 */
	MouldBase selectByMouldNumber(@Param("mouldNumber") String mouldNumber,@Param("pkId") Long pkId);
}
