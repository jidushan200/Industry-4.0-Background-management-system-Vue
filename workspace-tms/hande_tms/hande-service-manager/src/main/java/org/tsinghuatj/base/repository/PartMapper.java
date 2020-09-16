package org.tsinghuatj.base.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.base.domain.Part;
import org.tsinghuatj.framework.domain.QueryDto;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface PartMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param part
	 * @return
	 */
	Integer insert(@Param("part") Part part);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param part
	 * @param partId
	 * @return
	 */
	Integer updateActiveById(@Param("part") Part part, @Param("partId") Long partId);

	/**
	 * 查询列表
	 * 
	 * @param part
	 * @return
	 */
	List<Part> select(@Param("part") Part part);
	
	/**
	 * 分页查询列表
	 * 
	 * @param part
	 * @return
	 */
	Page<Part> selectPageList(@Param("part") Part part,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param partId
	 * @return
	 */
	Part selectById(@Param("partId") Long partId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param partId
	 * @return
	 */
	Integer deleteById(@Param("part") Part part);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param partId
	 * @return
	 */
	Integer removeById(@Param("partId") Long partId);
	
	/**
	 * 根据主键查询
	 * 
	 * @param partId
	 * @return
	 */
	Part selectByPartCode(@Param("partCode") String partCode,@Param("pkId") Long pkId);
	
	List<Part> selectByPartCodeArray(@Param("codeArray") String[] codeArray);
	
}
