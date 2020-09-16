package org.tsinghuatj.measure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.measure.domain.MeasureCheck;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MeasureCheckMapper {

	/**
	 * 插入数据
	 * 
	 * @param measureCheck
	 * @return
	 */
	Integer insert(@Param("measureCheck") MeasureCheck measureCheck);

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param measureCheck
	 * @param measureCheckId
	 * @return
	 */
	Integer updateActiveById(@Param("measureCheck") MeasureCheck measureCheck, @Param("measureCheckId") Long measureCheckId);

	/**
	 * 查询列表
	 * 
	 * @param measureCheck
	 * @return
	 */
	List<MeasureCheck> select(@Param("measureCheck") MeasureCheck measureCheck);

	/**
	 * 分页查询列表
	 * 
	 * @param measureCheck
	 * @return
	 */
	Page<MeasureCheck> selectPageList(@Param("measureCheck") MeasureCheck measureCheck, @Param("queryDto") QueryDto queryDto);

	/**
	 * 分页查询列表
	 * 
	 * @param measureCheck
	 * @return
	 */
	Page<MeasureCheck> selectNewPageList(@Param("measureCheck") MeasureCheck measureCheck, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param measureCheckId
	 * @return
	 */
	MeasureCheck selectById(@Param("measureCheckId") Long measureCheckId);

	/**
	 * 按主键物理删除
	 * 
	 * @param measureCheckId
	 * @return
	 */
	Integer deleteById(@Param("measureCheck") MeasureCheck measureCheck);

	/**
	 * 按主键物理删除
	 * 
	 * @param measureCheckId
	 * @return
	 */
	Integer removeById(@Param("measureCheckId") Long measureCheckId);

	/**
	 * 查询列表
	 * 
	 * @param measureIdList
	 * @return
	 */
	List<MeasureCheck> selectByMeasureIdList(@Param("measureIdList") List<Long> measureIdList);
}
