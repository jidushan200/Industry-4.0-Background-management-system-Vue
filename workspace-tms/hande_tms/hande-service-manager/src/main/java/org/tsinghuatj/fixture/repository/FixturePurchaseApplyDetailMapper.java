package org.tsinghuatj.fixture.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.fixture.domain.FixturePurchaseApplyDetail;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixturePurchaseApplyDetailMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param fixturePurchaseApplyDetail
	 * @return
	 */
	Integer insert(@Param("fixturePurchaseApplyDetail") FixturePurchaseApplyDetail fixturePurchaseApplyDetail);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param fixturePurchaseApplyDetail
	 * @param fixturePurchaseApplyDetailId
	 * @return
	 */
	Integer updateActiveById(@Param("fixturePurchaseApplyDetail") FixturePurchaseApplyDetail fixturePurchaseApplyDetail, @Param("fixturePurchaseApplyDetailId") Long fixturePurchaseApplyDetailId);

	/**
	 * 查询列表
	 * 
	 * @param fixturePurchaseApplyDetail
	 * @return
	 */
	List<FixturePurchaseApplyDetail> select(@Param("fixturePurchaseApplyDetail") FixturePurchaseApplyDetail fixturePurchaseApplyDetail);
	
	/**
	 * 分页查询列表
	 * 
	 * @param fixturePurchaseApplyDetail
	 * @return
	 */
	Page<FixturePurchaseApplyDetail> selectPageList(@Param("fixturePurchaseApplyDetail") FixturePurchaseApplyDetail fixturePurchaseApplyDetail,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixturePurchaseApplyDetailId
	 * @return
	 */
	FixturePurchaseApplyDetail selectById(@Param("fixturePurchaseApplyDetailId") Long fixturePurchaseApplyDetailId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixturePurchaseApplyDetailId
	 * @return
	 */
	Integer deleteById(@Param("fixturePurchaseApplyDetail") FixturePurchaseApplyDetail fixturePurchaseApplyDetail);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param fixturePurchaseApplyDetailId
	 * @return
	 */
	Integer removeById(@Param("fixturePurchaseApplyDetailId") Long fixturePurchaseApplyDetailId);
	
	/**
	 * 查询列表
	 * 
	 * @param fixturePurchaseApplyDetail
	 * @return
	 */
	List<FixturePurchaseApplyDetail> selectByApplyId(@Param("applyId") Long applyId);
	
	
	Integer updateActiveByApplyId(@Param("applyDetail") FixturePurchaseApplyDetail applyDetail);
	
}
