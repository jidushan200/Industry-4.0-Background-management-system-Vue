package org.tsinghuatj.base.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.base.domain.Terminal;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface TerminalMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param terminal
	 * @return
	 */
	Integer insert(@Param("terminal") Terminal terminal);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param terminal
	 * @param terminalId
	 * @return
	 */
	Integer updateActiveById(@Param("terminal") Terminal terminal, @Param("terminalId") Long terminalId);

	/**
	 * 查询列表
	 * 
	 * @param terminal
	 * @return
	 */
	List<Terminal> select(@Param("terminal") Terminal terminal);
	
	/**
	 * 分页查询列表
	 * 
	 * @param terminal
	 * @return
	 */
	Page<Terminal> selectPageList(@Param("terminal") Terminal terminal,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param terminalId
	 * @return
	 */
	Terminal selectById(@Param("terminalId") Long terminalId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param terminalId
	 * @return
	 */
	Integer deleteById(@Param("terminal") Terminal terminal);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param terminalId
	 * @return
	 */
	Integer removeById(@Param("terminalId") Long terminalId);
}
