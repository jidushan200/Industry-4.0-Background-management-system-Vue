package org.tsinghuatj.erp.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.erp.domain.ErpEquipment;

@Mapper
@Repository
public interface ErpEquipmentMapper {

	ErpEquipment selectByEquipmentCode(@Param("equipmentCode") String equipmentCode);
}
