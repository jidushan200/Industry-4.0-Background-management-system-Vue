package org.tsinghuatj.fixture.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.fixture.domain.FixtureMaintain;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixtureMaintainMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param fixtureMaintain
	 * @return
	 */
	Integer insert(@Param("fixtureMaintain") FixtureMaintain fixtureMaintain);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixtureMaintain
	 * @param fixtureMaintainId
	 * @return
	 */
	Integer updateActiveById(@Param("fixtureMaintain") FixtureMaintain fixtureMaintain, @Param("fixtureMaintainId") Long fixtureMaintainId);

	/**
	 * 查询列表
	 * 
	 * @param fixtureMaintain
	 * @return
	 */
	List<FixtureMaintain> select(@Param("fixtureMaintain") FixtureMaintain fixtureMaintain);
	
	/**
	 * 分页查询列表
	 * 
	 * @param fixtureMaintain
	 * @return
	 */
	Page<FixtureMaintain> selectPageList(@Param("fixtureMaintain") FixtureMaintain fixtureMaintain,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixtureMaintainId
	 * @return
	 */
	FixtureMaintain selectById(@Param("fixtureMaintainId") Long fixtureMaintainId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureMaintainId
	 * @return
	 */
	Integer deleteById(@Param("fixtureMaintain") FixtureMaintain fixtureMaintain);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixtureMaintainId
	 * @return
	 */
	Integer removeById(@Param("fixtureMaintainId") Long fixtureMaintainId);
}
