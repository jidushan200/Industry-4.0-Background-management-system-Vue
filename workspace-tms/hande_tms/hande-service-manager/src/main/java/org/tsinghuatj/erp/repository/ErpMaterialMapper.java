package org.tsinghuatj.erp.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.erp.domain.ErpMaterial;

@Mapper
@Repository
public interface ErpMaterialMapper {
	ErpMaterial selectByMaterialNumber(@Param("itemNumber") String itemNumber);
	
	Integer insert(@Param("erpMaterial") ErpMaterial erpMaterial);
}
