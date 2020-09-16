package org.tsinghuatj.mould.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.mould.domain.MouldCheck;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MouldCheckMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param mouldCheck
	 * @return
	 */
	Integer insert(@Param("mouldCheck") MouldCheck mouldCheck);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param mouldCheck
	 * @param mouldCheckId
	 * @return
	 */
	Integer updateActiveById(@Param("mouldCheck") MouldCheck mouldCheck, @Param("mouldCheckId") Long mouldCheckId);

	/**
	 * 查询列表
	 * 
	 * @param mouldCheck
	 * @return
	 */
	List<MouldCheck> select(@Param("mouldCheck") MouldCheck mouldCheck);
	
	/**
	 * 分页查询列表
	 * 
	 * @param mouldCheck
	 * @return
	 */
	Page<MouldCheck> selectPageList(@Param("mouldCheck") MouldCheck mouldCheck,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param mouldCheckId
	 * @return
	 */
	MouldCheck selectById(@Param("mouldCheckId") Long mouldCheckId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldCheckId
	 * @return
	 */
	Integer deleteById(@Param("mouldCheck") MouldCheck mouldCheck);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldCheckId
	 * @return
	 */
	Integer removeById(@Param("mouldCheckId") Long mouldCheckId);
	
	/**
	 * 查询列表
	 * 
	 * @param mouldIdList
	 * @return
	 */
	List<MouldCheck> selectByMouldIdList(@Param("mouldIdList") List<Long> mouldIdList);
}
