package ${basepackage}.${namespace}.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import ${basepackage}.framework.support.BusinessException;
import ${basepackage}.${namespace}.domain.${table.className};

/**
 * @ClassName: I${table.className}Service
 * @Description: ${table.className}服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface I${table.className}Service {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param ${table.classNameFirstLower}
	 * @return
	 */
	Integer insert(Long userId, ${table.className} ${table.classNameFirstLower}) throws BusinessException;
	
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param ${table.classNameFirstLower}
	 * @param ${table.classNameFirstLower}Id
	 * @return
	 */
	Integer updateActiveById(Long userId, ${table.className} ${table.classNameFirstLower}, ${table.pkColumn.javaType} ${table.classNameFirstLower}Id) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param ${table.classNameFirstLower}
	 * @return
	 */
	List<${table.className}> select(Long userId, ${table.className} ${table.classNameFirstLower}) throws BusinessException;
	
	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param ${table.classNameFirstLower}
	 * @return
	 */
	Pagination<${table.className}> selectPageList(Long userId, ${table.className} ${table.classNameFirstLower},QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param ${table.classNameFirstLower}Id
	 * @return
	 */
	${table.className} selectById(Long userId, ${table.pkColumn.javaType} ${table.classNameFirstLower}Id) throws BusinessException;

	
	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param ${table.classNameFirstLower}Id
	 * @return
	 */
	Integer deleteById(Long userId, ${table.pkColumn.javaType} ${table.classNameFirstLower}Id) throws BusinessException;
	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param ${table.classNameFirstLower}Id
	 * @return
	 */
	Integer removeById(Long userId, ${table.pkColumn.javaType} ${table.classNameFirstLower}Id) throws BusinessException;
}
