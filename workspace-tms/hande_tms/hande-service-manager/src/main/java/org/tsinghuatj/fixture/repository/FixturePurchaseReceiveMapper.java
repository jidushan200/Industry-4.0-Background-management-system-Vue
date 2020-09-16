package org.tsinghuatj.fixture.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.fixture.domain.FixturePurchaseApply;
import org.tsinghuatj.framework.domain.QueryDto;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface FixturePurchaseReceiveMapper {

	/**
	 * 分页查询列表
	 * 
	 * @param fixturePurchaseApply
	 * @return
	 */
	Page<FixturePurchaseApply> selectPageList(@Param("fixturePurchaseApply") FixturePurchaseApply fixturePurchaseApply, @Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param fixturePurchaseApplyId
	 * @return
	 */
	FixturePurchaseApply selectById(@Param("fixturePurchaseApplyId") Long fixturePurchaseApplyId);

}
