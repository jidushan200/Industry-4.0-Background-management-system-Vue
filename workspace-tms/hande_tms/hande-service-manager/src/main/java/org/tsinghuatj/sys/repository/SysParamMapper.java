package org.tsinghuatj.sys.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.sys.domain.SysParam;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface SysParamMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param sysParam
	 * @return
	 */
	Integer insert(@Param("sysParam") SysParam sysParam);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param sysParam
	 * @param sysParamId
	 * @return
	 */
	Integer updateActiveById(@Param("sysParam") SysParam sysParam, @Param("sysParamId") Long sysParamId);

	/**
	 * 查询列表
	 * 
	 * @param sysParam
	 * @return
	 */
	List<SysParam> select(@Param("sysParam") SysParam sysParam);
	
	/**
	 * 分页查询列表
	 * 
	 * @param sysParam
	 * @return
	 */
	Page<SysParam> selectPageList(@Param("sysParam") SysParam sysParam,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param sysParamId
	 * @return
	 */
	SysParam selectById(@Param("sysParamId") Long sysParamId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param sysParamId
	 * @return
	 */
	SysParam selectByParamKey(@Param("paramKey") String paramKey);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param sysParamId
	 * @return
	 */
	Integer deleteById(@Param("sysParam") SysParam sysParam);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param sysParamId
	 * @return
	 */
	Integer removeById(@Param("sysParamId") Long sysParamId);
}
