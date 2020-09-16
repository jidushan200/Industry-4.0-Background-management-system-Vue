package org.tsinghuatj.measure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.measure.domain.MeasureCalibration;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MeasureCalibrationMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param measureCalibration
	 * @return
	 */
	Integer insert(@Param("measureCalibration") MeasureCalibration measureCalibration);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param measureCalibration
	 * @param measureCalibrationId
	 * @return
	 */
	Integer updateActiveById(@Param("measureCalibration") MeasureCalibration measureCalibration, @Param("measureCalibrationId") Long measureCalibrationId);

	/**
	 * 查询列表
	 * 
	 * @param measureCalibration
	 * @return
	 */
	List<MeasureCalibration> select(@Param("measureCalibration") MeasureCalibration measureCalibration);
	
	/**
	 * 分页查询列表
	 * 
	 * @param measureCalibration
	 * @return
	 */
	Page<MeasureCalibration> selectPageList(@Param("measureCalibration") MeasureCalibration measureCalibration,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param measureCalibrationId
	 * @return
	 */
	MeasureCalibration selectById(@Param("measureCalibrationId") Long measureCalibrationId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param measureCalibrationId
	 * @return
	 */
	Integer deleteById(@Param("measureCalibration") MeasureCalibration measureCalibration);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param measureCalibrationId
	 * @return
	 */
	Integer removeById(@Param("measureCalibrationId") Long measureCalibrationId);
}
