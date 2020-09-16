package org.tsinghuatj.mould.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.mould.domain.MouldOutbound;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MouldOutboundMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param mouldOutbound
	 * @return
	 */
	Integer insert(@Param("mouldOutbound") MouldOutbound mouldOutbound);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param mouldOutbound
	 * @param mouldOutboundId
	 * @return
	 */
	Integer updateActiveById(@Param("mouldOutbound") MouldOutbound mouldOutbound, @Param("mouldOutboundId") Long mouldOutboundId);

	/**
	 * 查询列表
	 * 
	 * @param mouldOutbound
	 * @return
	 */
	List<MouldOutbound> select(@Param("mouldOutbound") MouldOutbound mouldOutbound);
	
	/**
	 * 分页查询列表
	 * 
	 * @param mouldOutbound
	 * @return
	 */
	Page<MouldOutbound> selectPageList(@Param("mouldOutbound") MouldOutbound mouldOutbound,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param mouldOutboundId
	 * @return
	 */
	MouldOutbound selectById(@Param("mouldOutboundId") Long mouldOutboundId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldOutboundId
	 * @return
	 */
	Integer deleteById(@Param("mouldOutbound") MouldOutbound mouldOutbound);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldOutboundId
	 * @return
	 */
	Integer removeById(@Param("mouldOutboundId") Long mouldOutboundId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param mouldOutboundId
	 * @return
	 */
	MouldOutbound selectRowByByMouldId(@Param("mouldId") Long mouldId);
	
	/**
	 * 查询列表
	 * 
	 * @param mouldIdList
	 * @return
	 */
	List<MouldOutbound> selectByMouldIdList(@Param("mouldIdList") List<Long> mouldIdList);
}
