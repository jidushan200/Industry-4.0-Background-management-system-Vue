package org.tsinghuatj.measure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.measure.domain.Measure;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MeasureMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param measure
	 * @return
	 */
	Integer insert(@Param("measure") Measure measure);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param measure
	 * @param measureId
	 * @return
	 */
	Integer updateActiveById(@Param("measure") Measure measure, @Param("measureId") Long measureId);

	/**
	 * 查询列表
	 * 
	 * @param measure
	 * @return
	 */
	List<Measure> select(@Param("measure") Measure measure);
	
	/**
	 * 分页查询列表
	 * 
	 * @param measure
	 * @return
	 */
	Page<Measure> selectPageList(@Param("measure") Measure measure,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param measureId
	 * @return
	 */
	Measure selectById(@Param("measureId") Long measureId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param measureId
	 * @return
	 */
	Integer deleteById(@Param("measure") Measure measure);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param measureId
	 * @return
	 */
	Integer removeById(@Param("measureId") Long measureId);
	
	Integer countByMeasureNumber(@Param("measureNumber") String measureNumber);
	
	Measure getSeqByMeasureNumber(@Param("measureNumber") String measureNumber);
	
	/**
	 * 根据主键查询
	 * 
	 * @param measureId
	 * @return
	 */
	Measure selectByFullNumber(@Param("fullNumber") String fullNumber);
	
	/**
	 * 分页查询列表
	 * 
	 * @param measure
	 * @return
	 */
	Page<Measure> selectScripPageList(@Param("measure") Measure measure,@Param("queryDto") QueryDto queryDto);
}
