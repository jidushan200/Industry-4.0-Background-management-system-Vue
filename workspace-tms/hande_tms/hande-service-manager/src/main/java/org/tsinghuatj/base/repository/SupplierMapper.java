package org.tsinghuatj.base.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.framework.domain.QueryDto;

import org.tsinghuatj.base.domain.Supplier;

import com.github.pagehelper.Page;

@Mapper
@Repository
public interface SupplierMapper {
	
	/**
	 * 插入数据
	 * 
	 * @param supplier
	 * @return
	 */
	Integer insert(@Param("supplier") Supplier supplier);
		
	/**
	 * 根据主键更新有值数据
	 * 
	 * @param supplier
	 * @param supplierId
	 * @return
	 */
	Integer updateActiveById(@Param("supplier") Supplier supplier, @Param("supplierId") Long supplierId);

	/**
	 * 查询列表
	 * 
	 * @param supplier
	 * @return
	 */
	List<Supplier> select(@Param("supplier") Supplier supplier);
	
	/**
	 * 分页查询列表
	 * 
	 * @param supplier
	 * @return
	 */
	Page<Supplier> selectPageList(@Param("supplier") Supplier supplier,@Param("queryDto") QueryDto queryDto);

	/**
	 * 根据主键查询
	 * 
	 * @param supplierId
	 * @return
	 */
	Supplier selectById(@Param("supplierId") Long supplierId);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param supplierId
	 * @return
	 */
	Integer deleteById(@Param("supplier") Supplier supplier);
	
	/**
	 * 按主键物理删除
	 * 
	 * @param supplierId
	 * @return
	 */
	Integer removeById(@Param("supplierId") Long supplierId);
	
	
	/**
	 * 根据主键查询
	 * 
	 * @param supplierCode
	 * @return
	 */
	Supplier selectBySupplierCode(@Param("supplierCode") String supplierCode,@Param("pkId") Long pkId);
	
	/**
	 * 查询列表
	 * 
	 * @param supplier
	 * @return
	 */
	List<Supplier> selectCodeList(@Param("codeList") List<String> codeList);
}
