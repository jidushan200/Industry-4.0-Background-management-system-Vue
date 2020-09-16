package org.tsinghuatj.measure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.measure.domain.MeasureBase;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MeasureBaseMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param measureBase
	 * @return
	 */
	Integer insert(@Param("measureBase") MeasureBase measureBase);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param measureBase
	 * @param measureBaseId
	 * @return
	 */
	Integer updateActiveById(@Param("measureBase") MeasureBase measureBase, @Param("measureBaseId") Long measureBaseId);

	/**
	 * 查询列表
	 * 
	 * @param measureBase
	 * @return
	 */
	List<MeasureBase> select(@Param("measureBase") MeasureBase measureBase);
	
	/**
	 * 分页查询列表
	 * 
	 * @param measureBase
	 * @return
	 */
	Page<MeasureBase> selectPageList(@Param("measureBase") MeasureBase measureBase,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param measureBaseId
	 * @return
	 */
	MeasureBase selectById(@Param("measureBaseId") Long measureBaseId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param measureBaseId
	 * @return
	 */
	Integer deleteById(@Param("measureBase") MeasureBase measureBase);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param measureBaseId
	 * @return
	 */
	Integer removeById(@Param("measureBaseId") Long measureBaseId);
    /**
     * 按编码查询
     * @param measureNumber
     * @return
     */
	MeasureBase selectByNumber(String measureNumber);
	/**
	 * 量具基础信息表导入
	 */
	Integer measureBaseImport(Long userId, List<MeasureBase> measureBaseList) throws BusinessException;
	
	
	/**
	 * 量具信息同步
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	Integer measureBaseSynchro(Long userId,String measureNumber)throws BusinessException;
}
