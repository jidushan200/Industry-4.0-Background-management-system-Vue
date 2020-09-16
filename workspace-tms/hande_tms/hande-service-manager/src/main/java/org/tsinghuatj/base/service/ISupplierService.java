package org.tsinghuatj.base.service;

import java.util.List;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.base.domain.Supplier;


/**
 * @ClassName: ISupplierService
 * @Description: Supplier服务接口
 * @date 2017年10月23日 17:26:51
 * 
 */
public interface ISupplierService {
	/**
	 * 插入数据
	 * 
	 * @param userId
	 * @param supplier
	 * @return
	 */
	Integer insert(Long userId, Supplier supplier) throws BusinessException;

	/**
	 * 根据主键更新有值数据
	 * 
	 * @param userId
	 * @param supplier
	 * @param supplierId
	 * @return
	 */
	Integer updateActiveById(Long userId, Supplier supplier, Long supplierId) throws BusinessException;

	/**
	 * 查询列表
	 * 
	 * @param userId
	 * @param supplier
	 * @return
	 */
	List<Supplier> select(Long userId, Supplier supplier) throws BusinessException;

	/**
	 * 分页查询列表
	 * 
	 * @param userId
	 * @param supplier
	 * @return
	 */
	Pagination<Supplier> selectPageList(Long userId, Supplier supplier, QueryDto queryDto) throws BusinessException;

	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param supplierId
	 * @return
	 */
	Supplier selectById(Long userId, Long supplierId) throws BusinessException;

	/**
	 * 按主键逻辑删除
	 * 
	 * @param userId
	 * @param supplierId
	 * @return
	 */
	Integer deleteById(Long userId, Long supplierId) throws BusinessException;

	/**
	 * 按主键物理删除
	 * 
	 * @param userId
	 * @param supplierId
	 * @return
	 */
	Integer removeById(Long userId, Long supplierId) throws BusinessException;

	/**
	 * 供应商导入
	 * 
	 * @param userId
	 * @param supplierList
	 * @return
	 * @throws BusinessException
	 */
	Integer supplierImport(Long userId, List<Supplier> supplierList) throws BusinessException;
	
	/**
	 * 供应商信息同步
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	Integer supplierSynchro(Long userId,String supplierCode)throws BusinessException;
	
	
	/**
	 * 根据主键查询
	 * 
	 * @param userId
	 * @param supplierId
	 * @return
	 */
	Supplier selectByCode(Long userId, String supplierCode) throws BusinessException;
	
	boolean checkSupplierCode(String supplierCode ,Long pkId)throws BusinessException;

}
