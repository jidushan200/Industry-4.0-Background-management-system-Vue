package org.tsinghuatj.mould.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.mould.domain.MouldCheckItem;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MouldCheckItemMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param mouldCheckItem
	 * @return
	 */
	Integer insert(@Param("mouldCheckItem") MouldCheckItem mouldCheckItem);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param mouldCheckItem
	 * @param mouldCheckItemId
	 * @return
	 */
	Integer updateActiveById(@Param("mouldCheckItem") MouldCheckItem mouldCheckItem, @Param("mouldCheckItemId") Long mouldCheckItemId);

	/**
	 * 查询列表
	 * 
	 * @param mouldCheckItem
	 * @return
	 */
	List<MouldCheckItem> select(@Param("mouldCheckItem") MouldCheckItem mouldCheckItem);
	
	/**
	 * 分页查询列表
	 * 
	 * @param mouldCheckItem
	 * @return
	 */
	Page<MouldCheckItem> selectPageList(@Param("mouldCheckItem") MouldCheckItem mouldCheckItem,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param mouldCheckItemId
	 * @return
	 */
	MouldCheckItem selectById(@Param("mouldCheckItemId") Long mouldCheckItemId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldCheckItemId
	 * @return
	 */
	Integer deleteById(@Param("mouldCheckItem") MouldCheckItem mouldCheckItem);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldCheckItemId
	 * @return
	 */
	Integer removeById(@Param("mouldCheckItemId") Long mouldCheckItemId);
	
	/**
	 * 查询列表
	 * 
	 * @param mouldCheckItem
	 * @return
	 */
	List<MouldCheckItem> selectByCheckId(@Param("checkId") Long checkId);
	
}
