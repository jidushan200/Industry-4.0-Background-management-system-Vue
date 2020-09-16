package org.tsinghuatj.base.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.base.domain.PartName;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface PartNameMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param partName
	 * @return
	 */
	Integer insert(@Param("partName") PartName partName);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param partName
	 * @param partNameId
	 * @return
	 */
	Integer updateActiveById(@Param("partName") PartName partName, @Param("partNameId") Long partNameId);

	/**
	 * 查询列表
	 * 
	 * @param partName
	 * @return
	 */
	List<PartName> select(@Param("partName") PartName partName);
	
	/**
	 * 分页查询列表
	 * 
	 * @param partName
	 * @return
	 */
	Page<PartName> selectPageList(@Param("partName") PartName partName,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param partNameId
	 * @return
	 */
	PartName selectById(@Param("partNameId") Long partNameId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param partNameId
	 * @return
	 */
	Integer deleteById(@Param("partName") PartName partName);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param partNameId
	 * @return
	 */
	Integer removeById(@Param("partNameId") Long partNameId);
	
	
	/**
	 * 查询列表
	 * 
	 * @param part
	 * @return
	 */
	List<PartName> selectByNameList(@Param("nameList")List<String> nameList);
}
