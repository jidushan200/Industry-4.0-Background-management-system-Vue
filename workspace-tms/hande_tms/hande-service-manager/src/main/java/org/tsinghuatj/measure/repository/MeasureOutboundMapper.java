package org.tsinghuatj.measure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.measure.domain.MeasureOutbound;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MeasureOutboundMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param measureOutbound
	 * @return
	 */
	Integer insert(@Param("measureOutbound") MeasureOutbound measureOutbound);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param measureOutbound
	 * @param measureOutboundId
	 * @return
	 */
	Integer updateActiveById(@Param("measureOutbound") MeasureOutbound measureOutbound, @Param("measureOutboundId") Long measureOutboundId);

	/**
	 * 查询列表
	 * 
	 * @param measureOutbound
	 * @return
	 */
	List<MeasureOutbound> select(@Param("measureOutbound") MeasureOutbound measureOutbound);
	
	/**
	 * 分页查询列表
	 * 
	 * @param measureOutbound
	 * @return
	 */
	Page<MeasureOutbound> selectPageList(@Param("measureOutbound") MeasureOutbound measureOutbound,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param measureOutboundId
	 * @return
	 */
	MeasureOutbound selectById(@Param("measureOutboundId") Long measureOutboundId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param measureOutboundId
	 * @return
	 */
	Integer deleteById(@Param("measureOutbound") MeasureOutbound measureOutbound);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param measureOutboundId
	 * @return
	 */
	Integer removeById(@Param("measureOutboundId") Long measureOutboundId);
	
	/**
	 * 查询列表
	 * 
	 * @param measureIdList
	 * @return
	 */
	List<MeasureOutbound> selectByMeasureIdList(@Param("measureIdList") List<Long> measureIdList);
}
