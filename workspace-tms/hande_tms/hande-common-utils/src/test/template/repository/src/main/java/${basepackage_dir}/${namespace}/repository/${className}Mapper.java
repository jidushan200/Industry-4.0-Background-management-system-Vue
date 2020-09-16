package ${basepackage}.${namespace}.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import ${basepackage}.${namespace}.domain.${table.className};

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface ${table.className}Mapper {
	
	/**
	 * 插入数据
	 * 
	 * @param ${table.classNameFirstLower}
	 * @return
	 */
	Integer insert(@Param("${table.classNameFirstLower}") ${table.className} ${table.classNameFirstLower});
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param ${table.classNameFirstLower}
	 * @param ${table.classNameFirstLower}Id
	 * @return
	 */
	Integer updateActiveById(@Param("${table.classNameFirstLower}") ${table.className} ${table.classNameFirstLower}, @Param("${table.classNameFirstLower}Id") ${table.pkColumn.javaType} ${table.classNameFirstLower}Id);

	/**
	 * 查询列表
	 * 
	 * @param ${table.classNameFirstLower}
	 * @return
	 */
	List<${table.className}> select(@Param("${table.classNameFirstLower}") ${table.className} ${table.classNameFirstLower});
	
	/**
	 * 分页查询列表
	 * 
	 * @param ${table.classNameFirstLower}
	 * @return
	 */
	Page<${table.className}> selectPageList(@Param("${table.classNameFirstLower}") ${table.className} ${table.classNameFirstLower},@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param ${table.classNameFirstLower}Id
	 * @return
	 */
	${table.className} selectById(@Param("${table.classNameFirstLower}Id") ${table.pkColumn.javaType} ${table.classNameFirstLower}Id);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param ${table.classNameFirstLower}Id
	 * @return
	 */
	Integer deleteById(@Param("${table.classNameFirstLower}") ${table.className} ${table.classNameFirstLower});
	
	/**
	 * 按主键物理删除
	 * 
	 * @param ${table.classNameFirstLower}Id
	 * @return
	 */
	Integer removeById(@Param("${table.classNameFirstLower}Id") ${table.pkColumn.javaType} ${table.classNameFirstLower}Id);
}
