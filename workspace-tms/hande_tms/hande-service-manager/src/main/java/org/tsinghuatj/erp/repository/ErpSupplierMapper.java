package org.tsinghuatj.erp.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.erp.domain.ErpSupplier;

@Mapper
@Repository
public interface ErpSupplierMapper {

	ErpSupplier selectBySupplierCode(@Param("supplierCode") String supplierCode);
}
