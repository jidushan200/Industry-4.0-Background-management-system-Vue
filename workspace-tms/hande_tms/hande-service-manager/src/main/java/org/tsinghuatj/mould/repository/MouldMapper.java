package org.tsinghuatj.mould.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.mould.domain.Mould;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MouldMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param mould
	 * @return
	 */
	Integer insert(@Param("mould") Mould mould);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param mould
	 * @param mouldId
	 * @return
	 */
	Integer updateActiveById(@Param("mould") Mould mould, @Param("mouldId") Long mouldId);

	/**
	 * 查询列表
	 * 
	 * @param mould
	 * @return
	 */
	List<Mould> select(@Param("mould") Mould mould);
	
	/**
	 * 分页查询列表
	 * 
	 * @param mould
	 * @return
	 */
	Page<Mould> selectPageList(@Param("mould") Mould mould,@Param("queryDto") QueryDto queryDto);
	

	/**
	 * 分页查询列表
	 * 
	 * @param mould
	 * @return
	 */
	Page<Mould> selectLifePageList(@Param("mould") Mould mould,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param mouldId
	 * @return
	 */
	Mould selectById(@Param("mouldId") Long mouldId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldId
	 * @return
	 */
	Integer deleteById(@Param("mould") Mould mould);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldId
	 * @return
	 */
	Integer removeById(@Param("mouldId") Long mouldId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param mouldId
	 * @return
	 */
	Mould selectByFullNumber(@Param("fullNumber") String fullNumber);
	
	/**
	 * 根据主键查询
	 * 
	 * @param mouldNumber
	 * @return
	 */
	Mould selectSeqByNumber(@Param("mouldNumber") String mouldNumber);
}
