package org.tsinghuatj.mould.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.mould.domain.MouldEmbryo;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MouldEmbryoMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param mouldEmbryo
	 * @return
	 */
	Integer insert(@Param("mouldEmbryo") MouldEmbryo mouldEmbryo);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param mouldEmbryo
	 * @param mouldEmbryoId
	 * @return
	 */
	Integer updateActiveById(@Param("mouldEmbryo") MouldEmbryo mouldEmbryo, @Param("mouldEmbryoId") Long mouldEmbryoId);

	/**
	 * 查询列表
	 * 
	 * @param mouldEmbryo
	 * @return
	 */
	List<MouldEmbryo> select(@Param("mouldEmbryo") MouldEmbryo mouldEmbryo);
	
	/**
	 * 分页查询列表
	 * 
	 * @param mouldEmbryo
	 * @return
	 */
	Page<MouldEmbryo> selectPageList(@Param("mouldEmbryo") MouldEmbryo mouldEmbryo,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param mouldEmbryoId
	 * @return
	 */
	MouldEmbryo selectById(@Param("mouldEmbryoId") Long mouldEmbryoId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldEmbryoId
	 * @return
	 */
	Integer deleteById(@Param("mouldEmbryo") MouldEmbryo mouldEmbryo);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldEmbryoId
	 * @return
	 */
	Integer removeById(@Param("mouldEmbryoId") Long mouldEmbryoId);
	
	/**
     * 按编码查询
     * @param measureNumber
     * @return
     */
	MouldEmbryo selectByCode(@Param("embryoCode") String embryoCode);
}
