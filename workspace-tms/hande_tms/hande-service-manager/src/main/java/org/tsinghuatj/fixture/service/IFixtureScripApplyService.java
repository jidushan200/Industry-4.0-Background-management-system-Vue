package org.tsinghuatj.fixture.service;

import java.util.List;

import org.tsinghuatj.fixture.domain.FixtureScripApply;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolApplyAudit;

/**
 * @ClassName: IFixtureScripApplyService
 * @Description: FixtureScripApply服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface IFixtureScripApplyService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param fixtureScripApply
	 * @return
	 */
	Integer insert(Long userId, FixtureScripApply fixtureScripApply) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param fixtureScripApply
	 * @param fixtureScripApplyId
	 * @return
	 */
	Integer updateActiveById(Long userId, FixtureScripApply fixtureScripApply, Long fixtureScripApplyId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param fixtureScripApply
	 * @return
	 */
	List<FixtureScripApply> select(Long userId, FixtureScripApply fixtureScripApply) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param fixtureScripApply
	 * @return
	 */
	Pagination<FixtureScripApply> selectPageList(Long userId, FixtureScripApply fixtureScripApply,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param fixtureScripApplyId
	 * @return
	 */
	FixtureScripApply selectById(Long userId, Long fixtureScripApplyId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param fixtureScripApplyId
	 * @return
	 */
	Integer deleteById(Long userId, Long fixtureScripApplyId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param fixtureScripApplyId
	 * @return
	 */
	Integer removeById(Long userId, Long fixtureScripApplyId) throws BusinessException;
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param toolScripApplyId
	 * @return
	 */
	FixtureScripApply applyGetByfullNumber(Long userId, String fixtureBarcode) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param toolPurchaseApply
	 * @param toolPurchaseApplyId
	 * @return
	 */
	Integer reportAudit(Long userId, String realName, ToolApplyAudit applyAudit, FixtureScripApply fixtureScripReport) throws BusinessException;
}
