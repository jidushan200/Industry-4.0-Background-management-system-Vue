package org.tsinghuatj.fixture.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.fixture.domain.FixturePurchaseApply;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixturePurchaseApplyMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param fixturePurchaseApply
	 * @return
	 */
	Integer insert(@Param("fixturePurchaseApply") FixturePurchaseApply fixturePurchaseApply);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixturePurchaseApply
	 * @param fixturePurchaseApplyId
	 * @return
	 */
	Integer updateActiveById(@Param("fixturePurchaseApply") FixturePurchaseApply fixturePurchaseApply, @Param("fixturePurchaseApplyId") Long fixturePurchaseApplyId);

	/**
	 * 查询列表
	 * 
	 * @param fixturePurchaseApply
	 * @return
	 */
	List<FixturePurchaseApply> select(@Param("fixturePurchaseApply") FixturePurchaseApply fixturePurchaseApply);
	
	/**
	 * 分页查询列表
	 * 
	 * @param fixturePurchaseApply
	 * @return
	 */
	Page<FixturePurchaseApply> selectPageList(@Param("fixturePurchaseApply") FixturePurchaseApply fixturePurchaseApply,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixturePurchaseApplyId
	 * @return
	 */
	FixturePurchaseApply selectById(@Param("fixturePurchaseApplyId") Long fixturePurchaseApplyId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixturePurchaseApplyId
	 * @return
	 */
	Integer deleteById(@Param("fixturePurchaseApply") FixturePurchaseApply fixturePurchaseApply);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixturePurchaseApplyId
	 * @return
	 */
	Integer removeById(@Param("fixturePurchaseApplyId") Long fixturePurchaseApplyId);
}
