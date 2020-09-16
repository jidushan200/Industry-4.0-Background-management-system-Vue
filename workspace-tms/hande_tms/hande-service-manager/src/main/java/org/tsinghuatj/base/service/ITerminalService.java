package org.tsinghuatj.base.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.base.domain.Terminal;

/**
 * @ClassName: ITerminalService
 * @Description: Terminal服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface ITerminalService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param terminal
	 * @return
	 */
	Integer insert(Long userId, Terminal terminal) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param terminal
	 * @param terminalId
	 * @return
	 */
	Integer updateActiveById(Long userId, Terminal terminal, Long terminalId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param terminal
	 * @return
	 */
	List<Terminal> select(Long userId, Terminal terminal) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param terminal
	 * @return
	 */
	Pagination<Terminal> selectPageList(Long userId, Terminal terminal,QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param terminalId
	 * @return
	 */
	Terminal selectById(Long userId, Long terminalId) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param terminalId
	 * @return
	 */
	Integer deleteById(Long userId, Long terminalId) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param terminalId
	 * @return
	 */
	Integer removeById(Long userId, Long terminalId) throws BusinessException;
	
	/**
	 * 终端信息表导入
	 */
	Integer terminalImport(Long userId, List<Terminal> terminalList) throws BusinessException;
}
