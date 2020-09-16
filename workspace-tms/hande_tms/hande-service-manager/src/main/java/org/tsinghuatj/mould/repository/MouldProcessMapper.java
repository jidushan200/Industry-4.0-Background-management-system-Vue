package org.tsinghuatj.mould.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.mould.domain.MouldProcess;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface MouldProcessMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param mouldProcess
	 * @return
	 */
	Integer insert(@Param("mouldProcess") MouldProcess mouldProcess);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param mouldProcess
	 * @param mouldProcessId
	 * @return
	 */
	Integer updateActiveById(@Param("mouldProcess") MouldProcess mouldProcess, @Param("mouldProcessId") Long mouldProcessId);

	/**
	 * 查询列表
	 * 
	 * @param mouldProcess
	 * @return
	 */
	List<MouldProcess> select(@Param("mouldProcess") MouldProcess mouldProcess);
	
	/**
	 * 分页查询列表
	 * 
	 * @param mouldProcess
	 * @return
	 */
	Page<MouldProcess> selectPageList(@Param("mouldProcess") MouldProcess mouldProcess,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param mouldProcessId
	 * @return
	 */
	MouldProcess selectById(@Param("mouldProcessId") Long mouldProcessId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldProcessId
	 * @return
	 */
	Integer deleteById(@Param("mouldProcess") MouldProcess mouldProcess);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param mouldProcessId
	 * @return
	 */
	Integer removeById(@Param("mouldProcessId") Long mouldProcessId);
	
	
	/**
	 * 根据主键查询
	 * 
	 * @param mouldProcessId
	 * @return
	 */
	MouldProcess selectSeqByMouldId(@Param("mouldId") Long mouldId);
	
	/**
	 * 查询列表
	 * 
	 * @param mouldIdList
	 * @return
	 */
	List<MouldProcess> selectByMouldIdList(@Param("mouldIdList") List<Long> mouldIdList);
	
	/**
	 * 根据主键查询
	 * 
	 * @param fullNumber
	 * @return
	 */
	Integer selectSumProcessAmount(@Param("fullNumber") String fullNumber);
	
}
